package com.bewareofraj.mytvtracker.watchlist;

import android.app.Fragment;
import android.app.FragmentManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.bewareofraj.mytvtracker.MainActivity;
import com.bewareofraj.mytvtracker.R;
import com.bewareofraj.mytvtracker.database.MyTvTrackerContract.WatchListEntry;
import com.bewareofraj.mytvtracker.database.MyTvTrackerDatabaseHelper;
import com.bewareofraj.mytvtracker.search.SearchFragment;

public class WatchListFragment extends Fragment {

	/**
	 * Database helper object
	 */
	private MyTvTrackerDatabaseHelper mDbHelper;
	
	/**
	 * Flag to determine if watch list is empty
	 */
	private boolean mWatchListEmpty;

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
			mWatchListEmpty = false;
			return inflater.inflate(R.layout.fragment_watch_list, container, false);
		} else {
			mWatchListEmpty = true;
			View inflatedView = null;
			inflatedView = inflater.inflate(R.layout.fragment_wach_list_empty, container, false);
			Button btnAddShow = (Button) inflatedView.findViewById(R.id.btnAdd);
			btnAddShow.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					getActivity().getActionBar().setTitle("Search");
					MainActivity.mDrawerList.setItemChecked(MainActivity.SCREEN_SEARCH, true);
					SearchFragment fragment = new SearchFragment();
					FragmentManager fm = getFragmentManager();
					fm.beginTransaction().replace(R.id.content_frame, fragment)
							.commit();
				}
			});
			return inflatedView;
		}
	}
}
