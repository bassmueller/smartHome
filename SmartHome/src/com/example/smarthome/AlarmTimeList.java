package com.example.smarthome;

import java.util.LinkedList;
import java.util.List;

public class AlarmTimeList {
	
	private List<AlarmTimes> alarmTimes;
	
	public AlarmTimeList(){
		this.setAlarmTimeList(new LinkedList<AlarmTimes>());
	}

	public List<AlarmTimes> getAlarmTimeList() {
		return this.alarmTimes;
	}

	public void setAlarmTimeList(List<AlarmTimes> alarmTimes) {
		this.alarmTimes = alarmTimes;
	}

}
