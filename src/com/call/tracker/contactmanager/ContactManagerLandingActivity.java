package com.call.tracker.contactmanager;

import java.io.IOException;
import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.call.tracker.BaseActivity;
import com.call.tracker.R;
import com.call.tracker.adapter.ContactManagerListAdapter;
import com.call.tracker.database.DBAdapter;
import com.call.tracker.model.ContactModel;

public class ContactManagerLandingActivity extends BaseActivity {
	protected static final String TAG = "CallListActivity";
	private ArrayList<ContactModel> arrayList = new ArrayList<ContactModel>();
	private ContactManagerListAdapter adapter;
	private ListView listContact;
	private DBAdapter dbAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_contact_manager);
		initControl();
	}

	private void initControl() {
		openDB();
		getdataFromDb();
		adapter = new ContactManagerListAdapter(this, arrayList);
		listContact = (ListView) findViewById(R.id.listContactManagerContacts);
		listContact.setAdapter(adapter);
	}

	private void getdataFromDb() {
		// TODO Auto-generated method stub
		dbAdapter.openDataBase();
		arrayList = dbAdapter.getContacts();
		dbAdapter.close();

	}

	public void callAdd(View view) {
		startActivity(new Intent(getApplicationContext(),
				ContactManagerMainActivity.class));
	}

	public void openDB() {
		// dbAdapter =

		dbAdapter = DBAdapter.getDBAdapterInstance(this);
		try {
			dbAdapter.createDataBase();
		} catch (IOException e) {
		}
	}

	@Override
	protected void onResume() {
		initControl();
		super.onResume();
	}

	public void callContactList(View view) {
		//
		// int currentVal = Integer.valueOf(preferences.getString(CURRENT_LIST,
		// "0"));
		// AlertDialog.Builder builderListChoiceDialog = new
		// AlertDialog.Builder(
		// this);
		// final String[] strings = new String[mListManagerModels.size() + 1];
		// for (int i = 0; i < mListManagerModels.size(); i++) {
		// if (i == 0)
		// strings[0] = "All contacts";
		// strings[i + 1] = mListManagerModels.get(i).getName();
		// }
		//
		// // final CharSequence[] array = { "All contacts", "FSBO list",
		// // "Open House Guest", "Expired List", "Other" };
		// builderListChoiceDialog.setTitle(R.string.select_call_list)
		// .setSingleChoiceItems(strings, currentVal,
		// new DialogInterface.OnClickListener() {
		//
		// public void onClick(DialogInterface dialog,
		// int position) {
		// updatePref(CURRENT_LIST,
		// String.valueOf(position));
		// dialog.dismiss();
		// int val = position;
		// if (position == 0) {
		// val = -1;
		// } else
		// val = Integer.valueOf(mListManagerModels
		// .get(position - 1).getId());
		// // generate call list if this is from call list
		// // otherwise only show contacts although if last
		// // and next call info are not available
		// generateContactsList("", val);
		// butContactList.setText(strings[position]);
		// }
		// });
		// AlertDialog listChoiceDialog = builderListChoiceDialog.create();
		// listChoiceDialog.setCanceledOnTouchOutside(true);
		// listChoiceDialog.show();
	}

}
