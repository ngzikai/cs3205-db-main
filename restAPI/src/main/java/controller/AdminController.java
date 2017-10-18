package controller;

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
	
	public JSONObject getAllAdmin() {
		JSONObject jsonObjectFinal = new JSONObject();
		ArrayList<Admin> adminList = null;
		String sql = "SELECT * FROM CS3205.admin";
		try {
			Connection connect = MySQLAccess.connectDatabase();
			PreparedStatement preparedStatement = connect.prepareStatement(sql);
			adminList = resultSetToAdminList(MySQLAccess.readDataBasePS(preparedStatement));
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
	
	public JSONObject getAdmin(String username) {
		JSONObject jsonObject = new JSONObject();
		ArrayList<Admin> adminList = null;
		String sql = "SELECT * FROM CS3205.admin WHERE username = ? ";
		try {
			Connection connect = MySQLAccess.connectDatabase();
			PreparedStatement preparedStatement = connect.prepareStatement(sql);
			preparedStatement.setString(1, username);
			adminList = resultSetToAdminList(MySQLAccess.readDataBasePS(preparedStatement));
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
			result = MySQLAccess.updateDataBasePS(preparedStatement);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		MySQLAccess.close();
		jsonObject.put("result", result);
		return jsonObject;
	}
	
	public JSONObject adminGetSecret(int adminId) {
		JSONObject jsonObject = new JSONObject();
		String sql = "SELECT secret FROM CS3205.admin where admin_id = ?";
		
		System.out.println("Getting secret from admin: " + adminId);
		try {
			Connection connect = MySQLAccess.connectDatabase();
			PreparedStatement preparedStatement = connect.prepareStatement(sql);
			preparedStatement.setInt(1, adminId);
			ResultSet resultSet = MySQLAccess.readDataBasePS(preparedStatement);
			while (resultSet.next()) {
				jsonObject.put("secret", resultSet.getString("secret")); 
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		MySQLAccess.close();
		return jsonObject;
	}
}
