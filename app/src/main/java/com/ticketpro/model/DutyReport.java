package com.ticketpro.model;

import android.content.ContentValues;
import android.util.Log;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ticketpro.exception.TPException;
import com.ticketpro.parking.activity.TPApplication;
import com.ticketpro.parking.dao.ParkingDatabase;
import com.ticketpro.util.DateUtil;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import io.reactivex.Completable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

@Entity(tableName = "duty_reports")
public class DutyReport implements Serializable {
    @Ignore
    private static final long serialVersionUID = 1L;
    @PrimaryKey
    @ColumnInfo(name = "report_id")
    @SerializedName("report_id")
    @Expose
    private int reportId;
    @ColumnInfo(name = "userid")
    @SerializedName("userid")
    @Expose
    private int userId;
    @ColumnInfo(name = "custid")
    @SerializedName("custid")
    @Expose
    private int custId;
    @ColumnInfo(name = "duty_id")
    @SerializedName("duty_id")
    @Expose
    private int dutyId;
    @ColumnInfo(name = "date_in")
    @SerializedName("date_in")
    @Expose
    private Date dateIn;
    @ColumnInfo(name = "date_out")
    @SerializedName("date_out")
    @Expose
    private Date dateOut;
    @ColumnInfo(name = "signature")
    @SerializedName("signature")
    @Expose
    private String signature;
    @Ignore
    private String dutyTitle;
    @ColumnInfo(name = "device_id")
    @SerializedName("device_id")
    @Expose
    private int deviceId;
    @ColumnInfo(name = "duty_report_id")
    @SerializedName("duty_report_id")
    @Expose
    private String duty_report_id;
    @ColumnInfo(name = "sync_status")
    @SerializedName("sync_status")
    @Expose
    private String sync_status;

    public DutyReport() {

    }

    public DutyReport(JSONObject object) throws Exception {

        this.setReportId(object.getInt("report_id"));
        this.setUserId(object.getInt("userid"));
        this.setCustId(!object.isNull("custid") ? object.getInt("custid") : 0);
        this.setDutyId(!object.isNull("duty_id") ? object.getInt("duty_id") : 0);
        this.setDateIn(DateUtil.getDateFromSQLString(object.getString("date_in")));
        this.setDateOut(DateUtil.getDateFromSQLString(object.getString("date_out")));
        this.setSignature(object.getString("signature"));
    }

    public static ArrayList<DutyReport> getDutyReports(int userId) throws TPException {
        ArrayList<DutyReport> list = new ArrayList<DutyReport>();
        list = (ArrayList<DutyReport>) ParkingDatabase.getInstance(TPApplication.getInstance()).dutyReportsDao().getDutyReports(userId);
        return list;
    }

    public static DutyReport getDutyReportByPrimaryKey(String primaryKey) throws TPException {
        DutyReport object = null;
        object = ParkingDatabase.getInstance(TPApplication.getInstance()).dutyReportsDao().getDutyReportByPrimaryKey(primaryKey);
        return object;
    }

    public static int getLastInsertId() throws Exception {
        int lastId;
        lastId = ParkingDatabase.getInstance(TPApplication.getInstance()).dutyReportsDao().getLastInsertId();
        return lastId;
    }

    public static void removeAll() throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).dutyReportsDao().removeAll();
    }

    public static void removeById(int id) throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).dutyReportsDao().removeById(id);
    }

    public static void insertDutyReport(final DutyReport DutyReport) {
        final ParkingDatabase database = ParkingDatabase.getInstance(TPApplication.getInstance());
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                database.dutyReportsDao().insertDutyReport(DutyReport).subscribeOn(Schedulers.io()).subscribe();
            }
        }).subscribeOn(Schedulers.io()).subscribe();
    }

    public String getDuty_report_id() {
        return duty_report_id;
    }

    public void setDuty_report_id(String duty_report_id) {
        this.duty_report_id = duty_report_id;
    }

    public String getSync_status() {
        return sync_status;
    }

    public void setSync_status(String sync_status) {
        this.sync_status = sync_status;
    }

    public ContentValues getContentValues() throws Exception {
        ContentValues values = new ContentValues();
        if (this.reportId != 0)
            values.put("report_id", this.reportId);

        values.put("userid", this.userId);
        values.put("custid", this.custId);
        values.put("duty_id", this.dutyId);
        values.put("date_in", DateUtil.getSQLStringFromDate2(this.dateIn));
        values.put("date_out", DateUtil.getSQLStringFromDate2(this.dateOut));
        values.put("signature", this.signature);
        values.put("device_id", this.deviceId);
        return values;
    }

    public JSONObject getJSONObject() {
        JSONObject object = new JSONObject();
        try {
            object.put("report_id", this.reportId);
            object.put("userid", this.userId);
            object.put("custid", this.custId);
            object.put("duty_id", this.dutyId);
            object.put("date_in", DateUtil.getSQLStringFromDate2(this.dateIn));
            object.put("date_out", DateUtil.getSQLStringFromDate2(this.dateOut));
            object.put("signature", this.signature);
            object.put("device_id", this.deviceId);
            object.put("duty_report_id", this.duty_report_id);

        } catch (Exception e) {
            Log.e("DutyReport", "Error " + e.getMessage());
        }

        return object;
    }

    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getDutyId() {
        return dutyId;
    }

    public void setDutyId(int dutyId) {
        this.dutyId = dutyId;
    }

    public Date getDateIn() {
        return dateIn;
    }

    public void setDateIn(Date dateIn) {
        this.dateIn = dateIn;
    }

    public Date getDateOut() {
        return dateOut;
    }

    public void setDateOut(Date dateOut) {
        this.dateOut = dateOut;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getDutyTitle() {
        return dutyTitle;
    }

    public void setDutyTitle(String dutyTitle) {
        this.dutyTitle = dutyTitle;
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


}
