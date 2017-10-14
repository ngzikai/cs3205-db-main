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
import utils.SystemConfig;

public class SessionController<T extends Session> {
  private String fileDirectory = SystemConfig.getConfig("storage_directory");
  protected String tableName = "";

  public SessionController(){

  }

  public SessionController(String tableName){
    this.tableName = tableName;
  }

  public SessionController(String tableName, String fileDirectory){
    this.tableName = tableName;
    this.fileDirectory = fileDirectory;
  }

  public int insert(Data object){
    String sql = "INSERT INTO CS3205.data (uid, type, subtype, title, creationdate, modifieddate, content) VALUES (?, ?, ?, ?, ?, ?, ?)";
    int result = -1;
    try{
      PreparedStatement ps = MySQLAccess.connectDatabase().prepareStatement(sql);
      ps.setInt(1, object.getUid());
      ps.setString(2, object.getType());
      ps.setString(3, object.getSubtype());
      ps.setString(4, object.getTitle());
      ps.setTimestamp(5, object.getCreationdate());
      ps.setTimestamp(6, object.getModifieddate());
      ps.setString(7, object.getContent());
      result = ps.executeUpdate();
      ps.close();
    } catch(Exception e){
      e.printStackTrace();
    }
    return result;
  }

  public JSONObject getStepMetaInfo(Data step){
    String jsonStr = readFile(step.getAbsolutePath());
    JSONObject jObj = new JSONObject(jsonStr);
    jObj.remove("sensor");
    return jObj;
  }

  public Data get(int userID, int rid){
    String sql = "SELECT * FROM CS3205.data WHERE uid = ? AND rid = ?";
    Data data = null;
    try{
      PreparedStatement ps = MySQLAccess.connectDatabase().prepareStatement(sql);
      ps.setInt(1, userID);
      ps.setInt(2, rid);
      ResultSet rs = ps.executeQuery();
      while(rs.next()){
        data = new Data();
        data.setRid(rs.getInt("rid"));
        data.setUid(rs.getInt("uid"));
        data.setType(rs.getString("type"));
        data.setSubtype(rs.getString("subtype"));
        data.setContent(rs.getString("content"));
        data.setTitle(rs.getString("title"));
        data.setCreationdate(rs.getTimestamp("creationdate"));
        data.setModifieddate(rs.getTimestamp("modifieddate"));
        break;
      }
      ps.close();
    } catch(Exception e) {
      e.printStackTrace();
    }
    return data;
  }

  public List<Data> getAll(int userID, String subtype){
    String sql = "SELECT * FROM CS3205.data WHERE uid = ? AND subtype =?";
    List<Data> list = new ArrayList<>();
    try{
      PreparedStatement ps = MySQLAccess.connectDatabase().prepareStatement(sql);
      ps.setInt(1, userID);
      ps.setString(2, subtype);
      ResultSet rs = ps.executeQuery();
      while(rs.next()){
        Data data = new Data();
        data.setRid(rs.getInt("rid"));
        data.setUid(rs.getInt("uid"));
        data.setType(rs.getString("type"));
        data.setSubtype(rs.getString("subtype"));
        data.setContent(rs.getString("content"));
        data.setTitle(rs.getString("title"));
        data.setCreationdate(rs.getTimestamp("creationdate"));
        data.setModifieddate(rs.getTimestamp("modifieddate"));
        list.add(data);
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

  public File getActualFile(String fileLocation){
    File file = new File(fileLocation);
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
