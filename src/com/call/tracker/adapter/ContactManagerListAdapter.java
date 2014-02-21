package com.call.tracker.adapter;

import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.call.tracker.R;
import com.call.tracker.calllist.CallHangUpActivity;
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
		return contactListModels.size();
	}

	@Override
	public Object getItem(int arg0) {
		return contactListModels.get(arg0);
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
			mView = inflater.inflate(R.layout.list_contact_list, parent, false);
			holder = new ViewHolder();
			holder.textName = (TextView) mView.findViewById(R.id.textName);
			holder.textGroup = (TextView) mView.findViewById(R.id.textGroup);
			holder.uri_badge = (ImageView) mView
					.findViewById(R.id.quickContactBadge2);
			mView.setTag(holder);
		}

		String name = contactListModels.get(position).getName();
		ArrayList<String> grp = contactListModels.get(position).getGroup();
		// Uri uri = (contactListModels.get(position).getUri());
		try {
			holder.uri_badge.setTag(contactListModels.get(position)
					.getNumber1());
			holder.uri_badge.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					CallHangUpActivity.calledFromApp = true;
					CallHangUpActivity.callStartTime = new Date();
					Intent callIntent = new Intent(Intent.ACTION_CALL);
					callIntent.setData(Uri
							.parse("tel:" + v.getTag().toString()));
					activity.startActivity(callIntent);
				}
			});

			// InputStream input = ContactsContract.Contacts
			// .openContactPhotoInputStream(activity.getContentResolver(),
			// uri);
			// if (null != input)
			// holder.uri_badge.setImageBitmap(BitmapFactory
			// .decodeStream(input));
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (name.length() == 0)
			name = contactListModels.get(position).getNumber1();
		holder.textName.setText(name);
		holder.textGroup.setText(grp.toString());
		return mView;
	}

	public class ViewHolder {
		private TextView textName;
		private TextView textGroup;
		private ImageView uri_badge;
	}
}
