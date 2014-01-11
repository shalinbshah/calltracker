package com.call.tracker.calllist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.call.tracker.BaseActivity;
import com.call.tracker.R;
import com.call.tracker.model.CallListModel;

public class VoiceCallStopActivity extends BaseActivity {
	private TextView textViewCallerName;
	private ImageView imgViewRecord;
	private CallListModel callUserModel;
	private ListView callVoiceListView;
	private Chronometer chronometerTimer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			setContentView(R.layout.layout_voice_call);
			callUserModel = (CallListModel) bundle.getSerializable("callData");
			initControl();
		} else {
			alertBox("No Data Found");
		}
	}

	public void callHome(View v) {
		gotoHome();
	}

	private void initControl() {
		// TODO Auto-generated method stub
		textViewCallerName = (TextView) findViewById(R.id.textViewCallerName);
		imgViewRecord = (ImageView) findViewById(R.id.imgViewRecord);
		TextView textViewHistory = (TextView) findViewById(R.id.textViewHistory);
		textViewHistory.setVisibility(View.GONE);

		imgViewRecord.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent intent = new Intent(getApplicationContext(),
						VoiceCallStopActivity.class);
				intent.putExtra("callData", callUserModel);
				startActivity(intent);
				chronometerTimer.stop();
				finish();
			}
		});

		callVoiceListView = (ListView) findViewById(R.id.listVoice);
		String name = callUserModel.getName();

		textViewCallerName.setText(name);
		callVoiceListView.setVisibility(View.GONE);

		chronometerTimer = (Chronometer) findViewById(R.id.chronometerTimer);
		chronometerTimer.start();

	}
}