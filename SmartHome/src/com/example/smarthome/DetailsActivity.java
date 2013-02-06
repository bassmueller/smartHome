package com.example.smarthome;

import com.example.smarthome.AlarmChangeDialog.AlarmChangeDialogListener;
import com.example.smarthome.ColorWheelDialog.ColorWheelDialogListener;
import com.example.smarthome.SmartAlarmClockService.SmartAlarmClockBinder;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class DetailsActivity extends Activity implements AlarmChangeDialogListener, ColorWheelDialogListener{
	// Debugging
	private static final String TAG = MainActivity.class.getSimpleName();
    private static final boolean D = true;
    
	private static final int REQUEST_CONNECT_BT_DEVICE = 2;
	public static final String EXTRA_BT_REMOTE_ADDRESS = "bt_remote_address";
	public static String rgbLED = "000255000";
	
    SmartAlarmClockService mSACService = null;
    boolean mBound = false;

	 @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);

         if (getResources().getConfiguration().orientation
                 == Configuration.ORIENTATION_LANDSCAPE) {
             // If the screen is now in landscape mode, we can show the
             // dialog in-line with the list so we don't need this activity.
             finish();
             return;
         }

         if (savedInstanceState == null) {
             // During initial setup, plug in the details fragment.
        	 Fragment details = null;
             switch(getIntent().getExtras().getInt("index")){
             	case 0: details = (Fragment) getFragmentManager().findFragmentById(R.id.alarmClock_layout);
             			if(details == null){
             				details = new AlarmClockFragment();
             			}
             			break;
             	case 1: details = (Fragment) getFragmentManager().findFragmentById(R.id.alarmList_layout);
                     	if(details == null){
             				details = new AlarmListFragment();
             			}
     					break;
             	case 2: details = (Fragment) getFragmentManager().findFragmentById(R.id.definescene_layout);
                     	if(details == null){
             				details = new DefineSceneFragment();
             			}
     					break;
     			default: details = (Fragment) getFragmentManager().findFragmentById(R.id.alarmClock_layout);
             			if(details == null){
             				details = new AlarmClockFragment();
             			}
     					break;
             }

             details.setArguments(getIntent().getExtras());
             getFragmentManager().beginTransaction().add(android.R.id.content, details).commit();
         }
     }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
    	Log.d(TAG, resultCode + ", " + data);
    	switch (requestCode) {
		
		case REQUEST_CONNECT_BT_DEVICE: {	
			if (resultCode == RESULT_OK) {
				if (data != null) {
					String remoteAddress = data.getExtras().getString(
							(DeviceListActivity.EXTRA_DEVICE_ADDRESS));
					if (remoteAddress != null) {
						final Intent intent = new Intent(DetailsActivity.this, SmartAlarmClockService.class);
						intent.putExtra(EXTRA_BT_REMOTE_ADDRESS, remoteAddress);
						new Thread(new Runnable() {
							@Override
							public void run() {
								bindService(intent, mConnection, Context.BIND_AUTO_CREATE);								
							}							
						}).start();						
					}
				}
			}
		} break;
		
		default: break;

		}
	}

	@Override
	public void onDialogPositiveClick(DialogFragment dailog) {
		InstanceSave.updateAlarmListFragment(this);
		
	}

	@Override
	public void onDialogNegativeClick(DialogFragment dailog) {

		
	}
	
	// Defines callbacks for service binding, passed to bindService()
	private ServiceConnection mConnection = new ServiceConnection() {
	    // Called when the connection with the service is established
	    public void onServiceConnected(ComponentName className, IBinder service) {
	    	if(D) Log.d(TAG, new Exception().getStackTrace()[0].getMethodName() + " called");
	        // Because we have bound to an explicit
	        // service that is running in our own process, we can
	        // cast its IBinder to a concrete class and directly access it.
	    	SmartAlarmClockBinder binder = (SmartAlarmClockBinder) service;
	        mSACService = binder.getService();
	        mBound = true;
	    }

	    // Called when the connection with the service disconnects unexpectedly
	    public void onServiceDisconnected(ComponentName className) {
	    	Log.e(TAG, new Exception().getStackTrace()[0].getMethodName() + " called");
	        mBound = false;
	    }
	};

	@Override
	public void onDialogPositiveClickCW(DialogFragment dailog) {

		
	}

	@Override
	public void onDialogNegativeClickCW(DialogFragment dailog) {

		
	}

}