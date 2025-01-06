package com.ticketpro.model;

import org.json.JSONException;
import org.json.JSONObject;

public class RemoteAction {
	private String task;
	private String params;
	
	public RemoteAction() {
	
	}
	
	public RemoteAction(JSONObject object) throws JSONException{
		this.setTask(!object.isNull("task") ? object.getString("task") : null);
		this.setParams(!object.isNull("params") ? object.getString("params") : null);
	}

	public String getTask() {
		return task;
	}

	public void setTask(String task) {
		this.task = task;
	}
	
	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

}
