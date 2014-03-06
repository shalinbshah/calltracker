package com.call.tracker.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import com.call.tracker.BaseActivity;
import com.call.tracker.R;

public class SalesGoalsActivity extends BaseActivity {
	private CheckBox checkdontshow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_salesgoals);
		checkdontshow = (CheckBox) findViewById(R.id.checkdontshow);

	}

	public void callNext(View v) {
		updatePref(IS_SALES_GOALS, String.valueOf(checkdontshow.isChecked()));
		startActivity(new Intent(getApplicationContext(),
				SalesMissionActivity.class));
		finish();
	}
}
