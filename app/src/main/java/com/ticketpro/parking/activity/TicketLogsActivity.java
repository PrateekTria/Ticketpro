package com.ticketpro.parking.activity;

import java.util.ArrayList;
import java.util.Collections;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ticketpro.model.TicketPicture;
import com.ticketpro.parking.R;
import com.ticketpro.exception.TPException;
import com.ticketpro.model.Ticket;
import com.ticketpro.parking.bl.CommonBLProcessor;
import com.ticketpro.util.DateUtil;
import com.ticketpro.util.TPUtility;

/**
 * Home screen (Ref. Mockups).
 *
 * @author : TicketPRO
 * @version : 1.0
 * @since : 1.0
 */

public class TicketLogsActivity extends BaseActivityImpl {

    private TableLayout tableLayout;
    private ArrayList<Ticket> tickets;
    private ProgressDialog progressDialog;
    private Handler dataLoadingHandler;
    private Handler errorHandler;
    private TextView voidedTextView;

    private final int ASC_ORDER = 1;
    private final int DESC_ORDER = 2;
    private int sortBy = 0;
    private int sortOrder = 0;

    /**
     * Entry point of the Activity
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.ticket_logs);
            setActiveScreen(this);

            setLogger(TicketLogsActivity.class.getName());
            setBLProcessor(new CommonBLProcessor((TPApplication) getApplicationContext()));
            tableLayout = (TableLayout) findViewById(R.id.logs_3_table_view);
            voidedTextView = (TextView) findViewById(R.id.ticket_logs_voided_textview);

            dataLoadingHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    initDatagrid();

                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
            };

            errorHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);

                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
            };

            bindDataAtLoadingTime();

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }

    private void initDatagrid() {
        try {
            if (tickets == null) {
                return;
            }

            tableLayout.removeAllViews();
            LayoutInflater inflater = LayoutInflater.from(this);

            // adding Header
            View headerRow = inflater.inflate(R.layout.table_row_ticketlog, null);

            TextView citationColumn = ((TextView) headerRow.findViewById(R.id.tr_header1));
            citationColumn.setText("Citation#");
            citationColumn.setClickable(true);
            citationColumn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Collections.sort(tickets, new Ticket.PlateComparator());

                    // Update Sorting Order
                    if (sortBy != 1) {
                        sortOrder = ASC_ORDER;
                    } else if (sortOrder == ASC_ORDER) {
                        sortOrder = DESC_ORDER;
                        Collections.reverse(tickets);
                    } else {
                        sortOrder = ASC_ORDER;
                    }

                    sortBy = 1;
                    initDatagrid();
                }
            });

            TextView timeColumn = ((TextView) headerRow.findViewById(R.id.tr_header2));
            timeColumn.setText("Time");
            timeColumn.setClickable(true);
            timeColumn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Collections.sort(tickets, new Ticket.DateComparator());


                    // Update Sorting Order
                    if (sortBy != 2) {
                        sortOrder = ASC_ORDER;
                    } else if (sortOrder == ASC_ORDER) {
                        sortOrder = DESC_ORDER;
                        Collections.reverse(tickets);
                    } else {
                        sortOrder = ASC_ORDER;
                    }

                    sortBy = 2;
                    initDatagrid();
                }
            });

            TextView plateColumn = ((TextView) headerRow.findViewById(R.id.tr_header3));
            plateColumn.setText("Plate#");
            plateColumn.setClickable(true);
            plateColumn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Collections.sort(tickets, new Ticket.PlateComparator());

                    // Update Sorting Order
                    if (sortBy != 3) {
                        sortOrder = ASC_ORDER;
                    } else if (sortOrder == ASC_ORDER) {
                        sortOrder = DESC_ORDER;
                        Collections.reverse(tickets);
                    } else {
                        sortOrder = ASC_ORDER;
                    }

                    sortBy = 3;
                    initDatagrid();
                }
            });

           /* TextView vinColumn = ((TextView) headerRow.findViewById(R.id.tr_header4));
            vinColumn.setText("Vin#");
            vinColumn.setClickable(true);
            vinColumn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Collections.sort(tickets, new Ticket.VinComparator());

                    // Update Sorting Order
                    if (sortBy != 4) {
                        sortOrder = ASC_ORDER;
                    } else if (sortOrder == ASC_ORDER) {
                        sortOrder = DESC_ORDER;
                        Collections.reverse(tickets);
                    } else {
                        sortOrder = ASC_ORDER;
                    }

                    sortBy = 4;
                    initDatagrid();
                }
            });*/
            switch (sortBy) {
                case 1:
                    if (sortOrder == ASC_ORDER)
                        citationColumn.setText("Citation# \u25BC");
                    else
                        citationColumn.setText("Citation# \u25B2");
                    break;
                case 2:
                    if (sortOrder == ASC_ORDER)
                        timeColumn.setText("Time \u25BC");
                    else
                        timeColumn.setText("Time \u25B2");
                    break;
                case 3:
                    if (sortOrder == ASC_ORDER)
                        plateColumn.setText("Plate# \u25BC");
                    else
                        plateColumn.setText("Plate# \u25B2");
                    break;

               /* case 4:
                    if (sortOrder == ASC_ORDER)
                        vinColumn.setText("Vin# \u25BC");
                    else
                        vinColumn.setText("Vin# \u25B2");
                    break;*/
            }

