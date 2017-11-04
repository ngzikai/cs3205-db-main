package controller;

import utils.Logger;
import utils.db.MySQLAccess;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import entity.Admin;

import java.sql.Connection;

public class AdminController {
	
	/*
	 * This method will get all admins' value from the database and return them.
	 * 
	 * @return JSONObject containing the result
	 * 		  null if empty or error
	 */
	public JSONObject getAllAdmin() {
		JSONObject jsonObjectFinal = new JSONObject();
		ArrayList<Admin> adminList = null;
		String sql = "SELECT * FROM CS3205.admin";
		try {
			Connection connect = MySQLAccess.connectDatabase();
			PreparedStatement preparedStatement = connect.prepareStatement(sql);
			String statement = preparedStatement.toString();
			adminList = resultSetToAdminList(MySQLAccess.readDataBasePS(preparedStatement));
			Logger.log(Logger.API.TEAM1.name(), Logger.TYPE.READ.name(), statement, adminList.size() == 0 ? 0 : 1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		JSONArray adminArray = new JSONArray();
		for(Admin admin : adminList) {
			JSONObject jsonObjectAdmin = new JSONObject();
			jsonObjectAdmin.put("admin_id", admin.getAdminId()); 
			jsonObjectAdmin.put("username", admin.getUsername());
			jsonObjectAdmin.put("password", admin.getPassword());
			jsonObjectAdmin.put("salt", admin.getSalt());
			jsonObjectAdmin.put("secret", admin.getSecret());
			adminArray.put(jsonObjectAdmin);
		}

		jsonObjectFinal.put("admins", adminArray);
		return jsonObjectFinal;
	}

	/*
	 * This method will take in a username of an admin and 
	 * return the Admin object from the database.
	 * 
	 * @param username
	 * 
	 * @return JSONObject containing the result
	 * 		  null if empty or error
	 */
	public JSONObject getAdmin(String username) {
		JSONObject jsonObject = new JSONObject();
		ArrayList<Admin> adminList = null;
		String sql = "SELECT * FROM CS3205.admin WHERE username = ? ";
		try {
			Connection connect = MySQLAccess.connectDatabase();
			PreparedStatement preparedStatement = connect.prepareStatement(sql);
			preparedStatement.setString(1, username);
			String statement = preparedStatement.toString();
			adminList = resultSetToAdminList(MySQLAccess.readDataBasePS(preparedStatement));
			Logger.log(Logger.API.TEAM1.name(), Logger.TYPE.READ.name(), statement, 1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		if(adminList.size() < 1) {
			return null;
		}
		Admin admin = adminList.get(0);
		jsonObject.put("admin_id", admin.getAdminId()); 
		jsonObject.put("username", admin.getUsername());
		jsonObject.put("password", admin.getPassword());
		jsonObject.put("salt", admin.getSalt());
		jsonObject.put("secret", admin.getSecret());
		admin.print();
		return jsonObject;
	}
	
	/*
	 * This method will take in a result set of a SQL operation and prepares a
	 * list of Admin object with the corresponding fields from the result set
	 * 
	 * @param result of SQL query
	 * @return ArrayList of Admin objects
	 * 
	 */
	private ArrayList<Admin> resultSetToAdminList(ResultSet resultSet) throws SQLException {
		// ResultSet is initially before the first data set
		ArrayList<Admin> adminList = new ArrayList<Admin>();
		while (resultSet.next()) {
			int id = resultSet.getInt("admin_id");
			String username = resultSet.getString("username");
			String password = resultSet.getString("password");
			String salt = resultSet.getString("salt");
			String secret  = resultSet.getString("secret");
			Admin admin = new Admin(id, username, password, salt, secret);
			adminList.add(admin);
		}
		MySQLAccess.close();
		return adminList;
	}
	
	/*
	 * This method takes in an admin id and a string secret where the secret will be set
	 * to the entry corresponding admin id in the database.
	 * 
	 * @param id of admin
	 * 		  secret string
	 * @return JSONObject containing 1 if success
	 * 								 0 if failed
	 * 		   null if empty or error
	 */
	public JSONObject adminSetSecret(int adminId, String secret) {
		int result = 0;
		JSONObject jsonObject = new JSONObject();
		String sql = "UPDATE CS3205.admin SET secret = ? where admin_id = ?";
		
		System.out.println("Setting secret for admin: " + adminId);
		try {
			Connection connect = MySQLAccess.connectDatabase();
			PreparedStatement preparedStatement = connect.prepareStatement(sql);
			preparedStatement.setString(1, secret);
		    preparedStatement.setInt(2, adminId);
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
	 * This method will take in an admin id and return the secret value
	 * that is associated with the respective admin id.
	 * 
	 * @param adminId
	 * 
	 * @return JSONObject containing the result
	 * 		  null if empty or error
	 */
	public JSONObject adminGetSecret(int adminId) {
		JSONObject jsonObject = new JSONObject();
		String sql = "SELECT secret FROM CS3205.admin where admin_id = ?";
		
		System.out.println("Getting secret from admin: " + adminId);
		try {
			Connection connect = MySQLAccess.connectDatabase();
			PreparedStatement preparedStatement = connect.prepareStatement(sql);
			preparedStatement.setInt(1, adminId);
			String statement = preparedStatement.toString();
			ResultSet resultSet = MySQLAccess.readDataBasePS(preparedStatement);
			while (resultSet.next()) {
				jsonObject.put("secret", resultSet.getString("secret")); 
			}
			Logger.log(Logger.API.TEAM1.name(), Logger.TYPE.READ.name(), statement, jsonObject.length() == 0 ? 0 : 1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		MySQLAccess.close();

		return jsonObject;
	}
}
