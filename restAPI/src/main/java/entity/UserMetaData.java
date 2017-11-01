package entity;

public class UserMetaData{
  private int uid;
  private int lockAttempts;
  private long lastAttempt;

	/**
	* Default empty UserMetaData constructor
	*/
	public UserMetaData() {
		super();
	}

	/**
	* Default UserMetaData constructor
	*/
	public UserMetaData(int uid, int lockAttempts, long lastAttempt) {
		super();
		this.uid = uid;
		this.lockAttempts = lockAttempts;
		this.lastAttempt = lastAttempt;
	}

	/**
	* Returns value of uid
	* @return
	*/
	public int getUid() {
		return uid;
	}

	/**
	* Sets new value of uid
	* @param
	*/
	public void setUid(int uid) {
		this.uid = uid;
	}

	/**
	* Returns value of lockAttempts
	* @return
	*/
	public int getLockAttempts() {
		return lockAttempts;
	}

	/**
	* Sets new value of lockAttempts
	* @param
	*/
	public void setLockAttempts(int lockAttempts) {
		this.lockAttempts = lockAttempts;
	}

	/**
	* Returns value of lastAttempt
	* @return
	*/
	public long getLastAttempt() {
		return lastAttempt;
	}

	/**
	* Sets new value of lastAttempt
	* @param
	*/
	public void setLastAttempt(long lastAttempt) {
		this.lastAttempt = lastAttempt;
	}
}
