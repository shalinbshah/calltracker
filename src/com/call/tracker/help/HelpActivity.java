package com.call.tracker.help;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.call.tracker.BaseActivity;
import com.call.tracker.R;

public class HelpActivity extends BaseActivity implements OnClickListener {

	TextView contactTextView, postTextView, turnTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout_help);

		initControl();
	}

	private void initControl() {

		contactTextView = (TextView) findViewById(R.id.textContact);
		contactTextView.setOnClickListener(this);

		postTextView = (TextView) findViewById(R.id.textPost);
		postTextView.setOnClickListener(this);

		turnTextView = (TextView) findViewById(R.id.textTurn);
		turnTextView.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.textContact:
			callEmail();
			break;
		case R.id.textPost:
			startActivity(new Intent(Intent.ACTION_VIEW,
					Uri.parse("https://www.facebook.com/654439111242611")));
			break;
		case R.id.textTurn:
			alertBox("Turn On all check boxs");
			break;
		default:
			break;
		}
	}

	private void callEmail() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_EMAIL,
				new String[] { "myagentcalljournal@gmail.com" });
		intent.putExtra(Intent.EXTRA_SUBJECT, "");
		intent.putExtra(Intent.EXTRA_TEXT, "");
		startActivity(Intent.createChooser(intent, "Send Email"));
	}
}
