package com.call.tracker.database;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import com.call.tracker.model.ContactModel;
import com.call.tracker.model.ListManagerModel;
import com.call.tracker.model.VoiceNotesModel;

public class DBAdapter extends SQLiteOpenHelper {

	private static String DB_PATH = "";
	private static final String DB_NAME = "call_tracker.db";
	private SQLiteDatabase myDataBase;
	private final Context myContext;

	private static DBAdapter mDBConnection;

	/**
	 * Constructor Takes and keeps a reference of the passed context in order to
	 * access to the application assets and resources.
	 * 
	 * @param context
	 */
	public DBAdapter(Context context) {
		super(context, DB_NAME, null, 1);
		this.myContext = context;
		DB_PATH = context.getDir("databases", Context.MODE_PRIVATE).getPath();
		// The Android's default system path of your application database is
		// "/data/data/mypackagename/databases/"
	}

	public SQLiteDatabase getMyDatabase() {
		return myDataBase;
	}

	/**
	 * getting Instance
	 * 
	 * @param context
	 * @return DBAdapter
	 */
	public static synchronized DBAdapter getDBAdapterInstance(Context context) {
		if (mDBConnection == null) {
			mDBConnection = new DBAdapter(context);
		}
		return mDBConnection;
	}

	/**
	 * Creates an empty database on the system and rewrites it with your own
	 * database.
	 **/
	public void createDataBase() throws IOException {

		boolean dbExist = checkDataBase();
		if (dbExist) {
			// do nothing - database already exist
		} else {
			this.getReadableDatabase();
			try {
				copyDataBase();
			} catch (IOException e) {
				throw new Error("Error copying database");
			}
		}
	}

