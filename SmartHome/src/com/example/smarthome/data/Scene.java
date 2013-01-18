package com.example.smarthome.data;

import java.io.Serializable;

public class Scene implements Serializable {

	private static final long serialVersionUID = 7461004185341686852L;
	private String description;
	private boolean lightShow;
	private boolean soundEffects;
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public boolean isLightShow() {
		return lightShow;
	}
	public void setLightShow(boolean lightShow) {
		this.lightShow = lightShow;
	}
	
	public boolean isSoundEffects() {
		return soundEffects;
	}
	public void setSoundEffects(boolean soundEffects) {
		this.soundEffects = soundEffects;
	}
	
	@Override
	public String toString() {
		return ("Scene " + this.description);
	}

}
