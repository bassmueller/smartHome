package com.example.smarthome;

import android.app.DialogFragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class AlarmListFragment extends ListFragment{

	private final String fileNameAlarm = "AlarmTimes";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.alarmlist_layout, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		AlarmTimeList alarmList = InstanceSave.<AlarmTimeList>readList(fileNameAlarm, getActivity());
		
		if(alarmList != null){
			setListAdapter(new ListAdapter<AlarmTime>(getActivity(), R.layout.listitem, alarmList.getEntireList()));
		}else{
			//setEmptyText("No alarms saved!");
		}
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		DialogFragment newFragment = new OptionsDialog();
		Bundle args = new Bundle();
		args.putInt("position", position);
		newFragment.setArguments(args);
	    newFragment.show(getFragmentManager(), "optionsDialog");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
}
