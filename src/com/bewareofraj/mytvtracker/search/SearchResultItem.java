package com.bewareofraj.mytvtracker.search;

public class SearchResultItem {
	
	private String mName, mId;
	
	public SearchResultItem() {
		
	}
	
	public SearchResultItem(String name, String id) {
		this.setName(name);
		this.setId(id);
	}

	public String getName() {
		return mName;
	}

	public void setName(String mName) {
		this.mName = mName;
	}

	public String getId() {
		return mId;
	}

	public void setId(String mId) {
		this.mId = mId;
	}

}
