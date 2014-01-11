package com.call.tracker.pro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.call.tracker.BaseActivity;
import com.call.tracker.R;

public class ProActivity extends BaseActivity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout_pro);

		initControl();
	}

	private void initControl() {
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.textContact:
			break;
		default:
			break;
		}
	}

	public void callContinue(View view) {
		startActivity(new Intent(getApplicationContext(), ProDetailsActivity.class));
		
		
	}

}
