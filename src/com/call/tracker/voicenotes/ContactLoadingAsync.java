package com.call.tracker.voicenotes;

import android.content.Context;
import android.os.AsyncTask;

import com.call.tracker.model.ContactModel;

import java.util.ArrayList;

public class ContactLoadingAsync extends
        AsyncTask<String, String, ArrayList<ContactModel>> {
    private ArrayList<ContactModel> arrayListContactDatas;
    private ContactLoadingCompletedListener listener;
    public String groupID;

    // private ProgressDialog mProgressDialog;

    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    /**
     * @param mContext
     * @param listener
     */
    public ContactLoadingAsync(Context mContext,
                               ContactLoadingCompletedListener listener) {
        super();
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        arrayListContactDatas = new ArrayList<ContactModel>();
    }

    @Override
    protected void onPostExecute(ArrayList<ContactModel> result) {
        super.onPostExecute(result);
        // mProgressDialog.dismiss();
        if (listener != null) {
            listener.onCompleted(arrayListContactDatas);
        }
    }

    @Override
    protected ArrayList<ContactModel> doInBackground(String... params) {
        return null;
    }

}
