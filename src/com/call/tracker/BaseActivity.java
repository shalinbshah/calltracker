package com.call.tracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Toast;

import com.bugsense.trace.BugSenseHandler;
import com.call.tracker.interfaces.Constants;

public class BaseActivity extends FragmentActivity implements Constants {
	public SharedPreferences preferences;
	public SharedPreferences.Editor editor;
	public ProgressDialog progress;
	public static String BUG_SENSE_KEY = "cc936a56";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		preferences = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		BugSenseHandler.initAndStartSession(BaseActivity.this, BUG_SENSE_KEY);
	}

	public String getStringFromXml(int actionSettings) {
		return getResources().getString(actionSettings);
	}

	public void updatePref(String key, String value) {
		preferences = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		editor = preferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public void updatePref(String key, Boolean value) {
		preferences = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		editor = preferences.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	public boolean getPref(String key, Boolean defaultValue) {
		preferences = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		return preferences.getBoolean(key, defaultValue);
	}

	public void callHome(View v) {
		gotoHome();
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
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	public void callHome() {
		Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
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

}
