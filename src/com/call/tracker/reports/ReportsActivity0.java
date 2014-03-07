package com.call.tracker.reports;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import com.call.tracker.BaseActivity;
import com.call.tracker.R;
import com.call.tracker.setting.SalesMissionActivity;
import com.devspark.appmsg.AppMsg;

public class ReportsActivity0 extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_reports_activity0);
	}

	public void callStatistics(View v) {
		if (getPref(SalesMissionActivity.IS_GOAL_SET, false)) {
			startActivity(new Intent(getApplicationContext(),
					ReportsActivity2.class));
		} else {
			AppMsg appMsg = AppMsg.makeText(ReportsActivity0.this,
					"Please set goals enabled from settings",
					AppMsg.STYLE_ALERT);
			appMsg.setLayoutGravity(Gravity.BOTTOM);
			appMsg.show();
		}

	}

	public void callReports(View v) {
		if (getPref(SalesMissionActivity.IS_GOAL_SET, false)) {
			startActivity(new Intent(getApplicationContext(),
					ReportsActivity1.class));
		} else {
			AppMsg appMsg = AppMsg.makeText(ReportsActivity0.this,
					"Please set goals enabled from settings",
					AppMsg.STYLE_ALERT);
			appMsg.setLayoutGravity(Gravity.BOTTOM);
			appMsg.show();
		}
	}
}
