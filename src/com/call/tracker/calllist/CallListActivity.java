package com.call.tracker.calllist;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
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

				CallListModel callListModel = new CallListModel();
				callListModel = (CallListModel) adapterView.getAdapter()
						.getItem(arg2);

				Intent intent = new Intent(getApplicationContext(),
						DetailActivity.class);
				intent.putExtra("callData", callListModel);
				startActivity(intent);

			}
		});
		adapter = new CallListAdapter(this, arrayList);
		listContact.setAdapter(adapter);
		new GetCallListFromWebService().execute();
		editTextSearch = (EditText) findViewById(R.id.editTextSearch);
		editTextSearch.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3) {
				int currentVal = Integer.valueOf(preferences.getString(
						CURRENT_LIST, "-1"));
				generateCallList(cs.toString(), currentVal);
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
	private void generateCallList(String a, int s) {

		Cursor managedCursor = getContentResolver().query(
				CallLog.Calls.CONTENT_URI, null, null, null, null);
		int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
		int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
		int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
		int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
		int nameVal = managedCursor.getColumnIndex(CallLog.Calls.CACHED_NAME);
		int id = managedCursor.getColumnIndex(CallLog.Calls._ID);

		arrayList.clear();
		while (managedCursor.moveToNext()) {
			String phNumber = managedCursor.getString(number);
			String callType = managedCursor.getString(type);
			String timeMillis = managedCursor.getString(date);
			String callDuration = managedCursor.getString(duration);
			String name = managedCursor.getString(nameVal);
			String contactId = managedCursor.getString(id);
			if (name == null)
				name = "";
			CallListModel callListModel = new CallListModel();
			callListModel.setId(contactId);
			callListModel.setName(name);
			Date d = new Date(Long.parseLong(timeMillis));
			SimpleDateFormat formatter = new SimpleDateFormat(
					"dd/MM/yyyy HH:mm:ss");
			callListModel.setDate(formatter.format(d));
			callListModel.setDuration(callDuration);
			callListModel.setNumber(phNumber);
			callListModel.setType(callType);
			callListModel.setUri(getContactUriWithPhoneNumber(phNumber));
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
		}

		@Override
		protected String[] doInBackground(Void... params) {
			generateCallList("", -1);
			return null;
		}

		@Override
		protected void onPostExecute(String[] result) {
			super.onPostExecute(result);
			adapter.notifyDataSetChanged();
			progressDialog.dismiss();
		}

	}

	private Uri getContactUriWithPhoneNumber(String pNumber) {
		ContentResolver cr = this.getContentResolver();
		Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
				null, null, null);
		Log.d("callTracker", "Finding uri of contact : " + pNumber);
		if (cur.getCount() > 0) {
			while (cur.moveToNext()) {
				String id = cur.getString(cur
						.getColumnIndex(ContactsContract.Contacts._ID));
				if (Integer
						.parseInt(cur.getString(cur
								.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
					Cursor pCur = cr.query(
							ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
							null,
							ContactsContract.CommonDataKinds.Phone.CONTACT_ID
									+ " = ?", new String[] { id }, null);
					while (pCur.moveToNext()) {
						String phoneNo = pCur
								.getString(pCur
										.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
						if (phoneNo.toUpperCase().trim()
								.contains(pNumber.toUpperCase().trim())) {
							Uri my_contact_Uri = Uri.withAppendedPath(
									ContactsContract.Contacts.CONTENT_URI,
									String.valueOf(id));
							if (null != my_contact_Uri) {
								cur.close();
								pCur.close();
							}
							return my_contact_Uri;
						}
					}
					pCur.close();
				}
			}
			cur.close();
		}
		return null;
	}
}
