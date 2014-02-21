package com.call.tracker.reports;

import java.util.ArrayList;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

import com.call.tracker.BaseActivity;
import com.call.tracker.R;
import com.call.tracker.calllist.CallHangUpActivity;
import com.call.tracker.database.DBAdapter;
import com.inqbarna.tablefixheaders.MyAdapter;
import com.inqbarna.tablefixheaders.TableFixHeaders;

public class ReportsActivity1 extends BaseActivity {
	ArrayList<String> addedContactIDs;
	TextView tvEvalTotalTimeOnCalls;
	TextView tvEvalTotalCalls;
	TextView tvEvalTotalPercAppts;
	TextView tvEvalTotalClosings;
	long totalCalls = 0;
	long totalTimeOnCalls = 0;
	long totalAppts = 0;
	long totalClosings = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_reports_activity1);
		processCallList();
		updateTable();
	}

	private void updateTable() {
		TableFixHeaders tableFixHeaders = (TableFixHeaders) findViewById(R.id.tblReports);

		String[][] a = new String[5][3];
		a[0][0] = "Stats";
		a[1][0] = "Total Calls";
		a[2][0] = "% Appt";
		a[3][0] = "Time On Calls";
		a[4][0] = "Closings";

		a[0][1] = "Goal";
		a[1][1] = "56";
		a[2][1] = "70";
		a[3][1] = "12000";
		a[4][1] = "10";

		a[0][2] = "Evaluation";
		a[1][2] = totalCalls + "";
		a[2][2] = (totalAppts / totalCalls) * 100 + "";
		a[3][2] = totalTimeOnCalls + "";
		a[4][2] = totalClosings + "";

		MyAdapter matrixTableAdapter = new MyAdapter(ReportsActivity1.this, a);
		tableFixHeaders.setAdapter(matrixTableAdapter);
	}

	private void processCallList() {
		DBAdapter adapter = new DBAdapter(getApplicationContext());
		adapter.openDataBase();

		//
		String query = "SELECT sum(call_duration_Seconds) FROM tbl_calls_track";
		Cursor cursor = adapter.selectRecordsFromDB(query, null);
		if (cursor.moveToFirst()) {
			do {
				totalTimeOnCalls = cursor.getInt(0);
			} while (cursor.moveToNext());
		}
		cursor.close();
		query = "SELECT COUNT(id) FROM tbl_calls_track";
		cursor = adapter.selectRecordsFromDB(query, null);
		if (cursor.moveToFirst()) {
			do {
				totalCalls = cursor.getInt(0);
			} while (cursor.moveToNext());
		}
		cursor.close();
		// Total Closings
		query = "SELECT COUNT(id) FROM tbl_stats where tracking_status = '"
				+ CallHangUpActivity.ContactStatus.SHOTDOWN + "'";
		cursor = adapter.selectRecordsFromDB(query, null);
		if (cursor.moveToFirst()) {
			do {
				totalClosings = cursor.getInt(0);
			} while (cursor.moveToNext());
		}
		cursor.close();
		// Total Appointments
		query = "SELECT COUNT(id) FROM tbl_stats where tracking_status = '"
				+ CallHangUpActivity.ContactStatus.APPOINTMENT + "'";
		cursor = adapter.selectRecordsFromDB(query, null);
		if (cursor.moveToFirst()) {
			do {
				totalAppts = cursor.getInt(0);
			} while (cursor.moveToNext());
		}
		cursor.close();
	}
}
