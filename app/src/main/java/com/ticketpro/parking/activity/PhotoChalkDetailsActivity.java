package com.ticketpro.parking.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import android.widget.TextView;
import android.widget.Toast;

import com.ticketpro.model.ALPRChalk;
import com.ticketpro.model.ChalkPicture;
import com.ticketpro.model.Duration;
import com.ticketpro.model.Ticket;
import com.ticketpro.parking.R;
import com.ticketpro.parking.bl.ChalkBLProcessor;
import com.ticketpro.util.DateUtil;
import com.ticketpro.util.TPConstant;
import com.ticketpro.util.TPUtility;
import com.ticketpro.util.TouchImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

/**
 * Home screen (Ref. Mockups).
 *
 * @author : TicketPRO
 * @version : 1.0
 * @since : 1.0
 */

public class PhotoChalkDetailsActivity extends BaseActivityImpl {

    final int DATA_ERROR = 0;
    final int DATA_SUCCESSFULL = 1;
    final int ERROR_LOAD = 1;
    final int ERROR_SERVICE = 2;
    private TouchImageView mainPhoto;
    //private ArrayList<ChalkVehicle> chalks;
    private ArrayList<ALPRChalk> alprChalks;
    private ProgressDialog progressDialog;
    private int PHOTO_INDEX = 0;
    private long chalkId;
    private TextView timeTextView;
    private TextView gpsTextView;
    private TextView currentChalkTextView;
    private TextView elaspedTextView;
    private TextView locationTextView;
    private Bitmap bitmap;
    private Button writeTicketButton;
    private GestureDetector gestureDetector;
    private Handler dataLoadingHandler;
    private Handler errorHandler;

