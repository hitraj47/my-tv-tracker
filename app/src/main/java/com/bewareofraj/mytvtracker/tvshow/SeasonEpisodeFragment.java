package com.bewareofraj.mytvtracker.tvshow;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.bewareofraj.mytvtracker.R;
import com.bewareofraj.mytvtracker.api.Episode;
import com.bewareofraj.mytvtracker.api.TraktApiHelper;
import com.bewareofraj.mytvtracker.images.ImageLoader;
import com.bewareofraj.mytvtracker.util.VolleyController;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SeasonEpisodeFragment extends Fragment {
	
	private int mSeason;
	
	private Spinner mSpnEpisodes;
	private TextView mLblEpisodeTitle;
	private ImageView mImgEpisodeImage;
	private TextView mLblFirstAired;
	private TextView mLblOverview;
	
	private ArrayList<Episode> mEpisodes;
	
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
        
		final String requestTag = "get_episodes";
        String query = TraktApiHelper.getEpisodesQuery(ShowListActivity.getShowId(), Integer.toString(mSeason), getString(R.string.trakt_api_key), requestTag);
        
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                VolleyLog.d(requestTag, "Error: " + volleyError.getMessage());
            }
        };
        
        Response.Listener<JSONArray> responseListener = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray result) {
                try {
                    mEpisodes = TraktApiHelper.getEpisodesResult(result, Integer.toString(mSeason));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(query, responseListener, errorListener);
        VolleyController.getInstance().addToRequestQueue(jsonArrayRequest, requestTag);

		// set action bar title
		getActivity().setTitle("Season " + mSeason);
		
		// episode list spinner
		mSpnEpisodes = (Spinner) rootView.findViewById(R.id.spnEpisode);
		String[] spinnerItems = new String[mEpisodes.size()];
		for (int i = 0; i < mEpisodes.size(); i++) {
			spinnerItems[i] = "Episode " + mEpisodes.get(i).getEpisodeNumber();
		}
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, spinnerItems);
		spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpnEpisodes.setAdapter(spinnerArrayAdapter);
		mSpnEpisodes.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				displayEpisode(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		
		// init other gui elements
		mLblEpisodeTitle = (TextView) rootView.findViewById(R.id.lblEpisodeTitle);
		mImgEpisodeImage = (ImageView) rootView.findViewById(R.id.imgEpisodeImage);
		mLblFirstAired = (TextView) rootView.findViewById(R.id.lblEpisodeFirstAired);
		mLblOverview = (TextView) rootView.findViewById(R.id.lblEpisodeOverview);
		
		// display the first episode initially
		displayEpisode(0);
		
		return rootView;
		
	}

	protected void displayEpisode(int position) {
		Episode episode = mEpisodes.get(position);
		
		mLblEpisodeTitle.setText("Episode " + episode.getEpisodeNumber() + ": " + episode.getTitle());
		
		//TODO: set appropriate image size instead of 900
		ImageLoader imgLoader = new ImageLoader(getActivity(), 900);
		int loadingImage = R.drawable.ic_launcher;	// loading image, use logo temporarily for now
		imgLoader.DisplayImage(episode.getImageUrl(), loadingImage, mImgEpisodeImage);
		
		mLblFirstAired.setText("First aired: " + makeFirstAiredDate(episode.getFirstAired()));
		
		mLblOverview.setText(episode.getOverview());
		
	}
	
	private String makeFirstAiredDate(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		String month = c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH);
		String dayOfMonth = Integer.toString(c.get(Calendar.DAY_OF_MONTH));
		String year = Integer.toString(c.get(Calendar.YEAR));
		return month + " " + dayOfMonth + ", " + year;
	}

	public int getSeason() {
		return mSeason;
	}

	public void setSeason(int mSeason) {
		this.mSeason = mSeason;
	}

}
