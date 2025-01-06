package com.ticketpro.parking.activity;

import java.util.ArrayList;
import java.util.Date;

import com.gdacciaro.iOSDialog.iOSDialog;
import com.gdacciaro.iOSDialog.iOSDialogBuilder;
import com.gdacciaro.iOSDialog.iOSDialogClickListener;
import com.ticketpro.model.ChalkPicture;
import com.ticketpro.model.ChalkVehicle;
import com.ticketpro.model.Duration;
import com.ticketpro.model.Feature;
import com.ticketpro.model.PrintTemplate;
import com.ticketpro.model.Ticket;
import com.ticketpro.model.User;
import com.ticketpro.parking.R;
import com.ticketpro.parking.bl.ChalkBLProcessor;
import com.ticketpro.print.TicketPrinter;
import com.ticketpro.util.ChalkTokenParser;
import com.ticketpro.util.DateUtil;
import com.ticketpro.util.TPConstant;
import com.ticketpro.util.TPUtility;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Home screen (Ref. Mockups).
 *
 * @author : TicketPRO
 * @version : 1.0
 * @since : 1.0
 */

public class ChalkedVehicleDetailsActivity extends BaseActivityImpl {

    private ProgressDialog progressDialog;
    private Handler errorHandler;

    private EditText meterEditText;
    private EditText plateNumberEditText;
    private EditText locationEditText;
    private EditText tmEditText;
    private EditText elapsedEditText;
    private EditText zoneEditText;
    private EditText colorEditText;
    private EditText makeEditText;
    private EditText notesEditText;
    private TextView chalksNavigationText;
    private EditText officerName;
    private Spinner tireSpinner;
    private String tireDisplayNames[] = {"", "Front Left", "Front Right", "Back Left", "Back Right"};
    private Button writeTicketButton;
    private Button commentsButton;
    private Button mapViewButton;
    private int currentIndex;
    private ArrayList<ChalkVehicle> chalks;
    private GestureDetector gestureDetector;
    private ChalkVehicle activeChalk;

    ImageView cImage;
    ImageView sImage;

    final int DATA_ERROR = 0;
    final int DATA_SUCCESSFULL = 1;
    final int ERROR_LOAD = 1;
    final int ERROR_SERVICE = 2;

    final int REQUEST_PICTURE = 1;
    final int REQUEST_WRITE_TICKET = 2;

    private PrintTemplate selectedTemplate;

