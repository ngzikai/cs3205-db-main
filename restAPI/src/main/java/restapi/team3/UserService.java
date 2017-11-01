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

import controller.UserDataController;
import entity.UserChallenge;
import entity.UserMetaData;

public class UserService {

  private JSONObject user;
  private String attribute;
  private UserDataController udc;

  public UserService(){
    udc = new UserDataController();
  }

  public UserService(JSONObject user){
    this.user = user;
    udc = new UserDataController();
  }

  @GET
  @Path("/challenge")
  public Response challenge(){
    return issueChallenge("login");
  }

  @GET
  @Path("/nfcchallenge")
  public Response nfcChallenge(){
    return issueChallenge("nfc");
  }

  private Response issueChallenge(String type){
    // generate challenge token
    byte[] challenge = Challenge.generateChallenge();
    // store challenge token
    if(Challenge.storeChallenge(challenge, user.getString("username"), type) >= 1){
      try{
        return Response.status(201)
               .entity(Base64.getEncoder()
               .encodeToString(challenge)).build();
      } catch(Exception e){
        e.printStackTrace();
        return Response.status(500).entity("Server unable to generate challenge.").build();
      }
    }
    return Response.status(401).entity("Server unable to generate challenge for unknown user.").build();
  }

  @POST
  @Path("/login")
  public Response login(@HeaderParam("X-NFC-Response")String nfcToken, @HeaderParam("X-Password-Response")String authorizationHeader, @HeaderParam("debug")boolean debugMode){

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
    int lockAttempts = CommonUtil.getLockAttempts(user.getString("username"));
    long lastAttempt = CommonUtil.getLastAttemptTime(user.getString("username"));
    long currentTimeMillis = System.currentTimeMillis();
    long totalWaitingTime = ((long)(Math.pow(2, lockAttempts)*1000)+lastAttempt);
    long timeDifference = totalWaitingTime - currentTimeMillis;
    if ( timeDifference > 0){
      return Response.status(Response.Status.BAD_REQUEST)
                     .entity("LoginTimeout:"+ (timeDifference/1000))
                     .header("X-Timeout", (timeDifference/1000))
                     .build();
    }
    // if failed attempts are more than 10
    if (lockAttempts > 10){
      return Response.status(Response.Status.BAD_REQUEST)
                     .entity("Your account has been locked out due to multiple failed login attempts. Please contact the administrator.").build();
    }
    byte[] response = Base64.getDecoder().decode(authHeader[1].getBytes());
    UserChallenge uc = udc.getChallengeData(user.getString("username"), "login");
    if (uc == null){
      return Response.status(401).entity("No challenge found for user.").build();
    }
    byte[] challenge = Base64.getDecoder().decode(uc.getChallengeString().getBytes());
    byte[] passwordHash = Base64.getDecoder().decode(user.getString("password2").getBytes());
    System.out.println("Response: "+ Base64.getEncoder().encodeToString(response));
    System.out.println("Challenge: " );
    System.out.println(" "+ Base64.getEncoder().encodeToString(challenge));
    System.out.println("PasswordHash: "+ Base64.getEncoder().encodeToString(passwordHash));
    if(debugMode || response.length == 32 && Challenge.validateResponse(response, challenge, passwordHash)){
      // remove challenge
      Challenge.deleteUserChallenge(user.getString("username"), "login");
      return validateNFCResponse(nfcToken);
    }

    // increase lock attempt
    lockAttempts++;
    CommonUtil.setLockAttempt(user.getString("username"), lockAttempts, System.currentTimeMillis());
    return Response.status(401).entity("Invalid credentials").build();
  }

  @POST
  @Path("/validatenfc")
  public Response validateNFCResponse(@HeaderParam("X-NFC-Response")String nfcToken){
    UserChallenge uc = udc.getChallengeData(user.getString("username"), "nfc");
    if (uc == null){
      return Response.status(401).entity("No NFC challenge found for user.").build();
    }
    byte[] challenge = Base64.getDecoder().decode(uc.getChallengeString().getBytes());
    System.out.println(Base64.getEncoder().encodeToString(challenge));
    byte[] nfcTokenByte = Base64.getDecoder().decode(nfcToken.getBytes());
    System.out.println("NFCTOKEN: "+nfcToken);
    byte[] nfcHash = Base64.getDecoder().decode(user.getString("nfcid").getBytes());
    System.out.println(user.getString("nfcid"));

    if(Challenge.validateNFCResponse(nfcTokenByte, challenge, nfcHash)){
      Challenge.deleteUserChallenge(user.getString("username"), "nfc");
      return Response.status(200).entity(user.getInt("uid")).build();
    }
    return Response.status(401).entity("Invalid NFC Response.").build();
  }

  @GET
  public Response getSalt(){
    String salt = user.getString("salt2");
    if(salt.isEmpty()){
      return Response.status(401).entity("No salt for the user.").build();
    }
    return Response.status(200).entity(salt).build();
  }

  @GET
  @Path("/passwordandsalt")
  public Response manuallySet(){
    String salt = GUID.BASE58(), password = "";
    Response response = Response.status(401).entity("Setingg password and salt failed.").build();
    try{
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] bytes = digest.digest((password+salt).getBytes());
      password = Base64.getEncoder().encodeToString(bytes);
    }catch(Exception e){
      e.printStackTrace();
      return Response.status(500).entity("Internal Server Error while generating hash.").build();
    }
    JSONObject result = udc.updateUserPassword2(user.getString("username"), password, salt);
    user.put("password2", password);
    user.put("salt2", salt);
    if(result.getInt("result") == 1){
      response = Response.status(200).entity(user.getString("password2")).build();
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
      Cryptography crypto = Cryptography.getInstance();
      byte[] cText = crypto.encrypt(Base64.getEncoder().encodeToString(secret).getBytes());
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
      ps.setString(2, user.getString("username"));
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