	/**
	 * Check if the database already exist to avoid re-copying the file each
	 * time you open the application.
	 * 
	 * @return true if it exists, false if it doesn't
	 */
	private boolean checkDataBase() {
		SQLiteDatabase checkDB = null;
		try {
			String myPath = DB_PATH + DB_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.NO_LOCALIZED_COLLATORS);
		} catch (SQLiteException e) {
			// database does't exist yet.
		}
		if (checkDB != null) {
			checkDB.close();
		}
		return checkDB != null ? true : false;
	}

	/**
	 * Copies your database from your local assets-folder to the just created
	 * empty database in the system folder, from where it can be accessed and
	 * handled. This is done by transfering bytestream.
	 * */
	private void copyDataBase() throws IOException {
		// Open your local db as the input stream
		InputStream myInput = myContext.getAssets().open(DB_NAME);
		// Path to the just created empty db
		String outFileName = DB_PATH + DB_NAME;
		// Open the empty db as the output stream
		OutputStream myOutput = new FileOutputStream(outFileName);
		// transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}
		// Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();
	}

	/**
	 * Open the database
	 * 
	 * @throws SQLException
	 */
	public void openDataBase() throws SQLException {
		String myPath = DB_PATH + DB_NAME;
		myDataBase = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.NO_LOCALIZED_COLLATORS);
	}

	/**
	 * Close the database if exist
	 */
	@Override
	public synchronized void close() {
		if (myDataBase != null)
			myDataBase.close();
		super.close();
	}

	/**
	 * Call on creating data base for example for creating tables at run time
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
	}

	/**
	 * can used for drop tables then call onCreate(db) function to create tables
	 * again - upgrade
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

	// ----------------------- CRUD Functions ------------------------------

	/**
	 * This function used to select the records from DB.
	 * 
	 * @param tableName
	 * @param tableColumns
	 * @param whereClase
	 * @param whereArgs
	 * @param groupBy
	 * @param having
	 * @param orderBy
	 * @return A Cursor object, which is positioned before the first entry.
	 */
	public Cursor selectRecordsFromDB(String tableName, String[] tableColumns,
			String whereClase, String whereArgs[], String groupBy,
			String having, String orderBy) {
		return myDataBase.query(tableName, tableColumns, whereClase, whereArgs,
				groupBy, having, orderBy);
	}

	/**
	 * select records from db and return in list
	 * 
	 * @param tableName
	 * @param tableColumns
	 * @param whereClase
	 * @param whereArgs
	 * @param groupBy
	 * @param having
	 * @param orderBy
	 * @return ArrayList>
	 */
	/*
	 * public ArrayList selectRecordsFromDBList(String tableName, String[]
	 * tableColumns, String whereClase, String whereArgs[], String groupBy,
	 * String having, String orderBy) {
	 * 
	 * ArrayList retList = new ArrayList>(); ArrayList list = new ArrayList();
	 * Cursor cursor = myDataBase.query(tableName, tableColumns, whereClase,
	 * whereArgs, groupBy, having, orderBy); if (cursor.moveToFirst()) {
	 */
	/*
	 * do { list = new ArrayList(); for(int i=0; ) }
	 */

	/**
	 * This function used to update the Record in DB.
	 * 
	 * @param tableName
	 * @param initialValues
	 * @param whereClause
	 * @param whereArgs
	 * @return 0 in case of failure otherwise return no of row(s) are updated
	 */
	public int updateRecordsInDB(String tableName, ContentValues initialValues,
			String whereClause, String whereArgs[]) {
		return myDataBase.update(tableName, initialValues, whereClause,
				whereArgs);
	}

	/**
	 * This function used to delete the Record in DB.
	 * 
	 * @param tableName
	 * @param whereClause
	 * @param whereArgs
	 * @return 0 in case of failure otherwise return no of row(s) are deleted.
	 */
	public int deleteRecordInDB(String tableName, String whereClause,
			String[] whereArgs) {
		return myDataBase.delete(tableName, whereClause, whereArgs);
	}

	// --------------------- Select Raw Query Functions ---------------------

	/**
	 * apply raw Query
	 * 
	 * @param query
	 * @param selectionArgs
	 * @return Cursor
	 */
	public Cursor selectRecordsFromDB(String query, String[] selectionArgs) {
		return myDataBase.rawQuery(query, selectionArgs);
	}

	public long insertGroup(String mName, String imagePath) {
		ContentValues values = new ContentValues();
		values.put("name", mName);
		values.put("image_path", imagePath);

		return myDataBase.insert("tbl_group", null, values); // Line 91
	}

	public long insertUser(int mName, String imagePath) {
		ContentValues values = new ContentValues();
		values.put("group_id", mName);
		values.put("user_id", imagePath);
		return myDataBase.insert("tbl_User_list", null, values);
	}

	public long updateGroupInfo(int groupId, String name, String imagePath) {
		// TODO Auto-generated method stub

		ContentValues cv = new ContentValues();
		cv.put("name", name);
		cv.put("image_path", imagePath);

		return myDataBase.update("tbl_group", cv, "id = " + groupId, null);
	}

	public long insertNotification(String message) {
		String currentDateTimeString = DateFormat.getDateTimeInstance().format(
				new Date());
		ContentValues values = new ContentValues();
		values.put("message", message);
		values.put("isClick", "false");
		values.put("isView", "false");
		values.put("dateVal", currentDateTimeString);
		return myDataBase.insert("tbl_notification", null, values);
	}

	public long insertList(String message) {
		ContentValues values = new ContentValues();
		values.put("name", message);
		values.put("isVis", "1");
		return myDataBase.insert("list_manager", null, values);
	}

	// list_manager
	public long updateList(int id, String name) {
		ContentValues cv = new ContentValues();
		cv.put("name", name);

		return myDataBase.update("list_manager", cv, "id = " + id, null);
	}

	public long deleteList(int id) {
		ContentValues cv = new ContentValues();
		cv.put("isVis", "0");

		return myDataBase.update("list_manager", cv, "id = " + id, null);
	}

	public void deleteContact(String contact_id) {
		openDataBase();
		myDataBase.delete("tbl_contacts", "contact_id = ?",
				new String[] { contact_id });
		close();
	}

	public void updateOrAddStats(String contact_id, String status) {
		openDataBase();
		ContentValues cv = new ContentValues();
		cv.put("contact_id", contact_id);
		cv.put("tracking_status", status);
		int i = myDataBase.update("tbl_stats", cv, "contact_id = ?",
				new String[] { contact_id });
		if (i <= 0) {
			myDataBase.insert("tbl_stats", null, cv);
		}
		close();

	}

	public long updateNotificationClick(int id) {
		// TODO Auto-generated method stub
		ContentValues cv = new ContentValues();
		cv.put("isClick", "true");
		// cv.put("isView", "true");
		// cv.put("name", name);
		// cv.put("image_path", imagePath);

		return myDataBase.update("tbl_notification", cv,
				"id = " + String.valueOf(id), null);
	}

	public long updateNotificationClickView() {
		// TODO Auto-generated method stub
		ContentValues cv = new ContentValues();
		cv.put("isView", "true");

		return myDataBase.update("tbl_notification", cv, "0 = 0", null);
	}

	public long updateContactFrequency(String contctID, String freq) {
		openDataBase();
		ContentValues cv = new ContentValues();
		cv.put("freq", freq);
		long l = myDataBase.update("tbl_contacts", cv, "contact_id = ?",
				new String[] { contctID });
		close();
		return l;
	}

	public long updateUserContact(String id, String name, String image_uri) {
		// TODO Auto-generated method stub
		ContentValues values = new ContentValues();
		values.put("caller_id", id);
		values.put("name", name);
		values.put("image", image_uri);

		return myDataBase.insert("tbl_contact_phone", null, values);
	}

	public long updateUserNumber(String caller_id, String phone,
			String numberType) {
		// TODO Auto-generated method stub
		ContentValues values = new ContentValues();
		values.put("caller_id", caller_id);
		values.put("number", phone);
		values.put("number_type", numberType);

		return myDataBase.insert("tbl_contact_number", null, values);
	}

	public long updateUserEmail(String caller_id, String email, String emailType) {
		// TODO Auto-generated method stub
		ContentValues values = new ContentValues();
		values.put("caller_id", caller_id);
		values.put("email", email);
		values.put("email_type", emailType);

		return myDataBase.insert("tbl_contact_email", null, values);
	}

	public void insertVoiceNote(ArrayList<VoiceNotesModel> notesModels) {
		openDataBase();
		String currentDateTimeString = DateFormat.getDateTimeInstance().format(
				new Date());
		for (int i = 0; i < notesModels.size(); i++) {
			ContentValues values = new ContentValues();
			values.put("voice_path", notesModels.get(i).getVoice_path());
			values.put("list_id", notesModels.get(i).getGroupId());
			values.put("contact_id", notesModels.get(i).getContact_id());
			values.put("urgent", notesModels.get(i).getUrgent());
			values.put("record_datetime", currentDateTimeString);
			values.put("record_duration", notesModels.get(i).getVoice_time());
			values.put("isVis", "1");

			myDataBase.insert("tbl_voice_notes", null, values);
		}
		close();

	}

	public void deleteTableContact() {
		myDataBase.execSQL("delete from tbl_contact_phone");
		myDataBase.execSQL("delete from tbl_contact_number");
		myDataBase.execSQL("delete from tbl_contact_email");
	}

	public ArrayList<VoiceNotesModel> getVoiceData() {
		String query = "select * from tbl_voice_notes";
		Cursor cursor = selectRecordsFromDB(query, null);
		ArrayList<VoiceNotesModel> arrayList = new ArrayList<VoiceNotesModel>();
		arrayList.clear();
		if (cursor.moveToFirst()) {
			do {
				VoiceNotesModel modelNotes = new VoiceNotesModel();
				modelNotes.setId(cursor.getInt(0));
				modelNotes.setVoice_path(cursor.getString(1));
				modelNotes.setUrgent(cursor.getInt(2));
				modelNotes.setDateTime(cursor.getString(3));
				modelNotes.setIsVisible(cursor.getString(4));
				modelNotes.setVoice_Duration(cursor.getString(5));
				modelNotes.setGroupId(cursor.getInt(6));
				modelNotes.setContact_id(cursor.getInt(7));
				modelNotes.setAlarm_id(cursor.getInt(8));
				arrayList.add(modelNotes);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return arrayList;
	}

	public ArrayList<ContactModel> getContacts() {
		String query = "select * from tbl_contacts";
		Cursor cursor = selectRecordsFromDB(query, null);
		ArrayList<ContactModel> arrayList = new ArrayList<ContactModel>();
		arrayList.clear();
		if (cursor.moveToFirst()) {
			do {
				ContactModel model = new ContactModel();
				model.setContactId(cursor.getString(cursor
						.getColumnIndex("contact_id")));
				model.setName(cursor.getString(cursor
						.getColumnIndex("contact_name")));
				model.setNumber1(cursor.getString(cursor
						.getColumnIndex("contact_number")));
				//
				query = "select name from list_manager where id = " + "'"
						+ cursor.getString(cursor.getColumnIndex("grp_id"))
						+ "'";
				Cursor cursor2 = selectRecordsFromDB(query, null);
				String grpName = null;
				if (cursor2.moveToFirst()) {
					do {
						grpName = cursor2.getString(cursor2
								.getColumnIndex("name"));
					} while (cursor2.moveToNext());
				}
				model.addGroup(grpName);
				Uri uri = getContactUriWithPhoneNumber(cursor.getString(cursor
						.getColumnIndex("contact_number")));
				model.setUri(uri);
				Log.e("CallTracker", "Uri of contact: " + uri);
				arrayList.add(model);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return arrayList;
	}

	public ArrayList<String> getContactsIDs() {
		openDataBase();
		String query = "select * from tbl_contacts";
		Cursor cursor = selectRecordsFromDB(query, null);
		ArrayList<String> arrayList = new ArrayList<String>();
		arrayList.clear();
		if (cursor.moveToFirst()) {
			do {
				arrayList.add(cursor.getString(cursor
						.getColumnIndex("contact_id")));
			} while (cursor.moveToNext());
		}
		cursor.close();
		close();
		return arrayList;
	}

	public void openDB(Context mContext) {
		DBAdapter dbAdapter = DBAdapter.getDBAdapterInstance(mContext);
		try {
			dbAdapter.createDataBase();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public Uri getContactUriWithPhoneNumber(String pNumber) {
		ContentResolver cr = myContext.getContentResolver();
		Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
				null, null, null);
		Log.d("callTracker", "Finding uri of contact : " + pNumber);
		if (cur.getCount() > 0) {
			while (cur.moveToNext()) {
				String id = cur.getString(cur
						.getColumnIndex(ContactsContract.Contacts._ID));
				if (Integer
						.parseInt(cur.getString(cur
								.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
					Cursor pCur = cr.query(
							ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
							null,
							ContactsContract.CommonDataKinds.Phone.CONTACT_ID
									+ " = ?", new String[] { id }, null);
					while (pCur.moveToNext()) {
						String phoneNo = pCur
								.getString(pCur
										.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
						if (phoneNo.toUpperCase().trim()
								.contains(pNumber.toUpperCase().trim())) {
							Uri my_contact_Uri = Uri.withAppendedPath(
									ContactsContract.Contacts.CONTENT_URI,
									String.valueOf(id));
							if (null != my_contact_Uri) {
								cur.close();
								pCur.close();
							}
							return my_contact_Uri;
						}
					}
					pCur.close();
				}
			}
			cur.close();
		}
		return null;
	}

	public ArrayList<ContactModel> getContactsOfGroup(String grpID) {
		// TODO change query to include group name
		String query = "select * from tbl_contacts where grp_id = '" + grpID
				+ "'";
		Cursor cursor = selectRecordsFromDB(query, null);
		ArrayList<ContactModel> arrayList = new ArrayList<ContactModel>();
		arrayList.clear();
		if (cursor.moveToFirst()) {
			do {
				ContactModel model = new ContactModel();
				model.setContactId(cursor.getString(cursor
						.getColumnIndex("contact_id")));
				model.setName(cursor.getString(cursor
						.getColumnIndex("contact_name")));
				model.setNumber1(cursor.getString(cursor
						.getColumnIndex("contact_number")));
				String grps = cursor.getString(cursor.getColumnIndex("grp_id"));
				for (String grp : grps.split(";")) {
					model.addGroup(grp);
				}
				Uri uri = getContactUriWithPhoneNumber(cursor.getString(cursor
						.getColumnIndex("contact_number")));
				model.setUri(uri);
				arrayList.add(model);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return arrayList;
	}

	public ArrayList<ListManagerModel> getListManagerdata() {
		// TODO Auto-generated method stub
		String query = "select * from list_manager where isVis =1";
		Cursor cursor = selectRecordsFromDB(query, null);
		ArrayList<ListManagerModel> arrayList = new ArrayList<ListManagerModel>();
		arrayList.clear();
		if (cursor.moveToFirst()) {
			do {
				ListManagerModel listManagerModel = new ListManagerModel();
				listManagerModel.setId(String.valueOf(cursor.getInt(0)));
				listManagerModel.setName(cursor.getString(1));
				listManagerModel.setIsVis(cursor.getString(2));
				arrayList.add(listManagerModel);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return arrayList;
	}

	public void updateContactFollowUpNotes(String contact_id, String followNotes) {
		openDataBase();
		ContentValues cv = new ContentValues();
		cv.put("followup_notes", followNotes);
		myDataBase.update("tbl_contacts", cv, "contact_id = ?",
				new String[] { contact_id });
		close();
	}

	public String getContactFollowUpNotes(String contact_id, String followNotes) {
		openDataBase();
		String notes = null;
		String query = "select * from tbl_contacts where contact_id = '"
				+ contact_id + "'";
		Cursor cursor = selectRecordsFromDB(query, null);
		ArrayList<ListManagerModel> arrayList = new ArrayList<ListManagerModel>();
		arrayList.clear();
		if (cursor.moveToFirst()) {
			do {
				notes = cursor.getString(cursor
						.getColumnIndex("followup_notes"));
			} while (cursor.moveToNext());
		}
		cursor.close();
		close();
		return notes;
	}

	/**
	 * CREATE TABLE "tbl_sales_goals_mission" ("id" INTEGER PRIMARY KEY NOT NULL
	 * , "mission_month" INTEGER, "mission_year" INTEGER, "mission_homes"
	 * INTEGER, "mission_appointments" INTEGER, "mission_avg_hoursperday"
	 * INTEGER, "mission_avg_dayperweek" INTEGER)
	 * 
	 * @param mission_homes
	 * @param mission_appointments
	 * @param mission_avg_hoursperday
	 * @param mission_avg_dayperweek
	 * @return
	 */
	public long addUpdateSalesMission(int i, int j, int k, int l) {
		openDataBase();
		ContentValues values = new ContentValues();
		Calendar calendar = Calendar.getInstance();
		values.put("mission_month", calendar.get(Calendar.MONTH) + 1);
		values.put("mission_year", calendar.get(Calendar.YEAR));
		values.put("mission_homes", i);
		values.put("mission_appointments", j);
		values.put("mission_avg_hoursperday", k);
		values.put("mission_avg_dayperweek", l);

		// check for already existence
		boolean alreadyExist = false;
		String query = "select * from tbl_sales_goals_mission where mission_month = '"
				+ (calendar.get(Calendar.MONTH) + 1)
				+ "' AND mission_year = '"
				+ calendar.get(Calendar.YEAR) + "'";
		Log.d("callTracker", query);
		Cursor cursor = selectRecordsFromDB(query, null);
		if (cursor.getCount() > 0) {
			alreadyExist = true;
		}
		if (alreadyExist) {
			long result = myDataBase.update("tbl_sales_goals_mission", values,
					"mission_month = ? AND mission_year = ?", new String[] {
							Integer.toString(calendar.get(Calendar.MONTH) + 1),
							Integer.toString(calendar.get(Calendar.YEAR)) });
			close();
			return result;

		} else {
			long result = myDataBase.insert("tbl_sales_goals_mission", null,
					values);
			return result;

		}
	}

}
