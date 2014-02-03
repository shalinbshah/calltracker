package com.call.tracker.calllist;

import java.io.InputStream;

import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.QuickContactBadge;
import android.widget.TextView;

import com.call.tracker.BaseActivity;
import com.call.tracker.R;

public class CallHangUpActivity extends BaseActivity {

	QuickContactBadge contactBadge;
	TextView tvContactName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_call_hang);
		contactBadge = (QuickContactBadge) findViewById(R.id.quickContactBadgeContactDateTime);
		if (getIntent().getExtras().containsKey("contact_number")) {
			String contactNumber = getIntent().getExtras().getString(
					"contact_number");
			contactBadge.assignContactFromPhone(contactNumber, true);
			String contactID = CallListActivity.getContactID(
					getApplicationContext(), contactNumber);
			Uri uri = Uri.withAppendedPath(
					ContactsContract.Contacts.CONTENT_URI,
					String.valueOf(contactID));
			if (ContactsContract.Contacts.openContactPhotoInputStream(
					getContentResolver(), uri) != null) {
				InputStream input = ContactsContract.Contacts
						.openContactPhotoInputStream(getContentResolver(), uri);
				contactBadge.setImageBitmap(BitmapFactory.decodeStream(input));
			}
		}
	}
}
