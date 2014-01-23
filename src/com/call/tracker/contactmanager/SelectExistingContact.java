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
import com.call.tracker.listmanager.ListManagerDetails;

public class SelectExistingContact extends BaseActivity {

	CheckBox checkdontshow;
	public static final int CONTACT_PICKER_RESULT = 1001;
	public static final int GRP_PICKER_RESULT = 1002;
	Intent pickedContact;

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
				pickedContact = data;
				Intent intent = new Intent(this, AlbumsListPopUp.class);
				startActivityForResult(intent, GRP_PICKER_RESULT);
				break;
			case GRP_PICKER_RESULT:
				ContactManagerUtility utility = new ContactManagerUtility();
				Log.d("CallTracker", "On Grp Selection " + data);
				Log.d("CallTracker",
						"On Grp Selection "
								+ data.getExtras().getString(
										ListManagerDetails.GROUP_ID_KEY));
				if (utility.addContactInDB(this,
						pickedContact.putExtras(data.getExtras()))) {
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
