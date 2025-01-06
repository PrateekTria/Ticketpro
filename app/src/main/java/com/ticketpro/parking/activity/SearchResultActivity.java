package com.ticketpro.parking.activity;


import android.os.Bundle;
import android.view.View;

import com.ticketpro.parking.R;
import com.ticketpro.parking.bl.SearchBLProcessor;
import com.ticketpro.util.TPUtility;

/**
 * Home screen (Ref. Mockups).
 * 
 * @author : TicketPRO
 * @version : 1.0
 * @since : 1.0 
 *
 */

public class SearchResultActivity extends BaseActivityImpl{

	/**
	 * Entry point of the Activity
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		try{	
			super.onCreate(savedInstanceState);
			setContentView(R.layout.search);
			setLogger(SearchResultActivity.class.getName());
			setActiveScreen(this);
			
			//Initialize Business Logic Handler
			setBLProcessor(new SearchBLProcessor());
			bindDataAtLoadingTime();
			
		}catch(Exception e){
			log.error(TPUtility.getPrintStackTrace(e)); 
		}
		
	}    

	public void bindDataAtLoadingTime(){
        
	}


	@Override
	public void onClick(View view) {
		
	}
	
	
	public void searchAction(View view){
		
	}
	
	public void backAction(View view){
		finish();
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