    /**
     * Entry point of the Activity
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.chalked_vehicle_details);
        setLogger(ChalkedVehicleDetailsActivity.class.getName());
        setBLProcessor(new ChalkBLProcessor((TPApplication) getApplicationContext()));
        setActiveScreen(this);

        tireSpinner = (Spinner) findViewById(R.id.chalk_details_tire_spinner);
        plateNumberEditText = (EditText) findViewById(R.id.chalk_details_plate_textview);
        meterEditText = (EditText) findViewById(R.id.chalk_details_meter_textview);
        locationEditText = (EditText) findViewById(R.id.chalk_details_location_textview);
        tmEditText = (EditText) findViewById(R.id.chalk_details_time_textview);
        elapsedEditText = (EditText) findViewById(R.id.chalk_details_elapsed_textview);
        zoneEditText = (EditText) findViewById(R.id.chalk_details_zone_textview);
        makeEditText = (EditText) findViewById(R.id.chalk_details_make_textview);
        colorEditText = (EditText) findViewById(R.id.chalk_details_color_textview);
        notesEditText = (EditText) findViewById(R.id.chalk_details_notes_textview);
        chalksNavigationText = (TextView) findViewById(R.id.chalk_details_navigation);
        officerName = (EditText) findViewById(R.id.chalk_details_marked_officer);

        cImage = (ImageView) findViewById(R.id.chalk_vehicle_c_img);
        sImage = (ImageView) findViewById(R.id.chalk_vehicle_s_img);
        writeTicketButton = (Button) findViewById(R.id.chalk_details_ticket_btn);
        commentsButton = (Button) findViewById(R.id.chalk_details_comments_btn);
        mapViewButton = (Button) findViewById(R.id.chalk_details_view_map);

        errorHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                if (progressDialog.isShowing())
                    progressDialog.dismiss();

                switch (msg.what) {
                    case ERROR_LOAD:
                        Toast.makeText(ChalkedVehicleDetailsActivity.this, "Failed to load chalk details. Please try again.", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                        break;

                    case ERROR_SERVICE:
                        Toast.makeText(ChalkedVehicleDetailsActivity.this, TPConstant.GENERIC_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
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

        bindDataAtLoadingTime();
    }

    public void bindDataAtLoadingTime() {
        progressDialog = ProgressDialog.show(this, "", "Loading...");
        new Thread() {
            public void run() {
                try {
                    chalks = TPApp.chalkList;
                    long chalkId = getIntent().getLongExtra("ChalkId", 0);
                    currentIndex = getIntent().getIntExtra("ListIndex", -1);

                    if (currentIndex == -1) {
                        chalks = new ArrayList<>();
                        chalks.add(ChalkVehicle.getChalkVehicleById(chalkId));
                        currentIndex = 0;
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateChalkDetails();
                        }
                    });

                } catch (Exception ae) {
                    errorHandler.sendEmptyMessage(ERROR_SERVICE);
                }
            }
        }.start();
    }


    private void updateChalkDetails() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();

        if (chalks == null || currentIndex == -1) {
            errorHandler.sendEmptyMessage(ERROR_LOAD);
            return;
        }

        activeChalk = chalks.get(currentIndex);

        checkPicture();

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = (metrics.widthPixels / 2);
        String screenDensityName = TPApp.getDensityName(TPApp.getApplicationContext());

        int circleRadius;
        if (screenDensityName.equalsIgnoreCase("xxhdpi") || screenDensityName.equalsIgnoreCase("xxxhdpi")) {
            circleRadius = (width / 2) - 10;
        } else {
            circleRadius = (width / 2) - 15;
        }

        //int circleRadius=(width/2)-15;
        int circleRadiusX = circleRadius;
        int circleRadiusY = circleRadius;
        int innerCircleRadius = circleRadius - 60;

        RelativeLayout chalkPanel = (RelativeLayout) findViewById(R.id.chalked_vehicle_details_circle_panel);
        chalkPanel.getLayoutParams().width = circleRadius * 2;
        chalkPanel.getLayoutParams().height = circleRadius * 2;

        if (activeChalk.getChalkx() > -1 && activeChalk.getChalky() > -1) {
            int x1 = (int) ((circleRadius - 20) * Math.cos(activeChalk.getChalkx() * Math.PI / 180) + circleRadiusX) - 20;
            int y1 = (int) ((circleRadius - 20) * Math.sin(activeChalk.getChalky() * Math.PI / 180) + circleRadiusY) - 20;
            RelativeLayout.LayoutParams cLayout = (RelativeLayout.LayoutParams) cImage.getLayoutParams();
            cLayout.setMargins(x1, y1, 0, 0);
            cLayout.setMarginStart(x1);
            if (screenDensityName.equalsIgnoreCase("xxhdpi") || screenDensityName.equalsIgnoreCase("xxxhdpi")) {
                if ((x1 - y1) > 150) {
                    cLayout.setMargins(x1 - 40, y1, 0, 0);
                    cLayout.setMarginStart(x1 - 40);
                } else {
                    cLayout.setMargins(x1, y1 - 40, 0, 0);
                    cLayout.setMarginStart(x1);
                }
            }
            cImage.setLayoutParams(cLayout);
        } else {
            cImage.setVisibility(View.GONE);
        }

        if (activeChalk.getStemx() > -1 && activeChalk.getStemy() > -1) {
            int x2 = (int) (innerCircleRadius * Math.cos(activeChalk.getStemx() * Math.PI / 180) + circleRadiusX) - 20;
            int y2 = (int) (innerCircleRadius * Math.sin(activeChalk.getStemy() * Math.PI / 180) + circleRadiusY) - 20;
            RelativeLayout.LayoutParams sLayout = (RelativeLayout.LayoutParams) sImage.getLayoutParams();
            sLayout.setMargins(x2, y2, 0, 0);
            if (x2 > circleRadius) {
                sLayout.setMarginStart(x2 - 40);
            } else
                sLayout.setMarginStart(x2);
            if (screenDensityName.equalsIgnoreCase("xxhdpi") || screenDensityName.equalsIgnoreCase("xxxhdpi")) {
                sLayout.setMargins(x2, y2 - 10, 0, 0);
                sLayout.setMarginStart(x2);
            }
            sImage.setLayoutParams(sLayout);
        } else {
            sImage.setVisibility(View.GONE);
        }

        chalksNavigationText.setText((currentIndex + 1) + "/" + (chalks.size()));

        plateNumberEditText.setText(activeChalk.getPlate());
        meterEditText.setText(activeChalk.getMeter());
        locationEditText.setText(TPUtility.getFullAddress(activeChalk));
        tmEditText.setText(DateUtil.getStringFromDateShortYear(activeChalk.getChalkDate()));
        zoneEditText.setText(Duration.getDurationTitleById(activeChalk.getDurationId()));
        makeEditText.setText(activeChalk.getMake());
        colorEditText.setText(activeChalk.getColor());
        notesEditText.setText(activeChalk.getNotes());
        commentsButton.setText("Comments(" + activeChalk.getComments().size() + ")");

        if (activeChalk == null || (activeChalk.getLatitude() == null || activeChalk.getLatitude().equals("") || activeChalk.getLongitude().equalsIgnoreCase("null"))) {

            mapViewButton.setClickable(false);
            mapViewButton.setBackgroundResource(R.drawable.btn_disabled);
        }

        try {
            officerName.setText(User.getUserInfo(TPApp.getCurrentUserId()).getUsername());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            int mins = Duration.getDurationMinsById(activeChalk.getDurationId(), this);
            long diff = (new Date().getTime() - activeChalk.getChalkDate().getTime());
            long expTime = (diff / 1000) / 60;

            int minutes = (int) ((diff / (1000 * 60)) % 60);
            int hours = (int) ((diff / (1000 * 60 * 60)));
            String hrs = (hours < 10) ? ("0" + hours) : hours + "";
            String min = (minutes < 10) ? ("0" + minutes) : minutes + "";
            elapsedEditText.setText(hrs + ":" + min + " h/m");

            if (expTime < mins) {
                writeTicketButton.setClickable(false);
                writeTicketButton.setBackgroundResource(R.drawable.btn_disabled);
            } else {
                activeChalk.setIsExpired("Y");
            }
        } catch (Exception e) {
        }

        if (tireSpinner == null)
            return;

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ChalkedVehicleDetailsActivity.this, android.R.layout.simple_spinner_item, tireDisplayNames);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tireSpinner.setAdapter(dataAdapter);

        int position = dataAdapter.getPosition(activeChalk.getTire());
        tireSpinner.setSelection(position);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (gestureDetector.onTouchEvent(event)) {
            return true;
        }

        return super.onTouchEvent(event);
    }

    public void onLeftSwipe() {
        if (currentIndex < (chalks.size() - 1)) {
            currentIndex++;
        }

        updateChalkDetails();
    }

    public void onRightSwipe() {
        if (currentIndex > 0) {
            currentIndex--;
        }

        updateChalkDetails();
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
                    ChalkedVehicleDetailsActivity.this.onLeftSwipe();
                }
                // Right swipe
                else if (-diff > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    ChalkedVehicleDetailsActivity.this.onRightSwipe();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (activeChalk == null) {
            return;
        }

        try {
            ChalkVehicle chalk = ChalkVehicle.getChalkVehicleById(activeChalk.getChalkId());
            if (chalk == null) {
                setResult(RESULT_OK);
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_PICTURE) {
                checkPicture();
            } else if (requestCode == REQUEST_WRITE_TICKET) {
                try {
                    if (activeChalk!=null) {
                        ChalkVehicle chalk = ChalkVehicle.getChalkVehicleById(activeChalk.getChalkId());
                        if (chalk == null) {
                            setResult(RESULT_OK);
                            finish();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void checkPicture() {
        try {
            ArrayList<ChalkPicture> chalkPictures = ChalkPicture.getChalkPictures(activeChalk.getChalkId());
            Button btn = (Button) findViewById(R.id.chalk_details_picture_btn);
            if (chalkPictures.size() > 0) {
                btn.setText("View Pictures" + "(" + chalkPictures.size() + ")");
            } else {
                btn.setText("Take New Picture");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public void takePictureAction(View view) {
        if (activeChalk == null) {
            return;
        }


        Button btn = (Button) findViewById(R.id.chalk_details_picture_btn);
        if (btn.getText().toString().equals("Take New Picture")) {
            int maxPhotos = 0;
            if (Feature.isFeatureAllowed(Feature.MAX_PHOTOS)) {
                String value = Feature.getFeatureValue(Feature.MAX_PHOTOS);
                try {
                    maxPhotos = Integer.parseInt(value);
                    if (activeChalk.isLPRCaptured()) {
                        maxPhotos = maxPhotos + 1;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (maxPhotos > 0 && activeChalk.getChalkPictures().size() >= maxPhotos) {
                btn.setEnabled(false);
                displayErrorMessage("Exceeded max photos limit.");
                return;
            }

            Intent i = new Intent();
            i.setClass(ChalkedVehicleDetailsActivity.this, TakePictureActivity.class);
            i.putExtra("ChalkPicture", true);
            i.putExtra("ChalkId", activeChalk.getChalkId());
            startActivityForResult(i, REQUEST_PICTURE);
        } else {
            Intent i = new Intent();
            i.setClass(ChalkedVehicleDetailsActivity.this, ViewChalkPicturesActivity.class);
            i.putExtra("ChalkId", activeChalk.getChalkId());
            startActivityForResult(i, REQUEST_PICTURE);
        }

        return;
    }

    public void writeTicketAction(View view) {
        if (activeChalk == null)
            return;

        try {
            int mins = Duration.getDurationMinsById(activeChalk.getDurationId(), this);
            long diff = (new Date().getTime() - activeChalk.getChalkDate().getTime());
            long expTime = (diff / 1000) / 60;
            if (expTime < mins) {
                displayErrorMessage("Chalk is not exipired. You can write ticket for expired chalks only.");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        long chalkId = activeChalk.getChalkId();
        try {
            ChalkVehicle activeChalk = ChalkVehicle.getChalkVehicleById(chalkId);
            if (activeChalk != null) {

                if (!Ticket.chalkTicketIssue(activeChalk.getChalkId())) {
                    Ticket ticket = TPApp.createTicketForChalk(activeChalk);
                    TPApp.setActiveTicket(ticket);

                    Intent i1 = new Intent();
                    i1.setClass(ChalkedVehicleDetailsActivity.this, WriteTicketActivity.class);
                    i1.putExtra("ChalkId", chalkId);
                    startActivityForResult(i1, REQUEST_WRITE_TICKET);
                }else {
                    new iOSDialogBuilder(this)
                            .setSubtitle("Chalk already issued.")
                            .setPositiveListener("OK", new iOSDialogClickListener() {
                                @Override
                                public void onClick(iOSDialog dialog) {
                                    dialog.dismiss();
                                }
                            })
                            .build().show();
                }
            }
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            Toast.makeText(this, TPConstant.GENERIC_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
        }


     /*   try {
            Ticket ticket = TPApp.createTicketForChalk(activeChalk);
            ticket.setPhoto_count(activeChalk.getChalkPictures().size());
            TPApp.setActiveTicket(ticket);

            Intent i = new Intent();
            i.setClass(ChalkedVehicleDetailsActivity.this, WriteTicketActivity.class);
            i.putExtra("ChalkId", activeChalk.getChalkId());
            startActivityForResult(i, REQUEST_WRITE_TICKET);

        } catch (Exception e) {
            Toast.makeText(this, TPConstant.GENERIC_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
        }*/

