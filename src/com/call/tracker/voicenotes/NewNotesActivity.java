package com.call.tracker.voicenotes;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Chronometer;
import android.widget.ImageView;

import com.call.tracker.BaseActivity;
import com.call.tracker.R;

public class NewNotesActivity extends BaseActivity {
	private ImageView mediaRecordStop;
	private Chronometer chronometerTime;
	private AudioRecorder audioRecorder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout_newnotes);

		initControl();
	}

	private void initControl() {
		// TODO Auto-generated method stub

		updatePref(FILEPATH, "");
		updatePref(VOICE_TIME, "");
		updatePref(URGENT, "");
		mediaRecordStop = (ImageView) findViewById(R.id.mediaRecordStop);
		String timeString = Long.toString(System.currentTimeMillis() / 1000L);
		audioRecorder = new AudioRecorder("/" + "voice_" + timeString);
		mediaRecordStop.setTag(1);
		mediaRecordStop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (v.getTag().toString().equals("1")) {
					startRecording();
				} else {
					stopRecording();
				}
			}
		});

		chronometerTime = (Chronometer) findViewById(R.id.chronometerTime);

	}

	protected void stopRecording() {
		// TODO Auto-generated method stub
		chronometerTime.stop();
		String val = chronometerTime.getText().toString();
		audioRecorder.stop();
		Intent intent = new Intent(getApplicationContext(),
				VoiceDetailsActivity.class);
		intent.putExtra("time", val);
		intent.putExtra("filepath", audioRecorder.getPath());
		startActivity(intent);
		finish();
	}

	protected void startRecording() {
		chronometerTime.setBase(SystemClock.elapsedRealtime());
		chronometerTime.start();
		mediaRecordStop.setImageResource(R.drawable.media_stop);
		mediaRecordStop.setTag(2);
		audioRecorder.start();
	}
}
