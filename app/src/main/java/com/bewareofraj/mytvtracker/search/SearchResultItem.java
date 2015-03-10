package com.bewareofraj.mytvtracker.search;

public class SearchResultItem {
	
	private String mName, mId, mImageUrl, mYear, mDescription;

	public SearchResultItem(String name, String id, String imageUrl, String year, String description) {
		this.setName(name);
		this.setId(id);
		this.setImageUrl(imageUrl);
		this.setYear(year);
		this.setDescription(description);
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

	public String getYear() {
		return mYear;
	}

	public void setYear(String mYear) {
		this.mYear = mYear;
	}

	public String getDescription() {
		return mDescription;
	}

	public void setDescription(String description) {
		this.mDescription = description;
	}

}
