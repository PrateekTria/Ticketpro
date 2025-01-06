package com.ticketpro.model;

import java.util.Comparator;
import java.util.Date;

public class MobileNowZoneItem {

	private String item;
	private String zoneType;
	private String responseType;
	private Date startTime;
	private Date endTime;
	
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	public static class ItemComparator implements Comparator<MobileNowZoneItem> {
		@Override
		public int compare(MobileNowZoneItem item1, MobileNowZoneItem item2) {
			return item1.getItem().compareToIgnoreCase(item1.getItem());
		}
    }
	
	public static class EndDateComparator implements Comparator<MobileNowZoneItem> {
		@Override
		public int compare(MobileNowZoneItem item1, MobileNowZoneItem item2) {
			return item1.getEndTime().compareTo(item2.getEndTime());
		}
    }
	
	public static class	StartDateComparator implements Comparator<MobileNowZoneItem> {
		@Override
		public int compare(MobileNowZoneItem item1, MobileNowZoneItem item2) {
			return item1.getStartTime().compareTo(item2.getStartTime());
		}
    }
	
	public static class ZoneTypeComparator implements Comparator<MobileNowZoneItem> {
		@Override
		public int compare(MobileNowZoneItem item1, MobileNowZoneItem item2) {
			return item1.getZoneType().compareToIgnoreCase(item2.getZoneType());
		}
    }
	
	public static class ResponseTypeComparator implements Comparator<MobileNowZoneItem> {
		@Override
		public int compare(MobileNowZoneItem item1, MobileNowZoneItem item2) {
			return item1.getResponseType().compareToIgnoreCase(item2.getResponseType());
		}
    }
	
	public String getZoneType() {
		return zoneType;
	}
	public void setZoneType(String zoneType) {
		this.zoneType = zoneType;
	}
	public String getResponseType() {
		return responseType;
	}
	public void setResponseType(String responseType) {
		this.responseType = responseType;
	}

}
