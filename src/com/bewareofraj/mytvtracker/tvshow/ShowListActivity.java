package com.bewareofraj.mytvtracker.tvshow;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import com.bewareofraj.mytvtracker.R;
import com.bewareofraj.mytvtracker.TestItemContainerActivity;

/**
 * An activity representing a list of Shows. This activity has different
 * presentations for handset and tablet-size devices. On handsets, the activity
 * presents a list of items, which when touched, lead to a
 * {@link ShowDetailActivity} representing item details. On tablets, the
 * activity presents the list of items and item details side-by-side using two
 * vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link ShowListFragment} and the item details (if present) is a
 * {@link ShowDetailFragment}.
 * <p>
 * This activity also implements the required {@link ShowListFragment.Callbacks}
 * interface to listen for item selections.
 */
public class ShowListActivity extends Activity implements
		ShowListFragment.Callbacks {
	
	/**
	 * The id to pass to the API. Right now this is the TVDB ID
	 */
	public static final String EXTRA_SHOW_ID = "show_id";

	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	private boolean mTwoPane;
	
	/**
	 * The Show ID, used to pass to the API to retrieve information
	 */
	private static String mShowId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_list);
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);

		if (findViewById(R.id.show_detail_container) != null) {
			// The detail container view will be present only in the
			// large-screen layouts (res/values-large and
			// res/values-sw600dp). If this view is present, then the
			// activity should be in two-pane mode.
			mTwoPane = true;

			// In two-pane mode, list items should be given the
			// 'activated' state when touched.
			((ShowListFragment) getFragmentManager().findFragmentById(
					R.id.show_list)).setActivateOnItemClick(true);
		}
		
		// Show TV show information as default/first screen
		Intent intent = getIntent();
		mShowId = intent.getStringExtra(EXTRA_SHOW_ID);
		
	}
	
	public static String getShowId() {
		return mShowId;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Callback method from {@link ShowListFragment.Callbacks} indicating that
	 * the item with the given ID was selected.
	 */
	@Override
	public void onItemSelected(String id) {
		if (mTwoPane) {
			// In two-pane mode, show the detail view in this activity by
			// adding or replacing the detail fragment using a
			// fragment transaction.
			Bundle arguments = new Bundle();
			arguments.putString(ShowDetailFragment.ARG_ITEM_ID, id);
			ShowDetailFragment fragment = new ShowDetailFragment();
			fragment.setArguments(arguments);
			getFragmentManager().beginTransaction()
					.replace(R.id.show_detail_container, fragment).commit();

		} else {
			// In single-pane mode, simply start the detail activity
			// for the selected item ID.
			Intent detailIntent = new Intent(this, ShowDetailActivity.class);
			detailIntent.putExtra(ShowDetailFragment.ARG_ITEM_ID, id);
			startActivity(detailIntent);
		}
	}
}
