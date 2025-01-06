package com.ticketpro.model;

import android.content.ContentValues;
import android.os.AsyncTask;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ticketpro.exception.TPException;
import com.ticketpro.parking.activity.TPApplication;
import com.ticketpro.parking.dao.ParkingDatabase;

import org.json.JSONObject;

import java.util.ArrayList;

import io.reactivex.Completable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

@Entity(tableName = "repeat_offenders")
public class RepeatOffender {
    @PrimaryKey
    @ColumnInfo(name = "repeat_offender_id")
    @SerializedName("repeat_offender_id")
    @Expose
    private int repeatOffenderId;
    @ColumnInfo(name = "custid")
    @SerializedName("custid")
    @Expose
    private int custId;
    @ColumnInfo(name = "plate")
    @SerializedName("plate")
    @Expose
    private String plate;
    @ColumnInfo(name = "violation")
    @SerializedName("violation")
    @Expose
    private String violation;
    @ColumnInfo(name = "count")
    @SerializedName("count")
    @Expose
    private int count;
    @ColumnInfo(name = "violation_id")
    @SerializedName("violation_id")
    @Expose
    private int violationId;
    @ColumnInfo(name = "state_code")
    @SerializedName("state_code")
    @Expose
    private String stateCode;
    @ColumnInfo(name = "created_date")
    @SerializedName("created_date")
    @Expose
    private String creatTime;
    @ColumnInfo(name = "sync_status")
    @SerializedName("sync_status")
    @Expose
    private String syncStatus;
    @SerializedName("sync_data_id")
    @Expose
    @Ignore
    private int syncDataId;
    @SerializedName("primary_key")
    @Expose
    @Ignore
    private int primaryKey;

    public RepeatOffender() {

    }

    public RepeatOffender(JSONObject object) throws Exception {
        // this.setRepeatOffenderId(object.getInt("repeat_offender_id"));
        this.setCustId(object.getInt("custid"));
        this.setPlate(object.getString("plate"));
        this.setViolation(object.getString("violation"));
        this.setCount(!object.isNull("count") ? object.getInt("count") : 0);
        this.setViolationId(!object.isNull("violation_id") ? object.getInt("violation_id") : 0);
        this.setStateCode(object.getString("state_code"));
        this.setCreatTime(object.getString("created_date"));
    }

    public RepeatOffender(JSONObject object, String plate, int id, String stateCode) throws Exception {
        if (stateCode.equalsIgnoreCase(object.getString("state_code")) && plate.equalsIgnoreCase(object.getString("plate")) && id == object.getInt("custid")) {
            // this.setRepeatOffenderId(object.getInt("repeat_offender_id"));
            this.setCustId(object.getInt("custid"));
            this.setPlate(object.getString("plate"));
            this.setViolation(object.getString("violation"));
            this.setCount(!object.isNull("count") ? object.getInt("count") : 0);
            this.setViolationId(!object.isNull("violation_id") ? object.getInt("violation_id") : 0);
            this.setStateCode(object.getString("state_code"));
            this.setCreatTime(object.getString("created_date"));
        }
    }

    public static RepeatOffender searchRepeatOffender(String plate, int violationId, String stateCode)
            throws Exception {
        RepeatOffender object = null;
        object = ParkingDatabase.getInstance(TPApplication.getInstance()).repeatOffendersDao().searchRepeatOffender(plate, violationId, stateCode);
        return object;
    }

    public static ArrayList<RepeatOffender> getRepeatOffender() throws TPException {
        ArrayList<RepeatOffender> offenderArrayList = new ArrayList<>();
        offenderArrayList = (ArrayList<RepeatOffender>) ParkingDatabase.getInstance(TPApplication.getInstance()).repeatOffendersDao().getRepeatOffender();
        return offenderArrayList;
    }

