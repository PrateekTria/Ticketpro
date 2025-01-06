package com.ticketpro.parking.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.ticketpro.parking.R;
import com.ticketpro.exception.TPException;
import com.ticketpro.model.State;
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

public class SelectStateActivity extends BaseActivityImpl{

	private ProgressDialog progressDialog;
	private Handler errorHandler;
	private Handler dataHandler;
	
	private List<State> stateList;
	EditText inputText;
	
	/**
	 * Entry point of the Activity
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		try{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.select_state);
			setLogger(SelectStateActivity.class.getName());
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
					if(s.toString().length()>=3){
						Toast.makeText(SelectStateActivity.this, "Going to text search for...."+s, Toast.LENGTH_LONG).show();
					}
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
					for(State state:stateList){
						stateTextList.add(state.getTitle());
						
					}
					
					ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(SelectStateActivity.this, R.layout.simple_row , stateTextList);  
					ListView listView=(ListView)findViewById(R.id.lookup_state_list);
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
					stateList =((LookupBLProcessor)screenBLProcessor).getStates();
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
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.layout.menu_lookup, menu);
		return true;
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId())
		{
			case R.id.lookup_menu_1:{
				Intent i = new Intent();
				i.setClass(SelectStateActivity.this, SelectVehicleMakeActivity.class);
				startActivity(i);
				return true;
			}
			case R.id.lookup_menu_2:{
				Intent i = new Intent();
				i.setClass(SelectStateActivity.this, SelectExpiryDateActivity.class);
				startActivity(i);
				return true;
			}
			default:
				return super.onOptionsItemSelected(item);
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
