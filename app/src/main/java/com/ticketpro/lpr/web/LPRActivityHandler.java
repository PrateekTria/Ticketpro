package com.ticketpro.lpr.web;

import com.ticketpro.lpr.camera.CameraManager;
import com.ticketpro.parking.R;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

final class LPRActivityHandler extends Handler {

	private static final String TAG = LPRActivityHandler.class.getSimpleName();

	private final LPRActivityScreen activity;
	private final DecodeThread decodeThread;
	private static State state;
	private final CameraManager cameraManager;

	private enum State {
		PREVIEW, PREVIEW_PAUSED, SUCCESS, DONE
	}

	LPRActivityHandler(LPRActivityScreen activity, CameraManager cameraManager) {
		this.activity = activity;
		this.cameraManager = cameraManager;
		
		cameraManager.startPreview();
		decodeThread = new DecodeThread(activity);
		decodeThread.start();
		restartPreview();
	}

	@Override
	public void handleMessage(final Message message) {
		switch (message.what) {
		case R.id.restart_preview:
			restartPreview();
			break;

		case R.id.preview_decode_succeeded:
			state = State.SUCCESS;
			activity.handlePreviewDecode((Bitmap) message.obj);
			break;

		case R.id.preview_decode_failed:
			state = State.PREVIEW;
			Toast toast = Toast.makeText(activity.getBaseContext(), "Failed to capture LPR Image. Please try again.",Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.TOP, 0, 120);
			toast.show();
			break;
		}
	}

	void stop() {
		Log.d(TAG, "Setting state to PREVIEW_PAUSED.");
		state = State.PREVIEW_PAUSED;
		removeMessages(R.id.ocr_decode);
	}

	void resetState() {
		if (state == State.PREVIEW_PAUSED) {
			Log.d(TAG, "Setting state to PREVIEW");
			state = State.PREVIEW;
			restartPreviewAndDecode();
		}
	}

	void quitSynchronously() {
		state = State.DONE;
		if (cameraManager != null) {
			cameraManager.stopPreview();
		}

		try {
			decodeThread.join(500L);
		} catch (InterruptedException e) {
			Log.w(TAG, "Caught InterruptedException in quitSyncronously()", e);
		} catch (RuntimeException e) {
			Log.w(TAG, "Caught RuntimeException in quitSyncronously()", e);
		} catch (Exception e) {
			Log.w(TAG, "Caught unknown Exception in quitSynchronously()", e);
		}

		removeMessages(R.id.ocr_decode_succeeded);
		removeMessages(R.id.ocr_decode_failed);
	}

	/**
	 * Start the preview, but don't try to LPR anything until the user presses
	 * the shutter button.
	 */
	private void restartPreview() {
		if (state == State.SUCCESS) {
			state = State.PREVIEW;
			activity.drawViewfinder();
		}
	}

	/**
	 * Send a decode request for realtime LPR mode
	 */
	private void restartPreviewAndDecode() {
		cameraManager.startPreview();
		cameraManager.requestOcrDecode(decodeThread.getHandler(), R.id.preview_decode);
		activity.drawViewfinder();
	}

	/**
	 * Request LPR on the current preview frame.
	 */
	private void previewDecode() {
		state = State.PREVIEW_PAUSED;
		
		cameraManager.requestOcrDecode(decodeThread.getHandler(), R.id.preview_decode);
	}

	/**
	 * Request OCR when the hardware shutter button is clicked.
	 */
	void hardwareShutterButtonClick() {
		if (state == State.PREVIEW) {
			previewDecode();
		}
	}

	/**
	 * Request OCR when the on-screen shutter button is clicked.
	 */
	void shutterButtonClick() {
		previewDecode();
	}

}
