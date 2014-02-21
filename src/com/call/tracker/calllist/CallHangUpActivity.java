package com.call.tracker.calllist;

import java.io.InputStream;
import java.util.Date;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.QuickContactBadge;
import android.widget.TextView;

import com.call.tracker.BaseActivity;
import com.call.tracker.R;
import com.call.tracker.database.DBAdapter;

public class CallHangUpActivity extends BaseActivity {

	public static boolean calledFromApp = false;
	public static Date callStartTime;

	QuickContactBadge contactBadge;
	TextView tvContactName;
	String contactID;
	Uri uri;
	String contactNumber;

	public enum ContactStatus {
		SHOTDOWN, NOCONTACT, APPOINTMENT, MESSAGELEFT, CONTACTMODE;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_call_hang);
		contactBadge = (QuickContactBadge) findViewById(R.id.quickContactBadgeContactDateTime);
		if (getIntent().getExtras().containsKey("contact_number")) {
			contactNumber = getIntent().getExtras().getString("contact_number");
			contactBadge.assignContactFromPhone(contactNumber, true);
			contactID = CallListActivity.getContactID(getApplicationContext(),
					contactNumber);
			uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI,
					String.valueOf(contactID));
			if (ContactsContract.Contacts.openContactPhotoInputStream(
					getContentResolver(), uri) != null) {
				InputStream input = ContactsContract.Contacts
						.openContactPhotoInputStream(getContentResolver(), uri);
				contactBadge.setImageBitmap(BitmapFactory.decodeStream(input));
			}
		}

		updateDBWithCallInfo(contactID,
				(new Date().getTime() - callStartTime.getTime()) / 1000);
	}

	private void updateDBWithCallInfo(String contactID2, long l) {
		DBAdapter adapter = new DBAdapter(getApplicationContext());
		adapter.openDataBase();
		ContentValues values = new ContentValues();
		values.put("contact_id", contactID2);
		values.put("call_duration_Seconds", l);
		adapter.getMyDatabase().insert("tbl_calls_track", null, values);
		adapter.close();
	}

	public void callAppointMentalMode(View v) {
		Intent intent = new Intent(getApplicationContext(),
				ContactFollowUpDetailsActivity.class);
		// intent.putExtra("CallListModel", callListModel);
		intent.putExtra("contact_name", contactID);
		intent.putExtra("contact_uri", uri);
		intent.putExtra("contact_number", contactNumber);
		// intent.putExtra("contact", arrayList.get(arg2));
		startActivity(intent);
	}

	public void callNoContact(View v) {
		DBAdapter adapter = new DBAdapter(getApplicationContext());
		adapter.deleteContact(contactID);
		finish();
	}

	public void callShotDown(View v) {
		DBAdapter adapter = new DBAdapter(getApplicationContext());
		adapter.updateOrAddStats(contactID, ContactStatus.SHOTDOWN.name());
		finish();
	}

	public void callMessageLeft(View v) {
		DBAdapter adapter = new DBAdapter(getApplicationContext());
		adapter.updateOrAddStats(contactID, ContactStatus.MESSAGELEFT.name());
		finish();
	}

	public void callContactMode(View v) {
		DBAdapter adapter = new DBAdapter(getApplicationContext());
		adapter.updateOrAddStats(contactID, ContactStatus.CONTACTMODE.name());
		finish();
	}

}
