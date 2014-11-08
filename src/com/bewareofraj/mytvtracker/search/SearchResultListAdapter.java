package com.bewareofraj.mytvtracker.search;

import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bewareofraj.mytvtracker.R;
import com.bewareofraj.mytvtracker.api.TraktApiHelper;
import com.bewareofraj.mytvtracker.images.ImageLoader;

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
		
		TextView lblNetwork = (TextView) convertView.findViewById(R.id.lblSearchNetwork);
		lblNetwork.setText(result.getNetwork());
		
		ImageView imgPoster = (ImageView) convertView.findViewById(R.id.imgSearchPoster);
		ImageLoader imgLoader = new ImageLoader(parent.getContext(), determineBestImageWidth(parent.getContext()));
		int loadingImage = R.drawable.ic_launcher;	// loading image, use logo temporarily for now
		imgLoader.DisplayImage(result.getImageUrl(), loadingImage, imgPoster);
		
		return convertView;
	}

	private int determineBestImageWidth(Context context) {
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		int width = metrics.widthPixels;
		
		if (width > 300) {
			int posterSize = Integer.parseInt(TraktApiHelper.API_POSTER_SIZE_SMALL.substring(TraktApiHelper.API_POSTER_SIZE_SMALL.lastIndexOf('-') + 1));
			return posterSize;
		} else {
			return 60;
		}
	}

}
