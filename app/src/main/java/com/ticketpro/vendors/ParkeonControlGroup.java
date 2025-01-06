package com.ticketpro.vendors;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.RequiresApi;

import com.ticketpro.model.Feature;
import com.ticketpro.model.ParkeonLot;
import com.ticketpro.model.VendorService;
import com.ticketpro.model.VendorServiceConfig;
import com.ticketpro.parking.R;
import com.ticketpro.parking.activity.BaseActivityImpl;
import com.ticketpro.parking.activity.TPApplication;

import org.apache.commons.io.IOUtils;
import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class ParkeonControlGroup extends BaseActivityImpl {

    private ListView mListView;
    ArrayList<ParkeonLot> lotArrayList1;
    private String from = "";


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parkeon_control_group);
        mListView = (ListView) findViewById(R.id.cont_listview);
        from = getIntent().getStringExtra("from");
        try {
            if (isNetworkConnected()) {
                try {
                    new ParkeonGet().execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (lotArrayList1 != null) {
                    try {
                        Intent intent = new Intent();
                        intent.setClass(ParkeonControlGroup.this, ParkeonZoneActivity.class);
                        intent.putExtra("control_id", lotArrayList1.get(position).getControl_group_id());
                        intent.putExtra("from", from);
                        startActivity(intent);
                        if (from!=null) {
                            finish();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private class ParkeonGet extends AsyncTask<Void, Void, Void> {
        private String res;
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ParkeonControlGroup.this);
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
            final String SOAP_ACTION = split[1];
            final String METHOD_NAME = "get";
            final String NAMESPACE = split[2];
            final String URL = split[0];
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("type", "control_groups");
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
                res = (String) envelope.getResponse();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            parseXML();
        }

        private void parseXML() {
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            SAXParser parser;
            final ArrayList<ParkeonLot> lotArrayList = new ArrayList<>();

            try {
                parser = saxParserFactory.newSAXParser();
                try {
                    parser.parse(IOUtils.toInputStream(res), new DefaultHandler() {
                        private String s;
                        ParkeonLot lot = new ParkeonLot();

                        @Override
                        public void characters(char[] ch, int start, int length) throws SAXException {
                            super.characters(ch, start, length);
                            s = new String(ch, start, length);
                        }

                        @Override
                        public void endElement(String uri, String localName, String qName) throws SAXException {
                            super.endElement(uri, localName, qName);
                            System.out.println(s);
                            if (qName.equals("control_group_id")) {
                                lot.setControl_group_id(s);
                            }
                            if (qName.equals("name")) {
                                lot.setName(s);
                            }
                            if (qName.equals("description")) {
                                lot.setDesc(s);
                            }
                            if (qName.equals("row")) {

                                lotArrayList.add(lot);
                                lot = new ParkeonLot();
                                /*if (_controlGroupId!=null){
                                    new ParkeonZoneService(_controlGroupId).execute();
                                }*/
                            }
                        }
                    });

                    progressDialog.dismiss();
                    if (lotArrayList.size() > 0) {
                        displayLot(lotArrayList);
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

    private void displayLot(ArrayList<ParkeonLot> lotArrayList) {
        lotArrayList1 = lotArrayList;
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ParkeonControlGroup.this, R.layout.simple);
        for (int i = 0; i < lotArrayList.size(); i++) {
            arrayAdapter.add(lotArrayList.get(i).getName());
        }
        mListView.setAdapter(arrayAdapter);
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
}
