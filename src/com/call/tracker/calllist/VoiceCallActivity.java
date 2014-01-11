package com.call.tracker.calllist;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.call.tracker.BaseActivity;
import com.call.tracker.R;
import com.call.tracker.model.CallListModel;

public class VoiceCallActivity extends BaseActivity {
	private CallListModel callUserModel;
	private TextView textViewCallerName;
	private ListView callVoiceListView;
	private ImageView imageView1;
	private Bitmap bitmap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			setContentView(R.layout.layout_voice_call);
			callUserModel = (CallListModel) bundle.getSerializable("callData");
			bitmap = (Bitmap) bundle.get("bitmap");
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
		imageView1 = (ImageView) findViewById(R.id.imgViewRecord);
		imageView1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent intent = new Intent(getApplicationContext(),
						VoiceCallStopActivity.class);
				intent.putExtra("callData", callUserModel);
				imageView1.setImageResource(R.drawable.media_stop);
				startActivity(intent);
			}
		});

		callVoiceListView = (ListView) findViewById(R.id.listVoice);
		String name = callUserModel.getName();

		textViewCallerName.setText(name);

		callVoiceListView.setAdapter(null);

		imageView1 = (ImageView) findViewById(R.id.mediaRecordStop);
		// callUserModel.get
		imageView1.setImageBitmap(bitmap);
	}
}
