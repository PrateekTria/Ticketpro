package com.ticketpro.vendors;

import java.util.Comparator;
import java.util.Date;

import org.json.JSONObject;

public class PassportParkingSpace {
	private String id;
	private String number;
	private String activelpn;
	private String zoneId;
	private String zoneName;
	private String zoneNumber;
	private String status;
	private String expiration;
	private String expirationTimeInSecs;
	private Date systemDate;
		
	public PassportParkingSpace(){
		
	}	
	
	public PassportParkingSpace(JSONObject object)  throws Exception{
		this.id=object.getString("id");
		this.number=object.getString("number");
		this.activelpn=object.getString("activelpn");
		this.zoneId=object.getString("zone_id");
		this.zoneName=object.getString("zonename");
		this.zoneNumber=object.getString("zonenumber");
		this.status=object.getString("status");
		this.expiration=object.getString("expiration");
		this.expirationTimeInSecs=object.getString("expirationtimeinsecs");
		this.systemDate=new Date();
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getActivelpn() {
		return activelpn;
	}

	public void setActivelpn(String activelpn) {
		this.activelpn = activelpn;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getExpiration() {
		return expiration;
	}

	public void setExpiration(String expiration) {
		this.expiration = expiration;
	}

	public String getExpirationTimeInSecs() {
		return expirationTimeInSecs;
	}

	public void setExpirationTimeInSecs(String expirationTimeInSecs) {
		this.expirationTimeInSecs = expirationTimeInSecs;
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
	
	public static class ZoneComparator implements Comparator<PassportParkingSpace> {
		@Override
		public int compare(PassportParkingSpace item1, PassportParkingSpace item2) {
			return item1.getZoneName().compareToIgnoreCase(item1.getZoneName());
		}
    }
	
	
	public static class StatusComparator implements Comparator<PassportParkingSpace> {
		@Override
		public int compare(PassportParkingSpace item1, PassportParkingSpace item2) {
			return item1.getStatus().compareToIgnoreCase(item1.getStatus());
		}
    }
	
	public static class ExpireComparator implements Comparator<PassportParkingSpace> {
		@Override
		public int compare(PassportParkingSpace item1, PassportParkingSpace item2) {
			return Integer.valueOf(item1.expirationTimeInSecs).compareTo(Integer.valueOf(item2.expirationTimeInSecs));
		}
    }

}
