package com.ticketpro.parking.activity;

import java.util.ArrayList;
import java.util.Date;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.ticketpro.model.ChalkVehicle;
import com.ticketpro.model.Duration;
import com.ticketpro.model.Ticket;
import com.ticketpro.model.User;
import com.ticketpro.parking.R;
import com.ticketpro.parking.bl.ChalkBLProcessor;
import com.ticketpro.util.DateUtil;
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

public class LocationChalkDetailsActivity extends BaseActivityImpl{

	private EditText locationEditText;
	private EditText timeEditText;
	private Spinner tireSpinner;
	private String tireDisplayNames[] = {"", "Front Left", "Front Right", "Back Left","Back Right"};
	private Spinner durationSpinner;
	private ArrayList<String> durations;
	
	private ProgressDialog progressDialog;
	private Handler dataLoadingHandler;
	private ChalkVehicle activeChalk;
	private ImageView cImage;
	private ImageView sImage;
	private Button writeTicketButton;
	private EditText officerName; 
	
	/**
	 * Entry point of the Activity
	 */
	@Override
    public void onCreate(Bundle savedInstanceState) {
		try{	
			
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.location_chalk_details);
	        setLogger(LocationChalkDetailsActivity.class.getName());
	        setActiveScreen(this);
		    
	        timeEditText=(EditText)findViewById(R.id.chalked_time_textview);
			locationEditText=(EditText)findViewById(R.id.chalked_location_textview);
			durationSpinner=(Spinner)findViewById(R.id.chalked_duration_spinner);
			tireSpinner=(Spinner)findViewById(R.id.chalked_tire_spinner);
			durationSpinner=(Spinner)findViewById(R.id.chalked_duration_spinner);
			cImage=(ImageView)findViewById(R.id.chalk_vehicle_c_img);
			sImage=(ImageView)findViewById(R.id.chalk_vehicle_s_img);
			writeTicketButton=(Button)findViewById(R.id.chalk_details_ticket_btn);
			officerName=(EditText)findViewById(R.id.location_chalk_officer);
			
			
			dataLoadingHandler=new Handler() {
				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					
					if(activeChalk==null)
						return;
					
					
					DisplayMetrics metrics = getResources().getDisplayMetrics();
					int width = (metrics.widthPixels/2);
					String screenDensityName = TPApp.getDensityName(TPApp.getApplicationContext());  

					int circleRadius;
					if(screenDensityName.equalsIgnoreCase("xxhdpi")||screenDensityName.equalsIgnoreCase("xxxhdpi")){ 
						circleRadius=(width/2)+20;
					}
					else{
						circleRadius=(width/2)-15;
					}
					int circleRadiusX=circleRadius;
					int circleRadiusY=circleRadius;
					int innerCircleRadius=circleRadius-60;
					
					RelativeLayout chalkPanel=(RelativeLayout)findViewById(R.id.location_chalk_details_circle_panel);
					chalkPanel.getLayoutParams().width=circleRadius * 2;
					chalkPanel.getLayoutParams().height=circleRadius * 2;
					
					if(activeChalk.getChalkx() > -1 && activeChalk.getChalky() > -1){
						int x1=(int)((circleRadius-20) * Math.cos(activeChalk.getChalkx()*Math.PI/180) + circleRadiusX)-20;
						int y1=(int)((circleRadius-20) * Math.sin(activeChalk.getChalky()*Math.PI/180) + circleRadiusY)-20;
						RelativeLayout.LayoutParams cLayout = (RelativeLayout.LayoutParams) cImage.getLayoutParams();
						cLayout.setMargins(x1,y1,0, 0);
						if (screenDensityName.equalsIgnoreCase("xxhdpi")|| screenDensityName.equalsIgnoreCase("xxxhdpi")) {
							if ((x1 - y1) > 150) {
								cLayout.setMargins(x1 - 40, y1, 0, 0);
							} else {
								cLayout.setMargins(x1, y1 - 40, 0, 0);
							}
						}
						cImage.setLayoutParams(cLayout);
					}else{
						cImage.setVisibility(View.GONE);
					}
				      
					if(activeChalk.getStemx() > -1 && activeChalk.getStemy() > -1){
					    int x2=(int)(innerCircleRadius * Math.cos(activeChalk.getStemx()*Math.PI/180) + circleRadiusX)-20;
						int y2=(int)(innerCircleRadius * Math.sin(activeChalk.getStemy()*Math.PI/180) + circleRadiusY)-20;
						RelativeLayout.LayoutParams sLayout = (RelativeLayout.LayoutParams) sImage.getLayoutParams();
						sLayout.setMargins(x2,y2,0, 0);	
						if(screenDensityName.equalsIgnoreCase("xxhdpi")||screenDensityName.equalsIgnoreCase("xxxhdpi")){ 
							sLayout.setMargins(x2,y2-10,0, 0);	
						}
						sImage.setLayoutParams(sLayout);
					}else{
						sImage.setVisibility(View.GONE);
					}
					
					
					locationEditText.setText(TPUtility.getFullAddress(activeChalk));
			        timeEditText.setText(DateUtil.getStringFromDate(activeChalk.getChalkDate()));
			        try {
			        	officerName.setText(User.getUserInfo(TPApp.getCurrentUserId()).getUsername());
			        }
			        catch(Exception e){e.printStackTrace();}
			        
			        try{
				        int mins=Duration.getDurationMinsById(activeChalk.getDurationId(),LocationChalkDetailsActivity.this);
				    	long diff=(new Date().getTime()-activeChalk.getChalkDate().getTime());
				    	long expTime=(diff/1000)/60;
				    	if(expTime < mins){
				    		writeTicketButton.setClickable(false);
				        	writeTicketButton.setBackgroundResource(R.drawable.btn_disabled);
				    	}else{
				    		activeChalk.setIsExpired("Y");
				    	}
			        }catch(Exception e){}
			        
			        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(LocationChalkDetailsActivity.this, android.R.layout.simple_spinner_item,tireDisplayNames);
					dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					tireSpinner.setAdapter(dataAdapter);
					
					int position=dataAdapter.getPosition(activeChalk.getTire());
					tireSpinner.setSelection(position);
					
					String[] durationArray=(String[]) durations.toArray(new String[0]);
					ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(LocationChalkDetailsActivity.this, android.R.layout.simple_spinner_item,durationArray);
					dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					durationSpinner.setAdapter(dataAdapter2);
					
					try{
						String duration=Duration.getDurationTitleUsingId(activeChalk.getDurationId());
						int position2=dataAdapter2.getPosition(duration);
						durationSpinner.setSelection(position2);
					}catch (Exception e) {
						e.printStackTrace();
					}
					
					if(progressDialog.isShowing())
						progressDialog.dismiss();
				}
			};
	
