package com.call.tracker.alarm;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.call.tracker.HomeActivity;
import com.call.tracker.R;

public class MyAlarmService extends Service {

	private NotificationManager mManager;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		String name = (String) intent.getExtras().get("contact_name");
		mManager = (NotificationManager) this.getApplicationContext()
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Intent intent1 = new Intent(this.getApplicationContext(),
				HomeActivity.class);
		Notification notification = new Notification.Builder(
				getApplicationContext()).setContentText("Call To : " + name)
				.setSmallIcon(R.drawable.icon)
				.setWhen(System.currentTimeMillis()).build();

		intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
				| Intent.FLAG_ACTIVITY_CLEAR_TOP);

		PendingIntent pendingNotificationIntent = PendingIntent.getActivity(
				this.getApplicationContext(), 0, intent1,
				PendingIntent.FLAG_UPDATE_CURRENT);
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification.setLatestEventInfo(this.getApplicationContext(),
				"CallTracker", "Call To : " + name, pendingNotificationIntent);

		mManager.notify(0, notification);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}