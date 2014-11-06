package com.bewareofraj.mytvtracker.search;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.bewareofraj.mytvtracker.R;

public class SearchFragment extends Fragment {
	
	private List<SearchResultItem> mResultList = new ArrayList<SearchResultItem>();
	private ListView mListView;
	private SearchResultListAdapter mAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View inflatedView = inflater.inflate(R.layout.fragment_search, container, false);
		
		mListView = (ListView) inflatedView.findViewById(R.id.search_results_list);
		mListView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(getActivity(), "testing", Toast.LENGTH_SHORT).show();				
			}
		});
		mAdapter = new SearchResultListAdapter(getActivity(), mResultList);
		mListView.setAdapter(mAdapter);
		
		mResultList.add(new SearchResultItem("The Walking Dead", "walkingdead"));
		mResultList.add(new SearchResultItem("The Big Bang Theory", "tbbt"));
		mResultList.add(new SearchResultItem("The Americans", "theamericans"));
		
		return inflatedView;
	}

}
