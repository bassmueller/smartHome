package com.example.smarthome;

import com.example.smarthome.AlarmChangeDialog.AlarmChangeDialogListener;
import com.example.smarthome.ColourWheel.OnColourWheelChangeListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.SeekBar;


@SuppressWarnings("unused")
public class ColorWheelDialog extends DialogFragment {

	private ColorWheelDialogListener listener;
	final static OnColourWheelChangeListener RGBLISTENER = new OnColourWheelChangeListener () {
		
		@Override
		public void onColourChanged(ColourWheel colourWheel, int red, int green, int blue) {
			Log.d(TAG, "Color changed to: RED: " + red + "GREEN: " + green + "BLUE: " + blue);
			ColorWheelDialog.red = red;
			ColorWheelDialog.green = green;
			ColorWheelDialog.blue = blue;
		}

		@Override
		public void onStartTrackingTouch(ColourWheel colourWheel) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStopTrackingTouch(ColourWheel colourWheel) {

		}
	};

	public interface ColorWheelDialogListener{
		public void onDialogPositiveClickCW(DialogFragment dailog);
		public void onDialogNegativeClickCW(DialogFragment dailog);
	}
	
	/** Called when the activity is first created. */
	SeekBar seekbar;
	
	ColourWheel seekbar2;
	
	private static final String TAG = ColourWheelActivity.class.getSimpleName();
	public static String RGB_COLORS = "rgb_colors";
	private static int red;
	private static int green;
	private static int blue;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try{
			listener = (ColorWheelDialogListener)activity;
		}catch(ClassCastException e){
			throw new ClassCastException(activity.toString() + " must implement ColorWheelDialogListener");
		}
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }
    
    @Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    // Get the layout inflater
	    LayoutInflater inflater = getActivity().getLayoutInflater();

	    // Inflate and set the layout for the dialog
	    // Pass null as the parent view because its going in the dialog layout
	    builder.setView(inflater.inflate(R.layout.colour_wheel_main, null))
	    // Add action buttons
	           .setPositiveButton(R.string.changeColor, new DialogInterface.OnClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int id) {
	            	   saveColors();
	            	  
	               }
	           })
	           .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	               
	               }
	           });
	    return builder.create();
	}
    
	@Override
	public void onStart() {
		super.onStart();
        seekbar2 = (ColourWheel) getDialog().findViewById(R.id.seekBar1);
        //seekbar = (SeekBar) findViewById(R.id.seekBar1);
        seekbar2.setOnColourWheelChangeListener( new OnColourWheelChangeListener () {
    		
    		@Override
    		public void onColourChanged(ColourWheel colourWheel, int red, int green, int blue) {
    			Log.d(TAG, "Color changed to: RED: " + red + "GREEN: " + green + "BLUE: " + blue);
    			ColorWheelDialog.red = red;
    			ColorWheelDialog.green = green;
    			ColorWheelDialog.blue = blue;
    		}

    		@Override
    		public void onStartTrackingTouch(ColourWheel colourWheel) {
    			
    		}

    		@Override
    		public void onStopTrackingTouch(ColourWheel colourWheel) {
    			saveColors();
    		}
    	});
	}

    private void saveColors(){
    	StringBuilder sb = new StringBuilder();
		sb.append(String.format("%03d", red));
		sb.append(String.format("%03d", green));
		sb.append(String.format("%03d", blue));
		if(getActivity() instanceof MainActivity){
			MainActivity.rgbLED = sb.toString();
		}else if(getActivity() instanceof DetailsActivity){
			DetailsActivity.rgbLED = sb.toString();
		}
		

		Log.d(TAG, "String that send to MainActivity: " + sb.toString());
		
    }
}

