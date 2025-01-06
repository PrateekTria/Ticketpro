package com.ticketpro.vendors;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.ticketpro.util.StringUtil;

public class PassportParkingParser{
	
	public static ArrayList<PassportParkingZoneItem> getZones(String responseJSON, String zoneId) throws Exception{
		ArrayList<PassportParkingZoneItem> zones=new ArrayList<PassportParkingZoneItem>();
		JSONObject response=new JSONObject(responseJSON);
		
		if(!response.isNull("data")){
			JSONArray data=response.getJSONArray("data");
			for(int i=0;i<data.length();i++){
				PassportParkingZoneItem parkingZoneItem=new PassportParkingZoneItem(data.getJSONObject(i));
				if(!StringUtil.isEmpty(zoneId) && zoneId.equalsIgnoreCase(parkingZoneItem.getZoneId())){
					
					if(parkingZoneItem.getChildren()!=null && parkingZoneItem.getChildren().size() > 0){
						return parkingZoneItem.getChildren();
					}
				}
				
				zones.add(parkingZoneItem);
			}
		}
		
		return zones;
	}
	
	public static PassportParkingZoneInfo getZoneInfo(String responseJSON) throws Exception{
		PassportParkingZoneInfo passportParkingZoneInfo=new PassportParkingZoneInfo();
		JSONObject response=new JSONObject(responseJSON);
		if(!response.isNull("data")){
			JSONArray data=response.getJSONArray("data");
			if(data!=null && data.length() > 0){
				passportParkingZoneInfo=new PassportParkingZoneInfo(data.getJSONObject(0));
			}
		}
		
		return passportParkingZoneInfo;
	}
	
	public static PassportParkingVehicle getVehicleInfo(String responseJSON){
		PassportParkingZoneInfo passportParkingZoneInfo;
		try {
			passportParkingZoneInfo = getZoneInfo(responseJSON);
			if(passportParkingZoneInfo.getVehicles().size() > 0){
				return passportParkingZoneInfo.getVehicles().get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public static PassportParkingVehicle getVehicleInfoByPlateResponse(String responseJSON){
		PassportParkingVehicle vehicleInfo=null;
		JSONObject response;
		try {
			response = new JSONObject(responseJSON);
			if(!response.isNull("data")){
				JSONArray data=response.getJSONArray("data");
				if(data!=null && data.length() > 0){
					vehicleInfo=new PassportParkingVehicle(data.getJSONObject(0));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return vehicleInfo;
	}
	
	public static PassportParkingSpace getSpaceInfo(String responseJSON){
		PassportParkingZoneInfo passportParkingZoneInfo;
		try {
			passportParkingZoneInfo = getZoneInfo(responseJSON);
			if(passportParkingZoneInfo.getSpaces().size() > 0){
				return passportParkingZoneInfo.getSpaces().get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
}