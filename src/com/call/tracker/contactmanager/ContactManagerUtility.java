package com.call.tracker.contactmanager;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.call.tracker.database.DBAdapter;
import com.call.tracker.model.ContactModel;

import java.io.IOException;

public class ContactManagerUtility {

    private DBAdapter mDbAdapter;
    public SharedPreferences preferences;

    public boolean addContactInDB(Context context, Intent contactData) {
        ContactModel contactModel = new ContactModel();
        boolean isSuccess = false;
        // Process Data
        // Uri contactData = data.getData();
        ContentResolver cr = context.getContentResolver();
        Cursor c = cr.query(contactData.getData(), null, null, null, null);
        if (c.moveToFirst()) {
            String id = c.getString(c
                    .getColumnIndexOrThrow(ContactsContract.Contacts._ID));
            String hasPhone = c
                    .getString(c
                            .getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
            if (hasPhone.equalsIgnoreCase("1")) {
                Cursor phones = cr.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                        + " = " + id, null, null);
                phones.moveToFirst();
                String cNumber = phones
                        .getString(phones
                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                String nameContact = c
                        .getString(c
                                .getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
                contactModel.setContactId(id);
                contactModel.setName(nameContact);
                contactModel.setNumber1(cNumber);
                contactModel.addGroup(TempHolder.selectedGroup);
                contactModel.setUri(contactData.getData());
                openDB(context);
                insertNewContact(contactModel);
                isSuccess = true;
                phones.close();
            }
        }
        c.close();
        return isSuccess;
    }

    private void insertNewContact(ContactModel contactModel) {
        // TODO Auto-generated method stub
        mDbAdapter.openDataBase();
        // String query = "select * from tbl_group";
        ContentValues values = new ContentValues();
        values.put("contact_id", contactModel.getContactId());
        values.put("contact_name", contactModel.getName());
        values.put("contact_number", contactModel.getNumber1());
        values.put("contact_uri", contactModel.getNumber1());
        values.put("grp_id", contactModel.getGroup().get(0));
        mDbAdapter.getMyDatabase().insert("tbl_contacts", null, values);
        mDbAdapter.close();

    }

    private void openDB(Context context) {
        mDbAdapter = DBAdapter.getDBAdapterInstance(context);
        try {
            mDbAdapter.createDataBase();
        } catch (IOException e) {
        }
    }
}