    public static void removeAll() throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).repeatOffendersDao().removeAll();
    }

    public static void removeById(int id) throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).repeatOffendersDao().removeById(id);
    }

    public static void updateRepeatOffender(int custId, String state_code, String plate, int violation_id, String status) throws TPException {

        if (status != null && !status.isEmpty()) {
            ParkingDatabase.getInstance(TPApplication.getInstance()).repeatOffendersDao().updateRepeatOffender(custId, state_code, plate, violation_id, status);
        } else {
            ParkingDatabase.getInstance(TPApplication.getInstance()).repeatOffendersDao().updateRepeatOffenders(custId, state_code, plate, violation_id);
        }
    }

    public static void countUpdateVoidCase(int custId, String state_code, String plate, int violation_id) {
        ParkingDatabase.getInstance(TPApplication.getInstance()).repeatOffendersDao().countUpdateVoidCase(custId, state_code, plate, violation_id);
    }

    public static boolean checkIsDataAlreadyInDBorNot(int custId, String state_code, String plate, int violation_id) throws TPException {
        ArrayList<RepeatOffender> repeatOffenders = (ArrayList<RepeatOffender>) ParkingDatabase.getInstance(TPApplication.getInstance()).repeatOffendersDao().checkIsDataAlreadyInDBorNot(custId, state_code, plate, violation_id);
        return repeatOffenders.size() > 0;
    }

    public static void insertUpdate(int custId, String plate, String violation, int count, int violation_id, String state_code, String created_date) {
        ParkingDatabase.getInstance(TPApplication.getInstance()).repeatOffendersDao().insertUpdate(custId, plate, violation, count, violation_id, state_code, created_date);
    }

    public static void updateInsert(int custId, String state_code, String plate, int violation_id) {
        ParkingDatabase.getInstance(TPApplication.getInstance()).repeatOffendersDao().updateInsert(custId, state_code, plate, violation_id);
    }

    public static Completable insertRepeatOffender(final RepeatOffender RepeatOffender) {
        final ParkingDatabase instance = ParkingDatabase.getInstance(TPApplication.getInstance());
        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                instance.repeatOffendersDao().insertRepeatOffender(RepeatOffender).subscribeOn(Schedulers.io()).subscribe();
            }
        });
        //new RepeatOffender.InsertRepeatOffender(RepeatOffender).execute();
    }

    public int getSyncDataId() {
        return syncDataId;
    }

    public void setSyncDataId(int syncDataId) {
        this.syncDataId = syncDataId;
    }

    public int getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(int primaryKey) {
        this.primaryKey = primaryKey;
    }



    public JSONObject getRepeatOffenderJson() throws Exception {

        JSONObject values = new JSONObject();
        // values.put("repeat_offender_id", this.repeatOffenderId);
        values.put("custid", this.custId);
        values.put("plate", this.plate);
        values.put("violation", this.violation);
        values.put("count", this.count);
        values.put("violation_id", this.violationId);
        values.put("state_code", this.stateCode);
        values.put("created_date", this.creatTime);
        values.put("sync_status", this.syncStatus);
        return values;

    }

    public ContentValues getContentValues() throws Exception {

        ContentValues values = new ContentValues();
        //values.put("repeat_offender_id", this.repeatOffenderId);
        values.put("custid", this.custId);
        values.put("plate", this.plate);
        values.put("violation", this.violation);
        values.put("count", this.count);
        values.put("violation_id", this.violationId);
        values.put("state_code", this.stateCode);
        values.put("created_date", this.creatTime);
        values.put("sync_status", this.syncStatus);
        return values;

    }

    public ContentValues getContentValues1() throws Exception {

        ContentValues values = new ContentValues();
        values.put("custid", this.custId);
        values.put("plate", this.plate);
        values.put("violation", this.violation);
        values.put("count", this.count);
        values.put("violation_id", this.violationId);
        values.put("state_code", this.stateCode);
        values.put("created_date", this.creatTime);
        values.put("sync_status", this.syncStatus);
        return values;

    }

    public String getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(String creatTime) {
        this.creatTime = creatTime;
    }

    public String getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(String syncStatus) {
        this.syncStatus = syncStatus;
    }

    public int getRepeatOffenderId() {
        return repeatOffenderId;
    }

    public void setRepeatOffenderId(int repeatOffenderId) {
        this.repeatOffenderId = repeatOffenderId;
    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getViolation() {
        return violation;
    }

    public void setViolation(String violation) {
        this.violation = violation;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getViolationId() {
        return violationId;
    }

    public void setViolationId(int violationId) {
        this.violationId = violationId;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

}
