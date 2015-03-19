package com.bewareofraj.mytvtracker.tvshow;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.bewareofraj.mytvtracker.R;
import com.bewareofraj.mytvtracker.traktapi.Episode;
import com.bewareofraj.mytvtracker.traktapi.TraktApiHelper;
import com.bewareofraj.mytvtracker.util.CustomRequest;
import com.bewareofraj.mytvtracker.util.MyApplication;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONException;

import java.util.ArrayList;

public class SeasonEpisodeFragment extends Fragment {
	
	private int mSeason;
	
	private Spinner mSpnEpisodes;
	private TextView mLblEpisodeTitle;
	private TextView mLblFirstAired;
	private TextView mLblOverview;
	
	private ArrayList<Episode> mEpisodes;
    private int mEpisodeNumber = -1;

    private static final String BUNDLE_EPISODES_ARRAY = "episodes";
    private static final String BUNDLE_EPISODE_NUMBER = "episode_number";
	
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
		final View rootView = inflater.inflate(R.layout.fragment_season_episode,
				container, false);

        if ( (savedInstanceState != null) && (savedInstanceState.getSerializable(BUNDLE_EPISODES_ARRAY)) != null) {
            mEpisodes = (ArrayList<Episode>) savedInstanceState.getSerializable(BUNDLE_EPISODES_ARRAY);
            mEpisodeNumber = savedInstanceState.getInt(BUNDLE_EPISODE_NUMBER);
            populateUi(rootView);
        } else {
            final String requestTag = "get_episodes";
            String query = TraktApiHelper.getEpisodesForSeason(ShowListActivity.getShowId(), mSeason);

            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    VolleyLog.d(requestTag, "Error: " + volleyError.getMessage());
                }
            };

            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String resultString) {
                    try {
                        mEpisodes = TraktApiHelper.getEpisodesFromResult(resultString);
                        mEpisodeNumber = 0; // display the first episode initially
                        populateUi(rootView);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };

            CustomRequest request = new CustomRequest(Request.Method.GET, query, responseListener, errorListener, MyApplication.getInstance().getTraktHeaders());
            MyApplication.getInstance().addToRequestQueue(request, requestTag);
        }

		// set action bar title
		getActivity().setTitle("Season " + mSeason);

		return rootView;
		
	}

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(BUNDLE_EPISODES_ARRAY, mEpisodes);
        outState.putInt(BUNDLE_EPISODE_NUMBER, mEpisodeNumber);
    }

    private void populateUi(final View rootView) {
        // episode list spinner
        mSpnEpisodes = (Spinner) rootView.findViewById(R.id.spnEpisode);
        String[] spinnerItems = new String[mEpisodes.size()];
        for (int i = 0; i < mEpisodes.size(); i++) {
            spinnerItems[i] = "Episode " + mEpisodes.get(i).getEpisodeNumber();
        }
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, spinnerItems);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpnEpisodes.setAdapter(spinnerArrayAdapter);
        mSpnEpisodes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                mEpisodeNumber = position;
                displayEpisode(mEpisodeNumber, rootView);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // init other gui elements
        mLblEpisodeTitle = (TextView) rootView.findViewById(R.id.lblEpisodeTitle);
        mLblFirstAired = (TextView) rootView.findViewById(R.id.lblEpisodeFirstAired);
        mLblOverview = (TextView) rootView.findViewById(R.id.lblEpisodeOverview);

        displayEpisode(mEpisodeNumber, rootView);
    }

    protected void displayEpisode(int position, View rootView) {
		Episode episode = mEpisodes.get(position);
		
		mLblEpisodeTitle.setText("Episode " + episode.getEpisodeNumber() + ": " + episode.getTitle());
		
		ImageLoader imageLoader = MyApplication.getInstance().getImageLoader();
        NetworkImageView imgPoster = (NetworkImageView) rootView.findViewById(R.id.imgEpisodeImage);
        imgPoster.setImageUrl(episode.getImageUrl(), imageLoader);

        DateTimeFormatter dtf = DateTimeFormat.forPattern("MMMM d, yyyy");
		mLblFirstAired.setText("First aired: " + episode.getFirstAired().toString(dtf));
		
		mLblOverview.setText(episode.getOverview());
		
	}

	public void setSeason(int mSeason) {
		this.mSeason = mSeason;
	}

}
