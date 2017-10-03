package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;

import entity.Search;
import entity.SearchResult;
import utils.db.MySQLAccess;

public class SearchController {
	
	public ArrayList<SearchResult> search(Search search){
		System.out.println(search.toString());
		
		String sql = "SELECT user.dob, user.gender, user.zipcode1, user.zipcode2, user.bloodtype, `condition`.condition_name FROM user, diagnosis, `condition` "
				+ "WHERE user.uid = diagnosis.patient_id AND diagnosis.condition_id = `condition`.condition_id";
		
		if (search.getBloodType() != null) {
			sql = sql + " AND user.bloodtype = '" + search.getBloodType() + "'";
		}
		
		if(search.getGender() != null) {
			sql = sql + " AND user.gender = '" + search.getGender() + "'";
		}
		
		if (search.getStartDob() != null || search.getEndDob() != null) {
			if(search.getStartDob() == null) {
				search.setStartDob(search.getEndDob());
			}
			
			if(search.getEndDob() == null) {
				search.setEndDob(search.getStartDob());
			}
			sql = sql + " AND user.dob BETWEEN '" + search.getStartDob() + "' AND '" + search.getEndDob() + "'";
		}
		
		if (search.getZipcode() != null) {
			sql = sql + " AND (user.zipcode1 LIKE '" + search.getZipcode() + "%' OR user.zipcode2 LIKE '"+ search.getZipcode() +"%')";
		}
		
		if(search.getCid() != null) {
			sql = sql + " AND diagnosis.condition_id = '" + search.getCid() + "'";
		}
		
		Connection connect = MySQLAccess.connectDatabase();
		ArrayList<SearchResult> results = new ArrayList<SearchResult>();
		
		try {
			System.out.println(sql);
			PreparedStatement ps = connect.prepareStatement(sql);
			ResultSet rs = MySQLAccess.readDataBasePS(ps);
			
			while(rs.next()) {
				SearchResult result = new SearchResult();
				result.setDob(new Date(rs.getDate(1).getTime()));
				result.setGender(rs.getString(2));
				result.setZipcode1(rs.getString(3));
				if(rs.getString(4) != null) {
					result.setZipcode2(rs.getString(4));
				}
				result.setBloodtype(rs.getString(5));
				result.setCondition_name(rs.getString(6));
				
				results.add(result);
			}
			
					
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}

}
