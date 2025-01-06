package com.ticketpro.parking.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.ticketpro.parking.R;
import com.ticketpro.parking.bl.SearchBLProcessor;
import com.ticketpro.util.TPConstant;
import com.ticketpro.util.TPUtility;

/**
 * Home screen (Ref. Mockups).
 * 
 * @author : TicketPRO
 * @version : 1.0
 * @since : 1.0 
 *
 */

public class SearchActivity extends BaseActivityImpl{

	private Spinner searchBySpinner;
	private EditText searchText;

	/**
	 * Entry point of the Activity
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		try{	
			super.onCreate(savedInstanceState);
			setContentView(R.layout.search);
			setLogger(SearchActivity.class.getName());
			setActiveScreen(this);
			
			//Initialize Business Logic Handler
			setBLProcessor(new SearchBLProcessor());
			searchBySpinner=(Spinner)findViewById(R.id.search_by_spinner);
			searchText=(EditText)findViewById(R.id.search_by_query_textview);
					
			bindDataAtLoadingTime();
			
		}catch(Exception e){
			log.error(TPUtility.getPrintStackTrace(e)); 
			displayErrorMessage(TPConstant.GENERIC_ERROR_MESSAGE);
		}
		
	}    

	public void bindDataAtLoadingTime(){
        try{
        	SearchBLProcessor blProcessor=(SearchBLProcessor)screenBLProcessor;  
		    
		    ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,blProcessor.getSearchByList());
			dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			searchBySpinner.setAdapter(dataAdapter1);
			
	    }catch(Exception e){
			log.error(TPUtility.getPrintStackTrace(e)); 
			e.printStackTrace();
		}
	}


	@Override
	public void onClick(View view) {
		
	}
	
	
	public void searchAction(View view){
		String searchBy=(String)searchBySpinner.getSelectedItem();
		
		Intent i = new Intent();
		i.setClass(SearchActivity.this, SearchResultActivity.class);
		startActivity(i);
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
