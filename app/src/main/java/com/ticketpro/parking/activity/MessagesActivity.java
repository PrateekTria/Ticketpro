package com.ticketpro.parking.activity;

import java.util.ArrayList;

import com.ticketpro.exception.TPException;
import com.ticketpro.model.Feature;
import com.ticketpro.model.Message;
import com.ticketpro.model.User;
import com.ticketpro.parking.R;
import com.ticketpro.parking.bl.CommonBLProcessor;
import com.ticketpro.parking.service.TPAsyncTask;
import com.ticketpro.util.DateUtil;
import com.ticketpro.util.StringUtil;
import com.ticketpro.util.TPTask;
import com.ticketpro.util.TPUtility;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Home screen (Ref. Mockups).
 * 
 * @author : TicketPRO
 * @version : 1.0
 * @since : 1.0
 *
 */

public class MessagesActivity extends BaseActivityImpl {

	private TableLayout tableLayout;
	private ArrayList<Message> messages;
	private Handler dataLoadingHandler;
	private Handler errorHandler;

	/**
	 * Entry point of the Activity
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.messages_logs);
			setLogger(MessagesActivity.class.getName());
			setBLProcessor(new CommonBLProcessor((TPApplication) getApplicationContext()));
			setActiveScreen(this);

			isNetworkInfoRequired = true;

			tableLayout = (TableLayout) findViewById(R.id.logs_3_table_view);
			dataLoadingHandler = new Handler() {
				@Override
				public void handleMessage(android.os.Message msg) {
					super.handleMessage(msg);
					initDatagrid();
				}
			};

			errorHandler = new Handler() {
				@Override
				public void handleMessage(android.os.Message msg) {
					super.handleMessage(msg);
				}
			};

			bindDataAtLoadingTime();

		} catch (Exception e) {
			log.error(TPUtility.getPrintStackTrace(e));
		}
	}

	private void initDatagrid() {
		try {
			if (messages == null){
				return;
			}

			tableLayout.removeAllViews();
			LayoutInflater mInflater = LayoutInflater.from(this);
			//View messagesView = mInflater.inflate(R.layout.logs, null);

			int i = 0;
			for (final Message message : messages) {
				View tableRow = mInflater.inflate(R.layout.messages_row_view, null);

				String messageText = message.getMessage();
				try {
					User user = User.getUserInfo(message.getFromUserId());
					if (user != null) {
						messageText = messageText + " - " + StringUtil.getDisplayString(user.getFirstName());
						if (user.getLastName() != null) {
							messageText = messageText + " " + StringUtil.getDisplayString(user.getLastName());
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				((TextView) tableRow.findViewById(R.id.title_textview)).setText(message.getSubject());
				((TextView) tableRow.findViewById(R.id.from_date_textview)).setText(DateUtil.getDateStringFromDate(message.getMessageDate()));
				((TextView) tableRow.findViewById(R.id.to_date_textview)).setText(DateUtil.getDateStringFromDate(message.getExpiryDate()));
				((TextView) tableRow.findViewById(R.id.messages_textview)).setText(messageText);

				if ((i % 2) == 0){
					tableRow.setBackgroundResource(R.drawable.tablerow_even);
				}else{
					tableRow.setBackgroundResource(R.drawable.tablerow_odd);
				}

				tableRow.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						showMessageDialog(message);
					}
				});
				
				tableLayout.addView(tableRow);
				i++;
			}
		} catch (Exception e) {
			log.error(TPUtility.getPrintStackTrace(e));
		}
	}

	public void showMessageDialog(Message message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(message.getSubject())
				.setMessage(message.getMessage() + "\n\n" + DateUtil.getDateStringFromDate(message.getMessageDate()) +" - "+ DateUtil.getDateStringFromDate(message.getExpiryDate()))
				.setCancelable(true)
				.setNeutralButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

		AlertDialog alert = builder.create();
		alert.show();
	}

	public void bindDataAtLoadingTime() {
		TPAsyncTask task = new TPAsyncTask(this, "Loading...");
		task.execute(new TPTask() {
			@Override
			public void execute() {
				try { 
					if(TPApp.isServiceAvailable && Feature.isFeatureAllowed(Feature.CHECK_MESSAGES)){
						messages = ((CommonBLProcessor) screenBLProcessor).getServerMessages();
					}else{
						messages = ((CommonBLProcessor) screenBLProcessor).getMessages();
					}
					
					dataLoadingHandler.sendEmptyMessage(1);
				
				}catch (TPException ae) {
					log.error(ae.getMessage());
					errorHandler.sendEmptyMessage(0);
				}
			}
		});
	}

	@Override
	public void onClick(View v) {

	}

	public void backAction(View view) {
		finish();
	}

	public void removeAllAction(View view) {
		try {
			if (messages != null && messages.size() > 0) {
				Message.removeAll();

				tableLayout.removeAllViews();
				messages.clear();

				Toast.makeText(this, "Removed all messages successfully", Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			log.error(TPUtility.getPrintStackTrace(e));
		}
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
