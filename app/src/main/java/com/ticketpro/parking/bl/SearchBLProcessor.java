package com.ticketpro.parking.bl;


import java.util.ArrayList;

import com.ticketpro.exception.TPException;

public class SearchBLProcessor extends BLProcessorImpl{
	
	
	public SearchBLProcessor(){
		setLogger(SearchBLProcessor.class.getName());
	}
	
	public String getVersionNo() throws TPException{
		return proxy.getVersionNo();
	}
		
	public ArrayList<String> getSearchByList(){
		ArrayList<String> list=new ArrayList<String>();
		list.add("Search By Meter");
		list.add("Search By Plate");
		return list;
	}

}
