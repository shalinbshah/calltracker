package com.call.tracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.call.tracker.interfaces.Constants;

public class BaseActivity extends Activity implements Constants {
	public SharedPreferences preferences;
	public SharedPreferences.Editor editor;
	public ProgressDialog progress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		preferences = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
	}

	public String getStringFromXml(int actionSettings) {
		// TODO Auto-generated method stub

		return getResources().getString(actionSettings);
	}

	public void updatePref(String key, String value) {
		editor = preferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public void preToast(String string) {
		Toast.makeText(getApplicationContext(), string, Toast.LENGTH_SHORT)
				.show();
	}

	public void startDialogBar() {
		progress = new ProgressDialog(this);
		progress.setTitle("Loading . . ");
		progress.setMessage("Wait while loading...");
		progress.setCancelable(false);
		progress.show();
		// progressBar.
	}

	public void closeDialogBar() {
		if (progress.isShowing() || progress != null)
			progress.dismiss();
	}

	public void gotoHome() {
		Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	public void callHome() {
		Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	public void alertBox(String message) {

		new AlertDialog.Builder(this)
				.setMessage(message)
				.setCancelable(false)
				.setPositiveButton("Ok",
						new android.content.DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						}).show();
	}

	public void callHome(View v) {
		gotoHome();
	}
}
