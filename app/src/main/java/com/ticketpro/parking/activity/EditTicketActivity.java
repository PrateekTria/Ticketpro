package com.ticketpro.parking.activity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ticketpro.model.State;
import com.ticketpro.model.SyncData;
import com.ticketpro.model.Ticket;
import com.ticketpro.model.TicketPicture;
import com.ticketpro.parking.R;
import com.ticketpro.parking.bl.WriteTicketBLProcessor;
import com.ticketpro.util.DateUtil;
import com.ticketpro.util.TPConstant;
import com.ticketpro.util.TPUtility;

import java.util.Date;


/**
 * Home screen (Ref. Mockups).
 *
 * @author : TicketPRO
 * @version : 1.0
 * @since : 1.0
 */

public class EditTicketActivity extends BaseActivityImpl {

    private EditText stateEditText;
    private EditText expEditText;
    private EditText makeEditText;
    private EditText bodyEditText;
    private EditText colorEditText;
    private EditText plateNumberEditText;
    private EditText vinNumberEditText;
    private EditText tmEditText;
    private EditText locationEditText;
    private EditText permitEditText;
    private EditText meterNumberEditText;
    private CheckBox warnChk;
    private CheckBox driveAwayChk;
    private ProgressDialog progressDialog;
    private Handler dataLoadingHandler;
    private ImageView statusIndicatorImageView;

    private Ticket activeTicket;
    private Button violationBtn;
    private Button photosBtn;

