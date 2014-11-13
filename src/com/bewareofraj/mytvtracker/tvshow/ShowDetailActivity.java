package com.bewareofraj.mytvtracker.tvshow;

import com.bewareofraj.mytvtracker.R;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

/**
 * An activity representing a single Show detail screen. This activity is only
 * used on handset devices. On tablet-size devices, item details are presented
 * side-by-side with a list of items in a {@link ShowListActivity}.
 * <p>
 * This activity is mostly just a 'shell' activity containing nothing more than
 * a {@link ShowDetailFragment}.
 */
public class ShowDetailActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_detail);
		
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		ShowDetailFragment fragment = new ShowDetailFragment();
		fragment.setArguments(savedInstanceState);
		FragmentManager fm = getFragmentManager();
		fm.beginTransaction().add(R.id.show_detail_container, fragment).commit();
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
			Intent intent = new Intent(this, ShowListActivity.class);
			intent.putExtra(ShowListActivity.EXTRA_SHOW_ID, ShowListActivity.getShowId());
			NavUtils.navigateUpTo(this, intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
