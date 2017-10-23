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

public class UserService {

  private String username;
  private String attribute;
  private String userID;
  public UserService(){

  }
  public UserService(String username, String attribute){
    this.username = username;
    this.attribute = attribute;
    //this.userID = userID;
  }

  @POST
  @Path("/login")
  public Response login(){
    return null;
    // if (request.username != null && authorizationHeader == null) {
    //     ObjectMapper mapper = new ObjectMapper();
    //     ObjectNode jObj = mapper.createObjectNode();
    //     // Generate challenge
    //     byte[] challenge = Challenge.generateChallenge();
    //     // Store challenge to server 3 local database
    //     if(storeChallenge(challenge, request.username) >= 1){
    //       // Put challenge to jObj
    //       jObj.put("challenge", Base64.getEncoder().encodeToString(challenge));
    //       // Obtain salt from server 4
    //       jObj.put("salt", getUserSalt(request.username));
    //       try{
    //         return Response.status(401)
    //                 .header("www-authenticate", mapper.writeValueAsString(jObj))
    //                 .entity("Missing Authorization Header.").build();
    //       } catch(Exception e){
    //         e.printStackTrace();
    //       }
    //     }
    //     return Response.status(500).entity("Server unable to generate challenge.").build();
    // }
    // if (request.username != null && authorizationHeader != null) {
    //   if (nfcToken == null) {
    //       throw new WebException(Response.Status.BAD_REQUEST, "Missing X-NFC-Token header");
    //   }
    //   System.out.println(authorizationHeader+" authorizationHeader:");
    //   String[] authHeader = authorizationHeader.split(" ");
    //   if(authHeader.length < 2){
    //     throw new WebException(Response.Status.BAD_REQUEST, "Invalid Authorization Header.");
    //   }
    //   byte[] response = Base64.getDecoder().decode(authHeader[1].getBytes());
    //   byte[] challenge = getUserChallenge(request.username);
    //   // get from server 4
    //   byte[] passwordHash = Base64.getDecoder().decode(getUserPasswordHash(request.username).getBytes());
    //   if(Challenge.validateResponse(response, challenge, passwordHash)){
    //     try {
    //         request.userId = 1;
    //         final String jwt = TokenUtils.createJWT(request);
    //         return Response.ok(new PasswordGrant(jwt)).build();
    //     } catch (JsonProcessingException | InvalidKeyException e) {
    //         throw new WebException(e);
    //     }
    //   }
    // }
    // throw new WebException(Response.Status.UNAUTHORIZED, "Invalid credential");
  }

  @GET
  public Response getAttribute(){
    String sql = "SELECT * FROM CS3205.user WHERE username = ?";
    try{
      PreparedStatement ps = MySQLAccess.connectDatabase().prepareStatement(sql);
      ps.setString(1, username);
      ResultSet rs = ps.executeQuery();
      while(rs.next()){
        String attr = rs.getString(attribute);
        if(attr != null){
          return Response.status(201).entity(attr).build();
        }
      }
    }catch(Exception e){
      e.printStackTrace();
      return Response.status(500).entity("Server internal error, probably failed SQL").build();
    }
    return Response.status(401).entity("Bad request, probably unknown attribute").build();
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
    String sql = "UPDATE CS3205.user SET " + attribute + " = ? WHERE username = ?";
    try{
      PreparedStatement ps = MySQLAccess.connectDatabase().prepareStatement(sql);
      ps.setString(1, value);
      ps.setString(2, username);
      int result = ps.executeUpdate();
      System.out.println("result: "+result);
      if(result == 1){
        return Response.status(201).entity("successfully added "+attribute + " to "+username).build();
      }
    }catch(Exception e){
      e.printStackTrace();
      return Response.status(500).entity("Server internal error, probably failed SQL.").build();
    }
    return Response.status(401).entity("Probably unknown attribute. Check attribute and body").build();
  }

//   private int storeChallenge(byte[] challenge, String username){
//   String sql = "SELECT uid FROM CS3205.user WHERE username = ?";
//   int result = 0;
//     try{
//         // Maybe need to get from server 4
//         PreparedStatement ps =  MySQLAccess.connectDatabase().prepareStatement(sql);
//         ps.setString(1, username);
//         ResultSet rs = ps.executeQuery();
//         while(rs.next()){
//           int userID = rs.getInt("uid");
//           sql = "INSERT INTO CS3205.challenge (challengeString, userID) VALUES (?, ?) ON DUPLICATE KEY UPDATE challengeString=VALUES(challengeString)";
//           ps = MySQLAccess.connectDatabase().prepareStatement(sql);
//           System.out.println("String: "+Base64.getEncoder().encodeToString(challenge) + "   "+Arrays.toString(challenge));
//           ps.setString(1, Base64.getEncoder().encodeToString(challenge));
//           ps.setInt(2, userID);
//           result = ps.executeUpdate();
//           break;
//         }
//     }catch(Exception e){
//         e.printStackTrace();
//     }
//     return result;
// }
//
//   private byte[] getUserChallenge(String username){
//     String sql = "SELECT * FROM CS3205.user WHERE username = ?";
//       try{
//           PreparedStatement ps = DB.connectDatabase().prepareStatement(sql);
//           ps.setString(1, username);
//           ResultSet rs = ps.executeQuery();
//           while(rs.next()){
//             int userID = rs.getInt("uid");
//             sql = "SELECT * FROM CS3205.challenge WHERE userID = ?";
//             ps = DB.connectDatabase().prepareStatement(sql);
//             ps.setInt(1, userID);
//             rs = ps.executeQuery();
//             while(rs.next()){
//               return Base64.getDecoder().decode(rs.getString("challengeString").getBytes());
//             }
//           }
//       }catch(Exception e){
//           e.printStackTrace();
//       }
//       return null;
//   }
//
//   private static final String RESOURCE_SERVER_SESSION_PATH = "http://cs3205-4-i.comp.nus.edu.sg/api/team3";
//
//   private String getUserSalt(String username){
//     final String target;
//         target = String.format("%s/%s?username=%s&attribute=%s", RESOURCE_SERVER_SESSION_PATH,
//                 "user", username, "salt2");
//     final Invocation.Builder client = ClientBuilder.newClient().target(target).request();
//     System.out.println("POST " + target);
//     // TODO Add in the headers for server 4 verification in the future
//     final Response response = client.get();
//     String rawResponse = response.readEntity(String.class);
//     System.out.println(rawResponse);
//     return rawResponse;
//   }
//
//   private String getUserPasswordHash(String username){
//     final String target;
//         target = String.format("%s/%s?username=%s&attribute=%s", RESOURCE_SERVER_SESSION_PATH,
//                 "user", username, "password2");
//     final Invocation.Builder client = ClientBuilder.newClient().target(target).request();
//     System.out.println("POST " + target);
//     // TODO Add in the headers for server 4 verification in the future
//     final Response response = client.get();
//     String rawResponse = response.readEntity(String.class);
//     System.out.println(rawResponse);
//     return rawResponse;
//   }
}
