package com.bewareofraj.mytvtracker;

import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;

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
		
		mPreferences = this.getSharedPreferences(getString(R.string.preferences_file_key), MODE_PRIVATE);
		mLastUpdatedPreferences = mPreferences.getLong(PREFS_KEY_CALENDAR_LAST_UPDATED, 0);
		
		if (mLastUpdatedPreferences == 0) {
			updatePreferences(getString(R.string.trakt_api_key));
		} else {
			if (!isLocalJSONUpdated()) {
				updatePreferences(getString(R.string.trakt_api_key));
			}
		}
		
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		
	}

	public static void updatePreferences(String apiKey) {
		TraktApiHelper helper = new TraktApiHelper(apiKey);
		String json = null;
		try {
			json = helper.getCalendarJSON();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Editor editor = mPreferences.edit();
		editor.putString(PREFS_KEY_CALENDAR_JSON, json);
		Calendar c = Calendar.getInstance();
		long currentTime = c.getTimeInMillis();
		editor.putLong(PREFS_KEY_CALENDAR_LAST_UPDATED, currentTime);
		editor.commit();
	}
	
	public static boolean isLocalJSONUpdated() {
		Calendar c = Calendar.getInstance();
		long currentTime = c.getTimeInMillis();
		return (currentTime - mLastUpdatedPreferences) < DAY_IN_MILLIS;
	}
	
	public static SharedPreferences getSharedPreferences() {
		return mPreferences;
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus) {
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
		}
	}
	
}
