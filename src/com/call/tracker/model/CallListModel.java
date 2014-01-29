package com.call.tracker.model;

import android.net.Uri;

import java.io.Serializable;

@SuppressWarnings("serial")
public class CallListModel implements Serializable {

	private String id, name, number, date, type, duration;
	private Uri uri;

	public Uri getContactUri() {
		return uri;
	}

	public void setContactUri(Uri uri) {
		this.uri = uri;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}
}
