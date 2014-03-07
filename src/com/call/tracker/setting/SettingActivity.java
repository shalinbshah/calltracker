package com.call.tracker.setting;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.call.tracker.BaseActivity;
import com.call.tracker.R;
import com.call.tracker.database.DBAdapter;
import com.devspark.appmsg.AppMsg;

public class SettingActivity extends BaseActivity implements OnClickListener {
	private LinearLayout layoutSync, layoutCSV, layoutSalesGoal,
			layoutFeedbask;
	private CheckBox mission_setting_switch;
	public static Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout_setting);
		mContext = getApplicationContext();
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
		mission_setting_switch = (CheckBox) findViewById(R.id.mission_setting_switch);
		if (getPref(SalesMissionActivity.IS_GOAL_SET, false)) {
			mission_setting_switch.setSelected(true);
			mission_setting_switch.setEnabled(true);
			mission_setting_switch.setChecked(true);

		} else {
			mission_setting_switch.setChecked(false);
			mission_setting_switch.setSelected(false);
		}
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

	public void getVCF() {
		String path = null;
		FileOutputStream mFileOutputStream = null;
		DBAdapter adapter = new DBAdapter(mContext);
		ArrayList<String> contactsIDs = adapter.getContactsIDs();
		final String vfile = new SimpleDateFormat("yyyy-MM-dd").format(Calendar
				.getInstance().getTime()) + ".vcf";
		Cursor phones = mContext.getContentResolver().query(
				ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
		phones.moveToFirst();

		for (int i = 0; i < phones.getCount(); i++) {

			String lookupKey = phones.getString(phones
					.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
			Uri uri = Uri.withAppendedPath(
					ContactsContract.Contacts.CONTENT_VCARD_URI, lookupKey);
			String id = phones.getString(phones
					.getColumnIndex(ContactsContract.Contacts._ID));

			if (contactsIDs.contains(id)) {
				AssetFileDescriptor fd;
				try {
					fd = mContext.getContentResolver().openAssetFileDescriptor(
							uri, "r");
					FileInputStream fis = fd.createInputStream();
					byte[] buf = new byte[(int) fd.getDeclaredLength()];
					fis.read(buf);
					String VCard = new String(buf);
					path = Environment.getExternalStorageDirectory().toString()
							+ File.separator + vfile;
					mFileOutputStream = new FileOutputStream(path, true);
					mFileOutputStream.write(VCard.toString().getBytes());

					Log.d("Vcard", VCard);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
			phones.moveToNext();

		}

		String fileName = path;
		if (fileName != null) {
			Intent shareIntent = new Intent(Intent.ACTION_SEND);
			shareIntent.setType("text/vcard");
			shareIntent.putExtra(Intent.EXTRA_STREAM,
					Uri.fromFile(new File(fileName)));
			AppMsg appMsg = AppMsg.makeText(this,
					"Some Problem Exporting Contacts", AppMsg.STYLE_INFO);
			appMsg.setLayoutGravity(Gravity.BOTTOM);
			appMsg.show();
			startActivity(Intent.createChooser(shareIntent, "Share File"));

		} else {
			AppMsg appMsg = AppMsg.makeText(this,
					"Some Problem Exporting Contacts", AppMsg.STYLE_ALERT);
			appMsg.setLayoutGravity(Gravity.BOTTOM);
			appMsg.show();
		}
	}

	private void openSingleChoicedialog() {
		getVCF();
		// CharSequence[] items = { getStringFromXml(R.string.send_to_dropbox),
		// getStringFromXml(R.string.send_to_evernote),
		// getStringFromXml(R.string.save_as) };
		// // TODO Auto-generated method stub
		// new AlertDialog.Builder(this)
		// .setSingleChoiceItems(items, -1,
		// new DialogInterface.OnClickListener() {
		// @Override
		// public void onClick(DialogInterface dialog,
		// int whichButton) {
		// dialog.dismiss();
		// if (whichButton == 0) {
		// openDropBox();
		// } else if (whichButton == 1) {
		// openEvernote();
		// } else if (whichButton == 2) {
		// openSaveAs();
		// } else {
		//
		// }
		// }
		// }).setTitle(R.string.export_contacts_to_csv).show();
	}

	protected void openSaveAs() {
		AppMsg appMsg = AppMsg.makeText(this, "Coming Soon", AppMsg.STYLE_INFO);
		appMsg.setLayoutGravity(Gravity.BOTTOM);
		appMsg.show();
	}

	protected void openEvernote() {
		AppMsg appMsg = AppMsg
				.makeText(this, "Coming Soon", AppMsg.STYLE_ALERT);
		appMsg.setLayoutGravity(Gravity.BOTTOM);
		appMsg.show();
	}

	protected void openDropBox() {
		AppMsg appMsg = AppMsg.makeText(this, "Coming Soon", AppMsg.STYLE_INFO);
		appMsg.setLayoutGravity(Gravity.BOTTOM);
		appMsg.show();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// if (getPref(SalesMissionActivity.IS_GOAL_SET, false)) {
		// mission_setting_switch
		// .setBackgroundResource(R.drawable.setting_switch_on);
		// }
	}
}
