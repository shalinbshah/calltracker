package com.call.tracker.contactmanager;

import java.util.ArrayList;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.call.tracker.BaseActivity;
import com.call.tracker.R;
import com.call.tracker.adapter.ContactManagerListAdapter;
import com.call.tracker.adapter.MyProgressDialog;
import com.call.tracker.calllist.ContactFollowUpDetailsActivity;
import com.call.tracker.database.DBAdapter;
import com.call.tracker.model.ContactModel;

public class ContactManagerLandingActivity extends BaseActivity {
	protected static final String TAG = "CallListActivity";
	private ArrayList<ContactModel> arrayList = new ArrayList<ContactModel>();
	private ContactManagerListAdapter adapter;
	private ListView listContact;
	private DBAdapter dbAdapter;
	private MyProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_contact_manager);
		initControl();
	}

	private void initControl() {
		adapter = new ContactManagerListAdapter(this, arrayList);
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
		dbAdapter = new DBAdapter(getApplicationContext());
		dbAdapter.openDataBase();
		arrayList.addAll(dbAdapter.getContacts());
		dbAdapter.close();
	}

	public void callAdd(View view) {
		startActivity(new Intent(getApplicationContext(),
				AddNewContactActivity.class));
	}

	public class GetContactsFromDB extends AsyncTask<Void, Void, String[]> {

		@Override
		protected void onPreExecute() {
			arrayList.clear();
			progressDialog = new MyProgressDialog(
					ContactManagerLandingActivity.this);
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
	protected void onResume() {
		new GetContactsFromDB().execute();
		super.onResume();
	}

}
