package entity;

import java.util.Date;

public class SearchResult {
	private Date dob;
	private String gender;
	private String zipcode1;
	private String zipcode2;
	private String bloodtype;
	private String condition_name;
	private String ethnicity;
	private String nationality;
	private String drug_allergy;
	private String timeseries_path;
	private String heartrate_path;
	
	public SearchResult() {
		
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getZipcode1() {
		return zipcode1;
	}

	public void setZipcode1(String zipcode1) {
		this.zipcode1 = zipcode1;
	}

	public String getZipcode2() {
		return zipcode2;
	}

	public void setZipcode2(String zipcode2) {
		this.zipcode2 = zipcode2;
	}

	public String getBloodtype() {
		return bloodtype;
	}

	public void setBloodtype(String bloodtype) {
		this.bloodtype = bloodtype;
	}

	public String getCondition_name() {
		return condition_name;
	}

	public void setCondition_name(String condition_name) {
		this.condition_name = condition_name;
	}

	public String getEthnicity() {
		return ethnicity;
	}

	public void setEthnicity(String ethnicity) {
		this.ethnicity = ethnicity;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getDrug_allergy() {
		return drug_allergy;
	}

	public void setDrug_allergy(String drug_allergy) {
		this.drug_allergy = drug_allergy;
	}

	public String getTimeseries_path() {
		return timeseries_path;
	}

	public void setTimeseries_path(String timeseries_path) {
		this.timeseries_path = timeseries_path;
	}

	public String getHeartrate_path() {
		return heartrate_path;
	}

	public void setHeartrate_path(String heartrate_path) {
		this.heartrate_path = heartrate_path;
	}




	
}
