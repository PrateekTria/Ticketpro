package com.ticketpro.parking.activity;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ticketpro.parking.R;
import com.ticketpro.parking.bl.LookupBLProcessor;
import com.ticketpro.util.TPUtility;

/**
 * Home screen (Ref. Mockups).
 * 
 * @author : TicketPRO
 * @version : 1.0
 * @since : 1.0 
 *
 */

public class SelectExpiryDateActivity extends BaseActivityImpl{

	
	/**
	 * Entry point of the Activity
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		try{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.select_expiry_date);
			setLogger(SelectExpiryDateActivity.class.getName());
			setBLProcessor(new LookupBLProcessor());
			setActiveScreen(this);
			
			bindDataAtLoadingTime();
			
		}catch(Exception e){
			log.error(TPUtility.getPrintStackTrace(e)); 
		}
	}	
	
	
	@Override
	public void bindDataAtLoadingTime() {
		try{
			ArrayList<String> monthValList=new ArrayList<String>();
			monthValList.add("Jan");
			monthValList.add("Feb");
			monthValList.add("March");
			monthValList.add("April");
			monthValList.add("May");
			monthValList.add("June");
			monthValList.add("July");
			monthValList.add("Aug");
			monthValList.add("Sept");
			monthValList.add("Oct");
			monthValList.add("Nov");
			monthValList.add("Dec");
			
			ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, R.layout.simple_row , monthValList);  
			
			ListView monthListView=(ListView)findViewById(R.id.select_expiry_date_months);
			monthListView.setAdapter(listAdapter);
			
			
			ArrayList<String> yearValList=new ArrayList<String>();
			for(int i=2012; i<2030;i++){
				yearValList.add(String.valueOf(i));
			}
			
			ArrayAdapter<String> listYearAdapter = new ArrayAdapter<String>(this, R.layout.simple_row , yearValList);  
			
			ListView yearListView=(ListView)findViewById(R.id.select_expiry_date_years);
			yearListView.setAdapter(listYearAdapter);
		
		}catch(Exception e){
			log.error(TPUtility.getPrintStackTrace(e)); 
		}
		
	}

	@Override
	public void onClick(View v) {
		int tagId=Integer.valueOf(v.getTag().toString());
		switch(tagId){
		
		}	
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
