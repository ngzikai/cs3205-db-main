package entity;

public class Consent {
	private int consentId;
	private int uid;
	private int rid;
	private boolean status;
	
	public Consent() {}
	
	public Consent(int consentId, int uid, int rid, boolean status) {
		super();
		this.consentId = consentId;
		this.uid = uid;
		this.rid = rid;
		this.status = status;
	}
	
	public Consent(int uid, int rid, boolean status) {
		super();
		this.uid = uid;
		this.rid = rid;
		this.status = status;
	}

	public int getConsentId() {
		return consentId;
	}
	
	public void setConsentId(int consentId) {
		this.consentId = consentId;
	}
	
	public int getUid() {
		return uid;
	}
	
	public void setUid(int uid) {
		this.uid = uid;
	}
	
	public int getRid() {
		return rid;
	}
	
	public void setRid(int rid) {
		this.rid = rid;
	}
	
	public boolean isStatus() {
		return status;
	}
	
	public void setStatus(boolean status) {
		this.status = status;
	}
}
