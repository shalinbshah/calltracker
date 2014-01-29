package com.call.tracker.voicenotes;

import java.io.IOException;
import java.util.ArrayList;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.call.tracker.BaseActivity;
import com.call.tracker.R;
import com.call.tracker.adapter.VoiceNotesMainAdapter;
import com.call.tracker.database.DBAdapter;
import com.call.tracker.model.VoiceNotesModel;

public class VoiceListActivity extends BaseActivity {

    ListView listVoiceList;
    private DBAdapter dbAdapter;
    private ArrayList<VoiceNotesModel> voiceNotesModels = new ArrayList<VoiceNotesModel>();
    private VoiceNotesMainAdapter adapter;
    private TextView textViewNodata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_voice_activity_list);
        initControl();
    }

    @Override
    protected void onResume() {
        openDB();
        getdataFromDbAndCloseDb();
        super.onResume();
    }

    public void callNewNote(View v) {
        startActivity(new Intent(getApplicationContext(),
                NewNotesActivity.class));
    }

    private void initControl() {
        // TODO Auto-generated method stub
        listVoiceList = (ListView) findViewById(R.id.listVoiceList);
        textViewNodata = (TextView) findViewById(R.id.textNodata);
        openDB();
        getdataFromDbAndCloseDb();
    }

    private void getdataFromDbAndCloseDb() {
        // TODO Auto-generated method stub
        dbAdapter.openDataBase();

        String query = "select * from tbl_voice_note where isVis =1";
        Cursor cursor = dbAdapter.selectRecordsFromDB(query, null);
        voiceNotesModels.clear();

        int mCount = cursor.getCount();
        if (mCount == 0) {
            textViewNodata.setVisibility(View.VISIBLE);
            listVoiceList.setVisibility(View.GONE);
        } else {
            textViewNodata.setVisibility(View.GONE);
            listVoiceList.setVisibility(View.VISIBLE);
            if (cursor.moveToFirst()) {
                do {
                    VoiceNotesModel modelNotes = new VoiceNotesModel();
                    modelNotes.setId(cursor.getInt(0));
                    modelNotes.setVoice_path(cursor.getString(1));
                    modelNotes.setGroupId(cursor.getString(2));
                    modelNotes.setGroup_name(cursor.getString(3));
                    modelNotes.setContact_name(cursor.getString(4));
                    modelNotes.setContact_number(cursor.getString(5));
                    modelNotes.setContact_id(cursor.getString(6));
                    modelNotes.setUrgent(cursor.getString(7));
                    modelNotes.setDateTime(cursor.getString(8));
                    modelNotes.setIsVisible(cursor.getString(9));
                    voiceNotesModels.add(modelNotes);
                } while (cursor.moveToNext());
            }
        }
        dbAdapter.close();

        adapter = new VoiceNotesMainAdapter(this, voiceNotesModels);
        listVoiceList.setAdapter(adapter);

        listVoiceList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                Intent intent = new Intent(getApplicationContext(),
                        VoiceDetailsActivity.class);
                intent.putExtra("data", voiceNotesModels.get(arg2));
                startActivity(intent);
            }
        });
    }

    public void openDB() {
        dbAdapter = DBAdapter.getDBAdapterInstance(this);
        try {
            dbAdapter.createDataBase();
        } catch (IOException e) {
        }
    }
}
