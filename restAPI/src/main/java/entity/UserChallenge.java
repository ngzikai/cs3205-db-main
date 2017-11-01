package entity;

public class UserChallenge{
	private int uid;
	private String type;
	private String challengeString;

	/**
	* Default empty UserChallenge constructor
	*/
	public UserChallenge() {
		super();
	}

	/**
	* Default UserChallenge constructor
	*/
	public UserChallenge(int uid, String type, String challengeString) {
		super();
		this.uid = uid;
		this.type = type;
		this.challengeString = challengeString;
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
	* Returns value of type
	* @return
	*/
	public String getType() {
		return type;
	}

	/**
	* Sets new value of type
	* @param
	*/
	public void setType(String type) {
		this.type = type;
	}

	/**
	* Returns value of challengeString
	* @return
	*/
	public String getChallengeString() {
		return challengeString;
	}

	/**
	* Sets new value of challengeString
	* @param
	*/
	public void setChallengeString(String challengeString) {
		this.challengeString = challengeString;
	}
}
