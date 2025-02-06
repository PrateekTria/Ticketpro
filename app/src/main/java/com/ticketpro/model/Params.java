package com.ticketpro.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * Created by Rohit on 15-06-2020.
 */
public class Params {

    @SerializedName("custId")
    @Expose
    private Integer custId;
    @SerializedName("userId")
    @Expose
    private Integer userId;
    @SerializedName("deviceId")
    @Expose
    private Integer deviceId;
    @SerializedName("deviceName")
    @Expose
    private String deviceIName;
    @SerializedName("deviceNameNew")
    @Expose
    private String deviceNameNew;
    @SerializedName("moduleName")
    @Expose
    private String moduleName;
    @SerializedName("updateDeviceId")
    @Expose
    private Boolean updateDeviceId;
    @SerializedName("fullSync")
    @Expose
    private Boolean fullSync;
    @SerializedName("devices")
    @Expose
    private List<DeviceInfo> devices = null;
    @SerializedName("tickets")
    @Expose
    private List<Ticket> tickets = null;
    @SerializedName("chalks")
    @Expose
    private List<ChalkVehicle> chalks = null;
    @SerializedName("from")
    @Expose
    private String from;
    @SerializedName("to")
    @Expose
    private String[] to;
    @SerializedName("subject")
    @Expose
    private String subject;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("chalkId")
    @Expose
    private long chalkId;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("dispositionReason")
    @Expose
    private String dispositionReason;
    @SerializedName("custid")
    @Expose
    private Integer custid;
    @SerializedName("created_date")
    @Expose
    private String created_date;
    @SerializedName("plate")
    @Expose
    private String plate;
    @SerializedName("vin")
    @Expose
    private String vin;

    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("module")
    @Expose
    private String module;
    @SerializedName("errorLog")
    @Expose
    private List<ErrorLog> errorLog = null;
    @SerializedName("LROArr")
    @Expose
    private List<RepeatOffender> repeatOffenders = null;
    @SerializedName("registrationId")
    @Expose
    private String registrationId = null;
    @SerializedName("citations")
    @Expose
    private List<String> citations = null;

    @SerializedName("mobileNowLogs")
    @Expose
    private List<MobileNowLog> mobileNowLogs = null;

    @SerializedName("dutyReports")
    @Expose
    private List<DutyReport> dutyReports = null;


    // This code is added by mohit 27/02/2023

    @SerializedName("citationNumber")
    @Expose
    private Long citationNumber;

    @SerializedName("violationId")
    @Expose
    private Integer violationId;

    @SerializedName("commentText")
    @Expose
    private String commentText;

    @SerializedName("imageName")
    @Expose
    private String imageName;

    @SerializedName("startDate")
    @Expose
    private String startDate;

    @SerializedName("endDate")
    @Expose
    private String endDate;

    @SerializedName("comments")
    @Expose
    private List<TicketComment> ticketComments = null;

    @SerializedName("AgencyCode")
    @Expose
    private String agencyCode;

    @SerializedName("User")
    @Expose
    private String user;

    @SerializedName("placard")
    @Expose
    private String placard;

    @SerializedName("source")
    @Expose
    private String source;

    @SerializedName("date")
    @Expose
    private String date;

    @SerializedName("patrollerId")
    @Expose
    private String patrollerId;

    @SerializedName("isActive")
    @Expose
    private int isActive;


    /*@SerializedName("plate_type")
    @Expose
    private String plate_type;

    @SerializedName("begin_date")
    @Expose
    private Date begin_date;

    @SerializedName("end_date")
    @Expose
    private Date end_date;

    @SerializedName("state_code")
    @Expose
    private String stateCode;

    @SerializedName("comments")
    @Expose
    private String comments;
*/
//End

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public List<String> getCitations() {
        return citations;
    }

