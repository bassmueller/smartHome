package com.example.smarthome;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AlarmTimeList implements Serializable {
	
	private static final long serialVersionUID = 2533088355622978100L;
	private List<AlarmTime> alarmTimes;
	
	public AlarmTimeList(){
		this.alarmTimes = new ArrayList<AlarmTime>();
	}

	public List<AlarmTime> getAlarmTimeList() {
		return this.alarmTimes;
	}

	public void addAlarmTime(AlarmTime alarmTime) {
		this.alarmTimes.add(alarmTime);
	}
	
	public AlarmTime removeAlarmTime(int location) {
		return this.alarmTimes.remove(location);
	}

}
