package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

import javax.json.Json;

import org.json.JSONArray;
import org.json.JSONObject;

import entity.User;
import utils.db.MySQLAccess;

public class UserController {

	public JSONObject getAllUser() {
		JSONObject jsonObjectFinal = new JSONObject();
		ArrayList<User> userList = null;
		String sql = "SELECT * FROM CS3205.user";
		try {
			Connection connect = MySQLAccess.connectDatabase();
			PreparedStatement preparedStatement = connect.prepareStatement(sql);
			userList = resultSetToUserList(MySQLAccess.readDataBasePS(preparedStatement));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		JSONArray userArray = new JSONArray();
		for(User user : userList) {
			JSONObject jsonObjectUser = buildUserObject(user);
			userArray.put(jsonObjectUser);
		}
		MySQLAccess.close();
		jsonObjectFinal.put("users", userArray);
		return jsonObjectFinal;
	}
	
	// This method will take in a user name and return the user object from the database;
	public JSONObject getUser(String username) {
		JSONObject jsonObject = new JSONObject();
		User user = null;
		String sql = "SELECT uid, password, salt, qualify, secret FROM CS3205.user WHERE username = ? ";
		System.out.println("Retrieving details of User account: " + username);
		try {
			Connection connect = MySQLAccess.connectDatabase();
			PreparedStatement preparedStatement = connect.prepareStatement(sql);
			preparedStatement.setString(1, username);
			user = resultSetToUserLogin(MySQLAccess.readDataBasePS(preparedStatement));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		if(user == null) {
			return null;
		}
		MySQLAccess.close();
		jsonObject = buildLoginObject(user);
		
		return jsonObject;
	}
	
	// This method will take in a user name and return the user object from the database;
	public JSONObject getUserWithUID(String uid) {
		JSONObject jsonObject = new JSONObject();
		ArrayList<User> userList = null;

		String sql = "SELECT * FROM CS3205.user WHERE uid = ? ";
		try {
			Connection connect = MySQLAccess.connectDatabase();
			PreparedStatement preparedStatement = connect.prepareStatement(sql);
			preparedStatement.setInt(1, Integer.parseInt(uid));
			userList = resultSetToUserList(MySQLAccess.readDataBasePS(preparedStatement));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		if(userList.size() < 1) {
			return null;
		}
		MySQLAccess.close();
		User user = userList.get(0);
		jsonObject = buildUserObject(user);
		System.out.println("Retrieving details of User account: " + uid);
		return jsonObject;
	}
	
	// This method will take in a user name and return the user object from the database;
	public JSONObject getUserPublicInfo(String uid) {
		JSONObject jsonObject = new JSONObject();
		User user = null;
		String sql = "SELECT uid, firstname, lastname, gender, phone1, qualify FROM CS3205.user WHERE uid = ? ";
		System.out.println("Retrieving details of User account: " + uid);
		try {
			Connection connect = MySQLAccess.connectDatabase();
			PreparedStatement preparedStatement = connect.prepareStatement(sql);
			preparedStatement.setInt(1, Integer.parseInt(uid));
			user = resultSetGetPublicInfo(MySQLAccess.readDataBasePS(preparedStatement));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		if(user == null) {
			return null;
		}
		MySQLAccess.close();
		jsonObject = buildPublicInfoObject(user);
		
		return jsonObject;
	}

	public JSONObject createUser(String username, String password, String salt, String firstName, String lastName,
			String nric, String dob, char gender, String phone1, String phone2, String phone3, String address1, 
			String address2, String address3, int zipcode1, int zipcode2, int zipcode3, int qualify, String bloodtype,
			String nfcid) {
		User user = new User(username, password, salt, firstName, lastName, nric, LocalDate.parse(dob), gender, phone1,
				phone2, phone3,  address1, address2, address3, zipcode1, zipcode2, zipcode3, qualify, bloodtype, nfcid);
		JSONObject jsonObject = createUser(user);
		return jsonObject;
	}
	
	public JSONObject createUser(User user) {
		JSONObject jsonObject = new JSONObject();
		int result = 0;
		String sql = "INSERT INTO CS3205.user VALUES (default, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, null)";
		try {
			Connection connect = MySQLAccess.connectDatabase();
			PreparedStatement preparedStatement = connect.prepareStatement(sql);
			preparedStatement.setString(1, user.getUsername());
			preparedStatement.setString(2, user.getPassword());
			preparedStatement.setString(3, user.getPassword());
			preparedStatement.setString(4, user.getSalt());
			preparedStatement.setString(5, user.getSalt());
			preparedStatement.setString(6, user.getFirstName());
			preparedStatement.setString(7, user.getLastName());
			preparedStatement.setString(8, user.getNric());
			preparedStatement.setObject(9, user.getDob());
			preparedStatement.setString(10, user.getGender() +"");
			preparedStatement.setString(11, user.getPhone()[0]);
			preparedStatement.setString(12, user.getPhone()[1]);
			preparedStatement.setString(13, user.getPhone()[2]);
			preparedStatement.setString(14, user.getAddress()[0]);
			preparedStatement.setString(15, user.getAddress()[1]);
			preparedStatement.setString(16, user.getAddress()[2]);
			preparedStatement.setInt(17, user.getZipcode()[0]);
			preparedStatement.setInt(18, user.getZipcode()[1]);
			preparedStatement.setInt(19, user.getZipcode()[2]);
			preparedStatement.setInt(20, user.getQualify());
			preparedStatement.setString(21, user.getBloodtype());
			preparedStatement.setString(22, user.getNfcid());
			result = MySQLAccess.updateDataBasePS(preparedStatement);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		MySQLAccess.close();
		System.out.println("Created user: " + user.getUsername());
		jsonObject.put("result", result);
		return jsonObject;
	}
	
	public JSONObject updateUser(String uid, String username, String password, String salt, String firstName, String lastName,
			String nric, String dob, char gender, String phone1, String phone2, String phone3, String address1, 
			String address2, String address3, int zipcode1, int zipcode2, int zipcode3, int qualify, String bloodtype,
			String nfcid) {
		
		User user = new User(Integer.parseInt(uid), username, password, salt, firstName, lastName, nric, LocalDate.parse(dob), gender, phone1,
				phone2, phone3,  address1, address2, address3, zipcode1, zipcode2, zipcode3, qualify, bloodtype, nfcid);
		JSONObject jsonObject = updateUser(user);
		return jsonObject;
	}
	
	public JSONObject updateUser(User user) {
		JSONObject jsonObject = new JSONObject();
		int result = 0;
		String sql = "UPDATE CS3205.user SET username = ?, password = ?, salt = ?, firstname = ?, lastname = ?, "
				+ "nric = ?, dob = ?, gender = ?, phone1 = ?, phone2 = ?, phone3 = ?, address1 = ?, address2 = ?, "
				+ "address3 = ?, zipcode1 = ?, zipcode2 = ?, zipcode3 = ?, qualify = ?, bloodtype = ?, nfcid = ? WHERE uid = ?";
		try {
			Connection connect = MySQLAccess.connectDatabase();
			PreparedStatement preparedStatement = connect.prepareStatement(sql);
			preparedStatement.setString(1, user.getUsername());
			preparedStatement.setString(2, user.getPassword());
			preparedStatement.setString(3, user.getSalt());
			preparedStatement.setString(4, user.getFirstName());
			preparedStatement.setString(5, user.getLastName());
			preparedStatement.setString(6, user.getNric());
			preparedStatement.setObject(7, user.getDob());
			preparedStatement.setString(8, user.getGender() +"");
			preparedStatement.setString(9, user.getPhone()[0]);
			preparedStatement.setString(10, user.getPhone()[1]);
			preparedStatement.setString(11, user.getPhone()[2]);
			preparedStatement.setString(12, user.getAddress()[0]);
			preparedStatement.setString(13, user.getAddress()[1]);
			preparedStatement.setString(14, user.getAddress()[2]);
			preparedStatement.setInt(15, user.getZipcode()[0]);
			preparedStatement.setInt(16, user.getZipcode()[1]);
			preparedStatement.setInt(17, user.getZipcode()[2]);
			preparedStatement.setInt(18, user.getQualify());
			preparedStatement.setString(19, user.getBloodtype());
			preparedStatement.setString(20, user.getNfcid());
			preparedStatement.setInt(21, user.getUid());
			result = MySQLAccess.updateDataBasePS(preparedStatement);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		MySQLAccess.close();
		System.out.println("Updated user: " + user.getUsername());
		jsonObject.put("result", result);
		return jsonObject;
	}
	
	public JSONObject updateUserPassword(String username, String password, String salt) {
		
		JSONObject jsonObject = new JSONObject();
		int result = 0;
		String sql = "UPDATE CS3205.user SET password = ?, salt = ? WHERE username = ?";
		try {
			Connection connect = MySQLAccess.connectDatabase();
			PreparedStatement preparedStatement = connect.prepareStatement(sql);
			preparedStatement.setString(1, password);
			preparedStatement.setString(2, salt);
			preparedStatement.setString(3, username);
			result = MySQLAccess.updateDataBasePS(preparedStatement);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		MySQLAccess.close();
		System.out.println("Updated user: " + username);
		jsonObject.put("result", result);
		return jsonObject;
	}
	
	public JSONObject deleteUser(int uid) {
		int result = 0;
		JSONObject jsonObject = new JSONObject();
		String sql = "DELETE FROM CS3205.user where uid = ?";
		
		System.out.println("Deleting user: " + uid);
		try {
			Connection connect = MySQLAccess.connectDatabase();
			PreparedStatement preparedStatement = connect.prepareStatement(sql);
		    preparedStatement.setInt(1, uid);
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
	
	public JSONObject getTherapists() {
		JSONObject jsonObjectFinal = new JSONObject();
		ArrayList<User> userList = null;
		String sql = "SELECT uid, firstname, lastname, gender FROM CS3205.user where qualify = 1";
		try {
			Connection connect = MySQLAccess.connectDatabase();
			PreparedStatement preparedStatement = connect.prepareStatement(sql);
			userList = resultSetToTherapistsList(MySQLAccess.readDataBasePS(preparedStatement));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		JSONArray userArray = new JSONArray();
		for(User user : userList) {
			JSONObject jsonObjectUser = buildTherapistsObject(user);
			userArray.put(jsonObjectUser);
		}
		MySQLAccess.close();
		jsonObjectFinal.put("users", userArray);
		return jsonObjectFinal;
	}
	
	public JSONObject userSetSecret(int uid, String secret) {
		int result = 0;
		JSONObject jsonObject = new JSONObject();
		String sql = "UPDATE CS3205.user SET secret = ? where uid = ?";
		
		System.out.println("Setting secret for user: " + uid);
		try {
			Connection connect = MySQLAccess.connectDatabase();
			PreparedStatement preparedStatement = connect.prepareStatement(sql);
			preparedStatement.setString(1, secret);
		    preparedStatement.setInt(2, uid);
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
	
	public JSONObject userGetSecret(int uid) {
		JSONObject jsonObject = new JSONObject();
		String sql = "SELECT secret FROM CS3205.user where uid = ?";
		
		System.out.println("Getting secret from user: " + uid);
		try {
			Connection connect = MySQLAccess.connectDatabase();
			PreparedStatement preparedStatement = connect.prepareStatement(sql);
			preparedStatement.setInt(1, uid);
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

	private ArrayList<User> resultSetToUserList(ResultSet resultSet) throws SQLException {
		// ResultSet is initially before the first data set
		ArrayList<User> userList = new ArrayList<User>();
		while (resultSet.next()) {
			int id = resultSet.getInt("uid");
			String username = resultSet.getString("username");
			String password = resultSet.getString("password");
			String salt = resultSet.getString("salt");
			String firstName = resultSet.getString("firstname");
			String lastName = resultSet.getString("lastname");
			String nric = resultSet.getString("nric");
			LocalDate dob = resultSet.getObject("dob", LocalDate.class);
			char gender = resultSet.getString("gender").charAt(0);
			String[] phone = {resultSet.getString("phone1"), resultSet.getString("phone2"),resultSet.getString("phone3")};
			String[] address = {resultSet.getString("address1"), resultSet.getString("address2"),resultSet.getString("address3")};
			int[] zipcode = {resultSet.getInt("zipcode1"), resultSet.getInt("zipcode2"),resultSet.getInt("zipcode3")};
			int qualify = resultSet.getInt("qualify");
			String bloodType = resultSet.getString("bloodtype");
			String nfcid = resultSet.getString("nfcid");
			String secret = resultSet.getString("secret");
			User user = new User(id, username, password, salt, firstName, lastName, nric
					, dob, gender, phone, address, zipcode, qualify, bloodType, nfcid, secret);
			userList.add(user);
		}
		MySQLAccess.close();
		return userList;
	}
	
	private ArrayList<User> resultSetToTherapistsList(ResultSet resultSet) throws SQLException {
		// ResultSet is initially before the first data set
		ArrayList<User> userList = new ArrayList<User>();
		while (resultSet.next()) {
			int id = resultSet.getInt("uid");
			String firstName = resultSet.getString("firstname");
			String lastName = resultSet.getString("lastname");
			char gender = resultSet.getString("gender").charAt(0);
			User user = new User(id, firstName, lastName, gender);
			userList.add(user);
		}
		MySQLAccess.close();
		return userList;
	}
	
	private JSONObject buildUserObject(User user) {
		JSONObject jsonObjectUser = new JSONObject();
		jsonObjectUser.put("uid", user.getUid()); 
		jsonObjectUser.put("username", user.getUsername());
		jsonObjectUser.put("password", user.getPassword());
		jsonObjectUser.put("salt", user.getSalt());
		jsonObjectUser.put("firstname", user.getFirstName());
		jsonObjectUser.put("lastname", user.getLastName());
		jsonObjectUser.put("dob", user.getDob().toString());
		jsonObjectUser.put("nric", user.getNric());
		jsonObjectUser.put("gender", user.getGender()+"");
		String[] phone = user.getPhone();
		JSONArray phoneArr = new JSONArray();
		phoneArr.put(phone[0]);
		phoneArr.put(phone[1]);
		phoneArr.put(phone[2]);
		jsonObjectUser.put("phone", phoneArr);
		String[] address = user.getAddress();
		JSONArray addressArr = new JSONArray();
		addressArr.put(address[0]);
		addressArr.put(address[1]);
		addressArr.put(address[2]);
		jsonObjectUser.put("address", addressArr);
		int[] zipcode = user.getZipcode();
		JSONArray zipArr = new JSONArray();
		zipArr.put(zipcode[0]);
		zipArr.put(zipcode[1]);
		zipArr.put(zipcode[2]);
		jsonObjectUser.put("zipcode", zipArr);
		jsonObjectUser.put("qualify", user.getQualify());
		jsonObjectUser.put("bloodtype", user.getBloodtype());
		jsonObjectUser.put("nfcid", user.getNfcid());
		jsonObjectUser.put("secret", user.getSecret());
		user.print();
		return jsonObjectUser;
	}
	
	private JSONObject buildTherapistsObject(User user) {
		JSONObject jsonObjectUser = new JSONObject();
		jsonObjectUser.put("uid", user.getUid()); 
		jsonObjectUser.put("firstname", user.getFirstName());
		jsonObjectUser.put("lastname", user.getLastName());
		jsonObjectUser.put("gender", user.getGender()+"");
		return jsonObjectUser;
	}
	
	private User resultSetGetPublicInfo(ResultSet resultSet) throws SQLException {
		// ResultSet is initially before the first data set
		User user = null;
		while (resultSet.next()) {
			int id = resultSet.getInt("uid");
			String firstName = resultSet.getString("firstname");
			String lastName = resultSet.getString("lastname");
			char gender = resultSet.getString("gender").charAt(0);
			String phone = resultSet.getString("phone1");
			int qualify = resultSet.getInt("qualify");
			user = new User(id, firstName, lastName, gender, phone, qualify);
		}
		MySQLAccess.close();
		return user;
	}
	
	private JSONObject buildPublicInfoObject(User user) {
		JSONObject jsonObjectUser = new JSONObject();
		jsonObjectUser.put("uid", user.getUid()); 
		jsonObjectUser.put("firstname", user.getFirstName());
		jsonObjectUser.put("lastname", user.getLastName());
		jsonObjectUser.put("gender", user.getGender()+"");
		jsonObjectUser.put("phone", user.getPhone()[0]);
		jsonObjectUser.put("qualify", user.getQualify());
		return jsonObjectUser;
	}
	
	
	private User resultSetToUserLogin(ResultSet resultSet) throws SQLException {
		// ResultSet is initially before the first data set
		User user = null;
		while (resultSet.next()) {
			int id = resultSet.getInt("uid");
			String password = resultSet.getString("password");
			String salt = resultSet.getString("salt");
			int qualify = resultSet.getInt("qualify");
			String secret = resultSet.getString("secret");
			user = new User(id, password, salt, qualify, secret);
		}
		MySQLAccess.close();
		return user;
	}
	
	private JSONObject buildLoginObject(User user) {
		JSONObject jsonObjectUser = new JSONObject();
		jsonObjectUser.put("uid", user.getUid()); 
		jsonObjectUser.put("password", user.getPassword());
		jsonObjectUser.put("salt", user.getSalt());
		jsonObjectUser.put("qualify", user.getQualify());
		jsonObjectUser.put("secret", user.getSecret());
		return jsonObjectUser;
	}
}
