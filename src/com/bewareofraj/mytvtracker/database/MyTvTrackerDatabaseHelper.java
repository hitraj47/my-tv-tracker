package com.bewareofraj.mytvtracker.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyTvTrackerDatabaseHelper extends SQLiteOpenHelper {

	// If you change the database schema, you must increment the database
	// version.
	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "MyTvTracker.db";

	private static final String TEXT_TYPE = " TEXT";
	private static final String COMMA_SEP = ",";
	private static final String SQL_CREATE_WATCH_LIST = "CREATE TABLE "
			+ MyTvTrackerContract.WatchListEntry.TABLE_NAME + " ("
			+ MyTvTrackerContract.WatchListEntry._ID + " INTEGER PRIMARY KEY,"
			+ MyTvTrackerContract.WatchListEntry.COLUMN_NAME_ENTRY_ID
			+ TEXT_TYPE + COMMA_SEP
			+ MyTvTrackerContract.WatchListEntry.COLUMN_NAME_TVDB_ID
			+ TEXT_TYPE + COMMA_SEP
			+ MyTvTrackerContract.WatchListEntry.COLUMN_NAME_CURRENTLY_AIRING
			+ TEXT_TYPE + COMMA_SEP
			+ MyTvTrackerContract.WatchListEntry.COLUMN_NAME_NEXT_EPISODE_DAY
			+ TEXT_TYPE + COMMA_SEP
			+ MyTvTrackerContract.WatchListEntry.COLUMN_NAME_NEXT_EPISODE_TIME
			+ TEXT_TYPE + " )";

	private static final String SQL_DELETE_WATCH_LIST = "DROP TABLE IF EXISTS "
			+ MyTvTrackerContract.WatchListEntry.TABLE_NAME;

	public MyTvTrackerDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_WATCH_LIST);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
	}

}