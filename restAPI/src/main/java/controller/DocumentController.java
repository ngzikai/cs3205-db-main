package controller;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Random;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.json.JSONObject;

import entity.Data;
import entity.Document;
import entity.User;
import utils.db.MySQLAccess;

public class DocumentController {
	private String type = "File";
	private String subType = "document";
	private String format = ".xml";
	SessionController sc = new SessionController();

	public JSONObject createDocument(Document document) {
		JSONObject jsonObject = new JSONObject();
		String random = randomInt() +"";
		Data data = new Data(document.getTherapistId(), type, subType, document.getTitle(),
				document.getCreationdate(), document.getModifieddate(), random);
		int result = 0;
		result = sc.insert(data);
		if(result == 1) {
			int rid = getRid(document, random);
			document.print();
			if(rid > 0) {
				document.setRid(rid);
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

	private boolean writeDocument(Document document) {
		boolean result = false;
		try{
			//creating the JAXB context
			JAXBContext jContext = JAXBContext.newInstance(Document.class);
			//creating the marshaller object
			Marshaller marshallObj = jContext.createMarshaller();
			//setting the property to show xml format output
			marshallObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			//calling the marshall method
			marshallObj.marshal(document, new FileOutputStream("C:\\data\\" + document.getRid() + format));
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
}
