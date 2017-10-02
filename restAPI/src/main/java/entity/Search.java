package entity;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Search {
	private String startDob;
	private String endDob;
	private String gender;
	private String bloodType;
	private String zipcode;
	private String cid;
	
	public Search() {
		
	}
	
	public Search(String startDob, String endDob, String gender, String bloodType, String zipcode, String cid)  {
		super();
		this.startDob = startDob;
		this.endDob = endDob;
		this.gender = gender;
		this.bloodType = bloodType;
		this.zipcode = zipcode;
		this.cid = cid; 
		
		System.out.println("Start Date: " + startDob);
		System.out.println("End Date: " + endDob);
		System.out.println("Gender: " + gender);
		System.out.println("Bloodtype: " + bloodType);
		System.out.println("Zipcode: " + zipcode);
		System.out.println("Condition ID: " + cid);
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
	
	public String getGender() {
		return this.gender;
	}
	
	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getBloodType() {
		return bloodType;
	}

	public void setBloodType(String bloodType) {
		this.bloodType = bloodType;
	}

	public String getZipcode() {
		if (zipcode == null) {
			return null;
		}
		
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	
	
	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
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
