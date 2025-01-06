package com.ticketpro.parking.activity;

import java.io.File;
import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ticketpro.model.ChalkPicture;
import com.ticketpro.model.Feature;
import com.ticketpro.parking.R;
import com.ticketpro.parking.bl.ChalkBLProcessor;
import com.ticketpro.util.BitmapDownloaderTask;
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

public class ViewChalkPicturesActivity extends BaseActivityImpl{

	private ImageView chalkPictureView;
	private ArrayList<ChalkPicture> chalkPictures;
	private TextView currentPictureTextView;
	
	private ProgressDialog progressDialog;
	private int PHOTO_INDEX=0;
	private long chalkId=0;
	private Bitmap bitmap;
	private GestureDetector gestureDetector;
	private Handler dataLoadingHandler;
	private Handler errorHandler;
	final int DATA_ERROR=0;
	final int DATA_SUCCESSFULL=1;
	final int ERROR_LOAD=1;
	final int ERROR_SERVICE=2;
	private TextView chalkPictureHeader;
	private Button takePictureBtn;

	private FrameLayout _frame;

	/**
	 * Entry point of the Activity
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		try{	
			setContentView(R.layout.view_chalk_pictures);
			setLogger(ViewChalkPicturesActivity.class.getName());
			setBLProcessor(new ChalkBLProcessor((TPApplication)getApplicationContext()));
			setActiveScreen(this);

			_frame=(FrameLayout) findViewById(R.id._frame);

			chalkPictureView=(ImageView) findViewById(R.id.chalk_picture_view);
			currentPictureTextView=(TextView)findViewById(R.id.current_picture_textview);
			chalkPictureHeader = (TextView)findViewById(R.id.chalkPictureHeader);
			takePictureBtn = (Button)findViewById(R.id.takePictureBtn);
			takePictureBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					takePictureAction();
				}
			});
			dataLoadingHandler=new Handler() {
				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					
					if(msg.what==DATA_SUCCESSFULL){
						showImage();
					}

				}
			};
			
			errorHandler=new Handler() {
				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					if(progressDialog.isShowing())
						progressDialog.dismiss();
				
					switch (msg.what) {
						case 0:
							bindDataAtLoadingTime(ERROR_LOAD);
							break;
							
						case ERROR_LOAD:
							Toast.makeText(getBaseContext(), "Failed to load chalk details. Please try again.", Toast.LENGTH_SHORT).show();
							break;
		
						case ERROR_SERVICE:
							Toast.makeText(getBaseContext(), TPConstant.GENERIC_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
							break;
					}
				}
			};
			
			gestureDetector = new GestureDetector(new SwipeGestureDetector());
	        View.OnTouchListener gestureListener = new View.OnTouchListener() {
	            public boolean onTouch(View v, MotionEvent event) {
	                return gestureDetector.onTouchEvent(event);
	            }
	        };
			
		}catch(Exception e){
			log.error(TPUtility.getPrintStackTrace(e));
		}
	}    
	
	@Override
	protected void onResume() {
		super.onResume();
		bindDataAtLoadingTime();
	}

	public void bindDataAtLoadingTime(){
		bindDataAtLoadingTime(0);
	}
	
	public void bindDataAtLoadingTime(final int reloadCount){
		progressDialog = ProgressDialog.show(this, "", "Loading...");
		new Thread(new Runnable() {
			@Override
			public void run() {
				try{
					if(chalkId==0){
						Intent data=getIntent();
						chalkId=data.getLongExtra("ChalkId", 0);
					}
					
					chalkPictures=ChalkPicture.getChalkPictures(chalkId);
					if (chalkPictures.size()>1){
						_frame.setVisibility(View.VISIBLE);
					}
					dataLoadingHandler.sendEmptyMessage(DATA_SUCCESSFULL);
				}catch (Exception e) {
					errorHandler.sendEmptyMessage(reloadCount);
				}
			}
		}).start();
		
	}
	
	@Override
	public void onBackPressed() {
		backAction(null);
	}
	
	public void backAction(View view){
		// Clear Bitmap
		if(bitmap!=null){
			bitmap.recycle();
			System.gc();
		}
		
		setResult(RESULT_OK);
		finish();
	}
	
	public void takePictureAction( ){
		
		if(chalkId==0){
			Intent data=getIntent();
			chalkId=data.getLongExtra("ChalkId", 0);
		}
		int maxPhotos=0;
		if(Feature.isFeatureAllowed(Feature.MAX_PHOTOS)){
			String value=Feature.getFeatureValue(Feature.MAX_PHOTOS);
			try{
				maxPhotos=Integer.parseInt(value);
				for(int i=0; i<chalkPictures.size(); i++) {
					ChalkPicture chalkPicture = chalkPictures.get(i);
					if (chalkPicture.getImagePath().contains("LPR")) {
						maxPhotos = maxPhotos + 1;
					}
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}

		if(maxPhotos>0 && chalkPictures.size()>=maxPhotos){
			displayErrorMessage("Exceeded max photos limit.");
			return;
		}
		Intent i = new Intent();
		i.setClass(this, TakePictureActivity.class);
		i.putExtra("ChalkPicture", true);
		i.putExtra("ChalkId", chalkId);
		startActivityForResult(i,0);
		
		return;
	}

	
	@Override
	public void onClick(View v) {
		
	}

	public void next(View v){
		if(PHOTO_INDEX<(chalkPictures.size()-1)){
			PHOTO_INDEX++;
		}
		
		showImage();
	}

	public void previous(View v){
		if(PHOTO_INDEX>0){
			PHOTO_INDEX--;
		}
	
		showImage();
	}
	
	public void showImage(){
		ChalkPicture picture=chalkPictures.get(PHOTO_INDEX);
		if(picture==null){
			Toast.makeText(this, "Chalk picture not available.", Toast.LENGTH_LONG).show();
		}
			try {
				 if(chalkPictures.get(PHOTO_INDEX).getImagePath().contains("LPR")){
						chalkPictureHeader.setText("Chalk Pictures - LPR");
					}else {
					 chalkPictureHeader.setText("Chalk Pictures");
					}
				int maxPhotos=0;
				if(Feature.isFeatureAllowed(Feature.MAX_PHOTOS)){
					String value=Feature.getFeatureValue(Feature.MAX_PHOTOS);
					try{
						maxPhotos=Integer.parseInt(value);
						for(int i=0; i<chalkPictures.size(); i++) {
							ChalkPicture chalkPicture = chalkPictures.get(i);
							if (chalkPicture.getImagePath().contains("LPR")) {
								maxPhotos = maxPhotos + 1;
							}
						}
					}catch(Exception e){
						e.printStackTrace();
					}
				}

				if(maxPhotos>0 && chalkPictures.size()>=maxPhotos){
					takePictureBtn.setEnabled(false);
					takePictureBtn.setBackgroundResource(R.drawable.btn_disabled);
				}
		}catch (Exception e){
			e.printStackTrace();
		}
		
		try{
			if(picture!=null && picture.getImagePath()!=null){
				if(picture.getImagePath().contains("TicketPro")){
					File imgFile = new  File(picture.getImagePath());
		            if(imgFile.exists()){
		            	bitmap=BitmapFactory.decodeFile(imgFile.getAbsolutePath());
						runOnUiThread(() -> chalkPictureView.setImageBitmap(bitmap));
		            }else{	
						Toast.makeText(this, "Image file not available.", Toast.LENGTH_LONG).show();
					}
				}else{
					String contentFolder=TPApp.getCustomerInfo().getContentFolder();
		        	if(contentFolder==null || contentFolder.equals("")){
		        		contentFolder=TPApp.getCustomerInfo().getCustId()+"";
		        	}
		        	
					String imageURL=TPConstant.IMAGES_URL+"/"+contentFolder+"/"+picture.getImagePath();
					System.out.println(imageURL);
					Glide.with(ViewChalkPicturesActivity.this)
							.load(imageURL)
							.error(R.drawable.image_not_found)
							.into(chalkPictureView);
		        	//BitmapDownloaderTask task=new BitmapDownloaderTask(chalkPictureView);
			        //task.execute(imageURL, picture.getImagePath());
				}
			}
		}catch(Exception e){
			log.error(TPUtility.getPrintStackTrace(e)); 
		}
		
		currentPictureTextView.setText((PHOTO_INDEX+1)+"/"+chalkPictures.size());

		if(progressDialog.isShowing())
			progressDialog.dismiss();
	}

	@Override
	 public boolean onTouchEvent(MotionEvent event) {
	     if (gestureDetector.onTouchEvent(event)) {
	       return true;
	     }
	     
	     return super.onTouchEvent(event); 
	}
	
	public void onLeftSwipe(){
		
		if(PHOTO_INDEX<(chalkPictures.size()-1)){
			PHOTO_INDEX++;
		}
		
		showImage();
	}
	    
	public void onRightSwipe(){
		
		if(PHOTO_INDEX>0){
			PHOTO_INDEX--;
		}
	
		showImage();
	}
	
	private class SwipeGestureDetector extends SimpleOnGestureListener {
        
		private static final int SWIPE_MIN_DISTANCE = 50;
        private static final int SWIPE_MAX_OFF_PATH = 200;
        private static final int SWIPE_THRESHOLD_VELOCITY = 200;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,float velocityY) {
        	try {
                float diffAbs = Math.abs(e1.getY() - e2.getY());
                float diff = e1.getX() - e2.getX();

                if (diffAbs > SWIPE_MAX_OFF_PATH)
                    return false;

                // Left swipe
                if (diff > SWIPE_MIN_DISTANCE
                        && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                	ViewChalkPicturesActivity.this.onLeftSwipe();
                } 
                // Right swipe
                else if (-diff > SWIPE_MIN_DISTANCE
                        && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                	ViewChalkPicturesActivity.this.onRightSwipe();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
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
