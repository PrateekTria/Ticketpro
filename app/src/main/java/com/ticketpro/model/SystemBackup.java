package com.ticketpro.model;

import java.util.Date;

public class SystemBackup {

	String citationNumber;
	Date backupDate;
	
	public String getCitationNumber() {
		return citationNumber;
	}

	public void setCitationNumber(String citationNumber) {
		this.citationNumber = citationNumber;
	}

	public Date getBackupDate() {
		return backupDate;
	}
	
	public void setBackupDate(Date backupDate) {
		this.backupDate = backupDate;
	}

}
