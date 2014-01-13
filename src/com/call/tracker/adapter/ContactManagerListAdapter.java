package com.call.tracker.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.call.tracker.R;
import com.call.tracker.contactmanager.ContactManagerLandingActivity;
import com.call.tracker.model.ContactModel;

public class ContactManagerListAdapter extends BaseAdapter {

	private ArrayList<ContactModel> contactListModels = new ArrayList<ContactModel>();
	private ContactManagerLandingActivity activity;

	public ContactManagerListAdapter(ContactManagerLandingActivity mActivity,
			ArrayList<ContactModel> contactList) {
		this.activity = mActivity;
		this.contactListModels = contactList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return contactListModels.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return contactListModels.get(arg0);
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
			mView = inflater.inflate(R.layout.list_contact_list, parent, false);
			holder = new ViewHolder();

			holder.textName = (TextView) mView.findViewById(R.id.textName);
			holder.textGroup = (TextView) mView.findViewById(R.id.textGroup);

			mView.setTag(holder);
		} else
			holder = (ViewHolder) mView.getTag();

		String name = contactListModels.get(position).getName();
		ArrayList<String> grp = contactListModels.get(position).getGroup();

		if (name.length() == 0)
			name = contactListModels.get(position).getNumber1();
		holder.textName.setText(name);
		holder.textGroup.setText(grp.toString());

		return mView;
	}

	public class ViewHolder {
		private TextView textName;
		private TextView textGroup;
	}
}
