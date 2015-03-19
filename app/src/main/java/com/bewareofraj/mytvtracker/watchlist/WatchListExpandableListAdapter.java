package com.bewareofraj.mytvtracker.watchlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.bewareofraj.mytvtracker.R;
import com.bewareofraj.mytvtracker.util.MyApplication;

import java.util.ArrayList;

public class WatchListExpandableListAdapter extends BaseExpandableListAdapter {

	private Context mContext;
	private ArrayList<WatchListGroup> mGroups;

	public WatchListExpandableListAdapter(Context context,
			ArrayList<WatchListGroup> groups) {
		this.mContext = context;
		this.mGroups = groups;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		ArrayList<WatchListChild> chList = mGroups.get(groupPosition)
				.getItems();
		return chList.get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	/**
	 * This is the method that will assign the layout elements of the child view
	 * with their data.
	 */
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		WatchListChild child = (WatchListChild) getChild(groupPosition,
				childPosition);
		if (convertView == null) {
			LayoutInflater inf = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inf.inflate(R.layout.watch_list_item, null);
		}
		TextView lblShowName = (TextView) convertView.findViewById(R.id.lblSearchTitle);
		NetworkImageView imgBoxArt = (NetworkImageView) convertView.findViewById(R.id.imgSearchPoster);

		TextView lblShowTime = (TextView) convertView.findViewById(R.id.show_time);

		lblShowName.setText(child.getName().toString());

        ImageLoader imageLoader = MyApplication.getInstance().getImageLoader();
        imgBoxArt.setImageUrl(child.getImage(), imageLoader);

		lblShowTime.setText(child.getShowTime().toString());

		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		ArrayList<WatchListChild> chList = mGroups.get(groupPosition)
				.getItems();
		return chList.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return mGroups.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return mGroups.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	/**
	 * This method sets the title of the parent group in the expandable list
	 */
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		WatchListGroup group = (WatchListGroup) getGroup(groupPosition);
		if (convertView == null) {
			LayoutInflater inf = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inf.inflate(R.layout.watch_list_group, null);
		}
		TextView headingTextView = (TextView) convertView
				.findViewById(R.id.watch_list_group);
		headingTextView.setText(group.getName());
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}
