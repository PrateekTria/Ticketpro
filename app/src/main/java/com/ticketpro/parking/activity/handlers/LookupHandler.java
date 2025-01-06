package com.ticketpro.parking.activity.handlers;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.ticketpro.api.ApiRequest;
import com.ticketpro.api.RetrofitServiceGenerator;
import com.ticketpro.api.ServiceGeneratorCubTrac;
import com.ticketpro.api.ServiceGeneratorJson001;
import com.ticketpro.api.ServiceGeneratorPass2;
import com.ticketpro.api.ServiceOffstreet;
import com.ticketpro.api.TokenGenerate;
import com.ticketpro.exception.TPException;
import com.ticketpro.model.Address;
import com.ticketpro.model.ArrayOfValidParkingData;
import com.ticketpro.model.Body;
import com.ticketpro.model.Color;
import com.ticketpro.model.CustomerInfo;
import com.ticketpro.model.Datum;
import com.ticketpro.model.Feature;
import com.ticketpro.model.Hotlist;
import com.ticketpro.model.LotwiseApi;
import com.ticketpro.model.MakeAndModel;
import com.ticketpro.model.Meter;
import com.ticketpro.model.MeterHandler;
import com.ticketpro.model.MobileNowLog;
import com.ticketpro.model.ParkeonZoneInfo;
import com.ticketpro.model.Permit;
import com.ticketpro.model.PermitHandler;
import com.ticketpro.model.PlateLookupResult;
import com.ticketpro.model.SearchVinHistoryHandler;
import com.ticketpro.model.State;
import com.ticketpro.model.Ticket;
import com.ticketpro.model.TicketHistory;
import com.ticketpro.model.TicketViolation;
import com.ticketpro.model.User;
import com.ticketpro.model.UserSetting;
import com.ticketpro.model.ValidParkingData1;
import com.ticketpro.model.Vendor;
import com.ticketpro.model.VendorItem;
import com.ticketpro.model.VendorService;
import com.ticketpro.model.VendorServiceConfig;
import com.ticketpro.model.Violation;
import com.ticketpro.parking.R;
import com.ticketpro.parking.activity.PlateLookupResultAdvance;
import com.ticketpro.parking.activity.TPApplication;
import com.ticketpro.parking.activity.WriteTicketActivity;
import com.ticketpro.parking.api.WriteTicketNetworkCalls;
import com.ticketpro.parking.bl.WriteTicketBLProcessor;
import com.ticketpro.parking.service.TPAsyncTask;
import com.ticketpro.util.AsyncCallback;
import com.ticketpro.util.CSVUtility;
import com.ticketpro.util.DateUtil;
import com.ticketpro.util.MobileNowParser;
import com.ticketpro.util.Preference;
import com.ticketpro.util.StringUtil;
import com.ticketpro.util.TPConstant;
import com.ticketpro.util.TPTask;
import com.ticketpro.util.TPUtility;
import com.ticketpro.vendors.CachedResult;
import com.ticketpro.vendors.IPSLotDetails;
import com.ticketpro.vendors.IPSParkingMeter;
import com.ticketpro.vendors.IPSParkingPlate;
import com.ticketpro.vendors.IPSParkingSpace;
import com.ticketpro.vendors.IPSParser;
import com.ticketpro.vendors.IPSPlateDetails;
import com.ticketpro.vendors.IPSQuery;
import com.ticketpro.vendors.ParkMobileParkingRight;
import com.ticketpro.vendors.ParkMobileParser;
import com.ticketpro.vendors.ParkMobileZoneInfo;
import com.ticketpro.vendors.ParkeonControlGroup;
import com.ticketpro.vendors.ParkingExpireInfo;
import com.ticketpro.vendors.PassportParkingParser;
import com.ticketpro.vendors.PassportParkingSpace;
import com.ticketpro.vendors.PassportParkingVehicle;
import com.ticketpro.vendors.PayByPhoneParking;
import com.ticketpro.vendors.PayByPhoneParser;
import com.ticketpro.vendors.PayByPhonePlateParser;
import com.ticketpro.vendors.SamtransZoneActivity;
import com.ticketpro.vendors.cubtrack.cbt_model.CubTracZone;
import com.ticketpro.vendors.cubtrack.cbt_model.CubtracRequest;
import com.ticketpro.vendors.cubtrack.cbt_model.CubtracResponse;
import com.ticketpro.vendors.dpt.PlateInfoService.IWsdl2CodeEvents;
import com.ticketpro.vendors.dpt.PlateInfoService.PlateInfoByPlateRequest;
import com.ticketpro.vendors.dpt.PlateInfoService.PlateInfoByPlateResponse;
import com.ticketpro.vendors.dpt.PlateInfoService.PlateInfoByRegionRequest;
import com.ticketpro.vendors.dpt.PlateInfoService.PlateInfoService;
import com.ticketpro.vendors.dpt.PlateInfoService.PlateInfoType;
import com.ticketpro.vendors.dpt.PlateInfoService.VectorPlateInfoType;
import com.ticketpro.vendors.offstreet.OffStreet;
import com.ticketpro.vendors.offstreet.OffStreetList;
import com.ticketpro.vendors.offstreet.OffstreetReqest;
import com.ticketpro.vendors.passport2_model.PP2Plate;
import com.ticketpro.vendors.passport2_model.Passport2Array;
import com.ticketpro.vendors.passport2_model.Passport2Data;
import com.ticketpro.vendors.passport2_model.PlateResponse;
import com.ticketpro.vendors.passport2_model.TokenRequest;
import com.ticketpro.vendors.passport2_model.PP2TokenResponse;
import com.ticketpro.vendors.progressive.Service1;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.StringReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LookupHandler extends Handler implements MeterHandler, PermitHandler, SearchVinHistoryHandler {

    final String RESPONSE_DATA = "responseData";
    final int LOOKUP_CS_PLATE = 1;
    final int LOOKUP_CS_PERMIT = 2;
    final int LOOKUP_PLATE_CHECK = 3;
    final int LOOKUP_MOBILE_NOW_PLATE = 4;
    final int LOOKUP_MOBILE_NOW_SPACE = 5;
    final int LOOKUP_EDMUNDS_VIN = 6;
    final int LOOKUP_PASSPORT_PARKING_PLATE = 7;
    final int LOOKUP_PASSPORT_PARKING_SPACE = 8;
    final int LOOKUP_PARK_MOBILE_PLATE = 9;
    final int LOOKUP_PARK_MOBILE_SPACE = 10;
    final int LOOKUP_PAYBYPHONE_PLATE = 11;
    final int LOOKUP_PAYBYPHONE_SPACE = 12;
    final int LOOKUP_DIGITALPAYTECH_PLATE = 13;
    final int LOOKUP_DIGITALPAYTECH_SPACE = 14;
    final int LOOKUP_IPS_PLATE = 15;
    final int LOOKUP_IPS_SPACE = 16;
    final int LOOKUP_PROGRESSIVE_PLATE = 17;
    private final WriteTicketActivity activity;
    private final Preference preference;
    private final Logger logpm = Logger.getLogger("LookupHandler");
    private String tickets;
    private String amount;
    private String CSPlateNumber;
    private CheckBox acceptDetails;
    private CheckBox acceptPermit;
    private TPApplication TPApp;
    private Dialog lookupDialog;
    private PlateInfoByPlateResponse plateInfoResponse = null;
    private VectorPlateInfoType vectorPlateInfoType = null;
    private Queue<AsyncCallback> plateLookupQueue;
    private Queue<AsyncCallback> spaceLookupQueue;
    private ProgressDialog progressDialog;
    private String samtranSpaceNumber;
    private AsyncTask<Void, Void, Void> task;
    private String plate;
    private boolean platelookup = false;
    private boolean parkmobile = false;
    private boolean flag = true;
    private Meter historyMeter;

    private String serviceName = "";

    public LookupHandler(WriteTicketActivity activity) {
        this.activity = activity;
        this.TPApp = TPApplication.getInstance();
        preference = Preference.getInstance(activity);
    }

    public static String getCharacterDataFromElement(Element e) {
        Node child = e.getFirstChild();
        if (child instanceof CharacterData) {
            CharacterData cd = (CharacterData) child;
            return cd.getData();
        }
        return "";
    }

    public TPApplication getTPApp() {
        return TPApp;
    }

    public void setTPApp(TPApplication tPApp) {
        TPApp = tPApp;
    }

    public void handleNetworkStatus(boolean connected, boolean isFastConnection) {
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        super.handleMessage(msg);

        Bundle data = msg.getData();
        String plate = activity.plateNumberEditText.getText().toString();
        String permit = activity.permitEditText.getText().toString();
        String response = data.getString(RESPONSE_DATA);

        switch (msg.what) {
            case LOOKUP_CS_PLATE:
                if (response == null || response.isEmpty()) {
                    processPlateLookupQueue(plate);
                    return;
                }
                displayCSLookupResult(response, plate);
                break;

            case LOOKUP_CS_PERMIT:
                if (response == null || response.isEmpty()) {
                    doPermitLookup(permit);
                    return;
                }
                displayCSLookupPermit(response, permit);
                break;

            case LOOKUP_PLATE_CHECK:
                displayPlateCheckMsg(response);
                break;

            case LOOKUP_MOBILE_NOW_PLATE:
                displayMobileNowPlateMsg(plate, response);
                break;

            case LOOKUP_MOBILE_NOW_SPACE:
                if (response != null) {
                    if (!response.equals("")) {
                        displayMobileNowPlateMsg(plate, response);
                    } else {
                        processSpaceLookupQueue();
                    }
                } else {
                    processSpaceLookupQueue();
                }
                break;

            case LOOKUP_EDMUNDS_VIN:
                String vin = TPUtility.getValidVIN(activity.vinNumberEditText.getText().toString());
                displayEdmundsVINLookupMsg(vin, response);
                break;

            case LOOKUP_PASSPORT_PARKING_PLATE:
                PassportParkingVehicle vehicleInfo = PassportParkingParser.getVehicleInfoByPlateResponse(response);
                if (vehicleInfo != null) {
                    displayVehicleInfoMsg(vehicleInfo, plate);
                } else {
                    processPlateLookupQueue(plate);
                }
                break;

            case LOOKUP_PASSPORT_PARKING_SPACE:
                if (response != null) {
                    if (!response.equals("")) {
                        displaySpaceInfoMsg(PassportParkingParser.getSpaceInfo(response));
                    } else {
                        processSpaceLookupQueue();
                    }
                } else {
                    processSpaceLookupQueue();
                }
                break;

            case LOOKUP_PARK_MOBILE_PLATE:
                if (Feature.isFeatureAllowed(Feature.PARK_SEARCH_TRACK)) {
                    logpm.info("LOOKUP_PARK_MOBILE_PLATE");
                }
                ArrayList<ParkMobileParkingRight> parkings = ParkMobileParser.getParkingRights(response);
                if (parkings.size() > 0) {
                    if (parkings.size() == 1) {
                        displayParkingInfoMsg(parkings.get(0), plate, false);
                    } else {
                        displayParkMobileParkings(parkings, plate);
                    }
                } else {
                    try {
                        if (Feature.isFeatureAllowed(Feature.PARK_SEARCH_TRACK)) {
                            logpm.info("__getDataFromCache(plate) executed");
                        }
                        __getDataFromCache(plate);
                    } catch (Exception e) {
                        processPlateLookupQueue(plate);
                        e.printStackTrace();
                    }
                }
                break;

            case LOOKUP_PARK_MOBILE_SPACE:
                String space = data.getString("SPACE");
                String zone = data.getString("ZONE");
                if (response != null) {
                    if (!response.equals("")) {
                        displayParkMobileParkings(ParkMobileParser.getParkingRights(response), zone, space);
                    } else {
                        processSpaceLookupQueue();
                    }
                } else {
                    processSpaceLookupQueue();
                }
                break;

            case LOOKUP_PAYBYPHONE_PLATE:
                //logpm.info("Response "+response);
                if (response.equals("Read timed out")) {
                    processPlateLookupQueue(plate);
                    return;
                }
                try {
                    PayByPhonePlateParser parser = new Gson().fromJson(response, PayByPhonePlateParser.class);
                    if (parser.getParkingEntitlements() != null && parser.getParkingEntitlements().size() > 0) {
                        ArrayList<PayByPhoneParking> results = PayByPhoneParser.getParkings(parser.getParkingEntitlements().get(0));
                        if (results.size() == 1) {
                            displayPayByPhoneInfoMsg(results.get(0), plate);
                        } else {
                            displayPayByPhoneParkings(results, plate, true);
                        }
                    } else {
                        processPlateLookupQueue(plate);
                    }
                } catch (Exception e) {
                    processPlateLookupQueue(plate);
                    e.printStackTrace();
                }
                break;

            case LOOKUP_PAYBYPHONE_SPACE:
                String spaceNumber = activity.spaceEditText.getText().toString();
                if (response != null) {
                    if (!response.equals("")) {
                        displayPayByPhoneParkings(PayByPhoneParser.getParkings(response), spaceNumber, false);
                    } else {
                        processSpaceLookupQueue();
                    }
                } else {
                    processSpaceLookupQueue();
                }
                break;

            case LOOKUP_DIGITALPAYTECH_PLATE:
                try {
                    if (plateInfoResponse != null && plateInfoResponse.plateInfo != null) {
                        displayDigitalPayTechInfoMsg(plateInfoResponse.plateInfo, plate);
                    } else {
                        processPlateLookupQueue(plate);
                    }
                } catch (Exception e) {
                    processPlateLookupQueue(plate);
                    e.printStackTrace();
                }
                break;

            case LOOKUP_DIGITALPAYTECH_SPACE:
                String spaceStr = activity.spaceEditText.getText().toString();
                if (vectorPlateInfoType != null) {
                    Enumeration<PlateInfoType> enumParking = vectorPlateInfoType.elements();
                    ArrayList<PlateInfoType> parkingList = new ArrayList<>();
                    if (enumParking.hasMoreElements()) {
                        parkingList.add(enumParking.nextElement());
                    }

                    if (parkingList.size() > 0) {
                        displayDigitalPayTechParkings(parkingList, spaceStr, false);
                    } else {
                        processSpaceLookupQueue();
                    }
                } else {
                    processSpaceLookupQueue();
                }
                break;

            case LOOKUP_IPS_PLATE:
                ArrayList<IPSParkingPlate> result = IPSParser.getPlateStatus(response);
                if (result != null && result.size() > 0) {
                    displayIPSInfoMsg(result, plate);
                } else {
                    processPlateLookupQueue(plate);
                }
                break;

            case LOOKUP_IPS_SPACE:
                if (response != null) {
                    if (!response.equals("")) {
                        displayIPSParkings(IPSParser.getSpaceStatus(response));
                    } else {
                        processSpaceLookupQueue();
                    }
                } else {
                    processSpaceLookupQueue();
                }
                break;

            case LOOKUP_PROGRESSIVE_PLATE:
                boolean validPermit = data.getBoolean("IsValidPermit", false);
                displayProgressiveResponse(validPermit, plate);
                break;

            default:
                break;
        }
    }

    private void __getDataFromCache(String plate) throws Exception {
        String zoneCode = preference.getString("ZoneCode");
        int zone_code = 0;
        try {
            if (zoneCode != null && !zoneCode.equalsIgnoreCase("null")) {
                zone_code = Integer.parseInt(zoneCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
            zone_code = 0;
        }

        try {
            String key = "ParkMobile-" + zone_code;
            CachedResult cachedResult = (CachedResult) TPApp.cachedMap.getResults(key);
            if (cachedResult != null && !cachedResult.hasExpired()) {
                ArrayList<ParkMobileParkingRight> results = (ArrayList<ParkMobileParkingRight>) cachedResult.getResults();
                ArrayList<ParkMobileParkingRight> arrayList = new ArrayList<>();
                for (ParkMobileParkingRight ppr : results) {
                    if (ppr.getLpn().equals(plate)) {
                        ParkMobileParkingRight pprs = new ParkMobileParkingRight();
                        pprs.setProductDescription(ppr.getProductDescription());
                        pprs.setLpn(ppr.getLpn());
                        pprs.setCustomField1(ppr.getCustomField1());
                        pprs.setSignageZoneCode(ppr.getSignageZoneCode());
                        pprs.setSpaceNumber(ppr.getSpaceNumber());
                        pprs.setPermit(ppr.getPermit());
                        pprs.setLpnState(ppr.getLpnState());
                        pprs.setPayedMinutes(ppr.getPayedMinutes());
                        pprs.setEndDateLocal(ppr.getEndDateLocal());
                        pprs.setCreationDate(ppr.getCreationDate());
                        pprs.setTimeZone(ppr.getTimeZone());
                        arrayList.add(pprs);
                        platelookup = true;
                        if (Feature.isFeatureAllowed(Feature.PARK_SEARCH_TRACK)) {

                            logpm.info("displayParkingInfoMsg from cacheBlock");
                        }
                        displayParkingInfoMsg(pprs, plate, false);
                        break;
                    }
                }
                if (!platelookup) {
                    processPlateLookupQueue(plate);
                    platelookup = false;
                }
            } else {
                if (Feature.isFeatureAllowed(Feature.PARK_SEARCH_TRACK)) {
                    logpm.info("processPlateLookupQueue from cache Block");
                }//6eqk270

                if ((Feature.isFeatureAllowed(Feature.PARK_PLATE_SEARCH_ZERO_OR_O)) && plate.contains("O") || plate.contains("0")) {

                    String plate2 = "";
                    if (plate.contains("O")) {
                        plate2 = plate.replace("O", "0");
                    } else if (plate.contains("0")) {
                        plate2 = plate.replace("0", "O");
                    } else {
                        plate2 = plate;
                    }
                    if (flag) {
                        lookupParkMobile(plate2);
                        flag = false;
                    } else {
                        processPlateLookupQueue(plate);
                    }
                } else {
                    processPlateLookupQueue(plate);
                }

            }
        } catch (Exception e) {
            logpm.info("Exception in cacheBlock");
            e.printStackTrace();
            processPlateLookupQueue(plate);
        }
    }

    private void processSpaceLookupQueue() {
        AsyncCallback callback = spaceLookupQueue.poll();
        if (callback != null) {
            callback.execute();
        }
        if (spaceLookupQueue.size() == 1) {
            enableBack();
        }
    }

    void disableBack() {
        activity.runOnUiThread(() -> {
            try {
                WriteTicketActivity.plateLookup = false;
                WriteTicketActivity.backBtn.setBackgroundResource(R.drawable.btn_disabled);
                WriteTicketActivity.backBtn.setEnabled(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    void enableBack() {
        activity.runOnUiThread(() -> {
            try {
                WriteTicketActivity.plateLookup = true;
                WriteTicketActivity.backBtn.setBackgroundResource(R.drawable.btn_yellow);
                WriteTicketActivity.backBtn.setEnabled(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void processPlateLookupQueue(String plate) {
        if (plateLookupQueue == null || plateLookupQueue.isEmpty()) {
           /* if (Feature.isFeatureAllowed(Feature.PARK_SEARCH_TRACK)) {
                logpm.info("doPlateLookup queue empty/null");
            }*/
            doPlateLookup(plate);
        } else {
            AsyncCallback callback = plateLookupQueue.poll();
            if (callback != null) {
                callback.execute();
            } else {
               /* if (Feature.isFeatureAllowed(Feature.PARK_SEARCH_TRACK)) {
                    logpm.info("doPlateLookup callback null");
                }*/
                doPlateLookup(plate);
            }
        }
    }

    public void displayCSLookupResult(final String response, final String plate) {
        //if (TPUtility.isActivityRunning(activity)) {
        activity.runOnUiThread(() -> {
            StringBuilder msg = new StringBuilder();
            StringBuilder values = new StringBuilder();

            String permitValue = null;
            StringTokenizer st = new StringTokenizer(response, "&");
            while (st.hasMoreTokens()) {
                String str = st.nextToken();
                if (str.length() > 0) {
                    String[] tokens = str.split("=");
                    if (tokens.length >= 2) {
                        if (tokens[0].equalsIgnoreCase("RESULT") && tokens[1].equalsIgnoreCase("ERROR")) {
                            processPlateLookupQueue(plate);
                            return;
                        }

                        if (tokens[0].equals("SHAHASH")) {
                            continue;
                        }

                        if (tokens[0].equals("PERMITTYPE")) {
                            tokens[0] = "TYPE";
                        } else if (tokens[0].contains("VEHBODY")) {
                            Body body = Body.getBodyByCode(tokens[1]);
                            if (body != null) {
                                tokens[1] = body.getTitle();
                            }
                        } else if (tokens[0].contains("VEHMAKE")) {
                            MakeAndModel make = MakeAndModel.getMakeByCode(tokens[1]);
                            if (make != null) {
                                tokens[1] = make.getMakeTitle();
                            }
                        } else if (tokens[0].contains("VEHMODEL")) {
                            MakeAndModel make = MakeAndModel.getModelByCode(tokens[1]);
                            if (make != null) {
                                tokens[1] = make.getModelTitle();
                            }
                        } else if (tokens[0].contains("VEHCOLOR")) {
                            Color color = Color.getColorByCode(tokens[1]);
                            if (color != null) {
                                tokens[1] = color.getTitle();
                            }
                        } else if (tokens[0].equals("PERMIT")) {
                            permitValue = tokens[1];
                        }

                        msg.append(tokens[0]).append("\n");
                        values.append(": ").append(StringUtil.getDisplayString(tokens[1])).append("\n");

                    }
                }
            }

            acceptDetails = new CheckBox(activity);
            acceptDetails.setText(" Use\n Vehicle Info");

            acceptPermit = new CheckBox(activity);
            acceptPermit.setText(" Use\n Permit ");

            LinearLayout parentLayout = new LinearLayout(activity);
            parentLayout.setOrientation(LinearLayout.VERTICAL);

            RelativeLayout relativeLayout = new RelativeLayout(activity);

            RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            lp1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

            RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            lp2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

            relativeLayout.addView(acceptDetails, lp1);

            // Add checkbox to accept permits
            if (permitValue != null && permitValue.length() > 0) {
                relativeLayout.addView(acceptPermit, lp2);
                acceptPermit.setVisibility(View.GONE);

                // Show Only if use vehicle info is checked
                acceptDetails.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) {
                        acceptPermit.setVisibility(View.VISIBLE);
                    } else {
                        acceptPermit.setVisibility(View.GONE);
                    }
                });
            }
            View view = LayoutInflater.from(activity.getBaseContext()).inflate(R.layout.item_view, null);
            TextView headerTV = view.findViewById(R.id.headerTV);
            TextView valueTV = view.findViewById(R.id.valueTV);
            headerTV.setText(msg.toString());
            valueTV.setText(values.toString());

            parentLayout.addView(view);
            parentLayout.addView(relativeLayout);

            new Builder(activity).setCancelable(false).setView(parentLayout).setTitle("CSLookup Result")
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.cancel()).setPositiveButton("Continue Lookup", (dialog, which) -> {
                        dialog.dismiss();

                        if (acceptDetails.isChecked()) {
                            populateCSLookupDetails(response, acceptPermit.isChecked());
                        }
                        processPlateLookupQueue(plate);
                    }).show();
        });
        //}
    }

    public void displayParkMobileParkings(final ArrayList<ParkMobileParkingRight> parkings, final String zoneCode, final String space) {
        //if (TPUtility.isActivityRunning(activity)) {
        activity.runOnUiThread(() -> {
            try {
                final Dialog dialog = new Dialog(activity);
                dialog.setContentView(R.layout.parking_list);
                dialog.getWindow().getAttributes().width = ViewGroup.LayoutParams.MATCH_PARENT;

                TableLayout tableLayout = dialog.findViewById(R.id.logs_4_table_view);
                tableLayout.removeAllViews();

                LayoutInflater inflater = LayoutInflater.from(activity.getBaseContext());
                View headerRow = inflater.inflate(R.layout.table_row_parking, null);

                TextView plateColumn = headerRow.findViewById(R.id.tr_header1);
                plateColumn.setText("LPN");

                TextView infoColumn = headerRow.findViewById(R.id.tr_header2);
                infoColumn.setText("Zone");

                TextView statusColumn = headerRow.findViewById(R.id.tr_header3);
                statusColumn.setText("Expire");
                tableLayout.addView(headerRow);

                if (parkings == null || parkings.isEmpty()) {
                    plateColumn.setText("No records found");
                    infoColumn.setVisibility(View.GONE);
                    statusColumn.setVisibility(View.GONE);
                }

                int index = 0;
                if (parkings != null) {
                    for (ParkMobileParkingRight item : parkings) {
                        if (item.getSpaceNumber() == null || !item.getSpaceNumber().equalsIgnoreCase(space)) {
                            continue;
                        }

                        View tableRow = inflater.inflate(R.layout.table_row_parking, null);

                        ImageView statusIcon = tableRow.findViewById(R.id.tr_status_img);
                        statusIcon.setBackgroundResource(R.drawable.color_green);

                        ImageView actionIcon = tableRow.findViewById(R.id.tr_action);
                        actionIcon.setBackgroundResource(R.drawable.info);
                        actionIcon.setVisibility(View.GONE);

                        ParkingExpireInfo expireInfo = item.getExpireInfo();
                        if (expireInfo.isExpired()) {
                            statusIcon.setBackgroundResource(R.drawable.color_red);
                            ((TextView) tableRow.findViewById(R.id.tr_header1))
                                    .setTextColor(android.graphics.Color.RED);
                            ((TextView) tableRow.findViewById(R.id.tr_header2))
                                    .setTextColor(android.graphics.Color.RED);
                            ((TextView) tableRow.findViewById(R.id.tr_header3))
                                    .setTextColor(android.graphics.Color.RED);
                        } else if (expireInfo.getDiffMinutes() <= 3 && expireInfo.getDiffHrs() == 0
                                && expireInfo.getDiffDays() == 0) {
                            statusIcon.setBackgroundResource(R.drawable.color_yellow);
                        }

                        ((TextView) tableRow.findViewById(R.id.tr_header1)).setText(item.getLpn());
                        ((TextView) tableRow.findViewById(R.id.tr_header2)).setText(item.getInternalZoneCode());
                        ((TextView) tableRow.findViewById(R.id.tr_header3)).setText(expireInfo.getExpireMsg());

                        if ((index % 2) == 0)
                            tableRow.setBackgroundResource(R.drawable.tablerow_even);
                        else
                            tableRow.setBackgroundResource(R.drawable.tablerow_odd);

                        tableLayout.addView(tableRow);
                        index++;
                    }
                }

                Button closeButton = dialog.findViewById(R.id.close_btn);
                closeButton.setOnClickListener(v -> dialog.dismiss());

                Button continueButton = dialog.findViewById(R.id.continue_btn);
                continueButton.setVisibility(View.GONE);

                dialog.setCancelable(false);
                dialog.setTitle("Zone Info - " + zoneCode + "/" + space);
                dialog.show();

            } catch (Exception e) {
                Log.e(activity.getClass().getSimpleName(), TPUtility.getPrintStackTrace(e));
            }
        });
        //}
    }

    public void displayParkMobileParkings(final ArrayList<ParkMobileParkingRight> parkings, final String plate) {
        //if (TPUtility.isActivityRunning(activity)) {
        activity.runOnUiThread(() -> {
            try {
                final Dialog dialog = new Dialog(activity);
                dialog.setContentView(R.layout.parking_list);
                dialog.getWindow().getAttributes().width = ViewGroup.LayoutParams.MATCH_PARENT;

                TableLayout tableLayout = dialog.findViewById(R.id.logs_4_table_view);
                tableLayout.removeAllViews();

                LayoutInflater inflater = LayoutInflater.from(activity.getBaseContext());
                View headerRow = inflater.inflate(R.layout.table_row_parking, null);

                TextView plateColumn = headerRow.findViewById(R.id.tr_header1);
                plateColumn.setText("LPN");

                TextView infoColumn = headerRow.findViewById(R.id.tr_header2);
                infoColumn.setText("Space");

                TextView statusColumn = headerRow.findViewById(R.id.tr_header3);
                statusColumn.setText("Expire");
                tableLayout.addView(headerRow);

                if (parkings == null || parkings.size() == 0) {
                    plateColumn.setText("No records found");
                    infoColumn.setVisibility(View.GONE);
                    statusColumn.setVisibility(View.GONE);
                }

                int index = 0;
                if (parkings != null) {
                    for (ParkMobileParkingRight item : parkings) {
                        View tableRow = inflater.inflate(R.layout.table_row_parking, null);

                        ImageView statusIcon = tableRow.findViewById(R.id.tr_status_img);
                        statusIcon.setBackgroundResource(R.drawable.color_green);

                        ImageView actionIcon = tableRow.findViewById(R.id.tr_action);
                        actionIcon.setBackgroundResource(R.drawable.info);
                        actionIcon.setVisibility(View.GONE);

                        ParkingExpireInfo expireInfo = item.getExpireInfo();
                        if (expireInfo.isExpired()) {
                            statusIcon.setBackgroundResource(R.drawable.color_red);
                            ((TextView) tableRow.findViewById(R.id.tr_header1))
                                    .setTextColor(android.graphics.Color.RED);
                            ((TextView) tableRow.findViewById(R.id.tr_header2))
                                    .setTextColor(android.graphics.Color.RED);
                            ((TextView) tableRow.findViewById(R.id.tr_header3))
                                    .setTextColor(android.graphics.Color.RED);
                        } else if (expireInfo.getDiffMinutes() <= 3 && expireInfo.getDiffHrs() == 0
                                && expireInfo.getDiffDays() == 0) {
                            statusIcon.setBackgroundResource(R.drawable.color_yellow);
                        }

                        ((TextView) tableRow.findViewById(R.id.tr_header1)).setText(item.getLpn());
                        ((TextView) tableRow.findViewById(R.id.tr_header2)).setText(item.getSpaceNumber());
                        ((TextView) tableRow.findViewById(R.id.tr_header3)).setText(expireInfo.getExpireMsg());

                        if ((index % 2) == 0) {
                            tableRow.setBackgroundResource(R.drawable.tablerow_even);
                        } else {
                            tableRow.setBackgroundResource(R.drawable.tablerow_odd);
                        }
                        tableRow.setOnClickListener(v -> {
                            displayParkingInfoMsg(item, plate, true);
                        });

                        tableLayout.addView(tableRow);
                        index++;
                    }
                }

                Button closeButton = dialog.findViewById(R.id.close_btn);
                closeButton.setOnClickListener(v -> dialog.dismiss());

                Button continueButton = dialog.findViewById(R.id.continue_btn);
                continueButton.setOnClickListener(v -> {
                    dialog.dismiss();
                    processPlateLookupQueue(plate);
                });

                dialog.setCancelable(false);
                dialog.setTitle("Parking - " + plate);
                dialog.show();

            } catch (Exception e) {
                Log.e("TicketPRO", "Passport Zone Info " + e.getMessage());
            }
        });
        //}
    }

    public void displayParkingInfoMsg(final ParkMobileParkingRight parking, final String plate, boolean skipLookup) {
        //if (TPUtility.isActivityRunning(activity)) {
        activity.runOnUiThread(() -> {
            StringBuilder message = new StringBuilder();
            StringBuilder buffer = new StringBuilder();
            View view = LayoutInflater.from(activity.getBaseContext()).inflate(R.layout.item_view, null);
            TextView headerTV = view.findViewById(R.id.headerTV);
            TextView valueTV = view.findViewById(R.id.valueTV);

            message.append("Name" + "\n");

            message.append("Zone" + "\n");

            message.append("Space" + "\n");

            message.append("Plate" + "\n");

            message.append("Permit" + "\n");

            message.append("State" + "\n");

            message.append("Payed Mins" + "\n");

            message.append("Expire" + "\n");
            message.append("Start Date" + "\n");
            message.append("End Date" + "\n");

            headerTV.setText(message.toString());

            buffer.append(StringUtil.getDisplayString(parking.getProductDescription())).append("\n");

            buffer.append(StringUtil.getDisplayString(parking.getCustomField1() + "-" + parking.getSignageZoneCode())).append("\n");

            buffer.append(StringUtil.getDisplayString(parking.getSpaceNumber())).append("\n");

            buffer.append(StringUtil.getDisplayString(parking.getLpn())).append("\n");

            buffer.append(StringUtil.getDisplayString(parking.getPermit())).append("\n");

            buffer.append(StringUtil.getDisplayString(parking.getLpnState())).append("\n");

            buffer.append(StringUtil.getDisplayString("" + parking.getPayedMinutes())).append("\n");

            buffer.append(StringUtil.getDisplayString(parking.getExpireInfo().getExpireMsg())).append("\n");
            buffer.append(StringUtil.getDisplayString(DateUtil.getStringFromDate2(parking.getStartDateLocal()))).append("\n");
            buffer.append(StringUtil.getDisplayString(DateUtil.getStringFromDate2(parking.getEndDateLocal()))).append("\n");

            valueTV.setText(buffer.toString());

            Builder dialog = new Builder(activity);
            dialog.setCancelable(false);
            dialog.setView(view);
            dialog.setTitle("Parking Info");
            dialog.setPositiveButton("Close", (dialog12, which) -> dialog12.dismiss());

            if (!skipLookup) {
                dialog.setNegativeButton("Continue", (dialog1, which) -> {
                    dialog1.dismiss();
                    processPlateLookupQueue(plate);
                });
            }

            TPUtility.applyButtonStyles(dialog.show());
        });
        //}
    }

    // DigitalPayTech display information starts here
    public void displayDigitalPayTechInfoMsg(final PlateInfoType parking, final String plate) {

        activity.runOnUiThread(() -> {
            StringBuilder message = new StringBuilder();
            StringBuilder values = new StringBuilder();

            View view = LayoutInflater.from(activity.getBaseContext()).inflate(R.layout.item_view, null);
            TextView headerTV = view.findViewById(R.id.headerTV);
            TextView valueTV = view.findViewById(R.id.valueTV);
            message.append("Status" + "\n");
            message.append("Region Name" + "\n");
            message.append("Purchased Date" + "\n");
            message.append("Expiry Date" + "\n");

            values.append(": ").append(StringUtil.getDisplayString(parking.status.toString())).append("\n");
            values.append(": ").append(StringUtil.getDisplayString(parking.regionName)).append("\n");
            values.append(": ").append(DateUtil.getStringFromDateShortYear(parking.getPurchasedDate())).append("\n");
            values.append(": ").append(DateUtil.getStringFromDateShortYear(parking.getExpiryDate())).append("\n");
            headerTV.setText(message.toString());
            valueTV.setText(values.toString());

            Builder dialog = new Builder(activity);
            dialog.setCancelable(false);

            dialog.setView(view);
            dialog.setTitle("DigitalPayTech\nPlate Info: " + plate);
            dialog.setPositiveButton("Close", (dialog12, which) -> dialog12.dismiss());

            dialog.setNegativeButton("Continue", (dialog1, which) -> {
                dialog1.dismiss();

                processPlateLookupQueue(plate);
            });

            TPUtility.applyButtonStyles(dialog.show());
        });

    }

    public void displayDigitalPayTechParkings(final ArrayList<PlateInfoType> parkings, final String searchData,
                                              final boolean doPlateLookup) {

        activity.runOnUiThread(() -> {
            try {
                final Dialog dialog = new Dialog(activity);
                dialog.setContentView(R.layout.parking_list);
                dialog.getWindow().getAttributes().width = ViewGroup.LayoutParams.MATCH_PARENT;

                TableLayout tableLayout = dialog.findViewById(R.id.logs_4_table_view);
                tableLayout.removeAllViews();

                LayoutInflater inflater = LayoutInflater.from(activity.getBaseContext());
                View headerRow = inflater.inflate(R.layout.table_row_parking, null);

                TextView plateColumn = headerRow.findViewById(R.id.tr_header1);
                plateColumn.setText("Plate");

                TextView infoColumn = headerRow.findViewById(R.id.tr_header2);
                infoColumn.setText("Purchased Date");

                TextView statusColumn = headerRow.findViewById(R.id.tr_header3);
                statusColumn.setText("Expiry");
                tableLayout.addView(headerRow);

                if (parkings == null || parkings.size() == 0) {
                    plateColumn.setText("No records found");
                    infoColumn.setVisibility(View.GONE);
                    statusColumn.setVisibility(View.GONE);
                }

                int index = 0;
                if (parkings != null) {
                    for (PlateInfoType item : parkings) {
                        View tableRow = inflater.inflate(R.layout.table_row_parking, null);

                        ImageView statusIcon = tableRow.findViewById(R.id.tr_status_img);
                        statusIcon.setBackgroundResource(R.drawable.color_green);

                        ImageView actionIcon = tableRow.findViewById(R.id.tr_action);
                        actionIcon.setBackgroundResource(R.drawable.info);
                        actionIcon.setVisibility(View.GONE);

                        ParkingExpireInfo expireInfo = item.getExpireInfo();
                        if (expireInfo.isExpired()) {
                            statusIcon.setBackgroundResource(R.drawable.color_red);
                            ((TextView) tableRow.findViewById(R.id.tr_header1))
                                    .setTextColor(android.graphics.Color.RED);
                            ((TextView) tableRow.findViewById(R.id.tr_header2))
                                    .setTextColor(android.graphics.Color.RED);
                            ((TextView) tableRow.findViewById(R.id.tr_header3))
                                    .setTextColor(android.graphics.Color.RED);
                        } else if (expireInfo.getDiffMinutes() <= 3 && expireInfo.getDiffHrs() == 0
                                && expireInfo.getDiffDays() == 0) {
                            statusIcon.setBackgroundResource(R.drawable.color_yellow);
                        }

                        ((TextView) tableRow.findViewById(R.id.tr_header1)).setText(item.plateNumber);
                        ((TextView) tableRow.findViewById(R.id.tr_header2))
                                .setText(DateUtil.getStringFromDateShortYear(item.getPurchasedDate()));
                        ((TextView) tableRow.findViewById(R.id.tr_header3)).setText(expireInfo.getExpireMsg());

                        if ((index % 2) == 0) {
                            tableRow.setBackgroundResource(R.drawable.tablerow_even);
                        } else {
                            tableRow.setBackgroundResource(R.drawable.tablerow_odd);
                        }

                        tableLayout.addView(tableRow);
                        index++;
                    }
                }

                Button closeButton = dialog.findViewById(R.id.close_btn);
                closeButton.setOnClickListener(v -> dialog.dismiss());

                Button continueButton = dialog.findViewById(R.id.continue_btn);
                continueButton.setOnClickListener(v -> {
                    dialog.dismiss();

                    if (doPlateLookup) {
                        processPlateLookupQueue(searchData);
                    }
                });

                dialog.setCancelable(false);
                dialog.setTitle("DigitalPayTech\nParking - " + searchData);
                dialog.show();

            } catch (Exception e) {
                Log.e("TicketPRO", "DigitalPaytech Zone Info " + e.getMessage());
            }
        });

    }

    // IPS Group display information starts here
    public void displayIPSInfoMsg(final ArrayList<IPSParkingPlate> parking, final String plate) {
        //if (TPUtility.isActivityRunning(activity)) {
        activity.runOnUiThread(() -> {
            StringBuilder message = new StringBuilder();
            StringBuilder values = new StringBuilder();

            View view = LayoutInflater.from(activity.getBaseContext()).inflate(R.layout.item_view, null);
            TextView headerTV = view.findViewById(R.id.headerTV);
            TextView valueTV = view.findViewById(R.id.valueTV);

            for (IPSParkingPlate parkingPlate : parking) {
                message.append("Start Time" + "\n");
                message.append("End Time" + "\n");
                message.append("Remaining" + "\n");
                message.append("SubArea" + "\n");
                message.append("\n");

                values.append(": ").append(StringUtil.getDisplayString(parkingPlate.getParkingStartTime())).append("\n");
                values.append(": ").append(StringUtil.getDisplayString(parkingPlate.getParkingExpiryTime())).append("\n");
                values.append(": ").append(StringUtil.getDisplayString(parkingPlate.getExpireInfo(DateUtil.getDateFromIPS(parkingPlate.getParkingExpiryTime())).getExpireMsg())).append("\n");
                values.append(": ").append(StringUtil.getDisplayString(parkingPlate.getSubAreaName())).append("\n");
                values.append("\n");
                headerTV.setText(message.toString());
                valueTV.setText(values.toString());
            }

            Builder dialog = new Builder(activity);
            dialog.setCancelable(false);
            dialog.setView(view);
            dialog.setTitle("IPS\nPlate Info - " + StringUtil.getDisplayString(parking.get(0).getLicensePlateNumber()));
            dialog.setPositiveButton("Close", (dialog12, which) -> {
                activity.locationEditText.setText(parking.get(0).getSubAreaName());
                dialog12.dismiss();
            });

            dialog.setNegativeButton("Continue", (dialog1, which) -> {
                dialog1.dismiss();
                processPlateLookupQueue(plate);
            });

            TPUtility.applyButtonStyles(dialog.show());
        });
        //}
    }
    // IPS display information ends here

    // IPS Parking displayIPSParkings
    public void displayIPSParkings(final IPSParkingSpace parkingSpace) {
        //if (TPUtility.isActivityRunning(activity)) {
        activity.runOnUiThread(() -> {
            try {
                StringBuilder message = new StringBuilder();
                StringBuilder values = new StringBuilder();

                View view = LayoutInflater.from(activity.getBaseContext()).inflate(R.layout.item_view, null);
                TextView headerTV = view.findViewById(R.id.headerTV);
                TextView valueTV = view.findViewById(R.id.valueTV);

                message.append("Space Number" + "\n");

                message.append("Space Group" + "\n");

                message.append("Start Time" + "\n");

                message.append("Expiry Time");

                values.append(": ").append(StringUtil.getDisplayString(parkingSpace.getSpace())).append("\n");

                values.append(": ").append(StringUtil.getDisplayString(parkingSpace.getSpaceGroup())).append("\n");

                if (parkingSpace.getStartDateTime()!=null) {
                    values.append(": ").append(parkingSpace.getStartDateTime().toString().substring(0, 20)).append("\n");
                }else {
                    values.append(": ").append("");
                }

                values.append(": ").append(parkingSpace.getExpiryTime().toString().substring(0, 20));

                headerTV.setText(message.toString());
                valueTV.setText(values.toString());

                Builder dialog = new Builder(activity);
                dialog.setCancelable(false);
                dialog.setView(view);
                dialog.setTitle("IPS\nSpace Info");
                dialog.setPositiveButton("OK", (dialog1, which) -> dialog1.dismiss());

                dialog.setNegativeButton("Change SpaceGroup", (dialog12, which) -> {
                    dialog12.dismiss();
                    selectIPSSpaceGroup(parkingSpace.getSpace(), "spaceGroup");
                });

                dialog.show();

            } catch (Exception e) {
                Log.e("TicketPRO", "IPS Space Info " + e.getMessage());
            }
        });
        //}
    }

    // displayProgressiveResponse
    public void displayProgressiveResponse(final boolean result, final String plate) {
        //if (TPUtility.isActivityRunning(activity)) {
        activity.runOnUiThread(() -> {
            try {
                StringBuilder message = new StringBuilder();
                StringBuilder values = new StringBuilder();

                View view = LayoutInflater.from(activity.getBaseContext()).inflate(R.layout.item_view, null);
                TextView headerTV = view.findViewById(R.id.headerTV);
                TextView valueTV = view.findViewById(R.id.valueTV);
                message.append("Plate" + "\n");
                values.append(": ").append(StringUtil.getDisplayString(plate)).append("\n");
                if (result) {
                    message.append("Result");
                    values.append(": Valid permit");
                } else {
                    message.append("Result");
                    values.append(": No permit found");
                }
                headerTV.setText(message.toString());
                valueTV.setText(values.toString());
                Builder dialog = new Builder(activity);
                dialog.setCancelable(false);
                dialog.setView(view);
                dialog.setTitle("Progressive Status");
                dialog.setPositiveButton("OK", (dialog12, which) -> dialog12.dismiss());
                dialog.setNegativeButton("Continue", (dialog1, which) -> {
                    dialog1.dismiss();
                    processPlateLookupQueue(plate);
                });
                dialog.show();
            } catch (Exception e) {
                Log.e("TicketPRO", "Progressive " + e.getMessage());
            }
        });
        //}
    }

    // IPS Parking displayIPSParkings
    public void displayIPSParkings(final IPSParkingMeter parkingMeter) {
        //if (TPUtility.isActivityRunning(activity)) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    StringBuilder message = new StringBuilder();
                    StringBuilder values = new StringBuilder();
                        /*message.append("<style>body{color:#fff;} table{color:#fff;} td{vertical-align:top;}</style>");
                        message.append("<table>");

                        message.append("<tr><td>Meter Number</td><td>:</td><td>"
                                + StringUtil.getDisplayString(parkingMeter.getMeterNumber()));
                        message.append("</td></tr>");

                        message.append("<tr><td>Start Time</td><td>:</td><td>"
                                + StringUtil.getDisplayString(parkingMeter.getStartDateTime()));
                        message.append("</td></tr>");

                        message.append("<tr><td>Expiry Time</td><td>:</td><td>"
                                + StringUtil.getDisplayString(parkingMeter.getExpiryTime()));
                        message.append("</td></tr>");

                        message.append("</table>");*/

                    message.append("Meter Number" + "\n");

                    message.append("Start Time" + "\n");

                    message.append("Expiry Time" + "\n");

                    values.append(": "
                            + StringUtil.getDisplayString(parkingMeter.getMeterNumber()) + "\n");

                    values.append(": "
                            + StringUtil.getDisplayString(parkingMeter.getStartDateTime()) + "\n");

                    values.append(": "
                            + StringUtil.getDisplayString(parkingMeter.getExpiryTime()) + "\n");
                    View view = LayoutInflater.from(activity.getBaseContext()).inflate(R.layout.item_view, null);
                    TextView headerTV = view.findViewById(R.id.headerTV);
                    TextView valueTV = view.findViewById(R.id.valueTV);
                    headerTV.setText(message.toString());
                    valueTV.setText(values.toString());
                        /*WebView wv = new WebView(activity.getBaseContext());
                        wv.loadData(message.toString(), "text/html", "utf-8");
                        wv.setBackgroundColor(android.graphics.Color.BLACK);
                        wv.getSettings().setDefaultTextEncodingName("utf-8");*/

                    Builder dialog = new Builder(activity);
                    dialog.setCancelable(false);
                    //dialog.setView(wv);
                    dialog.setView(view);
                    dialog.setTitle("IPS\nMeter Info");
                    dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    dialog.setNegativeButton("Continue", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                            checkMeterHistory(parkingMeter.getMeterNumber(), true);
                        }
                    });

                    dialog.show();

                } catch (Exception e) {
                    Log.e("TicketPRO", "IPS Meter Info " + e.getMessage());
                }
            }
        });
        //}
    }

    // PayByPhone display information
    public void displayPayByPhoneInfoMsg(final PayByPhoneParking parking, final String plate) {
        //if (TPUtility.isActivityRunning(activity)) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                StringBuilder message = new StringBuilder();
                StringBuilder values = new StringBuilder();


                View view = LayoutInflater.from(activity.getBaseContext()).inflate(R.layout.item_view, null);
                TextView headerTV = view.findViewById(R.id.headerTV);
                TextView valueTV = view.findViewById(R.id.valueTV);
                message.append("Plate" + "\n");
                message.append("Start Time" + "\n");
                message.append("Expiry Time" + "\n");

                values.append(": " + StringUtil.getDisplayString(parking.getPlate()) + "\n");
                values.append(": " + StringUtil.getDisplayString(DateUtil.getStringFromDate3(parking.getStartDateTime())) + "\n");
                values.append(": "
                        + StringUtil.getDisplayString(DateUtil.getStringFromDate3(parking.getEndDateTime())) + "\n");

                headerTV.setText(message.toString());
                valueTV.setText(values.toString());

                Builder dialog = new Builder(activity);
                dialog.setCancelable(false);
                ///dialog.setView(wv);
                dialog.setView(view);
                dialog.setTitle("PayByPhone\nPlate Info: " + plate);
                dialog.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                dialog.setNegativeButton("Continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        processPlateLookupQueue(plate);
                    }
                });

                TPUtility.applyButtonStyles(dialog.show());
            }
        });
        //}
    }

    public void displayPayByPhoneParkings(final ArrayList<PayByPhoneParking> parkings, final String searchData,
                                          final boolean doPlateLookup) {

        //if (TPUtility.isActivityRunning(activity)) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    final Dialog dialog = new Dialog(activity);
                    dialog.setContentView(R.layout.parking_list);
                    dialog.getWindow().getAttributes().width = ViewGroup.LayoutParams.MATCH_PARENT;

                    TableLayout tableLayout = dialog.findViewById(R.id.logs_4_table_view);
                    tableLayout.removeAllViews();

                    LayoutInflater inflater = LayoutInflater.from(activity.getBaseContext());
                    View headerRow = inflater.inflate(R.layout.table_row_parking, null);

                    TextView plateColumn = headerRow.findViewById(R.id.tr_header1);
                    plateColumn.setText("Plate");

                    TextView infoColumn = headerRow.findViewById(R.id.tr_header2);
                    infoColumn.setText("Stall");

                    TextView statusColumn = headerRow.findViewById(R.id.tr_header3);
                    statusColumn.setText("Expire");
                    tableLayout.addView(headerRow);

                    if (parkings == null || parkings.size() == 0) {
                        plateColumn.setText("No records found");
                        infoColumn.setVisibility(View.GONE);
                        statusColumn.setVisibility(View.GONE);
                    }

                    int index = 0;
                    for (PayByPhoneParking item : parkings) {
                        View tableRow = inflater.inflate(R.layout.table_row_parking, null);

                        ImageView statusIcon = tableRow.findViewById(R.id.tr_status_img);
                        statusIcon.setBackgroundResource(R.drawable.color_green);

                        ImageView actionIcon = tableRow.findViewById(R.id.tr_action);
                        actionIcon.setBackgroundResource(R.drawable.info);
                        actionIcon.setVisibility(View.GONE);

                        ParkingExpireInfo expireInfo = item.getExpireInfo();
                        if (expireInfo.isExpired()) {
                            statusIcon.setBackgroundResource(R.drawable.color_red);
                            ((TextView) tableRow.findViewById(R.id.tr_header1))
                                    .setTextColor(android.graphics.Color.RED);
                            ((TextView) tableRow.findViewById(R.id.tr_header2))
                                    .setTextColor(android.graphics.Color.RED);
                            ((TextView) tableRow.findViewById(R.id.tr_header3))
                                    .setTextColor(android.graphics.Color.RED);
                        } else if (expireInfo.getDiffMinutes() <= 3 && expireInfo.getDiffHrs() == 0
                                && expireInfo.getDiffDays() == 0) {
                            statusIcon.setBackgroundResource(R.drawable.color_yellow);
                        }

                        ((TextView) tableRow.findViewById(R.id.tr_header1)).setText(item.getPlate());
                        ((TextView) tableRow.findViewById(R.id.tr_header2)).setText(item.getStallNumber());
                        ((TextView) tableRow.findViewById(R.id.tr_header3)).setText(expireInfo.getExpireMsg());

                        if ((index % 2) == 0)
                            tableRow.setBackgroundResource(R.drawable.tablerow_even);
                        else
                            tableRow.setBackgroundResource(R.drawable.tablerow_odd);

                        tableLayout.addView(tableRow);
                        index++;
                    }

                    Button closeButton = dialog.findViewById(R.id.close_btn);
                    closeButton.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    Button continueButton = dialog.findViewById(R.id.continue_btn);
                    continueButton.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();

                            if (doPlateLookup) {
                                processPlateLookupQueue(searchData);
                            }
                        }
                    });

                    dialog.setCancelable(false);
                    dialog.setTitle("PayByPhone\nParking - " + searchData);
                    dialog.show();

                } catch (Exception e) {
                    Log.e("TicketPRO", "PayByPhone Zone Info " + e.getMessage());
                }
            }
        });
        //}
    }

    private void _displayMsgCalePlate(ValidParkingData1 validParkingData, final String plate) {
        View view = LayoutInflater.from(activity.getBaseContext()).inflate(R.layout.item_view, null);
        TextView headerTV = view.findViewById(R.id.headerTV);
        TextView valueTV = view.findViewById(R.id.valueTV);

        StringBuilder keys = new StringBuilder();
        StringBuilder values = new StringBuilder();

        keys.append("Zone" + "\n");
        keys.append("Plate" + "\n");
        keys.append("Start time" + "\n");
        keys.append("End time" + "\n");
        keys.append("Expire" + "\n");
        keys.append("Payed" + "\n");
        keys.append("Tariff" + "\n");

        headerTV.setText(keys.toString());


        values.append(" : ").append(StringUtil.getDisplayString(validParkingData.getZone())).append("\n");
        values.append(" : ").append(StringUtil.getDisplayString(validParkingData.getCode())).append("\n");
        values.append(" : ").append(StringUtil.getDisplayString(DateUtil.getConvertedDate(validParkingData.getStartDateUtc()))).append("\n");
        values.append(" : ").append(StringUtil.getDisplayString(DateUtil.getConvertedDate(validParkingData.getEndDateUtc()))).append("\n");
        values.append(" : ").append(StringUtil.getDisplayString(validParkingData.getExpireInfo().getExpireMsg())).append("\n");
        values.append(" : $").append(StringUtil.getDisplayString(validParkingData.getAmount())).append("\n");
        values.append(" : ").append(StringUtil.getDisplayString(validParkingData.getTariffList().get(0).getName())).append("\n");

        valueTV.setText(values.toString());

        Builder dialog = new AlertDialog.Builder(activity);
        dialog.setCancelable(false);

        dialog.setView(view);
        dialog.setTitle("Cale\nPlate Info: " + plate);
        dialog.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                enableBack();
                dialog.dismiss();
            }
        });

        dialog.setNegativeButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                processPlateLookupQueue(plate);
            }
        });

        TPUtility.applyButtonStyles(dialog.show());
    }

    private void _displayMsgCalePlateMultiple(List<ValidParkingData1> validParkingData, final String plate) {
       /* View view = LayoutInflater.from(activity.getBaseContext()).inflate(R.layout.item_view, null);
        TextView headerTV = view.findViewById(R.id.headerTV);
        TextView valueTV = view.findViewById(R.id.valueTV);
*/
        ArrayList<HashMap<String, String>> hashMapsArray = new ArrayList<>();

        for (int i = 0; i < validParkingData.size(); i++) {
            ValidParkingData1 validParkingData1 = validParkingData.get(i);

            StringBuilder keys = new StringBuilder();
            StringBuilder values = new StringBuilder();
            HashMap<String, String> hashMaps = new HashMap<>();
            keys.append("Zone" + "\n");
            keys.append("Plate" + "\n");
            keys.append("Start time" + "\n");
            keys.append("End time" + "\n");
            keys.append("Expire" + "\n");
            keys.append("Payed" + "\n");
            keys.append("Tariff" + "\n");

            //headerTV.setText(keys.toString());


            String zone = validParkingData1.getZone();
            String zoneName = "";
            if (zone != null && zone.contains("&")) {
                String[] split = zone.split("&");
                zoneName = split[0];
            } else {
                zoneName = zone;
            }

            values.append(" : ").append(StringUtil.getDisplayString(zoneName)).append("\n");
            values.append(" : ").append(StringUtil.getDisplayString(validParkingData1.getCode())).append("\n");
            values.append(" : ").append(StringUtil.getDisplayString(DateUtil.getConvertedDate(validParkingData1.getStartDateUtc()))).append("\n");
            values.append(" : ").append(StringUtil.getDisplayString(DateUtil.getConvertedDate(validParkingData1.getEndDateUtc()))).append("\n");
            values.append(" : ").append(StringUtil.getDisplayString(validParkingData1.getExpireInfo().getExpireMsg())).append("\n");
            values.append(" : $").append(StringUtil.getDisplayString(validParkingData1.getAmount())).append("\n");
            values.append(" : ").append(StringUtil.getDisplayString(validParkingData1.getTariffList().get(0).getName())).append("\n");

            //valueTV.setText(values.toString());
            hashMaps.put("Key", keys.toString());
            //hashMapsArray.add(hashMaps);
            hashMaps.put("Value", values.toString());
            hashMapsArray.add(hashMaps);
        }

        Builder dialog = new AlertDialog.Builder(activity);
        dialog.setCancelable(false);
        CustomAdapter adapter = new CustomAdapter(activity, hashMapsArray);

        dialog.setTitle("Cale\nPlate Info: " + plate).setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                enableBack();
                dialog.dismiss();
            }
        });

        dialog.setNegativeButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                processPlateLookupQueue(plate);
            }
        });

        TPUtility.applyButtonStyles(dialog.show());
    }



    public class CustomAdapter extends BaseAdapter {

        private Context context;
        private List<HashMap<String, String>> data;

        public CustomAdapter(Context context, List<HashMap<String, String>> data) {
            this.context = context;
            this.data = data;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            ViewHolder holder;

            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.item_view, null);
                holder = new ViewHolder();
                holder.textViewHeader = view.findViewById(R.id.headerTV);
                holder.textViewValue = view.findViewById(R.id.valueTV);

                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            // Bind data to the views
            HashMap<String, String> item = data.get(position);
            holder.textViewHeader.setText(item.get("Key"));
            holder.textViewValue.setText(item.get("Value"));

            return view;
        }

        class ViewHolder {
            TextView textViewHeader;
            TextView textViewValue;
        }
    }

    private void _displayMsgParkeonPlate(ParkeonZoneInfo validParkingData, final String plate) {
        Builder dialog = null;
        try {
            View view = LayoutInflater.from(activity.getBaseContext()).inflate(R.layout.item_view, null);
            TextView headerTV = view.findViewById(R.id.headerTV);
            TextView valueTV = view.findViewById(R.id.valueTV);

            StringBuilder keys = new StringBuilder();
            StringBuilder values = new StringBuilder();

            //keys.append("Zone" + "\n");
            keys.append("Plate" + "\n");
            keys.append("Start time" + "\n");
            keys.append("End time" + "\n");
            keys.append("Expire" + "\n");
            keys.append("Purchased" + "\n");
            keys.append("Zone name" + "\n");

            headerTV.setText(keys.toString());


            //values.append(" : ").append(StringUtil.getDisplayString(validParkingData.getZone_id())).append("\n");
            values.append(" : ").append(StringUtil.getDisplayString(validParkingData.getPlate_number())).append("\n");
            values.append(" : ").append(StringUtil.getDisplayString(DateUtil.getConvertedDateParkeon(validParkingData.getStart_date()))).append("\n");
            values.append(" : ").append(StringUtil.getDisplayString(DateUtil.getConvertedDateParkeon(validParkingData.getEnd_date()))).append("\n");
            values.append(" : ").append(StringUtil.getDisplayString(validParkingData.getExpireInfo().getExpireMsg())).append("\n");
            values.append(" : ").append(Math.abs(DateUtil.getTimeDiffHours(validParkingData.getStart_date(), validParkingData.getEnd_date())) + " H").append("\n");
            values.append(" : ").append(preference.getString(TPConstant.PARKEON_ZONE_NAME)).append("\n");

            valueTV.setText(values.toString());

            dialog = new Builder(activity);
            dialog.setCancelable(false);

            dialog.setView(view);
            dialog.setTitle("Parkeon\nPlate Info: " + plate);
            dialog.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    enableBack();
                    dialog.dismiss();
                }
            });

            dialog.setNegativeButton("Continue", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    processPlateLookupQueue(plate);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        TPUtility.applyButtonStyles(dialog.show());
    }


    public void doPlateLookup(final String plate) {
        if (Feature.isFeatureAllowed(Feature.PARK_SEARCH_TRACK)) {
            logpm.info("plate lookup executed");
        }
        if (plate == null || plate.isEmpty()) {
            return;
        }

        // Clear lookup queue
        try {
            if (plateLookupQueue != null && plateLookupQueue.size() > 0) {
                plateLookupQueue.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //if (TPUtility.isActivityRunning(activity)) {
        activity.runOnUiThread(() -> {
            TPAsyncTask task = new TPAsyncTask(activity, "Processing Plate...");
            task.execute(new TPTask() {
                @Override
                public void execute() {
                    try {
                        String state = activity.stateEditText.getText().toString();
                        ArrayList<Hotlist> hotlist = null;
                        ArrayList<Ticket> historyTicket = null;
                        ArrayList<Permit> permit = null;
                        // Check Ticket History
                        try {
                            activity.isServiceAvailable = !Feature.isFeatureAllowed(Feature.TOGGLE_NET_ON_OFF) || !TPApplication.getInstance().getNetOnOff().equals("Y");
                            if (activity.isServiceAvailable) {
                                historyTicket = WriteTicketNetworkCalls.searchAllTickets(plate, activity);
                                //logpm.info(historyTicket.size());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (historyTicket == null) {
                            historyTicket = Ticket.searchAllPreviousTicketByPlate(state, plate);
                            if (historyTicket.isEmpty()) {
                                historyTicket = TicketHistory.searchAllPreviousTicketsByPlate(state, plate);
                                //logpm.info(historyTicket.size());
                            }
                        }
                        activity.isServiceAvailable = !Feature.isFeatureAllowed(Feature.TOGGLE_NET_ON_OFF) || !TPApplication.getInstance().getNetOnOff().equals("Y");
                        if (activity.isServiceAvailable) {

                            hotlist = WriteTicketNetworkCalls.searchHotlist(plate, activity);
                            //logpm.info(hotlist.size());
                            //hotlist = ((WriteTicketBLProcessor) activity.screenBLProcessor).searchHotlist(plate, state);
                        }

                        if (hotlist == null) {
                            hotlist = Hotlist.searchHotlist(plate, state);
                            // logpm.info(hotlist.size());
                        }

                        if (Feature.isFeatureAllowed(Feature.LOOKUP_PLATE_PERMIT)) {
                            activity.isServiceAvailable = !Feature.isFeatureAllowed(Feature.TOGGLE_NET_ON_OFF) || !TPApplication.getInstance().getNetOnOff().equals("Y");
                            if (activity.isServiceAvailable) {
                                permit = WriteTicketNetworkCalls.searchAllPermitByPlate(plate, activity, hotlist.size(), historyTicket.size());
                                //logpm.info(permit.size());
                                //permit = ((WriteTicketBLProcessor) activity.screenBLProcessor).searchAllPermitByPlate(TPApp.custId, plate, state);
                            }
                        }

                        if (hotlist.size() > 0 || historyTicket.size() > 0 || permit != null && permit.size() > 1) {

                            PlateLookupResult lookupResult = new PlateLookupResult();
                            lookupResult.setAllTicket(historyTicket);

                            if (historyTicket.size() > 0) {
                                lookupResult.setHistoryTicket(historyTicket.get(0));
                            } else {
                                lookupResult.setHistoryTicket(null);
                            }

                            lookupResult.setHotlist(hotlist);
                            lookupResult.setAllPermit(permit);

                            if (permit != null && permit.size() > 0) {
                                lookupResult.setPermit(permit.get(0));
                            }

                            activity.updateActiveTicketInfo(false);

                            TPApp.setActiveLookupResult(lookupResult);

                            Intent intent = new Intent();
                            intent.putExtra(PlateLookupResultAdvance.PLATE_NUMBER, plate);
                            intent.setClass(activity, PlateLookupResultAdvance.class);
                            activity.startActivityForResult(intent, activity.REQUEST_PLATE_LOOKUP);

                        } else if (permit != null && permit.size() > 0) {
                            displayPermitInfo(permit.get(0), false);
                        }

                    } catch (Exception e) {
                        activity.log.error(TPUtility.getPrintStackTrace(e));
                    }
                    activity.runOnUiThread(() -> {
                        enableBack();
                    });
                }
            });
        });
        //}
    }

    //
    public void checkVinHistory(final String vin) {

        activity.runOnUiThread(() -> {
            TPAsyncTask task = new TPAsyncTask(activity, "Processing Vin...");
            task.execute(new TPTask() {
                @Override
                public void execute() {
                    try {
                        ArrayList<Ticket> historyTicket = null;
                        try {
                            activity.isServiceAvailable = !Feature.isFeatureAllowed(Feature.TOGGLE_NET_ON_OFF) || !TPApplication.getInstance().getNetOnOff().equals("Y");
                            if (activity.isServiceAvailable) {
                                historyTicket = WriteTicketNetworkCalls.searchAllTicketsByVin(vin, activity);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (historyTicket != null && historyTicket.size() > 0) {

                            PlateLookupResult lookupResult = new PlateLookupResult();
                            lookupResult.setAllTicket(historyTicket);

                            if (historyTicket.size() > 0) {
                                lookupResult.setHistoryTicket(historyTicket.get(0));
                            } else {
                                lookupResult.setHistoryTicket(null);
                            }
                            activity.updateActiveTicketInfo(false);

                            TPApp.setActiveLookupResult(lookupResult);

                            Intent intent = new Intent();
                            intent.putExtra(PlateLookupResultAdvance.PLATE_NUMBER, vin);
                            intent.setClass(activity, PlateLookupResultAdvance.class);

                            activity.startActivityForResult(intent, activity.REQUEST_PLATE_LOOKUP);

                        }

                    } catch (Exception e) {
                        activity.log.error(TPUtility.getPrintStackTrace(e));
                    }
                }
            });
        });
        //}
    }

    public void lookupSpacePassportParking2(final String spaceStr,String msg) {
        if (activity.isServiceAvailable) {
            getTokenPP2(spaceStr, msg);
        }
    }

    public void getTokenPP2(String spaceStr, String msg) {
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage(msg);
        progressDialog.setCancelable(false);
        progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        progressDialog.show();

        TokenRequest tokenRequest = new TokenRequest();
        tokenRequest.setGrantType("client_credentials");
        tokenRequest.setAudience("public.api.passportinc.com");
        tokenRequest.setClientId("Y9h6IjcZxNBU0EzkmZdxVuWFBNUbNjhd");
        tokenRequest.setClientSecret("FrTFITvPWKQD_ApL-R89cJymRO2cobdH7C5I7LynvDFZItjpR17nzNm4wa8F-PIO");
        //ApiRequest apiRequest = ServiceGeneratorPass2.getApiService();
        // Call<TokenResponse> tokenForPassportParking2 = apiRequest.getTokenForPassportParking2(tokenRequest);
        ApiRequest service = ServiceGeneratorPass2.createService(ApiRequest.class);
        Call<PP2TokenResponse> tokenForPassportParking21 = service.getTokenForPassportParking2(tokenRequest);
        tokenForPassportParking21.enqueue(new retrofit2.Callback<PP2TokenResponse>() {
            @Override
            public void onResponse(@NonNull Call<PP2TokenResponse> call, @NonNull Response<PP2TokenResponse> response) {

                if (response.isSuccessful() && response.code() == 200) {
                    assert response.body() != null;
                    String accessToken = response.body().getAccessToken();
                    if (accessToken != null && !accessToken.equals("")) {
                        __searchSpacePP2(spaceStr, accessToken, progressDialog);
                    }
                } else {
                    progressDialog.dismiss();
                    logpm.info("PP2 Message: " + response.message());

                }

            }

            @Override
            public void onFailure(@NonNull Call<PP2TokenResponse> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                logpm.error("PP2 Message: " + t.getMessage());

            }
        });
    }


    public void getTokenPP2Plate(String plate, String msg) {
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage(msg);
        progressDialog.setCancelable(false);
        progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        progressDialog.show();

        TokenRequest tokenRequest = new TokenRequest();
        tokenRequest.setGrantType("client_credentials");
        tokenRequest.setAudience("public.api.passportinc.com");
        tokenRequest.setClientId("Y9h6IjcZxNBU0EzkmZdxVuWFBNUbNjhd");
        tokenRequest.setClientSecret("FrTFITvPWKQD_ApL-R89cJymRO2cobdH7C5I7LynvDFZItjpR17nzNm4wa8F-PIO");
        //ApiRequest apiRequest = ServiceGeneratorPass2.getApiService();
        // Call<TokenResponse> tokenForPassportParking2 = apiRequest.getTokenForPassportParking2(tokenRequest);
        ApiRequest service = ServiceGeneratorPass2.createService(ApiRequest.class);
        Call<PP2TokenResponse> tokenForPassportParking21 = service.getTokenForPassportParking2(tokenRequest);
        tokenForPassportParking21.enqueue(new retrofit2.Callback<PP2TokenResponse>() {
            @Override
            public void onResponse(@NonNull Call<PP2TokenResponse> call, @NonNull Response<PP2TokenResponse> response) {

                if (response.isSuccessful() && response.code() == 200) {
                    assert response.body() != null;
                    String accessToken = response.body().getAccessToken();
                    if (accessToken != null && !accessToken.equals("")) {
                        __searchPlateLookupPP2(plate, accessToken, progressDialog);
                    }
                } else {
                    progressDialog.dismiss();
                    processPlateLookupQueue(plate);
                    logpm.info("PP2 Message: " + response.message());

                }

            }

            @Override
            public void onFailure(@NonNull Call<PP2TokenResponse> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                logpm.error("PP2 Message: " + t.getMessage());
                processPlateLookupQueue(plate);

            }
        });
    }


    private void __searchSpacePP2(String spaceStr, String accessToken, ProgressDialog progressDialog) {
        String URL = "https://api.us.passportinc.com/v4/enforcement/parking-rights?operator_id=1169338c-870a-465a-ae24-f783e436d376";


        ApiRequest service = ServiceGeneratorPass2.createService(ApiRequest.class, accessToken);
        service.getPP2Space(URL).enqueue(new retrofit2.Callback<Passport2Array>() {
            @Override
            public void onResponse(@NonNull Call<Passport2Array> call, @NonNull Response<Passport2Array> response) {
                progressDialog.dismiss();
                if (response.code() == 200 && response.isSuccessful()) {
                    List<Passport2Data> data = response.body().getData();
                    if (data != null && data.size() > 0) {
                        for (int i = 0; i < data.size(); i++) {
                            Passport2Data passport2Data = data.get(i);
                            if (passport2Data.getSpaceNumber().equals(spaceStr)) {
                                __displayPP2(passport2Data, spaceStr);
                                break;
                            }
                        }
                    } else {
                        processSpaceLookupQueue();

                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Passport2Array> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                processSpaceLookupQueue();
                call.cancel();

                if (t.getMessage().contains("timed out") || t.getMessage().contains("timeout") || t.getMessage().contains("HTTP/1.1 403 Forbidden")) {
                    if (parkmobile) {
                        Builder dialog = new Builder(activity);
                        dialog.setCancelable(true);
                        dialog.setTitle("PP2 Plate has not been validated.");
                        String finalResponse = t.getMessage();
                        dialog.setPositiveButton("Continue", (dialog1, which) -> {
                            processPlateLookupQueue(plate);
                        });
                        dialog.setNegativeButton("Cancel", (dialog12, i) -> dialog12.dismiss());
                        dialog.show();
                    }
                }

            }
        });

    }

    private void __displayPP2(Passport2Data passport2Data, String spaceStr) {
        activity.runOnUiThread(() -> {
            try {
                StringBuilder message = new StringBuilder();
                StringBuilder values = new StringBuilder();

                View view = LayoutInflater.from(activity.getBaseContext()).inflate(R.layout.item_view, null);
                TextView headerTV = view.findViewById(R.id.headerTV);
                TextView valueTV = view.findViewById(R.id.valueTV);

                message.append("Space Number" + "\n");

                message.append("Start Time" + "\n");
                message.append("End Time" + "\n");

                message.append("Expiry Time");

                values.append(": ").append(StringUtil.getDisplayString(passport2Data.getSpaceNumber())).append("\n");
                values.append(": ").append(DateUtil.getPP2DateFromUTCString1(passport2Data.getStartTime())).append("\n");
                values.append(": ").append(DateUtil.getPP2DateFromUTCString1(passport2Data.getEndTime())).append("\n");
                ParkingExpireInfo expireInfo = passport2Data.getExpireInfo();

                values.append(": ").append(expireInfo.getExpireMsg());

                headerTV.setText(message.toString());
                valueTV.setText(values.toString());

                Builder dialog = new Builder(activity);
                dialog.setCancelable(false);
                dialog.setView(view);
                dialog.setTitle("PP2\nSpace Info");
                dialog.setPositiveButton("OK", (dialog1, which) -> dialog1.dismiss());

                dialog.setNegativeButton("Cancel", (dialog12, which) -> {
                    dialog12.dismiss();

                });

                dialog.show();

            } catch (Exception e) {
                Log.e("TicketPRO", "pp2 Space Info " + e.getMessage());
            }
        });

    }


    private void lookupPassportParking(final String plate) {
        if (activity.isServiceAvailable) {
            if (!(TPApp.enablePassportParking && Feature.isFeatureAllowed(Feature.PASSPORT_PARKING))) {
                return;
            }
            this.plate = plate;
            final VendorServiceConfig config = VendorService.getServiceConfig(VendorService.PASSPORT_PARKING_PLATEINFO,
                    TPApp.deviceId);
            if (config != null && activity.isServiceAvailable) {
                TPAsyncTask task = new TPAsyncTask(activity, "Processing PassportParking...");
                task.execute(new TPTask() {
                    @Override
                    public void execute() {
                        UserSetting settings = TPApp.getUserSettings();
                        String safePlate = plate.replaceAll(" ", "");
                        String params = config.getParams();
                        params = params.replaceAll("\\{PLATE\\}", safePlate);

                        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
                        Date expiryDate = cal.getTime();
                        int cachedExpiry = 10;
                        //int cachedExpiry = 0;

                        if (settings != null) {
                            cachedExpiry = settings.getCacheExpiry();
                        }

                        cal.setTimeInMillis(expiryDate.getTime() - cachedExpiry * 60 * 1000);
                        //params may be - apikey=e63fa7ba4692456591ef90deb8e471ec,lpn={PLATE},includetransactionexitafter={EXIT_AFTER}
                        if (params.contains("includetransactionexitafter")) {
                            params = params.replaceAll("\\{EXIT_AFTER\\}", Uri.encode(DateUtil.getSQLStringFromDate2(cal.getTime())));
                        } else {
                            params = params.replaceAll("\\{EXIT_AFTER\\}", "");
                        }

                        HttpRequestTask task = new HttpRequestTask(LOOKUP_PASSPORT_PARKING_PLATE, new Bundle(), params);
                        task.setRequestMode(config.getRequestMode());
                        serviceName = "PassportParking";
                        task.executeTask(config.getServiceURL() + "?" + params);

                        this.asyncTask = task;
                    }
                });
            } else {
                processPlateLookupQueue(plate);
            }
        }
    }


    /***
     * //Code by Sanjiv
     * @param plate
     */
    private void __searchPlateLookupPP2(String plate, String accessToken, ProgressDialog progressDialog) {
        if (activity.isServiceAvailable) {
            if (!(TPApp.enablePassportParking2 && Feature.isFeatureAllowed(Feature.PASSPORT_PARKING2))) {
                return;
            }
            this.plate = plate;
            final VendorServiceConfig config = VendorService.getServiceConfig(VendorService.PP2_PLATEINFO,
                    TPApp.deviceId);
            if (config != null && activity.isServiceAvailable) {


                String serviceURL = config.getServiceURL();
                String params = config.getParams();
                String URL = serviceURL + params + "&vehicle_plate=" + plate;
                ApiRequest service = ServiceGeneratorPass2.createService(ApiRequest.class, accessToken);
                service.getPP2PlateInfo(URL).enqueue(new retrofit2.Callback<PlateResponse>() {
                    @Override
                    public void onResponse(Call<PlateResponse> call, Response<PlateResponse> response) {
                        progressDialog.dismiss();

                        if (response.code() == 200 && response.isSuccessful()) {
                            List<PP2Plate> data = response.body().getData();
                            if (data != null && data.size() > 0) {
                                PP2Plate pp2Plate = data.get(0);
                                if (Feature.isFeatureAllowed(Feature.PARK_SEARCH_TRACK)) {
                                    try {
                                        MobileNowLog log = new MobileNowLog();
                                        log.setCustId(TPApp.custId);
                                        log.setDeviceId(TPApp.deviceId);
                                        log.setUserId(TPApp.userId);
                                        log.setRequestDate(new Date());
                                        log.setPlate_number(plate);
                                        log.setRequestParams("PP2 Request: " + plate);
                                        log.setServiceMode(config.getRequestMode());
                                        if (pp2Plate.getVehicle().getVehiclePlate() != null) {
                                            log.setResponseText("True " + plate);
                                        } else {
                                            log.setResponseText("Plate not found.");
                                        }

                                        MobileNowLog.insertMobileNowLog(log);
                                        CSVUtility.writeMobileLogCSV(log);

                                        ArrayList<MobileNowLog> logs = new ArrayList<>();
                                        logs.add(log);
                                        WriteTicketNetworkCalls.sendMobileNogLogs(logs);
                                    } catch (Exception e) {
                                        //log.error("Error " + TPUtility.getPrintStackTrace(e));
                                        e.printStackTrace();
                                    }
                                }

                                __displayPP2PlateInfo(pp2Plate);
                            } else {
                                if (Feature.isFeatureAllowed(Feature.PARK_SEARCH_TRACK)) {
                                    try {
                                        MobileNowLog log = new MobileNowLog();
                                        log.setCustId(TPApp.custId);
                                        log.setDeviceId(TPApp.deviceId);
                                        log.setUserId(TPApp.userId);
                                        log.setRequestDate(new Date());
                                        log.setPlate_number(plate);
                                        log.setRequestParams("PP2 Request: " + plate);
                                        log.setServiceMode(config.getRequestMode());
                                        log.setResponseText("Plate not found.");

                                        MobileNowLog.insertMobileNowLog(log);
                                        CSVUtility.writeMobileLogCSV(log);

                                        ArrayList<MobileNowLog> logs = new ArrayList<>();
                                        logs.add(log);
                                        WriteTicketNetworkCalls.sendMobileNogLogs(logs);
                                    } catch (Exception e) {
                                        //log.error("Error " + TPUtility.getPrintStackTrace(e));
                                        e.printStackTrace();
                                    }
                                }

                                processPlateLookupQueue(plate);


                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<PlateResponse> call, Throwable t) {
                        progressDialog.dismiss();
                        processPlateLookupQueue(plate);
                        call.cancel();

                        if (t.getMessage().contains("timed out") || t.getMessage().contains("timeout") || t.getMessage().contains("read timeout") || t.getMessage().contains("HTTP/1.1 403 Forbidden")) {
                            if (parkmobile) {
                                Builder dialog = new Builder(activity);
                                dialog.setCancelable(true);
                                dialog.setTitle("Unable to reach pp2 . Plate has not been validated.");

                                String finalResponse = t.getMessage();
                                dialog.setPositiveButton("Continue", (dialog1, which) -> {
                                    processPlateLookupQueue(plate);
                                });
                                dialog.setNegativeButton("Cancel", (dialog12, i) -> dialog12.dismiss());
                                dialog.show();
                            }
                        }
                    }
                });


            } else {
                progressDialog.dismiss();
                processPlateLookupQueue(plate);
            }
        }
    }

    private void __displayPP2PlateInfo(PP2Plate pp2Plate) {
        activity.runOnUiThread(() -> {
            try {
                StringBuilder message = new StringBuilder();
                StringBuilder values = new StringBuilder();

                View view = LayoutInflater.from(activity.getBaseContext()).inflate(R.layout.item_view, null);
                TextView headerTV = view.findViewById(R.id.headerTV);
                TextView valueTV = view.findViewById(R.id.valueTV);

                message.append("Plate" + "\n");
                message.append("State" + "\n");

                message.append("Start Time" + "\n");
                message.append("End Time" + "\n");

                message.append("Expiry Time");

                values.append(": ").append(StringUtil.getDisplayString(pp2Plate.getVehicle().getVehiclePlate())).append("\n");
                values.append(": ").append(StringUtil.getDisplayString(pp2Plate.getVehicle().getVehicleState())).append("\n");
                values.append(": ").append(DateUtil.getPP2DateFromUTCString1(pp2Plate.getStartTime())).append("\n");
                values.append(": ").append(DateUtil.getPP2DateFromUTCString1(pp2Plate.getEndTime())).append("\n");
                ParkingExpireInfo expireInfo = pp2Plate.getExpireInfo();
                values.append(": ").append(expireInfo.getExpireMsg());

                headerTV.setText(message.toString());
                valueTV.setText(values.toString());

                Builder dialog = new Builder(activity);
                dialog.setCancelable(false);
                dialog.setView(view);
                dialog.setTitle("PP2\nPlate Info: " + plate);
                dialog.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        enableBack();
                        dialog.dismiss();
                    }
                });

                dialog.setNegativeButton("Continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        processPlateLookupQueue(plate);
                    }
                });

                TPUtility.applyButtonStyles(dialog.show());

            } catch (Exception e) {
                Log.e("TicketPRO", "pp2 Space Info " + e.getMessage());
            }
        });

    }


    private void lookupParkMobile(final String plate) {
        if (activity.isServiceAvailable) {
            if (!(TPApp.enableParkMobile && Feature.isFeatureAllowed(Feature.PARK_MOBILE))) {
                return;
            }

            this.plate = plate;
            final VendorServiceConfig config = VendorService.getServiceConfig(VendorService.PARK_MOBILE_PLATEINFO,
                    TPApp.deviceId, "/");
            if (config != null && activity.isServiceAvailable) {
                TPAsyncTask task = new TPAsyncTask(activity, "Processing ParkMobile...");
                task.execute(new TPTask() {
                    @Override
                    public void execute() {
                        String safePlate = plate.replaceAll(" ", "");
                        String params = config.getParams();
                        params = params.replaceAll("\\{PLATE\\}", safePlate);

                        String URL = config.getServiceURL() + "/" + params + "?format=json";
                       /* if (Feature.isFeatureAllowed(Feature.PARK_SEARCH_TRACK)) {
                            try {
                                MobileNowLog log = new MobileNowLog();
                                log.setCustId(TPApp.custId);
                                log.setDeviceId(TPApp.deviceId);
                                log.setUserId(TPApp.userId);
                                log.setRequestDate(new Date());
                                log.setPlate_number(plate);
                                log.setRequestParams("ParkMobile Request:"+params);
                                log.setServiceMode("Request");
                                MobileNowLog.insertMobileNowLog(log);
                                CSVUtility.writeMobileLogCSV(log);
                                ArrayList<MobileNowLog> logs = new ArrayList<>();
                                logs.add(log);
                                WriteTicketNetworkCalls.sendMobileNogLogs(logs);
                            } catch (Exception e) {
                                logpm.error(TPUtility.getPrintStackTrace(e));
                            }
                        }*/

                        HttpRequestTask task = new HttpRequestTask(LOOKUP_PARK_MOBILE_PLATE, new Bundle(), params);
                        task.setRequestMode(config.getRequestMode());
                        task.executeTask(URL, config.getUsername(), config.getPassword());
                        serviceName = "Parkmobile";
                        parkmobile = true;

                        this.asyncTask = task;
                    }
                });
            } else {
                processPlateLookupQueue(plate);
            }
        }
    }

    private void lookupPayByPhone(final String plate) {
        if (activity.isServiceAvailable) {
            if (!(TPApp.enablePayByPhone && Feature.isFeatureAllowed(Feature.PAYBY_PHONE))) {
                return;
            }
            this.plate = plate;
        /*final VendorServiceConfig config = VendorService.getServiceConfig(VendorService.PAYBYPHONE_ZONEINFO,
                TPApp.deviceId);*/
            final VendorServiceConfig config = VendorService.getServiceConfig(VendorService.PAYBYPHONE_PLATEINFO,
                    TPApp.deviceId);
            if (config != null && activity.isServiceAvailable) {
                TPAsyncTask task = new TPAsyncTask(activity, "Processing PayByPhone...");
                task.execute(new TPTask() {
                    @Override
                    public void execute() {

                       if (TPApplication.getSingleton().isVendorCode()){
                           int anInt = preference.getInt(TPConstant.VENDOR_CODE);
                           String safePlate = plate.replaceAll(" ", "");
                           String params = config.getParams();
                           params = params.replace("vendorId=12001", "vendorId="+anInt);

                           params = params.replaceAll("\\{PLATE\\}", safePlate);
                           String URL = config.getServiceURL() + "/?" + params;
                           HttpRequestTask task = new HttpRequestTask(LOOKUP_PAYBYPHONE_PLATE, new Bundle(), params);
                           serviceName = "PayByPhone";
                           task.setRequestMode(config.getRequestMode());
                           task.executeTask(URL, config.getUsername(), config.getPassword());

                           this.asyncTask = task;
                       }else {

                           String safePlate = plate.replaceAll(" ", "");
                           String params = config.getParams();
                           params = params.replaceAll("\\{PLATE\\}", safePlate);
                           String URL = config.getServiceURL() + "/?" + params;
                           HttpRequestTask task = new HttpRequestTask(LOOKUP_PAYBYPHONE_PLATE, new Bundle(), params);
                           serviceName = "PayByPhone";
                           task.setRequestMode(config.getRequestMode());
                           task.executeTask(URL, config.getUsername(), config.getPassword());

                           this.asyncTask = task;
                       }


                    /*try {
                        ArrayList<VendorItem> items = VendorItem.getVendorZones(config.getVendorId());
                        for (VendorItem item : items) {
                            params = config.getParams();
                            params = params.replaceAll("\\{ZONE_ID\\}", item.getItemCode());

                            response = TPUtility.getURLResponse(config.getServiceURL() + "?" + params);
                            if (response.contains(safePlate)) {
                                break;
                            } else {
                                response = "";
                            }
                        }

                        new RequestLogTask(params, config.getRequestMode(), response).execute();

                    } catch (Exception e) {
                        activity.log.error(TPUtility.getPrintStackTrace(e));
                    }

                    Bundle data = new Bundle();
                    data.putString(RESPONSE_DATA, response);

                    Message msg = obtainMessage();
                    msg.what = LOOKUP_PAYBYPHONE_PLATE;
                    msg.setData(data);
                    sendMessage(msg);*/
                    }
                });
            } else {
                processPlateLookupQueue(plate);
            }
        }
    }

    /**
     * Code by Sanjiv
     * Fetch data from vendor server
     *
     * @param plate
     */
    private void lookupCale(final String plate) {
        if (activity.isServiceAvailable) {
            try {
                if (!(TPApp.enableCale && Feature.isFeatureAllowed(Feature.CALE))) {
                    return;
                }

                //VendorServiceConfig config = VendorService.getServiceConfigCale(VendorService.CALE_PLATE_SEARCH, TPApp.deviceId, "/");
                ArrayList<VendorServiceConfig> config1 = VendorService.getServiceConfigCale1(VendorService.CALE_PLATE_SEARCH, TPApp.deviceId, "/");
                if (config1 == null)
                    return;
                if (activity.isServiceAvailable && config1.size() > 0) {
                    progressDialog = new ProgressDialog(activity);
                    progressDialog.setMessage("Processing Cale...");
                    progressDialog.setCancelable(false);
                    progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (task != null) {
                                task.cancel(true);
                            }
                            dialog.dismiss();
                        }
                    });
                    progressDialog.show();
                    final int[] responseSize = {0};
                    for (int i = 0; i < config1.size(); i++) {
                        VendorServiceConfig config = config1.get(i);
                        Map<String, String> paramsMap = config.getParamsMap();
                        String user = paramsMap.get("User");
                        String password = paramsMap.get("Password");
                        String serviceURL = config.getServiceURL();
                        String safePlate = plate.replaceAll(" ", "");
                        String url = null;
                        if ((Feature.isFeatureAllowed(Feature.PARK_PLATE_SEARCH_ZERO_OR_O)) && (safePlate.contains("O") || safePlate.contains("0"))) {
                            lookUpCaleWithZeroAndO(progressDialog, user, password, serviceURL, safePlate, "Cale");
                        } else {
                            url = serviceURL + safePlate + "/5";
                            ApiRequest service = RetrofitServiceGenerator.createService(ApiRequest.class, user, password);
                            service._serchPlate2(url).enqueue(new retrofit2.Callback<ArrayOfValidParkingData>() {
                                @Override
                                public void onResponse(@NonNull Call<ArrayOfValidParkingData> call, @NonNull Response<ArrayOfValidParkingData> response) {
                                    responseSize[0]++;
                                    if (response.isSuccessful() && response.code() == 200) {
                                        assert response.body() != null;
                                        ArrayOfValidParkingData body = response.body();
                                        List<ValidParkingData1> validParkingData = body.getValidParkingData();
                                        if (validParkingData != null && validParkingData.size() > 0) {
                                            ValidParkingData1 validParkingData1 = null;
                                            for (int j = 0; j < validParkingData.size(); j++) {
                                                validParkingData1 = validParkingData.get(j);
                                            }
                                            if (Feature.isFeatureAllowed(Feature.PARK_SEARCH_TRACK)) {
                                                try {
                                                    MobileNowLog log = new MobileNowLog();
                                                    log.setCustId(TPApp.custId);
                                                    log.setDeviceId(TPApp.deviceId);
                                                    log.setUserId(TPApp.userId);
                                                    log.setRequestDate(new Date());
                                                    log.setPlate_number(plate);
                                                    log.setRequestParams("Cale Request: " + plate);
                                                    log.setServiceMode(config.getRequestMode());
                                                    if (validParkingData1 != null) {
                                                        log.setResponseText("True " + plate);
                                                    } else {
                                                        log.setResponseText("Plate not found.");
                                                    }

                                                    MobileNowLog.insertMobileNowLog(log);
                                                    CSVUtility.writeMobileLogCSV(log);

                                                    ArrayList<MobileNowLog> logs = new ArrayList<>();
                                                    logs.add(log);
                                                    WriteTicketNetworkCalls.sendMobileNogLogs(logs);
                                                } catch (Exception e) {
                                                    //log.error("Error " + TPUtility.getPrintStackTrace(e));
                                                    e.printStackTrace();
                                                }
                                            }

                                            if (validParkingData1 != null) {
                                                progressDialog.isShowing();
                                                progressDialog.dismiss();
                                                // _displayMsgCalePlate(validParkingData1, plate);
                                                _displayMsgCalePlateMultiple(validParkingData, plate);

                                            } else {
                                                if (config1.size() == responseSize[0]) {
                                                    responseSize[0] = 0;
                                                    progressDialog.isShowing();
                                                    progressDialog.dismiss();
                                                    processPlateLookupQueue(plate);
                                                }
                                            }

                                        } else {
                                            if (Feature.isFeatureAllowed(Feature.PARK_SEARCH_TRACK)) {
                                                try {
                                                    MobileNowLog log = new MobileNowLog();
                                                    log.setCustId(TPApp.custId);
                                                    log.setDeviceId(TPApp.deviceId);
                                                    log.setUserId(TPApp.userId);
                                                    log.setRequestDate(new Date());
                                                    log.setPlate_number(plate);
                                                    log.setRequestParams("Cale Request: " + plate);
                                                    log.setServiceMode(config.getRequestMode());

                                                    log.setResponseText("Plate not found.");


                                                    MobileNowLog.insertMobileNowLog(log);
                                                    CSVUtility.writeMobileLogCSV(log);

                                                    ArrayList<MobileNowLog> logs = new ArrayList<>();
                                                    logs.add(log);
                                                    WriteTicketNetworkCalls.sendMobileNogLogs(logs);
                                                } catch (Exception e) {
                                                    //log.error("Error " + TPUtility.getPrintStackTrace(e));
                                                    e.printStackTrace();
                                                }
                                            }
                                            progressDialog.isShowing();
                                            progressDialog.dismiss();
                                            processPlateLookupQueue(plate);
                                        }
                                    } else {
                                        if (Feature.isFeatureAllowed(Feature.PARK_SEARCH_TRACK)) {
                                            try {
                                                MobileNowLog log = new MobileNowLog();
                                                log.setCustId(TPApp.custId);
                                                log.setDeviceId(TPApp.deviceId);
                                                log.setUserId(TPApp.userId);
                                                log.setRequestDate(new Date());
                                                log.setPlate_number(plate);
                                                log.setRequestParams("Cale Request: " + plate);
                                                log.setServiceMode(config.getRequestMode());

                                                log.setResponseText("Plate not found.");


                                                MobileNowLog.insertMobileNowLog(log);
                                                CSVUtility.writeMobileLogCSV(log);

                                                ArrayList<MobileNowLog> logs = new ArrayList<>();
                                                logs.add(log);
                                                WriteTicketNetworkCalls.sendMobileNogLogs(logs);
                                            } catch (Exception e) {
                                                //log.error("Error " + TPUtility.getPrintStackTrace(e));
                                                e.printStackTrace();
                                            }
                                        }
                                        progressDialog.isShowing();
                                        progressDialog.dismiss();
                                        processPlateLookupQueue(plate);
                                    }
                                }

                                @Override
                                public void onFailure(@NonNull Call<ArrayOfValidParkingData> call, @NonNull Throwable t) {
                                    progressDialog.isShowing();
                                    progressDialog.dismiss();
                                    processPlateLookupQueue(plate);
                                    System.out.println(t.getMessage());
                                }
                            });
                        }
                    }

                } else {
                    processPlateLookupQueue(plate);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    /**
     * Code by Sanjiv
     * Fetch data from vendor server
     *
     * @param plate
     */
    private void lookupCale2(final String plate) {
        if (activity.isServiceAvailable) {
            try {
                if (!(TPApp.enableCale2 && Feature.isFeatureAllowed(Feature.CALE2))) {
                    return;
                }

                //VendorServiceConfig config = VendorService.getServiceConfigCale(VendorService.CALE_PLATE_SEARCH, TPApp.deviceId, "/");
                ArrayList<VendorServiceConfig> config1 = VendorService.getServiceConfigCale1(VendorService.CALE2_PLATE_SEARCH, TPApp.deviceId, "/");
                if (config1 == null)
                    return;
                if (activity.isServiceAvailable && config1.size() > 0) {
                    progressDialog = new ProgressDialog(activity);
                    progressDialog.setMessage("Processing Cale2...");
                    progressDialog.setCancelable(false);
                    progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (task != null) {
                                task.cancel(true);
                            }
                            dialog.dismiss();
                        }
                    });
                    progressDialog.show();
                    final int[] responseSize = {0};
                    for (int i = 0; i < config1.size(); i++) {
                        VendorServiceConfig config = config1.get(i);
                        Map<String, String> paramsMap = config.getParamsMap();
                        String user = paramsMap.get("User");
                        String password = paramsMap.get("Password");
                        String serviceURL = config.getServiceURL();
                        String safePlate = plate.replaceAll(" ", "");
                        String url = null;
                        if ((Feature.isFeatureAllowed(Feature.PARK_PLATE_SEARCH_ZERO_OR_O)) && (safePlate.contains("O") || safePlate.contains("0"))) {
                            lookUpCaleWithZeroAndO(progressDialog, user, password, serviceURL, safePlate, "Cale2");
                        } else {
                            url = serviceURL + safePlate + "/5";
                            ApiRequest service = RetrofitServiceGenerator.createService(ApiRequest.class, user, password);
                            service._serchPlate2(url).enqueue(new retrofit2.Callback<ArrayOfValidParkingData>() {
                                @Override
                                public void onResponse(@NonNull Call<ArrayOfValidParkingData> call, @NonNull Response<ArrayOfValidParkingData> response) {
                                    responseSize[0]++;
                                    if (response.isSuccessful() && response.code() == 200) {
                                        assert response.body() != null;
                                        ArrayOfValidParkingData body = response.body();
                                        List<ValidParkingData1> validParkingData = body.getValidParkingData();
                                        if (validParkingData != null && validParkingData.size() > 0) {
                                            ValidParkingData1 validParkingData1 = null;
                                            for (int j = 0; j < validParkingData.size(); j++) {
                                                validParkingData1 = validParkingData.get(j);
                                            }
                                            if (Feature.isFeatureAllowed(Feature.PARK_SEARCH_TRACK)) {
                                                try {
                                                    MobileNowLog log = new MobileNowLog();
                                                    log.setCustId(TPApp.custId);
                                                    log.setDeviceId(TPApp.deviceId);
                                                    log.setUserId(TPApp.userId);
                                                    log.setRequestDate(new Date());
                                                    log.setPlate_number(plate);
                                                    log.setRequestParams("Cale2 Request: " + plate);
                                                    log.setServiceMode(config.getRequestMode());
                                                    if (validParkingData1 != null) {
                                                        log.setResponseText("True " + plate);
                                                    } else {
                                                        log.setResponseText("Plate not found.");
                                                    }

                                                    MobileNowLog.insertMobileNowLog(log);
                                                    CSVUtility.writeMobileLogCSV(log);

                                                    ArrayList<MobileNowLog> logs = new ArrayList<>();
                                                    logs.add(log);
                                                    WriteTicketNetworkCalls.sendMobileNogLogs(logs);
                                                } catch (Exception e) {
                                                    //log.error("Error " + TPUtility.getPrintStackTrace(e));
                                                    e.printStackTrace();
                                                }
                                            }

                                            if (validParkingData1 != null) {
                                                progressDialog.isShowing();
                                                progressDialog.dismiss();
                                                // _displayMsgCalePlate(validParkingData1, plate);
                                                _displayMsgCalePlateMultiple(validParkingData, plate);

                                            } else {
                                                if (config1.size() == responseSize[0]) {
                                                    responseSize[0] = 0;
                                                    progressDialog.isShowing();
                                                    progressDialog.dismiss();
                                                    processPlateLookupQueue(plate);
                                                }
                                            }

                                        } else {
                                            if (Feature.isFeatureAllowed(Feature.PARK_SEARCH_TRACK)) {
                                                try {
                                                    MobileNowLog log = new MobileNowLog();
                                                    log.setCustId(TPApp.custId);
                                                    log.setDeviceId(TPApp.deviceId);
                                                    log.setUserId(TPApp.userId);
                                                    log.setRequestDate(new Date());
                                                    log.setPlate_number(plate);
                                                    log.setRequestParams("Cale2 Request: " + plate);
                                                    log.setServiceMode(config.getRequestMode());

                                                    log.setResponseText("Plate not found.");


                                                    MobileNowLog.insertMobileNowLog(log);
                                                    CSVUtility.writeMobileLogCSV(log);

                                                    ArrayList<MobileNowLog> logs = new ArrayList<>();
                                                    logs.add(log);
                                                    WriteTicketNetworkCalls.sendMobileNogLogs(logs);
                                                } catch (Exception e) {
                                                    //log.error("Error " + TPUtility.getPrintStackTrace(e));
                                                    e.printStackTrace();
                                                }
                                            }
                                            progressDialog.isShowing();
                                            progressDialog.dismiss();
                                            processPlateLookupQueue(plate);
                                        }
                                    } else {
                                        if (Feature.isFeatureAllowed(Feature.PARK_SEARCH_TRACK)) {
                                            try {
                                                MobileNowLog log = new MobileNowLog();
                                                log.setCustId(TPApp.custId);
                                                log.setDeviceId(TPApp.deviceId);
                                                log.setUserId(TPApp.userId);
                                                log.setRequestDate(new Date());
                                                log.setPlate_number(plate);
                                                log.setRequestParams("Cale2 Request: " + plate);
                                                log.setServiceMode(config.getRequestMode());

                                                log.setResponseText("Plate not found.");


                                                MobileNowLog.insertMobileNowLog(log);
                                                CSVUtility.writeMobileLogCSV(log);

                                                ArrayList<MobileNowLog> logs = new ArrayList<>();
                                                logs.add(log);
                                                WriteTicketNetworkCalls.sendMobileNogLogs(logs);
                                            } catch (Exception e) {
                                                //log.error("Error " + TPUtility.getPrintStackTrace(e));
                                                e.printStackTrace();
                                            }
                                        }
                                        progressDialog.isShowing();
                                        progressDialog.dismiss();
                                        processPlateLookupQueue(plate);
                                    }
                                }

                                @Override
                                public void onFailure(@NonNull Call<ArrayOfValidParkingData> call, @NonNull Throwable t) {
                                    progressDialog.isShowing();
                                    progressDialog.dismiss();
                                    processPlateLookupQueue(plate);
                                    System.out.println(t.getMessage());
                                }
                            });
                        }
                    }

                } else {
                    processPlateLookupQueue(plate);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    /**
     * Code by Sanjiv
     * function will check plate history with replace zero to o and o to Zero
     */
    public void lookUpCaleWithZeroAndO(ProgressDialog progressDialog, String uName, String uPass, String URL, String plate, String name) {
        try {
            String finalUrl = URL + plate + "/5";
            String plate2 = "";
            if (plate.contains("O")) {
                plate2 = plate.replace("O", "0");
            } else if (plate.contains("0")) {
                plate2 = plate.replace("0", "O");
            } else {
                plate2 = plate;
            }
            String finalUrl2 = URL + plate2 + "/5";
            ApiRequest api = RetrofitServiceGenerator.createServiceRx(ApiRequest.class, uName, uPass);
            Observable<ArrayOfValidParkingData> arrayOfPlaveSearchObservable = api._searchPlateCALEWithZero(finalUrl);
            Observable<ArrayOfValidParkingData> arrayOfPlaveSearchObservable1 = api._searchPlateCALEWithoutZero(finalUrl2);
            Observer<List<Object>> observer = Observable.zip(arrayOfPlaveSearchObservable.subscribeOn(Schedulers.io()),
                            arrayOfPlaveSearchObservable1.subscribeOn(Schedulers.io()),
                            (response, response1) -> {
                                List<Object> list = new ArrayList<>();
                                list.add(response);
                                list.add(response1);

                                return list;
                            }
                    ).doOnError(throwable -> logpm.error(TPUtility.getPrintStackTrace(throwable)))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new Observer<List<Object>>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(List<Object> objects) {
                            Log.i("TAG", "onComplete: " + "onNext Successfully");
                            int responseSize = 0;
                            for (int i = 0; i < objects.size(); i++) {
                                responseSize++;
                                ArrayOfValidParkingData array = (ArrayOfValidParkingData) objects.get(i);
                                if (array.getValidParkingData() != null && array.getValidParkingData().size() > 0) {
                                    ValidParkingData1 validParkingData1 = null;
                                    List<ValidParkingData1> aa = new ArrayList<>();
                                    for (int j = 0; j < array.getValidParkingData().size(); j++) {
                                        validParkingData1 = array.getValidParkingData().get(j);
                                        aa.add(validParkingData1);
                                    }
                                    if (validParkingData1 != null) {
                                        if (Feature.isFeatureAllowed(Feature.PARK_SEARCH_TRACK)) {
                                            try {
                                                MobileNowLog log = new MobileNowLog();
                                                log.setCustId(TPApp.custId);
                                                log.setDeviceId(TPApp.deviceId);
                                                log.setUserId(TPApp.userId);
                                                log.setRequestDate(new Date());
                                                log.setServiceMode("Prod");
                                                log.setPlate_number(validParkingData1.getCode());
                                                log.setRequestParams(name + " Request: " + validParkingData1.getCode());
                                                log.setResponseText("True " + validParkingData1.getCode());
                                                //log.setResponseText("True " + plate);
                                                MobileNowLog.insertMobileNowLog(log);
                                                CSVUtility.writeMobileLogCSV(log);
                                                ArrayList<MobileNowLog> logs = new ArrayList<>();
                                                logs.add(log);
                                                WriteTicketNetworkCalls.sendMobileNogLogs(logs);
                                            } catch (Exception e) {
                                                //log.error("Error " + TPUtility.getPrintStackTrace(e));
                                                e.printStackTrace();
                                            }
                                        }
                                        //_displayMsgCalePlate(validParkingData1, plate);
                                        _displayMsgCalePlateMultiple(aa, plate);
                                        break;
                                    }

                                } else {
                                    if (objects.size() == responseSize) {
                                        responseSize = 0;
                                        if (Feature.isFeatureAllowed(Feature.PARK_SEARCH_TRACK)) {
                                            try {
                                                MobileNowLog log = new MobileNowLog();
                                                log.setCustId(TPApp.custId);
                                                log.setDeviceId(TPApp.deviceId);
                                                log.setUserId(TPApp.userId);
                                                log.setRequestDate(new Date());
                                                log.setPlate_number(plate);
                                                log.setServiceMode("Prod");
                                                log.setRequestParams(name + " Request: " + plate);

                                                log.setResponseText("Plate not found.");

                                                MobileNowLog.insertMobileNowLog(log);
                                                CSVUtility.writeMobileLogCSV(log);

                                                ArrayList<MobileNowLog> logs = new ArrayList<>();
                                                logs.add(log);
                                                WriteTicketNetworkCalls.sendMobileNogLogs(logs);
                                            } catch (Exception e) {
                                                //log.error("Error " + TPUtility.getPrintStackTrace(e));
                                                e.printStackTrace();
                                            }
                                        }


                                        processPlateLookupQueue(plate);
                                    }
                                }

                            }
                            progressDialog.dismiss();
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.i("TAG", "onComplete: " + "onError Successfully");
                            progressDialog.dismiss();
                            logpm.error("lookUpCaleWithZeroAndO " + e.getMessage());
                            processPlateLookupQueue(plate);
                        }

                        @Override
                        public void onComplete() {
                            Log.i("TAG", "onComplete: " + "onComplete Successfully");
                            progressDialog.dismiss();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Code by Sanjiv
     * function will check plate history with replace zero to o and o to Zero
     */
    public void lookUpCale2WithZeroAndO(ProgressDialog progressDialog, String uName, String uPass, String URL, String plate) {
        try {
            String finalUrl = URL + plate + "/5";
            String plate2 = "";
            if (plate.contains("O")) {
                plate2 = plate.replace("O", "0");
            } else if (plate.contains("0")) {
                plate2 = plate.replace("0", "O");
            } else {
                plate2 = plate;
            }
            String finalUrl2 = URL + plate2 + "/5";
            ApiRequest api = RetrofitServiceGenerator.createServiceRx(ApiRequest.class, uName, uPass);
            Observable<ArrayOfValidParkingData> arrayOfPlaveSearchObservable = api._searchPlateCALEWithZero(finalUrl);
            Observable<ArrayOfValidParkingData> arrayOfPlaveSearchObservable1 = api._searchPlateCALEWithoutZero(finalUrl2);
            Observer<List<Object>> observer = Observable.zip(arrayOfPlaveSearchObservable.subscribeOn(Schedulers.io()),
                            arrayOfPlaveSearchObservable1.subscribeOn(Schedulers.io()),
                            (response, response1) -> {
                                List<Object> list = new ArrayList<>();
                                list.add(response);
                                list.add(response1);

                                return list;
                            }
                    ).doOnError(throwable -> logpm.error(TPUtility.getPrintStackTrace(throwable)))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new Observer<List<Object>>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(List<Object> objects) {
                            Log.i("TAG", "onComplete: " + "onNext Successfully");
                            int responseSize = 0;
                            for (int i = 0; i < objects.size(); i++) {
                                responseSize++;
                                ValidParkingData1 o = (ValidParkingData1) objects.get(i);
                                if (o != null) {
                                    if (Feature.isFeatureAllowed(Feature.PARK_SEARCH_TRACK)) {
                                        try {
                                            MobileNowLog log = new MobileNowLog();
                                            log.setCustId(TPApp.custId);
                                            log.setDeviceId(TPApp.deviceId);
                                            log.setUserId(TPApp.userId);
                                            log.setRequestDate(new Date());
                                            log.setPlate_number(plate);
                                            log.setRequestParams("Cale2 Request: " + plate);

                                            //log.setResponseText("True " + plate);

                                            MobileNowLog.insertMobileNowLog(log);
                                            CSVUtility.writeMobileLogCSV(log);

                                            ArrayList<MobileNowLog> logs = new ArrayList<>();
                                            logs.add(log);
                                            WriteTicketNetworkCalls.sendMobileNogLogs(logs);
                                        } catch (Exception e) {
                                            //log.error("Error " + TPUtility.getPrintStackTrace(e));
                                            e.printStackTrace();
                                        }
                                    }
                                    //_displayMsgCalePlate(o.getValidParkingData(), plate);
                                    //_displayMsgCalePlate(, plate);
                                    break;
                                } else {
                                    if (objects.size() == responseSize) {
                                        responseSize = 0;
                                        if (Feature.isFeatureAllowed(Feature.PARK_SEARCH_TRACK)) {
                                            try {
                                                MobileNowLog log = new MobileNowLog();
                                                log.setCustId(TPApp.custId);
                                                log.setDeviceId(TPApp.deviceId);
                                                log.setUserId(TPApp.userId);
                                                log.setRequestDate(new Date());
                                                log.setPlate_number(plate);
                                                log.setRequestParams("Cale2 Request: " + plate);

                                                //.setResponseText("True " + plate);

                                                MobileNowLog.insertMobileNowLog(log);
                                                CSVUtility.writeMobileLogCSV(log);

                                                ArrayList<MobileNowLog> logs = new ArrayList<>();
                                                logs.add(log);
                                                WriteTicketNetworkCalls.sendMobileNogLogs(logs);
                                            } catch (Exception e) {
                                                //log.error("Error " + TPUtility.getPrintStackTrace(e));
                                                e.printStackTrace();
                                            }
                                        }


                                        processPlateLookupQueue(plate);
                                    }
                                }

                            }
                            progressDialog.dismiss();
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.i("TAG", "onComplete: " + "onError Successfully");
                            progressDialog.dismiss();
                            logpm.error("lookUpCaleWithZeroAndO " + e.getMessage());
                            processPlateLookupQueue(plate);
                        }

                        @Override
                        public void onComplete() {
                            Log.i("TAG", "onComplete: " + "onComplete Successfully");
                            progressDialog.dismiss();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Code by Sanjiv
     * Fetch data from vendor server
     *
     * @param plate
     */
    public void lookupParkeon(final String plate) {
        if (activity.isServiceAvailable) {
            try {
                if (!(TPApp.enableParkeon && Feature.isFeatureAllowed(Feature.PARKEON))) {
                    return;
                }
                String zone_id = preference.getString(TPConstant.PARKEON_ZONE_ID);
                if (zone_id != null) {
                    task = new LookupParkeonPlate(plate, zone_id).execute();
                } else {
                    //processPlateLookupQueue(plate);
                    Intent intent = new Intent(activity, ParkeonControlGroup.class);
                    TPConstant.parkeon = true;
                    intent.putExtra("from", "lookup");
                    activity.startActivity(intent);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void lookupDigitalPaytech(final String plate) {
        if (activity.isServiceAvailable) {
            if (!(TPApp.enableDPT && Feature.isFeatureAllowed(Feature.DIGITAL_PAYTECH))) {
                return;
            }
            this.plate = plate;
            final VendorServiceConfig config = VendorService.getServiceConfigT2(VendorService.DIGITALPAYTECH_PLATEINFO,
                    TPApp.deviceId);
            if (config != null && activity.isServiceAvailable) {
                TPAsyncTask task = new TPAsyncTask(activity, "Processing T2...");
                serviceName = "T2";
                task.execute(new TPTask() {
                    @Override
                    public void execute() {
                        plateInfoResponse = null;
                        try {
                            String safePlate = plate.replaceAll(" ", "");
                            String params = config.getParams();

                            PlateInfoService service = new PlateInfoService(new IWsdl2CodeEvents() {
                                @Override
                                public void Wsdl2CodeStartedRequest() {
                                }

                                @Override
                                public void Wsdl2CodeFinishedWithException(Exception ex) {
                                    activity.log.error("PlateInfoService Error " + TPUtility.getPrintStackTrace(ex));
                                }

                                @Override
                                public void Wsdl2CodeFinished(String methodName, Object Data) {
                                }

                                @Override
                                public void Wsdl2CodeEndedRequest() {
                                }
                            });

                            service.addAuthHeader(config.getUsername(), config.getPassword());
                            service.setUrl(config.getServiceURL());

                            PlateInfoByPlateRequest plateInfoByPlateRequest = new PlateInfoByPlateRequest();
                            plateInfoByPlateRequest.plateNumber = safePlate;
                            plateInfoByPlateRequest.token = config.getParamsMap().get("token");
                            plateInfoResponse = service.getPlateInfo(plateInfoByPlateRequest);

                            new RequestLogTask(params, config.getRequestMode(),
                                    plateInfoResponse != null ? plateInfoResponse.getInnerText() : "").execute();

                        } catch (Exception e) {
                            activity.log.error(TPUtility.getPrintStackTrace(e));
                        }

                        Bundle data = new Bundle();
                        if (plateInfoResponse != null) {
                            data.putString(RESPONSE_DATA, plateInfoResponse.toString());
                        }

                        Message msg = obtainMessage();
                        msg.what = LOOKUP_DIGITALPAYTECH_PLATE;
                        msg.setData(data);
                        sendMessage(msg);
                    }
                });
            } else {
                processPlateLookupQueue(plate);
            }
        }
    }

    private void lookupIPSGroup(final String plate) {
        if (activity.isServiceAvailable) {
            if (!(TPApp.enableIPS && Feature.isFeatureAllowed(Feature.IPS_GROUP))) {
                return;
            }

            this.plate = plate;
            final VendorServiceConfig config = VendorService.getServiceConfig(VendorService.IPS_PLATEINFO, TPApp.deviceId);
            if (config != null && activity.isServiceAvailable) {
                TPAsyncTask task = new TPAsyncTask(activity, "Processing IPS Group...");
                serviceName = "IPS";
                task.execute(new TPTask() {
                    @Override
                    public void execute() {
                        plateInfoResponse = null;
                        String response = "";
                        try {
                            String safePlate = plate.replaceAll(" ", "");
                            String params = config.getParams();
                            String url = config.getServiceURL();
                            String token = config.getParamsMap().get("token");

                            response = IPSQuery.getPlateStatus(url, token, safePlate);
                            new RequestLogTask(params, config.getRequestMode(), response).execute();

                        } catch (Exception e) {
                            activity.log.error(TPUtility.getPrintStackTrace(e));
                        }

                        Bundle data = new Bundle();
                        if (response != null) {
                            data.putString(RESPONSE_DATA, response);
                        }

                        Message msg = obtainMessage();
                        msg.what = LOOKUP_IPS_PLATE;
                        msg.setData(data);
                        sendMessage(msg);
                    }
                });
            } else {
                processPlateLookupQueue(plate);
            }
        }
    }

    private void lookupMobileNow(final String plate) {
        if (activity.isServiceAvailable) {
            if (!(TPApp.enableMobileNow && Feature.isFeatureAllowed(Feature.MOBILE_NOW))) {
                return;
            }

            final VendorServiceConfig config = VendorService.getServiceConfig(VendorService.MOBILE_NOW_PLATE_CHECK,
                    TPApp.deviceId);
            if (config != null && activity.isServiceAvailable) {
                TPAsyncTask task = new TPAsyncTask(activity, "Processing MobileNow...");
                task.execute(new TPTask() {
                    @Override
                    public void execute() {
                        String safePlate = plate.replaceAll(" ", "");
                        String params = config.getParams();
                        params = params.replaceAll("\\{PLATE\\}", safePlate);

                        HttpRequestTask task = new HttpRequestTask(LOOKUP_MOBILE_NOW_PLATE, new Bundle(), params);
                        task.setRequestMode(config.getRequestMode());
                        task.executeTask(config.getServiceURL() + "?" + params);
                        serviceName = "MobileNow";
                        this.asyncTask = task;
                    }
                });
            } else {
                processPlateLookupQueue(plate);
            }
        }
    }

    private void lookupProgressive(final String plate) {
        if (activity.isServiceAvailable) {
            if (!(TPApp.enableProgressive && Feature.isFeatureAllowed(Feature.PROGRESSIVE))) {
                return;
            }
            this.plate = plate;
            final VendorServiceConfig config = VendorService.getServiceConfig(VendorService.PROGRASSIVE_VALIDPERMIT,
                    TPApp.deviceId);
            if (config != null && activity.isServiceAvailable) {

                TPAsyncTask task = new TPAsyncTask(activity, "Processing Progressive...");
                serviceName = "Progressive";
                task.execute(new TPTask() {
                    @Override
                    public void execute() {
                        boolean result = false;
                        String safePlate = plate.replaceAll(" ", "");
                        try {

                            String configURL = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                                    "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ns1=\"http://tempuri.org/\">\n" +
                                    "  <SOAP-ENV:Body>\n" +
                                    "    <ns1:ValidPermit>\n" +
                                    "      <ns1:PermitID>0</ns1:PermitID>\n" +
                                    "      <ns1:LicensePlate>" + safePlate + "</ns1:LicensePlate>\n" +
                                    "    </ns1:ValidPermit>\n" +
                                    "  </SOAP-ENV:Body>\n" +
                                    "</SOAP-ENV:Envelope>";
                            String response = TPUtility.requestUrl(configURL);

                            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                            InputSource is = new InputSource();
                            is.setCharacterStream(new StringReader(response));
                            String requiredVaule = "";
                            Document doc = db.parse(is);
                            NodeList nodes = doc.getElementsByTagName("ValidPermitResponse");
                            for (int i = 0; i < nodes.getLength(); i++) {
                                Element element = (Element) nodes.item(i);

                                NodeList name = element.getElementsByTagName("ValidPermitResult");
                                Element line = (Element) name.item(0);
                                requiredVaule = getCharacterDataFromElement(line);
                            }
                            result = Boolean.parseBoolean(requiredVaule);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


					/*Service1 service = new Service1(new com.ticketpro.vendors.progressive.IWsdl2CodeEvents() {
						@Override
						public void Wsdl2CodeStartedRequest() {
						}

						@Override
						public void Wsdl2CodeFinishedWithException(Exception ex) {
						}

						@Override
						public void Wsdl2CodeFinished(String methodName, Object Data) {
						}

						@Override
						public void Wsdl2CodeEndedRequest() {
						}
					});


					service.setUrl(config.getServiceURL());
					boolean result = service.ValidPermit(0, safePlate);*/

                        Bundle data = new Bundle();
                        data.putString(RESPONSE_DATA, String.valueOf(result));
                        data.putBoolean("IsValidPermit", result);

                        Message msg = obtainMessage();
                        msg.what = LOOKUP_PROGRESSIVE_PLATE;
                        msg.setData(data);

                        sendMessage(msg);

                        new RequestLogTask(config.getParams(), config.getRequestMode(),
                                "Plate=" + safePlate + ", Result=" + result).execute();
                    }
                });
            } else {
                processPlateLookupQueue(plate);
            }
        }
    }

    private void lookupCSLookup(final String plate, final String state) {
        if (activity.isServiceAvailable) {
            if (!Feature.isFeatureAllowed(Feature.CS_LOOKUP)) {
                return;
            }
            this.plate = plate;
            TPAsyncTask task = new TPAsyncTask(activity, "Processing CSLookup...");
            task.execute(new TPTask() {
                @Override
                public void execute() {
                    String FICEGROUP = "";
                    String FICE = "";
                    String params = "";
                    String serviceURL = "https://www.credentials-inc.com/cgi-bin/gacgiturbo.pgm?";
                    VendorServiceConfig config = VendorService.getServiceConfig(VendorService.CS_LOOKUP, TPApp.deviceId);
                    if (config != null) {
                        FICE = config.getParamsMap().get("FICE");
                        FICEGROUP = config.getParamsMap().get("FICEGROUP");
                        serviceURL = config.getServiceURL();
                    } else {
                        params = Feature.getFeatureValue(Feature.CS_LOOKUP);
                        if (params == null || params.isEmpty()) {
                            FICE = "";
                        } else {
                            String[] tokens = params.split(",");
                            if (tokens.length > 1) {
                                FICEGROUP = tokens[1];
                            }
                            FICE = tokens[0];
                        }
                    }

                    if (FICE == null || FICE.equals("")) {
                        FICE = "XX1282";
                    }

                    if (FICEGROUP == null) {
                        FICEGROUP = "";
                    }

                    try {
                        String safePlate = plate.replaceAll(" ", "");
                        String SHA1 = TPUtility.generateSHA1Hash(FICE + safePlate);
                        String encodedParams = "FICE=" + URLEncoder.encode(FICE, "UTF-8") + "&FICEGROUP="
                                + URLEncoder.encode(FICEGROUP, "UTF-8") + "&PLATE=" + URLEncoder.encode(safePlate, "UTF-8")
                                + "&STATE=" + URLEncoder.encode(state, "UTF-8") + "&SHAHASH="
                                + URLEncoder.encode(SHA1, "UTF-8");

                        TLSHttpRequestTask task = new TLSHttpRequestTask(LOOKUP_CS_PLATE, new Bundle(), encodedParams);
                        task.executeTask(serviceURL + encodedParams);
                        this.asyncTask = task;
                    } catch (Exception e) {
                        activity.log.error(TPUtility.getPrintStackTrace(e));
                    }
                }
            });
        }
    }

    private void getCubtrackZone(String plate){
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Looking cubtrac zone");
        progressDialog.setCancelable(false);
        progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        progressDialog.show();

        if (activity.isServiceAvailable) {
            if (!(TPApp.enableCubtrac && Feature.isFeatureAllowed(Feature.PARK_CUBTRAC))) {
                progressDialog.dismiss();
                return;
            }

      /*  if (activity.isServiceAvailable) {
            if (!(TPApp.enableParkMobile && Feature.isFeatureAllowed(Feature.PARK_MOBILE))) {
                return;
            }
*/
            this.plate = plate;
            final VendorServiceConfig config = VendorService.getServiceConfig(VendorService.CUBTRAC_ZONE_SEARCH, TPApp.deviceId, "/");
            if (config==null){
                progressDialog.dismiss();
                return;
            }
            Map<String, String> paramsMap = config.getParamsMap();
            String user = paramsMap.get("User");
            String password = paramsMap.get("Password");
            String serviceURL = config.getServiceURL();
            String token = paramsMap.get("token");
            ApiRequest service = ServiceGeneratorCubTrac.createService(ApiRequest.class);
            service.cubtracGetZone(serviceURL,token).enqueue(new retrofit2.Callback<List<CubTracZone>>() {
                @Override
                public void onResponse(Call<List<CubTracZone>> call, Response<List<CubTracZone>> response) {
                    progressDialog.dismiss();
                    if (response.isSuccessful()&& response.code()==200) {
                        List<CubTracZone> zoneList = response.body();
                        if (zoneList!=null && zoneList.size()>0){
                            WriteTicketActivity.ItemAdapter adapter = new WriteTicketActivity.ItemAdapter(activity, zoneList);
                            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                            builder.setTitle("Select Zone");
                            builder.setAdapter(adapter, (dialog, which) -> {
                                CubTracZone selectedItem = zoneList.get(which);
                                System.out.println("Selected Item: " + selectedItem.getName() + " - " + selectedItem.getId());
                                preference.putString("ZONE_ID", selectedItem.getAssignedId());
                                preference.putString("ZONE_NAME",selectedItem.getName());
                                lookupCubtrac(plate,selectedItem.getAssignedId());

                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }else {
                            lookupPassportParking(plate);
                        }
                    }else {
                        lookupPassportParking(plate);
                    }
                }

                @Override
                public void onFailure(Call<List<CubTracZone>> call, Throwable t) {
                    progressDialog.dismiss();
                    processPlateLookupQueue(plate);
                }
            });

        }else {
            progressDialog.dismiss();
            processPlateLookupQueue(plate);
        }

    }
    private void lookupCubtrac(String plate, String id){
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Looking Curbtrac");
        progressDialog.setCancelable(false);
        progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        progressDialog.show();
        //added by saurav on 13_12_2024
        if (activity.isServiceAvailable) {
            if (!(TPApp.enableCubtrac && Feature.isFeatureAllowed(Feature.PARK_CUBTRAC))) {
                progressDialog.dismiss();
                return;
            }
        // removed by saurav 0n 13_12_2024
       /* if (activity.isServiceAvailable) {
            if (!(TPApp.enableParkMobile && Feature.isFeatureAllowed(Feature.PARK_MOBILE))) {
                return;
            }
*/
            this.plate = plate;
            final VendorServiceConfig config = VendorService.getServiceConfig(VendorService.CUBTRAC_PLATE_SEARCH, TPApp.deviceId, "/");
            if (config==null){
                progressDialog.dismiss();
                return;
            }
            Map<String, String> paramsMap = config.getParamsMap();
            String user = paramsMap.get("User");
            String password = paramsMap.get("Password");
            String serviceURL = config.getServiceURL();
            String token = paramsMap.get("token");

            CubtracRequest cubtracRequest = new CubtracRequest();
            cubtracRequest.setLicensePlate(plate);
            cubtracRequest.setZone(id);
            ApiRequest apiRequest = ServiceGeneratorCubTrac.createService(ApiRequest.class);
            apiRequest.cubtracPlatelookup(serviceURL,token,cubtracRequest).enqueue(new retrofit2.Callback<List<CubtracResponse>>() {
                @Override
                public void onResponse(Call<List<CubtracResponse>> call, Response<List<CubtracResponse>> response) {
                    progressDialog.dismiss();
                    if (response.isSuccessful()&& response.code()==200){
                        List<CubtracResponse> body = response.body();
                        if (body.size()>0){
                            if (Feature.isFeatureAllowed(Feature.PARK_SEARCH_TRACK)) {
                                try {
                                    MobileNowLog log = new MobileNowLog();
                                    log.setCustId(TPApp.custId);
                                    log.setDeviceId(TPApp.deviceId);
                                    log.setUserId(TPApp.userId);
                                    log.setRequestDate(new Date());
                                    log.setServiceMode("Prod");
                                    log.setPlate_number(plate);
                                    log.setRequestParams("Curbtrac " + " Request: " +plate);
                                    log.setResponseText("True " + plate);
                                    //log.setResponseText("True " + plate);
                                    MobileNowLog.insertMobileNowLog(log);
                                    CSVUtility.writeMobileLogCSV(log);
                                    ArrayList<MobileNowLog> logs = new ArrayList<>();
                                    logs.add(log);
                                    WriteTicketNetworkCalls.sendMobileNogLogs(logs);
                                } catch (Exception e) {
                                    //log.error("Error " + TPUtility.getPrintStackTrace(e));
                                    e.printStackTrace();
                                }
                            }
                            CubtracResponse cubtracResponse = body.get(0);
                            __displayCurbtracPlateInfo(cubtracResponse);
                        }else {
                            if (Feature.isFeatureAllowed(Feature.PARK_SEARCH_TRACK)) {
                                try {
                                    MobileNowLog log = new MobileNowLog();
                                    log.setCustId(TPApp.custId);
                                    log.setDeviceId(TPApp.deviceId);
                                    log.setUserId(TPApp.userId);
                                    log.setRequestDate(new Date());
                                    log.setServiceMode("Prod");
                                    log.setPlate_number(plate);
                                    log.setRequestParams("Curbtrac " + " Request: " +plate);
                                    log.setResponseText("False " + plate);
                                    //log.setResponseText("True " + plate);
                                    MobileNowLog.insertMobileNowLog(log);
                                    CSVUtility.writeMobileLogCSV(log);
                                    ArrayList<MobileNowLog> logs = new ArrayList<>();
                                    logs.add(log);
                                    WriteTicketNetworkCalls.sendMobileNogLogs(logs);
                                } catch (Exception e) {
                                    //log.error("Error " + TPUtility.getPrintStackTrace(e));
                                    e.printStackTrace();
                                }
                            }
                            processPlateLookupQueue(plate);
                        }

                    }else {
                        processPlateLookupQueue(plate);
                    }
                }

                @Override
                public void onFailure(Call<List<CubtracResponse>> call, Throwable t) {
                    if (Feature.isFeatureAllowed(Feature.PARK_SEARCH_TRACK)) {
                        try {
                            MobileNowLog log = new MobileNowLog();
                            log.setCustId(TPApp.custId);
                            log.setDeviceId(TPApp.deviceId);
                            log.setUserId(TPApp.userId);
                            log.setRequestDate(new Date());
                            log.setServiceMode("Prod");
                            log.setPlate_number(plate);
                            log.setRequestParams("Curbtrac " + " Request: " +plate);
                            log.setResponseText("fail " + t.getMessage());
                            //log.setResponseText("True " + plate);
                            MobileNowLog.insertMobileNowLog(log);
                            CSVUtility.writeMobileLogCSV(log);
                            ArrayList<MobileNowLog> logs = new ArrayList<>();
                            logs.add(log);
                            WriteTicketNetworkCalls.sendMobileNogLogs(logs);
                        } catch (Exception e) {
                            //log.error("Error " + TPUtility.getPrintStackTrace(e));
                            e.printStackTrace();
                        }
                    }
                    progressDialog.dismiss();
                    processPlateLookupQueue(plate);
                }
            });

        }else {
            progressDialog.dismiss();
            processPlateLookupQueue(plate);
        }

    }

    private void lookupOffstreet(String plate) {
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Looking OffStreet");
        progressDialog.setCancelable(false);
        progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        progressDialog.show();

        if (activity.isServiceAvailable) {
            if (!(TPApp.enableParkMobile && Feature.isFeatureAllowed(Feature.PARK_MOBILE))) {
                return;
            }

            this.plate = plate;
            final VendorServiceConfig config = VendorService.getServiceConfig(VendorService.OFFSTREET_PLATE_SEARCH, TPApp.deviceId, "/");
            Map<String, String> paramsMap = config.getParamsMap();
            String user = paramsMap.get("User");
            String password = paramsMap.get("Password");
            String serviceURL = config.getServiceURL();
            String token = paramsMap.get("token");

            OffstreetReqest cubtracRequest = new OffstreetReqest();
            cubtracRequest.setLocationId("cesarchavez");
            cubtracRequest.setPlate(plate);
            ApiRequest apiRequest = ServiceOffstreet.createService(ApiRequest.class);
            apiRequest.offstreetPlatelookup(serviceURL,token,cubtracRequest).enqueue(new retrofit2.Callback<OffStreetList>() {
                @Override
                public void onResponse(Call<OffStreetList> call, Response<OffStreetList> response) {
                    progressDialog.dismiss();

                    if (response.isSuccessful() && response.code()==200){
                        List<OffStreet> sessions = response.body().getSessions();
                        if (sessions!=null && sessions.size()>0){
                            if (Feature.isFeatureAllowed(Feature.PARK_SEARCH_TRACK)) {
                                try {
                                    MobileNowLog log = new MobileNowLog();
                                    log.setCustId(TPApp.custId);
                                    log.setDeviceId(TPApp.deviceId);
                                    log.setUserId(TPApp.userId);
                                    log.setRequestDate(new Date());
                                    log.setServiceMode("Prod");
                                    log.setPlate_number(plate);
                                    log.setRequestParams("Offstreet " + " Request: " +plate);
                                    log.setResponseText("True " + plate);
                                    //log.setResponseText("True " + plate);
                                    MobileNowLog.insertMobileNowLog(log);
                                    CSVUtility.writeMobileLogCSV(log);
                                    ArrayList<MobileNowLog> logs = new ArrayList<>();
                                    logs.add(log);
                                    WriteTicketNetworkCalls.sendMobileNogLogs(logs);
                                } catch (Exception e) {
                                    //log.error("Error " + TPUtility.getPrintStackTrace(e));
                                    e.printStackTrace();
                                }
                            }
                            OffStreet offStreet = sessions.get(0);
                            ___displayOffstreet(offStreet);
                        }else {
                            progressDialog.dismiss();
                            processPlateLookupQueue(plate);
                            if (Feature.isFeatureAllowed(Feature.PARK_SEARCH_TRACK)) {
                                try {
                                    MobileNowLog log = new MobileNowLog();
                                    log.setCustId(TPApp.custId);
                                    log.setDeviceId(TPApp.deviceId);
                                    log.setUserId(TPApp.userId);
                                    log.setRequestDate(new Date());
                                    log.setServiceMode("Prod");
                                    log.setPlate_number(plate);
                                    log.setRequestParams("Offstreet " + " Request: " +plate);
                                    log.setResponseText("false " + plate);
                                    //log.setResponseText("True " + plate);
                                    MobileNowLog.insertMobileNowLog(log);
                                    CSVUtility.writeMobileLogCSV(log);
                                    ArrayList<MobileNowLog> logs = new ArrayList<>();
                                    logs.add(log);
                                    WriteTicketNetworkCalls.sendMobileNogLogs(logs);
                                } catch (Exception e) {
                                    //log.error("Error " + TPUtility.getPrintStackTrace(e));
                                    e.printStackTrace();
                                }
                            }
                        }

                    }
                }

                @Override
                public void onFailure(Call<OffStreetList> call, Throwable t) {
                    progressDialog.dismiss();
                    processPlateLookupQueue(plate);
                    if (Feature.isFeatureAllowed(Feature.PARK_SEARCH_TRACK)) {
                        try {
                            MobileNowLog log = new MobileNowLog();
                            log.setCustId(TPApp.custId);
                            log.setDeviceId(TPApp.deviceId);
                            log.setUserId(TPApp.userId);
                            log.setRequestDate(new Date());
                            log.setServiceMode("Prod");
                            log.setPlate_number(plate);
                            log.setRequestParams("OffStreet " + " Request: " +plate);
                            log.setResponseText("fail " + t.getMessage());
                            //log.setResponseText("True " + plate);
                            MobileNowLog.insertMobileNowLog(log);
                            CSVUtility.writeMobileLogCSV(log);
                            ArrayList<MobileNowLog> logs = new ArrayList<>();
                            logs.add(log);
                            WriteTicketNetworkCalls.sendMobileNogLogs(logs);
                        } catch (Exception e) {
                            //log.error("Error " + TPUtility.getPrintStackTrace(e));
                            e.printStackTrace();
                        }
                    }
                }
            });

        }else {
            progressDialog.dismiss();
            processPlateLookupQueue(plate);
        }

    }

    private void ___displayOffstreet(OffStreet offStreet){
        activity.runOnUiThread(() -> {
            try {
                StringBuilder message = new StringBuilder();
                StringBuilder values = new StringBuilder();

                View view = LayoutInflater.from(activity.getBaseContext()).inflate(R.layout.item_view, null);
                TextView headerTV = view.findViewById(R.id.headerTV);
                TextView valueTV = view.findViewById(R.id.valueTV);

                message.append("Plate" + "\n");
                message.append("State" + "\n");

                message.append("Start Time" + "\n");
                message.append("End Time" + "\n");

                message.append("Expiry Time");

                values.append(": ").append(StringUtil.getDisplayString(offStreet.getPlate())).append("\n");
                values.append(": ").append(StringUtil.getDisplayString(offStreet.getState())).append("\n");
                values.append(": ").append(DateUtil.getCurbtracDateFromUTCString2(offStreet.getStartDateTime())).append("\n");
                values.append(": ").append(DateUtil.getCurbtracDateFromUTCString2(offStreet.getEndDateTime())).append("\n");
                ParkingExpireInfo expireInfo = offStreet.getExpireInfo();
                values.append(": ").append(expireInfo.getExpireMsg());

                headerTV.setText(message.toString());
                valueTV.setText(values.toString());

                Builder dialog = new Builder(activity);
                dialog.setCancelable(false);
                dialog.setView(view);
                dialog.setTitle("Offstreet\nPlate Info: " + plate);
                dialog.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        enableBack();
                        dialog.dismiss();
                    }
                });

                dialog.setNegativeButton("Continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        processPlateLookupQueue(plate);
                    }
                });

                TPUtility.applyButtonStyles(dialog.show());

            } catch (Exception e) {
                Log.e("TicketPRO", "pp2 Space Info " + e.getMessage());
            }
        });

    }
    private void __displayCurbtracPlateInfo(CubtracResponse pp2Plate) {
        activity.runOnUiThread(() -> {
            try {
                StringBuilder message = new StringBuilder();
                StringBuilder values = new StringBuilder();

                View view = LayoutInflater.from(activity.getBaseContext()).inflate(R.layout.item_view, null);
                TextView headerTV = view.findViewById(R.id.headerTV);
                TextView valueTV = view.findViewById(R.id.valueTV);

                message.append("Plate" + "\n");
                message.append("State" + "\n");

                message.append("Start Time" + "\n");
                message.append("End Time" + "\n");

               /* message.append("Expiry Time");*/
                String zonename = preference.getString("ZONE_NAME"); // Provide a default value
                if (zonename != null && !zonename.isEmpty()) {
                    message.append("Expiry Time" + "\n");
                    message.append("Zone");
                } else {
                    message.append("Expiry Time");
                }


                values.append(": ").append(StringUtil.getDisplayString(pp2Plate.getLicensePlate())).append("\n");
                values.append(": ").append(StringUtil.getDisplayString(pp2Plate.getLicensePlateState())).append("\n");
                values.append(": ").append(DateUtil.getCurbtracDateFromUTCString2(pp2Plate.getStart())).append("\n");
                values.append(": ").append(DateUtil.getCurbtracDateFromUTCString2(pp2Plate.getEnd())).append("\n");
                ParkingExpireInfo expireInfo = pp2Plate.getExpireInfo();
                values.append(": ").append(expireInfo.getExpireMsg()).append("\n");

                String Zone_name = preference.getString("ZONE_NAME"); // Provide a default value
                if (Zone_name != null && !Zone_name.isEmpty()) {
                    values.append(": ").append(Zone_name);
                }

                headerTV.setText(message.toString());
                valueTV.setText(values.toString());

                Builder dialog = new Builder(activity);
                dialog.setCancelable(false);
                dialog.setView(view);
                dialog.setTitle("Curbtrac\nPlate Info: " + plate);
                dialog.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        enableBack();
                        dialog.dismiss();
                    }
                });

                dialog.setNegativeButton("Continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        processPlateLookupQueue(plate);
                    }
                });

                TPUtility.applyButtonStyles(dialog.show());

            } catch (Exception e) {
                Log.e("TicketPRO", "pp2 Space Info " + e.getMessage());
            }
        });

    }

    public void lookupPlateHistory(String plateStr) {
        flag = true;
        if (activity.isServiceAvailable) {
            if (plateStr == null || plateStr.isEmpty()) {
                return;
            }

            if (activity.alprPlateNum.equalsIgnoreCase(plateStr)) {
                if (activity.alprMode) {
                    if (!activity.alprLookup) {
                        return;
                    }
                }
            }
            //disableBack();
            final String plate = TPUtility.getValidPlate(plateStr);
            final String state = activity.stateEditText.getText().toString();
            activity.isServiceAvailable = !Feature.isFeatureAllowed(Feature.TOGGLE_NET_ON_OFF) || !TPApplication.getInstance().getNetOnOff().equals("Y");
            if (!activity.isServiceAvailable) {
                if (Feature.isFeatureAllowed(Feature.PARK_SEARCH_TRACK)) {
                    logpm.info("doPlateLookup lookupPlateHistory");
                }
                doPlateLookup(plate);
                return;
            }

            ArrayList<String> featureNames = new ArrayList<>();
            ArrayList<Feature> plateLookupFeatures;

            featureNames.add(Feature.PASSPORT_PARKING);
            featureNames.add(Feature.PASSPORT_PARKING2);
            featureNames.add(Feature.PARK_MOBILE);
            featureNames.add(Feature.PAYBY_PHONE);
            featureNames.add(Feature.DIGITAL_PAYTECH);
            featureNames.add(Feature.MOBILE_NOW);
            featureNames.add(Feature.PROGRESSIVE);
            featureNames.add(Feature.CS_LOOKUP);
            featureNames.add(Feature.IPS_GROUP);
            featureNames.add(Feature.CALE);
            featureNames.add(Feature.CALE2);
            featureNames.add(Feature.PARKEON);
            featureNames.add(Feature.PARK_CUBTRAC);

            plateLookupQueue = new LinkedList<>();

            try {
                plateLookupFeatures = Feature.getFeatures(featureNames);

                for (Feature feature : plateLookupFeatures) {
                    if (Feature.PASSPORT_PARKING.equalsIgnoreCase(feature.getFeature())) {
                        if (TPApp.enablePassportParking) {
                            plateLookupQueue.add(new AsyncCallback() {
                                @Override
                                public void execute() {
                                    if (Feature.isFeatureAllowed(Feature.PARK_SEARCH_TRACK)) {
                                        logpm.info("lookupPassportParking");
                                    }
                                    lookupPassportParking(plate);
                                }
                            });
                        }
                    }
                    if (Feature.PASSPORT_PARKING2.equalsIgnoreCase(feature.getFeature())) {
                        if (TPApp.enablePassportParking2) {
                            plateLookupQueue.add(new AsyncCallback() {
                                @Override
                                public void execute() {
                                    if (Feature.isFeatureAllowed(Feature.PARK_SEARCH_TRACK)) {
                                        logpm.info("lookupPassportParking");
                                    }
                                    getTokenPP2Plate( plate, "Processing PP2..");
                                }
                            });
                        }
                    }

                    if (Feature.PARK_MOBILE.equalsIgnoreCase(feature.getFeature())) {
                        if (TPApp.enableParkMobile) {
                            plateLookupQueue.add(new AsyncCallback() {
                                @Override
                                public void execute() {
                                /*if (Feature.isFeatureAllowed("park_TrackPlateLookup")){

                                    logpm.info("lookupParkMobile");
                                }*/
                                    if (Feature.isFeatureAllowed(Feature.PARK_SEARCH_TRACK)) {
                                        logpm.info("Start lookupParkMobile");
                                    }

                                    lookupParkMobile(plate);
                                }
                            });
                        }
                    }

                    if (Feature.PAYBY_PHONE.equalsIgnoreCase(feature.getFeature())) {
                        if (TPApp.enablePayByPhone) {
                            plateLookupQueue.add(new AsyncCallback() {
                                @Override
                                public void execute() {
                                    if (Feature.isFeatureAllowed(Feature.PARK_SEARCH_TRACK)) {

                                        logpm.info("lookupPayByPhone");
                                    }
                                    lookupPayByPhone(plate);
                                }
                            });
                        }
                    }


                    if (Feature.DIGITAL_PAYTECH.equalsIgnoreCase(feature.getFeature())) {
                        if (TPApp.enableDPT) {
                            plateLookupQueue.add(new AsyncCallback() {
                                @Override
                                public void execute() {
                                    if (Feature.isFeatureAllowed(Feature.PARK_SEARCH_TRACK)) {

                                        logpm.info("lookupDigitalPaytech");
                                    }
                                    lookupDigitalPaytech(plate);
                                }
                            });
                        }
                    }

                    if (Feature.IPS_GROUP.equalsIgnoreCase(feature.getFeature())) {
                        if (TPApp.enableIPS) {
                            plateLookupQueue.add(new AsyncCallback() {
                                @Override
                                public void execute() {
                                    if (Feature.isFeatureAllowed(Feature.PARK_SEARCH_TRACK)) {
                                        logpm.info("lookupIPSGroup");
                                    }
                                    lookupIPSGroup(plate);
                                }
                            });
                        }
                    }

                    if (Feature.MOBILE_NOW.equalsIgnoreCase(feature.getFeature())) {
                        if (TPApp.enableMobileNow) {
                            plateLookupQueue.add(new AsyncCallback() {
                                @Override
                                public void execute() {
                                    if (Feature.isFeatureAllowed(Feature.PARK_SEARCH_TRACK)) {

                                        logpm.info("lookupMobileNow");
                                    }
                                    lookupMobileNow(plate);
                                }
                            });
                        }
                    }

                    if (Feature.PROGRESSIVE.equalsIgnoreCase(feature.getFeature())) {
                        if (TPApp.enableProgressive) {
                            plateLookupQueue.add(new AsyncCallback() {
                                @Override
                                public void execute() {
                                    if (Feature.isFeatureAllowed(Feature.PARK_SEARCH_TRACK)) {

                                        logpm.info("lookupProgressive");
                                    }
                                    lookupProgressive(plate);
                                }
                            });
                        }
                    }

                    if (Feature.CS_LOOKUP.equalsIgnoreCase(feature.getFeature())) {
                        if (feature.isAllowed()) {
                            plateLookupQueue.add(new AsyncCallback() {
                                @Override
                                public void execute() {
                                    if (Feature.isFeatureAllowed(Feature.PARK_SEARCH_TRACK)) {

                                        logpm.info("lookupCSLookup");
                                    }
                                    lookupCSLookup(plate, state);
                                }
                            });
                        }
                    }
                    if (Feature.CALE.equalsIgnoreCase(feature.getFeature())) {
                        if (TPApp.enableCale) {
                            plateLookupQueue.add(new AsyncCallback() {
                                @Override
                                public void execute() {
                                    if (Feature.isFeatureAllowed(Feature.PARK_SEARCH_TRACK)) {

                                        logpm.info("lookupCaleParking");
                                    }
                                    lookupCale(plate);
                                }
                            });
                        }
                    }
                    if (Feature.CALE2.equalsIgnoreCase(feature.getFeature())) {
                        if (TPApp.enableCale2) {
                            plateLookupQueue.add(new AsyncCallback() {
                                @Override
                                public void execute() {
                                    if (Feature.isFeatureAllowed(Feature.PARK_SEARCH_TRACK)) {

                                        logpm.info("lookupCale2Parking");
                                    }
                                    lookupCale2(plate);
                                }
                            });
                        }
                    }

                    if (Feature.PARKEON.equalsIgnoreCase(feature.getFeature())) {
                        if (TPApp.enableParkeon) {
                            plateLookupQueue.add(new AsyncCallback() {
                                @Override
                                public void execute() {
                                    if (Feature.isFeatureAllowed(Feature.PARK_SEARCH_TRACK)) {

                                        logpm.info("lookupParkeon");
                                    }
                                    lookupParkeon(plate);
                                }
                            });
                        }
                    }

                    if (Feature.PARK_CUBTRAC.equalsIgnoreCase(feature.getFeature())) {
                        if (TPApp.enableCubtrac) {
                            plateLookupQueue.add(new AsyncCallback() {
                                @Override
                                public void execute() {
                                    if (Feature.isFeatureAllowed(Feature.PARK_SEARCH_TRACK)) {

                                        logpm.info("lookupCubtrac");
                                    }

                                    String zoneId = preference.getString("ZONE_ID");
                                    if (zoneId!=null && !TextUtils.isEmpty(zoneId)){
                                        lookupCubtrac(plate,zoneId);
                                    }else {
                                        getCubtrackZone(plate);
                                    }
                                }
                            });
                        }
                    }

                    if (Feature.PARK_OFFSTEER.equalsIgnoreCase(feature.getFeature())) {
                        if (TPApp.enableOffStreet) {
                            plateLookupQueue.add(new AsyncCallback() {
                                @Override
                                public void execute() {
                                    if (Feature.isFeatureAllowed(Feature.PARK_SEARCH_TRACK)) {

                                        logpm.info("lookupOffstreet");
                                    }
                                    lookupOffstreet(plate);
                                }
                            });
                        }
                    }
                }

            } catch (Exception e1) {
                activity.log.error(TPUtility.getPrintStackTrace(e1));
            }

            // Process plate lookup queue
            processPlateLookupQueue(plate);
        }
    }



    public void checkVinHistory1(String vin) {
        checkVinHistory(vin, false);
    }

    public void checVinHistoryByGetAPermit(String vin) {
        if (vin == null || vin.equals("")) {
            return;
        }
        /*try {
            Ticket historyTicket = null;
            String state = activity.stateEditText.getText().toString();
            activity.isServiceAvailable = !Feature.isFeatureAllowed(Feature.TOGGLE_NET_ON_OFF) || !TPApplication.getInstance().getNetOnOff().equals("Y");
            if (activity.isServiceAvailable) {
                if (activity.screenBLProcessor instanceof WriteTicketBLProcessor)
                    historyTicket = ((WriteTicketBLProcessor) activity.screenBLProcessor).searchVinHistory(vin, state);
            }
            if (historyTicket != null) {
                displayPlateHistory(historyTicket);
            } else {
                // Can't create handler inside thread that has not called Looper.prepare()
                //if (TPUtility.isActivityRunning(activity)) {
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(activity.getApplicationContext(), "No record found.", Toast.LENGTH_SHORT).show();
                    }
                });
                //}
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        // This code is changed for searchVinHistory by mohit 5/4/2023

        try {
            Ticket historyTicket = null;
            String state = activity.stateEditText.getText().toString();
            activity.isServiceAvailable = !Feature.isFeatureAllowed(Feature.TOGGLE_NET_ON_OFF) || !TPApplication.getInstance().getNetOnOff().equals("Y");
            if (activity.isServiceAvailable) {
                if (activity.screenBLProcessor instanceof WriteTicketBLProcessor) {
                    //historyTicket = ((WriteTicketBLProcessor) activity.screenBLProcessor).searchVinHistory(vin, state);
                    ((WriteTicketBLProcessor) activity.screenBLProcessor).searchVinHistory1(vin, state, this);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void checkVinHistory(String vinStr, final boolean processEdmundsVin) {
        if (vinStr == null || vinStr.equals("")) {
            return;
        }

        final String vin = TPUtility.getValidVIN(vinStr);


       /* TPAsyncTask task = new TPAsyncTask(activity, "Processing VIN...");
        task.execute(new TPTask() {
            @Override
            public void execute() {
                try {
                    String state = activity.stateEditText.getText().toString();
                    Permit historyPermit = null;
                    //first check if permit feature is not enabled
                    if (Feature.isFeatureAllowed(Feature.LOOKUP_PLATE_PERMIT)) {
                        activity.isServiceAvailable = !Feature.isFeatureAllowed(Feature.TOGGLE_NET_ON_OFF) || !TPApplication.getInstance().getNetOnOff().equals("Y");
                        if (activity.isServiceAvailable) {
                            if (activity.screenBLProcessor instanceof WriteTicketBLProcessor)
                                historyPermit = ((WriteTicketBLProcessor) activity.screenBLProcessor).searchPermitVinHistory(vin, state);
                            if (historyPermit == null) {
                                checVinHistoryByGetAPermit(vin);
                                //Toast.makeText(TPApplication.getInstance().getApplicationContext(), "No permit record found", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            displayPermitInfo(historyPermit, true);

                        } else {
                            Toast.makeText(TPApplication.getInstance().getApplicationContext(), "Please refresh your internet connection.", Toast.LENGTH_SHORT).show();
                        }
                        return;
                    }
                    Ticket historyTicket = Ticket.searchPreviousTicketByVIN(state, vin);
                    if (historyTicket == null) {
                        historyTicket = TicketHistory.searchPreviousTicketByVIN(state, vin);
                    }

                    //This will run if permit feature is not enabled
                    activity.isServiceAvailable = !Feature.isFeatureAllowed(Feature.TOGGLE_NET_ON_OFF) || !TPApplication.getInstance().getNetOnOff().equals("Y");
                    if (activity.isServiceAvailable) {
                        if (historyTicket == null) {
                            if (activity.screenBLProcessor instanceof WriteTicketBLProcessor)
                                historyTicket = ((WriteTicketBLProcessor) activity.screenBLProcessor).searchVinHistory(vin, state);
                        }
                    }
                    if (historyTicket != null) {
                        displayPlateHistory(historyTicket);
                    } else
                        Toast.makeText(TPApplication.getInstance().getApplicationContext(), "Please retry.", Toast.LENGTH_SHORT).show();
						*//* else if (historyTicket == null && processEdmundsVin) {
						lookupEdmundsVin(vin);
					} *//*

                } catch (Exception e) {
                    activity.log.error(TPUtility.getPrintStackTrace(e));
                }
            }
        });*/

        // This code is changed for searchPermitVinHistory by mohit 4/04/2023

        try {
            String state = activity.stateEditText.getText().toString();
            Permit historyPermit = null;
            //first check if permit feature is not enabled
            if (Feature.isFeatureAllowed(Feature.LOOKUP_PLATE_PERMIT)) {
                activity.isServiceAvailable = !Feature.isFeatureAllowed(Feature.TOGGLE_NET_ON_OFF) || !TPApplication.getInstance().getNetOnOff().equals("Y");
                if (activity.isServiceAvailable) {
                    if (activity.screenBLProcessor instanceof WriteTicketBLProcessor)
                        // historyPermit = ((WriteTicketBLProcessor) activity.screenBLProcessor).searchPermitVinHistory(vin, state);
                        progressDialog = new ProgressDialog(activity);
                    progressDialog.setMessage("Processing VIN...");
                    // historyPermit = ((WriteTicketBLProcessor) activity.screenBLProcessor).searchPermitHistory(permit);
                    progressDialog.show();
                    ((WriteTicketBLProcessor) activity.screenBLProcessor).searchPermitVinHistory1(vin, state, this);

                } else {
                    Toast.makeText(TPApplication.getInstance().getApplicationContext(), "Please refresh your internet connection.", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            Ticket historyTicket = Ticket.searchPreviousTicketByVIN(state, vin);
            if (historyTicket == null) {
                historyTicket = TicketHistory.searchPreviousTicketByVIN(state, vin);
            }

            //This will run if permit feature is not enabled

            activity.isServiceAvailable = !Feature.isFeatureAllowed(Feature.TOGGLE_NET_ON_OFF) || !TPApplication.getInstance().getNetOnOff().equals("Y");
            /*if (activity.isServiceAvailable) {
                if (historyTicket == null) {
                    if (activity.screenBLProcessor instanceof WriteTicketBLProcessor)
                        historyTicket = ((WriteTicketBLProcessor) activity.screenBLProcessor).searchVinHistory(vin, state);
                }
            }
            if (historyTicket != null) {
                displayPlateHistory(historyTicket);
            } else
                Toast.makeText(TPApplication.getInstance().getApplicationContext(), "Please retry.", Toast.LENGTH_SHORT).show();
            */

            if (activity.isServiceAvailable) {
                if (historyTicket == null) {
                    if (activity.screenBLProcessor instanceof WriteTicketBLProcessor)
                        ((WriteTicketBLProcessor) activity.screenBLProcessor).searchVinHistory1(vin, state, this);
                }
            }
           /* if (historyTicket != null) {
                displayPlateHistory(historyTicket);
            } else
                Toast.makeText(TPApplication.getInstance().getApplicationContext(), "Please retry.", Toast.LENGTH_SHORT).show();*/


						/* else if (historyTicket == null && processEdmundsVin) {
            lookupEdmundsVin(vin);
        } */

        } catch (Exception e) {
            activity.log.error(TPUtility.getPrintStackTrace(e));
        }
        //End
    }

    public void checkMeterHistory(final String meter, final boolean skipLookup) {

        if (meter == null || meter.equals("")) {
            return;
        }
        //disableBack();
        // Process IPS Meter Lookup
        activity.isServiceAvailable = !Feature.isFeatureAllowed(Feature.TOGGLE_NET_ON_OFF) || !TPApplication.getInstance().getNetOnOff().equals("Y");
        if (!skipLookup && activity.isServiceAvailable && Feature.isFeatureAllowed(Feature.IPS_GROUP)) {
            TPAsyncTask task = new TPAsyncTask(activity, "Processing IPS Meter...");
            task.execute(new TPTask() {
                @Override
                public void execute() {
                    final String meterString = TPUtility.getValidMeter(meter);
                    try {

                        final VendorServiceConfig config = VendorService.getServiceConfig(VendorService.IPS_METERINFO,
                                TPApp.deviceId);
                        if (config != null) {
                            String token = config.getParamsMap().get("token");
                            String response = IPSQuery.getMeterStatus(config.getServiceURL(), token, meterString);
                            if (response != null && !response.equalsIgnoreCase("[]") && !response.equalsIgnoreCase("")) {
                                IPSParkingMeter parkingMeter = IPSParser.getMeterStatus(response);
                                displayIPSParkings(parkingMeter);
                            } else {
                                lookUpMeterHistroy(meterString);
                            }
                        } else {
                            lookUpMeterHistroy(meterString);
                        }

                    } catch (Exception e) {
                        activity.log.error(TPUtility.getPrintStackTrace(e));
                    }

                }
            });
        } else {
            try {
                String meterString = TPUtility.getValidMeter(meter);
                lookUpMeterHistroy(meterString);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    private void lookUpMeterHistroy(final String meter) {
        if (meter == null || meter.equals("") || !Feature.isFeatureAllowed(Feature.LOOKUP_METER)) {
            enableBack();
            return;
        }
        try {
            //if (TPUtility.isActivityRunning(activity)) {
         /*   activity.runOnUiThread(() -> {

                TPAsyncTask task = new TPAsyncTask(activity, "Processing Meter...");
                task.execute(new TPTask() {
                    @SuppressLint("SuspiciousIndentation")
                    @Override
                    public void execute() {*/
            final String meterString = TPUtility.getValidMeter(meter);
            try {
                historyMeter = Meter.searchMeterHistory(meterString);
                if (historyMeter == null) {
                    activity.isServiceAvailable = !Feature.isFeatureAllowed(Feature.TOGGLE_NET_ON_OFF) || !TPApplication.getInstance().getNetOnOff().equals("Y");
                    if (activity.isServiceAvailable) {
                        if (activity.screenBLProcessor instanceof WriteTicketBLProcessor)
                            progressDialog = new ProgressDialog(activity);
                        progressDialog.setMessage("Processing meter...");
                        // historyPermit = ((WriteTicketBLProcessor) activity.screenBLProcessor).searchPermitHistory(permit);
                        progressDialog.show();
                        //  historyMeter = ((WriteTicketBLProcessor) activity.screenBLProcessor).searchMeterHistory(meterString);
                        ((WriteTicketBLProcessor) activity.screenBLProcessor).searchMeterHistory1(meterString, this);
                        enableBack();
                    }
                } else {
                    enableBack();
                    displayMeterHistory(historyMeter, meterString);
                }
            } catch (TPException e) {
                enableBack();
                activity.log.error(TPUtility.getPrintStackTrace(e));

            } catch (Exception e) {
                enableBack();
                activity.log.error(TPUtility.getPrintStackTrace(e));
            }

                  /*  }
                });
            });*/
            //}
        } catch (Exception e) {
            enableBack();
            activity.log.error(TPUtility.getPrintStackTrace(e));
        }
    }

    private void updateMeterLocation(Meter historyMeter) {
        if (historyMeter == null) {
            return;
        }

        Ticket activeTicket = activity.activeTicket;
        activeTicket.setDirection(historyMeter.getDirection());
        activeTicket.setStreetNumber(historyMeter.getStreetNumber());
        activeTicket.setStreetPrefix(historyMeter.getStreetPrefix());
        activeTicket.setStreetSuffix(historyMeter.getStreetSuffix());
        activeTicket.setLocation(historyMeter.getLocation());

        activity.locationEditText.setText(TPUtility.getFullAddress(activeTicket));

        if (Feature.isFeatureAllowed(Feature.METER_VIOLATION) && historyMeter.getViolationCode() != null) {
            Violation violation = Violation.getViolationByCode(historyMeter.getViolationCode());
            if (violation == null || activity.isDuplicateViolation(violation.getId())) {
                return;
            }

            if (activeTicket.getTicketViolations().size() > 0) {
                activity.confirmAddViolation(violation);
                return;
            }

            TicketViolation ticketViolation = new TicketViolation();
            ticketViolation.setFine(violation.getBaseFine());
            ticketViolation.setViolationDesc(violation.getTitle());
            ticketViolation.setViolationId(violation.getId());
            ticketViolation.setViolationCode(violation.getCode());

            // Added to ticket violations list
            activeTicket.getTicketViolations().add(ticketViolation);
            activity.violationBtn.setText("V (" + activeTicket.getTicketViolations().size() + ")");

            if (activeTicket.getTicketViolations().size() > 0) {
                activity.violationDescText.setText(activeTicket.getTicketViolations().get(0).getViolationDisplay());
            } else {
                activity.violationDescText.setText("");
            }
        }
    }

    public void displayMeterHistory(final Meter historyMeter, final String meterString) {

        //if (TPUtility.isActivityRunning(activity)) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (historyMeter == null) {
                    activity.displayErrorMessage("Meter record not found.");
                    return;
                }

                // Do not show message if METER_SUPPRESS_MESSAGE is enabled
                if (Feature.isFeatureAllowed(Feature.METER_SUPPRESS_MESSAGE)) {
                    updateMeterLocation(historyMeter);
                    return;
                }

                Address meterAddress = new Address();
                meterAddress.setStreetNumber(historyMeter.getStreetNumber());
                meterAddress.setStreetPrefix(historyMeter.getStreetPrefix());
                meterAddress.setStreetSuffix(historyMeter.getStreetSuffix());
                meterAddress.setLocation(historyMeter.getLocation());

                StringBuffer msg = new StringBuffer();
                StringBuffer values = new StringBuffer();
                   /* msg.append("<style>body{color:#fff;} table{color:#fff;} td{vertical-align:top;}</style>");
                    msg.append("<p>Meter Lookup Details</p>");
                    msg.append("<table>");
                    msg.append("<tr><td>Meter</td><td>:</td><td>" + StringUtil.getDisplayString(meterString));
                    msg.append("</td></tr>");
    */

                msg.append("Meter Lookup Details\n\n");
                values.append("\n\n");
                msg.append("Meter" + "\n");
                values.append(": " + StringUtil.getDisplayString(meterString) + "\n");

                if (Feature.isFeatureAllowed(Feature.METER_VIOLATION)) {
                    if (historyMeter.getViolationCode() != null && !historyMeter.getViolationCode().equals("")) {
                        Violation violation = Violation.getViolationByCode(historyMeter.getViolationCode());
                        msg.append("Code" + "\n");
                        if (violation != null) {
                               /* msg.append("<tr><td>Code</td><td>:</td><td>"
                                        + StringUtil.getDisplayString(violation.getCode()));
                                msg.append("</td></tr>");
                                msg.append("<tr><td>Violation</td><td>:</td><td>"
                                        + StringUtil.getDisplayString(violation.getTitle()));
                                msg.append("</td></tr>");*/

                            msg.append("Violation" + "\n");
                            values.append(": "
                                    + StringUtil.getDisplayString(violation.getCode()) + "\n");

                            values.append(": "
                                    + StringUtil.getDisplayString(violation.getTitle()) + "\n");

                        } else {
                            values.append(": "
                                    + StringUtil.getDisplayString(historyMeter.getViolationCode()) + "\n");
                        }
                    }
                }

                msg.append("Location" + "\n");
                values.append(": " + TPUtility.getFullAddress(meterAddress) + "\n");

                View view = LayoutInflater.from(activity.getBaseContext()).inflate(R.layout.item_view, null);
                TextView headerTV = view.findViewById(R.id.headerTV);
                TextView valueTV = view.findViewById(R.id.valueTV);

                headerTV.setText(msg.toString());
                valueTV.setText(values.toString());

                    /*WebView wv = new WebView(activity.getBaseContext());
                    wv.loadData(msg.toString(), "text/html", "utf-8");
                    wv.setBackgroundColor(android.graphics.Color.TRANSPARENT);
                    wv.getSettings().setDefaultTextEncodingName("utf-8");*/
                new Builder(activity).setView(view).setCancelable(false).setTitle("Lookup Result")
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                                updateMeterLocation(historyMeter);
                            }
                        }).show();
            }
        });
        //}
    }

    public void lookupSpacePassportParking(final String spaceStr) {
        final VendorServiceConfig config = VendorService.getServiceConfig(VendorService.PASSPORT_PARKING_ZONEINFO, TPApp.deviceId);
        if (config != null) {
            TPAsyncTask task = new TPAsyncTask(activity, "Processing PassportParking...");
            task.execute(new TPTask() {
                @Override
                public void execute() {

                    VendorItem vendorItem = null;
                    String safeSpace = spaceStr.replaceAll(" ", "");
                    String params = config.getParams();

                    try {
                        vendorItem = VendorItem.getVendorZoneByType("PassportParking");
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }

                    if (vendorItem != null) {
                        params = params.replaceAll("\\{ZONE_ID\\}", vendorItem.getItemCode());
                    } else {
                        params = params.replaceAll("\\{ZONE_ID\\}", TPApp.getActiveDutyInfo().getCode());
                    }

                    params = params + "&spacenumber=" + safeSpace;
                    HttpRequestTask task = new HttpRequestTask(LOOKUP_PASSPORT_PARKING_SPACE, new Bundle(), params);
                    task.setRequestMode(config.getRequestMode());
                    task.executeTask(config.getServiceURL() + "?" + params);
                    serviceName = "SpacePassportParking";
                    this.asyncTask = task;

                }
            });
        }
    }

    public void lookupSpaceParkMobile(final String spaceStr) {
        final VendorServiceConfig config = VendorService.getServiceConfig(VendorService.PARK_MOBILE_SPACEINFO, TPApp.deviceId, "/");
        if (config != null) {
            TPAsyncTask task = new TPAsyncTask(activity, "Processing ParkMobile...");
            task.execute(new TPTask() {
                @Override
                public void execute() {

                    String safeSpace = spaceStr.replaceAll(" ", "");
                    String params = config.getParams();
                    String startZone = "1";
                    String endZone = "9999";

                    try {
                        VendorServiceConfig zoneConfig = VendorService
                                .getServiceConfig(VendorService.PARK_MOBILE_ZONELIST, TPApp.deviceId, "/");
                        if (zoneConfig != null) {
                            String responseJSON = TPUtility.getURLResponse(
                                    zoneConfig.getServiceURL() + "/" + zoneConfig.getParams() + "?format=json",
                                    zoneConfig.getUsername(), zoneConfig.getPassword());
                            ArrayList<ParkMobileZoneInfo> zoneItems = ParkMobileParser.getZones(responseJSON);
                            if (zoneItems != null && zoneItems.size() > 0) {
                                startZone = zoneItems.get(0).getSignageZoneCode();
                                endZone = zoneItems.get(zoneItems.size() - 1).getSignageZoneCode();
                            }
                        }

                        params = params.replaceAll("\\{START_ZONE_ID\\}", startZone);
                        params = params.replaceAll("\\{END_ZONE_ID\\}", endZone);

                        String response = TPUtility.getURLResponse(
                                config.getServiceURL() + "/" + params + "?format=json", config.getUsername(),
                                config.getPassword());
                        Bundle data = new Bundle();
                        data.putString(RESPONSE_DATA, response);
                        data.putString("SPACE", safeSpace);
                        data.putString("ZONE", startZone + "-" + endZone);

                        Message msg = obtainMessage();
                        msg.what = LOOKUP_PARK_MOBILE_SPACE;
                        msg.setData(data);
                        sendMessage(msg);

                    } catch (IOException e) {
                        activity.log.error(TPUtility.getPrintStackTrace(e));
                    }

                }
            });
        }
    }

    public void lookupSpacePayByPhone(final String spaceStr) {
        final VendorServiceConfig config = VendorService.getServiceConfig(VendorService.PAYBYPHONE_SPACEINFO, TPApp.deviceId);
        if (config != null) {
            TPAsyncTask task = new TPAsyncTask(activity, "Processing PayByPhone...");
            task.execute(new TPTask() {
                @Override
                public void execute() {
                    String safeSpace = spaceStr.replaceAll(" ", "");
                    String params = config.getParams();
                    params = params.replaceAll("\\{SPACE\\}", safeSpace);

                    HttpRequestTask task = new HttpRequestTask(LOOKUP_PAYBYPHONE_SPACE, new Bundle(), params);
                    task.setRequestMode(config.getRequestMode());
                    task.executeTask(config.getServiceURL() + "?" + params);
                    serviceName = "SpacePayByPhone";
                    this.asyncTask = task;
                }
            });
        }
    }

    public void lookupSpaceDPT(final String spaceStr) {
        final VendorServiceConfig config = VendorService.getServiceConfig(VendorService.DIGITALPAYTECH_SPACEINFO, TPApp.deviceId);
        if (config != null) {
            vectorPlateInfoType = null;

            TPAsyncTask task = new TPAsyncTask(activity, "Processing T2...");
            serviceName = "T2";
            task.execute(new TPTask() {
                @Override
                public void execute() {

                    String safeSpace = spaceStr.replaceAll(" ", "");
                    PlateInfoService service = new PlateInfoService(new IWsdl2CodeEvents() {
                        @Override
                        public void Wsdl2CodeStartedRequest() {

                        }

                        @Override
                        public void Wsdl2CodeFinishedWithException(Exception ex) {
                            activity.log.error("PlateInfoService Error " + TPUtility.getPrintStackTrace(ex));
                        }

                        @Override
                        public void Wsdl2CodeFinished(String methodName, Object Data) {

                        }

                        @Override
                        public void Wsdl2CodeEndedRequest() {

                        }
                    });

                    service.addAuthHeader(config.getUsername(), config.getPassword());
                    service.setUrl(config.getServiceURL());
                    service.setTimeOut(4000);

                    PlateInfoByRegionRequest request = new PlateInfoByRegionRequest();
                    request.token = config.getParamsMap().get("token");
                    request.region = safeSpace;
                    request.gracePeriod = 0;

                    vectorPlateInfoType = service.getValidPlatesByRegion(request);

                    new RequestLogTask(config.getParams(), config.getRequestMode(),
                            vectorPlateInfoType != null ? vectorPlateInfoType.getInnerText() : "").execute();

                    Bundle data = new Bundle();
                    if (vectorPlateInfoType != null) {
                        data.putString(RESPONSE_DATA, vectorPlateInfoType.toString());
                    }

                    data.putString("SPACE", safeSpace);

                    Message msg = obtainMessage();
                    msg.what = LOOKUP_DIGITALPAYTECH_SPACE;
                    msg.setData(data);
                    sendMessage(msg);

                }
            });
        }
    }

    public void lookupSpaceIPS(final String spaceStr) {

        if (spaceStr.isEmpty()) {
            if (Feature.isFeatureAllowed(Feature.IPS_SPACEGROUP) && Feature.isFeatureAllowed(Feature.IPS_SUBAREA)) {
                selectIPSSubAreaOrGroup(spaceStr);
                return;
            }

            if (Feature.isFeatureAllowed(Feature.IPS_SUBAREA)) {
                selectIPSSpaceGroup(spaceStr, "subArea");
                return;
            }

            if (Feature.isFeatureAllowed(Feature.IPS_SPACEGROUP)) {
                selectIPSSpaceGroup(spaceStr, "spaceGroup");
                return;
            }
        } else {
            if (TPApp.IPSSpaceGroup == null)
                selectIPSSpaceGroup(spaceStr, "spaceGroup");
            else
                lookupspaceIPS(spaceStr);
            return;
        }

    }

    private void lookupspaceIPS(final String spaceStr) {
        final VendorServiceConfig config = VendorService.getServiceConfig(VendorService.IPS_SPACEINFO, TPApp.deviceId);
        if (config != null && activity.isServiceAvailable) {
            TPAsyncTask task = new TPAsyncTask(activity, "Processing IPS Group...");
            task.execute(new TPTask() {
                @Override
                public void execute() {
                    String safeSpace = spaceStr.replaceAll(" ", "");
                    try {
                        String url = config.getServiceURL();
                        String token = config.getParamsMap().get("token");
                        String response = IPSQuery.getSpaceStatus(url, token, TPApp.IPSSpaceGroup, safeSpace);
                        Bundle data = new Bundle();
                        data.putString(RESPONSE_DATA, response);

                        Message msg = obtainMessage();
                        msg.what = LOOKUP_IPS_SPACE;
                        msg.setData(data);
                        sendMessage(msg);
                    } catch (Exception e) {
                        activity.log.error("IPS Space Exception " + TPUtility.getPrintStackTrace(e));
                    }
                }
            });
        }
    }

    private void selectIPSSubAreaOrGroup(final String spaceStr) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.activity);
        View headerView = LayoutInflater.from(this.activity).inflate(R.layout.dialog_header, null);
        final TextView titleView = headerView.findViewById(R.id.header_title);
        titleView.setText(activity.getText(R.string.select_spaceGroup_subArea));

        /* TextView groupName = new TextView(this.activity);
        groupName.setText(TPApp.IPSSpaceGroup);*/

        /*LinearLayout actionLayout = (LinearLayout) headerView.findViewById(R.id.header_actions);
        actionLayout.addView(groupName);*/
        builder.setCustomTitle(headerView);

        String[] listItems = {"SubArea", "SpaceGroup"};
        builder.setItems(listItems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        selectIPSSpaceGroup(spaceStr, "subArea");
                        break;
                    case 1:
                        selectIPSSpaceGroup(spaceStr, "spaceGroup");
                        break;
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public void lookupSpaceMobileNow(final String spaceStr) {
        final VendorServiceConfig config = VendorService.getServiceConfig(VendorService.MOBILE_NOW_SPACE_CHECK, TPApp.deviceId);
        if (config != null && activity.isServiceAvailable) {
            TPAsyncTask task = new TPAsyncTask(activity, "Processing MobileNow...");
            task.execute(new TPTask() {
                @Override
                public void execute() {

                    String safeSpace = spaceStr.replaceAll(" ", "");
                    String params = config.getParams();
                    params = params.replaceAll("\\{PLATE\\}", safeSpace);

                    // Add Zone Information
                    VendorItem vendorItem = null;
                    try {
                        vendorItem = VendorItem.getVendorZoneByType("check");
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }

                    if (vendorItem != null) {
                        params = params.replaceAll("\\{ZONE\\}", vendorItem.getItemCode());
                    } else {
                        params = params.replaceAll("\\{ZONE\\}", TPApp.getActiveDutyInfo().getCode());
                    }

                    HttpRequestTask task = new HttpRequestTask(LOOKUP_MOBILE_NOW_SPACE, new Bundle(), params);
                    task.setRequestMode(config.getRequestMode());
                    task.executeTask(config.getServiceURL() + "?" + params);

                    serviceName = "SpaceMobileNow";
                    this.asyncTask = task;

                }
            });
        }
    }

    private void lookupSpaceSamtrans(String spaceStr) {
        samtranSpaceNumber = spaceStr;
        String _spaceName = preference.getString(TPConstant.CURRENT_SAMTRANS_SPACE);
        if (_spaceName != null) {
            _getFreshData(_spaceName, Integer.parseInt(spaceStr));
        } else {
            //noRecordFoundDialog();
            try {
                Vendor samtrans = Vendor.getVendorByName("Samtrans");
                if (samtrans.getVendorId() > 0) {
                    ArrayList<VendorItem> vendorSamtrans = VendorItem.getVendorSamtrans(samtrans.getVendorId());
                    _popUpDialog(vendorSamtrans, samtranSpaceNumber);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void _getFreshData(final String zoneName, final int spaceNumber) {
        try {

            if (preference.getString(TPConstant.SAMTRANS_TOKEN) != null) {
                final VendorServiceConfig config = VendorService.getServiceConfigCale(VendorService.SAMTRANS_BASE_URL, TPApp.deviceId, "/");
                assert config != null;
                Map<String, String> paramsMap = config.getParamsMap();

                progressDialog = new ProgressDialog(activity);
                progressDialog.setMessage("Loading Samtrans...");
                progressDialog.setCancelable(false);
                progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                progressDialog.show();
                //String url = config.getServiceURL() + "/" + zoneName + "/20190501140552/20190712153200/" + spaceNumber;
                final String url = config.getServiceURL() + "/" + zoneName + "/" + DateUtil.getStartDateSamtrans() + "/" + DateUtil.getEndDateSamtrans() + "/" + spaceNumber;
                ApiRequest service = ServiceGeneratorJson001.createService(ApiRequest.class, preference.getString(TPConstant.SAMTRANS_TOKEN));
                service._getDataBylotname(url).enqueue(new retrofit2.Callback<LotwiseApi>() {
                    @Override
                    public void onResponse(@NotNull Call<LotwiseApi> call, @NotNull Response<LotwiseApi> response) {
                        if (response.isSuccessful()) {
                            assert response.body() != null;
                            List<Datum> data = response.body().getData();
                            if (data != null) {
                                if (Feature.isFeatureAllowed(Feature.PARK_SEARCH_TRACK)) {
                                    try {
                                        MobileNowLog log = new MobileNowLog();
                                        log.setCustId(TPApp.custId);
                                        log.setDeviceId(TPApp.deviceId);
                                        log.setUserId(TPApp.userId);
                                        log.setRequestDate(new Date());
                                        log.setRequestParams(url);
                                        log.setServiceMode(config.getRequestMode());
                                        log.setResponseText(new Gson().toJson(response.body()));
                                        MobileNowLog.insertMobileNowLog(log);
                                        CSVUtility.writeMobileLogCSV(log);
                                        /*DatabaseHelper.getInstance().openWritableDatabase();
                                        DatabaseHelper.getInstance().insertOrReplace(log.getContentValues(), "mobile_now_logs");
                                        DatabaseHelper.getInstance().closeWritableDb();*/

                                        ArrayList<MobileNowLog> logs = new ArrayList<MobileNowLog>();
                                        logs.add(log);
                                        WriteTicketNetworkCalls.sendMobileNogLogs(logs);
                                        /*if (activity.screenBLProcessor instanceof WriteTicketBLProcessor)
                                            ((WriteTicketBLProcessor) activity.screenBLProcessor).sendMobileNowLog(logs);*/
                                    } catch (Exception e) {
                                        activity.log.error(TPUtility.getPrintStackTrace(e));
                                    }
                                }
                                if (data.size() > 0) {
                                    try {
                                        ArrayList<Datum> noRepeat = Datum._removeDuplicateValueFromArray(data);
                                        Collections.sort(noRepeat, new Datum.ExpireComparator1());
                                        Collections.reverse(noRepeat);
                                        ArrayList<Datum> data1 = Datum.removeSpaceByRecentPurchaed(noRepeat);
                                        Datum parking = data1.get(0);
                                        displaySamtransInfo(parking, zoneName);


                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    processSpaceLookupQueue();
                                }
                            }
                            progressDialog.isShowing();
                            progressDialog.dismiss();
                        } else {
                            if (response.code() == 500) {
                                progressDialog.isShowing();
                                progressDialog.dismiss();
                            }
                            if (response.code() == 401) {
                                SamtransZoneActivity.getInstanc()._getToken();
                                progressDialog.isShowing();
                                progressDialog.dismiss();

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        _getFreshData(zoneName, spaceNumber);
                                    }
                                }, 3000);

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<LotwiseApi> call, Throwable t) {
                        progressDialog.isShowing();
                        progressDialog.dismiss();

                    }
                });

            } else {
                try {
                    VendorServiceConfig config = VendorService.getServiceConfigCale(VendorService.SAMTRANS_TOKEN, TPApp.deviceId, "/");
                    assert config != null;
                    Map<String, String> paramsMap = config.getParamsMap();
                    String user = paramsMap.get("User");
                    String password = paramsMap.get("Password");
                    String serviceURL = config.getServiceURL();

                    new TokenGenerate(activity, serviceURL, user, password).execute();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            _getFreshData(zoneName, spaceNumber);
                        }
                    }, 3000);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void _popUpDialog(ArrayList<VendorItem> vendorSamtrans, final String spaceStr) {
        ArrayList<String> vendorItemArrayList = new ArrayList<>();
        for (VendorItem item : vendorSamtrans) {
            vendorItemArrayList.add(item.getItemName());
        }

        final String[] objects;
        objects = vendorItemArrayList.toArray(new String[vendorItemArrayList.size()]);
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Select Zone");
        builder.setItems(objects, (dialog, which) -> {
            String object = objects[which];
            preference.putString(TPConstant.CURRENT_SAMTRANS_SPACE, object);
            _getFreshData(object, Integer.parseInt(spaceStr));
            dialog.dismiss();
        });
        builder.show();
    }

    private void noRecordFoundDialog(String str) {
        AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(activity);
        confirmBuilder.setTitle("Space info for " + str)
                .setMessage("Space not found")
                .setCancelable(true)
                .setNegativeButton("Change Location", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            Vendor samtrans = Vendor.getVendorByName("Samtrans");
                            if (samtrans.getVendorId() > 0) {
                                ArrayList<VendorItem> vendorSamtrans = VendorItem.getVendorSamtrans(samtrans.getVendorId());
                                _popUpDialog(vendorSamtrans, samtranSpaceNumber);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });


        confirmBuilder.create().show();
    }

    private void displaySamtransInfo(final Datum parking, final String spaceName) {
        try {
            StringBuffer message = new StringBuffer();
            StringBuffer values = new StringBuffer();

            message.append("Lot name" + "\n");
            message.append("Space Number" + "\n");
            message.append("Purchased Date" + "\n");
            message.append("Expiry Date" + "\n");
            message.append("Expire" + "\n");
            //message.append("Payed" + "\n");

            values.append(": " + StringUtil.getDisplayString(parking.getLotName()) + "\n");
            values.append(": " + StringUtil.getDisplayString(parking.getSpaceNumber()) + "\n");
            values.append(": " + StringUtil.getDisplayString(DateUtil.getConvertedDateSamtrans(parking.getDatePurchased().trim())) + "\n");
            values.append(": " + StringUtil.getDisplayString(DateUtil.getConvertedDateSamtrans(parking.getExpiryDate().trim())) + "\n");
            values.append(": " + StringUtil.getDisplayString(parking.getExpireInfo().getExpireMsg()) + "\n");
            //  values.append(": $" + StringUtil.getDisplayString("" + parking.getAmount()) + "\n");

            View view = LayoutInflater.from(activity).inflate(R.layout.item_view, null);
            TextView headerTV = view.findViewById(R.id.headerTV);
            TextView valueTV = view.findViewById(R.id.valueTV);

            headerTV.setText(message.toString());
            valueTV.setText(values.toString());

          /*  WebView wv = new WebView (getBaseContext());
            wv.loadData(message.toString(), "text/html", "utf-8");
            wv.setBackgroundColor(android.graphics.Color.BLACK);
            wv.getSettings().setDefaultTextEncodingName("utf-8");*/

            AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
            dialog.setCancelable(false);
            dialog.setView(view);
            dialog.setTitle("Samtrans\nSpace info for " + spaceName);
            dialog.setNeutralButton("Change Location", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        Vendor samtrans = Vendor.getVendorByName("Samtrans");
                        if (samtrans.getVendorId() > 0) {
                            ArrayList<VendorItem> vendorSamtrans = VendorItem.getVendorSamtrans(samtrans.getVendorId());
                            _popUpDialog(vendorSamtrans, samtranSpaceNumber);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            if (parking.getExpireInfo().isExpired()) {
                dialog.setNegativeButton("Write Ticket", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        Ticket ticket = TPApp.createNewTicket();
                        //ticket.setPlate(parking.getCode());
                        TPApp.setActiveTicket(ticket);
                        Intent intent = new Intent();
                        intent.setClass(activity, WriteTicketActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("SAMTRANS", true);
                        intent.putExtra("LOC", spaceName);
                        activity.startActivity(intent);
                    }
                });
            }

            dialog.show();

        } catch (Exception e) {
            Log.e("TicketPRO", "Passport Zone Info " + e.getMessage());
        }
    }

    public synchronized void lookupSpaceHistory(final String spaceStr) {
        activity.isServiceAvailable = !Feature.isFeatureAllowed(Feature.TOGGLE_NET_ON_OFF) || !TPApplication.getInstance().getNetOnOff().equals("Y");
        if (!activity.isServiceAvailable) {
            return;
        }

        //disableBack();

        ArrayList<String> featureNames = new ArrayList<>();
        ArrayList<Feature> spaceLookupFeatures;

        featureNames.add(Feature.PASSPORT_PARKING);
        featureNames.add(Feature.IPS_GROUP);
        featureNames.add(Feature.PARK_MOBILE);
        featureNames.add(Feature.PAYBY_PHONE);
        featureNames.add(Feature.DIGITAL_PAYTECH);
        featureNames.add(Feature.MOBILE_NOW);
        featureNames.add(Feature.SAMTRANS);

        spaceLookupQueue = new LinkedList<>();

        try {
            spaceLookupFeatures = Feature.getFeatures(featureNames);

            for (Feature feature : spaceLookupFeatures) {
                if (Feature.PASSPORT_PARKING.equalsIgnoreCase(feature.getFeature())) {
                    if (TPApp.enablePassportParking) {
                        spaceLookupQueue.add(() -> lookupSpacePassportParking(spaceStr));
                    }
                }
                if (Feature.PASSPORT_PARKING2.equalsIgnoreCase(feature.getFeature())) {
                    if (TPApp.enablePassportParking2) {
                        spaceLookupQueue.add(() -> lookupSpacePassportParking2(spaceStr, "Looking space"));
                    }
                }

                if (Feature.IPS_GROUP.equalsIgnoreCase(feature.getFeature())) {
                    if (TPApp.enableIPS) {
                        spaceLookupQueue.add(() -> lookupSpaceIPS(spaceStr));
                    }
                }
                if (Feature.PARK_MOBILE.equalsIgnoreCase(feature.getFeature())) {
                    if (TPApp.enableParkMobile) {
                        spaceLookupQueue.add(() -> lookupSpaceParkMobile(spaceStr));
                    }
                }

                if (Feature.PAYBY_PHONE.equalsIgnoreCase(feature.getFeature())) {
                    if (TPApp.enablePayByPhone) {
                        spaceLookupQueue.add(() -> lookupSpacePayByPhone(spaceStr));
                    }
                }

                if (Feature.DIGITAL_PAYTECH.equalsIgnoreCase(feature.getFeature())) {
                    if (TPApp.enableDPT) {
                        spaceLookupQueue.add(() -> lookupSpaceDPT(spaceStr));
                    }
                }


                if (Feature.MOBILE_NOW.equalsIgnoreCase(feature.getFeature())) {
                    if (TPApp.enableMobileNow) {
                        spaceLookupQueue.add(() -> lookupSpaceMobileNow(spaceStr));
                    }
                }

                if (Feature.SAMTRANS.equalsIgnoreCase(feature.getFeature())) {
                    if (TPApp.enableSamtrans) {
                        spaceLookupQueue.add(() -> lookupSpaceSamtrans(spaceStr));
                    }
                }
            }

        } catch (Exception e1) {
            activity.log.error(TPUtility.getPrintStackTrace(e1));
        }
        processSpaceLookupQueue();
    }

    public void lookupEdmundsVin(String vinString) {
        final String vin = TPUtility.getValidVIN(vinString);
        // VIN Code for testing
        // 1gkfk66867j196460
        //final String vin="1GKFK66867J196460";
		/*if(vin!=null){
			checkVinHistory(vin);
			return;
		}*/
        ConnectivityManager connectivityManager = (ConnectivityManager) activity
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (!Feature.isFeatureAllowed(Feature.EDMUNDS_VIN_LOOKUP) || networkInfo == null || !networkInfo.isConnectedOrConnecting()) {
            checkVinHistory(vin);
            return;
        } else {
            try {
                Toast.makeText(TPApplication.getInstance().getApplicationContext(), "Please check internet connection.", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        TPAsyncTask task = new TPAsyncTask(activity, "Processing VIN...");
        task.execute(new TPTask() {
            @Override
            public void execute() {
                try {

                    //*String response = TPUtility.getURLResponse("https://api.edmunds.com/api/vehicle/v2/vins/" + vin 	+ "?fmt=json&api_key=37bdfp5mnwhavntptk5y6qjj");*//*
                    String response = TPUtility.getURLResponseWithTLS("https://vpic.nhtsa.dot.gov/api/vehicles/decodevinvalues/" + vin + "*BA?format=json");
                    //Retrofit.Builder retrofitbuilder = new Re

                    Bundle data = new Bundle();
                    data.putString(RESPONSE_DATA, response);

                    Message msg = obtainMessage();
                    msg.what = LOOKUP_EDMUNDS_VIN;
                    msg.setData(data);
                    sendMessage(msg);

                } catch (Exception e) {
                    activity.log.error(TPUtility.getPrintStackTrace(e));
                }
            }
        });

    }

    public void displayEdmundsVINLookupMsg(final String vin, final String response) {
        //if (TPUtility.isActivityRunning(activity)) {
        activity.runOnUiThread(() -> {
            try {
                StringBuilder message = new StringBuilder();
                StringBuilder values = new StringBuilder();

                final JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.has("status")) {
                    String status = jsonObject.getString("status");
                    if ("NOT_FOUND".equalsIgnoreCase(status)) {
                        checkVinHistory(vin);
                        return;
                    }
                }

                JSONArray resultArray = new JSONArray();
                resultArray = jsonObject.getJSONArray("Results");
                JSONObject resultObject = new JSONObject();
                resultObject = resultArray.getJSONObject(0);
                //final JSONObject resultObject = new JSONObject(resultArray);

                final String make = resultObject.getString("Make");
                final String model = resultObject.getString("Model");
                //final String category = resultObject.getString("DriveType");
                final String years = resultObject.getString("ModelYear");


                if (jsonObject.has("vin")) {
                    message.append("VIN" + "\n");
                    values.append(": " + StringUtil.getDisplayString(resultObject.getString("vin")) + "\n");

                }

                if (resultObject.has("Make")) {
                    message.append("Make" + "\n");
                    values.append(": " + StringUtil.getDisplayString(resultObject.getString("Make")) + "\n");

                }

                if (resultObject.has("Model")) {
                    message.append("Model" + "\n");
                    values.append(": " + StringUtil.getDisplayString(resultObject.getString("Model")));

                }

                if (resultObject.has("DriveType")) {
                    message.append("Body" + "\n");
                    values.append(": " + StringUtil.getDisplayString(resultObject.getString("DriveType")));

                }

                try {
                    //JSONObject yearObj = (JSONObject) yearsObj.get(0);

                    if (resultObject.has("ModelYear")) {
                        message.append("Year");
                        values.append(": " + resultObject.getInt("ModelYear"));

                    }
                } catch (Exception e) {
                }


                View view = LayoutInflater.from(activity.getBaseContext()).inflate(R.layout.item_view, null);
                TextView headerTV = view.findViewById(R.id.headerTV);
                TextView valueTV = view.findViewById(R.id.valueTV);

                headerTV.setText(message.toString());
                valueTV.setText(values.toString());

                    /*WebView wv = new WebView(activity.getBaseContext());
                    wv.loadData(message.toString(), "text/html", "utf-8");
                    wv.setBackgroundColor(android.graphics.Color.BLACK);
                    wv.getSettings().setDefaultTextEncodingName("utf-8");*/

                Builder dialog = new Builder(activity);
                dialog.setCancelable(false);
                dialog.setView(view);
                dialog.setTitle("VIN Lookup");
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                dialog.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        try {
                            if (make != null) {
                                String makeTitle = make;
                                MakeAndModel makeRecord = MakeAndModel.getMakeByTitle(makeTitle);
                                if (makeRecord == null) {
                                    makeRecord = MakeAndModel.getMakeByCode(makeTitle);
                                }

                                if (makeRecord != null) {
                                    makeTitle = StringUtil.getDisplayString(makeRecord.getMakeTitle());
                                    activity.makeCode = StringUtil.getDisplayString(makeRecord.getMakeCode());
                                    activity.makeId = makeRecord.getMakeId();
                                    activity.makeEditText.setText(makeTitle);
                                } else {
                                    activity.makeEditText.setText(makeTitle);
                                }
                            }

                                /*if (category != null ) {
                                    String bodyTitle = category;//categoriesObj.getString("primaryBodyType").toUpperCase();
                                    Body bodyRecord = Body.getBodyByTitle(bodyTitle);
                                    if (bodyRecord == null) {
                                        bodyRecord = Body.getBodyByCode(bodyTitle);
                                    }

                                    if (bodyRecord != null) {
                                        bodyTitle = StringUtil.getDisplayString(bodyRecord.getTitle());
                                        activity.bodyCode = StringUtil.getDisplayString(bodyRecord.getCode());
                                        activity.bodyId = bodyRecord.getId();
                                        activity.bodyEditText.setText(bodyTitle);
                                    }else {
                                        activity.bodyEditText.setText(bodyTitle);
                                    }
                                }*/

                                /*if (years != null ) {
                                    //JSONObject yearObj = (JSONObject) yearsObj.get(0);
                                    if (yearObj.has("year")) {
                                        activityyearEditText.setText(StringUtil.getDisplayString(yearObj.getInt("year")+""));
                                    }
                                }*/
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                dialog.show();

            } catch (Exception e) {
                activity.log.error("VIN Lookup Error " + e.getMessage());
            }
        });
        //}
    }

    public void displayCSLookupPermit(final String response, final String permit) {
        //if (TPUtility.isActivityRunning(activity)) {
        activity.runOnUiThread(() -> {
            StringBuilder msg = new StringBuilder();
            StringBuilder values = new StringBuilder();


            CSPlateNumber = null;
            StringTokenizer st = new StringTokenizer(response, "&");
            while (st.hasMoreTokens()) {
                String str = st.nextToken();
                if (str.length() > 0) {
                    String[] tokens = str.split("=");
                    if (tokens.length >= 2) {
                        if (tokens[0].equalsIgnoreCase("RESULT") && tokens[1].equalsIgnoreCase("ERROR")) {

                            doPermitLookup(permit);
                            return;
                        }

                        if (tokens[0].equals("SHAHASH")) {
                            continue;
                        } else if (tokens[0].contains("VEHPLATE")) {
                            CSPlateNumber = StringUtil.getDisplayString(tokens[1]);
                        } else if (tokens[0].contains("VEHBODY")) {
                            Body body = Body.getBodyByCode(tokens[1]);
                            if (body != null) {
                                tokens[1] = body.getTitle();
                            }
                        } else if (tokens[0].contains("VEHMAKE")) {
                            MakeAndModel make = MakeAndModel.getMakeByCode(tokens[1]);
                            if (make != null) {
                                tokens[1] = make.getMakeTitle();
                            }
                        } else if (tokens[0].contains("VEHMODEL")) {
                            MakeAndModel make = MakeAndModel.getModelByCode(tokens[1]);
                            if (make != null) {
                                tokens[1] = make.getModelTitle();
                            }
                        } else if (tokens[0].contains("VEHCOLOR")) {
                            Color color = Color.getColorByCode(tokens[1]);
                            if (color != null) {
                                tokens[1] = color.getTitle();
                            }
                        } else if (tokens[0].equals("PERMITTYPE")) {
                            tokens[0] = "TYPE";
                        }

                        msg.append(tokens[0] + "\n");
                        values.append(StringUtil.getDisplayString(tokens[1] + "\n"));

                    }
                }
            }

            View view = LayoutInflater.from(activity.getBaseContext()).inflate(R.layout.item_view, null);
            TextView headerTV = view.findViewById(R.id.headerTV);
            TextView valueTV = view.findViewById(R.id.valueTV);

            headerTV.setText(msg.toString());
            valueTV.setText(values.toString());

            acceptDetails = new CheckBox(activity);
            acceptDetails.setText(" Use Vehicle Info");

            LinearLayout layout = new LinearLayout(activity);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.addView(view);
            layout.addView(acceptDetails);

            new Builder(activity).setCancelable(false).setView(layout).setTitle("CSLookup Result")
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }).setPositiveButton("Continue Lookup", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                            if (acceptDetails.isChecked()) {
                                populateCSLookupDetails(response, true);
                            }

                            // Lookup for plate number only
                            if (CSPlateNumber != null) {
                                if (Feature.isFeatureAllowed(Feature.PARK_SEARCH_TRACK)) {
                                    logpm.info("doPlateLookup displayCSLookupPermit");
                                }
                                doPlateLookup(CSPlateNumber);
                            }
                        }
                    }).show();
        });
        //}
    }

    public void checkPermitHistory(String permitStr) {
        if (permitStr == null || permitStr.equals("")) {
            return;
        }
        disableBack();


        final String permit = TPUtility.getValidPermit(permitStr);
        activity.isServiceAvailable = !Feature.isFeatureAllowed(Feature.TOGGLE_NET_ON_OFF) || !TPApplication.getInstance().getNetOnOff().equals("Y");
        if (Feature.isFeatureAllowed(Feature.CS_LOOKUP) && activity.isServiceAvailable) {
            TPAsyncTask task = new TPAsyncTask(activity, "Processing Permit...");
            task.execute(new TPTask() {
                @Override
                public void execute() {
                    String FICEGROUP = "";
                    String FICE;
                    String params;
                    String serviceURL = "https://www.credentials-inc.com/cgi-bin/gacgiturbo.pgm?";
                    VendorServiceConfig config = VendorService.getServiceConfig(VendorService.CS_LOOKUP,
                            TPApp.deviceId);

                    if (config != null) {
                        FICE = config.getParamsMap().get("FICE");
                        FICEGROUP = config.getParamsMap().get("FICEGROUP");
                        serviceURL = config.getServiceURL();
                    } else {
                        params = Feature.getFeatureValue(Feature.CS_LOOKUP);
                        if (params == null || params.isEmpty()) {
                            FICE = "";
                        } else {
                            String[] tokens = params.split(",");
                            if (tokens.length > 1) {
                                FICEGROUP = tokens[1];
                            }
                            FICE = tokens[0];
                        }
                    }

                    if (FICE == null || FICE.equals("")) {
                        FICE = "XX1282";
                    }

                    if (FICEGROUP == null) {
                        FICEGROUP = "";
                    }

                    try {
                        String SHA1 = TPUtility.generateSHA1Hash(FICE + permit);
                        String encodedParams = "FICE=" + URLEncoder.encode(FICE, "UTF-8") + "&FICEGROUP="
                                + URLEncoder.encode(FICEGROUP, "UTF-8") + "&PERMIT="
                                + URLEncoder.encode(permit, "UTF-8") + "&SHAHASH=" + URLEncoder.encode(SHA1, "UTF-8");
                        TLSHttpRequestTask task = new TLSHttpRequestTask(LOOKUP_CS_PERMIT, new Bundle(), encodedParams);
                        task.executeTask(serviceURL + encodedParams);
                    } catch (Exception e) {
                        activity.log.error(TPUtility.getPrintStackTrace(e));
                    }
                }
            });
            enableBack();
            return;
        }
        doPermitLookup(permit);
    }

    public void populateCSLookupDetails(String response, boolean isPermitRequired) {
        if (response == null || response.isEmpty())
            return;

        StringTokenizer st = new StringTokenizer(response, "&");
        while (st.hasMoreTokens()) {
            String str = st.nextToken();
            if (str.length() > 0) {
                String[] tokens = str.split("=");
                if (tokens.length >= 2) {
                    if (isPermitRequired && tokens[0].equals("PERMIT")) {
                        activity.permitEditText.setText(tokens[1]);
                        activity.activeTicket.setPermit(tokens[1]);

                    } else if (tokens[0].equals("VEHPLATE")) {
                        activity.plateNumberEditText.setText(tokens[1]);
                        activity.activeTicket.setPlate(tokens[1]);

                    } else if (tokens[0].equals("VEHSTATE")) {
                        activity.stateEditText.setText(tokens[1]);
                        activity.activeTicket.setStateCode(tokens[1]);
                        activity.stateId = State.getStateIdByCode(tokens[1]);

                    } else if (tokens[0].equals("VEHMAKE")) {
                        activity.makeCode = tokens[1];
                        MakeAndModel make = MakeAndModel.getMakeByCode(activity.makeCode);
                        if (make != null) {
                            activity.makeId = make.getMakeId();
                            activity.makeEditText.setText(make.getMakeTitle());
                        } else {
                            activity.makeEditText.setText(activity.makeCode);
                        }
                    } else if (tokens[0].equals("VEHBODY")) {
                        activity.bodyCode = tokens[1];
                        Body body = Body.getBodyByCode(activity.bodyCode);
                        if (body != null) {
                            activity.bodyId = body.getId();
                            activity.bodyEditText.setText(body.getTitle());
                        } else {
                            activity.bodyEditText.setText(activity.bodyCode);
                        }

                    } else if (tokens[0].equals("VEHCOLOR")) {
                        activity.colorCode = tokens[1];
                        Color color = Color.getColorByCode(activity.colorCode);
                        if (color != null) {
                            activity.colorId = color.getId();
                            activity.colorEditText.setText(color.getTitle());
                        } else {
                            activity.colorEditText.setText(activity.colorCode);
                        }
                    }
                }
            }
        }
    }

    public void displayMobileNowPlateMsg(final String plate, final String response) {
        //if (TPUtility.isActivityRunning(activity)) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                StringBuffer message = new StringBuffer();
                message.append(MobileNowParser.getCarCheckResponseHTML(response));

                WebView wv = new WebView(activity.getBaseContext());
                wv.loadData(message.toString(), "text/html", "utf-8");
                wv.setBackgroundColor(android.graphics.Color.WHITE);
                wv.getSettings().setDefaultTextEncodingName("utf-8");

                Builder dialog = new Builder(activity);
                dialog.setCancelable(false);
                dialog.setView(wv);
                dialog.setTitle("MobileNow Lookup");
                dialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                if (plate != null && plate.length() > 0) {
                    dialog.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            processPlateLookupQueue(plate);
                        }
                    });
                }
                dialog.show();
            }
        });
        //}
    }

    public void doPlatePermitLookup(final String plate) {
        this.plate = plate;
        TPAsyncTask task = new TPAsyncTask(activity, "Processing Permit...");
        task.execute(new TPTask() {
            @Override
            public void execute() {
                try {
                    String state = activity.stateEditText.getText().toString();
                    ArrayList<Permit> historyPermit = null;
                    activity.isServiceAvailable = !Feature.isFeatureAllowed(Feature.TOGGLE_NET_ON_OFF) || !TPApplication.getInstance().getNetOnOff().equals("Y");
                    if (activity.isServiceAvailable) {
                        if (activity.screenBLProcessor instanceof WriteTicketBLProcessor)
                            historyPermit = ((WriteTicketBLProcessor) activity.screenBLProcessor).searchAllPermitByPlate(TPApp.custId, plate, state);
                    }
                        /*if (historyPermit == null) {
                            historyPermit = Permit.searchPermitHistory(permit);
                        }*/
                    if (historyPermit == null) {
                        activity.displayErrorMessage("Permit record not found.");
                    } else
                        displayPermitInfo(historyPermit.get(0), false);
                } catch (TPException e) {
                    activity.log.error(e.getMessage());
                } catch (Exception e) {
                    activity.log.error(TPUtility.getPrintStackTrace(e));
                }
            }
        });
    }

    @SuppressLint("SuspiciousIndentation")
    public void doPermitLookup(final String permit) {
        if (!Feature.isFeatureAllowed(Feature.LOOKUP_PERMIT)) {
            enableBack();
            return;
        }

        if (TPApp.enableProgressive && Feature.isFeatureAllowed(Feature.PROGRESSIVE)) {
            final VendorServiceConfig config = VendorService.getServiceConfig(VendorService.PROGRASSIVE_VALIDPERMIT, TPApp.deviceId);
            final String plate = activity.plateNumberEditText.getText().toString();
            if (config != null) {
                TPAsyncTask task = new TPAsyncTask(activity, "Processing Progressive...");
                task.execute(new TPTask() {
                    @Override
                    public void execute() {
                        String safePermit = permit.replaceAll(" ", "");
                        String safePlate = plate.replaceAll(" ", "");
                        Service1 service = new Service1(new com.ticketpro.vendors.progressive.IWsdl2CodeEvents() {
                            @Override
                            public void Wsdl2CodeStartedRequest() {
                            }

                            @Override
                            public void Wsdl2CodeFinishedWithException(Exception ex) {
                                activity.log.error(TPUtility.getPrintStackTrace(ex));
                            }

                            @Override
                            public void Wsdl2CodeFinished(String methodName, Object Data) {
                            }

                            @Override
                            public void Wsdl2CodeEndedRequest() {
                            }
                        });

                        int permitNumber = 0;
                        try {
                            permitNumber = Integer.parseInt(safePermit);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        //service.setUrl(config.getServiceURL());
                        boolean result = service.ValidPermit(permitNumber, safePlate);

                        Bundle data = new Bundle();
                        data.putString(RESPONSE_DATA, String.valueOf(result));
                        data.putBoolean("IsValidPermit", result);

                        Message msg = obtainMessage();
                        msg.what = LOOKUP_PROGRESSIVE_PLATE;
                        msg.setData(data);

                        sendMessage(msg);
                    }
                });
                enableBack();
                return;
            }
        }

        /*TPAsyncTask task = new TPAsyncTask(activity, "Processing Permit...");
        task.execute(new TPTask() {
            @Override
            public void execute() {
                try {
                    Permit historyPermit = null;
                    activity.isServiceAvailable = !Feature.isFeatureAllowed(Feature.TOGGLE_NET_ON_OFF) || !TPApplication.getInstance().getNetOnOff().equals("Y");
                    if (activity.isServiceAvailable) {
                        if (activity.screenBLProcessor instanceof WriteTicketBLProcessor) {
                            progressDialog = new ProgressDialog(activity);
                            progressDialog.setMessage("Processing Permit...");
                             historyPermit = ((WriteTicketBLProcessor) activity.screenBLProcessor).searchPermitHistory(permit);

                        }
                    }
					*//*if (historyPermit == null) {
						historyPermit = Permit.searchPermitHistory(permit);
					}*//*

                    if (historyPermit == null) {
                        activity.displayErrorMessage("Permit record not found.");
                        enableBack();
                        return;
                    }
                    enableBack();
                    displayPermitInfo(historyPermit, false);
                } catch (TPException e) {
                    activity.log.error(e.getMessage());
                } catch (Exception e) {
                    activity.log.error(TPUtility.getPrintStackTrace(e));
                }
            }
        });*/

        // This Code is changed by mohit 4/4/2023
        try {
            Permit historyPermit = null;
            activity.isServiceAvailable = !Feature.isFeatureAllowed(Feature.TOGGLE_NET_ON_OFF) || !TPApplication.getInstance().getNetOnOff().equals("Y");
            if (activity.isServiceAvailable) {
                if (activity.screenBLProcessor instanceof WriteTicketBLProcessor) {
                    progressDialog = new ProgressDialog(activity);
                    progressDialog.setMessage("Processing Permit...");
                    progressDialog.setCancelable(true);
                    // historyPermit = ((WriteTicketBLProcessor) activity.screenBLProcessor).searchPermitHistory(permit);
                    progressDialog.show();
                    ((WriteTicketBLProcessor) activity.screenBLProcessor).searchPermitHistory1(permit, this);
                }
            }

        } catch (TPException e) {
            activity.log.error(e.getMessage());
        } catch (Exception e) {
            activity.log.error(TPUtility.getPrintStackTrace(e));
        }

        //End
    }

    public void displayPermitInfo(final Permit historyPermit, final boolean processOnAccept) {
        if (historyPermit == null) {
            return;
        }

        //if (TPUtility.isActivityRunning(activity)) {
        activity.runOnUiThread(() -> {
            try {
                LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.item_view_1, null);
                   /* alertDialogBuilder.setView(view);
                    alertDialogBuilder.setCancelable(false);
                    final AlertDialog dialog = alertDialogBuilder.create();
                    dialog.show();*/
                // View view = LayoutInflater.from(activity.getBaseContext()).inflate(R.layout.item_view_1, null);
                TextView headerTV = view.findViewById(R.id.id_version);
                TextView _type = view.findViewById(R.id.id_type);
                TextView _address = view.findViewById(R.id.id_address);
                TextView _Plate = view.findViewById(R.id.id_plate);
                TextView _Vin = view.findViewById(R.id.id_VIN);
                TextView _State = view.findViewById(R.id.id_State);
                TextView _Body = view.findViewById(R.id.id_Body);
                TextView _Color = view.findViewById(R.id.id_Color);
                TextView _Make = view.findViewById(R.id.id_Make);
                TextView _Exp_Date = view.findViewById(R.id.id_Exp_Date);
                TextView _status = view.findViewById(R.id.id_Status);
                TextView _being_date = view.findViewById(R.id.id_Begin_ate);
                TextView _end_date = view.findViewById(R.id.id_End_Date);
                String plate = historyPermit.getPlate();
                String vin = historyPermit.getVin();
                String bodyCode = historyPermit.getBodyCode();
                String colorCode = historyPermit.getColorCode();
                String makeCode = historyPermit.getMakeCode();
                String stateCode = historyPermit.getStateCode();
                String classCode = historyPermit.getClassCode();

                   /* // Get Expiration Date
                    String expDate = "";
                    if (historyPermit.getExpiration() != null) {
                        String[] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV",
                                "DEC"};
                        String monthName = "";
                        int month = historyPermit.getExpiration().getMonth();
                        if (month > 0) {
                            monthName = months[month - 1];
                        }

                        expDate = monthName + "/" + (historyPermit.getExpiration().getYear() + 1900);
                    }*/

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

                headerTV.setText("Found Permit Records. Do you want to use permit record below ?");
                _type.setText(": "
                        + StringUtil.getDisplayString(historyPermit.getPermitType()));
                _address.setText(": " + StringUtil.getDisplayString(historyPermit.getAddress1()) + " - " + StringUtil.getDisplayString(historyPermit.getPermitCode()));
                _Plate.setText(": " + StringUtil.getDisplayString(plate));
                _Vin.setText(": " + StringUtil.getDisplayString(vin));
                _State.setText(": " + StringUtil.getDisplayString(stateCode));
                _Body.setText(": " + StringUtil.getDisplayString(bodyTitle));
                _Color.setText(": " + StringUtil.getDisplayString(colorTitle));
                _Make.setText(": " + StringUtil.getDisplayString(makeTitle));
                _Exp_Date.setText(": " + StringUtil.getDisplayString(DateUtil.getStringFromDate1(historyPermit.getExpiration())));
                _status.setText(": " + StringUtil.getDisplayString(classCode));
                _being_date.setText(": "
                        + StringUtil.getDisplayString(DateUtil.getStringFromDate(historyPermit.getBeginDate())));
                _end_date.setText(": "
                        + StringUtil.getDisplayString(DateUtil.getStringFromDate(historyPermit.getEndDate())));
                new Builder(activity).setCancelable(false).setView(view)
                        .setTitle("Permit\nLookup Result - " + historyPermit.getPermitNumber())
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                activity.populatePermitValues(historyPermit);
                                if (processOnAccept) {
                                    checVinHistoryByGetAPermit(historyPermit.getVin());
                                }
                            }
                        }).show();

            } catch (Exception e) {
                activity.log.error(TPUtility.getPrintStackTrace(e));
            }
        });
        //}
    }

    public void displayPlateCheckMsg(final String response) {
        if (response == null || response.isEmpty()) {
            return;
        }

        //if (TPUtility.isActivityRunning(activity)) {
        activity.runOnUiThread(() -> {
            float amountOwed = 0;

            StringBuffer message = new StringBuffer();
            StringBuffer values = new StringBuffer();
            StringBuffer header = new StringBuffer();
            View view = LayoutInflater.from(activity.getBaseContext()).inflate(R.layout.item_view, null);
            TextView headerTV = view.findViewById(R.id.headerTV);
            TextView valueTV = view.findViewById(R.id.valueTV);
            TextView headTV = view.findViewById(R.id.headerTextTV);
            if (response.contains("-$")) {
                String[] respones = response.split("-");
                tickets = respones[0];
                amount = respones[1];
                try {
                    amountOwed = Float.parseFloat(amount.substring(1));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                message.append("Number Of Tickets" + "\n");
                values.append(tickets + "\n");
                message.append("Amount Owed");
                values.append(amount);


            } else {
                header.append(response);
                headTV.setVisibility(View.VISIBLE);
                headerTV.setVisibility(View.GONE);
                valueTV.setVisibility(View.GONE);
            }


            headTV.setText(header.toString());
            headerTV.setText(message.toString());
            valueTV.setText(values.toString());

                /*WebView wv = new WebView(activity.getBaseContext());
                wv.loadData(message.toString(), "text/html", "utf-8");
                wv.setBackgroundColor(android.graphics.Color.WHITE);
                wv.getSettings().setDefaultTextEncodingName("utf-8");*/

            Builder dialog = new Builder(activity);
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
                dialog.setNeutralButton("Send Email", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        activity.sendTowNotifyEmail(tickets, amount);
                    }
                });
            }
            dialog.show();
        });
        //}
    }

    private void displayVehicleInfoMsg(final PassportParkingVehicle passportParkingVehicle, final String plate) {
        //if (TPUtility.isActivityRunning(activity)) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    StringBuffer message = new StringBuffer();
                    StringBuffer values = new StringBuffer();


                    message.append("Name" + "\n");


                    message.append("Number" + "\n");


                    message.append("Plate" + "\n");


                    message.append("State" + "\n");


                    message.append("Entry" + "\n");


                    message.append("Exit" + "\n");

                    values.append(": " + StringUtil.getDisplayString(passportParkingVehicle.getZoneName()) + "\n");


                    values.append(": " + StringUtil.getDisplayString(passportParkingVehicle.getZoneNumber()) + "\n");


                    values.append(": " + StringUtil.getDisplayString(passportParkingVehicle.getLicensePlateNumber()) + "\n");


                    values.append(": " + StringUtil.getDisplayString(passportParkingVehicle.getStateCode()) + "\n");


                    values.append(": " + StringUtil.getDisplayString(passportParkingVehicle.getEntryTime()) + "\n");


                    values.append(": " + StringUtil.getDisplayString(passportParkingVehicle.getExitTime()) + "\n");

                    String expireStr = "";
                    long expireInMs = Long.parseLong(passportParkingVehicle.getExpirationTimeInSecs()) * 1000;
                    if (expireInMs > 0) {
                        long expireMinutes = expireInMs / (60 * 1000) % 60;
                        long expireHours = expireInMs / (60 * 60 * 1000) % 24;
                        long expireDays = expireInMs / (24 * 60 * 60 * 1000);

                        if (expireDays > 0) {
                            expireStr = "in " + expireDays + " days " + expireHours + " hrs";
                        } else if (expireHours > 0) {
                            expireStr = "in " + expireHours + " hrs " + expireMinutes + " mins";
                        } else if (expireMinutes > 0) {
                            expireStr = "in " + expireMinutes + " mins";
                        } else {
                            expireStr = "in " + passportParkingVehicle.getExpirationTimeInSecs() + " secs";
                        }
                    }

                    message.append("Expire" + "\n");
                    values.append(": " + StringUtil.getDisplayString(expireStr) + "\n");

                        /*WebView wv = new WebView(activity.getBaseContext());
                        wv.loadData(message.toString(), "text/html", "utf-8");
                        wv.setBackgroundColor(android.graphics.Color.BLACK);
                        wv.getSettings().setDefaultTextEncodingName("utf-8");*/

                    View view = LayoutInflater.from(activity.getBaseContext()).inflate(R.layout.item_view, null);
                    TextView headerTV = view.findViewById(R.id.headerTV);
                    TextView valueTV = view.findViewById(R.id.valueTV);
                    headerTV.setText(message.toString());
                    valueTV.setText(values.toString());
                    Builder dialog = new Builder(activity);
                    dialog.setCancelable(false);
                    dialog.setView(view);
                    dialog.setTitle("PassportParking\nPlate Info: " + plate);
                    dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    dialog.setNegativeButton("Continue", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            processPlateLookupQueue(plate);
                        }
                    });

                    dialog.show();

                } catch (Exception e) {
                    Log.e("TicketPRO", "Passport Parking Plate Info " + e.getMessage());
                }
            }
        });
        //}
    }

    public void checkPlate(final String plate) {

        TPAsyncTask task = new TPAsyncTask(activity, "Retrieving Plate info...");
        task.execute(new TPTask() {
            @Override
            public void execute() {

                String plate = activity.plateNumberEditText.getText().toString();
                String state = activity.stateEditText.getText().toString();

                CustomerInfo customer = TPApp.getCustomerInfo();
                String agencyCode = customer.getAgencyCode();

                try {
					/*String response = TPUtility.getURLResponse("https://www.pticket.com/platecheck.asp?lic=" + plate
							+ "&st=" + state + "&agcy=" + agencyCode);*/

                    String response = TPUtility.getURLResponseWithTLS("https://www.pticket.com/platecheck.asp?lic=" + plate
                            + "&st=" + state + "&agcy=" + agencyCode);

                    //getURLResponseWithTLS will use new TLS implementation
                    Pattern pattern = Pattern.compile("\\d+\\;url\\=(.*)");
                    Matcher matcher = pattern.matcher(response);
                    if (matcher.find()) {
                        String url = matcher.group(1);
                        //url = url.replaceAll("'>", "").trim();
                        assert url != null;
                        url = url.substring(0, url.indexOf("<")).trim();
                        response = TPUtility.getURLResponseWithTLS(url);
                    }

                    Bundle data = new Bundle();
                    data.putString(RESPONSE_DATA, response);

                    Message msg = obtainMessage();
                    msg.what = LOOKUP_PLATE_CHECK;
                    msg.setData(data);
                    sendMessage(msg);

                } catch (Exception e) {
                    activity.log.error(TPUtility.getPrintStackTrace(e));
                }

            }
        });

    }

    public void displaySpaceInfoMsg(final PassportParkingSpace passportParkingSpace) {
        //if (TPUtility.isActivityRunning(activity)) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    StringBuffer message = new StringBuffer();
                    StringBuffer values = new StringBuffer();


                    message.append("Name" + "\n");


                    message.append("Number" + "\n");


                    message.append("Active LPN" + "\n");


                    message.append("Status" + "\n");

                    values.append(": " + StringUtil.getDisplayString(passportParkingSpace.getZoneName()) + "\n");


                    values.append(": " + StringUtil.getDisplayString(passportParkingSpace.getZoneNumber()) + "\n");


                    values.append(": " + StringUtil.getDisplayString(passportParkingSpace.getActivelpn()) + "\n");


                    values.append(": " + StringUtil.getDisplayString(passportParkingSpace.getStatus()) + "\n");


                    String expireStr = "";
                    long expireInMs = Long.parseLong(passportParkingSpace.getExpirationTimeInSecs()) * 1000;
                    if (expireInMs > 0) {
                        long expireMinutes = expireInMs / (60 * 1000) % 60;
                        long expireHours = expireInMs / (60 * 60 * 1000) % 24;
                        long expireDays = expireInMs / (24 * 60 * 60 * 1000);

                        if (expireDays > 0) {
                            expireStr = "in " + expireDays + " days " + expireHours + " hrs";
                        } else if (expireHours > 0) {
                            expireStr = "in " + expireHours + " hrs " + expireMinutes + " mins";
                        } else if (expireMinutes > 0) {
                            expireStr = "in " + expireMinutes + " mins";
                        } else {
                            expireStr = "in " + passportParkingSpace.getExpirationTimeInSecs() + " secs";
                        }
                    }

                    message.append("Expire" + "\n");
                    values.append(": " + StringUtil.getDisplayString(expireStr) + "\n");


                        /*WebView wv = new WebView(activity.getBaseContext());
                        wv.loadData(message.toString(), "text/html", "utf-8");
                        wv.setBackgroundColor(android.graphics.Color.BLACK);
                        wv.getSettings().setDefaultTextEncodingName("utf-8");*/

                    View view = LayoutInflater.from(activity.getBaseContext()).inflate(R.layout.item_view, null);
                    TextView headerTV = view.findViewById(R.id.headerTV);
                    TextView valueTV = view.findViewById(R.id.valueTV);
                    headerTV.setText(message.toString());
                    valueTV.setText(values.toString());
                    Builder dialog = new Builder(activity);
                    dialog.setCancelable(false);
                    dialog.setView(view);
                    dialog.setTitle("PassportParking\nZone Info");
                    dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();

                } catch (Exception e) {
                    Log.e("TicketPRO", "Passport Zone Info " + e.getMessage());
                }
            }
        });
        //}
    }

    public void selectIPSSpaceGroup(final String space, final String spaceGroup) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.activity);
        View headerView = LayoutInflater.from(this.activity).inflate(R.layout.dialog_header, null);
        final TextView titleView = headerView.findViewById(R.id.header_title);
        if (spaceGroup.equalsIgnoreCase("spaceGroup"))
            titleView.setText(activity.getText(R.string.select_spacegroup));
        else
            titleView.setText(activity.getText(R.string.select_subArea));


        TextView groupName = new TextView(this.activity);
        groupName.setText(TPApp.IPSSpaceGroup);

        LinearLayout actionLayout = headerView.findViewById(R.id.header_actions);
        actionLayout.addView(groupName);
        builder.setCustomTitle(headerView);

        try {
            String ipsValue = Feature.getFeatureValue(Feature.IPS_GROUP);
            if (ipsValue == null) {
                ipsValue = "";
            }
            VendorServiceConfig config = null;
            if (spaceGroup.equalsIgnoreCase("spaceGroup")) {
                if (Feature.isFeatureAllowed(Feature.PARK_SEARCH_TRACK)) {

                    logpm.info(VendorService.IPS_SPACEINFO);
                }
                config = VendorService.getServiceConfig(VendorService.IPS_SPACEINFO, TPApp.deviceId);
            } else if (ipsValue.equalsIgnoreCase("plate")) {
                if (Feature.isFeatureAllowed(Feature.PARK_SEARCH_TRACK)) {

                    logpm.info(VendorService.IPS_PLATEBYSUBAREA);
                }
                config = VendorService.getServiceConfig(VendorService.IPS_PLATEBYSUBAREA, TPApp.deviceId);
            } else {
                if (Feature.isFeatureAllowed(Feature.PARK_SEARCH_TRACK)) {

                    logpm.info(VendorService.IPS_MULTISPACE_GROUP);
                }
                config = VendorService.getServiceConfig(VendorService.IPS_MULTISPACE_GROUP, TPApp.deviceId);
            }
            if (Feature.isFeatureAllowed(Feature.PARK_SEARCH_TRACK)) {

                logpm.info("Vendor ID:" + config.getVendorId());
            }
            if (config != null) {
                final ArrayList<VendorItem> groups;
                if (spaceGroup.equalsIgnoreCase("spaceGroup")) {
                    groups = VendorItem.getVendorItems(config.getVendorId(), "SpaceGroup");
                } else {
                    groups = VendorItem.getVendorItems(config.getVendorId(), "SubArea");
                }

                Collections.sort(groups, new SortByName());

                ListView listView = new ListView(this.activity);
                String[] from = new String[]{"search_title"};
                int[] to = new int[]{R.id.search_textview};

                List<HashMap<String, String>> fillMaps = new ArrayList<HashMap<String, String>>();
                for (VendorItem item : groups) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("search_title", item.getItemName());
                    fillMaps.add(map);
                }

                SimpleAdapter adapter = new SimpleAdapter(this.activity, fillMaps, R.layout.search_list_item, from, to);
                listView.setAdapter(adapter);
                String finalIpsValue = ipsValue;
                listView.setOnItemClickListener((viewAdapter, view, pos, arg3) -> {
                    VendorItem item = groups.get(pos);
                    if (spaceGroup.equalsIgnoreCase("spaceGroup"))
                        TPApp.IPSSpaceGroup = item.getItemCode();
                    else
                        TPApp.IPSSubArea = item.getItemCode();
                    lookupDialog.dismiss();

                    //if (Feature.isFeatureAllowed(Feature.IPS_MULTISPACE)) {
                    if (!space.isEmpty()) {
                        lookupspaceIPS(space);
                    } else {
                        if (TPApp.isServiceAvailable()) {
                            if (finalIpsValue.equalsIgnoreCase("plate")) {
                                Intent ipsLotInfoIntent = new Intent();
                                ipsLotInfoIntent.setClass(activity, IPSPlateDetails.class);
                                ipsLotInfoIntent.putExtra(TPConstant.LOT_NAME, item.getItemCode());
                                ipsLotInfoIntent.putExtra(TPConstant.LOT_DESC, item.getItemName());
                                ipsLotInfoIntent.putExtra("spaceGroup", spaceGroup);
                                activity.startActivityForResult(ipsLotInfoIntent, activity.REQUEST_IPS_MULTISPACE);
                            } else {
                                Intent ipsLotInfoIntent = new Intent();
                                ipsLotInfoIntent.setClass(activity, IPSLotDetails.class);
                                ipsLotInfoIntent.putExtra(TPConstant.LOT_NAME, item.getItemCode());
                                ipsLotInfoIntent.putExtra(TPConstant.LOT_DESC, item.getItemName());
                                ipsLotInfoIntent.putExtra("spaceGroup", spaceGroup);
                                activity.startActivityForResult(ipsLotInfoIntent, activity.REQUEST_IPS_MULTISPACE);
                            }
                        } else {
                            Toast.makeText(activity, "Please connect to internet and Re-Try", Toast.LENGTH_SHORT).show();
                        }
                    }
                    return;

                });

                builder.setView(listView);
            }

            lookupDialog = builder.create();
            lookupDialog.show();

        } catch (Exception e) {
            logpm.error(TPUtility.getPrintStackTrace(e));
            e.printStackTrace();
        }
    }

    private void displayPlateHistory(final Ticket historyTicket) {
        if (historyTicket == null) {
            return;
        }

        //if (TPUtility.isActivityRunning(activity)) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                try {

                    StringBuffer msg = new StringBuffer();
                    StringBuffer values = new StringBuffer();
                    StringBuffer headText = new StringBuffer();

                    if (historyTicket.isWarn()) {
                        headText.append("This vehicle was previously warned on "
                                + DateUtil.getStringFromDate2(historyTicket.getTicketDate()));
                    } else {
                        headText.append("This vehicle was previously ticketed on "
                                + DateUtil.getStringFromDate2(historyTicket.getTicketDate()));
                    }

                    if (historyTicket.getViolationCode() == null
                            || StringUtil.getDisplayString(historyTicket.getViolationCode()).equals("")) {
                        ArrayList<TicketViolation> violations = TicketViolation.getTicketViolationsByCitation(
                                historyTicket.getCitationNumber());
                        if (violations == null || violations.size() == 0) {
                            activity.isServiceAvailable = !Feature.isFeatureAllowed(Feature.TOGGLE_NET_ON_OFF) || !TPApplication.getInstance().getNetOnOff().equals("Y");
                            if (activity.isServiceAvailable) {
                                if (activity.screenBLProcessor instanceof WriteTicketBLProcessor)
                                    violations = ((WriteTicketBLProcessor) activity.screenBLProcessor)
                                            .getLiveTicketViolations(historyTicket.getCitationNumber());
                            }
                        }

                        if (violations != null && violations.size() > 0) {
                            msg.append("Code" + "\n");
                            msg.append("Violation" + "\n");

                            values.append("" + violations.get(0).getViolationCode() + "\n");
                            values.append("" + violations.get(0).getViolationDesc() + "\n");
                        }
                    } else {
                        msg.append("Code" + "\n");
                        msg.append("Violation" + "\n");

                        values.append(": " + historyTicket.getViolationCode() + "\n");
                        values.append(": " + historyTicket.getViolation() + "\n");
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

                    msg.append("Plate" + "\n");

                    msg.append("VIN" + "\n");

                    msg.append("State" + "\n");

                    msg.append("Body" + "\n");

                    msg.append("Color" + "\n");

                    msg.append("Make" + "\n");

                    msg.append("ExpDate" + "\n");

                    msg.append("Location" + "\n");

                    values.append(": " + StringUtil.getDisplayString(historyTicket.getPlate()) + "\n");

                    values.append(": " + StringUtil.getDisplayString(historyTicket.getVin()) + "\n");

                    values.append(": " + StringUtil.getDisplayString(historyTicket.getStateCode()) + "\n");

                    values.append(": " + StringUtil.getDisplayString(bodyTitle) + "\n");

                    values.append(": " + StringUtil.getDisplayString(colorTitle) + "\n");

                    values.append(": " + StringUtil.getDisplayString(makeTitle) + "\n");

                    values.append(": " + StringUtil.getDisplayString(historyTicket.getExpiration()) + "\n");

                    values.append(": " + TPUtility.getFullAddress(historyTicket) + "\n");


                    User userInfo = User.getUserInfo(historyTicket.getUserId());
                    if (userInfo != null) {
                        String name = StringUtil.getDisplayString(userInfo.getFirstName()) + " "
                                + StringUtil.getDisplayString(userInfo.getLastName());
                        msg.append("Officer" + "\n");
                        values.append(": " + name + " (" + userInfo.getBadge() + ")" + "\n");

                    }
                    View view = LayoutInflater.from(activity.getBaseContext()).inflate(R.layout.item_view, null);
                    TextView headerTV = view.findViewById(R.id.headerTV);
                    TextView valueTV = view.findViewById(R.id.valueTV);
                    TextView headTV = view.findViewById(R.id.headerTextTV);
                    headerTV.setText(msg.toString());
                    valueTV.setText(values.toString());
                    headTV.setText(headText.toString());
                        /*WebView wv = new WebView(activity.getBaseContext());
                        wv.loadData(msg.toString(), "text/html", "utf-8");
                        wv.setBackgroundColor(android.graphics.Color.TRANSPARENT);
                        wv.getSettings().setDefaultTextEncodingName("utf-8");*/

                    new Builder(activity).setCancelable(false).setView(view).setTitle("Lookup Result")
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            }).setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();

                                    activity.populateValues(historyTicket, false);
                                }
                            }).show();

                } catch (Exception e) {
                    activity.log.error(TPUtility.getPrintStackTrace(e));
                }

            }
        });
        //}
    }

    @Override
    public void meterResponse(Meter meter, String meterString) {
        progressDialog.dismiss();
        historyMeter = meter;
        // enableBack();
        displayMeterHistory(historyMeter, meterString);
    }

    @Override
    public void permitResponse(Permit permit) {
        progressDialog.dismiss();
        if (permit == null) {
            activity.displayErrorMessage("Permit record not found.");
            enableBack();
            return;
        }
        enableBack();
        displayPermitInfo(permit, false);
    }

    @Override
    public void permitResponseFail() {
        progressDialog.dismiss();
        enableBack();
    }

    @Override
    public void permitVinResponse(Permit permit, String Vin) {
        progressDialog.dismiss();
        if (permit == null) {
            checVinHistoryByGetAPermit(Vin);
            //Toast.makeText(TPApplication.getInstance().getApplicationContext(), "No permit record found", Toast.LENGTH_SHORT).show();
            return;
        }
        displayPermitInfo(permit, true);
    }

    @Override
    public void searchVinHistory(Ticket ticket) {

        if (ticket != null) {
            displayPlateHistory(ticket);
        } else {
            // Can't create handler inside thread that has not called Looper.prepare()
            //if (TPUtility.isActivityRunning(activity)) {
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(activity.getApplicationContext(), "No record found.", Toast.LENGTH_SHORT).show();
                }
            });
            //}
        }

    }

    static class SortByName implements Comparator<VendorItem> {
        public int compare(VendorItem a, VendorItem b) {
            return a.getItemName().compareTo(b.getItemName());
        }
    }

