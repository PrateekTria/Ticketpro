package com.ticketpro.parking.activity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ticketpro.model.Body;
import com.ticketpro.model.CallInList;
import com.ticketpro.model.CallInReport;
import com.ticketpro.model.Color;
import com.ticketpro.model.Contact;
import com.ticketpro.model.CustomerInfo;
import com.ticketpro.model.Feature;
import com.ticketpro.model.Hotlist;
import com.ticketpro.model.MakeAndModel;
import com.ticketpro.model.Permit;
import com.ticketpro.model.PlateLookupResult;
import com.ticketpro.model.SyncData;
import com.ticketpro.model.Ticket;
import com.ticketpro.model.TicketPicture;
import com.ticketpro.model.TicketViolation;
import com.ticketpro.model.User;
import com.ticketpro.parking.R;
import com.ticketpro.parking.bl.WriteTicketBLProcessor;
import com.ticketpro.util.DateUtil;
import com.ticketpro.util.StringUtil;
import com.ticketpro.util.TPConstant;
import com.ticketpro.util.TPUtility;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.Completable;

/**
 * Home screen (Ref. Mockups).
 *
 * @author : TicketPRO
 * @version : 1.0
 * @since : 1.0
 */

public class PlateLookupResultActivity extends BaseActivityImpl {

    private WebView webview;
    private PlateLookupResult activeLookupResult;
    private Button acceptButton;
    private Button backButton;
    private Button actionButton;
    private Button checkPlateButton;
    private Button sendEmailButton;
    private AlertDialog callInActionDialog;
    private CheckBox acceptDetailsCheckbox;
    private ProgressDialog progressDialog;
    private Handler checkPlateHandler;
    private String plateCheckResponse;
    private String tickets;
    private String amount;
    private Dialog emailDialog;
    private EditText emailMessageText;
    private EditText emailMessageExtraText;

