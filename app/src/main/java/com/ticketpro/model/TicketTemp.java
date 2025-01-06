package com.ticketpro.model;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ticketpro.parking.activity.TPApplication;
import com.ticketpro.parking.dao.ParkingDatabase;
import com.ticketpro.util.StringUtil;
import com.ticketpro.util.TPUtility;

import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Date;

import io.reactivex.CompletableObserver;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@Entity(tableName = "tickets_temp",indices = @Index(value = {"custid"}, unique = true))
public class TicketTemp implements Serializable {
    @Ignore
    private static final Logger log = Logger.getLogger("Ticket");
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ticket_id")
    @SerializedName("ticket_id")
    @Expose
    private long ticketId;
    @ColumnInfo(name = "userid")
    @SerializedName("userid")
    @Expose
    private int userId;
    @ColumnInfo(name = "custid")
    @SerializedName("custid")
    @Expose
    private int custId;
    @ColumnInfo(name = "device_id")
    @SerializedName("device_id")
    @Expose
    private int deviceId;
    @ColumnInfo(name = "citation_number")
    @SerializedName("citation_number")
    @Expose
    private long citationNumber;
    @ColumnInfo(name = "ticket_date")
    @SerializedName("ticket_date")
    @Expose
    private Date ticketDate;

    @ColumnInfo(name = "state_id")
    @SerializedName("state_id")
    @Expose
    private int stateId;

    @ColumnInfo(name = "state_code")
    @SerializedName("state_code")
    @Expose
    private String stateCode;

    @ColumnInfo(name = "plate")
    @SerializedName("plate")
    @Expose
    private String plate;

    @ColumnInfo(name = "vin")
    @SerializedName("vin")
    @Expose
    private String vin;

    @ColumnInfo(name = "expiration")
    @SerializedName("expiration")
    @Expose
    private String expiration;

    @ColumnInfo(name = "make_id")
    @SerializedName("make_id")
    @Expose
    private int makeId;

    @ColumnInfo(name = "make_code")
    @SerializedName("make_code")
    @Expose
    private String makeCode;

    @ColumnInfo(name = "body_id")
    @SerializedName("body_id")
    @Expose
    private int bodyId;

    @ColumnInfo(name = "body_code")
    @SerializedName("body_code")
    @Expose
    private String bodyCode;

    @ColumnInfo(name = "color_id")
    @SerializedName("color_id")
    @Expose
    private int colorId;

    @ColumnInfo(name = "color_code")
    @SerializedName("color_code")
    @Expose
    private String colorCode;

    @ColumnInfo(name = "street_prefix")
    @SerializedName("street_prefix")
    @Expose
    private String streetPrefix;

    @ColumnInfo(name = "street_suffix")
    @SerializedName("street_suffix")
    @Expose
    private String streetSuffix;

    @ColumnInfo(name = "street_number")
    @SerializedName("street_number")
    @Expose
    private String streetNumber;

    @ColumnInfo(name = "location")
    @SerializedName("location")
    @Expose
    private String location;
    @ColumnInfo(name = "direction")
    @SerializedName("direction")
    @Expose
    private String direction;
    @ColumnInfo(name = "latitude")
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @ColumnInfo(name = "longitude")
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @ColumnInfo(name = "gpstime")
    @SerializedName("gpstime")
    @Expose
    private Date gpstime;
    @ColumnInfo(name = "is_gps_location")
    @SerializedName("is_gps_location")
    @Expose
    private String isGPSLocation;
    @ColumnInfo(name = "is_void")
    @SerializedName("is_void")
    @Expose
    private String isVoid;
    @ColumnInfo(name = "is_chalked")
    @SerializedName("is_chalked")
    @Expose
    private String isChalked;
    @ColumnInfo(name = "is_warn")
    @SerializedName("is_warn")
    @Expose
    private String isWarn;
    @ColumnInfo(name = "is_driveaway")
    @SerializedName("is_driveaway")
    @Expose
    private String isDriveAway;
    @ColumnInfo(name = "void_id")
    @SerializedName("void_id")
    @Expose
    private int voidId;
    @ColumnInfo(name = "chalk_id")
    @SerializedName("chalk_id")
    @Expose
    private long chalkId;

