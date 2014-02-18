package com.call.tracker.contactmanager;

import java.io.IOException;
import java.util.ArrayList;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.call.tracker.BaseActivity;
import com.call.tracker.R;
import com.call.tracker.adapter.ContactManagerListAdapter;
import com.call.tracker.adapter.MyProgressDialog;
import com.call.tracker.calllist.ContactFollowUpDetailsActivity;
import com.call.tracker.calllist.SelectGroupPopUp;
import com.call.tracker.database.DBAdapter;
import com.call.tracker.model.ContactModel;

public class ContactManagerLandingActivity extends BaseActivity {
	protected static final String TAG = "CallTracker";
	private ArrayList<ContactModel> arrayList = new ArrayList<ContactModel>();
	private ContactManagerListAdapter adapter;
	private ListView listContact;
	private DBAdapter dbAdapter;
	private MyProgressDialog progressDialog;
	final int SELECT_GROUP_REQUEST_CODE = 1001;
	String selectedGroupId = "";
	ArrayList<String> addedContactsIDs = new ArrayList<String>();
	Button butContactList;
	private boolean onResume = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_contact_manager);
		initControl();
	}

	private void initControl() {
		butContactList = (Button) findViewById(R.id.butContactList);
		adapter = new ContactManagerListAdapter(this, arrayList);
		progressDialog = new MyProgressDialog(
				ContactManagerLandingActivity.this);
		listContact = (ListView) findViewById(R.id.listContactManagerContacts);
		listContact.setAdapter(adapter);
		listContact.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int arg2, long arg3) {

				Intent intent = new Intent(getApplicationContext(),
						ContactFollowUpDetailsActivity.class);
				// intent.putExtra("CallListModel", callListModel);
				intent.putExtra("contact_name", arrayList.get(arg2).getName());
				String contactID = arrayList.get(arg2).getContactId();
				Uri uri = Uri.withAppendedPath(
						ContactsContract.Contacts.CONTENT_URI,
						String.valueOf(contactID));
				intent.putExtra("contact_uri", uri);
				intent.putExtra("contact_number", arrayList.get(arg2)
						.getNumber1());
				startActivity(intent);
			}
		});
	}

	private void getdataFromDb() {
		arrayList.clear();
		dbAdapter = new DBAdapter(getApplicationContext());
		try {
			dbAdapter.createDataBase();
			dbAdapter.openDataBase();
			if (selectedGroupId.length() > 0)
				arrayList.addAll(dbAdapter.getContactsOfGroup(selectedGroupId));
			else
				arrayList.addAll(dbAdapter.getContacts());
			dbAdapter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void callAdd(View view) {
		onResume = true;
		startActivity(new Intent(getApplicationContext(),
				AddNewContactActivity.class));
	}

	public class GetContactsFromDB extends AsyncTask<Void, Void, String[]> {

		@Override
		protected void onPreExecute() {
			arrayList.clear();
			adapter.notifyDataSetChanged();
			progressDialog.show();
		}

		@Override
		protected String[] doInBackground(Void... params) {
			getdataFromDb();
			return null;
		}

		@Override
		protected void onPostExecute(String[] result) {
			super.onPostExecute(result);
			adapter.notifyDataSetChanged();
			progressDialog.dismiss();
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		onResume = false;
		if (resultCode == RESULT_OK) {
			if (requestCode == SELECT_GROUP_REQUEST_CODE) {
				selectedGroupId = TempHolder.selectedGroupId;
				butContactList.setText(TempHolder.selectedGroupName);
				new GetContactsFromDB().execute();
			}
		} else {
			selectedGroupId = "";
			butContactList.setText("All Contacts");
			new GetContactsFromDB().execute();
		}
	}

	public void callContactList(View v) {
		Intent intent = new Intent(getApplicationContext(),
				SelectGroupPopUp.class);
		startActivityForResult(intent, SELECT_GROUP_REQUEST_CODE);
	}

	@Override
	protected void onResume() {
		if (onResume)
			new GetContactsFromDB().execute();
		super.onResume();
	}

}
