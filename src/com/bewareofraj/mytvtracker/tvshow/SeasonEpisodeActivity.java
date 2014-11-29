package com.bewareofraj.mytvtracker.tvshow;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

import com.bewareofraj.mytvtracker.R;

public class SeasonEpisodeActivity extends Activity {
	
	public static final String EXTRA_SEASON = "season";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_season_episode);

		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		Intent intent = getIntent();

		SeasonEpisodeFragment fragment = new SeasonEpisodeFragment();
		fragment.setSeason(intent.getIntExtra(EXTRA_SEASON, 1));
		fragment.setArguments(savedInstanceState);
		FragmentManager fm = getFragmentManager();
		fm.beginTransaction().replace(R.id.season_episode_container, fragment)
				.commit();
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
