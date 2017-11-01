package utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.Instant;

import utils.db.MySQLAccess;

public class Logger {
	
	public enum TYPE {
	    READ, WRITE
	}
	
	public enum API {
	    TEAM1, TEAM2, TEAM3
	}
	
	public static int log(String api, String type, String description, int qResult) {
		int result = 0;
		String sql = "INSERT INTO CS3205.db_logs VALUES (default, ?, ?, ?, ?, ?)";
		try {
			Connection connect = MySQLAccess.connectDatabase();
			PreparedStatement preparedStatement = connect.prepareStatement(sql);
			preparedStatement.setString(1, api);
			preparedStatement.setString(2, type);
			preparedStatement.setTimestamp(3, new Timestamp(Instant.now().toEpochMilli()));
			preparedStatement.setString(4, description);
			preparedStatement.setInt(5, qResult);
			result = MySQLAccess.updateDataBasePS(preparedStatement);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
		MySQLAccess.close();
		return result;
	}
}
