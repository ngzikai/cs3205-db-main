package controller;

import entity.*;
import entity.steps.*;
import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import org.json.JSONObject;
import com.google.gson.*;
import utils.db.*;
import utils.SystemConfig;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ObjectInput;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import utils.Cryptography;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import com.google.gson.Gson;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;
import utils.team3.CommonUtil;
import utils.GUID;

public class SessionController {
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
    JSONObject jObj = new JSONObject(readStepFile(step.getAbsolutePath()));
    jObj.remove("time");
    jObj.remove("channels");
    return jObj;
  }

  public Data get(int rid){
	    String sql = "SELECT * FROM CS3205.data WHERE rid = ?";
	    Data data = null;
	    try{
	      PreparedStatement ps = MySQLAccess.connectDatabase().prepareStatement(sql);
	      ps.setInt(1, rid);
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

  public Data get(int userID, int rid){
    String sql = "SELECT * FROM CS3205.data WHERE `uid` = ? AND `rid` = ?";
    Data data = null;
    try{
      PreparedStatement ps = MySQLAccess.connectDatabase().prepareStatement(sql);
      if(userID == -1){
        ps.setString(1, "`uid`");
      } else {
        ps.setInt(1, userID);
      }
      ps.setInt(2, rid);
      ResultSet rs = ps.executeQuery();
      while(rs.next()){
        data = setAllData(rs);
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
        Data data = setAllData(rs);
        list.add(data);
      }
    }catch(Exception e){
      e.printStackTrace();
    }
    return list;
  }

  public List<Data> getAllFromUser(int userID){
	    String sql = "SELECT * FROM CS3205.data WHERE uid = ?";
	    List<Data> list = new ArrayList<>();
	    try{
	      PreparedStatement ps = MySQLAccess.connectDatabase().prepareStatement(sql);
	      ps.setInt(1, userID);
	      ResultSet rs = ps.executeQuery();
	      while(rs.next()){
          Data data = setAllData(rs);
	        list.add(data);
	      }
	    }catch(Exception e){
	      e.printStackTrace();
	    }
	    return list;
  }

  public Data setAllData(ResultSet rs) throws Exception{
    Data data = new Data();
    data.setRid(rs.getInt("rid"));
    data.setUid(rs.getInt("uid"));
    data.setType(rs.getString("type"));
    data.setSubtype(rs.getString("subtype"));
    data.setContent(rs.getString("content"));
    data.setTitle(rs.getString("title"));
    data.setCreationdate(rs.getTimestamp("creationdate"));
    data.setModifieddate(rs.getTimestamp("modifieddate"));
    return data;
  }

  public File getActualFile(String fileLocation, int userID, String type){
    return getActualFile(fileDirectory + "/" + userID + "/" + type + "/" + fileLocation);
  }

  public File getActualFile(String fileLocation){
    try{
        byte[] content = decryptFile(fileLocation);
        File temp = File.createTempFile("/tmp/"+GUID.BASE58(),".tmp");
        if(content != null){
          FileOutputStream fos = new FileOutputStream(temp.getAbsolutePath());
          fos.write(content);
          fos.close();
        }
      temp.deleteOnExit();
      return new File(temp.getAbsolutePath());
    }catch(Exception e){
      e.printStackTrace();
    }
    return null;
  }

  public Steps readStepFile(String fileLocation){
    try{
      byte[] content = decryptFile(fileLocation);
      ByteArrayInputStream bais = new ByteArrayInputStream(content);
      InputStreamReader isr = new InputStreamReader(bais);
      BufferedReader reader = null;
      if( content != null ){
        reader = new BufferedReader(isr);
        Gson gson = new GsonBuilder().create();
        Steps steps = gson.fromJson(reader, Steps.class);
        return steps;
      }
    }catch(Exception e){
      e.printStackTrace();
    }
    return null;
  }

  public byte[] decryptFile(String fileLocation){
    try{
      Path path = Paths.get(fileLocation);
      //Uses Cryptography to decrypt the content
      Cryptography crypto = Cryptography.getInstance();
      byte[] encrypted = Files.readAllBytes(path);
      byte[] content = crypto.decrypt(encrypted);
      return content;
    }catch(Exception e){
      e.printStackTrace();
    }
    return null;
  }

  // save uploaded file to new location
  public void writeToFile(InputStream uploadedInputStream, String uploadedFileLocation) {
  	try {
      // uploadedFileLocation = baseDir + user + "/" + typeFolder + "/" + uploadedFileLocation;
      File file = new File(uploadedFileLocation);
      file.getParentFile().mkdirs();

      // Encrypt the file
      Cryptography crypto = Cryptography.getInstance();
      byte[] targetArray = new byte[uploadedInputStream.available()];
      uploadedInputStream.read(targetArray);
			byte[] encrypted = crypto.encrypt(targetArray);

      // Write encrypted file to location
      FileOutputStream outputStream =  new FileOutputStream(file);
      outputStream.write(encrypted);
			outputStream.close();
  		// OutputStream out = new FileOutputStream(file);
  		// int read = 0;
  		// byte[] bytes = new byte[1024];
      //
  		// out = new FileOutputStream(new File(uploadedFileLocation));
  		// while ((read = uploadedInputStream.read(bytes)) != -1) {
  		// 	out.write(bytes, 0, read);
  		// }
  		// out.flush();
  		// out.close();
  	} catch (IOException e) {

  		e.printStackTrace();
  	}
  }
  public void writeStepsToFile(Steps steps, String uploadedFileLocation){
    try {
      File file = new File(uploadedFileLocation);
      file.getParentFile().mkdirs();
      byte[] bytes = CommonUtil.toByteArray(steps);
      // Encrypt the file
      Cryptography crypto = Cryptography.getInstance();
      byte[] encrypted = crypto.encrypt(bytes);

      // Write encrypted file to location
      FileOutputStream outputStream =  new FileOutputStream(file);
      outputStream.write(encrypted);
      outputStream.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static final Steps JSONtoSteps(JsonObject jsonObject) {
        try {
            Steps data = new Steps(jsonObject.get(Steps.FIELD_RECORD).getAsLong(), jsonObject.get(Steps.FIELD_TITLE).getAsString());

            JsonObject timeObj = jsonObject.getAsJsonObject(Steps.FIELD_TIME);
            JsonArray jTimeArr = timeObj.getAsJsonArray(Steps.FIELD_VALUE);
            Steps_Time time = new Steps_Time();
            ArrayList<Long> timeValues = new ArrayList<Long>();

            for (int i = 0; i < jTimeArr.size(); i++) {
                timeValues.add(jTimeArr.get(i).getAsLong());
            }
            time.setValues(timeValues);

            JsonObject channelsObj = jsonObject.getAsJsonObject(Steps.FIELD_CHANNELS);
            System.out.println("channelsObj:"+channelsObj.toString());
            JsonArray channelsArray = channelsObj.getAsJsonArray(Steps.FIELD_DATA);
            ArrayList<Steps_Channel> channelData = new ArrayList<Steps_Channel>();

            for (int i = 0; i < channelsArray.size(); i++) {
                Steps_Channel channel = new Steps_Channel();
                JsonObject channelObject = channelsArray.get(i).getAsJsonObject();

                channel.setName(channelObject.get(Steps.FIELD_NAME).getAsString());

                JsonArray channelArray = channelObject.getAsJsonArray(Steps.FIELD_VALUE);
                ArrayList<Long> values = new ArrayList<Long>();
                for (int j = 0; j < channelArray.size(); j++) {
                    values.add(channelArray.get(j).getAsLong());
                }

                channel.setValues(values);
                channelData.add(channel);
            }

            Steps_Channels channels = new Steps_Channels();
            channels.setData(channelData);
            data.setChannels(channels);

            return data;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
