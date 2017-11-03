package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import entity.Consent;
import entity.Data;
import utils.Logger;
import utils.db.MySQLAccess;

public class ConsentController {

	/*
	 * This method will get all consents value from the database and return them.
	 * 
	 * @return JSONObject containing the result
	 * 		  null if empty or error
	 */
	public JSONObject getAllConsent() {
		JSONObject jsonObjectFinal = new JSONObject();
		ArrayList<Consent> consentList = null;
		String sql = "SELECT * FROM CS3205.consent";
		try {
			Connection connect = MySQLAccess.connectDatabase();
			PreparedStatement preparedStatement = connect.prepareStatement(sql);
			String statement = preparedStatement.toString();
			consentList = resultSetToConsentList(MySQLAccess.readDataBasePS(preparedStatement));
			Logger.log(Logger.API.TEAM1.name(), Logger.TYPE.READ.name(), statement, consentList.size() == 0 ? 0 : 1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		JSONArray consentArray = new JSONArray();
		for(Consent consent : consentList) {
			JSONObject jsonObjectTreatment = buildConsentObject(consent);
			consentArray.put(jsonObjectTreatment);
		}
		MySQLAccess.close();

		jsonObjectFinal.put("consents", consentArray);
		return jsonObjectFinal;
	}
	
	/*
	 * This method will take in a Consent id and 
	 * return the Consent object from the database.
	 * 
	 * @param therapistid
	 * 		status
	 * 
	 * @return JSONObject containing the result
	 * 		  null if empty or error
	 */
	public JSONObject getConsentWithId(int id) {
		JSONObject jsonObject = new JSONObject();
		ArrayList<Consent> consentList = null;

		String sql = "SELECT * FROM CS3205.consent WHERE consent_id = ? ";
		try {
			Connection connect = MySQLAccess.connectDatabase();
			PreparedStatement preparedStatement = connect.prepareStatement(sql);
			preparedStatement.setInt(1, id);
			String statement = preparedStatement.toString();
			consentList = resultSetToConsentList(MySQLAccess.readDataBasePS(preparedStatement));
			Logger.log(Logger.API.TEAM1.name(), Logger.TYPE.READ.name(), statement, 1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		if(consentList.size() < 1) {
			return null;
		}
		MySQLAccess.close();
		
		Consent consent = consentList.get(0);
		jsonObject = buildConsentObject(consent);
		return jsonObject;
	}
	

	/*
	 * This method will take in a therapist id and a status, and 
	 * return the Consent object from the database.
	 * 
	 * @param therapistid
	 * 		status
	 * 
	 * @return JSONObject containing the result
	 * 		  null if empty or error
	 */
	public JSONObject getConsentWithUid(int therapistid, boolean status) {
		JSONObject jsonObject = new JSONObject();
		ArrayList<Consent> consentList = null;

		String sql = "SELECT * FROM CS3205.consent WHERE uid = ? AND status = ? ";
		try {
			Connection connect = MySQLAccess.connectDatabase();
			PreparedStatement preparedStatement = connect.prepareStatement(sql);
			preparedStatement.setInt(1, therapistid);
			preparedStatement.setInt(2, status ? 1 : 0);
			String statement = preparedStatement.toString();
			consentList = resultSetToConsentList(MySQLAccess.readDataBasePS(preparedStatement));
			Logger.log(Logger.API.TEAM1.name(), Logger.TYPE.READ.name(), statement, consentList.size() == 0 ? 0 : 1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		if(consentList.size() < 1) {
			return null;
		}
		JSONArray consentArray = new JSONArray();
		for(Consent consent : consentList) {
			JSONObject jsonObjectTreatment = buildConsentObject(consent);
			consentArray.put(jsonObjectTreatment);
		}
		MySQLAccess.close();
		
		jsonObject.put("consents", consentArray);
		//System.out.println("Retrieving details of Consent: " + id);
		return jsonObject;
	}
	
	/*
	 * This method will take in a therapist id, and return the consent object from the database.
	 * 
	 * @param therapistid
	 * 
	 * @return JSONObject containing the result
	 * 		  null if empty or error
	 */
	public JSONObject getConsentWithUid(int therapistid) {
		JSONObject jsonObject = new JSONObject();
		ArrayList<Consent> consentList = null;

		String sql = "SELECT * FROM CS3205.consent WHERE uid = ?";
		try {
			Connection connect = MySQLAccess.connectDatabase();
			PreparedStatement preparedStatement = connect.prepareStatement(sql);
			preparedStatement.setInt(1, therapistid);
			String statement = preparedStatement.toString();
			consentList = resultSetToConsentList(MySQLAccess.readDataBasePS(preparedStatement));
			Logger.log(Logger.API.TEAM1.name(), Logger.TYPE.READ.name(), statement, consentList.size() == 0 ? 0 : 1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		if(consentList.size() < 1) {
			return null;
		}
		JSONArray consentArray = new JSONArray();
		for(Consent consent : consentList) {
			JSONObject jsonObjectTreatment = buildConsentObject(consent);
			consentArray.put(jsonObjectTreatment);
		}
		MySQLAccess.close();

		jsonObject.put("consents", consentArray);
		//System.out.println("Retrieving details of Consent: " + id);
		return jsonObject;
	}
	
	/*
	 * This method will take in a record id, and return the consent object from the database.
	 * 
	 * @param rid
	 * 
	 * @return JSONObject containing the result
	 * 		  null if empty or error
	 */
	public JSONObject getConsentWithRid(int rid) {
		JSONObject jsonObject = new JSONObject();
		JSONArray consentArray = null;

		String sql = "SELECT c.consent_id, c.uid, u.firstname, u.lastname, c.status FROM CS3205.consent c INNER JOIN CS3205.user u ON c.uid = u.uid WHERE rid = ? ";
		try {
			Connection connect = MySQLAccess.connectDatabase();
			PreparedStatement preparedStatement = connect.prepareStatement(sql);
			preparedStatement.setInt(1, rid);
			String statement = preparedStatement.toString();
			consentArray = processTherapistList(MySQLAccess.readDataBasePS(preparedStatement));
			Logger.log(Logger.API.TEAM1.name(), Logger.TYPE.READ.name(), statement, 1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		if(consentArray == null) {
			return null;
		}
		MySQLAccess.close();
		
		jsonObject.put("consents", consentArray);
		//System.out.println("Retrieving details of Consent: " + id);
		return jsonObject;
	}

	/*
	 * This method will take in a patient id and a therapist id, and 
	 * return the Consent object from the database. It will join the consent table
	 * and data table and user table together to get all the values needed.
	 * 
	 * @param patientid
	 * 	      therapistId
	 * 
	 * @return JSONObject containing the result
	 * 		  null if empty or error
	 */
	public JSONObject getConsentWithUidAndTherapistId(int patientid, int therapistId) {
		JSONObject jsonObject = new JSONObject();
		JSONArray consentArray = null;

		String sql = "SELECT * FROM CS3205.consent c INNER JOIN CS3205.data d ON c.rid = d.rid INNER JOIN CS3205.user u ON d.uid = u.uid "
				+ "WHERE d.uid = ? AND c.uid = ? ";
		try {
			Connection connect = MySQLAccess.connectDatabase();
			PreparedStatement preparedStatement = connect.prepareStatement(sql);
			preparedStatement.setInt(1, patientid);
			preparedStatement.setInt(2, therapistId);
			String statement = preparedStatement.toString();
			consentArray = processUidWithTherapistIdList(MySQLAccess.readDataBasePS(preparedStatement));
			Logger.log(Logger.API.TEAM1.name(), Logger.TYPE.READ.name(), statement, consentArray.length() == 0 ? 0 : 1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		if(consentArray.length() == 0) {
			return null;
		}
		MySQLAccess.close();
		
		jsonObject.put("consents", consentArray);
		return jsonObject;
	}
	
	/*
	 * This method will create a consent object for the given uid and rid.
	 * 
	 * @param uid
	 * 		  rid
	 * 
	 * @return JSONobject containing 1 if success.
	 * 								 0 if failed.
	 * 		   null if empty or error
	 */
	public JSONObject createConsent(int uid, int rid) {
		Consent consent = new Consent(uid, rid, false);
		JSONObject jsonObject = new JSONObject();
		int result = 0;
		String sql = "INSERT INTO CS3205.consent VALUES (default, ?, ?, ?)";
		try {
			Connection connect = MySQLAccess.connectDatabase();
			PreparedStatement preparedStatement = connect.prepareStatement(sql);
			preparedStatement.setInt(1, consent.getUid());
			preparedStatement.setInt(2, consent.getRid());
			preparedStatement.setInt(3, consent.isStatus() ? 1: 0);
			String statement = preparedStatement.toString();
			result = MySQLAccess.updateDataBasePS(preparedStatement);
			Logger.log(Logger.API.TEAM1.name(), Logger.TYPE.WRITE.name(), statement, result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		MySQLAccess.close();
		System.out.println("Created consent: " + uid + " & " + rid);
		jsonObject.put("result", result);
		return jsonObject;
	}

	/*
	 * This method takes in a consent id and will check if the consent exists.
	 * If it exists, the consent values will be used and then it will set the consent status 
	 * to toggle between true and false upon the execution of the code. True set to false. 
	 * False set to true.
	 * 
	 * @param id of consent
	 * @return JSONObject containing 1 if success
	 * 								 0 if failed
	 * 		   null if empty or error
	 */
	public JSONObject updateConsent(int id) {	
		JSONObject jsonObject = new JSONObject();
		int result = 0;
		
		ArrayList<Consent> consentList = null;
		String sql = "SELECT * FROM CS3205.consent WHERE consent_id = ? ";
		try {
			Connection connect = MySQLAccess.connectDatabase();
			PreparedStatement preparedStatement = connect.prepareStatement(sql);
			preparedStatement.setInt(1, id);
			consentList = resultSetToConsentList(MySQLAccess.readDataBasePS(preparedStatement));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		if(consentList.size() < 1) {
			return null;
		}
		
		Consent consent = consentList.get(0);
		
		String sql2 = "UPDATE CS3205.consent SET status = ? WHERE consent_id = ?";
		try {
			Connection connect = MySQLAccess.connectDatabase();
			PreparedStatement preparedStatement = connect.prepareStatement(sql2);
			preparedStatement.setInt(1, consent.isStatus() ? 0 : 1);
			preparedStatement.setInt(2, id);
			String statement = preparedStatement.toString();
			result = MySQLAccess.updateDataBasePS(preparedStatement);
			Logger.log(Logger.API.TEAM1.name(), Logger.TYPE.WRITE.name(), statement, result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		MySQLAccess.close();

		System.out.println("Updated consent: " + id);
		jsonObject.put("result", result);
		return jsonObject;
	}
	
	/*
	 * This method deletes the consent's entry on the database based on the consent id.
	 * 
	 * @param id of the consent
	 * 
	 * @return JSONObject contained the result of the operation. 1 is success.
	 * 															 0 is failed.
	 * 		   null if error
	 */
	public JSONObject deleteConsent(int id) {
		int result = 0;
		JSONObject jsonObject = new JSONObject();
		String sql = "DELETE FROM CS3205.consent where consent_id = ?";
		
		System.out.println("Deleting consent: " + id);
		try {
			Connection connect = MySQLAccess.connectDatabase();
			PreparedStatement preparedStatement = connect.prepareStatement(sql);
		    preparedStatement.setInt(1, id);
		    String statement = preparedStatement.toString();
			result = MySQLAccess.updateDataBasePS(preparedStatement);
			Logger.log(Logger.API.TEAM1.name(), Logger.TYPE.WRITE.name(), statement, result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		MySQLAccess.close();
		Logger.log(Logger.API.TEAM1.name(), Logger.TYPE.WRITE.name(), sql, result);
		jsonObject.put("result", result);
		return jsonObject;
	}
	
	/*
	 * This method will check a user's uid to see if the user has all necessary
	 * consents required for the data, if it is a document, then it will check for
	 * all included data. 
	 * 
	 * @param uid of user
	 * 		  rid of the data
	 * 
	 * @return JSONObject of the result. True if have all consents needed.
	 * 									 False if does not have all consents.
	 *         null if empty or error
	 */
	public JSONObject checkUserAccessToData(int uid, int rid) {
		JSONObject jsonObject = new JSONObject();
		ResultSet resultSet = null;
		boolean result = false;
		SessionController sc = new SessionController();
		Data data = sc.get(rid);
		if(data == null){
			return null;
		}
		
		//Check for owner
		if(data.getUid() == uid) {
			//This checks if it is a document
			if(data.getSubtype() != null) {
				if(data.getSubtype().equalsIgnoreCase("document")) {
					DocumentController dc = new DocumentController();
					jsonObject = dc.checkUserAccessToAllRecordsInDoc(uid, rid);
					return jsonObject;
				}
			}
			//This is for non documents.
			result = true;
			jsonObject.put("result", result);
			return jsonObject;
		}
		
		//Not owner. Check consent.
		String sql = "SELECT * FROM CS3205.consent WHERE uid = ? AND rid = ? ";
		try {
			Connection connect = MySQLAccess.connectDatabase();
			PreparedStatement preparedStatement = connect.prepareStatement(sql);
			preparedStatement.setInt(1, uid);
			preparedStatement.setInt(1, rid);
			String statement = preparedStatement.toString();
			resultSet = MySQLAccess.readDataBasePS(preparedStatement);
			while (resultSet.next()) {
				result = true;
			}
			Logger.log(Logger.API.TEAM1.name(), Logger.TYPE.READ.name(), statement, result ? 1 : 0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		MySQLAccess.close();
		jsonObject.put("result", result);
		
		
		return jsonObject;
	}
	
	/*
	 * This method will take in a result set of a SQL operation and prepares a
	 * list of Consent object with the corresponding fields from the result set
	 * 
	 * @param result of SQL query
	 * @return ArrayList of Consent objects
	 * 
	 */
	private ArrayList<Consent> resultSetToConsentList(ResultSet resultSet) throws SQLException {
		// ResultSet is initially before the first data set
		ArrayList<Consent> consentList = new ArrayList<Consent>();
		while (resultSet.next()) {
			int id = resultSet.getInt("consent_id");
			int patientId = resultSet.getInt("uid");
			int therapistId = resultSet.getInt("rid");
			boolean status = (resultSet.getInt("status")==1) ? true : false;
			Consent consent = new Consent(id, patientId, therapistId, status);
			consentList.add(consent);
		}
		MySQLAccess.close();
		return consentList;
	}
	
	/*
	 * This method will take in a consent object and build a json object containing it.
	 * 
	 * @param result of SQL query
	 * @return JSONObject of Consent object
	 * 
	 */
	private JSONObject buildConsentObject(Consent consent) {
		JSONObject jsonObjectConsent = new JSONObject();
		jsonObjectConsent.put("consentId", consent.getConsentId()); 
		jsonObjectConsent.put("uid", consent.getUid());
		jsonObjectConsent.put("rid", consent.getRid());
		jsonObjectConsent.put("status", consent.isStatus());

		return jsonObjectConsent;
	}
	
	/*
	 * This method will take in a result set of a SQL operation and prepares a
	 * JSONObject with all required fields from the corresponding fields
	 * from the result set of the query
	 * 
	 * @param result of SQL query
	 * @return JSONArray of JSONObjects
	 * 		   null if empty
	 * 
	 */
	private JSONArray processTherapistList(ResultSet resultSet) throws SQLException {
		// ResultSet is initially before the first data set
		JSONArray result = new JSONArray();
		boolean empty = true;
		while (resultSet.next()) {
			JSONObject jsonObjectConsent = new JSONObject();
			jsonObjectConsent.put("consentId", resultSet.getInt("consent_id")); 
			jsonObjectConsent.put("uid", resultSet.getInt("uid"));
			jsonObjectConsent.put("firstname", resultSet.getString("firstname"));
			jsonObjectConsent.put("lastname", resultSet.getString("lastname"));
			jsonObjectConsent.put("status", (resultSet.getInt("status")==1) ? true : false);
			result.put(jsonObjectConsent);
			empty = false;
		}
		MySQLAccess.close();
		if(empty) {
			return null;
		}
		return result;
	}
	
	/*
	 * This method will take in a result set of a SQL operation and prepares a
	 * JSONObject with all required fields from the corresponding fields
	 * from the result set of the query
	 * 
	 * @param result of SQL query
	 * @return JSONArray of JSONObjects	 
	 *  	   null if empty
	 * 
	 */
	private JSONArray processUidWithTherapistIdList(ResultSet resultSet) throws SQLException {
		// ResultSet is initially before the first data set
		JSONArray result = new JSONArray();
		boolean empty = true;
		while (resultSet.next()) {
			JSONObject jsonObjectConsent = new JSONObject();
			jsonObjectConsent.put("consentId", resultSet.getInt("consent_id")); 
			jsonObjectConsent.put("uid", resultSet.getInt("c.uid"));
			jsonObjectConsent.put("rid", resultSet.getInt("rid"));
			jsonObjectConsent.put("owner_firstname", resultSet.getString("firstname"));
			jsonObjectConsent.put("owner_lastname", resultSet.getString("lastname"));
			jsonObjectConsent.put("status", (resultSet.getInt("status")==1) ? true : false);
			jsonObjectConsent.put("title", resultSet.getString("title"));
			jsonObjectConsent.put("modifieddate", resultSet.getTimestamp("modifieddate"));
			result.put(jsonObjectConsent);
			empty = false;
		}
		MySQLAccess.close();
		if(empty) {
			return null;
		}
		return result;
	}
}
