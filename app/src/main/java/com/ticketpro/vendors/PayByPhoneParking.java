package com.ticketpro.vendors;

import java.util.Comparator;
import java.util.Date;

public class PayByPhoneParking {
	
	private String plate;
	private String state;
	private String stallNumber;
	private String vendorStallNumber;
	private Date startDateTime;
	private Date endDateTime;
	private Date systemDate;
	
	public PayByPhoneParking(){

	}
	
	public String getPlate() {
		return plate;
	}

	public void setPlate(String plate) {
		this.plate = plate;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getStallNumber() {
		return stallNumber;
	}

	public void setStallNumber(String stallNumber) {
		this.stallNumber = stallNumber;
	}

	public String getVendorStallNumber() {
		return vendorStallNumber;
	}

	public void setVendorStallNumber(String vendorStallNumber) {
		this.vendorStallNumber = vendorStallNumber;
	}

	public Date getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(Date startDateTime) {
		this.startDateTime = startDateTime;
	}

	public Date getEndDateTime() {
		return endDateTime;
	}

	public void setEndDateTime(Date endDateTime) {
		this.endDateTime = endDateTime;
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

	public ParkingExpireInfo getExpireInfo(){
		ParkingExpireInfo parkingExpireInfo=new ParkingExpireInfo();
		
		String expireStr="";
		long diffMinutes,diffHours,diffDays;
    	long expiredDiff = new Date().getTime() - this.getEndDateTime().getTime();
		if(expiredDiff > 0){
			diffMinutes = expiredDiff / (60 * 1000) % 60;
			diffHours = expiredDiff / (60 * 60 * 1000) % 24;
			diffDays = expiredDiff / (24 * 60 * 60 * 1000);
			if(diffDays>=1){
				expireStr=diffDays+" d "+diffHours +" h ago";
			
			}else if(diffHours>=1){
				expireStr=diffHours+" h "+diffMinutes +" m ago";
			
			}else{
				expireStr=diffMinutes +" m ago";
			}
			
			parkingExpireInfo.setExpired(true);

		}else{
			expiredDiff=Math.abs(expiredDiff);
			diffMinutes = expiredDiff / (60 * 1000) % 60;
			diffHours = expiredDiff / (60 * 60 * 1000) % 24;
			diffDays = expiredDiff / (24 * 60 * 60 * 1000);
			
			if(diffDays>=1){
				expireStr=/*"in "+*/diffDays+" d "+diffHours+" h";
			
			}else if(diffHours>=1){
				expireStr=/*"in "+*/diffHours+" h "+diffMinutes+" m";
			
			}else{
				expireStr=/*"in "+*/diffMinutes +" m";
			}
			
			parkingExpireInfo.setExpired(false);
		}
		
		parkingExpireInfo.setExpireMsg(expireStr);
		parkingExpireInfo.setDiffDays((int)diffDays);
		parkingExpireInfo.setDiffHrs((int)diffHours);
		parkingExpireInfo.setDiffMinutes((int)diffMinutes);
		parkingExpireInfo.setDiffSeconds((int)diffMinutes * 60);
		
		return parkingExpireInfo;
	}

	public static class ZoneComparator implements Comparator<PayByPhoneParking> {
		@Override
		public int compare(PayByPhoneParking item1, PayByPhoneParking item2) {
			return item1.getStallNumber().compareToIgnoreCase(item2.getStallNumber());
		}
    }
	
	public static class ExpireComparator implements Comparator<PayByPhoneParking> {
		@Override
		public int compare(PayByPhoneParking item1, PayByPhoneParking item2) {
			return item1.getEndDateTime().compareTo(item2.getEndDateTime());
		}
    }
	
	public static class PlateComparator implements Comparator<PayByPhoneParking> {
		@Override
		public int compare(PayByPhoneParking item1, PayByPhoneParking item2) {
			return item1.getPlate().compareToIgnoreCase(item2.getPlate());
		}
    }
}
