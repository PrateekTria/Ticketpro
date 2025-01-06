package com.ticketpro.vendors;

import org.json.JSONObject;

public class PayByPhoneZoneItem {

	private String zoneName;
	private String zoneNumber;

	public PayByPhoneZoneItem() {

	}

	public PayByPhoneZoneItem(JSONObject object) throws Exception {
		this.zoneName = object.getString("zonename");
		this.zoneNumber = object.getString("zonenumber");
	}

	public String getZoneName() {
		return zoneName;
	}

	public void setZoneName(String zoneName) {
		this.zoneName = zoneName;
	}

	public String getZoneNumber() {
		return zoneNumber;
	}

	public void setZoneNumber(String zoneNumber) {
		this.zoneNumber = zoneNumber;
	}
}
