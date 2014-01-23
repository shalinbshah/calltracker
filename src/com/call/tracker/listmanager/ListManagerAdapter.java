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

		View mView = convertView;
		ListMangerViewHolder holder = null;
		if (mView == null) {
			mView = inflater.inflate(R.layout.listmanagerlist_cell, parent,
					false);
			holder = new ListMangerViewHolder();

			holder.textName = (TextView) mView.findViewById(R.id.tvAlbumName);
			// if (position % 2 == 0)
			// mView.setBackgroundColor(activity.getResources().getColor(
			// R.color.theme_color));
			mView.setTag(holder);
		} else
			holder = (ListMangerViewHolder) mView.getTag();
		if (position < data.size()) {
			String name = data.get(position).getName();
			holder.textName.setText(name);
		} else
			holder.textName.setText("Add New Group");
		return mView;
	}

	public class ListMangerViewHolder {
		private TextView textName;
	}

}