    /**
     * Entry point of the Activity
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_ticket);
        setLogger(EditTicketActivity.class.getName());
        setBLProcessor(new WriteTicketBLProcessor((TPApplication) getApplicationContext()));
        isNetworkInfoRequired = true;

        stateEditText = (EditText) findViewById(R.id.write_ticket_st);
        expEditText = (EditText) findViewById(R.id.write_ticket_exp);
        makeEditText = (EditText) findViewById(R.id.write_ticket_make);
        bodyEditText = (EditText) findViewById(R.id.write_ticket_body);
        colorEditText = (EditText) findViewById(R.id.write_ticket_color);
        plateNumberEditText = (EditText) findViewById(R.id.write_ticket_plate_number);
        vinNumberEditText = (EditText) findViewById(R.id.write_ticket_vin_number);
        tmEditText = (EditText) findViewById(R.id.write_ticket_tm_number);
        locationEditText = (EditText) findViewById(R.id.write_ticket_location_number);
        permitEditText = (EditText) findViewById(R.id.write_ticket_permit_number);
        meterNumberEditText = (EditText) findViewById(R.id.write_ticket_meter_number);
        warnChk = (CheckBox) findViewById(R.id.write_ticket_warn_chk);
        driveAwayChk = (CheckBox) findViewById(R.id.write_ticket_driveaway_chk);
        violationBtn = (Button) findViewById(R.id.write_ticket_violation_btn);
        photosBtn = (Button) findViewById(R.id.write_ticket_photos_btn);

        stateEditText.setEnabled(false);
        expEditText.setEnabled(false);
        makeEditText.setEnabled(false);
        bodyEditText.setEnabled(false);
        colorEditText.setEnabled(false);
        plateNumberEditText.setEnabled(false);
        vinNumberEditText.setEnabled(false);
        tmEditText.setEnabled(false);
        locationEditText.setEnabled(false);
        permitEditText.setEnabled(false);
        meterNumberEditText.setEnabled(false);
        warnChk.setEnabled(false);
        driveAwayChk.setEnabled(false);
        violationBtn.setEnabled(false);

        dataLoadingHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();

                if (activeTicket == null)
                    return;

                TextView citationNumber = (TextView) findViewById(R.id.citation_number);
                citationNumber.setText("#" + TPUtility.prefixZeros(activeTicket.getCitationNumber(), 8));
                populateValues(activeTicket);
            }
        };

        statusIndicatorImageView = (ImageView) findViewById(R.id.status_indicator_image);
        if (isServiceAvailable) {
            if (isFastConnection)
                statusIndicatorImageView.setImageResource(R.drawable.green_status_btn_bk);
            else
                statusIndicatorImageView.setImageResource(R.drawable.yellow_status_btn);
        } else {
            statusIndicatorImageView.setImageResource(R.drawable.gray_status_btn);
        }

        bindDataAtLoadingTime();
    }


    private void populateValues(Ticket ticket) {

        vinNumberEditText.setText(ticket.getVin());
        plateNumberEditText.setText(ticket.getPlate());
        meterNumberEditText.setText(ticket.getMeter());
        locationEditText.setText(ticket.getLocation());
        makeEditText.setText(ticket.getMakeCode());
        colorEditText.setText(ticket.getColorCode());
        meterNumberEditText.setText(ticket.getMeter());
        bodyEditText.setText(ticket.getBodyCode());
        tmEditText.setText(DateUtil.getStringFromDate(ticket.getTicketDate()));
        expEditText.setText(ticket.getExpiration());
        permitEditText.setText(ticket.getPermit());
        stateEditText.setText(State.getStateCodeById(ticket.getStateId()));

        photosBtn.setText("Photos(" + activeTicket.getTicketPictures().size() + ")");
        violationBtn.setText("Violations(" + activeTicket.getTicketViolations().size() + ")");
    }

    public void bindDataAtLoadingTime() {
        progressDialog = ProgressDialog.show(this, "", "Loading...");
        new Thread() {
            public void run() {
                long citationNumber = getIntent().getLongExtra("CitationNumber", 0);
                if (citationNumber != 0) {
                    try {
                        activeTicket = Ticket.getTicketByCitationWithViolations(citationNumber);
                    } catch (Exception e) {
                    }
                }
                dataLoadingHandler.sendEmptyMessage(1);
            }
        }.start();
    }

    @Override
    public void onClick(View v) {

    }

    public void viewViolationAction(View view) {
		/*Intent i = new Intent();
		i.setClass(EditTicketScreen.this, ViolationsScreen.class);
		startActivityForResult(i, REQUEST_VIOLATIONS);
		return;
		*/
    }

    public void viewPhotosAction(View view) {
        Intent i = new Intent();
        i.setClass(EditTicketActivity.this, PhotosActivity.class);
        i.putExtra("SharedTicket", true);
        TPApp.setSharedTicket(activeTicket);
        startActivityForResult(i, 0);
        return;
    }

    public void saveAction(View view) {
        try {
            TicketPicture.removePictureByTicketId(activeTicket.getTicketId());

            //DatabaseHelper.getInstance().openWritableDatabase();
            SyncData syncData = new SyncData();
            for (TicketPicture picture : activeTicket.getTicketPictures()) {
                int picId = TicketPicture.getNextPrimaryId();
                picture.setPictureId(picId);
                //DatabaseHelper.getInstance().insertOrReplace(picture.getContentValues(),"ticket_pictures");
                TicketPicture.insertTicketPicture(picture);
                syncData.setActivity("INSERT");
                syncData.setPrimaryKey(picId + "");
                syncData.setActivityDate(new Date());
                syncData.setCustId(TPApp.custId);
                syncData.setTableName(TPConstant.TABLE_TICKET_PICTURES);
                syncData.setStatus("Pending");

                //DatabaseHelper.getInstance().insertOrReplace(syncData.getContentValues(), "sync_data");
                SyncData.insertSyncData(syncData).subscribe();
            }

            //DatabaseHelper.getInstance().closeWritableDb();
            Toast.makeText(this, "Ticket Updated Successfully.", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(this, "Failed to updated ticket details. Please try again.", Toast.LENGTH_SHORT).show();
        }

        setResult(RESULT_OK);
        finish();
    }

    public void backAction(View view) {
        finish();
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
        statusIndicatorImageView = (ImageView) findViewById(R.id.status_indicator_image);
        if (connected) {
            if (isFastConnection)
                statusIndicatorImageView.setImageResource(R.drawable.green_status_btn_bk);
            else
                statusIndicatorImageView.setImageResource(R.drawable.yellow_status_btn);
        } else {
            statusIndicatorImageView.setImageResource(R.drawable.gray_status_btn);
        }
    }

}
