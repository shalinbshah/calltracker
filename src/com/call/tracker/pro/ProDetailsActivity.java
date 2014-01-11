package com.call.tracker.pro;

import android.os.Bundle;
import android.view.View;

import com.call.tracker.BaseActivity;
import com.call.tracker.R;

public class ProDetailsActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout_pro_details);

		initControl();
	}

	private void initControl() {
		// TODO Auto-generated method stub

	}

	public void callGoPro(View view) {
		alertBox("Upgrade Pro Version . . . Coming Soon ");
	}
}
