package com.call.tracker.voicenotes;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.call.tracker.BaseActivity;
import com.call.tracker.R;
import com.call.tracker.adapter.MainContactAdapterView;
import com.call.tracker.database.DBAdapter;
import com.call.tracker.model.ContactModel;
import com.call.tracker.model.ListManagerModel;
import com.call.tracker.model.VoiceNotesModel;

import java.util.ArrayList;
import java.util.Collections;

public class SelectContactOfGroupActivity extends BaseActivity {

    TextView textType;
    ListView listContact;
    private ArrayList<ContactModel> arrayListContactDatas = new ArrayList<ContactModel>();
    private ArrayList<ContactModel> mainListContactDatas = new ArrayList<ContactModel>();
    private MainContactAdapterView mAdapter;
    private EditText editTextSearch;
    private String typeString;
    // , voicePath = "", urgentString = "",
    // allGroupdata = "";
    private ArrayList<ListManagerModel> modelList;
    private DBAdapter dbAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_main_contact);

        initControl();
    }

    @SuppressWarnings("unchecked")
    private void initControl() {
        // TODO Auto-generated method stub
        dbAdapter = new DBAdapter(getApplicationContext());
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            typeString = bundle.getString("type");
            // allGroupdata = bundle.getString("alldata");
            modelList = (ArrayList<ListManagerModel>) bundle
                    .getSerializable("listdata");
        }
        // allGroupdata
        textType = (TextView) findViewById(R.id.textType);
        listContact = (ListView) findViewById(R.id.listContact);

        startDialogBar();
        getNumbers(modelList);

        ContactLoadingAsync loadingAsync = new ContactLoadingAsync(
                getApplicationContext(), new ContactLoadingCompletedListener() {
            public void onCompleted(
                    ArrayList<ContactModel> contactDatas1) {
                Collections.sort(arrayListContactDatas);
                assignSeparatorPositions(arrayListContactDatas);
                Collections.sort(mainListContactDatas);
                assignSeparatorPositions(mainListContactDatas);
                setdataToAdapter("");
            }
        });
        loadingAsync.execute("");

        // Collections.sort(arrayListContactDatas);
        // assignSeparatorPositions(arrayListContactDatas);

        editTextSearch = (EditText) findViewById(R.id.editTextSearch);
        editTextSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2,
                                      int arg3) {
                setdataToAdapter(cs.toString());
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

    protected void setdataToAdapter(String val) {
        // TODO Auto-generated method stub

        updateListView(val);
        mAdapter = new MainContactAdapterView(this, arrayListContactDatas);
        listContact.setAdapter(mAdapter);
        closeDialogBar();

    }

    private void updateListView(String string) {
        ArrayList<ContactModel> backupArray = new ArrayList<ContactModel>();
        // backupArray.addAll(arrayListContactDatas);

        for (int i = 0; i < mainListContactDatas.size(); i++) {
            String name = mainListContactDatas.get(i).getName();
            if (name.toUpperCase().contains(string.toUpperCase()))
                backupArray.add(mainListContactDatas.get(i));
            // }
        }
        arrayListContactDatas.clear();

        arrayListContactDatas.addAll(backupArray);
    }

    private void assignSeparatorPositions(ArrayList<ContactModel> items) {
        boolean[] separatorAtIndex = new boolean[items.size()];
        char currentChar = 0;
        for (int i = 0; i < items.size(); i++) {
            char c = Character.toUpperCase(items.get(i).getName().charAt(0));
            if (c != currentChar) {
                separatorAtIndex[i] = true;
            } else {
                separatorAtIndex[i] = false;
            }
            char temp = items.get(i).getName().charAt(0);
            currentChar = Character.toUpperCase(temp);
        }
    }

    public void getNumbers(ArrayList<ListManagerModel> modelList2) {
        dbAdapter.openDB(getApplicationContext());
        dbAdapter.openDataBase();

        for (int j = 0; j < modelList2.size(); j++) {
            if (modelList2.get(j).getIsCheck().equalsIgnoreCase("1")) {
                Toast.makeText(
                        this,
                        "List contacts of group " + modelList2.get(j).getName(),
                        Toast.LENGTH_LONG).show();
                ArrayList<ContactModel> a = dbAdapter
                        .getContactsOfGroup(modelList2.get(j).getId());
                for (int i = 0; i < a.size(); i++) {
                    String name = a.get(i).getName();
                    // get display name
                    String phoneNumber = a.get(i).getNumber1();
                    // String id = a.get(i).getContactId();
                    a.get(i).setCheck(false);
                    // get phone number
                    System.out.println(".................." + name + "---> "
                            + phoneNumber);
                    arrayListContactDatas.add(a.get(i));
                    mainListContactDatas.add(a.get(i));
                }
            }
        }
        dbAdapter.close();
    }

    public void callSave(View view) {
        // typeString
        dbAdapter.openDB(getApplicationContext());

        ArrayList<VoiceNotesModel> voiceNotesModels = new ArrayList<VoiceNotesModel>();
        ArrayList<ContactModel> mArrayList = mAdapter.mListManagerModels;

        for (int index = 0; index < modelList.size(); index++) {
            if (modelList.get(index).getIsCheck().equals("1")) {
                for (int i = 0; i < mArrayList.size(); i++) {
                    if (mArrayList.get(i).isCheck()) {
                        VoiceNotesModel voiceModel = new VoiceNotesModel();
                        voiceModel.setContact_id(mArrayList.get(i)
                                .getContactId());
                        voiceModel.setContact_name(mArrayList.get(i).getName());
                        voiceModel.setContact_number(mArrayList.get(i)
                                .getNumber1());
                        voiceModel
                                .setGroup_name(modelList.get(index).getName());
                        voiceModel.setUrgent(preferences.getString(URGENT, ""));
                        voiceModel.setVoice_time(preferences.getString(
                                VOICE_TIME, ""));
                        voiceModel.setVoice_path(preferences.getString(
                                FILEPATH, ""));
                        voiceModel.setGroupId(modelList.get(index).getId());
                        voiceModel.setIsVisible("1");
                        voiceNotesModels.add(voiceModel);
                    }
                }
            }
        }
        dbAdapter.insertVoiceNote(voiceNotesModels);

        if (typeString.equals("voice"))
            preToast("Voice-note recorded");
        else
            preToast("Contact Manager saved");

        gotoHome();
    }

}
