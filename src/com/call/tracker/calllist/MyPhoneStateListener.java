package com.call.tracker.calllist;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.call.tracker.database.DBAdapter;
import com.call.tracker.model.ContactModel;

public class MyPhoneStateListener extends BroadcastReceiver {
	String contactNumber;
	boolean isContactValid = false;

	public SharedPreferences preferences;
	public SharedPreferences.Editor editor;
	DBAdapter adapter;

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(
				TelephonyManager.EXTRA_STATE_RINGING)) {
			contactNumber = intent
					.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
		} else if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(
				TelephonyManager.EXTRA_STATE_IDLE)) {
			processLastCall(context);
		} else if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(
				TelephonyManager.EXTRA_STATE_OFFHOOK)) {
		}

	}

	public void processLastCall(Context context) {
		preferences = PreferenceManager.getDefaultSharedPreferences(context);
		contactNumber = preferences.getString("last_called_number", "");
		adapter = new DBAdapter(context);
		try {
			adapter.openDataBase();
			ArrayList<ContactModel> contacts = adapter.getContacts();
			adapter.close();
			for (ContactModel model : contacts) {
				Log.d("callTracker", "Comparing : "
						+ model.getNumber1().trim().replaceAll(" ", "")
								.replaceAll("-", "") + " with " + contactNumber);
				if (model.getNumber1().trim().replaceAll(" ", "")
						.replaceAll("-", "").contains(contactNumber)) {
					isContactValid = true;
					break;
				}
			}
		} catch (Exception e) {

		}
		if (isContactValid && CallHangUpActivity.calledFromApp) {
			Intent callHangUpActivityIntent = new Intent(context,
					CallHangUpActivity.class);
			callHangUpActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			callHangUpActivityIntent.putExtra("contact_number", contactNumber);
			context.startActivity(callHangUpActivityIntent);
		} else {
			Log.d("callTracker", "last called number is not valid "
					+ contactNumber);
		}
	}
}
