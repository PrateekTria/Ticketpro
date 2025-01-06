package com.ticketpro.parking.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

import com.ticketpro.parking.R;
import com.ticketpro.util.TPUtility;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

/**
 * Home screen (Ref. Mockups).
 * 
 * @author : TicketPRO
 * @version : 1.0
 * @since : 1.0 
 *
 */

public class PrintTestActivity extends BaseActivityImpl{

	private WebView webview;
	
	/**
	 * Entry point of the Activity
	 */
	@Override
    public void onCreate(Bundle savedInstanceState) {
		try{   
			super.onCreate(savedInstanceState);
	        setContentView(R.layout.print_test);
	        setLogger(PrintTestActivity.class.getName());
	        setActiveScreen(this);
			
	        webview=(WebView)findViewById(R.id.preview_webview);
	        WebSettings webSettings = webview.getSettings();
	        webSettings.setJavaScriptEnabled(true);
	        webview.setWebChromeClient(new WebChromeClient());
	        webview.loadUrl("file:///android_asset/printTest.html");
	        webview.setWebViewClient(new WebViewClient() {  
	            @Override  
	            public void onPageFinished(WebView view, String url) {
	            	//webview.loadUrl("javascript:loadPreview();");  
	           }  
	        });  
	        
	 	}catch(Exception e){
			log.error(TPUtility.getPrintStackTrace(e)); 
		} 
    }    
	
	public void bindDataAtLoadingTime(){
		
	}
	
	@Override
	public void onClick(View v) {
		
	}

	@Override
	public void onBackPressed() {
		setResult(RESULT_CANCELED);
		finish();
	}
	
	public void backAction(View view){
		setResult(RESULT_CANCELED);
		finish();
	}
	
	public void printAction(View view){
		try {
    		Picture pic=webview.capturePicture();
    		if(pic!=null){
        		Bitmap bmp = pictureDrawable2Bitmap(new PictureDrawable(pic)); 
        		String filename="PRINT-TEST-"+new Date().getTime()+".JPG";
        		File file = new File(TPUtility.getTicketsFolder(),filename);
        		
        		FileOutputStream out;
    			out = new FileOutputStream(file);
    	        bmp.compress(Bitmap.CompressFormat.JPEG, 90, out); 
    	        out.close();
    	        bmp.recycle();
    	        
    	        try{
        	        Uri printFile = Uri.fromFile(file);
        	        Intent i = new Intent(Intent.ACTION_VIEW);
        	        i.setPackage("com.dynamixsoftware.printershare");
        	        i.putExtra( "scaleFitToPage", true );
        	        i.setDataAndType(printFile,"image/jpeg");
        	        startActivity(i);
        	        
        	    }catch (ActivityNotFoundException e) {
        	    	
        	    	Toast.makeText(this, "PrintShare Application not installed. System will redirect to Google Market for installation.",Toast.LENGTH_LONG).show();
					Intent intent = new Intent(Intent.ACTION_VIEW); 
					intent.setData(Uri.parse("market://search?q=pname:com.dynamixsoftware.printershare")); 
					startActivity(intent);
        	    }
        	    
    	        return;
    	   }
	    } catch (Exception e) {
	    	log.error(TPUtility.getPrintStackTrace(e));
		}
	    
		setResult(RESULT_OK);
		finish();
	}
	
	
	private Bitmap pictureDrawable2Bitmap(PictureDrawable pictureDrawable){
        Bitmap bitmap = Bitmap.createBitmap(pictureDrawable.getIntrinsicWidth(),pictureDrawable.getIntrinsicHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawPicture(pictureDrawable.getPicture());
        return bitmap;
    }

	@Override
	public void handleVoiceInput(String text) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleVoiceMode(boolean voiceMode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleNetworkStatus(boolean connected,boolean isFastConnection) {
		// TODO Auto-generated method stub
		
	}
	
}
