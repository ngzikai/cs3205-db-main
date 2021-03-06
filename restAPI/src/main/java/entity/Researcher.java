package entity;

import java.util.ArrayList;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Researcher {
	private String researcher_id;
	private String researcher_username;
	private String password;
	private String firstname;
	private String lastname;
	private String nric;
	private Date dob;
	private String gender;
	private String phone1;
	private String phone2;
	private String address1;
	private String address2;
	private String zipcode1;
	private String zipcode2;
	private String qualification;
	private String qualification_name;
	private String isAdmin;
	private String otpsecret;
	private ArrayList<Integer> research_category;
	
	public Researcher() {
		
	}
	
	public Researcher(String researcher_username, String password) {
		super();
		this.researcher_username = researcher_username;
		this.password = password;
	}

	public Researcher(String researcher_id, String researcher_username, String password, String firstname,
			String lastname, String nric, Date dob, String gender, String phone1, String phone2, String address1,
			String address2, String zipcode1, String zipcode2, String qualification, String qualification_name,
			String isAdmin, String otpsecret, ArrayList<Integer> research_category) {
		super();
		this.researcher_id = researcher_id;
		this.researcher_username = researcher_username;
		this.password = password;
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
		this.qualification = qualification;
		this.qualification_name = qualification_name;
		this.isAdmin = isAdmin;
		this.otpsecret = otpsecret;
		this.research_category = research_category;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public String getQualification() {
		return qualification;
	}

	public void setQualification(String qualification) {
		this.qualification = qualification;
	}

	public String getQualification_name() {
		return qualification_name;
	}

	public void setQualification_name(String qualification_name) {
		this.qualification_name = qualification_name;
	}

	public String getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(String isAdmin) {
		this.isAdmin = isAdmin;
	}

	public String getOtpsecret() {
		return otpsecret;
	}

	public void setOtpsecret(String otpsecret) {
		this.otpsecret = otpsecret;
	}

	public ArrayList<Integer> getResearch_category() {
		return research_category;
	}

	public void setResearch_category(ArrayList<Integer> research_category) {
		this.research_category = research_category;
	}

	
	


}
