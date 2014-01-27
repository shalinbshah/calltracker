package com.call.tracker.calllist;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.PhoneLookup;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.call.tracker.BaseActivity;
import com.call.tracker.R;
import com.call.tracker.adapter.CallListDetailsAdapter;
import com.call.tracker.model.CallListModel;
import com.call.tracker.model.CallNumberModel;

public class DetailActivity extends BaseActivity {
	private CallListModel callUserModel;
	private TextView textViewCallerName;
	private ArrayList<CallNumberModel> callNumberModels = new ArrayList<CallNumberModel>();
	private ListView callListView;
	private CallListDetailsAdapter callListDetailsAdapter;
	private String currentEmail;
	Bitmap bitmapImage = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		Bundle bundle = getIntent().getExtras();

		if (bundle != null) {
			setContentView(R.layout.layout_detail);
			callUserModel = (CallListModel) bundle.getSerializable("callData");
			initControl();
		} else {
			alertBox("No Data Found");
		}
	}

	public void callEdit(View v) {
		preToast("Coming Soon");
	}

	public void callSMS(View v) {
		Toast.makeText(getApplicationContext(), "Sms button Touched",
				Toast.LENGTH_SHORT).show();
	}

	public void callAlert(View v) {
		// preToast("Coming Soon");

		Intent intent = new Intent(getApplicationContext(),
				VoiceCallActivity.class);
		intent.putExtra("callData", callUserModel);
		intent.putExtra("bitmap", bitmapImage);
		startActivity(intent);
	}

	public void callMail(View v) {
		// preToast("Coming Soon");

		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_EMAIL, currentEmail);
		intent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
		intent.putExtra(Intent.EXTRA_TEXT, "I'm email body.");

		startActivity(Intent.createChooser(intent, "Send Email"));
	}

	private void initControl() {
		// TODO Auto-generated method stub
		textViewCallerName = (TextView) findViewById(R.id.textViewCallerName);

		callListView = (ListView) findViewById(R.id.listCall);
		String name = callUserModel.getName();

		callNumberModels.clear();

		if (name.length() == 0) {
			name = "No Contact Found";
			CallNumberModel callNumberModel = new CallNumberModel();
			callNumberModel.setNumber(callUserModel.getNumber());
			callNumberModel.setNumberType("Other");
			callNumberModels.add(callNumberModel);

			setListAdapter();
		} else {
			getDetails(callUserModel.getNumber());
		}
		textViewCallerName.setText(name);
	}

	@SuppressLint("InlinedApi")
	public void readContacts(String id) {

		StringBuffer sb = new StringBuffer();
		sb.append("......Contact Details.....");
		ContentResolver cr = getContentResolver();
		Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
				ContactsContract.Contacts._ID + " = ?", new String[] { id },
				null);
		String phone = null;
		String caller_id = null;
		String emailContact = null;
		String emailType = null;
		String image_uri = "";

		if (cur.getCount() > 0) {
			while (cur.moveToNext()) {
				caller_id = cur.getString(cur
						.getColumnIndex(ContactsContract.Contacts._ID));
				String name = cur
						.getString(cur
								.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
				image_uri = cur
						.getString(cur
								.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));

				if (Integer
						.parseInt(cur.getString(cur
								.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
					System.out
							.println("name : " + name + ", ID : " + caller_id);
					sb.append("\n Contact Name:" + name);
					Cursor pCur = cr.query(
							ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
							null,
							ContactsContract.CommonDataKinds.Phone.CONTACT_ID
									+ " = ?", new String[] { caller_id }, null);

					while (pCur.moveToNext()) {
						phone = pCur
								.getString(pCur
										.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
						// String phoneM = pCur
						// .getString(pCur
						// .getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
						sb.append("\n Phone number:" + phone);

						// String customLabel = pCur
						// .getString(pCur
						// .getColumnIndex(ContactsContract.CommonDataKinds.Phone.LABEL));
						int phonetype = pCur
								.getInt(pCur
										.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));

						// String phoneLabel = (String)
						// ContactsContract.CommonDataKinds.Email
						// .getTypeLabel(this.getResources(), phonetype,
						// customLabel);
						String numberType = getNumberType(phonetype);

						CallNumberModel callNumberModel = new CallNumberModel();
						callNumberModel.setNumber(phone);
						callNumberModel.setNumberType(numberType);

						callNumberModels.add(callNumberModel);
					}
					pCur.close();
					Cursor emailCur = cr.query(
							ContactsContract.CommonDataKinds.Email.CONTENT_URI,
							null,
							ContactsContract.CommonDataKinds.Email.CONTACT_ID
									+ " = ?", new String[] { caller_id }, null);
					while (emailCur.moveToNext()) {
						emailContact = emailCur
								.getString(emailCur
										.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
						emailType = emailCur
								.getString(emailCur
										.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));
						sb.append("\nEmail:" + emailContact + "Email type:"
								+ emailType);
						currentEmail = emailContact;
					}
					emailCur.close();
				}
				if (image_uri != null) {
					System.out.println(Uri.parse(image_uri));
					try {
						bitmapImage = MediaStore.Images.Media
								.getBitmap(this.getContentResolver(),
										Uri.parse(image_uri));
						sb.append("\n Image in Bitmap:" + bitmapImage);
						System.out.println(bitmapImage);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				sb.append("\n........................................");
			}
		}

		setListAdapter();
	}

	private void setListAdapter() {
		callListDetailsAdapter = new CallListDetailsAdapter(this,
				callNumberModels);

		callListView.setAdapter(callListDetailsAdapter);
	}

	private String getNumberType(int phonetype) {
		// TODO Auto-generated method stub
		switch (phonetype) {
		case Phone.TYPE_MOBILE:
			return "Mobile";
		case Phone.TYPE_WORK:
			return "Work";
		case Phone.TYPE_HOME:
			return "Home";
		case Phone.TYPE_MAIN:
			return "Main";
		case Phone.TYPE_FAX_WORK:
			return "Work Fax";
		case Phone.TYPE_FAX_HOME:
			return "Home Fax";
		case Phone.TYPE_PAGER:
			return "Pager";
		case Phone.TYPE_OTHER:
			return "Other";
		case Phone.TYPE_CUSTOM:
			return "Custom";
		default:
			break;
		}
		return "Other";
	}

	public void getDetails(String phoneNumber) {
		ContentResolver contentResolver = getContentResolver();

		Uri uri = Uri.withAppendedPath(
				ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
				Uri.encode(phoneNumber));

		String[] projection = new String[] { PhoneLookup.DISPLAY_NAME,
				PhoneLookup._ID };

		Cursor cursor = contentResolver
				.query(uri, projection, null, null, null);

		if (cursor != null) {
			while (cursor.moveToNext()) {
				String contactName = cursor.getString(cursor
						.getColumnIndexOrThrow(PhoneLookup.DISPLAY_NAME));
				String contactId = cursor.getString(cursor
						.getColumnIndexOrThrow(PhoneLookup._ID));
				Log.d("----------------------->", "contactMatch name: "
						+ contactName);
				Log.d("----------------------->", "contactMatch id: "
						+ contactId);

				readContacts(contactId);
			}
			cursor.close();
		}
	}
}
