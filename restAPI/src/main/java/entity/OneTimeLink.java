package entity;

public class OneTimeLink {
	private String token;
	private String filepath;
	private String csrf;
	private int uid;
	
	public OneTimeLink(String token, String filepath, String csrf, int uid) {
		super();
		this.token = token;
		this.filepath = filepath;
		this.csrf = csrf;
		this.uid = uid;
	}

	public String getToken() {
		return token;
	}
	
	public void setToken(String token) {
		this.token = token;
	}
	
	public String getFilepath() {
		return filepath;
	}
	
	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}
	
	public String getCsrf() {
		return csrf;
	}
	
	public void setCsrf(String csrf) {
		this.csrf = csrf;
	}
	
	public int getUid() {
		return uid;
	}
	
	public void setUid(int uid) {
		this.uid = uid;
	}
	
	public String toString() {
		return token + "||" + uid + "||" + filepath + "||" + csrf;
	}
	
}
