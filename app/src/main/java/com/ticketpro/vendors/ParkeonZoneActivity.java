package com.ticketpro.vendors;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.RequiresApi;

import com.ticketpro.model.Feature;
import com.ticketpro.model.ParkeonZone;
import com.ticketpro.model.VendorService;
import com.ticketpro.model.VendorServiceConfig;
import com.ticketpro.parking.R;
import com.ticketpro.parking.activity.BaseActivityImpl;
import com.ticketpro.parking.activity.TPApplication;
import com.ticketpro.util.Preference;
import com.ticketpro.util.StringUtil;
import com.ticketpro.util.TPConstant;

import org.apache.commons.io.IOUtils;
import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class ParkeonZoneActivity extends BaseActivityImpl {

    private ProgressDialog progressDialog;
    private ListView listView;
    private EditText searchEditText;
    private ArrayList<ParkeonZone> zoneItems;
    private ArrayList<ParkeonZone> filteredItems = new ArrayList<>();
    private Preference preference;
    ArrayList<ParkeonZone> zoneArrayList;
    private String from = "";

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zone_list);
        preference = Preference.getInstance(this);
        from = getIntent().getStringExtra("from");
        listView = (ListView) findViewById(R.id.zone_listview);
        listView.setScrollbarFadingEnabled(false);
        listView.setFastScrollEnabled(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long arg3) {
                try {
                    if (from == null || from.isEmpty()) {
                        Intent intent = new Intent();
                        intent.setClass(ParkeonZoneActivity.this, ParkeonZoneActivityInfo.class);
                        intent.putExtra("ZoneName", filteredItems.get(pos).getZone_name());
                        intent.putExtra("ZoneCode", filteredItems.get(pos).getZone_id());
                        intent.putExtra("LocationCode", "");
                        preference.putString(TPConstant.PARKEON_ZONE_ID, filteredItems.get(pos).getZone_id());
                        preference.putString(TPConstant.PARKEON_ZONE_NAME, filteredItems.get(pos).getZone_name());
                        startActivity(intent);
                    } else {
                        preference.putString(TPConstant.PARKEON_ZONE_ID, filteredItems.get(pos).getZone_id());
                        preference.putString(TPConstant.PARKEON_ZONE_NAME, filteredItems.get(pos).getZone_name());
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        searchEditText = (EditText) findViewById(R.id.searchText);
        searchEditText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String searchText = searchEditText.getText().toString();
                String zoneName;

                filteredItems.clear();

                if (searchText.length() == 0) {
                    filteredItems.addAll(zoneItems);
                } else {
                    searchText = searchText.toLowerCase();
                    for (ParkeonZone zoneInfo : zoneItems) {
                        zoneName = zoneInfo.getZone_description();
                        if (!StringUtil.isEmpty(zoneName)) {
                            if (!TPApp.getUserSettings().isSearchContains() && zoneName.toLowerCase().startsWith(searchText)) {
                                filteredItems.add(zoneInfo);
                            } else if (zoneName.toLowerCase().contains(searchText)) {
                                filteredItems.add(zoneInfo);
                            }
                        }

                        zoneName = zoneInfo.getZone_name();
                        if (!StringUtil.isEmpty(zoneName)) {
                            if (!TPApp.getUserSettings().isSearchContains() && zoneName.toLowerCase().startsWith(searchText)) {
                                filteredItems.add(zoneInfo);
                            } else if (zoneName.toLowerCase().contains(searchText)) {
                                filteredItems.add(zoneInfo);
                            }
                        }
                    }
                }

                displayItems(filteredItems);
            }
        });
        String controlGroupId = getIntent().getStringExtra("control_id");

        //Call parkeon service for getting zone list
        if (Feature.isFeatureAllowed(Feature.TOGGLE_NET_ON_OFF) && TPApplication.getInstance().getNetOnOff().equals("Y")){
            return;
        }
        try {
            if (isNetworkConnected() && controlGroupId != null) {
                try {
                    new ParkeonZoneService(controlGroupId).execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Display all response item in list view
     *
     * @param zoneItems
     */
    private void displayItems(ArrayList<ParkeonZone> zoneItems) {
        // create the grid item mapping
        String[] from = new String[]{"search_title"};
        int[] to = new int[]{R.id.search_textview};

        // prepare the list of all records
        List<HashMap<String, String>> fillMaps = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < zoneItems.size(); i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("search_title", "" + zoneItems.get(i).getZone_name());
            fillMaps.add(map);
        }

        filteredItems = (ArrayList<ParkeonZone>) zoneItems.clone();

        // fill in the grid_item layout
        SimpleAdapter adapter = new SimpleAdapter(ParkeonZoneActivity.this, fillMaps, R.layout.search_list_item, from, to);
        listView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void bindDataAtLoadingTime() throws Exception {

    }

    @Override
    public void handleVoiceInput(String text) {

    }

    @Override
    public void handleVoiceMode(boolean voiceMode) {

    }

    @Override
    public void handleNetworkStatus(boolean connected, boolean isFastConnection) {

    }


   /* private void displayLot(ArrayList<ParkeonLot> lotArrayList) {

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(ParkeonZoneActivity.this);
        builderSingle.setTitle("SELECT LOT");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ParkeonZoneActivity.this, R.layout.simple);
        for (int i = 0; i <lotArrayList.size() ; i++) {
            arrayAdapter.add(lotArrayList.get(i).getName());
        }

        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                ParkeonZoneActivity.this.finish();
            }
        });

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    String control_group_id = lotArrayList.get(which).getControl_group_id();
                    System.out.println("Control group id is: " + control_group_id);
                    if (control_group_id != null) {
                        new ParkeonZoneService(control_group_id).execute();
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        });
        builderSingle.show();
    }*/

    /**
     *
     */
    private class ParkeonZoneService extends AsyncTask<Void, Void, Void> {

        private String response;

        String controlGroupId;

        public ParkeonZoneService(String controlGroupId) {

            this.controlGroupId = controlGroupId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(ParkeonZoneActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
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
            VendorServiceConfig config = VendorService.getServiceConfigCale(VendorService.PARKEON, TPApp.deviceId, "/");
            Map<String, String> paramsMap = config.getParamsMap();
            String user = paramsMap.get("User");
            String password = paramsMap.get("Password");

            String serviceURL = config.getServiceURL();
            String[] split = serviceURL.split(";");

            final String SOAP_ACTION = split[1];//"http://www.prm.parkeonsmartcenter.com:90/pbp_enforcement_api/#get";
            final String METHOD_NAME = "get";
            final String NAMESPACE = split[2];//"http://www.parkeonsmartcenter.com:90";
            final String URL = split[0];//"https://www.prm.parkeonsmartcenter.com:4443/pbp_enforcement_api/index.php";


            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("type", "control_areas");
            Request.addProperty("condition", "control_group_id=" + controlGroupId);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(Request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.debug = true;

            try {
                List<HeaderProperty> headerList = new ArrayList<>();

                String authString = user + ":" + password;
                headerList.add(new HeaderProperty("Authorization", "Basic " + org.kobjects.base64.Base64.encode(authString.getBytes())));

                androidHttpTransport.call(SOAP_ACTION, envelope, headerList);
                response = (String) envelope.getResponse();


            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (response != null) {
                parseZoneXML();
            }
        }

        private void parseZoneXML() {

            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            SAXParser parser = null;
            try {
                parser = saxParserFactory.newSAXParser();
                try {
                    parser.parse(IOUtils.toInputStream(response), new DefaultHandler() {
                        ParkeonZone parkeonZone;
                        private StringBuilder data = null;
                        protected boolean zone_id;
                        protected boolean zone_name;
                        protected boolean zone_description;

                        @Override
                        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                            super.startElement(uri, localName, qName, attributes);

                            if (qName.equalsIgnoreCase("row")) {
                                parkeonZone = new ParkeonZone();
                                if (zoneArrayList == null)
                                    zoneArrayList = new ArrayList<>();
                            } else if (qName.equalsIgnoreCase("zone_id")) {
                                zone_id = true;
                            } else if (qName.equalsIgnoreCase("zone_name")) {
                                zone_name = true;
                            } else if (qName.equalsIgnoreCase("zone_description")) {
                                zone_description = true;
                            }

                            data = new StringBuilder();

                        }

                        @Override
                        public void characters(char[] ch, int start, int length) throws SAXException {
                            super.characters(ch, start, length);
                            data.append(new String(ch, start, length));

                        }

                        @Override
                        public void endElement(String uri, String localName, String qName) throws SAXException {
                            super.endElement(uri, localName, qName);

                            if (zone_id) {
                                parkeonZone.setZone_id(data.toString());
                                zone_id = false;
                            } else if (zone_name) {
                                parkeonZone.setZone_name(data.toString());
                                zone_name = false;
                            } else if (zone_description) {
                                parkeonZone.setZone_description(data.toString());
                                zone_description = false;
                            }

                            if (qName.equals("row")) {
                                zoneArrayList.add(parkeonZone);

                            }
                        }
                    });
                    progressDialog.dismiss();
                    if (zoneArrayList.size() > 0) {
                        zoneItems = new ArrayList<>();
                        zoneItems.addAll(zoneArrayList);
                        if (zoneItems.size()==1){
                            try {
                                if (from == null || from.isEmpty()) {
                                    Intent intent = new Intent();
                                    intent.setClass(ParkeonZoneActivity.this, ParkeonZoneActivityInfo.class);
                                    intent.putExtra("ZoneName", zoneItems.get(0).getZone_name());
                                    intent.putExtra("ZoneCode", zoneItems.get(0).getZone_id());
                                    intent.putExtra("LocationCode", "");
                                    preference.putString(TPConstant.PARKEON_ZONE_ID, zoneItems.get(0).getZone_id());
                                    preference.putString(TPConstant.PARKEON_ZONE_NAME, zoneItems.get(0).getZone_name());
                                    startActivity(intent);
                                    finish();
                                } else {
                                    preference.putString(TPConstant.PARKEON_ZONE_ID, zoneItems.get(0).getZone_id());
                                    preference.putString(TPConstant.PARKEON_ZONE_NAME, zoneItems.get(0).getZone_name());
                                    finish();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }else {
                            displayItems(zoneItems);
                        }
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }


            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }

        }
    }


}
