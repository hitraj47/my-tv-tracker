package com.bewareofraj.mytvtracker.watchlist;

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
import com.bewareofraj.mytvtracker.traktapi.Show;
import com.bewareofraj.mytvtracker.database.MyTvTrackerContract.WatchListEntry;
import com.bewareofraj.mytvtracker.database.MyTvTrackerDatabaseHelper;
import com.bewareofraj.mytvtracker.search.SearchFragment;
import com.bewareofraj.mytvtracker.tvshow.ShowListActivity;

import java.util.ArrayList;
import java.util.Locale;
import java.util.TreeMap;

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
					getActivity().getActionBar().setTitle(getString(R.string.search_activity_title));
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
			Show show = new Show();
			// get the info from the row
			show.setTitle(c.getString(c.getColumnIndex(WatchListEntry.COLUMN_NAME_SHOW_NAME)));
			
			String firstLetter = show.getTitle().substring(0, 1).toUpperCase(Locale.ENGLISH);	// used for expandable list headers
			
			show.setPosterUrl(c.getString(c.getColumnIndex(WatchListEntry.COLUMN_NAME_POSTER_URL_SMALL)));
			show.setStatus(c.getString(c.getColumnIndex(WatchListEntry.COLUMN_NAME_STATUS)));
			show.setAirTime(c.getString(c.getColumnIndex(WatchListEntry.COLUMN_NAME_AIR_TIME)));
			show.setAirDay(c.getString(c.getColumnIndex(WatchListEntry.COLUMN_NAME_AIR_DAY)));
			show.setTvdbId(c.getString(c.getColumnIndex(WatchListEntry.COLUMN_NAME_IMDB_ID)));
			show.setFirstAiredTimeStamp(c.getInt(c.getColumnIndex(WatchListEntry.COLUMN_NAME_FIRST_AIRED_TIMESTAMP)));

            //TODO: determine show time
            String showTime = "Show time";
			
			WatchListChild child = createWatchListChild(show.getPosterUrl(), show.getTitle(), showTime, show.getTvdbId());
			
			WatchListGroup watchListGroup;
			
			if (groups.containsKey(firstLetter)) {
				watchListGroup = groups.get(firstLetter);
				
				ArrayList<WatchListChild> watchListItems = watchListGroup
						.getItems();
				watchListItems.add(child);
				
				watchListGroup.setItems(watchListItems);
				groups.put(firstLetter, watchListGroup);
				list.set(list.indexOf(watchListGroup), watchListGroup);
			} else {
				watchListGroup = new WatchListGroup();
				watchListGroup.setName(firstLetter);

				ArrayList<WatchListChild> watchListItems = new ArrayList<WatchListChild>();
				watchListItems.add(child);

				watchListGroup.setItems(watchListItems);

				groups.put(firstLetter, watchListGroup);
				list.add(watchListGroup);
			}
		}
		
		return list;
	}

	private void setupExpandableList() {
		mWatchListAdapter = new WatchListExpandableListAdapter(getActivity(),
				mWatchListItems);
		mExpandableList.setAdapter(mWatchListAdapter);
		mExpandableList.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
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
