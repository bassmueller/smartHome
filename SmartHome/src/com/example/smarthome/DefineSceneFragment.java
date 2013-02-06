package com.example.smarthome;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ToggleButton;

public class DefineSceneFragment extends Fragment{
	
	private boolean isLightShow = false;
	private boolean isSoundEffects = false;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.definescene_layout, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		final String fileName = "Scenes";
		
		Button button = (Button) getActivity().findViewById(R.id.sceneButtonColorWheel);
		button.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
				DialogFragment newFragment = new ColorWheelDialog();
			    newFragment.show(getFragmentManager(), "colorWheelDialog");
		    }
		});
		
		Switch isLightShowSwitch = (Switch)getActivity().findViewById(R.id.lightShow);
		isLightShowSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					isLightShow = true;
				}else{
					isLightShow = false;
				}
			}
		});
		
		Switch isSoundEffectsSwitch = (Switch)getActivity().findViewById(R.id.audio);
		isSoundEffectsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					isSoundEffects = true;
				}else{
					isSoundEffects = false;
				}
			}
		});
		
		//Function called by the Save-Button
		Button save = (Button)getActivity().findViewById(R.id.sceneButtonSave);
		save.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SceneList sceneList = null;
				
				try{
					FileInputStream fis = getActivity().openFileInput(fileName);
					ObjectInputStream is = new ObjectInputStream(fis);
					sceneList = (SceneList) is.readObject();
					is.close();
				}catch(FileNotFoundException e){
					sceneList = new SceneList();
				}catch(Exception e){
					e.printStackTrace();
				}
				
				if(sceneList != null){
					Scene currentSelection = new Scene();
					EditText description = (EditText) getActivity().findViewById(R.id.descriptionScene);
					currentSelection.setDescription(description.getText().toString());
					currentSelection.setLightShow(isLightShow);
					currentSelection.setSoundEffects(isSoundEffects);
					currentSelection.setRgbLED(MainActivity.rgbLED);
					
					sceneList.addItem(currentSelection);
					
					FileOutputStream fos;
					ObjectOutputStream os;
					try {
						fos = getActivity().openFileOutput(fileName, Context.MODE_PRIVATE);
						os = new ObjectOutputStream(fos);
						os.writeObject(sceneList);
						os.close();
						toastMessage("Saved!");
					} catch (Exception e) {
						e.printStackTrace();
						toastMessage("Not saved!");
					}
				}else{
					toastMessage("Not saved!");
				}
			}
		});
		
		ToggleButton toggle = (ToggleButton) getActivity().findViewById(R.id.togglebuttonTestLED);
		toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
		    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		    	SmartAlarmClockService service = null;
				if(getActivity() instanceof MainActivity){
					service =((MainActivity)getActivity()).mSACService;
				}else if(getActivity() instanceof DetailsActivity){
					service = ((DetailsActivity)getActivity()).mSACService;
				}
				
		    	if (isChecked && (service != null)) {
		    		String rgbColor = null;
		    		if(getActivity() instanceof MainActivity){
						rgbColor = MainActivity.rgbLED;
					}else if(getActivity() instanceof DetailsActivity){
						rgbColor = DetailsActivity.rgbLED;
					}
		    		service.write("Cu" + rgbColor +"/");
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
		    		service.write("La/");
		        } else {
		        	service.write("Lo/");
		        }
		    }
		});
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	private void toastMessage(String text){
		Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
	}

}
