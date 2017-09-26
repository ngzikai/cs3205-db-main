package controller;

import utils.db.core.*;
import entity.sessions.*;

import java.util.*;
import java.io.*;

import org.json.JSONObject;

public class SessionController {

  private SessionTable sessionTable = null;
  private String baseDir = "/home/jim/temp/";

  public SessionController(String tableName){
    sessionTable = new SessionTable(tableName);
  }

  public DataTable getTable(){
    return sessionTable;
  }

  public JSONObject getSessionObject(String id){
    Session s = sessionTable.getDataObject(id);
    String jsonData = readFile(baseDir+s.getFileLocation());
    JSONObject jsonObject = new JSONObject(jsonData);
    return jsonObject;
  }

  public List<DataObject> getAllObjects(){
    return sessionTable.getAllObjects();
  }

  public Session newObject(){
    return new Session(sessionTable);
  }

  public boolean uploadFile(InputStream inputstream){
    // save the file onto the destination
    Session session = sessionTable.newSession();
    session.put("location", session.get("uid").toString()+".json");
    session.put("createdDate", System.currentTimeMillis()/1000);
    // For now hard code, but need to take the current userID
    session.put("ownerID", 1);
    int result = session.save();
    if (result > 0) {
      writeToFile(inputstream, baseDir + sessionTable.getTableName() + "/" + session.get("location").toString());
      return true;
    } else {
      return false;
    }
  }

  private String readFile(String filename){
    String result = "";
    File file = new File(filename);
    String currentDirectory = file.getAbsolutePath();
    System.out.println(currentDirectory);
    try {
        BufferedReader br = new BufferedReader(new FileReader(filename));
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
  private void writeToFile(InputStream uploadedInputStream,
  	String uploadedFileLocation) {

  	try {
  		OutputStream out = new FileOutputStream(new File(
  				uploadedFileLocation));
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
