package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import entity.Treatment;
import utils.Logger;
import utils.db.MySQLAccess;

public class TreatmentController {

	
	/*
	 * This method will get all treatments' value from the database and return them.
	 * 
	 * @return JSONObject containing the result
	 * 		  null if empty or error
	 */
	public JSONObject getAllTreatment() {
		JSONObject jsonObjectFinal = new JSONObject();
		ArrayList<Treatment> treatmentList = null;
		String sql = "SELECT * FROM CS3205.treatment";
		try {
			Connection connect = MySQLAccess.connectDatabase();
			PreparedStatement preparedStatement = connect.prepareStatement(sql);
			String statement = preparedStatement.toString();
			treatmentList = resultSetToTreatmentList(MySQLAccess.readDataBasePS(preparedStatement));
			Logger.log(Logger.API.TEAM1.name(), Logger.TYPE.READ.name(), statement, treatmentList.size() == 0 ? 0 : 1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		JSONArray treatmentArray = new JSONArray();
		for(Treatment treatment : treatmentList) {
			JSONObject jsonObjectTreatment = buildTreatmentObject(treatment);
			treatmentArray.put(jsonObjectTreatment);
		}
		MySQLAccess.close();
		jsonObjectFinal.put("treatments", treatmentArray);
		return jsonObjectFinal;
	}
	
	
	/*
	 * This method will take in a treatment id and return the treatment object from the database.
	 * 
	 * @param id of treatment
	 * 
	 * @return JSONObject containing the result
	 * 		  null if empty or error
	 */
	public JSONObject getTreatmentWithId(int id) {
		JSONObject jsonObject = new JSONObject();
		ArrayList<Treatment> treatmentList = null;

		String sql = "SELECT * FROM CS3205.treatment WHERE treatment_id = ? ";
		try {
			Connection connect = MySQLAccess.connectDatabase();
			PreparedStatement preparedStatement = connect.prepareStatement(sql);
			preparedStatement.setInt(1, id);
			String statement = preparedStatement.toString();
			treatmentList = resultSetToTreatmentList(MySQLAccess.readDataBasePS(preparedStatement));
			Logger.log(Logger.API.TEAM1.name(), Logger.TYPE.READ.name(), statement, treatmentList.size() == 0 ? 0 : 1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		if(treatmentList.size() < 1) {
			return null;
		}
		MySQLAccess.close();
		Treatment treatment = treatmentList.get(0);
		jsonObject = buildTreatmentObject(treatment);
		return jsonObject;
	}
	
	/*
	 * This method will take in a patient id and a status, and return the treatment 
	 * object from the database.
	 * 
	 * @param patientid
	 * 		  status
	 * 
	 * @return JSONObject containing the result
	 * 		  null if empty or error
	 */
	public JSONObject getTreatmentWithPatientId(int patientid, boolean status) {
		JSONObject jsonObject = new JSONObject();
		ArrayList<Treatment> treatmentList = null;

		String sql = "SELECT * FROM CS3205.treatment WHERE patient_id = ? AND status = ? ";
		try {
			Connection connect = MySQLAccess.connectDatabase();
			PreparedStatement preparedStatement = connect.prepareStatement(sql);
			preparedStatement.setInt(1, patientid);
			preparedStatement.setInt(2, status ? 1 : 0);
			String statement = preparedStatement.toString();
			treatmentList = resultSetToTreatmentList(MySQLAccess.readDataBasePS(preparedStatement));
			Logger.log(Logger.API.TEAM1.name(), Logger.TYPE.READ.name(), statement, treatmentList.size() == 0 ? 0 : 1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		if(treatmentList.size() < 1) {
			return null;
		}
		JSONArray treatmentArray = new JSONArray();
		for(Treatment treatment : treatmentList) {
			JSONObject jsonObjectTreatment = buildTreatmentObject(treatment);
			treatmentArray.put(jsonObjectTreatment);
		}
		MySQLAccess.close();
		jsonObject.put("treatments", treatmentArray);
		//System.out.println("Retrieving details of Treatment: " + id);
		return jsonObject;
	}
	
	/*
	 *This method will take in a therapist id and a status, and return the treatment 
	 * object from the database.
	 * 
	 * @param patientid
	 * 		  status
	 * 
	 * @return JSONObject containing the result
	 * 		  null if empty or error
	 */
	public JSONObject getTreatmentWithTherapistId(int therapistid, boolean status) {
		JSONObject jsonObject = new JSONObject();
		ArrayList<Treatment> treatmentList = null;

		String sql = "SELECT * FROM CS3205.treatment WHERE therapist_id = ? AND status = ? ";
		try {
			Connection connect = MySQLAccess.connectDatabase();
			PreparedStatement preparedStatement = connect.prepareStatement(sql);
			preparedStatement.setInt(1, therapistid);
			preparedStatement.setInt(2, status ? 1 : 0);
			String statement = preparedStatement.toString();
			treatmentList = resultSetToTreatmentList(MySQLAccess.readDataBasePS(preparedStatement));
			Logger.log(Logger.API.TEAM1.name(), Logger.TYPE.READ.name(), statement, treatmentList.size() == 0 ? 0 : 1);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		if(treatmentList.size() < 1) {
			return null;
		}
		JSONArray treatmentArray = new JSONArray();
		for(Treatment treatment : treatmentList) {
			JSONObject jsonObjectTreatment = buildTreatmentObject(treatment);
			treatmentArray.put(jsonObjectTreatment);
		}
		MySQLAccess.close();
		jsonObject.put("treatments", treatmentArray);
		//System.out.println("Retrieving details of Treatment: " + id);
		return jsonObject;
	}

	/*
	 * This method will create a treatment object with the necessary variables to be stored in the database. 
	 * 
	 * @param patientId
	 * 		  therapistId
	 * 		  currentConsent
	 * 		  futureConsent
	 * 
	 * @return JSONobject containing 1 if success.
	 * 								 0 if failed.
	 * 		   null if empty or error
	 */
	public JSONObject createTreatment(int patientId, int therapistId, boolean currentConsent,
			boolean futureConsent ) {
		Treatment treatment = new Treatment(patientId, therapistId, false, currentConsent, futureConsent);
		JSONObject jsonObject = new JSONObject();
		int result = 0;
		String sql = "INSERT INTO CS3205.treatment VALUES (default, ?, ?, ?, ?,?)";
		try {
			Connection connect = MySQLAccess.connectDatabase();
			PreparedStatement preparedStatement = connect.prepareStatement(sql);
			preparedStatement.setInt(1, treatment.getPatientId());
			preparedStatement.setInt(2, treatment.getTherapistId());
			preparedStatement.setInt(3, treatment.isStatus() ? 1: 0);
			preparedStatement.setInt(4, treatment.isCurrentConsent() ? 1: 0);
			preparedStatement.setInt(5, treatment.isFutureConsent() ? 1: 0);
			String statement = preparedStatement.toString();
			result = MySQLAccess.updateDataBasePS(preparedStatement);
			Logger.log(Logger.API.TEAM1.name(), Logger.TYPE.WRITE.name(), statement, result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		MySQLAccess.close();
		System.out.println("Created treatment: " + patientId + " & " + therapistId);
		jsonObject.put("result", result);
		return jsonObject;
	}

	/*
	 * This method takes in a treatment id where the corresponding treatment object will
	 * be set to a status value of true in the database.
	 * 
	 * @param id of treatment
	 * @return JSONObject containing 1 if success
	 * 								 0 if failed
	 * 		   null if empty or error
	 */
	public JSONObject updateTreatment(int id) {
		JSONObject jsonObject = new JSONObject();
		int result = 0;
		String sql = "UPDATE CS3205.treatment SET status = ? WHERE treatment_id = ?";
		try {
			Connection connect = MySQLAccess.connectDatabase();
			PreparedStatement preparedStatement = connect.prepareStatement(sql);
			preparedStatement.setInt(1, 1);
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
		System.out.println("Updated treatment: " + id);
		jsonObject.put("result", result);
		return jsonObject;
	}
	
	/*
	 * This method takes in a treatment object where the treatment id will be used
	 * to set the current consent and future consent of the corresponding treatment value
	 * in the database.
	 * 
	 * @param id of treatment
	 * @return JSONObject containing 1 if success
	 * 								 0 if failed
	 * 		   null if empty or error
	 */
	public JSONObject updateConsentSetting(Treatment treatment) {
		JSONObject jsonObject = new JSONObject();
		int result = 0;
		String sql = "UPDATE CS3205.treatment SET current_consent = ?, future_consent = ? WHERE treatment_id = ?";
		try {
			Connection connect = MySQLAccess.connectDatabase();
			PreparedStatement preparedStatement = connect.prepareStatement(sql);
			preparedStatement.setInt(1, treatment.isCurrentConsent() ? 1 : 0);
			preparedStatement.setInt(2, treatment.isFutureConsent() ? 1 : 0);
			preparedStatement.setInt(3, treatment.getId());
			String statement = preparedStatement.toString();
			result = MySQLAccess.updateDataBasePS(preparedStatement);
			Logger.log(Logger.API.TEAM1.name(), Logger.TYPE.WRITE.name(), statement, result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		MySQLAccess.close();
		System.out.println("Updated treatment: " + treatment.getId());
		jsonObject.put("result", result);
		return jsonObject;
	}
	
	/*
	 * This method deletes the treatment entry on the database based on the treatment id.
	 * 
	 * @param id of treatment
	 * 
	 * @return JSONObject contained the result of the operation. 1 is success.
	 * 															 0 is failed.
	 * 		   null if error
	 */
	public JSONObject deleteTreatment(int id) {
		int result = 0;
		JSONObject jsonObject = new JSONObject();
		String sql = "DELETE FROM CS3205.treatment where treatment_id = ?";
		
		System.out.println("Deleting treatment: " + id);
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
		jsonObject.put("result", result);
		return jsonObject;
	}
	
	/*
	 * This method will take in a result set of a SQL operation and prepares a
	 * list of treatment object with the corresponding fields from the result set
	 * 
	 * @param result of SQL query
	 * @return ArrayList of Treatment objects
	 * 
	 */
	private ArrayList<Treatment> resultSetToTreatmentList(ResultSet resultSet) throws SQLException {
		// ResultSet is initially before the first data set
		ArrayList<Treatment> treatmentList = new ArrayList<Treatment>();
		while (resultSet.next()) {
			int id = resultSet.getInt("treatment_id");
			int patientId = resultSet.getInt("patient_id");
			int therapistId = resultSet.getInt("therapist_id");
			boolean status = (resultSet.getInt("status")==1) ? true : false;
			boolean currentConsent = (resultSet.getInt("current_consent")==1) ? true : false;
			boolean futureConsent = (resultSet.getInt("future_consent")==1) ? true : false;
			Treatment treatment = new Treatment(id, patientId, therapistId, status, currentConsent, futureConsent);
			treatmentList.add(treatment);
		}
		MySQLAccess.close();
		return treatmentList;
	}
	
	/*
	 * This method will take in a treatment object and build a json object containing it.
	 * 
	 * @param result of SQL query
	 * @return JSONObject of Treatment object
	 * 
	 */
	private JSONObject buildTreatmentObject(Treatment treatment) {
		JSONObject jsonObjectTreatment = new JSONObject(treatment);
		return jsonObjectTreatment;
	}
}
