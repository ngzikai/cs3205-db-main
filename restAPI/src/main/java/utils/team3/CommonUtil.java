package utils.team3;

import java.sql.*;
import java.util.*;
import utils.db.MySQLAccess;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class CommonUtil{
  public static String getUserPasswordHash(String username){
    String sql = "SELECT * FROM CS3205.user WHERE username = ?";
      try{
          PreparedStatement ps = MySQLAccess.connectDatabase().prepareStatement(sql);
          ps.setString(1, username);
          ResultSet rs = ps.executeQuery();
          while(rs.next()){
              return rs.getString("password2");
          }
      }catch(Exception e){
          e.printStackTrace();
      }
    return "";
  }

  public static String getUserNFC(String username){
    String sql = "SELECT * FROM CS3205.user WHERE username = ?";
      try{
          PreparedStatement ps = MySQLAccess.connectDatabase().prepareStatement(sql);
          ps.setString(1, username);
          ResultSet rs = ps.executeQuery();
          while(rs.next()){
              return rs.getString("nfcid");
          }
      }catch(Exception e){
          e.printStackTrace();
      }
    return "";
  }

  public static String getUserID(String username){
    String sql = "SELECT * FROM CS3205.user WHERE username = ?";
      try{
          PreparedStatement ps = MySQLAccess.connectDatabase().prepareStatement(sql);
          ps.setString(1, username);
          ResultSet rs = ps.executeQuery();
          while(rs.next()){
              return rs.getString("uid");
          }
      }catch(Exception e){
          e.printStackTrace();
      }
    return "";
  }

  public static String getUserSalt(String username){
    String sql = "SELECT * FROM CS3205.user WHERE username = ?";
    try{
      PreparedStatement ps = MySQLAccess.connectDatabase().prepareStatement(sql);
      ps.setString(1, username);
      ResultSet rs = ps.executeQuery();
      while(rs.next()){
        return rs.getString("salt2");
      }
      ps.close();
    }catch(Exception e){
      e.printStackTrace();
    }
    return "";
  }

  public static String generateTOTP(String nfcToken) {
    try{
      Mac hmacSHA256 = Mac.getInstance("HmacSHA256");
      long timeCounter = System.currentTimeMillis() / 30000;
      hmacSHA256.init(new SecretKeySpec(nfcToken.getBytes(), "SHA-256"));
      return Base64.getEncoder().encodeToString(hmacSHA256.doFinal(String.valueOf(timeCounter).getBytes()));
    }catch(Exception e){
      e.printStackTrace();
    }
    return "";
  }

  public static int getLockAttempts(String username){
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
            return rs.getInt("lockAttempts");
          }
        }
    }catch(Exception e){
        e.printStackTrace();
    }
    return 0;
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

  public static long getLastAttemptTime(String username){
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
            return rs.getLong("lastAttempt");
          }
        }
    }catch(Exception e){
        e.printStackTrace();
    }
    return -1l;
  }

  public static byte[] computeXOR(byte[] b1, byte[] b2) {
    byte[] result = new byte[32];
    for (int i = 0; i < 32; i++) {
        result[i] = (byte)(b1[i] ^ b2[i]);
    }
    return result;
  }
}
