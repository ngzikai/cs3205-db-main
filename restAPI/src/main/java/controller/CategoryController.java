package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import entity.Category;
import entity.CategoryRequest;
import entity.Condition;
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

}
