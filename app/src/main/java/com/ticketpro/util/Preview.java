package com.ticketpro.util;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;

import com.ticketpro.parking.activity.TPApplication;

import android.content.Context;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Handler;
import android.view.OrientationEventListener;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

public class Preview extends SurfaceView implements SurfaceHolder.Callback, Camera.PreviewCallback {

	public boolean makeSnapshot = true;
	public Camera.Size cs = null;
	private SurfaceHolder mHolder;
	public Camera camera;
	protected Object lock = new Object();
	public byte[] previewBitmapData;
	private Context context;
	private OrientationEventListener mOrientationEventListener;
	public int mOrientation = -1;
	public static final int ORIENTATION_PORTRAIT_NORMAL = 1;
	public static final int ORIENTATION_PORTRAIT_INVERTED = 2;
	public static final int ORIENTATION_LANDSCAPE_NORMAL = 3;
	public static final int ORIENTATION_LANDSCAPE_INVERTED = 4;

	private Logger log = Logger.getLogger("CameraPreview");
	
	//Pinch Zoom Variables
	float mDist = 0;
	public boolean openFrontCamera = false;
 
	public Preview(Context context) {
		super(context);
		this.context = context;
		this.mHolder = getHolder();
		this.mHolder.addCallback(this);

		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
			this.mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}

		if (mOrientationEventListener == null) {
			mOrientationEventListener = new OrientationEventListener(context, SensorManager.SENSOR_DELAY_NORMAL) {

				@Override
				public void onOrientationChanged(int orientation) {
					int lastOrientation = mOrientation;
					if (orientation >= 315 || orientation < 45) {
						if (mOrientation != ORIENTATION_PORTRAIT_NORMAL) {
							mOrientation = ORIENTATION_PORTRAIT_NORMAL;
						}
					} else if (orientation < 315 && orientation >= 225) {
						if (mOrientation != ORIENTATION_LANDSCAPE_NORMAL) {
							mOrientation = ORIENTATION_LANDSCAPE_NORMAL;
						}
					} else if (orientation < 225 && orientation >= 135) {
						if (mOrientation != ORIENTATION_PORTRAIT_INVERTED) {
							mOrientation = ORIENTATION_PORTRAIT_INVERTED;
						}
					} else { // orientation <135 && orientation > 45
						if (mOrientation != ORIENTATION_LANDSCAPE_INVERTED) {
							mOrientation = ORIENTATION_LANDSCAPE_INVERTED;
						}
					}

					if (lastOrientation != mOrientation) {
						// changeRotation(mOrientation, lastOrientation);
					}
				}
			};
		}

