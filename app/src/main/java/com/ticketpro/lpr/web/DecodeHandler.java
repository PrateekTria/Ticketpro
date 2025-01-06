package com.ticketpro.lpr.web;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Display;
import android.view.Surface;

import com.ticketpro.parking.R;
import com.ticketpro.lpr.PlanarYUVLuminanceSource;
import com.ticketpro.lpr.camera.CameraManager;
import com.ticketpro.lpr.camera.CameraManager.CameraMode;

final class DecodeHandler extends Handler {

	private final LPRActivityScreen activity;
	private boolean running = true;
	private Bitmap bitmap;

	DecodeHandler(LPRActivityScreen activity) {
		this.activity = activity;
	}

	@Override
	public void handleMessage(Message message) {
		if (!running) {
			return;
		}

		switch (message.what) {
		case R.id.preview_decode:
			previewDecode((byte[]) message.obj, message.arg1, message.arg2);
			break;
			
		case R.id.quit:
			running = false;
			Looper.myLooper().quit();
			break;
		}
	}

	private void previewDecode(byte[] data, int width, int height) {
		PlanarYUVLuminanceSource source;
		CameraManager cameraManager = activity.cameraManager;
		
		// Rotate Image in Portrait Orientation
		Display display = this.activity.getWindowManager().getDefaultDisplay();
		int screenWidth = display.getWidth();
		int screenHeight = display.getHeight();
		if (screenWidth < screenHeight) {
			byte[] rotatedData = new byte[data.length];
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					rotatedData[x * height + height - y - 1] = data[x + y * width];
				}
			}

			int tmp = width;
			width = height;
			height = tmp;
			source = activity.getCameraManager().buildLuminanceSource(rotatedData, width, height);
		} else {
			source = activity.getCameraManager().buildLuminanceSource(data, width, height);
		}

		if (source == null) {
			sendPreviewFailMessage();
			return;
		}

		if(cameraManager.cameraMode == CameraMode.AUTO_LPR) {
			bitmap = source.renderCroppedBitmap();
		}else {
			bitmap = source.renderCroppedGreyscaleBitmap();
		}
		
		Handler handler = activity.getHandler();
		if (handler == null) {
			return;
		}

		try {
			if (display.getRotation() == Surface.ROTATION_270) {
				Matrix matrix = new Matrix();
				matrix.postRotate(180);
				
				bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
			}

			Message message = Message.obtain(handler, R.id.preview_decode_succeeded, bitmap);
			message.sendToTarget();
		
		} catch (NullPointerException e) {
			activity.stopHandler();
		}
	}

	private void sendPreviewFailMessage() {
		Handler handler = activity.getHandler();
		
		if (handler != null) {
			Message message = Message.obtain(handler, R.id.preview_decode_failed, null);
			message.sendToTarget();
		}
	}
}
