package com.example.smarthome;

import android.bluetooth.BluetoothDevice;

public interface ISmartConnectionService {
	public static final String EXTRA_BT_REMOTE_ADDRESS = "bt_remote_address";
	public void write(String message);
	public int getState();
	public void start();
	public void stop();
	public void connect(BluetoothDevice remoteDevice);
}
