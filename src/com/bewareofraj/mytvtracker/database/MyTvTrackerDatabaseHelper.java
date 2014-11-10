package com.bewareofraj.mytvtracker.database;

import com.bewareofraj.mytvtracker.api.Show;
import com.bewareofraj.mytvtracker.database.MyTvTrackerContract.WatchListEntry;

import android.content.Context;
import android.database.Cursor;
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
			+ WatchListEntry.TABLE_NAME_WATCH_LIST + " ("
			+ WatchListEntry._ID + " INTEGER PRIMARY KEY" + COMMA_SEP
			+ WatchListEntry.COLUMN_NAME_SHOW_NAME + TEXT_TYPE + COMMA_SEP
			+ WatchListEntry.COLUMN_NAME_POSTER_URL_SMALL + TEXT_TYPE + COMMA_SEP
			+ WatchListEntry.COLUMN_NAME_TVDB_ID + TEXT_TYPE + COMMA_SEP
			+ WatchListEntry.COLUMN_NAME_STATUS + TEXT_TYPE + COMMA_SEP
			+ WatchListEntry.COLUMN_NAME_AIR_DAY + TEXT_TYPE + COMMA_SEP
			+ WatchListEntry.COLUMN_NAME_AIR_TIME + TEXT_TYPE 
			+ " )";

	private static final String SQL_DELETE_WATCH_LIST = "DROP TABLE IF EXISTS "
			+ WatchListEntry.TABLE_NAME_WATCH_LIST;
	
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

	public Cursor getWatchList() {
		SQLiteDatabase db = this.getReadableDatabase();

		String[] projection = { WatchListEntry.COLUMN_NAME_STATUS,
				WatchListEntry.COLUMN_NAME_AIR_DAY,
				WatchListEntry.COLUMN_NAME_AIR_TIME,
				WatchListEntry.COLUMN_NAME_TVDB_ID,
				WatchListEntry.COLUMN_NAME_SHOW_NAME,
				WatchListEntry.COLUMN_NAME_POSTER_URL_SMALL };

		String sortOrder = WatchListEntry.COLUMN_NAME_SHOW_NAME + " ASC";

		Cursor cursor = db.query(WatchListEntry.TABLE_NAME_WATCH_LIST, // The table to query
				projection, // The columns to return
				null, // The columns for the WHERE clause
				null, // The values for the WHERE clause
				null, // don't group the rows
				null, // don't filter by row groups
				sortOrder // The sort order
				);

		return cursor;
	}
	
	public void clearWatchList() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL(SQL_DELETE_WATCH_LIST);
	}

	public boolean isOnWatchList(String showId) {
		Cursor cursor = getWatchList();
		while(cursor.moveToNext()) {
			String id = cursor.getString(cursor.getColumnIndex(WatchListEntry.COLUMN_NAME_TVDB_ID));
			if (id.equals(showId)) {
				return true;
			}
		}
		return false;
	}

	public void removeFromWatchList(String[] ids) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(WatchListEntry.TABLE_NAME_WATCH_LIST, WatchListEntry.COLUMN_NAME_TVDB_ID, ids);
	}

	public void addToWatchList(Show show) {
		SQLiteDatabase db = getWritableDatabase();
	}

}
