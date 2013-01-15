package com.example.smarthome;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream;
import java.io.Serializable;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.Toast;


public class MainActivity extends Activity {

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
	synchronized public <T extends Serializable> T readFile(String fileName){
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
	
	private void toastMessage(String text){
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}

}
