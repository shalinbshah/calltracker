package com.call.tracker.adapter;

import java.io.InputStream;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.call.tracker.R;
import com.call.tracker.calllist.CallListActivity;
import com.call.tracker.model.CallListModel;

public class CallListAdapter extends BaseAdapter {
	private ArrayList<CallListModel> callListModels;
	private CallListActivity activity;
	InputStream input;

	public CallListAdapter(CallListActivity mActivity,
			ArrayList<CallListModel> callList) {
		this.activity = mActivity;
		this.callListModels = callList;
	}

	public void setCallListModels(ArrayList<CallListModel> callListModels) {
		this.callListModels = callListModels;
	}

	@Override
	public int getCount() {
		return callListModels.size();
	}

	@Override
	public Object getItem(int arg0) {
		return callListModels.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		View mView = convertView;
		ViewHolder holder = null;
		mView = null;

		if (mView == null) {
			LayoutInflater inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			mView = inflater.inflate(R.layout.list_call_list, parent, false);
			holder = new ViewHolder();

			holder.textName = (TextView) mView.findViewById(R.id.textName);
			holder.textLast = (TextView) mView.findViewById(R.id.textLast);
			holder.textNext = (TextView) mView.findViewById(R.id.textNext);
			holder.uri_badge = (ImageView) mView
					.findViewById(R.id.quickContactBadge1);
			mView.setTag(callListModels.get(position));
		}
		String name = callListModels.get(position).getName();
		try {
			input = ContactsContract.Contacts.openContactPhotoInputStream(
					activity.getContentResolver(), callListModels.get(position)
							.getContactUri());
			if (null != input) {
				holder.uri_badge.setImageBitmap(BitmapFactory
						.decodeStream(input));
				input = null;
			}
		} catch (Exception e) {
			holder.uri_badge.setImageBitmap(BitmapFactory.decodeResource(
					activity.getResources(), R.drawable.man));
			e.printStackTrace();
		}
		if (name.length() == 0)
			name = callListModels.get(position).getNumber();
		holder.textName.setText(name);
		holder.textLast.setText("Last Call : "
				+ callListModels.get(position).getDate());
		holder.textNext.setText("Next Call : "
				+ callListModels.get(position).getDate());
		return mView;
	}

	public class ViewHolder {
		private ImageView uri_badge;
		private TextView textName;
		private TextView textLast;
		private TextView textNext;
	}

}
