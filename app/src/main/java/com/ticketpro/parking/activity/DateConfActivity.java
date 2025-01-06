package com.ticketpro.parking.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;

import com.ticketpro.api.ApiRequest;
import com.ticketpro.api.ServiceGenerator;
import com.ticketpro.model.Feature;
import com.ticketpro.model.Message;
import com.ticketpro.model.MessageResponse;
import com.ticketpro.model.Params;
import com.ticketpro.model.RequestPOJO;
import com.ticketpro.parking.R;
import com.ticketpro.parking.bl.CommonBLProcessor;
import com.ticketpro.util.DateUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


/**
 * Home screen (Ref. Mockups).
 *
 * @author : TicketPRO
 * @version : 1.0
 * @since : 1.0
 */
public class DateConfActivity extends BaseActivityImpl {
    private static final String TAG = "DateConfActivity";
    final int DATE_DIALOG_ID = 0;
    private TextView dateView;
    private TextView weekdayView, juliandateTextView;
    private ArrayList<Message> messages;
    private Handler dataLoadingHandler;
    private Handler errorHandler;
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.date_conf);
        setLogger(DateConfActivity.class.getName());
        setBLProcessor(new CommonBLProcessor((TPApplication) getApplicationContext()));
        setActiveScreen(this);
        dateView = (TextView) findViewById(R.id.dateTextView);
        weekdayView = (TextView) findViewById(R.id.weekdayTextView);

        juliandateTextView = (TextView) findViewById(R.id.juliandateTextView);

        final String[] MONTHS = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        final String[] DAYS = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

        Calendar c = Calendar.getInstance();
        int cyear = c.get(Calendar.YEAR);
        int cmonth = c.get(Calendar.MONTH);
        int cday = c.get(Calendar.DAY_OF_MONTH);
        int cweekDay = c.get(Calendar.DAY_OF_WEEK);

        dateView.setText(MONTHS[cmonth] + " " + cday + ", " + cyear);
        weekdayView.setText(DAYS[cweekDay - 1]);

        juliandateTextView.setText("JD" + DateUtil.dateToJulian(c));
        try {
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
            this.bindDataAtLoadingTime();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void initDatagrid() {
        try {
            //User userInfo=User.getUserInfo(TPApp.getCurrentUserId());
            //ArrayList<Message> messages=Message.getMessages(userInfo.getDepartment());

            if (messages != null && messages.size() > 0) {
                LayoutInflater mInflater = LayoutInflater.from(this);
                View messagesView = mInflater.inflate(R.layout.logs, null);

                TableLayout tableLayout = (TableLayout) messagesView.findViewById(R.id.logs_table_view);

                int i = 0;
                for (final Message message : messages) {
                    View tableRow = mInflater.inflate(R.layout.messages_row_view, null);

                    String messageText = message.getMessage();
					
					/* As per feedback no need to display from user
					try{
						User user=User.getUserInfo(message.getFromUserId());
						if(user!=null){
							messageText=messageText+" - "+StringUtil.getDisplayString(user.getFirstName());
							if(user.getLastName()!=null){
								messageText=messageText+" "+StringUtil.getDisplayString(user.getLastName());
							}
						}
					}catch(Exception e){
						log.error(TPUtility.getPrintStackTrace(e));
					}*/

                    ((TextView) tableRow.findViewById(R.id.title_textview)).setText(message.getSubject());
                    ((TextView) tableRow.findViewById(R.id.from_date_textview)).setText(DateUtil.getDateStringFromDate(message.getMessageDate()));
                    ((TextView) tableRow.findViewById(R.id.to_date_textview)).setText(DateUtil.getDateStringFromDate(message.getExpiryDate()));
                    ((TextView) tableRow.findViewById(R.id.messages_textview)).setText(messageText);

                    if ((i % 2) == 0) {
                        tableRow.setBackgroundResource(R.drawable.tablerow_even);
                    } else {
                        tableRow.setBackgroundResource(R.drawable.tablerow_odd);
                    }

                    tableRow.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //int logIndex = ((Integer) view.getTag(R.id.TicketIndex)).intValue();
                            showMessageDialog(message);
                        }
                    });

                    tableLayout.addView(tableRow);
                    i++;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Messages")
                        .setView(messagesView)
                        .setCancelable(true)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                AlertDialog alert = builder.create();
                alert.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showMessageDialog(Message message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(message.getSubject())
                .setMessage(message.getMessage() + "\n\n" + DateUtil.getDateStringFromDate(message.getMessageDate()) + " - " + DateUtil.getDateStringFromDate(message.getExpiryDate()))
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
        progressDialog = new ProgressDialog(DateConfActivity.this);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Checking for messages...");
        progressDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Cancel", (dialog, which) -> {
            dialog.dismiss();
        });

        try {
            if (TPApp.isServiceAvailable && Feature.isFeatureAllowed(Feature.CHECK_MESSAGES)) {
                progressDialog.show();
                getMessages();
            }
        } catch (Exception ae) {
            log.error(ae.getMessage());
            errorHandler.sendEmptyMessage(0);
        }
    }

    @Override
    public void onClick(View v) {
    }

    public void backAction(View view) {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    public void acceptAction(View view) {
        Intent i = new Intent();
        i.setClass(this, DayActivitiesActivity.class);
        startActivity(i);

    }

    @Override
    public void handleVoiceInput(String text) {
        // TODO Auto-generated method stub
    }

    @Override
    public void handleVoiceMode(boolean voiceMode) {
        // TODO Auto-generated method stub
    }

    @Override
    public void handleNetworkStatus(boolean connected, boolean isFastConnection) {
        // TODO Auto-generated method stub
    }

    private void getMessages() {
        RequestPOJO requestPOJO = new RequestPOJO();
        Params params = new Params();
        params.setFullSync(true);
        params.setCustId(TPApplication.getInstance().custId);
        requestPOJO.setParams(params);
        requestPOJO.setMethod("getMessages");
        ApiRequest api = ServiceGenerator.createRxService(ApiRequest.class);
        final Observable<MessageResponse> message = api.getMessages(requestPOJO);
        message.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).map(new Function<MessageResponse, List<Message>>() {
            @Override
            public List<Message> apply(MessageResponse messageResponse) throws Exception {
                return messageResponse.getResult();
            }
        }).subscribeWith(new Observer<List<Message>>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.i(TAG, "onSubscribe: " + d);
            }


            @Override
            public void onNext(List<Message> o) {
                DateConfActivity.this.messages = new ArrayList<>();
                DateConfActivity.this.messages.addAll(o);
                dataLoadingHandler.sendEmptyMessage(1);
                Log.i(TAG, "onNext: " + o.toString());
            }

            @Override
            public void onError(Throwable e) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                Log.e(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "onComplete: ");
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        });

    }

}
