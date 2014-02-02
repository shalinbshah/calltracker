package com.call.tracker.calllist;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class CallHangUpReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context ctx, Intent intent) {
		// if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(
		// TelephonyManager.EXTRA_STATE_RINGING)) {
		//
		// // Phone number
		// String incomingNumber = intent
		// .getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
		//
		// // Ringing state
		// // This code will execute when the phone has an incoming call
		// } else
		String contactNumber = this.getResultData();
		Intent callHangUpActivityIntent = new Intent(ctx,
				CallHangUpActivity.class);
		callHangUpActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		callHangUpActivityIntent.putExtra("contact_number", contactNumber);
		ctx.startActivity(callHangUpActivityIntent);
		// This code will execute when the call is answered or disconnected

	}
}
