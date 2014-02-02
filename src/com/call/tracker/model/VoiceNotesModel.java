package com.call.tracker.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class VoiceNotesModel implements Serializable {

	// dbAdapter.insertVoiceNote(voice_path, group_name, contact_name,
	// contact_number, contact_id, urgent);
	private String voice_path, dateTime, isVisible, voice_time;
	private int id, contact_id, urgent, groupId, alarm_id;

	public int getAlarm_id() {
		return alarm_id;
	}

	public void setAlarm_id(int alarm_id) {
		this.alarm_id = alarm_id;
	}

	public String getVoice_path() {
		return voice_path;
	}

	public void setVoice_path(String voice_path) {
		this.voice_path = voice_path;
	}

	public int getContact_id() {
		return contact_id;
	}

	public void setContact_id(int contact_id) {
		this.contact_id = contact_id;
	}

	public int getUrgent() {
		return urgent;
	}

	public void setUrgent(int urgent) {
		this.urgent = urgent;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setIsVisible(String isVisible) {
		this.isVisible = isVisible;
	}

	public String getIsVisible() {
		return isVisible;
	}

	public void setVoice_time(String voice_time) {
		this.voice_time = voice_time;
	}

	public String getVoice_time() {
		return voice_time;
	}
}
