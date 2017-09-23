package entity;

public class Admin {
	private int adminId;
	private String username;
	private String password;
	private String salt;
	
	
	public Admin(int adminId, String username, String password, String salt) {
		super();
		this.adminId = adminId;
		this.username = username;
		this.password = password;
		this.salt = salt;
	}
	
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
	public int getAdminId() {
		return adminId;
	}
	public String getPassword() {
		return password;
	}
	public String getSalt() {
		return salt;
	}
	
	public void print() {
		System.out.println("Admin id: " + adminId);
		System.out.println("User: " + username);
		System.out.println("Password: " + password);
		System.out.println("Salt: " + salt);
	}
	

}
