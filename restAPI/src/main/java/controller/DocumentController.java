package controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

/*
 * This class handles all operation on the document feature and the related database operations
 * needed for the documents feature to work. This class controls the operations and uses
 * the relevant entities required.
 */
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
	 * 
	 * @param document
	 * @return JSONObject containing 1 if success
	 * 								 0 if failed
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
				for(int record : document.getRecords()) {
					int includeRes = createInclusion(rid, record);
					if(includeRes == 0) {
						System.out.println("Failed creating inclusion: " + rid + " & " + record  ) ;
					}
				}
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
	 * 
	 * @param document 
	 * @return true if success
	 * 			false if failed
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

	/*
	 * This method will take a document object and retrieve the rid of the associated document as creation of
	 * rid is by database. It is using as much similarity as it could to avoid collision with
	 * other document that is being created at the same time.
	 * 
	 * @param document
	 * 		  random   String value
	 * 
	 * @return rid 
	 */
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
			e.printStackTrace();
			return -1;
		}
		MySQLAccess.close();
		return rid;
	}

	/*
	 * This method takes in a document object and update the document entry in the database
	 * with the correct file path stored by the system in the file system
	 * 
	 * @param document
	 * @return int 	1 if success
	 * 				0 if failed
	 * 
	 */
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
			e.printStackTrace();
			return 0;
		}
		MySQLAccess.close();
		return result;
	}

	/*
	 * This method generates a random integer within 1 to 1000000.
	 * 
	 * @return the random integer
	 */
	private int randomInt() {
		Random randomGenerator = new Random();
		return randomGenerator.nextInt(1000000);
	}


	/*
	 * This method will retrieve the document using the data object as metadata to identify the
	 * user id and the file path of the document. It uses the decrypt function of the crypto
	 * and reads in the xml information of Document class, which then converted to JSONObject
	 * to be returned.
	 * 
	 * @param data
	 * @return JSONObject containing the document
	 */
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
		return jsonObject;
	}

	/*
	 * This method deletes the document and the relevant entry on the database based on the rid
	 * and the owner's uid. 
	 * 
	 * @param rid of the document
	 * 		  uid of the owner
	 * 
	 * @return JSONObject contained the result of the operation. 1 is success.
	 * 															 0 is failed.
	 */
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

	/*
	 * This method deletes the file from the file system if it exists.
	 * 
	 * @param path
	 * @return true if success.
	 * 		   false if failed.
	 */
	private boolean removeDocument(Path path) {
		System.out.println("Removing " + path.toString());
		try {
			Files.deleteIfExists(path);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/*
	 * This method creates the inclusion of the records that are include in
	 * the document created. 
	 * 
	 * @param report's rid 
	 * 		  record's rid
	 * @return 1 if success.
	 * 		   0 if failed.
	 * 
	 */
	private int createInclusion(int reportId, int recordId) {
		int result = 0;
		String sql = "INSERT INTO CS3205.inclusion VALUES (default, ?, ?)";
		try {
			Connection connect = MySQLAccess.connectDatabase();
			PreparedStatement preparedStatement = connect.prepareStatement(sql);
			preparedStatement.setInt(1, reportId);
			preparedStatement.setInt(2, recordId);
			String statement = preparedStatement.toString();
			result = MySQLAccess.updateDataBasePS(preparedStatement);
			Logger.log(Logger.API.TEAM1.name(), Logger.TYPE.WRITE.name(), statement, result);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		MySQLAccess.close();
		return 1;
	}

	/*
	 * This method takes in a therapist id and it will retrieve all the documents that are created from the database first.
	 * The records in each documents which is stored in the inclusion table will then be used to check if each records have consent
	 * granted to the therapist id. This is a complex SQL query where it uses NOT EXISTS to filter out the necessary information to
	 * retrieve a list of documents that this therapistId has full consents for the records included in them.
	 * 
	 *@param therapistId int
	 *@return JSONObject containing the list of records
	 */
	public JSONObject getAllDocumentWithFullConsentsFromUid(int therapistid) {
		JSONObject jsonObject = new JSONObject();
		ArrayList<Data> consentList = null;

		String sql = "SELECT * FROM CS3205.data d WHERE d.subtype = \"document\" AND NOT EXISTS ("
				+ "SELECT * FROM CS3205.inclusion i WHERE i.report_id = d.rid AND NOT EXISTS ("
				+ "SELECT * FROM CS3205.consent c WHERE c.rid = i.record_id AND status = 1 AND c.uid = ?));";
		try {
			Connection connect = MySQLAccess.connectDatabase();
			PreparedStatement preparedStatement = connect.prepareStatement(sql);
			preparedStatement.setInt(1, therapistid);
			String statement = preparedStatement.toString();
			consentList = resultSetToDataList(MySQLAccess.readDataBasePS(preparedStatement));
			Logger.log(Logger.API.TEAM1.name(), Logger.TYPE.READ.name(), statement, consentList.size() == 0 ? 0 : 1);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		if(consentList.size() < 1) {
			return null;
		}

		MySQLAccess.close();

		jsonObject.put("records", consentList);
		return jsonObject;
	}

	/*
	 * This method will take in a result set of a SQL operation and prepares a
	 * list of Data object with the corresponding fields from the result set
	 * 
	 * @param result of SQL query
	 * @return ArrayList of Data objects
	 * 
	 */
	private ArrayList<Data> resultSetToDataList(ResultSet result) throws SQLException {
		ArrayList<Data> dataList = new ArrayList<Data>();
		while(result.next()){
			Data data = new Data();
			data.setRid(result.getInt("rid"));
			data.setUid(result.getInt("uid"));
			data.setType(result.getString("type"));
			data.setSubtype(result.getString("subtype"));
			data.setContent(result.getString("content"));
			data.setTitle(result.getString("title"));
			data.setCreationdate(result.getTimestamp("creationdate"));
			data.setModifieddate(result.getTimestamp("modifieddate"));
			dataList.add(data);
		}
		MySQLAccess.close();
		return dataList;
	}
	
	/*
	 * This method takes in a therapist id and it will retrieve all the documents that are created from the database first.
	 * The records in each documents which is stored in the inclusion table will then be used to check if each records have consent
	 * granted to the therapist id. This is a complex SQL query where it uses NOT EXISTS to filter out the necessary information to
	 * retrieve a list of documents that this therapistId has full consents for the records included in them.
	 * 
	 *@param therapistId int
	 *@return JSONObject containing the list of records
	 */
	public JSONObject checkUserAccessToAllRecordsInDoc(int uid, int rid) {
		JSONObject jsonObject = new JSONObject();
		ResultSet resultSet = null;
		boolean result = false;
		String sql = "SELECT * FROM CS3205.data d WHERE d.subtype = \"document\" AND d.rid = ? AND NOT EXISTS ("
				+ "SELECT * FROM CS3205.inclusion i WHERE i.report_id = d.rid AND NOT EXISTS ("
				+ "SELECT * FROM CS3205.consent c WHERE c.rid = i.record_id AND status = 1 AND c.uid = ?));";
		try {
			Connection connect = MySQLAccess.connectDatabase();
			PreparedStatement preparedStatement = connect.prepareStatement(sql);
			preparedStatement.setInt(1, rid);
			preparedStatement.setInt(2, uid);
			String statement = preparedStatement.toString();
			resultSet = MySQLAccess.readDataBasePS(preparedStatement);
			while (resultSet.next()) {
				result = true;
			}
			Logger.log(Logger.API.TEAM1.name(), Logger.TYPE.READ.name(), statement, result ? 1 : 0);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		MySQLAccess.close();

		jsonObject.put("result", result);
		return jsonObject;
	}
}
