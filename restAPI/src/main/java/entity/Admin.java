package entity;

public class Admin {
	private int adminId;
	private String username;
	private String password;
	
	
	public Admin(int adminId, String username, String password) {
		super();
		this.adminId = adminId;
		this.username = username;
		this.password = password;
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
	
	public void print() {
		System.out.println("Admin id: " + adminId);
		System.out.println("User: " + username);
		System.out.println("Password: " + password);
	}
	

}
