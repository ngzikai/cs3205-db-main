package controller;

import entity.sessions.*;
import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import org.json.JSONObject;
import utils.db.*;
import utils.db.core.*;

public class SessionController<T extends Session> {
  protected String tableName = "";
  public SessionController(String tableName){
    this.tableName = tableName;
  }

  public int insert(T object){
    String sql = "INSERT INTO CS3205." + tableName + " VALUES (?, ?, ?, ?)";
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

  public int update(T object){
    return -1;
  }

  public int remove(T object){
    return -1;
  }

  public <T> T get(String uid, int userID){
    String sql = "SELECT * FROM CS3205." + tableName + " WHERE uid = ? AND ownerID = ?";
    T t = null;
    try{
      PreparedStatement ps = MySQLAccess.connectDatabase().prepareStatement(sql);
      ps.setString(1, uid);
      ps.setInt(2, userID);
      ResultSet rs = ps.executeQuery();
      while(rs.next()){
        // t = new T(rs.getString("uid"), rs.getLong("createdDate"), rs.getString("ownerID"), rs.getString("location"));
      }
      ps.close();
    } catch(Exception e) {
      e.printStackTrace();
    }
    return t;
  }

  public List<T> getAll(int userID){
    String sql = "SELECT * FROM CS3205." + tableName + " WHERE owner = ?";
    List<T> t = null;
    try{
      PreparedStatement ps = MySQLAccess.connectDatabase().prepareStatement(sql);
      ps.setInt(1, userID);
      ResultSet rs = ps.executeQuery();
      while(rs.next()){
        // t = new T(rs.getString("uid"), rs.getLong("createdDate"), rs.getString("ownerID"), rs.getString("location"));
      }
      ps.close();
    } catch(Exception e) {
      e.printStackTrace();
    }
    return t;
  }

  // public boolean uploadFile(InputStream inputstream){
  //   // save the file onto the destination
  //   Session session = sessionTable.newSession();
  //   session.put("location", session.get("uid").toString()+".json");
  //   session.put("createdDate", System.currentTimeMillis()/1000);
  //   // For now hard code, but need to take the current userID
  //   session.put("ownerID", 1);
  //   int result = session.save();
  //   if (result > 0) {
  //     writeToFile(inputstream, baseDir, "user1", sessionTable.getTableName(), session.get("location").toString());
  //     return true;
  //   } else {
  //     return false;
  //   }
  // }

  private String readFile(String fileLocation){
    String result = "";
    File file = new File(fileLocation);
    String currentDirectory = file.getAbsolutePath();
    System.out.println(currentDirectory);
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
  private void writeToFile(InputStream uploadedInputStream, String uploadedFileLocation) {

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
