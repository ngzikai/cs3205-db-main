package controller;

import entity.Step;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import java.io.*;
import utils.db.*;

public class StepController extends SessionController<Step> {

  private String fileDirectory = "";

  public StepController(String tableName, String fileDirectory){
    super(tableName);
    this.fileDirectory = fileDirectory;
  }

  public int insertStep(InputStream is, long createdDate, int userID){
    Step s = new Step();
    s.setTimestamp(createdDate);
    s.setOwnerID(userID);
    s.setFileLocation(createdDate + "_" + s.getUid() + ".json");
    int result = insert(s);
    if (result == 1){
      super.writeToFile(is, fileDirectory + "/" + s.getOwnerID() + "/" + this.tableName +"/" + s.getFileLocation());
    }
    return result;
  }

  public File getFile(String uid, int userID){
    Step s = get(uid, userID);
    File file = new File(fileDirectory + "/" + s.getOwnerID() + "/" + this.tableName + "/" + s.getFileLocation());
    return file;
  }

  @Override
  public int insert(Step object){
    String sql = "INSERT INTO CS3205." + super.tableName + " VALUES (?, ?, ?, ?)";
    int result = -1;
    try{
      PreparedStatement ps = MySQLAccess.connectDatabase().prepareStatement(sql);
      ps.setString(1, object.getUid());
      ps.setLong(2, object.getTimestamp());
      ps.setString(3, object.getFileLocation());
      ps.setInt(4, object.getOwnerID());
      result = ps.executeUpdate();
      ps.close();
    } catch(Exception e){
      e.printStackTrace();
    }
    return result;
  }

  @Override
  public int update(Step object){
    String sql = "UPDATE CS3205." + super.tableName + " SET location = ? WHERE uid = ?";
    int result = -1;
    try{
      PreparedStatement ps = MySQLAccess.connectDatabase().prepareStatement(sql);
      ps.setString(1, object.getFileLocation());
      ps.setString(2, object.getUid());
      result = ps.executeUpdate();
      ps.close();
    } catch(Exception e){
      e.printStackTrace();
    }
    return result;
  }

  @Override
  public int remove(Step object){
    return -1;
  }

  @Override
  public Step get(String uid, int userID){
    String sql = "SELECT * FROM CS3205." + super.tableName + " WHERE uid = ? AND ownerID = ?";
    Step step = null;
    try{
      PreparedStatement ps = MySQLAccess.connectDatabase().prepareStatement(sql);
      ps.setString(1, uid);
      ps.setInt(2, userID);
      ResultSet rs = ps.executeQuery();
      while(rs.next()){
        step = new Step();
        step.setUid(rs.getString("uid"));
        step.setTimestamp(rs.getLong("createdDate"));
        step.setOwnerID(rs.getInt("ownerID"));
        step.setFileLocation(rs.getString("location"));
        break;
      }
      ps.close();
    } catch(Exception e) {
      e.printStackTrace();
    }
    return step;
  }

  @Override
  public List<Step> getAll(int userID){
    String sql = "SELECT * FROM CS3205." + super.tableName + " WHERE ownerID = ?";
    List<Step> list = new ArrayList<>();
    try{
      PreparedStatement ps = MySQLAccess.connectDatabase().prepareStatement(sql);
      ps.setInt(1, userID);
      ResultSet rs = ps.executeQuery();
      while(rs.next()){
        Step s = new Step();
        s.setUid(rs.getString("uid"));
        s.setTimestamp(rs.getLong("createdDate"));
        s.setFileLocation(rs.getString("location"));
        s.setOwnerID(rs.getInt("ownerID"));
        list.add(s);
      }
    }catch(Exception e){
      e.printStackTrace();
    }
    return list;
  }


}
