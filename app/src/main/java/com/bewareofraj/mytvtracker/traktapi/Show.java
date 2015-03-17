package com.bewareofraj.mytvtracker.traktapi;

import android.content.Context;

import com.bewareofraj.mytvtracker.R;

import org.joda.time.DateTime;

import java.io.Serializable;

/**
 * A Show class represents a TV show.
 * @author Rajesh
 *
 */
public class Show implements Serializable {
	
	private String mTitle;
	private int mYear;
	private DateTime mFirstAired;
	private String mCountry;
	private String mOverview;
	private String mStatus;
	private String mNetwork;
	private String mAirDay;
	private String mAirTime;
    private String mAirTimeZone;
	private String mTvdbId;
	private String mPosterUrl;
	private int mSeasons;
	private int mFirstAiredTimestamp;
    private String mShowTime;
    private String mImdbId;
    private int mRunTimeMinutes;
    private Boolean mIsOnAir;
	
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

	public DateTime getFirstAired() {
		return mFirstAired;
	}

	public void setFirstAired(DateTime mFirstAired) {
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

    public String getShowTime() {
        return mShowTime;
    }

	public int getFirstAiredTimeStamp() {
		return mFirstAiredTimestamp;
	}

	public void setFirstAiredTimeStamp(int mFirstAiredTimeStamp) {
		this.mFirstAiredTimestamp = mFirstAiredTimeStamp;
	}

    public String getImdbId() {
        return mImdbId;
    }

    public void setImdbId(String id) {
        this.mImdbId = id;
    }

    public String getAirTimeZone() {
        return mAirTimeZone;
    }

    public void setAirTimeZone(String mAirTimeZone) {
        this.mAirTimeZone = mAirTimeZone;
    }

    public int getRunTimeMinutes() {
        return mRunTimeMinutes;
    }

    public void setRunTimeMinutes(int mRunTimeMinutes) {
        this.mRunTimeMinutes = mRunTimeMinutes;
    }

    public void setOnAir(boolean isOnAir) {
        mIsOnAir = isOnAir;
    }

    public Boolean isOnAir() {
        return mIsOnAir;
    }

    public String makeShowTimeString(Context context) {
        String showTime = "";
        if (getStatus().equalsIgnoreCase("ended")) {
            showTime = context.getString(R.string.show_ended);
        } else if (getStatus().equalsIgnoreCase("returning series")) {
            if (isOnAir()) {
                showTime = "Airs: " + getAirDay() + " at " + getAirTime();
            } else {
                showTime = context.getString(R.string.show_on_break);
            }
        } else if (getStatus().equalsIgnoreCase("cancelled")) {
            showTime = context.getString(R.string.show_cancelled);
        } else if (getStatus().equalsIgnoreCase("returning series")) {
            showTime = context.getString(R.string.show_in_production);
        }
        return showTime;
    }

}
