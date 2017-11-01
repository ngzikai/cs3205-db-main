package controller;

// Java imports
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Base64;
import javax.ws.rs.core.Response;
import org.json.JSONObject;

// Data objects
import entity.UserChallenge;
import entity.UserMetaData;

// Utils imports
import utils.db.MySQLAccess;
import utils.team3.*;

public class UserDataController extends UserController{

  JSONObject user = null;

  public UserDataController(JSONObject user){
    this.user = user;
  }

  public UserMetaData getMetaData(String username){
    UserMetaData metaData = null;

    String sql = "SELECT * FROM CS3205.user WHERE username = ?";
    try{
        PreparedStatement ps = MySQLAccess.connectDatabase().prepareStatement(sql);
        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
          int userID = rs.getInt("uid");
          sql = "SELECT * FROM CS3205.user_metadata WHERE uid = ?";
          ps = MySQLAccess.connectDatabase().prepareStatement(sql);
          ps.setInt(1, userID);
          rs = ps.executeQuery();
          while(rs.next()){
            metaData = new UserMetaData(rs.getInt("uid"), rs.getInt("lockAttempts"), rs.getLong("lastAttempt"));
          }
        }
    }catch(Exception e){
        e.printStackTrace();
    }
    MySQLAccess.close();
    return metaData;
  }

  public UserChallenge getChallengeData(String username, String type){
    UserChallenge challengeData = null;
    String sql = "SELECT * FROM CS3205.user WHERE username = ?";
      try{
          PreparedStatement ps = MySQLAccess.connectDatabase().prepareStatement(sql);
          ps.setString(1, username);
          ResultSet rs = ps.executeQuery();
          System.out.println("username:"+username);
          while(rs.next()){
            int userID = rs.getInt("uid");
            System.out.println("uid:"+userID);
            sql = "SELECT * FROM CS3205.user_challenge WHERE uid = ? AND type = ?";
            ps = MySQLAccess.connectDatabase().prepareStatement(sql);
            ps.setInt(1, userID);
            ps.setString(2, type);
            rs = ps.executeQuery();
            while(rs.next()){
              System.out.println(rs.getString("challengeString"));
              challengeData = new UserChallenge(rs.getInt("uid"), rs.getString("type"), rs.getString("challengeString"));
            }
          }
      }catch(Exception e){
          e.printStackTrace();
      }
    MySQLAccess.close();
    return challengeData;
  }

  public static int setLockAttempt(String username, int lockAttempts, long lastAttempt){
    String sql = "SELECT * FROM CS3205.user WHERE username = ?";
    try{
        PreparedStatement ps = MySQLAccess.connectDatabase().prepareStatement(sql);
        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
          int userID = rs.getInt("uid");
          sql = "INSERT INTO CS3205.user_metadata (uid, lockAttempts, lastAttempt) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE lockAttempts=VALUES(lockAttempts), lastAttempt=VALUES(lastAttempt)";
          ps = MySQLAccess.connectDatabase().prepareStatement(sql);
          ps.setInt(1, userID);
          ps.setInt(2, lockAttempts);
          ps.setLong(3, lastAttempt);
          return ps.executeUpdate();
        }
    }catch(Exception e){
        e.printStackTrace();
    }
    return -1;
  }

  // Store NFC Secret for the user
  public int setNFCID(String value){
    String sql = "UPDATE CS3205.user SET nfcid = ? WHERE username = ?";
    int result = 0;
    try{
      PreparedStatement ps = MySQLAccess.connectDatabase().prepareStatement(sql);
      ps.setString(1, value);
      ps.setString(2, user.getString("username"));
      result = ps.executeUpdate();
    }catch(Exception e){
      e.printStackTrace();
    }
    return result;
  }


  public Response issueChallenge(String type){
    // generate challenge token
    byte[] challenge = Challenge.generateChallenge();
    // store challenge token
    if(Challenge.storeChallenge(challenge, user.getString("username"), type) >= 1){
      try{
        return Response.status(201)
               .entity(Base64.getEncoder().encodeToString(challenge))
               .build();
      } catch(Exception e){
        e.printStackTrace();
        return Response.status(500).entity("Server unable to generate challenge.").build();
      }
    }
    return Response.status(401).entity("Server unable to generate challenge for unknown user.").build();
  }

  public int deleteUserChallenge(String username, String type){
    return Challenge.deleteUserChallenge(username, type);
  }

  public boolean validateResponse(byte[] response, byte[] challenge, byte[] passwordHash){
    return Challenge.validateResponse(response, challenge, passwordHash);
  }

  public boolean validateNFCResponse(byte[] nfcTokenByte, byte[] challenge, byte[] nfcHash){
    return Challenge.validateNFCResponse(nfcTokenByte, challenge, nfcHash);
  }
}
