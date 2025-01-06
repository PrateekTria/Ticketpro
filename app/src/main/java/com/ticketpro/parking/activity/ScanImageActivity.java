package com.ticketpro.parking.activity;
import java.io.File;

import com.ticketpro.parking.R;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class ScanImageActivity extends BaseActivityImpl {

	public static final String TAG = "ImageScan";
    
    private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
    public static final int CAMERA_REQUEST_CODE = 0x101;
    
    private String mCameraFile;
    private EditText mTextView;
    private ImageView mImgView;
    private Bitmap mBitmap;

    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_image);

        mTextView = (EditText) findViewById(R.id.textResult);
        mImgView = (ImageView) findViewById(R.id.imgImage);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mCameraFile = extras.getString(ExtrasKeys.EXTRAS_IMAGE_PATH);
        }
        
        startCamera();
    }
    
    /**
     * Starts built-in camera intent for taking scan images.
     */
    private void startCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(mCameraFile)));
        startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
    }

    /**
     * Handler for button "Take Photo"
     */
    public void takePhotoHandler(View view) {
        startCamera();
    }
   
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // obtain image that was saved to external storage by camera activity
                try {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inPreferredConfig = BITMAP_CONFIG;
                    mBitmap = BitmapFactory.decodeFile(mCameraFile, options);
                    
                    // show camera image
                    mImgView.setImageBitmap(mBitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            }
        }
    }
    
    public void cancelButtonHandler(View view) {
    	if(mCameraFile!=null && !mCameraFile.isEmpty()){
    		try{
    			new File(mCameraFile).delete();
    		}catch(Exception e){
    			e.printStackTrace();
    		}
    	}
    	
    	setResult(RESULT_CANCELED);
    	finish();
    }
    
    public void acceptButtonHandler(View view) {
    	Intent intent = new Intent();
    	Bundle bundle = new Bundle();
    	bundle.putString("Data", mTextView.getText().toString());
    	intent.putExtra(ExtrasKeys.EXTRAS_RECOGNITION_RESULTS, bundle);
    	
    	if(mCameraFile!=null && !mCameraFile.isEmpty()){
    		try{
    			if(new File(mCameraFile).exists()){
    	    		intent.putExtra(ExtrasKeys.EXTRAS_IMAGE_PATH, mCameraFile);
    	        }
    		}catch(Exception e){
    			e.printStackTrace();
    		}
    	}
    	
    	setResult(RESULT_OK, intent);
    	finish();
    }

	@Override
	public void bindDataAtLoadingTime() throws Exception {
		// TODO Auto-generated method stub
		
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
	public void handleNetworkStatus(boolean connected, boolean isFastConnection) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
	}
}