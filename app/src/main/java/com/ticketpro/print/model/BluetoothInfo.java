package com.ticketpro.print.model;

import java.io.Serializable;

public class BluetoothInfo implements Serializable {

	static final long serialVersionUID = -4095241456238880463L;
	private String deviceName;

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String value) {
		deviceName = value;
	}

	public BluetoothInfo(String device) {
		deviceName = device;
	}
}
