package com.ticketpro.vendors;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;


public class PassportParkingZoneItem {

	private String id;
	private String zoneId;
	private String zoneName;
	private String zoneNumber;
	private String enableCashCheckin;
	private String forceLogOfftime;
	private String zoneTypeId;
	private String monitorSpacebasedActiveOnly;
	private String defaultViolationTypeId;
	private String zonetypeName;
	private String operatorId;
	private ArrayList<PassportParkingZoneItem> children;
	
	public PassportParkingZoneItem(){
		
	}
	
	public PassportParkingZoneItem(JSONObject object)  throws Exception{
		this.id=object.getString("id");
		this.zoneId=object.getString("zoneid");
		this.zoneName=object.getString("zonename");
		this.zoneNumber=object.getString("zonenumber");
		this.enableCashCheckin=object.getString("enablecashcheckin");
		this.forceLogOfftime=object.getString("forcelogofftime");
		this.zoneTypeId=object.getString("zonetype_id");
		this.monitorSpacebasedActiveOnly=object.getString("monitorspacebasedactiveonly");
		this.defaultViolationTypeId=object.getString("default_violationtype_id");
		this.zonetypeName=object.getString("zonetype_name");
		this.operatorId=object.getString("operator_id");
		
		JSONArray children=object.getJSONArray("children");
		if(children!=null){
			this.children=new ArrayList<PassportParkingZoneItem>();
			for(int i=0;i<children.length();i++){
				this.children.add(new PassportParkingZoneItem(children.getJSONObject(i)));
			}
		}
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getZoneId() {
		return zoneId;
	}
	public void setZoneId(String zoneId) {
		this.zoneId = zoneId;
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
	public String getEnableCashCheckin() {
		return enableCashCheckin;
	}
	public void setEnableCashCheckin(String enableCashCheckin) {
		this.enableCashCheckin = enableCashCheckin;
	}
	public String getForceLogOfftime() {
		return forceLogOfftime;
	}
	public void setForceLogOfftime(String forceLogOfftime) {
		this.forceLogOfftime = forceLogOfftime;
	}
	public String getZoneTypeId() {
		return zoneTypeId;
	}
	public void setZoneTypeId(String zoneTypeId) {
		this.zoneTypeId = zoneTypeId;
	}
	public String getMonitorSpacebasedActiveOnly() {
		return monitorSpacebasedActiveOnly;
	}
	public void setMonitorSpacebasedActiveOnly(String monitorSpacebasedActiveOnly) {
		this.monitorSpacebasedActiveOnly = monitorSpacebasedActiveOnly;
	}
	public String getDefaultViolationTypeId() {
		return defaultViolationTypeId;
	}
	public void setDefaultViolationTypeId(String defaultViolationTypeId) {
		this.defaultViolationTypeId = defaultViolationTypeId;
	}
	public String getZonetypeName() {
		return zonetypeName;
	}
	public void setZonetypeName(String zonetypeName) {
		this.zonetypeName = zonetypeName;
	}
	public String getOperatorId() {
		return operatorId;
	}
	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	public ArrayList<PassportParkingZoneItem> getChildren() {
		return children;
	}

	public void setChildren(ArrayList<PassportParkingZoneItem> children) {
		this.children = children;
	}
}
