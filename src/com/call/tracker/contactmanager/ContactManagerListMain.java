package com.call.tracker.contactmanager;

import java.io.IOException;
import java.util.ArrayList;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;

import com.call.tracker.BaseActivity;
import com.call.tracker.R;
import com.call.tracker.database.DBAdapter;
import com.call.tracker.model.ListManagerModel;
import com.call.tracker.voicenotes.AssignContactToVoiceNoteActivity;
import com.call.tracker.voicenotes.SelectContactOfGroupActivity;

public class ContactManagerListMain extends BaseActivity {
	private DBAdapter dbAdapter;
	private ArrayList<ListManagerModel> arrayList = new ArrayList<ListManagerModel>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout_con_listmain);

		openDB();
		getdataFromDb();
	}

	public void callChoose(View v) {
		Intent intent = new Intent(getApplicationContext(),
				AssignContactToVoiceNoteActivity.class);
		intent.putExtra("type", "contact");
		intent.putExtra("listdata", arrayList);
		startActivity(intent);
	}

	public void callJustPopulate(View v) {
		Intent intents = new Intent(getApplicationContext(),
				SelectContactOfGroupActivity.class);
		intents.putExtra("type", "contact");
		intents.putExtra("alldata", "yes");
		startActivity(intents);
	}

	private void getdataFromDb() {
		// TODO Auto-generated method stub
		dbAdapter.openDataBase();

		String query = "select * from list_manager where isVis =1";
		Cursor cursor = dbAdapter.selectRecordsFromDB(query, null);
		arrayList.clear();

		// int mCount = cursor.getCount();
		if (cursor.moveToFirst()) {
			do {
				ListManagerModel listManagerModel = new ListManagerModel();
				listManagerModel.setId(String.valueOf(cursor.getInt(0)));
				listManagerModel.setName(cursor.getString(1));
				listManagerModel.setIsVis(cursor.getString(2));
				listManagerModel.setIsCheck("1");
				arrayList.add(listManagerModel);
			} while (cursor.moveToNext());
		}

		dbAdapter.close();

	}

	public void openDB() {
		// dbAdapter =

		dbAdapter = DBAdapter.getDBAdapterInstance(this);
		try {
			dbAdapter.createDataBase();
		} catch (IOException e) {
		}
	}

}
