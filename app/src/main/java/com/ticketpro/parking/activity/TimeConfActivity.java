package com.ticketpro.parking.activity;

import java.util.Calendar;

import com.ticketpro.parking.R;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TimePicker;


/**
 * Home screen (Ref. Mockups).
 * 
 * @author : TicketPRO
 * @version : 1.0
 * @since : 1.0 
 *
 */

public class TimeConfActivity extends BaseActivityImpl{

	 final int DATE_DIALOG_ID = 0;
	/**
	 * Entry point of the Activity
	 */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.date_conf);
        showDialog(DATE_DIALOG_ID);
	}    
	
	public void bindDataAtLoadingTime(){
	}

	@Override
	public void onClick(View v) {
	}
	
	
	@Override
    protected Dialog onCreateDialog(int id) {
		final Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
        
        switch (id) {
            case DATE_DIALOG_ID:{
            	TimePickerDialog dialog= new TimePickerDialog(this, mDateSetListener, hour, minute, false);
                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            	
                            	Intent intent = new Intent();
        			        	intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        			        	intent.setClass(TimeConfActivity.this, HomeActivity.class);
        						startActivity(intent);
        						
        						finish();
                            }
                  });
                
                dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Accept",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            	Intent i = new Intent();
                    			i.setClass(TimeConfActivity.this, DayActivitiesActivity.class);
                    			startActivity(i);
                    			
                    			finish();
                            }
                 });
                
                dialog.setCancelable(false);
                return dialog;
            }
        }
        return null;
    }
	
	private TimePickerDialog.OnTimeSetListener mDateSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        	Calendar cPickerDate = Calendar.getInstance();
        	int cyear = cPickerDate.get(Calendar.YEAR);
            int cmonth = cPickerDate.get(Calendar.MONTH);
            int cday = cPickerDate.get(Calendar.DAY_OF_MONTH);
            
            cPickerDate.set(cyear, cmonth, cday, hourOfDay, minute);
		}
    };
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if (keyCode == KeyEvent.KEYCODE_BACK) {
            return false;
        }
        return false;
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
