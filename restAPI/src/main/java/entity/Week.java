package entity;

import java.util.ArrayList;

public class Week {
	private String week;
	private DaysOfWeek days;
	
	public Week() {
		
	}

	public Week(String week, DaysOfWeek days) {
		super();
		this.week = week;
		this.days = days;
	}

	public String getWeek() {
		return week;
	}

	public DaysOfWeek getDays() {
		return days;
	}

	public void setWeek(String week) {
		this.week = week;
	}

	public void setDays(DaysOfWeek days) {
		this.days = days;
	}
	
	
}
