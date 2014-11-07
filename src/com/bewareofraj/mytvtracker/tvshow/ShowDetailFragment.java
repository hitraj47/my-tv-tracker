package com.bewareofraj.mytvtracker.tvshow;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bewareofraj.mytvtracker.R;
import com.bewareofraj.mytvtracker.api.Show;
import com.bewareofraj.mytvtracker.api.TraktApiHelper;

/**
 * A fragment representing a single Show detail screen. This fragment is either
 * contained in a {@link ShowListActivity} in two-pane mode (on tablets) or a
 * {@link ShowDetailActivity} on handsets.
 */
public class ShowDetailFragment extends Fragment {

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

		// TODO: Get data from web using show id, then use the Show object to
		// display info in layout components
		TraktApiHelper helper = new TraktApiHelper(getResources().getString(R.string.trakt_api_key));
		Show show;
		try {
			show = helper.getShow(ShowListActivity.getShowId());
			
			TextView lblShowName = (TextView) rootView.findViewById(R.id.lblShowName);
			lblShowName.setText(show.getTitle());
			
			TextView lblShowYear = (TextView) rootView.findViewById(R.id.lblShowYear);
			lblShowYear.setText(show.getYear());
			
			TextView lblShowTime = (TextView) rootView.findViewById(R.id.lblShowTime);
			lblShowTime.setText(show.getAirDay() + ", " + show.getAirTime());
			
			TextView lblFirstAired = (TextView) rootView.findViewById(R.id.lblFirstAired);
			lblFirstAired.setText("First aired: " + makeFirstAiredDated(show.getFirstAired()));
			
			Button btnWatchList = (Button) rootView.findViewById(R.id.btnWatchList);
			String buttonText = isOnWatchList(ShowListActivity.getShowId()) ? "Remove from Watch List" : "Add to Watch List";
			btnWatchList.setText(buttonText);
			//TODO: Add button listener
			
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
		
		return rootView;
	}

	private boolean isOnWatchList(String showId) {
		// TODO Auto-generated method stub
		return false;
	}

	private String makeFirstAiredDated(Date date) {
		// TODO Auto-generated method stub
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		String month = c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH);
		String dayOfMonth = c.getDisplayName(Calendar.DATE, Calendar.LONG, Locale.ENGLISH);
		String year = c.getDisplayName(Calendar.YEAR, Calendar.LONG, Locale.ENGLISH);
		return month + " " + dayOfMonth + ", " + year;
	}
}
