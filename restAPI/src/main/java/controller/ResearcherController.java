package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.Date;

import entity.Researcher;
import utils.db.MySQLAccess;

public class ResearcherController {
	
	public Researcher login(Researcher login) {
		String sql = "SELECT password, otpsecret FROM researcher WHERE researcher_username = ?";
		Connection connect = MySQLAccess.connectDatabase();
		Researcher researcher = new Researcher();
		
		try {
			PreparedStatement ps = connect.prepareStatement(sql);
			ps.setString(1, login.getResearcher_username());
			ResultSet rs = MySQLAccess.readDataBasePS(ps);
			
			while(rs.next()) {
				researcher.setPassword(rs.getString(1));
				researcher.setOtpsecret(rs.getString(2));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		MySQLAccess.close();
		return researcher;
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
				researcher.setResearcher_id(rs.getString(1));
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
				researcher.setZipcode1(rs.getString(13));
				researcher.setZipcode2(rs.getString(14));
				researcher.setQualification(rs.getString(15));
				researcher.setQualification_name(rs.getString(16));
				
				if(rs.getString(17).equals("1")) {
					researcher.setIsAdmin("true");
				}else {
					researcher.setIsAdmin("false");
				}
				
			}
			
			//System.out.println(researcher.getNric() + researcher.getQualifcation() + researcher.getAddress2());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(researcher.getResearcher_id() != null) {
			sql = "SELECT category_id FROM researcher_category WHERE researcher_id = ? AND approval_status = 'Approved'";
			ArrayList<Integer> researchCategory = new ArrayList<Integer>();
			try {
				PreparedStatement ps = connect.prepareStatement(sql);
				ps.setString(1, researcher.getResearcher_id());
				ResultSet rs = MySQLAccess.readDataBasePS(ps);
				
				while(rs.next()) {
					researchCategory.add(rs.getInt(1));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			researcher.setResearch_category(researchCategory);
		}
		
//		ArrayList<Integer> categories = researcher.getResearch_category();
//		
//		for(int i : categories) {
//			System.out.println("Category ID: " + i);
//		}
		
		MySQLAccess.close();
		return researcher;
	}
	
	public boolean addResearcher(Researcher researcher) {
		String sql = "INSERT INTO researcher (firstname, lastname, researcher_username, password, isAdmin) VALUES (?,?,?,?,0)";
		Connection connect = MySQLAccess.connectDatabase();
		
		try {
			PreparedStatement ps = connect.prepareStatement(sql);
			ps.setString(1, researcher.getFirstname());
			ps.setString(2, researcher.getLastname());
			ps.setString(3, researcher.getResearcher_username());
			ps.setString(4, researcher.getPassword());
			
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
		String sql = "UPDATE researcher SET firstname = ?, lastname = ?, nric = ?, dob = ?, gender = ?,"
				+ "phone1 = ?, phone2 = ?, address1 = ?, address2 = ?, zipcode1 = ?, zipcode2 = ?, qualification = ?, qualification_name = ? "
				+ "WHERE researcher_username = ?";
		
		Connection connect = MySQLAccess.connectDatabase();
		
		try {
			PreparedStatement ps = connect.prepareStatement(sql);
			ps.setString(1, researcher.getFirstname());
			ps.setString(2, researcher.getLastname());
			ps.setString(3, researcher.getNric());
			ps.setDate(4, new java.sql.Date(researcher.getDob().getTime()));
			ps.setString(5, researcher.getGender());
			ps.setString(6, researcher.getPhone1());
			ps.setString(7, researcher.getPhone2());
			ps.setString(8, researcher.getAddress1());
			ps.setString(9, researcher.getAddress2());
			ps.setString(10, researcher.getZipcode1());
			ps.setString(11, researcher.getZipcode2());
			ps.setString(12, researcher.getQualification());
			ps.setString(13, researcher.getQualification_name());
			ps.setString(14, researcher.getResearcher_username());
			
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
	
	public boolean registerOTP(Researcher researcher) {
		String sql = "UPDATE researcher SET otpsecret = ? WHERE researcher_username = ?";
		
		Connection connect = MySQLAccess.connectDatabase();
		
		try {
			PreparedStatement ps = connect.prepareStatement(sql);
			ps.setString(1, researcher.getOtpsecret());
			ps.setString(2, researcher.getResearcher_username());
			
			MySQLAccess.updateDataBasePS(ps);
			
		} catch(Exception e) {
			e.printStackTrace();
			MySQLAccess.close();
			return false;
		}
		
		MySQLAccess.close();
		return true;
	}
	
	public boolean changePassword(Researcher researcher) {
		String sql = "UPDATE researcher SET password = ? WHERE researcher_username = ?";
		
		Connection connect = MySQLAccess.connectDatabase();
		
		try {
			PreparedStatement ps = connect.prepareStatement(sql);
			ps.setString(1, researcher.getPassword());
			ps.setString(2, researcher.getResearcher_username());
			
			MySQLAccess.updateDataBasePS(ps);
			
		} catch(Exception e) {
			e.printStackTrace();
			MySQLAccess.close();
			return false;
		}
		
		MySQLAccess.close();
		return true;
	}
	
	public boolean checkOTPEnabled(String researcher_username) {
		
		String sql = "SELECT otpsecret FROM researcher WHERE researcher_username = ?";
		
		Connection connect = MySQLAccess.connectDatabase();
		
		try {
			PreparedStatement ps = connect.prepareStatement(sql);
			ps.setString(1, researcher_username);
			
			ResultSet rs = MySQLAccess.readDataBasePS(ps);
			
			while(rs.next()) {
				if(rs.getObject(1) == null) {
					MySQLAccess.close();
					return false;
				}else {
					MySQLAccess.close();
					return true;
				}
			}
			
		} catch(Exception e) {
			e.printStackTrace();
			MySQLAccess.close();
			return false;
		}
		
		MySQLAccess.close();
		return false;
	}
	
}
