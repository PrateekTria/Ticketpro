package com.ticketpro.model;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.ticketpro.exception.TPException;
import com.ticketpro.parking.activity.TPApplication;
import com.ticketpro.parking.dao.ParkingDatabase;
import com.ticketpro.util.DateUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

@Entity(tableName = "call_in_reports")
public class CallInReport {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "report_id")
    private int reportId;
    @ColumnInfo(name = "userid")
    private int userId;
    @ColumnInfo(name = "custid")
    private int custid;
    @ColumnInfo(name = "call_in_date")
    private Date callInDate;
    @ColumnInfo(name = "plate")
    private String plate;
    @ColumnInfo(name = "plate_type")
    private String plateType;
    @ColumnInfo(name = "state")
    private String state;
    @ColumnInfo(name = "from_search")
    private String fromSearch;
    @ColumnInfo(name = "from_hit")
    private String fromHit;
    @ColumnInfo(name = "action_taken")
    private String actionTaken;
    @ColumnInfo(name = "photo1")
    private String photo1;
    @ColumnInfo(name = "photo2")
    private String photo2;
    @ColumnInfo(name = "photo3")
    private String photo3;

    public CallInReport() {

    }

    public CallInReport(JSONObject object) throws Exception {

        this.setReportId(object.getInt("report_id"));
        this.setCallInDate(DateUtil.getDateFromSQLString(object.getString("call_in_date")));
        this.setUserId(object.getInt("userid"));
        this.setCustid(!object.isNull("custid") ? object.getInt("custid") : 0);
        this.setPlate(object.getString("plate"));
        this.setPlateType(object.getString("plate_type"));
        this.setState(object.getString("state"));
        this.setFromSearch(object.getString("from_search"));
        this.setFromHit(object.getString("from_hit"));
        this.setActionTaken(object.getString("action_taken"));
        this.setPhoto1(object.getString("photo1"));
        this.setPhoto2(object.getString("photo2"));
        this.setPhoto3(object.getString("photo3"));

    }

    public static ArrayList<CallInReport> getCallInReports() throws Exception {
        return (ArrayList<CallInReport>) ParkingDatabase.getInstance(TPApplication.getInstance()).callInListReportDao().getCallInReports();
    }

    public static CallInReport getCallInReportByPrimaryKey(String primaryKey) throws TPException {
        return ParkingDatabase.getInstance(TPApplication.getInstance()).callInListReportDao().getCallInReportByPrimaryKey(primaryKey);
    }

    public static int getLastInsertId() throws Exception {
        return ParkingDatabase.getInstance(TPApplication.getInstance()).callInListReportDao().getLastInsertId();
    }

    public static void removeAll() throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).callInListReportDao().removeAll();
    }

    public static void removeById(int id) throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).callInListReportDao().removeById(id);
    }

    public static void insertCallInReport(CallInReport CallInReport) {
        new CallInReport.InsertCallInReport(CallInReport).execute();
    }

    public ContentValues getContentValues() throws Exception {
        ContentValues values = new ContentValues();
        values.put("report_id", this.reportId);
        values.put("call_in_date", DateUtil.getSQLStringFromDate2(this.callInDate));
        values.put("userid", this.userId);
        values.put("custid", this.custid);
        values.put("plate", this.plate);
        values.put("plate_type", this.plateType);
        values.put("state", this.state);
        values.put("from_search", this.fromSearch);
        values.put("from_hit", this.fromHit);
        values.put("action_taken", this.actionTaken);
        values.put("photo1", this.photo1);
        values.put("photo2", this.photo2);
        values.put("photo3", this.photo3);

        return values;
    }

    public JSONObject getJSONObject() {
        JSONObject object = new JSONObject();
        try {
            object.put("report_id", this.reportId);
            object.put("call_in_date", DateUtil.getSQLStringFromDate2(this.callInDate));
            object.put("userid", this.userId);
            object.put("custid", this.custid);
            object.put("plate", this.plate);
            object.put("plate_type", this.plateType);
            object.put("state", this.state);
            object.put("from_search", this.fromSearch);
            object.put("from_hit", this.fromHit);
            object.put("action_taken", this.actionTaken);
            object.put("photo1", this.photo1);
            object.put("photo2", this.photo2);
            object.put("photo3", this.photo3);

        } catch (Exception e) {
            Log.e("CallInReport", "Error " + e.getMessage());
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

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getPlateType() {
        return plateType;
    }

    public void setPlateType(String plateType) {
        this.plateType = plateType;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getFromSearch() {
        return fromSearch;
    }

    public void setFromSearch(String fromSearch) {
        this.fromSearch = fromSearch;
    }

    public String getFromHit() {
        return fromHit;
    }

    public void setFromHit(String fromHit) {
        this.fromHit = fromHit;
    }

    public String getActionTaken() {
        return actionTaken;
    }

    public void setActionTaken(String actionTaken) {
        this.actionTaken = actionTaken;
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

    public String getPhoto3() {
        return photo3;
    }

    public void setPhoto3(String photo3) {
        this.photo3 = photo3;
    }

    public Date getCallInDate() {
        return callInDate;
    }

    public void setCallInDate(Date callInDate) {
        this.callInDate = callInDate;
    }

    public int getCustid() {
        return custid;
    }

    public void setCustid(int custid) {
        this.custid = custid;
    }

    private static class InsertCallInReport extends AsyncTask<Void, Void, Void> {
        private CallInReport CallInReport;

        public InsertCallInReport(CallInReport CallInReport) {
            this.CallInReport = CallInReport;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ParkingDatabase.getInstance(TPApplication.getInstance()).callInListReportDao().insertCallInReport(CallInReport);
            return null;
        }
    }

}
