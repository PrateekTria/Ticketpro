package com.ticketpro.vendors.dpt;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.ticketpro.model.VendorService;
import com.ticketpro.model.VendorServiceConfig;
import com.ticketpro.parking.R;
import com.ticketpro.parking.activity.BaseActivityImpl;
import com.ticketpro.util.TPUtility;
import com.ticketpro.vendors.dpt.PaystationInfoService.IWsdl2CodeEvents;
import com.ticketpro.vendors.dpt.PaystationInfoService.PaystationInfoService;
import com.ticketpro.vendors.dpt.PaystationInfoService.PaystationRequest;
import com.ticketpro.vendors.dpt.PaystationInfoService.PaystationType;
import com.ticketpro.vendors.dpt.PaystationInfoService.VectorPaystationType;

public class DigitalPaytechPaystationsActivity extends BaseActivityImpl {

	private ProgressDialog progressDialog;
	private Handler dataLoadHandler;
	private Handler errorHandler;
	private ArrayList<PaystationType> paystationItems;
	private String regionName;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try{
			setContentView(R.layout.zone_list);
			setLogger(DigitalPaytechPaystationsActivity.class.getName());
			
			regionName=getIntent().getStringExtra("RegionName");
			if(regionName!=null){
				((TextView)findViewById(R.id.zone_info_text)).setText("PayStations - " + regionName);
			}
			
			dataLoadHandler=new Handler() {
				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					
					ListView listView = (ListView) findViewById(R.id.zone_listview);
					listView.setScrollbarFadingEnabled(false);
					listView.setFastScrollEnabled(true);
					
					// create the grid item mapping
					String[] from = new String[] {"search_title"};
					int[] to = new int[] { R.id.search_textview};

					// prepare the list of all records
					List<HashMap<String, String>> fillMaps = new ArrayList<HashMap<String, String>>();
					for(int i = 0; i < paystationItems.size(); i++){
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("search_title", paystationItems.get(i).paystationName);
						fillMaps.add(map);
					}

					// fill in the grid_item layout
					SimpleAdapter adapter = new SimpleAdapter(DigitalPaytechPaystationsActivity.this, fillMaps, R.layout.search_list_item, from, to);
					listView.setAdapter(adapter);
					listView.setOnItemClickListener(new OnItemClickListener(){
						@Override
						public void onItemClick(AdapterView<?> adapterView, View view, int pos, long arg3) {
							Intent intent = new Intent();
							intent.setClass(DigitalPaytechPaystationsActivity.this, DigitalPaytechZoneInfoActivity.class);
							intent.putExtra("RegionName", paystationItems.get(pos).regionName);
							startActivity(intent);
						}
					});
					
					if(progressDialog.isShowing())
						progressDialog.dismiss();
				}
			};

			errorHandler=new Handler() {
				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					if(progressDialog.isShowing()){
						progressDialog.dismiss();
					}
					
					Toast.makeText(DigitalPaytechPaystationsActivity.this, "Error loading zones information.", Toast.LENGTH_SHORT).show();
				}
			};	

			bindDataAtLoadingTime();
		
		}catch(Exception e){
			log.error(TPUtility.getPrintStackTrace(e)); 
		}
	}    

	public void bindDataAtLoadingTime(){
		progressDialog = ProgressDialog.show(this, "", "Loading...");
		new Thread(){
			public void run() {
				try{
					VendorServiceConfig config=VendorService.getServiceConfigT2(VendorService.DIGITALPAYTECH_PAYSTATIONLIST, TPApp.deviceId);
					if(config!=null){
						paystationItems=new ArrayList<PaystationType>();
						PaystationInfoService service=new PaystationInfoService(new IWsdl2CodeEvents() {
							@Override
							public void Wsdl2CodeStartedRequest() {
							
							}
							
							@Override
							public void Wsdl2CodeFinishedWithException(Exception ex) {
								log.error("PaystationInfoService Error "+ TPUtility.getPrintStackTrace(ex));
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
						
						PaystationRequest request=new PaystationRequest();
						request.token=config.getParamsMap().get("token");
						
						VectorPaystationType vectorPaystations=service.getPaystations(request);
						Enumeration<PaystationType> enumRegions=vectorPaystations.elements();
						while(enumRegions.hasMoreElements()){
							PaystationType type=(PaystationType)enumRegions.nextElement();
							if(regionName.equalsIgnoreCase(type.regionName)){
								paystationItems.add(type);
							}
						}
						
						progressDialog.dismiss();
						dataLoadHandler.sendEmptyMessage(1);
					}
					else{
						errorHandler.sendEmptyMessage(0);
					}
				}catch(Exception ae){
					log.error(ae.getMessage());
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
