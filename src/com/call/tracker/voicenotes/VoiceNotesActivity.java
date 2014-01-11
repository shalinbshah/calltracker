package com.call.tracker.voicenotes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.call.tracker.BaseActivity;
import com.call.tracker.R;

public class VoiceNotesActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout_voice);
	}


	public void callViewlist(View v) {
		startActivity(new Intent(getApplicationContext(),
				VoiceListActivity.class));
	}
}
