package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import entity.Researcher;
import utils.db.MySQLAccess;

public class ResearcherController {
	
	public Researcher login(Researcher loginResearcher) {
		String sql = "SELECT * FROM researcher WHERE researcher_username = ? AND password = ?";
		Researcher researcher = new Researcher();
		Connection connect = MySQLAccess.connectDatabase();
		
		//GET RESEARCHER ENTITY BASED ON USERNAME
		try {
			PreparedStatement ps = connect.prepareStatement(sql);
			ps.setString(1, loginResearcher.getResearcher_username());
			ps.setString(2, loginResearcher.getPassword());
			ResultSet rs = MySQLAccess.readDataBasePS(ps);
			
			while (rs.next()) {
				researcher.setResearcher_id(rs.getInt(1));
				researcher.setResearcher_username(rs.getString(2));
				researcher.setPassword(rs.getString(3));
				researcher.setSalt(rs.getString(4));
				researcher.setFirstname(rs.getString(5));
				researcher.setLastname(rs.getString(6));
				researcher.setNric(rs.getString(7));
				researcher.setDob(rs.getString(8));
				researcher.setGender(rs.getString(9));
				researcher.setPhone1(rs.getString(10));
				researcher.setPhone2(rs.getString(11));
				researcher.setAddress1(rs.getString(12));
				researcher.setAddress2(rs.getString(13));
				researcher.setZipcode1(rs.getString(14));
				researcher.setZipcode2(rs.getString(15));
				researcher.setQualifcation(rs.getString(16));
				researcher.setQualification_name(rs.getString(17));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(researcher.getResearcher_id() >= 0) {
			sql = "SELECT category_id from researcher_category WHERE researcher_id = ? AND approval_status = 'Approved'";
			ArrayList<Integer> researchCategory = new ArrayList<Integer>();
			try {
				PreparedStatement ps = connect.prepareStatement(sql);
				ps.setInt(1, researcher.getResearcher_id());
				ResultSet rs = MySQLAccess.readDataBasePS(ps);
				
				
				
				while(rs.next()) {
					researchCategory.add(rs.getInt(1));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			researcher.setResearchCategory(researchCategory);
		}
		
		return researcher;
		
	}
}
