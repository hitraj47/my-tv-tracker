package com.bewareofraj.mytvtracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.bewareofraj.mytvtracker.traktapi.TraktApiHelper;
import com.bewareofraj.mytvtracker.util.CachedCustomRequest;
import com.bewareofraj.mytvtracker.util.MyApplication;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class SplashActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

        updateShowCalendarIds();

        /*
        DateTime showCalendarLastUpdated = MyApplication.getInstance().getShowCalendarLastUpdated();
        if (showCalendarLastUpdated == null || isShowCalendarOld(showCalendarLastUpdated)) {
            updateShowCalendarIds();
        } else {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }*/
	}

    private boolean isShowCalendarOld(DateTime showCalendarLastUpdated) {
        DateTime now = new DateTime();
        Period period = new Period(showCalendarLastUpdated, now);
        return period.getDays() > 1;
    }

    public void updateShowCalendarIds() {
        int numDays = 7;
        String query = TraktApiHelper.getShowCalendar(numDays);
        final String requestTag = "update_show_calendar_ids";

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                VolleyLog.v(requestTag, "Error: " + volleyError.getMessage());
            }
        };

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String resultString) {
                try {
                    ArrayList<String> ids = TraktApiHelper.buildIdListFromCalendar(resultString);
                    MyApplication.getInstance().setShowCalendarIds(ids);
                    MyApplication.getInstance().setShowCalendarLastUpdated(new DateTime());
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Cache cache = MyApplication.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(query);
        if (entry != null) {
            String resultString = null;
            try {
                Log.d("test", "cached entry exists");
                resultString = new String(entry.data, "UTF-8");
                ArrayList<String> ids = TraktApiHelper.buildIdListFromCalendar(resultString);
                MyApplication.getInstance().setShowCalendarIds(ids);
                MyApplication.getInstance().setShowCalendarLastUpdated(new DateTime());
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            CachedCustomRequest request = new CachedCustomRequest(Request.Method.GET, query, responseListener, errorListener, MyApplication.getInstance().getTraktHeaders());
            MyApplication.getInstance().addToRequestQueue(request, requestTag);
        }

    }

}