    @ColumnInfo(name = "permit")
    @SerializedName("permit")
    @Expose
    private String permit;

    @ColumnInfo(name = "meter")
    @SerializedName("meter")
    @Expose
    private String meter;

    @ColumnInfo(name = "fine")
    @SerializedName("fine")
    @Expose
    private double fine;

    @ColumnInfo(name = "time_marked")
    @SerializedName("time_marked")
    @Expose
    private Date timeMarked;

    @ColumnInfo(name = "space")
    @SerializedName("space")
    @Expose
    private String space;

    @ColumnInfo(name = "violation_code")
    @SerializedName("violation_code")
    @Expose
    private String violationCode;

    @ColumnInfo(name = "violation")
    @SerializedName("violation")
    @Expose
    private String violation;

    @ColumnInfo(name = "void_reason_code")
    private String voidReasonCode;

    @ColumnInfo(name = "duty_id")
    @SerializedName("duty_id")
    @Expose
    private int dutyId;

    @ColumnInfo(name = "is_lpr")
    @SerializedName("is_lpr")
    @Expose
    private String isLPR;

    @ColumnInfo(name = "violation_id")
    @SerializedName("violation_id")
    @Expose
    private int violationId;

    @ColumnInfo(name = "photo_count")
    @SerializedName("photo_count")
    @Expose
    private int photo_count;

    @ColumnInfo(name = "lpr_notification_id")
    private String lprNotificationId;

    @ColumnInfo(name = "status")
    private String status;

    @ColumnInfo(name = "placard")
    private String placard;

    @SerializedName("duty_report_id")
    @Expose
    @ColumnInfo(name = "duty_report_id")
    private String dutyReportId;

    @SerializedName("app_version")
    @Expose
    @ColumnInfo(name = "app_version")
    private String appVersion;

