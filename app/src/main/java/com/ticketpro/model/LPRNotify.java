package com.ticketpro.model;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.ticketpro.parking.activity.TPApplication;
import com.ticketpro.parking.dao.ParkingDatabase;
import com.ticketpro.util.DateUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

import io.reactivex.Completable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

@Entity(tableName = "lpr_notifications")
public class LPRNotify {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "notification_id")
    private String notificationId;
    @ColumnInfo(name = "plate")
    private String plate;
    @ColumnInfo(name = "state")
    private String state;
    @ColumnInfo(name = "make")
    private String make;
    @ColumnInfo(name = "model")
    private String model;
    @ColumnInfo(name = "body")
    private String body;
    @ColumnInfo(name = "status")
    private String status;
    @ColumnInfo(name = "location")
    private String location;
    @ColumnInfo(name = "latitude")
    private String latitude;
    @ColumnInfo(name = "longitude")
    private String longitude;
    @ColumnInfo(name = "photo1")
    private String photo1;
    @ColumnInfo(name = "photo2")
    private String photo2;
    @ColumnInfo(name = "photo3")
    private String photo3;
    @ColumnInfo(name = "photo4")
    private String photo4;
    @ColumnInfo(name = "date_notify")
    private Date notifyDate;
    @ColumnInfo(name = "first_chalk_time")
    private Date firstChalkTime;
    @ColumnInfo(name = "last_seen")
    private Date lastSeen;
    @ColumnInfo(name = "lpr_scan_id")
    private String lprScanId;
    @ColumnInfo(name = "lpr_camera_id")
    private String lprCameraId;
    @ColumnInfo(name = "lpr_user_id")
    private String lprUserId;
    @ColumnInfo(name = "color")
    private String color;
    @ColumnInfo(name = "permit")
    private String permit;
    @ColumnInfo(name = "permit_type")
    private String permitType;
    @ColumnInfo(name = "permit_status")
    private String permitStatus;
    @ColumnInfo(name = "duty_group")
    private String dutyGroup;
    @ColumnInfo(name = "comments")
    private String comments;
    @ColumnInfo(name = "comments2")
    private String comments2;
    @ColumnInfo(name = "violation_id")
    private String violationId;
    @ColumnInfo(name = "violation_desc")
    private String violationDesc;
    @ColumnInfo(name = "violation_code")
    private String violationCode;
    @ColumnInfo(name = "device_id")
    private String deviceId;
    @ColumnInfo(name = "zone")
    private String zone;
    @ColumnInfo(name = "ticket_issues")
    private boolean ticketIssued;

    public LPRNotify() {

    }

    public LPRNotify(JSONObject object) throws JSONException {
        this.setPlate(!object.isNull("plate") ? object.getString("plate") : "");
        this.setState(!object.isNull("state") ? object.getString("state") : "");
        this.setMake(!object.isNull("make") ? object.getString("make") : "");
        this.setModel(!object.isNull("model") ? object.getString("model") : "");
        this.setBody(!object.isNull("body") ? object.getString("body") : "");
        this.setStatus(!object.isNull("status") ? object.getString("status") : "");
        this.setLocation(!object.isNull("location") ? object.getString("location") : "");
        this.setLatitude(!object.isNull("lat") ? object.getString("lat") : "");
        this.setLongitude(!object.isNull("long") ? object.getString("long") : "");
        this.setPhoto1(!object.isNull("photo1") ? object.getString("photo1") : "");
        this.setPhoto2(!object.isNull("photo2") ? object.getString("photo2") : "");
        this.setPhoto3(!object.isNull("photo3") ? object.getString("photo3") : "");
        this.setPhoto4(!object.isNull("photo4") ? object.getString("photo4") : "");
        this.setLprScanId(!object.isNull("lprScanId") ? object.getString("lprScanId") : "");
        this.setLprCameraId(!object.isNull("lprCameraId") ? object.getString("lprCameraId") : "");
        this.setLprUserId(!object.isNull("lprUserId") ? object.getString("lprUserId") : "");
        this.setNotificationId(!object.isNull("notificationId") ? object.getString("notificationId") : "");
        this.setColor(!object.isNull("color") ? object.getString("color") : "");
        this.setPermit(!object.isNull("permit") ? object.getString("permit") : "");
        this.setPermitType(!object.isNull("permitType") ? object.getString("permitType") : "");
        this.setPermitStatus(!object.isNull("permitStatus") ? object.getString("permitStatus") : "");
        this.setViolationId(!object.isNull("violationId") ? object.getString("violationId") : "");
        this.setViolationCode(!object.isNull("violationCode") ? object.getString("violationCode") : "");
        this.setViolationDesc(!object.isNull("violationDesc") ? object.getString("violationDesc") : "");
        this.setComments(!object.isNull("comments") ? object.getString("comments") : "");
		this.setComments2(!object.isNull("comments2")?object.getString("comments2"):"");
        this.setDutyGroup(!object.isNull("dutyGroup") ? object.getString("dutyGroup") : "");
        this.setNotifyDate(!object.isNull("dateNotify") ? DateUtil.getDateFromSQLString(object.getString("dateNotify")) : new Date());
        this.setFirstChalkTime(!object.isNull("firstChalkTime") ? DateUtil.getDateFromSQLString(object.getString("firstChalkTime")) : new Date());
        this.setLastSeen(!object.isNull("lastSeen") ? DateUtil.getDateFromSQLString(object.getString("lastSeen")) : new Date());
        this.setDutyGroup(!object.isNull("dutyGroup") ? object.getString("dutyGroup") : "");
        this.setZone(!object.isNull("zone") ? object.getString("zone") : "");
        this.setDeviceId(!object.isNull("deviceId") ? object.getString("deviceId") : "");

        this.setNotifyDate(new Date());
    }

    public static ArrayList<LPRNotify> getLPRNotifications() throws Exception {
        ArrayList<LPRNotify> list = new ArrayList<LPRNotify>();
        list = (ArrayList<LPRNotify>) ParkingDatabase.getInstance(TPApplication.getInstance()).lprNotificationsDao().getLPRNotifications();
        return list;
    }

    public static LPRNotify getLPRNotificationById(int notificationId) throws Exception {
        LPRNotify object = null;
        object = ParkingDatabase.getInstance(TPApplication.getInstance()).lprNotificationsDao().getLPRNotificationById(notificationId);
        return object;
    }

    public static void removeAllNotifications() throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).lprNotificationsDao().removeAllNotifications();
    }

    public static void removeNotificationById(String notificationId) throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).lprNotificationsDao().removeNotificationById(notificationId);
    }

    public static Completable insertLPRNotify(final LPRNotify LPRNotify) {
        final ParkingDatabase instance = ParkingDatabase.getInstance(TPApplication.getInstance());
        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
           instance.lprNotificationsDao().insertLPRNotify(LPRNotify).subscribeOn(Schedulers.io()).subscribe();
            }
        });
        //new LPRNotify.InsertLPRNotify(LPRNotify).execute();
    }

    public ContentValues getContentValues() throws Exception {
        ContentValues values = new ContentValues();
        values.put("notification_id", this.notificationId);
        values.put("plate", this.plate);
        values.put("state", this.state);
        values.put("make", this.make);
        values.put("model", this.model);
        values.put("body", this.body);
        values.put("status", this.status);
        values.put("location", this.location);
        values.put("latitude", this.latitude);
        values.put("longitude", this.longitude);
        values.put("photo1", this.photo1);
        values.put("photo2", this.photo2);
        values.put("photo3", this.photo3);
        values.put("photo4", this.photo4);
        values.put("lpr_scan_id", this.lprScanId);
        values.put("lpr_camera_id", this.lprCameraId);
        values.put("lpr_user_id", this.lprUserId);
        values.put("color", this.color);
        values.put("permit", this.permit);
        values.put("permit_type", this.permitType);
        values.put("permit_status", this.permitStatus);
        values.put("duty_group", this.dutyGroup);
        values.put("comments", this.comments);
		values.put("comments2", this.comments2);
        values.put("violation_id", this.violationId);
        values.put("violation_desc", this.violationDesc);
        values.put("violation_code", this.violationCode);
        values.put("date_notify", DateUtil.getSQLStringFromDate2(this.notifyDate));
        values.put("first_chalk_time", DateUtil.getSQLStringFromDate2(this.firstChalkTime));
        values.put("last_seen", DateUtil.getSQLStringFromDate2(this.lastSeen));
        values.put("device_id", this.deviceId);
        values.put("zone", this.zone);
        values.put("ticket_issued", this.ticketIssued ? "Y" : "N");

        return values;
    }

    public JSONObject getJSONObject() {
        JSONObject values = new JSONObject();
        try {
            values.put("notification_id", this.notificationId);
            values.put("plate", this.plate);
            values.put("plate", this.plate);
            values.put("state", this.state);
            values.put("make", this.make);
            values.put("model", this.model);
            values.put("body", this.body);
            values.put("status", this.status);
            values.put("location", this.location);
            values.put("latitude", this.latitude);
            values.put("longitude", this.longitude);
            values.put("photo1", this.photo1);
            values.put("photo2", this.photo2);
            values.put("photo3", this.photo3);
            values.put("photo4", this.photo4);
            values.put("lpr_scan_id", this.lprScanId);
            values.put("lpr_camera_id", this.lprCameraId);
            values.put("lpr_user_id", this.lprUserId);
            values.put("color", this.color);
            values.put("permit", this.permit);
            values.put("permit_type", this.permitType);
            values.put("permit_status", this.permitStatus);
            values.put("duty_group", this.dutyGroup);
            values.put("comments", this.comments);
			values.put("comments2", this.comments2);
            values.put("violation_id", this.violationId);
            values.put("violation_desc", this.violationDesc);
            values.put("violation_code", this.violationCode);
            values.put("date_notify", DateUtil.getSQLStringFromDate2(this.notifyDate));
            values.put("first_chalk_time", DateUtil.getSQLStringFromDate2(this.firstChalkTime));
            values.put("last_seen", DateUtil.getSQLStringFromDate2(this.lastSeen));
            values.put("device_id", this.deviceId);
            values.put("zone", this.zone);
            values.put("ticket_issued", this.ticketIssued ? "Y" : "N");

        } catch (Exception e) {
            Log.e("Ticket", "Error " + e.getMessage());
        }

        return values;
    }

    public String getComments2() {
        return comments2;
    }

    public void setComments2(String comments2) {
        this.comments2 = comments2;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
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

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public String getPhoto1() {
        return photo1;
    }

    public void setPhoto1(String photo1) {
        this.photo1 = photo1;
    }

    public String getPhoto2() {
        return photo2;
    }

    public void setPhoto2(String photo2) {
        this.photo2 = photo2;
    }

    public Date getNotifyDate() {
        return notifyDate;
    }

    public void setNotifyDate(Date notifyDate) {
        this.notifyDate = notifyDate;
    }

    public String getLprScanId() {
        return lprScanId;
    }

    public void setLprScanId(String lprScanId) {
        this.lprScanId = lprScanId;
    }

    public String getLprCameraId() {
        return lprCameraId;
    }

    public void setLprCameraId(String lprCameraId) {
        this.lprCameraId = lprCameraId;
    }

    public String getLprUserId() {
        return lprUserId;
    }

    public void setLprUserId(String lprUserId) {
        this.lprUserId = lprUserId;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getPermit() {
        return permit;
    }

    public void setPermit(String permit) {
        this.permit = permit;
    }

    public String getPermitType() {
        return permitType;
    }

    public void setPermitType(String permitType) {
        this.permitType = permitType;
    }

    public String getPermitStatus() {
        return permitStatus;
    }

    public void setPermitStatus(String permitStatus) {
        this.permitStatus = permitStatus;
    }

    public String getDutyGroup() {
        return dutyGroup;
    }

    public void setDutyGroup(String dutyGroup) {
        this.dutyGroup = dutyGroup;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getViolationId() {
        return violationId;
    }

    public void setViolationId(String violationId) {
        this.violationId = violationId;
    }

    public String getViolationDesc() {
        return violationDesc;
    }

    public void setViolationDesc(String violationDesc) {
        this.violationDesc = violationDesc;
    }

    public String getViolationCode() {
        return violationCode;
    }

    public void setViolationCode(String violationCode) {
        this.violationCode = violationCode;
    }

    public String getPhoto3() {
        return photo3;
    }

    public void setPhoto3(String photo3) {
        this.photo3 = photo3;
    }

    public String getPhoto4() {
        return photo4;
    }

    public void setPhoto4(String photo4) {
        this.photo4 = photo4;
    }

    public Date getFirstChalkTime() {
        return firstChalkTime;
    }

    public void setFirstChalkTime(Date firstChalkTime) {
        this.firstChalkTime = firstChalkTime;
    }

    public Date getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(Date lastSeen) {
        this.lastSeen = lastSeen;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public boolean isTicketIssued() {
        return ticketIssued;
    }

    public void setTicketIssued(boolean ticketIssued) {
        this.ticketIssued = ticketIssued;
    }

    public static class PlateComparator implements Comparator<LPRNotify> {
        @Override
        public int compare(LPRNotify notify1, LPRNotify notify2) {
            return notify1.getPlate().compareToIgnoreCase(notify2.getPlate());
        }
    }

    public static class DateComparator implements Comparator<LPRNotify> {
        @Override
        public int compare(LPRNotify notify1, LPRNotify notify2) {
            return notify1.getNotifyDate().compareTo(notify2.getNotifyDate());
        }
    }

    public static class StatusComparator implements Comparator<LPRNotify> {
        @Override
        public int compare(LPRNotify notify1, LPRNotify notify2) {
            return notify1.getPermitStatus().compareToIgnoreCase(notify2.getPermitStatus());
        }
    }

}
