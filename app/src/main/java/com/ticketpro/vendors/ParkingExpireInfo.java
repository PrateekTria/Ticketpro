package com.ticketpro.vendors;

public class ParkingExpireInfo {
	private String expireMsg;
	private boolean isExpired;
	private int diffDays;
	private int diffHrs;
	private int diffMinutes;
	private int diffSeconds;

	public String getExpireMsg() {
		return expireMsg;
	}

	public void setExpireMsg(String expireMsg) {
		this.expireMsg = expireMsg;
	}

	public boolean isExpired() {
		return isExpired;
	}

	public void setExpired(boolean isExpired) {
		this.isExpired = isExpired;
	}

	public int getDiffDays() {
		return diffDays;
	}

	public void setDiffDays(int diffDays) {
		this.diffDays = diffDays;
	}

	public int getDiffHrs() {
		return diffHrs;
	}

	public void setDiffHrs(int diffHrs) {
		this.diffHrs = diffHrs;
	}

	public int getDiffMinutes() {
		return diffMinutes;
	}

	public void setDiffMinutes(int diffMinutes) {
		this.diffMinutes = diffMinutes;
	}

	public int getDiffSeconds() {
		return diffSeconds;
	}

	public void setDiffSeconds(int diffSeconds) {
		this.diffSeconds = diffSeconds;
	}

}
