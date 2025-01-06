package com.ticketpro.model;

public class VioEventsBean {

    /**
     * space_name : E13_B26-056
     * zone : Assay St
     * zone_id : 5864
     * latitude : 29.920829
     * longitude : -95.202422
     * entry_image : https://pems.pemsportal.com/importtran/api/v1/bollard/vioImage/TurboData/03ad2c7c-c2f1-4594-a7df-f592aa15487c
     * id : 03ad2c7c-c2f1-4594-a7df-f592aa15487c
     * is_Violated : Y
     * vio_type : No Payment
     * vio_sub_type : null
     * make : Chevrolet
     * model : Trax
     * color : black
     * entry_time : 2024-10-19T17:48:18
     * plate_no : fwb6477
     * state : NA
     */

    private String space_name;
    private String zone;
    private String zone_id;
    private double latitude;
    private double longitude;
    private String entry_image;
    private String id;
    private String is_Violated;
    private String vio_type;
    private Object vio_sub_type;
    private String make;
    private String model;
    private String color;
    private String entry_time;
    private String plate_no;
    private String state;

    public String getSpace_name() {
        return space_name;
    }

    public void setSpace_name(String space_name) {
        this.space_name = space_name;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getZone_id() {
        return zone_id;
    }

    public void setZone_id(String zone_id) {
        this.zone_id = zone_id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getEntry_image() {
        return entry_image;
    }

    public void setEntry_image(String entry_image) {
        this.entry_image = entry_image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIs_Violated() {
        return is_Violated;
    }

    public void setIs_Violated(String is_Violated) {
        this.is_Violated = is_Violated;
    }

    public String getVio_type() {
        return vio_type;
    }

    public void setVio_type(String vio_type) {
        this.vio_type = vio_type;
    }

    public Object getVio_sub_type() {
        return vio_sub_type;
    }

    public void setVio_sub_type(Object vio_sub_type) {
        this.vio_sub_type = vio_sub_type;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getEntry_time() {
        return entry_time;
    }

    public void setEntry_time(String entry_time) {
        this.entry_time = entry_time;
    }

    public String getPlate_no() {
        return plate_no;
    }

    public void setPlate_no(String plate_no) {
        this.plate_no = plate_no;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
