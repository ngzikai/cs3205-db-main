package entity;

public class Csrf {
	private String csrfToken;
	private int uid;
	private int expiry;
	
	public Csrf(String csrfToken, int uid, int expiry) {
		super();
		this.csrfToken = csrfToken;
		this.uid = uid;
		this.expiry = expiry;
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
	
	public String toString() {
		return csrfToken + "||" + uid + "||" + expiry;
	}
	
}
