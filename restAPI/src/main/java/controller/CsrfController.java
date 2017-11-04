package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import entity.Csrf;
import utils.Logger;
import utils.db.MySQLAccess;

public class CsrfController {
	
	/*
	 * This method will get all csrfs' value from the database and return them.
	 * 
	 * @return JSONObject containing the result
	 * 		  null if empty or error
	 */
	public JSONObject getAllCsrf() {
		JSONObject jsonObjectFinal = new JSONObject();
		ArrayList<Csrf> csrfList = null;
		String sql = "SELECT * FROM CS3205.csrf";
		try {
			Connection connect = MySQLAccess.connectDatabase();
			PreparedStatement preparedStatement = connect.prepareStatement(sql);
			String statement = preparedStatement.toString();
			csrfList = resultSetToCsrfList(MySQLAccess.readDataBasePS(preparedStatement));
			Logger.log(Logger.API.TEAM1.name(), Logger.TYPE.READ.name(), statement, csrfList.size() == 0 ? 0 : 1);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		JSONArray csrfArray = new JSONArray();
		for(Csrf csrf : csrfList) {
			JSONObject jsonObjectCsrf = buildCsrfObject(csrf);
			csrfArray.put(jsonObjectCsrf);
		}
		MySQLAccess.close();
		jsonObjectFinal.put("csrf", csrfArray);
		return jsonObjectFinal;
	}


	/*
	 * This method will take in a csrfToken and 
	 * return the csrf object from the database.
	 * 
	 * @param csrfToken
	 * 
	 * @return JSONObject containing the result
	 * 		  null if empty or error
	 */
	public JSONObject getCsrfWithToken(String csrfToken) {
		JSONObject jsonObject = new JSONObject();
		ArrayList<Csrf> csrfList = null;

		String sql = "SELECT * FROM CS3205.csrf WHERE csrf_token = ? ";
		try {
			Connection connect = MySQLAccess.connectDatabase();
			PreparedStatement preparedStatement = connect.prepareStatement(sql);
			preparedStatement.setString(1, csrfToken);
			String statement = preparedStatement.toString();
			csrfList = resultSetToCsrfList(MySQLAccess.readDataBasePS(preparedStatement));
			Logger.log(Logger.API.TEAM1.name(), Logger.TYPE.READ.name(), statement, 1);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		if(csrfList.size() < 1) {
			return null;
		}
		MySQLAccess.close();
		
		Csrf otl = csrfList.get(0);
		jsonObject = buildCsrfObject(otl);
		System.out.println("Retrieving details of Csrf Token: " + csrfToken);
		return jsonObject;
	}

	/*
	 * This method will create a CSRF object with the necessary variables to be stored in the database. 
	 * 
	 * @param csrfToken
	 * 		  uid
	 * 		  expiry
	 * 		  description
	 * 
	 * @return JSONobject containing 1 if success.
	 * 								 0 if failed.
	 * 		   null if empty or error
	 */
	public JSONObject createCsrf(String csrfToken, int uid, int expiry, String description) {
		Csrf csrf = new Csrf(csrfToken, uid, expiry, description);
		JSONObject jsonObject = new JSONObject();
		int result = 0;
		String sql = "INSERT INTO CS3205.csrf VALUES (?, ?, ?, ?)";
		try {
			Connection connect = MySQLAccess.connectDatabase();
			PreparedStatement preparedStatement = connect.prepareStatement(sql);
			preparedStatement.setString(1, csrf.getCsrfToken());
			preparedStatement.setInt(2, csrf.getUid());
			preparedStatement.setInt(3, csrf.getExpiry());
			preparedStatement.setString(4, csrf.getDescription());
			String statement = preparedStatement.toString();
			result = MySQLAccess.updateDataBasePS(preparedStatement);
			Logger.log(Logger.API.TEAM1.name(), Logger.TYPE.WRITE.name(), statement, result);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		MySQLAccess.close();

		System.out.println("Created One Time Link: " + csrf.toString());
		jsonObject.put("result", result);
		return jsonObject;
	}

	/*
	 * This method takes in a csrf token and an int of expiry date where the csrf will be set
	 * to the entry corresponding csrf token in the database.
	 * 
	 * @param csrfToken
	 * 		  expiry
	 * @return JSONObject containing 1 if success
	 * 								 0 if failed
	 * 		   null if empty or error
	 */
	public JSONObject updateCSRF(String csrfToken, int expiry) {

		JSONObject jsonObject = new JSONObject();
		int result = 0;
		String sql = "UPDATE CS3205.csrf SET expiry = ? WHERE csrf_token = ?";
		try {
			Connection connect = MySQLAccess.connectDatabase();
			PreparedStatement preparedStatement = connect.prepareStatement(sql);
			preparedStatement.setInt(1, expiry);
			preparedStatement.setString(2, csrfToken);
			String statement = preparedStatement.toString();
			result = MySQLAccess.updateDataBasePS(preparedStatement);
			Logger.log(Logger.API.TEAM1.name(), Logger.TYPE.WRITE.name(), statement, result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		MySQLAccess.close();

		System.out.println("Updated csrf: " + csrfToken + " to new expiry " + expiry);
		jsonObject.put("result", result);
		return jsonObject;
	}

	/*
	 * This method deletes the csrf entry on the database based on the csrf token.
	 * 
	 * @param csrfToken
	 * 
	 * @return JSONObject contained the result of the operation. 1 is success.
	 * 															 0 is failed.
	 * 		   null if error
	 */
	public JSONObject deleteCsrf(String csrfToken) {
		
		int result = 0;
		JSONObject jsonObject = new JSONObject();
		String sql = "DELETE FROM CS3205.csrf where csrf_token = ?";

		System.out.println("Deleting csrf token: " + csrfToken);
		try {
			Connection connect = MySQLAccess.connectDatabase();
			PreparedStatement preparedStatement = connect.prepareStatement(sql);
			preparedStatement.setString(1, csrfToken);
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
	 * list of Csrf object with the corresponding fields from the result set
	 * 
	 * @param result of SQL query
	 * @return ArrayList of Csrf objects
	 * 
	 */
	private ArrayList<Csrf> resultSetToCsrfList(ResultSet resultSet) throws SQLException {
		// ResultSet is initially before the first data set
		ArrayList<Csrf> csrfList = new ArrayList<Csrf>();
		while (resultSet.next()) {
			String csrfToken = resultSet.getString("csrf_token");
			int uid = resultSet.getInt("uid");
			int expiry = resultSet.getInt("expiry");
			String description = resultSet.getString("description");
			Csrf csrf = new Csrf(csrfToken, uid, expiry, description);
			csrfList.add(csrf);
		}
		MySQLAccess.close();
		return csrfList;
	}

	/*
	 * This method will take in a csrf object and build a json object containing it.
	 * 
	 * @param result of SQL query
	 * @return JSONObject of Csrf object
	 * 
	 */
	private JSONObject buildCsrfObject(Csrf csrf) {
		JSONObject jsonObjectCsrf = new JSONObject();
		jsonObjectCsrf.put("csrfToken", csrf.getCsrfToken()); 
		jsonObjectCsrf.put("uid", csrf.getUid());
		jsonObjectCsrf.put("expiry", csrf.getExpiry());
		jsonObjectCsrf.put("description", csrf.getDescription());
		return jsonObjectCsrf;
	}
}
