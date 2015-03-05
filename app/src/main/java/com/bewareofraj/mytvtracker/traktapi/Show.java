package com.bewareofraj.mytvtracker.traktapi;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.bewareofraj.mytvtracker.R;
import com.bewareofraj.mytvtracker.util.ApplicationController;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.Serializable;
import java.util.Date;

/**
 * A Show class represents a TV show.
 * @author Rajesh
 *
 */
public class Show implements Serializable {
	
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
	private int mFirstAiredTimestamp;
    private String mShowTime;
	
	public Show() {
		
	}

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String mTitle) {
		this.mTitle = mTitle;
	}

	public int getYear() {
		if (mFirstAiredTimestamp == 0) {
			return 0;
		} else {
			return mYear;
		}
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
	
	public void determineShowTime(final Context context) {
		if (mStatus.equals("Ended")) {
            mShowTime = context.getString(R.string.show_ended);
		} else if (mFirstAiredTimestamp == 0) {
			mShowTime = context.getString(R.string.show_not_started);
		} else {
            final String requestTag = "on_air";
			String query = TraktApiHelper.getCurrentlyOnAirQuery(context.getString(R.string.trakt_api_key));
            
            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    VolleyLog.d(requestTag, "Error: " + volleyError.getMessage());
                }
            };
            
            Response.Listener<JSONArray> responseListener = new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray jsonArray) {
                    try {
                        boolean currentlyOnAir = TraktApiHelper.getCurrentlyOnAirResult(jsonArray, mTvdbId);
                        if (currentlyOnAir) {
                            mShowTime = mAirDay + ", " + mAirTime;
                        } else {
                            mShowTime = context.getString(R.string.show_on_break);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };

            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(query, responseListener, errorListener);
            ApplicationController.getInstance().addToRequestQueue(jsonArrayRequest, requestTag);
		}
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

}
