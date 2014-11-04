package com.bewareofraj.mytvtracker.watchlist;

import android.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bewareofraj.mytvtracker.R;
import com.bewareofraj.mytvtracker.database.MyTvTrackerContract.WatchListEntry;
import com.bewareofraj.mytvtracker.database.MyTvTrackerDatabaseHelper;

public class WatchListFragment extends Fragment {

	private MyTvTrackerDatabaseHelper mDbHelper;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		mDbHelper = new MyTvTrackerDatabaseHelper(getActivity());

		SQLiteDatabase db = mDbHelper.getReadableDatabase();

		String[] projection = { WatchListEntry.COLUMN_NAME_CURRENTLY_AIRING,
				WatchListEntry.COLUMN_NAME_ENTRY_ID,
				WatchListEntry.COLUMN_NAME_NEXT_EPISODE_DAY,
				WatchListEntry.COLUMN_NAME_NEXT_EPISODE_TIME,
				WatchListEntry.COLUMN_NAME_TVDB_ID, };

		String sortOrder = WatchListEntry.COLUMN_NAME_NEXT_EPISODE_DAY
				+ " DESC";

		Cursor c = db.query(WatchListEntry.TABLE_NAME, // The table to query
				projection, // The columns to return
				null, // The columns for the WHERE clause
				null, // The values for the WHERE clause
				null, // don't group the rows
				null, // don't filter by row groups
				sortOrder // The sort order
				);
		
		if (c.getCount() > 0) {
			return inflater.inflate(R.layout.fragment_watch_list, container, false);
		} else {
			return inflater.inflate(R.layout.fragment_wach_list_empty, container, false);
		}
	}
}
