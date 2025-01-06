package com.ticketpro.parking.activity;

import com.ticketpro.parking.R;

import android.os.Bundle;
import android.view.View;

/**
 * Home screen (Ref. Mockups).
 * 
 * @author : TicketPRO
 * @version : 1.0
 * @since : 1.0 
 *
 */

public class StandardActivity extends BaseActivityImpl{

	/**
	 * Entry point of the Activity
	 */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
   }    
	
	public void bindDataAtLoadingTime(){
		
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
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
