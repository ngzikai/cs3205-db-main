package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.StringTokenizer;

import entity.HeartRate;
import utils.db.MySQLAccess;

public class HeartRateController {
	
	public ArrayList<HeartRate> getHeartRate(String userId) {
		String sql = "SELECT creationdate, content FROM data WHERE type = 'Heart Rate' AND uid = ? ORDER BY creationdate ASC";
		Connection connect = MySQLAccess.connectDatabase();
		
		ArrayList<HeartRate> results = new ArrayList<HeartRate>();
		
		try {
			PreparedStatement ps = connect.prepareStatement(sql);
			ps.setString(1, userId);
			
			ResultSet rs = MySQLAccess.readDataBasePS(ps);
			
			while(rs.next()) {
				String dateTime = rs.getString(1);
				String content = rs.getString(2);
				
				StringTokenizer tok = new StringTokenizer(dateTime, " ");
				String date = tok.nextToken();
				String time = tok.nextToken();
				
				results.add(new HeartRate(date, time, content));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			MySQLAccess.close();
			return null;
		}
		
		MySQLAccess.close();
		
		if(results.isEmpty()) {
			return null;
		}else {
			return results;
		}
	}

}
