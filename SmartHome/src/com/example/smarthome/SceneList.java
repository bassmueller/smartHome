package com.example.smarthome;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SceneList implements Serializable {

	private static final long serialVersionUID = 6127556117456624948L;
	private List<Scene> scenes;
	
	public SceneList(){
		this.scenes = new ArrayList<Scene>();
	}
	
	public List<Scene> getScenes() {
		return scenes;
	}
	
	public void addScene(Scene scene) {
		this.scenes.add(scene);
	}
	
	public Scene removeScene(int location) {
		return this.scenes.remove(location);
	}
	
}
