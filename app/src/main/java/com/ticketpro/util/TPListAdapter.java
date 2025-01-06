package com.ticketpro.util;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ticketpro.parking.R;

public class TPListAdapter extends ArrayAdapter<String>{
	private Context context;
	private ArrayList<String> items;
	
	public TPListAdapter(Context context, int textViewResourceId, ArrayList<String> items){
		super(context, textViewResourceId, items);
		
		this.context=context;
		this.items=items;
    }

    @SuppressLint("ViewHolder")
	@Override
    public View getView(final int position, View convertView, ViewGroup parent){
    	LayoutInflater layoutInflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
    	View itemView = layoutInflater.inflate(R.layout.list_item, parent, false);

    	TextView titleView = (TextView) itemView.findViewById(R.id.title_textview);
    	titleView.setText(this.items.get(position));
    	
    	ImageView deleteBtn = (ImageView) itemView.findViewById(R.id.delete_imageview);
		deleteBtn.setClickable(true);
		deleteBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
			    builder.setTitle("Delete Confirmation")
			    .setMessage("Are you sure you want to Delete?")
			    .setCancelable(true)
			    .setNegativeButton("No", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
					}
			 	})
			 	.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						items.remove(position);
						notifyDataSetChanged();
					}
			 	});
			    
			    AlertDialog alert = builder.create();
			    alert.show();
			}
		});
		
        return itemView;
    }
 } 