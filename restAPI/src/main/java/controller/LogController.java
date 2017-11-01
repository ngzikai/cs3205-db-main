package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import org.json.JSONObject;

import entity.Log;
import utils.Logger;
import utils.db.MySQLAccess;

public class LogController {
	
	public JSONObject getAllLog() {
		JSONObject jsonObjectFinal = new JSONObject();
		ArrayList<Log> logList = null;
		String sql = "SELECT * FROM CS3205.transaction_logs";
		try {
			Connection connect = MySQLAccess.connectDatabase();
			PreparedStatement preparedStatement = connect.prepareStatement(sql);
			String statement = preparedStatement.toString();
			logList = resultSetToLogList(MySQLAccess.readDataBasePS(preparedStatement));
			Logger.log(Logger.API.TEAM1.name(), Logger.TYPE.READ.name(), statement, logList.size() == 0 ? 0 : 1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		MySQLAccess.close();
		jsonObjectFinal.put("logs", logList);
		return jsonObjectFinal;
	}
	
	public JSONObject createLog(Log log) {
		JSONObject jsonObject = new JSONObject();
		int result = 0;
		String sql = "INSERT INTO CS3205.transaction_logs VALUES (default, ?, ?, ?, ?, ?)";
		try {
			Connection connect = MySQLAccess.connectDatabase();
			PreparedStatement preparedStatement = connect.prepareStatement(sql);
			preparedStatement.setString(1, log.getApi());
			preparedStatement.setString(2, log.getClassification());
			preparedStatement.setTimestamp(3, log.getTime());
			preparedStatement.setInt(4, log.getUid());
			preparedStatement.setString(5, log.getDescription());
			String statement = preparedStatement.toString();
			result = MySQLAccess.updateDataBasePS(preparedStatement);
			Logger.log(Logger.API.TEAM1.name(), Logger.TYPE.WRITE.name(),  statement, result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		MySQLAccess.close();
		System.out.println("Created transaction log for user: " + log.getUid() + " at " + log.getTime());
		jsonObject.put("result", result);
		return jsonObject;
	}
	
	private ArrayList<Log> resultSetToLogList(ResultSet resultSet) throws SQLException {
		// ResultSet is initially before the first data set
		ArrayList<Log> logList = new ArrayList<Log>();
		while (resultSet.next()) {
			int id = resultSet.getInt("log_id");
			String api = resultSet.getString("api");
			String classification = resultSet.getString("classification");
			Timestamp time = resultSet.getTimestamp("time");
			int uid = resultSet.getInt("uid");
			String description = resultSet.getString("description");
			Log log = new Log(id, api, classification, time, uid, description);
			logList.add(log);
		}
		MySQLAccess.close();
		return logList;
	}
}
