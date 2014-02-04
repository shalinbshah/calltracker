package com.call.tracker.reports;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import com.call.tracker.BaseActivity;
import com.call.tracker.R;
import com.devspark.appmsg.AppMsg;

public class ReportsActivity0 extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_reports_activity0);
	}

	public void callReports(View v) {
		// startActivity(new Intent(getApplicationContext(),
		// ReportsActivity1.class));
		AppMsg appMsg = AppMsg.makeText(ReportsActivity0.this, "Coming Soon",
				AppMsg.STYLE_ALERT);
		appMsg.setLayoutGravity(Gravity.BOTTOM);
		appMsg.show();
	}

	public void callStatisctics(View v) {
		startActivity(new Intent(getApplicationContext(),
				ReportsActivity1.class));
	}
}
