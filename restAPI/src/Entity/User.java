package Entity;

import java.util.Date;

//@Entity
public class User {
	//Attributes of User Class
	private int uid;
	private String password;
	private String salt;
	private String firstName;
	private String lastName;
	private String nric;
	private Date dob;
	private char gender;
	private String[] phone; //= new String[3];
	private String[] address; //= new String[3];
	private int[] zipcode; //= new String[3];
	private int qualify;
	private String bloodtype;
	private String nfcid;
	
	//Constructors for User Class
	public User(int uid, String password, String salt, String firstName, String lastName, String nric, Date dob,
			char gender, String[] phone, String[] address, int[] zipcode, int qualify, String bloodtype, String nfcid) {
		super();
		this.uid = uid;
		this.password = password;
		this.salt = salt;
		this.firstName = firstName;
		this.lastName = lastName;
		this.nric = nric;
		this.dob = dob;
		this.gender = gender;
		this.phone = phone;
		this.address = address;
		this.zipcode = zipcode;
		this.qualify = qualify;
		this.bloodtype = bloodtype;
		this.nfcid = nfcid;
	}
	
	public User(int uid, String password, String salt, String firstName, String lastName, String nric, Date dob,
			char gender, String[] phone, String[] address, int[] zipcode, int qualify, String bloodtype) {
		super();
		this.uid = uid;
		this.password = password;
		this.salt = salt;
		this.firstName = firstName;
		this.lastName = lastName;
		this.nric = nric;
		this.dob = dob;
		this.gender = gender;
		this.phone = phone;
		this.address = address;
		this.zipcode = zipcode;
		this.qualify = qualify;
		this.bloodtype = bloodtype;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
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

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
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

	public char getGender() {
		return gender;
	}

	public void setGender(char gender) {
		this.gender = gender;
	}

	public String[] getPhone() {
		return phone;
	}

	public void setPhone(String[] phone) {
		this.phone = phone;
	}

	public String[] getAddress() {
		return address;
	}

	public void setAddress(String[] address) {
		this.address = address;
	}

	public int[] getZipcode() {
		return zipcode;
	}

	public void setZipcode(int[] zipcode) {
		this.zipcode = zipcode;
	}

	public int getQualify() {
		return qualify;
	}

	public void setQualify(int qualify) {
		this.qualify = qualify;
	}

	public String getBloodtype() {
		return bloodtype;
	}

	public void setBloodtype(String bloodtype) {
		this.bloodtype = bloodtype;
	}

	public String getNfcid() {
		return nfcid;
	}

	public void setNfcid(String nfcid) {
		this.nfcid = nfcid;
	}
	
	
}
