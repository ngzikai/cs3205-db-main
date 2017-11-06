package controller;

// Utils imports
import utils.GUID;
import utils.SystemConfig;
import utils.Cryptography;
import utils.db.*;
import utils.team3.*;

// Java imports
import java.io.File;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.ArrayList;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Statement;

// Data objects
import org.json.JSONObject;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.GsonBuilder;
import entity.Data;
import entity.steps.*;


public class SessionController {
	private String fileDirectory = SystemConfig.getConfig("storage_directory");

	public SessionController(){
	}

	public SessionController(String fileDirectory){
		this.fileDirectory = fileDirectory;
	}

	public int insert(Data object){
		String sql = "INSERT INTO CS3205.data (uid, type, subtype, title, creationdate, modifieddate, content) VALUES (?, ?, ?, ?, ?, ?, ?)";
		int result = -1;
		try{
			PreparedStatement ps = MySQLAccess.connectDatabase().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, object.getUid());
			ps.setString(2, object.getType());
			ps.setString(3, object.getSubtype());
			ps.setString(4, object.getTitle());
			ps.setTimestamp(5, object.getCreationdate());
			ps.setTimestamp(6, object.getModifieddate());
			ps.setString(7, object.getContent());
			result = ps.executeUpdate();
			try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                result = generatedKeys.getInt(1);
            } else {
                throw new Exception("No ID obtained.");
            }
        }
			ps.close();
		} catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}

	public JSONObject getStepMetaInfo(Data step){
		JSONObject jObj = null;
		try{
		 jObj = new JSONObject(JSONUtil.processMETA(readStepFile(step.getAbsolutePath())));
		}catch(Exception e){

		}
		return jObj;
	}

	public Data get(int rid){
		return get(-1, rid);
	}

	public Data get(int userID, int rid){
		return get(userID, rid, null);
	}

	public Data get(int userID, int rid, String type){
		String insertUid = (userID == -1) ? "`uid`" : "?";
		String insertType = (type == null) ? "`subtype`" : "?";
		String sql = "SELECT * FROM CS3205.data WHERE `rid` = ? AND `uid` = "+insertUid+" AND `subtype` = "+insertType;
		Data data = null;
		try{
			PreparedStatement ps = MySQLAccess.connectDatabase().prepareStatement(sql);
			ps.setInt(1, rid);
			if(userID != -1){
				ps.setInt(2, userID);
			}
			if(type != null){
				ps.setString(3, type);
			}
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
		if(type.equalsIgnoreCase("step")){
			type = "time series";
		}
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
		Steps steps = null;
		try{
			byte[] content = decryptFile(fileLocation);
			BufferedReader reader = prepareContentToJSON(content);
			if( content != null ){
				Gson gson = new GsonBuilder().create();
				steps = gson.fromJson(reader, Steps.class);
			}
			reader.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return steps;
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

	public BufferedReader prepareContentToJSON(byte[] content){
		ByteArrayInputStream bais = new ByteArrayInputStream(content);
		InputStreamReader isr = new InputStreamReader(bais);
		return new BufferedReader(isr);
	}

	// save uploaded file to new location
	public void writeToFile(InputStream uploadedInputStream, String uploadedFileLocation) {
		try {
			// uploadedFileLocation = baseDir + user + "/" + typeFolder + "/" + uploadedFileLocation;
			File file = new File(uploadedFileLocation);
			file.getParentFile().mkdirs();

			// Encrypt the file
			Cryptography crypto = Cryptography.getInstance();
			byte[] content = CommonUtil.inputStreamToByteArray(uploadedInputStream);
			byte[] encrypted = crypto.encrypt(content);

			// Write encrypted file to location
			FileOutputStream outputStream =  new FileOutputStream(file);
			outputStream.write(encrypted);
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
