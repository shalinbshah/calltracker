package com.call.tracker.alarm;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Vibrator;
import android.widget.Toast;

import com.call.tracker.R;
import com.call.tracker.calllist.ContactFollowUpDetailsActivity;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class AlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Toast.makeText(context, "Alarm received!", Toast.LENGTH_LONG).show();
		String name = (String) intent.getExtras().get("contact_name");
		showNotification(intent, context, "Call to : " + name);
	}

	private void playSound(Context context) {
		try {
			MediaPlayer player = MediaPlayer.create(context, R.raw.tone);
			// player.prepare();
			player.start();
			// player.stop();
			// player.reset();
			// player.release();
			// player = null;
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
	}

	private void showNotification(Intent recIntent, Context paramContext,
			String message) {
		try {
			int contact_id = recIntent.getExtras().getInt("contact_id");
			Vibrator v = (Vibrator) paramContext
					.getSystemService(Context.VIBRATOR_SERVICE);
			// Vibrate for 500 milliseconds
			v.vibrate(500);

			Intent intent = new Intent(paramContext, ContactFollowUpDetailsActivity.class);
			intent.putExtras(recIntent.getExtras());

			NotificationManager nm = (NotificationManager) paramContext
					.getSystemService(Context.NOTIFICATION_SERVICE);

			Resources res = paramContext.getResources();
			Notification.Builder builder = new Notification.Builder(
					paramContext);
			PendingIntent localPendingIntent = PendingIntent.getActivity(
					paramContext, contact_id, intent, 0);

			builder.setContentIntent(localPendingIntent)
					.setSmallIcon(R.drawable.notification_small_icon)
					.setLargeIcon(
							BitmapFactory.decodeResource(res, R.drawable.icon))
					.setTicker(message).setWhen(System.currentTimeMillis())
					.setAutoCancel(true)
					.setContentTitle(res.getString(R.string.app_name))
					.setContentText(message);
			Notification n = builder.build();
			nm.notify(contact_id, n);
			playSound(paramContext);
			// OLDER IMPLEMENTATION
			// Notification localNotification = new
			// Notification(R.drawable.icon,
			// paramContext.getString(R.string.app_name),
			// System.currentTimeMillis());
			// localNotification.flags = (0x10 | localNotification.flags);
			// // PendingIntent that will start the EnterWeightActivity
			//
			// PendingIntent localPendingIntent = PendingIntent.getActivity(
			// paramContext, contact_id, intent, 0);
			//
			// localNotification.setLatestEventInfo(paramContext, "CallTracker",
			// message, localPendingIntent);
			// // Retrieve a NotificationManager to show the notification
			// ((NotificationManager) paramContext
			// .getSystemService("notification")).notify(contact_id,
			// localNotification);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
