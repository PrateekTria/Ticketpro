package com.ticketpro.parking.activity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ticketpro.model.User;
import com.ticketpro.model.UserSetting;
import com.ticketpro.parking.R;
import com.ticketpro.parking.bl.LoginBLProcessor;
import com.ticketpro.util.TPUtility;

/**
 * Home screen (Ref. Mockups).
 * 
 * @author : TicketPRO
 * @version : 1.0
 * @since : 1.0 
 *
 */

public class LoginUserPswdActivity extends BaseActivityImpl{

	private final int BACK_BUTTON_TAG=1;
	private final int LOGIN_BUTTON_TAG=2;
	private Button backButton;
	private Button loginButton;
	private EditText userInputField;
	private EditText passwordInputField;
	private ProgressDialog progressDialog;
	private Handler loginHandler;
	private Handler errorHandler;
	
	final int LOGIN_FAILED=0;
	final int LOGIN_SUCCESSFULL=1;

	/**
	 * Entry point of the Activity
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		try{	
			setContentView(R.layout.login_user_password);
			setLogger(DayActivitiesActivity.class.getName());
			setBLProcessor(new LoginBLProcessor());
			isNetworkInfoRequired=true;
	
			backButton=(Button)findViewById(R.id.login_user_password_back_btn);
			backButton.setOnClickListener(this);
			backButton.setTag(BACK_BUTTON_TAG);
	
			loginButton=(Button)findViewById(R.id.login_user_password_login_btn);
			loginButton.setOnClickListener(this);
			loginButton.setTag(LOGIN_BUTTON_TAG);
	
			userInputField=(EditText)findViewById(R.id.login_user_pwd_user_field);
			passwordInputField=(EditText)findViewById(R.id.login_user_pwd_password_field);
	
			userInputField.setOnKeyListener(new View.OnKeyListener() {
				@Override
				public boolean onKey(View v, int keyCode, KeyEvent event) {
					if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
				            (keyCode == KeyEvent.KEYCODE_ENTER)) {
						passwordInputField.requestFocus();
						return true;
					};
					return false;
				}
			});
			
			passwordInputField.setOnKeyListener(new View.OnKeyListener() {
				@Override
				public boolean onKey(View v, int keyCode, KeyEvent event) {
					
					if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
				            (keyCode == KeyEvent.KEYCODE_ENTER)) {
						
						doLogin();
						return true;
					};
					
					return false;
				}
			});
	
			loginHandler=new Handler() {
				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					if(progressDialog.isShowing())
						progressDialog.dismiss();
					
					if(msg.what==LOGIN_SUCCESSFULL){
						Intent i = new Intent();
						i.setClass(LoginUserPswdActivity.this, DateConfActivity.class);
						startActivity(i);
					}
					else if(msg.what==LOGIN_FAILED){
						displayErrorMessage("Failed to login. Please check username/password");
					}
					
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
	
			
		}catch(Exception e){
			log.error(TPUtility.getPrintStackTrace(e)); 
		}
	}    

	public void bindDataAtLoadingTime(){
		
	}

	private void doLogin(){
		progressDialog = ProgressDialog.show(this, "", "Login in Progress...");
		new Thread(){
			public void run() {
				try{
					User loginUserInfo=((LoginBLProcessor)screenBLProcessor).doLogin(userInputField.getText().toString(),passwordInputField.getText().toString());
					if(loginUserInfo==null){
						loginHandler.sendEmptyMessage(LOGIN_FAILED);
						return;
					}
					
					if(loginUserInfo!=null){
						TPApp.setUserInfo(loginUserInfo);
						TPApp.userId=loginUserInfo.getUserId();
						TPApp.setUserSettings(UserSetting.getUserSettings(loginUserInfo.getUserId()));
					}
					
					loginHandler.sendEmptyMessage(LOGIN_SUCCESSFULL);
				
				}catch(Exception ae){
					log.error(ae.getMessage());
					errorHandler.sendEmptyMessage(0);
				}
			}
		}.start();
	}

	@Override
	public void onClick(View v) {
		try{
			int tagId=Integer.valueOf(v.getTag().toString());
			if(tagId==BACK_BUTTON_TAG){
				finish();
			}else if(tagId==LOGIN_BUTTON_TAG){
				doLogin();	
			}
		}catch (Exception e) {
			log.error(TPUtility.getPrintStackTrace(e)); 
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
