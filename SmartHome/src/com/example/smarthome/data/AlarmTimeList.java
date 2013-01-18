package com.example.smarthome.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AlarmTimeList implements Serializable,IStoredList<AlarmTime> {
	
	private static final long serialVersionUID = 2533088355622978100L;
	private List<AlarmTime> alarmTimes;
	
	public AlarmTimeList(){
		this.alarmTimes = new ArrayList<AlarmTime>();
	}

	@Override
	public List<AlarmTime> getEntireList() {
		return this.alarmTimes;
	}

	@Override
	public void addItem(AlarmTime object) {
		this.alarmTimes.add(object);
	}

	@Override
	public AlarmTime removeItem(int location) {
		return this.alarmTimes.remove(location);
	}

}
