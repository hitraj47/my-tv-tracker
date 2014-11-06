package com.bewareofraj.mytvtracker.search;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bewareofraj.mytvtracker.R;

public class SearchResultListAdapter extends BaseAdapter {

	private Activity mActivity;
	private LayoutInflater mInflater;
	private List<SearchResultItem> mResultItems;

	public SearchResultListAdapter(Activity activity,
			List<SearchResultItem> resultItems) {
		this.mActivity = activity;
		this.mResultItems = resultItems;
	}

	@Override
	public int getCount() {
		return mResultItems.size();
	}

	@Override
	public Object getItem(int position) {
		return mResultItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
			
		if (mInflater == null) {
			mInflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.search_result_item, null);
		}
		
		TextView showName = (TextView) convertView.findViewById(R.id.show_name);
		SearchResultItem result = mResultItems.get(position);
		showName.setText(result.getName());
		
		return convertView;
	}

}
