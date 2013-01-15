package com.example.smarthome;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream;

import android.app.DialogFragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class AlarmListFragment extends ListFragment{

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		String fileName = "AlarmTimes";
		AlarmTimeList timeList = null;
		
		try{
			FileInputStream fis = getActivity().openFileInput(fileName);
			ObjectInputStream is = new ObjectInputStream(fis);
			timeList = (AlarmTimeList) is.readObject();
			is.close();
		}catch(FileNotFoundException e){
			toastMessage("File not found!");
		}catch(Exception e){
			toastMessage("Unknown error!");
			e.printStackTrace();
		}
		if(timeList != null){
			AlarmTime[]times = (AlarmTime[]) timeList.getAlarmTimeList().toArray(new AlarmTime[timeList.getAlarmTimeList().size()]);
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
	    newFragment.show(getFragmentManager(), "optionsDialog");
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