        return;
    }

    public void commentsAction(View view) {
        Intent i = new Intent();
        i.setClass(this, ChalkCommentsActivity.class);
        i.putExtra("ChalkId", activeChalk.getChalkId());

        startActivityForResult(i, 0);
    }

    public void removeEntryAction(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Confirmation")
                .setMessage("Are you sure you want to delete?")
                .setCancelable(true)
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (activeChalk == null)
                            return;

                        try {
                            if (Feature.isFeatureAllowed(Feature.CHALK_REMOVE_REASION)) {
                                _selectOption();
                            } else {
                                ChalkVehicle.removeChalkById(activeChalk.getChalkId(), "");
                                if (chalks != null) {
                                    chalks.remove(currentIndex);
                                    if (chalks.size() == 0) {
                                        setResult(RESULT_OK);
                                        finish();
                                    } else {
                                        onRightSwipe();
                                    }
                                }
                            }

                        } catch (Exception ae) {
                            log.error(ae);
                        }
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();

    }

    private void _selectOption() {
        String featureValue = Feature.getFeatureValue(Feature.CHALK_REMOVE_REASION);
        final String[] split = featureValue.split(";");
        final AlertDialog.Builder myDialog =
                new AlertDialog.Builder(ChalkedVehicleDetailsActivity.this);
        myDialog.setTitle("Select Reason");
        myDialog.setItems(split, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                String item = split[which];
                try {
                    ChalkVehicle.removeChalkById(activeChalk.getChalkId(), item);
                    if (chalks != null) {
                        chalks.remove(currentIndex);
                        if (chalks.size() == 0) {
                            setResult(RESULT_OK);
                            finish();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        myDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

            }
        });
        myDialog.setPositiveButton("Add New Comment", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                _openEditDialog();

            }
        });

        myDialog.show();
    }

    private void _openEditDialog() {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(ChalkedVehicleDetailsActivity.this);
        View mView = this.getLayoutInflater().inflate(R.layout.chalk_remove_dialog, null);
        final EditText inputText = (EditText) mView.findViewById(R.id.add_comment_input_dialog_text_field);

        final Button mCancle = (Button) mView.findViewById(R.id.add_comment_input_dialog_cancel_btn);
        final Button mOk = (Button) mView.findViewById(R.id.add_comment_input_dialog_enter_btn);

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();
        mOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(inputText.getText().toString())) {
                    try {
                        TPUtility.hideKeyboard(ChalkedVehicleDetailsActivity.this);
                        ChalkVehicle.removeChalkById(activeChalk.getChalkId(), inputText.getText().toString());
                        if (chalks != null) {
                            chalks.remove(currentIndex);
                            if (chalks.size() == 0) {
                                setResult(RESULT_OK);
                                finish();
                            }
                        }

                        dialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {

                    TPUtility.hideKeyboard(ChalkedVehicleDetailsActivity.this);
                    dialog.dismiss();
                }

            }
        });

        mCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TPUtility.hideKeyboard(ChalkedVehicleDetailsActivity.this);
                dialog.dismiss();
            }
        });
        TPUtility.showSoftKeyboard(ChalkedVehicleDetailsActivity.this, inputText);
    }


    public void viewOnMapAction(View view) {
        if (activeChalk == null || (activeChalk.getLatitude() == null || activeChalk.getLatitude().equals(""))) {
            Toast.makeText(this, "GPS location not available to view on map", Toast.LENGTH_LONG).show();
            return;
        }

        Intent i = new Intent();
        i.setClass(this, ChalkVehicleOnMapViewActivity.class);
        i.putExtra("ChalkId", activeChalk.getChalkId());
        startActivity(i);
    }

    public void backAction(View view) {
        finish();
    }


    public void printAction(View view) {
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item);
        final ArrayList<PrintTemplate> templates = PrintTemplate.getChalkTemplates();

        for (PrintTemplate template : templates) {
            arrayAdapter.add(template.getDisplayName());
        }

        selectedTemplate = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Template");
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedTemplate = templates.get(which);
                printChalk(selectedTemplate);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void handleVoiceInput(String text) {
        if (text == null) return;

        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
        if (text.contains("BACK") || text.contains("GO BACK") || text.contains("CLOSE")) {
            backAction(null);
        }
    }

    @Override
    public void handleVoiceMode(boolean voiceMode) {
        // TODO Auto-generated method stub
    }

    @Override
    public void handleNetworkStatus(boolean connected, boolean isFastConnection) {
        // TODO Auto-generated method stuffs
    }

    private void printChalk(PrintTemplate template) {
        ChalkTokenParser tokenParser = new ChalkTokenParser(activeChalk, template.getTemplateData());

        Toast.makeText(this, "Processing chalk printing...", Toast.LENGTH_SHORT).show();
        TicketPrinter.print(this, tokenParser.parseTokens());
    }
}
