package com.call.tracker.listmanager;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.call.tracker.R;
import com.call.tracker.model.ListManagerModel;

public class ListManagerAdapter extends BaseAdapter {

	private final Activity activity;
	private final ArrayList<ListManagerModel> data;
	private static LayoutInflater inflater = null;

	public ListManagerAdapter(Activity mainActivity,
			ArrayList<ListManagerModel> listCollectionDetails) {
		activity = mainActivity;
		data = listCollectionDetails;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = convertView;
		convertView = null;
		ListMangerViewHolder viewHolder = new ListMangerViewHolder();
		if (convertView == null) {
			view = inflater.inflate(R.layout.listmanagerlist_cell, null);
			viewHolder.textName = (TextView) view
					.findViewById(R.id.tvAlbumName);

		}
		// Bind the data with the holder.
		view.setTag(position);
		viewHolder.textName.setText(data.get(position).getName());
		return view;
	}

	public class ListMangerViewHolder {
		private TextView textName;
	}

}
