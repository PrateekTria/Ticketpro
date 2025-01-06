package com.ticketpro.util;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ticketpro.parking.R;

public class ListArrayAdapter extends ArrayAdapter<String>{

	// used to keep selected position in ListView
	private int selectedPos = -1;	// init value for not-selected

	public ListArrayAdapter(Context context, int textViewResourceId, List<String> objects) {
		super(context, textViewResourceId, objects);
	}

	public void setSelectedPosition(int pos){
		selectedPos = pos;
		notifyDataSetChanged();
	}

	public int getSelectedPosition(){
		return selectedPos;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	    View v = convertView;

	    // only inflate the view if it's null
	    if (v == null) {
	        LayoutInflater vi=(LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        v = vi.inflate(R.layout.expiration_entry_item, null);
	    }

	    // get text view
        TextView label = (TextView)v.findViewById(R.id.expiration_entry_textview);

        // change the row color based on selected state
        if(selectedPos == position){
        	label.setBackgroundColor(Color.parseColor("#f3ae1b"));
        }else{
        	label.setBackgroundColor(Color.parseColor("#5e606c"));
        }

        label.setText(this.getItem(position).toString());
        return(v);
	}
}
