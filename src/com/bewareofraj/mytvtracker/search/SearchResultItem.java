package com.bewareofraj.mytvtracker.search;

public class SearchResultItem {
	
	private String mName, mId, mImageUrl;
	
	public SearchResultItem() {
		
	}
	
	public SearchResultItem(String name, String id, String imageUrl) {
		this.setName(name);
		this.setId(id);
		this.setImageUrl(imageUrl);
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

	public String getImageUrl() {
		return mImageUrl;
	}

	public void setImageUrl(String mImageUrl) {
		this.mImageUrl = mImageUrl;
	}

}
