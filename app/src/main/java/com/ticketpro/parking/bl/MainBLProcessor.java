package com.ticketpro.parking.bl;


import android.util.Log;
import android.widget.Toast;

import com.ticketpro.model.DutyReport;
import com.ticketpro.model.SyncData;
import com.ticketpro.parking.activity.TPApplication;
import com.ticketpro.util.TPConstant;
import com.ticketpro.util.TPUtility;

import java.util.Date;

public class MainBLProcessor extends BLProcessorImpl {

    public MainBLProcessor(TPApplication TPApp) {
        setLogger(BLProcessorImpl.class.getName());
        setTPApplication(TPApp);
    }

    public String getVersionNo() {
        return proxy.getVersionNo();
    }

    public void closeActiveDuty(boolean isServiceAvailable) {
        try {
            if (TPApp.getActiveDutyReport() == null) {
                TPApp.resetUserSession();
                return;
            }

            TPApp.getActiveDutyReport().setDateOut(new Date());
            TPApp.getActiveDutyReport().setCustId(TPApp.getCustId());
            DutyReport.insertDutyReport(TPApp.getActiveDutyReport());
			/*DatabaseHelper.getInstance().openWritableDatabase();
			DatabaseHelper.getInstance().insertOrReplace(TPApp.getActiveDutyReport().getContentValues(), "duty_reports");*/

            boolean result = false;
            if (isServiceAvailable) {
                result = proxy.updateDutyReport(TPApp.getActiveDutyReport());
            }

            if (!result) {
                SyncData syncData = new SyncData();
                syncData.setActivity("INSERT");
                syncData.setPrimaryKey(DutyReport.getLastInsertId() + "");
                syncData.setActivityDate(new Date());
                syncData.setCustId(TPApp.getCustId());
                syncData.setTableName(TPConstant.TABLE_DUTY_REPORTS);
                syncData.setStatus("Pending");
                SyncData.insertSyncData(syncData).subscribe();
                //DatabaseHelper.getInstance().insertOrReplace(syncData.getContentValues(), "sync_data");
                //DatabaseHelper.getInstance().closeWritableDb();
            }

            // Close User Session
            TPApp.resetUserSession();
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }

    public void closeActiveDuty1(boolean isServiceAvailable) {
        try {
            if (TPApp.getActiveDutyReport() == null) {
                TPApp.resetUserSession();
                return;
            }

            TPApp.getActiveDutyReport().setDateOut(new Date());
            TPApp.getActiveDutyReport().setCustId(TPApp.getCustId());
            DutyReport.insertDutyReport(TPApp.getActiveDutyReport());
			/*DatabaseHelper.getInstance().openWritableDatabase();
			DatabaseHelper.getInstance().insertOrReplace(TPApp.getActiveDutyReport().getContentValues(), "duty_reports");*/

            boolean result = false;
            if (isServiceAvailable) {
               // result = proxy.updateDutyReport(TPApp.getActiveDutyReport());
                proxy.updateDutyReport1(TPApp.getActiveDutyReport(),TPApp.getCustId());
            }
            else{

                    SyncData syncData = new SyncData();
                    syncData.setActivity("INSERT");
                    syncData.setPrimaryKey(DutyReport.getLastInsertId() + "");
                    syncData.setActivityDate(new Date());
                    syncData.setCustId(TPApp.getCustId());
                    syncData.setTableName(TPConstant.TABLE_DUTY_REPORTS);
                    syncData.setStatus("Pending");
                    SyncData.insertSyncData(syncData).subscribe();
                    Log.e("Save To Local", "Duty Report Updated" );
                    //DatabaseHelper.getInstance().insertOrReplace(syncData.getContentValues(), "sync_data");
                    //DatabaseHelper.getInstance().closeWritableDb();


            }


            // Close User Session
            TPApp.resetUserSession();
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }


    public void shiftActiveDuty() {
        try {
            if (TPApp.getActiveDutyReport() == null)
                return;

            TPApp.getActiveDutyReport().setDateOut(new Date());
            TPApp.getActiveDutyReport().setCustId(TPApp.getCustId());
            DutyReport.insertDutyReport(TPApp.getActiveDutyReport());
			/*DatabaseHelper.getInstance().openWritableDatabase();
			DatabaseHelper.getInstance().insertOrReplace(TPApp.getActiveDutyReport().getContentValues(), "duty_reports");*/

            SyncData syncData = new SyncData();
            syncData.setActivity("INSERT");
            syncData.setPrimaryKey(DutyReport.getLastInsertId() + "");
            syncData.setActivityDate(new Date());
            syncData.setCustId(TPApp.getCustId());
            syncData.setTableName(TPConstant.TABLE_DUTY_REPORTS);
            syncData.setStatus("Pending");
            SyncData.insertSyncData(syncData).subscribe();
			/*DatabaseHelper.getInstance().insertOrReplace(syncData.getContentValues(), "sync_data");
			DatabaseHelper.getInstance().closeWritableDb();*/

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            ;
        }
    }


    public void syncServices() {
        try {
            proxy.updateUserServices(false);
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }
}
