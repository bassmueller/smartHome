package com.example.smarthome;

import com.example.smarthome.SmartAlarmClockService.SmartAlarmClockBinder;

import android.os.Bundle;
import android.os.IBinder;
import android.app.Activity;
import android.app.DialogFragment;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends Activity implements AlarmChangeDialog.AlarmChangeDialogListener{
	
    private static final boolean D = true;
	public static final String TAG = "com.exampe.smarthome";
	private static final int REQUEST_ENABLE_BT = 1;
	private static final int REQUEST_CONNECT_BT_DEVICE = 2;
	private static final int REQUEST_RGB_COLORS = 3;
	public static final String EXTRA_BT_REMOTE_ADDRESS = "bt_remote_address";
	
    SmartAlarmClockService mSACService = null;
    private BluetoothAdapter mBluetoothAdapter = null;
    boolean mBound = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_layout);
		
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();	
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
        case R.id.menu_connect:
        	Log.v(TAG, "openDeviceListClick clicked");
			if (checkBTState()) {
				Intent intent = new Intent(MainActivity.this, DeviceListActivity.class);
		        startActivityForResult(intent, REQUEST_CONNECT_BT_DEVICE);
			}
            return true;
        case R.id.menu_disconnect:
        	if (mBound) {
	            unbindService(mConnection);
	            mBound = false;
	        }
            return true;
        default:
            return super.onOptionsItemSelected(item);
    }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
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
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
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
	protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
    	Log.d(TAG, resultCode + ", " + data);
    	switch (requestCode) {
		
		case REQUEST_CONNECT_BT_DEVICE: {	
			if (resultCode == RESULT_OK) {
				if (data != null) {
					String remoteAddress = data.getExtras().getString(
							(DeviceListActivity.EXTRA_DEVICE_ADDRESS));
					if (remoteAddress != null) {
						final Intent intent = new Intent(MainActivity.this, SmartAlarmClockService.class);
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
    
    private boolean checkBTState() {
		// Check for Bluetooth support and then check to make sure it is turned on
		if (mBluetoothAdapter == null) {
			Log.e(TAG, "Bluetooth not support");
			return false;
		} else {
			if (mBluetoothAdapter.isEnabled()) {
				Log.d(TAG, "...Bluetooth ON...");
			} else {
				// Prompt user to turn on Bluetooth
				
				Intent enableBtIntent = new Intent(
						BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
			}
			return true;
		}
	}
    

}
