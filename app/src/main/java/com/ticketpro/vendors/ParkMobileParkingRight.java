package com.ticketpro.vendors;

import java.util.Comparator;
import java.util.Date;

import org.json.JSONObject;

import com.ticketpro.util.DateUtil;

public class ParkMobileParkingRight {

	private long parkingRightId;
	private String signageZoneCode;
	private String internalZoneCode;
	private long supplierId;
	private String lpn;
	private String lpnState;
	private Date startDateLocal;
	private Date endDateLocal;
	private String productDescription;
	private String spaceNumber;
	private long spaceNumberLong;
	private String timeZone;
	private String permit;
	private String customField1;
	private Date modifiedDate;
	private int payedMinutes;
	private int purchaseAmount;
	private int productTypeId;
	private Date systemDate;
	private Date creationDate;

	public ParkMobileParkingRight() {
	}

	public ParkMobileParkingRight(JSONObject object)  throws Exception{
		this.parkingRightId=object.getLong("parkingRightId");
		this.supplierId=object.getLong("supplierId");
		this.signageZoneCode=object.getString("signageZoneCode");
		this.internalZoneCode=object.getString("internalZoneCode");
		this.lpn=object.getString("lpn");
		this.lpnState=object.getString("lpnState");
		this.startDateLocal=DateUtil.getDateFromUTCString(object.getString("startDateLocal"));
		this.endDateLocal=DateUtil.getDateFromUTCString(object.getString("endDateLocal"));
		this.productDescription=object.getString("productDescription");
		this.spaceNumber=object.getString("spaceNumber");
		this.timeZone=object.getString("timeZone");
		this.permit=object.getString("permit");
		this.customField1=object.getString("customField1");
		this.modifiedDate=DateUtil.getDateFromUTCString(object.getString("modifiedDate"));
		this.payedMinutes=object.getInt("payedMinutes");
		this.purchaseAmount=object.getInt("purchaseAmount");
		this.productTypeId=object.getInt("productTypeId");
		this.creationDate=new Date();
		
		if(this.spaceNumber!=null){
			try{
				this.spaceNumberLong=Long.parseLong(this.spaceNumber);
			}catch(NumberFormatException e){
				e.printStackTrace();
			}
		}
	}

	public String getCustomField1() {
		return customField1;
	}

	public void setCustomField1(String customField1) {
		this.customField1 = customField1;
	}

	public long getParkingRightId() {
		return parkingRightId;
	}

	public void setParkingRightId(long parkingRightId) {
		this.parkingRightId = parkingRightId;
	}

	public String getSignageZoneCode() {
		return signageZoneCode;
	}

	public void setSignageZoneCode(String signageZoneCode) {
		this.signageZoneCode = signageZoneCode;
	}

	public String getInternalZoneCode() {
		return internalZoneCode;
	}

	public void setInternalZoneCode(String internalZoneCode) {
		this.internalZoneCode = internalZoneCode;
	}

	public long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(long supplierId) {
		this.supplierId = supplierId;
	}

	public String getLpn() {
		return lpn;
	}

	public void setLpn(String lpn) {
		this.lpn = lpn;
	}

	public String getLpnState() {
		return lpnState;
	}

	public void setLpnState(String lpnState) {
		this.lpnState = lpnState;
	}

	public Date getStartDateLocal() {
		return startDateLocal;
	}

	public void setStartDateLocal(Date startDateLocal) {
		this.startDateLocal = startDateLocal;
	}

	public Date getEndDateLocal() {
		return endDateLocal;
	}

	public void setEndDateLocal(Date endDateLocal) {
		this.endDateLocal = endDateLocal;
	}

	public String getProductDescription() {
		return productDescription;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	public String getSpaceNumber() {
		return spaceNumber;
	}

	public void setSpaceNumber(String spaceNumber) {
		this.spaceNumber = spaceNumber;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public String getPermit() {
		return permit;
	}

	public void setPermit(String permit) {
		this.permit = permit;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public int getPayedMinutes() {
		return payedMinutes;
	}

	public void setPayedMinutes(int payedMinutes) {
		this.payedMinutes = payedMinutes;
	}

	public int getPurchaseAmount() {
		return purchaseAmount;
	}

	public void setPurchaseAmount(int purchaseAmount) {
		this.purchaseAmount = purchaseAmount;
	}

	public int getProductTypeId() {
		return productTypeId;
	}

	public void setProductTypeId(int productTypeId) {
		this.productTypeId = productTypeId;
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
	
	public long getSpaceNumberLong() {
		return spaceNumberLong;
	}

	public void setSpaceNumberLong(long spaceNumberLong) {
		this.spaceNumberLong = spaceNumberLong;
	}
	
	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	
	
	public long getCreationDateDiffInSec(){
		if(creationDate==null){
			return 0;
		}
		
		return ((new Date().getTime() - creationDate.getTime()) / 1000);
	}

	public ParkingExpireInfo getExpireInfo(){
		ParkingExpireInfo parkingExpireInfo=new ParkingExpireInfo();
		
		String expireStr="";
		long diffMinutes,diffHours,diffDays;
    	long expiredDiff = new Date().getTime() - this.getEndDateLocal().getTime();
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


	public static class PlateComparator implements Comparator<ParkMobileParkingRight> {
		@Override
		public int compare(ParkMobileParkingRight item1, ParkMobileParkingRight item2) {
			return item1.getLpn().compareToIgnoreCase(item2.getLpn());
		}
    }
	
	public static class SpaceComparator implements Comparator<ParkMobileParkingRight> {
		@Override
		public int compare(ParkMobileParkingRight item1, ParkMobileParkingRight item2) {
			return Long.compare(item1.getSpaceNumberLong(), item2.getSpaceNumberLong());
		}
    }

	/*public static class SpaceComparator implements Comparator<ParkMobileParkingRight> {
		@Override
		public int compare(ParkMobileParkingRight item1, ParkMobileParkingRight item2) {
			return Long.compare(item1.getSpaceNumberLong(), item2.getSpaceNumberLong());
		}
	}*/
	
	public static class ExpireComparator implements Comparator<ParkMobileParkingRight> {
		@Override
		public int compare(ParkMobileParkingRight item1, ParkMobileParkingRight item2) {
			return item1.endDateLocal.compareTo(item2.endDateLocal);
		}
    }
}
