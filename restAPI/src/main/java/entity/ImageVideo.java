package entity;

import entity.sessions.Session;

public class ImageVideo extends Session{
  private String fileLocation = "";
  private String type = "";

	/**
	* Default empty ImageVideo constructor
	*/
	public ImageVideo() {
		super();
	}

	/**
	* Default ImageVideo constructor
	*/
	public ImageVideo(String fileLocation, String type) {
		super();
		this.fileLocation = fileLocation;
		this.type = type;
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
}
