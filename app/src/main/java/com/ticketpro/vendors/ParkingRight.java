package com.ticketpro.vendors;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ParkingRight {

    @SerializedName("parkingRightId")
    @Expose
    private Integer parkingRightId;
    @SerializedName("signageZoneCode")
    @Expose
    private String signageZoneCode;
    @SerializedName("internalZoneCode")
    @Expose
    private String internalZoneCode;
    @SerializedName("supplierId")
    @Expose
    private Integer supplierId;
    @SerializedName("lpn")
    @Expose
    private String lpn;
    @SerializedName("lpnState")
    @Expose
    private String lpnState;
    @SerializedName("startDateLocal")
    @Expose
    private String startDateLocal;
    @SerializedName("startDateUtc")
    @Expose
    private String startDateUtc;
    @SerializedName("endDateLocal")
    @Expose
    private String endDateLocal;
    @SerializedName("endDateUtc")
    @Expose
    private String endDateUtc;
    @SerializedName("productDescription")
    @Expose
    private String productDescription;
    @SerializedName("spaceNumber")
    @Expose
    private String spaceNumber;
    @SerializedName("timeZone")
    @Expose
    private String timeZone;
    @SerializedName("permit")
    @Expose
    private String permit;
    @SerializedName("modifiedDate")
    @Expose
    private String modifiedDate;
    @SerializedName("modifiedDateUtc")
    @Expose
    private String modifiedDateUtc;
    @SerializedName("payedMinutes")
    @Expose
    private Integer payedMinutes;
    @SerializedName("purchaseAmount")
    @Expose
    private Integer purchaseAmount;
    @SerializedName("productTypeId")
    @Expose
    private Integer productTypeId;
    @SerializedName("createdDateLocal")
    @Expose
    private String createdDateLocal;
    @SerializedName("createdDateUtc")
    @Expose
    private String createdDateUtc;
    @SerializedName("customField1")
    @Expose
    private String customField1;
    @SerializedName("customField2")
    @Expose
    private String customField2;
    @SerializedName("internalZoneCode2")
    @Expose
    private String internalZoneCode2;

    public Integer getParkingRightId() {
        return parkingRightId;
    }

    public void setParkingRightId(Integer parkingRightId) {
        this.parkingRightId = parkingRightId;
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

    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    public String getLpn() {
        return lpn;
    }

    public void setLpn(String lpn) {
        this.lpn = lpn;
    }

    public String getLpnState() {
        return lpnState;
    }

    public void setLpnState(String lpnState) {
        this.lpnState = lpnState;
    }

    public String getStartDateLocal() {
        return startDateLocal;
    }

    public void setStartDateLocal(String startDateLocal) {
        this.startDateLocal = startDateLocal;
    }

    public String getStartDateUtc() {
        return startDateUtc;
    }

    public void setStartDateUtc(String startDateUtc) {
        this.startDateUtc = startDateUtc;
    }

    public String getEndDateLocal() {
        return endDateLocal;
    }

    public void setEndDateLocal(String endDateLocal) {
        this.endDateLocal = endDateLocal;
    }

    public String getEndDateUtc() {
        return endDateUtc;
    }

    public void setEndDateUtc(String endDateUtc) {
        this.endDateUtc = endDateUtc;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getSpaceNumber() {
        return spaceNumber;
    }

    public void setSpaceNumber(String spaceNumber) {
        this.spaceNumber = spaceNumber;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getPermit() {
        return permit;
    }

    public void setPermit(String permit) {
        this.permit = permit;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getModifiedDateUtc() {
        return modifiedDateUtc;
    }

    public void setModifiedDateUtc(String modifiedDateUtc) {
        this.modifiedDateUtc = modifiedDateUtc;
    }

    public Integer getPayedMinutes() {
        return payedMinutes;
    }

    public void setPayedMinutes(Integer payedMinutes) {
        this.payedMinutes = payedMinutes;
    }

    public Integer getPurchaseAmount() {
        return purchaseAmount;
    }

    public void setPurchaseAmount(Integer purchaseAmount) {
        this.purchaseAmount = purchaseAmount;
    }

    public Integer getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(Integer productTypeId) {
        this.productTypeId = productTypeId;
    }

    public String getCreatedDateLocal() {
        return createdDateLocal;
    }

    public void setCreatedDateLocal(String createdDateLocal) {
        this.createdDateLocal = createdDateLocal;
    }

    public String getCreatedDateUtc() {
        return createdDateUtc;
    }

    public void setCreatedDateUtc(String createdDateUtc) {
        this.createdDateUtc = createdDateUtc;
    }

    public String getCustomField1() {
        return customField1;
    }

    public void setCustomField1(String customField1) {
        this.customField1 = customField1;
    }

    public String getCustomField2() {
        return customField2;
    }

    public void setCustomField2(String customField2) {
        this.customField2 = customField2;
    }

    public String getInternalZoneCode2() {
        return internalZoneCode2;
    }

    public void setInternalZoneCode2(String internalZoneCode2) {
        this.internalZoneCode2 = internalZoneCode2;
    }
}
