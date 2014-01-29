package com.call.tracker.calllist;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.call.tracker.BaseActivity;
import com.call.tracker.R;
import com.call.tracker.adapter.CallListAdapter;
import com.call.tracker.adapter.MyProgressDialog;
import com.call.tracker.contactmanager.AlbumsListPopUp;
import com.call.tracker.model.CallListModel;

public class CallListActivity extends BaseActivity {
	protected static final String TAG = "CallListActivity";
	private ListView listContact;
	private CallListAdapter adapter;
	private ArrayList<CallListModel> arrayList = new ArrayList<CallListModel>();
	private EditText editTextSearch;
	private Button butAdd;
	private Button butContactList;
	MyProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_call_list);

		initControl();
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			int val = bundle.getInt("key");
			if (val == 1) {
				butAdd.setVisibility(View.VISIBLE);
			}
		}
	}

	public void callContactList(View v) {
		Intent intent = new Intent(getApplicationContext(),
				AlbumsListPopUp.class);
		startActivity(intent);
	}

	private void initControl() {
		// updatePref(CURRENT_LIST, "0");

		butContactList = (Button) findViewById(R.id.butContactList);
		butContactList.setText("All contacts");
		butAdd = (Button) findViewById(R.id.butAdd);

		listContact = (ListView) findViewById(R.id.listContact);
		listContact.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int arg2, long arg3) {

				Intent intent = new Intent(getApplicationContext(),
						SampleTimeDefault.class);
				intent.putExtra("contact_name", arrayList.get(arg2).getName());
				intent.putExtra("contact_uri", arrayList.get(arg2)
						.getContactUri());
				startActivity(intent);
			}
		});
		new GetCallListFromWebService().execute();
		editTextSearch = (EditText) findViewById(R.id.editTextSearch);
		editTextSearch.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3) {
				generateCallList();
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			@Override
			public void afterTextChanged(Editable arg0) {
			}
		});
	}

	@SuppressLint("SimpleDateFormat")
	private void generateCallList() {

		Cursor managedCursor = getContentResolver().query(
				CallLog.Calls.CONTENT_URI, null, null, null, null);
		int numberIndex = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
		int typeIndex = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
		int dateIndex = managedCursor.getColumnIndex(CallLog.Calls.DATE);
		int durationIndex = managedCursor
				.getColumnIndex(CallLog.Calls.DURATION);
		int nameValIndex = managedCursor
				.getColumnIndex(CallLog.Calls.CACHED_NAME);
		int callLogIdIndex = managedCursor.getColumnIndex(CallLog.Calls._ID);

		arrayList.clear();
		while (managedCursor.moveToNext()) {
			String phoneNumber = managedCursor.getString(numberIndex);
			String callType = managedCursor.getString(typeIndex);
			String timeMillis = managedCursor.getString(dateIndex);
			String callDuration = managedCursor.getString(durationIndex);
			String name = managedCursor.getString(nameValIndex);
			String callLogId = managedCursor.getString(callLogIdIndex);
			if (name == null)
				name = "";
			CallListModel callListModel = new CallListModel();
			callListModel.setId(callLogId);
			callListModel.setName(name);
			Date d = new Date(Long.parseLong(timeMillis));
			SimpleDateFormat formatter = new SimpleDateFormat(
					"dd/MM/yyyy HH:mm:ss");
			callListModel.setDate(formatter.format(d));
			callListModel.setDuration(callDuration);
			callListModel.setNumber(phoneNumber);
			callListModel.setType(callType);
			callListModel.setContactUri(getContactUri(getApplicationContext(),
					phoneNumber));
			arrayList.add(callListModel);
		}
		managedCursor.close();
		Collections.reverse(arrayList);
	}

	public class GetCallListFromWebService extends
			AsyncTask<Void, Void, String[]> {

		@Override
		protected void onPreExecute() {
			arrayList.clear();
			progressDialog = new MyProgressDialog(CallListActivity.this);
			progressDialog.show();
			// progressDialog.setCancelable(true);
		}

		@Override
		protected String[] doInBackground(Void... params) {
			generateCallList();
			return null;
		}

		@Override
		protected void onPostExecute(String[] result) {
			super.onPostExecute(result);
			adapter = new CallListAdapter(CallListActivity.this, arrayList);
			listContact.setAdapter(adapter);
			adapter.notifyDataSetChanged();
			progressDialog.dismiss();
		}
	}

	public static String getContactName(Context context, String phoneNumber) {
		ContentResolver cr = context.getContentResolver();
		Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI,
				Uri.encode(phoneNumber));
		Log.d("callTracker", "Found uri of contact by new method: " + uri);
		Cursor cursor = cr.query(uri,
				new String[] { PhoneLookup.DISPLAY_NAME }, null, null, null);
		if (cursor == null) {
			return null;
		}
		String contactName = null;
		if (cursor.moveToFirst()) {
			contactName = cursor.getString(cursor
					.getColumnIndex(PhoneLookup.DISPLAY_NAME));
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}

		return contactName;
	}

	private Uri getContactUri(Context context, String number) {

		String name = null;
		String contactId = null;
		InputStream input = null;

		// define the columns I want the query to return
		String[] projection = new String[] {
				ContactsContract.PhoneLookup.DISPLAY_NAME,
				ContactsContract.PhoneLookup._ID };

		// encode the phone number and build the filter URI
		Uri contactUri = Uri.withAppendedPath(
				ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
				Uri.encode(number));

		// query time
		Cursor cursor = context.getContentResolver().query(contactUri,
				projection, null, null, null);

		if (cursor.moveToFirst()) {

			// Get values from contacts database:
			contactId = cursor.getString(cursor
					.getColumnIndex(ContactsContract.PhoneLookup._ID));
			name = cursor.getString(cursor
					.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));

			// Get photo of contactId as input stream:
			Uri uri = ContentUris.withAppendedId(
					ContactsContract.Contacts.CONTENT_URI,
					Long.parseLong(contactId));
			input = ContactsContract.Contacts.openContactPhotoInputStream(
					context.getContentResolver(), uri);

			Log.v("CallTracker", "Started listing: Contact Found @ " + number);
			Log.v("CallTracker", "Started listing: Contact name  = " + name);
			Log.v("CallTracker", "Started listing: Contact id    = "
					+ contactId);

		} else {

			Log.v("CallTracker", "Started listing: Contact Not Found @ "
					+ number);
			cursor.close();
			return null; // contact not found

		}

		// Only continue if we found a valid contact photo:
		if (input == null) {
			Log.v("CallTracker", "Started listing: No photo found, id = "
					+ contactId + " name = " + name);
			Uri uri = Uri.withAppendedPath(
					ContactsContract.Contacts.CONTENT_URI,
					String.valueOf(contactId));
			Log.v("CallTracker", "found uri : " + uri + " for contact id : "
					+ contactId);
			cursor.close();
			return uri;
			// no photo
		} else {
			Log.v("CallTracker", "Started listing: Photo found, id = "
					+ contactId + " name = " + name);
		}
		Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI,
				String.valueOf(contactId));
		Log.v("CallTracker", "found uri : " + uri + " for contact id : "
				+ contactId);
		cursor.close();
		return uri;
	}
}
