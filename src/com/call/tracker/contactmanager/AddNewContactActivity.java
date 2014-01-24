package com.call.tracker.contactmanager;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;

import com.call.tracker.BaseActivity;
import com.call.tracker.R;

public class AddNewContactActivity extends BaseActivity {
	private static final int ADD_NEW_CONTACT = 2001;
	public static final int CONTACT_PICKER_RESULT = 2001;
	public static final int GRP_PICKER_RESULT = 2002;
	Intent pickedContact;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_contact_ways);
	}

	public void callAddExisting(View v) {
		startActivity(new Intent(getApplicationContext(),
				SelectExistingContact.class));
	}

	public void callAddNew(View v) {
		Intent addNewContact = new Intent(Intent.ACTION_INSERT);
		addNewContact.setType(ContactsContract.Contacts.CONTENT_TYPE);
		startActivityForResult(addNewContact, ADD_NEW_CONTACT);
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
			// case GRP_PICKER_RESULT:
			// ContactManagerUtility utility = new ContactManagerUtility();
			// Log.d("CallTracker", "On Grp Selection " + data);
			// Log.d("CallTracker",
			// "On Grp Selection "
			// + data.getExtras().getString(
			// ListManagerDetails.GROUP_ID_KEY));
			// if (utility.addContactInDB(this,
			// pickedContact.putExtras(data.getExtras()))) {
			// Toast.makeText(getApplicationContext(),
			// "Contact Added Successfully !!!",
			// Toast.LENGTH_SHORT).show();
			// finish();
			// } else {
			// Toast.makeText(getApplicationContext(),
			// "Contact Add UnSuccessfull !!!", Toast.LENGTH_SHORT)
			// .show();
			// finish();
			// }
			// break;
			}
		} else {
			Log.w("CallTracker", "Warning: activity result not ok");
		}
	}
}
