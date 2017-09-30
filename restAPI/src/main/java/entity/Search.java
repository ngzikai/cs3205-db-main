package entity;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Search {
	private Date startDob;
	private Date endDob;
	private String bloodType;
	private int zipcode;
	private int conditionID;
	
	public Search() {
		
	}
	
	public Search(Date startDob, Date endDob, String bloodType, int zipcode, int conditionID)  {
		super();
		this.startDob = startDob;
		this.endDob = endDob;
		this.bloodType = bloodType;
		this.zipcode = zipcode;
		this.conditionID = conditionID; 
	}

	public Date getStartDob() {
		return startDob;
	}

	public void setStartDob(Date startDob) {
		this.startDob = startDob;
	}

	public Date getEndDob() {
		return endDob;
	}

	public void setEndDob(Date endDob) {
		this.endDob = endDob;
	}

	public String getBloodType() {
		return bloodType;
	}

	public void setBloodType(String bloodType) {
		this.bloodType = bloodType;
	}

	public int getZipcode() {
		return zipcode;
	}

	public void setZipcode(int zipcode) {
		this.zipcode = zipcode;
	}
	
	public int getConditionId() {
		return this.conditionID;
	}
	
	public void setConditionId(int conditionID) {
		this.conditionID = conditionID;
	}
}
