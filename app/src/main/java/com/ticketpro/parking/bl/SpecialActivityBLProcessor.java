package com.ticketpro.parking.bl;

import java.util.ArrayList;

import com.ticketpro.exception.TPException;
import com.ticketpro.model.SpecialActivityHandler;
import com.ticketpro.model.SpecialActivityReport;

import org.json.JSONArray;

public class SpecialActivityBLProcessor extends BLProcessorImpl{

	public SpecialActivityBLProcessor(){
		setLogger(SpecialActivityBLProcessor.class.getName());
	}
	
	public ArrayList<String> getSpecialActivities() throws TPException{
		return proxy.getSpecialActivitiesTitles();
	}
	
	public ArrayList<String> getSpecialDispositions() throws TPException{
		return proxy.getSpecialDispositionsTitles();
	}
	public ArrayList<SpecialActivityReport> getSpecialActivityList(int custId,String date) throws TPException{
		return proxy.getActivityReport(custId,date);
	}
	// This method is added by mohit 27/03/2023
	public void getActivityReport1(int custId, String date, SpecialActivityHandler specialActivityHandler) throws TPException{
		proxy.getActivityReport1(custId,date,specialActivityHandler);
	}
	//End

	public boolean updateActivity(JSONArray activityReports) throws Exception {
		return proxy.updateActivity(activityReports);
	}

	public boolean updateActivityPicture(JSONArray activityPicture, ArrayList<String> image) throws Exception {
		return proxy.updateActivityPicture(activityPicture,image);
	}

}
