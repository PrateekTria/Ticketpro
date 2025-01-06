package com.ticketpro.parking.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;

import com.ticketpro.parking.R;
import com.ticketpro.parking.bl.ChalkBLProcessor;
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

public class LocationChalkPreviousActivity extends BaseActivityImpl{

	private TableLayout tableLayout;
	
	/**
	 * Entry point of the Activity
	 */
	@Override
    public void onCreate(Bundle savedInstanceState) {
      try{ 
    	
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.location_chalk_previous);
        setLogger(LocationChalkPreviousActivity.class.getName());
        setBLProcessor(new ChalkBLProcessor((TPApplication)getApplicationContext()));
        setActiveScreen(this);
		
        
        
        
        tableLayout=(TableLayout)findViewById(R.id.location_chalk_prev_table_view);
		LayoutInflater inflater = LayoutInflater.from(this); // 1
        
        //adding Header
        View headerRow = inflater.inflate(R.layout.table_row_2_coulm_view, null); // 2 and 3
    	((TextView)headerRow.findViewById(R.id.tr_header1  )).setText("Date ");
    	((TextView)headerRow.findViewById(R.id.tr_header2)).setText("Time Elapsed ");
    	headerRow.setBackgroundColor(Color.YELLOW);
    	
    	tableLayout.addView(headerRow);
        
        for(int i=0; i<20;i++){
        	
        	View tableRow = inflater.inflate(R.layout.table_row_2_coulm_view, null); // 2 and 3
        	
        	((TextView)tableRow.findViewById(R.id.tr_header2  )).setText("date "+i);
        	((TextView)tableRow.findViewById(R.id.tr_header3)).setText("Time Elapsed "+i);
        	
        	
        	if((i%2)==0)
        		tableRow.setBackgroundResource(R.drawable.tablerow_even);
        	else
        		tableRow.setBackgroundResource(R.drawable.tablerow_odd);


        	tableLayout.addView(tableRow);
        }
        
      }catch(Exception e){
			log.error(TPUtility.getPrintStackTrace(e)); 
			displayErrorMessage(TPConstant.GENERIC_ERROR_MESSAGE);
		}
	}    
	
	public void bindDataAtLoadingTime(){
		
	}
	
	@Override
	public void onClick(View v) {
		
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
