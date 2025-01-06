package com.ticketpro.parking.activity;

import com.ticketpro.parking.R;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

/**
 * Home screen (Ref. Mockups).
 * 
 * @author : TicketPRO
 * @version : 1.0
 * @since : 1.0 
 *
 */

public class CommentsActivity extends BaseActivityImpl{

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
    }    
	
	public void bindDataAtLoadingTime(){
		
	}
	
	@Override
	public void onClick(View v) {
	}


	@Override
	public void handleVoiceInput(String text) {
		if(text==null) return;
		
		Toast.makeText(this, text, Toast.LENGTH_LONG).show();
		if(text.contains("BACK") || text.contains("GO BACK") || text.contains("CLOSE")){
			finish();
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
