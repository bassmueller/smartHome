/* Colour Wheel Demo
 * Demo of the colour wheel widget
 * Copyright (C) 2011  Oliver Thearle
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 *
 *http://code.google.com/p/colour-wheel/
 */
 package com.example.smarthome;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;

import com.example.smarthome.ColourWheel.OnColourWheelChangeListener;

public class ColourWheelActivity extends Activity {
    /** Called when the activity is first created. */
	SeekBar seekbar;
	
	ColourWheel seekbar2;
	
	private static final String TAG = ColourWheelActivity.class.getSimpleName();
	public static String RGB_COLORS = "rgb_colors";
	private static int red;
	private static int green;
	private static int blue;
	
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.colour_wheel_main);
        seekbar2 = (ColourWheel) findViewById(R.id.seekBar1);
        //seekbar = (SeekBar) findViewById(R.id.seekBar1);
        seekbar2.setOnColourWheelChangeListener(RGBLISTENER);
    }
    
    @Override
	public void onBackPressed() {
    	sendRGBColors();
    }
    
    
    private void sendRGBColors(){
		StringBuilder sb = new StringBuilder();
		sb.append("Cu");
		String.format("%03d", red);
		sb.append(String.format("%03d", red));
		sb.append(String.format("%03d", green));
		sb.append(String.format("%03d", blue));
		sb.append("/");
        
		Log.d(TAG, "String that send to MainActivity: " + sb.toString());
		
        Intent intent = new Intent();
        intent.putExtra(RGB_COLORS, sb.toString());

        // Set result and finish this Activity
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
    
 final OnColourWheelChangeListener RGBLISTENER = new OnColourWheelChangeListener () {

		@Override
		public void onColourChanged(ColourWheel colourWheel, int red, int green, int blue) {
			Log.d(TAG, "Color changed to: RED: " + red + "GREEN: " + green + "BLUE: " + blue);
			ColourWheelActivity.red = red;
			ColourWheelActivity.green = green;
			ColourWheelActivity.blue = blue;
		}

		@Override
		public void onStartTrackingTouch(ColourWheel colourWheel) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStopTrackingTouch(ColourWheel colourWheel) {
			sendRGBColors();
		}
    	
    };
}