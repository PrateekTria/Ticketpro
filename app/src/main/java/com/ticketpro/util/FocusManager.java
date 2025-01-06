package com.ticketpro.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;

public final class FocusManager implements Camera.AutoFocusCallback {

  private static final String TAG = FocusManager.class.getSimpleName();

  private static final long AUTO_FOCUS_INTERVAL_MS = 3500L;
  private static final Collection<String> FOCUS_MODES_CALLING_AF;
  static {
    FOCUS_MODES_CALLING_AF = new ArrayList<String>(2);
    FOCUS_MODES_CALLING_AF.add(Camera.Parameters.FOCUS_MODE_AUTO);
    FOCUS_MODES_CALLING_AF.add(Camera.Parameters.FOCUS_MODE_MACRO);
  }

  private boolean active;
  private boolean manual;
  private final boolean useAutoFocus;
  private final Camera camera;
  private final Timer timer;
  private TimerTask outstandingTask;

  public FocusManager(Context context, Camera camera) {
    this.camera = camera;
    timer = new Timer(true);
    String currentFocusMode = camera.getParameters().getFocusMode();
    useAutoFocus=FOCUS_MODES_CALLING_AF.contains(currentFocusMode);
    Log.i(TAG, "Current focus mode '" + currentFocusMode + "'; use auto focus? " + useAutoFocus);
    manual = false;
    checkAndStart();
  }

  @Override
  public synchronized void onAutoFocus(boolean success, Camera theCamera) {
    if (active && !manual) {
      outstandingTask = new TimerTask() {
        @Override
        public void run() {
          checkAndStart();
        }
      };
      timer.schedule(outstandingTask, AUTO_FOCUS_INTERVAL_MS);
    }
    manual = false;
  }

  void checkAndStart() {
  	if (useAutoFocus) {
  	  active = true;
      start();
    }
  }

  synchronized void start() {
	 try{
		  camera.autoFocus(this);
	 } catch (RuntimeException re) {
		  Log.w(TAG, "Unexpected exception while focusing", re);
	 }
  }

  /**
   * Performs a manual auto-focus after the given delay.
   * @param delay Time to wait before auto-focusing, in milliseconds
   */
  synchronized void start(long delay) {
  	outstandingTask = new TimerTask() {
  		@Override
  		public void run() {
  			manual = true;
  			start();
  		}
  	};
  	timer.schedule(outstandingTask, delay);
  }

  synchronized void stop() {
    if (useAutoFocus) {
      camera.cancelAutoFocus();
    }
    
    if (outstandingTask != null) {
      outstandingTask.cancel();
      outstandingTask = null;
    }
    
    active = false;
    manual = false;
  }

}
