package com.ticketpro.model;

public class ServiceResult {
	
	public static final String SUCCESSFULL="Successfull";
	public static final String NOT_ACTIVE="NotActive";
	public static final String SERVICE_ERROR="ServiceError";
	public static final String DATA_NOT_AVAILABLE="DataNotAvailable";
	
	private boolean result;
	private String resultCode;
	private String message;
	private Object data;
	
	public boolean isResult() {
		return result;
	}
	public void setResult(boolean result) {
		this.result = result;
	}
	public String getResultCode() {
		return resultCode;
	}
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
}
