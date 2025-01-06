package com.ticketpro.parking.service;

import com.ticketpro.model.RemoteAction;
import com.ticketpro.model.UserSetting;
import com.ticketpro.parking.activity.TPApplication;
import com.ticketpro.util.TPConstant;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;


public class RemoteActionHandler extends Handler{
	public static final int ACTION_EXECUTE_TASK=1;
	
	private RemoteAction action;
	private Context context;
	
	public RemoteActionHandler(Context context, RemoteAction action){
		this.context=context;
		this.action=action;
	}
	
	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg); 
		
		switch(msg.what){
		case ACTION_EXECUTE_TASK:
			processTask();
			break;
		}
	}
	
	private void processTask(){
		SharedPreferences mPreferences=context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE);
		if("AutoLookup".equalsIgnoreCase(action.getTask())){
			UserSetting settings=TPApplication.getInstance().getUserSettings();
			SharedPreferences.Editor editor = mPreferences.edit();
			if(settings!=null){
				settings.setAutoLookup("Y".equalsIgnoreCase(action.getParams()));
				editor.putBoolean(TPConstant.PREFS_KEY_SETTING_AUTO_LOOKUP, settings.isAutoLookup());
			}else{
				editor.putBoolean(TPConstant.PREFS_KEY_SETTING_AUTO_LOOKUP, "Y".equalsIgnoreCase(action.getParams()));
			}
			
			editor.commit();
		}
		else if("DebugAction".equalsIgnoreCase(action.getTask())){
			SharedPreferences logPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
			//boolean isLoggingEnabled = logPreferences.getBoolean(TPConstant.PREFS_KEY_ENABLE_LOG, false);
			SharedPreferences.Editor editor = logPreferences.edit();

			//editor.putBoolean(TPConstant.PREFS_KEY_ENABLE_LOG, !isLoggingEnabled);
			editor.commit();

			//LoggerConfigurator.toggleDebugLog(this, !isLoggingEnabled);

		}
		
		else if("ShowPrintDialog".equalsIgnoreCase(action.getTask())){
			TPApplication.getInstance().showPrintDialog="Y".equalsIgnoreCase(action.getParams());
			SharedPreferences.Editor editor = mPreferences.edit();
	        editor.putBoolean(TPConstant.PREFS_KEY_SHOW_PRINTDIALOG, TPApplication.getInstance().showPrintDialog);
	        editor.commit();
		}
	}
}
