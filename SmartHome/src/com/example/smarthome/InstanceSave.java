package com.example.smarthome;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.widget.Toast;

public class InstanceSave {
	
	@SuppressWarnings("unchecked")
	static synchronized public <T extends Serializable> T readList(String fileName, Activity context){
		T loadedObject = null;
		
		try{
			FileInputStream fis = context.openFileInput(fileName);
			ObjectInputStream is = new ObjectInputStream(fis);
			loadedObject = (T)is.readObject();
			is.close();
		}catch(FileNotFoundException e){
			Toast.makeText(context, "File not found!", Toast.LENGTH_SHORT).show();
		}catch(Exception e){
			Toast.makeText(context, "Unknown error!", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
		
		return loadedObject;
	}
	
	static synchronized public <T extends Serializable> T readSpecificObject(String fileName, int location, Activity context){
		IStoredList<T> list = readList(fileName, context);
		return (list!=null)?list.getEntireList().get(location):null;
	}
	
	static synchronized public <T extends Serializable> T deleteSpecificObject(String fileName, int location, Activity context){
		IStoredList<T> list = readList(fileName, context);
		T objectDeleted = (list!=null)?list.getEntireList().remove(location):null;
		if(objectDeleted != null){
			FileOutputStream fos;
			ObjectOutputStream os;
			try {
				fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
				os = new ObjectOutputStream(fos);
				os.writeObject(list);
				os.close();
			} catch (Exception e) {
				e.printStackTrace();	
			}
		}
		updateAlarmListFragment(context);
		return objectDeleted;
	}
	
	@SuppressWarnings("unchecked")
	static synchronized public <T extends Serializable> void appendToList(String fileName, T objectToAppend, Activity context) {
		IStoredList<T> list = null;

		try{
			FileInputStream fis = context.openFileInput(fileName);
			ObjectInputStream is = new ObjectInputStream(fis);
			list = (IStoredList<T>) is.readObject();
			is.close();
		}catch(FileNotFoundException e){
			if(objectToAppend instanceof AlarmTime){
				list = (IStoredList<T>) new AlarmTimeList();
			}else if(objectToAppend instanceof Scene){
				list = (IStoredList<T>)new SceneList();
			}else{
				e.printStackTrace();
				Toast.makeText(context, "File not found!", Toast.LENGTH_SHORT).show();
			}
		}catch(Exception e){
			e.printStackTrace();
			Toast.makeText(context, "Unknown error!", Toast.LENGTH_SHORT).show();
		}
		
		if(list != null){
			list.addItem(objectToAppend);
			if(objectToAppend instanceof AlarmTime){
				Collections.sort((List<AlarmTime>)list.getEntireList(), new AlarmTimeComparator());
			}
			
			FileOutputStream fos;
			ObjectOutputStream os;
			try {
				fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
				os = new ObjectOutputStream(fos);
				os.writeObject(list);
				os.close();
				Toast.makeText(context, "Saved!", Toast.LENGTH_SHORT).show();
			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(context, "Error! Saving failed!", Toast.LENGTH_SHORT).show();
			}
		}else{
			Toast.makeText(context, "Saved data is corrupt! Saving failed!", Toast.LENGTH_SHORT).show();
		}
	}
	
	@SuppressWarnings("unchecked")
	static synchronized public <T extends Serializable> void changeItem(String fileName, T objectToSave, int location, Activity context){
		IStoredList<T> list = readList(fileName, context);
		
		if(list != null){
			try{
				list.getEntireList().set(location, objectToSave);
				Collections.sort((List<AlarmTime>)list.getEntireList(), new AlarmTimeComparator());
			}catch(Exception e){
				e.printStackTrace();
			}
			
			FileOutputStream fos;
			ObjectOutputStream os;
			try {
				fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
				os = new ObjectOutputStream(fos);
				os.writeObject(list);
				os.close();
				Toast.makeText(context, "changes saved!", Toast.LENGTH_SHORT).show();
			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(context, "Save changes failed!", Toast.LENGTH_SHORT).show();
			}
		}else{
			Toast.makeText(context, "Data corrupt! Saving failed!", Toast.LENGTH_SHORT).show();
		}
	}
	
	static synchronized public void updateAlarmListFragment(Activity context){
		FragmentTransaction ft = context.getFragmentManager().beginTransaction();
		int id = 0;
		if(context instanceof MainActivity){
			id = R.id.details;
		}else{
			id = android.R.id.content;
		}
		ft.replace(id, new AlarmListFragment());
	    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
	    ft.commit();
	}

}
