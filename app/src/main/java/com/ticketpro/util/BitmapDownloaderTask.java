package com.ticketpro.util;

import java.io.FileOutputStream;
import java.lang.ref.WeakReference;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

public class BitmapDownloaderTask extends AsyncTask<String, Void, Bitmap> {

    private final WeakReference<ImageView> imageViewReference;
    private CallbackHandler callback;
    
    public BitmapDownloaderTask(ImageView imageView) {
        imageViewReference = new WeakReference<ImageView>(imageView);
    }
    
    public CallbackHandler getCallback() {
		return callback;
	}

	public void setCallback(CallbackHandler callback) {
		this.callback = callback;
	}

	public WeakReference<ImageView> getImageViewReference() {
		return imageViewReference;
	}


	@Override
    protected Bitmap doInBackground(String... params) {
    	Bitmap bitmap=TPUtility.downloadBitmap(params[0]);
    	
    	FileOutputStream out;
    	try{
    		out = new FileOutputStream(TPUtility.getLPRImagesFolder()+params[1]);
//            assert bitmap != null;
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
        }catch (Exception e) {
    		Log.e("BitmapDownloaderTask", TPUtility.getPrintStackTrace(e));
    	}
    	
    	return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if(callback!=null){
        	callback.success("Done");
        }
        
    	if (isCancelled()) {
            bitmap = null;
        }

        if (imageViewReference != null) {
            ImageView imageView = imageViewReference.get();
            if (imageView != null && bitmap!=null) {
                imageView.setImageBitmap(bitmap);


            }
        }
    }
}