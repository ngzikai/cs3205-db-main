package controller;

import entity.ImageVideo;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import utils.db.*;

public class ImageVideoController extends SessionController<ImageVideo> {

  private String fileDirectory = "";

  public ImageVideoController(String tableName, String fileDirectory){
    super(tableName);
    this.fileDirectory = fileDirectory + "/" + tableName;
  }

  @Override
  public int insert(ImageVideo object){
    String sql = "INSERT INTO CS3205." + super.tableName + " VALUES (?, ?, ?, ?, ?)";
    int result = -1;
    try{
      PreparedStatement ps = MySQLAccess.connectDatabase().prepareStatement(sql);
      ps.setString(1, object.getUid());
      ps.setLong(2, object.getTimestamp());
      ps.setInt(3, object.getOwnerID());
      ps.setString(4, object.getFileLocation());
      ps.setString(5, object.getType());
      result = ps.executeUpdate();
      ps.close();
    } catch(Exception e){
      e.printStackTrace();
    }
    return result;
  }

  @Override
  public int update(ImageVideo object){
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
  public int remove(ImageVideo object){
    return -1;
  }

  @Override
  public ImageVideo get(String uid, int userID){
    String sql = "SELECT * FROM CS3205." + super.tableName + " WHERE uid = ? AND ownerID = ?";
    ImageVideo iv = null;
    try{
      PreparedStatement ps = MySQLAccess.connectDatabase().prepareStatement(sql);
      ps.setString(1, uid);
      ps.setInt(2, userID);
      ResultSet rs = ps.executeQuery();
      while(rs.next()){
        iv = new ImageVideo();
        iv.setUid(rs.getString("uid"));
        iv.setTimestamp(rs.getLong("createdDate"));
        iv.setOwnerID(rs.getInt("ownerID"));
        iv.setFileLocation(rs.getString("location"));
        iv.setType(rs.getString("type"));
        break;
      }
      ps.close();
    } catch(Exception e) {
      e.printStackTrace();
    }
    return iv;
  }

  @Override
  public List<ImageVideo> getAll(String userID){
    super.getAll(userID);
    return null;
  }


}
