package com.bewareofraj.mytvtracker.tvshow;

import com.bewareofraj.mytvtracker.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SeasonEpisodeFragment extends Fragment {
	
	private int mSeason;
	private int mEpisode = 1;
	
	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public SeasonEpisodeFragment() {
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_season_episode,
				container, false);
		return rootView;
		
	}

	public int getSeason() {
		return mSeason;
	}

	public void setSeason(int mSeason) {
		this.mSeason = mSeason;
	}

	public int getEpisode() {
		return mEpisode;
	}

	public void setEpisode(int mEpisode) {
		this.mEpisode = mEpisode;
	}

}
