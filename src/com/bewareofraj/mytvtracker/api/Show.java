package com.bewareofraj.mytvtracker.api;

import java.util.Date;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;

/**
 * A Show class represents a TV show.
 * @author Rajesh
 *
 */
public class Show {
	
	private String mTitle;
	private int mYear;
	private Date mFirstAired;
	private String mCountry;
	private String mOverview;
	private String mStatus;
	private String mNetwork;
	private String mAirDay;
	private String mAirTime;
	private String mTvdbId;
	private String mPosterUrl;
	private int mSeasons;
	
	public Show() {
		
	}

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String mTitle) {
		this.mTitle = mTitle;
	}

	public int getYear() {
		return mYear;
	}

	public void setYear(int mYear) {
		this.mYear = mYear;
	}

	public Date getFirstAired() {
		return mFirstAired;
	}

	public void setFirstAired(Date mFirstAired) {
		this.mFirstAired = mFirstAired;
	}

	public String getCountry() {
		return mCountry;
	}

	public void setCountry(String mCountry) {
		this.mCountry = mCountry;
	}

	public String getOverview() {
		return mOverview;
	}

	public void setOverview(String mOverview) {
		this.mOverview = mOverview;
	}

	public String getStatus() {
		return mStatus;
	}

	public void setStatus(String mStatus) {
		this.mStatus = mStatus;
	}

	public String getNetwork() {
		return mNetwork;
	}

	public void setNetwork(String mNetwork) {
		this.mNetwork = mNetwork;
	}

	public String getAirDay() {
		return mAirDay;
	}

	public void setAirDay(String mAirDay) {
		this.mAirDay = mAirDay;
	}

	public String getAirTime() {
		return mAirTime;
	}

	public void setAirTime(String mAirTime) {
		this.mAirTime = mAirTime;
	}

	public String getTvdbId() {
		return mTvdbId;
	}

	public void setTvdbId(String mTvdbId) {
		this.mTvdbId = mTvdbId;
	}

	public String getPosterUrl() {
		return mPosterUrl;
	}

	public void setPosterUrl(String mPosterUrl) {
		this.mPosterUrl = mPosterUrl;
	}

	public int getSeasons() {
		return mSeasons;
	}

	public void setSeasons(int mSeasons) {
		this.mSeasons = mSeasons;
	}
	
	public String getSizedPosterUrl(String size) {
		String baseUrl = mPosterUrl.substring(0, mPosterUrl.lastIndexOf('.'));
		String ext = mPosterUrl.substring(mPosterUrl.lastIndexOf('.'));
		return baseUrl + size + ext;
	}
	
	public String determineShowTime(String apiKey) {
		String showTime = null;
		if (this.mStatus.equals("Ended")) {
			showTime = "Show Ended";
		} else {
			TraktApiHelper helper = new TraktApiHelper(apiKey);
			try {
				boolean currentlyOnAir = helper.isCurrentlyOnAir(this.mTvdbId);
				if (currentlyOnAir) {
					showTime = this.mAirDay + ", " + this.mAirTime;
				} else {
					showTime = "Currently off-air";
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return showTime;
	}

}
