package com.ticketpro.parking.activity;

import com.ticketpro.model.MaintenanceLog;
import com.ticketpro.model.MaintenancePicture;
import com.ticketpro.parking.R;
import com.ticketpro.parking.bl.CommonBLProcessor;
import com.ticketpro.util.CustomWebView;
import com.ticketpro.util.DateUtil;
import com.ticketpro.util.StringUtil;
import com.ticketpro.util.TPUtility;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

public class MaintenanceViewActivity extends BaseActivityImpl {
	private MaintenanceLog activeLog;
	private CustomWebView webview;
	private ProgressDialog progressDialog;
	private Handler dataLoadingHandler;
	private Handler errorHandler;
	private int logId;

	/**
	 * Entry point of the Activity
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);

			setContentView(R.layout.maintenance_view);
			setLogger(MaintenanceViewActivity.class.getName());
			setBLProcessor(new CommonBLProcessor((TPApplication) getApplicationContext()));
			setActiveScreen(this);

			isNetworkInfoRequired = true;
			
			Intent data = getIntent();
			logId = data.getIntExtra("LogId", 0);

			dataLoadingHandler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					if (progressDialog.isShowing()) {
						progressDialog.dismiss();
					}
					
					WebSettings webSettings = webview.getSettings();
					webSettings.setJavaScriptEnabled(true);
					webview.setWebChromeClient(new WebChromeClient());
					webview.loadUrl("file:///android_asset/printPreview.html");
					webview.setWebViewClient(new WebViewClient() {
						@Override
						public void onPageFinished(WebView view, String url) {
							String previewHTML = getPreviewHTML();
							view.loadUrl("javascript:loadHTML(\'" + previewHTML + "\','left')");
						}
					});
				}
			};

			errorHandler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					
					if (progressDialog.isShowing()) {
						progressDialog.dismiss();
					}
				}
			};

			webview = (CustomWebView) findViewById(R.id.preview_webview);
			
			bindDataAtLoadingTime();

		} catch (Exception e) {
			log.error(TPUtility.getPrintStackTrace(e));
		}
	}

	public void bindDataAtLoadingTime() {
		progressDialog = ProgressDialog.show(this, "", "Loading...");
		new Thread() {
			public void run() {
				try {
					activeLog = MaintenanceLog.getLogById(logId);
					dataLoadingHandler.sendEmptyMessage(1);
				} catch (Exception ae) {
					errorHandler.sendEmptyMessage(0);
				}
			}
		}.start();
	}

	@Override
	public void onClick(View v) {

	}

	@Override
	public void onBackPressed() {
		setResult(RESULT_OK);
		finish();
	}

	public void backAction(View view) {
		setResult(RESULT_OK);
		finish();
	}

	public void removeAction(View view) {
		if(activeLog==null){
			return;
		}
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Delete Confirmation")
	    .setMessage("Are you sure you want to maintenance log?")
	    .setCancelable(true).setNegativeButton("No", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		 })
		 .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				try {
					MaintenanceLog.removeById(activeLog.getLogId());
					setResult(RESULT_OK);

					finish();
					
				} catch (Exception ae) {
					log.error(ae);
				}
			}
		 });

		AlertDialog alert = builder.create();
		alert.show();
	}
	
	private String getPreviewHTML() {
		String templateHTML = TPUtility.getMaintenancePreviewTemplate(getApplicationContext());
		templateHTML = parseLog(templateHTML, activeLog);
		templateHTML = templateHTML.replaceAll("\n", "");
		templateHTML = templateHTML.replaceAll("\r", "");
		templateHTML = templateHTML.replaceAll("\t", "");

		return templateHTML;
	}
	
	private String parseLog(String templateHTML, MaintenanceLog log){
		String template = new String(templateHTML);
		try {
			template = template.replaceAll("\\{ITEM_NAME\\}", StringUtil.getDisplayString(log.getItemName()));
			template = template.replaceAll("\\{LOG_DATE\\}", DateUtil.getStringFromDate2(log.getLogDate()));
			template = template.replaceAll("\\{PROBLEM_TYPE\\}", StringUtil.getDisplayString(log.getProblemType()));
			template = template.replaceAll("\\{COMMENTS\\}", StringUtil.getDisplayString(log.getComments()));
			template = template.replaceAll("\\{LOCATION\\}", StringUtil.getDisplayString(log.getLocation()));
			
			if(!StringUtil.isEmpty(log.getLatitude())
					&& !StringUtil.isEmpty(log.getLongitude())){
				template = template.replaceAll("\\{MAPLINK\\}", "http://maps.google.com/maps?q="
						+ log.getLatitude() + "," + log.getLongitude());

				template = template.replaceAll("\\{MAPIMG\\}",
						"<img src='http://maps.google.com/maps/api/staticmap?center=" + log.getLatitude() + ","
						+ log.getLongitude() + "&zoom=15&size=260x260&sensor=false'/>");
			}else{
				template = template.replaceAll("\\{MAPLINK\\}", "");
				template = template.replaceAll("\\{MAPIMG\\}", "");
			}
			
			StringBuffer imageHTML = new StringBuffer();
			for(MaintenancePicture picture: log.getPictures()){
				imageHTML.append("<img src=\"file://" + picture.getImagePath() + "\"/> <br/>");
			}
			
			template = template.replaceAll("\\{IMAGES\\}", imageHTML.toString());
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return template;
	}
	
	@Override
	public void handleVoiceInput(String text) {
		if (text == null){
			return;
		}

		Toast.makeText(this, text, Toast.LENGTH_LONG).show();
		if (text.contains("BACK") 
				|| text.contains("GO BACK") 
				|| text.contains("CLOSE")) {
			
			backAction(null);
		}
	}

	@Override
	public void handleVoiceMode(boolean voiceMode) {
		// TODO Auto-generated method stub
	}

	@Override
	public void handleNetworkStatus(boolean connected, boolean isFastConnection) {
		// TODO Auto-generated method stub
	}
}
