package com.call.tracker.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ListManagerModel implements Serializable {

	private String id, name, isVis,isCheck;

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

	public void setIsVis(String isVis) {
		this.isVis = isVis;
	}

	public String getIsVis() {
		return isVis;
	}

	public void setIsCheck(String isCheck) {
		this.isCheck = isCheck;
	}

	public String getIsCheck() {
		return isCheck;
	}
}
