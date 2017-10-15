package entity;

import java.util.ArrayList;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Search {
	private String startDob;
	private String endDob;
	private ArrayList<String> gender;
	private ArrayList<String> bloodType;
	private ArrayList<String> zipcode;
	private ArrayList<String> cid;
	
	public Search() {
		
	}
	
	public Search(String startDob, String endDob, ArrayList<String> gender, ArrayList<String> bloodType,
			ArrayList<String> zipcode, ArrayList<String> cid) {
		super();
		this.startDob = startDob;
		this.endDob = endDob;
		this.gender = gender;
		this.bloodType = bloodType;
		this.zipcode = zipcode;
		this.cid = cid;
	}



	public String getStartDob() {
		return startDob;
	}

	public void setStartDob(String startDob) {
		this.startDob = startDob;
	}

	public String getEndDob() {
		return endDob;
	}

	public void setEndDob(String endDob) {
		this.endDob = endDob;
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

	public String toString() {
		String s = "";
		s += "Start dob:" + getStartDob() + "\n";
		s += "End dob: " + getEndDob() + "\n";
		s += "Gender: " + getGender() + "\n";
		s += "Blood Type: " + getBloodType() + "\n";
		s += "Zip Code: " + getZipcode() + "\n";
		s += "Condition ID: " + getCid() + "\n";
		
		return s;
	}
}
