package com.ticketpro.vendors;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.ticketpro.api.ApiRequest;
import com.ticketpro.api.ServiceGeneratorCubTrac;
import com.ticketpro.model.Feature;
import com.ticketpro.model.VendorService;
import com.ticketpro.model.VendorServiceConfig;
import com.ticketpro.parking.R;
import com.ticketpro.parking.activity.BaseActivityImpl;
import com.ticketpro.util.Preference;
import com.ticketpro.util.StringUtil;
import com.ticketpro.util.TPConstant;
import com.ticketpro.vendors.cubtrack.cbt_model.CubTracZone;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;

public class CurbTracPlateList extends BaseActivityImpl {

    private ProgressDialog progressDialog;
    private ListView listView;
    private EditText searchEditText;
    private ArrayList<CubTracZone> zoneItems;
    private ArrayList<CubTracZone> filteredItems = new ArrayList<>();
    Preference preference;
    String plateServiceerviceURL;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_curbtrack_zone_list);


        setContentView(R.layout.zone_list);
        preference = Preference.getInstance(this);
        listView = (ListView) findViewById(R.id.zone_listview);
        listView.setScrollbarFadingEnabled(false);
        listView.setFastScrollEnabled(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long arg3) {
                Intent intent = new Intent();
                intent.setClass(CurbTracPlateList.this, CaleZoneInfoActivity.class);
                intent.putExtra("ZoneName", filteredItems.get(pos).getName());
                intent.putExtra("ZoneCode", "");
                intent.putExtra("LocationCode", "");
                intent.putExtra("ZONE_ID", filteredItems.get(pos).getAssignedId());
                startActivity(intent);
                preference.putString("ZONE_ID", filteredItems.get(pos).getAssignedId());
//                finish();


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

                if (zoneItems==null)
                    return;

                filteredItems.clear();

                if (searchText.length() == 0) {
                    filteredItems.addAll(zoneItems);
                } else {
                    searchText = searchText.toLowerCase();
                    if (zoneItems!=null && zoneItems.size()>0) {
                        for (CubTracZone zoneInfo : zoneItems) {
                            zoneName = zoneInfo.getName();
                            if (!StringUtil.isEmpty(zoneName)) {
                                if (!TPApp.getUserSettings().isSearchContains() && zoneName.toLowerCase().startsWith(searchText)) {
                                    filteredItems.add(zoneInfo);
                                } else if (zoneName.toLowerCase().contains(searchText)) {
                                    filteredItems.add(zoneInfo);
                                }
                            }

                            zoneName = zoneInfo.getName();
                            if (!StringUtil.isEmpty(zoneName)) {
                                if (!TPApp.getUserSettings().isSearchContains() && zoneName.toLowerCase().startsWith(searchText)) {
                                    filteredItems.add(zoneInfo);
                                } else if (zoneName.toLowerCase().contains(searchText)) {
                                    filteredItems.add(zoneInfo);
                                }
                            }
                        }
                    }
                }

                displayItems(filteredItems);
            }
        });
        try {
            __openDialogForZoneList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void displayItems(ArrayList<CubTracZone> zoneItems) {
        // create the grid item mapping
        String[] from = new String[]{"search_title"};
        int[] to = new int[]{R.id.search_textview};

        // prepare the list of all records
        List<HashMap<String, String>> fillMaps = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < zoneItems.size(); i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("search_title", "" +  zoneItems.get(i).getName()+" ("+zoneItems.get(i).getAssignedId()+")  ");//zoneItems.get(i).getDescription()
            fillMaps.add(map);
        }

        filteredItems = (ArrayList<CubTracZone>) zoneItems.clone();

        // fill in the grid_item layout
        SimpleAdapter adapter = new SimpleAdapter(this, fillMaps, R.layout.search_list_item, from, to);
        listView.setAdapter(adapter);
    }


    private void __openDialogForZoneList() throws IOException {

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Looking Curbtrac zone");
        progressDialog.setCancelable(false);
        progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        progressDialog.show();

        if (isNetworkConnected()) {
            if (!(TPApp.enableCubtrac && Feature.isFeatureAllowed(Feature.PARK_CUBTRAC))) {
                return;
            }

            final VendorServiceConfig config = VendorService.getServiceConfig(VendorService.CUBTRAC_ZONE_SEARCH, TPApp.deviceId, "/");
            Map<String, String> paramsMap = config.getParamsMap();
            String user = paramsMap.get("User");
            String password = paramsMap.get("Password");
            String serviceURL = config.getServiceURL();
            String token = paramsMap.get("token");

            VendorService vendorService = VendorService.getServiceByName(VendorService.CURBSENSE_ZONE);
            if (TPConstant.IS_DEVELOPMENT_BUILD) {
                if (vendorService.getTestURL() != null && !vendorService.getTestURL().isEmpty()) {
                    plateServiceerviceURL = vendorService.getTestURL();
                }
            } else {
                if (vendorService.getProdURL() != null && !vendorService.getProdURL().isEmpty()) {
                    plateServiceerviceURL = vendorService.getProdURL();
                }
            }

            ApiRequest service = ServiceGeneratorCubTrac.createService(ApiRequest.class);
            service.cubtracGetZone(plateServiceerviceURL,token).enqueue(new retrofit2.Callback<List<CubTracZone>>() {
                @Override
                public void onResponse(Call<List<CubTracZone>> call, Response<List<CubTracZone>> response) {
                    progressDialog.dismiss();
                    if (response.isSuccessful()&& response.code()==200) {

                        List<CubTracZone> zoneList = response.body();
                        if (zoneList!=null && zoneList.size()>0){
                            CubTracZone.sortZoneListByName(zoneList);

                            zoneItems = (ArrayList<CubTracZone>) zoneList;
                            displayItems(zoneItems);

                        }
                    }
                }

                @Override
                public void onFailure(Call<List<CubTracZone>> call, Throwable t) {
                    progressDialog.dismiss();

                }
            });

        }else {
            progressDialog.dismiss();
        }

    }
    @Override
    public void onClick(View v) {

    }

    @Override
    public void bindDataAtLoadingTime() throws Exception {

    }

    @Override
    public void handleVoiceInput(String text) throws Exception {

    }

    @Override
    public void handleVoiceMode(boolean voiceMode) {

    }

    @Override
    public void handleNetworkStatus(boolean connected, boolean isFastConnection) {

    }
   /* public static class ItemAdapter extends ArrayAdapter<CubTracZone> {
        private Context context;
        private List<CubTracZone> items;

        public ItemAdapter(Context context, List<CubTracZone> items) {
            super(context, 0, items);
            this.context = context;
            this.items = items;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_2, parent, false);
            }

            CubTracZone item = items.get(position);

            TextView nameTextView = convertView.findViewById(android.R.id.text1);
            TextView descriptionTextView = convertView.findViewById(android.R.id.text2);

            nameTextView.setText(item.getName());
            nameTextView.setTextColor(R.color.black);
            descriptionTextView.setText(item.getAssignedId());
            descriptionTextView.setTextColor(R.color.black);


            return convertView;
        }
    }
*/
}