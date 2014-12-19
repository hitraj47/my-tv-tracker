package com.bewareofraj.mytvtracker;

import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;

import com.bewareofraj.mytvtracker.api.RetrieveTraktJSONTask;
import com.bewareofraj.mytvtracker.api.TraktApiHelper;

public class SplashActivity extends Activity {

	static SharedPreferences mPreferences;

	public static final String PREFS_KEY_CALENDAR_LAST_UPDATED = "last_updated";
	public static final String PREFS_KEY_CALENDAR_JSON = "calendar_json";
	public static final long DAY_IN_MILLIS = 86400000;

	private static long mLastUpdatedPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		mPreferences = this.getSharedPreferences(
				getString(R.string.preferences_file_key), MODE_PRIVATE);
		mLastUpdatedPreferences = mPreferences.getLong(
				PREFS_KEY_CALENDAR_LAST_UPDATED, 0);

		if (mLastUpdatedPreferences == 0 || !isLocalJSONUpdated(this)) {
			getCalendarJSONFromWeb();
		} else {
			Handler handler;
            handler = new Handler();
            handler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this,
                            MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 1000);
		}

	}

	public void getCalendarJSONFromWeb() {
		TraktApiHelper helper = new TraktApiHelper(
				getString(R.string.trakt_api_key));
		try {
			new SplashAsync().execute(helper.getCalendarJSONQuery());
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

	public static void updatePreferences(String json) {
		Editor editor = mPreferences.edit();
		editor.putString(PREFS_KEY_CALENDAR_JSON, json);
		Calendar c = Calendar.getInstance();
		long currentTime = c.getTimeInMillis();
		editor.putLong(PREFS_KEY_CALENDAR_LAST_UPDATED, currentTime);
		editor.apply();
	}

	public static boolean isLocalJSONUpdated(Context context) {
		Calendar c = Calendar.getInstance();
		long currentTime = c.getTimeInMillis();
		mPreferences = context.getSharedPreferences(
				context.getString(R.string.preferences_file_key), MODE_PRIVATE);
		mLastUpdatedPreferences = mPreferences.getLong(
				PREFS_KEY_CALENDAR_LAST_UPDATED, 0);
		return (currentTime - mLastUpdatedPreferences) < DAY_IN_MILLIS;
	}

	public static SharedPreferences getSharedPreferences() {
		return mPreferences;
	}

	private class SplashAsync extends RetrieveTraktJSONTask {

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			updatePreferences(result);
			Intent intent = new Intent(SplashActivity.this, MainActivity.class);
			startActivity(intent);
			finish();
		}
	}

}
