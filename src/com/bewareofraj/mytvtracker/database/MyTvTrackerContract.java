package com.bewareofraj.mytvtracker.database;

import android.provider.BaseColumns;

public final class MyTvTrackerContract {
	
	// To prevent instantiation of the class
	public MyTvTrackerContract() {}
	
	/**
	 * Inner class defining table for Watch List
	 */
	public static abstract class WatchListEntry implements BaseColumns {
		public static final String TABLE_NAME = "watch_list_entry";
		public static final String COLUMN_NAME_ENTRY_ID = "entry_id";
		public static final String COLUMN_NAME_TVDB_ID = "tvdb_id";
		public static final String COLUMN_NAME_CURRENTLY_AIRING = "currently_airing";
		public static final String COLUMN_NAME_NEXT_EPISODE_DAY = "next_episode_day";
		public static final String COLUMN_NAME_NEXT_EPISODE_TIME = "next_episode_time";
	}
	
}