package com.ticketpro.model;

import java.util.ArrayList;
import java.util.Date;

public class MobileNowZoneInfo {

	private String zone;
	private String zoneType;
	private Date sysDate;
	private int gracePeriod;
	private String message;
	private boolean isValid;
	private ArrayList<MobileNowZoneItem> zoneItems=new ArrayList<MobileNowZoneItem>();
	
	public String getZone() {
		return zone;
	}
	public void setZone(String zone) {
		this.zone = zone;
	}
	public String getZoneType() {
		return zoneType;
	}
	public void setZoneType(String zoneType) {
		this.zoneType = zoneType;
	}
	public Date getSysDate() {
		return sysDate;
	}
	public void setSysDate(Date sysDate) {
		this.sysDate = sysDate;
	}
	public int getGracePeriod() {
		return gracePeriod;
	}
	public void setGracePeriod(int gracePeriod) {
		this.gracePeriod = gracePeriod;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public boolean isValid() {
		return isValid;
	}
	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}
	public ArrayList<MobileNowZoneItem> getZoneItems() {
		return zoneItems;
	}
	public void setZoneItems(ArrayList<MobileNowZoneItem> zoneItems) {
		this.zoneItems = zoneItems;
	}
	
	
	
}
