package com.bewareofraj.mytvtracker.search;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
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
import com.bewareofraj.mytvtracker.api.RetrieveTraktJSONTask;
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
							getString(R.string.search_field_empty),
							Toast.LENGTH_LONG).show();
				} else {
					try {
						search(URLEncoder.encode(searchTerms, "UTF-8"), 10);
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
	}

	private void search(String terms, int limit) {
		TraktApiHelper helper = new TraktApiHelper(getResources().getString(
				R.string.trakt_api_key));
		String query;
		try {
			query = helper.searchShows(terms, Integer.toString(limit));
			new SearchAsync().execute(query);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	private void createResultItemsFromShows(ArrayList<Show> results) {
		if (results.size() == 0) {
			Toast.makeText(getActivity(), getString(R.string.search_no_results),
					Toast.LENGTH_LONG).show();
		} else {
			mResultList.clear();
			Iterator<Show> it = results.iterator();
			while (it.hasNext()) {
				Show show = it.next();
				String title = show.getTitle();
				String id = show.getTvdbId();
				String image = show
						.getSizedPosterUrl(TraktApiHelper.API_POSTER_SIZE_SMALL);
				String year = (show.getYear() == 0) ? getString(R.string.tbd) : Integer
						.toString(show.getYear());
				String network = show.getNetwork();
				mResultList.add(new SearchResultItem(title, id, image, year,
						network));

				// set adapter
				mAdapter = new SearchResultListAdapter(getActivity(),
						mResultList);
				mListView.setAdapter(mAdapter);
				mAdapter.notifyDataSetChanged();
			}
		}
	}

	private class SearchAsync extends RetrieveTraktJSONTask {
		private ProgressDialog mDialog;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			mDialog = new ProgressDialog(getActivity());
			mDialog.setMessage(getString(R.string.search_please_wait));
			mDialog.setCancelable(true);
			mDialog.setOnCancelListener(new OnCancelListener() {
				
				@Override
				public void onCancel(DialogInterface dialog) {
					cancel(true);
					mDialog.dismiss();
				}
			});
			mDialog.show();
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			TraktApiHelper helper = new TraktApiHelper(
					getString(R.string.trakt_api_key));
			if (mDialog.isShowing()) {
				mDialog.dismiss();
			}
			try {
				createResultItemsFromShows(helper.getShowSearchResults(result));
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
	}

}
