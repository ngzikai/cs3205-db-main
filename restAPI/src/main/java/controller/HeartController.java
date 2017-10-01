package controller;

import entity.HeartRate;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import utils.db.*;

public class HeartController extends SessionController<HeartRate> {

  public HeartController(String tableName){
    super(tableName);
  }

  public int insertHeartRate(int userID, int heartRate, long createdDate){
    HeartRate hr = new HeartRate();
    hr.setOwnerID(userID);
    hr.setHeartrate(heartRate);
    hr.setTimestamp(createdDate);
    int result = insert(hr);
    return result;
  }

  @Override
  public int insert(HeartRate object){
    String sql = "INSERT INTO CS3205." + super.tableName + " VALUES (?, ?, ?, ?)";
    int result = -1;
    try{
      PreparedStatement ps = MySQLAccess.connectDatabase().prepareStatement(sql);
      ps.setString(1, object.getUid());
      ps.setLong(2, object.getTimestamp());
      ps.setInt(3, object.getHeartrate());
      ps.setInt(4, object.getOwnerID());
      result = ps.executeUpdate();
      ps.close();
    } catch(Exception e){
      e.printStackTrace();
    }
    return result;
  }

  @Override
  public int update(HeartRate object){
    String sql = "UPDATE CS3205." + super.tableName + " SET heartrate = ? WHERE uid = ?";
    int result = -1;
    try{
      PreparedStatement ps = MySQLAccess.connectDatabase().prepareStatement(sql);
      ps.setInt(1, object.getHeartrate());
      ps.setString(2, object.getUid());
      result = ps.executeUpdate();
      ps.close();
    } catch(Exception e){
      e.printStackTrace();
    }
    return result;
  }

  @Override
  public int remove(HeartRate object){
    return -1;
  }

  @Override
  public HeartRate get(String uid, int userID){
    String sql = "SELECT * FROM CS3205." + super.tableName + " WHERE uid = ? AND ownerID = ?";
    HeartRate heartrate = null;
    try{
      PreparedStatement ps = MySQLAccess.connectDatabase().prepareStatement(sql);
      ps.setString(1, uid);
      ps.setInt(2, userID);
      ResultSet rs = ps.executeQuery();
      while(rs.next()){
        heartrate = new HeartRate();
        heartrate.setUid(rs.getString("uid"));
        heartrate.setTimestamp(rs.getLong("createdDate"));
        heartrate.setOwnerID(rs.getInt("ownerID"));
        heartrate.setHeartrate(rs.getInt("heartrate"));
        break;
      }
      ps.close();
    } catch(Exception e) {
      e.printStackTrace();
    }
    return heartrate;
  }

  @Override
  public List<HeartRate> getAll(int userID){
    System.out.println(userID);
    String sql = "SELECT * FROM CS3205." + super.tableName + " WHERE ownerID = ?";
    List<HeartRate> list = new ArrayList<>();
    try{
      PreparedStatement ps = MySQLAccess.connectDatabase().prepareStatement(sql);
      ps.setInt(1, userID);
      ResultSet rs = ps.executeQuery();
      while(rs.next()){
        HeartRate hr = new HeartRate();
        hr.setOwnerID(rs.getInt("ownerID"));
        hr.setUid(rs.getString("uid"));
        hr.setHeartrate(rs.getInt("heartrate"));
        hr.setTimestamp(rs.getLong("createdDate"));
        list.add(hr);
      }
      ps.close();
    } catch(Exception e) {
      e.printStackTrace();
    }
    return list;
  }


}
