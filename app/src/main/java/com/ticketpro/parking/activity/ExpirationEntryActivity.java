package com.ticketpro.parking.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ticketpro.parking.R;
import com.ticketpro.util.ListArrayAdapter;
import com.ticketpro.util.TPUtility;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

public class ExpirationEntryActivity extends BaseActivityImpl{

	private ListView monthView;
	private ListView yearView;
	private List<String> years;
	private boolean isMonthSelected;
	private boolean isYearSelected;
	private String selectedMonth;
	private String selectedYear;
	
	/**
	 * Entry point of the Activity
	 */
	@Override
    public void onCreate(Bundle savedInstanceState) {
      try{ 
		super.onCreate(savedInstanceState);
        setContentView(R.layout.expiration_entry);
        setLogger(ExpirationEntryActivity.class.getName());
        setActiveScreen(this);
		
        monthView=(ListView)findViewById(R.id.exp_month_listivew);
        yearView=(ListView)findViewById(R.id.exp_year_listview);
        
        Intent intent=getIntent();
        TextView textview=(TextView)findViewById(R.id.exp_date_textview);
    	String expDate=intent.getStringExtra("EXP");
        if(expDate!=null && !expDate.equals("")){
        	textview.setText(expDate);
        	try{
	        	if(expDate.contains("/")){
	        		String[] dates=expDate.split("/");
	        		selectedMonth=dates[0];
	        		isMonthSelected=true;
	        		
	        		if(dates.length > 1){
	        			selectedYear=dates[1];
	        			isYearSelected=true;
	        		}
	        	}else{
	        		if(TPUtility.isNumeric(expDate)){
	        			selectedYear=expDate;
	        			isYearSelected=true;
	        		}else{
	        			selectedMonth=expDate;
	        			isMonthSelected=true;
	        		}
	        	}
	        }catch (Exception e) {}
        }
        
        bindDataAtLoadingTime();
        final String []months={"JAN","FEB","MAR","APR","MAY","JUN","JUL","AUG","SEP","OCT","NOV","DEC"};
		List<String> monthList=new ArrayList();
        
        for(String month:months){
        	monthList.add(month);
        }
        
		final ListArrayAdapter adapter1=new ListArrayAdapter(getApplicationContext(), 0, monthList);
		monthView.setAdapter(adapter1);
		monthView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		monthView.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,int pos, long arg3) {
				adapter1.setSelectedPosition(pos);
				
				String selectedItem = (String)adapterView.getItemAtPosition(pos);
				selectedMonth=selectedItem;
				isMonthSelected=true;
				updateExpiration();
				
				if(isYearSelected) performAccecptAction(); 
			}
		});
		
		Date dt=new Date();
 		int startYear=dt.getYear();
 		startYear=(startYear+1900) - 30;
 		int endYear=startYear+60;
 		
 		years=new ArrayList<String>();
 		int index=0;
 		for(int y=startYear;y<=endYear;y++){
 			years.add(""+y);
 		}
 		
 		final ListArrayAdapter adapter2=new ListArrayAdapter(getApplicationContext(), 0, years);
		yearView.setAdapter(adapter2);
		yearView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		yearView.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,int pos, long arg3) {
				adapter2.setSelectedPosition(pos);
				
				String selectedItem = (String)adapterView.getItemAtPosition(pos);
				selectedYear=selectedItem;
				isYearSelected=true;
				updateExpiration();
				
				if(isMonthSelected)	performAccecptAction();
				
			}
		});
		
		yearView.setSelection(30);
		
      }catch(Exception e){
			log.error(TPUtility.getPrintStackTrace(e)); 
		}
	}    
	
	public void bindDataAtLoadingTime(){
		//Initialize Business Logic Handler
	}
	
	/*
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		
	}
	
	public void clearAction(View v){
		TextView textview=(TextView)findViewById(R.id.exp_date_textview);
    	textview.setText("");
    	isMonthSelected=false;
    	isYearSelected=false;
    	selectedMonth="";
    	selectedYear="";
    	
    	return;
	}
	
	public void cancelAction(View v){
		finish();
	}
	
	public void acceptAction(View v){
		performAccecptAction();
	}
	
	
	private void updateExpiration(){
		TextView expDateText=(TextView)findViewById(R.id.exp_date_textview);
		
		if(isMonthSelected && isYearSelected)
			expDateText.setText(selectedMonth+"/"+selectedYear);
		
		else if(isMonthSelected)
			expDateText.setText(selectedMonth);
		
		else if(isYearSelected)
			expDateText.setText(selectedYear);
	}
	
	private void performAccecptAction(){
		try{
			TextView expDateText=(TextView)findViewById(R.id.exp_date_textview);
			String expString=expDateText.getText().toString();
			
	    	Intent data=new Intent();
	    	data.putExtra("EXP", expString);
	    	if(getParent() == null) {
	    	    setResult(Activity.RESULT_OK, data);
	    	}else{
	    	    getParent().setResult(Activity.RESULT_OK, data);
	    	}
	    	
	    	finish();
	    }catch(Exception e){
			log.error(TPUtility.getPrintStackTrace(e)); 
		}
	}
	
	
	class SelectableAdapter extends ArrayAdapter<String>{

	    Context context; 
	    int layoutResourceId;    
	    String data[] = null;
	    
	    public SelectableAdapter(Context context, int layoutResourceId, String[] data) {
	        super(context, layoutResourceId, data);
	        this.layoutResourceId = layoutResourceId;
	        this.context = context;
	        this.data = data;
	    }

	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	        View row = convertView;
	        ViewHolder holder = null;
	        if(row == null){
	            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
	            row = inflater.inflate(layoutResourceId, parent, false);
	            holder = new ViewHolder();
	            holder.textView= (TextView)row.findViewById(R.id.expiration_entry_textview);
	            row.setTag(holder);
	        }
	        else{
	            holder = (ViewHolder)row.getTag();
	        }
	        
	        String text=data[position];
	        holder.textView.setText(text);
	        
	        return row;
	    }
	}
	
	
	public static class ViewHolder{
	    public TextView textView;
	}


	@Override
	public void handleVoiceInput(String text) {
		if(text==null) return;
		
		Toast.makeText(this, text, Toast.LENGTH_LONG).show();
		if(text.contains("BACK") || text.contains("GO BACK") || text.contains("CLOSE")){
			finish();
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
