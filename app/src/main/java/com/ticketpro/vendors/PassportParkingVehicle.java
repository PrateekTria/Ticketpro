package com.ticketpro.vendors;

import java.util.Comparator;
import java.util.Date;

import org.json.JSONObject;

public class PassportParkingVehicle {

	private String entryTime;
	private String exitTime;
	private String vehicleId;
	private String lpn;
	private String stateCode;
	private String rateName;
	private String zoneName;
	private String zoneNumber;
	private String licensePlateNumber;
	private String expirationTimeInSecs;
	private Date systemDate;
	
	public PassportParkingVehicle(){
		
	}
	
	public PassportParkingVehicle(JSONObject object)  throws Exception{
		this.entryTime=object.getString("entrytime");
		this.exitTime=object.getString("exittime");
		this.vehicleId=object.getString("vehicleid");
		this.zoneName=object.getString("zonename");
		this.zoneNumber=object.getString("zonenumber");
		this.lpn=object.getString("lpn");
		this.rateName=object.getString("ratename");
		this.stateCode=object.getString("stateabbreviation");
		this.licensePlateNumber=object.getString("licenseplatenumber");
		this.expirationTimeInSecs=object.getString("expirationtimeinsecs");
		this.systemDate=new Date();
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

	public String getEntryTime() {
		return entryTime;
	}

	public void setEntryTime(String entryTime) {
		this.entryTime = entryTime;
	}

	public String getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(String vehicleId) {
		this.vehicleId = vehicleId;
	}

	public String getLicensePlateNumber() {
		return licensePlateNumber;
	}

	public void setLicensePlateNumber(String licensePlateNumber) {
		this.licensePlateNumber = licensePlateNumber;
	}

	public String getExpirationTimeInSecs() {
		return expirationTimeInSecs;
	}

	public void setExpirationTimeInSecs(String expirationTimeInSecs) {
		this.expirationTimeInSecs = expirationTimeInSecs;
	}
	
	public String getExitTime() {
		return exitTime;
	}

	public void setExitTime(String exitTime) {
		this.exitTime = exitTime;
	}

	public String getLpn() {
		return lpn;
	}

	public void setLpn(String lpn) {
		this.lpn = lpn;
	}

	public String getStateCode() {
		return stateCode;
	}

	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

	public String getRateName() {
		return rateName;
	}

	public void setRateName(String rateName) {
		this.rateName = rateName;
	}

	public Date getSystemDate() {
		if(systemDate==null){
			systemDate=new Date();
		}
		
		return systemDate;
	}

	public void setSystemDate(Date systemDate) {
		this.systemDate = systemDate;
	}
	
	public long getSystemTimeDiffInSec(){
		if(systemDate==null){
			return 0;
		}
		
		return ((new Date().getTime() - getSystemDate().getTime()) / 1000);
	}
	
	public boolean hasExpired(){
		long expireInSec = Long.parseLong(getExpirationTimeInSecs());
		return getSystemTimeDiffInSec() >= expireInSec;
	}

	public static class ZoneComparator implements Comparator<PassportParkingVehicle> {
		@Override
		public int compare(PassportParkingVehicle item1, PassportParkingVehicle item2) {
			return item1.getZoneName().compareToIgnoreCase(item1.getZoneName());
		}
    }
	
	public static class ExpireComparator implements Comparator<PassportParkingVehicle> {
		@Override
		public int compare(PassportParkingVehicle item1, PassportParkingVehicle item2) {
			return Integer.valueOf(item1.expirationTimeInSecs).compareTo(Integer.valueOf(item2.expirationTimeInSecs));
		}
    }
	
	public static class PlateComparator implements Comparator<PassportParkingVehicle> {
		@Override
		public int compare(PassportParkingVehicle item1, PassportParkingVehicle item2) {
			return item1.getLicensePlateNumber().compareToIgnoreCase(item1.getLicensePlateNumber());
		}
    }

}
