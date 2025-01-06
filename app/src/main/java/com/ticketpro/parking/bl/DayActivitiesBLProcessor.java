package com.ticketpro.parking.bl;

import static com.ticketpro.util.TPConstant.TAG;

import android.util.Log;

import com.ticketpro.api.ApiRequest;
import com.ticketpro.api.ServiceGenerator;
import com.ticketpro.exception.TPException;
import com.ticketpro.model.Duty;
import com.ticketpro.model.DutyReport;
import com.ticketpro.model.Params;
import com.ticketpro.model.RequestPOJO;
import com.ticketpro.model.SyncData;
import com.ticketpro.parking.activity.TPApplication;
import com.ticketpro.util.TPConstant;
import com.ticketpro.util.TPUtility;

import java.util.ArrayList;
import java.util.Date;

import io.reactivex.annotations.NonNull;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DayActivitiesBLProcessor extends BLProcessorImpl {

    public DayActivitiesBLProcessor(TPApplication TPApp) {
        setLogger(DayActivitiesBLProcessor.class.getName());
        setTPApplication(TPApp);
    }

    public ArrayList<Duty> getActivities() throws TPException {
        return proxy.getDuties();
    }

    public void initDayActivity(Duty duty) {
        DutyReport dutyReport = new DutyReport();
        dutyReport.setDateIn(new Date());
        dutyReport.setDutyId(duty.getId());
        dutyReport.setUserId(TPApp.getCurrentUserId());
        dutyReport.setCustId(TPApp.getCustId());
        dutyReport.setDeviceId(TPApp.getDeviceId());
        dutyReport.setDuty_report_id(System.currentTimeMillis() + "_" + TPApp.deviceId);
        TPApp.setActiveDutyReport(dutyReport);
        TPApp.setActiveDutyInfo(duty);
    }

    public void shiftActiveDuty(boolean isServiceAvailable, DutyReport dutyReport) {
        try {
            if (TPApp.getActiveDutyReport() == null)
                return;

            dutyReport.setDateOut(new Date());
            dutyReport.setCustId(TPApp.getCustId());
            DutyReport.insertDutyReport(dutyReport);
            //boolean result = false;
            if (isServiceAvailable) {
                ApiRequest apiRequest = ServiceGenerator.createRxService(ApiRequest.class);
                RequestPOJO requestPOJO = new RequestPOJO();
                requestPOJO.setMethod("updateDutyReports");
                Params params = new Params();
                ArrayList<DutyReport> reports = new ArrayList<>();
                reports.add(dutyReport);
                params.setDutyReports(reports);
                requestPOJO.setParams(params);
                apiRequest.updateDutyReport(requestPOJO).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        Log.i(TAG, "onResponse: Duty report submitted");
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                        Log.e(TAG, "onFailure: Duty report submission failed" );
                        SyncData syncData = new SyncData();
                        syncData.setActivity("INSERT");
                        try {
                            syncData.setPrimaryKey(DutyReport.getLastInsertId() + "");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        syncData.setActivityDate(new Date());
                        syncData.setCustId(TPApp.getCustId());
                        syncData.setTableName(TPConstant.TABLE_DUTY_REPORTS);
                        syncData.setStatus("Pending");

                        SyncData.insertSyncData(syncData).subscribe();
                    }
                });
//                result = proxy.updateDutyReport(dutyReport);
            }

            /*if (!result) {
                SyncData syncData = new SyncData();
                syncData.setActivity("INSERT");
                syncData.setPrimaryKey(DutyReport.getLastInsertId() + "");
                syncData.setActivityDate(new Date());
                syncData.setCustId(TPApp.getCustId());
                syncData.setTableName(TPConstant.TABLE_DUTY_REPORTS);
                syncData.setStatus("Pending");

                SyncData.insertSyncData(syncData).subscribe();
                *//*DatabaseHelper.getInstance().insertOrReplace(syncData.getContentValues(), "sync_data");
                DatabaseHelper.getInstance().closeWritableDb();*//*
            }*/
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }
}
