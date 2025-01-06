package com.ticketpro.parking.activity;

import java.util.Collections;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ticketpro.model.LPRNotify;
import com.ticketpro.parking.R;
import com.ticketpro.parking.activity.handlers.NotificationHandler;
import com.ticketpro.parking.bl.CommonBLProcessor;
import com.ticketpro.util.CallbackHandler;
import com.ticketpro.util.DateUtil;
import com.ticketpro.util.TPUtility;

/**
 * Home screen (Ref. Mockups).
 * 
 * @author : TicketPRO
 * @version : 1.0
 * @since : 1.0
 *
 */

public class LPRNotifyActivity extends BaseActivityImpl {

	private TableLayout tableLayout;
	private List<LPRNotify> lprNotifyList;
	private final int ASC_ORDER = 1;
	private final int DESC_ORDER = 2;
	private int sortBy = 0;
	private int sortOrder = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.lpr_notify_logs);
			setActiveScreen(this);

			setLogger(LPRNotifyActivity.class.getName());
			setBLProcessor(new CommonBLProcessor((TPApplication) getApplicationContext()));
			tableLayout = (TableLayout) findViewById(R.id.logs_3_table_view);

			bindDataAtLoadingTime();

		} catch (Exception e) {
			log.error(TPUtility.getPrintStackTrace(e));
		}
	}

	private void initDatagrid() {
		try {
			if (lprNotifyList == null) {
				return;
			}

			tableLayout.removeAllViews();
			LayoutInflater inflater = LayoutInflater.from(this);

			// adding Header
			View headerRow = inflater.inflate(R.layout.table_row_3_coulm_view, null);

			TextView plateColumn = ((TextView) headerRow.findViewById(R.id.tr_header1));
			plateColumn.setText("Plate");
			plateColumn.setClickable(true);
			plateColumn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					Collections.sort(lprNotifyList, new LPRNotify.PlateComparator());

					// Update Sorting Order
					if (sortBy != 1) {
						sortOrder = ASC_ORDER;
					} else if (sortOrder == ASC_ORDER) {
						sortOrder = DESC_ORDER;
						Collections.reverse(lprNotifyList);
					} else {
						sortOrder = ASC_ORDER;
					}

					sortBy = 1;
					initDatagrid();
				}
			});

			TextView make = ((TextView) headerRow.findViewById(R.id.tr_header01));
			make.setText("Make");

			TextView color = ((TextView) headerRow.findViewById(R.id.tr_header02));
			color.setText("Color");

			TextView dateTime = ((TextView) headerRow.findViewById(R.id.tr_header2));
			dateTime.setText("Date/Time");
			dateTime.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					Collections.sort(lprNotifyList, new LPRNotify.DateComparator());

					// Update Sorting Order
					if (sortBy != 2) {
						sortOrder = ASC_ORDER;
					} else if (sortOrder == ASC_ORDER) {
						sortOrder = DESC_ORDER;
						Collections.reverse(lprNotifyList);
					} else {
						sortOrder = ASC_ORDER;
					}

					sortBy = 2;
					initDatagrid();
				}
			});

			TextView statusColumn = ((TextView) headerRow.findViewById(R.id.tr_header3));
			statusColumn.setText("Status");
			statusColumn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					Collections.sort(lprNotifyList, new LPRNotify.StatusComparator());

					// Update Sorting Order
					if (sortBy != 3) {
						sortOrder = ASC_ORDER;
					} else if (sortOrder == ASC_ORDER) {
						sortOrder = DESC_ORDER;
						Collections.reverse(lprNotifyList);
					} else {
						sortOrder = ASC_ORDER;
					}

					sortBy = 3;
					initDatagrid();
				}
			});

			switch (sortBy) {
			case 1:
				if (sortOrder == ASC_ORDER) {
					plateColumn.setText("Plate \u25BC");
				} else {
					plateColumn.setText("Plate \u25B2");
				}

				break;

			case 2:
				if (sortOrder == ASC_ORDER) {
					dateTime.setText("Date/Time \u25BC");
				} else {
					dateTime.setText("Date/Time \u25B2");
				}

				break;

			case 3:
				if (sortOrder == ASC_ORDER) {
					statusColumn.setText("Status \u25BC");
				} else {
					statusColumn.setText("Status \u25B2");
				}

				break;
			}

			tableLayout.addView(headerRow);

			int i = 0;
			for (LPRNotify lprNotify : lprNotifyList) {
				View tableRow = inflater.inflate(R.layout.table_row_3_coulm_view, null);

				((TextView) tableRow.findViewById(R.id.tr_header1)).setText(lprNotify.getPlate() + "");
				((TextView) tableRow.findViewById(R.id.tr_header01)).setText(lprNotify.getMake() + "");
				((TextView) tableRow.findViewById(R.id.tr_header02)).setText(lprNotify.getColor() + "");
				((TextView) tableRow.findViewById(R.id.tr_header2)).setText(DateUtil.getStringFromDateShortYear(lprNotify.getNotifyDate()));
				((TextView) tableRow.findViewById(R.id.tr_header3)).setText(lprNotify.getPermitStatus());

				if ((i % 2) == 0) {
					tableRow.setBackgroundResource(R.drawable.tablerow_even);
				} else {
					tableRow.setBackgroundResource(R.drawable.tablerow_odd);
				}

				if (lprNotify.isTicketIssued()) {
					tableRow.setBackgroundResource(R.drawable.tablerow_ticketed);
				}

				ImageView actionIcon = ((ImageView) tableRow.findViewById(R.id.tr_action));
				actionIcon.setBackgroundResource(R.drawable.delete_icon);
				actionIcon.setVisibility(View.VISIBLE);
				actionIcon.setTag(R.id.NotificationId, lprNotify.getNotificationId());
				actionIcon.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						String notificationId = (String) v.getTag(R.id.NotificationId);
						deleteNotification(notificationId);
					}
				});

				tableRow.setTag(R.id.TicketIndex, i);
				tableRow.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						int logIndex = ((Integer) v.getTag(R.id.TicketIndex)).intValue();
						LPRNotify lprNotify = lprNotifyList.get(logIndex);
						NotificationHandler handler = new NotificationHandler(getApplicationContext());
						handler.setRemoveCallback(new CallbackHandler() {
							@Override
							public void success(String notificationId) {
								deleteNotification(notificationId);
							}

							@Override
							public void failure(String message) {
							}
						});

						handler.showLPRNotify(lprNotify);

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

	public void bindDataAtLoadingTime() {
		try {

			lprNotifyList = LPRNotify.getLPRNotifications();
			initDatagrid();

		} catch (Exception e) {
			TPUtility.showErrorToast(this, "Error loading LPR Notifications");
			log.error(TPUtility.getPrintStackTrace(e));
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			try {
				lprNotifyList = LPRNotify.getLPRNotifications();
				initDatagrid();
			} catch (Exception e) {
				TPUtility.showErrorToast(this, "Error loading LPR Notifications");
				log.error(TPUtility.getPrintStackTrace(e));
			}
		}
	}

	@Override
	public void onClick(View v) {

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		backAction(null);
	}

	public void backAction(View view) {
		finish();
	}

	public void mapViewAction(View view) {
		Intent i = new Intent();
		i.setClass(this, LPRMapViewActivity.class);
		startActivityForResult(i, 0);
	}

	public void clearAction(View view) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Delete Confirmation");
		builder.setMessage("Are you sure you want to delete all LPR Notifications?");
		builder.setCancelable(true).setNegativeButton("No", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});

		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				try {
					LPRNotify.removeAllNotifications();
				} catch (Exception e) {
					TPUtility.showErrorToast(LPRNotifyActivity.this, "Error removing LPR Notifications");
					log.error(TPUtility.getPrintStackTrace(e));
				}

				bindDataAtLoadingTime();
			}
		});

		AlertDialog alert = builder.create();
		alert.show();
	}

	public void deleteNotification(final String notificationId) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Delete Confirmation");
		builder.setMessage("Are you sure you want to delete LPR Notification?");
		builder.setCancelable(true).setNegativeButton("No", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});

		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				try {
					LPRNotify.removeNotificationById(notificationId);
				} catch (Exception e) {
					TPUtility.showErrorToast(LPRNotifyActivity.this, "Error removing LPR Notification");
					log.error(TPUtility.getPrintStackTrace(e));
				}

				bindDataAtLoadingTime();
			}
		});

		AlertDialog alert = builder.create();
		alert.show();
	}

	@Override
	public void handleVoiceInput(String text) {
		if (text == null) {
			return;
		}

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
}
