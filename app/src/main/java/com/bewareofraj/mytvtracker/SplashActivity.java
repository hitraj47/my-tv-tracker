package com.bewareofraj.mytvtracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.bewareofraj.mytvtracker.traktapi.TraktApiHelper;
import com.bewareofraj.mytvtracker.util.CachedCustomRequest;
import com.bewareofraj.mytvtracker.util.MyApplication;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class SplashActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

        updateShowCalendarIds();
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
                    updateCalendarAndLaunchMainActivity(resultString);
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
                resultString = new String(entry.data, "UTF-8");
                updateCalendarAndLaunchMainActivity(resultString);
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

    private void updateCalendarAndLaunchMainActivity(String resultString) throws JSONException {
        ArrayList<String> ids = TraktApiHelper.buildIdListFromCalendar(resultString);
        MyApplication.getInstance().setShowCalendarIds(ids);
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
