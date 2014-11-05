package com.bewareofraj.mytvtracker.watchlist;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;

import android.app.Fragment;
import android.app.FragmentManager;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;

import com.bewareofraj.mytvtracker.MainActivity;
import com.bewareofraj.mytvtracker.R;
import com.bewareofraj.mytvtracker.database.MyTvTrackerDatabaseHelper;
import com.bewareofraj.mytvtracker.search.SearchFragment;

public class WatchListFragment extends Fragment {

	private WatchListExpandableListAdapter mWatchListAdapter;
	private ArrayList<WatchListGroup> mWatchListItems;
	private ExpandableListView mExpandableList;

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

		Cursor c = mDbHelper.getWatchList();

		// TODO: change this back to > 0
		if (c.getCount() == 0) {
			mWatchListEmpty = false;
			View inflatedView = inflater.inflate(R.layout.fragment_watch_list,
					container, false);

			mExpandableList = (ExpandableListView) inflatedView
					.findViewById(R.id.watch_list_expandable);
			mWatchListItems = SetStandardGroups();
			mWatchListAdapter = new WatchListExpandableListAdapter(
					getActivity(), mWatchListItems);
			mExpandableList.setAdapter(mWatchListAdapter);

			return inflatedView;
		} else {
			mWatchListEmpty = true;
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

	// temporary method for testing
	public ArrayList<WatchListGroup> SetStandardGroups() {

		int numShows = 10;

		String groupHeadings[] = new String[numShows];

		String[] showNames = { "The Walking Dead", "The Americans",
				"American Horror Story", "Game of Thrones",
				"The Big Bang Theory", "Grimm", "CSI: Miami", "Modern Family", "Homeland", "24" };
		Arrays.sort(showNames);
		
		String[] showTimes = { "Sunday, 9pm", "Monday 8pm",
				"Sunday, 10pm", "Sunday, 8:30pm",
				"Wednesday, 7pm", "Thursday, 8:30pm", "Show ended", "Monday, 6pm", "Sunday, 9pm", "On break" };

		int images[] = { R.drawable.action_search, R.drawable.action_search,
				R.drawable.action_search, R.drawable.action_search,
				R.drawable.action_search, R.drawable.action_search,
				R.drawable.action_search, R.drawable.action_search,
				R.drawable.action_search, R.drawable.action_search, };

		ArrayList<WatchListGroup> list = new ArrayList<WatchListGroup>();

		HashMap<String, WatchListGroup> groups = new HashMap<String, WatchListGroup>();
		
		for (int i = 0; i < numShows; i++) {
			String firstLetter = showNames[i].substring(0).toUpperCase(Locale.ENGLISH);
			if (groups.containsKey(firstLetter)) {
				
			} else {
				WatchListGroup watchListGroup = new WatchListGroup();
				watchListGroup.setName(firstLetter);
				
				WatchListChild child = new WatchListChild();
				child.setImage(images[i]);
				child.setName(showNames[i]);
				child.setShowTime(showTimes[i]);
				
				ArrayList<WatchListChild> watchListItems = new ArrayList<WatchListChild>();
				watchListItems.add(child);
				
				watchListGroup.setItems(watchListItems);
				
				groups.put(firstLetter, watchListGroup);
			}
		}

		return list;
	}
}
