package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import entity.Filter;
import utils.db.MySQLAccess;

public class FilterController {
	
	public ArrayList<Filter> getFilters(){
		String sql = "SELECT * FROM filters WHERE parent_id IS NULL";
		Connection connect  = MySQLAccess.connectDatabase();
		
		ArrayList<Filter> results = new ArrayList<Filter>();
		try {
			PreparedStatement ps = connect.prepareStatement(sql);
			
			ResultSet rs = MySQLAccess.readDataBasePS(ps);
			
			while(rs.next()) {
				results.add(new Filter(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), null));
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			MySQLAccess.close();
		}
		
		for(int i = 0; i < results.size(); i++) {
			sql = "SELECT * FROM filters WHERE parent_id = ?";
			
			try {
				PreparedStatement ps = connect.prepareStatement(sql);
				ps.setString(1, results.get(i).getId());
				ResultSet rs = MySQLAccess.readDataBasePS(ps);
				
				ArrayList<Filter> temp = new ArrayList<Filter>();
		
				while(rs.next()) {
					temp.add(new Filter(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), null));
				}
				
				if(!temp.isEmpty()) {
					results.get(i).setChildren(temp);
				}
					
			} catch (Exception e) {
				e.printStackTrace();
				MySQLAccess.close();
			}
			
		}
		
		MySQLAccess.close();
		return results;
	}

}
