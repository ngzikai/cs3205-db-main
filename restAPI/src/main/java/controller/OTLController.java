package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import entity.OneTimeLink;
import utils.Logger;
import utils.db.MySQLAccess;

public class OTLController {

	/*
	 * This method will get all One time links' value from the database and return them.
	 * 
	 * @return JSONObject containing the result
	 * 		  null if empty or error
	 */
	public JSONObject getAllOTL() {
		JSONObject jsonObjectFinal = new JSONObject();
		ArrayList<OneTimeLink> otlList = null;
		String sql = "SELECT * FROM CS3205.one_time_link";
		try {
			Connection connect = MySQLAccess.connectDatabase();
			PreparedStatement preparedStatement = connect.prepareStatement(sql);
			String statement = preparedStatement.toString();
			otlList = resultSetToOtlList(MySQLAccess.readDataBasePS(preparedStatement));
			Logger.log(Logger.API.TEAM1.name(), Logger.TYPE.READ.name(), statement, otlList.size() == 0 ? 0 : 1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		JSONArray otlArray = new JSONArray();
		for(OneTimeLink otl : otlList) {
			JSONObject jsonObjectOTL = buildTreatmentObject(otl);
			otlArray.put(jsonObjectOTL);
		}
		MySQLAccess.close();
		jsonObjectFinal.put("one_time_links", otlArray);
		return jsonObjectFinal;
	}
	
	
	/*
	 * This method will take in a token and 
	 * return the one time link object from the database.
	 * 
	 * @param token
	 * 
	 * @return JSONObject containing the result
	 * 		  null if empty or error
	 */
	public JSONObject getOTLWithToken(String token) {
		JSONObject jsonObject = new JSONObject();
		ArrayList<OneTimeLink> otlList = null;

		String sql = "SELECT * FROM CS3205.one_time_link WHERE token = ? ";
		try {
			Connection connect = MySQLAccess.connectDatabase();
			PreparedStatement preparedStatement = connect.prepareStatement(sql);
			preparedStatement.setString(1, token);
			String statement = preparedStatement.toString();
			otlList = resultSetToOtlList(MySQLAccess.readDataBasePS(preparedStatement));
			Logger.log(Logger.API.TEAM1.name(), Logger.TYPE.READ.name(), statement, otlList.size() == 0 ? 0 : 1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		if(otlList.size() < 1) {
			return null;
		}
		MySQLAccess.close();
		OneTimeLink otl = otlList.get(0);
		jsonObject = buildTreatmentObject(otl);
		System.out.println("Retrieving details of One Time Link: " + token);
		return jsonObject;
	}

	/*
	 * This method will create a One Time Link object with the necessary variables to be stored in the database. 
	 * 
	 * @param token
	 * 		  uid
	 * 		  filepath
	 * 		  csrf
	 * 		  dataType
	 * 
	 * @return JSONobject containing 1 if success.
	 * 								 0 if failed.
	 * 		   null if empty or error
	 */
	public JSONObject createOTL(String token, int uid, String filepath, String csrf, String dataType) {
		OneTimeLink otl = new OneTimeLink(token, filepath, csrf, uid, dataType);
		JSONObject jsonObject = createOTL(otl);
		return jsonObject;
	}
	
	/*
	 * This method will create a One Time Link object with the necessary variables to be stored in the database. 
	 * 
	 * @param onetimelink object
	 * 
	 * @return JSONobject containing 1 if success.
	 * 								 0 if failed.
	 * 		   null if empty or error
	 */
	public JSONObject createOTL(OneTimeLink otl) {
		JSONObject jsonObject = new JSONObject();
		int result = 0;
		String sql = "INSERT INTO CS3205.one_time_link VALUES (?, ?, ?, ?, ?)";
		try {
			Connection connect = MySQLAccess.connectDatabase();
			PreparedStatement preparedStatement = connect.prepareStatement(sql);
			preparedStatement.setString(1, otl.getToken());
			preparedStatement.setInt(2, otl.getUid());
			preparedStatement.setString(3, otl.getFilepath());
			preparedStatement.setString(4, otl.getCsrf());
			preparedStatement.setString(5, otl.getDataType());
			String statement = preparedStatement.toString();
			result = MySQLAccess.updateDataBasePS(preparedStatement);
			Logger.log(Logger.API.TEAM1.name(), Logger.TYPE.WRITE.name(), statement, result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		MySQLAccess.close();
		System.out.println("Created One Time Link: " + otl.toString());
		jsonObject.put("result", result);
		return jsonObject;
	}

	/*
	 * This method takes in a token and a csrf token where the one time link will be set
	 * to the entry corresponding csrf token in the database.
	 * 
	 * @param token
	 * 		  csrf
	 * @return JSONObject containing 1 if success
	 * 								 0 if failed
	 * 		   null if empty or error
	 */
	public JSONObject updateCSRF(String token, String csrf) {
		
		JSONObject jsonObject = new JSONObject();
		int result = 0;
		String sql = "UPDATE CS3205.one_time_link SET csrf = ? WHERE token = ?";
		try {
			Connection connect = MySQLAccess.connectDatabase();
			PreparedStatement preparedStatement = connect.prepareStatement(sql);
			preparedStatement.setString(1, csrf);
			preparedStatement.setString(2, token);
			String statement = preparedStatement.toString();
			result = MySQLAccess.updateDataBasePS(preparedStatement);
			Logger.log(Logger.API.TEAM1.name(), Logger.TYPE.WRITE.name(), statement, result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		MySQLAccess.close();
		System.out.println("Updated OTL: " + token + " to " + csrf);
		jsonObject.put("result", result);
		return jsonObject;
	}
	
	/*
	 * This method deletes the one time link entry on the database based on the token.
	 * 
	 * @param token
	 * 
	 * @return JSONObject contained the result of the operation. 1 is success.
	 * 															 0 is failed.
	 * 		   null if error
	 */
	public JSONObject deleteOTL(String token) {
		int result = 0;
		JSONObject jsonObject = new JSONObject();
		String sql = "DELETE FROM CS3205.one_time_link where token = ?";
		
		System.out.println("Deleting one time link of token: " + token);
		try {
			Connection connect = MySQLAccess.connectDatabase();
			PreparedStatement preparedStatement = connect.prepareStatement(sql);
		    preparedStatement.setString(1, token);
		    String statement = preparedStatement.toString();
			result = MySQLAccess.updateDataBasePS(preparedStatement);
			Logger.log(Logger.API.TEAM1.name(), Logger.TYPE.WRITE.name(), statement, result);
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
	 * list of one time link object with the corresponding fields from the result set
	 * 
	 * @param result of SQL query
	 * @return ArrayList of Otl objects
	 * 
	 */
	private ArrayList<OneTimeLink> resultSetToOtlList(ResultSet resultSet) throws SQLException {
		// ResultSet is initially before the first data set
		ArrayList<OneTimeLink> treatmentList = new ArrayList<OneTimeLink>();
		while (resultSet.next()) {
			String token = resultSet.getString("token");
			int uid = resultSet.getInt("uid");
			String filepath = resultSet.getString("filepath");
			String csrf = resultSet.getString("csrf");
			String dataType = resultSet.getString("datatype");
			OneTimeLink otl = new OneTimeLink(token, filepath, csrf, uid, dataType);
			treatmentList.add(otl);
		}
		MySQLAccess.close();
		return treatmentList;
	}
	
	/*
	 * This method will take in a one time link object and build a json object containing it.
	 * 
	 * @param result of SQL query
	 * @return JSONObject of Otl object
	 * 
	 */
	private JSONObject buildTreatmentObject(OneTimeLink otl) {
		JSONObject jsonObjectTreatment = new JSONObject();
		jsonObjectTreatment.put("token", otl.getToken()); 
		jsonObjectTreatment.put("uid", otl.getUid());
		jsonObjectTreatment.put("filepath", otl.getFilepath());
		jsonObjectTreatment.put("csrf", otl.getCsrf());
		jsonObjectTreatment.put("datatype", otl.getDataType());
		return jsonObjectTreatment;
	}
}
