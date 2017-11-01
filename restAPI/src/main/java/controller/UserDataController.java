package controller;

import entity.UserChallenge;
import entity.UserMetaData;

import java.sql.*;
import utils.db.MySQLAccess;

public class UserDataController extends UserController{

  public UserDataController(){

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
}
