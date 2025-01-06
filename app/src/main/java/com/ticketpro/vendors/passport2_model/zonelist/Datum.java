
package com.ticketpro.vendors.passport2_model.zonelist;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("legacy_id")
    @Expose
    private String legacyId;
    @SerializedName("settings")
    @Expose
    private Settings settings;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("extend_transaction_fee_in_cents")
    @Expose
    private Integer extendTransactionFeeInCents;
    @SerializedName("addresses")
    @Expose
    private List<Address> addresses;
    @SerializedName("usage_type")
    @Expose
    private String usageType;
    @SerializedName("geography")
    @Expose
    private Geography geography;
    @SerializedName("operator")
    @Expose
    private Operator operator;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("number")
    @Expose
    private String number;
    @SerializedName("spaces")
    @Expose
    private List<String> spaces;
    @SerializedName("operator_id")
    @Expose
    private String operatorId;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("transaction_fee_in_cents")
    @Expose
    private Integer transactionFeeInCents;
    @SerializedName("session_type")
    @Expose
    private String sessionType;

    public String getLegacyId() {
        return legacyId;
    }

    public void setLegacyId(String legacyId) {
        this.legacyId = legacyId;
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getExtendTransactionFeeInCents() {
        return extendTransactionFeeInCents;
    }

    public void setExtendTransactionFeeInCents(Integer extendTransactionFeeInCents) {
        this.extendTransactionFeeInCents = extendTransactionFeeInCents;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public String getUsageType() {
        return usageType;
    }

    public void setUsageType(String usageType) {
        this.usageType = usageType;
    }

    public Geography getGeography() {
        return geography;
    }

    public void setGeography(Geography geography) {
        this.geography = geography;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public List<String> getSpaces() {
        return spaces;
    }

    public void setSpaces(List<String> spaces) {
        this.spaces = spaces;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getTransactionFeeInCents() {
        return transactionFeeInCents;
    }

    public void setTransactionFeeInCents(Integer transactionFeeInCents) {
        this.transactionFeeInCents = transactionFeeInCents;
    }

    public String getSessionType() {
        return sessionType;
    }

    public void setSessionType(String sessionType) {
        this.sessionType = sessionType;
    }

}
