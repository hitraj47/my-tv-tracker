package com.bewareofraj.mytvtracker.search;

public class SearchResultItem {
	
	private String mName, mTime, mId;
	
	public SearchResultItem() {
		
	}
	
	public SearchResultItem(String name, String time, String id) {
		this.setName(name);
		this.setTime(time);
		this.setId(id);
	}

	public String getName() {
		return mName;
	}

	public void setName(String mName) {
		this.mName = mName;
	}

	public String getTime() {
		return mTime;
	}

	public void setTime(String mTime) {
		this.mTime = mTime;
	}

	public String getId() {
		return mId;
	}

	public void setId(String mId) {
		this.mId = mId;
	}

}
