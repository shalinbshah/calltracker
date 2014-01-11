package com.call.tracker;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.call.tracker.calllist.CallListActivity;
import com.call.tracker.contactmanager.ContactGuideActivity;
import com.call.tracker.help.HelpActivity;
import com.call.tracker.listmanager.ListManagerDetails;
import com.call.tracker.listmanager.ListmanagerActivity;
import com.call.tracker.pro.ProActivity;
import com.call.tracker.setting.SettingActivity;
import com.call.tracker.voicenotes.VoiceListActivity;

public class HomeActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout_home);

	}

	@Override
	protected void onResume() {
		updatePref(FILEPATH, "");
		updatePref(VOICE_TIME, "");
		updatePref(URGENT, "");
		super.onResume();
	}

	public void callCallList(View view) {
		startActivity(new Intent(getApplicationContext(),
				CallListActivity.class));
	}

	public void callContact(View view) {
		if (preferences.getString(IS_CONTACT_MANAGER, "false").equals("false")) {
			startActivity(new Intent(getApplicationContext(),
					ContactGuideActivity.class));
		} else {
			Intent intent = new Intent(getApplicationContext(),
					CallListActivity.class);
			intent.putExtra("key", 1);
			startActivity(intent);
		}
	}

	public void callListManage(View view) {
		if (preferences.getString(IS_LIST_MANAGER, "false").equals("false"))
			startActivity(new Intent(getApplicationContext(),
					ListmanagerActivity.class));
		else
			startActivity(new Intent(getApplicationContext(),
					ListManagerDetails.class));
	}

	public void callReport(View view) {
		preToast("Coming Soon Report");
	}

	public void callVoice(View view) {
		startActivity(new Intent(getApplicationContext(),
				VoiceListActivity.class));
	}

	public void callSetting(View view) {
		startActivity(new Intent(getApplicationContext(), SettingActivity.class));
	}

	public void callLikeus(View view) {
		startActivity(new Intent(Intent.ACTION_VIEW,
				Uri.parse("https://www.facebook.com/654439111242611")));
	}

	public void callBecomePro(View view) {
		startActivity(new Intent(getApplicationContext(), ProActivity.class));
	}

	public void callHelp(View view) {
		startActivity(new Intent(getApplicationContext(), HelpActivity.class));
	}
}
