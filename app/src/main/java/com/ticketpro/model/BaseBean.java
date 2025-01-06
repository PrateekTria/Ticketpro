package com.ticketpro.model;

import com.ticketpro.exception.TPException;

public abstract class BaseBean {

	private TPException appException;
	private int resultStatus;
	private String statusMessage;

	public TPException getAppException() {
		return appException;
	}

	public void setAppException(TPException appException) {
		this.appException = appException;
	}

	public int getResultStatus() {
		return resultStatus;
	}

	public void setResultStatus(int resultStatus) {
		this.resultStatus = resultStatus;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}
	
	
	
}
