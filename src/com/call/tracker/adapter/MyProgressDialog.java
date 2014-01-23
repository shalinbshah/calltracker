package com.call.tracker.adapter;

import android.app.Activity;
import android.app.ProgressDialog;

import com.call.tracker.R;

public class MyProgressDialog extends ProgressDialog {

	Activity activity;
	ProgressDialog progressDialog;

	@Override
	public void show() {
		getMyProgressDialog().show();
	};

	@Override
	public void dismiss() {
		super.dismiss();
		getMyProgressDialog().dismiss();
	}

	public ProgressDialog getMyProgressDialog() {
		return progressDialog;
	}

	public MyProgressDialog(Activity activity) {
		super(activity);
		this.activity = activity;
		progressDialog = new ProgressDialog(activity);// Create a new progress
														// dialog
		progressDialog = ProgressDialog.show(activity, "Loading...",
				"Loading application View, please wait...", false, false);
		progressDialog.setContentView(R.layout.custom_progress_dialog);
		// Set the progress dialog to display a horizontal progress bar
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		// Set the dialog title to 'Loading...'
		progressDialog.setTitle("Loading...");
		// Set the dialog message to 'Loading application View, please wait...'
		progressDialog.setMessage("Loading application View, please wait...");
		// This dialog can't be canceled by pressing the back key
		progressDialog.setCancelable(false);
		// This dialog isn't indeterminate
		progressDialog.setIndeterminate(true);// The maximum number of items is
												// 100
		// progressDialog.setMax(100);
		// Set the current progress to zero
		// progressDialog.setProgress(0);
		// Display the progress dialog
	}
}