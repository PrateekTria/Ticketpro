package com.ticketpro.parking.activity;

import com.ticketpro.parking.R;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class PackageReplacedReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
    	if(!intent.getDataString().equals("package:com.ticketpro.parking")){
    		return;
    	}
    	
    	if (intent.getAction().equals(Intent.ACTION_PACKAGE_REPLACED) || intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)) {
            addShortcut(context);
        
    	}else if (intent.getAction().equals(Intent.ACTION_PACKAGE_REMOVED)) {
    		removeShortcut(context);
        }
    }
    
    private void addShortcut(Context context){
    	removeShortcut(context);
    	
    	Intent shortcutIntent = new Intent(context, SplashActivity.class);
	    shortcutIntent.setAction(Intent.ACTION_MAIN);
	    shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
	    
	    Intent addIntent = new Intent();
	    addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
	    addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "TicketPRO Parking");
	    addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(context, R.drawable.icon));
	    addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
	    context.sendBroadcast(addIntent);
	}
    
    private void removeShortcut(Context context){
    	Intent shortcutIntent = new Intent(context, SplashActivity.class);
	    shortcutIntent.setAction(Intent.ACTION_MAIN);
         
	    Intent addIntent = new Intent();
	    addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
	    addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "TicketPRO Parking");
	    addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(context, R.drawable.icon));
	    addIntent.setAction("com.android.launcher.action.UNINSTALL_SHORTCUT");
	    context.sendBroadcast(addIntent);
	}
  
}