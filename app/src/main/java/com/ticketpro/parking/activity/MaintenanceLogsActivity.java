package com.ticketpro.parking.activity;

import java.util.ArrayList;
import java.util.Collections;

import com.ticketpro.exception.TPException;
import com.ticketpro.model.MaintenanceLog;
import com.ticketpro.parking.R;
import com.ticketpro.parking.bl.CommonBLProcessor;
import com.ticketpro.util.DateUtil;
import com.ticketpro.util.TPUtility;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Home screen (Ref. Mockups).
 * 
 * @author : TicketPRO
 * @version : 1.0
 * @since : 1.0
 */

public class MaintenanceLogsActivity extends BaseActivityImpl {

	private TableLayout tableLayout;
	private ArrayList<MaintenanceLog> logs;
	private ProgressDialog progressDialog;
	private Handler dataLoadingHandler;
	private Handler errorHandler;
	
	private final int ASC_ORDER = 1;
	private final int DESC_ORDER = 2;
	private int sortBy = 0;
	private int sortOrder = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.maintenance_logs);
			setActiveScreen(this);

			setLogger(MaintenanceLogsActivity.class.getName());
			setBLProcessor(new CommonBLProcessor((TPApplication) getApplicationContext()));
			tableLayout = (TableLayout) findViewById(R.id.logs_3_table_view);
		
			dataLoadingHandler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
	
					initDatagrid();

					if (progressDialog.isShowing()) {
						progressDialog.dismiss();
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

			bindDataAtLoadingTime();

		} catch (Exception e) {
			log.error(TPUtility.getPrintStackTrace(e));
		}
	}

	private void initDatagrid() {
		try {
			if (logs == null) {
				return;
			}

			tableLayout.removeAllViews();
			LayoutInflater inflater = LayoutInflater.from(this);

			// adding Header
			View headerRow = inflater.inflate(R.layout.table_row_3_coulm_view, null);

			TextView itemColumn = ((TextView) headerRow.findViewById(R.id.tr_header1));
			itemColumn.setText("ItemName");
			itemColumn.setClickable(true);
			itemColumn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					Collections.sort(logs, new MaintenanceLog.ItemComparator());

					// Update Sorting Order
					if (sortBy != 1) {
						sortOrder = ASC_ORDER;
					} 
					else if (sortOrder == ASC_ORDER) {
						sortOrder = DESC_ORDER;
						Collections.reverse(logs);
					} else {
						sortOrder = ASC_ORDER;
					}

					sortBy = 1;
					initDatagrid();
				}
			});

			TextView typeColumn = ((TextView) headerRow.findViewById(R.id.tr_header2));
			typeColumn.setText("Type");
			typeColumn.setClickable(true);
			typeColumn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					Collections.sort(logs, new MaintenanceLog.TypeComparator());

					// Update Sorting Order
					if (sortBy != 2) {
						sortOrder = ASC_ORDER;
					} else if (sortOrder == ASC_ORDER) {
						sortOrder = DESC_ORDER;
						Collections.reverse(logs);
					} else {
						sortOrder = ASC_ORDER;
					}

					sortBy = 2;
					initDatagrid();
				}
			});

			TextView dateColumn = ((TextView) headerRow.findViewById(R.id.tr_header3));
			dateColumn.setText("Date");
			dateColumn.setClickable(true);
			dateColumn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					Collections.sort(logs, new MaintenanceLog.DateComparator());

					// Update Sorting Order
					if (sortBy != 3) {
						sortOrder = ASC_ORDER;
					} else if (sortOrder == ASC_ORDER) {
						sortOrder = DESC_ORDER;
						Collections.reverse(logs);
					} else {
						sortOrder = ASC_ORDER;
					}

					sortBy = 3;
					initDatagrid();
				}
			});

			switch (sortBy) {
			case 1:
				if (sortOrder == ASC_ORDER)
					itemColumn.setText("Item \u25BC");
				else
					itemColumn.setText("Item \u25B2");
				break;
			case 2:
				if (sortOrder == ASC_ORDER)
					typeColumn.setText("Type \u25BC");
				else
					typeColumn.setText("Type \u25B2");
				break;
			case 3:
				if (sortOrder == ASC_ORDER)
					dateColumn.setText("Date \u25BC");
				else
					dateColumn.setText("Date \u25B2");
				break;
			}

			tableLayout.addView(headerRow);

			int i = 0;
			for (MaintenanceLog log : logs) {

				View tableRow = inflater.inflate(R.layout.table_row_3_coulm_view, null);

				((TextView) tableRow.findViewById(R.id.tr_header1)).setText(log.getItemName());
				((TextView) tableRow.findViewById(R.id.tr_header2)).setText(log.getProblemType());
				((TextView) tableRow.findViewById(R.id.tr_header3)).setText(DateUtil.getStringFromDateShortYear(log.getLogDate()));
				
				if ((i % 2) == 0) {
					tableRow.setBackgroundResource(R.drawable.tablerow_even);
				} else {
					tableRow.setBackgroundResource(R.drawable.tablerow_odd);
				}

				tableRow.setTag(R.id.ItemId, log.getLogId());
				tableRow.setTag(R.id.ListIndex, i);
				tableRow.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						long logId = ((Long) v.getTag(R.id.ItemId)).longValue();
						int logIndex = ((Integer) v.getTag(R.id.ListIndex)).intValue();

						Intent intent = new Intent();
						intent.setClass(MaintenanceLogsActivity.this, MaintenanceViewActivity.class);
						intent.putExtra("LogId", logId);
						intent.putExtra("LogIndex", logIndex);
						startActivityForResult(intent, 0);
						
						return;
					}
				});
				
				tableLayout.addView(tableRow);
				i++;
			}

		} catch (Exception e) {
			log.error(TPUtility.getPrintStackTrace(e));
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			try {
				logs = ((CommonBLProcessor) screenBLProcessor).getMaintenanceLogs();
				dataLoadingHandler.sendEmptyMessage(1);
			} catch (Exception e) {
				log.error(TPUtility.getPrintStackTrace(e));
			}
		}
	}

	public void bindDataAtLoadingTime() {
		try {
			progressDialog = ProgressDialog.show(this, "", "Loading...");
			new Thread() {
				public void run() {
					try {
						logs = ((CommonBLProcessor) screenBLProcessor).getMaintenanceLogs();
						dataLoadingHandler.sendEmptyMessage(1);
					} catch (TPException ae) {
						log.error(ae.getMessage());
						errorHandler.sendEmptyMessage(0);
					}
				}
			}.start();

		} catch (Exception e) {
			log.error(TPUtility.getPrintStackTrace(e));
		}
	}

	@Override
	public void onClick(View v) {

	}

	@Override
	public void onBackPressed() {
		backAction(null);
	}

	public void backAction(View view) {
		finish();
	}
	
	public void newAction(View view) {
		Intent intent = new Intent();
		intent.setClass(this, AddMaintenanceActivity.class);
		startActivityForResult(intent, 1);
	}

	@Override
	public void handleVoiceInput(String text) {
		if (text == null){
			return;
		}

		Toast.makeText(this, text, Toast.LENGTH_LONG).show();
		if (text.contains("BACK") 
				|| text.contains("GO BACK") 
				|| text.contains("CLOSE")) {
			
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
}
