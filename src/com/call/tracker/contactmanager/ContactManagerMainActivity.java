package com.call.tracker.contactmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.call.tracker.BaseActivity;
import com.call.tracker.R;

public class ContactManagerMainActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout_contact_main);
	}

	public void callAddExisting(View v) {
		startActivity(new Intent(getApplicationContext(),
				ContactManagerList1.class));

	}

	public void callAddNew(View v) {
		Intent intent = new Intent(getApplicationContext(),
				NewContactActivity.class);
		intent.putExtra("Type", "contact");
		startActivity(intent);
	}
}
