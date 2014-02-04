package com.call.tracker.calllist;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Gravity;
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
import com.call.tracker.contactmanager.TempHolder;
import com.call.tracker.database.DBAdapter;
import com.call.tracker.model.CallListModel;
import com.call.tracker.model.ContactModel;
import com.devspark.appmsg.AppMsg;

public class CallListActivity extends BaseActivity {
	protected static final String TAG = "CallListActivity";
	private ListView listContact;
	private CallListAdapter adapter;
	private ArrayList<CallListModel> arrayList;
	private EditText editTextSearch;
	private Button butContactList;
	DBAdapter dbAdapter;
	final int SELECT_GROUP_REQUEST_CODE = 1001;
	String selectedGroupId = "";
	ArrayList<String> addedContactsIDs = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_call_list);
		dbAdapter = new DBAdapter(getApplicationContext());
		arrayList = new ArrayList<CallListModel>();
		adapter = new CallListAdapter(CallListActivity.this, arrayList);
		initControl();
		try {
			addedContactsIDs = dbAdapter.getContactsIDs();
			new GetCallListFromWebService().execute();

		} catch (Exception e) {
			AppMsg appMsg = AppMsg.makeText(CallListActivity.this,
					"Please add contacts from Contact Manager",
					AppMsg.STYLE_ALERT);
			appMsg.setLayoutGravity(Gravity.BOTTOM);
			appMsg.show();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == SELECT_GROUP_REQUEST_CODE) {
				selectedGroupId = TempHolder.selectedGroupId;
				butContactList.setText(TempHolder.selectedGroupName);
				new GetCallListFromWebService().execute();
			}
		} else {
			selectedGroupId = "";
			butContactList.setText("All Contacts");
			new GetCallListFromWebService().execute();
		}
	}

	public void callContactList(View v) {
		Intent intent = new Intent(getApplicationContext(),
				SelectGroupPopUp.class);
		startActivityForResult(intent, SELECT_GROUP_REQUEST_CODE);
	}

	private void initControl() {
		// updatePref(CURRENT_LIST, "0");

		butContactList = (Button) findViewById(R.id.butContactLista);
		butContactList.setText("All contacts");

		listContact = (ListView) findViewById(R.id.listContact);
		listContact.isTextFilterEnabled();
		listContact.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int arg2, long arg3) {

				Intent intent = new Intent(getApplicationContext(),
						ContactFollowUpDetailsActivity.class);
				// intent.putExtra("CallListModel", callListModel);
				intent.putExtra("contact_name", arrayList.get(arg2).getName());
				intent.putExtra("contact_uri", arrayList.get(arg2)
						.getContactUri());
				intent.putExtra("contact_number", arrayList.get(arg2)
						.getNumber());
				// intent.putExtra("contact", arrayList.get(arg2));
				startActivity(intent);
			}
		});
		editTextSearch = (EditText) findViewById(R.id.editTextSearch);
	}

	public void callButSearch(View v) {
		// arrayList.clear();
		// adapter.notifyDataSetChanged();
		new GetCallListFromWebService().execute(editTextSearch.getText()
				.toString());
	}

	@SuppressLint("SimpleDateFormat")
	private void generateCallList(String groupName, String filter) {
		ArrayList<ContactModel> contactsOfSelectedGroup = null;
		if (groupName.length() > 0) {
			dbAdapter.openDataBase();
			contactsOfSelectedGroup = dbAdapter.getContactsOfGroup(groupName);
			dbAdapter.close();
		}
		Cursor managedCursor;
		if (filter.length() > 0)
			managedCursor = getContentResolver().query(
					CallLog.Calls.CONTENT_URI, null,
					CallLog.Calls.CACHED_NAME + " LIKE ?",
					new String[] { "%" + filter + "%" },
					CallLog.Calls.DEFAULT_SORT_ORDER);
		else if (null != contactsOfSelectedGroup
				&& contactsOfSelectedGroup.size() > 0) {
			String selection = "";
			ArrayList<String> list = new ArrayList<String>();

			for (int i = 0; i < contactsOfSelectedGroup.size(); i++) {
				if (i != 0)
					selection = selection + " or " + CallLog.Calls.CACHED_NAME
							+ " LIKE ?";
				else
					selection = CallLog.Calls.CACHED_NAME + " LIKE ?";
				list.add(contactsOfSelectedGroup.get(i).getName());
				// selectionArgs[i] = contactsOfSelectedGroup.get(i).getName();
			}
			String[] selectionArgs = new String[list.size()];

			managedCursor = getContentResolver().query(
					CallLog.Calls.CONTENT_URI, null, selection,
					list.toArray(selectionArgs),
					CallLog.Calls.DEFAULT_SORT_ORDER);
		} else
			managedCursor = getContentResolver().query(
					CallLog.Calls.CONTENT_URI, null, null, null,
					CallLog.Calls.DEFAULT_SORT_ORDER);
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
			String contactID = getContactID(getApplicationContext(),
					phoneNumber);
			Uri uri = Uri.withAppendedPath(
					ContactsContract.Contacts.CONTENT_URI,
					String.valueOf(contactID));
			callListModel.setContactUri(uri);
			if (addedContactsIDs.contains(contactID))
				arrayList.add(callListModel);
		}
		managedCursor.close();
	}

	public class GetCallListFromWebService extends
			AsyncTask<String, Void, String[]> {
		MyProgressDialog progressDialog;

		@Override
		protected void onPreExecute() {
			arrayList.clear();
			adapter.notifyDataSetChanged();
			progressDialog = new MyProgressDialog(CallListActivity.this);
			progressDialog.show();
			progressDialog.setCancelable(true);
		}

		@Override
		protected String[] doInBackground(String... params) {
			if (null != params && params.length > 0)
				generateCallList(selectedGroupId, params[0]);
			else
				generateCallList(selectedGroupId, "");
			return null;
		}

		@Override
		protected void onPostExecute(String[] result) {
			progressDialog.dismiss();
			adapter.setCallListModels(arrayList);
			listContact.setAdapter(adapter);
			adapter.notifyDataSetChanged();
			super.onPostExecute(result);
		}
	}

	public static String getContactID(Context context, String number) {

		String name = null;
		String contactId = null;
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
			Log.v("CallTracker", "Contact Found @ " + number);
			Log.v("CallTracker", "Contact name  = " + name);
			Log.v("CallTracker", "Contact id    = " + contactId);
		} else {
			Log.v("CallTracker", "Contact Not Found @ " + number);
			cursor.close();
			return null;
		}

		cursor.close();
		return contactId;
	}
}
