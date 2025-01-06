package com.ticketpro.parking.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.ticketpro.model.ChalkComment;
import com.ticketpro.model.ChalkPicture;
import com.ticketpro.model.ChalkVehicle;
import com.ticketpro.model.Duration;
import com.ticketpro.model.User;
import com.ticketpro.parking.R;
import com.ticketpro.parking.bl.ChalkBLProcessor;
import com.ticketpro.util.DateUtil;
import com.ticketpro.util.TPConstant;
import com.ticketpro.util.TPUtility;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Home screen (Ref. Mockups).
 * 
 * @author : TicketPRO
 * @version : 1.0
 * @since : 1.0 
 *
 */

public class RetrieveChalkActivity extends BaseActivityImpl{

	private ArrayList<ChalkVehicle> chalks;
	private ArrayList<Duration> durations;
	private List<User> users;
	
	private Handler dataLoadingHandler;
	private Handler errorHandler;
	private ProgressDialog progressDialog;
	private TextView totalTextView;
	private Spinner usersSpinner;
	private Spinner durationSpinner;
	private TableLayout tableLayout;
	private Button fromDateBtn;
	private Button fromTimeBtn;
	private Button toDateBtn;
	private Button toTimeBtn;
	
	private Date fromDate;
	private Date toDate;
	private User selectedUser;
	private Duration selectedDuration;
	
	final int DATA_ERROR=0;
	final int DATA_SUCCESSFULL=1;
	final int ERROR_LOAD=1;
	final int ERROR_SERVICE=2;
	
	//Date/Time Picker Constants
	final int FROM_DATE_DIALOG_ID = 0;
	final int FROM_TIME_DIALOG_ID = 1;
	final int TO_DATE_DIALOG_ID = 2;
	final int TO_TIME_DIALOG_ID = 3;
	
	private final int ASC_ORDER=1;
	private final int DESC_ORDER=2;
	private int sortBy=0;
	private int sortOrder=0;
	
	/**
	 * Entry point of the Activity
	 */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
	        setContentView(R.layout.retrieve_chalks);
	        setLogger(RetrieveChalkActivity.class.getName());
	        setActiveScreen(this);
			
	        tableLayout=(TableLayout)findViewById(R.id.chalk_4_table_view);
		    usersSpinner=(Spinner)findViewById(R.id.users_spinner);
		    durationSpinner=(Spinner)findViewById(R.id.zones_spinner);
		    totalTextView=(TextView)findViewById(R.id.total_chalks_textview);
		    fromDateBtn=(Button)findViewById(R.id.from_date_btn);
		    fromTimeBtn=(Button)findViewById(R.id.from_time_btn);
		    toDateBtn=(Button)findViewById(R.id.to_date_btn);
		    toTimeBtn=(Button)findViewById(R.id.to_time_btn);
		    
	        dataLoadingHandler=new Handler() {
				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					
					switch (msg.what) {
					case 0:
						if(users!=null){
							ArrayList<String> userList=new ArrayList<String>();
							userList.add("All Users");
							for(User user:users){
								userList.add(user.getUsername());
							}
							
							ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(RetrieveChalkActivity.this, android.R.layout.simple_spinner_item, userList);
							dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
							usersSpinner.setAdapter(dataAdapter); 
							usersSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
							    @Override
							    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
							    	if(position==0){
							    		selectedUser=null;
							    		return;
							    	}
							    	
							    	selectedUser=users.get(position-1);
							    }

							    @Override
							    public void onNothingSelected(AdapterView<?> parentView) {
							    	
							    }
							});
							
							
							ArrayList<String> durationList=new ArrayList<String>();
							durationList.add("All Zones");
							for(Duration duration:durations){
								durationList.add(duration.getTitle());
							}
							
							ArrayAdapter<String> durationAdapter = new ArrayAdapter<String>(RetrieveChalkActivity.this, android.R.layout.simple_spinner_item,durationList);
							durationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
							durationSpinner.setAdapter(durationAdapter); 
							durationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
							    @Override
							    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
							    	if(position==0){
							    		selectedDuration=null;
							    		return;
							    	}
							    	
