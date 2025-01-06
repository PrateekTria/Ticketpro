package com.ticketpro.parking.activity;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ticketpro.parking.R;
import com.ticketpro.exception.TPException;
import com.ticketpro.model.Ticket;
import com.ticketpro.parking.bl.CommonBLProcessor;
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

public class VehiclesActivity extends BaseActivityImpl{

	private TableLayout tableLayout;
	private ArrayList<Ticket> tickets;
	private ProgressDialog progressDialog;
	private Handler dataLoadingHandler;
	private Handler errorHandler;
	
	/**
	 * Entry point of the Activity
	 */
	@Override
    public void onCreate(Bundle savedInstanceState) {
		
		try{	
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.vehicle_logs);
	        setLogger(VehiclesActivity.class.getName());
	        setBLProcessor(new CommonBLProcessor((TPApplication)getApplicationContext()));
	        setActiveScreen(this);
	        
	        tableLayout=(TableLayout)findViewById(R.id.logs_3_table_view);
		  
	        dataLoadingHandler=new Handler() {
				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					initDatagrid();
					
					if(progressDialog.isShowing())
						progressDialog.dismiss();
				}
			};
	
			errorHandler=new Handler() {
				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					if(progressDialog.isShowing())
						progressDialog.dismiss();
				}
			};	
			
			bindDataAtLoadingTime();
			
		}catch(Exception e){
			log.error(TPUtility.getPrintStackTrace(e)); 
		}
	}    
	
	private void initDatagrid(){
		try{	
			if(tickets==null)
				return;
			
			LayoutInflater inflater = LayoutInflater.from(this); 
			int i=0;
	        for(Ticket ticket:tickets){
	        	View tableRow = inflater.inflate(R.layout.table_row_3_coulm_view, null);
	        	
	        	((TextView)tableRow.findViewById(R.id.tr_header1)).setText(ticket.getVin());
	        	((TextView)tableRow.findViewById(R.id.tr_header2)).setText(ticket.getPlate());
	        	((TextView)tableRow.findViewById(R.id.tr_header3)).setText(ticket.getLocation());
	        	
	        	if((i%2)==0)
	        		tableRow.setBackgroundResource(R.drawable.tablerow_even);
	        	else
	        		tableRow.setBackgroundResource(R.drawable.tablerow_odd);
	
	        	tableLayout.addView(tableRow);
	        	i++;
	        }
	    }catch(Exception e){
			log.error(TPUtility.getPrintStackTrace(e)); 
		} 
	} 
	
	public void bindDataAtLoadingTime(){
		try{	
			progressDialog = ProgressDialog.show(this, "", "Loading...");
			new Thread(){
				public void run() {
					try{
						tickets=((CommonBLProcessor)screenBLProcessor).getTickets();
						dataLoadingHandler.sendEmptyMessage(1);
					}catch(TPException ae){
						log.error(ae.getMessage());
						errorHandler.sendEmptyMessage(0);
					}
				}
			}.start();

		}catch (Exception e) {
			log.error(TPUtility.getPrintStackTrace(e)); 
			displayErrorMessage(TPConstant.GENERIC_ERROR_MESSAGE);
		}
	}
	
	@Override
	public void onClick(View v) {
		
	}
	
	public void backAction(View view){
		finish();
	}

	@Override
	public void handleVoiceInput(String text) {	
		if(text==null) return;
		
		Toast.makeText(this, text, Toast.LENGTH_LONG).show();
		if(text.contains("BACK") || text.contains("GO BACK") || text.contains("CLOSE")){
			backAction(null);
		}
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
