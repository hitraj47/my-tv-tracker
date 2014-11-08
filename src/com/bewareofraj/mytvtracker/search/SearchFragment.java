package com.bewareofraj.mytvtracker.search;

import java.util.ArrayList;
import java.util.List;

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
		final EditText txtSearch = (EditText) inflatedView.findViewById(R.id.txtSearch);		
		Button btnSearch = (Button) inflatedView.findViewById(R.id.btnSearch);
		btnSearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String searchTerms = txtSearch.getText().toString();
				if (searchTerms.equals("")) {
					Toast.makeText(getActivity(), "Please enter something to search for...", Toast.LENGTH_LONG).show();
				} else {
					search(searchTerms);
				}				
			}
		});

		//TODO: remove this some time!
		createDummyData();

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
		//mAdapter = new SearchResultListAdapter(getActivity(), mResultList);
		//mListView.setAdapter(mAdapter);

		return inflatedView;
	}

	protected void search(String string) {
		// TODO Auto-generated method stub
		
	}

	private void createDummyData() {
		mResultList.add(new SearchResultItem("The Walking Dead", "153021"));
		mResultList.add(new SearchResultItem("The Big Bang Theory", "153021"));
		mResultList.add(new SearchResultItem("The Americans", "153021"));
	}

}
