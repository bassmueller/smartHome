package com.example.smarthome;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListAdapter<T> extends ArrayAdapter<T> {
	
	private List<T> objects;

	public ListAdapter(Context context, int textViewResourceId, List<T> objects) {
		super(context, textViewResourceId, objects);
		this.objects = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		if (row == null) {     
	        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        row = inflater.inflate(R.layout.listitem, parent, false);
	    }

	    T item = (T) objects.get(position);

	    if (item != null) {

	        TextView tv = (TextView) row.findViewById(R.id.textItem);
	        ImageView iv = (ImageView) row.findViewById(R.id.imageItem);

	        if(item instanceof AlarmTime){
		        if (tv != null) {
		            tv.setText(item.toString());
		        }
		        if (iv != null) {
		        	BitmapDrawable bitmap;
		        	if(((AlarmTime) item).isActive()){
		        		bitmap = (BitmapDrawable)this.getContext().getResources().getDrawable(R.drawable.alarmclock_on);
		        	}else{
		        		bitmap = (BitmapDrawable)this.getContext().getResources().getDrawable(R.drawable.alarmclock_off);
		        	}
					
					iv.setImageBitmap(bitmap.getBitmap());
		        }
	        }
	    }
		return row;
	}
	

}
