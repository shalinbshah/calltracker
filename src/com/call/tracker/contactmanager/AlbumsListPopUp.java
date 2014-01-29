package com.call.tracker.contactmanager;

import java.io.IOException;
import java.util.ArrayList;

import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.call.tracker.BaseActivity;
import com.call.tracker.R;
import com.call.tracker.adapter.MyProgressDialog;
import com.call.tracker.database.DBAdapter;
import com.call.tracker.model.ListManagerModel;

public class AlbumsListPopUp extends BaseActivity {

    public static String colID;
    public static String collectionName;

    /**
     * Called when the activity is first created.
     */
    public ArrayList<ListManagerModel> listCollectionDetails;
    private DBAdapter dbAdapter;

    static BaseAdapter albumsListAdapter;
    ListView listView;
    private MyProgressDialog progressDialog;

    public class GetCollectionListFromWebService extends
            AsyncTask<Void, Void, String[]> {

        @Override
        protected void onPreExecute() {
            listCollectionDetails.clear();
            albumsListAdapter.notifyDataSetChanged();
            progressDialog = new MyProgressDialog(AlbumsListPopUp.this);
            progressDialog.show();
        }

        @Override
        protected String[] doInBackground(Void... params) {
            initializeContactGrps();
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            super.onPostExecute(result);
            albumsListAdapter.notifyDataSetChanged();
            progressDialog.dismiss();
        }

    }

    private int initializeContactGrps() {

        listCollectionDetails.clear();
        loadDatabase();
        return 100;
    }

    private void loadDatabase() {
        openDB();
        // TODO Auto-generated method stub
        dbAdapter.openDataBase();

        String query = "select * from list_manager where isVis =1";
        Cursor cursor = dbAdapter.selectRecordsFromDB(query, null);
        listCollectionDetails.clear();
        if (cursor.moveToFirst()) {
            do {
                ListManagerModel listManagerModel = new ListManagerModel();
                listManagerModel.setId(String.valueOf(cursor.getInt(0)));
                listManagerModel.setName(cursor.getString(1));
                listManagerModel.setIsVis(cursor.getString(2));
                listCollectionDetails.add(listManagerModel);
                System.out.println(listManagerModel);
            } while (cursor.moveToNext());
        }

        dbAdapter.close();
    }

    public void openDB() {
        // dbAdapter =

        dbAdapter = DBAdapter.getDBAdapterInstance(this);
        try {
            dbAdapter.createDataBase();
        } catch (IOException e) {
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        setTitle("Select Group");
        View viewToLoad = LayoutInflater.from(this).inflate(
                R.layout.albums_list_popup, null);
        setContentView(viewToLoad);
        setTitle(collectionName);
        listCollectionDetails = new ArrayList<ListManagerModel>();
        listCollectionDetails.clear();

        albumsListAdapter = new CollectionListAdapter(this, getIntent(),
                listCollectionDetails);
        listView = (ListView) findViewById(R.id.listbox_deals);
        listView.setAdapter(albumsListAdapter);
        listView = (ListView) findViewById(R.id.listbox_deals);

        listView.setCacheColorHint(Color.TRANSPARENT);
        listView.setClickable(false);
        listView.setAdapter(albumsListAdapter);
        new GetCollectionListFromWebService().execute();
        // listView.setOnTouchListener(swipeDetector);
    }
}
