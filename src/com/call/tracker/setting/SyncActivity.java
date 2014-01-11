package com.call.tracker.setting;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.call.tracker.BaseActivity;
import com.call.tracker.HomeActivity;
import com.call.tracker.R;
import com.call.tracker.adapter.SyncPagerAdapter;

public class SyncActivity extends BaseActivity {
	private ViewPager myPager;
	private int currntPos = 0;
	private ArrayList<String> arrayList = new ArrayList<String>();
	private Button butSync, butNotNow;
	private CheckBox checkdontshow;
	private SyncPagerAdapter pagerAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout_sync);
		initControl();
	}

	public void callNext(View v) {
		int currentVal = myPager.getCurrentItem();

		// if (currntPos == 2) {
		// updatePref(IS_GUIDE, String.valueOf(checkdontshow.isChecked()));
		// // startHomeActivity();
		// butSync.setText(getStringFromXml(R.string.next));
		// myPager.setCurrentItem(currentVal);
		// } else {
		// butSync.setText(getStringFromXml(R.string.next));
		// myPager.setCurrentItem(currentVal);
		// }
		//

		if (currntPos == 1) {
			butSync.setText(getStringFromXml(R.string.ohh));
			checkdontshow.setVisibility(View.VISIBLE);
			butNotNow.setVisibility(View.GONE);
			myPager.setCurrentItem(currentVal + 1);
			updatePref(IS_SYNC, String.valueOf(checkdontshow.isChecked()));
		} else if (currntPos == 2) {
			butSync.setText(getStringFromXml(R.string.yes_please));
			checkdontshow.setVisibility(View.INVISIBLE);
			butNotNow.setVisibility(View.VISIBLE);

			preToast("Coming Soon");
		} else {
			butSync.setText(getStringFromXml(R.string.not_me));
			checkdontshow.setVisibility(View.INVISIBLE);
			butNotNow.setVisibility(View.GONE);
			myPager.setCurrentItem(currentVal + 1);

		}
	}

	public void callNotNow(View v) {
		gotoHome();
	}

	private void initControl() {
		myPager = (ViewPager) findViewById(R.id.imageViewPager);

		butSync = (Button) findViewById(R.id.butSync);
		butSync.setText(R.string.not_me);

		butNotNow = (Button) findViewById(R.id.butNotNow);
		butNotNow.setVisibility(View.GONE);

		checkdontshow = (CheckBox) findViewById(R.id.checkdontshow);
		checkdontshow.setVisibility(View.INVISIBLE);

		arrayList.add(getStringFromXml(R.string.sync_text_1));
		arrayList.add(getStringFromXml(R.string.sync_text_2));
		arrayList.add(getStringFromXml(R.string.sync_text_3));

		pagerAdapter = new SyncPagerAdapter(this, arrayList);
		myPager.setAdapter(pagerAdapter);
		myPager.setOnPageChangeListener(new MyPageChangeListener());

		int val = 0;
		if (preferences.getString(IS_SYNC, "false").equals("false"))
			val = 0;
		else
			val = 2;
		myPager.setCurrentItem(val);
	}

	private class MyPageChangeListener extends
			ViewPager.SimpleOnPageChangeListener {
		@Override
		public void onPageSelected(int position) {
			currntPos = position;
			if (currntPos == 1) {
				butSync.setText(getStringFromXml(R.string.ohh));
				checkdontshow.setVisibility(View.VISIBLE);
				butNotNow.setVisibility(View.GONE);
			} else if (currntPos == 2) {
				butSync.setText(getStringFromXml(R.string.yes_please));
				checkdontshow.setVisibility(View.INVISIBLE);
				butNotNow.setVisibility(View.VISIBLE);
			} else {
				butSync.setText(getStringFromXml(R.string.not_me));
				checkdontshow.setVisibility(View.INVISIBLE);
				butNotNow.setVisibility(View.GONE);
			}
		}
	}

	public void startHomeActivity() {
		startActivity(new Intent(getApplicationContext(), HomeActivity.class));
		finish();
	}
}