package com.ticketpro.parking.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.ticketpro.exception.TPException;
import com.ticketpro.model.Contact;
import com.ticketpro.model.Feature;
import com.ticketpro.model.HotListHandler;
import com.ticketpro.model.Hotlist;
import com.ticketpro.model.PrintTemplate;
import com.ticketpro.model.RepeatOffender;
import com.ticketpro.model.SyncData;
import com.ticketpro.model.Ticket;
import com.ticketpro.model.TicketComment;
import com.ticketpro.model.TicketViolation;
import com.ticketpro.model.VoidTicketReason;
import com.ticketpro.parking.R;
import com.ticketpro.parking.bl.CommonBLProcessor;
import com.ticketpro.parking.bl.WriteTicketBLProcessor;
import com.ticketpro.print.TicketPrinter;
import com.ticketpro.util.CustomWebView;
import com.ticketpro.util.DateUtil;
import com.ticketpro.util.PrintTokenParser;
import com.ticketpro.util.TPConstant;
import com.ticketpro.util.TPUtility;

import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import io.reactivex.Completable;

/**
 * Home screen (Ref. Mockups).
 *
 * @author : TicketPRO
 * @version : 1.0
 * @since : 1.0
 */

public class TicketViewActivity extends BaseActivityImpl implements HotListHandler {
    private Ticket activeTicket;
    private CustomWebView webview;
    private boolean voidListFlag;
    private ArrayList<Ticket> tickets;
    private ProgressDialog progressDialog;
    private Handler dataLoadingHandler;
    private Handler errorHandler;
    private TextView ticketCounter;
    private int ticketIndex;
    private Dialog emailDialog;
    private EditText emailMessageText;
    private EditText emailMessageExtraText;
    private boolean isTicketHistory;
    private Button actionButton;
    private Dialog otherReasonDialog;
    private EditText voidReasonText;
    private View inputDlgView;
    private EditText beginDate;
    private EditText endDate;

    /**
     * Entry point of the Activity
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);

            setContentView(R.layout.ticket_view);
            setLogger(TicketViewActivity.class.getName());
            setBLProcessor(new CommonBLProcessor((TPApplication) getApplicationContext()));
            setActiveScreen(this);

            isNetworkInfoRequired = true;
            actionButton = findViewById(R.id.actionButton);

            Intent data = getIntent();
            ticketIndex = data.getIntExtra("TicketIndex", 0);

            if (data.hasExtra("CitationNumber")) {
                long citationNumber = data.getLongExtra("CitationNumber", 0);
                activeTicket = Ticket.getTicketByCitationWithViolations(citationNumber);
                isTicketHistory = true;
            }

            if (data.hasExtra("activeTicket")) {
                activeTicket = (Ticket) data.getSerializableExtra("activeTicket");
                isTicketHistory = true;
                actionButton.setVisibility(View.GONE);
                addTicketViolation(activeTicket);
            }

            if (activeTicket == null) {
                TPUtility.showErrorToast(this, "Error loading ticket details. Please try again.");

                finish();
                return;
            }

            ticketCounter = findViewById(R.id.ticket_view_counter);
            dataLoadingHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    try {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        if (tickets!=null && tickets.size()>0) {
                           ticketCounter.setText((ticketIndex + 1) + "/" + tickets.size());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
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

            webview = findViewById(R.id.preview_webview);
            /*if (0 != (getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE)) {
                WebView.setWebContentsDebuggingEnabled(true);
            }*/

