package com.bewareofraj.mytvtracker;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.bewareofraj.mytvtracker.traktapi.TraktApiHelper;
import com.bewareofraj.mytvtracker.tvshow.ShowListActivity;
import com.bewareofraj.mytvtracker.util.CustomRequest;
import com.bewareofraj.mytvtracker.util.MyApplication;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.json.JSONException;

import java.util.ArrayList;

public class SplashActivity extends Activity {

    public static final String EXTRA_LAUNCH_ACTIVITY = "launch_activity";
    public static final String EXTRA_ACTIVITY_MAIN = "main_activity";
    public static final String EXTRA_ACTIVITY_SHOW_DETAIL = "show_detail_activity";
    public static final String EXTRA_SHOW_ID = "show_id";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            SharedPreferences preferences = getSharedPreferences(MyApplication.SHARED_PREFERENCES_FILE, MODE_PRIVATE);
            String lastUpdatedString = preferences.getString(MyApplication.KEY_SHOW_CAL_LAST_UPDATED, null);
            if (lastUpdatedString == null) {
                updateShowCalendarAndLaunchActivity(EXTRA_ACTIVITY_MAIN);
            } else {
                DateTime now = new DateTime();
                DateTime lastUpdated = new DateTime(lastUpdatedString);
                Period period = new Period(lastUpdated, now);
                if (period.getDays() > 1) {
                    updateShowCalendarAndLaunchActivity(EXTRA_ACTIVITY_MAIN);
                } else {
                    launchActivity(EXTRA_ACTIVITY_MAIN);
                }
            }
        } else {
            String activityToLaunch = extras.getString(EXTRA_LAUNCH_ACTIVITY);
            updateShowCalendarAndLaunchActivity(activityToLaunch);
        }
	}

    private void launchActivity(String activityToLaunch) {
        if (activityToLaunch.equals(EXTRA_ACTIVITY_MAIN)) {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
        } else if (activityToLaunch.equals(EXTRA_ACTIVITY_SHOW_DETAIL)) {
            Intent intent = new Intent(SplashActivity.this, ShowListActivity.class);
            intent.putExtra(ShowListActivity.EXTRA_SHOW_ID, (String) getIntent().getExtras().get(EXTRA_SHOW_ID));
            startActivity(intent);
        }
        finish();
    }

    private void updateShowCalendarAndLaunchActivity(final String activityToLaunch) {
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
                    SharedPreferences.Editor editor = getSharedPreferences(MyApplication.SHARED_PREFERENCES_FILE, MODE_PRIVATE).edit();
                    editor.putString(MyApplication.KEY_SHOW_CAL_LAST_UPDATED, new DateTime().toString());
                    editor.commit();
                    launchActivity(activityToLaunch);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        CustomRequest request = new CustomRequest(Request.Method.GET, query, responseListener, errorListener, MyApplication.getInstance().getTraktHeaders());
        MyApplication.getInstance().addToRequestQueue(request, requestTag);
    }

}
