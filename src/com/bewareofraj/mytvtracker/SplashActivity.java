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
	
	SharedPreferences mPreferences;
	
	public static final String PREFS_KEY_CALENDAR_LAST_UPDATED = "last_updated";
	public static final String PREFS_KEY_CALENDAR_JSON = "calendar_json";
	public static final long DAY_IN_MILLIS = 86400000;
	
	private static boolean mJSONUpToDate = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		mPreferences = this.getSharedPreferences(getString(R.string.preferences_file_key), MODE_PRIVATE);
		long lastUpdatedPreference = mPreferences.getLong(PREFS_KEY_CALENDAR_LAST_UPDATED, 0);
		
		if (lastUpdatedPreference == 0) {
			updatePreferences();
		} else {
			Calendar c = Calendar.getInstance();
			long currentTime = c.getTimeInMillis();
			if ( (currentTime - lastUpdatedPreference) > DAY_IN_MILLIS) {
				updatePreferences();
			} else {
				mJSONUpToDate = true;
			}
		}
		
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		
	}

	private void updatePreferences() {
		TraktApiHelper helper = new TraktApiHelper(getResources().getString(R.string.trakt_api_key));
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
		mJSONUpToDate = true;
	}
	
	public static boolean isJSONUpToDate() {
		return mJSONUpToDate;
	}
	
}
