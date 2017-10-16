package entity;

import java.time.LocalDate;
import javax.xml.bind.annotation.XmlRootElement;

//@Entity
@XmlRootElement
public class User {
	//Attributes of User Class
	private int uid;
	private String username;
	private String password;
	private String salt;	
	private String password2;
	private String salt2;
	private String firstName;
	private String lastName;
	private String nric;
	private LocalDate dob;
	private char gender;
	private String[] phone; 
	private String[] address; 
	private int[] zipcode; 
	private int qualify;
	private String bloodtype;
	private String nfcid;
	private String secret;
	
	//Constructors for User Class
	public User() {
		
	}

	public User(int uid, String password, String salt, int qualify, String secret) {
		super();
		this.uid = uid;
		this.password = password;
		this.salt = salt;
		this.qualify = qualify;
		this.secret = secret;
	}

	public User(int uid, String firstName, String lastName, char gender, String phone, int qualify) {
		super();
		this.uid = uid;
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
		this.phone = new String[3];
		this.phone[0] = phone;
		this.qualify = qualify;
	}

	//Constructor without UID. Most likely used for creating a new user entry.
	public User(String username, String password, String salt, String firstName, String lastName, String nric,
			LocalDate dob, char gender, String phone1, String phone2, String phone3, String address1, String address2,
			String address3, int zipcode1, int zipcode2, int zipcode3, int qualify, String bloodtype,
			String nfcid) {
		super();
		this.username = username;
		this.password = password;
		this.salt = salt;
		this.firstName = firstName;
		this.lastName = lastName;
		this.nric = nric;
		this.dob = dob;
		this.gender = gender;
		this.phone = new String[3];
		phone[0] = phone1;
		phone[1] = validateString(phone2);
		phone[2] = validateString(phone3);
		this.address = new String[3];
		address[0] = address1;
		address[1] = validateString(address2);
		address[2] = validateString(address3);
		this.zipcode = new int[3];
		zipcode[0] = zipcode1;
		zipcode[1] = zipcode2;
		zipcode[2] = zipcode3;
		this.qualify = qualify;
		this.bloodtype = bloodtype;
		this.nfcid = validateString(nfcid);
	}
	
	public User(int uid, String username, String password, String salt, String firstName, String lastName, String nric,
			LocalDate dob, char gender, String phone1, String phone2, String phone3, String address1, String address2,
			String address3, int zipcode1, int zipcode2, int zipcode3, int qualify, String bloodtype,
			String nfcid) {
		super();
		this.uid = uid;
		this.username = username;
		this.password = password;
		this.salt = salt;
		this.firstName = firstName;
		this.lastName = lastName;
		this.nric = nric;
		this.dob = dob;
		this.gender = gender;
		this.phone = new String[3];
		phone[0] = validateString(phone1);
		phone[1] = validateString(phone2);
		phone[2] = validateString(phone3);
		this.address = new String[3];
		address[0] = address1;
		address[1] = validateString(address2);
		address[2] = validateString(address3);
		this.zipcode = new int[3];
		zipcode[0] = zipcode1;
		zipcode[1] = zipcode2;
		zipcode[2] = zipcode3;
		this.qualify = qualify;
		this.bloodtype = bloodtype;
		this.nfcid = validateString(nfcid);
	}
	

	public User(int uid, String firstName, String lastName, char gender) {
		super();
		this.uid = uid;
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
	}

	public User(int uid, String username, String password, String salt, String firstName, String lastName, String nric,
			LocalDate dob, char gender, String[] phone, String[] address, int[] zipcode, int qualify, String bloodtype,
			String nfcid, String secret) {
		super();
		this.uid = uid;
		this.username = username;
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
		this.secret = secret;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
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

	public LocalDate getDob() {
		return dob;
	}

	public void setDob(LocalDate dob) {
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}
		
	public String getPassword2() {
		return password2;
	}

	public void setPassword2(String password2) {
		this.password2 = password2;
	}

	public String getSalt2() {
		return salt2;
	}

	public void setSalt2(String salt2) {
		this.salt2 = salt2;
	}

	public void print() {
		System.out.println("User id: " + uid);
		System.out.println("User: " + username);
		System.out.println("Password: " + password);
		System.out.println("Salt: " + salt);
		System.out.println("FirstName: " + firstName);
		System.out.println("LastName: " + lastName);
		System.out.println("NRIC: " + nric);
		System.out.println("DOB: " + dob);
		System.out.println("Gender: " + gender);
		System.out.println("Phone: " + allPhoneToString());
		System.out.println("Address: " + allAddressToString());
		System.out.println("Zipcode: " + allZipcodeToString());
		System.out.println("Qualify: " + qualify);
		System.out.println("Blood Type: " + bloodtype);
		System.out.println("NFCID: " + nfcid);
		System.out.println("Secret: " + secret);
	}
	
	public String allPhoneToString() {
		return phone[0] + ", " + phone[1] + ", " + phone[2];
	}
	
	public String allAddressToString() {
		return address[0] + ", " + address[1] + ", " + address[2];
	}
	
	public String allZipcodeToString() {
		return zipcode[0] + ", " + zipcode[1] + ", " + zipcode[2];
	}
	
	public String validateString(String s) {
		if(s.equals("-")) {
			return "";
		}
		return s;
	}
	
	
}
