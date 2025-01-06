package com.ticketpro.parking.activity;

import java.io.File;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jsibbold.zoomage.ZoomageView;
import com.ticketpro.model.ChalkPicture;
import com.ticketpro.model.Ticket;
import com.ticketpro.model.TicketPicture;
import com.ticketpro.parking.R;
import com.ticketpro.parking.bl.PhotosBLProcessor;
import com.ticketpro.util.TPUtility;
import com.ticketpro.util.TouchImageView;

/**
 * Home screen (Ref. Mockups).
 * 
 * @author : TicketPRO
 * @version : 1.0
 * @since : 1.0 
 *
 */

public class ViewPictureActivity extends BaseActivityImpl{

	private int pictureIndex;
	private TicketPicture activeTicketPicture;
	private Bitmap imageBitmap;
	private boolean isChalkPicture=false;
	private boolean isChalkPicturePrev=false;
	private boolean isSharedTicket=false;
	private	long chalkId;
	private String ImageUrl;
	private Button deleteImageButton;
	//TouchImageView imgView;
	ZoomageView imgView;
	/**
	 * Entry point of the Activity
	 */
	@Override
    public void onCreate(Bundle savedInstanceState) {
		try{   
			super.onCreate(savedInstanceState);
	        setContentView(R.layout.view_pictures);
	        setLogger(ViewPictureActivity.class.getName());
	        setBLProcessor(new PhotosBLProcessor());
	        setActiveScreen(this);
	        isNetworkInfoRequired=true;

	        deleteImageButton = (Button)findViewById(R.id.deleteImageButton);
	        Intent intent=getIntent();
	        pictureIndex=intent.getIntExtra("PictureIndex", 0);
	        isChalkPicture=intent.getBooleanExtra("ChalkPicture", false);
			isChalkPicturePrev=intent.getBooleanExtra("isChalkPicturePrev", false);
	        isSharedTicket=intent.getBooleanExtra("SharedTicket", false);
	        ImageUrl = intent.getStringExtra("ImageUrl");
	        chalkId=intent.getLongExtra("ChalkId", 0);
			imgView=(ZoomageView) findViewById(R.id.view_picture_imgview);

			if(isChalkPicture){
	        	try{
	    			ChalkPicture chalkPicture=ChalkPicture.getChalkPictureById(chalkId);
	    			if(chalkPicture==null){
	    				return;
	    			}
	    			
			        File previewImg = new File(chalkPicture.getImagePath());
			        if(previewImg.exists()){
			        	if(chalkPicture.getImagePath().contains("LPR")){
			        		deleteImageButton.setVisibility(View.GONE);
						}
			        	imageBitmap = BitmapFactory.decodeFile(previewImg.getAbsolutePath());
						imgView.setImageBitmap(imageBitmap);


				    }


			   }catch (Exception e) {
	    			log.error(e.getMessage());
	    		}
			} else if (isChalkPicturePrev) {
	        	imgView = (ZoomageView) findViewById(R.id.view_picture_imgview);

				File previewImg = new File(ImageUrl);
				if (previewImg.exists()) {
					imageBitmap = BitmapFactory.decodeFile(previewImg.getAbsolutePath());
					imgView.setImageBitmap(imageBitmap);

					deleteImageButton.setVisibility(View.GONE);

				}

			} else {
				Ticket activeTicket = isSharedTicket ? TPApp.getSharedTicket() : TPApp.getActiveTicket();
				if (activeTicket != null && activeTicket.getTicketPictures() != null) {
					activeTicketPicture = activeTicket.getTicketPictures().get(pictureIndex);
				}

				if (activeTicketPicture == null) {
					return;
				}

				//ImageView imgView = (ImageView) findViewById(R.id.view_picture_imgview);
				imgView = (ZoomageView) findViewById(R.id.view_picture_imgview);

				File previewImg = new File(activeTicketPicture.getImagePath());
				if (previewImg.exists()) {
					imageBitmap = BitmapFactory.decodeFile(previewImg.getAbsolutePath());
					imgView.setImageBitmap(imageBitmap);

					if (activeTicketPicture.getImagePath().contains("LPR")) {
						deleteImageButton.setVisibility(View.GONE);
					}
				}
				if (ImageUrl != null && !TextUtils.isEmpty(ImageUrl)) {
					imgView = (ZoomageView) findViewById(R.id.view_picture_imgview);
					imgView.setVisibility(View.VISIBLE);
					deleteImageButton.setVisibility(View.GONE);

					//Picasso.get().load(ImageUrl).into(imgView);
					Glide.with(ViewPictureActivity.this)
							.load(ImageUrl)
							.into(imgView);
				}
			}
      
		}catch(Exception e){
			log.error(TPUtility.getPrintStackTrace(e)); 
		}
		deleteImageButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				removeAction();
			}
		});
    }


	private void openImageDialog(Bitmap imageBitmap) {

		Dialog builder = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
		builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
		builder.setCancelable(true);
		builder.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialogInterface) {
				//nothing;
			}
		});

		TouchImageView imageView = new TouchImageView(this);
		imageView.setImageBitmap(imageBitmap);
		builder.addContentView(imageView, new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT));
		builder.show();
	}

	public void bindDataAtLoadingTime(){
		
	}
	
	@Override
	public void onClick(View v) {
		
	}

	@Override
	public void onBackPressed() {
		if(imageBitmap!=null){
			imageBitmap.recycle();
			System.gc();
		}
		
		setResult(RESULT_CANCELED);
	    finish();
	}
	
	public void backAction(View view){
		if(imageBitmap!=null){
			imageBitmap.recycle();
			System.gc();
		}
		
		setResult(RESULT_CANCELED);
	    finish();
	}
	
	public void removeAction(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setTitle("Delete Confirmation")
	    .setMessage("Are you sure you want to remove picture?")
	    .setCancelable(true)
	    .setNegativeButton("No", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
	 	})
	 	.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				try{
					if(isChalkPicture){
						try{
							ChalkPicture.removeChalkPictureById(chalkId);
						}catch (Exception e) {
							log.error(TPUtility.getPrintStackTrace(e));
						}
						
					}else if(activeTicketPicture!=null){
						Ticket activeTicket=isSharedTicket ? TPApp.getSharedTicket() : TPApp.getActiveTicket();
						if(isSharedTicket){
		    				try {
		    					TicketPicture picture=activeTicket.getTicketPictures().get(pictureIndex);
		    					if(picture!=null){
		    						if(isNetworkConnected()){
										((PhotosBLProcessor)screenBLProcessor).deleteTicketPicture(picture.getCitationNumber(), picture.getImagePath());
									}
		    						
		    						TPUtility.removeFile(picture.getImagePath());
		    						TicketPicture.removePictureById(picture.getPictureId());
		    					}
		    				}catch (Exception e) {
		    					log.error(TPUtility.getPrintStackTrace(e));
							}
						}
					
						activeTicket.getTicketPictures().remove(pictureIndex);
					}
				}catch(Exception e){
					log.error(TPUtility.getPrintStackTrace(e));
				}
				
				if(imageBitmap!=null){
					imageBitmap.recycle();
					System.gc();
				}
		        
		        setResult(RESULT_OK);
		        finish();
			}
	 	});
	    
	    AlertDialog alert = builder.create();
	    alert.show();
	}

	@Override
	public void handleVoiceInput(String text) {
		if(text==null) return;
		
		Toast.makeText(this, text, Toast.LENGTH_LONG).show();
		if(text.contains("BACK") || text.contains("GO BACK") || text.contains("CLOSE")){
			backAction(null);
		}
		
		else if(text.contains("REMOVE") || text.contains("DELETE")){
			removeAction();
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
