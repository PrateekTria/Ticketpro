package com.ticketpro.vendors;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ticketpro.util.DateUtil;

import java.util.Date;

public class ParkMobileSpaceData {
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
    private Double purchaseAmount;
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

    private Date startDateLocal1;
    private Date endDateLocal1;
    private Date modifiedDate1;
    private Date systemDate;
    private Date creationDate;

    public ParkingExpireInfo getExpireInfo(){
        setStartDateLocal1(DateUtil.getDateFromUTCString(startDateLocal));
        setEndDateLocal1(DateUtil.getDateFromUTCString(endDateLocal));
        ParkingExpireInfo parkingExpireInfo=new ParkingExpireInfo();

        String expireStr="";
        long diffMinutes,diffHours,diffDays;
        long expiredDiff = new Date().getTime() - this.getEndDateLocal1().getTime();
        if(expiredDiff > 0){
            diffMinutes = expiredDiff / (60 * 1000) % 60;
            diffHours = expiredDiff / (60 * 60 * 1000) % 24;
            diffDays = expiredDiff / (24 * 60 * 60 * 1000);
            if(diffDays>=1){
                expireStr=diffDays+" d "+diffHours +" h ago";

            }else if(diffHours>=1){
                expireStr=diffHours+" h "+diffMinutes +" m ago";

            }else{
                expireStr=diffMinutes +" m ago";
            }

            parkingExpireInfo.setExpired(true);

        }else{
            expiredDiff=Math.abs(expiredDiff);
            diffMinutes = expiredDiff / (60 * 1000) % 60;
            diffHours = expiredDiff / (60 * 60 * 1000) % 24;
            diffDays = expiredDiff / (24 * 60 * 60 * 1000);

            if(diffDays>=1){
                expireStr=/*"in "+*/diffDays+" d "+diffHours+" h";

            }else if(diffHours>=1){
                expireStr=/*"in "+*/diffHours+" h "+diffMinutes+" m";

            }else{
                expireStr=/*"in "+*/diffMinutes +" m";
            }

            parkingExpireInfo.setExpired(false);
        }

        parkingExpireInfo.setExpireMsg(expireStr);
        parkingExpireInfo.setDiffDays((int)diffDays);
        parkingExpireInfo.setDiffHrs((int)diffHours);
        parkingExpireInfo.setDiffMinutes((int)diffMinutes);
        parkingExpireInfo.setDiffSeconds((int)diffMinutes * 60);

        return parkingExpireInfo;
    }


    public Date getStartDateLocal1() {
        return startDateLocal1;
    }

    public void setStartDateLocal1(Date startDateLocal1) {
        this.startDateLocal1 = startDateLocal1;
    }

    public Date getEndDateLocal1() {
        return endDateLocal1;
    }

    public void setEndDateLocal1(Date endDateLocal1) {
        this.endDateLocal1 = endDateLocal1;
    }

    public Date getModifiedDate1() {
        return modifiedDate1;
    }

    public void setModifiedDate1(Date modifiedDate1) {
        this.modifiedDate1 = modifiedDate1;
    }

    public Date getSystemDate() {
        return systemDate;
    }

    public void setSystemDate(Date systemDate) {
        this.systemDate = systemDate;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

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

    public Double getPurchaseAmount() {
        return purchaseAmount;
    }

    public void setPurchaseAmount(Double purchaseAmount) {
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
