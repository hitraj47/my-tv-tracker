package com.bewareofraj.mytvtracker.search;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.bewareofraj.mytvtracker.R;
import com.bewareofraj.mytvtracker.util.MyApplication;

import java.util.List;

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
		
		SearchResultItem result = mResultItems.get(position);
		
		TextView lblShowTitle = (TextView) convertView.findViewById(R.id.lblSearchTitle);
		lblShowTitle.setText(result.getName());
		
		TextView lblYear = (TextView) convertView.findViewById(R.id.lblSearchYear);
		lblYear.setText(result.getYear());
		
		TextView lblDescription = (TextView) convertView.findViewById(R.id.lblSearchDescription);
		lblDescription.setText(result.getDescription());

        ImageLoader imageloader = MyApplication.getInstance().getImageLoader();
        NetworkImageView imgPoster = (NetworkImageView) convertView.findViewById(R.id.imgSearchPoster);
        imgPoster.setImageUrl(result.getImageUrl(), imageloader);

		return convertView;
	}

}
