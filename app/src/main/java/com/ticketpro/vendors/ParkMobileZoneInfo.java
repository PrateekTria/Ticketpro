package com.ticketpro.vendors;

import org.json.JSONObject;

public class ParkMobileZoneInfo {

	private long supplierId;
	private String signageZoneCode;
	private String internalZoneCode;
	private String locationCode;
	private String description;

	public ParkMobileZoneInfo(JSONObject object)  throws Exception{
		this.supplierId=object.getLong("supplierId");
		this.signageZoneCode=object.getString("signageZoneCode");
		this.internalZoneCode=object.getString("internalZoneCode");
		this.locationCode=object.getString("locationCode");
		this.description=object.getString("description");
	}
	
	public long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(long supplierId) {
		this.supplierId = supplierId;
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

	public String getLocationCode() {
		return locationCode;
	}

	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

} 