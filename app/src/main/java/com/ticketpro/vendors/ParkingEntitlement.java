package com.ticketpro.vendors;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ParkingEntitlement {

    @SerializedName("plate")
    @Expose
    private String plate;
    @SerializedName("regionId")
    @Expose
    private Integer regionId;
    @SerializedName("locationNumber")
    @Expose
    private String locationNumber;
    @SerializedName("locationName")
    @Expose
    private String locationName;
    @SerializedName("startDateTime")
    @Expose
    private String startDateTime;
    @SerializedName("endDateTime")
    @Expose
    private String endDateTime;
    @SerializedName("stall")
    @Expose
    private String stall;
    @SerializedName("isActive")
    @Expose
    private Boolean isActive;
    @SerializedName("eligibilityStatus")
    @Expose
    private String eligibilityStatus;
    @SerializedName("vendorLocationId")
    @Expose
    private String vendorLocationId;
    @SerializedName("vehicleState")
    @Expose
    private String vehicleState;
    @SerializedName("vehicleType")
    @Expose
    private String vehicleType;

    @SerializedName("chargeableMinutes")
    @Expose
    private String chargeableMinutes;

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public Integer getRegionId() {
        return regionId;
    }

    public void setRegionId(Integer regionId) {
        this.regionId = regionId;
    }

    public String getLocationNumber() {
        return locationNumber;
    }

    public void setLocationNumber(String locationNumber) {
        this.locationNumber = locationNumber;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }

    public String getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(String endDateTime) {
        this.endDateTime = endDateTime;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getEligibilityStatus() {
        return eligibilityStatus;
    }

    public void setEligibilityStatus(String eligibilityStatus) {
        this.eligibilityStatus = eligibilityStatus;
    }

    public String getVendorLocationId() {
        return vendorLocationId;
    }

    public void setVendorLocationId(String vendorLocationId) {
        this.vendorLocationId = vendorLocationId;
    }

    public String getVehicleState() {
        return vehicleState;
    }

    public void setVehicleState(String vehicleState) {
        this.vehicleState = vehicleState;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getChargeableMinutes() {
        return chargeableMinutes;
    }

    public void setChargeableMinutes(String chargeableMinutes) {
        this.chargeableMinutes = chargeableMinutes;
    }

    public String getStall() {
        return stall;
    }

    public void setStall(String stall) {
        this.stall = stall;
    }
}
