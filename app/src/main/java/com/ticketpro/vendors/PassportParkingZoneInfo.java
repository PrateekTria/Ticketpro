package com.ticketpro.vendors;

import java.util.ArrayList;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.ticketpro.model.Feature;

public class PassportParkingZoneInfo {

	private String zoneId;
	private String zoneName;
	private String zoneNumber;
	private String zoneTypeId;
	private String zonetypeName;
	private String operatorId;
	private String enableCashCheckin;
	private String forceLogOfftime;
	private String checkinEnabled;
	private ArrayList<PassportParkingSpace> passportParkingSpaces=new ArrayList<PassportParkingSpace>();
	private ArrayList<PassportParkingVehicle> passportParkingVehicles=new ArrayList<PassportParkingVehicle>();
	
	private static Map<String, ArrayList<PassportParkingSpace>> previousParkingSpaces;
	private static Map<String, ArrayList<PassportParkingVehicle>> previousParkingVehicles;
	private static int expiredResultCacheTime=10;
	
	public PassportParkingZoneInfo(){
		
	}
	
	public PassportParkingZoneInfo(JSONObject object)  throws Exception{
		this.zoneId=object.getString("zoneid");
		this.zoneName=object.getString("zonename");
		this.zoneNumber=object.getString("zonenumber");
		this.operatorId=object.getString("operator_id");
		this.enableCashCheckin=object.getString("enablecashcheckin");
		this.forceLogOfftime=object.getString("forcelogofftime");
		this.checkinEnabled=object.getString("checkinenabled");
		
		if(object.has("zonetype")){
			JSONObject zoneType=object.getJSONObject("zonetype");
			this.zonetypeName=zoneType.getString("id");
			this.zoneTypeId=zoneType.getString("name");
		}
		
		if(object.has("locations_spaces")){
			JSONArray spaces=object.getJSONArray("locations_spaces");
			for(int i=0;i<spaces.length();i++){
				PassportParkingSpace parkingSpace=new PassportParkingSpace(spaces.getJSONObject(i));
				this.passportParkingSpaces.add(parkingSpace);
			}
		}
		
		if(object.has("location_lpn")){
			JSONArray vehicles=object.getJSONArray("location_lpn");
			for(int i=0;i<vehicles.length();i++){
				PassportParkingVehicle parkingVehicles=new PassportParkingVehicle(vehicles.getJSONObject(i));
				this.passportParkingVehicles.add(parkingVehicles);
			}
		}
		
		String expiredResultDuration=Feature.getFeatureValue(Feature.EXPIRED_RESULT_CACHE_TIME);
		if(expiredResultDuration!=null && !expiredResultDuration.isEmpty()){
			try{
				expiredResultCacheTime=Integer.parseInt(expiredResultDuration);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		//Get Previous result
		if(previousParkingSpaces!=null){
		for(PassportParkingSpace parkingSpace: previousParkingSpaces.get(this.zoneId)){
			if(!hasParkingSpace(parkingSpace)){
				if(parkingSpace.getSystemTimeDiffInSec() < (expiredResultCacheTime * 60)){
					parkingSpace.setExpirationTimeInSecs("0");
					this.passportParkingSpaces.add(parkingSpace);
				}
			}
		}
		previousParkingSpaces.put(this.zoneId, passportParkingSpaces);
		}else{
			 
		}
		
		//Get Previous result
		if(previousParkingVehicles!=null){
		for(PassportParkingVehicle parkingVehicle: previousParkingVehicles.get(this.zoneId)){
			if(!hasParkingVehicle(parkingVehicle)){
				if(parkingVehicle.getSystemTimeDiffInSec() < (expiredResultCacheTime * 60)){
					parkingVehicle.setExpirationTimeInSecs("0");
					this.passportParkingVehicles.add(parkingVehicle);
				}
			}
		}
		previousParkingVehicles.put(this.zoneId, passportParkingVehicles);
		}
		else{
			
		}
		
	}
	
	private boolean hasParkingSpace(PassportParkingSpace newParkingSpace){
		boolean result=false;
		for(PassportParkingSpace parkingSpace: this.passportParkingSpaces){
			if(parkingSpace.getId().equalsIgnoreCase(newParkingSpace.getId())){
				result=true;
				break;
			}
		}
		
		return result;
	}
	
	private boolean hasParkingVehicle(PassportParkingVehicle newParkingVehicle){
		boolean result=false;
		for(PassportParkingVehicle parkingVehicle: this.passportParkingVehicles){
			if(parkingVehicle.getLicensePlateNumber().equalsIgnoreCase(newParkingVehicle.getLicensePlateNumber())){
				result=true;
				break;
			}
		}
		
		return result;
	}

	public String getZoneId() {
		return zoneId;
	}

	public void setZoneId(String zoneId) {
		this.zoneId = zoneId;
	}

	public String getZoneName() {
		return zoneName;
	}

	public void setZoneName(String zoneName) {
		this.zoneName = zoneName;
	}

	public String getZoneNumber() {
		return zoneNumber;
	}

	public void setZoneNumber(String zoneNumber) {
		this.zoneNumber = zoneNumber;
	}

	public String getZoneTypeId() {
		return zoneTypeId;
	}

	public void setZoneTypeId(String zoneTypeId) {
		this.zoneTypeId = zoneTypeId;
	}

	public String getZonetypeName() {
		return zonetypeName;
	}

	public void setZonetypeName(String zonetypeName) {
		this.zonetypeName = zonetypeName;
	}

	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	public ArrayList<PassportParkingSpace> getSpaces() {
		return passportParkingSpaces;
	}

	public void setSpaces(ArrayList<PassportParkingSpace> passportParkingSpaces) {
		this.passportParkingSpaces = passportParkingSpaces;
	}

	public ArrayList<PassportParkingVehicle> getVehicles() {
		return passportParkingVehicles;
	}

	public void setVehicles(ArrayList<PassportParkingVehicle> passportParkingVehicles) {
		this.passportParkingVehicles = passportParkingVehicles;
	}

	public String getEnableCashCheckin() {
		return enableCashCheckin;
	}

	public void setEnableCashCheckin(String enableCashCheckin) {
		this.enableCashCheckin = enableCashCheckin;
	}

	public String getForceLogOfftime() {
		return forceLogOfftime;
	}

	public void setForceLogOfftime(String forceLogOfftime) {
		this.forceLogOfftime = forceLogOfftime;
	}

	public String getCheckinEnabled() {
		return checkinEnabled;
	}

	public void setCheckinEnabled(String checkinEnabled) {
		this.checkinEnabled = checkinEnabled;
	}

}
