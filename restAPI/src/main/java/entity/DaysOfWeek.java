package entity;

import java.util.ArrayList;

public class DaysOfWeek {
	ArrayList<HeartRate> sunday;
	ArrayList<HeartRate> monday;
	ArrayList<HeartRate> tuesday;
	ArrayList<HeartRate> wednesday;
	ArrayList<HeartRate> thursday;
	ArrayList<HeartRate> friday;
	ArrayList<HeartRate> saturday;
	
	public DaysOfWeek() {

	}

	public DaysOfWeek(ArrayList<HeartRate> sunday, ArrayList<HeartRate> monday, ArrayList<HeartRate> tuesday,
			ArrayList<HeartRate> wednesday, ArrayList<HeartRate> thursday, ArrayList<HeartRate> friday,
			ArrayList<HeartRate> saturday) {
		super();
		this.sunday = sunday;
		this.monday = monday;
		this.tuesday = tuesday;
		this.wednesday = wednesday;
		this.thursday = thursday;
		this.friday = friday;
		this.saturday = saturday;
	}

	public ArrayList<HeartRate> getSunday() {
		return sunday;
	}

	public void setSunday(ArrayList<HeartRate> sunday) {
		this.sunday = sunday;
	}

	public ArrayList<HeartRate> getMonday() {
		return monday;
	}

	public void setMonday(ArrayList<HeartRate> monday) {
		this.monday = monday;
	}

	public ArrayList<HeartRate> getTuesday() {
		return tuesday;
	}

	public void setTuesday(ArrayList<HeartRate> tuesday) {
		this.tuesday = tuesday;
	}

	public ArrayList<HeartRate> getWednesday() {
		return wednesday;
	}

	public void setWednesday(ArrayList<HeartRate> wednesday) {
		this.wednesday = wednesday;
	}

	public ArrayList<HeartRate> getThursday() {
		return thursday;
	}

	public void setThursday(ArrayList<HeartRate> thursday) {
		this.thursday = thursday;
	}

	public ArrayList<HeartRate> getFriday() {
		return friday;
	}

	public void setFriday(ArrayList<HeartRate> friday) {
		this.friday = friday;
	}

	public ArrayList<HeartRate> getSaturday() {
		return saturday;
	}

	public void setSaturday(ArrayList<HeartRate> saturday) {
		this.saturday = saturday;
	}

}
