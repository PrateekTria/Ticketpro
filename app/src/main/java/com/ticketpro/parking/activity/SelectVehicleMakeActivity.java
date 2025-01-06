package com.ticketpro.parking.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.ticketpro.parking.R;
import com.ticketpro.exception.TPException;
import com.ticketpro.model.MakeAndModel;
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

public class SelectVehicleMakeActivity extends BaseActivityImpl{

	private ProgressDialog progressDialog;
	private Handler errorHandler;
	private Handler dataHandler;
	private List<MakeAndModel> makeList;
	
	EditText inputText;
	
	/**
	 * Entry point of the Activity
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		try{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.select_vehicle_make);
			setLogger(SelectVehicleMakeActivity.class.getName());
			setBLProcessor(new LookupBLProcessor());
			setActiveScreen(this);
			
			EditText searchText=(EditText)findViewById(R.id.add_comment_search_text);
			searchText.addTextChangedListener(new TextWatcher() {
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
				}
				
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
				}
				
				@Override
				public void afterTextChanged(Editable s) {
					
				}
			});
			
			
			bindDataAtLoadingTime();
			errorHandler=new Handler() {
				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					if(progressDialog.isShowing())
						progressDialog.dismiss();
				}
			};
			
			
			dataHandler=new Handler() {
				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					
					ArrayList<String> stateTextList=new ArrayList<String>();
					for(MakeAndModel make:makeList){
						stateTextList.add(make.getMakeTitle());
					}
					
					ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(SelectVehicleMakeActivity.this, R.layout.simple_row , stateTextList);  
					ListView listView=(ListView)findViewById(R.id.lookup_make_list);
					listView.setAdapter(listAdapter);
					
					if(progressDialog.isShowing())
						progressDialog.dismiss();
					
				}
			};
			
		}catch(Exception e){
			log.error(TPUtility.getPrintStackTrace(e)); 
		}
		
	}	
	
	
	@Override
	public void bindDataAtLoadingTime() {
		progressDialog = ProgressDialog.show(this, "", "Loading...");
		new Thread(){
			public void run() {
				try{
					makeList =((LookupBLProcessor)screenBLProcessor).getMakeAndModels();
					dataHandler.sendEmptyMessage(0);
				}catch(TPException ae){
					log.error(ae.getMessage());
					errorHandler.sendEmptyMessage(0);
				}
			}
		}.start();
		
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
