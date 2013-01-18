package com.example.smarthome.data;

import java.io.Serializable;

public class AlarmTime implements Serializable {

	private static final long serialVersionUID = 4643718182072449744L;
	private String description;
	private String time;
	private String date;

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
	@Override
	public String toString() {
		return (this.time + "  " + this.date + System.getProperty("line.separator") + this.description);
	}
}
