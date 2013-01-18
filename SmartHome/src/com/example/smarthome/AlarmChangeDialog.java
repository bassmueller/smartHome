package com.example.smarthome;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

public class AlarmChangeDialog extends DialogFragment {
	
	private final String fileNameAlarm = "AlarmTimes";


	@SuppressWarnings("static-access")
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		MainActivity activity = (MainActivity)getActivity();
		Log.v(activity.TAG,"Argument 'position' in " + this.getClass() + ":" + String.valueOf(getArguments().getInt("position")));
		AlarmTime selectedAlarm = activity.<AlarmTime>readSpecificObject(fileNameAlarm, getArguments().getInt("position"));
		if(selectedAlarm != null){
			EditText description = (EditText)getActivity().findViewById(R.id.alarmChangeDescription);
			description.setText(selectedAlarm.getDescription(), TextView.BufferType.EDITABLE);
			TimePicker timePicker = (TimePicker)getActivity().findViewById(R.id.alarmChangeTimePicker);
			String[] time = selectedAlarm.getTime().split(":");
			timePicker.setCurrentHour(Integer.valueOf(time[0]));
			timePicker.setCurrentMinute(Integer.valueOf(time[1]));
			DatePicker datePicker = (DatePicker)getActivity().findViewById(R.id.alarmChangeDatePicker);
			String[] date = selectedAlarm.getDate().split(".");
			datePicker.updateDate(Integer.valueOf(date[2]), Integer.valueOf(date[1]), Integer.valueOf(date[0]));
		}else{
			activity.toastMessage("Element not found!");
			AlarmChangeDialog.this.getDialog().cancel();
		}
		
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    // Get the layout inflater
	    LayoutInflater inflater = getActivity().getLayoutInflater();

	    // Inflate and set the layout for the dialog
	    // Pass null as the parent view because its going in the dialog layout
	    builder.setView(inflater.inflate(R.layout.alarmdialog_layout, null))
	    // Add action buttons
	           .setPositiveButton(R.string.alarmChange, new DialogInterface.OnClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int id) {
	            	   
	            	   MainActivity activity = (MainActivity)getActivity();
	            	   IStoredList<AlarmTime> list = activity.readList(fileNameAlarm);
	            	   AlarmTime alarmToChange = null;
	            	   if(list != null){
	            		   alarmToChange = list.getEntireList().get(getArguments().getInt("position"));
	            	   }
	            	   if(alarmToChange != null){
		            	   EditText description = (EditText) getActivity().findViewById(R.id.alarmChangeDescription);
		            	   alarmToChange.setDescription(description.getText().toString());
		            	   TimePicker time = (TimePicker)getActivity().findViewById(R.id.alarmChangeTimePicker);
		            	   DatePicker date = (DatePicker) getActivity().findViewById(R.id.alarmChangeDatePicker);
		            	   alarmToChange.setTime(time.getCurrentHour() + ":" + time.getCurrentMinute());
		            	   alarmToChange.setDate(date.getDayOfMonth() + "." + date.getMonth()+1 + "." + date.getYear());
	            	   }
	               }
	           })
	           .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	                   AlarmChangeDialog.this.getDialog().cancel();
	               }
	           });      
	    return builder.create();
	}

}
