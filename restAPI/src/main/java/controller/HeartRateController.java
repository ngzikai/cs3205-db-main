package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.StringTokenizer;

import com.mysql.cj.api.xdevapi.Result;

import entity.DaysOfWeek;
import entity.HeartRate;
import entity.Week;
import utils.db.MySQLAccess;

public class HeartRateController {
	
	public LinkedList<Week> getHeartRate(String key) {
		String sql = "SELECT value FROM secrets WHERE `key` = ?";
		
		Connection connect = MySQLAccess.connectDatabase();
		String value = null;
		
		try {
			PreparedStatement ps = connect.prepareStatement(sql);
			ps.setString(1, key);
			
			ResultSet rs = MySQLAccess.readDataBasePS(ps);
			
			while(rs.next()) {
				value = rs.getString(1);
			}
			
			if(value != null) {
				sql = "DELETE FROM secrets WHERE `key` = ?";
				
				ps = connect.prepareStatement(sql);
				ps.setString(1, key);
				
				MySQLAccess.updateDataBasePS(ps);
				
			}else {
				System.out.println("value: " + value);
				System.out.println("Unable to Find Results With Heart Rate");
				throw new Exception();
			}
			
		} catch(Exception e) {
			e.printStackTrace();
			MySQLAccess.close();
			return null;
		}
		
		
		
		sql = "SELECT creationdate, content FROM data WHERE type = 'Heart Rate' AND isAvailable = 1 AND uid = ? ORDER BY creationdate ASC";
		
		
		LinkedList<HeartRate> results = new LinkedList<HeartRate>();
		
		try {
			PreparedStatement ps = connect.prepareStatement(sql);
			ps.setString(1, value);
			
			ResultSet rs = MySQLAccess.readDataBasePS(ps);
			
			while(rs.next()) {
				String dateTime = rs.getString(1);
				String content = rs.getString(2);
				
				StringTokenizer tok = new StringTokenizer(dateTime, " ");
				String date = tok.nextToken();
				String time = tok.nextToken();
				
				results.add(new HeartRate(date, time, content));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			MySQLAccess.close();
			return null;
		}
		
		MySQLAccess.close();
		
		if(results.isEmpty()) {
			return null;
		}else {
			return processResults(results);
		}
	}
	
	public LinkedList<Week> processResults(LinkedList<HeartRate> results){
		
		LinkedList<Week> weeks = new LinkedList<Week>();
		int weekCount = 1;
		weeks = addNewWeek(weeks, weekCount);
		
		String[] dateSplit = results.getFirst().getDate().split("-");
		
		int year = Integer.parseInt(dateSplit[0]);
		int month = Integer.parseInt(dateSplit[1]);
		int day = Integer.parseInt(dateSplit[2]);
		
		Calendar firstDate  = new GregorianCalendar(year,month-1,day);
		int dayOfWeek = firstDate.get(Calendar.DAY_OF_WEEK);
		Calendar startOfWeek  = getStartOfWeek(firstDate, dayOfWeek);
		
		weeks = addHeartRate(weeks, dayOfWeek, results.getFirst());
		results.removeFirst();
		
		
		while(!results.isEmpty()) {
			
			dateSplit = results.getFirst().getDate().split("-");
			
			year = Integer.parseInt(dateSplit[0]);
			month = Integer.parseInt(dateSplit[1]);
			day = Integer.parseInt(dateSplit[2]);
			
			Calendar currDate  = new GregorianCalendar(year,month-1,day);
			dayOfWeek = currDate.get(Calendar.DAY_OF_WEEK);
			Calendar currStartOfWeek = Calendar.getInstance();
			currStartOfWeek.setTime(currDate.getTime());
			currStartOfWeek = getStartOfWeek(currStartOfWeek, dayOfWeek);
			
			if(currStartOfWeek.equals(startOfWeek)) {
				System.out.println("Current Start of Week Equals Curr Day Start Of Week");
				System.out.println("Adding Date to Week: " + weekCount);
				//System.out.println("Day of Week:" + currDate.get(Calendar.DAY_OF_WEEK));
				System.out.println("Current Date: " + day + "-" + month + "-" + year);
				System.out.println("Start of Week: " + currStartOfWeek.get(Calendar.DAY_OF_MONTH) + "-" + currStartOfWeek.get(Calendar.MONTH) + "-" + currStartOfWeek.get(Calendar.YEAR));
				System.out.println("Current Start of Week: " + startOfWeek.get(Calendar.DAY_OF_MONTH) + "-" + startOfWeek.get(Calendar.MONTH) + "-" + startOfWeek.get(Calendar.YEAR));
								
				weeks = addHeartRate(weeks, dayOfWeek, results.getFirst());
				results.removeFirst();
			}else {
				System.out.println("Current Start of Week Not Equals Curr Day Start Of Week");
				weekCount += 1;
				System.out.println("Adding Date to Week: " + weekCount);
				//System.out.println("Day of Week:" + currDate.get(Calendar.DAY_OF_WEEK));
				System.out.println("Current Date: " + day + "-" + month + "-" + year);
				System.out.println("Start of Week: " + currStartOfWeek.get(Calendar.DAY_OF_MONTH) + "-" + currStartOfWeek.get(Calendar.MONTH) + "-" +currStartOfWeek.get(Calendar.YEAR));
				System.out.println("Current Start of Week: " + startOfWeek.get(Calendar.DAY_OF_MONTH) + "-" + startOfWeek.get(Calendar.MONTH) + "-" + startOfWeek.get(Calendar.YEAR));
				

				weeks = addNewWeek(weeks, weekCount);
				startOfWeek.setTime(currStartOfWeek.getTime());
				
				weeks = addHeartRate(weeks, dayOfWeek, results.getFirst());
				results.removeFirst();
			}
			
			
		}
		
		return weeks;
	}
	
	public Calendar getStartOfWeek(Calendar calendar, int dayOfWeek) {
		
		System.out.println("DEBUG: Day of Week: " + dayOfWeek);
		Calendar cal = Calendar.getInstance();
		
		if(dayOfWeek == 2) {
			//MONDAY
			calendar.add(Calendar.DAY_OF_MONTH, -1);
			cal.setTime(calendar.getTime());
			
		}else if(dayOfWeek == 3) {
			//TUESDAY
			calendar.add(Calendar.DAY_OF_MONTH, -2);
			cal.setTime(calendar.getTime());
			
		}else if (dayOfWeek == 4) {
			//WEDNESDAY
			calendar.add(Calendar.DAY_OF_MONTH, -3);
			cal.setTime(calendar.getTime());
			
		}else if (dayOfWeek == 5) {
			//THURSDAY
			calendar.add(Calendar.DAY_OF_MONTH, -4);
			cal.setTime(calendar.getTime());
			
		}else if (dayOfWeek == 6) {
			//FRIDAY
			calendar.add(Calendar.DAY_OF_MONTH, -5);
			cal.setTime(calendar.getTime());
			
		}else if (dayOfWeek == 7) {
			//SATURDAY
			calendar.add(Calendar.DAY_OF_MONTH, -6);
			cal.setTime(calendar.getTime());
			
		}else if (dayOfWeek == 1) {
			//SUNDAY
			cal.setTime(calendar.getTime());
		}
		
		return cal;
	}
	
	public LinkedList<Week> addHeartRate(LinkedList<Week> weeks, int dayOfWeek, HeartRate heartRate) {
		
		heartRate.setDate(null);
		
		if(dayOfWeek == 2) {
			weeks.getLast().getDays().getMonday().add(heartRate);
		}else if(dayOfWeek == 3) {
			weeks.getLast().getDays().getTuesday().add(heartRate);
		}else if(dayOfWeek == 4) {
			weeks.getLast().getDays().getWednesday().add(heartRate);
		}else if(dayOfWeek == 5) {
			weeks.getLast().getDays().getThursday().add(heartRate);
		}else if (dayOfWeek == 6) {
			weeks.getLast().getDays().getFriday().add(heartRate);
		}else if (dayOfWeek == 7) {
			weeks.getLast().getDays().getSaturday().add(heartRate);
		}else if (dayOfWeek == 1) {
			weeks.getLast().getDays().getSunday().add(heartRate);
		}
		
		return weeks;
		
	}
	
	public LinkedList<Week> addNewWeek(LinkedList<Week> weeks, int weekCount){
		weeks.addLast(new Week("" + weekCount, new DaysOfWeek(new ArrayList<HeartRate>(),new ArrayList<HeartRate>(),new ArrayList<HeartRate>(),new ArrayList<HeartRate>(),new ArrayList<HeartRate>(),new ArrayList<HeartRate>(),new ArrayList<HeartRate>()))); 
		return weeks;
	}

}
