package com.bewareofraj.mytvtracker.tvshow;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bewareofraj.mytvtracker.R;
import com.bewareofraj.mytvtracker.dummy.DummyContent;

/**
 * A fragment representing a single Show detail screen. This fragment is either
 * contained in a {@link ShowListActivity} in two-pane mode (on tablets) or a
 * {@link ShowDetailActivity} on handsets.
 */
public class ShowDetailFragment extends Fragment {
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_ID = "item_id";

	/**
	 * The dummy content this fragment is presenting.
	 */
	private DummyContent.DummyItem mItem;
	
	public static final String SHOW_ID = "show_id";
	public static final String SHOW_NAME = "show_name";
	public static final String SHOW_TIME = "show_time";
	
	private String mShowId, mShowName, mShowTime;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public ShowDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getActivity().getIntent();
		mShowName = intent.getStringExtra(SHOW_NAME);
		mShowTime = intent.getStringExtra(SHOW_TIME);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_show_detail,
				container, false);
		
		TextView txtShowName = (TextView) rootView.findViewById(R.id.txtShowName);
		txtShowName.setText(mShowName);
		
		TextView txtShowTime = (TextView) rootView.findViewById(R.id.txtShowTime);
		txtShowTime.setText(mShowTime);
		
		return rootView;
	}
}
