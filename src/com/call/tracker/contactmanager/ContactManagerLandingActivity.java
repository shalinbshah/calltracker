package com.call.tracker.contactmanager;

import java.util.ArrayList;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.call.tracker.BaseActivity;
import com.call.tracker.R;
import com.call.tracker.adapter.ContactManagerListAdapter;
import com.call.tracker.adapter.MyProgressDialog;
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
