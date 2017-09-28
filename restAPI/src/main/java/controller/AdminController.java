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
		}
		JSONArray adminArray = new JSONArray();
		for(Admin admin : adminList) {
			JSONObject jsonObjectAdmin = new JSONObject();
			jsonObjectAdmin.put("admin_id", admin.getAdminId()); 
			jsonObjectAdmin.put("username", admin.getUsername());
			jsonObjectAdmin.put("password", admin.getPassword());
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
		}
		if(adminList.size() < 1) {
			return null;
		}
		Admin admin = adminList.get(0);
		jsonObject.put("admin_id", admin.getAdminId()); 
		jsonObject.put("username", admin.getUsername());
		jsonObject.put("password", admin.getPassword());
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

			Admin admin = new Admin(id, username, password);
			adminList.add(admin);
		}
		MySQLAccess.close();
		return adminList;
	}
}
