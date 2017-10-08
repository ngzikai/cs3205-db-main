package controller;

import entity.sessions.*;
import entity.*;
import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import org.json.JSONObject;
import utils.db.*;
import utils.db.core.*;

public class SessionController<T extends Session> {
  private String fileDirectory = "/home/jim/temp";
  protected String tableName = "";
  public SessionController(String tableName){
    this.tableName = tableName;
  }

  public SessionController(String tableName, String fileDirectory){
    this.tableName = tableName;
    this.fileDirectory = fileDirectory;
  }

  public int insertStep(Step object){
    String sql = "INSERT INTO CS3205.step VALUES (?, ?, ?, ?)";
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

  public int insertHeartRate(HeartRate object){
    String sql = "INSERT INTO CS3205.heartrate VALUES (?, ?, ?, ?)";
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

  public int insertFile(ImageVideo object){
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

  public int update(T object){
    return -1;
  }

  public int remove(T object){
    return -1;
  }

  public Step getStep(String uid, int userID){
    String sql = "SELECT * FROM CS3205.step WHERE uid = ? AND ownerID = ?";
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
  public HeartRate getHeartrate(String uid, int userID){
    String sql = "SELECT * FROM CS3205.heartrate WHERE uid = ? AND ownerID = ?";
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
  public ImageVideo getFile(String uid, int userID, String type){
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

  public List<Step> getAllStep(int userID){
    String sql = "SELECT * FROM CS3205.step WHERE ownerID = ?";
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

  public List<HeartRate> getAllHeartRate(int userID){
    String sql = "SELECT * FROM CS3205.heartrate WHERE ownerID = ?";
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

  public List<ImageVideo> getAllImageVideo(int userID, String type){
    String sql = "SELECT * FROM CS3205.file WHERE ownerID = ? AND type = ?";
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

  public File getActualFile(String fileLocation, int userID, String type){
    File file = new File(fileDirectory + "/" + userID + "/" + type + "/" + fileLocation);
    return file;
  }

  public String readFile(String fileLocation){
    String result = "";
    File file = new File(fileLocation);
    String currentDirectory = file.getAbsolutePath();
    try {
        BufferedReader br = new BufferedReader(new FileReader(fileLocation));
        StringBuilder sb = new StringBuilder();
        String line = br.readLine();
        while (line != null) {
            sb.append(line);
            line = br.readLine();
        }
        result = sb.toString();
    } catch(Exception e) {
        e.printStackTrace();
    }
    return result;
  }

  // save uploaded file to new location
  public void writeToFile(InputStream uploadedInputStream, String uploadedFileLocation) {

  	try {
      // uploadedFileLocation = baseDir + user + "/" + typeFolder + "/" + uploadedFileLocation;
      File file = new File(uploadedFileLocation);
      file.getParentFile().mkdirs();
  		OutputStream out = new FileOutputStream(file);
  		int read = 0;
  		byte[] bytes = new byte[1024];

  		out = new FileOutputStream(new File(uploadedFileLocation));
  		while ((read = uploadedInputStream.read(bytes)) != -1) {
  			out.write(bytes, 0, read);
  		}
  		out.flush();
  		out.close();
  	} catch (IOException e) {

  		e.printStackTrace();
  	}
  }
}
