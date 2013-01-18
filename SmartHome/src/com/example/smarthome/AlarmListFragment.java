package com.example.smarthome;

import com.example.smarthome.data.AlarmTime;
import com.example.smarthome.data.AlarmTimeList;

import android.app.DialogFragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class AlarmListFragment extends ListFragment{

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		String fileName = "AlarmTimes";

		MainActivity activity = (MainActivity)getActivity();
		AlarmTimeList alarmList = activity.<AlarmTimeList>readList(fileName);
		
		if(alarmList != null){
			AlarmTime[]times = (AlarmTime[]) alarmList.getEntireList().toArray(new AlarmTime[alarmList.getEntireList().size()]);
			setListAdapter(new ArrayAdapter<AlarmTime>(getActivity(), android.R.layout.simple_list_item_1, times));
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.alarmlist_layout, container, false);
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		DialogFragment newFragment = new OptionsDialog();
		newFragment.setArguments(new Bundle(position));
	    newFragment.show(getFragmentManager(), "optionsDialog");
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
}
