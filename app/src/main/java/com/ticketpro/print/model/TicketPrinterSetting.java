package com.ticketpro.print.model;

import java.io.Serializable;

public class TicketPrinterSetting implements Serializable {
	private static final long serialVersionUID = 4762643389154364957L;
	private String selectedFileName;
	private String communicationMethod;
	
	public TicketPrinterSetting() {
		
	}
	
	public TicketPrinterSetting(String selectedFileName, String communicationMethod) {
		this.selectedFileName = selectedFileName;
		this.communicationMethod = communicationMethod;
	}
	
	public String getSelectedFileName() {
		return selectedFileName;
	}

	public void setSelectedFileName(String value) {
		selectedFileName = value;
	}

	public String getCommunicationMethod() {
		return communicationMethod;
	}

	public void setCommunicationMethod(String value) {
		communicationMethod = value;
	}
}
