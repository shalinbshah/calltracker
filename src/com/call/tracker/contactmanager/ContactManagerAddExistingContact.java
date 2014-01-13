package com.call.tracker.contactmanager;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.call.tracker.BaseActivity;
import com.call.tracker.R;

public class ContactManagerAddExistingContact extends BaseActivity {

	CheckBox checkdontshow;
	private static final int CONTACT_PICKER_RESULT = 1001;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout_con_list1);
		checkdontshow = (CheckBox) findViewById(R.id.checkdontshow);

	}

	public void callGot(View v) {
		updatePref(IS_CONTACT_LIST1, String.valueOf(checkdontshow.isSelected()));
		Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
				Contacts.CONTENT_URI);
		startActivityForResult(contactPickerIntent, CONTACT_PICKER_RESULT);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case CONTACT_PICKER_RESULT:
				ContactManagerUtility utility = new ContactManagerUtility();
				if (utility.addContactInDB(this, data)) {
					Toast.makeText(getApplicationContext(),
							"Contact Added Successfully !!!",
							Toast.LENGTH_SHORT).show();
					finish();
				} else {
					Toast.makeText(getApplicationContext(),
							"Contact Add UnSuccessfull !!!", Toast.LENGTH_SHORT)
							.show();
					finish();
				}
				break;
			}
		} else {
			Log.w("CallTracker", "Warning: activity result not ok");
		}
	}
}
