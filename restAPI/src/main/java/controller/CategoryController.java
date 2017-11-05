package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import entity.Category;
import entity.CategoryRequest;
import entity.CategoryStatus;
import entity.Condition;
import entity.Researcher;
import entity.ResearcherCategory;
import utils.db.MySQLAccess;

public class CategoryController {
	
	public ArrayList<Category> listAllCategories(){
		String sql = "SELECT * FROM category";
		Connection connect = MySQLAccess.connectDatabase();
		ArrayList<Category> results = new ArrayList<Category>();
		
		try {
			PreparedStatement ps = connect.prepareStatement(sql);
			ResultSet rs = MySQLAccess.readDataBasePS(ps);
			
			while(rs.next()) {
				Category newCategory = new Category(rs.getInt(1), rs.getString(2));
				newCategory.setConditions(null);
				results.add(newCategory);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		MySQLAccess.close();
		return results;
	}
	
	public ArrayList<Condition> listConditionsFromCategory(int category_id){
		String sql = "SELECT `condition`.condition_id, `condition`.condition_name  "
				+ "FROM `condition`, condition_category, category "
				+ "WHERE `condition`.condition_id = condition_category.condition_id "
				+ "AND condition_category.category_id = category.category_id "
				+ "AND  condition_category.category_id = ?";
		
		Connection connect = MySQLAccess.connectDatabase();
		ArrayList<Condition> results = new ArrayList<Condition>();
		
		try {
			PreparedStatement ps = connect.prepareStatement(sql);
			ps.setInt(1, category_id);
			
			ResultSet rs = MySQLAccess.readDataBasePS(ps);
			
			while(rs.next()) {
				results.add(new Condition(rs.getInt(1), rs.getString(2)));
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		MySQLAccess.close();
		return results;	
	}
	
	public ArrayList<Category> listAllCategoryConditions(){
		ArrayList<Category> categories = listAllCategories();
		
		for (Category category : categories) {
			category.setConditions(listConditionsFromCategory(category.getCategory_id()));
		}
		
		return categories;
	}
	
	public String newRequest(CategoryRequest request) {
		String sql = "SELECT * FROM researcher_category WHERE researcher_id = ? AND category_id = ?";
		
		Connection connect = MySQLAccess.connectDatabase();
		
		try {
			PreparedStatement ps = connect.prepareStatement(sql);
			ps.setString(1, request.getResearcher_id());
			ps.setString(2, request.getCategory_id());
			
			ResultSet rs = MySQLAccess.readDataBasePS(ps);
			
			int rowCount = 0;
			
			while(rs.next()) {
				rowCount++;
			}
			
			if(rowCount > 0) {
				return "Failed";
			}else {
				sql = "INSERT INTO researcher_category (researcher_id, category_id, approval_status) VALUES (?,?, 'Pending')";
				ps = connect.prepareStatement(sql);
				ps.setString(1, request.getResearcher_id());
				ps.setString(2, request.getCategory_id());
				
				MySQLAccess.updateDataBasePS(ps);
			}
		} catch (Exception e){
			e.printStackTrace();
			MySQLAccess.close();
			return "Failed";
		}
		
		MySQLAccess.close();
		return "Success";
		
	}
	
	public String approveRequest(CategoryRequest request) {
		String sql = "SELECT * FROM researcher_category WHERE researcher_id = ? AND category_id = ?";
		
		Connection connect = MySQLAccess.connectDatabase();
		
		try {
			PreparedStatement ps = connect.prepareStatement(sql);
			ps.setString(1, request.getResearcher_id());
			ps.setString(2, request.getCategory_id());
			
			ResultSet rs = MySQLAccess.readDataBasePS(ps);
			
			int rowCount = 0;
			
			while(rs.next()) {
				rowCount++;
			}
						
			if (rowCount != 1) {
				return "Failed";
			} else {
				sql = "UPDATE researcher_category SET approval_status = 'Approved' WHERE researcher_id = ? AND category_id = ?";
				ps = connect.prepareStatement(sql);
				ps.setString(1, request.getResearcher_id());
				ps.setString(2, request.getCategory_id());
				
				MySQLAccess.updateDataBasePS(ps);
			}
		} catch (Exception e){
			e.printStackTrace();
			MySQLAccess.close();
			return "Failed";
		}
		
		MySQLAccess.close();
		return "Success";
	}
	
	public String declineRequest(CategoryRequest request) {
		String sql = "SELECT * FROM researcher_category WHERE researcher_id = ? AND category_id = ?";
		
		Connection connect = MySQLAccess.connectDatabase();
		
		try {
			PreparedStatement ps = connect.prepareStatement(sql);
			ps.setString(1, request.getResearcher_id());
			ps.setString(2, request.getCategory_id());
			
			ResultSet rs = MySQLAccess.readDataBasePS(ps);
			
			int rowCount = 0;
			
			while(rs.next()) {
				rowCount++;
			}
						
			if (rowCount != 1) {
				return "Failed";
			} else {
				sql = "UPDATE researcher_category SET approval_status = 'Not Approved' WHERE researcher_id = ? AND category_id = ?";
				ps = connect.prepareStatement(sql);
				ps.setString(1, request.getResearcher_id());
				ps.setString(2, request.getCategory_id());
				
				MySQLAccess.updateDataBasePS(ps);
			}
		} catch (Exception e){
			e.printStackTrace();
			MySQLAccess.close();
			return "Failed";
		}
		
		MySQLAccess.close();
		return "Success";
	}
	
	public ResearcherCategory getResearcherCategories(String researcher_id){
		String sql = "SELECT researcher_category.category_id, category.category_name, researcher_category.approval_status, researcher.researcher_username\n" + 
				"FROM researcher_category, category, researcher WHERE researcher_category.category_id = category.category_id "
				+ "AND researcher.researcher_id = researcher_category.researcher_id "
				+ "AND researcher_category.researcher_id = ?";
		
		Connection connect = MySQLAccess.connectDatabase();
		
		ArrayList<CategoryStatus> categories = new ArrayList<CategoryStatus>();
		ResearcherCategory researcher;
		String researcher_username = "";
		
		try {
			PreparedStatement ps = connect.prepareStatement(sql);
			ps.setString(1, researcher_id);
			
			ResultSet rs = MySQLAccess.readDataBasePS(ps);
			
			
			
			while (rs.next()) {
				categories.add(new CategoryStatus(rs.getString(1), rs.getString(2), rs.getString(3)));
				researcher_username = rs.getString(4);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			MySQLAccess.close();
			return null;
		}
		
		ResearcherController rc = new ResearcherController();
		Researcher currResearcher = rc.getResearcher(researcher_username);
		
		if(categories.isEmpty()) {
			return null;
		} else {
			researcher =  new ResearcherCategory(currResearcher.getResearcher_id(),currResearcher.getResearcher_username(), currResearcher.getFirstname(), currResearcher.getLastname(), currResearcher.getQualification(), currResearcher.getQualification_name(), categories);
		}
		
		MySQLAccess.close();
		return researcher;
	}
	
	public ArrayList<ResearcherCategory> getAllResearcherCategories(){
		String sql = "SELECT DISTINCT researcher_id FROM researcher_category";
		
		Connection connect = MySQLAccess.connectDatabase();
		ArrayList<ResearcherCategory> list = new ArrayList<ResearcherCategory>();
		
		try {
			PreparedStatement ps = connect.prepareStatement(sql);
			
			ResultSet rs = MySQLAccess.readDataBasePS(ps);
			ArrayList<String> researchers = new ArrayList<String>();
			
			while(rs.next()) {
				researchers.add(rs.getString(1));
			}
			
			for(String researcher : researchers) {
				list.add(getResearcherCategories(researcher));
			}
			
		} catch(Exception e) {
			e.printStackTrace();
			MySQLAccess.close();
			return null;
		}
		
		return list;
	}

}
