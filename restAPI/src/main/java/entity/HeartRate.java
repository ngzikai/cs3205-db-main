package entity;

public class HeartRate {
	public String date;
	public String time;
	public String heartRate;
	
	public HeartRate() {
		 
	}

	public HeartRate(String date, String time, String heartRate) {
		super();
		this.date = date;
		this.time = time;
		this.heartRate = heartRate;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getHeartRate() {
		return heartRate;
	}

	public void setHeartRate(String heartRate) {
		this.heartRate = heartRate;
	}
	
	
	
	
}
