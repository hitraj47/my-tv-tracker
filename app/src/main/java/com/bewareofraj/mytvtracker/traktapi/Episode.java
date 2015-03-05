package com.bewareofraj.mytvtracker.traktapi;

import java.util.Date;

public class Episode {
	
	private int mSeason;
	private int mEpisodeNumber;
	private String mTitle;
	private String mImageUrl;
	private Date mFirstAired;
	private String mOverview;
	
	public Episode(int season) {
		this.mSeason = season;
	}
	
	public int getSeason() {
		return mSeason;
	}
	public void setSeason(int mSeason) {
		this.mSeason = mSeason;
	}

	public int getEpisodeNumber() {
		return mEpisodeNumber;
	}

	public void setEpisodeNumber(int mEpisodeNumber) {
		this.mEpisodeNumber = mEpisodeNumber;
	}

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String mTitle) {
		this.mTitle = mTitle;
	}

	public String getImageUrl() {
		return mImageUrl;
	}

	public void setImageUrl(String mImageUrl) {
		this.mImageUrl = mImageUrl;
	}

	public Date getFirstAired() {
		return mFirstAired;
	}

	public void setFirstAired(int timestamp) {
		this.mFirstAired = getDateFromUnixTimestamp(timestamp);
	}

	public String getOverview() {
		return mOverview;
	}

	public void setOverview(String mOverview) {
		this.mOverview = mOverview;
	}
	
	private Date getDateFromUnixTimestamp(int timestamp) {
		Date date = new Date((long) timestamp * 1000);
		return date;
	}
}