		if (mOrientationEventListener.canDetectOrientation()) {
			mOrientationEventListener.enable();
		}
	}
	 
	public Preview(Context context, boolean isFrontCamera) {
		super(context);
		this.context = context;
		this.openFrontCamera = isFrontCamera;
		this.mHolder = getHolder();
		this.mHolder.addCallback(this);

		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
			this.mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}

		if (mOrientationEventListener == null) {
			mOrientationEventListener = new OrientationEventListener(context, SensorManager.SENSOR_DELAY_NORMAL) {

				@Override
				public void onOrientationChanged(int orientation) {
					int lastOrientation = mOrientation;
					if (orientation >= 315 || orientation < 45) {
						if (mOrientation != ORIENTATION_PORTRAIT_NORMAL) {
							mOrientation = ORIENTATION_PORTRAIT_NORMAL;
						}
					} else if (orientation < 315 && orientation >= 225) {
						if (mOrientation != ORIENTATION_LANDSCAPE_NORMAL) {
							mOrientation = ORIENTATION_LANDSCAPE_NORMAL;
						}
					} else if (orientation < 225 && orientation >= 135) {
						if (mOrientation != ORIENTATION_PORTRAIT_INVERTED) {
							mOrientation = ORIENTATION_PORTRAIT_INVERTED;
						}
					} else { // orientation <135 && orientation > 45
						if (mOrientation != ORIENTATION_LANDSCAPE_INVERTED) {
							mOrientation = ORIENTATION_LANDSCAPE_INVERTED;
						}
					}

					if (lastOrientation != mOrientation) {
						// changeRotation(mOrientation, lastOrientation);
					}
				}
			};
		}

		if (mOrientationEventListener.canDetectOrientation()) {
			mOrientationEventListener.enable();
		}
	}



	public int getRotationAngle() {
		switch (mOrientation) {
		case ORIENTATION_PORTRAIT_NORMAL:
			return 90;

		case ORIENTATION_LANDSCAPE_NORMAL:
			return 0;

		case ORIENTATION_PORTRAIT_INVERTED:
			return 90;

		case ORIENTATION_LANDSCAPE_INVERTED:
			return 180;

		default:
			return 0;
		}
	}
	
	public int getRotationAngleFront() {
		switch (mOrientation) {
		case ORIENTATION_PORTRAIT_NORMAL:
			return 90;

		case ORIENTATION_LANDSCAPE_NORMAL:
			return 0;

		case ORIENTATION_PORTRAIT_INVERTED:
			return 90;

		case ORIENTATION_LANDSCAPE_INVERTED:
			return 180;

		default:
			return 0;
		}
	}
	
	
	private int getFrontCameraId(){
        int camId = -1;
        int numberOfCameras = Camera.getNumberOfCameras();
        Camera.CameraInfo ci = new Camera.CameraInfo();

        for(int i = 0;i < numberOfCameras;i++){
            Camera.getCameraInfo(i,ci);
            if(ci.facing == Camera.CameraInfo.CAMERA_FACING_FRONT){
                camId = i;
            }
        }

        return camId;
    }
	public void surfaceCreated(SurfaceHolder holder) {
		try {
			if(openFrontCamera){ 
				    camera = Camera.open(getFrontCameraId());
			}else{
			camera = Camera.open();
			}
			camera.setDisplayOrientation(90);
		} catch (Exception e) {
			Toast.makeText(this.context, "Failed to initialize camera preview. Please try again.", Toast.LENGTH_SHORT)
					.show();

			return;
		}

		try {
			new FocusManager(this.context, camera);
		} catch (Exception e) {
			log.error(TPUtility.getPrintStackTrace(e));
		}

		registerPreviewCallback();
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		if (camera == null) {
			return;
		}

		mOrientationEventListener.disable();
		camera.setPreviewCallback(null);
		camera.stopPreview();
		camera.release();
		camera = null;
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		if (camera == null) {
			return;
		}

		Camera.Parameters parameters = camera.getParameters();
		List<Camera.Size> sizes = parameters.getSupportedPreviewSizes();
		cs = sizes.get(0);
		for (Camera.Size s : sizes) {
			if (s.width > cs.width) {
				cs = s;
			}
		}

		// Set Focus Mode
		try {
			List<String> focusModes = parameters.getSupportedFocusModes();
			if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
				parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
			}
		} catch (Exception e) {
			log.error(TPUtility.getPrintStackTrace(e));
		}

		// Set Flash Mode
		try {
			List<String> flashModes = parameters.getSupportedFlashModes();
			if (flashModes.contains(Camera.Parameters.FLASH_MODE_AUTO)) {
				parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
			}
		} catch (Exception e) {
			log.error(TPUtility.getPrintStackTrace(e));
		}

		if (TPApplication.getInstance().pictureFlashLED) {
			parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
		}

		// Camera Parameters
		parameters.setJpegQuality(100);
		parameters.setPreviewFormat(ImageFormat.NV21);

		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
			parameters.setPreviewFrameRate(14);
		}

		parameters.setPreviewSize(cs.width, cs.height);
		camera.setParameters(parameters);

		// start an auto-focus after a slight (100ms) delay
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
			new Handler().postDelayed(new Runnable() {
				public void run() {
					try {
						if(camera!=null && !openFrontCamera){
						camera.autoFocus(new Camera.AutoFocusCallback() {
							@Override
							public void onAutoFocus(boolean success, Camera camera) {
								camera.cancelAutoFocus();

								Parameters params = camera.getParameters();
								if (params.getFocusMode() != Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE) {
									params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
									camera.setParameters(params);
								}
							}
						});}
					} catch (Exception e) {
						log.error(TPUtility.getPrintStackTrace(e));
					}
				}
			}, 100);
		}

		camera.startPreview();
	}

	public void registerPreviewCallback() {
		// Preview Callback
		try {
			if (camera == null) {
				return;
			}
			camera.setPreviewDisplay(this.mHolder);
			camera.setPreviewCallback(this);
		} catch (IOException e) {
			log.error(TPUtility.getPrintStackTrace(e));
		}
	}

	public void onPreviewFrame(final byte[] data, Camera arg1) {
		synchronized (lock) {
			previewBitmapData = data;
		}
	}

	public void setFlashlightMode(String flashMode) {
		if (camera == null) {
			return;
		}

		try {
			Camera.Parameters parameters = camera.getParameters();
			parameters.setFlashMode(flashMode);
			camera.setParameters(parameters);
		} catch (Exception e) {
			log.error(TPUtility.getPrintStackTrace(e));
		}
	}

	public void setAutoFocusMode(String autofocusMode) {
		if (camera == null) {
			return;
		}

		try {
			Camera.Parameters parameters = camera.getParameters();
			parameters.setFocusMode(autofocusMode);
			camera.setParameters(parameters);
		} catch (Exception e) {
			log.error(TPUtility.getPrintStackTrace(e));
		}
	}
	
	/** 
	 * @author bankesh
	 * @since  3.2.006
	 * @interface_required - Touch Event Interface 
	 * @UseCase Handle Zoom controls while using camera
	 *  
	 *  Handling Pinch Zoom Feature - User need to pinch zoom using two or more finger*/
	//Commented due to request.  	
