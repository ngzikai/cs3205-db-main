package entity;

import java.util.ArrayList;

public class Category {
	private int category_id;
	private String category_name;
	private ArrayList<Condition> conditions;
	
	public Category() {
		
	}

	public Category(int category_id, String category_name) {
		super();
		this.category_id = category_id;
		this.category_name = category_name;
		conditions =  new ArrayList<Condition>();
	}

	public int getCategory_id() {
		return category_id;
	}

	public void setCategory_id(int category_id) {
		this.category_id = category_id;
	}

	public String getCategory_name() {
		return category_name;
	}

	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}

	public ArrayList<Condition> getConditions() {
		return conditions;
	}

	public void setConditions(ArrayList<Condition> conditions) {
		this.conditions = conditions;
	}
	
	public void addCondition(Condition condition) {
		conditions.add(condition);
	}
}
