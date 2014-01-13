package com.call.tracker.voicenotes;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;

import com.call.tracker.model.ContactModel;

public class ContactLoadingAsync extends
		AsyncTask<String, String, ArrayList<ContactModel>> {
	private ArrayList<ContactModel> arrayListContactDatas;
	private Context mContext;
	private ContactLoadingCompletedListener listener;

	// private ProgressDialog mProgressDialog;

	/**
	 * @param mContext
	 * @param listener
	 */
	public ContactLoadingAsync(Context mContext,
			ContactLoadingCompletedListener listener) {
		super();
		this.mContext = mContext;
		this.listener = listener;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		arrayListContactDatas = new ArrayList<ContactModel>();
	}

	@Override
	protected void onPostExecute(ArrayList<ContactModel> result) {
		super.onPostExecute(result);
		// mProgressDialog.dismiss();
		if (listener != null) {
			listener.onCompleted(arrayListContactDatas);
		}
	}

	@Override
	protected ArrayList<ContactModel> doInBackground(String... params) {
		readContacts();
		return null;
	}

	public void readContacts() {
		ContentResolver cr = mContext.getContentResolver();
		Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
				null, null, null);

		if (cur.getCount() > 0) {
			while (cur.moveToNext()) {

				ContactModel contactData = new ContactModel();
				String[] phoneNumber = new String[10000];

				String id = cur.getString(cur
						.getColumnIndex(ContactsContract.Contacts._ID));
				String name = cur
						.getString(cur
								.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
				contactData.setContactId(id);
				if (Integer
						.parseInt(cur.getString(cur
								.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
					// Log.i("name->" + name, "ID" + id);
					// get the phone number
					Cursor pCur = cr.query(
							ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
							null,
							ContactsContract.CommonDataKinds.Phone.CONTACT_ID
									+ " = ?", new String[] { id }, null);
					int count = 0;
					while (pCur.moveToNext()) {
						String phone = pCur
								.getString(pCur
										.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
						Log.i("phone->" + phone, "ID" + id);
						phoneNumber[count] = phone;
						count++;
					}
					pCur.close();

					if (phoneNumber != null && phoneNumber.length == 1
							&& count != 0) {
						// contactData = new ContactData(name, phoneNumber[0],
						// "",
						// "", "",false);
						contactData.setCheck(false);
						contactData.setName(name);
						contactData.setNumber1(phoneNumber[0]);
						arrayListContactDatas.add(contactData);
					} else if (phoneNumber != null && phoneNumber.length == 2) {
						// contactData = new ContactData(name, phoneNumber[0],
						// phoneNumber[1], "", "",false);
						contactData.setCheck(false);
						contactData.setName(name);
						contactData.setNumber1(phoneNumber[0]);
						arrayListContactDatas.add(contactData);
					} else if (phoneNumber != null && phoneNumber.length == 3) {
						// contactData = new ContactData(name, phoneNumber[0],
						// phoneNumber[1], phoneNumber[2], "",false);
						contactData.setCheck(false);
						contactData.setName(name);
						contactData.setNumber1(phoneNumber[0]);
						arrayListContactDatas.add(contactData);
					} else if (phoneNumber != null && phoneNumber.length >= 4) {
						// contactData = new ContactData(name, phoneNumber[0],
						// phoneNumber[1], phoneNumber[2],
						// phoneNumber[3],false);
						contactData.setCheck(false);
						contactData.setName(name);
						contactData.setNumber1(phoneNumber[0]);
						arrayListContactDatas.add(contactData);
					}
				}
			}
		}
	}
}
