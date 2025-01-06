package com.ticketpro.parking.bl;


import java.util.ArrayList;

import com.ticketpro.exception.TPException;

public class UserSettingsBLProcessor extends BLProcessorImpl{
	
	public UserSettingsBLProcessor(){
		setLogger(UserSettingsBLProcessor.class.getName());
	}
	
	public String getVersionNo() throws TPException{
		return proxy.getVersionNo();
	}
		
	public ArrayList<String> getSynchIntervalList(){
		ArrayList<String> list=new ArrayList<String>();
		list.add("12 Hrs");
		list.add("24 Hrs");
		
		return list;
	}
	
	public ArrayList<String> getDRPIntervalList(){
		ArrayList<String> list=new ArrayList<String>();
		list.add("12 Hrs");
		list.add("24 Hrs");

		return list;
	}
	
	
	public ArrayList<String> getCacheExpiryList(){
		ArrayList<String> list=new ArrayList<String>();
		list.add("5 Mins");
		list.add("10 Mins");
		list.add("20 Mins");
		list.add("30 Mins");
		list.add("50 Mins");
		list.add("60 Mins");
		list.add("90 Mins");
		list.add("120 Mins");
		
		return list;
	}
	
	public ArrayList<String> getGPSList(){
		ArrayList<String> list=new ArrayList<String>();
		list.add("On");
		list.add("Off");

		return list;
	}
	
	public ArrayList<String> getDataBackupList(){
		ArrayList<String> list=new ArrayList<String>();
		list.add("On");
		list.add("Off");

		return list;
	}
}
