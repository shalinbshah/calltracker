package com.call.tracker;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.bugsense.trace.BugSenseHandler;

public class SplashActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BugSenseHandler.startSession(this);
		setContentView(R.layout.layout_splash);
		initControl();
	}

	public void initControl() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Thread.sleep(2000);
					handler.sendEmptyMessage(0);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			startUserActivity();
		}

		private void startUserActivity() {
			// TODO Auto-generated method stub

			String isGuide = preferences.getString(IS_GUIDE, "false");
			if (isGuide.equals("false")) {
				startActivity(new Intent(getApplicationContext(),
						GuideActivity.class));
			} else {
				startActivity(new Intent(getApplicationContext(),
						HomeActivity.class));
			}
			finish();
		}
	};

}
