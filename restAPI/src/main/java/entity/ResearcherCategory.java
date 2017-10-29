package entity;

import java.util.ArrayList;

public class ResearcherCategory {
	public String researcher_id;
	public String researcher_username;
	public String firstname;
	public String lastname;
	public String qualification;
	public String qualication_name;
	
	public ArrayList<CategoryStatus> categories;
	
	public ResearcherCategory(){
		
	}

	public ResearcherCategory(String researcher_id, String researcher_username, String firstname, String lastname,
			String qualification, String qualication_name, ArrayList<CategoryStatus> categories) {
		super();
		this.researcher_id = researcher_id;
		this.researcher_username = researcher_username;
		this.firstname = firstname;
		this.lastname = lastname;
		this.qualification = qualification;
		this.qualication_name = qualication_name;
		this.categories = categories;
	}

	public String getResearcher_id() {
		return researcher_id;
	}

	public void setResearcher_id(String researcher_id) {
		this.researcher_id = researcher_id;
	}

	public String getResearcher_username() {
		return researcher_username;
	}

	public void setResearcher_username(String researcher_username) {
		this.researcher_username = researcher_username;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getQualification() {
		return qualification;
	}

	public void setQualification(String qualification) {
		this.qualification = qualification;
	}

	public String getQualication_name() {
		return qualication_name;
	}

	public void setQualication_name(String qualication_name) {
		this.qualication_name = qualication_name;
	}

	public ArrayList<CategoryStatus> getCategories() {
		return categories;
	}

	public void setCategories(ArrayList<CategoryStatus> categories) {
		this.categories = categories;
	}

}
