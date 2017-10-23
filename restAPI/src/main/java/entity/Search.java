package entity;

import java.util.ArrayList;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Search {
	private ArrayList<String> ageRange;
	private ArrayList<String> gender;
	private ArrayList<String> bloodType;
	private ArrayList<String> zipcode;
	private ArrayList<String> cid;
	
	public Search() {
		
	}

	public Search(ArrayList<String> ageRange, ArrayList<String> gender, ArrayList<String> bloodType,
			ArrayList<String> zipcode, ArrayList<String> cid) {
		super();
		this.ageRange = ageRange;
		this.gender = gender;
		this.bloodType = bloodType;
		this.zipcode = zipcode;
		this.cid = cid;
	}

	public ArrayList<String> getAgeRange() {
		return ageRange;
	}

	public void setAgeRange(ArrayList<String> ageRange) {
		this.ageRange = ageRange;
	}

	public ArrayList<String> getGender() {
		return gender;
	}

	public void setGender(ArrayList<String> gender) {
		this.gender = gender;
	}

	public ArrayList<String> getBloodType() {
		return bloodType;
	}

	public void setBloodType(ArrayList<String> bloodType) {
		this.bloodType = bloodType;
	}

	public ArrayList<String> getZipcode() {
		return zipcode;
	}

	public void setZipcode(ArrayList<String> zipcode) {
		this.zipcode = zipcode;
	}

	public ArrayList<String> getCid() {
		return cid;
	}

	public void setCid(ArrayList<String> cid) {
		this.cid = cid;
	}
	
	
}
