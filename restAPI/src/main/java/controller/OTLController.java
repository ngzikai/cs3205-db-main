package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import entity.OneTimeLink;
import utils.db.MySQLAccess;

public class OTLController {

	public JSONObject getAllOTL() {
		JSONObject jsonObjectFinal = new JSONObject();
		ArrayList<OneTimeLink> otlList = null;
		String sql = "SELECT * FROM CS3205.one_time_link";
		try {
			Connection connect = MySQLAccess.connectDatabase();
			PreparedStatement preparedStatement = connect.prepareStatement(sql);
			otlList = resultSetToOtlList(MySQLAccess.readDataBasePS(preparedStatement));
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
	
	
	// This method will take in a treatment name and return the treatment object from the database;
	public JSONObject getOTLWithToken(String token) {
		JSONObject jsonObject = new JSONObject();
		ArrayList<OneTimeLink> otlList = null;

		String sql = "SELECT * FROM CS3205.one_time_link WHERE token = ? ";
		try {
			Connection connect = MySQLAccess.connectDatabase();
			PreparedStatement preparedStatement = connect.prepareStatement(sql);
			preparedStatement.setString(1, token);
			otlList = resultSetToOtlList(MySQLAccess.readDataBasePS(preparedStatement));
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

	public JSONObject createOTL(String token, int uid, String filepath, String csrf) {
		OneTimeLink otl = new OneTimeLink(token, filepath, csrf, uid);
		JSONObject jsonObject = new JSONObject();
		int result = 0;
		String sql = "INSERT INTO CS3205.one_time_link VALUES (?, ?, ?, ?)";
		try {
			Connection connect = MySQLAccess.connectDatabase();
			PreparedStatement preparedStatement = connect.prepareStatement(sql);
			preparedStatement.setString(1, otl.getToken());
			preparedStatement.setInt(2, otl.getUid());
			preparedStatement.setString(3, otl.getFilepath());
			preparedStatement.setString(4, otl.getCsrf());
			result = MySQLAccess.updateDataBasePS(preparedStatement);

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

	public JSONObject updateCSRF(String token, String csrf) {
		
		//Treatment treatment = new Treatment(Integer.parseInt(uid), treatmentname, password, firstName, lastName, nric, LocalDate.parse(dob), gender, phone1,
				//phone2, phone3,  address1, address2, address3, zipcode1, zipcode2, zipcode3, qualify, bloodtype, nfcid);
		JSONObject jsonObject = new JSONObject();
		int result = 0;
		String sql = "UPDATE CS3205.one_time_link SET csrf = ? WHERE token = ?";
		try {
			Connection connect = MySQLAccess.connectDatabase();
			PreparedStatement preparedStatement = connect.prepareStatement(sql);
			preparedStatement.setString(1, csrf);
			preparedStatement.setString(2, token);
			result = MySQLAccess.updateDataBasePS(preparedStatement);
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
	
	
	public JSONObject deleteOTL(String token) {
		int result = 0;
		JSONObject jsonObject = new JSONObject();
		String sql = "DELETE FROM CS3205.one_time_link where token = ?";
		
		System.out.println("Deleting one time link of token: " + token);
		try {
			Connection connect = MySQLAccess.connectDatabase();
			PreparedStatement preparedStatement = connect.prepareStatement(sql);
		    preparedStatement.setString(1, token);
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
	
	private ArrayList<OneTimeLink> resultSetToOtlList(ResultSet resultSet) throws SQLException {
		// ResultSet is initially before the first data set
		ArrayList<OneTimeLink> treatmentList = new ArrayList<OneTimeLink>();
		while (resultSet.next()) {
			String token = resultSet.getString("token");
			int uid = resultSet.getInt("uid");
			String filepath = resultSet.getString("filepath");
			String csrf = resultSet.getString("csrf");
			OneTimeLink otl = new OneTimeLink(token, filepath, csrf, uid);
			treatmentList.add(otl);
		}
		MySQLAccess.close();
		return treatmentList;
	}
	
	private JSONObject buildTreatmentObject(OneTimeLink otl) {
		JSONObject jsonObjectTreatment = new JSONObject();
		jsonObjectTreatment.put("token", otl.getToken()); 
		jsonObjectTreatment.put("uid", otl.getUid());
		jsonObjectTreatment.put("filepath", otl.getFilepath());
		jsonObjectTreatment.put("csrf", otl.getCsrf());

		return jsonObjectTreatment;
	}
}
