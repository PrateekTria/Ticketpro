package com.ticketpro.parking.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.ticketpro.model.Feature;
import com.ticketpro.parking.R;
import com.ticketpro.util.DateUtil;
import com.ticketpro.util.Preview;
import com.ticketpro.util.StringUtil;
import com.ticketpro.util.TPConstant;
import com.ticketpro.util.TPUtility;
import com.ticketpro.util.VerticalSeekBar;

import org.apache.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class CaptureImageActivity extends Activity {
    private Uri picUri;
    private String imagePath;
    private Preview cView;
    private Handler handler;
    private Bitmap bitmap;
    private ImageView flashlightLEDImage;
    private ImageView flashlightModeImage;
    private boolean flashlightLED = false;
    private FlashLightMode flashlightMode = FlashLightMode.AUTO;
    private VerticalSeekBar verticalSeekBar;
    private CheckBox stickyZoom;
    private int maxZoomLevel = 0;
    private int savedzoomLevel = 0;
    private SharedPreferences mPreferences;
    private TPApplication TPApp = TPApplication.getInstance();
    private Logger log = Logger.getLogger("CaptureImageActivity");
    private CheckBox nightModeCheck;
    private boolean nightMode;
    private boolean isALPRRequest;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if take picture is allowed on not.
      /*  try {
            if (TPUtility.isRunningOnEmulator(getApplicationContext())) {
                Toast.makeText(this, "This feature is disabled.", Toast.LENGTH_LONG).show();
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        cView = new Preview(this);
        setContentView(cView);

        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.capture_image, null, false);
        addContentView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        Intent data = getIntent();
        imagePath = data.getStringExtra("ImagePath");
        if (StringUtil.isEmpty(imagePath)) {
            imagePath = TPUtility.getDataFolder() + "Image_" + (new Date().getTime());
        }
        isALPRRequest = data.getBooleanExtra("isALPRRequest", false);

        verticalSeekBar = (VerticalSeekBar) findViewById(R.id.verticalSeekBar);
        stickyZoom = (CheckBox) findViewById(R.id.stickyZoom);
        nightModeCheck = (CheckBox) findViewById(R.id.nightModeCheck);

        flashlightLEDImage = (ImageView) findViewById(R.id.flashlight_led_imgview);
        flashlightLEDImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    cView.setFlashlightMode(Camera.Parameters.FLASH_MODE_OFF);
                    if (!flashlightLED) {
                        flashlightLEDImage.setImageResource(R.drawable.flashlight_disable);
                        flashlightLED = true;
                        cView.setFlashlightMode(Camera.Parameters.FLASH_MODE_TORCH);
                    } else {
                        flashlightLEDImage.setImageResource(R.drawable.flashlight);
                        flashlightLED = false;
                        cView.setFlashlightMode(Camera.Parameters.FLASH_MODE_OFF);
                    }

                    TPApplication.getInstance().pictureFlashLED = flashlightLED;

                } catch (Exception e) {
                    Log.e("TakePicture", "Error :" + TPUtility.getPrintStackTrace(e));
                }
            }
        });

        flashlightModeImage = (ImageView) findViewById(R.id.flashlight_mode_imgview);
        flashlightModeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (flashlightMode == FlashLightMode.AUTO) {
                        flashlightMode = FlashLightMode.OFF;
                        flashlightModeImage.setImageResource(R.drawable.flashlight_off);
                        cView.setFlashlightMode(Camera.Parameters.FLASH_MODE_OFF);

                    } else if (flashlightMode == FlashLightMode.OFF) {
                        flashlightMode = FlashLightMode.ON;
                        flashlightModeImage.setImageResource(R.drawable.flashlight_on);
                        cView.setFlashlightMode(Camera.Parameters.FLASH_MODE_ON);

                    } else {
                        flashlightMode = FlashLightMode.AUTO;
                        flashlightModeImage.setImageResource(R.drawable.flashlight_auto);
                        cView.setFlashlightMode(Camera.Parameters.FLASH_MODE_AUTO);
                    }

                } catch (Exception e) {
                    Log.e("TakePicture", "Error :" + TPUtility.getPrintStackTrace(e));
                }
            }
        });

        stickyZoom.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int progress = verticalSeekBar.getProgress();
                if (isChecked) {
                    updateProgress(progress);
                } else {
                    updateProgress(0);
                }
            }
        });

        verticalSeekBar.setOnSeekBarChangeListener(new VerticalSeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                try {
                    Camera camera = cView.camera;
                    if (camera.getParameters().isZoomSupported()) {
                        maxZoomLevel = camera.getParameters().getMaxZoom();
                        verticalSeekBar.setMax(maxZoomLevel);
                        if (progress <= maxZoomLevel) {
                            Camera.Parameters p = camera.getParameters();
                            p.setZoom(progress);
                            camera.setParameters(p);
                            if (stickyZoom.isChecked()) {
                                updateProgress(progress);
                            } else {
                                updateProgress(0);
                            }
                        }
                    }
                } catch (Exception e) {
                    log.error(TPUtility.getPrintStackTrace(e));
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

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startPreview();
            }
        }, 500);

        try {
            if (isALPRRequest) {
                mPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
                nightMode = mPreferences.getBoolean(TPConstant.PREFS_KEY_SETTING_ALPR_VEHICLE_NIGHT_MODE, false);
                nightModeCheck.setVisibility(View.VISIBLE);
                nightModeCheck.setChecked(nightMode);

                nightModeCheck.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            nightMode = true;
                            updateNightModeStatus(isChecked);
                        } else {
                            nightMode = false;
                            updateNightModeStatus(isChecked);
                        }
                    }
                });
            } else {
                nightMode = false;
                nightModeCheck.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateProgress(int progress) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(TPConstant.PREFS_KEY_SETTING_STICKY_ZOOM, progress);
        editor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (cView == null) {
            return;
        }

        // Turn on FlashLED if it was turned on earlier
        flashlightLED = TPApp.pictureFlashLED;
        if (flashlightLED) {
            flashlightLEDImage.setImageResource(R.drawable.flashlight_disable);
            cView.setFlashlightMode(Camera.Parameters.FLASH_MODE_TORCH);
        } else {
            flashlightLEDImage.setImageResource(R.drawable.flashlight);
            cView.setFlashlightMode(Camera.Parameters.FLASH_MODE_OFF);
        }

        if (flashlightMode == FlashLightMode.AUTO) {
            flashlightMode = FlashLightMode.OFF;
            flashlightModeImage.setImageResource(R.drawable.flashlight_off);
            cView.setFlashlightMode(Camera.Parameters.FLASH_MODE_OFF);
        } else if (flashlightMode == FlashLightMode.OFF) {
            flashlightMode = FlashLightMode.ON;
            flashlightModeImage.setImageResource(R.drawable.flashlight_on);
            cView.setFlashlightMode(Camera.Parameters.FLASH_MODE_ON);
        } else {
            flashlightMode = FlashLightMode.AUTO;
            flashlightModeImage.setImageResource(R.drawable.flashlight_auto);
            cView.setFlashlightMode(Camera.Parameters.FLASH_MODE_AUTO);
        }

        try {
            mPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
            savedzoomLevel = mPreferences.getInt(TPConstant.PREFS_KEY_SETTING_STICKY_ZOOM, 0);
            Handler handler = new Handler();

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        Camera camera = cView.camera;
                        if (camera == null) {
                            return;
                        }
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
                        log.error(TPUtility.getPrintStackTrace(e));
                    }
                }
            }, 500);

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }

    private void startPreview() {
        if (cView != null) {
            // Start Camera Preview and Register PreviewCallback
            if (cView.camera != null) {
                cView.camera.startPreview();
            }

            cView.registerPreviewCallback();

            // Turn on Flash Torch if already enabled
            if (TPApp.pictureFlashLED) {
                try {
                    if (cView != null) {
                        cView.setFlashlightMode(Camera.Parameters.FLASH_MODE_TORCH);
                    }
                } catch (Exception e) {
                    log.error("Error " + e.getMessage());
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        backAction(null);
    }

    public void backAction(View view) {
        try {
            if (cView != null) {
                if (cView.camera != null) {
                    cView.camera.stopPreview();
                }

                cView.setFlashlightMode(Camera.Parameters.FLASH_MODE_OFF);
            }
        } catch (Exception e) {
        }

        // Clear Bitmap
        if (bitmap != null) {
            bitmap.recycle();
            System.gc();
        }

        setResult(RESULT_CANCELED);
        finish();
    }

    public void captureAction(View view) {
        File file = new File(imagePath);

        try {
            if (cView.camera == null) {
                return;
            }

            file.createNewFile();

            picUri = Uri.fromFile(file);
            cView.playSoundEffect(SoundEffectConstants.CLICK);
            if (cView.previewBitmapData == null) {
                return;
            }

            // Turn on torch mode
            try {
                if (flashlightMode == FlashLightMode.AUTO || flashlightMode == FlashLightMode.ON) {
                    cView.setFlashlightMode(Camera.Parameters.FLASH_MODE_TORCH);
                }

                cView.setFlashlightMode(Camera.Parameters.FLASH_MODE_OFF);
            } catch (Exception e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }

            final byte[] data = cView.previewBitmapData.clone();
            cView.camera.stopPreview();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Camera.Parameters parameters = cView.camera.getParameters();
                        Size size = parameters.getPreviewSize();

                        YuvImage yuvimage = new YuvImage(data, ImageFormat.NV21, size.width, size.height, null);
                        ByteArrayOutputStream outstr = new ByteArrayOutputStream();
                        Rect rect = new Rect(0, 0, size.width, size.height);
                        yuvimage.compressToJpeg(rect, 90, outstr);

                        // Set MAX Image resolutions
                        int imageWidth = size.width;
                        int imageHeight = size.height;
                        try {
                            String imageSize = Feature.getFeatureValue("MaxImageResolution");
                            if (!StringUtil.isEmpty(imageSize)) {
                                String[] sizes = imageSize.split("x");
                                if (sizes.length == 2) {
                                    imageWidth = Integer.parseInt(sizes[0]);
                                    imageHeight = Integer.parseInt(sizes[1]);
                                }
                            }
                        } catch (Exception e) {
                            log.error(TPUtility.getPrintStackTrace(e));
                        }

                        boolean isLandscape = false;
                        int degrees = cView.getRotationAngle();
                        if (degrees == 0 || degrees == 180) {
                            isLandscape = true;
                            //imageResolution = imageHeight + "x" + imageWidth;
                        } else {
                            //imageResolution = imageWidth + "x" + imageHeight;
                        }

                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = TPUtility.getBitmapScale(size.width, size.height, imageWidth, imageHeight);
                        options.inJustDecodeBounds = false;
                        options.outWidth = imageWidth;
                        options.outHeight = imageHeight;

                        bitmap = BitmapFactory.decodeByteArray(outstr.toByteArray(), 0, outstr.size(), options);
                        bitmap = TPUtility.resizeBitmap(bitmap, imageWidth, imageHeight);

                        // Print date and time on photo
                        try {
                            Matrix matrix = new Matrix();
                            matrix.postRotate(degrees);
                            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);

                            if (Feature.isFeatureAllowed(Feature.PRINT_TIME_ON_PHOTO)) {
                                int y = bitmap.getHeight() - 30;
                                int x = 20;

                                if (isLandscape) {
                                    bitmap = bitmap.copy(Config.ARGB_8888, true);
                                }

                                Paint paint = new Paint();
                                paint.setColor(Color.RED);
                                paint.setTextSize(20);
                                paint.setFlags(Paint.ANTI_ALIAS_FLAG);
                                Canvas canvas = new Canvas(bitmap);
                                canvas.drawText(DateUtil.getCurrentDateTime(), x, y, paint);
                            }
                        } catch (Exception e) {
                            log.error(TPUtility.getPrintStackTrace(e));
                        }

                        FileOutputStream fos = new FileOutputStream(picUri.getPath());
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
                        fos.flush();
                        fos.close();

                    } catch (IOException e) {
                        log.error(TPUtility.getPrintStackTrace(e));
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Clear Bitmap
                            if (bitmap != null) {
                                bitmap.recycle();
                                System.gc();
                            }

                            Intent intent = new Intent();
                            if (picUri != null) {
                                intent.putExtra("ImagePath", picUri.getPath());
                            }
                            if (isALPRRequest) {
                                intent.putExtra("vehicleInfoRequired", nightMode);
                            }
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    });
                }
            }).start();

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }

    /**
     * @return - Return overridden prefs. of user to prevent vehicle request instead of plate.
     * @Params - Nothing Required as param
     * @Required - Application setting required
     * @Uses -  It controls the request point uses and helps to control the cost of request.
     * @since - Version - 3.2.013
     **/
	/*private boolean isALPRVehicleRequired() {
		boolean isALPRVehicleInfoRequired = false;
		if (TPApplication.getInstance().getUserSettings() != null && TPApplication.getInstance().getUserSettings().isALPRVehicleRequired()) {
			isALPRVehicleInfoRequired = true;
		} else {
			isALPRVehicleInfoRequired = false;
		}

		return isALPRVehicleInfoRequired;
	}*/
    private void updateNightModeStatus(boolean isChecked) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putBoolean(TPConstant.PREFS_KEY_SETTING_ALPR_VEHICLE_NIGHT_MODE, isChecked);
        editor.commit();
    }

    enum FlashLightMode {
        ON, OFF, AUTO
    }
}
