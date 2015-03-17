package com.bewareofraj.mytvtracker.tvshow;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.bewareofraj.mytvtracker.R;
import com.bewareofraj.mytvtracker.database.MyTvTrackerDatabaseHelper;
import com.bewareofraj.mytvtracker.traktapi.Show;
import com.bewareofraj.mytvtracker.traktapi.TraktApiHelper;
import com.bewareofraj.mytvtracker.util.CustomRequest;
import com.bewareofraj.mytvtracker.util.MyApplication;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * A fragment representing a single Show detail screen. This fragment is either
 * contained in a {@link ShowListActivity} in two-pane mode (on tablets) or a
 * {@link ShowDetailActivity} on handsets.
 */
public class ShowDetailFragment extends Fragment {
	
	private boolean mIsOnWatchList;
    private Show mShow;

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
		final View rootView = inflater.inflate(R.layout.fragment_show_detail,
				container, false);
        
        if ( (savedInstanceState != null) && savedInstanceState.getSerializable("show") != null) {
            mShow = (Show) savedInstanceState.getSerializable("show");
            populateUi(rootView);
        } else {
            final String requestTag = "get_show";
            String query = TraktApiHelper.getShowQuery(ShowListActivity.getShowId());

            final ProgressDialog dialog = new ProgressDialog(getActivity());
            dialog.setMessage(getString(R.string.show_retrieving));
            dialog.setCancelable(false);
            dialog.show();

            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    VolleyLog.d(requestTag, "Error: " + volleyError.getMessage());
                    dialog.dismiss();
                }
            };

            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String stringResponse) {
                    try {
                        mShow = TraktApiHelper.getShowFromResult(stringResponse);
                        populateUi(rootView);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        dialog.dismiss();
                    }
                }
            };

            CustomRequest request = new CustomRequest(Request.Method.GET, query, responseListener, errorListener, MyApplication.getInstance().getTraktHeaders());
            MyApplication.getInstance().addToRequestQueue(request, requestTag);
        }

        // set action bar title
		getActivity().setTitle("Summary");
		
		return rootView;
	}

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("show", mShow);
    }

    private void populateUi(View rootView) {
        ImageLoader imageLoader = MyApplication.getInstance().getImageLoader();
        NetworkImageView imgPoster = (NetworkImageView) rootView.findViewById(R.id.imgPoster);
        imgPoster.setImageUrl(mShow.getPosterUrl(), imageLoader);

        TextView lblShowName = (TextView) rootView.findViewById(R.id.lblShowName);
        lblShowName.setText(mShow.getTitle());

        TextView lblShowYear = (TextView) rootView.findViewById(R.id.lblShowYear);
        String year = (mShow.getYear() == 0) ? getString(R.string.show_year_unknown) : Integer.toString(mShow.getYear());
        lblShowYear.setText(year);

        TextView lblShowTime = (TextView) rootView.findViewById(R.id.lblShowTime);
        if (mShow.isOnAir() == null) {
            createShowTimeLabel(lblShowTime);
        } else {
            setShowTimeLabel(lblShowTime);
        }

        TextView lblShowNetwork = (TextView) rootView.findViewById(R.id.lblShowNetwork);
        lblShowNetwork.setText(mShow.getNetwork());

        TextView lblShowCountry = (TextView) rootView.findViewById(R.id.lblShowCountry);
        lblShowCountry.setText(mShow.getCountry());

        TextView lblFirstAired = (TextView) rootView.findViewById(R.id.lblFirstAired);
        DateTimeFormatter dtf = DateTimeFormat.forPattern("MMMM d, yyyy");
        String firstAired = "First aired: ";
        firstAired = firstAired + mShow.getFirstAired().toString(dtf);
        lblFirstAired.setText(firstAired);

        final Button btnWatchList = (Button) rootView.findViewById(R.id.btnWatchList);
        mIsOnWatchList = isOnWatchList(mShow.getImdbId());
        String buttonText = mIsOnWatchList ? getString(R.string.watchlist_remove) : getString(R.string.watchlist_add);
        btnWatchList.setText(buttonText);
        btnWatchList.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                MyTvTrackerDatabaseHelper db = new MyTvTrackerDatabaseHelper(getActivity());
                if (mIsOnWatchList) {
                    String[] ids = { mShow.getImdbId() };
                    db.removeFromWatchList(ids);
                    Toast.makeText(getActivity(), getString(R.string.watchlist_removed), Toast.LENGTH_LONG).show();
                    btnWatchList.setText(getString(R.string.watchlist_add));
                    mIsOnWatchList = false;
                } else {
                    db.addToWatchList(mShow);
                    Toast.makeText(getActivity(), getString(R.string.watchlist_added), Toast.LENGTH_LONG).show();
                    btnWatchList.setText(getString(R.string.watchlist_remove));
                    mIsOnWatchList = true;
                }
            }
        });

        TextView lblOverviewBody = (TextView) rootView.findViewById(R.id.lblOverviewBody);
        lblOverviewBody.setText(mShow.getOverview());
    }

    private void setShowTimeLabel(TextView lblShowTime) {
        String showTimeString = mShow.makeShowTimeString(getActivity());
        lblShowTime.setText(showTimeString);
    }

    private void createShowTimeLabel(final TextView lblShowTime) {
        final ArrayList<String> ids = new ArrayList<>();
        int numDays = 7;
        final String requestTag = "calendar_query";
        String query = TraktApiHelper.getShowCalendar(numDays);

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
                    mShow.setOnAir(TraktApiHelper.isOnAir(resultString, mShow.getImdbId()));
                    setShowTimeLabel(lblShowTime);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        CustomRequest request = new CustomRequest(Request.Method.GET, query, responseListener, errorListener, MyApplication.getInstance().getTraktHeaders());
        MyApplication.getInstance().addToRequestQueue(request, requestTag);
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
}
