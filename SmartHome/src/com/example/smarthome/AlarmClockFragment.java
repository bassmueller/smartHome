package com.example.smarthome;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SlidingDrawer;
import android.widget.Spinner;
import android.widget.TimePicker;

public class AlarmClockFragment extends Fragment{
	
	private final String fileNameAlarm = "AlarmTimes";
	private final String fileNameScene = "Scenes";
	
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
				SmartAlarmClockService service = null;
				if(getActivity() instanceof MainActivity){
					service =((MainActivity)getActivity()).mSACService;
				}else if(getActivity() instanceof DetailsActivity){
					service = ((DetailsActivity)getActivity()).mSACService;
				}
				AlarmTime currentSelection = new AlarmTime();
				EditText description = (EditText) getActivity().findViewById(R.id.alarmDescription);
				currentSelection.setDescription(description.getText().toString());

				TimePicker time = (TimePicker)getActivity().findViewById(R.id.alarmTimePicker);
				DatePicker date = (DatePicker) getActivity().findViewById(R.id.alarmDatePicker);
				currentSelection.setTime(String.format("%02d:%02d", time.getCurrentHour(), time.getCurrentMinute()));
				currentSelection.setDate(String.format("%02d.%02d.%04d", date.getDayOfMonth(), (date.getMonth()+1), date.getYear()));
				Spinner spinner = (Spinner)getActivity().findViewById(R.id.sceneSpinner);
				currentSelection.setScene((Scene)spinner.getSelectedItem());
				InstanceSave.appendToList(fileNameAlarm, currentSelection, getActivity());
				
				IStoredList<AlarmTime> list = InstanceSave.readList(fileNameAlarm, getActivity());
         	   	InstanceSave.sendAlarmTime(service, list.getEntireList().get(list.getEntireList().size()-1), getActivity());	
			}
		});
		

		SceneList sceneList = InstanceSave.<SceneList>readList(fileNameScene, getActivity());
		
		Spinner spinner = (Spinner) getActivity().findViewById(R.id.sceneSpinner);
		
		if(sceneList != null){
			Scene[]scenes = (Scene[]) sceneList.getEntireList().toArray(new Scene[sceneList.getEntireList().size()]);
			ArrayAdapter<Scene> adapter = new ArrayAdapter<Scene>(getActivity(), android.R.layout.simple_list_item_1, scenes);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner.setAdapter(adapter);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
