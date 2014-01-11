package com.call.tracker.listmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import com.call.tracker.BaseActivity;
import com.call.tracker.R;

public class ListmanagerActivity extends BaseActivity {
	private CheckBox checkdontshow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout_list_manager);

		intiControl();
	}

	private void intiControl() {
		// TODO Auto-generated method stub
		checkdontshow = (CheckBox) findViewById(R.id.checkdontshow);
	}

	public void callGot(View view) {
		updatePref(IS_LIST_MANAGER, String.valueOf(checkdontshow.isChecked()));
		startActivity(new Intent(getApplicationContext(),
				ListManagerDetails.class));
		finish();
	}
}
