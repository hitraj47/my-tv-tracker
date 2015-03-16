package com.bewareofraj.mytvtracker.traktapi;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.bewareofraj.mytvtracker.R;

import org.joda.time.DateTime;
import org.json.JSONException;

import java.io.Serializable;
import java.util.ArrayList;

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
    private boolean mIsOnAir;
	
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

    /*
	public void determineShowTime(final Context context) {
		if (mStatus.equals("Ended")) {
            mShowTime = context.getString(R.string.show_ended);
		} else if (mFirstAiredTimestamp == 0) {
			mShowTime = context.getString(R.string.show_in_production);
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
            MyApplication.getInstance().addToRequestQueue(jsonArrayRequest, requestTag);
		}
	}*/
    
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

    public boolean isOnAir() {
        return mIsOnAir;
    }

    public String makeShowTimeString(Context context) {
        String showTime = "";
        if (getStatus().equalsIgnoreCase("ended")) {
            showTime = context.getString(R.string.show_ended);
        } else if (getStatus().equalsIgnoreCase("returning series")) {
            //TODO: determine if show is currently airing and display day and time otherwise display on break string
            determineIfShowOnAir();
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

    private void determineIfShowOnAir() {
        boolean onAir = false;
        final ArrayList<String> ids = new ArrayList<>();
        int numDays = 7;
        final String requestTag = "calendar_query";
        String query = TraktApiHelper.getShowCalendar(numDays);

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                VolleyLog.d(requestTag, "Error: " + volleyError.getMessage());
            }
        };

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String resultString) {
                try {
                    setOnAir(TraktApiHelper.isOnAir(resultString, getImdbId()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }
}
