package com.call.tracker.calllist;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.call.tracker.BaseActivity;
import com.call.tracker.R;
import com.call.tracker.adapter.CallListAdapter;
import com.call.tracker.contactmanager.ContactManagerMainActivity;
import com.call.tracker.database.DBAdapter;
import com.call.tracker.model.CallListModel;
import com.call.tracker.model.CallUserModel;
import com.call.tracker.model.ListManagerModel;
import com.call.tracker.model.VoiceNotesModel;

public class CallListActivity extends BaseActivity {
	protected static final String TAG = "CallListActivity";
	private ListView listContact;
	private CallListAdapter adapter;
	private ArrayList<CallListModel> arrayList = new ArrayList<CallListModel>();
	private ArrayList<CallUserModel> callUserModels = new ArrayList<CallUserModel>();
	private EditText editTextSearch;
	private DBAdapter mDbAdapter;
	private Button butAdd;
	private ArrayList<VoiceNotesModel> mVoiceArrayList = new ArrayList<VoiceNotesModel>();
	private ArrayList<ListManagerModel> mListManagerModels = new ArrayList<ListManagerModel>();
	private Button butContactList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_call_list);

		openDB();
		initControl();
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			int val = bundle.getInt("key");
			if (val == 1)
				butAdd.setVisibility(View.VISIBLE);
		}
	}

	// public void startApp() {
	// // TODO Auto-generated method stub
	// final Handler handler = new Handler();
	// handler.postDelayed(new Runnable() {
	// @Override
	// public void run() {
	// readContacts("");
	// initControl();
	// }
	// }, 1000);
	//
	// }

	private void initControl() {
		// TODO Auto-generated method stub
		updatePref(CURRENT_LIST, "0");

		butContactList = (Button)findViewById(R.id.butContactList);
		butContactList.setText("All contacts");
		butAdd = (Button) findViewById(R.id.butAdd);

		listContact = (ListView) findViewById(R.id.listContact);
		listContact.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int arg2, long arg3) {

				CallListModel callListModel = new CallListModel();
				callListModel = (CallListModel) adapterView.getAdapter()
						.getItem(arg2);

				Intent intent = new Intent(getApplicationContext(),
						DetailActivity.class);
				intent.putExtra("callData", callListModel);
				startActivity(intent);

			}
		});
		mDbAdapter.openDataBase();
		// mDbAdapter.deleteTableContact();
		// mVoiceArrayList = mDbAdapter.getVoiceData();
		mVoiceArrayList = mDbAdapter.getVoiceData();
		mListManagerModels = mDbAdapter.getListManagerdata();
		mDbAdapter.close();
		generateCallList("", -1);

		editTextSearch = (EditText) findViewById(R.id.editTextSearch);
		editTextSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3) {
				int currentVal = Integer.valueOf(preferences.getString(
						CURRENT_LIST, "-1"));
				generateCallList(cs.toString(), currentVal);

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			@Override
			public void afterTextChanged(Editable arg0) {
			}
		});
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void generateCallList(String string, int listId) {
		// TODO Auto-generated method stub

		StringBuffer sb = new StringBuffer();
		Cursor managedCursor = getContentResolver().query(
				CallLog.Calls.CONTENT_URI, null, null, null, null);
		int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
		int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
		int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
		int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
		int nameVal = managedCursor.getColumnIndex(CallLog.Calls.CACHED_NAME);
		int id = managedCursor.getColumnIndex(CallLog.Calls._ID);
		// int nameValM = managedCursor.getColumnIndex(CallLog.Calls.);

		sb.append("Call Details :");

		arrayList.clear();
		while (managedCursor.moveToNext()) {
			String phNumber = managedCursor.getString(number);
			String callType = managedCursor.getString(type);
			String callDate = managedCursor.getString(date);
			Date callDayTime = new Date(Long.valueOf(callDate));
			String callDuration = managedCursor.getString(duration);
			String name = managedCursor.getString(nameVal);
			String contactId = managedCursor.getString(id);
			if (name == null)
				name = "";
			String dir = null;
			int dircode = Integer.parseInt(callType);
			switch (dircode) {

			case CallLog.Calls.OUTGOING_TYPE:
				dir = "OUTGOING";
				break;

			case CallLog.Calls.INCOMING_TYPE:
				dir = "INCOMING";
				break;

			case CallLog.Calls.MISSED_TYPE:
				dir = "MISSED";
				break;
			}
			sb.append("\nPhone Number:--- " + phNumber + " \nCall Type:--- "
					+ dir + " \nCall Date:--- " + callDayTime
					+ " \nCall duration in sec :--- " + callDuration);
			sb.append("\n----------------------------------");

			CallListModel callListModel = new CallListModel();
			callListModel.setId(contactId);
			callListModel.setName(name);
			callListModel.setDate(callDayTime.toString());
			callListModel.setDuration(callDuration);
			callListModel.setNumber(phNumber);
			callListModel.setType(callType);

			if (name.toUpperCase().contains(string.toUpperCase())
					|| phNumber.contains(string))

				for (int i = 0; i < mVoiceArrayList.size(); i++) {

					if (PhoneNumberUtils.compare(phNumber,
							mVoiceArrayList.get(i).getContact_number()))
						if (listId == -1)
							arrayList.add(callListModel);
						else if (mVoiceArrayList.get(i).getGroupId()
								.equals(String.valueOf(listId)))
							arrayList.add(callListModel);
						else {

						}
				}
		}
		managedCursor.close();
		Collections.reverse(arrayList);
		Set<String> set = new HashSet(arrayList);
		Log.d(TAG, "---->Data->      " + set.size());
		adapter = new CallListAdapter(this, arrayList);
		listContact.setAdapter(adapter);

	}

	public void callAdd(View view) {
		startActivity(new Intent(getApplicationContext(),
				ContactManagerMainActivity.class));
	}

	public void callContactList(View view) {
		//
		int currentVal = Integer.valueOf(preferences.getString(CURRENT_LIST,
				"0"));
		AlertDialog.Builder builderListChoiceDialog = new AlertDialog.Builder(
				this);
		final String[] strings = new String[mListManagerModels.size() + 1];
		for (int i = 0; i < mListManagerModels.size(); i++) {
			if (i == 0)
				strings[0] = "All contacts";
			strings[i + 1] = mListManagerModels.get(i).getName();
		}

		// final CharSequence[] array = { "All contacts", "FSBO list",
		// "Open House Guest", "Expired List", "Other" };
		builderListChoiceDialog.setTitle(R.string.select_call_list)
				.setSingleChoiceItems(strings, currentVal,
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int position) {
								updatePref(CURRENT_LIST,
										String.valueOf(position));
								dialog.dismiss();
								int val = position;
								if (position == 0) {
									val = -1;
								} else
									val = Integer.valueOf(mListManagerModels
											.get(position - 1).getId());
								generateCallList("", val);
								butContactList.setText(strings[position]);
							}
						});
		AlertDialog listChoiceDialog = builderListChoiceDialog.create();
		listChoiceDialog.setCanceledOnTouchOutside(true);
		listChoiceDialog.show();
	}

	// @SuppressLint("InlinedApi")
	// private void readContacts(String id) {
	//
	// mDbAdapter.openDataBase();
	// mDbAdapter.deleteTableContact();
	// mVoiceArrayList = mDbAdapter.getVoiceData();
	// // mListManagerModels = mDbAdapter.getListManagerdata();
	//
	// callUserModels.clear();
	// StringBuffer sb = new StringBuffer();
	// sb.append("......Contact Details.....");
	// ContentResolver cr = getContentResolver();
	// Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
	// ContactsContract.Contacts._ID + " = ?", new String[] { id },
	// null);
	// String phone = null;
	// String caller_id = null;
	// String emailContact = null;
	// String emailType = null;
	// String image_uri = "";
	// Bitmap bitmap = null;
	// if (cur.getCount() > 0) {
	// while (cur.moveToNext()) {
	// caller_id = cur.getString(cur
	// .getColumnIndex(ContactsContract.Contacts._ID));
	// String name = cur
	// .getString(cur
	// .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
	// image_uri = cur
	// .getString(cur
	// .getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
	//
	// mDbAdapter.updateUserContact(caller_id, name, image_uri);
	// if (Integer
	// .parseInt(cur.getString(cur
	// .getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
	// System.out
	// .println("name : " + name + ", ID : " + caller_id);
	// sb.append("\n Contact Name:" + name);
	// Cursor pCur = cr.query(
	// ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
	// null,
	// ContactsContract.CommonDataKinds.Phone.CONTACT_ID
	// + " = ?", new String[] { caller_id }, null);
	// while (pCur.moveToNext()) {
	// phone = pCur
	// .getString(pCur
	// .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
	// String phoneM = pCur
	// .getString(pCur
	// .getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
	// sb.append("\n Phone number:" + phone);
	//
	// String customLabel = pCur
	// .getString(pCur
	// .getColumnIndex(ContactsContract.CommonDataKinds.Phone.LABEL));
	// int phonetype = pCur
	// .getInt(pCur
	// .getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
	//
	// String phoneLabel = (String) ContactsContract.CommonDataKinds.Email
	// .getTypeLabel(this.getResources(), phonetype,
	// customLabel);
	//
	// mDbAdapter.updateUserNumber(caller_id, phone,
	// getNumberType(phonetype));
	//
	// updateUserDetails(caller_id, name, image_uri, phone,
	// getNumberType(phonetype), "", "");
	// // sb.append("\n Phone numberMMMM :" + phoneM);
	// System.out.println("phone" + phone);
	// System.out.println("numberMMMM" + phoneM);
	// System.out.println("numberMMMM---> " + phoneLabel);
	// }
	// pCur.close();
	// Cursor emailCur = cr.query(
	// ContactsContract.CommonDataKinds.Email.CONTENT_URI,
	// null,
	// ContactsContract.CommonDataKinds.Email.CONTACT_ID
	// + " = ?", new String[] { caller_id }, null);
	// while (emailCur.moveToNext()) {
	// emailContact = emailCur
	// .getString(emailCur
	// .getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
	// emailType = emailCur
	// .getString(emailCur
	// .getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));
	// sb.append("\nEmail:" + emailContact + "Email type:"
	// + emailType);
	// mDbAdapter.updateUserNumber(caller_id, emailContact,
	// emailType);
	// System.out.println("Email " + emailContact
	// + " Email Type : " + emailType);
	//
	// updateUserDetails(caller_id, name, image_uri, "", "",
	// emailContact, emailType);
	// }
	// emailCur.close();
	// }
	// if (image_uri != null) {
	// System.out.println(Uri.parse(image_uri));
	// try {
	// bitmap = MediaStore.Images.Media
	// .getBitmap(this.getContentResolver(),
	// Uri.parse(image_uri));
	// sb.append("\n Image in Bitmap:" + bitmap);
	// System.out.println(bitmap);
	// } catch (FileNotFoundException e) {
	// e.printStackTrace();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	// sb.append("\n........................................");
	// }
	// }
	// }

	private void openDB() {
		mDbAdapter = DBAdapter.getDBAdapterInstance(this);
		try {
			mDbAdapter.createDataBase();
		} catch (IOException e) {
		}
	}

	// private String getNumberType(int phonetype) {
	// // TODO Auto-generated method stub
	// switch (phonetype) {
	// case Phone.TYPE_MOBILE:
	// return "Mobile";
	// case Phone.TYPE_WORK:
	// return "Work";
	// case Phone.TYPE_HOME:
	// return "Home";
	// case Phone.TYPE_MAIN:
	// return "Main";
	// case Phone.TYPE_FAX_WORK:
	// return "Work Fax";
	// case Phone.TYPE_FAX_HOME:
	// return "Home Fax";
	// case Phone.TYPE_PAGER:
	// return "Pager";
	// case Phone.TYPE_OTHER:
	// return "Other";
	// case Phone.TYPE_CUSTOM:
	// return "Custom";
	// default:
	// break;
	// }
	// return "Other";
	// }

	public void updateUserDetails(String callerId, String name, String image,
			String number, String number_type, String email, String email_type) {
		CallUserModel callUserModel = new CallUserModel();
		callUserModel.setCallerId(callerId);
		callUserModel.setName(name);
		callUserModel.setImage(image);
		callUserModel.setNumber(number);
		callUserModel.setNumber_type(number_type);
		callUserModel.setEmail(email);
		callUserModel.setEmail_type(email_type);
		callUserModels.add(callUserModel);
	}
	// ArrayList<String> phones = new ArrayList<String>();
	//
	// Cursor cursor = mContentResolver.query(
	// CommonDataKinds.Phone.CONTENT_URI,
	// null,
	// CommonDataKinds.Phone.CONTACT_ID +" = ?",
	// new String[]{id}, null);
	//
	// while (cursor.moveToNext())
	// {
	// phones.add(cursor.getString(cursor.getColumnIndex(CommonDataKinds.Phone.NUMBER)));
	// }
	//
	// cursor.close();
	// return(phones);
	// Read more:
	// http://www.androidhub4you.com/2013/06/get-phone-contacts-details-in-android_6.html#ixzz2mPqwCkZw
}
