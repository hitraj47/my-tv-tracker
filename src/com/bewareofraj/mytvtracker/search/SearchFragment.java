package com.bewareofraj.mytvtracker.search;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.bewareofraj.mytvtracker.R;
import com.bewareofraj.mytvtracker.api.Show;
import com.bewareofraj.mytvtracker.api.TraktApiHelper;
import com.bewareofraj.mytvtracker.tvshow.ShowListActivity;

public class SearchFragment extends Fragment {

	private List<SearchResultItem> mResultList = new ArrayList<SearchResultItem>();
	private ListView mListView;
	private SearchResultListAdapter mAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View inflatedView = inflater.inflate(R.layout.fragment_search,
				container, false);

		// init ui components for searching
		initSearchUi(inflatedView);

		// results list view
		mListView = (ListView) inflatedView
				.findViewById(R.id.search_results_list);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent showIntent = new Intent(getActivity(),
						ShowListActivity.class);
				showIntent.putExtra(ShowListActivity.EXTRA_SHOW_ID, mResultList
						.get(position).getId());
				getActivity().startActivity(showIntent);

			}

		});

		return inflatedView;
	}

	private void initSearchUi(View inflatedView) {
		final EditText txtSearch = (EditText) inflatedView
				.findViewById(R.id.txtSearch);
		Button btnSearch = (Button) inflatedView.findViewById(R.id.btnSearch);
		btnSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String searchTerms = txtSearch.getText().toString();
				if (searchTerms.equals("")) {
					Toast.makeText(getActivity(),
							"Please enter something to search for...",
							Toast.LENGTH_LONG).show();
				} else {
					try {
						search(URLEncoder.encode(searchTerms, "UTF-8"));
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
	}

	protected void search(String terms) {
		TraktApiHelper helper = new TraktApiHelper(getResources().getString(
				R.string.trakt_api_key));
		try {
			ArrayList<Show> results = helper.getShowSearchResults(terms);
			if (results.size() == 0) {
				Toast.makeText(getActivity(), "Sorry, no results found :(",
						Toast.LENGTH_LONG).show();
			} else {
				// clear results list first
				mResultList.clear();
				createResultItemsFromShows(results);
				mAdapter.notifyDataSetChanged();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void createResultItemsFromShows(ArrayList<Show> results) {
		Iterator<Show> it = results.iterator();
		while (it.hasNext()) {
			Show show = it.next();
			String title = show.getTitle();
			String id = show.getTvdbId();
			String image = show
					.getSizedPosterUrl(TraktApiHelper.API_POSTER_SIZE_SMALL);
			String year = Integer.toString(show.getYear());
			String network = show.getNetwork();
			mResultList.add(new SearchResultItem(title, id, image, year,
					network));

			// set adapter
			mAdapter = new SearchResultListAdapter(getActivity(), mResultList);
			mListView.setAdapter(mAdapter);
		}
	}

}
