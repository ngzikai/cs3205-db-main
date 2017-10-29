package restapi.team3;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import utils.db.*;

import java.sql.*;
import utils.db.*;
import utils.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

// Challenge Response Implementation imports
import java.util.Base64;
import java.util.Arrays;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import utils.team3.*;

import org.json.JSONObject;

import java.security.SecureRandom;

public class UserService {

  private String username;
  private String attribute;
  private String userID;
  public UserService(){

  }
  public UserService(String username, String attribute){
    this.username = username;
    this.attribute = attribute;
  }

  @GET
  @Path("/challenge")
  public Response challenge(){
    // generate challenge token
    byte[] challenge = Challenge.generateChallenge();
    // store challenge token
    if(Challenge.storeChallenge(challenge, username, "login") >= 1){
      try{
        return Response.status(201)
                .entity(Base64.getEncoder().encodeToString(challenge)).build();
      } catch(Exception e){
        e.printStackTrace();
        return Response.status(500).entity("Server unable to generate challenge.").build();
      }
    }
    return Response.status(401).entity("Server unable to generate challenge for unknown user.").build();
  }

  @GET
  @Path("/nfcchallenge")
  public Response nfcChallenge(){
    // generate challenge token
    byte[] challenge = Challenge.generateChallenge();
    // store challenge token
    if(Challenge.storeChallenge(challenge, username, "nfc") >= 1){
      try{
        return Response.status(201)
                .entity(Base64.getEncoder().encodeToString(challenge)).build();
      } catch(Exception e){
        e.printStackTrace();
        return Response.status(500).entity("Server unable to generate challenge.").build();
      }
    }
    return Response.status(401).entity("Server unable to generate challenge for unknown user.").build();
  }

  @POST
  @Path("/login")
  public Response login(@HeaderParam("X-NFC-Response")String nfcToken, @HeaderParam("Authorization")String authorizationHeader, @HeaderParam("debug")boolean debugMode){

    if (authorizationHeader == null) {
      return Response.status(Response.Status.BAD_REQUEST)
                     .entity("Missing Authorization Header.").build();
    }
    // read challenge token
    String[] authHeader = authorizationHeader.split(" ");
    if(authHeader.length < 2){
      return Response.status(Response.Status.BAD_REQUEST)
                     .entity("Invalid Authorization Header.").build();
    }
    if (nfcToken == null) {
      return Response.status(Response.Status.BAD_REQUEST)
                     .entity("Missing NFC Response Header.").build();
    }
    // check number of login attempts
    int lockAttempts = CommonUtil.getLockAttempts(username);
    long lastAttempt = CommonUtil.getLastAttemptTime(username);
    long currentTimeMillis = System.currentTimeMillis();
    long totalWaitingTime = ((long)(Math.pow(2, lockAttempts)*1000)+lastAttempt);
    long timeDifference = totalWaitingTime - currentTimeMillis;
    if ( timeDifference > 0){
      return Response.status(Response.Status.BAD_REQUEST)
                     .entity("You still have to wait for "+ (timeDifference/1000) +" seconds before you can login again.").build();
    }
    if (lockAttempts > 10){
      return Response.status(Response.Status.BAD_REQUEST)
                     .entity("Your account has been locked out due to multiple failed login attempts. Please contact the administrator.").build();
    }
    byte[] response = Base64.getDecoder().decode(authHeader[1].getBytes());
    byte[] challenge = Challenge.getUserChallenge(username, "login");
    byte[] passwordHash = Base64.getDecoder().decode(CommonUtil.getUserPasswordHash(username).getBytes());
    if(debugMode || response.length == 32 && Challenge.validateResponse(response, challenge, passwordHash)){
      // remove challenge
      Challenge.deleteUserChallenge(username, "login");
      return validateNFCResponse(nfcToken);
    }

    // increase lock attempt
    lockAttempts++;
    CommonUtil.setLockAttempt(username, lockAttempts, System.currentTimeMillis());
    return Response.status(401).entity("Invalid credentials").build();
  }

  @POST
  @Path("/validatenfc")
  public Response validateNFCResponse(@HeaderParam("X-NFC-Response")String nfcToken){
    byte[] challenge = Challenge.getUserChallenge(username, "nfc");
    byte[] nfcTokenByte = Base64.getDecoder().decode(nfcToken.getBytes());
    byte[] nfcHash = Base64.getDecoder().decode(CommonUtil.getUserNFC(username).getBytes());
    if(Challenge.validateNFCResponse(nfcTokenByte, challenge, nfcHash)){
      Challenge.deleteUserChallenge(username, "nfc");
      return Response.status(200).entity(CommonUtil.getUserID(username)).build();
    }
    return Response.status(401).entity("Invalid NFC Response.").build();
  }

  @GET
  public Response getSalt(){
    String salt = CommonUtil.getUserSalt(username);
    if(salt.isEmpty()){
      return Response.status(401).entity("No salt for the user.").build();
    }
    return Response.status(200).entity(salt).build();
  }

  @GET
  @Path("/calculateHash")
  public Response calculateHash(@QueryParam("hash")String hash){
    try{
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] bytes = digest.digest(hash.getBytes());
      return Response.status(500).entity(Base64.getEncoder().encodeToString(bytes)).build();
    }catch(Exception e){
      e.printStackTrace();
    }
    return Response.status(500).entity("Internal Server Error.").build();
  }

  @GET
  @Path("/passwordandsalt")
  public Response manuallySet(){
    String salt = GUID.BASE58();
    attribute = "salt2";
    Response response = setAttribute(salt);
    if(response.getStatus() == 500 ||response.getStatus() == 401){
      return response;
    }
    String password = "password";
    attribute = "password2";
    try{
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] bytes = digest.digest((password+salt).getBytes());
      response = setAttribute(Base64.getEncoder().encodeToString(bytes));
    }catch(Exception e){
      e.printStackTrace();
    }
    return response;
  }

  @GET
  @Path("/populateNFC")
  public Response setNFC(){
    String nfcID = GUID.BASE58();
    byte[] secret = new byte[32];
    new SecureRandom().nextBytes(secret);
    attribute = "nfcid";
    byte[] computedResult = new byte[32];
    try{
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      computedResult = digest.digest(secret);
    }catch(Exception e){
      e.printStackTrace();
      return Response.status(500).entity("Internal Server Error.").build();
    }
    System.out.println(Base64.getEncoder().encodeToString(computedResult)+" comptuedResult");
    Response response = setAttribute(Base64.getEncoder().encodeToString(computedResult));
    if(response.getStatus() == 201){
      // send back the original value to write to card
      Encryption e = new Encryption();
      e.setKey("testing");
      byte[] cText = e.encrypt(Base64.getEncoder().encodeToString(secret).getBytes());
      response = Response.status(201).entity(Base64.getEncoder().encodeToString(cText)).build();
    }
    return response;
  }

  @POST
  public Response setAttribute(String value){
    // WARNING: DANGEROUS WAY OF SETTING PREPARESTATEMENT
    // REMOVE IT
    String sql = "UPDATE CS3205.user SET " + attribute + " = ? WHERE username = ?";
    try{
      PreparedStatement ps = MySQLAccess.connectDatabase().prepareStatement(sql);
      ps.setString(1, value);
      ps.setString(2, username);
      int result = ps.executeUpdate();
      System.out.println("result: "+result);
      if(result == 1){
        return Response.status(201).entity(value).build();
      }
    }catch(Exception e){
      e.printStackTrace();
      return Response.status(500).entity("Server internal error, probably failed SQL.").build();
    }
    return Response.status(401).entity("Probably unknown attribute. Check attribute and body").build();
  }
}
