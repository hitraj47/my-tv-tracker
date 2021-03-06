package com.bewareofraj.mytvtracker.watchlist;

import java.util.ArrayList;

/**
 * This class defines how Watch List Items will be grouped. Currently they will
 * be grouped alphabetically.
 * 
 * @author Rajesh
 * 
 */
public class WatchListGroup {

	private String mName;
	private ArrayList<WatchListChild> mItems;

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		this.mName = name;
	}

	public ArrayList<WatchListChild> getItems() {
		return mItems;
	}

	public void setItems(ArrayList<WatchListChild> items) {
		this.mItems = items;
	}

}