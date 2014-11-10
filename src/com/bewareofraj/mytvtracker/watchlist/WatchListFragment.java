package com.bewareofraj.mytvtracker.watchlist;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import com.bewareofraj.mytvtracker.MainActivity;
import com.bewareofraj.mytvtracker.R;
import com.bewareofraj.mytvtracker.database.MyTvTrackerContract.WatchListEntry;
import com.bewareofraj.mytvtracker.database.MyTvTrackerDatabaseHelper;
import com.bewareofraj.mytvtracker.search.SearchFragment;
import com.bewareofraj.mytvtracker.tvshow.ShowListActivity;

public class WatchListFragment extends Fragment {

	private WatchListExpandableListAdapter mWatchListAdapter;
	private ArrayList<WatchListGroup> mWatchListItems;
	private ExpandableListView mExpandableList;

	/**
	 * Database helper object
	 */
	private MyTvTrackerDatabaseHelper mDbHelper;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		mDbHelper = new MyTvTrackerDatabaseHelper(getActivity());

		Cursor c = mDbHelper.getWatchList();

		if (c.getCount() > 0) {
			View inflatedView = inflater.inflate(R.layout.fragment_watch_list,
					container, false);
			mExpandableList = (ExpandableListView) inflatedView
					.findViewById(R.id.watch_list_expandable);
			
			mWatchListItems = createWatchListItems(c);
			
			setupExpandableList();

			return inflatedView;
		} else {
			View inflatedView = null;
			inflatedView = inflater.inflate(R.layout.fragment_wach_list_empty,
					container, false);
			Button btnAddShow = (Button) inflatedView.findViewById(R.id.btnAdd);
			btnAddShow.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					getActivity().getActionBar().setTitle("Search");
					MainActivity.mDrawerList.setItemChecked(
							MainActivity.SCREEN_SEARCH, true);
					SearchFragment fragment = new SearchFragment();
					FragmentManager fm = getFragmentManager();
					fm.beginTransaction().replace(R.id.content_frame, fragment)
							.commit();
				}
			});
			return inflatedView;
		}
	}

	private ArrayList<WatchListGroup> createWatchListItems(Cursor c) {
		ArrayList<WatchListGroup> list = new ArrayList<WatchListGroup>();
		
		TreeMap<String, WatchListGroup> groups = new TreeMap<String, WatchListGroup>();
		
		while(c.moveToNext()) {
			// get the info from the row
			String showName = c.getString(c.getColumnIndex(WatchListEntry.COLUMN_NAME_SHOW_NAME));
			String firstLetter = showName.substring(0, 1).toUpperCase(Locale.ENGLISH);	// used for expandable list headers
			String posterUrl = c.getString(c.getColumnIndex(WatchListEntry.COLUMN_NAME_POSTER_URL_SMALL));
			String status = c.getString(c.getColumnIndex(WatchListEntry.COLUMN_NAME_STATUS));
			String airTime = c.getString(c.getColumnIndex(WatchListEntry.COLUMN_NAME_AIR_TIME));
			String airDay = c.getString(c.getColumnIndex(WatchListEntry.COLUMN_NAME_AIR_DAY));
			String id = c.getString(c.getColumnIndex(WatchListEntry.COLUMN_NAME_TVDB_ID));
			
			String showTime = determineShowTime(airTime, airDay, status);
			
			WatchListChild child = createWatchListChild(posterUrl, showName, showTime, id);
			
			if (groups.containsKey(firstLetter)) {
				WatchListGroup watchListGroup = groups.get(firstLetter);
				
				ArrayList<WatchListChild> watchListItems = watchListGroup
						.getItems();
				watchListItems.add(child);
				
				watchListGroup.setItems(watchListItems);
				groups.put(firstLetter, watchListGroup);
			} else {
				WatchListGroup watchListGroup = new WatchListGroup();
				watchListGroup.setName(firstLetter);

				ArrayList<WatchListChild> watchListItems = new ArrayList<WatchListChild>();
				watchListItems.add(child);

				watchListGroup.setItems(watchListItems);

				groups.put(firstLetter, watchListGroup);
			}
		}
		
		return list;
	}

	private String determineShowTime(String airTime, String airDay,
			String status) {
		String showTime = null;
		if (status.equals("Ended")) {
			showTime = "Show Ended";
		} else {
			showTime = airDay + ", " + airTime;
		}
		return showTime;
	}

	private void setupExpandableList() {
		mWatchListAdapter = new WatchListExpandableListAdapter(getActivity(),
				mWatchListItems);
		mExpandableList.setAdapter(mWatchListAdapter);
		mExpandableList.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				// TODO Auto-generated method stub
				WatchListGroup group = mWatchListItems.get(groupPosition);
				ArrayList<WatchListChild> child = group.getItems();
				WatchListChild show = child.get(childPosition);
				
				Intent intent = new Intent(getActivity(), ShowListActivity.class);
				intent.putExtra(ShowListActivity.EXTRA_SHOW_ID, show.getApiId());
				startActivity(intent);
				
				return false;
			}
		});
	}

	private WatchListChild createWatchListChild(String imageUrl, String name,
			String time, String apiId) {
		WatchListChild child = new WatchListChild();
		child.setImage(imageUrl);
		child.setName(name);
		child.setShowTime(time);
		child.setApiId(apiId);
		return child;
	}
}