    public void setCitations(List<String> citations) {
        this.citations = citations;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getCustid() {
        return custid;
    }

    public void setCustid(Integer custid) {
        this.custid = custid;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public List<RepeatOffender> getRepeatOffenders() {
        return repeatOffenders;
    }

    public void setRepeatOffenders(List<RepeatOffender> repeatOffenders) {
        this.repeatOffenders = repeatOffenders;
    }

    public long getChalkId() {
        return chalkId;
    }

    public void setChalkId(long chalkId) {
        this.chalkId = chalkId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDispositionReason() {
        return dispositionReason;
    }

    public void setDispositionReason(String dispositionReason) {
        this.dispositionReason = dispositionReason;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String[] getTo() {
        return to;
    }

    public void setTo(String[] to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    public String getDeviceIName() {
        return deviceIName;
    }

    public void setDeviceIName(String deviceIName) {
        this.deviceIName = deviceIName;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public Boolean getUpdateDeviceId() {
        return updateDeviceId;
    }

    public void setUpdateDeviceId(Boolean updateDeviceId) {
        this.updateDeviceId = updateDeviceId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }

    public Integer getCustId() {
        return custId;
    }

    public void setCustId(Integer custId) {
        this.custId = custId;
    }

    public Boolean getFullSync() {
        return fullSync;
    }

    public void setFullSync(Boolean fullSync) {
        this.fullSync = fullSync;
    }

    public List<DeviceInfo> getDevices() {
        return devices;
    }

    public void setDevices(List<DeviceInfo> devices) {
        this.devices = devices;
    }

    public List<ErrorLog> getErrorLog() {
        return errorLog;
    }

    public void setErrorLog(List<ErrorLog> errorLog) {
        this.errorLog = errorLog;
    }

    public List<MobileNowLog> getMobileNowLogs() {
        return mobileNowLogs;
    }

    public void setMobileNowLogs(List<MobileNowLog> mobileNowLogs) {
        this.mobileNowLogs = mobileNowLogs;
    }

    public List<ChalkVehicle> getChalks() {
        return chalks;
    }

    public void setChalks(List<ChalkVehicle> chalks) {
        this.chalks = chalks;
    }

    public String getDeviceNameNew() {
        return deviceNameNew;
    }

    public void setDeviceNameNew(String deviceNameNew) {
        this.deviceNameNew = deviceNameNew;
    }

    public List<DutyReport> getDutyReports() {
        return dutyReports;
    }

    public void setDutyReports(List<DutyReport> dutyReports) {
        this.dutyReports = dutyReports;
    }

    public String getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }

    public long getCitationNumber() {
        return citationNumber;
    }

    // This code is added by mohit 27/02/2023

    public void setCitationNumber(long citationNumber) {
        this.citationNumber = citationNumber;
    }

    public int getViolationId() {
        return violationId;
    }

    public void setViolationId(int violationId) {
        this.violationId = violationId;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public List<TicketComment> getTicketCommment() {
        return ticketComments;
    }

    public void setTicketCommment(List<TicketComment> ticketComments) {
        this.ticketComments = ticketComments;
    }

    public String getAgencyCode() {
        return agencyCode;
    }

    public void setAgencyCode(String agencyCode) {
        this.agencyCode = agencyCode;
    }

    public String getPlacard() {
        return placard;
    }

    public void setPlacard(String placard) {
        this.placard = placard;
    }


    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public String getPatrollerId() {
        return patrollerId;
    }

    public void setPatrollerId(String patrollerId) {
        this.patrollerId = patrollerId;
    }

    /*

    public String getPlateType() {
        return plate_type;
    }

    public void setPlateType(String plateType) {
        this.plate_type = plate_type;
    }

    public Date getBeginDate() {
        return begin_date;
    }

    public void setBeginDate(Date begin_date) {
        this.begin_date = begin_date;
    }

    public Date getendDate() {
        return end_date;
    }

    public void setEndDate(Date end_date) {
        this.end_date = end_date;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }
    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
*/




}