package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import entity.BasicString;
import utils.db.MySQLAccess;

public class EthnicityNationalityController {
	
	public ArrayList<BasicString> getEthnicity(){
		String sql = "SELECT DISTINCT ethnicity from user"; 
		Connection connect = MySQLAccess.connectDatabase();
		
		ArrayList<BasicString> results = new ArrayList<BasicString>();
		
		try {
			PreparedStatement ps = connect.prepareStatement(sql);
			ResultSet rs = MySQLAccess.readDataBasePS(ps);
			
			while(rs.next()) {
				results.add(new BasicString(rs.getString(1)));
			}
			
		}catch (Exception e) {
			e.printStackTrace();
			MySQLAccess.close();
			return null;
		}
		
		MySQLAccess.close();
		return results;
	}
	
	public ArrayList<BasicString> getNationality(){
		String sql = "SELECT DISTINCT nationality from user"; 
		Connection connect = MySQLAccess.connectDatabase();
		
		ArrayList<BasicString> results = new ArrayList<BasicString>();
		
		try {
			PreparedStatement ps = connect.prepareStatement(sql);
			ResultSet rs = MySQLAccess.readDataBasePS(ps);
			
			while(rs.next()) {
				results.add(new BasicString(rs.getString(1)));
			}
			
		}catch (Exception e) {
			e.printStackTrace();
			MySQLAccess.close();
			return null;
		}
		
		MySQLAccess.close();
		return results;
	}

}
