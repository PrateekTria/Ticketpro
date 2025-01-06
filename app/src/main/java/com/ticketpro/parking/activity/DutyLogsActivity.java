package com.ticketpro.parking.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ticketpro.exception.TPException;
import com.ticketpro.model.DutyReport;
import com.ticketpro.parking.R;
import com.ticketpro.parking.bl.CommonBLProcessor;
import com.ticketpro.util.DateUtil;
import com.ticketpro.util.TPConstant;
import com.ticketpro.util.TPUtility;

import java.util.ArrayList;

/**
 * Home screen (Ref. Mockups).
 *
 * @author : TicketPRO
 * @version : 1.0
 * @since : 1.0
 */

public class DutyLogsActivity extends BaseActivityImpl {

    private TableLayout tableLayout;
    private ArrayList<DutyReport> dutyReports;
    private ProgressDialog progressDialog;
    private Handler dataLoadingHandler;
    private Handler errorHandler;

    /**
     * Entry point of the Activity
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.duty_logs);

            setLogger(DutyLogsActivity.class.getName());
            setBLProcessor(new CommonBLProcessor((TPApplication) getApplicationContext()));
            setActiveScreen(this);

            tableLayout = (TableLayout) findViewById(R.id.logs_3_table_view);

            dataLoadingHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    initDatagrid();
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
                }
            };

            bindDataAtLoadingTime();
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            displayErrorMessage(TPConstant.GENERIC_ERROR_MESSAGE);
        }
    }

    private void initDatagrid() {
        try {
            if (dutyReports == null)
                return;

            LayoutInflater inflater = LayoutInflater.from(this);

            int i = 0;
            for (DutyReport report : dutyReports) {

                View tableRow = inflater.inflate(R.layout.table_row_3_coulm_view, null);

                ((TextView) tableRow.findViewById(R.id.tr_header1)).setText(report.getDutyTitle());
                ((TextView) tableRow.findViewById(R.id.tr_header2)).setText(DateUtil.getSQLStringFromDate(report.getDateIn()));
                ((TextView) tableRow.findViewById(R.id.tr_header3)).setText(DateUtil.getSQLStringFromDate(report.getDateOut()));

                if ((i % 2) == 0)
                    tableRow.setBackgroundResource(R.drawable.tablerow_even);
                else
                    tableRow.setBackgroundResource(R.drawable.tablerow_odd);

                tableLayout.addView(tableRow);
                i++;
            }

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }

    public void bindDataAtLoadingTime() {
        try {
            progressDialog = ProgressDialog.show(this, "", "Loading...");
            new Thread() {
                public void run() {
                    try {
                        dutyReports = DutyReport.getDutyReports(TPApp.userId);
                        dataLoadingHandler.sendEmptyMessage(1);
                    } catch (TPException ae) {
                        log.error(ae.getMessage());
                        errorHandler.sendEmptyMessage(0);
                    }
                }
            }.start();

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            displayErrorMessage(TPConstant.GENERIC_ERROR_MESSAGE);
        }
    }

    @Override
    public void onClick(View v) {

    }

    public void backAction(View view) {
        finish();
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
        // TODO Auto-generated method stub

    }

}
