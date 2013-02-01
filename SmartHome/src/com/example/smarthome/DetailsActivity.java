package com.example.smarthome;

import com.example.smarthome.AlarmChangeDialog.AlarmChangeDialogListener;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.res.Configuration;
import android.os.Bundle;

public class DetailsActivity extends Activity implements AlarmChangeDialogListener{

	 @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);

         if (getResources().getConfiguration().orientation
                 == Configuration.ORIENTATION_LANDSCAPE) {
             // If the screen is now in landscape mode, we can show the
             // dialog in-line with the list so we don't need this activity.
             finish();
             return;
         }

         if (savedInstanceState == null) {
             // During initial setup, plug in the details fragment.
        	 Fragment details = null;
             switch(getIntent().getExtras().getInt("index")){
             	case 0: details = (Fragment) getFragmentManager().findFragmentById(R.id.alarmClock_layout);
             			if(details == null){
             				details = new AlarmClockFragment();
             			}
             			break;
             	case 1: details = (Fragment) getFragmentManager().findFragmentById(R.id.alarmList_layout);
                     	if(details == null){
             				details = new AlarmListFragment();
             			}
     					break;
             	case 2: details = (Fragment) getFragmentManager().findFragmentById(R.id.definescene_layout);
                     	if(details == null){
             				details = new DefineSceneFragment();
             			}
     					break;
     			default: details = (Fragment) getFragmentManager().findFragmentById(R.id.alarmClock_layout);
             			if(details == null){
             				details = new AlarmClockFragment();
             			}
     					break;
             }

             //details.setArguments(getIntent().getExtras());
             getFragmentManager().beginTransaction().add(android.R.id.content, details).commit();
         }
     }

	@Override
	public void onDialogPositiveClick(DialogFragment dailog) {
		InstanceSave.updateAlarmListFragment(this);
		
	}

	@Override
	public void onDialogNegativeClick(DialogFragment dailog) {
		// TODO Auto-generated method stub
		
	}

}
