/* Triangle Path
 * This is the triangle object found in the middle of the colour wheel
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

import android.graphics.Path;
import android.util.Log;

public class TrianglePath extends Path {

	public int x1 = 0;
	public int y1 = 0;
	public int x2 = 0;
	public int y2 = 0;
	public int x3 = 0;
	public int y3 = 0;
	
	public static int sx1 = 0;
	public static int sy1 = 0;
	public static int sx2 = 0;
	public static int sy2 = 0;
	public static int sx3 = 0;
	public static int sy3 = 0;
	
	public double u;
	public double v;
	public double w;
	
	private double tu;
	private double tv;
	private double tw;
	
	public int px;
	public int py;
	
	private int[][] T = {{0,0},{0,0},{0,0}};
	private float[][] invT = {{0,0},{0,0},{0,0}};
	
	public int cx;
	public int cy;
	
	public TrianglePath () {
		super();
	}
	
	public void addVirtices (int[][] vertices) { 
		sx1 = vertices[0][0] + cx;
		sy1 = vertices[0][1] + cy;
		sx2 = vertices[1][0] + cx;
		sy2 = vertices[1][1] + cy;
		sx3 = vertices[2][0] + cx;
		sy3 = vertices[2][1] + cy;
		
		x1 = sx1;
		y1 = sy1;
		x2 = sx2;
		y2 = sy2;
		x3 = sx3;
		y3 = sy3;
		
		this.reset();
		this.moveTo(x1, y1);
		this.lineTo(x2, y2);
		this.lineTo(x3, y3);
		this.lineTo(x1, y1);
		this.close();
		
		
		
		makeT();
	}
	
	private void makeT () {
		float det;
		T[0][0] = x1-x3;
		T[0][1] = x2-x3;
		T[1][0] = y1-y3;
		T[1][1] = y2-y3;
		det = T[0][0]*T[1][1] - T[1][0]*T[0][1];
		invT[0][0] = T[1][1]/det;
		invT[0][1] = -T[0][1]/det;
		invT[1][0] = -T[1][0]/det;
		invT[1][1] = T[0][0]/det;
	}
	
	public boolean isContained (int x, int y) {
		// split function into two. isInsideTriangle and toBarry
		tu = invT[0][0]*x + invT[0][1]*y - invT[0][0]*x3 - invT[0][1]*y3;
		tv = invT[1][0]*x + invT[1][1]*y - invT[1][0]*x3 - invT[1][1]*y3;
		tw = 1 - tu - tv;
		
		if((tu >= 0) && (tv >=0) && (tu + tv < 1)) {
			return true;
		}
		
		return false;
	}
		
	public void toBarycentric (int x, int y) {
		tu = invT[0][0]*x + invT[0][1]*y - invT[0][0]*x3 - invT[0][1]*y3;
		tv = invT[1][0]*x + invT[1][1]*y - invT[1][0]*x3 - invT[1][1]*y3;
		tw = 1 - tu - tv;
		if((tu >= 0) && (tv >= 0) && (tu + tv < 1)) {
			u = tu;
			v = tv;
			w = tw;
			Log.i("wwww", String.valueOf(u) + " - " + String.valueOf(v) + " - " + String.valueOf(w));
		} else {			
			u = tu;
			v = tv;
			if (tu < 0) {			
				u = 0;
			}
			if (tv < 0){
				v = 0;
			}
			if (tu > 1){
				u = 1;
			}
			if (tv > 1){
				v = 1;
			}

			w = 1 - u - v;
			
			if (w < 0){
				u = u/(u+v);
				v = 1-u;
				w = 0;
			}
			if (w > 1){
				w = 1;
				u = 0;
				v = 0;
			}
		}
	}
	
	public void invBarycentric () {
		px = (int) (T[0][0]*u + T[0][1]*v) + x3;
		py = (int) (T[1][0]*u + T[1][1]*v) + y3;			
	}
	
	public void rotate(double theta) {
		x1 = (int)((sx1 - cx)*Math.cos(theta) - (sy1 - cy)*Math.sin(theta)) + cx;
		y1 = (int)((sx1 - cx)*Math.sin(theta) + (sy1 - cy)*Math.cos(theta)) + cy;
		x2 = (int)((sx2 - cx)*Math.cos(theta) - (sy2 - cy)*Math.sin(theta)) + cx;
		y2 = (int)((sx2 - cx)*Math.sin(theta) + (sy2 - cy)*Math.cos(theta)) + cy;
		x3 = (int)((sx3 - cx)*Math.cos(theta) - (sy3 - cy)*Math.sin(theta)) + cx;
		y3 = (int)((sx3 - cx)*Math.sin(theta) + (sy3 - cy)*Math.cos(theta)) + cy;
		Log.i("WEfW", String.valueOf(x2));
		this.reset();
		this.moveTo(x1, y1);
		this.lineTo(x2, y2);
		this.lineTo(x3, y3);
		this.lineTo(x1, y1);
		this.close();
	
		makeT();
	}
	
	public void addCenter(int x, int y){
		cx = x;
		cy = y;
	}

}
