package com.ticketpro.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

import android.os.AsyncTask;

public class DownloadFilesTask extends AsyncTask<String, Integer, Void> {
	private CallbackHandler callback;
	
	protected Void doInBackground(String... urls) {
		if (urls.length < 2) {
			return null;
		}

		String fileURL = urls[0];
		String targetFile = urls[1];

		downloadFile(fileURL, targetFile);
		return null;
    }

	protected void downloadFile(String source, String target) {
		try {
			HttpGet httpget = new HttpGet(source);
			HttpResponse response = new TPHttpClient().execute(httpget);
			HttpEntity entity = response.getEntity();

			if (entity != null) {
				File targetFile = new File(target);
				BufferedInputStream bis = new BufferedInputStream(entity.getContent());
				BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(targetFile));

				int inByte;
				while((inByte = bis.read()) != -1) {
					bos.write(inByte);
				}

				bis.close();
				bos.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(callback!=null) {
			callback.success("Done");
		}
	}
	public CallbackHandler getCallback() {
		return callback;
 }
	public void setCallback(CallbackHandler callback) {
		this.callback = callback;
	}

}