    public static void removeAll() throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).ticketsDaoTemp().removeAll();
    }

    @SuppressLint("CheckResult")
    public static void insertTicket(@NotNull final TicketTemp ticket) {
        TicketTemp activeTicket;
        activeTicket = ticket;
        final ParkingDatabase instance = ParkingDatabase.getInstance(TPApplication.getInstance());
        long time = System.currentTimeMillis();
        instance.ticketsDaoTemp().insertTicket(activeTicket).subscribeOn(Schedulers.io()).subscribeWith(new CompletableObserver() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onComplete() {
                Log.i("Ticket End", "onComplete: " + (System.currentTimeMillis() - time));
            }

            @Override
            public void onError(@NonNull Throwable e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }
        });
    }

    public static TicketTemp getLastTicket() {
        try {
            TicketTemp object = null;
            object = ParkingDatabase.getInstance(TPApplication.getInstance()).ticketsDaoTemp().getLastTicket();

            return object;

        } catch (Exception e) {
            Log.e("Ticket", "Error " + e.getMessage());
            return null;
        }
    }
    public static int getCount() throws Exception {
        return ParkingDatabase.getInstance(TPApplication.getInstance()).ticketsDaoTemp().getCount();
    }
    public static Logger getLog() {
        return log;
    }


    public long getTicketId() {
        return ticketId;
    }

    public void setTicketId(long ticketId) {
        this.ticketId = ticketId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public long getCitationNumber() {
        return citationNumber;
    }

    public void setCitationNumber(long citationNumber) {
        this.citationNumber = citationNumber;
    }

    public Date getTicketDate() {
        return ticketDate;
    }

    public void setTicketDate(Date ticketDate) {
        this.ticketDate = ticketDate;
    }

    public int getStateId() {
        return stateId;
    }

    public void setStateId(int stateId) {
        this.stateId = stateId;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    public int getMakeId() {
        return makeId;
    }

    public void setMakeId(int makeId) {
        this.makeId = makeId;
    }

    public String getMakeCode() {
        return makeCode;
    }

    public void setMakeCode(String makeCode) {
        this.makeCode = makeCode;
    }

    public int getBodyId() {
        return bodyId;
    }

    public void setBodyId(int bodyId) {
        this.bodyId = bodyId;
    }

    public String getBodyCode() {
        return bodyCode;
    }

    public void setBodyCode(String bodyCode) {
        this.bodyCode = bodyCode;
    }

    public int getColorId() {
        return colorId;
    }

    public void setColorId(int colorId) {
        this.colorId = colorId;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public String getStreetPrefix() {
        return streetPrefix;
    }

    public void setStreetPrefix(String streetPrefix) {
        this.streetPrefix = streetPrefix;
    }

    public String getStreetSuffix() {
        return streetSuffix;
    }

    public void setStreetSuffix(String streetSuffix) {
        this.streetSuffix = streetSuffix;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public Date getGpstime() {
        return gpstime;
    }

    public void setGpstime(Date gpstime) {
        this.gpstime = gpstime;
    }

    public String getIsGPSLocation() {
        return isGPSLocation;
    }

    public void setIsGPSLocation(String isGPSLocation) {
        this.isGPSLocation = isGPSLocation;
    }

    public String getIsVoid() {
        return isVoid;
    }

    public void setIsVoid(String isVoid) {
        this.isVoid = isVoid;
    }

    public String getIsChalked() {
        return isChalked;
    }

    public void setIsChalked(String isChalked) {
        this.isChalked = isChalked;
    }

    public String getIsWarn() {
        return isWarn;
    }

    public void setIsWarn(String isWarn) {
        this.isWarn = isWarn;
    }

    public String getIsDriveAway() {
        return isDriveAway;
    }

    public void setIsDriveAway(String isDriveAway) {
        this.isDriveAway = isDriveAway;
    }

    public int getVoidId() {
        return voidId;
    }

    public void setVoidId(int voidId) {
        this.voidId = voidId;
    }

    public long getChalkId() {
        return chalkId;
    }

    public void setChalkId(long chalkId) {
        this.chalkId = chalkId;
    }

    public String getPermit() {
        return permit;
    }

    public void setPermit(String permit) {
        this.permit = permit;
    }

    public String getMeter() {
        return meter;
    }

    public void setMeter(String meter) {
        this.meter = meter;
    }

    public double getFine() {
        return fine;
    }

    public void setFine(double fine) {
        this.fine = fine;
    }

    public Date getTimeMarked() {
        return timeMarked;
    }

    public void setTimeMarked(Date timeMarked) {
        this.timeMarked = timeMarked;
    }

    public String getSpace() {
        return space;
    }

    public void setSpace(String space) {
        this.space = space;
    }

    public String getViolationCode() {
        return violationCode;
    }

    public void setViolationCode(String violationCode) {
        this.violationCode = violationCode;
    }

    public String getViolation() {
        return violation;
    }

    public void setViolation(String violation) {
        this.violation = violation;
    }

    public String getVoidReasonCode() {
        return voidReasonCode;
    }

    public void setVoidReasonCode(String voidReasonCode) {
        this.voidReasonCode = voidReasonCode;
    }

    public int getDutyId() {
        return dutyId;
    }

    public void setDutyId(int dutyId) {
        this.dutyId = dutyId;
    }

    public String getIsLPR() {
        return isLPR;
    }

    public void setIsLPR(String isLPR) {
        this.isLPR = isLPR;
    }

    public int getViolationId() {
        return violationId;
    }

    public void setViolationId(int violationId) {
        this.violationId = violationId;
    }

    public int getPhoto_count() {
        return photo_count;
    }

    public void setPhoto_count(int photo_count) {
        this.photo_count = photo_count;
    }

    public String getLprNotificationId() {
        return lprNotificationId;
    }

    public void setLprNotificationId(String lprNotificationId) {
        this.lprNotificationId = lprNotificationId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPlacard() {
        return placard;
    }

    public void setPlacard(String placard) {
        this.placard = placard;
    }

    public String getDutyReportId() {
        return dutyReportId;
    }

    public void setDutyReportId(String dutyReportId) {
        this.dutyReportId = dutyReportId;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }
}
