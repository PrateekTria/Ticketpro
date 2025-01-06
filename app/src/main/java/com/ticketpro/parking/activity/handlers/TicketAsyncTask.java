package com.ticketpro.parking.activity.handlers;

import android.os.Handler;

import com.ticketpro.parking.activity.WriteTicketActivity;

public class TicketAsyncTask extends Handler {
	private WriteTicketActivity activity;

	public TicketAsyncTask(WriteTicketActivity activity) {
		this.activity = activity;
	}
}