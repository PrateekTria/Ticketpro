package com.ticketpro.model;

public class ZonePoleModel {
    private int custId;
    private String zone_Id;
    private String zone_Name;
    private boolean is_Active;


    public ZonePoleModel() {
    }

    // Constructor
    public ZonePoleModel(int custId, String zone_Id, String zone_Name, boolean is_Active) {
        this.custId = custId;
        this.zone_Id = zone_Id;
        this.zone_Name = zone_Name;
        this.is_Active = is_Active;
    }

    // Getters and Setters
    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public String getZone_Id() {
        return zone_Id;
    }

    public void setZone_Id(String zone_Id) {
        this.zone_Id = zone_Id;
    }

    public String getZone_Name() {
        return zone_Name;
    }

    public void setZone_Name(String zone_Name) {
        this.zone_Name = zone_Name;
    }

    public boolean isActive() {
        return is_Active;
    }

    public void setActive(boolean is_Active) {
        this.is_Active = is_Active;
    }
}
