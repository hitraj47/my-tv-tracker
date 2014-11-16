package com.bewareofraj.mytvtracker.tvshow;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bewareofraj.mytvtracker.R;
import com.bewareofraj.mytvtracker.api.Show;
import com.bewareofraj.mytvtracker.api.TraktApiHelper;
import com.bewareofraj.mytvtracker.database.MyTvTrackerDatabaseHelper;
import com.bewareofraj.mytvtracker.images.ImageLoader;

/**
 * A fragment representing a single Show detail screen. This fragment is either
 * contained in a {@link ShowListActivity} in two-pane mode (on tablets) or a
 * {@link ShowDetailActivity} on handsets.
 */
public class ShowDetailFragment extends Fragment {
	
	private boolean mIsOnWatchList;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public ShowDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_show_detail,
				container, false);

		TraktApiHelper helper = new TraktApiHelper(getResources().getString(R.string.trakt_api_key));
		final Show show;
		try {
			show = helper.getShow(ShowListActivity.getShowId());
			
			ImageView imgPoster = (ImageView) rootView.findViewById(R.id.imgPoster);
			ImageLoader imgLoader = new ImageLoader(getActivity(), determineBestImageWidth());
			int loadingImage = R.drawable.ic_launcher;	// loading image, use logo temporarily for now
			imgLoader.DisplayImage(show.getSizedPosterUrl(TraktApiHelper.API_POSTER_SIZE_MEDIUM), loadingImage, imgPoster);
			
			TextView lblShowName = (TextView) rootView.findViewById(R.id.lblShowName);
			lblShowName.setText(show.getTitle());
			
			TextView lblShowYear = (TextView) rootView.findViewById(R.id.lblShowYear);
			String year = (show.getYear() == 0) ? "TBD" : Integer.toString(show.getYear());
			lblShowYear.setText(year);
			
			TextView lblShowTime = (TextView) rootView.findViewById(R.id.lblShowTime);
			lblShowTime.setText(show.determineShowTime(getResources().getString(R.string.trakt_api_key)));
			
			TextView lblShowNetwork = (TextView) rootView.findViewById(R.id.lblShowNetwork);
			lblShowNetwork.setText(show.getNetwork());
			
			TextView lblShowCountry = (TextView) rootView.findViewById(R.id.lblShowCountry);
			lblShowCountry.setText(show.getCountry());
			
			TextView lblFirstAired = (TextView) rootView.findViewById(R.id.lblFirstAired);
			String firstAired;
			if (show.getFirstAiredTimeStamp() == 0) {
				firstAired = "First aired: TBD";
			} else {
				firstAired = "First aired: " + makeFirstAiredDate(show.getFirstAired());
			}
			lblFirstAired.setText(firstAired);
			
			final Button btnWatchList = (Button) rootView.findViewById(R.id.btnWatchList);
			mIsOnWatchList = isOnWatchList(ShowListActivity.getShowId());
			String buttonText = mIsOnWatchList ? "Remove from Watch List" : "Add to Watch List";
			btnWatchList.setText(buttonText);
			btnWatchList.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					MyTvTrackerDatabaseHelper db = new MyTvTrackerDatabaseHelper(getActivity());
					if (mIsOnWatchList) {
						String[] ids = { show.getTvdbId() };
						db.removeFromWatchList(ids);
						Toast.makeText(getActivity(), "Show removed from Watch List", Toast.LENGTH_LONG).show();
						btnWatchList.setText("Add to Watch List");
					} else {
						db.addToWatchList(show);
						Toast.makeText(getActivity(), "Show added to Watch List", Toast.LENGTH_LONG).show();
						btnWatchList.setText("Remove from Watch List");
					}
				}
			});
			
			TextView lblOverviewBody = (TextView) rootView.findViewById(R.id.lblOverviewBody);
			lblOverviewBody.setText(show.getOverview());
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		// set action bar title
		getActivity().setTitle("Summary");
		
		return rootView;
	}

	@SuppressWarnings("deprecation")
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private int determineBestImageWidth() {
		Display display = getActivity().getWindowManager().getDefaultDisplay();
		Point size = new Point();
		int width;
		if (android.os.Build.VERSION.SDK_INT >= 13) {
			display.getSize(size);
			width = size.x;
		} else {
			width = display.getWidth();
		}
		
		if (width > 500) {
			int posterSize = Integer.parseInt(TraktApiHelper.API_POSTER_SIZE_MEDIUM.substring(TraktApiHelper.API_POSTER_SIZE_MEDIUM.lastIndexOf('-') + 1));
			return posterSize;
		} else if (width > 300) {
			return 100;
		} else {
			return 60;
		}
	}

	private boolean isOnWatchList(String showId) {
		MyTvTrackerDatabaseHelper db = new MyTvTrackerDatabaseHelper(getActivity());
		return db.isOnWatchList(showId);
	}

	private String makeFirstAiredDate(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		String month = c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH);
		String dayOfMonth = Integer.toString(c.get(Calendar.DAY_OF_MONTH));
		String year = Integer.toString(c.get(Calendar.YEAR));
		return month + " " + dayOfMonth + ", " + year;
	}
}
