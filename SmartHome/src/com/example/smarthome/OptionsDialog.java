package com.example.smarthome;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class OptionsDialog extends DialogFragment {
	
	private final String fileNameAlarm = "AlarmTimes";

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    builder.setTitle(R.string.titleOptionsDialog);
	    builder.setItems(R.array.choicesOptionsDialog, new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int which) {
	               switch(which){
               		case 0:
	               		DialogFragment newFragment = new AlarmChangeDialog();
	               		newFragment.setArguments(getArguments());
	            	    newFragment.show(getFragmentManager(), "changeDialog");
	            	    break;
               		case 1:
               			try{
               				InstanceSave.deleteSpecificObject(fileNameAlarm, getArguments().getInt("position"), getActivity());
                   			MainActivity.toastMessage("Item deleted!", getActivity());
               			}catch(Exception e){
               				e.printStackTrace();
               				MainActivity.toastMessage("ERROR! Item not deleted!", getActivity());
               			}
               			
               			break;
           			default: 
           				OptionsDialog.this.getDialog().cancel();
           				break;
	               }
	           }
	    });
	    return builder.create();
	}

	
}
