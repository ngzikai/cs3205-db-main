package entity;

import java.util.Date;

public class SearchResult {
	private Date dob;
	private String gender;
	private String zipcode1;
	private String zipcode2;
	private String bloodtype;
	private String condition_name;
	private String timeseries_path;
	
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

	public String getTimeseries_path() {
		return timeseries_path;
	}

	public void setTimeseries_path(String timeseries_path) {
		this.timeseries_path = timeseries_path;
	}
	
	
}
