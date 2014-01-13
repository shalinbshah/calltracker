package com.call.tracker.contactmanager;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;

import com.call.tracker.BaseActivity;
import com.call.tracker.R;

public class ContactManagerMainActivity extends BaseActivity {
	private static final int ADD_NEW_CONTACT = 1002;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_contact_main);
	}

	public void callAddExisting(View v) {
		startActivity(new Intent(getApplicationContext(),
				ContactManagerAddExistingContact.class));
	}

	public void callAddNew(View v) {
		// Intent intent = new Intent(getApplicationContext(),
		// NewContactActivity.class);
		// intent.putExtra("Type", "contact");
		// startActivity(intent);
		Intent addNewContact = new Intent(Intent.ACTION_INSERT);
		addNewContact.setType(ContactsContract.Contacts.CONTENT_TYPE);
		startActivityForResult(addNewContact, ADD_NEW_CONTACT);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ADD_NEW_CONTACT) {
			if (resultCode == -1) {
				ContactManagerUtility utility = new ContactManagerUtility();
				utility.addContactInDB(this, data);
			} else {
				Log.i("New contact Added : ",
						"Canceled to adding new contacts : Not need to update database");
			}
		}
	}
}
