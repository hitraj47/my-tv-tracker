package com.bewareofraj.mytvtracker.watchlist;

/**
 * This is a class for each child item in the Watch List. The child item are the
 * shows. The member variables represent the information that will be displayed.
 * 
 * @author Rajesh
 * 
 */
public class WatchListChild {

	private String mShowName;
	private int mShowImage;
	private String mShowTime;

	public String getName() {
		return mShowName;
	}

	public void setName(String name) {
		this.mShowName = name;
	}

	public int getImage() {
		return mShowImage;
	}

	public void setImage(int image) {
		this.mShowImage = image;
	}

	public String getShowTime() {
		return mShowTime;
	}

	public void setShowTime(String mShowTime) {
		this.mShowTime = mShowTime;
	}
}