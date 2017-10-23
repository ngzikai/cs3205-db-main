package restapi.team3;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import utils.db.*;

import java.sql.*;
import utils.db.*;
import utils.*;
import java.util.Base64;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

// Challenge Response Implementation imports
// import java.util.Base64;
// import java.util.Arrays;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import utils.team3.*;

import org.json.JSONObject;

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
    if(Challenge.storeChallenge(challenge, username) >= 1){
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
  public Response login(@HeaderParam("X-NFC-Token")String nfcToken, @HeaderParam("Authorization")String authorizationHeader){
    // check number of login attempts
    // read challenge token
    String[] authHeader = authorizationHeader.split(" ");
    if(authHeader.length < 2){
      return Response.status(Response.Status.BAD_REQUEST)
                     .entity("Invalid Authorization Header.").build();
    }
    if (nfcToken == null) {
      return Response.status(Response.Status.BAD_REQUEST)
                     .entity("Missing NFC Token Header.").build();
    }
    byte[] response = Base64.getDecoder().decode(authHeader[1].getBytes());
    byte[] challenge = Challenge.getUserChallenge(username);
    byte[] passwordHash = Base64.getDecoder().decode(CommonUtil.getUserPasswordHash(username).getBytes());
    if(Challenge.validateResponse(response, challenge, passwordHash)){
      // remove challenge
      // read NFC token
      String userNFC = CommonUtil.getUserNFC(username);
      String totp = CommonUtil.generateTOTP(userNFC);
      System.out.println("totp:"+totp);
      if(totp.equals(nfcToken)){
        return Response.status(201).entity("true").build();
      }
    }

    // increase lock attempt
    return Response.status(400).entity("Invalid credentials").build();
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
    attribute = "nfcid";
    Response response = setAttribute(nfcID);
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
