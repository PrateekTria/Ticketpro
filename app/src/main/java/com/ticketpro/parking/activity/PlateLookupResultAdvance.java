package com.ticketpro.parking.activity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
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
import com.ticketpro.model.TicketComment;
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
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlateLookupResultAdvance extends BaseActivityImpl {

    public static String PLATE_NUMBER = "";
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
    private PlateLookupListAdapter listAdapter;
    private ExpandableListView expListView;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;
    private String plateForPriorTicket = "";
    private String priorTicketHeader = "";
    private ArrayList<Ticket> priortickets;
    private boolean isEmptyHotList = true;
    private ArrayList<Ticket> priorTicektList;
    private ArrayList<Permit> permits;
    private ArrayList<Hotlist> activeHotList;
    private TextView plateLookupResultLabel;
    private String BEGIN_DATE = "Begin Date";
    private String END_DATE = "End Date";
    private String MAKE = "make";
    private String COLOR = "color";
    private boolean isEmptyPermit;
    private boolean isEmptyTicket;
    boolean isScofflaw = false;
    boolean isStolen = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plate_lookup_view_advance);
        setLogger(PlateLookupResultAdvance.class.getName());
        setBLProcessor(new WriteTicketBLProcessor((TPApplication) getApplicationContext()));
        setActiveScreen(this);

        isNetworkInfoRequired = true;

        if (TPApp.getActiveLookupResult() == null) {
            finish();
            return;
        }

        // Lookup execution
        activeLookupResult = TPApp.getActiveLookupResult();
        TPApp.setPlateLookupResultActivity(this);

        checkPlateButton = (Button) findViewById(R.id.plate_lookup_check_plate_btn);
        sendEmailButton = (Button) findViewById(R.id.plate_lookup_send_email_btn);
        acceptButton = (Button) findViewById(R.id.plate_lookup_accept_btn);
        backButton = (Button) findViewById(R.id.plate_lookup_back_btn);
        actionButton = (Button) findViewById(R.id.plate_lookup_action_btn);
        expListView = (ExpandableListView) findViewById(R.id.listViewExpnd);
        plateLookupResultLabel = (TextView) findViewById(R.id.plateLookupResultLabel);

        try {
            Intent intent = getIntent();
            plateLookupResultLabel.setText("Lookup Result - " + intent.getStringExtra(PLATE_NUMBER));
        } catch (Exception e) {
            e.printStackTrace();
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    String html = getPlateHistoryHTML(activeLookupResult.getAllTicket());
                    String permitHistory = getPermitHistoryHTML(activeLookupResult.getPermit());
                    String hotList = getHotlistHTML(activeLookupResult.getHotlist());
                    String priorTickets = getPriorTicketsHTML(activeLookupResult.getAllTicket());

                    isEmptyHotList = hotList.isEmpty();
                    isEmptyPermit = permitHistory.isEmpty();
                    isEmptyTicket = html.isEmpty();
                    try {
                        if (activeLookupResult.getAllPermit() != null) {
                            permits = activeLookupResult.getAllPermit();
                        }
                    } catch (Exception e) {
                        log.error(TPUtility.getPrintStackTrace(e));
                    }

                    // preparing list data
                    prepareListData(activeLookupResult.getAllTicket(), activeLookupResult.getAllPermit(), html, hotList, permitHistory, priorTickets);

                    handleDataAdapter();

                } catch (Exception e) {
                    log.error(TPUtility.getPrintStackTrace(e));
                }
            }
        }, 1000);

        //boolean isScofflaw = false;
        if (activeLookupResult.getHotlist() != null && activeLookupResult.getHotlist().size() > 0) {
            for (Hotlist hotlist : activeLookupResult.getHotlist()) {
                if (hotlist.getPlateType() != null && hotlist.getPlateType().equalsIgnoreCase("SCOFFLAW")) {
                    isScofflaw = true;
                    break;
                }
                if (hotlist.getPlateType() != null && hotlist.getPlateType().equalsIgnoreCase("STOLEN")) {
                    isStolen = true;
                    break;
                }
            }
        }

        // Enable action button if plate type if SCOFFLAW Or STOLEN VEHICLE
        if (isScofflaw ||isStolen) {
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

        expListView.setOnGroupClickListener(new OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return false;
            }
        });

        expListView.setOnGroupExpandListener(new OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {

            }
        });

        expListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {

            }
        });
        expListView.setOnChildClickListener(new OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                final int groupCount = expListView.getExpandableListAdapter().getGroupCount();
                if (priorTicektList != null && priorTicektList.size() != 0 && groupPosition == (groupCount - 1)) {
                    displayTicketHistory(priorTicektList, childPosition, groupPosition, groupCount);
                } else {
                    if (permits != null && permits.size() != 0) {
                        displayPermitHistory(permits, childPosition, groupPosition, groupCount);
                    } else {
                        if (priorTicektList != null)
                            displayTicketHistory(priorTicektList, childPosition, groupPosition, groupCount);
                    }

                }

                return false;
            }
        });

        setIndicatorToRight();
    }

    private void handleDataAdapter() {
        try {
            listAdapter = new PlateLookupListAdapter(this, listDataHeader, listDataChild);
            expListView.setAdapter(listAdapter);

            if (listDataHeader.get(0).equals("STOLEN") || listDataHeader.get(0).equals("SCOFFLAW")) {
                expListView.expandGroup(0);
            }

            if (isEmptyTicket && isEmptyPermit) {

            } else {
                //expListView.expandGroup(2);
                if (!isEmptyTicket && isEmptyPermit) {
                    expListView.expandGroup(0);
                }
                if (!isEmptyPermit && isEmptyTicket) {
                    expListView.expandGroup(0);
                }
                if (isEmptyTicket && !isEmptyPermit) {
                    expListView.expandGroup(0);
                }
                if (!isEmptyTicket && !isEmptyPermit) {
                    expListView.expandGroup(0);
                    expListView.expandGroup(1);
                }
				/*else if (activeHotList != null && !isEmptyPermit) {
					if(!isEmptyTicket){
						expListView.expandGroup(2);
					}else{
						expListView.expandGroup(0);
					}
				}else if ((activeHotList == null || isEmptyHotList) && !isEmptyPermit) {
					if(!isEmptyTicket)
					expListView.expandGroup(1);
					else
						expListView.expandGroup(0);
				}*/
            }
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }

    private CustomerInfo getCustomerInfo(ArrayList<CustomerInfo> customers, int custId) {
        CustomerInfo info = null;
        for (CustomerInfo customer : customers) {
            if (customer.getCustId() == custId) {
                info = customer;
                break;
            }
        }
        return info;
    }

    /* Preparing List data here */
    private void prepareListData(ArrayList<Ticket> arrayList, ArrayList<Permit> permitList, String ticketData, String hotList, String permitHistory, String priorTicketHistory) {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        List<String> ticketRecords = new ArrayList<String>();
        List<String> permitRecords = new ArrayList<String>();
        List<String> priorTickets = new ArrayList<String>();

        // Adding data to header
        if (!StringUtil.isEmpty(permitHistory) && permitList != null) {
            listDataHeader.add("Permit History");
        }

        if (arrayList != null && !ticketData.isEmpty()) {
            listDataHeader.add("Last Ticket");
        }
        if (!StringUtil.isEmpty(hotList) || activeHotList != null) {
            try {
                boolean hasAlert = false;
                for (int i = 0; i < activeHotList.size(); i++) {
                    Hotlist object = activeHotList.get(i);
                    if (object.getPlateType().contains("ALERT")) {
                        hasAlert = true;
                    } else {
                        listDataHeader.add(object.getPlateType());
                    }
                }
                if (hasAlert) {
                    listDataHeader.add("ALERT");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (priorTicektList != null) {
            priorTicketHeader = "Prior Tickets" + " (" + priorTicektList.size() + ")";
        }

        if (!StringUtil.isEmpty(priorTicketHistory)) {
            listDataHeader.add(priorTicketHeader);
        }

        // Adding child data
        if (ticketData.isEmpty()) {
            ticketRecords.add("No Last Ticket!");
        } else {
            ticketRecords.add(ticketData);
        }

        if (priorTicektList != null && !priorTicketHistory.isEmpty()) {
            ArrayList<CustomerInfo> customers;
            try {
                customers = CustomerInfo.getCustomers();
            } catch (Exception e) {
                customers = new ArrayList<CustomerInfo>();
                log.error(TPUtility.getPrintStackTrace(e));
            }

            for (int i = 0; i < priorTicektList.size(); i++) {
                Ticket ticket = priorTicektList.get(i);
                String agencyName = "";
                String violCode = "";
                String violDesc = "";

                // Add Agency Name
                try {
                    CustomerInfo agency = getCustomerInfo(customers, ticket.getCustId());
                    if (agency != null) {
                        agencyName = agency.getName();
                    }

                    violCode = StringUtil.getDisplayString(ticket.getViolationCode());
                    violDesc = StringUtil.getDisplayString(ticket.getViolation());

                } catch (Exception e1) {
                    log.error(TPUtility.getPrintStackTrace(e1));
                }

                String ticketType = getTicketType(ticket);

                priorTickets.add(ticketType + DateUtil.getStringFromDate2(ticket.getTicketDate()) + " - " + agencyName + "\n" + violCode.trim() + " - " + violDesc.trim());
            }

        } else {
            priorTickets.add("No Prior Tickets!");
        }

	/*	// hot list
		if (!hotList.isEmpty()){
			hotListRecord.add(hotList);
		}else{
			hotListRecord.add("No Hotlist!");
		}*/

        // Adding permit Records here as
        if (permitList != null || !permitHistory.isEmpty()) {
            for (int i = 0; i < permitList.size(); i++) {
                permitRecords.add(permitList.get(i).getPlate() + " - " + permitList.get(i).getPermitNumber() + " - " + permitList.get(i).getClassCode() + "\n" + permitList.get(i).getPermitType() + "\n" + permitList.get(i).getAddress1() + " - " + permitList.get(i).getPermitCode() + "\n" + BEGIN_DATE + " - " + getPermitDate(BEGIN_DATE, permitList.get(i)) + "\n" + END_DATE + "    - " + getPermitDate(END_DATE, permitList.get(i)) + "\n" + getVehicleTitleByType(COLOR, permitList.get(i).getColorCode()) + " - " + getVehicleTitleByType(MAKE, permitList.get(i).getMakeCode()));
            }
        } else {
            permitRecords.add("No Permit History!");
        }


        //Adding Child Item To Group
        if (permitHistory != null && !permitHistory.isEmpty()) {
            listDataChild.put("Permit History", permitRecords);
        }
        if (arrayList != null && !ticketData.isEmpty()) {
            listDataChild.put("Last Ticket", ticketRecords);
        }

        if (priorTicketHistory != null && !priorTicketHistory.isEmpty()) {
            listDataChild.put(priorTicketHeader, priorTickets);
        }

        if (hotList != null && !hotList.isEmpty()) {
            try {
                StringBuffer hotListData = new StringBuffer();
                List<String> hotListAlert = new ArrayList<String>();
                for (int i = 0; i < activeHotList.size(); i++) {
                    //hotListRecord.add(getHotlistHTML(activeHotList.get(i)));
                    List<String> hotListRecord = new ArrayList<String>();
                    if (activeHotList.get(i).getPlateType().contains("ALERT")) {
                        hotListData.append(getHotlistHTML(activeHotList.get(i)));
                    } else {
                        hotListRecord.add(getHotlistHTML(activeHotList.get(i)));
                        listDataChild.put(activeHotList.get(i).getPlateType(), hotListRecord);
                    }
                }
                hotListAlert.add(hotListData.toString());
                listDataChild.put("ALERT", hotListAlert);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    private String getPriorTicketsHTML(ArrayList<Ticket> ticketlist) {
        if (ticketlist == null || ticketlist.size() == 0 || ticketlist.size() == 1) {
            return "";
        }

        StringBuffer msg = new StringBuffer();
        try {
            priorTicektList = ticketlist;
            for (int i = 0; i < ticketlist.size(); i++) {
                Ticket object = ticketlist.get(i);

                if ((i % 2) == 0) {
                    msg.append("<table style='background-color:#333333; color:#fff; width:100%;'>");
                } else {
                    msg.append("<table style='background-color:#fffcce; width:100%;'>");
                }

                msg.append("<tr><td>Date:&nbsp;&nbsp;" + DateUtil.getStringFromDate2(object.getTicketDate()) + "&nbsp;&nbsp;&nbsp;Warned(" + object.getIsWarn() + ")</td></tr>");
                msg.append("<tr style = 'padding-bottom:10px;'><td>Viol:&nbsp;&nbsp;&nbsp;" + object.getViolation() + "</td></tr>");
                msg.append("</table>");
            }
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }

        return msg.toString();
    }

    private String getPlateHistoryHTML(ArrayList<Ticket> arrayList) {
        if (arrayList == null || arrayList.isEmpty()) {
            return "";
        }

        Ticket ticket = arrayList.get(0);
        ArrayList<TicketComment> ticketComments = ticket.getTicketComments();
        StringBuffer msg = new StringBuffer();
        if (ticket.isWarn()) {
            msg.append("<p>This vehicle was previously warned on " + DateUtil.getStringFromDate2(ticket.getTicketDate()));
        } else {
            msg.append("<p>This vehicle was previously ticketed on " + DateUtil.getStringFromDate2(ticket.getTicketDate()));
        }

        msg.append("</p>");
        msg.append("<table>");

        // Add Agency Name
        try {
            CustomerInfo agency = CustomerInfo.getCustomerInfo(ticket.getCustId());
            if (agency != null) {
                msg.append("<tr><td>Agency</td><td>:</td><td>" + StringUtil.getDisplayString(agency.getName()) + "</td></tr>");
            }
        } catch (Exception e1) {
            log.error(TPUtility.getPrintStackTrace(e1));
        }

        // Add Citation number
        msg.append("<tr><td>Cite</td><td>:</td><td>" + ticket.getCitationNumber() + "</td></tr>");

        try {
            if (StringUtil.isEmpty(ticket.getViolationCode())) {
                ArrayList<TicketViolation> violations = TicketViolation.getTicketViolationsByCitation(ticket.getCitationNumber());
                if (violations == null || violations.size() == 0) {
                    if (isServiceAvailable) {
                        violations = ((WriteTicketBLProcessor) screenBLProcessor).getLiveTicketViolations(ticket.getCitationNumber());
                    }
                }

                if (violations != null && violations.size() > 0) {
                    msg.append("<tr><td>Code</td><td>:</td><td>" + violations.get(0).getViolationCode() + "</td></tr>");
                    msg.append("<tr><td>Viol</td><td>:</td><td>" + violations.get(0).getViolationDesc() + "</td></tr>");
                }
            } else {
                msg.append("<tr><td>Code</td><td>:</td><td>" + ticket.getViolationCode() + "</td></tr>");
                msg.append("<tr><td>Viol</td><td>:</td><td>" + ticket.getViolation() + "</td></tr>");
            }
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }

        if (Feature.isFeatureAllowed("park_commentsInLookup")) {

            if (ticketComments.size() > 0) {
                int n = 1;
                for (int i = 0; i < ticketComments.size(); i++) {
                    TicketComment tc = ticketComments.get(i);

                    if (!tc.isPrivate()) {
                        msg.append("<tr><td>CMT" + n + "</td><td>:</td><td>" + tc.getComment() + "</td></tr>");
                        n++;
                    }
                    if (tc.isPrivate()) {
                        msg.append("<tr><td>CMTPRV</td><td>:</td><td>" + tc.getComment() + "</td></tr>");
                    }
                }
            }
        }

        String bodyTitle = ticket.getBodyCode();
        String colorTitle = ticket.getColorCode();
        String makeTitle = ticket.getMakeCode();
        Body body = Body.getBodyByCode(ticket.getBodyCode());
        if (body != null) {
            bodyTitle = body.getTitle();
        }

        Color color = Color.getColorByCode(ticket.getColorCode());
        if (color != null) {
            colorTitle = color.getTitle();
        }

        MakeAndModel make = MakeAndModel.getMakeByCode(ticket.getMakeCode());
        if (make != null) {
            makeTitle = make.getMakeTitle();
        }

        plateForPriorTicket = StringUtil.getDisplayString(ticket.getPlate());

        msg.append("<tr><td>Plate</td><td>:</td><td>" + StringUtil.getDisplayString(ticket.getPlate()) + " - " + StringUtil.getDisplayString(ticket.getStateCode()));
        msg.append("</td></tr>");
        msg.append("<tr><td>VIN</td><td>:</td><td>" + StringUtil.getDisplayString(ticket.getVin()));
        msg.append("</td></tr>");

        msg.append("<tr><td>Body</td><td>:</td><td>" + StringUtil.getDisplayString(bodyTitle));
        msg.append("</td></tr>");

        msg.append("<tr><td>Make</td><td>:</td><td>" + StringUtil.getDisplayString(colorTitle) + " " + StringUtil.getDisplayString(makeTitle));
        msg.append("</td></tr>");
        msg.append("<tr><td>Exp</td><td>:</td><td>" + StringUtil.getDisplayString(ticket.getExpiration()));
        msg.append("</td></tr>");
        msg.append("<tr><td>Loc</td><td>:</td><td>" + TPUtility.getFullAddress(ticket));
        msg.append("</td></tr>");

        try {
            User userInfo = User.getUserInfo(ticket.getUserId());
            if (userInfo != null) {
                String name = StringUtil.getDisplayString(userInfo.getFirstName()) + " " + StringUtil.getDisplayString(userInfo.getLastName());
                msg.append("<tr><td>Officer</td><td>:</td><td>" + name + " (" + userInfo.getBadge() + ")");
                msg.append("</td></tr>");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        msg.append("</table>");

        return msg.toString();
    }

    private String getHotlistHTML(ArrayList<Hotlist> hotlist) {
        if (hotlist == null || hotlist.size() == 0) {
            return "";
        }

        StringBuffer msg = new StringBuffer();
        activeHotList = hotlist;
        for (int i = 0; i < hotlist.size(); i++) {
            Hotlist object = hotlist.get(i);
            msg.append("<h3>Hotlist : " + object.getPlateType() + "</h3>");
            msg.append("<table>");
            msg.append("<tr><td>Plate</td><td>:</td><td>" + StringUtil.getDisplayString(object.getPlate()));
            msg.append("</td></tr>");

            // Add Agency Name
            try {
                CustomerInfo agency = CustomerInfo.getCustomerInfo(object.getCustId());
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


    private String getHotlistHTML(Hotlist object) {
        if (object == null) {
            return "";
        }
        StringBuilder msg = new StringBuilder();
        msg.append("<h3>Hotlist : ").append(object.getPlateType()).append("</h3>");
        msg.append("<table>");
        msg.append("<tr><td>Plate</td><td>:</td><td>").append(StringUtil.getDisplayString(object.getPlate()));
        msg.append("</td></tr>");

        // Add Agency Name
        try {
            CustomerInfo agency = CustomerInfo.getCustomerInfo(object.getCustId());
            if (agency != null) {
                msg.append("<tr><td>Agency</td><td>:</td><td>").append(StringUtil.getDisplayString(agency.getName())).append("</td></tr>");
            }
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }

        msg.append("<tr><td>Lookup</td><td>:</td><td>").append(StringUtil.getDisplayString(object.getPlateType()));
        msg.append("</td></tr>");

        if (object.getPlateType() != null && object.getPlateType().equals("SCOFFLAWS")) {
            msg.append("<tr><td>Number of Tickets</td><td>:</td><td>").append(object.getNumberOfTickets());
            msg.append("</td></tr>");
            msg.append("<tr><td>Amount Owned</td><td>:</td><td> $").append(object.getAmountOwed());
            msg.append("</td></tr>");
        } else {
            msg.append("<tr><td>Begin Date</td><td>:</td><td>").append(StringUtil.getDisplayString(DateUtil.getStringFromDate(object.getBeginDate())));
            msg.append("</td></tr>");
            msg.append("<tr><td>End Date</td><td>:</td><td>").append(StringUtil.getDisplayString(DateUtil.getStringFromDate(object.getEndDate())));
            msg.append("</td></tr>");
        }

        msg.append("<tr><td style='vertical-align:top'>Comments</td><td style='vertical-align:top'>:</td><td style='vertical-align:top'>").append(StringUtil.getDisplayString(object.getComments()));
        msg.append("</td></tr>");
        msg.append("</table>");
        return msg.toString();
    }


    public String getPermitHistoryHTML(Permit historyPermit) {
        if (historyPermit == null) {
            return "";
        }

        StringBuilder msg = new StringBuilder();
        msg.append("<style>body{color:#000;background-color: transparent;padding:10px;} table{color:#000;} td{vertical-align:top;} h3{color:#000; font-size:1.2em;}</style>");
        msg.append("<h3>Permit Number : ").append(historyPermit.getPermitNumber()).append("</h3>");
        msg.append("<table>");

        String plate = historyPermit.getPlate();
        String vin = historyPermit.getVin();
        String bodyCode = historyPermit.getBodyCode();
        String colorCode = historyPermit.getColorCode();
        String makeCode = historyPermit.getMakeCode();
        String stateCode = historyPermit.getStateCode();
        String classCode = historyPermit.getClassCode();

        // Get Expiration Date
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

        msg.append("<tr><td>Type</td><td>:</td><td>").append(StringUtil.getDisplayString(historyPermit.getPermitType()));
        msg.append("</td></tr>");
        msg.append("<tr><td>Address</td><td>:</td><td>").append(StringUtil.getDisplayString(historyPermit.getAddress1())).append("</td><td> - </td><td>").append(StringUtil.getDisplayString(historyPermit.getPermitCode()));
        msg.append("</td></tr>");
        msg.append("<tr><td>Plate</td><td>:</td><td>").append(StringUtil.getDisplayString(plate));
        msg.append("</td></tr>");
        msg.append("<tr><td>VIN</td><td>:</td><td>").append(StringUtil.getDisplayString(vin));
        msg.append("</td></tr>");
        msg.append("<tr><td>State</td><td>:</td><td>").append(StringUtil.getDisplayString(stateCode));
        msg.append("</td></tr>");
        msg.append("<tr><td>Body</td><td>:</td><td>").append(StringUtil.getDisplayString(bodyTitle));
        msg.append("</td></tr>");
        msg.append("<tr><td>Color</td><td>:</td><td>").append(StringUtil.getDisplayString(colorTitle));
        msg.append("</td></tr>");
        msg.append("<tr><td>Make</td><td>:</td><td>").append(StringUtil.getDisplayString(makeTitle));
        msg.append("</td></tr>");
        msg.append("<tr><td>Exp Date</td><td>:</td><td>").append(StringUtil.getDisplayString(expDate));
        msg.append("</td></tr>");
        msg.append("<tr><td>Status</td><td>:</td><td>").append(StringUtil.getDisplayString(classCode));
        msg.append("</td></tr>");
        msg.append("<tr><td>Begin Date</td><td>:</td><td>").append(StringUtil.getDisplayString(DateUtil.getStringFromDate(historyPermit.getBeginDate())));
        msg.append("</td></tr>");
        msg.append("<tr><td>End Date</td><td>:</td><td>").append(StringUtil.getDisplayString(DateUtil.getStringFromDate(historyPermit.getEndDate())));
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

        StringBuilder message = new StringBuilder();
        StringBuilder values = new StringBuilder();
        StringBuilder header = new StringBuilder();

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
            } catch (Exception ignored) {
            }


            message.append("Number Of Tickets" + "\n");

            values.append(": ").append(tickets).append("\n");

            message.append("Amount Owed" + "\n");

            values.append(": ").append(amount).append("\n");
        } else {
            headerTV.setVisibility(View.GONE);
            valueTV.setVisibility(View.GONE);
            headTV.setVisibility(View.VISIBLE);
            header.append(response);
            headTV.setText(header.toString());
        }

        headerTV.setText(message.toString());
        valueTV.setText(values.toString());

        /*WebView f = new WebView(getBaseContext());
        wv.loadData(message.toString(), "text/html", "utf-8");
        wv.setBackgroundColor(android.graphics.Color.WHITE);
        wv.getSettings().setDefaultTextEncodingName("utf-8");*/

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
                    // String
                    // response=TPUtility.getURLResponse("https://www.pticket.com/platecheck.asp?lic=5JDG733&st=CA&agcy=55");

                    Pattern pattern = Pattern.compile("\\d+\\;url\\=(.*)");
                    Matcher matcher = pattern.matcher(response);
                    if (matcher.find()) {
                        String url = matcher.group(1);
                        //url = url.replaceAll("'>", "").trim();
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
                    if (isServiceAvailable) {
                        try {
                            boolean result = ((WriteTicketBLProcessor) screenBLProcessor).sendEmail(from, to, subject, message);
                            if (!result) {
                                Toast.makeText(PlateLookupResultAdvance.this, "Failed sending email, please try again", Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            log.error(TPUtility.getPrintStackTrace(e));
                        }
                    } else {
                        Toast.makeText(PlateLookupResultAdvance.this, "Network not available, please try again", Toast.LENGTH_LONG).show();
                    }

                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
            };

            saveHandler.sendEmptyMessage(1);

        } catch (Exception e) {
            Toast.makeText(PlateLookupResultAdvance.this, "Failed sending email, please try again", Toast.LENGTH_LONG).show();
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
                            Toast.makeText(PlateLookupResultAdvance.this, "Contact information not available. Please sync the device", Toast.LENGTH_LONG).show();
                            return;
                        }

                        String msg = Html.toHtml(emailMessageText.getText());
                        msg += "<br/>" + Html.toHtml(emailMessageExtraText.getText());

                        // Attached Images as HTML Image Data
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
        if (activeLookupResult.getHotlist() != null && activeLookupResult.getHotlist().size() > 0
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

        // Add Extra Actions
        for (String action : extraActions) {
            choiceList.add(action);
        }

        if (isStolen) {
            acceptDetailsCheckbox = new CheckBox(this);
            acceptDetailsCheckbox.setText("Accept Ticket Details");
            acceptDetailsCheckbox.setTextColor(android.graphics.Color.BLACK);
            acceptDetailsCheckbox.setVisibility(View.GONE);
        } else {
            acceptDetailsCheckbox = new CheckBox(this);
            acceptDetailsCheckbox.setText("Accept Ticket Details");
            acceptDetailsCheckbox.setTextColor(android.graphics.Color.BLACK);
            acceptDetailsCheckbox.setVisibility(View.VISIBLE);
        }
       /* acceptDetailsCheckbox = new CheckBox(this);
        acceptDetailsCheckbox.setText("Accept Ticket Details");
        acceptDetailsCheckbox.setTextColor(android.graphics.Color.BLACK);*/

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
                        SyncData.insertSyncData(syncData).subscribe();
                        //DatabaseHelper.getInstance().insertOrReplace(syncData.getContentValues(), "sync_data");
                    }

                    //DatabaseHelper.getInstance().closeWritableDb();
                    Toast.makeText(PlateLookupResultAdvance.this, "Updated Call in report successfully.", Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    log.error(TPUtility.getPrintStackTrace(e));
                }

                if (action.equals("Return To Ticket") || action.equals("NA")) {
                    if (acceptDetails) {
                        acceptAction(null);
                    } else {
                        backAction(null);
                    }

                    return;
                }

                if (acceptDetails) {
                    acceptAction(null);
                } else {
                    finish();
                }
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

    public void displayTicketHistory(ArrayList<Ticket> ticketlist, int pos, int groupPos, int groupCount) {
        try {
            priortickets = new ArrayList<>();
            priortickets.addAll(ticketlist);

            TPApp.setTicketList(priortickets);

            Intent intent = new Intent(this, PlateLookupResultPriorTickets.class);
            intent.putExtra("position", (int) pos);
            intent.putExtra("groupPos", (int) groupPos);
            intent.putExtra("groupCount", (int) groupCount);
            intent.putExtra("displayType", "plate");

            startActivity(intent);

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }

    // This method will show permit records in details
    public void displayPermitHistory(ArrayList<Permit> permitList, int pos, int groupPos, int groupCount) {
        if (permitList == null) {
            return;
        }

        try {
            Intent intent = new Intent(this, PlateLookupResultPriorTickets.class);
            intent.putExtra("position", (int) pos);
            intent.putExtra("groupPos", (int) groupPos);
            intent.putExtra("groupCount", (int) groupCount);
            intent.putExtra("displayType", "permit");

            startActivity(intent);

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }

    public String getTicketHistory(ArrayList<Ticket> arrayList, int pos) {
        if (arrayList == null || arrayList.isEmpty()) {
            return "";
        }

        StringBuffer msg = new StringBuffer();
        Ticket ticket = arrayList.get(pos);
        ArrayList<TicketComment> ticketComments = ticket.getTicketComments();

        if (ticket.isWarn()) {
            msg.append("<p>This vehicle was previously warned on " + DateUtil.getStringFromDate2(ticket.getTicketDate()));
        } else if (ticket.isVoided()) {
            msg.append("<p>Citation Voided, originally written on <br>" + DateUtil.getStringFromDate2(ticket.getTicketDate()));
        } else {
            msg.append("<p>This vehicle was previously ticketed on " + DateUtil.getStringFromDate2(ticket.getTicketDate()));
        }

        msg.append("</p>");
        msg.append("<table>");

        // Add Agency Name
        try {
            CustomerInfo agency = CustomerInfo.getCustomerInfo(ticket.getCustId());
            if (isServiceAvailable && (agency == null || agency.getName() == null)) {
                agency = ((WriteTicketBLProcessor) screenBLProcessor).getCustomerInfo(ticket.getCustId());
            }

            if (agency != null) {
                msg.append("<tr><td>Agency</td><td>:</td><td>" + StringUtil.getDisplayString(agency.getName()) + "</td></tr>");
            }
        } catch (Exception e1) {
            log.error(TPUtility.getPrintStackTrace(e1));
        }

        // Add Citation number
        msg.append("<tr><td>Cite</td><td>:</td><td>" + ticket.getCitationNumber() + "</td></tr>");

        try {
            if (StringUtil.isEmpty(ticket.getViolationCode())) {
                ArrayList<TicketViolation> violations = TicketViolation.getTicketViolationsByCitation(ticket.getCitationNumber());
                if (violations == null || violations.size() == 0) {
                    if (isServiceAvailable) {
                        violations = ((WriteTicketBLProcessor) screenBLProcessor).getLiveTicketViolations(ticket.getCitationNumber());
                    }
                }

                if (violations != null && violations.size() > 0) {
                    msg.append("<tr><td>Code</td><td>:</td><td>" + violations.get(pos).getViolationCode() + "</td></tr>");
                    msg.append("<tr><td>Viol</td><td>:</td><td>" + violations.get(pos).getViolationDesc() + "</td></tr>");
                }
            } else {
                msg.append("<tr><td>Code</td><td>:</td><td>" + ticket.getViolationCode() + "</td></tr>");
                msg.append("<tr><td>Viol</td><td>:</td><td>" + ticket.getViolation() + "</td></tr>");
            }
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }

        if (Feature.isFeatureAllowed("park_commentsInLookup")) {

            if (ticketComments.size() > 0) {
                int n = 1;
                for (int i = 0; i < ticketComments.size(); i++) {
                    TicketComment tc = ticketComments.get(i);

                    if (!tc.isPrivate()) {
                        msg.append("<tr><td>CMT" + n + "</td><td>:</td><td>" + tc.getComment() + "</td></tr>");
                        n++;
                    }
                    if (tc.isPrivate()) {
                        msg.append("<tr><td>CMTPRV</td><td>:</td><td>" + tc.getComment() + "</td></tr>");
                    }
                }
            }
        }

        String bodyTitle = ticket.getBodyCode();
        String colorTitle = ticket.getColorCode();
        String makeTitle = ticket.getMakeCode();

        Body body = Body.getBodyByCode(ticket.getBodyCode());
        if (body != null) {
            bodyTitle = body.getTitle();
        }

        Color color = Color.getColorByCode(ticket.getColorCode());
        if (color != null) {
            colorTitle = color.getTitle();
        }

        MakeAndModel make = MakeAndModel.getMakeByCode(ticket.getMakeCode());
        if (make != null) {
            makeTitle = make.getMakeTitle();
        }

        msg.append("<tr><td>Plate</td><td>:</td><td>" + StringUtil.getDisplayString(ticket.getPlate()) + " - " + StringUtil.getDisplayString(ticket.getStateCode()));
        msg.append("</td></tr>");
        msg.append("<tr><td>VIN</td><td>:</td><td>" + StringUtil.getDisplayString(ticket.getVin()));
        msg.append("</td></tr>");

        msg.append("<tr><td>Body</td><td>:</td><td>" + StringUtil.getDisplayString(bodyTitle));
        msg.append("</td></tr>");

        msg.append("<tr><td>Make</td><td>:</td><td>" + StringUtil.getDisplayString(colorTitle) + " " + StringUtil.getDisplayString(makeTitle));
        msg.append("</td></tr>");
        msg.append("<tr><td>Exp</td><td>:</td><td>" + StringUtil.getDisplayString(ticket.getExpiration()));
        msg.append("</td></tr>");
        msg.append("<tr><td>Loc</td><td>:</td><td>" + TPUtility.getFullAddress(ticket));
        msg.append("</td></tr>");

        try {
            User userInfo = User.getUserInfo(ticket.getUserId());
            if (userInfo != null) {
                String name = StringUtil.getDisplayString(userInfo.getFirstName()) + " " + StringUtil.getDisplayString(userInfo.getLastName());
                msg.append("<tr><td>Officer</td><td>:</td><td>" + name + " (" + userInfo.getBadge() + ")");
                msg.append("</td></tr>");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        msg.append("</table>");
        return msg.toString();
    }

    public String getTicketType(Ticket ticket) {
        String ticketType = "";
        try {
            boolean isTrue = ticket.isVoided();
            if (!isTrue) {
                isTrue = ticket.isWarn();
                if (!isTrue) {
                    isTrue = ticket.isDriveAway();
                    if (!isTrue) {
                        ticketType = "";
                    } else {
                        ticketType = "D";
                    }
                } else {
                    ticketType = "W";
                }
            } else {
                ticketType = "V";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ticketType;
    }

    // Align tab selector indicator towards right
    private void setIndicatorToRight() {
        try {
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int width = metrics.widthPixels;

            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
                expListView.setIndicatorBounds(width - GetPixelFromDips(50), width - GetPixelFromDips(10));
                //expListView.setIndicatorBounds(width - GetPixelFromDips(50), width - GetPixelFromDips(10));
            } else {
                expListView.setIndicatorBoundsRelative(width - GetPixelFromDips(50), width - GetPixelFromDips(10));
                //expListView.setIndicatorBoundsRelative(width - GetPixelFromDips(50), width - GetPixelFromDips(10));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int GetPixelFromDips(float pixels) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;

        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }

    private String getPermitDate(String dateType, Permit permit) {
        try {
            if (dateType.equalsIgnoreCase(BEGIN_DATE)) {
                return StringUtil.getDisplayString(DateUtil.getStringFromDate(permit.getBeginDate()));
            } else {
                return StringUtil.getDisplayString(DateUtil.getStringFromDate(permit.getEndDate()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private String getVehicleTitleByType(String type, String code) {
        try {
            if (type.equalsIgnoreCase(COLOR)) {
                Color color = Color.getColorByCode(code);
                if (color != null) {
                    return color.getTitle();
                }
            }
            if (type.equalsIgnoreCase(MAKE)) {
                MakeAndModel make = MakeAndModel.getMakeByCode(code);
                if (make != null) {
                    return make.getMakeTitle();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
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
