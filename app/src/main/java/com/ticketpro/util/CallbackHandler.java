package com.ticketpro.util;

public interface CallbackHandler {
	public void success(String data);
	public void failure(String message);
}