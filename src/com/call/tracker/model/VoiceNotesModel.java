package com.call.tracker.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class VoiceNotesModel implements Serializable {

	// dbAdapter.insertVoiceNote(voice_path, group_name, contact_name,
	// contact_number, contact_id, urgent);
	private String voice_path, group_name, contact_name, contact_number,
			contact_id, urgent, groupId, dateTime, isVisible, voice_time;
	private int id;

	public String getVoice_path() {
		return voice_path;
	}

	public void setVoice_path(String voice_path) {
		this.voice_path = voice_path;
	}

	public String getGroup_name() {
		return group_name;
	}

	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}

	public String getContact_name() {
		return contact_name;
	}

	public void setContact_name(String contact_name) {
		this.contact_name = contact_name;
	}

	public String getContact_number() {
		return contact_number;
	}

	public void setContact_number(String contact_number) {
		this.contact_number = contact_number;
	}

	public String getContact_id() {
		return contact_id;
	}

	public void setContact_id(String contact_id) {
		this.contact_id = contact_id;
	}

	public String getUrgent() {
		return urgent;
	}

	public void setUrgent(String urgent) {
		this.urgent = urgent;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getGroupId() {
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
