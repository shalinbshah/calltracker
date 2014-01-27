package com.call.tracker.voicenotes;

import com.call.tracker.model.ContactModel;

import java.util.ArrayList;

public interface ContactLoadingCompletedListener {
    public void onCompleted(ArrayList<ContactModel> contactDatas);
}
