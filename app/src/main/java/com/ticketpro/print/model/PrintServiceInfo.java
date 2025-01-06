package com.ticketpro.print.model;

import java.io.Serializable;

public class PrintServiceInfo implements Serializable {
	static final long serialVersionUID = 1119260164106065074L;
	private String deviceName;

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String value) {
		deviceName = value;
	}

	public PrintServiceInfo(String device) {
		deviceName = device;
	}
}
