package com.bewareofraj.mytvtracker.search;

import android.app.Fragment;
import android.app.ProgressDialog;
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

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.bewareofraj.mytvtracker.R;
import com.bewareofraj.mytvtracker.api.Show;
import com.bewareofraj.mytvtracker.api.TraktApiHelper;
import com.bewareofraj.mytvtracker.tvshow.ShowListActivity;
import com.bewareofraj.mytvtracker.util.ApplicationController;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SearchFragment extends Fragment {

	private List<SearchResultItem> mResultList = new ArrayList<>();
	private ListView mListView;
    
    private EditText mTxtSearch;
    private Button mBtnSearch;
    
    private ProgressDialog mDialog;

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
		mTxtSearch = (EditText) inflatedView
				.findViewById(R.id.txtSearch);
		mBtnSearch = (Button) inflatedView.findViewById(R.id.btnSearch);
        mBtnSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String searchTerms = mTxtSearch.getText().toString();
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
        final String requestTag = "search";
		String query = TraktApiHelper.getSearchQuery(terms, limit, getString(R.string.trakt_api_key), requestTag);
        
        mDialog = new ProgressDialog(getActivity());
        mDialog.setMessage(getString(R.string.search_please_wait));
        mDialog.setCancelable(true);
        mDialog.show();
        
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                VolleyLog.d(requestTag, "Error: " + volleyError.getMessage());
                mDialog.dismiss();
            }
        };
        
        Response.Listener<JSONArray> responseListener = new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray jsonArray) {
                try {
                    ArrayList<Show> results = TraktApiHelper.getSearchResults(jsonArray);
                    createResultItemsFromShows(results);
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    mDialog.dismiss();
                }
            }
        };

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(query, responseListener, errorListener);
        ApplicationController.getInstance().addToRequestQueue(jsonArrayRequest, requestTag);
        
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
				//String image = show.getSizedPosterUrl(TraktApiHelper.API_POSTER_SIZE_SMALL);
                String image = show.getPosterUrl();
				String year = (show.getYear() == 0) ? getString(R.string.tbd) : Integer
						.toString(show.getYear());
				String network = show.getNetwork();
				mResultList.add(new SearchResultItem(title, id, image, year,
						network));

				// set adapter
                SearchResultListAdapter mAdapter = new SearchResultListAdapter(getActivity(),
                        mResultList);
				mListView.setAdapter(mAdapter);
				mAdapter.notifyDataSetChanged();
			}
		}
	}

}
