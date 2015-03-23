package com.bewareofraj.mytvtracker.tvshow;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.bewareofraj.mytvtracker.R;
import com.bewareofraj.mytvtracker.SplashActivity;
import com.bewareofraj.mytvtracker.traktapi.TraktApiHelper;
import com.bewareofraj.mytvtracker.util.CustomRequest;
import com.bewareofraj.mytvtracker.util.MyApplication;

import org.json.JSONException;

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
	private static boolean mTwoPane;

	/**
	 * The Show ID, used to pass to the API to retrieve information
	 */
	private static String mShowId;

	/**
	 * The number of seasons + 1 will be the size of the list
	 */
	private static int mNumberOfSeasons;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Show TV show information as default/first screen
		Intent intent = getIntent();
		mShowId = intent.getStringExtra(EXTRA_SHOW_ID);

        // Check if show calendar needs to be updated
        if (!MyApplication.getInstance().isShowCalendarUpdated()) {
            Intent splashIntent = new Intent(ShowListActivity.this, SplashActivity.class);
            splashIntent.putExtra(SplashActivity.EXTRA_LAUNCH_ACTIVITY, SplashActivity.EXTRA_ACTIVITY_SHOW_DETAIL);
            splashIntent.putExtra(SplashActivity.EXTRA_SHOW_ID, mShowId);
            startActivity(splashIntent);
            finish();
        } else {
            final String tag = "get_seasons";
            String query = TraktApiHelper.getNumberOfSeasonsQuery(mShowId);

            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    VolleyLog.d(tag, "Error: " + volleyError.getMessage());
                }
            };

            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String stringResponse) {
                    try {
                        mNumberOfSeasons = TraktApiHelper.getNumberOfSeasonsFromResult(stringResponse);
                        displayListLayout();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };

            CustomRequest request = new CustomRequest(Request.Method.GET, query, responseListener, errorListener, MyApplication.getInstance().getTraktHeaders());
            MyApplication.getInstance().addToRequestQueue(request, tag);

            // Show the Up button in the action bar.
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
	}

    private void displayListLayout() {
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
        setContentView(R.layout.activity_show_list);
    }

    public static String getShowId() {
		return mShowId;
	}

	public static int getNumberOfSeasons() {
		return mNumberOfSeasons;
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
			ShowDetailFragment fragment = new ShowDetailFragment();
			getFragmentManager().beginTransaction()
					.replace(R.id.show_detail_container, fragment).commit();

		} else {
			// In single-pane mode, simply start the detail activity
			// for the selected item ID.
			Intent detailIntent = new Intent(this, ShowDetailActivity.class);
			detailIntent.putExtra(EXTRA_SHOW_ID, mShowId);
			startActivity(detailIntent);
		}
	}
	
	public static boolean isTwoPane() {
		return mTwoPane;
	}
}