							    	selectedDuration=durations.get(position-1);
							    }

							    @Override
							    public void onNothingSelected(AdapterView<?> parentView) {
							    	
							    }
							});
							
						}
						break;

					case 1:
						initDatagrid();
						break;
					}
					
					if(progressDialog.isShowing())
						progressDialog.dismiss();
				}
			};
	
			errorHandler=new Handler() {
				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					if(progressDialog.isShowing())
						progressDialog.dismiss();
				}
			};	
			
			bindDataAtLoadingTime();
	    
        }catch(Exception e){
			log.error(TPUtility.getPrintStackTrace(e)); 
		}
	}    
	
	private void initDatagrid(){
		try{
			if(chalks==null || tableLayout==null)
				return;
			
			totalTextView.setText("Total ("+chalks.size()+")");
			tableLayout.removeAllViews();
			LayoutInflater inflater = LayoutInflater.from(this); 
			
			//adding Header
	        View headerRow = inflater.inflate(R.layout.table_row_4_column_view, null);
	    	
	        TextView plateColumn=((TextView)headerRow.findViewById(R.id.tr_header1));
	        TextView timeColumn=((TextView)headerRow.findViewById(R.id.tr_header2));
	        TextView zoneColumn=((TextView)headerRow.findViewById(R.id.tr_header3));
	        TextView elapsedColumn=((TextView)headerRow.findViewById(R.id.tr_header4));
	        
	        plateColumn.setText("Plate/VIN");
	        plateColumn.setClickable(true);
	        plateColumn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					Collections.sort(chalks,new ChalkVehicle.PlateComparator());
					
					//Update Sorting Order
					if(sortBy!=1){
						sortOrder=ASC_ORDER;
					}else if(sortOrder==ASC_ORDER){
						sortOrder=DESC_ORDER;
						Collections.reverse(chalks);
					}else{
						sortOrder=ASC_ORDER;
					}
					
					sortBy=1;
					initDatagrid();
				}
			});
	        
	        timeColumn.setText("Time");
	        timeColumn.setClickable(true);
	        timeColumn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					Collections.sort(chalks,new ChalkVehicle.DateComparator());
					
					//Update Sorting Order
					if(sortBy!=2){
						sortOrder=ASC_ORDER;
					}else if(sortOrder==ASC_ORDER){
						sortOrder=DESC_ORDER;
						Collections.reverse(chalks);
					}else{
						sortOrder=ASC_ORDER;
					}
					
					sortBy=2;
					initDatagrid();
				}
			});
	        
	        zoneColumn.setText("Zone");
	        zoneColumn.setClickable(true);
	        zoneColumn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					Collections.sort(chalks,new ChalkVehicle.ZoneComparator());
					
					//Update Sorting Order
					if(sortBy!=3){
						sortOrder=ASC_ORDER;
					}else if(sortOrder==ASC_ORDER){
						sortOrder=DESC_ORDER;
						Collections.reverse(chalks);
					}else{
						sortOrder=ASC_ORDER;
					}
					
					sortBy=3;
					initDatagrid();
				}
			});
	        
	        elapsedColumn.setText("Elapsed");
	        elapsedColumn.setGravity(Gravity.CENTER);
	        elapsedColumn.setClickable(true);
	        elapsedColumn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					Collections.sort(chalks,new ChalkVehicle.DateComparator());
					
					//Update Sorting Order
					if(sortBy!=4){
						sortOrder=ASC_ORDER;
					}else if(sortOrder==ASC_ORDER){
						sortOrder=DESC_ORDER;
						Collections.reverse(chalks);
						
					}else{
						sortOrder=ASC_ORDER;
					}
					
					sortBy=4;
					initDatagrid();
				}
			});
	        
	        switch (sortBy) {
				case 1:
					if(sortOrder==ASC_ORDER)
						plateColumn.setText("Plate/VIN \u25BC");
					else
						plateColumn.setText("Plate/VIN \u25B2");
					break;
				case 2:
					if(sortOrder==ASC_ORDER)
						timeColumn.setText("Time \u25BC");
					else
						timeColumn.setText("Time \u25B2");
					break;
				case 3:
					if(sortOrder==ASC_ORDER)
						zoneColumn.setText("Zone \u25BC");
					else
						zoneColumn.setText("Zone \u25B2");
					break;
				case 4:
					if(sortOrder==ASC_ORDER)
						elapsedColumn.setText("Elasped \u25BC");
					else
						elapsedColumn.setText("Elasped \u25B2");
					break;
	        }
	        
	    	tableLayout.addView(headerRow);
	    	
			int i=0;
	        for(ChalkVehicle chalk:chalks){
	        	View tableRow = inflater.inflate(R.layout.table_row_4_column_view, null);
	        	
	        	String plate="NA";
	        	if(chalk.getPlate()!=null && !chalk.getPlate().equals(""))
	        		plate=chalk.getPlate();
	        	
	        	else if(chalk.getVin()!=null && !chalk.getVin().equals(""))
	        		plate=chalk.getVin();
	        		
	        	((TextView)tableRow.findViewById(R.id.tr_header1)).setText(plate);
	        	((TextView)tableRow.findViewById(R.id.tr_header2)).setText(DateUtil.getSQLStringFromDate(chalk.getChalkDate()));
	        	((TextView)tableRow.findViewById(R.id.tr_header3)).setText(getDuration(chalk.getDurationId()));
	        	
	        	int mins=Duration.getDurationMinsById(chalk.getDurationId(),this);
	        	long diff=(new Date().getTime()-chalk.getChalkDate().getTime());
	        	long expTime=(diff/1000)/60;
	        	
	        	((TextView)tableRow.findViewById(R.id.tr_header4)).setText(TPUtility.getHrsMinSecs(diff));
	        	((TextView)tableRow.findViewById(R.id.tr_header4)).setGravity(Gravity.CENTER);
	        	
	        	if((i%2)==0)
	        		tableRow.setBackgroundResource(R.drawable.tablerow_even);
	        	else
	        		tableRow.setBackgroundResource(R.drawable.tablerow_odd);
	        	
	        	if(expTime > mins)
	        		tableRow.setBackgroundResource(R.drawable.tablerow_expired);
	        	
	        	tableRow.setTag(R.id.ChalkId,chalk.getChalkId());
	        	tableRow.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						///long chalkId=((Long)v.getTag(R.id.ChalkId)).longValue();
						return;
					}
				});
	        	
	        	tableLayout.addView(tableRow);
	        	i++;
	        }
	   
		}catch(Exception e){
			log.error(TPUtility.getPrintStackTrace(e)); 
		}
	} 
	
	public void bindDataAtLoadingTime(){
		progressDialog = ProgressDialog.show(this, "", "Loading...");
		new Thread(){
			public void run() {
				try{
					users=User.getUsers(TPConstant.MODULE_NAME);
					durations=Duration.getDurations();
					dataLoadingHandler.sendEmptyMessage(0);
				} 
				catch (Exception e) {
					log.error(TPUtility.getPrintStackTrace(e)); 
					errorHandler.sendEmptyMessage(1);
				}
			}
		}.start();
		
		
		//Update Default Date and Time
		Calendar c = Calendar.getInstance();
		int hourOfDay = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		int monthOfYear = c.get(Calendar.MONTH);
		int year = c.get(Calendar.YEAR);
		int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
		
		
		fromDateBtn.setText(prefixZero(monthOfYear+1)+"/"+prefixZero(dayOfMonth)+"/"+year);
		fromTimeBtn.setText(prefixZero(0)+":"+prefixZero(0));
		toDateBtn.setText(prefixZero(monthOfYear+1)+"/"+prefixZero(dayOfMonth)+"/"+year);
		toTimeBtn.setText(prefixZero(hourOfDay)+":"+prefixZero(minute));
		
	}
	
	@Override
	public void onClick(View v) {
		
	}
	
	@Override
	public void onBackPressed() {
		finish();
	}
	
	public void resetFilters(View view){
		fromDateBtn.setText("Date");
		fromTimeBtn.setText("Time");
		toDateBtn.setText("Date");
		toTimeBtn.setText("Time");
		
		fromDate=null;
		toDate=null;
		
		if(chalks!=null && tableLayout!=null){
			chalks.clear();
			initDatagrid();
		}
	}
	
	private String getDuration(int durationId){
		if(this.durations==null)
			return "NA";
		
		for(Duration duration:this.durations){
			if(duration.getId()==durationId){
				return duration.getTitle();
			}
		}
		
		return "NA";
	}
	
	public void retrieveChalks(View view){
		int durationId=0;
		if(selectedDuration!=null){
			durationId=selectedDuration.getId();
		}
		
		final int durationIdFinal=durationId;
		
		String fromDateStr=fromDateBtn.getText().toString();
		String toDateStr=toDateBtn.getText().toString();
		if(fromDateStr.equals("Date") || toDateStr.equals("Date")){
			displayErrorMessage("Please select from and to date");
			return;
		}
		
		String[] fromDateArray=fromDateStr.split("/");
		String[] toDateArray=toDateStr.split("/");
		
		int fMonth=Integer.parseInt(fromDateArray[0]);
		int fDay=Integer.parseInt(fromDateArray[1]);
		int fYear=Integer.parseInt(fromDateArray[2]);
		
		int tMonth=Integer.parseInt(toDateArray[0]);
		int tDay=Integer.parseInt(toDateArray[1]);
		int tYear=Integer.parseInt(toDateArray[2]);
		
		int fHours=0;
		int fMinutes=0;
		int tHours=0;
		int tMinutes=0;
		
		String fromTimeStr=fromTimeBtn.getText().toString();
		if(!fromTimeStr.equals("Time")){
			String[] fromTimeArray=fromTimeStr.split(":");
			fHours=Integer.parseInt(fromTimeArray[0]);
			fMinutes=Integer.parseInt(fromTimeArray[1]);
		}
		
		String toTimeStr=toTimeBtn.getText().toString();
		if(!toTimeStr.equals("Time")){
			String[] toTimeArray=toTimeStr.split(":");
			tHours=Integer.parseInt(toTimeArray[0]);
			tMinutes=Integer.parseInt(toTimeArray[1]);
		}
		
		Calendar fromCal=Calendar.getInstance();
		fromCal.set(Calendar.YEAR,fYear);
		fromCal.set(Calendar.MONTH,fMonth-1);
		fromCal.set(Calendar.DAY_OF_MONTH,fDay);
		fromCal.set(Calendar.HOUR_OF_DAY,fHours);
		fromCal.set(Calendar.MINUTE,fMinutes);
		
		Calendar toCal=Calendar.getInstance();
		toCal.set(Calendar.YEAR,tYear);
		toCal.set(Calendar.MONTH,tMonth-1);
		toCal.set(Calendar.DAY_OF_MONTH,tDay);
		toCal.set(Calendar.HOUR_OF_DAY,tHours);
		toCal.set(Calendar.MINUTE,tMinutes);
		
		fromDate=fromCal.getTime();
		toDate=toCal.getTime();
		
		if(fromDate.getTime() > toDate.getTime()){
			displayErrorMessage("Invalid date range. From date can not be greater than to date");
			return;
		}
		
		progressDialog = ProgressDialog.show(this, "", "Retrieving chalks...");
		new Thread(){
			public void run() {
				ChalkBLProcessor bl=new ChalkBLProcessor((TPApplication)getApplicationContext());
				int userId;
				try{
					if(selectedUser==null){
						userId=-1;
					}else{
						userId=selectedUser.getUserId();
					}
					
					chalks=bl.getUserChalks(userId, fromDate, toDate, durationIdFinal);
					dataLoadingHandler.sendEmptyMessage(1);
				} catch (Exception e) {
					log.error(TPUtility.getPrintStackTrace(e)); 
					errorHandler.sendEmptyMessage(1);
				}
			}
		}.start();
	}
	
	public void downloadAction(View view){
		if(chalks==null || chalks.size()==0){
			Toast.makeText(this, "No chalks to download. Please retrieve chalks first.", Toast.LENGTH_LONG).show();
			return;
		}
		
		//DatabaseHelper.getInstance().openWritableDatabase();
		for(ChalkVehicle chalk: chalks){
			/* Translate userId to Device id for download chalks*/
			chalk.setDeviceId(chalk.getUserId());
			chalk.setUserId(-1);
			ChalkVehicle.insertChalkVehicle(chalk).subscribe();
			//DatabaseHelper.getInstance().insertOrReplace(chalk.getContentValues(), "chalk_vehicles");
			for(ChalkComment comment: chalk.getComments()){
				ChalkComment.insertChalkComment(comment).subscribe();
				//DatabaseHelper.getInstance().insertOrReplace(comment.getContentValues(), "chalk_comments");
			}
			
			for(ChalkPicture picture: chalk.getChalkPictures()){
				String contentFolder=TPApp.getCustomerInfo().getContentFolder();
				if(contentFolder==null || contentFolder.equals("")){
					contentFolder=TPApp.getCustomerInfo().getCustId()+"";
				}
				String imageURL=TPConstant.IMAGES_URL+"/"+contentFolder+"/"+picture.getImagePath();

				picture.setDownloadImage(imageURL);
				ChalkPicture.insertChalkPicture(picture).subscribe();
				//DatabaseHelper.getInstance().insertOrReplace(picture.getContentValues(), "chalk_pictures");
			}
		}
		
		//DatabaseHelper.getInstance().closeWritableDb();
		Toast.makeText(this, "Downloaded chalks successfully.", Toast.LENGTH_LONG).show();
		resetFilters(view);
	}
	
	public void backAction(View view){
		finish();
		return;
	}
	
	public void fromDateAction(View view){
		showDialog(FROM_DATE_DIALOG_ID);
	}
	
	public void fromTimeAction(View view){
		showDialog(FROM_TIME_DIALOG_ID);
	}
	
	public void toDateAction(View view){
		showDialog(TO_DATE_DIALOG_ID);
	}
	
	public void toTimeAction(View view){
		showDialog(TO_TIME_DIALOG_ID);
	}
	
	
	@Override
    protected Dialog onCreateDialog(int id) {
		
		final Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		int month = c.get(Calendar.MONTH);
		int year = c.get(Calendar.YEAR);
		int day = c.get(Calendar.DATE);
		
        switch (id) {
            case FROM_DATE_DIALOG_ID:{
            	if (android.os.Build.VERSION.SDK_INT >22) {
            		return new DatePickerDialog(this,android.R.style.Theme_DeviceDefault_Light_NoActionBar,fromDateSetListener,year, month, day);
            	}else
            	return new DatePickerDialog(this,fromDateSetListener,year, month, day);
            }
            
            case FROM_TIME_DIALOG_ID:{
            	if (android.os.Build.VERSION.SDK_INT >22) {
            		return new TimePickerDialog(this,android.R.style.Theme_DeviceDefault_Light_NoActionBar,fromTimeSetListener,hour, minute, false);
            	}else
            	return new TimePickerDialog(this,fromTimeSetListener,hour, minute, false);
            }
            
            case TO_DATE_DIALOG_ID:{
            	if (android.os.Build.VERSION.SDK_INT >22) {
            		return new DatePickerDialog(this,android.R.style.Theme_DeviceDefault_Light_NoActionBar,toDateSetListener,year, month, day);
            	}else
            	return new DatePickerDialog(this,toDateSetListener,year, month, day);
            }
            
            case TO_TIME_DIALOG_ID:{
            	if (android.os.Build.VERSION.SDK_INT >22) {
            		return new TimePickerDialog(this,android.R.style.Theme_DeviceDefault_Light_NoActionBar,toTimeSetListener,hour, minute, false);
            	}else
            	return new TimePickerDialog(this,toTimeSetListener,hour, minute, false);
            }
        }
        
        return null;
    }
	
	
	private String prefixZero(int number){
		if(number<10)
			return "0"+number;
		
		return ""+number;
	}
	
	private DatePickerDialog.OnDateSetListener fromDateSetListener=new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, 
                              int monthOfYear, int dayOfMonth) {
        	fromDateBtn.setText(prefixZero(monthOfYear+1)+"/"+prefixZero(dayOfMonth)+"/"+year);
        }
	};
	
	private TimePickerDialog.OnTimeSetListener fromTimeSetListener=new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        	fromTimeBtn.setText(prefixZero(hourOfDay)+":"+prefixZero(minute));
        }
	};
	
	
	private DatePickerDialog.OnDateSetListener toDateSetListener=new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, 
                              int monthOfYear, int dayOfMonth) {
        	toDateBtn.setText(prefixZero(monthOfYear+1)+"/"+prefixZero(dayOfMonth)+"/"+year);
        }
	};
	
	private TimePickerDialog.OnTimeSetListener toTimeSetListener=new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        	toTimeBtn.setText(prefixZero(hourOfDay)+":"+prefixZero(minute));
        }
	};
	
	
	@Override
	public void handleVoiceInput(String text) {
		if(text==null) return;
		
		Toast.makeText(this, text, Toast.LENGTH_LONG).show();
		if(text.contains("BACK") || text.contains("GO BACK") || text.contains("CLOSE")){
			backAction(null);
		}
	}


	@Override
	public void handleVoiceMode(boolean voiceMode) {
		// TODO Auto-generated method stub
	}


	@Override
	public void handleNetworkStatus(boolean connected,boolean isFastConnection) {
		// TODO Auto-generated method stub
		
	}

}
