package utils.team3;

import java.util.Random;
import java.util.Arrays;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.math.BigInteger;
import java.sql.*;
import utils.db.MySQLAccess;
import java.util.*;
public class Challenge{

  public static byte[] generateChallenge(){
    byte[] challenge = new byte[32];
    new Random().nextBytes(challenge);
    return challenge;
  }

  public static byte[] generateExpectedResponse(byte[] challenge){
    // create from array
    BigInteger bigInt = new BigInteger(challenge);
    // shift
    BigInteger shiftInt = bigInt.shiftRight(4);
    // back to array
    byte [] shifted = shiftInt.toByteArray();
    return shifted;
  }

  public static boolean validateResponse(byte[] response, byte[] challenge, byte[] passwordHash){
    try{
      // h(h(h(pwd)) + c) + h(pwd) = response
      //XOR hash of password hash with challenge
      byte[] expectedResult = computeXOR(generateHash(passwordHash), challenge); // h(h(pwd)) + c
      //hash the result
      expectedResult = generateHash(expectedResult); // h(h(h(pwd)) + c)
      //XOR result with challenge response
      expectedResult = computeXOR(expectedResult, response); // h(h(h(pwd)) + c) + h(h(h(pwd)) + c) + h(pwd) = h(pwd)
      //hash the result
      expectedResult = generateHash(expectedResult); // h(h(pwd))

      return Arrays.equals(expectedResult, generateHash(passwordHash));
    } catch(Exception e){
      e.printStackTrace();
    }
    return false;
  }

  private static byte[] computeXOR(byte[] b1, byte[] b2) {
    byte[] result = new byte[32];
    for (int i = 0; i < 32; i++) {
        result[i] = (byte)(b1[i] ^ b2[i]);
    }
    return result;
  }

  public static byte[] generateHash(byte[] input) throws NoSuchAlgorithmException {
    MessageDigest digest = MessageDigest.getInstance("SHA-256");
    return digest.digest(input);
  }

  public static int storeChallenge(byte[] challenge, String username){
    String sql = "SELECT uid FROM CS3205.user WHERE username = ?";
    int result = 0;
      try{
          // Maybe need to get from server 4
          PreparedStatement ps =  MySQLAccess.connectDatabase().prepareStatement(sql);
          ps.setString(1, username);
          ResultSet rs = ps.executeQuery();
          while(rs.next()){
            int userID = rs.getInt("uid");
            sql = "INSERT INTO CS3205.user_challenge (challengeString, uid) VALUES (?, ?) ON DUPLICATE KEY UPDATE challengeString=VALUES(challengeString)";
            ps = MySQLAccess.connectDatabase().prepareStatement(sql);
            System.out.println("String: "+Base64.getEncoder().encodeToString(challenge) + "   "+Arrays.toString(challenge));
            ps.setString(1, Base64.getEncoder().encodeToString(challenge));
            ps.setInt(2, userID);
            result = ps.executeUpdate();
            break;
          }
      }catch(Exception e){
          e.printStackTrace();
      }
      return result;
  }

  public static byte[] getUserChallenge(String username){
    String sql = "SELECT * FROM CS3205.user WHERE username = ?";
      try{
          PreparedStatement ps = MySQLAccess.connectDatabase().prepareStatement(sql);
          ps.setString(1, username);
          ResultSet rs = ps.executeQuery();
          while(rs.next()){
            int userID = rs.getInt("uid");
            sql = "SELECT * FROM CS3205.user_challenge WHERE uid = ?";
            ps = MySQLAccess.connectDatabase().prepareStatement(sql);
            ps.setInt(1, userID);
            rs = ps.executeQuery();
            while(rs.next()){
              return Base64.getDecoder().decode(rs.getString("challengeString").getBytes());
            }
          }
      }catch(Exception e){
          e.printStackTrace();
      }
      return null;
  }
}
