package controller;

import entity.ImageVideo;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import java.io.*;
import utils.db.*;

public class ImageVideoController extends SessionController<ImageVideo> {

  private String fileDirectory = "";
  private String type = "";

  public ImageVideoController(String tableName, String fileDirectory){
    super("file");
    this.fileDirectory = fileDirectory;
    this.type = tableName;
  }

  public int insertFile(InputStream is, long createdDate, int userID){
    ImageVideo s = new ImageVideo();
    s.setTimestamp(createdDate);
    s.setOwnerID(userID);
    String mediaExt = (type.equalsIgnoreCase("video")) ? ".mp4" : (type.equalsIgnoreCase("image")) ? ".jpeg" : "unknown";
    if(mediaExt.equalsIgnoreCase("unknown")){
      return -1;
    }
    s.setFileLocation(createdDate + "_" + s.getUid() + mediaExt);
    s.setType(type);
    int result = insert(s);
    if (result == 1){
      super.writeToFile(is, fileDirectory + "/" + s.getOwnerID() + "/" + type +"/" + s.getFileLocation());
    }
    return result;
  }

  public File getFile(String uid, int userID){
    ImageVideo s = get(uid, userID);
    System.out.println(userID);
    if(s == null){
      return null;
    }
    System.out.println(s==null);
    System.out.println(fileDirectory);
    System.out.println(s.getOwnerID());
    System.out.println(type);
    System.out.println(s.getFileLocation());
    System.out.println(fileDirectory + "/" + s.getOwnerID() + "/" + type + "/" + s.getFileLocation());
    File file = new File(fileDirectory + "/" + s.getOwnerID() + "/" + type + "/" + s.getFileLocation());
    return file;
  }

  @Override
  public int insert(ImageVideo object){
    String sql = "INSERT INTO CS3205.file VALUES (?, ?, ?, ?, ?)";
    int result = -1;
    try{
      PreparedStatement ps = MySQLAccess.connectDatabase().prepareStatement(sql);
      ps.setString(1, object.getUid());
      ps.setLong(2, object.getTimestamp());
      ps.setString(3, object.getFileLocation());
      ps.setInt(4, object.getOwnerID());
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
    String sql = "UPDATE CS3205.file SET location = ? WHERE uid = ?";
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
    String sql = "SELECT * FROM CS3205.file WHERE uid = ? AND ownerID = ? AND type = ?";
    ImageVideo iv = null;
    try{
      PreparedStatement ps = MySQLAccess.connectDatabase().prepareStatement(sql);
      ps.setString(1, uid);
      ps.setInt(2, userID);
      ps.setString(3, type);
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
  public List<ImageVideo> getAll(int userID){
    String sql = "SELECT * FROM CS3205.file WHERE ownerID = ? AND type = ?";
    System.out.println(type);
    List<ImageVideo> list = new ArrayList<>();
    try{
      PreparedStatement ps = MySQLAccess.connectDatabase().prepareStatement(sql);
      ps.setInt(1, userID);
      ps.setString(2, type);
      ResultSet rs = ps.executeQuery();
      while(rs.next()){
        ImageVideo s = new ImageVideo();
        s.setUid(rs.getString("uid"));
        s.setTimestamp(rs.getLong("createdDate"));
        s.setFileLocation(rs.getString("location"));
        s.setOwnerID(rs.getInt("ownerID"));
        s.setType(rs.getString("type"));
        list.add(s);
      }
    }catch(Exception e){
      e.printStackTrace();
    }
    return list;
  }


}
