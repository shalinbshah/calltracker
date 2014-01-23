package com.call.tracker.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.call.tracker.R;
import com.call.tracker.listmanager.ListManagerDetails;
import com.call.tracker.model.ListManagerModel;

public class ListManagerAdapter extends BaseAdapter {

	private ArrayList<ListManagerModel> mListManagerModels = new ArrayList<ListManagerModel>();
	private ListManagerDetails activity;

	public ListManagerAdapter(ListManagerDetails mActivity,
			ArrayList<ListManagerModel> callList) {
		this.activity = mActivity;
		this.mListManagerModels = callList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mListManagerModels.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mListManagerModels.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		View mView = convertView;
		ViewHolder holder = null;
		if (mView == null) {
			LayoutInflater inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			mView = inflater.inflate(R.layout.list_list_manager, parent, false);
			holder = new ViewHolder();

			holder.textName = (TextView) mView.findViewById(R.id.textName);

			mView.setTag(holder);
		} else
			holder = (ViewHolder) mView.getTag();

		String name = mListManagerModels.get(position).getName();

		// if(name)
		if (name.length() == 0)
			name = mListManagerModels.get(position).getName();

		holder.textName.setText(name);
		return mView;
	}

	public class ViewHolder {
		private TextView textName;
	}
}
