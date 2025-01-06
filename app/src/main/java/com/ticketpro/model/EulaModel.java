package com.ticketpro.model;

import android.os.AsyncTask;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.ticketpro.parking.activity.TPApplication;
import com.ticketpro.parking.dao.ParkingDatabase;

import org.json.JSONObject;

import java.io.Serializable;

@Entity(tableName = "tpm_eula")
public class EulaModel implements Serializable {
    @PrimaryKey
    @ColumnInfo(name = "rec_id")
    private int resId;
    @ColumnInfo(name = "EULA_text")
    private String eulaText;
    @ColumnInfo(name = "Entry_date")
    private String entryDate;

    public EulaModel() {
    }

    @ColumnInfo(name = "Effective_date")
    private String effectiveDate;
    @ColumnInfo(name = "cust_id")
    private int custId;
    @ColumnInfo(name = "module_id")
    private int moduleId;
    @Ignore
    private String isActivity;
    @Ignore
    private String message;
    @ColumnInfo(name = "is_active")
    private String isActive;

    @Ignore
    private String userId;


    public EulaModel(JSONObject object) throws Exception {
        //this.setCustId(object.getInt("custid"));
        JSONObject eula = object.getJSONObject("eula");
        this.setResId(eula.getInt("eula_id"));
        this.setEulaText(eula.getString("eula_text"));
        this.setMessage(object.getString("EulaAcceptedByCust"));

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public String getEulaText() {
        return eulaText;
    }

    public void setEulaText(String eulaText) {
        this.eulaText = eulaText;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public int getModuleId() {
        return moduleId;
    }

    public void setModuleId(int moduleId) {
        this.moduleId = moduleId;
    }

    public String getIsActivity() {
        return isActivity;
    }

    public void setIsActivity(String isActivity) {
        this.isActivity = isActivity;
    }

    public String getIsActive() {
        return isActive;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public static void insertEulaModel(EulaModel EulaModel){
        new EulaModel.InsertEulaModel(EulaModel).execute();
    }

    private static class InsertEulaModel extends AsyncTask<Void,Void,Void> {
        private EulaModel EulaModel;

        public InsertEulaModel(EulaModel EulaModel) {
            this.EulaModel = EulaModel;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ParkingDatabase.getInstance(TPApplication.getInstance()).tpmEulaDao().insertEulaModel(EulaModel);
            return null;
        }
    }
}
