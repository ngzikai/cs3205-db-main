package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import entity.Filter;
import utils.db.MySQLAccess;

public class FilterController {
	
	public ArrayList<Filter> getFilters(){
		String sql = "SELECT * FROM filters";
		Connection connect  = MySQLAccess.connectDatabase();
		
		try {
			PreparedStatement ps = connect.prepareStatement(sql);
			
			ResultSet rs = MySQLAccess.readDataBasePS(ps);
			
		} catch (Exception e) {
			e.printStackTrace();
			MySQLAccess.close();
		}
		
		
		return null;
	}

}
