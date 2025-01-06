package com.ticketpro.parking.activity;

//import com.crashlytics.android.Crashlytics;
import com.ticketpro.model.Ticket;
import com.ticketpro.parking.R;
import com.ticketpro.util.TPUtility;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;

//import io.fabric.sdk.android.Fabric;

public class PlatePriorTicketFragment extends Fragment {

	private int mCurrentPage;
	private int allPage;
	private String message;
	private String ticketType = "";

	private ImageButton print_copy;
	private long citationNumber = 0;
	private PlateLookupResultPriorTickets  lookupActivity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle data = getArguments();
		//Fabric.with(getActivity(), new Crashlytics());
		mCurrentPage = data.getInt("current_page", 0);
		allPage = data.getInt("total_page", 0);
		message = data.getString("message");
		
		lookupActivity = TPApplication.getInstance().getPlateLookupActivity();

		if ((!"permit".equalsIgnoreCase(lookupActivity.getDisplayType()))) {
			ticketType = data.getString("ticketType");
			citationNumber = data.getLong("CitationNumber");
		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.ticket_history, container, false);
		WebView wv = (WebView) v.findViewById(R.id.wv);
		TextView counter_label = (TextView) v.findViewById(R.id.counter_label);
		TextView text = (TextView) v.findViewById(R.id.text);
		print_copy = (ImageButton) v.findViewById(R.id.print_copy);
		
		if("permit".equalsIgnoreCase(lookupActivity.getDisplayType())){
			print_copy.setVisibility(View.GONE);
			text.setVisibility(View.GONE);
		}else{
			text.setText(ticketType);

			if (ticketType.matches("W")) {
				text.setBackgroundResource(R.drawable.color_green_1);
			} 
			else if (ticketType.matches("V")) {
				text.setBackgroundResource(R.drawable.color_red_1);
				print_copy.setVisibility(View.VISIBLE);
			} 
			else if (ticketType.matches("D")) {
				text.setBackgroundResource(R.drawable.color_orange_1);
			} 
			else {
				text.setTextColor(getResources().getColor(R.color.transparent));
			}
	
			print_copy.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					printCopy();
				}
			});
		}
		
		String countText = mCurrentPage + 1 + " of " + allPage;
		counter_label.setText(countText);
		wv.loadData(message, "text/html", "utf-8");

		wv.setBackgroundColor(android.graphics.Color.WHITE);
		wv.getSettings().setDefaultTextEncodingName("utf-8");

		return v;
	}

	private void printCopy() {
		try{
			Ticket activeTicket = lookupActivity.getTicketList().get(mCurrentPage);

			Intent i = new Intent();
			i.setClass(getActivity(), TicketViewActivity.class);
			i.putExtra("CitationNumber", citationNumber);
			i.putExtra("TicketIndex", 0);  
			i.putExtra("activeTicket", activeTicket);
			startActivity(i);
		}catch(Exception e){
			Log.e("PlateLookup", TPUtility.getPrintStackTrace(e));
		}
	}
}
