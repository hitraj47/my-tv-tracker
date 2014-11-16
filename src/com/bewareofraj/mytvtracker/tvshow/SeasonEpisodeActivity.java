package com.bewareofraj.mytvtracker.tvshow;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;

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
		fm.beginTransaction().add(R.id.show_detail_container, fragment)
				.commit();
	}
}