			bindDataAtLoadingTime();
	        
		}catch(Exception e){
			log.error(TPUtility.getPrintStackTrace(e)); 
		} 
	}    
	
	public void bindDataAtLoadingTime(){
		
		progressDialog = ProgressDialog.show(this, "", "Loading...");
		new Thread(){
			public void run() {
				ChalkBLProcessor blProcessor=new ChalkBLProcessor((TPApplication)getApplicationContext());
				try {
					long chalkId=getIntent().getLongExtra("ChalkId", 0);
					activeChalk=ChalkVehicle.getChalkVehicleById(chalkId);
					
					if(activeChalk!=null){
						durations=blProcessor.getDurations();
						dataLoadingHandler.sendEmptyMessage(1);
					}else{
						Toast.makeText(LocationChalkDetailsActivity.this, "Failed to load chalk details. Please try again.", Toast.LENGTH_SHORT).show();
						setResult(RESULT_OK);
						finish();
					}
				}catch (Exception e) {
					log.error(TPUtility.getPrintStackTrace(e));
					if(progressDialog.isShowing())
						progressDialog.dismiss();
				}
			}
		}.start();

	}

	@Override
	protected void onResume() {
		super.onResume();
		
		try{
			ChalkVehicle chalk=ChalkVehicle.getChalkVehicleById(activeChalk.getChalkId());
			if(chalk==null){
				setResult(RESULT_OK);
				finish();
			}
		}catch(Exception e){}
	}
	
	public void removeAction(View view){
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setTitle("Delete Confirmation")
	    .setMessage("Are you sure you want to delete location chalk?")
	    .setCancelable(true)
	    .setNegativeButton("No", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
	 	})
	 	.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				if(activeChalk==null)
					return;
				
				try{
					ChalkVehicle.removeChalkById(activeChalk.getChalkId(), "");
					setResult(RESULT_OK);
					finish();
				}catch(Exception ae){
					log.error(ae);
				}
				
			}
	 	});
	    
	    AlertDialog alert = builder.create();
	    alert.show();
	}
	
	public void writeTicketAction(View view){
		if(activeChalk==null)
			return;
		
		try{
			int mins=Duration.getDurationMinsById(activeChalk.getDurationId(),this);
	    	long diff=(new Date().getTime()-activeChalk.getChalkDate().getTime());
	    	long expTime=(diff/1000)/60;
	    	if(expTime < mins){
	    		displayErrorMessage("Chalk is not exipired. You can write ticket for expired chalks only.");
				return;
			}
		}catch (Exception e) {}
		
		
		try{
			Ticket ticket=TPApp.createTicketForChalk(activeChalk);
			TPApp.setActiveTicket(ticket);
		
			Intent i = new Intent();
			i.setClass(LocationChalkDetailsActivity.this, WriteTicketActivity.class);
			startActivityForResult(i,0);
			
		}catch (Exception e) {
			Toast.makeText(LocationChalkDetailsActivity.this, TPConstant.GENERIC_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
		}
		
		return;
	}
	
	public void backAction(View view){
		finish();
	}
	
	@Override
	public void onClick(View v) {
		
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
