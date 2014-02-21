package com.call.tracker.reports;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.call.tracker.BaseActivity;
import com.call.tracker.R;

public class ReportsActivity0 extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_reports_activity0);
	}

	public void callStatistics(View v) {
		// startActivity(new Intent(getApplicationContext(),
		// ReportsActivity1.class));
		// AppMsg appMsg = AppMsg.makeText(ReportsActivity0.this, "Coming Soon",
		// AppMsg.STYLE_ALERT);
		// appMsg.setLayoutGravity(Gravity.BOTTOM);
		// appMsg.show();
		startActivity(new Intent(getApplicationContext(),
				ReportsActivity2.class));

	}

	public void callReports(View v) {
		startActivity(new Intent(getApplicationContext(),
				ReportsActivity1.class));
	}
}
