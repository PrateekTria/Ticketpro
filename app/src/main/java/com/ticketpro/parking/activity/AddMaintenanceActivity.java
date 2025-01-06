package com.ticketpro.parking.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.ticketpro.model.Contact;
import com.ticketpro.model.Feature;
import com.ticketpro.model.GPSLocation;
import com.ticketpro.model.MaintenanceLog;
import com.ticketpro.model.MaintenancePicture;
import com.ticketpro.parking.R;
import com.ticketpro.parking.bl.WriteTicketBLProcessor;
import com.ticketpro.parking.service.GPSResultReceiver.Receiver;
import com.ticketpro.util.GPSTracker;
import com.ticketpro.util.StringUtil;
import com.ticketpro.util.TPConstant;
import com.ticketpro.util.TPUtility;
import com.ticketpro.util.UIHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;

/**
 * Home screen (Ref. Mockups).
 *
 * @author : TicketPRO
 * @version : 1.0
 * @since : 1.0
 */

public class AddMaintenanceActivity extends BaseActivityImpl {
    private EditText itemNameEditText;
    private EditText locationEditText;
    private EditText commentsEditText;
    private Spinner problemTypeSpinner;
    private Button photosBtn;
    private ArrayList<MaintenancePicture> pictures = new ArrayList<MaintenancePicture>();
    private MaintenanceLog maintenanceLog = new MaintenanceLog();
    private Handler GPSHandler;
    private GPSTracker gpsTracker;
    private ProgressBar GPSProgressBar;
    private Button gpsButton;
    private Dialog emailDialog;
    private ProgressDialog progressDialog;
    private EditText emailMessageText;
    private EditText emailMessageExtraText;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.add_maintenance);
            setLogger(AddMaintenanceActivity.class.getName());
            setBLProcessor(new WriteTicketBLProcessor(TPApp));
            setActiveScreen(this);

            isNetworkInfoRequired = true;
            maintenanceLog.setLogDate(new Date());
            maintenanceLog.setLogId(new Date().getTime());
            maintenanceLog.setCustId(TPApp.getCustId());
            maintenanceLog.setUserId(TPApp.getCurrentUserId());
            maintenanceLog.setDeviceId(TPApp.getDeviceId());

            gpsButton = (Button) findViewById(R.id.gps_btn);
            GPSProgressBar = (ProgressBar) findViewById(R.id.GPSProcess);
            locationEditText = (EditText) findViewById(R.id.location_text);
            itemNameEditText = (EditText) findViewById(R.id.item_name_text);
            commentsEditText = (EditText) findViewById(R.id.comments_text);
            problemTypeSpinner = (Spinner) findViewById(R.id.problem_type_spinner);
            photosBtn = (Button) findViewById(R.id.picture_btn);

            // Disable photos, GPS and LPR functionality on Emulators
            if (TPUtility.isRunningOnEmulator(getApplicationContext())) {
                gpsButton.setEnabled(false);
                gpsButton.setBackgroundResource(R.drawable.btn_disabled);
            } else {

                if (UIHelper.isGpsDeviceValue(TPApp.deviceId)){
                    UIHelper.toggleButtonState(gpsButton, false);
                }else {
                    if (!Feature.isFeatureAllowed(Feature.GPS)) {
                        gpsButton.setEnabled(false);
                        gpsButton.setBackgroundResource(R.drawable.btn_disabled);
                    }
                }
                // Update GPS button
                gpsTracker = new GPSTracker(this);
                if (!gpsTracker.isGPSAvailable()) {
                    gpsButton.setEnabled(false);
                    gpsButton.setBackgroundResource(R.drawable.btn_disabled);
                }

                if (TPApp.getUserSettings() != null && TPApp.getUserSettings().getGps() != null
                        && TPApp.getUserSettings().getGps().equals("N")) {
                    gpsButton.setEnabled(false);
                    gpsButton.setBackgroundResource(R.drawable.btn_disabled);
                }
            }

            locationEditText.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    LocationEntryAction(v);
                    return true;
                }
            });

            GPSHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if (maintenanceLog != null) {
                        locationEditText.setText(maintenanceLog.getLocation());
                    }

                    GPSProgressBar.setVisibility(View.GONE);
                }
            };

            bindDataAtLoadingTime();

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }


    public void bindDataAtLoadingTime() {
        String types = Feature.getFeatureValue(Feature.MAINTENANCE_TYPES);
        if (StringUtil.isEmpty(types)) {
            types = "UNKNOWN";
        }

        StringTokenizer token = new StringTokenizer(types, ",");
        ArrayList<String> problemTypes = new ArrayList<String>();

        while (token.hasMoreTokens()) {
            String type = token.nextToken();
            problemTypes.add(type);
        }

        String[] typesArray = (String[]) problemTypes.toArray(new String[0]);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.simple_spinner_item, typesArray);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        problemTypeSpinner.setAdapter(dataAdapter);
    }

    public void LocationEntryAction(View view) {
        Intent i = new Intent();
        i.setClass(this, LocationEntryActivity.class);
        i.putExtra("Location", maintenanceLog.getLocation());
        startActivityForResult(i, 3);
        return;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, android.content.Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                if (data.hasExtra("PicturePath")) {
                    MaintenancePicture picture = new MaintenancePicture();
                    picture.setMaintenanceId(maintenanceLog.getLogId());
                    picture.setImagePath(data.getStringExtra("PicturePath"));
                    picture.setImageSize(TPUtility.getImageSize(data.getStringExtra("PicturePath")));
                    picture.setImageResolution(data.getStringExtra("ImageResolution"));

                    pictures.add(picture);
                }

                photosBtn.setText("Photos (" + pictures.size() + ")");
            } else if (requestCode == 2) {
                locationEditText.setText(data.getStringExtra("Location"));
            } else if (requestCode == 3) {
                locationEditText.setText(data.getStringExtra("FULL_LOCATION"));
            }
        }
    }

    ;

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        backAction(null);
    }

    @Override
    public void handleVoiceInput(String text) {

    }

    @Override
    public void handleVoiceMode(boolean voiceMode) {
        // TODO Auto-generated method stub
    }

    @Override
    public void handleNetworkStatus(boolean connected, boolean isFastConnection) {
        if (gpsButton == null) {
            return;
        }

        if (connected) {
            if (UIHelper.isGpsDeviceValue(TPApp.deviceId)){
                UIHelper.toggleButtonState(gpsButton, false);
            }else {
                if (Feature.isFeatureAllowed(Feature.GPS)) {
                    gpsButton.setEnabled(true);
                    gpsButton.setBackgroundResource(R.drawable.btn_yellow);
                } else {
                    gpsButton.setEnabled(false);
                    gpsButton.setBackgroundResource(R.drawable.btn_disabled);
                }
            }

        } else {
            gpsButton.setEnabled(false);
            gpsButton.setBackgroundResource(R.drawable.btn_disabled);
        }

        try {
            if (TPUtility.isRunningOnEmulator(getApplicationContext())) {
                gpsButton.setEnabled(false);
                gpsButton.setBackgroundResource(R.drawable.btn_disabled);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void backAction(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }

    public void gpsAction(View view) {
        try {
            if (TPUtility.isRunningOnEmulator(getApplicationContext())
                    || gpsTracker == null
                    || !gpsTracker.isGPSAvailable()) {

                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        GPSProgressBar.setVisibility(View.VISIBLE);

        gpsTracker.initService(new Receiver() {
            @Override
            public void onReceiveResult(Location location, Bundle resultData) {
                if (location != null) {
                    maintenanceLog.setLatitude(location.getLatitude() + "");
                    maintenanceLog.setLongitude(location.getLongitude() + "");
                }

                GPSProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onReceiveResult(GPSLocation location, Bundle resultData) {
                if (location != null && (!location.getLocation().equals("") || !location.getStreetNumber().equals(""))) {
                    maintenanceLog.setLocation(location.getLocation().toUpperCase());
                    maintenanceLog.setLongitude(location.getLongitude());
                    maintenanceLog.setLatitude(location.getLatitude());
                }

                GPSHandler.sendEmptyMessage(0);
            }

            @Override
            public void onTimeout() {
                // TODO Auto-generated method stub

            }
        });
    }

    public void selectLocation(View view) {
        Intent intent = new Intent();
        intent.setClass(this, SearchLookupActivity.class);
        intent.putExtra("LIST_TYPE", TPConstant.SELECT_LOCATION_LIST);
        intent.putExtra("Location", maintenanceLog.getLocation());
        startActivityForResult(intent, 2);
    }

    public void pictureAction(View view) {
        Intent intent = new Intent();
        intent.setClass(this, TakePictureActivity.class);
        intent.putExtra("GenericPicture", true);
        intent.putExtra("ImageName", "MAINTENANCE-" + maintenanceLog.getLogId() + "-" + (pictures.size() + 1) + ".JPG");
        startActivityForResult(intent, 1);
    }

    public void saveAction(View view) {
        maintenanceLog.setItemName(itemNameEditText.getText().toString());
        maintenanceLog.setComments(commentsEditText.getText().toString());
        maintenanceLog.setProblemType((String) problemTypeSpinner.getSelectedItem());
        maintenanceLog.setLocation(locationEditText.getText().toString());
        maintenanceLog.setPictures(pictures);

        if (maintenanceLog.getItemName().isEmpty()) {
            displayErrorMessage("Maintenance Item is empty. Please enter details and try again");
            return;
        }

        if (maintenanceLog.getLocation().isEmpty()) {
            displayErrorMessage("Location is empty. Please select location and try again");
            return;
        }

        if (maintenanceLog.getComments().isEmpty()) {
            displayErrorMessage("Comments is empty. Please enter details and try again");
            return;
        }

        sendMaintenanceEmail();
    }

    private void saveAndFinish() {
        try {
            MaintenanceLog.insertMaintenanceLog(maintenanceLog);
			/*DatabaseHelper.getInstance().openWritableDatabase();
			DatabaseHelper.getInstance().insertOrReplace(maintenanceLog.getContentValues(), "maintenance_logs");*/

            for (MaintenancePicture picture : pictures) {
                MaintenancePicture.insertMaintenancePicture(picture);
                //DatabaseHelper.getInstance().insertOrReplace(picture.getContentValues(), "maintenance_pictures");
            }

            //DatabaseHelper.getInstance().closeWritableDb();
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }

        setResult(RESULT_OK);
        finish();
    }


    private void sendMaintenanceEmail() {
        try {
            emailDialog = new Dialog(this);
            LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            View inputDlgView = layoutInflater.inflate(R.layout.send_email_input_dialog, null, false);
            emailDialog.setTitle("Maintenance");
            emailDialog.setContentView(inputDlgView);
            emailDialog.show();

            Button sendBtn = (Button) inputDlgView.findViewById(R.id.send_email_input_dialog_enter_btn);
            emailMessageText = (EditText) inputDlgView.findViewById(R.id.send_email_input_dialog_text_field);
            emailMessageText.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return true;
                }
            });

            emailMessageExtraText = (EditText) inputDlgView.findViewById(R.id.send_email_extra_input_dialog_text_field);
            emailMessageExtraText.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    emailMessageExtraText.setText("");
                    return true;
                }
            });

            emailMessageExtraText.requestFocus();
            emailMessageText.setText(Html.fromHtml(TPUtility.getMaintenanceEmail(getApplicationContext(), maintenanceLog)));
            sendBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (emailDialog.isShowing()) {
                        emailDialog.dismiss();
                    }

                    try {
                        ArrayList<Contact> contacts = Contact.getTowNotifyContacts();
                        if (contacts.size() == 0) {
                            Toast.makeText(AddMaintenanceActivity.this, "Contact information not available. Please sync the device", Toast.LENGTH_LONG).show();
                            return;
                        }

                        String msg = Html.toHtml(emailMessageText.getText());
                        msg += "<br/>" + Html.toHtml(emailMessageExtraText.getText());

                        String[] emails = new String[contacts.size()];
                        for (int i = 0; i < contacts.size(); i++) {
                            emails[i] = contacts.get(i).getEmailId();
                        }

                        sendEmail(TPApp.getUserInfo().getEmailAddress(), emails, "Maintenance", msg);

                    } catch (Exception e) {
                        log.error(TPUtility.getPrintStackTrace(e));
                    }

                    saveAndFinish();
                }
            });

            Button cancelBtn = (Button) inputDlgView.findViewById(R.id.send_email_input_dialog_cancel_btn);
            cancelBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (emailDialog.isShowing()) {
                        emailDialog.dismiss();
                    }
                }
            });

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }


    private void sendEmail(final String from, final String[] to, final String subject, final String message) {
        try {
            progressDialog = ProgressDialog.show(this, "", "Sending Email...");
            Handler saveHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if (isFastConnection) {
                        try {
                            boolean result = ((WriteTicketBLProcessor) screenBLProcessor).sendEmail(from, to, subject, message);
                            if (!result) {
                                Toast.makeText(AddMaintenanceActivity.this, "Failed sending email, please try again", Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            log.error(TPUtility.getPrintStackTrace(e));
                        }
                    } else {
                        Toast.makeText(AddMaintenanceActivity.this, "Network not available, please try again", Toast.LENGTH_LONG).show();
                    }

                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
            };

            saveHandler.sendEmptyMessage(1);

        } catch (Exception e) {
            Toast.makeText(this, "Failed sending email, please try again", Toast.LENGTH_LONG).show();
        }
    }
}
