package com.call.tracker.calllist;

import java.io.InputStream;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.text.format.Time;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.QuickContactBadge;
import android.widget.TextView;

import com.call.tracker.HomeActivity;
import com.call.tracker.R;
import com.call.tracker.alarm.AlarmReceiver;
import com.call.tracker.database.DBAdapter;
import com.call.tracker.voicenotes.VoiceListActivity;
import com.doomonafireball.betterpickers.recurrencepicker.EventRecurrence;
import com.doomonafireball.betterpickers.recurrencepicker.EventRecurrenceFormatter;
import com.doomonafireball.betterpickers.recurrencepicker.RecurrencePickerDialog;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.fourmob.datetimepicker.date.DatePickerDialog.OnDateSetListener;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

public class ContactFollowUpDetailsActivity extends FragmentActivity implements
		OnDateSetListener, TimePickerDialog.OnTimeSetListener,
		RecurrencePickerDialog.OnRecurrenceSetListener {

	public static final String DATEPICKER_TAG = "datepicker";
	public static final String TIMEPICKER_TAG = "timepicker";
	private static final String FRAG_TAG_RECUR_PICKER = "recurrencePickerDialogFragment";

	Calendar calNow = Calendar.getInstance();
	private EventRecurrence mEventRecurrence = new EventRecurrence();

	Button setDate;
	Button setRepeat;
	TextView alarmText;
	TimePickerDialog timePickerDialog;
	DatePickerDialog datePickerDialog;
	RecurrencePickerDialog recurrencePickerDialog;
	private String mRrule;
	QuickContactBadge contactBadge;
	TextView tvContactName;
	DBAdapter adapter;
	EditText etFollowUpnotes;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_foolowup_activity);
		adapter = new DBAdapter(getApplicationContext());
		tvContactName = (TextView) findViewById(R.id.tvContactName);
		etFollowUpnotes = (EditText) findViewById(R.id.eTFollowUpNotes);
		etFollowUpnotes.setInputType(InputType.TYPE_NULL);

		if (getIntent().getExtras().containsKey("contact_name")) {
			String name = (String) getIntent().getExtras().get("contact_name");
			tvContactName.setText(name);
		}
		contactBadge = (QuickContactBadge) findViewById(R.id.quickContactBadgeContactDateTime);
		if (getIntent().getExtras().containsKey("contact_uri")
				&& null != getIntent().getExtras().get("contact_uri")) {
			Uri uri = (Uri) getIntent().getExtras().get("contact_uri");
			contactBadge.assignContactUri(uri);
			if (ContactsContract.Contacts.openContactPhotoInputStream(
					getContentResolver(), uri) != null) {
				InputStream input = ContactsContract.Contacts
						.openContactPhotoInputStream(getContentResolver(), uri);
				contactBadge.setImageBitmap(BitmapFactory.decodeStream(input));
			}
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
		setRepeat = (Button) findViewById(R.id.repeatButton);
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

		setRepeat.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				recurrencePickerDialog = new RecurrencePickerDialog();
				Bundle b = new Bundle();
				Time t = new Time();
				t.setToNow();
				b.putLong(RecurrencePickerDialog.BUNDLE_START_TIME_MILLIS,
						t.toMillis(false));
				b.putString(RecurrencePickerDialog.BUNDLE_TIME_ZONE, t.timezone);
				// may be more efficient to serialize and pass in
				// EventRecurrence
				b.putString(RecurrencePickerDialog.BUNDLE_RRULE, mRrule);
				recurrencePickerDialog.setArguments(b);
				recurrencePickerDialog
						.setOnRecurrenceSetListener(ContactFollowUpDetailsActivity.this);
				recurrencePickerDialog.show(getSupportFragmentManager(),
						FRAG_TAG_RECUR_PICKER);
			}
		});

	}

	private boolean isVibrate() {
		return true;
	}

	public void callHome(View v) {
		gotoHome();
	}

	public void gotoHome() {
		Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
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
			Resources r = getResources();
			String repeatString = EventRecurrenceFormatter.getRepeatString(
					this, r, mEventRecurrence, true);
			alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
					targetCal.getTimeInMillis(), TimeUnit.MINUTES.toMillis(5),
					pendingIntent);
			alarmText.setText("Follow Up is set @ " + targetCal.getTime()
					+ " and then " + repeatString + "\n");
			adapter.updateContactFrequency(Integer.toString(contact_id), mRrule);
		} else {
			alarmManager.set(AlarmManager.RTC_WAKEUP,
					targetCal.getTimeInMillis(), pendingIntent);
			alarmText
					.setText("Follow Up is set@ " + targetCal.getTime() + "\n");
		}

	}

	public void toggleEditable(View v) {
		etFollowUpnotes.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
	}

	public void updateFollowUpNotes(View v) {
		Object uri = (Uri) getIntent().getExtras().get("contact_uri");
		int contact_id = Integer.parseInt(uri.toString().replace(
				"content://com.android.contacts/contacts/", ""));
		adapter.updateContactFollowUpNotes(Integer.toString(contact_id),
				etFollowUpnotes.getText().toString());
		etFollowUpnotes.setInputType(InputType.TYPE_NULL);

	}

	public void openVoiceNotes(View v) {
		Intent intent = new Intent(getBaseContext(), VoiceListActivity.class);
		startActivity(intent);
	}

	public void cancelAlarms(View v) {
		alarmText.setText("\n" + "FollowUp Cancelled! \n");
		Object uri = (Uri) getIntent().getExtras().get("contact_uri");
		int contact_id = Integer.parseInt(uri.toString().replace(
				"content://com.android.contacts/contacts/", ""));
		Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(
				getBaseContext(), contact_id, intent, 0);
		AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(pendingIntent);
	}

	@Override
	public void onRecurrenceSet(String rrule) {
		mRrule = rrule;
		if (mRrule != null) {
			mEventRecurrence.parse(mRrule);
		}
		populateRepeats();
	}

	private void populateRepeats() {
		Resources r = getResources();
		@SuppressWarnings("unused")
		String repeatString = "";
		if (!TextUtils.isEmpty(mRrule)) {
			repeatString = EventRecurrenceFormatter.getRepeatString(this, r,
					mEventRecurrence, true);
			setAlarm(calNow, true);
		} else
			setAlarm(calNow, false);
	}
}
