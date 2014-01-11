package com.call.tracker.setting;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.call.tracker.BaseActivity;
import com.call.tracker.R;

public class SettingActivity extends BaseActivity implements OnClickListener {
	private LinearLayout layoutSync, layoutCSV, layoutSalesGoal,
			layoutFeedbask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout_setting);

		initControl();
	}

	private void initControl() {
		// TODO Auto-generated method stub
		layoutSync = (LinearLayout) findViewById(R.id.layoutSync);
		layoutSync.setOnClickListener(this);

		layoutCSV = (LinearLayout) findViewById(R.id.layoutCSV);
		layoutCSV.setOnClickListener(this);

		layoutSalesGoal = (LinearLayout) findViewById(R.id.layoutSalesGoals);
		layoutSalesGoal.setOnClickListener(this);

		layoutFeedbask = (LinearLayout) findViewById(R.id.layoutFeedback);
		layoutFeedbask.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.layoutSync:
			startActivity(new Intent(getApplicationContext(),
					SyncActivity.class));
			break;
		case R.id.layoutCSV:
			openSingleChoicedialog();
			break;
		case R.id.layoutSalesGoals:
			if (preferences.getString(IS_SALES_GOALS, "false").equals("false"))
				startActivity(new Intent(getApplicationContext(),
						SalesGoalsActivity.class));
			else
				startActivity(new Intent(getApplicationContext(),
						SalesMissionActivity.class));
			break;
		case R.id.layoutFeedback:
			openSingleChoicedialogFeedback();
			break;

		default:
			break;
		}
	}

	private void openSingleChoicedialogFeedback() {
		CharSequence[] itemsData = { getStringFromXml(R.string.email_us),
				getStringFromXml(R.string.visit),
				getStringFromXml(R.string.post_review) };
		// TODO Auto-generated method stub
		new AlertDialog.Builder(this)
				.setTitle(R.string.select_choice)
				.setSingleChoiceItems(itemsData, -1,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								dialog.dismiss();
								if (whichButton == 0) {
									callEmail();
								} else if (whichButton == 1) {
									visitFb();
								} else {
									postReview();
								}
							}
						}).show();
	}

	protected void postReview() {
		startActivity(new Intent(
				Intent.ACTION_VIEW,
				Uri.parse("https://play.google.com/store/apps/details?id=com.call.tracker")));
	}

	protected void visitFb() {
		startActivity(new Intent(Intent.ACTION_VIEW,
				Uri.parse("https://www.facebook.com/654439111242611")));
	}

	protected void callEmail() {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_EMAIL,
				new String[] { "myagentcalljournal@gmail.com" });
		intent.putExtra(Intent.EXTRA_SUBJECT, "");
		intent.putExtra(Intent.EXTRA_TEXT, "");
		startActivity(Intent.createChooser(intent, "Send Email"));

	}

	private void openSingleChoicedialog() {
		CharSequence[] items = { getStringFromXml(R.string.send_to_dropbox),
				getStringFromXml(R.string.send_to_evernote),
				getStringFromXml(R.string.save_as) };
		// TODO Auto-generated method stub
		new AlertDialog.Builder(this)
				.setSingleChoiceItems(items, -1,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int whichButton) {
								dialog.dismiss();
								if (whichButton == 0) {
									openDropBox();
								} else if (whichButton == 1) {
									openEvernote();
								} else if (whichButton == 2) {
									openSaveAs();
								} else {

								}
							}
						}).setTitle(R.string.export_contacts_to_csv).show();
	}

	protected void openSaveAs() {
		// TODO Auto-generated method stub

	}

	protected void openEvernote() {
		// TODO Auto-generated method stub

	}

	protected void openDropBox() {
		// TODO Auto-generated method stub

	}
}
