package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.Date;

import entity.Researcher;
import utils.db.MySQLAccess;

public class ResearcherController {
	
	public String login(String username) {
		String sql = "SELECT password FROM researcher WHERE researcher_username = ?";
		Connection connect = MySQLAccess.connectDatabase();
		String password = "false";
		
		try {
			PreparedStatement ps = connect.prepareStatement(sql);
			ps.setString(1, username);
			ResultSet rs = MySQLAccess.readDataBasePS(ps);
			
			while(rs.next()) {
				password = rs.getString(1);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		MySQLAccess.close();
		return password;
	}
	
	
	public Researcher getResearcher(String username) {
		String sql = "SELECT * FROM researcher WHERE researcher_username = ? ";
		Researcher researcher = new Researcher();
		Connection connect = MySQLAccess.connectDatabase();
		
		//GET RESEARCHER ENTITY BASED ON USERNAME
		try {
			PreparedStatement ps = connect.prepareStatement(sql);
			ps.setString(1, username);
			ResultSet rs = MySQLAccess.readDataBasePS(ps);
			
			//System.out.println("Sucessfully retrieved user!");
			
			while (rs.next()) {
				researcher.setResearcher_id(rs.getInt(1));
				researcher.setResearcher_username(rs.getString(2));
				researcher.setPassword(rs.getString(3));
				researcher.setFirstname(rs.getString(4));
				researcher.setLastname(rs.getString(5));
				researcher.setNric(rs.getString(6));
				researcher.setDob(new Date(rs.getDate(7).getTime()));
				researcher.setGender(rs.getString(8));
				researcher.setPhone1(rs.getString(9));
				researcher.setPhone2(rs.getString(10));
				researcher.setAddress1(rs.getString(11));
				researcher.setAddress2(rs.getString(12));
				researcher.setZipcode1(rs.getInt(13));
				researcher.setZipcode2(rs.getInt(14));
				researcher.setQualification(rs.getString(15));
				researcher.setQualification_name(rs.getString(16));
			}
			
			//System.out.println(researcher.getNric() + researcher.getQualifcation() + researcher.getAddress2());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(researcher.getResearcher_id() >= 0) {
			sql = "SELECT category_id FROM researcher_category WHERE researcher_id = ? AND approval_status = 'Approved'";
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
		
		ArrayList<Integer> categories = researcher.getResearchCategory();
		
		for(int i : categories) {
			System.out.println("Category ID: " + i);
		}
		
		MySQLAccess.close();
		return researcher;
	}
	
	public boolean addResearcher(Researcher researcher) {
		String sql = "INSERT INTO researcher (researcher_username, password) VALUES (?,?)";
		Connection connect = MySQLAccess.connectDatabase();
		
		try {
			PreparedStatement ps = connect.prepareStatement(sql);
			ps.setString(1, researcher.getResearcher_username());
			ps.setString(2, researcher.getPassword());
			
			MySQLAccess.updateDataBasePS(ps);
		} catch (Exception e) {
			e.printStackTrace();
			MySQLAccess.close();
			return false;
		}
		
		MySQLAccess.close();
		return true;
		
	}
	
	
	public boolean updateResearcher(Researcher researcher) {
		String sql = "UPDATE researcher SET password = ?, firstname = ?, lastname = ?, nric = ?, dob = ?, gender = ?,"
				+ "phone1 = ?, phone2 = ?, address1 = ?, address2 = ?, zipcode1 = ?, zipcode2 = ?, qualification = ?, qualification_name = ? "
				+ "WHERE researcher_username = ?";
		
		Connection connect = MySQLAccess.connectDatabase();
		
		try {
			PreparedStatement ps = connect.prepareStatement(sql);
			ps.setString(1, researcher.getPassword());
			ps.setString(2, researcher.getFirstname());
			ps.setString(3, researcher.getLastname());
			ps.setString(4, researcher.getNric());
			ps.setDate(5, new java.sql.Date(researcher.getDob().getTime()));
			ps.setString(6, researcher.getGender());
			ps.setString(7, researcher.getPhone1());
			ps.setString(8, researcher.getPhone2());
			ps.setString(9, researcher.getAddress1());
			ps.setString(10, researcher.getAddress2());
			ps.setInt(11, researcher.getZipcode1());
			ps.setInt(12, researcher.getZipcode2());
			ps.setString(13, researcher.getQualification());
			ps.setString(14, researcher.getQualification_name());
			ps.setString(15, researcher.getResearcher_username());
			
			System.out.println(ps.toString());
			MySQLAccess.updateDataBasePS(ps);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			MySQLAccess.close();
			return false;
		}
		
		MySQLAccess.close();
		return true;
	}
	
	public boolean deleteResearcher(int researcher_id) {
		String sql = "DELETE FROM researcher WHERE researcher_id = ?";
		
		Connection connect = MySQLAccess.connectDatabase();
		
		try {
			PreparedStatement ps = connect.prepareStatement(sql);
			ps.setInt(1, researcher_id);
			
			MySQLAccess.updateDataBasePS(ps);
		}catch (Exception e) {
			e.printStackTrace();
			MySQLAccess.close();
			return false;
		}
		
		MySQLAccess.close();
		return true;
	}
}
