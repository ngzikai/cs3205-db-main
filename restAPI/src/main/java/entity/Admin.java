package entity;

public class Admin {
	private int adminId;
	private String username;
	private String password;
	private String salt;
	private String secret;

	
	public Admin(int adminId, String username, String password, String salt, String secret) {
		super();
		this.adminId = adminId;
		this.username = username;
		this.password = password;
		this.salt = salt;
		this.secret = secret;
	}
	
	public Admin(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public int getAdminId() {
		return adminId;
	}
	
	public String getPassword() {
		return password;
	}
	
	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public void print() {
		System.out.println("Admin id: " + adminId);
		System.out.println("User: " + username);
		System.out.println("Password: " + password);
		System.out.println("Salt: " + salt);
		System.out.println("Secret: " + secret);
	}
	

}
