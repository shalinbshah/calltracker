package com.call.tracker.reports;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.call.tracker.BaseActivity;
import com.call.tracker.R;
import com.call.tracker.calllist.CallHangUpActivity;
import com.call.tracker.database.DBAdapter;
import com.devspark.appmsg.AppMsg;
import com.inqbarna.tablefixheaders.MyAdapter;
import com.inqbarna.tablefixheaders.TableFixHeaders;

public class ReportsActivity1 extends BaseActivity {

	private static final int DATE_DIALOG_ID1 = 1;
	private static final int DATE_DIALOG_ID2 = 2;
	long totalCalls = 0;
	long totalTimeOnCalls = 0;
	long totalAppts = 0;
	long totalClosings = 0;

	long goalCalls = 0;
	long goalTimeOnCalls = 0;
	long goalAppts = 0;
	long goalClosings = 0;
	private EditText mEdit1;
	private EditText mEdit2;

	private int year1;
	private int month1;
	private int day1;

	private int year2;
	private int month2;
	private int day2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_reports_activity1);
		mEdit1 = (EditText) findViewById(R.id.editText1);
		mEdit2 = (EditText) findViewById(R.id.editText2);
		final Calendar c = Calendar.getInstance();
		year1 = c.get(Calendar.YEAR);
		month1 = c.get(Calendar.MONTH);
		day1 = c.get(Calendar.DAY_OF_MONTH);
		year2 = c.get(Calendar.YEAR);
		month2 = c.get(Calendar.MONTH) + 1;
		day2 = c.get(Calendar.DAY_OF_MONTH);

		mEdit1.setText(new StringBuilder().append(month1).append("/")
				.append(day1).append("/").append(year1).append(" "));
		mEdit2.setText(new StringBuilder().append(month2).append("/")
				.append(day2).append("/").append(year2).append(" "));
		updateTable();
	}

	public void selectDate1(View view) {
		showDialog(DATE_DIALOG_ID1);
	}

	public void selectDate2(View view) {
		showDialog(DATE_DIALOG_ID2);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID1:
			return new DatePickerDialog(this, datePickerListener1, year1,
					month1 - 1, day1);

		case DATE_DIALOG_ID2:
			return new DatePickerDialog(this, datePickerListener2, year2,
					month2 - 1, day2);
		}
		return null;
	}

	private DatePickerDialog.OnDateSetListener datePickerListener1 = new DatePickerDialog.OnDateSetListener() {

		// when dialog box is closed, below method will be called.
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {
			year1 = selectedYear;
			month1 = selectedMonth;
			day1 = selectedDay;

			// set selected date into textview
			mEdit1.setText(new StringBuilder().append(month1 + 1).append("/")
					.append(day1).append("/").append(year1).append(" "));

			if (day2 > 0)
				updateTable();
			// set selected date into datepicker also

		}
	};

	private DatePickerDialog.OnDateSetListener datePickerListener2 = new DatePickerDialog.OnDateSetListener() {

		// when dialog box is closed, below method will be called.
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {
			year2 = selectedYear;
			month2 = selectedMonth;
			day2 = selectedDay;

			// set selected date into textview
			mEdit2.setText(new StringBuilder().append(month2 + 1).append("/")
					.append(day2).append("/").append(year2).append(" "));
			if (day1 > 0)
				updateTable();
			// set selected date into datepicker also

		}
	};

	private void updateTable() {
		if (datesInValid())
			return;
		processCallList();

		TableFixHeaders tableFixHeaders = (TableFixHeaders) findViewById(R.id.tblReports);

		String[][] a = new String[5][3];
		a[0][0] = "Stats";
		a[1][0] = "Total Calls";
		a[2][0] = "% Appt";
		a[3][0] = "Time On Calls";
		a[4][0] = "Closings";

		a[0][1] = "Goal";
		a[1][1] = goalCalls + "";
		a[2][1] = goalCalls == 0 ? "0" : (goalAppts / goalCalls) * 100 + "";
		int min = (int) (goalTimeOnCalls / 60);
		long sec = goalTimeOnCalls % 60;
		a[3][1] = min + ":" + sec + "";
		a[4][1] = "N.A.";

		a[0][2] = "Evaluation";
		a[1][2] = totalCalls + "";
		a[2][2] = totalCalls == 0 ? "0" : (totalAppts / totalCalls) * 100 + "";
		min = (int) (totalTimeOnCalls / 60);
		sec = totalTimeOnCalls % 60;
		a[3][2] = min + ":" + sec + "";
		a[4][2] = totalClosings + "";

		MyAdapter matrixTableAdapter = new MyAdapter(ReportsActivity1.this, a);
		tableFixHeaders.setAdapter(matrixTableAdapter);
	}

	@SuppressWarnings("deprecation")
	private boolean datesInValid() {
		Date date1 = new Date(year1, month1, day1);
		Date date2 = new Date(year2, month2, day2);
		if (date1.after(date2)) {
			AppMsg appMsg = AppMsg.makeText(this,
					"Selected Dates are not valid", AppMsg.STYLE_ALERT);
			appMsg.setLayoutGravity(Gravity.BOTTOM);
			appMsg.show();
			return true;
		} else
			return false;
	}

	private void processCallList() {
		DBAdapter adapter = new DBAdapter(getApplicationContext());
		adapter.openDataBase();
		Date date1 = new Date(year1 - 1900, month1, day1);
		Date date2 = new Date(year2 - 1900, month2, day2);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		String dateConstraint = " where call_time between '"
				+ dateFormat.format(date1) + "' and '"
				+ dateFormat.format(date2) + "'";
		//
		String query = "SELECT sum(call_duration_Seconds) FROM tbl_calls_track"
				+ dateConstraint;
		Cursor cursor = adapter.selectRecordsFromDB(query, null);
		if (cursor.moveToFirst()) {
			do {
				totalTimeOnCalls = cursor.getInt(0);
			} while (cursor.moveToNext());
		}
		cursor.close();

		query = "SELECT COUNT(id) FROM tbl_calls_track" + dateConstraint;
		cursor = adapter.selectRecordsFromDB(query, null);
		if (cursor.moveToFirst()) {
			do {
				totalCalls = cursor.getInt(0);
			} while (cursor.moveToNext());
		}
		cursor.close();
		// Total Closings
		query = "SELECT COUNT(id) FROM tbl_stats where tracking_status = '"
				+ CallHangUpActivity.ContactStatus.SHOTDOWN
				+ "' and "
				+ dateConstraint.replace("call_time", "tracking_status")
						.replace("where", "");
		;
		cursor = adapter.selectRecordsFromDB(query, null);
		if (cursor.moveToFirst()) {
			do {
				totalClosings = cursor.getInt(0);
			} while (cursor.moveToNext());
		}
		cursor.close();
		// Total Appointments
		query = "SELECT COUNT(id) FROM tbl_stats where tracking_status = '"
				+ CallHangUpActivity.ContactStatus.APPOINTMENT
				+ "' and "
				+ dateConstraint.replace("call_time", "tracking_status")
						.replace("where", "");
		;
		cursor = adapter.selectRecordsFromDB(query, null);
		if (cursor.moveToFirst()) {
			do {
				totalAppts = cursor.getInt(0);
			} while (cursor.moveToNext());
		}
		cursor.close();

		// Goal Appointments
		query = "SELECT mission_appointments FROM tbl_sales_goals_mission where mission_year = '"
				+ Calendar.getInstance().get(Calendar.YEAR) + "'";
		cursor = adapter.selectRecordsFromDB(query, null);
		if (cursor.moveToFirst()) {
			do {
				goalAppts = cursor.getInt(0);
			} while (cursor.moveToNext());
		}
		cursor.close();

		// Goal Calls/Homes
		query = "SELECT mission_homes FROM tbl_sales_goals_mission where mission_year = '"
				+ Calendar.getInstance().get(Calendar.YEAR) + "'";
		cursor = adapter.selectRecordsFromDB(query, null);
		if (cursor.moveToFirst()) {
			do {
				goalCalls = cursor.getInt(0);
			} while (cursor.moveToNext());
		}
		cursor.close();

		// Goal Calls/Homes
		query = "SELECT mission_avg_hoursperday FROM tbl_sales_goals_mission where mission_year = '"
				+ Calendar.getInstance().get(Calendar.YEAR) + "'		";
		cursor = adapter.selectRecordsFromDB(query, null);
		if (cursor.moveToFirst()) {
			do {
				// TODO replace one by selected days
				goalTimeOnCalls = cursor.getInt(0) * 1;
			} while (cursor.moveToNext());
		}
		cursor.close();

	}

}
