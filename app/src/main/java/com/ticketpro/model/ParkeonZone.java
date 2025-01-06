package com.ticketpro.model;

public class ParkeonZone {

    private String zone_id;

    private String zone_name;

    private String zone_description;

    public String getZone_id ()
    {
        return zone_id;
    }

    public void setZone_id (String zone_id)
    {
        this.zone_id = zone_id;
    }

    public String getZone_name ()
    {
        return zone_name;
    }

    public void setZone_name (String zone_name)
    {
        this.zone_name = zone_name;
    }

    public String getZone_description ()
    {
        return zone_description;
    }

    public void setZone_description (String zone_description)
    {
        this.zone_description = zone_description;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [zone_id = "+zone_id+", zone_name = "+zone_name+", zone_description = "+zone_description+"]";
    }
}
