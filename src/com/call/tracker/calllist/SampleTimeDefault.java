package com.call.tracker.calllist;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.QuickContactBadge;
import android.widget.TextView;

import com.call.tracker.R;
import com.call.tracker.alarm.AlarmReceiver;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.fourmob.datetimepicker.date.DatePickerDialog.OnDateSetListener;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

public class SampleTimeDefault extends FragmentActivity implements
		OnDateSetListener, TimePickerDialog.OnTimeSetListener {

	public static final String DATEPICKER_TAG = "datepicker";
	public static final String TIMEPICKER_TAG = "timepicker";
	Calendar calNow = Calendar.getInstance();
	Button setDate;
	Button setTime;
	TextView alarmText;
	TimePickerDialog timePickerDialog;
	DatePickerDialog datePickerDialog;
	QuickContactBadge contactBadge;
	TextView tvContactName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		tvContactName = (TextView) findViewById(R.id.tvContactName);
		if (getIntent().getExtras().containsKey("contact_name")) {
			String name = (String) getIntent().getExtras().get("contact_name");
			tvContactName.setText(name);
		}
		contactBadge = (QuickContactBadge) findViewById(R.id.quickContactBadgeContactDateTime);
		if (getIntent().getExtras().containsKey("contact_uri")) {
			Uri uri = (Uri) getIntent().getExtras().get("contact_uri");
			contactBadge.assignContactUri(uri);
		}
		calNow = (Calendar) calNow.clone();
		final Calendar calendar = Calendar.getInstance();

		datePickerDialog = DatePickerDialog.newInstance(this,
				calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH), isVibrate());
		timePickerDialog = TimePickerDialog.newInstance(this,
				calendar.get(Calendar.HOUR_OF_DAY),
				calendar.get(Calendar.MINUTE), false, false);
		setDate = (Button) findViewById(R.id.dateButton);
		setTime = (Button) findViewById(R.id.timeButton);
		alarmText = (TextView) findViewById(R.id.textAlarmTime);
		setDate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				datePickerDialog.setVibrate(isVibrate());
				datePickerDialog.setYearRange(1985, 2028);
				datePickerDialog.show(getSupportFragmentManager(),
						DATEPICKER_TAG);
			}
		});

		setTime.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				timePickerDialog.setVibrate(isVibrate());
				timePickerDialog.show(getSupportFragmentManager(),
						TIMEPICKER_TAG);
			}
		});

		if (savedInstanceState != null) {
			DatePickerDialog dpd = (DatePickerDialog) getSupportFragmentManager()
					.findFragmentByTag(DATEPICKER_TAG);
			if (dpd != null) {
				dpd.setOnDateSetListener(this);
			}

			TimePickerDialog tpd = (TimePickerDialog) getSupportFragmentManager()
					.findFragmentByTag(TIMEPICKER_TAG);
			if (tpd != null) {
				tpd.setOnTimeSetListener(this);
			}
		}
	}

	private boolean isVibrate() {
		return ((CheckBox) findViewById(R.id.checkBoxVibrate)).isChecked();
	}

	@Override
	public void onDateSet(DatePickerDialog datePickerDialog, int year,
			int month, int day) {
		calNow.set(Calendar.DAY_OF_MONTH, day);
		calNow.set(Calendar.YEAR, year);
		calNow.set(Calendar.MONTH, month);
		timePickerDialog.setVibrate(isVibrate());
		timePickerDialog.show(getSupportFragmentManager(), TIMEPICKER_TAG);
	}

	@Override
	public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
		alarmText.setText("" + hourOfDay + ":" + minute);
		calNow.set(Calendar.HOUR_OF_DAY, hourOfDay);
		calNow.set(Calendar.MINUTE, minute);
		calNow.set(Calendar.SECOND, 0);
		calNow.set(Calendar.MILLISECOND, 0);
		setAlarm(calNow, false);
	}

	private void setAlarm(Calendar targetCal, boolean repeat) {

		Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
		intent.putExtras(getIntent().getExtras());

		Object uri = (Uri) intent.getExtras().get("contact_uri");
		int contact_id = Integer.parseInt(uri.toString().replace(
				"content://com.android.contacts/contacts/", ""));
		intent.putExtra("contact_id", contact_id);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(
				getBaseContext(), contact_id, intent, 0);
		AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(),
				pendingIntent);

		if (repeat) {
			alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
					targetCal.getTimeInMillis(), TimeUnit.MINUTES.toMillis(5),
					pendingIntent);

			alarmText.setText("\n\n***\n" + "Alarm is set@ "
					+ targetCal.getTime() + "\n" + "Repeat every 5 minutes\n"
					+ "***\n");
		} else {
			alarmManager.set(AlarmManager.RTC_WAKEUP,
					targetCal.getTimeInMillis(), pendingIntent);
			alarmText.setText("\n\n***\n" + "Alarm is set@ "
					+ targetCal.getTime() + "\n" + "One shot\n" + "***\n");
		}

	}

	private void cancelAlarm(int requestCode) {

		alarmText.setText("\n\n***\n" + "Alarm Cancelled! \n" + "***\n");

		Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(
				getBaseContext(), requestCode, intent, 0);
		AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(pendingIntent);

	}
}
