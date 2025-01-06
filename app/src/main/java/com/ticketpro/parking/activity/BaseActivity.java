package com.ticketpro.parking.activity;

import com.ticketpro.parking.bl.BLProcessor;

/**
 * Base Interface of the screens of the application
 * 
 * @author : TicketPRO
 * @version : 1.0
 * @since : 1.0
 * 	
 */
public interface BaseActivity {
	
	public void setBLProcessor(BLProcessor screenBLProcessor); 					//sets the business logic processor of the screen
	public void setLogger(String classInstance); 	  							//sets the logger for the screen.
	public void bindDataAtLoadingTime() throws Exception;  						//binds data to screen during loading time
	public void handleVoiceInput(String text) throws Exception;									//handle voice commands
	public void handleVoiceMode(boolean voiceMode);								//handle voice mode status change
	public void handleNetworkStatus(boolean connected,boolean isFastConnection);//handle network connection status
	
}
