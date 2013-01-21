package com.example.smarthome;


import java.io.Serializable;

public class AlarmTime implements Serializable {

	private static final long serialVersionUID = 4643718182072449744L;
	private String description;
	private String time;
	private String date;
	private Scene scene;

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
	
	public Scene getScene() {
		return scene;
	}
	public void setScene(Scene scene) {
		this.scene = scene;
	}
	
	@Override
	public String toString() {
		return (this.time + "  " + this.date + System.getProperty("line.separator") + "Description: " + this.description + "                       Scene: " + this.scene.getDescription());
	}
}
