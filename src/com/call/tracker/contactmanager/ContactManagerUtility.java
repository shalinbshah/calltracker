package com.call.tracker.contactmanager;

import java.io.IOException;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.widget.Toast;

import com.call.tracker.database.DBAdapter;
import com.call.tracker.model.ContactModel;

public class ContactManagerUtility {

	private DBAdapter mDbAdapter;

	public boolean addContactInDB(Activity activity, Intent data) {
		ContactModel contactModel = new ContactModel();
		boolean isSuccess = false;
		// Process Data
		Uri contactData = data.getData();
		ContentResolver cr = activity.getContentResolver();
		Cursor c = cr.query(contactData, null, null, null, null);
		if (c.moveToFirst()) {
			String id = c.getString(c
					.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

			String hasPhone = c
					.getString(c
							.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

			if (hasPhone.equalsIgnoreCase("1")) {
				Cursor phones = activity.getContentResolver().query(
						ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
						null,
						ContactsContract.CommonDataKinds.Phone.CONTACT_ID
								+ " = " + id, null, null);
				phones.moveToFirst();
				String cNumber = phones
						.getString(phones
								.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
				Toast.makeText(activity, cNumber, Toast.LENGTH_SHORT).show();

				String nameContact = c
						.getString(c
								.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
				contactModel.setContactId(id);
				contactModel.setName(nameContact);
				contactModel.setNumber1(cNumber);
				openDB(activity);
				insertNewContact(contactModel);
				// contact number = cNumber
				// contact name = nameContact
			}
		}
		return isSuccess;
	}

	private void insertNewContact(ContactModel contactModel) {
		// TODO Auto-generated method stub
		mDbAdapter.openDataBase();

		// String query = "select * from tbl_group";
		ContentValues values = new ContentValues();
		values.put("contact_id", contactModel.getContactId());
		values.put("contact_name", contactModel.getName());
		values.put("contact_number", contactModel.getNumber1());
		mDbAdapter.getMyDatabase().insert("tbl_contacts", null, values);
		mDbAdapter.close();

	}

	private void openDB(Activity activity) {
		mDbAdapter = DBAdapter.getDBAdapterInstance(activity);
		try {
			mDbAdapter.createDataBase();
		} catch (IOException e) {
		}
	}
}
