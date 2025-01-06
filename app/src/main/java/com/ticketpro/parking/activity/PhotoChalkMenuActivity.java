package com.ticketpro.parking.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import android.view.KeyEvent;

import com.ticketpro.parking.R;
import com.ticketpro.util.DataUtility;
import com.ticketpro.util.TPConstant;

public class PhotoChalkMenuActivity extends FragmentActivity{

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    mSlideoutHelper = new SlideoutHelper(this);
	    mSlideoutHelper.activate();
	    getSupportFragmentManager().beginTransaction().add(R.id.slideout_placeholder, new PhotoChalkMenuFragment(), "menu").commit();
	    mSlideoutHelper.open();

	}

	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			mSlideoutHelper.close();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}


	public SlideoutHelper getSlideoutHelper(){
		return mSlideoutHelper;
	}
	
	public void callAction(String action){
		if(action.equals("Chalk Xchange")){
			Intent intent = new Intent();
			intent.setClass(this,RetrieveChalkActivity.class);
			startActivity(intent);
			return;
			
		}else if(action.equals("Map View")){
			Intent intent = new Intent();
			intent.setClass(this,LocationChalkMapViewActivity.class);
			startActivity(intent);
			return;
		
		}else if(action.equals("Turn Off Chalk Alerts")){
			TPApplication.getInstance().enableChalkAlerts=false;
			
			SharedPreferences mPreferences=getSharedPreferences(getPackageName(), MODE_PRIVATE);
			SharedPreferences.Editor editor = mPreferences.edit();
			editor.putBoolean(TPConstant.PREFS_KEY_ENABLE_CHALKALERTS, false);
			editor.commit();
			return;
		
		}else if(action.equals("Turn On Chalk Alerts")){
			TPApplication.getInstance().enableChalkAlerts=true;
			
			SharedPreferences mPreferences=getSharedPreferences(getPackageName(), MODE_PRIVATE);
			SharedPreferences.Editor editor = mPreferences.edit();
			editor.putBoolean(TPConstant.PREFS_KEY_ENABLE_CHALKALERTS, true);
			editor.commit();
			return;
		}
		
		else if(action.equals("Clear Chalk Log")){
			DataUtility.checkExpiredChalks(this);
			return;
		}
		
	}
	
	public Context getContent(){
		return getApplicationContext();
	}
	
	private SlideoutHelper mSlideoutHelper;

}
