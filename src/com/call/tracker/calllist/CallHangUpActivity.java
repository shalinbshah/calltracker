package com.call.tracker.calllist;

import java.io.InputStream;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.QuickContactBadge;
import android.widget.TextView;

import com.call.tracker.R;

public class CallHangUpActivity extends Activity {

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
			Uri uri = CallListActivity.getContactUri(getApplicationContext(),
					contactNumber);
			if (ContactsContract.Contacts.openContactPhotoInputStream(
					getContentResolver(), uri) != null) {
				InputStream input = ContactsContract.Contacts
						.openContactPhotoInputStream(getContentResolver(), uri);
				contactBadge.setImageBitmap(BitmapFactory.decodeStream(input));
			}
		}
	}
}
