package com.ticketpro.parking.activity;

import java.util.ArrayList;
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
import android.widget.Toast;

import com.ticketpro.model.VendorItem;
import com.ticketpro.model.VendorService;
import com.ticketpro.parking.R;
import com.ticketpro.util.TPUtility;

public class ZoneListActivity extends BaseActivityImpl {
	private ProgressDialog progressDialog;
	private Handler dataLoadHandler;
	private Handler errorHandler;
	private ArrayList<VendorItem> zoneItems;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.zone_list);
			setLogger(ZoneListActivity.class.getName());

			dataLoadHandler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);

					ListView listView = (ListView) findViewById(R.id.zone_listview);
					listView.setScrollbarFadingEnabled(false);
					listView.setFastScrollEnabled(true);

					// create the grid item mapping
					String[] from = new String[] { "search_title" };
					int[] to = new int[] { R.id.search_textview };

					// prepare the list of all records
					List<HashMap<String, String>> fillMaps = new ArrayList<HashMap<String, String>>();
					for (int i = 0; i < zoneItems.size(); i++) {
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("search_title", zoneItems.get(i).getItemName());
						fillMaps.add(map);
					}

					// fill in the grid_item layout
					SimpleAdapter adapter = new SimpleAdapter(ZoneListActivity.this, fillMaps, R.layout.search_list_item, from, to);
					listView.setAdapter(adapter);
					listView.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> adapterView, View view, int pos, long arg3) {
							Intent intent = new Intent();
							intent.setClass(ZoneListActivity.this, ZoneLogActivity.class);
							intent.putExtra("ZoneName", zoneItems.get(pos).getItemName());
							intent.putExtra("ZoneCode", zoneItems.get(pos).getItemCode());
							startActivity(intent);
						}
					});

					if (progressDialog.isShowing()){
						progressDialog.dismiss();
					}
				}
			};

			errorHandler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					if (progressDialog.isShowing()){
						progressDialog.dismiss();
					}

					Toast.makeText(ZoneListActivity.this, 
							"Error loading zones information.", Toast.LENGTH_SHORT).show();
				}
			};

			bindDataAtLoadingTime();
		
		} catch (Exception e) {
			log.error(TPUtility.getPrintStackTrace(e));
		}
	}

	public void bindDataAtLoadingTime() {
		progressDialog = ProgressDialog.show(this, "", "Loading...");
		new Thread() {
			public void run() {
				try {
					VendorService service = VendorService.getServiceByName(VendorService.MOBILE_NOW_ZONE_CHECK);
					zoneItems = VendorItem.getVendorZones(service.getVendorId());
					dataLoadHandler.sendEmptyMessage(1);
				} catch (Exception ae) {
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

	public void backAction(View view) {
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
	public void handleNetworkStatus(boolean connected, boolean isFastConnection) {
		// TODO Auto-generated method stub
	}
}
