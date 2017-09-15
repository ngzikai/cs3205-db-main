package Entity;

public class Admin {
	private String admin_id;
	private String username;
	private String password;
	private String salt;
	
	
	public Admin(String username, String password, String salt) {
		super();
		this.username = username;
		this.password = password;
		this.salt = salt;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getAdmin_id() {
		return admin_id;
	}
	public String getPassword() {
		return password;
	}
	public String getSalt() {
		return salt;
	}
	

}
