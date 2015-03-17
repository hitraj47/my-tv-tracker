package com.bewareofraj.mytvtracker.database;

import android.provider.BaseColumns;

public final class MyTvTrackerContract {
	
	// To prevent instantiation of the class
	public MyTvTrackerContract() {}
	
	/**
	 * Inner class defining table for Watch List
	 */
	public static abstract class WatchListEntry implements BaseColumns {
		public static final String TABLE_NAME_WATCH_LIST = "watch_list_entry";
		public static final String COLUMN_NAME_IMDB_ID = "imdb_id";
		public static final String COLUMN_NAME_POSTER_URL_SMALL = "poster_url_small";
		public static final String COLUMN_NAME_STATUS = "status";
		public static final String COLUMN_NAME_AIR_DAY = "air_day";
		public static final String COLUMN_NAME_AIR_TIME = "air_time";
		public static final String COLUMN_NAME_SHOW_NAME = "show_name";
		public static final String COLUMN_NAME_FIRST_AIRED_TIMESTAMP = "first_aired_timestamp";
	}
	
}
