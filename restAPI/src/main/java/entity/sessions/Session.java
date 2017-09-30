package entity.sessions;

import utils.GUID;

public class Session {

  private String uid = "";
  private long timestamp = 0l;
  private int ownerID = 0;
  private String fileLocation = "";

	/**
	* Default empty Session constructor
	*/
	public Session() {
    uid = GUID.BASE58();
	}

	/**
	* Default Session constructor
	*/
	public Session(String uid, long timestamp, int ownerID, String fileLocation) {
		this.uid = uid;
		this.timestamp = timestamp;
		this.ownerID = ownerID;
    this.fileLocation = fileLocation;
	}

	/**
	* Returns value of uid
	* @return
	*/
	public String getUid() {
		return uid;
	}

	/**
	* Sets new value of uid
	* @param
	*/
	public void setUid(String uid) {
		this.uid = uid;
	}

	/**
	* Returns value of timestamp
	* @return
	*/
	public long getTimestamp() {
		return timestamp;
	}

	/**
	* Sets new value of timestamp
	* @param
	*/
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	/**
	* Returns value of ownerID
	* @return
	*/
	public int getOwnerID() {
		return ownerID;
	}

	/**
	* Sets new value of ownerID
	* @param
	*/
	public void setOwnerID(int ownerID) {
		this.ownerID = ownerID;
	}

	/**
	* Returns value of fileLocation
	* @return
	*/
	public String getFileLocation() {
		return fileLocation;
	}

	/**
	* Sets new value of fileLocation
	* @param
	*/
	public void setFileLocation(String fileLocation) {
		this.fileLocation = fileLocation;
	}
}
