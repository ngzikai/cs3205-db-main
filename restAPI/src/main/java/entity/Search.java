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
	private ArrayList<String> ethnicity;
	private ArrayList<String> nationality;
	private ArrayList<String> drug_allergy;
	
	public Search() {
		
	}

	public Search(ArrayList<String> ageRange, ArrayList<String> gender, ArrayList<String> bloodType,
			ArrayList<String> zipcode, ArrayList<String> cid, ArrayList<String> ethnicity,
			ArrayList<String> nationality, ArrayList<String> drug_allergy) {
		super();
		this.ageRange = ageRange;
		this.gender = gender;
		this.bloodType = bloodType;
		this.zipcode = zipcode;
		this.cid = cid;
		this.ethnicity = ethnicity;
		this.nationality = nationality;
		this.drug_allergy = drug_allergy;
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

	public ArrayList<String> getEthnicity() {
		return ethnicity;
	}

	public void setEthnicity(ArrayList<String> ethnicity) {
		this.ethnicity = ethnicity;
	}

	public ArrayList<String> getNationality() {
		return nationality;
	}

	public void setNationality(ArrayList<String> nationality) {
		this.nationality = nationality;
	}

	public ArrayList<String> getDrug_allergy() {
		return drug_allergy;
	}

	public void setDrug_allergy(ArrayList<String> drug_allergy) {
		this.drug_allergy = drug_allergy;
	}



	
}
