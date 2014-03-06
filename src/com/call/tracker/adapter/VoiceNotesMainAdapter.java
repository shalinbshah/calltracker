package com.call.tracker.adapter;

import java.io.InputStream;
import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.QuickContactBadge;
import android.widget.TextView;

import com.call.tracker.R;
import com.call.tracker.model.VoiceNotesModel;
import com.call.tracker.voicenotes.VoiceListActivity;

public class VoiceNotesMainAdapter extends BaseAdapter {

	private ArrayList<VoiceNotesModel> voiceNotesModels = new ArrayList<VoiceNotesModel>();
	private VoiceListActivity activity;
	InputStream input;

	public VoiceNotesMainAdapter(VoiceListActivity mActivity,
			ArrayList<VoiceNotesModel> callList) {
		this.activity = mActivity;
		this.voiceNotesModels = callList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return voiceNotesModels.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return voiceNotesModels.get(arg0);
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
		mView = null;
		LayoutInflater inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mView = inflater.inflate(R.layout.list_voice_notes, parent, false);
		holder = new ViewHolder();

		holder.textName = (TextView) mView.findViewById(R.id.textName);
		holder.textUrgent = (ImageView) mView.findViewById(R.id.textUrgent);
		holder.textDate = (TextView) mView.findViewById(R.id.textDate);
		holder.quickContactBadgeVoiceList = (ImageView) mView
				.findViewById(R.id.quickContactBadgeVoiceList);

		mView.setTag(holder);

		String contactId = Integer.toString(voiceNotesModels.get(position)
				.getContact_id());
		String name = getContactName(activity, contactId);
		holder.textName.setText(name);
		boolean isUrgent = voiceNotesModels.get(position).getUrgent() == 1 ? true
				: false;
		if (isUrgent)
			holder.textUrgent.setImageResource(R.drawable.icon_alert_red);
		else
			holder.textUrgent.setImageResource(R.drawable.icon_alert_grey);
		holder.textDate.setText(voiceNotesModels.get(position).getDateTime());
		Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI,
				String.valueOf(contactId));
		try {
			input = ContactsContract.Contacts.openContactPhotoInputStream(
					activity.getContentResolver(), uri);
			if (null != input) {
				holder.quickContactBadgeVoiceList.setImageBitmap(BitmapFactory
						.decodeStream(input));
				input = null;
			}
		} catch (Exception e) {
			holder.quickContactBadgeVoiceList.setImageBitmap(BitmapFactory
					.decodeResource(activity.getResources(), R.drawable.man));
			e.printStackTrace();
		}
		return mView;
	}

	public String getContactName(Context context, String contactId) {
		Uri myPhoneUri = Uri.withAppendedPath(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI, contactId);
		ContentResolver contentResolver = context.getContentResolver();
		String contactName = null;
		// Query the table
		Cursor phoneCursor = contentResolver.query(
				ContactsContract.Contacts.CONTENT_URI, null,
				ContactsContract.Contacts._ID + " = ?",
				new String[] { contactId }, null);

		// Get the details from the contact
		if (phoneCursor.moveToFirst()) {
			contactName = phoneCursor
					.getString(phoneCursor
							.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
		}
		return contactName;
	}

	public class ViewHolder {
		private TextView textName;
		private ImageView textUrgent;
		private TextView textDate;
		private ImageView quickContactBadgeVoiceList;
	}
}
