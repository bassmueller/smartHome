package com.example.smarthome;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;


import android.os.Bundle;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.view.Menu;
import android.widget.Toast;

public class MainActivity extends Activity implements AlarmChangeDialog.AlarmChangeDialogListener{
	
	public static final String TAG = "com.exampe.smarthome";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_layout);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

	    /*
	     * Force the system to close the application down completely instead of
	     * retaining it in the background. The process that runs the application
	     * will be killed. The application will be completely created as a new
	     * application in a new process if the user starts the application
	     * again.
	     */
	    System.exit(0);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	@SuppressWarnings("unchecked")
	synchronized public <T extends Serializable> T readList(String fileName){
		T loadedObject = null;
		
		try{
			FileInputStream fis = openFileInput(fileName);
			ObjectInputStream is = new ObjectInputStream(fis);
			loadedObject = (T)is.readObject();
			is.close();
		}catch(FileNotFoundException e){
			toastMessage("File not found!");
		}catch(Exception e){
			toastMessage("Unknown error!");
			e.printStackTrace();
		}
		
		return loadedObject;
	}
	
	synchronized public <T extends Serializable> T readSpecificObject(String fileName, int location){
		IStoredList<T> list = this.readList(fileName);
		return (list!=null)?list.getEntireList().get(location):null;
	}
	
	synchronized public <T extends Serializable> T deleteSpecificObject(String fileName, int location){
		IStoredList<T> list = this.readList(fileName);
		T objectDeleted = (list!=null)?list.getEntireList().remove(location):null;
		if(objectDeleted != null){
			FileOutputStream fos;
			ObjectOutputStream os;
			try {
				fos = openFileOutput(fileName, Context.MODE_PRIVATE);
				os = new ObjectOutputStream(fos);
				os.writeObject(list);
				os.close();
			} catch (Exception e) {
				e.printStackTrace();	
			}
		}
		return objectDeleted;
	}
	
	@SuppressWarnings("unchecked")
	synchronized public <T extends Serializable> void appendToList(String fileName, T objectToAppend) {
		IStoredList<T> list = null;

		try{
			FileInputStream fis = openFileInput(fileName);
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
				toastMessage("File not found!");
			}
		}catch(Exception e){
			e.printStackTrace();
			toastMessage("Unknown error!");
		}
		
		if(list != null){
			list.addItem(objectToAppend);
			if(objectToAppend instanceof AlarmTime){
				Collections.sort((List<AlarmTime>)list.getEntireList(), new AlarmTimeComparator());
			}
			
			FileOutputStream fos;
			ObjectOutputStream os;
			try {
				fos = openFileOutput(fileName, Context.MODE_PRIVATE);
				os = new ObjectOutputStream(fos);
				os.writeObject(list);
				os.close();
				toastMessage("Saved!");
			} catch (Exception e) {
				e.printStackTrace();	
				toastMessage("Error! Saving failed!");
			}
		}else{
			toastMessage("Saved data is corrupt! Saving failed!");
		}
	}
	
	@SuppressWarnings("unchecked")
	synchronized public <T extends Serializable> void changeItem(String fileName, T objectToSave, int location){
		IStoredList<T> list = this.readList(fileName);
		
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
				fos = openFileOutput(fileName, Context.MODE_PRIVATE);
				os = new ObjectOutputStream(fos);
				os.writeObject(list);
				os.close();
				toastMessage("changes saved!");
			} catch (Exception e) {
				e.printStackTrace();	
				toastMessage("Save changes failed!");
			}
		}else{
			toastMessage("Data corrupt! Saving failed!");
		}
	}
	
	public void toastMessage(String text){
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public void onDialogPositiveClick(DialogFragment dailog) {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(R.id.details, new AlarmListFragment());
	    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
	    ft.commit();
		
	}

	@Override
	public void onDialogNegativeClick(DialogFragment dailog) {
		// TODO Auto-generated method stub
		
	}
    

}
