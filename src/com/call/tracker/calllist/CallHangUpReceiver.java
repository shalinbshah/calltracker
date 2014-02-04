package com.call.tracker.calllist;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class CallHangUpReceiver extends BroadcastReceiver {
	public SharedPreferences preferences;
	public SharedPreferences.Editor editor;

	@Override
	public void onReceive(Context ctx, Intent intent) {
		String contactNumber = this.getResultData();
		preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
		editor = preferences.edit();
		editor.putString("last_called_number", contactNumber);
		editor.commit();
	}

}
