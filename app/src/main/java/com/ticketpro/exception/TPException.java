package com.ticketpro.exception;

public class TPException extends Exception {
	private static final long serialVersionUID = 1L;
	public static final int DB_SYNC_ERROR=100;
	public static final int NETWORK_IO_ERROR=101;
	public static final int GENERIC_ERROR=102;
	
	private String errorMessage;
	private int errorCode;
	
	public TPException(){
		this.errorCode=GENERIC_ERROR;
	}
	
	public TPException(int errorCode, String errorMessage){
		this.errorCode=errorCode;
		this.errorMessage=errorMessage;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
}