    /**
     * Entry point of the Activity
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.plate_lookup_view);
        setLogger(PlateLookupResultActivity.class.getName());
        setBLProcessor(new WriteTicketBLProcessor((TPApplication) getApplicationContext()));
        setActiveScreen(this);
        isNetworkInfoRequired = true;

        if (TPApp.getActiveLookupResult() == null) {
            finish();
            return;
        }

        activeLookupResult = TPApp.getActiveLookupResult();
        webview = (WebView) findViewById(R.id.plate_lookup_result_webview);

        checkPlateButton = (Button) findViewById(R.id.plate_lookup_check_plate_btn);
        sendEmailButton = (Button) findViewById(R.id.plate_lookup_send_email_btn);
        acceptButton = (Button) findViewById(R.id.plate_lookup_accept_btn);
        backButton = (Button) findViewById(R.id.plate_lookup_back_btn);
        actionButton = (Button) findViewById(R.id.plate_lookup_action_btn);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String html = getPlateHistoryHTML(activeLookupResult.getHistoryTicket());
                html += getPermitHistoryHTML(activeLookupResult.getPermit());
                html += getHotlistHTML(activeLookupResult.getHotlist());

                WebSettings webSettings = webview.getSettings();
                webSettings.setJavaScriptEnabled(true);
                webview.setWebChromeClient(new WebChromeClient());
                webview.setBackgroundColor(0x00000000);
                webview.loadData(html, "text/html", "UTF-8");
                webview.setWebViewClient(new WebViewClient() {
                    @Override
                    public void onPageFinished(WebView view, String url) {

                    }
                });
            }
        }, 500);

        boolean isScofflaw = false;
        if (activeLookupResult.getHotlist() != null && activeLookupResult.getHotlist().size() > 0) {
            for (Hotlist hotlist : activeLookupResult.getHotlist()) {
                if (hotlist.getPlateType() != null && hotlist.getPlateType().equalsIgnoreCase("SCOFFLAW")) {
                    isScofflaw = true;
                    break;
                }
            }
        }

        //Enable action button if plate type if SCOFFLAW
        if (isScofflaw) {
            acceptButton.setVisibility(View.GONE);
            backButton.setVisibility(View.GONE);

            if (Feature.isFeatureAllowed(Feature.CHECK_PLATE)) {
                checkPlateButton.setVisibility(View.VISIBLE);
            }

        } else {
            actionButton.setVisibility(View.GONE);
        }

        checkPlateHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                displayResponseMsg(plateCheckResponse);

                checkPlateButton.setEnabled(false);
                checkPlateButton.setBackgroundResource(R.drawable.btn_disabled);
            }
        };
    }


    private String getHotlistHTML(ArrayList<Hotlist> hotlist) {
        if (hotlist == null || hotlist.size() == 0) return "";

        StringBuffer msg = new StringBuffer();

        for (int i = 0; i < hotlist.size(); i++) {
            Hotlist object = hotlist.get(i);
            msg.append("<h3>Hotlist : " + object.getPlateType() + "</h3>");
            msg.append("<table>");
            msg.append("<tr><td>Plate</td><td>:</td><td>" + StringUtil.getDisplayString(object.getPlate()));
            msg.append("</td></tr>");

            //Add Agency Name
            try {
                CustomerInfo agency = CustomerInfo.getCustomerInfo(object.getCustId());
                if (Feature.isFeatureAllowed(Feature.TOGGLE_NET_ON_OFF) && TPApplication.getInstance().getNetOnOff().equals("Y")){
                    isServiceAvailable = false;
                }else {
                    isServiceAvailable = true;
                }
                if (isServiceAvailable && (agency == null || agency.getName() == null)) {
                    agency = ((WriteTicketBLProcessor) screenBLProcessor).getCustomerInfo(object.getCustId());
                }

                if (agency != null) {
                    msg.append("<tr><td>Agency</td><td>:</td><td>" + StringUtil.getDisplayString(agency.getName()) + "</td></tr>");
                }
            } catch (Exception e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }

            msg.append("<tr><td>Lookup</td><td>:</td><td>" + StringUtil.getDisplayString(object.getPlateType()));
            msg.append("</td></tr>");

            if (object.getPlateType() != null && object.getPlateType().equals("SCOFFLAWS")) {
                msg.append("<tr><td>Number of Tickets</td><td>:</td><td>" + object.getNumberOfTickets());
                msg.append("</td></tr>");
                msg.append("<tr><td>Amount Owned</td><td>:</td><td> $" + object.getAmountOwed());
                msg.append("</td></tr>");
            } else {
                msg.append("<tr><td>Begin Date</td><td>:</td><td>" + StringUtil.getDisplayString(DateUtil.getStringFromDate(object.getBeginDate())));
                msg.append("</td></tr>");
                msg.append("<tr><td>End Date</td><td>:</td><td>" + StringUtil.getDisplayString(DateUtil.getStringFromDate(object.getEndDate())));
                msg.append("</td></tr>");
            }

            msg.append("<tr><td style='vertical-align:top'>Comments</td><td style='vertical-align:top'>:</td><td style='vertical-align:top'>" + StringUtil.getDisplayString(object.getComments()));
            msg.append("</td></tr>");
            msg.append("</table>");
        }

        return msg.toString();
    }


    private String getPlateHistoryHTML(Ticket historyTicket) {

        if (historyTicket == null) return "";

        StringBuffer msg = new StringBuffer();
        msg.append("<style>body{color:#000;background-color: transparent;padding:10px;} table{color:#000;} td{vertical-align:top;} h3{color:#000; font-size:1.2em;}</style>");
        msg.append("<h3>Ticket History : " + historyTicket.getPlate() + "</h3>");
        if (historyTicket.isWarn())
            msg.append("<p>This vehicle was previously warned on " + DateUtil.getStringFromDate2(historyTicket.getTicketDate()));
        else
            msg.append("<p>This vehicle was previously ticketed on " + DateUtil.getStringFromDate2(historyTicket.getTicketDate()));

        msg.append("</p>");
        msg.append("<table>");


        //Add Agency Name
        try {
            CustomerInfo agency = CustomerInfo.getCustomerInfo(historyTicket.getCustId());
            if (Feature.isFeatureAllowed(Feature.TOGGLE_NET_ON_OFF) && TPApplication.getInstance().getNetOnOff().equals("Y")){
                isServiceAvailable = false;
            }else {
                isServiceAvailable = true;
            }
            if (isServiceAvailable && (agency == null || agency.getName() == null)) {
                agency = ((WriteTicketBLProcessor) screenBLProcessor).getCustomerInfo(historyTicket.getCustId());
            }

            if (agency != null) {
                msg.append("<tr><td>Agency</td><td>:</td><td>" + StringUtil.getDisplayString(agency.getName()) + "</td></tr>");
            }
        } catch (Exception e1) {
            log.error(TPUtility.getPrintStackTrace(e1));
        }
        //Add Citation number
        try {
            msg.append("<tr><td>Cite</td><td>:</td><td>" + historyTicket.getCitationNumber() + "</td></tr>");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (historyTicket.getViolationCode() == null || StringUtil.getDisplayString(historyTicket.getViolationCode()).equals("")) {
                ArrayList<TicketViolation> violations = TicketViolation.getTicketViolationsByCitation(historyTicket.getCitationNumber());
                if (violations == null || violations.size() == 0) {
                    if (Feature.isFeatureAllowed(Feature.TOGGLE_NET_ON_OFF) && TPApplication.getInstance().getNetOnOff().equals("Y")){
                        isServiceAvailable = false;
                    }else {
                        isServiceAvailable = true;
                    }
                    if (isServiceAvailable) {
                        violations = ((WriteTicketBLProcessor) screenBLProcessor).getLiveTicketViolations(historyTicket.getCitationNumber());
                    }
                }

                if (violations != null && violations.size() > 0) {
                    msg.append("<tr><td>Code</td><td>:</td><td>" + violations.get(0).getViolationCode() + "</td></tr>");
                    msg.append("<tr><td>Viol</td><td>:</td><td>" + violations.get(0).getViolationDesc() + "</td></tr>");
                }
            } else {
                msg.append("<tr><td>Code</td><td>:</td><td>" + historyTicket.getViolationCode() + "</td></tr>");
                msg.append("<tr><td>Viol</td><td>:</td><td>" + historyTicket.getViolation() + "</td></tr>");
            }
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }

        String bodyTitle = historyTicket.getBodyCode();
        String colorTitle = historyTicket.getColorCode();
        String makeTitle = historyTicket.getMakeCode();
        Body body = Body.getBodyByCode(historyTicket.getBodyCode());
        if (body != null) {
            bodyTitle = body.getTitle();
        }

        Color color = Color.getColorByCode(historyTicket.getColorCode());
        if (color != null) {
            colorTitle = color.getTitle();
        }

        MakeAndModel make = MakeAndModel.getMakeByCode(historyTicket.getMakeCode());
        if (make != null) {
            makeTitle = make.getMakeTitle();
        }

        msg.append("<tr><td>Plate</td><td>:</td><td>" + StringUtil.getDisplayString(historyTicket.getPlate()) + " - " + StringUtil.getDisplayString(historyTicket.getStateCode()));
        msg.append("</td></tr>");
        msg.append("<tr><td>VIN</td><td>:</td><td>" + StringUtil.getDisplayString(historyTicket.getVin()));
        msg.append("</td></tr>");
		/*msg.append("<tr><td>State</td><td>:</td><td>"+StringUtil.getDisplayString(historyTicket.getStateCode()));
		msg.append("</td></tr>");*/
        msg.append("<tr><td>Body</td><td>:</td><td>" + StringUtil.getDisplayString(bodyTitle));
        msg.append("</td></tr>");
/*		msg.append("<tr><td>Color</td><td>:</td><td>"+StringUtil.getDisplayString(colorTitle));
		msg.append("</td></tr>");*/
        msg.append("<tr><td>Make</td><td>:</td><td>" + StringUtil.getDisplayString(colorTitle) + " " + StringUtil.getDisplayString(makeTitle));
        msg.append("</td></tr>");
        msg.append("<tr><td>Exp</td><td>:</td><td>" + StringUtil.getDisplayString(historyTicket.getExpiration()));
        msg.append("</td></tr>");
        msg.append("<tr><td>Loc</td><td>:</td><td>" + TPUtility.getFullAddress(historyTicket));
        msg.append("</td></tr>");