/*	public void handleFocus(MotionEvent event, Camera.Parameters params) {
		try {
			if(camera==null){
				return;
			}
			int pointerId = event.getPointerId(0);
			int pointerIndex = event.findPointerIndex(pointerId);
			// Get the pointer's current position
			float x = event.getX(pointerIndex);
			float y = event.getY(pointerIndex);

			List<String> supportedFocusModes = params.getSupportedFocusModes();
			if (supportedFocusModes != null
					&& supportedFocusModes
							.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
				camera.autoFocus(new Camera.AutoFocusCallback() {
					@Override
					public void onAutoFocus(boolean b, Camera camera) {
						// currently set to auto-focus on single touch
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	*//** Determine the space between the first two fingers *//*
	private float getFingerSpacing(MotionEvent event) {
		// ...
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return (float) Math.sqrt(x * x + y * y);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// Get the pointer ID
		try {
			if(camera==null){
				return false;
			}
			Camera.Parameters params = camera.getParameters();
			int action = event.getAction();

			if (event.getPointerCount() > 1) {
				// handle multi-touch events
				if (action == MotionEvent.ACTION_POINTER_DOWN) {
					mDist = getFingerSpacing(event);
				} else if (action == MotionEvent.ACTION_MOVE
						&& params.isZoomSupported()) {
					camera.cancelAutoFocus();
					handleZoom(event, params);
				}
			} else {
				// handle single touch events
				if (action == MotionEvent.ACTION_UP) {
					// handleFocus(event, params);
				}
			}
			try {
				handleFocus(event, params);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	private void handleZoom(MotionEvent event, Camera.Parameters params) {
		try { 
			if(camera==null){
				return;
			}
			int maxZoom = params.getMaxZoom();
			int zoom = params.getZoom();
			float newDist = getFingerSpacing(event);
			if (newDist > mDist) {
				// zoom in
				if (zoom < maxZoom)
					zoom++;
			} else if (newDist < mDist) {
				// zoom out
				if (zoom > 0)
					zoom--;
			}
			mDist = newDist;
			params.setZoom(zoom);
			camera.setParameters(params);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
*/
}
