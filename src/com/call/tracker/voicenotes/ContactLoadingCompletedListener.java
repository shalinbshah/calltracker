package com.call.tracker.voicenotes;

import java.util.ArrayList;

import com.call.tracker.model.ContactModel;

public interface ContactLoadingCompletedListener {
    public void onCompleted(ArrayList<ContactModel> contactDatas);
}