        try {
            User userInfo = User.getUserInfo(historyTicket.getUserId());
            if (userInfo != null) {
                String name = StringUtil.getDisplayString(userInfo.getFirstName()) + " " + StringUtil.getDisplayString(userInfo.getLastName());
                msg.append("<tr><td>Officer</td><td>:</td><td>" + name + " (" + userInfo.getBadge() + ")");
                msg.append("</td></tr>");
            }
        } catch (Exception e) {
        }

        msg.append("</table>");
        return msg.toString();
    }


    private String getPermitHistoryHTML(Permit historyPermit) {
        if (historyPermit == null) {
            return "";
        }

        StringBuffer msg = new StringBuffer();
        msg.append("<style>body{color:#000;background-color: transparent;padding:10px;} table{color:#000;} td{vertical-align:top;} h3{color:#000; font-size:1.2em;}</style>");
        msg.append("<h3>Permit Number : " + historyPermit.getPermitNumber() + "</h3>");
        msg.append("<table>");

        String plate = historyPermit.getPlate();
        String vin = historyPermit.getVin();
        String bodyCode = historyPermit.getBodyCode();
        String colorCode = historyPermit.getColorCode();
        String makeCode = historyPermit.getMakeCode();
        String stateCode = historyPermit.getStateCode();
        String classCode = historyPermit.getClassCode();

        //Get Expiration Date
        String expDate = "";
        if (historyPermit.getExpiration() != null) {
            String[] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
            String monthName = "";
            int month = historyPermit.getExpiration().getMonth();
            if (month > 0) {
                monthName = months[month];
            }

            expDate = monthName + "/" + (historyPermit.getExpiration().getYear() + 1900);
        }

        String bodyTitle = "";
        String colorTitle = "";
        String makeTitle = "";
        Body body = Body.getBodyByCode(bodyCode);
        if (body != null) {
            bodyTitle = body.getTitle();
        }

        Color color = Color.getColorByCode(colorCode);
        if (color != null) {
            colorTitle = color.getTitle();
        }

        MakeAndModel make = MakeAndModel.getMakeByCode(makeCode);
        if (make != null) {
            makeTitle = make.getMakeTitle();
        }

        msg.append("<tr><td>Type</td><td>:</td><td>" + StringUtil.getDisplayString(historyPermit.getPermitType()));
        msg.append("</td></tr>");
        msg.append("<tr><td>Plate</td><td>:</td><td>" + StringUtil.getDisplayString(plate));
        msg.append("</td></tr>");
        msg.append("<tr><td>VIN</td><td>:</td><td>" + StringUtil.getDisplayString(vin));
        msg.append("</td></tr>");
        msg.append("<tr><td>State</td><td>:</td><td>" + StringUtil.getDisplayString(stateCode));
        msg.append("</td></tr>");
        msg.append("<tr><td>Body</td><td>:</td><td>" + StringUtil.getDisplayString(bodyTitle));
        msg.append("</td></tr>");
        msg.append("<tr><td>Color</td><td>:</td><td>" + StringUtil.getDisplayString(colorTitle));
        msg.append("</td></tr>");
        msg.append("<tr><td>Make</td><td>:</td><td>" + StringUtil.getDisplayString(makeTitle));
        msg.append("</td></tr>");
        msg.append("<tr><td>Exp</td><td>:</td><td>" + StringUtil.getDisplayString(expDate));
        msg.append("</td></tr>");
        msg.append("<tr><td>Status</td><td>:</td><td>" + StringUtil.getDisplayString(classCode));
        msg.append("</td></tr>");
        msg.append("<tr><td>Begin Date</td><td>:</td><td>" + StringUtil.getDisplayString(DateUtil.getStringFromDate(historyPermit.getBeginDate())));
        msg.append("</td></tr>");
        msg.append("<tr><td>End</td><td>:</td><td>" + StringUtil.getDisplayString(DateUtil.getStringFromDate(historyPermit.getEndDate())));
        msg.append("</td></tr>");

        msg.append("</table>");
        return msg.toString();
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


    public void acceptAction(View view) {
        setResult(RESULT_OK);
        finish();
    }

    private void displayResponseMsg(String response) {
        float amountOwed = 0;

        StringBuffer message = new StringBuffer();
        StringBuffer values = new StringBuffer();
        StringBuffer header = new StringBuffer();
        View view = LayoutInflater.from(getBaseContext()).inflate(R.layout.item_view, null);
        TextView headerTV = (TextView) view.findViewById(R.id.headerTV);
        TextView valueTV = (TextView) view.findViewById(R.id.valueTV);
        TextView headTV = (TextView) view.findViewById(R.id.headerTextTV);
        if (!response.equals("") && response.contains("-$")) {
            String[] respones = response.split("-");
            tickets = respones[0];
            amount = respones[1];

            try {
                amountOwed = Float.parseFloat(amount.substring(1));
            } catch (Exception e) {
            }


            message.append("Number Of Tickets" + "\n");
            values.append(": " + tickets + "\n");
            message.append("Amount Owed" + "\n");
            values.append(": " + amount + "\n");
        } else {
            headerTV.setVisibility(View.GONE);
            valueTV.setVisibility(View.GONE);
            headTV.setVisibility(View.VISIBLE);
            header.append(response);
            headTV.setText(header.toString());
        }
        headerTV.setText(message.toString());
        valueTV.setText(values.toString());
		/*WebView wv = new WebView (getBaseContext());
		wv.loadData(message.toString(), "text/html", "utf-8");
		wv.setBackgroundColor(android.graphics.Color.WHITE);
		wv.getSettings().setDefaultTextEncodingName("utf-8");
		*/
        Builder dialog = new AlertDialog.Builder(this);
        dialog.setCancelable(false);
        dialog.setView(view);
        dialog.setTitle("Plate Check Response");
        dialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        if (amountOwed > 0 && Feature.isFeatureAllowed(Feature.NOTIFY_TOW)) {
            sendEmailButton.setVisibility(View.VISIBLE);
            dialog.setNeutralButton("Send Email", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    sendEmailAction(null);
                    dialog.cancel();
                }
            });
        }

        dialog.show();
    }

    public void checkPlateAction(View view) {
        if (activeLookupResult.getHotlist().size() == 0) {
            return;
        }

        progressDialog = ProgressDialog.show(this, "", "Retrieving Plate info...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Hotlist hotlist = activeLookupResult.getHotlist().get(0);
                String plate = hotlist.getPlate();
                String state = hotlist.getStateCode();

                CustomerInfo customer = TPApp.getCustomerInfo();
                String agencyCode = customer.getAgencyCode();

                try {
                    String response = TPUtility.getURLResponseWithTLS("https://www.pticket.com/platecheck.asp?lic=" + plate + "&st=" + state + "&agcy=" + agencyCode);
                    //String response=TPUtility.getURLResponse("https://www.pticket.com/platecheck.asp?lic=5JDG733&st=CA&agcy=55");

                    Pattern pattern = Pattern.compile("\\d+\\;url\\=(.*)");
                    Matcher matcher = pattern.matcher(response);
                    if (matcher.find()) {
                        String url = matcher.group(1);
                        //url=url.replaceAll("'>", "").trim();
                        url = url.substring(0, url.indexOf("<")).trim();
                        response = TPUtility.getURLResponseWithTLS(url);
                    }

                    plateCheckResponse = response;
                    checkPlateHandler.sendEmptyMessage(1);
                } catch (IOException e) {
                    log.error("PlateCheck Error " + TPUtility.getPrintStackTrace(e));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                progressDialog.dismiss();
            }
        }).start();
    }

    @Override
    public void onBackPressed() {
        // DO Nothing on back action
    }

    public void sendEmailAction(View view) {
        sendTowNotifyEmail(tickets, amount);
    }


    private void sendEmail(final String from, final String[] to, final String subject, final String message) {
        try {
            progressDialog = ProgressDialog.show(this, "", "Sending Email...");
            Handler saveHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if (Feature.isFeatureAllowed(Feature.TOGGLE_NET_ON_OFF) && TPApplication.getInstance().getNetOnOff().equals("Y")){
                        isServiceAvailable = false;
                    }else {
                        isServiceAvailable = true;
                    }
                    if (isServiceAvailable) {
                        try {
                            boolean result = ((WriteTicketBLProcessor) screenBLProcessor).sendEmail(from, to, subject, message);
                            if (!result) {
                                Toast.makeText(PlateLookupResultActivity.this, "Failed sending email, please try again", Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            log.error(TPUtility.getPrintStackTrace(e));
                        }
                    } else {
                        Toast.makeText(PlateLookupResultActivity.this, "Network not available, please try again", Toast.LENGTH_LONG).show();
                    }

                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
            };

            saveHandler.sendEmptyMessage(1);

        } catch (Exception e) {
            Toast.makeText(PlateLookupResultActivity.this, "Failed sending email, please try again", Toast.LENGTH_LONG).show();
        }
    }

    private void sendTowNotifyEmail(String tickets, String amounts) {
        try {
            emailDialog = new Dialog(this);
            LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            View inputDlgView = layoutInflater.inflate(R.layout.send_email_input_dialog, null, false);
            emailDialog.setTitle("Tow Vehicle");
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
            emailMessageText.setText(Html.fromHtml(TPUtility.getTowNotifyEmail(this, tickets, amounts)));
            sendBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (emailDialog.isShowing())
                        emailDialog.dismiss();

                    try {
                        ArrayList<Contact> contacts = Contact.getTowNotifyContacts();
                        if (contacts.size() == 0) {
                            Toast.makeText(PlateLookupResultActivity.this, "Contact information not available. Please sync the device", Toast.LENGTH_LONG).show();
                            return;
                        }

                        String msg = Html.toHtml(emailMessageText.getText());
                        msg += "<br/>" + Html.toHtml(emailMessageExtraText.getText());

                        //Attached Images as HTML Image Data
                        if (TPApp.getActiveTicket().getTicketPictures().size() > 0) {
                            for (TicketPicture picture : TPApp.getActiveTicket().getTicketPictures()) {
                                Bitmap bitmap = BitmapFactory.decodeFile(picture.getImagePath());
                                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                                bitmap.compress(CompressFormat.PNG, 0, bos);
                                byte[] bitmapdata = bos.toByteArray();
                                String dataToUpload = Base64.encodeToString(bitmapdata, Base64.DEFAULT);
                                msg = msg + "<br/><img src='" + dataToUpload + "'>";
                            }
                        }

                        String[] emails = new String[contacts.size()];
                        for (int i = 0; i < contacts.size(); i++) {
                            emails[i] = contacts.get(i).getEmailId();
                        }

                        sendEmail(TPApp.getUserInfo().getEmailAddress(), emails, "Tow Vehicle", msg);
                    } catch (Exception e) {
                    }
                }
            });

            Button cancelBtn = (Button) inputDlgView.findViewById(R.id.send_email_input_dialog_cancel_btn);
            cancelBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (emailDialog.isShowing())
                        emailDialog.dismiss();
                }
            });

            return;
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }

    public void actionAction(View view) {
        if (activeLookupResult == null) {
            return;
        }

        ArrayList<String> extraActions = new ArrayList<String>();
        if (activeLookupResult.getHotlist() != null
                && activeLookupResult.getHotlist().size() > 0
                && (Feature.isFeatureAllowed(Feature.HOTLIST_RETURN_TO_TICKET) || Feature.isFeatureAllowed(Feature.SCOFFLAW_RETURN_TO_TICKET))) {

            extraActions.add("Return To Ticket");
        }

        ArrayList<String> choiceList = new ArrayList<String>();
        final ArrayList<CallInList> callInList = CallInList.getCallInList();
        if (callInList != null && callInList.size() > 0) {
            for (CallInList callIn : callInList) {
                choiceList.add(callIn.getActionItem());
            }
        } else {
            choiceList.add("NA");
        }

        //Add Extra Actions
        for (String action : extraActions) {
            choiceList.add(action);
        }

        acceptDetailsCheckbox = new CheckBox(this);
        acceptDetailsCheckbox.setText("Accept Ticket Details");
        acceptDetailsCheckbox.setTextColor(android.graphics.Color.BLACK);

        ListView listview = new ListView(this);
        listview.setAdapter(new ListAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, choiceList));
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                callInActionDialog.dismiss();

                String action = ((TextView) view).getText().toString();
                boolean acceptDetails = acceptDetailsCheckbox.isChecked();


                ArrayList<Hotlist> hotlist = TPApp.getActiveLookupResult().getHotlist();
                CallInReport report = new CallInReport();
                report.setActionTaken(action);
                report.setCallInDate(new Date());
                report.setFromSearch(hotlist.get(0).getPlateType());
                report.setFromHit("T");
                report.setPlate(hotlist.get(0).getPlate());
                report.setState(hotlist.get(0).getStateCode());
                report.setUserId(TPApp.userId);
                report.setCustid(TPApp.custId);

                try {
		        		/*DatabaseHelper.getInstance().openWritableDatabase();
		    			DatabaseHelper.getInstance().insertOrReplace(report.getContentValues(), "call_in_reports");*/
                    CallInReport.insertCallInReport(report);

                    boolean syncFlag = false;
                    if (isServiceAvailable) {
                        WriteTicketBLProcessor blProcessor = (WriteTicketBLProcessor) screenBLProcessor;
                        syncFlag = blProcessor.getProxy().updateCallInReport(report);
                    }

                    if (!syncFlag) {
                        SyncData syncData = new SyncData();
                        syncData.setActivity("INSERT");
                        syncData.setPrimaryKey(CallInReport.getLastInsertId() + "");
                        syncData.setActivityDate(new Date());
                        syncData.setCustId(TPApp.custId);
                        syncData.setTableName(TPConstant.TABLE_CALL_IN_REPORTS);
                        syncData.setStatus("Pending");
                        Completable completable = SyncData.insertSyncData(syncData);
                        completable.subscribe();
                        //DatabaseHelper.getInstance().insertOrReplace(syncData.getContentValues(), "sync_data");
                    }

                    //DatabaseHelper.getInstance().closeWritableDb();
                    Toast.makeText(PlateLookupResultActivity.this, "Updated Call in report successfully.", Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    log.error(TPUtility.getPrintStackTrace(e));
                }
                if (action.equals("Return To Ticket") || action.equals("NA")) {
                    if (acceptDetails)
                        acceptAction(null);
                    else
                        backAction(null);

                    return;
                }

                if (acceptDetails)
                    acceptAction(null);
                else
                    finish();
            }
        });

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setBackgroundColor(android.graphics.Color.WHITE);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.addView(listview);
        linearLayout.addView(acceptDetailsCheckbox);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Action");
        builder.setView(linearLayout);
        builder.setCancelable(true);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        callInActionDialog = builder.create();
        callInActionDialog.show();
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


    class ListAdapter extends ArrayAdapter<String> {
        public ListAdapter(Context context, int resID, ArrayList<String> items) {
            super(context, resID, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            ((TextView) view).setTextColor(android.graphics.Color.BLACK);
            return view;
        }
    }

}
