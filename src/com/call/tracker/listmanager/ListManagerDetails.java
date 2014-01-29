package com.call.tracker.listmanager;

import java.io.IOException;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.call.tracker.BaseActivity;
import com.call.tracker.R;
import com.call.tracker.database.DBAdapter;
import com.call.tracker.model.ListManagerModel;

public class ListManagerDetails extends BaseActivity {
    private ListView listViewContactsAndGroup;
    private DBAdapter dbAdapter;
    public final static String GROUP_ID_KEY = "contactGrpID";
    private ArrayList<ListManagerModel> arrayList = new ArrayList<ListManagerModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_list_manager_details);
        openDB();
        initControl();
    }

    private void initControl() {
        // TODO Auto-generated method stub

        listViewContactsAndGroup = (ListView) findViewById(R.id.listviewList);
        // listViewContactsAndGroup.setAdapter( new ListManagerAdapter(this,
        // listCollectionDetails);
        listViewContactsAndGroup.setAdapter(null);
        final Button btnAddMore = new Button(this);
        btnAddMore.setText("Add New List");
        listViewContactsAndGroup.addFooterView(btnAddMore);
        btnAddMore.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                openDialog("Add New Group", 0, "", 0);
            }
        });

        loadDatabase();
    }

    private void loadDatabase() {
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
                arrayList.add(listManagerModel);
            } while (cursor.moveToNext());
        }

        dbAdapter.close();

        ListManagerAdapter adapter = new ListManagerAdapter(this, arrayList);
        listViewContactsAndGroup.setAdapter(adapter);

        listViewContactsAndGroup
                .setOnItemLongClickListener(new OnItemLongClickListener() {

                    @Override
                    public boolean onItemLongClick(AdapterView<?> arg0,
                                                   View arg1, int arg2, long arg3) {
                        deleteUpdateListItem(Integer.parseInt(arg1.getTag()
                                .toString()));
                        return false;
                    }
                });
        listViewContactsAndGroup
                .setOnLongClickListener(new OnLongClickListener() {

                    @Override
                    public boolean onLongClick(View v) {
                        deleteUpdateListItem(Integer.parseInt(v.getTag()
                                .toString()));
                        return false;
                    }
                });
    }

    protected void deleteUpdateListItem(final int pos) {
        // TODO Auto-generated method stub
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Choose Option");
        alert.setMessage("Select any 1");

        // Set an EditText view to get user input
        alert.setPositiveButton("Rename",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        openDialog("Update value", 1, arrayList.get(pos)
                                .getName(), Integer.valueOf(arrayList.get(pos)
                                .getId()));
                    }
                });

        alert.setNegativeButton("Delete",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        openDeleteDialog(arrayList.get(pos).getId(), arrayList
                                .get(pos).getName());
                    }
                });

        alert.show();
    }

    protected void openDeleteDialog(final String id, String name) {
        // TODO Auto-generated method stub
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Erase List");
        alert.setMessage("Want to erase " + name);

        // Set an EditText view to get user input
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                deleteList(id);
            }
        });

        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });

        alert.show();
    }

    protected void deleteList(String id) {
        // TODO Auto-generated method stub
        dbAdapter.openDataBase();

        // String query = "select * from tbl_group";
        dbAdapter.deleteList(Integer.valueOf(id));
        // dbAdapter.insertList(value);

        dbAdapter.close();

        loadDatabase();
    }

    protected void openDialog(String message, final int val, String edittext,
                              final int id) {
        // TODO Auto-generated method stub
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("List Manager");
        alert.setMessage(message);

        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        alert.setView(input);
        input.setText(edittext);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();
                // Do something with value!
                if (val == 0) {
                    if (value.trim().length() == 0) {
                    } else {
                        saveToDb(value);
                    }
                } else {
                    updateInDb(id, value);
                }
            }

        });

        alert.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });

        alert.show();
    }

    protected void updateInDb(int id, String value) {
        // TODO Auto-generated method stub
        dbAdapter.openDataBase();

        // String query = "select * from tbl_group";
        dbAdapter.updateList(id, value);
        // dbAdapter.insertList(value);

        dbAdapter.close();

        loadDatabase();
    }

    protected void saveToDb(String value) {
        // TODO Auto-generated method stub
        dbAdapter.openDataBase();

        // String query = "select * from tbl_group";
        dbAdapter.insertList(value);

        dbAdapter.close();

        loadDatabase();
    }

    public void openDB() {
        // dbAdapter =

        dbAdapter = DBAdapter.getDBAdapterInstance(this);
        try {
            dbAdapter.createDataBase();
        } catch (IOException e) {
        }

    }
}
