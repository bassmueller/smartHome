package com.example.smarthome;

import java.io.Serializable;
import java.util.Date;

public class AlarmTimes implements Serializable {

	private static final long serialVersionUID = 4643718182072449744L;
	private String description;
	private Date timestamp;
	
	
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

}
