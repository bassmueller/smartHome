package com.example.smarthome;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

public class AlarmClockFragment extends Fragment{
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.alarmclock_layout, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		//Function called by the Save-Button
		Button save = (Button)getActivity().findViewById(R.id.alarmButtonSave);
		save.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String fileName = "AlarmTimes";
				AlarmTimeList timeList = null;
				
				try{
					FileInputStream fis = getActivity().openFileInput(fileName);
					ObjectInputStream is = new ObjectInputStream(fis);
					timeList = (AlarmTimeList) is.readObject();
					is.close();
				}catch(FileNotFoundException e){
					timeList = new AlarmTimeList();
				}catch(Exception e){
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(timeList != null){
					AlarmTime currentSelection = new AlarmTime();
					EditText description = (EditText) getActivity().findViewById(R.id.alarmDescription);
					currentSelection.setDescription(description.getText().toString());
					TimePicker time = (TimePicker)getActivity().findViewById(R.id.alarmTimePicker);
					DatePicker date = (DatePicker) getActivity().findViewById(R.id.alarmDatePicker);
					currentSelection.setTime(time.getCurrentHour() + ":" + time.getCurrentMinute());
					currentSelection.setDate(date.getDayOfMonth() + "." + date.getMonth()+1 + "." + date.getYear());
					
					timeList.addAlarmTime(currentSelection);
					
					FileOutputStream fos;
					ObjectOutputStream os;
					try {
						fos = getActivity().openFileOutput(fileName, Context.MODE_PRIVATE);
						os = new ObjectOutputStream(fos);
						os.writeObject(timeList);
						os.close();
						toastMessage("Saved!");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						toastMessage("Not saved!");
					}
				}else{
					toastMessage("Not saved!");
				}
			}
		});
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	private void toastMessage(String text){
		Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
	}

}
