package com.ticketpro.vendors.dpt;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

import com.ticketpro.model.VendorService;
import com.ticketpro.model.VendorServiceConfig;
import com.ticketpro.parking.R;
import com.ticketpro.parking.activity.BaseActivityImpl;
import com.ticketpro.parking.activity.TPApplication;
import com.ticketpro.parking.bl.CommonBLProcessor;
import com.ticketpro.parking.service.RequestLogTask;
import com.ticketpro.util.StringUtil;
import com.ticketpro.util.TPUtility;
import com.ticketpro.vendors.dpt.PlateInfoService.IWsdl2CodeEvents;
import com.ticketpro.vendors.dpt.PlateInfoService.PlateInfoService;
import com.ticketpro.vendors.dpt.PlateInfoService.RegionRequest;
import com.ticketpro.vendors.dpt.PlateInfoService.RegionType;
import com.ticketpro.vendors.dpt.PlateInfoService.VectorRegionType;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class DigitalPaytechZonesActivity extends BaseActivityImpl {

	private ProgressDialog progressDialog;
	private Handler dataLoadHandler;
	private Handler errorHandler;
	private EditText searchEditText;
	private ListView listView;

	private ArrayList<RegionType> zoneItems;
	private ArrayList<RegionType> filteredItems= new ArrayList<RegionType>();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try{
			setContentView(R.layout.zone_list);
			setLogger(DigitalPaytechZonesActivity.class.getName());
			setBLProcessor(new CommonBLProcessor((TPApplication)getApplicationContext()));
	        
			((TextView)findViewById(R.id.zone_info_text)).setText("Regions");
			
			listView = (ListView) findViewById(R.id.zone_listview);
			listView.setScrollbarFadingEnabled(false);
			listView.setFastScrollEnabled(true);
			listView.setOnItemClickListener(new OnItemClickListener(){
				@Override
				public void onItemClick(AdapterView<?> adapterView, View view, int pos, long arg3) {
					Intent intent = new Intent();
					intent.setClass(DigitalPaytechZonesActivity.this, DigitalPaytechZoneInfoActivity.class);
					intent.putExtra("RegionName", filteredItems.get(pos).regionName);
					startActivity(intent);
				}
			});
			
			searchEditText = (EditText)findViewById(R.id.searchText);
			searchEditText.addTextChangedListener(new TextWatcher() {
				public void afterTextChanged(Editable s) {}
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
				
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					String searchText = searchEditText.getText().toString();
					String zoneName;
					
					filteredItems.clear();

					if(searchText.length()==0){
						filteredItems.addAll(zoneItems);
					}else{
						searchText = searchText.toLowerCase();
						for(RegionType zoneInfo: zoneItems){
							zoneName = zoneInfo.regionName;
							if(!StringUtil.isEmpty(zoneName)){
								if(!TPApp.getUserSettings().isSearchContains() && zoneName.toLowerCase().startsWith(searchText)){
									filteredItems.add(zoneInfo);
								}
								else if(zoneName.toLowerCase().contains(searchText)){
									filteredItems.add(zoneInfo);
								}
							}
						}
					}
					
					displayItems(filteredItems);
				}
			});
			
			
			dataLoadHandler=new Handler() {
				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					displayItems(zoneItems);
				}
			};

			errorHandler=new Handler() {
				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					if(progressDialog.isShowing()){
						progressDialog.dismiss();
					}
					
					Toast.makeText(DigitalPaytechZonesActivity.this, "Error loading zones information.", Toast.LENGTH_SHORT).show();
				}
			};	

			bindDataAtLoadingTime();
		}catch(Exception e){
			log.error(TPUtility.getPrintStackTrace(e)); 
		}
	} 
	
	
	private void displayItems(ArrayList<RegionType> zoneItems){
		// create the grid item mapping
		String[] from = new String[] {"search_title"};
		int[] to = new int[] { R.id.search_textview};

		// prepare the list of all records
		List<HashMap<String, String>> fillMaps = new ArrayList<HashMap<String, String>>();
		for(int i = 0; i < zoneItems.size(); i++){
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("search_title", zoneItems.get(i).regionName);
			fillMaps.add(map);
		}

		filteredItems = (ArrayList<RegionType>) zoneItems.clone();
		
		// fill in the grid_item layout
		SimpleAdapter adapter = new SimpleAdapter(this, fillMaps, R.layout.search_list_item, from, to);
		listView.setAdapter(adapter);
	}

	public void bindDataAtLoadingTime(){
		progressDialog = ProgressDialog.show(this, "", "Loading...");
		new Thread(){
			public void run() {
				try{
					VendorServiceConfig config=VendorService.getServiceConfigT2(VendorService.DIGITALPAYTECH_ZONELIST, TPApp.deviceId);
					if(config!=null){
						zoneItems=new ArrayList<RegionType>();
						PlateInfoService service=new PlateInfoService(new IWsdl2CodeEvents() {
							@Override
							public void Wsdl2CodeStartedRequest() {
							
							}
							
							@Override
							public void Wsdl2CodeFinishedWithException(Exception ex) {
								log.error("PlateInfoService Error "+ TPUtility.getPrintStackTrace(ex));
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
						
						RegionRequest request=new RegionRequest();
						request.token=config.getParamsMap().get("token");
						
						VectorRegionType vectorRegions=service.getRegions(request);
						Enumeration<RegionType> enumRegions=vectorRegions.elements();
						while(enumRegions.hasMoreElements()){
							RegionType type=(RegionType)enumRegions.nextElement();
							zoneItems.add(type);
						}
						
						String responseData=vectorRegions!=null ? StringUtil.escapeQuotes(vectorRegions.toString()) : "";
				    	RequestLogTask task=new RequestLogTask(config.getParams(), config.getRequestMode(), responseData);
				    	task.execute();
				    	
				    	progressDialog.dismiss();
						dataLoadHandler.sendEmptyMessage(1);
					}
					else{
						errorHandler.sendEmptyMessage(0);
					}
				}catch(Exception e){
					log.error(TPUtility.getPrintStackTrace(e));
					errorHandler.sendEmptyMessage(0);
				}
			}
		}.start();
	}	


	@Override
	public void onBackPressed() {
		backAction(null);
	}
	
	public void backAction(View view){
		finish();
	}
	
	@Override
	public void onClick(View v) {
	
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if (keyCode == KeyEvent.KEYCODE_BACK) {
    		backAction(null);
            return false;
        }
    	
        return false;
	}

	@Override
	public void handleVoiceInput(String text) {
		
	}

	@Override
	public void handleVoiceMode(boolean voiceMode) {
	
	}

	@Override
	public void handleNetworkStatus(boolean connected,boolean isFastConnection) {
		// TODO Auto-generated method stub
	}
}
