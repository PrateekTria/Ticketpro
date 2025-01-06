package com.ticketpro.parking.activity.handlers;

import java.util.concurrent.CountDownLatch;

import com.ticketpro.parking.activity.TPApplication;
import com.ticketpro.parking.activity.WriteTicketActivity;
import com.ticketpro.parking.bl.WriteTicketBLProcessor;

import android.os.Looper;
import android.util.Log;

public class LookupThread extends Thread {

	private LookupHandler handler;
	private final WriteTicketActivity activity;
	private final CountDownLatch handlerInitLatch;

	public LookupThread(WriteTicketActivity activity) {
		this.activity = activity;
		handlerInitLatch = new CountDownLatch(1);
	}

	public LookupHandler getHandler() {
		try {
			activity.setBLProcessor(new WriteTicketBLProcessor((TPApplication) activity.getApplicationContext()));
			handlerInitLatch.await();
		} catch (InterruptedException ie) {
			//Log.e("LookupThread", ie.getMessage());
			Log.e("LookupThread", "LookupThread exception");
		}

		return handler;
	}

	@Override
	public void run() {
		Looper.prepare();
		handler = new LookupHandler(activity);
		handlerInitLatch.countDown();
		Looper.loop();
	}
}
