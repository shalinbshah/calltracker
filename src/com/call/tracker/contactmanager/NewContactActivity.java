package com.call.tracker.contactmanager;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentProviderOperation;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Data;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.call.tracker.BaseActivity;
import com.call.tracker.DateTimePicker;
import com.call.tracker.R;

public class NewContactActivity extends BaseActivity {
	String[] items = { "Mobile", "Home", "Work", "Main", "Other" };
	String[] emailItems = { "Home", "Work", "Other" };
	int numberOnePreference = 1;
	int numberTwopreference = 1;
	int numberThreepreference = 1;
	EditText editPhone1;
	EditText editPhone2;
	EditText email;
	EditText name;
	Button phoButton;
	Button phoButton2;
	Button emButton;
	int[] phoneTypes = { Phone.TYPE_MOBILE, Phone.TYPE_HOME, Phone.TYPE_WORK,
			Phone.TYPE_MAIN, Phone.TYPE_OTHER };
	int[] emailTypes = { Email.TYPE_HOME, Email.TYPE_WORK, Email.TYPE_OTHER };
	TextView textViewDate, textViewTime;
	ArrayList<ContentProviderOperation> op_list = new ArrayList<ContentProviderOperation>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout_create_contacts);
		initControl();
	}

	public void callSave(View v) {
		addContact(v);
		finish();
	}

	public void initControl() {
		editPhone1 = (EditText) findViewById(R.id.phoneNumber1);
		editPhone2 = (EditText) findViewById(R.id.phoneNumber2);
		email = (EditText) findViewById(R.id.Email);
		name = (EditText) findViewById(R.id.name);
		phoButton = (Button) findViewById(R.id.phoneNumber1selection);
		phoButton2 = (Button) findViewById(R.id.phoneNumber2selection);
		emButton = (Button) findViewById(R.id.emailSelection);

		textViewDate = (TextView) findViewById(R.id.textDate);
		textViewTime = (TextView) findViewById(R.id.textTime);
	}

	public OnClickListener onClickButton = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			// Button button = (Button) v;

		}
	};

	public void selection(View v) {
		switch (v.getId()) {
		case R.id.phoneNumber1selection:
			getSelected(1);
			break;
		case R.id.phoneNumber2selection:
			getSelected(2);
			break;

		}
	}

	public void getSelected(final int a) {
		new AlertDialog.Builder(this)
				.setSingleChoiceItems(items, -1, null)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						int selectedPosition = ((AlertDialog) dialog)
								.getListView().getCheckedItemPosition();
						if (a == 1) {
							numberOnePreference = selectedPosition;
							phoButton.setText(items[selectedPosition]);

						} else if (a == 2) {
							numberTwopreference = selectedPosition;
							phoButton2.setText(items[selectedPosition]);
						}

					}
				})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();

							}
						}).show();
	}

	public void forEmail(View v) {
		new AlertDialog.Builder(this)
				.setSingleChoiceItems(emailItems, -1, null)
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {

						int selectedPosition = ((AlertDialog) dialog)
								.getListView().getCheckedItemPosition();
						numberThreepreference = selectedPosition;
						emButton.setText(emailItems[selectedPosition]);

					}
				})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
							}
						}).show();
	}

	public void addContact(View v) {
		op_list.clear();
		op_list.add(ContentProviderOperation
				.newInsert(ContactsContract.RawContacts.CONTENT_URI)
				.withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
				.withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
				// .withValue(RawContacts.AGGREGATION_MODE,
				// RawContacts.AGGREGATION_MODE_DEFAULT)
				.build());

		setName(name.getText().toString());
		setNumber(numberOnePreference, editPhone1.getText().toString());
		setNumber(numberTwopreference, editPhone2.getText().toString());
		setEmail(numberThreepreference, email.getText().toString());

		try {
			getContentResolver()
					.applyBatch(ContactsContract.AUTHORITY, op_list);
			Toast.makeText(getApplicationContext(), "contact added",
					Toast.LENGTH_SHORT).show();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void setName(String a) {
		op_list.add(ContentProviderOperation
				.newInsert(Data.CONTENT_URI)
				.withValueBackReference(Data.RAW_CONTACT_ID, 0)
				.withValue(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE)
				.withValue(StructuredName.GIVEN_NAME, name.getText().toString())
				.build());
	}

	public void setNumber(int a, String s) {
		op_list.add(ContentProviderOperation
				.newInsert(Data.CONTENT_URI)
				.withValueBackReference(Data.RAW_CONTACT_ID, 0)
				.withValue(
						ContactsContract.Data.MIMETYPE,
						ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
				.withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, s)
				.withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
						phoneTypes[a]).build());
	}

	public void setEmail(int a, String s) {
		op_list.add(ContentProviderOperation
				.newInsert(Data.CONTENT_URI)
				.withValueBackReference(Data.RAW_CONTACT_ID, 0)

				.withValue(
						ContactsContract.Data.MIMETYPE,
						ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
				.withValue(ContactsContract.CommonDataKinds.Email.DATA,
						s.toString())
				.withValue(ContactsContract.CommonDataKinds.Email.TYPE,
						emailTypes[a]).build());
	}

	private void showDateTimeDialog(String dateTime) {
		// Create the dialog
		final Dialog mDateTimeDialog = new Dialog(this);
		// Inflate the root layout
		final RelativeLayout mDateTimeDialogView = (RelativeLayout) getLayoutInflater()
				.inflate(R.layout.date_time_dialog, null);
		// Grab widget instance
		final DateTimePicker mDateTimePicker = (DateTimePicker) mDateTimeDialogView
				.findViewById(R.id.DateTimePicker);
		// Check is system is set to use 24h time (this doesn't seem to work as
		// expected though)
		final String timeS = android.provider.Settings.System.getString(
				getContentResolver(),
				android.provider.Settings.System.TIME_12_24);
		final boolean is24h = !(timeS == null || timeS.equals("12"));

		// Update demo TextViews when the "OK" button is clicked
		((Button) mDateTimeDialogView.findViewById(R.id.SetDateTime))
				.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						mDateTimePicker.clearFocus();
						// TODO Auto-generated method stub
						StringBuilder builderDate = new StringBuilder();
						String date = (mDateTimePicker.get(Calendar.YEAR) + "/"
								+ (mDateTimePicker.get(Calendar.MONTH) + 1)
								+ "/" + mDateTimePicker
								.get(Calendar.DAY_OF_MONTH));
						builderDate.append(date).append(" ");
						if (mDateTimePicker.is24HourView()) {
							String time = (mDateTimePicker
									.get(Calendar.HOUR_OF_DAY) + ":" + mDateTimePicker
									.get(Calendar.MINUTE));
							builderDate.append(time).append(" ");
						} else {
							String timePM = (mDateTimePicker.get(Calendar.HOUR)
									+ ":"
									+ mDateTimePicker.get(Calendar.MINUTE)
									+ ":" + (mDateTimePicker
									.get(Calendar.AM_PM) == Calendar.AM ? "AM"
									: "PM"));
							builderDate.append(timePM).append(" ");
						}
						// butDep.setText(builder.toString());
						textViewDate.setText(builderDate.toString());
						textViewTime.setText("");
						mDateTimeDialog.dismiss();
					}
				});

		// Cancel the dialog when the "Cancel" button is clicked
		((Button) mDateTimeDialogView.findViewById(R.id.CancelDialog))
				.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						// TODO Auto-generated method stub
						mDateTimeDialog.cancel();
					}
				});

		// Reset Date and Time pickers when the "Reset" button is clicked
		((Button) mDateTimeDialogView.findViewById(R.id.ResetDateTime))
				.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						// TODO Auto-generated method stub
						mDateTimePicker.reset();
					}
				});

		// Setup TimePicker
		mDateTimePicker.setIs24HourView(is24h);
		if (dateTime.length() != 0) {
			String[] valTime = dateTime.split(" ");
			String[] valDate = valTime[0].split("/");
			String[] valTimeVal = valTime[1].split(":");

			mDateTimePicker.updateDate(Integer.valueOf(valDate[0]),
					Integer.valueOf(valDate[1]) - 1,
					Integer.valueOf(valDate[2]));

			mDateTimePicker.updateTime(Integer.valueOf(valTimeVal[0]),
					Integer.valueOf(valTimeVal[1]));
		}

		// No title on the dialog window
		mDateTimeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// Set the dialog content view
		mDateTimeDialog.setContentView(mDateTimeDialogView);
		// Display the dialog
		mDateTimeDialog.show();
	}

	public void callCalender(View view) {
		showDateTimeDialog("");
	}
}