    /**
     * Entry point of the Activity
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            setContentView(R.layout.photo_chalk_details);
            mainPhoto = (TouchImageView) findViewById(R.id.photo_chalk_details_main_photo);

            setLogger(PhotoChalkDetailsActivity.class.getName());
            setBLProcessor(new ChalkBLProcessor((TPApplication) getApplicationContext()));
            setActiveScreen(this);

            timeTextView = (TextView) findViewById(R.id.photo_chalk_detail_time_textview);
            gpsTextView = (TextView) findViewById(R.id.photo_chalk_details_gps_textview);
            elaspedTextView = (TextView) findViewById(R.id.photo_chalk_details_elasped_textview);
            locationTextView = (TextView) findViewById(R.id.photo_chalk_details_location_textview);
            currentChalkTextView = (TextView) findViewById(R.id.current_chalk_textview);
            writeTicketButton = (Button) findViewById(R.id.chalk_details_ticket_btn);

            dataLoadingHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);

                    if (msg.what == DATA_SUCCESSFULL) {
                        showImage();
                    }

                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                }
            };

            errorHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if (progressDialog.isShowing())
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
            mainPhoto.setOnTouchListener(gestureListener);
            bindDataAtLoadingTime();

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }

    public void bindDataAtLoadingTime() {
        bindDataAtLoadingTime(0);
    }

    public void bindDataAtLoadingTime(final int reloadCount) {
        progressDialog = ProgressDialog.show(this, "", "Loading...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Intent data = getIntent();
                    chalkId = data.getLongExtra("ChalkId", 0);
                    //chalks = ((ChalkBLProcessor) screenBLProcessor).getChalkByPhoto(PhotoChalkDetailsActivity.this);
                    alprChalks = ((ChalkBLProcessor) screenBLProcessor).getChalkByPhotoALPR(PhotoChalkDetailsActivity.this);
                    for (int i = 0; i < alprChalks.size(); i++) {
                        if (alprChalks.get(i).getChalkId() == chalkId) {
                            PHOTO_INDEX = i;
                            break;
                        }
                    }

                    dataLoadingHandler.sendEmptyMessage(DATA_SUCCESSFULL);

                } catch (Exception e) {
                    errorHandler.sendEmptyMessage(reloadCount);
                }
            }
        }).start();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            try {
                ALPRChalk chalk = ALPRChalk.getChalkVehicleById(chalkId);
                if (chalk == null) {
                    setResult(RESULT_OK);
                    finish();
                }
            } catch (Exception e) {
            }
        }
    }


    public void removeAction(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Confirmation")
                .setMessage("Are you sure you want to delete photo chalk?")
                .setCancelable(true)
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            ALPRChalk chalk = alprChalks.get(PHOTO_INDEX);
                            if (chalk == null)
                                return;
                            ALPRChalk.removeChalkById(chalk.getChalkId(), "");
                            alprChalks.remove(PHOTO_INDEX);
                            if (alprChalks.size() == 0) {
                                backAction(null);
                            } else {
                                PHOTO_INDEX = 0;
                                showImage();
                            }
                        } catch (Exception ae) {
                            log.error(ae);
                        }
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }


    @Override
    public void onBackPressed() {
        backAction(null);
    }

    public void backAction(View view) {
        // Clear Bitmap
        if (bitmap != null) {
            bitmap.recycle();
            System.gc();
        }

        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onClick(View v) {

    }

    public void next(View v) {
        if (PHOTO_INDEX < (alprChalks.size() - 1)) {
            PHOTO_INDEX++;
        }

        showImage();
    }

    public void previous(View v) {
        if (PHOTO_INDEX > 0) {
            PHOTO_INDEX--;
        }

        showImage();
    }

    public void writeTicketAction(View view) {
        ALPRChalk chalk = alprChalks.get(PHOTO_INDEX);
        if (chalk == null)
            return;

        try {
            int mins = Duration.getDurationMinsById(chalk.getChalkDurationId(), this);
            long diff = (new Date().getTime() - chalk.getFirstDateTime().getTime());
            long expTime = (diff / 1000) / 60;
            if (expTime < mins) {
                displayErrorMessage("Chalk is not exipired. You can write ticket for expired chalks only.");
                return;
            }
        } catch (Exception ignored) {
        }


        try {
            Ticket ticket = TPApp.createTicketForChalk(chalk);
            TPApp.setActiveTicket(ticket);

            Intent i = new Intent();
            i.setClass(this, WriteTicketActivity.class);
            i.putExtra("ChalkId", chalk.getChalkId());
            startActivityForResult(i, 0);

        } catch (Exception e) {
            Toast.makeText(this, TPConstant.GENERIC_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
        }

        return;
    }

    public void showImage() {
        ALPRChalk chalk = alprChalks.get(PHOTO_INDEX);
        ChalkPicture picture = null;
        if (chalk.getChalkPictures().size() > 0)
            picture = chalk.getChalkPictures().get(0);

        if (picture == null) {
            Toast.makeText(this, "Chalk picture not available.", Toast.LENGTH_LONG).show();
        }

        try {
            // Clear Bitmap
            if (bitmap != null) {
                bitmap.recycle();
                System.gc();
            }

            currentChalkTextView.setText((PHOTO_INDEX + 1) + "/" + alprChalks.size());
            try {
                int mins = Duration.getDurationMinsById(chalk.getChalkDurationId(), this);
                long diff = (new Date().getTime() - chalk.getFirstDateTime().getTime());
                long expTime = (diff / 1000) / 60;
                if (expTime < mins) {
                    chalk.setIsExpired("N");
                    writeTicketButton.setClickable(false);
                    writeTicketButton.setBackgroundResource(R.drawable.btn_disabled);
                } else {
                    chalk.setIsExpired("Y");
                    writeTicketButton.setClickable(true);
                    writeTicketButton.setBackgroundResource(R.drawable.btn_blue);
                }
            } catch (Exception e) {
            }

            if (picture != null) {
                File imgFile = new File(picture.getImagePath());
                if (imgFile.exists()) {
                    bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mainPhoto.setImageBitmap(bitmap);
                        }
                    });
                } else {
                    Toast.makeText(this, "Image file not available.", Toast.LENGTH_LONG).show();
                }
            }

            locationTextView.setText(chalk.getChalkLocation());
            if (chalk.getFirstLocLat() != null && chalk.getFirstLocLong() != null)
                gpsTextView.setText(chalk.getFirstLocLat() + "," + chalk.getFirstLocLong());

            timeTextView.setText(DateUtil.getSQLStringFromDate(chalk.getFirstDateTime()));
            long diff = (new Date().getTime()) - chalk.getFirstDateTime().getTime();
            int minutes = (int) ((diff / (1000 * 60)) % 60);
            int hours = (int) ((diff / (1000 * 60 * 60)));
            String hrs = (hours < 10) ? ("0" + hours) : hours + "";
            String min = (minutes < 10) ? ("0" + minutes) : minutes + "";
            elaspedTextView.setText(hrs + ":" + min + " hrs/min");

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (gestureDetector.onTouchEvent(event)) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    public void onLeftSwipe() {
        if (PHOTO_INDEX < (alprChalks.size() - 1)) {
            PHOTO_INDEX++;
        }

        showImage();
    }

    public void onRightSwipe() {

        if (PHOTO_INDEX > 0) {
            PHOTO_INDEX--;
        }

        showImage();
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

    private class SwipeGestureDetector extends SimpleOnGestureListener {
        private static final int SWIPE_MIN_DISTANCE = 50;
        private static final int SWIPE_MAX_OFF_PATH = 200;
        private static final int SWIPE_THRESHOLD_VELOCITY = 200;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                float diffAbs = Math.abs(e1.getY() - e2.getY());
                float diff = e1.getX() - e2.getX();

                if (diffAbs > SWIPE_MAX_OFF_PATH)
                    return false;

                // Left swipe
                if (diff > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    PhotoChalkDetailsActivity.this.onLeftSwipe();
                }
                // Right swipe
                else if (-diff > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    PhotoChalkDetailsActivity.this.onRightSwipe();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
    }

}
