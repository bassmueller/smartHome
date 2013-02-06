package com.example.smarthome;


import java.util.Comparator;

public class AlarmTimeComparator implements Comparator<AlarmTime> {
	
	/**
	 * @return
	 * the value 0 if the arguments contain the same date and time; 
	 * a value greater than 0 if lhs contains a date and a time, which is less than rhs; 
	 * and a value less than 0 if lhs contains a date and a time, which is greater than rhs.
	 */
	@Override
	public int compare(AlarmTime lhs, AlarmTime rhs) {
		String[] leftTime = lhs.getTime().split(":");
		String[] leftDate = lhs.getDate().split("\\.");
		String[] rightTime = rhs.getTime().split(":");
		String[] rightDate = rhs.getDate().split("\\.");
		int result = 0;
		
		for(int i = leftDate.length; (i > 0) && (result == 0); i--){
			result = Integer.parseInt(rightDate[i-1])-Integer.parseInt(leftDate[i-1]);
		}
		for(int i = leftTime.length; (i > 0) && (result == 0); i--){
			result = Integer.parseInt(rightTime[i-1])-Integer.parseInt(leftTime[i-1]);
		}
		return result;
	}

}
