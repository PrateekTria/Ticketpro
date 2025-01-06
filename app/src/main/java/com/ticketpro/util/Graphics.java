package com.ticketpro.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

public class Graphics {
	public static Bitmap scaleDownBitmap(Bitmap originalImage, float maxImageSize, boolean filter) {
		float ratio = Math.min((float) maxImageSize / originalImage.getWidth(),
				(float) maxImageSize / originalImage.getHeight());
		
		int width = (int) Math.round(ratio * (float) originalImage.getWidth());
		int height = (int) Math.round(ratio * (float) originalImage.getHeight());

		Bitmap newBitmap = Bitmap.createScaledBitmap(originalImage, width, height, filter);
		return newBitmap;
	}

	public static Bitmap scaleBitmap(Bitmap originalImage, int newWidth, int newHeight) {
		Bitmap output = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		Matrix m = new Matrix();
		m.setScale((float) newWidth / originalImage.getWidth(), (float) newHeight / originalImage.getHeight());
		
		canvas.drawBitmap(originalImage, m, new Paint());
		return output;
	}
}