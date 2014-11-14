package com.bewareofraj.mytvtracker;

import java.util.Calendar;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;

public class SplashActivity extends Activity {
	
	SharedPreferences mPreferences;
	
	public static final String PREFS_KEY_CALENDAR_LAST_UPDATED = "last_updated";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		mPreferences = this.getSharedPreferences(getString(R.string.preferences_file_key), MODE_PRIVATE);
		long lastUpdatedPreference = mPreferences.getLong(PREFS_KEY_CALENDAR_LAST_UPDATED, 0);
		
		if (lastUpdatedPreference == 0) {
			//TODO: get calendar JSON string, store in string preferences
			
			Editor editor = mPreferences.edit();
			Calendar c = Calendar.getInstance();
			long currentTime = c.getTimeInMillis();
			editor.putLong(PREFS_KEY_CALENDAR_LAST_UPDATED, currentTime);
			
			editor.commit();
		} else {
			//TODO: check if it has been over a day since last updated, if so, pull new JSON
		}
		
	}
}
