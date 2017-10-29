package entity;

public class Csrf {
	private String csrfToken;
	private int uid;
	private int expiry;
	private String description;
	
	public Csrf(String csrfToken, int uid, int expiry, String description) {
		super();
		this.csrfToken = csrfToken;
		this.uid = uid;
		this.expiry = expiry;
		this.description = description;
	}
	
	public String getCsrfToken() {
		return csrfToken;
	}
	
	public void setCsrfToken(String csrfToken) {
		this.csrfToken = csrfToken;
	}
	
	public int getUid() {
		return uid;
	}
	
	public void setUid(int uid) {
		this.uid = uid;
	}
	
	public int getExpiry() {
		return expiry;
	}
	
	public void setExpiry(int expiry) {
		this.expiry = expiry;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String toString() {
		return csrfToken + "||" + uid + "||" + expiry;
	}
	
}