            tableLayout.addView(headerRow);

            int i = 0, voided = 0, warning = 0;
            for (Ticket ticket : tickets) {

                String value = "";

                if (ticket.isWarn()) {
                    value = "W";
                }

                if (ticket.isWarn() && ticket.isDriveAway()) {
                    value = "W";
                }

                if (ticket.isDriveAway() && !(ticket.isWarn())) {
                    value = "D";
                }

                if (ticket.isVoided()) {
                    value = "V";
                }

                if (ticket.isVoided() && ticket.isDriveAway()) {
                    value = "V";
                }

                if (ticket.isDriveAway() && !(ticket.isVoided()) && !(ticket.isWarn())) {
                    value = "D";
                }

                View tableRow = inflater.inflate(R.layout.table_column_ticketlog, null);

                ((TextView) tableRow.findViewById(R.id.tr_header1))
                        .setText(TPUtility.prefixZeros(ticket.getCitationNumber(), 8) + " " + value);
                ((TextView) tableRow.findViewById(R.id.tr_header2))
                        .setText(DateUtil.getStringFromDateShortYear(ticket.getTicketDate()));

                final String plate = ticket.getPlate();
                final TextView plateVin = (TextView) tableRow.findViewById(R.id.tr_header3);
                if (plate!=null && !TextUtils.isEmpty(plate)){
                    plateVin.setText(plate);
                }else {
                    plateVin.setText(ticket.getVin());
                }
                //((TextView) tableRow.findViewById(R.id.tr_header4)).setText(ticket.getVin());

                if ((i % 2) == 0) {
                    tableRow.setBackgroundResource(R.drawable.tablerow_even);
                } else {
                    tableRow.setBackgroundResource(R.drawable.tablerow_odd);
                }

                tableRow.setTag(R.id.CitationNumber, ticket.getCitationNumber());
                tableRow.setTag(R.id.TicketIndex, i);
                tableRow.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        long citationNumber = (Long) v.getTag(R.id.CitationNumber);
                        int ticketIndex = (Integer) v.getTag(R.id.TicketIndex);

                        Intent i = new Intent();
                        i.setClass(TicketLogsActivity.this, TicketViewActivity.class);
                        i.putExtra("CitationNumber", citationNumber);
                        i.putExtra("TicketIndex", ticketIndex);
                        startActivityForResult(i, 0);
                        return;
                    }
                });

                if (ticket.isDriveAway()) {
                    tableRow.setBackgroundResource(R.drawable.color_orange);
                }

                if (ticket.isVoided()) {
                    voided++;

                    tableRow.setBackgroundResource(R.drawable.tablerow_expired);
                }

                if (ticket.isWarn()) {
                    warning++;

                    ((TextView) tableRow.findViewById(R.id.tr_header1)).setTextColor(Color.BLACK);
                    ((TextView) tableRow.findViewById(R.id.tr_header2)).setTextColor(Color.BLACK);
                    ((TextView) tableRow.findViewById(R.id.tr_header3)).setTextColor(Color.BLACK);

                    tableRow.setBackgroundResource(R.drawable.tablerow_warned);
                }

                tableLayout.addView(tableRow);
                i++;
            }

            voidedTextView.setText("T=" + tickets.size() + " V=" + voided + " W=" + warning);

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            try {
                tickets = ((CommonBLProcessor) screenBLProcessor).getTickets();
                dataLoadingHandler.sendEmptyMessage(1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void bindDataAtLoadingTime() {
        try {
            progressDialog = ProgressDialog.show(this, "", "Loading...");
            new Thread() {
                public void run() {
                    try {
                        tickets = ((CommonBLProcessor) screenBLProcessor).getTickets();
                        dataLoadingHandler.sendEmptyMessage(1);
                    } catch (TPException ae) {
                        log.error(ae.getMessage());
                        errorHandler.sendEmptyMessage(0);
                    }
                }
            }.start();

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onBackPressed() {
        backAction(null);
    }

    public void backAction(View view) {
        finish();
    }

    public void backAction1(View view) {
        __showPendingTickets();
    }

    private void __showPendingTickets() {
        boolean b = Ticket.checkOlderTickets(TPApplication.getInstance().userId);
        AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(TicketLogsActivity.this);
        confirmBuilder.setCancelable(false);
        if (b){
            confirmBuilder.setTitle("Alert")
                    .setMessage(R.string.clear_ticket_msg).setCancelable(true)
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).setPositiveButton("Continue", (dialog, which) -> {
                        Ticket.removeOlderTickets(TPApplication.getInstance().userId);
                        try {
                            Thread.sleep(2000);
                            ArrayList<TicketPicture> lastPhotos = TPApp.getLastPhotos();
                            if (lastPhotos.size()>0) {
                                lastPhotos.clear();
                            }
                            bindDataAtLoadingTime();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    });

            AlertDialog confirmAlert = confirmBuilder.create();
            confirmAlert.show();
        }else {
            confirmBuilder.setTitle("Alert").setMessage(R.string.no_tickets_found).setCancelable(true)
                    .setNegativeButton("Ok", (dialog, which) -> dialog.dismiss());


            AlertDialog confirmAlert = confirmBuilder.create();
            confirmAlert.show();
        }
    }

    @Override
    public void handleVoiceInput(String text) {
        if (text == null)
            return;

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
