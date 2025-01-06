package com.ticketpro.parking.activity;

import java.io.InputStream;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.ticketpro.model.PrintTemplate;
import com.ticketpro.model.Ticket;
import com.ticketpro.parking.R;
import com.ticketpro.util.TPUtility;

/**
 * Home screen (Ref. Mockups).
 * 
 * @author : TicketPRO
 * @version : 1.0
 * @since : 1.0 
 *
 */

public class PrintPreviewActivity extends BaseActivityImpl{

	private Ticket activeTicket;
	private WebView webview;
	private TextView ticketsTextview;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		try{   
			super.onCreate(savedInstanceState);
	        setContentView(R.layout.print_preview);
	        setLogger(PrintPreviewActivity.class.getName());
	        setActiveScreen(this);
			
	        activeTicket=TPApp.getActiveTicket();
	        if(activeTicket==null){
	        	TPUtility.showErrorToast(this, "Error loading ticket preview");
	        	finish();
	        }
	        
	        webview=(WebView)findViewById(R.id.preview_webview);
	        ticketsTextview=(TextView)findViewById(R.id.print_preview_count_textview);
	        
	        final String previewHTML;
	        if(activeTicket.getTicketViolations().size()==0){
	        	previewHTML="<h3>Please select violations</h3>";
	        }else{
	        	previewHTML=getPreviewHTML();
	        }

			WebSettings webSettings = webview.getSettings();
			webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
			webSettings.setJavaScriptEnabled(true);
			webSettings.setAllowContentAccess(true);
			webSettings.setAllowFileAccess(true);
			webSettings.setAllowFileAccessFromFileURLs(true);
			webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
			webSettings.setDomStorageEnabled(true);
			webSettings.setUseWideViewPort(true);
			webSettings.setLoadWithOverviewMode(true);
			webview.setVerticalScrollBarEnabled(true);
	        webview.loadUrl("file:///android_asset/printPreview.html");
	        webview.setWebViewClient(new WebViewClient(){
	            @Override
	            public void onPageFinished(WebView view, String url) {
	            	view.loadUrl("javascript:loadHTML(\'"+previewHTML+"\')");
	            }
	        });

	        ticketsTextview.setText(activeTicket.getTicketViolations().size()+" Tickets");
	    
		}catch(Exception e){
			log.error(TPUtility.getPrintStackTrace(e)); 
		} 
    }    
	
	public void bindDataAtLoadingTime(){
		
	}
	
	private String getPreviewHTML(){
		String templateHTML=getPrintTemplate();
	    templateHTML=TPUtility.parseTicketViolations(templateHTML, activeTicket);
	    templateHTML=templateHTML.replaceAll("\n", "");
	    templateHTML=templateHTML.replaceAll("\r", "");
	    templateHTML=templateHTML.replaceAll("\t", "");
	    
	    return templateHTML;
	}
	
	private String getPrintTemplate(){
		
		String html="";
		try{
			PrintTemplate template=PrintTemplate.getPrintTemplateByName("PrintPreview");
			if(template!=null){
				return template.getTemplateData();
			}
			
			InputStream is = getAssets().open("previewTemplate.html");
	        int size = is.available();
	
	        byte[] buffer = new byte[size];
	        is.read(buffer);
	        is.close();
	        
	        html=new String(buffer);
		}catch(Exception ignored){}
		
		return html;
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
		setResult(RESULT_OK);
		finish();
	}

	@Override
	public void handleVoiceInput(String text) {
		if(text==null) return;
		
		Toast.makeText(this, text, Toast.LENGTH_LONG).show();
		if(text.contains("BACK") || text.contains("GO BACK") || text.contains("CLOSE")){
			backAction(null);
		}
		
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
