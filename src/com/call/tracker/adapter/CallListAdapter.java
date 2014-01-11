package com.call.tracker.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.call.tracker.R;
import com.call.tracker.calllist.CallListActivity;
import com.call.tracker.model.CallListModel;

public class CallListAdapter extends BaseAdapter {

	private ArrayList<CallListModel> callListModels = new ArrayList<CallListModel>();
	private CallListActivity activity;

	public CallListAdapter(CallListActivity mActivity,
			ArrayList<CallListModel> callList) {
		this.activity = mActivity;
		this.callListModels = callList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return callListModels.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return callListModels.get(arg0);
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
			mView = inflater.inflate(R.layout.list_call_list, parent, false);
			holder = new ViewHolder();

			holder.textName = (TextView) mView.findViewById(R.id.textName);
			holder.textLast = (TextView) mView.findViewById(R.id.textLast);
			holder.textNext = (TextView) mView.findViewById(R.id.textNext);

			mView.setTag(holder);
		} else
			holder = (ViewHolder) mView.getTag();

		String name = callListModels.get(position).getName();

		// if(name)
		if (name.length() == 0)
			name = callListModels.get(position).getNumber();
		// String mNumber = callListModels.get(position).getNumber();
		// for (int i = 0; i < userListModels.size(); i++) {
		// if (mNumber.contains(userListModels.get(i).getNumber())
		// || userListModels.get(i).getNumber().contains(mNumber)) {
		// name = userListModels.get(i).getName();
		// break;
		// } else {
		// name = callListModels.get(position).getNumber();
		// }
		// }

		holder.textName.setText(name);
		holder.textLast.setText(callListModels.get(position).getDate());
		holder.textNext.setText("0");

		return mView;
	}

	public class ViewHolder {
		private TextView textName;
		private TextView textLast;
		private TextView textNext;
	}
}