            WebSettings webSettings = webview.getSettings();
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            webSettings.setJavaScriptEnabled(true);
            webSettings.setAllowContentAccess(true);
            webSettings.setAllowFileAccess(true);
            webSettings.setAllowFileAccessFromFileURLs(true);
            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            webSettings.setDomStorageEnabled(true);
            webSettings.setUseWideViewPort(true);
            webSettings.setLoadWithOverviewMode(true);
            webview.setVerticalScrollBarEnabled(true);
            webview.setGestureDetector( new GestureDetector(TicketViewActivity.this, new CustomeGestureDetector()));
            webview.loadUrl("file:///android_asset/printPreview.html");
            webview.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    String previewHTML = getPreviewHTML();
                    view.loadUrl("javascript:loadHTML(\'" + previewHTML + "\','left')");
                }

                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                    super.onReceivedError(view, request, error);
                    log.error("View error"+error.getDescription());
                }

                @Override
                public void onUnhandledKeyEvent(WebView view, KeyEvent event) {
                    super.onUnhandledKeyEvent(view, event);
                }
            });

            bindDataAtLoadingTime();

        } catch (Exception e) {
            log.error("View error1"+TPUtility.getPrintStackTrace(e));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //TPUtility.hideSoftKeyboard(TicketViewActivity.this);

        closeKeyboard();

    }

    public void bindDataAtLoadingTime() {
        progressDialog = ProgressDialog.show(this, "", "Loading...");
        new Thread() {
            public void run() {
                try {
                    tickets = ((CommonBLProcessor) screenBLProcessor).getTickets();
                    dataLoadingHandler.sendEmptyMessage(1);
                } catch (TPException ae) {
                    errorHandler.sendEmptyMessage(0);
                }
            }
        }.start();
    }

    @Override
    public void onClick(View v) {

    }

    public void onLeftSwipe() {
        if (ticketIndex < tickets.size()) {
            ticketIndex++;

            if (tickets.get(ticketIndex) != null) {
                loadTicketView(tickets.get(ticketIndex).getCitationNumber(), "right");
            }
        }
    }

    public void onRightSwipe() {
        if (ticketIndex > 0) {
            ticketIndex--;

            if (tickets.get(ticketIndex) != null) {
                loadTicketView(tickets.get(ticketIndex).getCitationNumber(), "left");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            //loadTicketView(activeTicket.getCitationNumber(), "left");

            webview = findViewById(R.id.preview_webview);
            /*if (0 != (getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE)) {
                WebView.setWebContentsDebuggingEnabled(true);
            }*/

            WebSettings webSettings = webview.getSettings();
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            webSettings.setJavaScriptEnabled(true);
            webSettings.setAllowContentAccess(true);
            webSettings.setAllowFileAccess(true);
            webSettings.setAllowFileAccessFromFileURLs(true);
            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            webSettings.setDomStorageEnabled(true);
            webSettings.setUseWideViewPort(true);
            webSettings.setLoadWithOverviewMode(true);
            webview.setVerticalScrollBarEnabled(true);
            webview.clearCache(true);
            webview.setGestureDetector( new GestureDetector(TicketViewActivity.this, new CustomeGestureDetector()));
            webview.loadUrl("file:///android_asset/printPreview.html");
            webview.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    String previewHTML = getPreviewHTML();
                    view.loadUrl("javascript:loadHTML(\'" + previewHTML + "\','left')");
                }

                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                    super.onReceivedError(view, request, error);
                    log.error(error.getDescription());
                }

                @Override
                public void onUnhandledKeyEvent(WebView view, KeyEvent event) {
                    super.onUnhandledKeyEvent(view, event);
                }
            });


        }
    }

    private void loadTicketView(long citationNumber, String direction) {
        try {
            activeTicket = Ticket.getTicketByCitationWithViolations(citationNumber);
            String previewHTML = getPreviewHTML();
            webview.loadUrl("javascript:loadHTML('" + previewHTML + "','" + direction + "')");
            ticketCounter.setText((ticketIndex + 1) + "/" + tickets.size());

        } catch (TPException e) {
            Toast.makeText(this, "Failed loading ticket details", Toast.LENGTH_LONG).show();
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        finish();
    }

    public void backAction(View view) {
        setResult(RESULT_OK);
        finish();
    }

    public void otherActions(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Action");

        final CharSequence[] choiceList = {"Void Ticket", // 0
                "Edit Photos", // 1
                "Add a Comment", // 2
                "Send Support Email", // 3
                "Make a Warning", // 4
                "Add Hotlist" //5
        };

        builder.setItems(choiceList, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    if (which == 0) {
                        if(!isServiceAvailable)
                        {
                            if(!checkNetworkAndAction(activeTicket))
                            {
                                checckNetworkAndActionAlert("Voiding");
                            }
                            else{
                                 if (Feature.isSystemFeatureAllowed(Feature.UPDATE_CUTOFF_PERIOD)) {
                                    String cutOffTime = Feature.getFeatureValue(Feature.UPDATE_CUTOFF_PERIOD);
                                    if (cutOffTime != null && !cutOffTime.equals("") && activeTicket.getTicketDate() != null) {
                                        try {
                                            int cutOffMins = Integer.parseInt(cutOffTime);
                                            long milliseconds = (new Date().getTime() - activeTicket.getTicketDate().getTime());
                                            int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
                                            if (minutes > cutOffMins) {
                                                displayErrorMessage("No update allowed. Cutoff time exceeded.");
                                                return;
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                                voidTicket();
                            }
                        }
                       else {
                            if (Feature.isSystemFeatureAllowed(Feature.UPDATE_CUTOFF_PERIOD)) {
                                String cutOffTime = Feature.getFeatureValue(Feature.UPDATE_CUTOFF_PERIOD);
                                if (cutOffTime != null && !cutOffTime.equals("") && activeTicket.getTicketDate() != null) {
                                    try {
                                        int cutOffMins = Integer.parseInt(cutOffTime);
                                        long milliseconds = (new Date().getTime() - activeTicket.getTicketDate().getTime());
                                        int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
                                        if (minutes > cutOffMins) {
                                            displayErrorMessage("No update allowed. Cutoff time exceeded.");
                                            return;
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            voidTicket();
                        }
                    } else if (which == 1) {
                        if (!Feature.isFeatureAllowed(Feature.EDIT_TICKET_PICTURES)) {
                            Toast.makeText(TicketViewActivity.this, "This feature is disabled.", Toast.LENGTH_LONG).show();
                            return;
                        }

                        Intent i = new Intent();
                        i.setClass(TicketViewActivity.this, PhotosActivity.class);
                        i.putExtra("SharedTicket", true);
                        i.putExtra("EditTicketPictures", true);

                        TPApp.setSharedTicket(activeTicket);

                        startActivityForResult(i, 0);
                        return;
                    } else if (which == 2) {
                        if (!Feature.isFeatureAllowed(Feature.EDIT_TICKET_COMMENTS)) {
                            Toast.makeText(TicketViewActivity.this, "This feature is disabled.", Toast.LENGTH_LONG).show();
                            return;
                        }

                        TPApp.setSharedTicket(activeTicket);

                        Intent i = new Intent();
                        i.setClass(TicketViewActivity.this, ViolationsActivity.class);
                        i.putExtra("EditCommentsOnly", true);
                        startActivityForResult(i, 0);
                        return;
                    } else if (which == 3) {
                        sendSupportEmail();

                    } else if (which == 4) {
                        if(!isServiceAvailable)
                        {

                                if(!checkNetworkAndAction(activeTicket))
                                {
                                    checckNetworkAndActionAlert("Warning");
                                }
                                else{
                                    AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(TicketViewActivity.this);
                                    confirmBuilder.setTitle("Warn Ticket")
                                            .setMessage("Are you sure you want to make this ticket a warning?").setCancelable(true)
                                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                }
                                            }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                    warnTicket();
                                                }
                                            });

                                    AlertDialog confirmAlert = confirmBuilder.create();
                                    confirmAlert.show();
                                }

                        }
                        else {
                            AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(TicketViewActivity.this);
                            confirmBuilder.setTitle("Warn Ticket")
                                    .setMessage("Are you sure you want to make this ticket a warning?").setCancelable(true)
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            warnTicket();
                                        }
                                    });

                            AlertDialog confirmAlert = confirmBuilder.create();
                            confirmAlert.show();
                        }
                    } else if (which == 5) {
                        hotListAction();

                    }


            }
        }).setCancelable(true).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    // this code is added by mohit 17 jan 2023
    private boolean checkNetworkAndAction(Ticket lastTicket)
    {
        boolean isVoid = true;
        if(lastTicket.getStatus().equals("P"))
        {
            isVoid = true;
        }
        else {
            isVoid = false;
        }
        return isVoid;
    }

    private void checckNetworkAndActionAlert(String type)
    {
        AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(TicketViewActivity.this);
        confirmBuilder.setTitle("Alert")
                .setMessage(type+" a ticket must be done while the device is connected. Please try again later.").setCancelable(true)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        return;
                    }
                });

        AlertDialog confirmAlert = confirmBuilder.create();
        confirmAlert.show();
    }

    //End

    private void sendSupportEmail() {
        try {
            emailDialog = new Dialog(this);
            LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            View inputDlgView = layoutInflater.inflate(R.layout.send_email_input_dialog, null, false);
            emailDialog.setTitle("Send Support Email");
            emailDialog.setContentView(inputDlgView);
            emailDialog.show();

            Button sendBtn = inputDlgView.findViewById(R.id.send_email_input_dialog_enter_btn);
            emailMessageText = inputDlgView.findViewById(R.id.send_email_input_dialog_text_field);
            emailMessageText.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return true;
                }
            });

            emailMessageExtraText = inputDlgView.findViewById(R.id.send_email_extra_input_dialog_text_field);
            emailMessageExtraText.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    emailMessageExtraText.setText("");
                    return true;
                }
            });

            emailMessageExtraText.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

            emailMessageText.setText(Html.fromHtml(TPUtility.getSupportEmail(this, activeTicket)));
            sendBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    TPUtility.hideSoftKeyboard(TicketViewActivity.this);

                    if (emailDialog.isShowing()) {
                        emailDialog.dismiss();
                    }

                    String msg = Html.toHtml(emailMessageText.getText());
                    msg += "<br/>" + Html.toHtml(emailMessageExtraText.getText());

                    try {
                        ArrayList<Contact> contacts = Contact.getSupportContacts();
                        if (contacts.size() == 0) {
                            String errorText = "Contact information not available. Please sync the device";
                            Toast.makeText(TicketViewActivity.this, errorText, Toast.LENGTH_LONG).show();

                            return;
                        }

                        String[] emails = new String[contacts.size()];
                        for (int i = 0; i < contacts.size(); i++) {
                            emails[i] = contacts.get(i).getEmailId();
                        }

                        String emailAddress = TPApp.getUserInfo().getEmailAddress();
                        if (emailAddress == null || emailAddress.equals("")) {
                            emailAddress = "device@turbodata.com";
                        }

                        String citation = TPUtility.prefixZeros(activeTicket.getCitationNumber(), 8);
                        String subject = TPUtility.getSupportEmailSubject(citation, activeTicket.getPlate());

                        sendEmail(emailAddress, emails, subject, msg);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            Button cancelBtn = inputDlgView.findViewById(R.id.send_email_input_dialog_cancel_btn);
            cancelBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    TPUtility.hideSoftKeyboard(TicketViewActivity.this);

                    if (emailDialog.isShowing()) {
                        emailDialog.dismiss();
                    }
                }
            });

            return;

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }

    private void sendEmail(final String from, final String[] to, final String subject, final String message) {
        if (!isServiceAvailable) {
            Toast.makeText(this, "Network not available, please try again", Toast.LENGTH_LONG).show();
            return;
        }

        progressDialog = ProgressDialog.show(this, "", "Sending Email...");

        AsyncTask<Void, Void, Boolean> emailTask = new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... voids) {
                try {
                    return ((CommonBLProcessor) screenBLProcessor).sendEmail(from, to, subject, message);
                } catch (Exception e) {
                    log.error(TPUtility.getPrintStackTrace(e));
                }

                return false;
            }

            @Override
            protected void onPostExecute(Boolean result) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                if (result.booleanValue()) {
                    Toast.makeText(TicketViewActivity.this, "Your email sent successfully", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(TicketViewActivity.this, "Failed sending email, please try again", Toast.LENGTH_LONG)
                            .show();
                }
            }
        };

        emailTask.execute();
    }

    public void voidTicket() {
        try {
            if (!Feature.isFeatureAllowed(Feature.VOID_TICKET_LOG) || !Feature.isFeatureAllowed(Feature.VOID_TICKET)) {
                Toast.makeText(this, "This feature is disabled.", Toast.LENGTH_LONG).show();
                return;
            }

            if (activeTicket.isVoided()) {
                displayErrorMessage("This ticket has already been voided.");
                return;
            }
            if (activeTicket.isWarn()) {
                displayErrorMessage("This ticket was issued as a warning. Cannot be voided.");
                return;
            }

            CharSequence[] choiceList;
            final ArrayList<VoidTicketReason> reasons = VoidTicketReason.getVoidReasons(TPApp.getCustId());
            if (reasons != null && reasons.size() > 0) {
                choiceList = new CharSequence[reasons.size()];
                int index = 0;
                for (VoidTicketReason reason : reasons) {
                    choiceList[index++] = reason.getTitle();
                }

                voidListFlag = true;
            } else {
                choiceList = new CharSequence[1];
                choiceList[0] = "NA";
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Select Void Reason");
            builder.setItems(choiceList, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        // Show Dialog only for void reason other case
                        if (reasons.get(which).getTitle().equalsIgnoreCase("OTHER")) {
                            if (Feature.isFeatureAllowed(Feature.VOID_TICKET_OTHER_COMMENT)) {
                                try {
                                    otherVoidReasonPopup(which, reasons);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                updateTicket(which, reasons);
                            }
                        } else {
                            updateTicket(which, reasons);
                        }

                    } catch (Exception e) {
                        log.error(TPUtility.getPrintStackTrace(e));
                    }
                }
            })
                    .setCancelable(true).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            AlertDialog alert = builder.create();
            alert.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeAction(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Confirmation").setMessage("Are you sure you want to delete this ticket?")
                .setCancelable(true).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (activeTicket == null) {
                            return;
                        }
                        try {
                            Ticket.removeTicketById(activeTicket.getTicketId());
                            setResult(RESULT_OK);
                            finish();
                        } catch (Exception ae) {
                            log.error(ae);
                        }
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void printAction(View view) {
        Toast.makeText(this, "Processing ticket printing...", Toast.LENGTH_SHORT).show();
        printAllTickets(activeTicket, isTicketHistory);
    }

    private void printAllTickets(Ticket printTicket, boolean isTicketHistory) {
        PrintTemplate template = TPUtility.getTicketPrintTemplate(getSharedPreferences(getPackageName(), MODE_PRIVATE));
        if (template == null) {
            Toast.makeText(this, "Print template not found", Toast.LENGTH_LONG).show();
            return;
        }

        PrintTokenParser tokenParser;
        if (isTicketHistory) {
            tokenParser = new PrintTokenParser(printTicket, template.getTemplateData(), isTicketHistory);
            tokenParser.setTicketHistory(false);
        } else {
            tokenParser = new PrintTokenParser(printTicket, template.getTemplateData());
            tokenParser.setTicketHistory(isTicketHistory);
        }

        tokenParser.setMultiPrint(true);

        ArrayList<String> printTickets = tokenParser.parseTickets();

        TicketPrinter.print(this, printTickets);
    }

    private String getPreviewHTML() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            return "";
        }
        String templateHTML = getPrintPreviewTemplate();
        templateHTML = TPUtility.parseTicket(templateHTML, activeTicket);
        templateHTML = templateHTML.replaceAll("\n", "");
        templateHTML = templateHTML.replaceAll("\r", "");
        templateHTML = templateHTML.replaceAll("\t", "");

        return templateHTML;
    }

    private String getPrintPreviewTemplate() {
        String html = "";
        try {
            PrintTemplate template = PrintTemplate.getPrintTemplateByName("PrintPreview");
            if (template != null) {
                return template.getTemplateData();
            }

            InputStream is = getAssets().open("previewTemplate.html");
            int size = is.available();

            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            html = new String(buffer);
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }

        return html;
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

    private void warnTicket() {
        if (activeTicket == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(TicketViewActivity.this);
            builder.setTitle("Make this ticket a warning");
            builder.setCancelable(true);
            builder.setMessage("Ticket not available.");
            AlertDialog alert = builder.create();
            alert.show();
        } else {
            if (Feature.isSystemFeatureAllowed(Feature.UPDATE_CUTOFF_PERIOD)) {
                String cutOffTime = Feature.getFeatureValue(Feature.UPDATE_CUTOFF_PERIOD);
                if (cutOffTime != null && !cutOffTime.equals("") && activeTicket.getTicketDate() != null) {
                    try {
                        int cutOffMins = Integer.parseInt(cutOffTime);
                        long milliseconds = (new Date().getTime() - activeTicket.getTicketDate().getTime());
                        int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
                        if (minutes > cutOffMins) {
                            displayErrorMessage("No update allowed. Cutoff time exceeded.");
                            return;
                        }
                    } catch (Exception e) {
                        log.error(TPUtility.getPrintStackTrace(e));
                    }
                }
            }

            if (activeTicket.isVoided()) {
                displayErrorMessage("No update allowed. Ticket is already voided.");
                return;
            }
            if (activeTicket.isWarn()) {
                displayErrorMessage("No update allowed. Ticket is already warned.");
                return;
            }

            try {
                activeTicket.setIsWarn("Y");
                activeTicket.setFine(0);
				/*DatabaseHelper.getInstance().openWritableDatabase();
				DatabaseHelper.getInstance().insertOrReplace(activeTicket.getContentValues(), "tickets");*/
                Ticket.updateTicket(activeTicket);

                boolean b = RepeatOffender.checkIsDataAlreadyInDBorNot(TPApp.custId, activeTicket.getStateCode(), activeTicket.getPlate(), activeTicket.getViolationId());
                if (b) {

                    RepeatOffender.countUpdateVoidCase(TPApp.custId, activeTicket.getStateCode(), activeTicket.getPlate(), activeTicket.getViolationId());
                }
                boolean syncFlag = false;
                if (Feature.isFeatureAllowed(Feature.TOGGLE_NET_ON_OFF) && TPApplication.getInstance().getNetOnOff().equals("Y")){
                    isServiceAvailable = false;
                }else {
                    isServiceAvailable = true;
                }
                if (isServiceAvailable) {
                    WriteTicketBLProcessor blProcessor = new WriteTicketBLProcessor(
                            (TPApplication) getApplicationContext());
                    ArrayList<Ticket> tickets = new ArrayList<Ticket>();
                    tickets.add(activeTicket);
                    syncFlag = blProcessor.getProxy().updateTickets(tickets);
                }

                // UPDATE SYNC DATA
                if (!syncFlag) {
                    SyncData syncData = new SyncData();
                    syncData.setActivity("UPDATE");
                    syncData.setPrimaryKey(activeTicket.getTicketId() + "");
                    syncData.setActivityDate(new Date());
                    syncData.setCustId(TPApp.custId);
                    syncData.setTableName(TPConstant.TABLE_TICKETS);
                    syncData.setStatus("Pending");
                    SyncData.insertSyncData(syncData).subscribe();
                    //DatabaseHelper.getInstance().insertOrReplace(syncData.getContentValues(), "sync_data");
                }

                //DatabaseHelper.getInstance().closeWritableDb();
                Toast.makeText(TicketViewActivity.this, "Updated ticket successfully.", Toast.LENGTH_SHORT).show();
                isTicketHistory = true;
                webview.loadUrl("file:///android_asset/printPreview.html");

            } catch (Exception e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }
        }
    }

    // Adding ticket violation from ticket for ticket history
    private void addTicketViolation(Ticket ticket) {
        JSONObject ticketObject;
        ArrayList<TicketViolation> ticketViolationList = new ArrayList<TicketViolation>();
        TicketViolation ticketViolation = null;
        try {
            ticketObject = new JSONObject();

            ticketObject.put("ticket_violation_id", ticket.getViolationId());
            ticketObject.put("ticket_id", ticket.getTicketId());
            ticketObject.put("custid", ticket.getCustId());
            ticketObject.put("violation_id", ticket.getViolationId());
            ticketObject.put("citation_number", ticket.getCitationNumber());
            ticketObject.put("violation_code", ticket.getViolationCode());
            ticketObject.put("violation", ticket.getViolation());
            ticketObject.put("fine", ticket.getFine());

            ticketViolation = new TicketViolation(ticketObject);

        } catch (Exception e) {
        } finally {
            ticketViolationList.add(ticketViolation);
            activeTicket.setTicketViolations(ticketViolationList);
        }
    }

    //Show popup to get void reason from users and sync it to server after issuing the ticket
    private void otherVoidReasonPopup(final int which, final ArrayList<VoidTicketReason> reasons) {
        try {
            //To-Do items
            otherReasonDialog = new Dialog(this);
            LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            View inputDialogView = layoutInflater.inflate(R.layout.void_reason_input_dialog, null, false);
            otherReasonDialog.setTitle("Add Void Reason");
            otherReasonDialog.setContentView(inputDialogView);
            otherReasonDialog.show();

            Button saveBtn = inputDialogView.findViewById(R.id.void_reason_input_dialog_enter_btn);
            voidReasonText = inputDialogView.findViewById(R.id.void_reason_input_dialog_text_field);
            TPUtility.showSoftKeyboard(TicketViewActivity.this, voidReasonText);

            saveBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    TPUtility.hideSoftKeyboard(TicketViewActivity.this);
                    if (otherReasonDialog.isShowing()) {
                        otherReasonDialog.dismiss();
                    }
                    addOtherCommentReson(voidReasonText.getText().toString(), which, reasons);
                }
            });

            Button cancelBtn = inputDialogView.findViewById(R.id.void_reason_input_dialog_cancel_btn);
            cancelBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    TPUtility.hideSoftKeyboard(TicketViewActivity.this);

                    if (otherReasonDialog.isShowing()) {
                        otherReasonDialog.dismiss();
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // Adds void reason for other selection
    private void addOtherCommentReson(String voidReason, int which, ArrayList<VoidTicketReason> reasons) {
        //DatabaseHelper.getInstance().openWritableDatabase();
        TicketComment tc = new TicketComment();
        int ticketCommentId = 0;
        ArrayList<TicketComment> ticketComments = new ArrayList<TicketComment>();
        try {
            ticketCommentId = TicketComment.getNextPrimaryId();

            if (tc != null) {
                tc.setCommentId(0); // As said
                tc.setTicketId(activeTicket.getTicketId());
                tc.setCitationNumber(activeTicket.getCitationNumber());
                tc.setTicketCommentId(ticketCommentId);
                tc.setCustId(TPApp.custId);
                tc.setComment(voidReason);
                tc.setVoiceComment(false);
                TicketComment.insertTicketComment(tc);
                //DatabaseHelper.getInstance().insertOrReplace(tc.getContentValues(),"ticket_comments");
            }

            SyncData syncCommentData = new SyncData();
            syncCommentData.setActivity("INSERT");
            syncCommentData.setPrimaryKey(ticketCommentId + "");
            syncCommentData.setActivityDate(new Date());
            syncCommentData.setCustId(TPApp.custId);
            syncCommentData.setTableName(TPConstant.TABLE_TICKET_COMMENTS);
            syncCommentData.setStatus("Pending");

            boolean result = false;
            if (Feature.isFeatureAllowed(Feature.TOGGLE_NET_ON_OFF) && TPApplication.getInstance().getNetOnOff().equals("Y")){
                isServiceAvailable = false;
            }else {
                isServiceAvailable = true;
            }
            if (isServiceAvailable) {
                ticketComments.add(tc);
                try {
                    WriteTicketBLProcessor blProcessor = new WriteTicketBLProcessor((TPApplication) getApplicationContext());
                    result = blProcessor.getProxy().updateTicketsComments(activeTicket.getViolationId(), ticketComments);

                } catch (Exception e) {
                    log.error(TPUtility.getPrintStackTrace(e));
                }

            }

            if (!result) {
                try {
                    SyncData.insertSyncData(syncCommentData);
                    //DatabaseHelper.getInstance().insertOrReplace(syncCommentData.getContentValues(), "sync_data");
                } catch (SQLiteException e) {
                    log.error(TPUtility.getPrintStackTrace(e));

                } catch (Exception e) {
                    log.error(TPUtility.getPrintStackTrace(e));
                }

            }

            //DatabaseHelper.getInstance().closeWritableDb();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            updateTicket(which, reasons);
        }
    }

    private void updateTicket(int which, ArrayList<VoidTicketReason> reasons) {
        try {
            activeTicket.setIsVoid("Y");
            activeTicket.setFine(0);
            activeTicket.setVoidId(voidListFlag ? reasons.get(which).getId() : 0);
            activeTicket.setVoidReasonCode(reasons.get(which).getCode());

            Ticket.updateTicket(activeTicket);
			/*DatabaseHelper.getInstance().openWritableDatabase();
			DatabaseHelper.getInstance().insertOrReplace(activeTicket.getContentValues(), "tickets");*/


            boolean syncFlag = false;
            if (Feature.isFeatureAllowed(Feature.TOGGLE_NET_ON_OFF) && TPApplication.getInstance().getNetOnOff().equals("Y")){
                isServiceAvailable = false;
            }else {
                isServiceAvailable = true;
            }
            if (isServiceAvailable) {
                WriteTicketBLProcessor blProcessor = new WriteTicketBLProcessor((TPApplication) getApplicationContext());
                ArrayList<Ticket> tickets = new ArrayList<Ticket>();
                tickets.add(activeTicket);
                syncFlag = blProcessor.getProxy().updateTickets(tickets);
            }
            boolean b = RepeatOffender.checkIsDataAlreadyInDBorNot(TPApp.custId, activeTicket.getStateCode(), activeTicket.getPlate(), activeTicket.getViolationId());
            if (b) {

                RepeatOffender.countUpdateVoidCase(TPApp.custId, activeTicket.getStateCode(), activeTicket.getPlate(), activeTicket.getViolationId());
            }

            // UPDATE SYNC DATA
            if (!syncFlag) {
                SyncData syncData = new SyncData();
                syncData.setActivity("UPDATE");
                syncData.setPrimaryKey(activeTicket.getTicketId() + "");
                syncData.setActivityDate(new Date());
                syncData.setCustId(TPApp.getCustId());
                syncData.setTableName(TPConstant.TABLE_TICKETS);
                syncData.setStatus("Pending");
                SyncData.insertSyncData(syncData).subscribe();
                //DatabaseHelper.getInstance().insertOrReplace(syncData.getContentValues(), "sync_data");
            }

            //DatabaseHelper.getInstance().closeWritableDb();
            Toast.makeText(TicketViewActivity.this, "Updated ticket successfully.", Toast.LENGTH_SHORT).show();

            webview.loadUrl("file:///android_asset/printPreview.html");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void hotListHandler(boolean result) {
            Toast.makeText(this, "HotList added successfully.", Toast.LENGTH_LONG).show();
    }

    private class CustomeGestureDetector extends SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e1 == null || e2 == null)
                return false;

            if (e1.getPointerCount() > 1 || e2.getPointerCount() > 1)
                return false;

            else {
                try { // right to left swipe .. go to next page
                    if (e1.getX() - e2.getX() > 50 && Math.abs(velocityX) > 600) {
                        onLeftSwipe();
                        return true;

                    } // left to right swipe .. go to prev page
                    else if (e2.getX() - e1.getX() > 50 && Math.abs(velocityX) > 600) {
                        onRightSwipe();
                        return true;

                    } // bottom to top, go to next document
                    else if (e1.getY() - e2.getY() > 100 && Math.abs(velocityY) > 800 && webview
                            .getScrollY() >= webview.getScale() * (webview.getContentHeight() - webview.getHeight())) {
                        return true;

                    } // top to bottom, go to prev document
                    else if (e2.getY() - e1.getY() > 100 && Math.abs(velocityY) > 800) {
                        return true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return false;
            }
        }
    }

    public void hotListAction() {
        try {
            if (!isServiceAvailable) {
                displayErrorMessage("Service not available. Please check network settings and try again.");
                return;
            }
            emailDialog = new Dialog(TicketViewActivity.this);
            LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            inputDlgView = layoutInflater.inflate(R.layout.dialog_hotlist, null, false);
            emailDialog.setTitle("Add HotList");
            emailDialog.setContentView(inputDlgView);
            emailDialog.setCanceledOnTouchOutside(false);
            emailDialog.show();

            final EditText commentEditText = inputDlgView.findViewById(R.id.hotlist_Comment);
            Button btnCancel = inputDlgView.findViewById(R.id.btnCancel);
            Button btnAccept = inputDlgView.findViewById(R.id.btnAccept);
            Button btnClear = inputDlgView.findViewById(R.id.btnClear);
            beginDate = inputDlgView.findViewById(R.id.hotlist_BeginDate);
            endDate = inputDlgView.findViewById(R.id.hotlist_EndDate);

            commentEditText.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

            /*
             * String badge =""; if (TPApp.getUserInfo() != null) badge =
             * TPApp.getUserInfo().getBadge()+"- "; commentEditText.setText(badge+"- ");
             */

            beginDate.setText(DateUtil.getBeginEndDate(new Date()));

            btnCancel.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    emailDialog.dismiss();
                    TPUtility.hideSoftKeyboard(TicketViewActivity.this);
                    //closeKeyboard();
                }
            });
            btnClear.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    commentEditText.setText("");
                    // beginDate.setText("");
                    endDate.setText("");
                }
            });
            /*
             * beginDate.setOnClickListener(new View.OnClickListener() {
             *
             * @Override public void onClick(View v) { showDatePicker(true); } });
             */
            endDate.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //showDatePicker(false);
                }
            });

            btnAccept.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // Ask permission to send data to server
                    if (commentEditText.getText().toString().trim().isEmpty()) {
                        displayErrorMessage("Please provide your comment");
                        return;
                    }
                    TPUtility.hideSoftKeyboard(TicketViewActivity.this);

                   // closeKeyboard();
                    AlertDialog.Builder builder = new AlertDialog.Builder(TicketViewActivity.this);
                    builder.setTitle("Confirmation").setMessage("Are you sure you want to add this plate as HotList?")
                            .setCancelable(false).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // process ticket to server or offline
                            if (!commentEditText.getText().toString().trim().isEmpty()) {
                                addNewHotList(activeTicket.getPlate().trim(),
                                        activeTicket.getStateCode(),
                                        commentEditText.getText().toString().trim(),
                                        beginDate.getText().toString().trim(),
                                        endDate.getText().toString().trim());
                                emailDialog.dismiss();
                                TPUtility.hideSoftKeyboard(TicketViewActivity.this);
                                //closeKeyboard();
                            } else
                                TPUtility.showErrorToast(TicketViewActivity.this,
                                        "Please provide your comment");
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addNewHotList(String plate, String stateCode, String comment, String beginDate, String endDate) {
        //DatabaseHelper.getInstance().openWritableDatabase();
        Hotlist hotList = new Hotlist();
        int hotListId = 0;
        try {
            hotListId = Hotlist.getNextPrimaryId();
            String badge = "";
            if (TPApp.getUserInfo() != null)
                badge = TPApp.getUserInfo().getBadge() + "- ";

            if (hotList != null) {
                hotList.setHostlistId(hotListId);
                hotList.setCustId(TPApp.custId);
                hotList.setPlate(plate.toUpperCase(Locale.getDefault()));
                hotList.setStateCode(stateCode.toUpperCase(Locale.getDefault()));
                hotList.setBeginDate(DateUtil.getDateObjectFromString(beginDate));
                hotList.setEndDate(DateUtil.getDateObjectFromString(endDate));
                hotList.setPlateType("ALERT");
                hotList.setComments(badge + comment.toUpperCase(Locale.getDefault()));
                Completable completable = Hotlist.insertHotlist(hotList);
                completable.subscribe();
                //DatabaseHelper.getInstance().insertOrReplace(hotList.getContentValues(), "hotlist");
            }

            boolean result = false;

            if (isServiceAvailable) {
                try {
                    //WriteTicketBLProcessor blProcessor = (WriteTicketBLProcessor) screenBLProcessor;
                    WriteTicketBLProcessor blProcessor = new WriteTicketBLProcessor((TPApplication) getApplicationContext());
                    //result = blProcessor.getProxy().newHotlist(hotList);
                    // This code is changed by mohit 01/03/2023
                    result = blProcessor.getProxy().newHotlist(hotList,this);
                    //End

                } catch (Exception e) {
                    log.error(TPUtility.getPrintStackTrace(e));
                }
            } else {
                SyncData syncHotListData = new SyncData();
                syncHotListData.setActivity("INSERT");
                syncHotListData.setActivityDate(new Date());
                syncHotListData.setCustId(TPApp.custId);
                syncHotListData.setPrimaryKey(hotList.getHostlistId() + "");
                syncHotListData.setTableName(TPConstant.TABLE_HOTLIST);
                syncHotListData.setStatus("Pending");
                Completable completable = SyncData.insertSyncData(syncHotListData);
                completable.subscribe();
                Toast.makeText(this, "HotList saved successfully.", Toast.LENGTH_LONG).show();
                //DatabaseHelper.getInstance().insertOrReplace(syncHotListData.getContentValues(), "sync_data");
            }
                // This code is changed by mohit 01/03/2023
           /* if (!result) {
                try {
                    Toast.makeText(this, "HotList saved successfully.", Toast.LENGTH_LONG).show();
                } catch (SQLiteException e) {
                    log.error(TPUtility.getPrintStackTrace(e));

                } catch (Exception e) {
                    log.error(TPUtility.getPrintStackTrace(e));
                }
                // syncDataList.add(syncCommentData);
            } else {
                Toast.makeText(this, "HotList added successfully.", Toast.LENGTH_LONG).show();
            }*/
            //End
            //DatabaseHelper.getInstance().closeWritableDb();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void closeKeyboard()
    {
        // this will give us the view
        // which is currently focus
        // in this layout
        View view = this.getCurrentFocus();

        // if nothing is currently
        // focus then this will protect
        // the app from crash
        if (view != null) {

            // now assign the system
            // service to InputMethodManager
            InputMethodManager manager
                    = (InputMethodManager)
                    getSystemService(
                            Context.INPUT_METHOD_SERVICE);
            manager
                    .hideSoftInputFromWindow(
                            view.getWindowToken(), 0);
        }
    }
}
