package com.call.tracker.contactmanager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.call.tracker.BaseActivity;
import com.call.tracker.R;

public class SelectExistingContact extends BaseActivity {

	CheckBox checkdontshow;
	public static final int CONTACT_PICKER_RESULT = 1001;
	public static final int GRP_PICKER_RESULT = 1002;
	Intent pickedContact;
	public SharedPreferences preferences;
	public SharedPreferences.Editor editor;
	public ProgressDialog progress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		preferences = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());

		if (preferences.getString(BaseActivity.IS_CONTACT_LIST1, "true")
				.equalsIgnoreCase("true")) {
			setContentView(R.layout.layout_con_list1);
			checkdontshow = (CheckBox) findViewById(R.id.checkdontshow);
		} else {
			Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
					ContactsContract.Contacts.CONTENT_URI);
			startActivityForResult(contactPickerIntent, CONTACT_PICKER_RESULT);
		}

	}

	public void callGot(View v) {
		preferences = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		editor = preferences.edit();
		editor.putString(BaseActivity.IS_CONTACT_LIST1,
				String.valueOf(checkdontshow.isSelected()));
		editor.commit();
		Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
				ContactsContract.Contacts.CONTENT_URI);
		startActivityForResult(contactPickerIntent, CONTACT_PICKER_RESULT);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case CONTACT_PICKER_RESULT:

				pickedContact = data;
				Intent intent = new Intent(this, AlbumsListPopUp.class);
				intent.putExtra("selectedContact", data.getData());
				TempHolder.pickedContact = data;
				startActivity(intent);
				finish();
				break;
			case GRP_PICKER_RESULT:
				ContactManagerUtility utility = new ContactManagerUtility();
				if (utility.addContactInDB(this,
						pickedContact.putExtras(data.getExtras()))) {
					Toast.makeText(getApplicationContext(),
							"Contact Added Successfully !!!",
							Toast.LENGTH_SHORT).show();
					finish();
				} // else {
					// Toast.makeText(getApplicationContext(),
					// "Contact Add UnSuccessfull !!!", Toast.LENGTH_SHORT)
					// .show();
					// finish();
					// }
					// break;
			}
		} else {
			Toast.makeText(getApplicationContext(),
					"Warning: activity result not ok", Toast.LENGTH_SHORT)
					.show();
			finish();
			Log.w("CallTracker", "Warning: activity result not ok");
		}
	}
}
