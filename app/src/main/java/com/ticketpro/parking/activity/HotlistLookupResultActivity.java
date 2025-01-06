package com.ticketpro.parking.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ticketpro.model.CallInList;
import com.ticketpro.model.CallInReport;
import com.ticketpro.model.Hotlist;
import com.ticketpro.model.SyncData;
import com.ticketpro.parking.R;
import com.ticketpro.util.DateUtil;
import com.ticketpro.util.StringUtil;
import com.ticketpro.util.TPConstant;
import com.ticketpro.util.TPUtility;

import java.util.ArrayList;
import java.util.Date;

/**
 * Home screen (Ref. Mockups).
 *
 * @author : TicketPRO
 * @version : 1.0
 * @since : 1.0
 */

public class HotlistLookupResultActivity extends BaseActivityImpl {

    /**
     * Entry point of the Activity
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.hotlist_view);
        setLogger(HotlistLookupResultActivity.class.getName());
        setActiveScreen(this);

        if (TPApp.getActiveHotlist() == null) {
            finish();
            return;
        }

        Hotlist hotlist = TPApp.getActiveHotlist();
        TextView plateText = (TextView) findViewById(R.id.hotlist_lookup_plate_textview);
        TextView plateTypeText = (TextView) findViewById(R.id.hotlist_lookup_platetype_textview);
        TextView beginDateText = (TextView) findViewById(R.id.hotlist_lookup_begindate_textview);
        TextView endDateText = (TextView) findViewById(R.id.hotlist_lookup_enddate_textview);
        TextView numberOfTicketText = (TextView) findViewById(R.id.hotlist_lookup_number_of_tickets_textview);
        TextView amountOwnedText = (TextView) findViewById(R.id.hotlist_lookup_amount_owned_textview);
        TextView commentsText = (TextView) findViewById(R.id.hotlist_lookup_comments_textview);

        LinearLayout beginDateLayout = (LinearLayout) findViewById(R.id.hotlist_lookup_begin_date);
        LinearLayout endDateLayout = (LinearLayout) findViewById(R.id.hotlist_lookup_end_date);
        LinearLayout numberTicketsLayout = (LinearLayout) findViewById(R.id.hotlist_lookup_number_of_tickets);
        LinearLayout amountLayout = (LinearLayout) findViewById(R.id.hotlist_lookup_amount_owned);

        if (hotlist.getPlateType() != null && hotlist.getPlateType().equals("SCOFFLAWS")) {
            beginDateLayout.setVisibility(View.GONE);
            endDateLayout.setVisibility(View.GONE);
        } else {
            numberTicketsLayout.setVisibility(View.GONE);
            amountLayout.setVisibility(View.GONE);
        }

        plateText.setText(StringUtil.getDisplayString(hotlist.getPlate()));
        plateTypeText.setText(StringUtil.getDisplayString(hotlist.getPlateType()));
        beginDateText.setText(StringUtil.getDisplayString(DateUtil.getStringFromDate(hotlist.getBeginDate())));
        endDateText.setText(StringUtil.getDisplayString(DateUtil.getStringFromDate(hotlist.getEndDate())));
        numberOfTicketText.setText(StringUtil.getDisplayString(hotlist.getNumberOfTickets() + ""));
        amountOwnedText.setText(StringUtil.getDisplayString("$" + hotlist.getAmountOwed()));
        commentsText.setText(StringUtil.getDisplayString(hotlist.getComments()));
    }

    @Override
    public void bindDataAtLoadingTime() {
        // TODO
    }

    @Override
    public void onClick(View v) {

    }

    public void backAction(View view) {
        finish();
        return;
    }


    public void actionAction(View view) {
        if (TPApp.getActiveHotlist() == null) {
            finish();
            return;
        }

        CharSequence[] choiceList;
        final ArrayList<CallInList> callInList = CallInList.getCallInList();
        if (callInList != null && callInList.size() > 0) {
            choiceList = new CharSequence[callInList.size()];
            int index = 0;
            for (CallInList callIn : callInList) {
                choiceList[index++] = callIn.getActionItem();
            }
        } else {
            choiceList = new CharSequence[1];
            choiceList[0] = "NA";
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select CallIn Action");
        builder.setItems(choiceList, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (callInList == null) {
                    finish();
                    return;
                }

                CallInList callIn = callInList.get(which);
                CallInReport report = new CallInReport();
                report.setActionTaken(callIn.getActionItem());
                report.setCallInDate(new Date());
                report.setFromSearch(TPApp.getActiveHotlist().getPlateType());
                report.setFromHit("T");
                report.setPlate(TPApp.getActiveHotlist().getPlate());
                report.setState(TPApp.getActiveHotlist().getStateCode());
                report.setUserId(TPApp.userId);
                report.setCustid(TPApp.custId);

                try {
                    //DatabaseHelper.getInstance().openWritableDatabase();
                    //DatabaseHelper.getInstance().insertOrReplace(report.getContentValues(), "call_in_reports");
                    CallInReport.insertCallInReport(report);
                    SyncData syncData = new SyncData();
                    syncData.setActivity("INSERT");
                    syncData.setPrimaryKey(CallInReport.getLastInsertId() + "");
                    syncData.setActivityDate(new Date());
                    syncData.setCustId(TPApp.custId);
                    syncData.setTableName(TPConstant.TABLE_CALL_IN_REPORTS);
                    syncData.setStatus("Pending");

                    SyncData.insertSyncData(syncData).subscribe();
	    			/*DatabaseHelper.getInstance().insertOrReplace(syncData.getContentValues(), "sync_data");
	    			DatabaseHelper.getInstance().closeWritableDb();*/
                    Toast.makeText(HotlistLookupResultActivity.this, "Updated Call in report successfully.", Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    log.error(TPUtility.getPrintStackTrace(e));
                }

                finish();
            }
        })
                .setCancelable(true)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

        return;
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
