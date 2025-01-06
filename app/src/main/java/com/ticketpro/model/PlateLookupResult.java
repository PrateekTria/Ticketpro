package com.ticketpro.model;

import java.util.ArrayList;

public class PlateLookupResult {

	private ArrayList<Hotlist> hotlist;
	private ArrayList<Ticket> tickets;
	private ArrayList<Permit> permits;
	private Ticket historyTicket;
	private String plate;
	private String state;
	private Permit permit;

	public ArrayList<Hotlist> getHotlist() {
		return hotlist;
	}

	public void setHotlist(ArrayList<Hotlist> hotlist) {
		this.hotlist = hotlist;
	}
	
	public ArrayList<Ticket> getAllTicket() {
		return tickets;
	}

	public void setAllTicket(ArrayList<Ticket> tickets) {
		this.tickets = tickets;
	}
	
	/**
	 * @return - Returns permit list record
	 * ****/
	public ArrayList<Permit> getAllPermit() {
		return permits;
	}

	public void setAllPermit(ArrayList<Permit> permits) {
		this.permits = permits;
	}


	public Ticket getHistoryTicket() {
		return historyTicket;
	}

	public void setHistoryTicket(Ticket historyTicket) {
		this.historyTicket = historyTicket;
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

	public Permit getPermit() {
		return permit;
	}

	public void setPermit(Permit permit) {
		this.permit = permit;
	}
}
