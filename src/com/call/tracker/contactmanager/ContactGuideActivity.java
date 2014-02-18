package com.call.tracker.contactmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import com.call.tracker.BaseActivity;
import com.call.tracker.R;

public class ContactGuideActivity extends BaseActivity {

	CheckBox checkdontshow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout_con_guide);

		checkdontshow = (CheckBox) findViewById(R.id.checkdontshow);

	}

	public void callGo(View v) {
		updatePref(IS_CONTACT_MANAGER,
				String.valueOf(checkdontshow.isChecked()));

		Intent intent = new Intent(getApplicationContext(),
				ContactManagerLandingActivity.class);
		startActivity(intent);
		finish();
	}
}