/*
	private void displayPlateHistoryByPermitRecord(final Permit historyPermit,final Ticket historyTicket) {
		if (historyPermit == null) {
			try {
    			Toast.makeText(TPApp.getApplicationContext(), "Given VIN doesn't have any record.", Toast.LENGTH_SHORT).show();
			} catch(Exception e) { e.printStackTrace(); }
			return;
		}
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				try {
					StringBuffer msg = new StringBuffer();
					msg.append("<style>body{color:#fff;} table{color:#fff;} td{vertical-align:top;}</style>");
					if(historyTicket==null){
						msg.append("<p></p>");
						msg.append("<table>");
					}
					else{
					if (historyTicket.isWarn()) {
						msg.append("<p>This vehicle was previously warned on "
								+ DateUtil.getStringFromDate2(historyTicket.getTicketDate()));
					} else {
						msg.append("<p>This vehicle was previously ticketed on "
								+ DateUtil.getStringFromDate2(historyTicket.getTicketDate()));
					}

					msg.append("</p>");
					msg.append("<table>");

					if (historyTicket.getViolationCode() == null
							|| StringUtil.getDisplayString(historyTicket.getViolationCode()).equals("")) {
						ArrayList<TicketViolation> violations = TicketViolation.getTicketViolationsByCitation(
								DatabaseHelper.getInstance(), historyTicket.getCitationNumber());
						if (violations == null || violations.size() == 0) {
							if (activity.isServiceAvailable) {
								violations = ((WriteTicketBLProcessor) activity.screenBLProcessor)
										.getLiveTicketViolations(historyTicket.getCitationNumber());
							}
						}

						if (violations != null && violations.size() > 0) {
							msg.append("<tr><td>Code</td><td>:</td><td>" + violations.get(0).getViolationCode()
									+ "</td></tr>");
							msg.append("<tr><td>Violation</td><td>:</td><td>" + violations.get(0).getViolationDesc()
									+ "</td></tr>");
						}
					} else {
						msg.append("<tr><td>Code</td><td>:</td><td>" + historyTicket.getViolationCode() + "</td></tr>");
						msg.append(
								"<tr><td>Violation</td><td>:</td><td>" + historyTicket.getViolation() + "</td></tr>");
					}
					}

					String bodyTitle = historyPermit.getBodyCode();
					String colorTitle = historyPermit.getColorCode();
					String makeTitle = historyPermit.getMakeCode();
					Body body = Body.getBodyByCode(historyPermit.getBodyCode());
					if (body != null) {
						bodyTitle = body.getTitle();
					}

					Color color = Color.getColorByCode(historyPermit.getColorCode());
					if (color != null) {
						colorTitle = color.getTitle();
					}

					MakeAndModel make = MakeAndModel.getMakeByCode(historyPermit.getMakeCode());
					if (make != null) {
						makeTitle = make.getMakeTitle();
					}

					msg.append(
							"<tr><td>Plate</td><td>:</td><td>" + StringUtil.getDisplayString(historyPermit.getPlate()));
					msg.append("</td></tr>");
					msg.append("<tr><td>VIN</td><td>:</td><td>" + StringUtil.getDisplayString(historyPermit.getVin()));
					msg.append("</td></tr>");
					msg.append("<tr><td>State</td><td>:</td><td>"
							+ StringUtil.getDisplayString(historyPermit.getStateCode()));
					msg.append("</td></tr>");
					msg.append("<tr><td>Body</td><td>:</td><td>" + StringUtil.getDisplayString(bodyTitle));
					msg.append("</td></tr>");
					msg.append("<tr><td>Color</td><td>:</td><td>" + StringUtil.getDisplayString(colorTitle));
					msg.append("</td></tr>");
					msg.append("<tr><td>Make</td><td>:</td><td>" + StringUtil.getDisplayString(makeTitle));
					msg.append("</td></tr>");
					msg.append("<tr><td>ExpDate</td><td>:</td><td>"
							+ StringUtil.getDisplayString(historyPermit.getExpiration() + ""));
					msg.append("</td></tr>");
					if(historyPermit.getAddress1()!=null && historyPermit.getAddress2()!=null && !historyPermit.getAddress1().equalsIgnoreCase("null") && !historyPermit.getAddress2().equalsIgnoreCase("null")){
					msg.append("<tr><td>Location</td><td>:</td><td>" + historyPermit.getAddress1()+ historyPermit.getAddress2());
					msg.append("</td></tr>");}
					else{
						if(historyTicket!=null){
							msg.append("<tr><td>Location</td><td>:</td><td>" + TPUtility.getFullAddress(historyTicket));
							msg.append("</td></tr>");
						}
					}

					if(historyTicket!=null){
					User userInfo = User.getUserInfo(historyTicket.getUserId());
					if (userInfo != null) {
						String name = StringUtil.getDisplayString(userInfo.getFirstName()) + " "
								+ StringUtil.getDisplayString(userInfo.getLastName());
						msg.append("<tr><td>Officer</td><td>:</td><td>" + name + " (" + userInfo.getBadge() + ")");
						msg.append("</td></tr>");
					}}

					msg.append("</table>");

					WebView wv = new WebView(activity.getBaseContext());
					wv.loadData(msg.toString(), "text/html", "utf-8");
					wv.setBackgroundColor(android.graphics.Color.TRANSPARENT);
					wv.getSettings().setDefaultTextEncodingName("utf-8");

					new AlertDialog.Builder(activity).setCancelable(false).setView(wv).setTitle("Lookup Result")
							.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.cancel();
								}
							}).setPositiveButton("Accept", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();

									activity.populatePermitValues(historyPermit, false, historyTicket);
								}
							}).show();

				} catch (Exception e) {
					activity.log.error(TPUtility.getPrintStackTrace(e));
				}
			}
		});
	}
*/

    private class LookupParkeonPlate extends AsyncTask<Void, Void, Void> {

        private final String plate;
        private final String zone_id;
        private final Logger log = Logger.getLogger("RequestLogTask");
        private String response = null;

        public LookupParkeonPlate(String plate, String zone_id) {
            this.plate = plate;
            this.zone_id = zone_id;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage("Processing Parkeon...");
            progressDialog.setCancelable(false);
            progressDialog.setButton(DialogInterface.BUTTON_NEUTRAL, preference.getString(TPConstant.PARKEON_ZONE_NAME), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(activity, ParkeonControlGroup.class);
                    TPConstant.parkeon = true;
                    intent.putExtra("from", "lookup");
                    activity.startActivity(intent);
                    task.cancel(true);
                }
            });
            progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            final VendorServiceConfig config = VendorService.getServiceConfigCale(VendorService.PARKEON, TPApp.deviceId, "/");

            if (config != null) {

                Map<String, String> paramsMap = config.getParamsMap();
                final String user = paramsMap.get("User");
                final String password = paramsMap.get("Password");

                String serviceURL = config.getServiceURL();
                String[] split = serviceURL.split(";");

                final String SOAP_ACTION = split[1];
                final String METHOD_NAME = "get";
                final String NAMESPACE = split[2];
                final String URL = split[0];

                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
                Request.addProperty("type", "check_plate");
                Request.addProperty("condition", "plate_number=" + "'" + plate + "'" + "AND zone_id=" + "'" + zone_id + "'");


                final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(Request);
                final HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
                androidHttpTransport.debug = true;

                try {
                    final List<HeaderProperty> headerList = new ArrayList<>();
                    String authString = user + ":" + password;
                    headerList.add(new HeaderProperty("Authorization", "Basic " + org.kobjects.base64.Base64.encode(authString.getBytes())));
                    androidHttpTransport.call(SOAP_ACTION, envelope, headerList);
                    /*new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {*/
                    try {
                        response = (String) envelope.getResponse();
                        if (Feature.isFeatureAllowed(Feature.PARK_SEARCH_TRACK)) {
                            try {
                                MobileNowLog log = new MobileNowLog();
                                log.setCustId(TPApp.custId);
                                log.setDeviceId(TPApp.deviceId);
                                log.setUserId(TPApp.userId);
                                log.setRequestDate(new Date());
                                log.setPlate_number(plate);
                                log.setRequestParams("Parkeon Request: " + SOAP_ACTION + "/" + envelope + "/" + headerList);
                                log.setServiceMode(config.getRequestMode());
                                log.setResponseText(response);

                                MobileNowLog.insertMobileNowLog(log);
                                CSVUtility.writeMobileLogCSV(log);
                               /* DatabaseHelper.getInstance().openWritableDatabase();
                                DatabaseHelper.getInstance().insertOrReplace(log.getContentValues(), "mobile_now_logs");
                                DatabaseHelper.getInstance().closeWritableDb();*/

                                ArrayList<MobileNowLog> logs = new ArrayList<>();
                                logs.add(log);
                                WriteTicketNetworkCalls.sendMobileNogLogs(logs);
                                /*if (activity.screenBLProcessor instanceof WriteTicketBLProcessor)
                                    ((WriteTicketBLProcessor) activity.screenBLProcessor).sendMobileNowLog(logs);*/
                            } catch (Exception e) {
                                log.error("Error " + TPUtility.getPrintStackTrace(e));
                                e.printStackTrace();
                            }
                        }
                    } catch (SoapFault soapFault) {
                        soapFault.printStackTrace();
                    }
                   /*     }
                    },5000);*/
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            if (response != null) {
                SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
                SAXParser parser;
                try {
                    parser = saxParserFactory.newSAXParser();
                    try {
                        parser.parse(IOUtils.toInputStream(response), new DefaultHandler() {
                            final ParkeonZoneInfo parkeonZoneInfo = new ParkeonZoneInfo();
                            private String responseData;
                            private String noResponse = null;

                            @Override
                            public void characters(char[] ch, int start, int length) throws SAXException {
                                super.characters(ch, start, length);
                                responseData = new String(ch, start, length);
                            }

                            @Override
                            public void endElement(String uri, String localName, String qName) throws SAXException {
                                super.endElement(uri, localName, qName);
                                if (qName.equals("plate_number")) {
                                    parkeonZoneInfo.setPlate_number(responseData);
                                }
                                if (qName.equals("zone_id")) {
                                    parkeonZoneInfo.setZone_id(responseData);
                                }
                                if (qName.equals("start_date")) {
                                    parkeonZoneInfo.setStart_date(responseData);
                                }
                                if (qName.equals("end_date")) {
                                    parkeonZoneInfo.setEnd_date(responseData);
                                }
                                if (qName.equals("received_date")) {
                                    parkeonZoneInfo.setReceived_date(responseData);
                                }
                                if (qName.equals("response")) {
                                    noResponse = responseData;
                                }
                                if (qName.equals("row")) {
                                    if (noResponse != null) {
                                        processPlateLookupQueue(plate);
                                    } else {
                                        _displayMsgParkeonPlate(parkeonZoneInfo, plate);
                                    }
                                }
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (ParserConfigurationException | SAXException e) {
                    e.printStackTrace();
                }
            } else {
                processPlateLookupQueue(plate);
            }
        }
    }

    class HttpRequestTask extends AsyncTask<String, Void, String> {
        private final Bundle lookupData;
        private final int lookupType;
        private final String params;
        private final Logger log = Logger.getLogger("HttpRequestTask");
        private String requestMode;

        public HttpRequestTask(int lookupType, Bundle data, String params) {
            this.lookupType = lookupType;
            this.lookupData = data;
            this.params = params;
        }

        public void setRequestMode(String mode) {
            this.requestMode = mode;
        }

        public AsyncTask<String, Void, String> executeTask(String... params) {
            return executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
        }

        @Override
        protected String doInBackground(String... params) {
            String response;
            try {
                if (params.length >= 3) {
                    if (lookupType == LOOKUP_PARK_MOBILE_PLATE) {
                        response = TPUtility.getPMURLResponse(params[0], params[1], params[2]);
                    } else {
                        response = TPUtility.getURLResponse(params[0], params[1], params[2]);
                    }
                } else {
                    response = TPUtility.getURLResponse(params[0], true);
                }
            } catch (Exception e) {
                log.error(TPUtility.getPrintStackTrace(e));
                return e.getMessage();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            if (isCancelled()) {
                response = null;
            }
            if (Feature.isFeatureAllowed(Feature.PARK_SEARCH_TRACK)) {
                try {
                    MobileNowLog log = new MobileNowLog();
                    log.setCustId(TPApp.custId);
                    log.setDeviceId(TPApp.deviceId);
                    log.setUserId(TPApp.userId);
                    log.setRequestDate(new Date());
                    log.setPlate_number(plate);
                    log.setRequestParams(serviceName + " : " + this.params);
                    log.setServiceMode(this.requestMode);
                    log.setResponseText(response);
                    MobileNowLog.insertMobileNowLog(log);
                    CSVUtility.writeMobileLogCSV(log);
                    ArrayList<MobileNowLog> logs = new ArrayList<>();
                    logs.add(log);
                    WriteTicketNetworkCalls.sendMobileNogLogs(logs);
                } catch (Exception e) {
                    log.error(TPUtility.getPrintStackTrace(e));
                }
            }
            if (response != null) {
                if (response.contains("timed out") || response.contains("timeout") || response.contains("HTTP/1.1 403 Forbidden")) {
                    if (parkmobile) {
                        Builder dialog = new Builder(activity);
                        dialog.setCancelable(true);
                        dialog.setTitle("Unable to reach " + serviceName + " . Plate has not been validated.");
                        String finalResponse = response;
                        dialog.setPositiveButton("Continue", (dialog1, which) -> {
                            lookupData.putString(RESPONSE_DATA, finalResponse);
                            Message msg = obtainMessage();
                            msg.what = lookupType;
                            msg.setData(lookupData);
                            sendMessage(msg);
                        });
                        dialog.setNegativeButton("Cancel", (dialog12, i) -> dialog12.dismiss());
                        dialog.show();
                        return;
                    }
                }
            }

            this.lookupData.putString(RESPONSE_DATA, response);
            Message msg = obtainMessage();
            msg.what = this.lookupType;
            msg.setData(this.lookupData);
            sendMessage(msg);
        }
    }

    class TLSHttpRequestTask extends AsyncTask<String, Void, String> {
        private final Bundle lookupData;
        private final int lookupType;
        private final String params;
        private final Logger log = Logger.getLogger("TLSHttpRequestTask");
        private String requestMode;

        public TLSHttpRequestTask(int lookupType, Bundle data, String params) {
            this.lookupType = lookupType;
            this.lookupData = data;
            this.params = params;
        }

        public void setRequestMode(String mode) {
            this.requestMode = mode;
        }

        public AsyncTask<String, Void, String> executeTask(String... params) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                return executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
            } else {
                return execute(params);
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String response = null;

            try {
                response = TPUtility.getURLResponseWithTLS(params[0]);


            } catch (Exception e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }

            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            if (isCancelled()) {
                response = null;
            }
            if (Feature.isFeatureAllowed(Feature.PARK_SEARCH_TRACK)) {
                try {
                    MobileNowLog log = new MobileNowLog();
                    log.setCustId(TPApp.custId);
                    log.setDeviceId(TPApp.deviceId);
                    log.setUserId(TPApp.userId);
                    log.setRequestDate(new Date());
                    log.setPlate_number(plate);
                    log.setRequestParams(this.params);
                    log.setServiceMode(this.requestMode);
                    log.setResponseText(response);
                    MobileNowLog.insertMobileNowLog(log);
                    CSVUtility.writeMobileLogCSV(log);

                        /*DatabaseHelper.getInstance().openWritableDatabase();
                        DatabaseHelper.getInstance().insertOrReplace(log.getContentValues(), "mobile_now_logs");
                        DatabaseHelper.getInstance().closeWritableDb();*/

                    ArrayList<MobileNowLog> logs = new ArrayList<MobileNowLog>();
                    logs.add(log);
                    WriteTicketNetworkCalls.sendMobileNogLogs(logs);
                        /*if (activity.screenBLProcessor instanceof WriteTicketBLProcessor)
                            ((WriteTicketBLProcessor) activity.screenBLProcessor).sendMobileNowLog(logs);*/
                } catch (Exception e) {
                    log.error(TPUtility.getPrintStackTrace(e));
                }
            }

            this.lookupData.putString(RESPONSE_DATA, response);

            Message msg = obtainMessage();
            msg.what = this.lookupType;
            msg.setData(this.lookupData);

            sendMessage(msg);
        }
    }

    class RequestLogTask extends AsyncTask<String, Void, String> {
        private final String params;
        private final String response;
        private final String requestMode;
        private final Logger log = Logger.getLogger("RequestLogTask");

        public RequestLogTask(String params, String requestMode, String response) {
            this.requestMode = requestMode;
            this.response = response;
            this.params = params;
        }

        @Override
        protected String doInBackground(String... params) {

            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            if (Feature.isFeatureAllowed(Feature.PARK_SEARCH_TRACK)) {
                try {
                    MobileNowLog log = new MobileNowLog();
                    log.setCustId(TPApp.custId);
                    log.setDeviceId(TPApp.deviceId);
                    log.setUserId(TPApp.userId);
                    log.setPlate_number(plate);
                    log.setRequestDate(new Date());
                    log.setRequestParams(serviceName + " Request :" + this.params);
                    log.setServiceMode(this.requestMode);
                    log.setResponseText(this.response);
                    MobileNowLog.insertMobileNowLog(log);
                    CSVUtility.writeMobileLogCSV(log);
                    /*DatabaseHelper.getInstance().openWritableDatabase();
                    DatabaseHelper.getInstance().insertOrReplace(log.getContentValues(), "mobile_now_logs");
                    DatabaseHelper.getInstance().closeWritableDb();*/

                    ArrayList<MobileNowLog> logs = new ArrayList<>();
                    logs.add(log);
                    WriteTicketNetworkCalls.sendMobileNogLogs(logs);
                    /*if (activity.screenBLProcessor instanceof WriteTicketBLProcessor)
                        ((WriteTicketBLProcessor) activity.screenBLProcessor).sendMobileNowLog(logs);*/
                } catch (Exception e) {
                    log.error(TPUtility.getPrintStackTrace(e));
                }
            }
            if (isCancelled()) {
                response = null;
            }
        }
    }
}
