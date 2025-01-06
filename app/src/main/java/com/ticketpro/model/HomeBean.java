package com.ticketpro.model;

public class HomeBean extends BaseBean{
	
	private String currentDateTime;
	private String lastSynchedDateTime;
	private String uniqueNo;
	
	public String getCurrentDateTime() {
		return currentDateTime;
	}
	public void setCurrentDateTime(String currentDateTime) {
		this.currentDateTime = currentDateTime;
	}
	public String getLastSynchedDateTime() {
		return lastSynchedDateTime;
	}
	public void setLastSynchedDateTime(String lastSynchedDateTime) {
		this.lastSynchedDateTime = lastSynchedDateTime;
	}
	public String getUniqueNo() {
		return uniqueNo;
	}
	public void setUniqueNo(String uniqueNo) {
		this.uniqueNo = uniqueNo;
	}

}
