package controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Random;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.json.JSONObject;

import entity.Data;
import entity.Document;
import utils.Cryptography;
import utils.Logger;
import utils.SystemConfig;
import utils.db.MySQLAccess;

public class DocumentController {
	private String type = "File";
	public static String SUBTYPE = "document";
	private String format = ".xml";
	private String fileDirectory = SystemConfig.getConfig("storage_directory");
	//private String fileDirectory = "C:\\data\\";

	SessionController sc = new SessionController();

	/*
	 * This method will create a document first by creating a data object that describes
	 * this document, then insert the data object into the database as a "metadata" for 
	 * the document. 
	 * Then it will retrieve the rid of the data object from database to be used as an
	 * unique file name for the document.
	 * The document will then be written into the file system, followed by updating
	 * the database on the filepath of this document. 
	 */
	public JSONObject createDocument(Document document) {
		JSONObject jsonObject = new JSONObject();
		String random = randomInt() +"";
		Data data = new Data(document.getTherapistId(), type, SUBTYPE, document.getTitle(),
				document.getCreationdate(), document.getModifieddate(), random);
		int result = 0;
		result = sc.insert(data);
		if(result == 1) {
			int rid = getRid(document, random);
			
			if(rid > 0) {
				document.setRid(rid);
				document.print();
				boolean isWriten = writeDocument(document);
				boolean isUpdated = (updateContent(document) == 1 )? true: false;
				if(!isWriten || !isUpdated) {
					result = 0;
				}
				System.out.println("Created document: " + document.getTitle() + " ... " + isWriten );
			} 
		} else if (result < 0) {
			result = 0;
		}
		jsonObject.put("result", result);
		return jsonObject;
	}

	/*
	 * Write XMl file of the document from a therapist.
	 * If directory does not exist, it will be created.
	 */
	private boolean writeDocument(Document document) {
		boolean result = false;
		try{
			String filepath = fileDirectory + "/" + document.getTherapistId() 
			+ "/" + SUBTYPE +"/";
		    File directory = new File(filepath);
		    if (! directory.exists()){
		        directory.mkdirs();
		    }
			//creating the JAXB context
			JAXBContext jContext = JAXBContext.newInstance(Document.class);
			//creating the marshaller object
			Marshaller marshallObj = jContext.createMarshaller();
			//setting the property to show xml format output
			marshallObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			//calling the marshall method and outputing it to a byte stream
			ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
			marshallObj.marshal(document,dataStream);
			
			//Use utils.Cryptography to get instance and encrypt data
			Cryptography crypto = Cryptography.getInstance();
			byte[] encrypted = crypto.encrypt(dataStream.toByteArray());
			
			//Output file content
			FileOutputStream outputStream =  new FileOutputStream(filepath +"/" + document.getRid() + format);
			outputStream.write(encrypted);
			outputStream.close();
			result = true;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private int getRid(Document document, String random) {
		int rid = 0;
		String sql = "SELECT * FROM CS3205.data WHERE uid = ? AND title = ? AND content = ?";
		try {
			Connection connect = MySQLAccess.connectDatabase();
			PreparedStatement preparedStatement = connect.prepareStatement(sql);
			preparedStatement.setInt(1, document.getTherapistId());
			preparedStatement.setString(2, document.getTitle());
			preparedStatement.setString(3, random);
			System.out.println(preparedStatement);
			ResultSet rs = MySQLAccess.readDataBasePS(preparedStatement);
			while(rs.next()){
				rid = rs.getInt("rid");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
		MySQLAccess.close();
		return rid;
	}
	
	private int updateContent(Document document) {
		int result = 0;
		String sql = "UPDATE CS3205.data SET content = ? WHERE rid = ?";
		try {
			Connection connect = MySQLAccess.connectDatabase();
			PreparedStatement preparedStatement = connect.prepareStatement(sql);
			preparedStatement.setString(1, document.getRid()+format);
			preparedStatement.setInt(2, document.getRid());
			result = MySQLAccess.updateDataBasePS(preparedStatement);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
		MySQLAccess.close();
		return result;
	}
	
	private int randomInt() {
		Random randomGenerator = new Random();
		return randomGenerator.nextInt(1000000);
	}
	
	// This method will take in a patient id and a therapist, and return the Consent object from the database;
	public JSONObject getDocument(Data data) {
		JSONObject jsonObject = null;
		try {
			Path path = Paths.get(fileDirectory + "/" + data.getUid() + "/" + SUBTYPE +"/" + data.getContent());
			//Uses Cryptography to decrypt the content 
			Cryptography crypto = Cryptography.getInstance();
			byte[] encrypted = Files.readAllBytes(path);
			byte[] content = crypto.decrypt(encrypted);
			
			//Converts XML content into document object
			JAXBContext jContext = JAXBContext.newInstance(Document.class);
			Unmarshaller unmarshallerObj = jContext.createUnmarshaller();
			Document document = (Document) unmarshallerObj.unmarshal(new ByteArrayInputStream(content));
			jsonObject = new JSONObject(document);
		}catch(Exception e){
		    e.printStackTrace();
		    return null;
		}
		
		jsonObject.put("creationdate", data.getCreationdate());
		jsonObject.put("modifieddate", data.getModifieddate());
		//System.out.println("Retrieving details of Consent: " + id);
		return jsonObject;
	}
	
	public JSONObject deleteDocument(int rid, int uid) {
		int result = 0;
		JSONObject jsonObject = new JSONObject();
		String sql = "DELETE FROM CS3205.data where rid = ?";
		
		System.out.println("Deleting data : " + rid);
		try {
			Connection connect = MySQLAccess.connectDatabase();
			PreparedStatement preparedStatement = connect.prepareStatement(sql);
		    preparedStatement.setInt(1, rid);
		    String statement = preparedStatement.toString();
			result = MySQLAccess.updateDataBasePS(preparedStatement);
			Logger.log(Logger.API.TEAM1.name(), Logger.TYPE.WRITE.name(), statement, result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		if(result == 1) {
			boolean isRemoved = removeDocument(Paths.get(fileDirectory + "/" + uid + "/" + SUBTYPE +"/" + rid + format));
		}
		MySQLAccess.close();
		jsonObject.put("result", result);
		return jsonObject;
	}
	
	private boolean removeDocument(Path path) {
		System.out.println("Removing " + path.toString());
		try {
			Files.deleteIfExists(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
