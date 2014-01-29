package com.call.tracker.voicenotes;

import java.io.IOException;
import java.util.ArrayList;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.ListView;

import com.call.tracker.BaseActivity;
import com.call.tracker.R;
import com.call.tracker.adapter.ListAdapterView;
import com.call.tracker.database.DBAdapter;
import com.call.tracker.model.ListManagerModel;

public class AssignContactToVoiceNoteActivity extends BaseActivity {
    private ListView listManager;
    private ListAdapterView adapter;
    private DBAdapter dbAdapter;
    private ArrayList<ListManagerModel> arrayList = new ArrayList<ListManagerModel>();
    private String typeString;
    private CheckBox checkAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_voice_list);

        initControl();
    }

    private void initControl() {
        // TODO Auto-generated method stub

        Bundle bundle = getIntent().getExtras();

        typeString = bundle.getString("type");

        listManager = (ListView) findViewById(R.id.listManager);

        checkAll = (CheckBox) findViewById(R.id.checkAll);
        checkAll.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (checkAll.isChecked())
                    adapter.selectAll("1");
                else
                    adapter.selectAll("0");
            }
        });

        openDB();
        getdataFromDb();
    }

    private void getdataFromDb() {
        // TODO Auto-generated method stub
        dbAdapter.openDataBase();

        String query = "select * from list_manager where isVis =1";
        Cursor cursor = dbAdapter.selectRecordsFromDB(query, null);
        arrayList.clear();

        // int mCount = cursor.getCount();
        if (cursor.moveToFirst()) {
            do {
                ListManagerModel listManagerModel = new ListManagerModel();
                listManagerModel.setId(String.valueOf(cursor.getInt(0)));
                listManagerModel.setName(cursor.getString(1));
                listManagerModel.setIsVis(cursor.getString(2));
                listManagerModel.setIsCheck("0");
                arrayList.add(listManagerModel);
            } while (cursor.moveToNext());
        }

        dbAdapter.close();

        adapter = new ListAdapterView(this, arrayList);
        listManager.setAdapter(adapter);

        listManager.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
            }
        });
    }

    public void openDB() {
        // dbAdapter =

        dbAdapter = DBAdapter.getDBAdapterInstance(this);
        try {
            dbAdapter.createDataBase();
        } catch (IOException e) {
        }
    }

    public void callReady(View view) {
        ArrayList<ListManagerModel> mArrayList = adapter.mArrayList;
        ArrayList<ListManagerModel> groupsSelected = new ArrayList<ListManagerModel>();
        int countVal = 0;
        for (int i = 0; i < mArrayList.size(); i++) {
            if (mArrayList.get(i).getIsCheck().equals("1")) {
                countVal++;
                groupsSelected.add(mArrayList.get(i));
            }
        }
        if (countVal == 0) {
            alertBox("Please select any contact group");
        } else {
            Intent intents = new Intent(getApplicationContext(),
                    SelectContactOfGroupActivity.class);
            intents.putExtra("type", typeString);
            intents.putExtra("listdata", groupsSelected);
            String alldata = "";
            if (checkAll.isChecked())
                alldata = "yes";
            else
                alldata = "no";
            intents.putExtra("alldata", alldata);
            startActivity(intents);
        }
    }

    public void updateCheck() {
        ArrayList<ListManagerModel> mArrayList = adapter.mArrayList;

        int countVal = 0;
        for (int i = 0; i < mArrayList.size(); i++) {
            if (mArrayList.get(i).getIsCheck().equals("1")) {
                countVal++;
            }
        }
        if (countVal == mArrayList.size())
            checkAll.setChecked(true);
        else
            checkAll.setChecked(false);
    }
}
