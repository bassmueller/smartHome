package com.example.smarthome;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

public class AlarmChangeDialog extends DialogFragment {
	
	private final String fileNameAlarm = "AlarmTimes";
	private final String fileNameScene = "Scenes";
	private AlarmChangeDialogListener listener;

	public interface AlarmChangeDialogListener{
		public void onDialogPositiveClick(DialogFragment dailog);
		public void onDialogNegativeClick(DialogFragment dailog);
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try{
			listener = (AlarmChangeDialogListener)activity;
		}catch(ClassCastException e){
			throw new ClassCastException(activity.toString() + " must implement AlarmChangeDialogListener");
		}
	}
	
	@Override
	public void onStart() {
		super.onStart();

		AlarmTime selectedAlarm = InstanceSave.<AlarmTime>readSpecificObject(fileNameAlarm, getArguments().getInt("position"), getActivity());
		if(selectedAlarm != null){
			EditText description = (EditText)getDialog().findViewById(R.id.alarmChangeDescription);
			try {
				description.setText(selectedAlarm.getDescription(), TextView.BufferType.EDITABLE);
            }catch(Exception e) {
                Log.i("Log", e.getMessage()+" Error!"); // LogCat message
            }
			
			TimePicker timePicker = (TimePicker)getDialog().findViewById(R.id.alarmChangeTimePicker);
			String[] time = selectedAlarm.getTime().split(":");
			timePicker.setCurrentHour(Integer.valueOf(time[0]));
			timePicker.setCurrentMinute(Integer.valueOf(time[1]));
			DatePicker datePicker = (DatePicker)getDialog().findViewById(R.id.alarmChangeDatePicker);
			String[] date = selectedAlarm.getDate().split("\\.");
			datePicker.updateDate(Integer.valueOf(date[2]), Integer.valueOf(date[1])-1, Integer.valueOf(date[0]));
			
			SceneList sceneList = InstanceSave.<SceneList>readList(fileNameScene, getActivity());
			Spinner spinner = (Spinner) getDialog().findViewById(R.id.sceneChangeSpinner);
			if(sceneList != null){
				Scene[]scenes = (Scene[]) sceneList.getEntireList().toArray(new Scene[sceneList.getEntireList().size()]);
				ArrayAdapter<Scene> adapter = new ArrayAdapter<Scene>(getActivity(), android.R.layout.simple_list_item_1, scenes);
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spinner.setAdapter(adapter);
				int i = 0;
				while((i < scenes.length) && (selectedAlarm.getScene().getDescription().compareTo(scenes[i].getDescription()) != 0)) {
					i++;
				}
				if(i <= scenes.length){
					spinner.setSelection(i);
				}else{
					MainActivity.toastMessage("Select new Scene! Old Scene was removed from List", getActivity());
				}
			}
		}else{
			MainActivity.toastMessage("Element not found!", getActivity());
			AlarmChangeDialog.this.getDialog().cancel();
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
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
	            	   
	            	   IStoredList<AlarmTime> list = InstanceSave.readList(fileNameAlarm, getActivity());
	            	   AlarmTime alarmToChange = null;
	            	   if(list != null){
	            		   alarmToChange = list.getEntireList().get(getArguments().getInt("position"));
	            	   }
	            	   if(alarmToChange != null){
		            	   EditText description = (EditText) getDialog().findViewById(R.id.alarmChangeDescription);
		            	   alarmToChange.setDescription(description.getText().toString());
		            	   TimePicker time = (TimePicker)getDialog().findViewById(R.id.alarmChangeTimePicker);
		            	   DatePicker date = (DatePicker) getDialog().findViewById(R.id.alarmChangeDatePicker);
		            	   alarmToChange.setTime(time.getCurrentHour() + ":" + time.getCurrentMinute());
		            	   alarmToChange.setDate(date.getDayOfMonth() + "." + (date.getMonth()+1) + "." + date.getYear());
		            	   Spinner spinner = (Spinner) getDialog().findViewById(R.id.sceneChangeSpinner);
		            	   alarmToChange.setScene((Scene)spinner.getSelectedItem());
		            	   InstanceSave.changeItem(fileNameAlarm, alarmToChange, getArguments().getInt("position"), getActivity());
	            	   }else{
	            		   MainActivity.toastMessage("Object to change not found!", getActivity());
	            	   }
	            	   listener.onDialogPositiveClick(AlarmChangeDialog.this);
	               }
	           })
	           .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	                   AlarmChangeDialog.this.getDialog().cancel();
	                   listener.onDialogNegativeClick(AlarmChangeDialog.this);
	               }
	           });      
	    return builder.create();
	}

}
