package entity;

import java.util.ArrayList;

public class ResearcherCategory {
	public String researcher_id;
	public ArrayList<CategoryStatus> categories;
	
	public ResearcherCategory(){
		
	}

	public ResearcherCategory(String researcher_id, ArrayList<CategoryStatus> categories) {
		super();
		this.researcher_id = researcher_id;
		this.categories = categories;
	}

	public String getResearcher_id() {
		return researcher_id;
	}

	public void setResearcher_id(String researcher_id) {
		this.researcher_id = researcher_id;
	}

	public ArrayList<CategoryStatus> getCategories() {
		return categories;
	}

	public void setCategories(ArrayList<CategoryStatus> categories) {
		this.categories = categories;
	}
	
	

}
