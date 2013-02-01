package com.example.smarthome;

import android.os.Bundle;
import android.app.Activity;
import android.app.DialogFragment;
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
	
	static public void toastMessage(String text, Activity context){
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public void onDialogPositiveClick(DialogFragment dailog) {
		InstanceSave.updateAlarmListFragment(this);
	}

	@Override
	public void onDialogNegativeClick(DialogFragment dailog) {
		// TODO Auto-generated method stub
		
	}
    

}
