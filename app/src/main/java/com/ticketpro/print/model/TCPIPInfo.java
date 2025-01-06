package com.ticketpro.print.model;

import java.io.Serializable;

public class TCPIPInfo implements Serializable {

	static final long serialVersionUID = 1119260164106065074L;
	private String IPAddress;
	private int TCPIPPort;

	public String getIPAddress() {
		return IPAddress;
	}

	public void setIPAddress(String value) {
		IPAddress = value;
	}

	public int getTCPIPPort() {
		return TCPIPPort;
	}

	public void setTCPIPPort(int value) {
		TCPIPPort = value;
	}

	public TCPIPInfo(String IP, int Port) {
		IPAddress = IP;
		TCPIPPort = Port;
	}

}
