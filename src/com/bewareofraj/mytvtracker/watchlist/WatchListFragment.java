package com.bewareofraj.mytvtracker.watchlist;

import java.util.ArrayList;

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

			mExpandableList = (ExpandableListView) inflatedView.findViewById(
					R.id.watch_list_expandable);
			mWatchListItems = SetStandardGroups();
			mWatchListAdapter = new WatchListExpandableListAdapter(getActivity(),
					mWatchListItems);
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

	public ArrayList<WatchListGroup> SetStandardGroups() {

		String group_names[] = getActivity().getResources().getStringArray(
				R.array.days);

		String country_names[] = { "Brazil", "Mexico", "Croatia", "Cameroon",
				"Netherlands", "chile", "Spain", "Australia", "Colombia",
				"Greece", "Ivory Coast", "Japan", "Costa Rica", "Uruguay",
				"Italy", "England", "France", "Switzerland", "Ecuador",
				"Honduras", "Agrentina", "Nigeria", "Bosnia and Herzegovina",
				"Iran", "Germany", "United States", "Portugal", "Ghana",
				"Belgium", "Algeria", "Russia", "Korea Republic" };

		int Images[] = { R.drawable.action_search, R.drawable.action_search,
				R.drawable.action_search, R.drawable.action_search,
				R.drawable.action_search, R.drawable.action_search,
				R.drawable.action_search, R.drawable.action_search,
				R.drawable.action_search, R.drawable.action_search,
				R.drawable.action_search, R.drawable.action_search,
				R.drawable.action_search, R.drawable.action_search,
				R.drawable.action_search, R.drawable.action_search,
				R.drawable.action_search, R.drawable.action_search,
				R.drawable.action_search, R.drawable.action_search,
				R.drawable.action_search, R.drawable.action_search,
				R.drawable.action_search, R.drawable.action_search,
				R.drawable.action_search, R.drawable.action_search,
				R.drawable.action_search, R.drawable.action_search,
				R.drawable.action_search, R.drawable.action_search,
				R.drawable.action_search, R.drawable.action_search };

		ArrayList<WatchListGroup> list = new ArrayList<WatchListGroup>();

		ArrayList<WatchListChild> ch_list;

		int size = 4;
		int j = 0;

		for (String group_name : group_names) {
			WatchListGroup gru = new WatchListGroup();
			gru.setName(group_name);

			ch_list = new ArrayList<WatchListChild>();
			for (; j < size; j++) {
				WatchListChild ch = new WatchListChild();
				ch.setName(country_names[j]);
				ch.setImage(Images[j]);
				ch_list.add(ch);
			}
			gru.setItems(ch_list);
			list.add(gru);

			size = size + 4;
		}

		return list;
	}

}
