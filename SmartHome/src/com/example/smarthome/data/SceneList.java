package com.example.smarthome.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SceneList implements Serializable,IStoredList<Scene> {

	private static final long serialVersionUID = 6127556117456624948L;
	private List<Scene> scenes;
	
	public SceneList(){
		this.scenes = new ArrayList<Scene>();
	}

	@Override
	public List<Scene> getEntireList() {
		return this.scenes;
	}

	@Override
	public void addItem(Scene object) {
		this.scenes.add(object);
	}

	@Override
	public Scene removeItem(int location) {
		return this.scenes.remove(location);
	}
	
}
