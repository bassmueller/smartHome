package com.example.smarthome;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class FunctionsFragment extends ListFragment{
	//private final String TAG = FunctionsFragment.class.getSimpleName();
	boolean dual;
    int position = 0;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Populate list with our static array of titles.
        setListAdapter(new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_activated_1, FunctionsList.FUNCTIONS));

        // Check to see if we have a frame in which to embed the details
        // fragment directly in the containing UI.
        View detailsFrame = getActivity().findViewById(R.id.details);
        dual = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;

        if (savedInstanceState != null) {
            // Restore last state for checked position.
            position = savedInstanceState.getInt("currentChoice", 0);
        }

        if (dual) {
            // In dual-pane mode, the list view highlights the selected item.
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            // Make sure our UI is in the correct state.
            showDetails(position);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("currentChoice", position);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        showDetails(position);
    }

    /**
     * Helper function to show the details of a selected item, either by
     * displaying a fragment in-place in the current UI, or starting a
     * whole new activity in which it is displayed.
     */
    void showDetails(int index) {
    	if (dual) {
            // We can display everything in-place with fragments, so update
            // the list to highlight the selected item and show the data.
            getListView().setItemChecked(index, true);

            // Check what fragment is currently shown, replace if needed.
            if (position != index || position == 0) {
            	
            	Fragment details = null;
                switch(index){
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

                // Execute a transaction, replacing any existing fragment
                // with this one inside the frame.
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.details, details);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
            }
        }else {
            // Otherwise we need to launch a new activity to display
            // the dialog fragment with selected text.
            Intent intent = new Intent();
            intent.setClass(getActivity(), DetailsActivity.class);
            intent.putExtra("index", index);
            startActivity(intent);
        }
    	position = index;
    }

}
