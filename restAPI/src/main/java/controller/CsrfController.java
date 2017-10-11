package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import entity.Csrf;
import utils.db.MySQLAccess;

public class CsrfController {
	public JSONObject getAllCsrf() {
		JSONObject jsonObjectFinal = new JSONObject();
		ArrayList<Csrf> csrfList = null;
		String sql = "SELECT * FROM CS3205.csrf";
		try {
			Connection connect = MySQLAccess.connectDatabase();
			PreparedStatement preparedStatement = connect.prepareStatement(sql);
			csrfList = resultSetToCsrfList(MySQLAccess.readDataBasePS(preparedStatement));
		} catch (Exception e) {
			// TODO Auto-generated catch block
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


	// This method will take in a treatment name and return the treatment object from the database;
	public JSONObject getCsrfWithToken(String csrfToken) {
		JSONObject jsonObject = new JSONObject();
		ArrayList<Csrf> csrfList = null;

		String sql = "SELECT * FROM CS3205.csrf WHERE csrf_token = ? ";
		try {
			Connection connect = MySQLAccess.connectDatabase();
			PreparedStatement preparedStatement = connect.prepareStatement(sql);
			preparedStatement.setString(1, csrfToken);
			csrfList = resultSetToCsrfList(MySQLAccess.readDataBasePS(preparedStatement));
		} catch (Exception e) {
			// TODO Auto-generated catch block
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

	public JSONObject createCsrf(String csrfToken, int uid, int expiry) {
		Csrf csrf = new Csrf(csrfToken, uid, expiry);
		JSONObject jsonObject = new JSONObject();
		int result = 0;
		String sql = "INSERT INTO CS3205.csrf VALUES (?, ?, ?)";
		try {
			Connection connect = MySQLAccess.connectDatabase();
			PreparedStatement preparedStatement = connect.prepareStatement(sql);
			preparedStatement.setString(1, csrf.getCsrfToken());
			preparedStatement.setInt(2, csrf.getUid());
			preparedStatement.setInt(3, csrf.getExpiry());
			result = MySQLAccess.updateDataBasePS(preparedStatement);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		MySQLAccess.close();
		System.out.println("Created One Time Link: " + csrf.toString());
		jsonObject.put("result", result);
		return jsonObject;
	}


	public JSONObject updateCSRF(String csrfToken, int expiry) {

		//Treatment treatment = new Treatment(Integer.parseInt(uid), treatmentname, password, firstName, lastName, nric, LocalDate.parse(dob), gender, phone1,
		//phone2, phone3,  address1, address2, address3, zipcode1, zipcode2, zipcode3, qualify, bloodtype, nfcid);
		JSONObject jsonObject = new JSONObject();
		int result = 0;
		String sql = "UPDATE CS3205.csrf SET expiry = ? WHERE csrf_token = ?";
		try {
			Connection connect = MySQLAccess.connectDatabase();
			PreparedStatement preparedStatement = connect.prepareStatement(sql);
			preparedStatement.setInt(1, expiry);
			preparedStatement.setString(2, csrfToken);
			result = MySQLAccess.updateDataBasePS(preparedStatement);
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


	public JSONObject deleteCsrf(String csrfToken) {
		
		int result = 0;
		JSONObject jsonObject = new JSONObject();
		String sql = "DELETE FROM CS3205.csrf where csrf_token = ?";

		System.out.println("Deleting csrf token: " + csrfToken);
		try {
			Connection connect = MySQLAccess.connectDatabase();
			PreparedStatement preparedStatement = connect.prepareStatement(sql);
			preparedStatement.setString(1, csrfToken);
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

	private ArrayList<Csrf> resultSetToCsrfList(ResultSet resultSet) throws SQLException {
		// ResultSet is initially before the first data set
		ArrayList<Csrf> csrfList = new ArrayList<Csrf>();
		while (resultSet.next()) {
			String csrfToken = resultSet.getString("csrf_token");
			int uid = resultSet.getInt("uid");
			int expiry = resultSet.getInt("expiry");;
			Csrf csrf = new Csrf(csrfToken, uid, expiry);
			csrfList.add(csrf);
		}
		MySQLAccess.close();
		return csrfList;
	}

	private JSONObject buildCsrfObject(Csrf csrf) {
		JSONObject jsonObjectCsrf = new JSONObject();
		jsonObjectCsrf.put("csrfToken", csrf.getCsrfToken()); 
		jsonObjectCsrf.put("uid", csrf.getUid());
		jsonObjectCsrf.put("expiry", csrf.getExpiry());
		return jsonObjectCsrf;
	}
}
