package com.ticketpro.parking.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ticketpro.parking.R;
import com.ticketpro.parking.bl.ChalkBLProcessor;
import com.ticketpro.util.CircleShape;
import com.ticketpro.util.TPUtility;

/**
 * Home screen (Ref. Mockups).
 *
 * @author : TicketPRO
 * @version : 1.0
 * @since : 1.0
 */

public class LocationChalkActivity extends BaseActivityImpl {
    final int CIRCLE_CENTER_X = 80;
    final int CIRCLE_CENTER_Y = 70;
    final int CIRCLE_R = 60;

    boolean cStatus = false;
    boolean sStatus = false;
    ImageView cImage;
    ImageView sImage;

    /**
     * Entry point of the Activity
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.location_chalk);
            setLogger(LocationChalkActivity.class.getName());
            setBLProcessor(new ChalkBLProcessor((TPApplication) getApplicationContext()));
            setActiveScreen(this);

            final RelativeLayout chalkPanel = (RelativeLayout) findViewById(R.id.location_chalk_circle_panel);

            DisplayMetrics metrics = getResources().getDisplayMetrics();
            int width = (metrics.widthPixels / 2);
            final int circleRadius = (width / 2) - 10;
            final int circleRadiusX = circleRadius + 10;
            final int circleRadiusY = circleRadius + 5;

            cImage = (ImageView) findViewById(R.id.chalk_location_c_img);
            cImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cStatus = true;
                    cImage.setImageResource(R.drawable.c_marker_selected);
                    sImage.setImageResource(R.drawable.s_marker);
                    sStatus = false;
                }
            });

            sImage = (ImageView) findViewById(R.id.chalk_location_s_img);
            sImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sStatus = true;
                    sImage.setImageResource(R.drawable.s_marker_selected);
                    cImage.setImageResource(R.drawable.c_marker);
                    cStatus = false;
                }
            });

            chalkPanel.addView(new CircleShape(this, circleRadiusX, circleRadiusY, circleRadius, 45, false));
            chalkPanel.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent e) {
                    float x = e.getX();
                    float y = e.getY();

                    int measuredValue = (int) Math.sqrt((circleRadiusX - x) * (circleRadiusX - x) + (circleRadiusY - y) * (circleRadiusY - y));

                    if (e.getAction() == MotionEvent.ACTION_UP) {
                        if (cStatus) {
                            if (measuredValue <= circleRadius && measuredValue >= circleRadius - 30) {
                                RelativeLayout.LayoutParams relLP = new RelativeLayout.LayoutParams(20, 20);
                                relLP.setMargins((int) x, (int) y, 0, 0);
                                cImage.setLayoutParams(relLP);
                            } else {
                                RelativeLayout.LayoutParams relLP = new RelativeLayout.LayoutParams(20, 20);
                                relLP.setMargins(0, 0, 0, 0);
                                cImage.setLayoutParams(relLP);

                            }

                        }
                        if (sStatus) {
                            if (measuredValue < circleRadius - 30) {
                                RelativeLayout.LayoutParams relLP = new RelativeLayout.LayoutParams(20, 20);
                                relLP.setMargins((int) x, (int) y, 0, 0);
                                sImage.setLayoutParams(relLP);
                            } else {
                                RelativeLayout.LayoutParams relLP = new RelativeLayout.LayoutParams(20, 20);
                                relLP.setMargins(20, 0, 0, 0);
                                sImage.setLayoutParams(relLP);

                            }
                        }

                        cStatus = false;
                        sStatus = false;
                        cImage.setImageResource(R.drawable.c_marker);
                        sImage.setImageResource(R.drawable.s_marker);

                    } else if (e.getAction() == MotionEvent.ACTION_MOVE) {
                        RelativeLayout.LayoutParams relLP = new RelativeLayout.LayoutParams(20, 20);
                        relLP.setMargins((int) x, (int) y, 0, 0);
                        if (cStatus) {
                            cImage.setLayoutParams(relLP);
                        }
                        if (sStatus) {
                            sImage.setLayoutParams(relLP);
                        }
                    }

                    return true;
                }
            });

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }

    public void bindDataAtLoadingTime() {

    }

    @Override
    public void onClick(View v) {

    }

    public void viewList(View view) {
        Intent i = new Intent();
        i.setClass(this, LocationChalkListActivity.class);
        startActivity(i);
        return;
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

    public void ticketLogsAction(View view) {
        Intent intent = new Intent(this, TicketLogsActivity.class);
        startActivity(intent);
    }
}
