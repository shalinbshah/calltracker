package com.call.tracker.calllist;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
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
			if (addedContactsIDs == null || addedContactsIDs.size() == 0) {
				AppMsg appMsg = AppMsg.makeText(CallListActivity.this,
						"Please add contacts from Contact Manager",
						AppMsg.STYLE_ALERT);
				appMsg.setLayoutGravity(Gravity.BOTTOM);
				appMsg.show();
			}
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

	private void generateCallList(String selectedGroupID, String filter) {
		ArrayList<String> contactsOfSelectedGroup = new ArrayList<String>();
		arrayList.clear();
		if (selectedGroupID.length() > 0) {
			dbAdapter.openDataBase();
			contactsOfSelectedGroup.clear();
			contactsOfSelectedGroup = dbAdapter
					.getContactsNamesOfGroup(selectedGroupID);
			dbAdapter.close();
		}
		String query = "select * from tbl_calls_track";
		dbAdapter.openDataBase();

		Cursor managedCursor = dbAdapter.selectRecordsFromDB(query, null);

		arrayList.clear();
		while (managedCursor != null && managedCursor.moveToNext()) {

			int callDuration = managedCursor.getInt(managedCursor
					.getColumnIndex("call_duration_seconds"));
			String name = managedCursor.getString(managedCursor
					.getColumnIndex("contact_name"));
			String lastCallTime = managedCursor.getString(managedCursor
					.getColumnIndex("call_time"));
			if (name == null)
				name = "";
			CallListModel callListModel = new CallListModel();
			callListModel.setName(name);
			int a = managedCursor.getColumnIndex("call_duration_seconds");

			callListModel.setDate(lastCallTime);
			callListModel.setDuration(Integer.toString(callDuration));
			String contactID = managedCursor.getString(managedCursor
					.getColumnIndex("contact_id"));
			Uri uri = Uri.withAppendedPath(
					ContactsContract.Contacts.CONTENT_URI,
					String.valueOf(contactID));
			callListModel.setContactUri(uri);
			if (null == filter || filter.length() <= 0) {
				if (selectedGroupID.length() > 0) {
					if (contactsOfSelectedGroup.contains(name))
						arrayList.add(callListModel);
				} else
					arrayList.add(callListModel);
			} else if (name.contains(filter)) {
				if (selectedGroupID.length() > 0) {
					if (contactsOfSelectedGroup.contains(name))
						arrayList.add(callListModel);
				} else
					arrayList.add(callListModel);
			}
		}
		managedCursor.close();
		dbAdapter.close();

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
