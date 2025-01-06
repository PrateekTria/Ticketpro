package com.ticketpro.lpr.web;

import java.util.concurrent.CountDownLatch;

import android.os.Handler;
import android.os.Looper;

final class DecodeThread extends Thread {
	private final LPRActivityScreen activity;
	private final CountDownLatch handlerInitLatch;
	private Handler handler;

	DecodeThread(LPRActivityScreen activity) {
		this.activity = activity;
		
		handlerInitLatch = new CountDownLatch(1);
	}

	Handler getHandler() {
		try {
			handlerInitLatch.await();
		} catch (InterruptedException ie) {}
		
		return handler;
	}

	@Override
	public void run() {
		Looper.prepare();
		handler = new DecodeHandler(activity);
		handlerInitLatch.countDown();
		Looper.loop();
	}
}
