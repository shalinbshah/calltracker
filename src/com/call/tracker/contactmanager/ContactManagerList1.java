package com.call.tracker.contactmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import com.call.tracker.BaseActivity;
import com.call.tracker.R;

public class ContactManagerList1 extends BaseActivity {

	CheckBox checkdontshow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout_con_list1);

		checkdontshow = (CheckBox) findViewById(R.id.checkdontshow);

	}

	public void callGot(View v) {
		updatePref(IS_CONTACT_LIST1, String.valueOf(checkdontshow.isSelected()));

		startActivity(new Intent(getApplicationContext(),
				ContactManagerListMain.class));
	}
}
