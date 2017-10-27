package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import entity.Search;
import entity.SearchResult;
import utils.db.MySQLAccess;

public class SearchController {
	
	public ArrayList<SearchResult> search(Search search){
		//System.out.println(search.toString());
		
		String sql = "SELECT user.dob, user.sex, user.zipcode1, user.zipcode2, user.bloodtype, user.ethnicity, "
				+ "user.nationality, user.drug_allergy, `condition`.condition_name, data.content, user.uid "
				+ "FROM user, diagnosis, `condition`, data "
				+ "WHERE user.uid = diagnosis.patient_id AND diagnosis.condition_id = `condition`.condition_id AND user.uid = data.uid AND data.title = 'GMSv5_2-Foot_R'";
		
		if(search.getAgeRange() == null && search.getBloodType() == null && search.getCid() == null && search.getGender() == null && search.getZipcode() == null & search.getDrug_allergy() == null & search.getEthnicity() == null & search.getNationality() == null) {
			return null;
		}
		
		if (search.getBloodType() != null) {
			sql += " AND (user.bloodtype = '" + search.getBloodType().get(0) + "'";
			
			for(int i=1; i < search.getBloodType().size(); i++) {
				sql += " OR user.bloodtype = '" + search.getBloodType().get(i) + "'";
			}
			
			sql += ")";
		}
		
		if(search.getGender() != null) {
			sql += " AND (user.sex = '" + search.getGender().get(0) + "'";
			
			for(int i=1; i < search.getGender().size(); i++) {
				sql += " OR user.sex = '" + search.getGender().get(i) + "'";
			}
			
			sql += ")";
		}
		
//		if (search.getStartDob() != null || search.getEndDob() != null) {
//			if(search.getStartDob() == null) {
//				search.setStartDob(search.getEndDob());
//			}
//			
//			if(search.getEndDob() == null) {
//				search.setEndDob(search.getStartDob());
//			}
//			sql = sql + " AND user.dob BETWEEN '" + search.getStartDob() + "' AND '" + search.getEndDob() + "'";
//		}
		
		if(search.getAgeRange() != null) {
			ArrayList<String> dateRanges = getDatesFromAgeRange(search.getAgeRange().get(0));
			
			sql += " AND (user.dob BETWEEN '" + dateRanges.get(0) + "' AND '" + dateRanges.get(1) + "'";
			
			for(int i = 1; i < search.getAgeRange().size(); i++) {
				dateRanges = getDatesFromAgeRange(search.getAgeRange().get(i));
				sql += " OR user.dob BETWEEN '" + dateRanges.get(0) + "' AND '" + dateRanges.get(1) + "'";
			}
			
			sql += ")";
		}
		
		
		if (search.getZipcode() != null) {
			sql += " AND ((user.zipcode1 LIKE '" + search.getZipcode().get(0) + "%' OR user.zipcode2 LIKE '"+ search.getZipcode().get(0) +"%')";
			
			for(int i=1; i < search.getZipcode().size(); i++) {
				sql += " OR (user.zipcode1 LIKE '" + search.getZipcode().get(i) + "%' OR user.zipcode2 LIKE '"+ search.getZipcode().get(i) +"%')";
			}
			
			sql += ")";
		}
		
		if(search.getCid() != null) {
			sql +=  " AND (diagnosis.condition_id = '" + search.getCid().get(0) + "'";
			
			for(int i=1; i < search.getCid().size(); i++) {
				sql += " OR diagnosis.condition_id = '" + search.getCid().get(i) + "'";
			}
			
			sql += ")";
		}
		
		if(search.getEthnicity() != null) {
			sql += " AND (user.ethnicity = '" + search.getEthnicity().get(0) + "'";
			
			for (int i = 1; i < search.getEthnicity().size(); i++) {
				sql += " OR user.ethnicity = '" + search.getEthnicity().get(i) + "'";
			}
			
			sql += ")";
			
		}
		
		if (search.getNationality() != null){
			sql += " AND (user.nationality = '" + search.getNationality().get(0) + "'";
			
			for (int i = 1; i < search.getNationality().size(); i++) {
				sql += " OR user.nationality = '" + search.getNationality().get(i) + "'";
			}
			
			sql += ")";

		}
		
		if (search.getDrug_allergy() != null) {
			sql += " AND (user.drug_allergy = '" + search.getDrug_allergy().get(0) + "'";
			
			for(int i = 1; i < search.getDrug_allergy().size(); i++) {
				sql += " OR user.drug_allergy = '" + search.getDrug_allergy().get(i) + "'";
			}
			
			sql += ")";
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
				result.setEthnicity(rs.getString(6));
				result.setNationality(rs.getString(7));
				result.setDrug_allergy(rs.getString(8));
				result.setCondition_name(rs.getString(9));
				result.setTimeseries_path(rs.getString(10));
				
				//HATEOAS STUFF COME BACK LATER
				String uid = rs.getString(11);
				
				results.add(result);
			}
			
					
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		MySQLAccess.close();
		return results;
	}
	
	private ArrayList<String> getDatesFromAgeRange(String ageRange){
		int endRange = Integer.parseInt(ageRange.split("-")[0]);
		int startRange = Integer.parseInt(ageRange.split("-")[1]);
		
		Calendar startDate = Calendar.getInstance();
		Calendar endDate = Calendar.getInstance();
		
		startDate.add(Calendar.YEAR, -startRange);
		endDate.add(Calendar.YEAR, -endRange);
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		
		String start = df.format(startDate.getTime());
		String end = df.format(endDate.getTime());
		
		System.out.println(startRange);
		System.out.println(endRange);
		
		System.out.println("Start Date: " + start);
		System.out.println("End Date: " + end);
		
		ArrayList<String> result = new ArrayList<String>();
		result.add(start);
		result.add(end);
		
		return result;
	}

}
