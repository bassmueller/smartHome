package com.example.smarthome;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

public class SmartAlarmClockService extends Service {
    // Debugging
 	private static final String TAG = SmartAlarmClockService.class.getSimpleName();
    private static final boolean D = true;
    
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;	
    
    // Key names received from the BluetoothChatService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";
    
    // Binder given to clients for binding (really? -.-)
    private final IBinder mBinder = new SmartAlarmClockBinder();
    // indicates behavior if the service is killed
    private boolean mStartMode;       
    // indicates whether onRebind should be used
    private boolean mAllowRebind; 
    // Represents the MAC-Address
    private String mRemoteAddress = null;
    private BluetoothAdapter mBluetoothAdapter = null;
    // Later the concrete connector service e.g. bluetooth or wifi
    private static ISmartConnectionService mConnectionService = null;
    
    @Override
    public void onCreate() {
        // The service is being created
    	if(D) Log.d(TAG, new Exception().getStackTrace()[0].getMethodName() + " called");
    	
    	mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		if (mBluetoothAdapter == null) {
            //No BluetoothDialog
			finishDialogNoBluetooth();
			return;
		}
		
		mConnectionService = new SmartConnectionService(this, mHandlerBT);

    }
    
    @Override
    public IBinder onBind(Intent intent) {
    	// The service is starting, due to a call to startService()
    	if(D) Log.d(TAG, new Exception().getStackTrace()[0].getMethodName() + " called");
    	

	    if ( (mBluetoothAdapter != null)  && (!mBluetoothAdapter.isEnabled()) ) {
		
	    	finishDialogNoBluetooth(); 
	    }		
	
	    if (mConnectionService != null) {
	    	// Only if the state is STATE_NONE, do we know that we haven't started already
	    	//TODO Interface me
	    	if (mConnectionService.getState() == SmartConnectionService.STATE_NONE) {
	    		// Start the connection
	    		mConnectionService.start();
	    	}
	    }

	    if (mBluetoothAdapter != null) {
	    	//OK let's do it
	    	mRemoteAddress = intent.getExtras().getString((MainActivity.EXTRA_BT_REMOTE_ADDRESS));
			if (mRemoteAddress != null) {
				mConnectionService.connect(mBluetoothAdapter.getRemoteDevice(mRemoteAddress));
			}
	    }
	    mStartMode = true;
	    
	    return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        // All clients have unbound with unbindService()
    	if(D) Log.d(TAG, new Exception().getStackTrace()[0].getMethodName() + " called");
    	if (mConnectionService != null) {
	    	// Only if the state is STATE_NONE, do we know that we haven't started already
	    	if (mConnectionService.getState() == SmartConnectionService.STATE_CONNECTED) {
	    		// Stop the connection
	    		mConnectionService.stop();
	    	}
	    }
        return mAllowRebind;
    }
    
    @Override
	public void onRebind(Intent intent) {
		// A client is binding to the service with bindService(),
		if (D)
			Log.d(TAG, new Exception().getStackTrace()[0].getMethodName()
					+ " called");
	}
    
    @Override
    public void onDestroy() {
        // The service is no longer used and is being destroyed
    	if(D) Log.d(TAG, new Exception().getStackTrace()[0].getMethodName() + " called");
    	// Stop the connection in order you get no information back .. If not happened at unbind
    	if (mConnectionService != null) {
    		if (mConnectionService.getState() == SmartConnectionService.STATE_CONNECTED) {
	    		// Stop the connection
	    		mConnectionService.stop();
	    	}
    	}
    	mStartMode = false;
    }
    
	public void write(String message) {
    	if(D) Log.d(TAG, new Exception().getStackTrace()[0].getMethodName() + " called");
		if(mStartMode) mConnectionService.write(message);
	}
	
	public int getConnectionState() {
		return mConnectionService.getState();
	}
    
    // The Handler that gets information back from the SmartConnectionService
    private final Handler mHandlerBT = new Handler() {
		@SuppressLint("NewApi")
		@Override
        public void handleMessage(Message msg) {        	
            switch (msg.what) {
            case MESSAGE_STATE_CHANGE:
                if(D) Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                switch (msg.arg1) {
                case SmartConnectionService.STATE_NONE:
                	// Notify App that state is Disconnect
                    break;
                    
                case SmartConnectionService.STATE_CONNECTING:
                	// Notify App that state is Connecting
                    break;
                    
                case SmartConnectionService.STATE_CONNECTED:
                	// Notify App that state is Connected
                    break;
                }
                break;
            case MESSAGE_WRITE:   
                break;
              
            case MESSAGE_READ: {
            	byte[] readBuf = (byte[]) msg.obj;
            	StringBuilder sb = new StringBuilder();
                String strIncom = new String(readBuf, 0, msg.arg1);     
                if (strIncom.contains("$"))
                	sb.append(strIncom.substring(strIncom.indexOf('$') + 1, msg.arg1));
                else sb.append(strIncom);
                Log.d(TAG, "...String:"+ sb.toString() +  "Byte:" + msg.arg1 + "...");
                int endOfLineIndex = sb.indexOf("\r\n");                            
                if (endOfLineIndex > 0) {                                           
                    String sbMsg = sb.substring(0, endOfLineIndex);               
                    sb.delete(0, sb.length());
//                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                	PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(), 1, intent, 0);
//                    NotificationManager notificationManager =
//                    	    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//                    	Notification noti = new Notification.Builder(getApplicationContext())
//                    	.setContentTitle("Smart Alarm Clock")
//                        .setContentText(sbMsg)
//                        .setContentIntent(pIntent)
//                        .build();
//
//                    	noti.flags |= Notification.FLAG_AUTO_CANCEL;
//                    	notificationManager.notify(0, noti);
                    	Toast.makeText(getApplicationContext(), sbMsg,
                                Toast.LENGTH_SHORT).show();
                }
               
            	
            }
                break;
            case MESSAGE_TOAST:
                Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),
                               Toast.LENGTH_SHORT).show();
     
            }
        }
    };    
    
    private void finishDialogNoBluetooth() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.alert_dialog_no_bt)
        .setIcon(android.R.drawable.ic_dialog_info)
        .setTitle(R.string.app_name)
        .setCancelable( false )
        .setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       stopSelf();            	
                	   }
               });
        AlertDialog alert = builder.create();
        alert.show(); 
    }
   
    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class SmartAlarmClockBinder extends Binder {
    	SmartAlarmClockService getService() {
            // Return this instance of LocalService so clients can call public methods!
            return SmartAlarmClockService.this;
        }
    }
}
