package cs3205.db.restapi.team2.model;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Researcher {
	private int researcher_id;
	private String researcher_username;
	private String password;
	private String salt;
	private String firstname;
	private String lastname;
	private String nric;
	private String dob;
	private String gender;
	private String phone1;
	private String phone2;
	private String address1;
	private String address2;
	private String zipcode1;
	private String zipcode2;
	private String qualifcation;
	private String qualification_name;
	private ArrayList<String> research_category;
	
	public Researcher() {
		
	}
	
	public Researcher(int researcher_id, String researcher_username, String password, String salt, String firstname,
			String lastname, String nric, String dob, String gender, String phone1, String phone2, String address1,
			String address2, String zipcode1, String zipcode2, String qualifcation, String qualification_name,
			ArrayList<String> research_category) {
		super();
		this.researcher_id = researcher_id;
		this.researcher_username = researcher_username;
		this.password = password;
		this.salt = salt;
		this.firstname = firstname;
		this.lastname = lastname;
		this.nric = nric;
		this.dob = dob;
		this.gender = gender;
		this.phone1 = phone1;
		this.phone2 = phone2;
		this.address1 = address1;
		this.address2 = address2;
		this.zipcode1 = zipcode1;
		this.zipcode2 = zipcode2;
		this.qualifcation = qualifcation;
		this.qualification_name = qualification_name;
		this.research_category = research_category;
	}

	public int getResearcher_id() {
		return researcher_id;
	}
	public void setResearcher_id(int researcher_id) {
		this.researcher_id = researcher_id;
	}
	public String getResearcher_username() {
		return researcher_username;
	}
	public void setResearcher_username(String researcher_username) {
		this.researcher_username = researcher_username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSalt() {
		return salt;
	}
	public void setSalt(String salt) {
		this.salt = salt;
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
	public String getNric() {
		return nric;
	}
	public void setNric(String nric) {
		this.nric = nric;
	}
	public String getDob() {
		return dob;
	}
	public void setDob(String dob) {
		this.dob = dob;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getPhone1() {
		return phone1;
	}
	public void setPhone1(String phone1) {
		this.phone1 = phone1;
	}
	public String getPhone2() {
		return phone2;
	}
	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}
	public String getAddress1() {
		return address1;
	}
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	public String getAddress2() {
		return address2;
	}
	public void setAddress2(String address2) {
		this.address2 = address2;
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
	public String getQualifcation() {
		return qualifcation;
	}
	public void setQualifcation(String qualifcation) {
		this.qualifcation = qualifcation;
	}
	public String getQualification_name() {
		return qualification_name;
	}
	public void setQualification_name(String qualification_name) {
		this.qualification_name = qualification_name;
	}

}
