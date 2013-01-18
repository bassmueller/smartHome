package com.example.smarthome;


import java.util.List;

public interface IStoredList<T> {
	
	/**
	 * 
	 * @return The list as List<T> stored in that facade-pattern
	 */
	public List<T> getEntireList();
	
	/**
	 * 
	 * @param object to be added to the internal list of that facade-pattern
	 */
	public void addItem(T object);
	
	/**
	 * Removes item of the internal list of that facade-pattern at given location
	 * @param location of object to be deleted in list 
	 * @return deleted object
	 */
	public T removeItem(int location);
	
}
