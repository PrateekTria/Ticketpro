package com.ticketpro.parking.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.webkit.WebView;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.zxing.client.android.Intents;
import com.ticketpro.exception.TPException;
import com.ticketpro.model.PrintTemplate;
import com.ticketpro.model.SpecialActivityHandler;
import com.ticketpro.model.SpecialActivityReport;
import com.ticketpro.model.User;
import com.ticketpro.parking.R;
import com.ticketpro.parking.bl.SpecialActivityBLProcessor;
import com.ticketpro.print.TicketPrinter;
import com.ticketpro.util.DateUtil;
import com.ticketpro.util.PrintTokenParserSpecialActivity;
import com.ticketpro.util.TPUtility;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;

public class PrintPreviewSpecialActivity extends BaseActivityImpl implements SpecialActivityHandler {
    Handler handler;
    private WebView webview;
    private ArrayList<SpecialActivityReport> report;
    private TextView calender, mDateLable;
    private LinearLayout mPickDate;
    private ProgressDialog pd;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView mOfficerName,mTotalMinute;
    private int userId;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_print_preview_special1);

            Intent intent = getIntent();
            userId = intent.getIntExtra("USER_ID",0);
            mTotalMinute = findViewById(R.id.txtTotal);
            mOfficerName = findViewById(R.id.txtOfficerName);
            mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
            mRecyclerView.setHasFixedSize(true);

            mLayoutManager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(mLayoutManager);

            report = SpecialActivityReport.getSpecialActivityReports(TPApp.custId, DateUtil.getCurrentDate());
            //webview = (WebView) findViewById(R.id.preview_webview11);
            calender = findViewById(R.id.txt_calender);
            mPickDate = (LinearLayout) findViewById(R.id.ly_one);
            mDateLable = (TextView) findViewById(R.id.button3);
            mDateLable.setText(DateUtil.getCurrentDateTimeAC());
            handler = new Handler() {
                /**
                 * Subclasses must implement this to receive messages.
                 *
                 * @param msg
                 */
                @Override
                public void handleMessage(@NonNull Message msg) {
                    super.handleMessage(msg);
                    if (pd.isShowing())
                        pd.dismiss();
                    _loadData();
                }
            };
            __setOfficeName();
            _loadData();
            mPickDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openCalenderDioalog();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.error(TPUtility.getPrintStackTrace(e));
        }

    }

    private void __setOfficeName() {
        try {
            User userInfo = User.getUserInfo(userId);
            if (userInfo != null) {
                mOfficerName.setText(userInfo.getFullName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private String getTotalH(ArrayList<SpecialActivityReport> printTickets) {
        if (printTickets.size()<0)
            return "";
        String total= "";
        int mTotal = 0;
        int mHours = 0;
        int m = 0;
        int th = 0;
        for (int i = 0; i <printTickets.size() ; i++) {
            final String s = printTickets.get(i).getDuration();
            if (s.length()>4){
                String h[] = s.split(" ");
                th = Integer.parseInt(h[0]);

                mHours = mHours+th;

                m = Integer.parseInt(h[2]);
                mTotal = mTotal+m;
            }else {
                String c[] = s.split(" ");

                m = Integer.parseInt(c[0]);
                mTotal = mTotal+m;
            }

        }
        //final int i = mTotal / 60;
        if (mHours > 0){
            return String.valueOf(mHours +"h "+mTotal+" m");
        }else {
            return String.valueOf(mTotal+" m");
        }
    }
    void openCalenderDioalog() {
        DatePickerDialog.OnDateSetListener myDateListener = new
                DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker arg0, int arg3, int arg1, int arg2) {
                        final String s = new StringBuilder(String.valueOf((arg1 + 1) < 10 ? "0" + (arg1 + 1) : (arg1 + 1))).append("/").append(arg2 < 10 ? "0" + arg2 : arg2).append("/").append(arg3 < 10 ? "0" + arg3 : arg3).toString();
                        mDateLable.setText(s);
                        try {
                            // mProgress.setVisibility(View.VISIBLE);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    _callService(s);
                                }
                            }, 500);

                        } catch (Exception e) {
                            e.printStackTrace();
                            //mProgress.setVisibility(View.GONE);
                        }
                    }
                };

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        int year = calendar.get(Calendar.YEAR);

        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(PrintPreviewSpecialActivity.this, myDateListener, year, month, day);
        dialog.getDatePicker().setMaxDate(System.currentTimeMillis());

        dialog.show();
    }

    void _loadData() {

        mAdapter = new PrinstPreviwSpecialAdapter(report, PrintPreviewSpecialActivity.this);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        mTotalMinute.setText(getTotalH(report));
    }

    private void _callService(final String s) {

        /*pd = ProgressDialog.show(PrintPreviewSpecialActivity.this, "", "Loading...", false, true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                SpecialActivityBLProcessor blProcessor = new SpecialActivityBLProcessor();
                try {
                    ArrayList<SpecialActivityReport> specialActivityList = blProcessor.getSpecialActivityList(TPApp.custId, DateUtil.getSpecialActivity(s));
                    report = specialActivityList;
                    handler.sendEmptyMessage(1);
                } catch (TPException e) {
                    e.printStackTrace();
                    if (pd.isShowing()) {
                        pd.dismiss();
                    }
                }
            }
        }).start();
*/
        // This code is changed for getActivityReport by mohit 27/3/2023

        SpecialActivityBLProcessor blProcessor = new SpecialActivityBLProcessor();
        pd = ProgressDialog.show(PrintPreviewSpecialActivity.this, "", "Loading...", false, true);
        try {
            blProcessor.getActivityReport1(TPApp.custId, DateUtil.getSpecialActivity(s), this);
        }
        catch (TPException e)
        {
            e.printStackTrace();
        }

        //End
    }

    private String getPreviewHTML() {
        String templateHTML = getPrintTemplate();
        templateHTML = TPUtility.parseSpecialActivity(templateHTML, report);
        templateHTML = templateHTML.replaceAll("\n", "");
        templateHTML = templateHTML.replaceAll("\r", "");
        templateHTML = templateHTML.replaceAll("\t", "");

        return templateHTML;
    }

    private String getPrintTemplate() {
        String html = "";
        try {
            PrintTemplate template = PrintTemplate.getPrintTemplateByName("PrintPreviewSpecialActivity");
            if (template != null) {
                String printTemplate = template.getTemplateData();
                return printTemplate;
            }

            InputStream is = getAssets().open("previewSpecialTemplate.html");
            int size = is.available();

            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            html = new String(buffer);
        } catch (Exception e) {
        }
        return html;
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }

    public void backAction(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }

    public void printAction(View view) {

        printTicket();
    }

    /* Print Preview and Ticket Printing */
    private void printTicket() {
        PrintTemplate template = TPUtility.getTicketPrintTemplateForActivity(getSharedPreferences(getPackageName(), MODE_PRIVATE));
        if (template != null) {
            printTickets(report, template.getTemplateData(), false, false);
        } else {
            Toast.makeText(this, "Print template not found", Toast.LENGTH_LONG).show();
        }

    }

    private void printTickets(ArrayList<SpecialActivityReport> printTicket, String printTemplateData, boolean printSpecialTemplate,
                              boolean isPreviousTicket) {

        PrintTokenParserSpecialActivity tokenParser = new PrintTokenParserSpecialActivity(printTicket, printTemplateData);
        tokenParser.setSpecialTemplate(printSpecialTemplate);

        final String s = tokenParser.parseTickets();

        Toast.makeText(this, "Processing ticket printing...", Toast.LENGTH_SHORT).show();
        TicketPrinter.print(this, s);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void bindDataAtLoadingTime() throws Exception {

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

    }

    @Override
    public void handleNetworkStatus(boolean connected, boolean isFastConnection) {

    }

    @Override
    public void specialActivityHandler(ArrayList<SpecialActivityReport> list) {

        if (pd.isShowing()) {
            pd.dismiss();
        }
        report = list;
        handler.sendEmptyMessage(1);
    }
}