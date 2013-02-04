/* Colour Wheel
 * This is a colour wheel widget for android
 * Copyright (C) 2011  Oliver Thearle
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * http://code.google.com/p/colour-wheel/
 */
package com.example.smarthome;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class ColourWheel extends View {

	private Canvas canvas;	
	
	//Canvas width and height
	private int h = -1;
	private int w = -1;
	
	//circle properties
	private Paint paint;
	private Path circle;
	private Point c;
	private int outerRadius;
	private int innerRadius;
	private int circleThickness = 20;
	private int[] colourarry = {Color.RED, Color.MAGENTA, Color.BLUE, Color.CYAN, Color.GREEN, Color.YELLOW, Color.RED};
	
	
	//point click in colour wheel
	private double theta = 0;
	private int quadrant = 0;


	
	//triangle properties
	private Paint triangleG;
	private TrianglePath triangle;
	private int distance;
	private int triangleRed;
	private int triangleBlue;
	private int triangleGreen;
	
	//cursor properties
	private Paint cursorpaint;
	private Path cursor;
	private int cursorx;
	private int cursory;		
	int padding = 10;

	int randomstring = 6;
	
	private int tracking = NONE;
	
	private static int TRIANGLE = 1;
	private static int CIRCLE = 2;
	private static int NONE = 0;
	
	OnColourWheelChangeListener onColourWheelChangeListener = null;
	
	public ColourWheel(Context context){
		super(context);
		initCircleSeekBar();
	}
	
	public ColourWheel(Context context, AttributeSet attrs) {
		super(context, attrs);
		initCircleSeekBar();
	}
	
	private void initCircleSeekBar() {
		
    	canvas = new Canvas();
    	circle = new Path();
    	paint = new Paint();
    	triangleG = new Paint();
    	cursorpaint = new Paint();
    	cursor = new Path();
		c = new Point();
    	triangle = new TrianglePath();
    	

    	this.draw(canvas);
	}
	

	@Override
	protected void onSizeChanged(int width, int height, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(width, height, oldw, oldh);
		
		w = width;
		h = height;
		Log.i("dooms day", String.valueOf(w) + "   " + String.valueOf(h));
		c.set(w/2, h/2);
		drawCircle();
		drawTriangle();
	}
	
	private void drawCircle() {
		outerRadius = Math.min(h,w)/2;
		circleThickness = (int) (outerRadius*0.15);
		innerRadius = outerRadius - circleThickness;

    	circle.addArc(new RectF(c.x - outerRadius + circleThickness/2, c.y - outerRadius + circleThickness/2, c.x + outerRadius - circleThickness/2, c.y + outerRadius - circleThickness/2 ), 0, 360);
    	circle.moveTo(c.x, c.y);
    	paint.setShader(new SweepGradient(w/2,h/2, colourarry, null));
    	paint.setStyle(Style.STROKE);
    	paint.setStrokeWidth(circleThickness);
	}
	
	private void drawTriangle() {
    	triangle.addCenter(c.x, c.y);    	
    	int[][] tmp = {{innerRadius,0},{(int)(-innerRadius*Math.sin(Math.PI/6)),(int)(innerRadius*Math.cos(Math.PI/6))},{(int)(-innerRadius*Math.sin(Math.PI/6)),(int)(-innerRadius*Math.cos(Math.PI/6))}};
    	triangle.addVirtices(tmp);
    	distance = (int)(innerRadius*(Math.cos(Math.PI/6) +1));
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
			super.onDraw(canvas);
			canvas.drawColor(randomstring);
    	if(circle != null){
    	//draw circle
    	canvas.drawPath(circle, paint);
    	}
    	if(w != -1) {
    	//make the center triangle
    	//draw triangle three times with different gradient patterns
    	angle2Colour();
    	int temp = Color.rgb(triangleRed, triangleGreen, triangleBlue);
    	triangleG.setShader(new RadialGradient(triangle.x1, triangle.y1, distance, temp, temp, Shader.TileMode.MIRROR));
    	canvas.drawPath(triangle, triangleG);
    	triangleG.setShader(new RadialGradient(triangle.x2, triangle.y2, distance, Color.WHITE,Color.TRANSPARENT, Shader.TileMode.MIRROR));
    	canvas.drawPath(triangle, triangleG);
    	triangleG.setShader(new RadialGradient(triangle.x3, triangle.y3, distance, Color.BLACK,Color.TRANSPARENT, Shader.TileMode.MIRROR));
    	canvas.drawPath(triangle, triangleG);
    	
    	//draw cursor
    	cursorpaint.setColor(Color.BLACK);
    	cursorpaint.setStyle(Style.STROKE);
    	cursorpaint.setStrokeWidth(4);
    	cursor.reset();
    	cursor.addCircle(cursorx, cursory, 7, Path.Direction.CCW);
    	if(!cursor.isEmpty()){
    		canvas.drawPath(cursor, cursorpaint);
    	}
		}
	}
	

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		if (!isEnabled()) {
            return false;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                setPressed(true);
                onStartTrackingTouch(event);
                trackTouchEvent(event);
                break;

            case MotionEvent.ACTION_MOVE:
                trackTouchEvent(event);
                attemptClaimDrag();
                break;

            case MotionEvent.ACTION_UP:
                trackTouchEvent(event);
                onStopTrackingTouch();
                setPressed(false);
                // ProgressBar doesn't know to repaint the thumb drawable
                // in its inactive state when the touch stops (because the
                // value has not apparently changed)
                invalidate();
                break;

            case MotionEvent.ACTION_CANCEL:
                onStopTrackingTouch();
                setPressed(false);
                invalidate(); // see above explanation
                break;
        }
		
		return true;
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);
		
		setMeasuredDimension(width,height);
	}

	private void onStartTrackingTouch(MotionEvent event) {
		int py = (int)event.getY();
		int px = (int)event.getX();
		int r = (int)Math.sqrt(Math.pow(px-c.x, 2) + Math.pow(py-c.y, 2));
		if (triangle.isContained(px,py)) {
			tracking = TRIANGLE;
		}
		else if(r < (outerRadius + padding) & (r > innerRadius - padding)) {
			tracking = CIRCLE;
		}
		
	}

	private void onStopTrackingTouch() {
		tracking = NONE; 
		
	}

	private void attemptClaimDrag() {
		// TODO Auto-generated method stub
		
	}

	private void trackTouchEvent(MotionEvent event) {
		
		int py = (int)event.getY() - c.y;
		int px = (int)event.getX() - c.x;

		if(tracking == TRIANGLE){
			triangle.toBarycentric(px+c.x, py+c.y);
		} else if(tracking == CIRCLE) {
			theta = Math.atan2(py, px);
			triangle.rotate(theta);
			triangle.invBarycentric();
			tracking = CIRCLE;
			cursorx = triangle.px;
			cursory = triangle.py;
		}
		
		triangle.invBarycentric();
		cursorx = triangle.px;
		cursory = triangle.py;
		randomstring = barry2Colour(triangle.v, triangle.w);
		this.invalidate();
		//check if point is in radius
	}
	
	
	private void angle2Colour() {

		//0 to 60
		if(theta > 0 & theta <= Math.PI/3) {
			triangleRed = 255;
			triangleGreen = 0;
			triangleBlue= (int)(theta*3*255/Math.PI);
			quadrant = 0;
		}
		//60 to 120
		else if (theta > Math.PI/3 & theta <= 2*Math.PI/3) {
			triangleRed = (int)(255*(2 - (3*theta/Math.PI)));
			triangleGreen = 0;
			triangleBlue = 255;
			quadrant = 1;
		}
		//120 to 180
		else if (theta > 2*Math.PI/3 & theta <= Math.PI) {
			triangleRed = 0;
			triangleGreen = (int)(255*((3*theta/Math.PI) - 2));
			triangleBlue = 255;
			quadrant = 2;
		}	
		//180 to 240
		else if (-theta >= 2*Math.PI/3 & -theta < Math.PI) {
			triangleRed = 0;
			triangleGreen = 255;
			triangleBlue = (int)(255*(-2 - (3*theta/Math.PI)));
			quadrant = 3;
		}
		//240 to 300
		else if (-theta >= Math.PI/3 & -theta < 2*Math.PI/3) {
			triangleRed = (int)(255*(3*theta/Math.PI + 1));
			triangleGreen = 255;
			triangleBlue = 0;
			quadrant = 4;
		}
		//300 to 360
		else if (-theta >= 0 & -theta < Math.PI/3) {
			triangleRed = 255;
			triangleGreen = (int)(255*(-theta*3/Math.PI));
			triangleBlue = 0;
			quadrant = 5;
		}
		
		
	}
	
	private int barry2Colour(double u, double v) {
		int red = 0;
		int green = 0;
		int blue = 0;		
			
			switch(quadrant) {
			//0 to 60
			case 0:
				red = (int) (255*(1-v));
				green = (int) (255*u);
				blue= (int)(triangleBlue - (triangleBlue - 255)*u - triangleBlue*v);
				break;
			//60 to 120
			case 1: 
				red = (int)(triangleRed - (triangleRed - 255)*u - triangleRed*v);
				green = (int) (255*u);
				blue = (int) (255*(1-v));
				break;
			//120 to 180
			case 2:
				red = (int) (255*u);
				green = (int)(triangleGreen - (triangleGreen - 255)*u - triangleGreen*v);
				blue = (int) (255*(1-v));
				break;	
			//180 to 240
			case 3:
				red = (int) (255*u);
				green = (int) (255*(1-v));
				blue = (int)(triangleBlue - (triangleBlue - 255)*u - triangleBlue*v);
				break;
			//240 to 300
			case 4:
				red = (int)(triangleRed + 255 - triangleRed*u - (triangleRed + 255)*v);
				green = (int) (255*(1-v));
				blue = (int) (255*u);
				break;
			//300 to 360
			case 5:
				red = (int) (255*(1-v));
				green = (int)(triangleGreen - (triangleGreen - 255)*u - triangleGreen*v);
				blue = (int) (255*u);
				break;
			}
			

			Log.i("whoopsy",String.valueOf(theta) + "-" + String.valueOf(red) + "-" + String.valueOf(green) + "-" + String.valueOf(blue) + " - " + String.valueOf(u+v) + "-" + String.valueOf(v));
			
			if (onColourWheelChangeListener != null) {
				onColourWheelChangeListener.onColourChanged(this, red, green, blue);
			}

			return Color.rgb(red, green, blue);
		

	}
	
	public void setSize(int x, int y){
		h = y;
		w = x;
	}
	
	public void setCirleThickness(int t){
		circleThickness = t;
	}
	
	public void setColour(int red, int green, int blue){
		if (red == 255) {
			
			
		}
	}
	
	public void setOnColourWheelChangeListener (OnColourWheelChangeListener listener) {
		onColourWheelChangeListener = listener;
	}
	
	public interface OnColourWheelChangeListener{
		public void onColourChanged (ColourWheel colourWheel, int red, int green, int blue);
		public void onStartTrackingTouch (ColourWheel colourWheel);
		public void onStopTrackingTouch (ColourWheel colourWheel);
	}
	
	
}
