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
	
	
	// This method will take in a Consent id and return the Consent object from the database;
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
	
	// This method will take in a therapist id and a status, and return the Consent object from the database;
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
	
	// This method will take in a therapistid id, and return the consent object from the database;
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
	
	// This method will take in a record id, and return the consent object from the database;
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

	// This method will take in a patient id and a therapist, and return the Consent object from the database;
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
		//System.out.println("Retrieving details of Consent: " + id);
		return jsonObject;
	}
	
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
		
		if(data.getSubtype().equalsIgnoreCase("document")) {
			DocumentController dc = new DocumentController();
			jsonObject = dc.checkUserAccessToAllRecordsInDoc(uid, rid);
			return jsonObject;
		}
		
		String sql = "SELECT * FROM CS3205.consent WHERE uid = ? AND rid = ?";
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
	
	private JSONObject buildConsentObject(Consent consent) {
		JSONObject jsonObjectConsent = new JSONObject();
		jsonObjectConsent.put("consentId", consent.getConsentId()); 
		jsonObjectConsent.put("uid", consent.getUid());
		jsonObjectConsent.put("rid", consent.getRid());
		jsonObjectConsent.put("status", consent.isStatus());

		return jsonObjectConsent;
	}
	
	
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
