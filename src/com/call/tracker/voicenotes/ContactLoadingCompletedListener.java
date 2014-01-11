package com.call.tracker.voicenotes;

import java.util.ArrayList;

import com.call.tracker.model.ContactData;

public interface ContactLoadingCompletedListener {
	public void onCompleted(ArrayList<ContactData> contactDatas);
}
