package com.call.tracker.setting;

import java.util.Calendar;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.call.tracker.BaseActivity;
import com.call.tracker.R;
import com.call.tracker.database.DBAdapter;

public class SalesMissionActivity extends BaseActivity {
	private DBAdapter dbAdapter;
	int mission_homes;
	int mission_appointments;
	int mission_avg_hoursperday;
	int mission_avg_dayperweek;
	EditText et_mission_homes;
	EditText et_mission_appointments;
	EditText et_avg_mission_appointments;
	EditText et_mission_avg_hoursperday;
	EditText et_mission_avg_dayperweek;
	static String IS_GOAL_SET = "SalesMissionActivity_IS_GOAL_SET";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_sales_mission);
		dbAdapter = DBAdapter.getDBAdapterInstance(getApplicationContext());
		initControl();
	}

	private void initControl() {
		dbAdapter.openDataBase();
		/**
		 * CREATE TABLE "tbl_sales_goals_mission" ("id" INTEGER PRIMARY KEY NOT
		 * NULL , "mission_month" INTEGER, "mission_year" INTEGER,
		 * "mission_homes" INTEGER, "mission_appointments" INTEGER,
		 * "mission_avg_hoursperday" INTEGER, "mission_avg_dayperweek" INTEGER)
		 */
		Calendar calendar = Calendar.getInstance();
		String query = "select * from tbl_sales_goals_mission where mission_month = '"
				+ (calendar.get(Calendar.MONTH) + 1)
				+ "' AND mission_year = '"
				+ calendar.get(Calendar.YEAR) + "'";
		Log.d("callTracker", query);
		Cursor cursor = dbAdapter.selectRecordsFromDB(query, null);
		if (cursor.getCount() > 0) {
			cursor.moveToNext();
			mission_homes = cursor.getInt(cursor
					.getColumnIndex("mission_homes"));
			mission_appointments = cursor.getInt(cursor
					.getColumnIndex("mission_appointments"));
			mission_avg_hoursperday = cursor.getInt(cursor
					.getColumnIndex("mission_avg_hoursperday"));
			mission_avg_dayperweek = cursor.getInt(cursor
					.getColumnIndex("mission_avg_dayperweek"));
			cursor.close();
		}
		dbAdapter.close();
		et_mission_homes = (EditText) findViewById(R.id.etHomes);
		et_mission_appointments = (EditText) findViewById(R.id.etAppointments);
		et_avg_mission_appointments = (EditText) findViewById(R.id.etAvgAppointments);
		et_mission_avg_hoursperday = (EditText) findViewById(R.id.etHoursPerDay);
		et_mission_avg_dayperweek = (EditText) findViewById(R.id.etDaysPerWeek);
		et_mission_homes.setText(mission_homes + "");
		et_mission_appointments.setText(mission_appointments + "");
		et_mission_avg_hoursperday.setText(mission_avg_hoursperday + "");
		et_mission_avg_dayperweek.setText(mission_avg_dayperweek + "");
		et_avg_mission_appointments.setText((mission_appointments / 12) + "");
	}

	public void saveSalesMission(View v) {
		dbAdapter.addUpdateSalesMission(Integer.parseInt(et_mission_homes
				.getText().toString()), Integer
				.parseInt(et_mission_appointments.getText().toString()),
				Integer.parseInt(et_mission_avg_hoursperday.getText()
						.toString()), Integer
						.parseInt(et_mission_avg_dayperweek.getText()
								.toString()));

		updatePref(IS_GOAL_SET, true);
		finish();
	}
}
