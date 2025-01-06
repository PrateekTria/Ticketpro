package com.ticketpro.lpr.web;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.ticketpro.lpr.LPRRequest;
import com.ticketpro.lpr.ViewfinderView;
import com.ticketpro.lpr.camera.CameraManager;
import com.ticketpro.lpr.camera.CameraManager.CameraMode;
import com.ticketpro.lpr.camera.CloseButton;
import com.ticketpro.lpr.camera.ShutterButton;
import com.ticketpro.model.Feature;
import com.ticketpro.parking.R;
import com.ticketpro.parking.activity.SearchLookupActivity;
import com.ticketpro.parking.activity.TPApplication;
import com.ticketpro.parking.service.TPAsyncTask;
import com.ticketpro.util.DateUtil;
import com.ticketpro.util.TPConstant;
import com.ticketpro.util.TPTask;
import com.ticketpro.util.TPUtility;
import com.ticketpro.util.VerticalSeekBar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;

public class LPRActivityScreen extends Activity
		implements SurfaceHolder.Callback, ShutterButton.OnShutterButtonListener, CloseButton.OnCloseButtonListener {

	protected CameraManager cameraManager;
	protected LPRActivityHandler handler;
	protected ViewfinderView viewfinderView;
	protected SurfaceView surfaceView;
	protected SurfaceHolder surfaceHolder;
	protected boolean hasSurface;
	protected ShutterButton shutterButton;
	protected CloseButton closeButton;
	protected ImageView flashlightButton;
	protected boolean flashlightLED = false;
	protected static final String NAMESPACE = "LPRService";
	protected static final String SOAP_ACTION = "LPRService/ILPRService/ReadLPRDataByXML";
	protected static final String URL = TPConstant.LPR_URL;
	protected final String TAG = "LPR";
	protected ImageView previewImage;
	protected LinearLayout resultView;
	protected EditText plateNumberEditView;
	protected EditText stateCodeEditView;
	protected EditText colorEditView;
	protected EditText bodyEditView;
	protected EditText makeEditView;
	
	protected boolean isStateCodeRequired = false;
	protected String state;
	protected String plateNumber;
	protected String plateImageFile;
	protected String resolution;
	protected String imageSize;
	protected byte[] byteArray;
	protected RelativeLayout cameraButtonView;

	protected int maxZoomLevel = 0;
	protected VerticalSeekBar verticalSeekBar;
	protected CheckBox stickyZoom;
	protected int savedzoomLevel = 0;
	protected SharedPreferences mPreferences;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Check if LPR is allowed on not.
		if (!Feature.isFeatureAllowed(Feature.LPR)) {
			Toast.makeText(this, "This feature is disabled.", Toast.LENGTH_LONG).show();
			finish();
		}

		Window window = getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.lpr_capture);

		previewImage = findViewById(R.id.image_view);

		DisplayMetrics metrics = getResources().getDisplayMetrics();
		int height = metrics.heightPixels / 3;
		previewImage.getLayoutParams().height = height;

		resultView = findViewById(R.id.result_view);
		resultView.setVisibility(View.GONE);
		
		cameraButtonView = findViewById(R.id.camera_button_view);
		
		plateNumberEditView = findViewById(R.id.plate_number);
		stateCodeEditView = findViewById(R.id.state);
		
		colorEditView = findViewById(R.id.color);
		bodyEditView = findViewById(R.id.body);
		makeEditView = findViewById(R.id.make);
		
		colorEditView.setVisibility(View.GONE);
		bodyEditView.setVisibility(View.GONE);
		makeEditView.setVisibility(View.GONE);
		
		isStateCodeRequired = Feature.isFeatureAllowed(Feature.USE_LPR_STATE);

		viewfinderView = findViewById(R.id.viewfinder_view);
		shutterButton = findViewById(R.id.shutter_button);
		shutterButton.setOnShutterButtonListener(this);
		shutterButton.setVisibility(View.VISIBLE);

		closeButton = findViewById(R.id.close_button);
		closeButton.setOnCloseButtonListener(this);
		flashlightButton = findViewById(R.id.flashlight_led_imgview);
		flashlightButton.setOnClickListener(v -> {
			try {
				if (!flashlightLED) {
					flashlightLED = true;
					flashlightButton.setImageResource(R.drawable.flashlight_disable);
					cameraManager.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
				} else {
					flashlightLED = false;
					flashlightButton.setImageResource(R.drawable.flashlight);
					cameraManager.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
				}

				TPApplication.getInstance().LPRFlashLED = flashlightLED;

			} catch (Exception e) {
				Log.e("LPRActivity", "Error :" + e.getMessage());
			}
		});

		handler = null;
		cameraManager = new CameraManager(getApplication());
		viewfinderView.setCameraManager(cameraManager);

		// Set listener to change the size of the viewfinder rectangle.
		viewfinderView.setOnTouchListener(new View.OnTouchListener() {
			int lastX = -1;
			int lastY = -1;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					lastX = -1;
					lastY = -1;
					return true;
				case MotionEvent.ACTION_MOVE:
					int currentX = (int) event.getX();
					int currentY = (int) event.getY();

					try {
						Rect rect = cameraManager.getFramingRect();

						final int BUFFER = 50;
						final int BIG_BUFFER = 60;
						if (lastX >= 0) {
							// Adjust the size of the viewfinder rectangle.
							// Check if the touch event occurs in the corner
							// areas first, because the regions overlap.
							if (((currentX >= rect.left - BIG_BUFFER && currentX <= rect.left + BIG_BUFFER)
									|| (lastX >= rect.left - BIG_BUFFER && lastX <= rect.left + BIG_BUFFER))
									&& ((currentY <= rect.top + BIG_BUFFER && currentY >= rect.top - BIG_BUFFER)
											|| (lastY <= rect.top + BIG_BUFFER && lastY >= rect.top - BIG_BUFFER))) {
								// Top left corner: adjust both top and left
								// sides
								cameraManager.adjustFramingRect(2 * (lastX - currentX), 2 * (lastY - currentY));
								viewfinderView.removeResultText();
							} else if (((currentX >= rect.right - BIG_BUFFER && currentX <= rect.right + BIG_BUFFER)
									|| (lastX >= rect.right - BIG_BUFFER && lastX <= rect.right + BIG_BUFFER))
									&& ((currentY <= rect.top + BIG_BUFFER && currentY >= rect.top - BIG_BUFFER)
											|| (lastY <= rect.top + BIG_BUFFER && lastY >= rect.top - BIG_BUFFER))) {
								// Top right corner: adjust both top and right
								// sides
								cameraManager.adjustFramingRect(2 * (currentX - lastX), 2 * (lastY - currentY));
								viewfinderView.removeResultText();
							} else if (((currentX >= rect.left - BIG_BUFFER && currentX <= rect.left + BIG_BUFFER)
									|| (lastX >= rect.left - BIG_BUFFER && lastX <= rect.left + BIG_BUFFER))
									&& ((currentY <= rect.bottom + BIG_BUFFER && currentY >= rect.bottom - BIG_BUFFER)
											|| (lastY <= rect.bottom + BIG_BUFFER
													&& lastY >= rect.bottom - BIG_BUFFER))) {
								// Bottom left corner: adjust both bottom and
								// left sides
								cameraManager.adjustFramingRect(2 * (lastX - currentX), 2 * (currentY - lastY));
								viewfinderView.removeResultText();
							} else if (((currentX >= rect.right - BIG_BUFFER && currentX <= rect.right + BIG_BUFFER)
									|| (lastX >= rect.right - BIG_BUFFER && lastX <= rect.right + BIG_BUFFER))
									&& ((currentY <= rect.bottom + BIG_BUFFER && currentY >= rect.bottom - BIG_BUFFER)
											|| (lastY <= rect.bottom + BIG_BUFFER
													&& lastY >= rect.bottom - BIG_BUFFER))) {
								// Bottom right corner: adjust both bottom and
								// right sides
								cameraManager.adjustFramingRect(2 * (currentX - lastX), 2 * (currentY - lastY));
								viewfinderView.removeResultText();
							} else if (((currentX >= rect.left - BUFFER && currentX <= rect.left + BUFFER)
									|| (lastX >= rect.left - BUFFER && lastX <= rect.left + BUFFER))
									&& ((currentY <= rect.bottom && currentY >= rect.top)
											|| (lastY <= rect.bottom && lastY >= rect.top))) {
								// Adjusting left side: event falls within
								// BUFFER pixels of left side, and between top
								// and bottom side limits
								cameraManager.adjustFramingRect(2 * (lastX - currentX), 0);
								viewfinderView.removeResultText();
							} else if (((currentX >= rect.right - BUFFER && currentX <= rect.right + BUFFER)
									|| (lastX >= rect.right - BUFFER && lastX <= rect.right + BUFFER))
									&& ((currentY <= rect.bottom && currentY >= rect.top)
											|| (lastY <= rect.bottom && lastY >= rect.top))) {
								// Adjusting right side: event falls within
								// BUFFER pixels of right side, and between top
								// and bottom side limits
								cameraManager.adjustFramingRect(2 * (currentX - lastX), 0);
								viewfinderView.removeResultText();
							} else if (((currentY <= rect.top + BUFFER && currentY >= rect.top - BUFFER)
									|| (lastY <= rect.top + BUFFER && lastY >= rect.top - BUFFER))
									&& ((currentX <= rect.right && currentX >= rect.left)
											|| (lastX <= rect.right && lastX >= rect.left))) {
								// Adjusting top side: event falls within BUFFER
								// pixels of top side, and between left and
								// right side limits
								cameraManager.adjustFramingRect(0, 2 * (lastY - currentY));
								viewfinderView.removeResultText();
							} else if (((currentY <= rect.bottom + BUFFER && currentY >= rect.bottom - BUFFER)
									|| (lastY <= rect.bottom + BUFFER && lastY >= rect.bottom - BUFFER))
									&& ((currentX <= rect.right && currentX >= rect.left)
											|| (lastX <= rect.right && lastX >= rect.left))) {
								// Adjusting bottom side: event falls within
								// BUFFER pixels of bottom side, and between
								// left and right side limits
								cameraManager.adjustFramingRect(0, 2 * (currentY - lastY));
								viewfinderView.removeResultText();
							}
						}
					} catch (NullPointerException e) {
						Log.e(TAG, "Framing rect not available", e);
					}
					v.invalidate();
					lastX = currentX;
					lastY = currentY;
					return true;
				case MotionEvent.ACTION_UP:
					lastX = -1;
					lastY = -1;
					return true;
				}
				return false;
			}
		});

	}

	Handler getHandler() {
		return handler;
	}

	CameraManager getCameraManager() {
		return cameraManager;
	}

	public void backAction(View view) {
		// Turn off LED
		try {
			if (cameraManager != null) {
				cameraManager.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		finish();
	}

	public void processAction(View view) {
		handler.shutterButtonClick();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		backAction(null);
	}

	public void sendLPRDetails(String state, String plateNumber, String plateImageFile, String resolution, String imageSize) {
		Intent data = new Intent();
		if (isStateCodeRequired) {
			data.putExtra("State", state);
		}

		data.putExtra("PlateNumber", plateNumber);
		data.putExtra("PlateImageFile", plateImageFile);
		data.putExtra("Resolution", resolution);
		data.putExtra("ImageSize", imageSize);

		if (getParent() == null) {
			setResult(Activity.RESULT_OK, data);
		} else {
			getParent().setResult(Activity.RESULT_OK, data);
		}

		backAction(null);
	}

	public void retryAction(View view) {
		resultView.setVisibility(View.GONE);
		cameraButtonView.setVisibility(View.VISIBLE);
		
		handler.resetState();

		if (TPApplication.getInstance().LPRFlashLED) {
			try {
				if (cameraManager != null) {
					cameraManager.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void acceptAction(View view) {
		try {
			Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
			try {
				if (Feature.isFeatureAllowed(Feature.PRINT_TIME_ON_PHOTO)) {
					int y = bitmap.getHeight() - 10;
					int x = 10;
					bitmap = bitmap.copy(Config.ARGB_8888, true);

					Paint paint = new Paint();
					paint.setColor(Color.RED);
					paint.setTextSize(16);
					paint.setFlags(Paint.ANTI_ALIAS_FLAG);
					Canvas canvas = new Canvas(bitmap);
					canvas.drawText(DateUtil.getCurrentDateTime(), x, y, paint);
				}
			} catch (Exception e) {
				Log.e(TPConstant.TAG, TPUtility.getPrintStackTrace(e));
			}

			File lprImageFile = new File(plateImageFile);
			lprImageFile.createNewFile();
			FileOutputStream fos = new FileOutputStream(lprImageFile);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
			fos.flush();
			fos.close();

		} catch (Exception e) {
			Log.e("LPR", "Failed to save LPR Image." + e.getMessage());
		}

		sendLPRDetails(stateCodeEditView.getText().toString(), plateNumberEditView.getText().toString(), plateImageFile, resolution, imageSize);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) {
			return;
		}

		if (requestCode == 0) {
			if (data.hasExtra("STATE")) {
				this.state = data.getStringExtra("STATE");
				stateCodeEditView.setText(this.state);
			}
		}
	}

	public void selectStateAction(View view) {
		Intent i = new Intent();
		i.setClass(this, SearchLookupActivity.class);
		i.putExtra("LIST_TYPE", TPConstant.STATES_SEARCH_LIST);
		startActivityForResult(i, 0);
		return;
	}

	@Override
	public void onShutterButtonClick(ShutterButton b) {
		if (handler != null) {
			handler.shutterButtonClick();
		}
	}

	@Override
	public void onShutterButtonFocus(ShutterButton b, boolean pressed) {

	}

	@Override
	public void onCloseButtonFocus(CloseButton b, boolean pressed) {

	}

	@Override
	public void onCloseButtonClick(CloseButton b) {
		if (handler != null) {
			handler.quitSynchronously();
		}

		backAction(null);
	}

	boolean handlePreviewDecode(final Bitmap bitmap) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
		byteArray = stream.toByteArray();

		TPAsyncTask task = new TPAsyncTask(this, "Processing LPR...");
		task.execute(new TPTask() {
			@Override
			public void execute() {
				String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
				String XMLString = "<Image><UploadImage><ImageFile><![CDATA[" + encoded+ "]]></ImageFile></UploadImage></Image>";

				LPRRequest lpr = new LPRRequest();
				lpr.strPrecisionMode = "0";
				lpr.strXMLImage = XMLString;
				lpr.strError = "";
				lpr.strPlateNumber = "";
				lpr.strState = "";

				SoapObject request = new SoapObject(NAMESPACE, "ReadLPRDataByXML");
				request.addProperty("strXMLImage", XMLString);
				request.addProperty("strPrecisionMode", lpr.strPrecisionMode);
				PropertyInfo pi = new PropertyInfo();
				pi.setName("ReadLPRDataByXML");
				pi.setValue(lpr);
				pi.setType(LPRRequest.class);
				request.addProperty(pi);

				HttpTransportSE androidHttpTransport = null;
				try {
					SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
					envelope.dotNet = true;
					envelope.setOutputSoapObject(request);

					androidHttpTransport = new HttpTransportSE(URL);
					androidHttpTransport.debug = true;
					androidHttpTransport.call(SOAP_ACTION, envelope);

					SoapObject resultObject = (SoapObject) envelope.bodyIn;
					String xmlResult = resultObject.getPropertyAsString("ReadLPRDataByXMLResult");
					String plateNumber = resultObject.getPropertyAsString("strPlateNumber");
					String state = resultObject.getPropertyAsString("strState");

					if (xmlResult.equals("true")) {
						if (state == null || state.equals("") || state.equals("anyType{}")) {
							state = "";
						}

						if (plateNumber == null || plateNumber.equals("") || plateNumber.equals("anyType{}")) {
							plateNumber = "";
						}

						if (state.equals("") && plateNumber.equals("")) {
							handler.sendEmptyMessage(R.id.preview_decode_failed);
						} else {
							Date dt = new Date();
						 	String plateImageFile = TPUtility.getLPRImagesFolder()  + dt.getTime() + ".jpg";

							LPRActivityScreen.this.resolution = bitmap.getWidth() + "x" + bitmap.getHeight();
							LPRActivityScreen.this.plateNumber = plateNumber;
							LPRActivityScreen.this.state = state;
							LPRActivityScreen.this.plateImageFile = plateImageFile;
							LPRActivityScreen.this.imageSize = TPUtility.getImageSize(byteArray);

							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									previewImage.setImageBitmap(bitmap);
									resultView.setVisibility(View.VISIBLE);
									cameraButtonView.setVisibility(View.GONE);
									
									stateCodeEditView.setText(LPRActivityScreen.this.state);
									plateNumberEditView.setText(LPRActivityScreen.this.plateNumber);

									if (!isStateCodeRequired) {
										stateCodeEditView.setVisibility(View.GONE);
									}

									try {
										if (cameraManager != null) {
											cameraManager.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
										}
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							});
						}

					} else {
						handler.sendEmptyMessage(R.id.preview_decode_failed);
					}

				} catch (Exception e) {
					handler.sendEmptyMessage(R.id.preview_decode_failed);
				}
			}
		});
		
		return true;
	}

	void drawViewfinder() {
		viewfinderView.drawViewfinder();
	}

	protected void initCamera(SurfaceHolder surfaceHolder) {
		Log.d(TAG, "initCamera()");
		if (surfaceHolder == null) {
			throw new IllegalStateException("No SurfaceHolder provided");
		}

		try {
			// Open and initialize the camera
			cameraManager.openDriver(surfaceHolder);
			cameraManager.cameraMode = CameraMode.LPR;
			TPUtility.setCameraDisplayOrientation(this, CameraInfo.CAMERA_FACING_BACK, cameraManager.getCamera());
			handler = new LPRActivityHandler(this, cameraManager);

			// Turn on FlashLED if it was turned on earlier
			flashlightLED = TPApplication.getInstance().LPRFlashLED;
			if (flashlightLED) {
				flashlightButton.setImageResource(R.drawable.flashlight_disable);
				cameraManager.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
			} else {
				flashlightButton.setImageResource(R.drawable.flashlight);
				cameraManager.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
			}

			verticalSeekBar = findViewById(R.id.verticalSeekbar);
			stickyZoom = findViewById(R.id.stickyZoom);

			verticalSeekBar.setOnSeekBarChangeListener(new VerticalSeekBar.OnSeekBarChangeListener() {
				@Override
				public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
					try {
						Camera camera = cameraManager.getCamera();
						if (camera.getParameters().isZoomSupported()) {
							maxZoomLevel = camera.getParameters().getMaxZoom();

							if (progress <= maxZoomLevel) {
								Camera.Parameters p = camera.getParameters();
								p.setZoom(progress);
								camera.setParameters(p);

								if (stickyZoom.isChecked()){
									updateProgress(progress);
								}else{
									updateProgress(0);
								}
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {
					// TODO Auto-generated method stub
				}

				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {
					// TODO Auto-generated method stub
				}
			});

			stickyZoom.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					// update your model (or other business logic) based on
					// isChecked
					int progress = verticalSeekBar.getProgress();
					if (isChecked){
						updateProgress(progress);
					}else{
						updateProgress(0);
					}
				}
			});
			
			try {
				mPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
				savedzoomLevel = mPreferences.getInt(TPConstant.PREFS_KEY_SETTING_STICKY_LPR_ZOOM, 0);
				Handler handler = new Handler();

				handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						try {
							Camera camera = cameraManager.getCamera();
							if (camera.getParameters().isZoomSupported()) {
								maxZoomLevel = camera.getParameters().getMaxZoom();
								verticalSeekBar.setMax(maxZoomLevel);
								if (savedzoomLevel != 0) {
									if (savedzoomLevel <= maxZoomLevel) {
										Camera.Parameters p = camera.getParameters();
										p.setZoom(savedzoomLevel);
										camera.setParameters(p);
									}

									verticalSeekBar.setProgress(savedzoomLevel);
									verticalSeekBar.updateThumb();
									stickyZoom.setChecked(true);
								}

							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}, 100);

			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (IOException ioe) {
			showErrorMessage("Error", "Could not initialize camera. Please try restarting device.");

		} catch (RuntimeException e) {
			showErrorMessage("Error", "Could not initialize camera. Please try restarting device.");
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		
		TPUtility.setCameraDisplayOrientation(this, CameraInfo.CAMERA_FACING_BACK, cameraManager.getCamera());
	}

	@Override
	protected void onResume() {
		super.onResume();

		// Set up the camera preview surface.
		surfaceView = findViewById(R.id.preview_view);
		surfaceHolder = surfaceView.getHolder();
		
		if (!hasSurface) {
			surfaceHolder.addCallback(this);
			
			try{
				surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
			}catch(Exception e){
				e.printStackTrace();
			}
		}

		if (handler != null) {
			handler.resetState();
		}

		if (hasSurface) {
			initCamera(surfaceHolder);
		}

	}

	@Override
	protected void onPause() {
		if (handler != null) {
			handler.quitSynchronously();
		}

		// Stop using the camera, to avoid conflicting with other camera-based
		// apps
		cameraManager.closeDriver();

		if (!hasSurface) {
			SurfaceHolder surfaceHolder = surfaceView.getHolder();
			surfaceHolder.removeCallback(this);
		}

		super.onPause();
	}

	void stopHandler() {
		if (handler != null) {
			handler.stop();
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Log.d(TAG, "surfaceCreated()");
		if (holder == null) {
			Log.e(TAG, "surfaceCreated gave us a null surface");
		}

		if (!hasSurface) {
			Log.d(TAG, "surfaceCreated(): calling initCamera()...");
			initCamera(holder);
		}

		hasSurface = true;
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

	}

	void showErrorMessage(String title, String message) {
		new AlertDialog.Builder(this)
			.setTitle(title)
			.setMessage(message)
			.setOnCancelListener(new FinishListener(this))
			.setPositiveButton("OK", new FinishListener(this))
			.show();
	}

	protected void updateProgress(int progress) {
		SharedPreferences.Editor editor = mPreferences.edit();
		editor.putInt(TPConstant.PREFS_KEY_SETTING_STICKY_LPR_ZOOM, progress);
		editor.commit();
	}
}