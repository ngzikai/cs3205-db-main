package entity;

import entity.sessions.Session;

public class HeartRate extends Session{
  private int heartrate = 0;

	/**
	* Default empty HeartRate constructor
	*/
	public HeartRate() {
		super();
	}

	/**
	* Default HeartRate constructor
	*/
	public HeartRate(int heartrate) {
		super();
		this.heartrate = heartrate;
	}

	/**
	* Returns value of heartrate
	* @return
	*/
	public int getHeartrate() {
		return heartrate;
	}

	/**
	* Sets new value of heartrate
	* @param
	*/
	public void setHeartrate(int heartrate) {
		this.heartrate = heartrate;
	}
}
