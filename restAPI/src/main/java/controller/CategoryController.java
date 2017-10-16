package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import entity.Category;
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
				results.add(new Category(rs.getInt(1), rs.getString(2)));
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

}
