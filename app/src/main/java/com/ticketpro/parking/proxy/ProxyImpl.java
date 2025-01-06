package com.ticketpro.parking.proxy;

import static android.content.Context.MODE_PRIVATE;
import static com.ticketpro.model.RepeatOffender.insertUpdate;
import static com.ticketpro.model.RepeatOffender.updateInsert;
import static com.ticketpro.model.RepeatOffender.updateRepeatOffender;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteException;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ticketpro.api.ApiRequest;
import com.ticketpro.api.ServiceGenerator;
import com.ticketpro.exception.TPException;
import com.ticketpro.model.*;
import com.ticketpro.model.devicefeature.ResponseResult;
import com.ticketpro.parking.activity.BaseActivityImpl;
import com.ticketpro.parking.activity.TPApplication;
import com.ticketpro.parking.api.WriteTicketNetworkCalls;
import com.ticketpro.parking.service.ServiceHandler;
import com.ticketpro.parking.service.ServiceHandlerImpl;
import com.ticketpro.util.DateUtil;
import com.ticketpro.util.LongDeserializer;
import com.ticketpro.util.StringUtil;
import com.ticketpro.util.TPConstant;
import com.ticketpro.util.TPUtility;

import org.alexd.jsonrpc.JSONRPCException;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProxyImpl implements Proxy {

    private static final String TAG = "PROXYIMPL";
    static Logger log = Logger.getLogger("ProxyImpl");
    boolean syncStatus = true;
    boolean observable1 = false, observable2 = false, observable3 = false, observable4 = false, observable5 = false, observable6 = false,observable7 = false;
    private ServiceHandler service;

    public ProxyImpl() {
        setServiceHandler(new ServiceHandlerImpl());
    }

    @Override
    public String getLastSynchTime() {
        return "Last Synched : NA";
    }

    public ServiceHandler getService() {
        return service;
    }

    public void setService(ServiceHandler service) {
        this.service = service;
    }

    @Override
    public boolean syncDatabase(final boolean fullSync, Context context, Handler.Callback callback) throws TPException {
        if (TPApplication.getInstance().updateCheckInProgress) {
            return true;
        }
        // Sync Local DatabaseupdateAllTablesupdateAllTables.
        try {
            Thread thread = new Thread(() -> {
                try {
                    updateAllTables(fullSync, message -> {
                        try {
                            updateTicketHistory();
                        } catch (TPException e) {
                            e.printStackTrace();
                        }
                        if (callback != null) {
                            callback.handleMessage(message);
                        }
                        // Clear Memory
                        System.gc();
                        return false;
                    });
                } catch (TPException e) {
                    e.printStackTrace();
                }
            });
            thread.start();
            thread.join();

            // Sync Server Database.
            try {
                uploadAllChanges(context, fullSync);
                // Verify pending tickets and upload them
                verifyAndUploadTickets(context, fullSync);
                // Clear Memory
                System.gc();
            } catch (Exception e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }
            // Send Error Reports...
            try {
                ArrayList<ErrorLog> errors = ErrorLog.getErrorLogs();
                service.sendErrorLog(errors);
                ErrorLog.removeAll();
            } catch (Exception e) {
                log.error(TPUtility.getPrintStackTrace(e));
                //   return true;
            }
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            // return true;
        }
        return false;
    }

    @Override
    public ServiceResult isRegisteredDevice(String deviceName) throws TPException {
        try {
            TPApplication TPApp = TPApplication.getInstance();
            ServiceResult result = new ServiceResult();
            DeviceInfo deviceInfo;
            if (TPApp.dbConfigured) {
                // Get Device Info
                deviceInfo = DeviceInfo.getDeviceInfo(deviceName);
                if (deviceInfo == null) {
                    result = checkDeviceOnServer(deviceName);
                    if (result.isResult()) {
                        deviceInfo = (DeviceInfo) result.getData();
                    } else {
                        return result;
                    }
                }

                if (deviceInfo != null) {
                    TPApp.setDeviceInfo(deviceInfo);

                    CustomerInfo customerInfo = CustomerInfo.getCustomerInfo(deviceInfo.getCustId());
                    if (customerInfo == null) {
                        JSONObject clientInfo = service.getClientInfo(deviceInfo.getCustId());
                        if (clientInfo != null) {
                            customerInfo = new CustomerInfo(clientInfo);
                        }
                    }

                    if (customerInfo != null) {
                        TPApp.setCustomerInfo(customerInfo);
                        TPApp.custId = customerInfo.getCustId();
                        TPApp.deviceId = deviceInfo.getDeviceId();

                        result.setResult(true);
                        result.setResultCode(ServiceResult.SUCCESSFULL);
                        return result;

                    } else {
                        TPException appEx = new TPException();
                        appEx.setErrorCode(TPException.DB_SYNC_ERROR);
                        appEx.setErrorMessage("Database Error. Please sync your device to reinitialize it.");
                        throw appEx;
                    }
                }
            }

            result = checkDeviceOnServer(deviceName);
            if (result.isResult()) {
                deviceInfo = (DeviceInfo) result.getData();
            } else {
                return result;
            }

            // Get Customer Info
            JSONObject clientInfo = service.getClientInfo(deviceInfo.getCustId());
            CustomerInfo customerInfo = new CustomerInfo(clientInfo);
            TPApp.setCustomerInfo(customerInfo);
            TPApp.custId = customerInfo.getCustId();
            TPApp.deviceId = deviceInfo.getDeviceId();

            result.setResult(true);
            result.setResultCode(ServiceResult.SUCCESSFULL);
            return result;

        } catch (TPException e) {
            log.error(TPUtility.getPrintStackTrace(e));
            throw e;
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            TPException appEx = new TPException();
            appEx.setErrorMessage("Failed to get device registration status. Please check your network settings.");
            throw appEx;
        }
    }

    private ServiceResult checkDeviceOnServer(String deviceName) throws Exception {
        ServiceResult result = new ServiceResult();
        //Fetch database from services.
        JSONObject object = service.getDeviceInfo(deviceName);
        if (object == null || !object.isNull("notRegistered") || !object.isNull("serviceError")) {
            result.setResult(false);
            result.setResultCode(ServiceResult.DATA_NOT_AVAILABLE);
            return result;
        }

        if (!object.isNull("notActive")) {
            result.setResult(false);
            result.setResultCode(ServiceResult.NOT_ACTIVE);
            return result;
        }

        // Get Device Info
        DeviceInfo deviceInfo = new DeviceInfo(object);
        TPApplication.getInstance().setDeviceInfo(deviceInfo);
        DeviceInfo.insertDeviceInfo(deviceInfo);

        result.setResultCode(ServiceResult.SUCCESSFULL);
        result.setResult(true);
        result.setData(deviceInfo);

        return result;
    }

    @Override
    public DeviceInfo registeredDevice(DeviceInfo device) throws TPException {
        try {
            JSONObject deviceJSON = device.getJSONObject();
            JSONObject deviceObj = service.registerDevice(deviceJSON);
            if (deviceObj == null) {
                return null;
            }
            return new DeviceInfo(deviceObj);

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }

        return null;
    }

    @Override
    public void updateDeviceInfo(Context context) throws TPException {
        JSONArray deviceArray = new JSONArray();
        ArrayList<DeviceInfo> devices = DeviceInfo.getDevices();
        for (DeviceInfo device : devices) {
            JSONObject deviceJSON = device.getJSONObject();
            deviceArray.put(deviceJSON);
        }
        Log.i(TAG, "updateDeviceInfo: " + devices.get(0).getAppVersion());
        service.syncDevices(deviceArray);
    }

    public void syncTicket(Ticket ticket) throws InterruptedException {
        ArrayList<Ticket> tickets = new ArrayList<>();
        long ticketId = ticket.getTicketId();
        long citationNumber = ticket.getCitationNumber();
        try {
            ArrayList<TicketComment> comments = TicketComment.getTicketComments(ticketId, citationNumber);
            ticket.setTicketComments(comments);
            ArrayList<TicketPicture> pictures = TicketPicture.getTicketPictures(ticketId, citationNumber);
            ArrayList<TicketPicture> ticketPictures = new ArrayList<>();
            for (TicketPicture picture : pictures) {
                String[] path = picture.getImagePath().split("/");
                picture.setImagePath(path[path.length - 1]);
                ticketPictures.add(picture);
            }
            ticket.setTicketPictures(ticketPictures);
            ArrayList<TicketViolation> violations = TicketViolation.getTicketViolations(ticketId, citationNumber);
            ticket.setTicketViolations(violations);
        } catch (Exception e) {
            e.printStackTrace();
        }
        tickets.add(ticket);
        WriteTicketNetworkCalls.syncTickets(tickets);
        /*SyncTicketTask task = new SyncTicketTask(service);
        task.execute(ticket);*/
    }

    @Override
    public void verifyAndUploadTickets(Context context, boolean fullSync) throws TPException {
        if (!TPApplication.getInstance().dbConfigured || TPApplication.getInstance().updateCheckInProgress) {
            return;
        }

        // Update tickets
        ArrayList<DeviceInfo> deviceInfo = new ArrayList<>();
        ArrayList<DeviceInfo> devices = DeviceInfo.getDevices();
        ArrayList<String> pendingCitations = Ticket.getPendingCitations();
        if (!pendingCitations.isEmpty()) {
            try {
                ApiRequest api = ServiceGenerator.createRxService(ApiRequest.class);
                RequestPOJO requestPOJO = new RequestPOJO();
                requestPOJO.setMethod("verifyAndSyncTickets");
                Params params = new Params();
                params.setCustId(TPApplication.getInstance().custId);
                params.setCitations(pendingCitations);
                requestPOJO.setParams(params);
                api.verifyAndSyncTickets(requestPOJO).enqueue(new Callback<VerifyAndSyncTicketsResponse>() {
                    @Override
                    public void onResponse(@NotNull Call<VerifyAndSyncTicketsResponse> call, @NotNull Response<VerifyAndSyncTicketsResponse> response) {
                        try {
                            Log.i(TAG, "onResponse:");
                            if (response.code()==500)
                                return;
                            ArrayList<String> missingCitations = new ArrayList<>();
                            if (response.body() != null && !response.body().equals("")) {
                                missingCitations = (ArrayList<String>) response.body().getResult();
                            }
                            List<String> syncedCitations = new ArrayList<>(Ticket.getPendingCitations());
                            syncedCitations.removeAll(missingCitations);
                            for (String citation : syncedCitations) {
                                Ticket.updateTicket(citation, "S");
                            }
                            try {
                                for (String citation : missingCitations) {
                                    syncTicket(Ticket.getTicketByCitationWithViolations(Long.parseLong(citation)));
                                }
                            } catch (TPException e) {
                                e.printStackTrace();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<VerifyAndSyncTicketsResponse> call, Throwable t) {
                        log.error(TPUtility.getPrintStackTrace(t));
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Update device info
            if (devices != null && TPApplication.getInstance().dbConfigured) {
                try {
                    for (DeviceInfo device : devices) {
                        device.setLastSync(new Date());
                        deviceInfo.add(device);
                    }

                    WriteTicketNetworkCalls.syncDevices(deviceInfo, "verifyAndUploadTickets");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }

    @Override
    public void uploadAllChanges(Context context, boolean fullSync) throws TPException {
        JSONArray deviceArray = new JSONArray();
        ArrayList<DeviceInfo> devices = DeviceInfo.getDevices();
        if (devices != null) {
            for (DeviceInfo device : devices) {
                device.setLastSync(new Date());
                JSONObject deviceJSON = device.getJSONObject();
                deviceArray.put(deviceJSON);
            }
            WriteTicketNetworkCalls.syncDevices(devices, "uploadAllChanges");
            //service.syncDevices(deviceArray);
        }

        JSONArray settingsArray = new JSONArray();
        UserSetting settings = UserSetting.getUserSettings(TPApplication.getInstance().userId);
        if (settings != null) {
            try{
            JSONObject settingsJSON = settings.getJSONObject();

            settingsArray.put(settingsJSON);
            // Ensure user_prefs is correctly formatted
            if (settingsJSON.has("user_prefs")) {
                String userPrefs = settingsJSON.getString("user_prefs");
                userPrefs = StringUtil.escapeQuotes(userPrefs.toString());
                // Parse it back to ensure correct formatting
                JSONObject parsedUserPrefs = new JSONObject(userPrefs);
                Log.e("parsedUserPrefs>>>>>>",parsedUserPrefs.toString());
                settingsJSON.put("user_prefs", parsedUserPrefs); // Replace with correctly formatted JSON
            }
            service.syncUserSettings(settingsArray);

        } catch (Exception e) {
            e.printStackTrace(); // Handle exception appropriately
        }
        }

        JSONArray reports = new JSONArray();
        JSONArray tickets = new JSONArray();
        JSONArray updateTickets = new JSONArray();
        JSONArray chalks = new JSONArray();
        JSONArray specialActivityReports = new JSONArray();
        JSONArray callInReports = new JSONArray();
        JSONArray hotListReports = new JSONArray();
        ArrayList<Hotlist> hotlists = new ArrayList<Hotlist>();
        ArrayList<String> uploadImages = new ArrayList<String>();
        ArrayList<String> uploadVoiceComments = new ArrayList<String>();
        ArrayList<SyncData> syncData = SyncData.getSyncData();

        for (SyncData data : syncData) {
            String table = data.getTableName();
            String primaryKey = data.getPrimaryKey();
            String opt = data.getActivity();
            if (opt.equals("INSERT")) {
                if (table.equals(TPConstant.TABLE_DUTY_REPORTS)) {
                    DutyReport report = DutyReport.getDutyReportByPrimaryKey(primaryKey);
                    if (report != null) {
                        reports.put(report.getJSONObject());
                    }
                } else if (table.equals(TPConstant.TABLE_TICKETS)) {
                    Ticket ticket = Ticket.getTicketsByPrimaryId(primaryKey);
                    if (ticket != null) {
                        tickets.put(ticket.getJSONObject());
                    }
                } else if (table.equals(TPConstant.TABLE_CHALKS)) {
                    ChalkVehicle chalk = ChalkVehicle.getChalkVehicleByPrimaryKey(primaryKey);
                    if (chalk != null) {
                        chalks.put(chalk.getJSONObject());
                    }
                    try {
                        ALPRChalk alprChalk = ALPRChalk.getChalkVehicleById(Long.parseLong(primaryKey));
                        if (alprChalk != null) {
                            chalks.put(alprChalk.getJSONObject());
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                } else if (table.equals(TPConstant.TABLE_SPECIAL_ACTIVITY_REPORTS)) {
                    SpecialActivityReport report = SpecialActivityReport
                            .getSpecialActivityReportByPrimaryKey(primaryKey);
                    if (report != null) {
                        specialActivityReports.put(report.getJSONObject());
                    }
                } else if (table.equals(TPConstant.TABLE_CALL_IN_REPORTS)) {
                    CallInReport report = CallInReport.getCallInReportByPrimaryKey(primaryKey);
                    if (report != null) {
                        callInReports.put(report.getJSONObject());
                    }
                } else if (table.equals(TPConstant.TABLE_HOTLIST)) {
                    Hotlist report = Hotlist.getHostlistReportByPrimaryKey(primaryKey);
                    if (report != null) {
                        hotListReports.put(report.getJSONObject());
                        hotlists.add(report);
                    }
                }

            } else if (opt.equals("UPDATE")) {
                if (table.equals(TPConstant.TABLE_TICKETS)) {
                    Ticket ticket = Ticket.getTicketsByPrimaryId(primaryKey);
                    if (ticket != null) {
                        updateTickets.put(ticket.getJSONObject());
                    }
                }
            }
        }

        // Prepare Tickets JSON
        for (int i = 0; i < tickets.length(); i++) {
            try {
                JSONObject object = tickets.getJSONObject(i);
                long ticketId = object.getLong("ticket_id");
                long citationNumber = object.getLong("citation_number");

                JSONArray ticketComments = new JSONArray();
                ArrayList<TicketComment> comments = TicketComment.getTicketComments(ticketId, citationNumber);
                for (TicketComment comment : comments) {
                    ticketComments.put(comment.getJSONObject());
                    if (comment.isVoiceComment()) {
                        uploadVoiceComments.add(comment.getComment());
                    }
                }

                object.put("ticketComments", ticketComments);

                JSONArray ticketPictures = new JSONArray();
                ArrayList<TicketPicture> pictures = TicketPicture.getTicketPictures(ticketId, citationNumber);
                for (TicketPicture picture : pictures) {
                    if ("Y".equalsIgnoreCase(picture.getLprNotification())) {
                        continue;
                    }

                    if (!uploadImages.contains(picture.getImagePath())) {
                        uploadImages.add(picture.getImagePath());
                    }

                    ticketPictures.put(picture.getJSONObject());
                }

                object.put("ticketPictures", ticketPictures);

                JSONArray ticketViolations = new JSONArray();
                ArrayList<TicketViolation> violations = TicketViolation
                        .getTicketViolations(ticketId, citationNumber);
                for (TicketViolation violation : violations) {
                    ticketViolations.put(violation.getJSONObject());
                }

                object.put("ticketViolations", ticketViolations);

            } catch (Exception e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }
        }

        // Prepare Chalks JSON
        for (int i = 0; i < chalks.length(); i++) {
            try {
                JSONObject object = chalks.getJSONObject(i);
                long chalkId = object.getLong("chalk_id");

                JSONArray chalkPictures = new JSONArray();
                ArrayList<ChalkPicture> pictures = ChalkPicture.getChalkPictures(chalkId);
                for (ChalkPicture picture : pictures) {
                    uploadImages.add(picture.getImagePath());
                    chalkPictures.put(picture.getJSONObject());
                }

                object.put("chalkPictures", chalkPictures);

                JSONArray chalkComments = new JSONArray();
                ArrayList<ChalkComment> comments = ChalkComment.getChalkComments(chalkId);
                for (ChalkComment comment : comments) {
                    chalkComments.put(comment.getJSONObject());
                }

                object.put("chalkComments", chalkComments);

            } catch (Exception e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }
        }

        JSONArray syncTicketResult;
        try {
            syncTicketResult = service.syncTickets(tickets);

            if (syncTicketResult != null) {
                if (syncTicketResult.length() > 0) {
                    // Log Errors
                    ArrayList<String> failedTickets = new ArrayList<String>();
                    for (int i = 0; i < syncTicketResult.length(); i++) {
                        JSONObject failure = syncTicketResult.getJSONObject(i);
                        failedTickets.add(failure.getString("ticketId"));

                        // Remove duplicate citations
                        if (failure.getString("message") != null
                                && failure.getString("message").contains("Citation already exists")) {
                            SyncData.removeSyncData(TPConstant.TABLE_TICKETS, failure.getString("ticketId"));
                        }

                        log.error("Sync Error: citation# " + failure.getString("citationNumber") + ", error - "
                                + failure.getString("message"));
                    }

                    // Remove success records
                    for (int i = 0; i < tickets.length(); i++) {
                        String primaryKey = ((JSONObject) tickets.get(i)).getString("ticket_id");

                        if (!failedTickets.contains(primaryKey)) {
                            SyncData.removeSyncData(TPConstant.TABLE_TICKETS, primaryKey);
                            //Ticket.removeOlderTicketById(Long.parseLong(primaryKey));
                        }
                    }
                } else {

                    // Remove sync records and tickets
                    for (int i = 0; i < tickets.length(); i++) {
                        String primaryKey = ((JSONObject) tickets.get(i)).getString("ticket_id");
                        SyncData.removeSyncData(TPConstant.TABLE_TICKETS, primaryKey);
                    }

                    //Ticket.removeAllOlderTickets(context);
                }
            }
        } catch (Exception e1) {
            log.error("SyncError " + e1.getMessage());
        }

        boolean syncUpdateResult = service.syncUpdatesTickets(updateTickets);
        if (syncUpdateResult) {
            try {
                for (int i = 0; i < updateTickets.length(); i++) {
                    String primaryKey = ((JSONObject) updateTickets.get(i)).getString("ticket_id");
                    SyncData.removeSyncData(TPConstant.TABLE_TICKETS, primaryKey);
                }
            } catch (Exception e) {
                log.error("SyncError " + e.getMessage());
            }
        }

        boolean syncChalkResult = service.syncChalks(chalks);
        if (syncChalkResult) {
            try {
                for (int i = 0; i < chalks.length(); i++) {
                    String primaryKey = ((JSONObject) chalks.get(i)).getString("chalk_id");
                    SyncData.removeSyncData(TPConstant.TABLE_CHALKS, primaryKey);
                }
            } catch (Exception e) {
                log.error("SyncError " + e.getMessage());
            }
        }

        boolean syncDutyReportResult = service.syncDutyReports(reports);
        if (syncDutyReportResult) {
            try {
                for (int i = 0; i < reports.length(); i++) {
                    String primaryKey = ((JSONObject) reports.get(i)).getString("report_id");
                    SyncData.removeSyncData(TPConstant.TABLE_DUTY_REPORTS, primaryKey);
                }
            } catch (Exception e) {
                log.error("SyncError " + e.getMessage());
            }
        }

        boolean syncCallInReportResult = service.syncCallInReports(callInReports);
        if (syncCallInReportResult) {
            try {
                for (int i = 0; i < callInReports.length(); i++) {
                    String primaryKey = ((JSONObject) callInReports.get(i)).getString("report_id");
                    SyncData.removeSyncData(TPConstant.TABLE_CALL_IN_REPORTS, primaryKey);
                }
            } catch (Exception e) {
                log.error("SyncError " + e.getMessage());
            }
        }

        boolean syncSpecialActivityResult = service.syncSpecialActivityReports(specialActivityReports);
        if (syncSpecialActivityResult) {
            try {
                for (int i = 0; i < specialActivityReports.length(); i++) {
                    String primaryKey = ((JSONObject) specialActivityReports.get(i)).getString("report_id");
                    SyncData.removeSyncData(TPConstant.TABLE_SPECIAL_ACTIVITY_REPORTS, primaryKey);
                }
            } catch (Exception e) {
                log.error("SyncError " + e.getMessage());
            }
        }

        // Sync and remove hotList data
      /*  try {
            boolean syncHotListResult = service.syncHotListReports(hotListReports);
            if (syncHotListResult) {
                try {
                    for (int i = 0; i < hotListReports.length(); i++) {
                        String primaryKey = ((JSONObject) hotListReports.get(i)).getString("hotlist_id");
                        SyncData.removeSyncData(TPConstant.TABLE_HOTLIST, primaryKey);
                    }
                } catch (Exception e) {
                    log.error("SyncError " + e.getMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        try {
            ApiRequest apiRequest = ServiceGenerator.createRxService(ApiRequest.class);
            ActivityRequest_Rpc requestPOJO = new ActivityRequest_Rpc();
            requestPOJO.setMethod("updateHotListReports");
            ActivityRequest params = new ActivityRequest();
            params.sethotlistRequest(hotlists);
            requestPOJO.setParams(params);
            System.out.println("RequestObj**" + new Gson().toJson(requestPOJO));
            apiRequest.updateHotListReports(requestPOJO).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        // progressDialog.dismiss();
                        // Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                        try {
                            for (int i = 0; i < hotlists.size(); i++) {
                                String primaryKey = String.valueOf(hotlists.get(i).getHostlistId());
                                SyncData.removeSyncData(TPConstant.TABLE_HOTLIST, primaryKey);
                            }
                        } catch (Exception e) {
                            log.error("SyncError " + e.getMessage());
                        }
                        Log.i(TAG, "onResponse: hotListReports Updated");
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                    Log.e(TAG, "onFailure: update hotListReports failed");
                }
            });
            // return service.deleteTicketComments(citationNumber, violationId, comment);
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
        // Get All Pending Image Uploads
        ArrayList<SyncData> syncImageUpload = SyncData.getImageUploadSyncData();
        for (SyncData data : syncImageUpload) {
            uploadImages.add(data.getActivitySource());
        }

        // Get All Pending Voice Comment Uploads
        ArrayList<SyncData> syncVoiceUpload = SyncData.getVoiceUploadSyncData();
        for (SyncData data : syncVoiceUpload) {
            uploadVoiceComments.add(data.getActivitySource());
        }

        // Remove Pending Uploads Sync Data
        try {
            SyncData.removeSyncUploads();
        } catch (Exception e) {
            log.error("Error removing sync uploads.");
        }

        // Upload Pictures
        service.syncUploadImages(uploadImages);

        // Upload Voice Comments
        final int custId = TPApplication.getInstance().custId;
        final ArrayList<String> voiceMemos = uploadVoiceComments;
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean hasUploaded = false;
                for (String memo : voiceMemos) {
                    hasUploaded = TPUtility.uploadFile(TPUtility.getVoiceMemosFolder() + memo,
                            TPConstant.FILE_UPLOAD + "/uploadfile", custId);
                    if (!hasUploaded) {
                        TPUtility.markPendingVoiceComment(memo);
                    }
                }
            }
        }).start();

        //
        saveDeviceFeaturesDetails();
    }

    //
    private void saveDeviceFeaturesDetails() {
        try {

            RequestPOJODeviceFeatures requestPOJO = new RequestPOJODeviceFeatures();
            ArrayList<DeviceFeatures> arrayList = new ArrayList<>();
            requestPOJO.setMethod("saveDeviceFeatures");
            ArrayList<Feature> features = Feature.getFeaturesList(TPApplication.getInstance().custId);

            if (features.size() > 0) {
                for (int i = 0; i < features.size(); i++) {
                    Feature feature = features.get(i);

                    DeviceFeatures p = new DeviceFeatures();
                    p.setCustId(TPApplication.getInstance().custId);
                    p.setUserId(TPApplication.getInstance().userId);
                    p.setDeviceId(TPApplication.getInstance().deviceId);
                    p.setDevice(TPApplication.getInstance().deviceName);
                    p.setFeatureName(feature.getFeature());
                    p.setAdmin(feature.getAllowedAdmin());
                    p.setIsActive(String.valueOf(feature.isAllowed()));
                    p.setModuleName("Parking");
                    p.setOfficer(feature.getAllowedOfficer());
                    p.setValue(feature.getValue());
                    arrayList.add(p);


                }

                ParamsDeviceFeatures pp = new ParamsDeviceFeatures();
                pp.setDeviceFeaturesData(arrayList);
                requestPOJO.setParams(pp);
                ApiRequest api = ServiceGenerator.createRxService(ApiRequest.class);
                api.saveDeviceFeatures1(requestPOJO).enqueue(new Callback<ResponseResult>() {
                    @Override
                    public void onResponse(Call<ResponseResult> call, Response<ResponseResult> response) {

                        if (response.isSuccessful()) {
                            System.out.println("Response is: " + response.body().getResult());
                            // Result result = response.body().getResult();

                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseResult> call, Throwable t) {

                        System.out.println(t.getMessage());
                        api.saveDeviceFeatures1(requestPOJO).cancel();
                    }
                });
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    /*@Override
    public void updateAllTables(boolean fullSync) throws TPException {
        try {
            CustomerInfo customerInfo = TPApplication.getInstance().getCustomerInfo();
            DeviceInfo deviceInfo = TPApplication.getInstance().getDeviceInfo();
            User userInfo = TPApplication.getInstance().getUserInfo();
            int userId = -1, custId = 1, i;
            JSONArray objects;
            if (userInfo != null) {
                userId = userInfo.getUserId();
            }
            if (customerInfo != null) {
                custId = customerInfo.getCustId();
            }
            DatabaseHelper dbHelper = DatabaseHelper.getInstance();
            dbHelper.openWritableDatabase();
            */

    /* Sync All Customers *//*

            try {
                objects = service.getCustomers(custId);
                if (objects != null && objects.length() > 0) {
                    CustomerInfo.removeAll(custId);
                    for (i = 0; i < objects.length(); i++) {
                        CustomerInfo customer = new CustomerInfo((JSONObject) objects.get(i));
                        dbHelper.insertOrReplace(customer.getContentValues(), "customers");
                    }
                    log.info("Updated " + objects.length() + " Customer Records");
                }
            } catch (Exception e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }

            *//* Sync Customers *//*
            try {
                JSONObject newCust = service.getClientInfo(custId);
                if (newCust != null) {
                    CustomerInfo newCustInfo = new CustomerInfo(newCust);
                    dbHelper.insertOrReplace(newCustInfo.getContentValues(), "customers");
                    TPApplication.getInstance().setCustomerInfo(newCustInfo);
                }
            } catch (Exception e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }

            *//* Sync Devices *//*
            try {
                if (deviceInfo != null) {
                    JSONObject newDevice = service.getDeviceInfo(deviceInfo.getDeviceName());

                    if (newDevice != null) {
                        DeviceInfo newDeviceInfo = new DeviceInfo(newDevice);
                        deviceInfo.setDefaultTemplateId(newDeviceInfo.getDefaultTemplateId());
                        deviceInfo.setEndCitationNumber(newDeviceInfo.getEndCitationNumber());
                        deviceInfo.setEndPhotoNumber(newDeviceInfo.getEndPhotoNumber());
                        deviceInfo.setEndWarningNumber(newDeviceInfo.getEndWarningNumber());
                        deviceInfo.setLastSync(new Date());
                        dbHelper.insertOrReplace(deviceInfo.getContentValues(), "devices");
                        TPApplication.getInstance().setDeviceInfo(deviceInfo);

                        log.info("Updated Device Info");
                    }
                } else {
                    JSONObject newDevice = service.getDeviceInfo(TPApplication.getInstance().deviceName);

                    if (newDevice != null) {
                        deviceInfo = new DeviceInfo(newDevice);
                        dbHelper.insertOrReplace(deviceInfo.getContentValues(), "devices");
                        TPApplication.getInstance().setDeviceInfo(deviceInfo);

                        log.info("Updated Device Info");
                    }
                }
            } catch (Exception e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }

            *//* Sync Users *//*
            try {
                objects = service.getUsers(custId);
                if (objects != null && objects.length() > 0) {
                    User.removeAll();

                    for (i = 0; i < objects.length(); i++) {
                        User user = new User((JSONObject) objects.get(i));
                        dbHelper.insertOrReplace(user.getContentValues(), "users");
                    }

                    log.info("Updated " + objects.length() + " Users Records");
                }
            } catch (Exception e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }

            *//* Sync UserSettings *//*
            try {
                objects = service.getUserSettings(custId);
                if (objects != null && objects.length() > 0) {
                    UserSetting.removeAll();

                    for (i = 0; i < objects.length(); i++) {
                        UserSetting settings = new UserSetting((JSONObject) objects.get(i));
                        dbHelper.insertOrReplace(settings.getContentValues(), "user_settings");
                    }

                    log.info("Updated " + objects.length() + " UserSettings Records");
                }
            } catch (Exception e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }

            *//* Sync Messages *//*
            try {
                objects = service.getMessages(custId, fullSync);
                if (fullSync) {
                    Message.removeAll();
                }

                for (i = 0; i < objects.length(); i++) {
                    JSONObject object = (JSONObject) objects.get(i);
                    if (!fullSync && !object.isNull("sync_data_id")) {
                        int primaryKey = object.getInt("primary_key");
                        Message.removeById(primaryKey);
                        continue;
                    }

                    Message message = new Message((JSONObject) objects.get(i));
                    dbHelper.insertOrReplace(message.getContentValues(), "messages");
                }

                log.info("Updated " + objects.length() + " Messages Records");

            } catch (Exception e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }

            *//* Sync Bodies *//*
            try {
                objects = service.getBodies(custId, fullSync);
                if (objects != null && objects.length() > 0) {
                    if (fullSync) {
                        Body.removeAll();
                    }
                }

                for (i = 0; i < objects.length(); i++) {
                    JSONObject object = (JSONObject) objects.get(i);
                    if (!fullSync && !object.isNull("sync_data_id")) {
                        int primaryKey = object.getInt("primary_key");
                        Body.removeById(primaryKey);
                        continue;
                    }

                    Body body = new Body((JSONObject) objects.get(i));
                    dbHelper.insertOrReplace(body.getContentValues(), "bodies");
                }

                log.info("Updated " + objects.length() + " Bodies Records");
            } catch (Exception e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }

            *//* Sync Contacts *//*
            try {
                objects = service.getContacts(custId, fullSync);
                if (objects != null && objects.length() > 0) {
                    if (fullSync) {
                        Contact.removeAll();
                    }
                }
                for (i = 0; i < objects.length(); i++) {
                    JSONObject object = (JSONObject) objects.get(i);
                    if (!fullSync && !object.isNull("sync_data_id")) {
                        int primaryKey = object.getInt("primary_key");
                        Contact.removeById(primaryKey);
                        continue;
                    }

                    Contact contact = new Contact((JSONObject) objects.get(i));
                    dbHelper.insertOrReplace(contact.getContentValues(), "contacts");
                }

                log.info("Updated " + objects.length() + " Contacts Records");
            } catch (Exception e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }

            *//* Sync Colors *//*
            try {
                objects = service.getColors(custId, fullSync);
                if (objects != null && objects.length() > 0) {
                    if (fullSync) {
                        Color.removeAll();
                    }
                }
                for (i = 0; i < objects.length(); i++) {
                    JSONObject object = (JSONObject) objects.get(i);
                    if (!fullSync && !object.isNull("sync_data_id")) {
                        int primaryKey = object.getInt("primary_key");
                        Color.removeById(primaryKey);
                        continue;
                    }

                    Color color = new Color((JSONObject) objects.get(i));
                    dbHelper.insertOrReplace(color.getContentValues(), "colors");
                }

                log.info("Updated " + objects.length() + " Colors Records");
            } catch (Exception e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }

            *//* Sync Comments *//*
            try {
                objects = service.getComments(custId, fullSync);
                if (objects != null && objects.length() > 0) {
                    if (fullSync) {
                        Comment.removeAll();
                    }
                }
                if (Feature.isSystemFeatureAllowed(Feature.CLEAR_COMMENTS_ON_SYNC)) {
                    Comment.removeAddedComments();
                }

                for (i = 0; i < objects.length(); i++) {
                    JSONObject object = (JSONObject) objects.get(i);
                    if (!fullSync && !object.isNull("sync_data_id")) {
                        int primaryKey = object.getInt("primary_key");
                        Comment.removeById(primaryKey);
                        continue;
                    }

                    Comment comment = new Comment((JSONObject) objects.get(i));
                    dbHelper.insertOrReplace(comment.getContentValues(), "comments");
                }

                log.info("Updated " + objects.length() + " Comments Records");
            } catch (Exception e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }

            *//* Sync Directions *//*
            try {
                objects = service.getDirections(custId, fullSync);
                if (objects != null && objects.length() > 0) {
                    if (fullSync) {
                        Direction.removeAll();
                    }
                }
                for (i = 0; i < objects.length(); i++) {
                    JSONObject object = (JSONObject) objects.get(i);
                    if (!fullSync && !object.isNull("sync_data_id")) {
                        int primaryKey = object.getInt("primary_key");
                        Direction.removeById(primaryKey);
                        continue;
                    }

                    Direction direction = new Direction((JSONObject) objects.get(i));
                    dbHelper.insertOrReplace(direction.getContentValues(), "directions");
                }

                log.info("Updated " + objects.length() + " Directions Records");
            } catch (Exception e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }

            *//* Sync Durations *//*
            try {
                objects = service.getDurations(custId, fullSync);
                if (objects != null && objects.length() > 0) {
                    if (fullSync) {
                        Duration.removeAll();
                    }
                }
                for (i = 0; i < objects.length(); i++) {
                    JSONObject object = (JSONObject) objects.get(i);
                    if (!fullSync && !object.isNull("sync_data_id")) {
                        int primaryKey = object.getInt("primary_key");
                        Duration.removeById(primaryKey);
                        continue;
                    }

                    Duration duration = new Duration((JSONObject) objects.get(i));
                    dbHelper.insertOrReplace(duration.getContentValues(), "durations");
                }

                log.info("Updated " + objects.length() + " Durations Records");
            } catch (Exception e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }

            *//* Sync Duties *//*
            try {
                objects = service.getDuties(custId, fullSync);
                if (objects != null && objects.length() > 0) {
                    if (fullSync) {
                        Duty.removeAll();
                    }
                }
                for (i = 0; i < objects.length(); i++) {
                    JSONObject object = (JSONObject) objects.get(i);
                    if (!fullSync && !object.isNull("sync_data_id")) {
                        int primaryKey = object.getInt("primary_key");
                        Duty.removeById(primaryKey);
                        continue;
                    }

                    Duty duty = new Duty((JSONObject) objects.get(i));
                    dbHelper.insertOrReplace(duty.getContentValues(), "duties");
                }

                log.info("Updated " + objects.length() + " Duties Records");
            } catch (Exception e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }

            *//* Sync Features *//*
            try {
                objects = service.getFeatures(custId, fullSync);
                if (objects != null && objects.length() > 0) {
                    if (fullSync) {
                        Feature.removeAll();
                    }
                }
                for (i = 0; i < objects.length(); i++) {
                    JSONObject object = (JSONObject) objects.get(i);
                    if (!fullSync && !object.isNull("sync_data_id")) {
                        int primaryKey = object.getInt("primary_key");
                        Feature.removeById(primaryKey);
                        continue;
                    }

                    Feature feature = new Feature((JSONObject) objects.get(i));
                    dbHelper.insertOrReplace(feature.getContentValues(), "features");
                }

                log.info("Updated " + objects.length() + " Features Records");

            } catch (Exception e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }

            *//* Sync GPS Locations *//*
            try {
                objects = service.getGPSLocations(custId, fullSync);
                if (objects != null && objects.length() > 0) {
                    if (fullSync) {
                        GPSLocation.removeAll();
                    }
                }
                for (i = 0; i < objects.length(); i++) {
                    JSONObject object = (JSONObject) objects.get(i);
                    if (!fullSync && !object.isNull("sync_data_id")) {
                        int primaryKey = object.getInt("primary_key");
                        GPSLocation.removeById(primaryKey);
                        continue;
                    }

                    GPSLocation gpsLocation = new GPSLocation((JSONObject) objects.get(i));
                    dbHelper.insertOrReplace(gpsLocation.getContentValues(), "gps_locations");
                }

                log.info("Updated " + objects.length() + " GPS Locations Records");
            } catch (Exception e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }

            *//* Sync Hotlist *//*
            try {
                objects = service.getHotlist(custId, fullSync);
                if (objects != null && objects.length() > 0) {
                    if (fullSync) {
                        Hotlist.removeAll();
                    }
                }
                for (i = 0; i < objects.length(); i++) {
                    JSONObject object = (JSONObject) objects.get(i);
                    if (!fullSync && !object.isNull("sync_data_id")) {
                        int primaryKey = object.getInt("primary_key");
                        Hotlist.removeById(primaryKey);
                        continue;
                    }

                    Hotlist hotlist = new Hotlist((JSONObject) objects.get(i));
                    dbHelper.insertOrReplace(hotlist.getContentValues(), "hotlist");
                }

                log.info("Updated " + objects.length() + " Hotlist Records");
            } catch (Exception e) {
                Log.e("Hotlist", TPUtility.getPrintStackTrace(e));
            }

            *//* Sync Locations *//*
            try {
                objects = service.getLocations(custId, fullSync);
                if (objects != null && objects.length() > 0) {
                    if (fullSync) {
                        Location.removeAll();
                    }
                }
                for (i = 0; i < objects.length(); i++) {
                    JSONObject object = (JSONObject) objects.get(i);
                    if (!fullSync && !object.isNull("sync_data_id")) {
                        int primaryKey = object.getInt("primary_key");
                        Location.removeById(primaryKey);
                        continue;
                    }

                    Location location = new Location((JSONObject) objects.get(i));
                    dbHelper.insertOrReplace(location.getContentValues(), "locations");
                }

                log.info("Updated " + objects.length() + " Locations Records");
            } catch (Exception e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }

            *//* Sync Repeat Offenders *//*
            try {
                objects = service.getRepeatOffenders(custId, fullSync);
                if (objects != null && objects.length() > 0) {
                    if (fullSync) {
                        removeAll();
                    }
                }
                for (i = 0; i < objects.length(); i++) {
                    JSONObject object = (JSONObject) objects.get(i);
                    if (!fullSync && !object.isNull("sync_data_id")) {
                        int primaryKey = object.getInt("primary_key");
                        removeById(primaryKey);
                        continue;
                    }

                    RepeatOffender repeatOffender = new RepeatOffender((JSONObject) objects.get(i));
                    dbHelper.insertOrReplace(repeatOffender.getContentValues(), "repeat_offenders");
                }
                log.info("Updated " + objects.length() + " RepeatOffender Records");
            } catch (Exception e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }

            *//* Sync Make and Models *//*
            try {
                objects = service.getMakesAndModels(custId, fullSync);
                if (objects != null && objects.length() > 0) {
                    if (fullSync) {
                        MakeAndModel.removeAll();
                    }
                }
                for (i = 0; i < objects.length(); i++) {
                    JSONObject object = (JSONObject) objects.get(i);
                    if (!fullSync && !object.isNull("sync_data_id")) {
                        int primaryKey = object.getInt("primary_key");
                        MakeAndModel.removeById(primaryKey);
                        continue;
                    }

                    MakeAndModel make = new MakeAndModel((JSONObject) objects.get(i));
                    dbHelper.insertOrReplace(make.getContentValues(), "makes_and_models");
                }

                log.info("Updated " + objects.length() + " Make and Models Records");
            } catch (Exception e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }

            *//* Sync Meters *//*
            try {
                objects = service.getMeters(custId, fullSync);
                if (objects != null && objects.length() > 0) {
                    if (fullSync) {
                        Meter.removeAll();
                    }
                }
                for (i = 0; i < objects.length(); i++) {
                    JSONObject object = (JSONObject) objects.get(i);
                    if (!fullSync && !object.isNull("sync_data_id")) {
                        int primaryKey = object.getInt("primary_key");
                        Meter.removeById(primaryKey);
                        continue;
                    }

                    Meter meter = new Meter((JSONObject) objects.get(i));
                    dbHelper.insertOrReplace(meter.getContentValues(), "meters");
                }

                log.info("Updated " + objects.length() + " Meters Records");
            } catch (Exception e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }

			*//* Sync Permits
			try {
				objects = service.getPermits(custId, fullSync);
				if (fullSync) {
					Permit.removeAll();
				}

				for (i = 0; i < objects.length(); i++) {
					JSONObject object = (JSONObject) objects.get(i);
					if (!fullSync && !object.isNull("sync_data_id")) {
						int primaryKey = object.getInt("primary_key");
						Permit.removeById(primaryKey);
						continue;
					}

					dbHelper.insertOrReplaceByJSON(object, new Permit().getContentValues(), "permits");
					// Permit permit=new Permit((JSONObject)objects.get(i));
					// dbHelper.insertOrReplace(permit.getContentValues(),
					// "permits");
				}

				log.info("Updated " + objects.length() + " Permits Records");
			} catch (Exception e) {
				log.error(TPUtility.getPrintStackTrace(e));
			}*//*

     *//* Sync States *//*
            try {
                objects = service.getStates(custId, fullSync);
                if (objects != null && objects.length() > 0) {
                    if (fullSync) {
                        State.removeAll();
                    }
                }
                for (i = 0; i < objects.length(); i++) {
                    JSONObject object = (JSONObject) objects.get(i);
                    if (!fullSync && !object.isNull("sync_data_id")) {
                        int primaryKey = object.getInt("primary_key");
                        State.removeById(primaryKey);
                        continue;
                    }

                    State states = new State((JSONObject) objects.get(i));
                    dbHelper.insertOrReplace(states.getContentValues(), "states");
                }

                log.info("Updated " + objects.length() + " States Records");
            } catch (Exception e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }

            *//* Sync Street Prefixes *//*
            try {
                objects = service.getStreetPrefixes(custId, fullSync);
                if (objects != null && objects.length() > 0) {
                    if (fullSync) {
                        StreetPrefix.removeAll();
                    }
                }
                for (i = 0; i < objects.length(); i++) {
                    JSONObject object = (JSONObject) objects.get(i);
                    if (!fullSync && !object.isNull("sync_data_id")) {
                        int primaryKey = object.getInt("primary_key");
                        StreetPrefix.removeById(primaryKey);
                        continue;
                    }

                    StreetPrefix prefix = new StreetPrefix((JSONObject) objects.get(i));
                    dbHelper.insertOrReplace(prefix.getContentValues(), "street_prefixes");
                }

                log.info("Updated " + objects.length() + " Street Prefixes Records");
            } catch (Exception e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }

            *//* Sync Street Suffixes *//*
            try {
                objects = service.getStreetSuffixes(custId, fullSync);
                if (objects != null && objects.length() > 0) {
                    if (fullSync) {
                        StreetSuffix.removeAll();
                    }
                }
                for (i = 0; i < objects.length(); i++) {
                    JSONObject object = (JSONObject) objects.get(i);
                    if (!fullSync && !object.isNull("sync_data_id")) {
                        int primaryKey = object.getInt("primary_key");
                        StreetSuffix.removeById(primaryKey);
                        continue;
                    }

                    StreetSuffix suffix = new StreetSuffix((JSONObject) objects.get(i));
                    dbHelper.insertOrReplace(suffix.getContentValues(), "street_suffixes");
                }

                log.info("Updated " + objects.length() + " Street Suffixes Records");
            } catch (Exception e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }

            *//* Sync Violations *//*
            try {
                objects = service.getViolations(custId, fullSync);
                if (objects != null && objects.length() > 0) {
                    if (fullSync) {
                        Violation.removeAll();
                    }
                }
                for (i = 0; i < objects.length(); i++) {
                    JSONObject object = (JSONObject) objects.get(i);
                    if (!fullSync && !object.isNull("sync_data_id")) {
                        int primaryKey = object.getInt("primary_key");
                        Violation.removeById(primaryKey);
                        continue;
                    }

                    Violation violation = new Violation((JSONObject) objects.get(i));
                    dbHelper.insertOrReplace(violation.getContentValues(), "violations");
                }

                log.info("Updated " + objects.length() + " Violations Records");
            } catch (Exception e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }

            *//* Sync Zones *//*
            try {
                objects = service.getZones(custId, fullSync);
                if (objects != null && objects.length() > 0) {
                    if (fullSync) {
                        Zone.removeAll();
                    }
                }
                for (i = 0; i < objects.length(); i++) {
                    JSONObject object = (JSONObject) objects.get(i);
                    if (!fullSync && !object.isNull("sync_data_id")) {
                        int primaryKey = object.getInt("primary_key");
                        Zone.removeById(primaryKey);
                        continue;
                    }

                    Zone zone = new Zone((JSONObject) objects.get(i));
                    dbHelper.insertOrReplace(zone.getContentValues(), "zones");
                }

                log.info("Updated " + objects.length() + " Zones Records");
            } catch (Exception e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }


            *//* Sync device_groups *//*
            try {
                objects = service.getDeviceGroup(custId, fullSync);
                if (objects != null && objects.length() > 0) {
                    if (fullSync) {
                        DeviceGroup.removeAll();
                    }
                }
                for (i = 0; i < objects.length(); i++) {
                    JSONObject object = (JSONObject) objects.get(i);
                    if (!fullSync && !object.isNull("sync_data_id")) {
                        int primaryKey = object.getInt("primary_key");
                        DeviceGroup.removeById(primaryKey);
                        continue;
                    }

                    DeviceGroup group = new DeviceGroup((JSONObject) objects.get(i));
                    dbHelper.insertOrReplace(group.getContentValues(), "device_groups");
                }

                log.info("Updated " + objects.length() + " Device Group Records");
            } catch (Exception e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }


            *//* Sync Void Ticket Reasons *//*
            try {
                objects = service.getVoidTicketReasons(custId, fullSync);
                if (objects != null && objects.length() > 0) {
                    if (fullSync) {
                        VoidTicketReason.removeAll();
                    }
                }
                for (i = 0; i < objects.length(); i++) {
                    JSONObject object = (JSONObject) objects.get(i);
                    if (!fullSync && !object.isNull("sync_data_id")) {
                        int primaryKey = object.getInt("primary_key");
                        VoidTicketReason.removeById(primaryKey);
                        continue;
                    }

                    VoidTicketReason voidReason = new VoidTicketReason((JSONObject) objects.get(i));
                    dbHelper.insertOrReplace(voidReason.getContentValues(), "void_ticket_reasons");
                }

                log.info("Updated " + objects.length() + " Void Ticket Reasons Records");
            } catch (Exception e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }

            *//* Sync Special Activites *//*
            try {
                objects = service.getSpecialActivities(custId, fullSync);
                if (objects != null && objects.length() > 0) {
                    if (fullSync) {
                        SpecialActivity.removeAll();
                    }
                }
                for (i = 0; i < objects.length(); i++) {
                    JSONObject object = (JSONObject) objects.get(i);
                    if (!fullSync && !object.isNull("sync_data_id")) {
                        int primaryKey = object.getInt("primary_key");
                        SpecialActivity.removeById(primaryKey);
                        continue;
                    }

                    SpecialActivity specialActivity = new SpecialActivity((JSONObject) objects.get(i));
                    dbHelper.insertOrReplace(specialActivity.getContentValues(), "special_activities");
                }

                log.info("Updated " + objects.length() + " Special Activities Records");
            } catch (Exception e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }

            *//* Sync special_activity_dispositions *//*
            try {
                objects = service.getSpecialActivityDispositions(custId, fullSync);
                if (objects != null && objects.length() > 0) {
                    if (fullSync) {
                        SpecialActivityDisposition.removeAll();
                    }
                }
                for (i = 0; i < objects.length(); i++) {
                    JSONObject object = (JSONObject) objects.get(i);
                    if (!fullSync && !object.isNull("sync_data_id")) {
                        int primaryKey = object.getInt("primary_key");
                        SpecialActivityDisposition.removeById(primaryKey);
                        continue;
                    }

                    SpecialActivityDisposition specialActivityDisposition = new SpecialActivityDisposition(
                            (JSONObject) objects.get(i));
                    dbHelper.insertOrReplace(specialActivityDisposition.getContentValues(),
                            "special_activity_dispositions");
                }

                log.info("Updated " + objects.length() + " Special Activity Dispositions Records");
            } catch (Exception e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }

            *//* Sync print_macros *//*
            try {
                objects = service.getPrintMacros(custId, fullSync);
                if (objects != null && objects.length() > 0) {
                    if (fullSync) {
                        PrintMacro.removeAll();
                    }
                }
                for (i = 0; i < objects.length(); i++) {
                    JSONObject object = (JSONObject) objects.get(i);
                    if (!fullSync && !object.isNull("sync_data_id")) {
                        int primaryKey = object.getInt("primary_key");
                        PrintMacro.removeById(primaryKey);
                        continue;
                    }

                    PrintMacro printMacro = new PrintMacro((JSONObject) objects.get(i));
                    dbHelper.insertOrReplace(printMacro.getContentValues(), "print_macros");
                }

                log.info("Updated " + objects.length() + " Print Macros Records");
            } catch (Exception e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }

            try {
                objects = service.getPrintTemplates(custId, fullSync);
                if (objects != null && objects.length() > 0) {
                    if (fullSync) {
                        PrintTemplate.removeAll();
                    }
                }
                for (i = 0; i < objects.length(); i++) {
                    JSONObject object = (JSONObject) objects.get(i);
                    if (!fullSync && !object.isNull("sync_data_id")) {
                        int primaryKey = object.getInt("primary_key");
                        PrintTemplate.removeById(primaryKey);
                        continue;
                    }
                    PrintTemplate printTemplate = new PrintTemplate((JSONObject) objects.get(i));
                    dbHelper.insertOrReplace(printTemplate.getContentValues(), "print_templates");
                }

                log.info("Updated " + objects.length() + " Print Templates Records");
            } catch (Exception e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }

            try {
                objects = service.getCallInList(custId, fullSync);
                if (objects != null && objects.length() > 0) {
                    if (fullSync) {
                        CallInList.removeAll();
                    }
                }
                for (i = 0; i < objects.length(); i++) {
                    JSONObject object = (JSONObject) objects.get(i);
                    if (!fullSync && !object.isNull("sync_data_id")) {
                        int primaryKey = object.getInt("primary_key");
                        CallInList.removeById(primaryKey);
                        continue;
                    }

                    CallInList callInList = new CallInList((JSONObject) objects.get(i));
                    dbHelper.insertOrReplace(callInList.getContentValues(), "call_in_list");
                }

                log.info("Updated " + objects.length() + " Call In List Records");

            } catch (Exception e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }

            try {
                objects = service.getLocationGroups(custId, fullSync);
                if (objects != null && objects.length() > 0) {
                    if (fullSync) {
                        LocationGroup.removeAll();
                    }
                }
                for (i = 0; i < objects.length(); i++) {
                    JSONObject object = (JSONObject) objects.get(i);
                    if (!fullSync && !object.isNull("sync_data_id")) {
                        int primaryKey = object.getInt("primary_key");
                        LocationGroup.removeById(primaryKey);
                        continue;
                    }

                    LocationGroup locationGroup = new LocationGroup((JSONObject) objects.get(i));
                    dbHelper.insertOrReplace(locationGroup.getContentValues(), "location_groups");
                }

                log.info("Updated " + objects.length() + " Location Group Records");
            } catch (Exception e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }

            try {
                objects = service.getLocationGroupLocations(custId, fullSync);
                if (objects != null && objects.length() > 0) {
                    if (fullSync) {
                        LocationGroupLocation.removeAll();
                    }
                }
                for (i = 0; i < objects.length(); i++) {
                    JSONObject object = (JSONObject) objects.get(i);
                    if (!fullSync && !object.isNull("sync_data_id")) {
                        int primaryKey = object.getInt("primary_key");
                        LocationGroupLocation.removeById(primaryKey);
                        continue;
                    }

                    LocationGroupLocation locationGroup = new LocationGroupLocation((JSONObject) objects.get(i));
                    dbHelper.insertOrReplace(locationGroup.getContentValues(), "location_group_locations");
                }

                log.info("Updated " + objects.length() + " Location Group Location Records");
            } catch (Exception e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }

            try {
                objects = service.getCommentGroups(custId, fullSync);
                if (objects != null && objects.length() > 0) {
                    if (fullSync) {
                        CommentGroup.removeAll();
                    }
                }
                for (i = 0; i < objects.length(); i++) {
                    JSONObject object = (JSONObject) objects.get(i);
                    if (!fullSync && !object.isNull("sync_data_id")) {
                        int primaryKey = object.getInt("primary_key");
                        CommentGroup.removeById(primaryKey);
                        continue;
                    }

                    CommentGroup commentGroup = new CommentGroup((JSONObject) objects.get(i));
                    dbHelper.insertOrReplace(commentGroup.getContentValues(), "comment_groups");
                }

                log.info("Updated " + objects.length() + " Comment Group Records");
            } catch (Exception e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }

            try {
                objects = service.getCommentGroupComments(custId, fullSync);
                if (objects != null && objects.length() > 0) {
                    if (fullSync) {
                        CommentGroupComment.removeAll();
                    }
                }
                for (i = 0; i < objects.length(); i++) {
                    JSONObject object = (JSONObject) objects.get(i);
                    if (!fullSync && !object.isNull("sync_data_id")) {
                        int primaryKey = object.getInt("primary_key");
                        CommentGroupComment.removeById(primaryKey);
                        continue;
                    }

                    CommentGroupComment commentGroup = new CommentGroupComment((JSONObject) objects.get(i));
                    dbHelper.insertOrReplace(commentGroup.getContentValues(), "comment_group_comments");
                }

                log.info("Updated " + objects.length() + " Comment Group Comment Records");
            } catch (Exception e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }

            try {
                objects = service.getViolationGroups(custId, fullSync);
                if (objects != null && objects.length() > 0) {
                    if (fullSync) {
                        ViolationGroup.removeAll();
                    }
                }
                for (i = 0; i < objects.length(); i++) {
                    JSONObject object = (JSONObject) objects.get(i);
                    if (!fullSync && !object.isNull("sync_data_id")) {
                        int primaryKey = object.getInt("primary_key");
                        ViolationGroup.removeById(primaryKey);
                        continue;
                    }

                    ViolationGroup violationGroup = new ViolationGroup((JSONObject) objects.get(i));
                    dbHelper.insertOrReplace(violationGroup.getContentValues(), "violation_groups");
                }

                log.info("Updated " + objects.length() + " Violation Group Records");
            } catch (Exception e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }

            try {
                objects = service.getViolationGroupViolations(custId, fullSync);
                if (objects != null && objects.length() > 0) {
                    if (fullSync) {
                        ViolationGroupViolation.removeAll();
                    }
                }
                for (i = 0; i < objects.length(); i++) {
                    JSONObject object = (JSONObject) objects.get(i);
                    if (!fullSync && !object.isNull("sync_data_id")) {
                        int primaryKey = object.getInt("primary_key");
                        ViolationGroupViolation.removeById(primaryKey);
                        continue;
                    }

                    ViolationGroupViolation violationGroup = new ViolationGroupViolation((JSONObject) objects.get(i));
                    dbHelper.insertOrReplace(violationGroup.getContentValues(), "violation_group_violations");
                }

                log.info("Updated " + objects.length() + " Violation Group Violation Records");
            } catch (Exception e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }

            try {
                objects = service.getVendors(custId, fullSync);
                if (objects != null && objects.length() > 0) {
                    if (fullSync) {
                        Vendor.removeAll();
                    }
                }
                for (i = 0; i < objects.length(); i++) {
                    JSONObject object = (JSONObject) objects.get(i);
                    if (!fullSync && !object.isNull("sync_data_id")) {
                        int primaryKey = object.getInt("primary_key");
                        Vendor.removeById(primaryKey);
                        continue;
                    }

                    Vendor vendor = new Vendor((JSONObject) objects.get(i));
                    dbHelper.insertOrReplace(vendor.getContentValues(), "vendors");
                }

                log.info("Updated " + objects.length() + " Vendor Records");
            } catch (Exception e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }

            try {
                objects = service.getVendorServices(custId, fullSync);
                if (objects != null && objects.length() > 0) {
                    if (fullSync) {
                        VendorService.removeAll();
                    }
                }
                for (i = 0; i < objects.length(); i++) {
                    JSONObject object = (JSONObject) objects.get(i);
                    if (!fullSync && !object.isNull("sync_data_id")) {
                        int primaryKey = object.getInt("primary_key");
                        VendorService.removeById(primaryKey);
                        continue;
                    }

                    VendorService vendorService = new VendorService((JSONObject) objects.get(i));
                    dbHelper.insertOrReplace(vendorService.getContentValues(), "vendor_services");
                }

                log.info("Updated " + objects.length() + " Vendor Service Records");
            } catch (Exception e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }

            try {
                objects = service.getVendorItems(custId, fullSync);
                if (objects != null && objects.length() > 0) {
                    if (fullSync) {
                        VendorItem.removeAll();
                    }
                }
                for (i = 0; i < objects.length(); i++) {
                    JSONObject object = (JSONObject) objects.get(i);
                    if (!fullSync && !object.isNull("sync_data_id")) {
                        int primaryKey = object.getInt("primary_key");
                        VendorItem.removeById(primaryKey);
                        continue;
                    }

                    VendorItem vendorItem = new VendorItem((JSONObject) objects.get(i));
                    dbHelper.insertOrReplace(vendorItem.getContentValues(), "vendor_items");
                }

                log.info("Updated " + objects.length() + " Vendor Item Records");
            } catch (Exception e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }

            try {
                objects = service.hotlist.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).map(new Function<HotlistResponse, Object>() {
            @Override
            public Object apply(HotlistResponse result) throws Exception {
                return result;
            }
        }).subscribeWith(new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.i(TAG, "onSubscribe: " + d);
            }

            @Override
            public void onNext(Object o) {
                Log.i(TAG, "onNext: " + o.toString());
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "onComplete: ");
            }
        });(custId, userId, deviceInfo.getDeviceId(), fullSync);
                if (objects != null && objects.length() > 0) {
                    if (fullSync) {
                        VendorServiceRegistration.removeAll();
                    }
                }
                for (i = 0; i < objects.length(); i++) {
                    JSONObject object = (JSONObject) objects.get(i);
                    if (!fullSync && !object.isNull("sync_data_id")) {
                        int primaryKey = object.getInt("primary_key");
                        VendorServiceRegistration.removeById(primaryKey);
                        continue;
                    }

                    VendorServiceRegistration vendorService = new VendorServiceRegistration(
                            (JSONObject) objects.get(i));
                    dbHelper.insertOrReplace(vendorService.getContentValues(), "vendor_service_registrations");
                }

                log.info("Updated " + objects.length() + " Vendor Service Registration Records");
            } catch (Exception e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }

            *//* Sync Modules *//*
            try {
                objects = service.getModules(custId, fullSync);
                Module.removeAll();

                for (i = 0; i < objects.length(); i++) {
                    JSONObject object = (JSONObject) objects.get(i);
                    if (!fullSync && !object.isNull("sync_data_id")) {
                        int primaryKey = object.getInt("primary_key");
                        Module.removeById(primaryKey);
                        continue;
                    }
                    Module module = new Module((JSONObject) objects.get(i));
                    dbHelper.insertOrReplace(module.getContentValues(), "modules");
                }
                log.info("Updated " + objects.length() + " Modules Records");
            } catch (Exception e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }
            *//* Sync Customer Modules *//*
            try {
                objects = service.getCustomerModules(custId, fullSync);
                CustomerModule.removeAll();
                for (i = 0; i < objects.length(); i++) {
                    JSONObject object = (JSONObject) objects.get(i);
                    if (!fullSync && !object.isNull("sync_data_id")) {
                        int primaryKey = object.getInt("primary_key");
                        CustomerModule.removeById(primaryKey);
                        continue;
                    }
                    CustomerModule module = new CustomerModule((JSONObject) objects.get(i));
                    dbHelper.insertOrReplace(module.getContentValues(), "customer_modules");
                }
                log.info("Updated " + objects.length() + " Customer Modules Records");
            } catch (Exception e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }
            dbHelper.closeWritableDb();
            // Clear Memory Leaks
            System.gc();

        } catch (Exception e) {

            log.error(TPUtility.getPrintStackTrace(e));
            TPException appEx = new TPException();
            appEx.setErrorMessage("Internal system error. Please try again");
            throw appEx;
        }
    }
*/

    @SuppressLint("CheckResult")
    @Override
    public void updateAllTables(final boolean fullSync, final Handler.Callback callback) throws TPException {
        final int custid = TPApplication.getInstance().custId;
        int deviceId = TPApplication.getInstance().deviceId;
        int userId = TPApplication.getInstance().userId;
        ApiRequest api = ServiceGenerator.createRxService(ApiRequest.class);
        Params params = new Params();
        params.setCustId(custid);
        params.setFullSync(fullSync);

        RequestPOJO requestPOJO = new RequestPOJO();
        requestPOJO.setParams(params);
        requestPOJO.setMethod("getCustomers");
        Observable<CustomerResponse> customers = api.getCustomers(requestPOJO);

        requestPOJO = new RequestPOJO();
        requestPOJO.setParams(params);
        requestPOJO.setMethod("getClientInfo");
        Observable<ClientInfoResponse> clientInfo = api.getClientInfo(requestPOJO);

        requestPOJO = new RequestPOJO();
        requestPOJO.setParams(params);
        requestPOJO.setMethod("getUsers");
        Call<UserResponse> users = api.getUsers(requestPOJO);

        requestPOJO = new RequestPOJO();
        requestPOJO.setParams(params);
        requestPOJO.setMethod("getUserSettings");
        Observable<UserSettingResponse> userSetting = api.getUserSettings(requestPOJO);

        requestPOJO = new RequestPOJO();
        requestPOJO.setParams(params);
        requestPOJO.setMethod("getMessages");
        Observable<MessageResponse> messages = api.getMessages(requestPOJO);

        requestPOJO = new RequestPOJO();
        requestPOJO.setParams(params);
        requestPOJO.setMethod("getBodies");
        Observable<BodyResponse> bodies = api.getBodies(requestPOJO);

        requestPOJO = new RequestPOJO();
        requestPOJO.setParams(params);
        requestPOJO.setMethod("getPatrollersData");
        Call<PatrollersResponse> patrollers = api.getPatrollersId(requestPOJO);

        requestPOJO = new RequestPOJO();
        requestPOJO.setParams(params);
        requestPOJO.setMethod("getLprBodyMap");
        Observable<LprBodyResponse> lprBodies = api.getLPRBodies(requestPOJO);

        requestPOJO = new RequestPOJO();
        requestPOJO.setParams(params);
        requestPOJO.setMethod("getContacts");
        Observable<ContactResponse> contacts = api.getContacts(requestPOJO);

        requestPOJO = new RequestPOJO();
        requestPOJO.setParams(params);
        requestPOJO.setMethod("getColors");
        Observable<ColorResponse> colors = api.getColors(requestPOJO);

        requestPOJO = new RequestPOJO();
        requestPOJO.setParams(params);
        requestPOJO.setMethod("getComments");
        Observable<CommentResponse> comments = api.getComments(requestPOJO);

        requestPOJO = new RequestPOJO();
        requestPOJO.setParams(params);
        requestPOJO.setMethod("getDirections");
        Observable<DirectionResponse> directions = api.getDirections(requestPOJO);

        requestPOJO = new RequestPOJO();
        requestPOJO.setParams(params);
        requestPOJO.setMethod("getDurations");
        Observable<DurationResponse> durations = api.getDurations(requestPOJO);

        requestPOJO = new RequestPOJO();
        requestPOJO.setParams(params);
        requestPOJO.setMethod("getDuties");
        Observable<DutyResponse> duties = api.getDuties(requestPOJO);

        requestPOJO = new RequestPOJO();
        requestPOJO.setParams(params);
        requestPOJO.setMethod("getFeatures");
        Observable<FeatureResponse> features = api.getFeatures(requestPOJO);

        requestPOJO = new RequestPOJO();
        requestPOJO.setParams(params);
        requestPOJO.setMethod("getGPSLocations");
        Observable<GPSLocationResponse> gpsLocations = api.getGPSLocations(requestPOJO);

        requestPOJO = new RequestPOJO();
        requestPOJO.setParams(params);
        requestPOJO.setMethod("getSpecialActivitiesLocation");
        Observable<SpecialActivitiesLocationResponse> specialActivitiesLocation = api.getSpecialActivitiesLocation(requestPOJO);

        requestPOJO = new RequestPOJO();
        requestPOJO.setParams(params);
        requestPOJO.setMethod("getHotlist");
        Observable<HotlistResponse> hotlist = api.getHotlist(requestPOJO);

        requestPOJO = new RequestPOJO();
        requestPOJO.setParams(params);
        requestPOJO.setMethod("getLocations");
        Observable<LocationResponse> locations = api.getLocations(requestPOJO);

        requestPOJO = new RequestPOJO();
        requestPOJO.setParams(params);
        requestPOJO.setMethod("getRepeatOffenders");
        Observable<RepeatOffenderResponse> repeatOffenders = api.getRepeatOffenders(requestPOJO);

        requestPOJO = new RequestPOJO();
        requestPOJO.setParams(params);
        requestPOJO.setMethod("getModelsAndMakes");
        Observable<MakeAndModelResponse> makesAndModels = api.getMakesAndModels(requestPOJO);

        requestPOJO = new RequestPOJO();
        requestPOJO.setParams(params);
        requestPOJO.setMethod("getMeters");
        Observable<MeterResponse> meters = api.getMeters(requestPOJO);

        requestPOJO = new RequestPOJO();
        requestPOJO.setParams(params);
        requestPOJO.setMethod("getPermits");
        Observable<PermitResponse> permits = api.getPermits(requestPOJO);

        requestPOJO = new RequestPOJO();
        requestPOJO.setParams(params);
        requestPOJO.setMethod("getStates");
        Observable<StateResponse> states = api.getStates(requestPOJO);

        requestPOJO = new RequestPOJO();
        requestPOJO.setParams(params);
        requestPOJO.setMethod("getStreetPrefixes");
        Observable<StreetPrefixResponse> streetPrefixes = api.getStreetPrefixes(requestPOJO);

        requestPOJO = new RequestPOJO();
        requestPOJO.setParams(params);
        requestPOJO.setMethod("getStreetSuffixes");
        Observable<StreetSuffixResponse> streetSuffixes = api.getStreetSuffixes(requestPOJO);

        requestPOJO = new RequestPOJO();
        requestPOJO.setParams(params);
        requestPOJO.setMethod("getViolations");
        Observable<ViolationResponse> violations = api.getViolations(requestPOJO);

        requestPOJO = new RequestPOJO();
        requestPOJO.setParams(params);
        requestPOJO.setMethod("getZones");
        Observable<ZoneResponse> zones = api.getZones(requestPOJO);

        requestPOJO = new RequestPOJO();
        requestPOJO.setParams(params);
        requestPOJO.setMethod("getDeviceGroup");
        Observable<DeviceGroupResponse> deviceGroup = api.getDeviceGroup(requestPOJO);

        requestPOJO = new RequestPOJO();
        requestPOJO.setParams(params);
        requestPOJO.setMethod("getVoidTicketReasons");
        Observable<VoidTicketReasonResponse> voidTicketReasons = api.getVoidTicketReasons(requestPOJO);

        requestPOJO = new RequestPOJO();
        requestPOJO.setParams(params);
        requestPOJO.setMethod("getSpecialActivities");
        Observable<SpecialActivityResponse> specialActivities = api.getSpecialActivities(requestPOJO);

        requestPOJO = new RequestPOJO();
        requestPOJO.setParams(params);
        requestPOJO.setMethod("getSpecialActivityDispositions");
        Observable<SpecialActivityDispositionResponse> specialActivityDispositions = api.getSpecialActivityDispositions(requestPOJO);

        requestPOJO = new RequestPOJO();
        requestPOJO.setParams(params);
        requestPOJO.setMethod("getPrintMacros");
        Observable<PrintMacroResponse> printMacros = api.getPrintMacros(requestPOJO);

        requestPOJO = new RequestPOJO();
        requestPOJO.setParams(params);
        requestPOJO.setMethod("getPrintTemplates");
        Observable<PrintTemplateResponse> printTemplates = api.getPrintTemplates(requestPOJO);

        requestPOJO = new RequestPOJO();
        requestPOJO.setParams(params);
        requestPOJO.setMethod("getCallInList");
        final Observable<CallInListResponse> callInList = api.getCallInList(requestPOJO);

        requestPOJO = new RequestPOJO();
        requestPOJO.setParams(params);
        requestPOJO.setMethod("getLocationGroups");
        Observable<LocationGroupResponse> locationGroups = api.getLocationGroups(requestPOJO);

        requestPOJO = new RequestPOJO();
        requestPOJO.setParams(params);
        requestPOJO.setMethod("getLocationGroupLocations");
        Observable<LocationGroupLocationResponse> locationGroupLocations = api.getLocationGroupLocations(requestPOJO);

        requestPOJO = new RequestPOJO();
        requestPOJO.setParams(params);
        requestPOJO.setMethod("getCommentGroups");
        Observable<CommentGroupResponse> commentGroups = api.getCommentGroups(requestPOJO);

        requestPOJO = new RequestPOJO();
        requestPOJO.setParams(params);
        requestPOJO.setMethod("getCommentGroupComments");
        Observable<CommentGroupCommentResponse> commentGroupComments = api.getCommentGroupComments(requestPOJO);

        requestPOJO = new RequestPOJO();
        requestPOJO.setParams(params);
        requestPOJO.setMethod("getViolationGroups");
        Observable<ViolationGroupResponse> violationGroups = api.getViolationGroups(requestPOJO);

        requestPOJO = new RequestPOJO();
        requestPOJO.setParams(params);
        requestPOJO.setMethod("getViolationGroupViolations");
        Observable<ViolationGroupViolationResponse> violationGroupViolations = api.getViolationGroupViolations(requestPOJO);

        requestPOJO = new RequestPOJO();
        requestPOJO.setParams(params);
        requestPOJO.setMethod("getVendors");
        Observable<VendorResponse> vendors = api.getVendors(requestPOJO);

        requestPOJO = new RequestPOJO();
        requestPOJO.setParams(params);
        requestPOJO.setMethod("getVendorServices");
        Observable<VendorServiceResponse> vendorServices = api.getVendorServices(requestPOJO);

        requestPOJO = new RequestPOJO();
        requestPOJO.setParams(params);
        requestPOJO.setMethod("getVendorItems");
        Observable<VendorItemResponse> vendorItems = api.getVendorItems(requestPOJO);

        requestPOJO = new RequestPOJO();
        Params params1 = new Params();
        params1.setFullSync(fullSync);
        params1.setCustId(custid);
        params1.setDeviceId(deviceId);
        params1.setUserId(userId);
        requestPOJO.setParams(params1);
        requestPOJO.setMethod("getVendorServiceRegistrations");
        Observable<VendorServiceRegistrationResponse> vendorServiceRegistrations = api.getVendorServiceRegistrations(requestPOJO);

        requestPOJO = new RequestPOJO();
        requestPOJO.setParams(params);
        requestPOJO.setMethod("getModules");
        Observable<ModuleResponse> modules = api.getModules(requestPOJO);

        requestPOJO = new RequestPOJO();
        requestPOJO.setParams(params);
        requestPOJO.setMethod("getCustomerModules");
        Observable<CustomerModuleResponse> customerModules = api.getCustomerModules(requestPOJO);

        requestPOJO = new RequestPOJO();
        Params params2 = new Params();
        params2.setDeviceIName(TPApplication.getInstance().deviceName);
        requestPOJO.setParams(params2);
        requestPOJO.setMethod("getDeviceInfo");
        Observable<DeviceInfoResponse> deviceInfo = api.getDeviceInfo(requestPOJO);

        final long currentTime = System.currentTimeMillis();
        Log.i(TAG, "updateAllTables: " + currentTime);
        Observable.zip(


                        customers.subscribeOn(Schedulers.io()).onErrorReturn(throwable -> {
                            syncStatus = false;
                            log.error(TPUtility.getPrintStackTrace(throwable) + " getCustomers api");
                            return null;
                        }),
                        clientInfo.subscribeOn(Schedulers.io()).onErrorReturn(throwable -> {
                            syncStatus = false;
                            log.error(TPUtility.getPrintStackTrace(throwable) + " getClientInfo api");
                            return null;
                        }),
                        violationGroups.subscribeOn(Schedulers.io()).onErrorReturn(throwable -> {
                            syncStatus = false;
                            log.error(TPUtility.getPrintStackTrace(throwable) + " getViolationGroups API");
                            return null;
                        }),
                        colors.subscribeOn(Schedulers.io()).onErrorReturn(throwable -> {
                            syncStatus = false;
                            log.error(TPUtility.getPrintStackTrace(throwable) + " getColors API");
                            return null;
                        }),
                        lprBodies.subscribeOn(Schedulers.io()).onErrorReturn(throwable -> {
                            syncStatus = false;
                            log.error(TPUtility.getPrintStackTrace(throwable) + " getlprBodies api");
                            return null;
                        }),
                        userSetting.subscribeOn(Schedulers.io()).onErrorReturn(throwable -> {
                            syncStatus = false;
                            log.error(TPUtility.getPrintStackTrace(throwable) + " getUserSettings API");
                            return null;
                        }),
                        deviceInfo.subscribeOn(Schedulers.io()).onErrorReturn(throwable -> {
                            syncStatus = false;
                            log.error(TPUtility.getPrintStackTrace(throwable) + "getDeviceInfo API");
                            return null;
                        }),
                        contacts.subscribeOn(Schedulers.io()).onErrorReturn(throwable -> {
                            syncStatus = false;
                            log.error(TPUtility.getPrintStackTrace(throwable) + " getContacts API");
                            return null;

                        }),
                        comments.subscribeOn(Schedulers.io()).onErrorReturn(throwable -> {
                            syncStatus = false;
                            log.error(TPUtility.getPrintStackTrace(throwable) + " getComments API");
                            return null;
                        }),

                        (customerResponse, clientInfoResponse, violationGroupResponse, colorResponse, lprBodyResponse, userSettingResponse, deviceInfoResponse, contactResponse, commentResponse) -> {
                            if (fullSync) {
                                CustomerInfo.removeAll(custid);
                                ViolationGroup.removeAll();
                                Color.removeAll();
                                UserSetting.removeAll();
                                Contact.removeAll();
                                Comment.removeAll();
                                LprBodyMap.removeAll();
 Observable.zip(directions.subscribeOn(Schedulers.io()).onErrorReturn(throwable -> {
                    syncStatus = false;
                    log.error(TPUtility.getPrintStackTrace(throwable));
                    return null;
                }),
                durations.subscribeOn(Schedulers.io()).onErrorReturn(throwable -> {
                    syncStatus = false;
                    log.error(TPUtility.getPrintStackTrace(throwable));
                    return null;
                }),
                duties.subscribeOn(Schedulers.io()).onErrorReturn(throwable -> {
                    syncStatus = false;
                    log.error(TPUtility.getPrintStackTrace(throwable));
                    return null;
                }),
                features.subscribeOn(Schedulers.io()).onErrorReturn(throwable -> {
                    syncStatus = false;
                    log.error(TPUtility.getPrintStackTrace(throwable));
                    return null;
                }),
                gpsLocations.subscribeOn(Schedulers.io()).onErrorReturn(throwable -> {
                    syncStatus = false;
                    log.error(TPUtility.getPrintStackTrace(throwable));
                    return null;
                }),
                specialActivitiesLocation.subscribeOn(Schedulers.io()).onErrorReturn(throwable -> {
                    syncStatus = false;
                    log.error(TPUtility.getPrintStackTrace(throwable));
                    return null;
                }),
                locations.subscribeOn(Schedulers.io()).onErrorReturn(throwable -> {
                    syncStatus = false;
                    log.error(TPUtility.getPrintStackTrace(throwable));
                    return null;
                }),
                repeatOffenders.subscribeOn(Schedulers.io()).onErrorReturn(throwable -> {
                    syncStatus = false;
                    log.error(TPUtility.getPrintStackTrace(throwable));
                    return null;
                }),
                makesAndModels.subscribeOn(Schedulers.io()).onErrorReturn(throwable -> {
                    syncStatus = false;
                    log.error(TPUtility.getPrintStackTrace(throwable));
                    return null;
                }),
                (directionResponse, durationResponse, dutyResponse, featureResponse, gpsLocationResponse, specialActivitiesLocationResponse, locationResponse, repeatOffenderResponse, makeAndModelResponse) -> {
                    List<Object> list = new ArrayList<>();
                    if (fullSync) {
                        Direction.removeAll();
                        Duration.removeAll();
                        Duty.removeAll();
                        Feature.removeAll();
                        GPSLocation.removeAll();
                        SpecialActivitiesLocation.removeAll();
                        Location.removeAll();
                        RepeatOffender.removeAll();
                        MakeAndModel.removeAll();
                    }
                    try {
                        if (directionResponse.getResult() != null) {
                            for (Direction direction : directionResponse.getResult()) {
                                if (!fullSync && direction.getSyncDataId() != 0) {
                                    Direction.removeById(direction.getPrimaryKey());
                                    continue;
                                }
                                Direction.insertDirection(direction);
                            }
                            log.info("Updated " + directionResponse.getResult().size() + " Direction Records");
                        } else {
                            syncStatus = false;
                        }
                    } catch (Exception e) {
                        syncStatus = false;
                        log.error(TPUtility.getPrintStackTrace(e));
                    }
                    try {
                        if (durationResponse.getResult() != null) {
                            for (Duration duration : durationResponse.getResult()) {
                                if (!fullSync && duration.getSyncDataId() != 0) {
                                    Duration.removeById(duration.getPrimaryKey());
                                    continue;
                                }
                                Duration.insertDuration(duration);
                            }
                            log.info("Updated " + durationResponse.getResult().size() + " Duration Records");
                        } else {
                            syncStatus = false;
                        }
                    } catch (Exception e) {
                        syncStatus = false;
                        log.error(TPUtility.getPrintStackTrace(e));
                    }
                    try {
                        if (dutyResponse.getResult() != null) {
                            for (Duty duty : dutyResponse.getResult()) {
                                if (!fullSync && duty.getSyncDataId() != 0) {
                                    Duty.removeById(duty.getPrimaryKey());
                                    continue;
                                }
                                Duty.insertDuty(duty);

                            }
                            log.info("Updated " + dutyResponse.getResult().size() + " Duty Records");
                        } else {
                            syncStatus = false;
                        }
                    } catch (Exception e) {
                        syncStatus = false;
                        log.error(TPUtility.getPrintStackTrace(e));
                    }
                    try {
                        if (featureResponse.getResult() != null) {
                            for (Feature feature : featureResponse.getResult()) {
                                if (!fullSync && feature.getSyncDataId() != 0) {
                                    Feature.removeById(feature.getPrimaryKey());
                                    continue;
                                }
                                feature.setFeature(feature.getFeature().toUpperCase());
                                Feature.insertFeature(feature);

                            }
                            log.info("Updated " + featureResponse.getResult().size() + " Feature Records");
                        } else {
                            syncStatus = false;
                        }
                    } catch (Exception e) {
                        syncStatus = false;
                        log.error(TPUtility.getPrintStackTrace(e));
                    }
                    try {
                        if (gpsLocationResponse.getResult() != null) {
                            for (GPSLocation gpsLocation : gpsLocationResponse.getResult()) {
                                if (!fullSync && gpsLocation.getSyncDataId() != 0) {
                                    GPSLocation.removeById(gpsLocation.getPrimaryKey());
                                    continue;
                                }
                                GPSLocation.insertGPSLocation(gpsLocation);
                            }
                            log.info("Updated " + gpsLocationResponse.getResult().size() + " GPSLocation Records");
                        } else {
                            syncStatus = false;
                        }
                    } catch (Exception e) {
                        syncStatus = false;
                        log.error(TPUtility.getPrintStackTrace(e));
                    }
                    try {
                        if (specialActivitiesLocationResponse.getResult() != null) {
                            for (SpecialActivitiesLocation activitiesLocation : specialActivitiesLocationResponse.getResult()) {
                                SpecialActivitiesLocation.insertSpecialActivitiesLocation(activitiesLocation);
                            }
                            log.info("Updated " + specialActivitiesLocationResponse.getResult().size() + " SpecialActivitiesLocation Records");
                        } else {
                            syncStatus = false;
                        }
                    } catch (Exception e) {
                        syncStatus = false;
                        log.error(TPUtility.getPrintStackTrace(e));
                    }
                    try {
                        if (locationResponse.getResult() != null) {
                            for (Location location : locationResponse.getResult()) {
                                if (!fullSync && location.getSyncDataId() != 0) {
                                    Location.removeById(location.getPrimaryKey());
                                    continue;
                                }
                                Location.insertLocation(location);
                            }
                            log.info("Updated " + locationResponse.getResult().size() + " Location Records");
                        } else {
                            syncStatus = false;
                        }
                    } catch (Exception e) {
                        syncStatus = false;
                        log.error(TPUtility.getPrintStackTrace(e));
                    }

                    // this code is changed by mohit 12/01/2023.this code is used for restrict repeat offender in ofline mode
                    try {
                        if (repeatOffenderResponse.getResult() != null) {
                            /*for (RepeatOffender repeatOffender : repeatOffenderResponse.getResult()) {
                                if (!fullSync && repeatOffender.getSyncDataId() != 0) {
                                    RepeatOffender.removeById(repeatOffender.getPrimaryKey());
                                    continue;
                                }
                                //RepeatOffender.insertRepeatOffender(repeatOffender).subscribe();
                            }*/
                            //log.info("Updated " + repeatOffenderResponse.getResult().size() + " RepeatOffender Records");
                        } else {
                            syncStatus = false;
                        }
                    } catch (Exception e) {
                        syncStatus = false;
                        log.error(TPUtility.getPrintStackTrace(e));
                    }
                    //End
                    try {
                        if (makeAndModelResponse.getResult() != null) {
                            for (MakeAndModel makeAndModel : makeAndModelResponse.getResult()) {
                                if (!fullSync && makeAndModel.getSyncDataId() != 0) {
                                    MakeAndModel.removeById(makeAndModel.getPrimaryKey());
                                    continue;
                                }
                                MakeAndModel.insertMakeAndModel(makeAndModel);
                            }
                            log.info("Updated " + makeAndModelResponse.getResult().size() + " MakeAndModel Records");
                        } else {
                            syncStatus = false;
                        }
                    } catch (Exception e) {
                        syncStatus = false;
                        log.error(TPUtility.getPrintStackTrace(e));
                    }

                    list.add(directionResponse);
                    list.add(durationResponse);
                    list.add(dutyResponse);
                    list.add(featureResponse);
                    list.add(gpsLocationResponse);
                    list.add(specialActivitiesLocationResponse);
                    list.add(locationResponse);
                    list.add(repeatOffenderResponse);
                    list.add(makeAndModelResponse);
                    //Log.i(TAG, "apply: Time Taken results1" + (currentTime - System.currentTimeMillis()));
                    return list;
                }).observeOn(AndroidSchedulers.mainThread()).subscribeWith(new Observer<List<Object>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.i(TAG, "onSubscribe: " + d.toString());
            }

            @Override
            public void onNext(@NonNull List<Object> objects) {

            }

            @Override
            public void onError(@NonNull Throwable e) {
                syncStatus = false;
                log.error(TPUtility.getPrintStackTrace(e));
                /*TPException appEx = new TPException();
                appEx.setErrorMessage("Internal system error. Please try again");
                try {
                    throw appEx;
                } catch (TPException ex) {
                    ex.printStackTrace();
                }*/
                observable5 = true;
                sendCallbackForSync(callback);
                Log.e(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onComplete() {
                observable5 = true;
                sendCallbackForSync(callback);
            }
        });
                            }

                            try {
                                if (deviceInfoResponse.getResult() != null) {
                                    TPApplication.getInstance().setDeviceInfo(deviceInfoResponse.getResult());
                                    DeviceInfo.insertDeviceInfo(deviceInfoResponse.getResult());
                                    log.info("Updated device Records");
                                } else {
                                    log.info("deviceInfo response null");
                                    syncStatus = false;
                                }
                            } catch (Exception e) {
                                syncStatus = false;
                                log.error(TPUtility.getPrintStackTrace(e) + "getDeviceInfo API");
                            }
                            try {
                                if (lprBodyResponse.getResult() != null) {
                                    for (LprBodyMap body : lprBodyResponse.getResult()) {
                                        if (!fullSync && body.getSyncDataId() != 0) {
                                            Body.removeById(body.getPrimaryKey());
                                            continue;
                                        }
                                        LprBodyMap.insertBody(body);
                                    }
                                    log.info("Updated " + lprBodyResponse.getResult().size() + " LprBody Records");
                                } else {
                                    syncStatus = false;
                                }
                            } catch (Exception e) {
                                syncStatus = false;
                                log.error(TPUtility.getPrintStackTrace(e));
                            }
                            try {
                                if (customerResponse.getResult() != null) {
                                    CustomerInfo.inserCustomerInfo(customerResponse.getResult());
                                    log.info("Updated " + customerResponse.getResult().size() + " Customer Records");
                                } else {
                                    syncStatus = false;
                                }
                            } catch (Exception e) {
                                syncStatus = false;
                                log.error(TPUtility.getPrintStackTrace(e));
                            }

                            try {
                                if (violationGroupResponse.getResult() != null) {
                                    for (ViolationGroup violationGroup : violationGroupResponse.getResult()) {
                                        if (!fullSync && violationGroup.getSyncDataId() != 0) {
                                            ViolationGroup.removeById(violationGroup.getPrimaryKey());
                                            continue;
                                        }
                                        ViolationGroup.insertViolationGroup(violationGroup);
                                    }
                                    log.info("Updated " + violationGroupResponse.getResult().size() + " Violation groups Records");
                                } else {
                                    syncStatus = false;
                                }
                            } catch (Exception e) {
                                syncStatus = false;
                                log.error(TPUtility.getPrintStackTrace(e));
                            }
                            try {
                                if (colorResponse.getResult() != null) {
                                    for (Color color : colorResponse.getResult()) {
                                        if (!fullSync && color.getSyncDataId() != 0) {
                                            Color.removeById(color.getPrimaryKey());
                                            continue;
                                        }
                                        Color.insertColor(color);
                                    }
                                    log.info("Updated " + colorResponse.getResult().size() + " Colors Records");
                                } else {
                                    syncStatus = false;
                                }
                            } catch (Exception e) {
                                syncStatus = false;
                                log.error(TPUtility.getPrintStackTrace(e));
                            }

                            try {
                                if (userSettingResponse.getResult() != null) {
                                    UserSetting.insertUserSettingList(userSettingResponse.getResult());
                                    log.info("Updated " + userSettingResponse.getResult().size() + " UserSettings Records");
                                } else {
                                    syncStatus = false;
                                }
                            } catch (Exception e) {
                                syncStatus = false;
                                log.error(TPUtility.getPrintStackTrace(e));
                            }
                            try {
                                if (contactResponse.getResult() != null) {
                                    for (Contact contact : contactResponse.getResult()) {
                                        if (!fullSync && contact.getSyncDataId() != 0) {
                                            Contact.removeById(contact.getPrimaryKey());
                                            continue;
                                        }
                                        Contact.insertContacts(contact);
                                    }
                                    log.info("Updated " + contactResponse.getResult().size() + " Contacts Records");
                                } else {
                                    syncStatus = false;
                                }
                            } catch (Exception e) {
                                syncStatus = false;
                                log.error(TPUtility.getPrintStackTrace(e));
                            }
                            try {
                                if (commentResponse.getResult() != null) {
                                    for (Comment comment : commentResponse.getResult()) {
                                        if (!fullSync && comment.getSyncDataId() != 0) {
                                            Comment.removeById(comment.getPrimaryKey());
                                            continue;
                                        }
                                        Comment.insertComment(comment);
                                    }
                                    log.info("Updated " + commentResponse.getResult().size() + " Comments Records");
                                } else {
                                    syncStatus = false;
                                }
                            } catch (Exception e) {
                                syncStatus = false;
                                log.error(TPUtility.getPrintStackTrace(e));
                            }


                            List<Object> list = new ArrayList<>();
                            list.add(customerResponse);
                            list.add(violationGroupResponse);
                            list.add(colorResponse);
                            list.add(lprBodyResponse);
                            list.add(userSettingResponse);
                            list.add(clientInfoResponse);
                            list.add(contactResponse);
                            list.add(commentResponse);
                            //Log.i(TAG, "apply: Time Taken" + (currentTime - System.currentTimeMillis()));
                            return list;
                        }).doOnError(throwable -> {
                    syncStatus = false;
                    log.error(TPUtility.getPrintStackTrace(throwable));
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new Observer<List<Object>>() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
                        Log.i(TAG, "onSubscribe: " + d.toString());
                    }

                    @Override
                    public void onNext(@io.reactivex.annotations.NonNull List<Object> objects) {

                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        log.error(TPUtility.getPrintStackTrace(e));
                        syncStatus = false;

                        observable1 = true;
                        sendCallbackForSync(callback);
                        Log.e(TAG, "onError: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        observable1 = true;
                        sendCallbackForSync(callback);
                        Log.i(TAG, "onComplete: " + "Completed Successfully");
                    }
                });

        Observable.zip(
                meters.subscribeOn(Schedulers.io()).onErrorReturn(throwable -> {
                    syncStatus = false;
                    log.error(TPUtility.getPrintStackTrace(throwable));
                    return null;
                }),
                permits.subscribeOn(Schedulers.io()).onErrorReturn(throwable -> {
                    syncStatus = false;
                    log.error(TPUtility.getPrintStackTrace(throwable));
                    return null;
                }),
                states.subscribeOn(Schedulers.io()).onErrorReturn(throwable -> {
                    syncStatus = false;
                    log.error(TPUtility.getPrintStackTrace(throwable));
                    return null;
                }),
                streetPrefixes.subscribeOn(Schedulers.io()).onErrorReturn(throwable -> {
                    syncStatus = false;
                    log.error(TPUtility.getPrintStackTrace(throwable));
                    return null;
                }),
                streetSuffixes.subscribeOn(Schedulers.io()).onErrorReturn(throwable -> {
                    syncStatus = false;
                    log.error(TPUtility.getPrintStackTrace(throwable));
                    return null;
                }),
                violations.subscribeOn(Schedulers.io()).onErrorReturn(throwable -> {
                    syncStatus = false;
                    log.error(TPUtility.getPrintStackTrace(throwable));
                    return null;
                }),
                zones.subscribeOn(Schedulers.io()).onErrorReturn(throwable -> {
                    syncStatus = false;
                    log.error(TPUtility.getPrintStackTrace(throwable));
                    return null;
                }),
                deviceGroup.subscribeOn(Schedulers.io()).onErrorReturn(throwable -> {
                    syncStatus = false;
                    log.error(TPUtility.getPrintStackTrace(throwable));
                    return null;
                }),
                voidTicketReasons.subscribeOn(Schedulers.io()).onErrorReturn(throwable -> {
                    syncStatus = false;
                    log.error(TPUtility.getPrintStackTrace(throwable));
                    return null;
                }),
                (meterResponse, permitResponse, stateResponse, streetPrefixResponse, streetSuffixResponse, violationResponse, zoneResponse, deviceGroupResponse, voidTicketReasonResponse) -> {
                    if (fullSync) {
                        Meter.removeAll();
                        Permit.removeAll();
                        State.removeAll();
                        StreetPrefix.removeAll();
                        StreetSuffix.removeAll();
                        Violation.removeAll();
                        Zone.removeAll();
                        DeviceGroup.removeAll();
                        VoidTicketReason.removeAll();
                    }
                    try {
                        //if (meterResponse.getResult() != null) {
                        for (Meter meter : meterResponse.getResult()) {
                            if (!fullSync && meter.getSyncDataId() != 0) {
                                Meter.removeById(meter.getPrimaryKey());
                                continue;
                            }
                            Meter.insertMeter(meter);
                        }
                        log.info("Updated " + meterResponse.getResult().size() + " Meter Records");
                        /*} else {
                            syncStatus = false;
                        }*/
                    } catch (Exception e) {
                        //syncStatus = false;
                        log.error(TPUtility.getPrintStackTrace(e));
                    }
                    try {

                        if (permitResponse.getResult() != null) {
                            for (Permit permit : permitResponse.getResult()) {
                                if (!fullSync && permit.getSyncDataId() != 0) {
                                    Permit.removeById(permit.getPrimaryKey());
                                    continue;
                                }
                                Permit.insertPermit(permit);
                            }
                            log.info("Updated " + permitResponse.getResult().size() + " Permit Records");
                        }

                    } catch (Exception e) {
                        //syncStatus = false;
                        log.error(TPUtility.getPrintStackTrace(e));
                    }
                    try {
                        if (stateResponse.getResult() != null) {
                            for (State state : stateResponse.getResult()) {
                                if (!fullSync && state.getSyncDataId() != 0) {
                                    State.removeById(state.getPrimaryKey());
                                    continue;
                                }
                                State.insertState(state);
                            }
                            log.info("Updated " + stateResponse.getResult().size() + " State Records");
                        } else {
                            syncStatus = false;
                        }
                    } catch (Exception e) {
                        syncStatus = false;
                        log.error(TPUtility.getPrintStackTrace(e));
                    }
                    try {
                        if (streetPrefixResponse.getResult() != null) {
                            for (StreetPrefix streetPrefix : streetPrefixResponse.getResult()) {
                                if (!fullSync && streetPrefix.getSyncDataId() != 0) {
                                    StreetPrefix.removeById(streetPrefix.getPrimaryKey());
                                    continue;
                                }
                                StreetPrefix.insertStreetPrefix(streetPrefix);
                            }
                            log.info("Updated " + streetPrefixResponse.getResult().size() + " StreetPrefix Records");
                        } else {
                            syncStatus = false;
                        }
                    } catch (Exception e) {
                        syncStatus = false;
                        log.error(TPUtility.getPrintStackTrace(e));
                    }
                    try {
                        if (streetSuffixResponse.getResult() != null) {
                            for (StreetSuffix streetSuffix : streetSuffixResponse.getResult()) {
                                if (!fullSync && streetSuffix.getSyncDataId() != 0) {
                                    StreetSuffix.removeById(streetSuffix.getPrimaryKey());
                                    continue;
                                }
                                StreetSuffix.insertStreetSuffix(streetSuffix);
                            }
                            log.info("Updated " + streetSuffixResponse.getResult().size() + " StreetSuffix Records");
                        } else {
                            syncStatus = false;
                        }
                    } catch (Exception e) {
                        syncStatus = false;
                        log.error(TPUtility.getPrintStackTrace(e));
                    }
                    try {
                        if (violationResponse.getResult() != null) {
                            for (Violation violation : violationResponse.getResult()) {
                                if (!fullSync && violation.getSyncDataId() != 0) {
                                    Violation.removeById(violation.getPrimaryKey());
                                    continue;
                                }
                                Violation.insertViolation(violation);
                            }
                            log.info("Updated " + violationResponse.getResult().size() + " Violation Records");
                        } else {
                            syncStatus = false;
                        }
                    } catch (Exception e) {
                        syncStatus = false;
                        log.error(TPUtility.getPrintStackTrace(e));
                    }
                    try {
                        if (zoneResponse.getResult() != null) {
                            for (Zone zone : zoneResponse.getResult()) {
                                if (!fullSync && zone.getSyncDataId() != 0) {
                                    Zone.removeById(zone.getPrimaryKey());
                                    continue;
                                }
                                Zone.insertZone(zone);
                            }
                            log.info("Updated " + zoneResponse.getResult().size() + " Zone Records");
                        } else {
                            syncStatus = false;
                        }
                    } catch (Exception e) {
                        syncStatus = false;
                        log.error(TPUtility.getPrintStackTrace(e));
                    }
                    try {
                        if (deviceGroupResponse.getResult() != null) {
                            for (DeviceGroup deviceGroup1 : deviceGroupResponse.getResult()) {
                                if (!fullSync && deviceGroup1.getSyncDataId() != 0) {
                                    DeviceGroup.removeById(deviceGroup1.getPrimaryKey());
                                    continue;
                                }
                                DeviceGroup.insertDeviceGroup(deviceGroup1);

                            }
                            log.info("Updated " + deviceGroupResponse.getResult().size() + " DeviceGroup Records");
                        } else {
                            syncStatus = false;
                        }
                    } catch (Exception e) {
                        syncStatus = false;
                        log.error(TPUtility.getPrintStackTrace(e));
                    }
                    try {
                        if (voidTicketReasonResponse.getResult() != null) {
                            for (VoidTicketReason voidTicketReason : voidTicketReasonResponse.getResult()) {
                                if (!fullSync && voidTicketReason.getSyncDataId() != 0) {
                                    VoidTicketReason.removeById(voidTicketReason.getPrimaryKey());
                                    continue;
                                }
                                VoidTicketReason.insertVoidTicketReason(voidTicketReason);
                            }
                            log.info("Updated " + voidTicketReasonResponse.getResult().size() + " VoidTicketReason Records");
                        } else {
                            syncStatus = false;
                        }
                    } catch (Exception e) {
                        syncStatus = false;
                        log.error(TPUtility.getPrintStackTrace(e));
                    }
                    List<Object> list = new ArrayList<>();
                    list.add(meterResponse);
                    list.add(permitResponse);
                    list.add(stateResponse);
                    list.add(streetPrefixResponse);
                    list.add(streetSuffixResponse);
                    list.add(violationResponse);
                    list.add(zoneResponse);
                    list.add(deviceGroupResponse);
                    list.add(voidTicketReasonResponse);
                    //Log.i(TAG, "apply: Time Taken results2" + (currentTime - System.currentTimeMillis()));
                    return list;
                }).doOnError(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.i(TAG, "accept: " + throwable.getMessage());
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribeWith(new Observer<List<Object>>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.i(TAG, "onSubscribe: " + d.toString());
            }

            @Override
            public void onNext(List<Object> objects) {
                /*for (Object o : objects) {

                }*/
            }

            @Override
            public void onError(Throwable e) {
                syncStatus = false;
                log.error(TPUtility.getPrintStackTrace(e));
                /*TPException appEx = new TPException();
                appEx.setErrorMessage("Internal system error. Please try again");
                try {
                    throw appEx;
                } catch (TPException ex) {
                    ex.printStackTrace();
                }*/
                observable2 = true;
                sendCallbackForSync(callback);
                Log.e(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onComplete() {
                observable2 = true;
                sendCallbackForSync(callback);
                Log.i(TAG, "onComplete: " + "Completed Successfully");
            }
        });

        Observable.zip(
                specialActivities.subscribeOn(Schedulers.io()).onErrorReturn(throwable -> {
                    syncStatus = false;
                    log.error(TPUtility.getPrintStackTrace(throwable));
                    return null;
                }),
                specialActivityDispositions.subscribeOn(Schedulers.io()).onErrorReturn(throwable -> {
                    syncStatus = false;
                    log.error(TPUtility.getPrintStackTrace(throwable));
                    return null;
                }),
                printMacros.subscribeOn(Schedulers.io()).onErrorReturn(throwable -> {
                    syncStatus = false;
                    log.error(TPUtility.getPrintStackTrace(throwable));
                    return null;
                }),
                printTemplates.subscribeOn(Schedulers.io()).onErrorReturn(throwable -> {
                    syncStatus = false;
                    log.error(TPUtility.getPrintStackTrace(throwable));
                    return null;
                }),
                callInList.subscribeOn(Schedulers.io()).onErrorReturn(throwable -> {
                    syncStatus = false;
                    log.error(TPUtility.getPrintStackTrace(throwable));
                    return null;
                }),
                locationGroups.subscribeOn(Schedulers.io()).onErrorReturn(throwable -> {
                    syncStatus = false;
                    log.error(TPUtility.getPrintStackTrace(throwable));
                    return null;
                }),
                commentGroupComments.subscribeOn(Schedulers.io()).onErrorReturn(throwable -> {
                    syncStatus = false;
                    log.error(TPUtility.getPrintStackTrace(throwable));
                    return null;
                }),
                locationGroupLocations.subscribeOn(Schedulers.io()).onErrorReturn(throwable -> {
                    syncStatus = false;
                    log.error(TPUtility.getPrintStackTrace(throwable));
                    return null;
                }),
                commentGroups.subscribeOn(Schedulers.io()).onErrorReturn(throwable -> {
                    syncStatus = false;
                    log.error(TPUtility.getPrintStackTrace(throwable));
                    return null;
                }),
                (specialActivityResponse, specialActivityDispositionResponse, printMacroResponse, printTemplateResponse, callInListResponse, locationGroupResponse, commentGroupCommentResponse, locationGroupLocationResponse, commentGroupResponse) -> {
                    if (fullSync) {
                        SpecialActivity.removeAll();
                        SpecialActivityDisposition.removeAll();
                        PrintMacro.removeAll();
                        PrintTemplate.removeAll();
                        CallInList.removeAll();
                        LocationGroup.removeAll();
                        CommentGroupComment.removeAll();
                        LocationGroupLocation.removeAll();
                        CommentGroup.removeAll();
                    }
                    try {
                        if (specialActivityResponse.getResult() != null) {
                            for (SpecialActivity specialActivity : specialActivityResponse.getResult()) {
                                if (!fullSync && specialActivity.getSyncDataId() != 0) {
                                    SpecialActivity.removeById(specialActivity.getPrimaryKey());
                                    continue;
                                }
                                SpecialActivity.insertSpecialActivity(specialActivity);
                            }
                            log.info("Updated " + specialActivityResponse.getResult().size() + " SpecialActivity Records");
                        } else {
                            syncStatus = false;
                        }
                    } catch (Exception e) {
                        syncStatus = false;
                        log.error(TPUtility.getPrintStackTrace(e));
                    }
                    try {
                        if (specialActivityDispositionResponse.getResult() != null) {
                            for (SpecialActivityDisposition specialActivityDisposition : specialActivityDispositionResponse.getResult()) {
                                if (!fullSync && specialActivityDisposition.getSyncDataId() != 0) {
                                    SpecialActivityDisposition.removeById(specialActivityDisposition.getPrimaryKey());
                                    continue;
                                }
                                SpecialActivityDisposition.insertSpecialActivityDisposition(specialActivityDisposition);
                            }
                            log.info("Updated " + specialActivityDispositionResponse.getResult().size() + " SpecialActivityDisposition Records");
                        } else {
                            syncStatus = false;
                        }
                    } catch (Exception e) {
                        syncStatus = false;
                        log.error(TPUtility.getPrintStackTrace(e));
                    }
                    try {
                        if (printMacroResponse.getResult() != null) {
                            for (PrintMacro printMacro : printMacroResponse.getResult()) {
                                if (!fullSync && printMacro.getSyncDataId() != 0) {
                                    PrintMacro.removeById(printMacro.getPrimaryKey());
                                    continue;
                                }
                                PrintMacro.insertPrintMacro(printMacro);
                            }
                            log.info("Updated " + printMacroResponse.getResult().size() + " PrintMacro Records");
                        } else {
                            syncStatus = false;
                        }
                    } catch (Exception e) {
                        syncStatus = false;
                        log.error(TPUtility.getPrintStackTrace(e));
                    }
                    try {
                        if (printTemplateResponse.getResult() != null) {
                            for (PrintTemplate printTemplate : printTemplateResponse.getResult()) {
                                if (!fullSync && printTemplate.getSyncDataId() != 0) {
                                    PrintTemplate.removeById(printTemplate.getPrimaryKey());
                                    continue;
                                }
                                PrintTemplate.insertPrintTemplate(printTemplate);
                            }
                            log.info("Updated " + printTemplateResponse.getResult().size() + " PrintTemplate Records");
                        } else {
                            syncStatus = false;
                        }
                    } catch (Exception e) {
                        syncStatus = false;
                        log.error(TPUtility.getPrintStackTrace(e));
                    }
                    try {
                        if (callInListResponse.getResult() != null) {
                            for (CallInList callInList1 : callInListResponse.getResult()) {
                                if (!fullSync && callInList1.getSyncDataId() != 0) {
                                    CallInList.removeById(callInList1.getPrimaryKey());
                                    continue;
                                }
                                CallInList.insertCallInList(callInList1);
                            }
                            log.info("Updated " + callInListResponse.getResult().size() + " CallInList Records");
                        } else {
                            syncStatus = false;
                        }
                    } catch (Exception e) {
                        syncStatus = false;
                        log.error(TPUtility.getPrintStackTrace(e));
                    }
                    try {
                        if (locationGroupResponse.getResult() != null) {
                            for (LocationGroup locationGroup : locationGroupResponse.getResult()) {
                                if (!fullSync && locationGroup.getSyncDataId() != 0) {
                                    LocationGroup.removeById(locationGroup.getPrimaryKey());
                                    continue;
                                }
                                LocationGroup.insertLocationGroup(locationGroup);
                            }
                            log.info("Updated " + locationGroupResponse.getResult().size() + " LocationGroup Records");
                        } else {
                            syncStatus = false;
                        }
                    } catch (Exception e) {
                        syncStatus = false;
                        log.error(TPUtility.getPrintStackTrace(e));
                    }
                    try {
                        if (commentGroupResponse.getResult() != null) {
                            for (CommentGroup commentGroup : commentGroupResponse.getResult()) {
                                if (!fullSync && commentGroup.getSyncDataId() != 0) {
                                    CommentGroup.removeById(commentGroup.getPrimaryKey());
                                    continue;
                                }
                                CommentGroup.insertCommentGroup(commentGroup);
                            }
                            log.info("Updated " + commentGroupResponse.getResult().size() + " CommentGroupComment Records");
                        } else {
                            syncStatus = false;
                        }
                    } catch (Exception e) {
                        syncStatus = false;
                        log.error(TPUtility.getPrintStackTrace(e));
                    }
                    try {
                        if (locationGroupLocationResponse.getResult() != null) {
                            for (LocationGroupLocation locationGroupLocation : locationGroupLocationResponse.getResult()) {
                                if (!fullSync && locationGroupLocation.getSyncDataId() != 0) {
                                    LocationGroupLocation.removeById(locationGroupLocation.getPrimaryKey());
                                    continue;
                                }
                                LocationGroupLocation.insertLocationGroupLocation(locationGroupLocation);
                            }
                            log.info("Updated " + locationGroupLocationResponse.getResult().size() + " LocationGroupLocation Records");
                        } else {
                            syncStatus = false;
                        }
                    } catch (Exception e) {
                        syncStatus = false;
                        log.error(TPUtility.getPrintStackTrace(e));
                    }
                    try {
                        if (commentGroupCommentResponse.getResult() != null) {
                            for (CommentGroupComment commentGroupComment : commentGroupCommentResponse.getResult()) {
                                if (!fullSync && commentGroupComment.getSyncDataId() != 0) {
                                    CommentGroupComment.removeById(commentGroupComment.getPrimaryKey());
                                    continue;
                                }
                                CommentGroupComment.insertCommentGroupComment(commentGroupComment);
                            }
                            log.info("Updated " + commentGroupCommentResponse.getResult().size() + " CommentGroupComment Records");
                        } else {
                            syncStatus = false;
                        }
                    } catch (Exception e) {
                        syncStatus = false;
                        log.error(TPUtility.getPrintStackTrace(e));
                    }
                    List<Object> list = new ArrayList<>();
                    list.add(specialActivityResponse);
                    list.add(specialActivityDispositionResponse);
                    list.add(printMacroResponse);
                    list.add(printTemplateResponse);
                    list.add(callInListResponse);
                    list.add(locationGroupResponse);
                    list.add(commentGroupCommentResponse);
                    list.add(locationGroupResponse);
                    list.add(commentGroupResponse);
                    //Log.i(TAG, "apply: Time Taken results3" + (currentTime - System.currentTimeMillis()));

                    return list;
                }).doOnError(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.i(TAG, "accept: " + throwable.getMessage());
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribeWith(new Observer<List<Object>>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.i(TAG, "onSubscribe: " + d.toString());
            }

            @Override
            public void onNext(List<Object> objects) {
                /*for (Object o : objects) {

                }*/
            }

            @Override
            public void onError(Throwable e) {
                syncStatus = false;
                log.error(TPUtility.getPrintStackTrace(e));
                /*TPException appEx = new TPException();
                appEx.setErrorMessage("Internal system error. Please try again");
                try {
                    throw appEx;
                } catch (TPException ex) {
                    ex.printStackTrace();
                }*/
                observable3 = true;
                sendCallbackForSync(callback);
                Log.e(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onComplete() {
                observable3 = true;
                sendCallbackForSync(callback);
                Log.i(TAG, "onComplete: " + "Completed Successfully");
            }
        });

        Observable.zip(
                violationGroupViolations.subscribeOn(Schedulers.io()).onErrorReturn(throwable -> {
                    syncStatus = false;
                    log.error(TPUtility.getPrintStackTrace(throwable));
                    return null;
                }),
                vendors.subscribeOn(Schedulers.io()).onErrorReturn(throwable -> {
                    syncStatus = false;
                    log.error(TPUtility.getPrintStackTrace(throwable));
                    return null;
                }),
                vendorServices.subscribeOn(Schedulers.io()).onErrorReturn(throwable -> {
                    syncStatus = false;
                    log.error(TPUtility.getPrintStackTrace(throwable));
                    return null;
                }),
                vendorItems.subscribeOn(Schedulers.io()).onErrorReturn(throwable -> {
                    syncStatus = false;
                    log.error(TPUtility.getPrintStackTrace(throwable));
                    return null;
                }),
                vendorServiceRegistrations.subscribeOn(Schedulers.io()).onErrorReturn(throwable -> {
                    syncStatus = false;
                    log.error(TPUtility.getPrintStackTrace(throwable));
                    return null;
                }),
                modules.subscribeOn(Schedulers.io()).onErrorReturn(throwable -> {
                    syncStatus = false;
                    log.error(TPUtility.getPrintStackTrace(throwable));
                    return null;
                }),
                customerModules.subscribeOn(Schedulers.io()).onErrorReturn(throwable -> {
                    syncStatus = false;
                    log.error(TPUtility.getPrintStackTrace(throwable));
                    return null;
                }),
                messages.subscribeOn(Schedulers.io()).onErrorReturn(throwable -> {
                    syncStatus = false;
                    log.error(TPUtility.getPrintStackTrace(throwable));
                    return null;
                }),
                bodies.subscribeOn(Schedulers.io()).onErrorReturn(throwable -> {
                    syncStatus = false;
                    log.error(TPUtility.getPrintStackTrace(throwable));
                    return null;
                }),
                (violationGroupViolationResponse, vendorResponse, vendorServiceResponse, vendorItemResponse, vendorServiceRegistrationResponse, moduleResponse, customerModuleResponse, messageResponse, bodyResponse) -> {
                    if (fullSync) {
                        Vendor.removeAll();
                        VendorService.removeAll();
                        VendorItem.removeAll();
                        VendorServiceRegistration.removeAll();
                        Module.removeAll();
                        CustomerModule.removeAll();
                        Message.removeAll();
                        Body.removeAll();
                        ViolationGroupViolation.removeAll();
                    }
                    try {
                        if (violationGroupViolationResponse.getResult() != null) {
                            for (ViolationGroupViolation violationGroupViolation : violationGroupViolationResponse.getResult()) {
                                if (!fullSync && violationGroupViolation.getSyncDataId() != 0) {
                                    ViolationGroupViolation.removeById(violationGroupViolation.getPrimaryKey());
                                    continue;
                                }
                                ViolationGroupViolation.insertViolationGroupViolation(violationGroupViolation);
                            }
                            log.info("Updated " + violationGroupViolationResponse.getResult().size() + " ViolationGroupViolation Records");
                        } else {
                            syncStatus = false;
                        }
                    } catch (Exception e) {
                        syncStatus = false;
                        log.error(TPUtility.getPrintStackTrace(e));
                    }

                    try {
                        if (vendorResponse.getResult() != null) {
                            for (Vendor vendor : vendorResponse.getResult()) {
                                if (!fullSync && vendor.getSyncDataId() != 0) {
                                    Vendor.removeById(vendor.getPrimaryKey());
                                    continue;
                                }
                                Vendor.insertVendor(vendor);
                            }
                            log.info("Updated " + vendorResponse.getResult().size() + " Vendor Records");
                        } else {
                            syncStatus = false;
                        }
                    } catch (Exception e) {
                        syncStatus = false;
                        log.error(TPUtility.getPrintStackTrace(e));
                    }

                    try {
                        if (vendorServiceResponse.getResult() != null) {
                            for (VendorService vendorService : vendorServiceResponse.getResult()) {
                                if (!fullSync && vendorService.getSyncDataId() != 0) {
                                    VendorService.removeById(vendorService.getPrimaryKey());
                                    continue;
                                }
                                VendorService.insertVendorService(vendorService);
                            }
                            log.info("Updated " + vendorServiceResponse.getResult().size() + " VendorService Records");
                        } else {
                            syncStatus = false;
                        }
                    } catch (Exception e) {
                        syncStatus = false;
                        log.error(TPUtility.getPrintStackTrace(e));
                    }

                    try {
                        if (vendorItemResponse.getResult() != null) {
                            for (VendorItem vendorItem : vendorItemResponse.getResult()) {
                                if (!fullSync && vendorItem.getSyncDataId() != 0) {
                                    VendorItem.removeById(vendorItem.getPrimaryKey());
                                    continue;
                                }
                                VendorItem.insertVendorItem(vendorItem);
                            }
                            log.info("Updated " + vendorItemResponse.getResult().size() + " VendorItem Records");
                        } else {
                            syncStatus = false;
                        }
                    } catch (Exception e) {
                        syncStatus = false;
                        log.error(TPUtility.getPrintStackTrace(e));
                    }
                    try {
                        if (vendorServiceRegistrationResponse.getResult() != null) {
                            for (VendorServiceRegistration vendorServiceRegistration : vendorServiceRegistrationResponse.getResult()) {
                                if (!fullSync && vendorServiceRegistration.getSyncDataId() != 0) {
                                    VendorServiceRegistration.removeById(vendorServiceRegistration.getPrimaryKey());
                                    continue;
                                }
                                VendorServiceRegistration.insertVendorServiceRegistration(vendorServiceRegistration);
                            }
                            log.info("Updated " + vendorServiceRegistrationResponse.getResult().size() + " VendorServiceRegistration Records");
                        } else {
                            syncStatus = false;
                        }
                    } catch (Exception e) {
                        syncStatus = false;
                        log.error(TPUtility.getPrintStackTrace(e));
                    }
                    try {
                        if (moduleResponse.getResult() != null) {
                            for (Module module : moduleResponse.getResult()) {
                                if (!fullSync && module.getSyncDataId() != 0) {
                                    Module.removeById(module.getPrimaryKey());
                                    continue;
                                }
                                Module.insertModule(module);
                            }
                            log.info("Updated " + moduleResponse.getResult().size() + " Module Records");
                        } else {
                            syncStatus = false;
                        }
                    } catch (Exception e) {
                        syncStatus = false;
                        log.error(TPUtility.getPrintStackTrace(e));
                    }
                    try {
                        if (customerModuleResponse.getResult() != null) {
                            for (CustomerModule customerModule : customerModuleResponse.getResult()) {
                                if (!fullSync && customerModule.getSyncDataId() != 0) {
                                    CustomerModule.removeById(customerModule.getPrimaryKey());
                                    continue;
                                }
                                CustomerModule.insertCustomerModule(customerModule);
                            }
                            log.info("Updated " + customerModuleResponse.getResult().size() + " CustomerModule Records");
                        } else {
                            syncStatus = false;
                        }
                    } catch (Exception e) {
                        syncStatus = false;
                        log.error(TPUtility.getPrintStackTrace(e));
                    }
                    try {
                        if (messageResponse.getResult() != null) {
                            for (Message message : messageResponse.getResult()) {
                                if (!fullSync && message.getSyncDataId() != 0) {
                                    Message.removeById(message.getPrimaryKey());
                                    continue;
                                }
                                Message.insertMessage(message);
                            }
                            log.info("Updated " + messageResponse.getResult().size() + " Message Records");
                        } else {
                            syncStatus = false;
                        }
                    } catch (Exception e) {
                        syncStatus = false;
                        log.error(TPUtility.getPrintStackTrace(e));
                    }
                    try {
                        if (bodyResponse.getResult() != null) {
                            for (Body body : bodyResponse.getResult()) {
                                if (!fullSync && body.getSyncDataId() != 0) {
                                    Body.removeById(body.getPrimaryKey());
                                    continue;
                                }
                                Body.insertBody(body);
                            }
                            log.info("Updated " + bodyResponse.getResult().size() + " Body Records");
                        } else {
                            syncStatus = false;
                        }
                    } catch (Exception e) {
                        syncStatus = false;
                        log.error(TPUtility.getPrintStackTrace(e));
                    }
                    List<Object> list = new ArrayList<>();
                    list.add(violationGroupViolationResponse);
                    list.add(vendorResponse);
                    list.add(vendorServiceResponse);
                    list.add(vendorItemResponse);
                    list.add(vendorServiceRegistrationResponse);
                    list.add(moduleResponse);
                    list.add(customerModuleResponse);
                    list.add(messageResponse);
                    list.add(bodyResponse);
                    //Log.i(TAG, "apply: Time Taken results4" + (currentTime - System.currentTimeMillis()));
                    return list;
                }).doOnError(throwable -> Log.i(TAG, "accept: " + throwable.getMessage())).observeOn(AndroidSchedulers.mainThread()).subscribeWith(new Observer<List<Object>>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.i(TAG, "onSubscribe: " + d.toString());
            }

            @Override
            public void onNext(List<Object> objects) {

            }

            @Override
            public void onError(Throwable e) {
                syncStatus = false;
                observable4 = true;
                log.error(TPUtility.getPrintStackTrace(e));
                sendCallbackForSync(callback);
                /*TPException appEx = new TPException();
                appEx.setErrorMessage("Internal system error. Please try again");
                try {
                    throw appEx;
                } catch (TPException ex) {
                    ex.printStackTrace();
                }*/

                Log.e(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onComplete() {
                observable4 = true;
                sendCallbackForSync(callback);
                Log.i(TAG, "onComplete: " + "Completed Successfully");
            }
        });

        Observable.zip(directions.subscribeOn(Schedulers.io()).onErrorReturn(throwable -> {
                    syncStatus = false;
                    log.error(TPUtility.getPrintStackTrace(throwable));
                    return null;
                }),
                durations.subscribeOn(Schedulers.io()).onErrorReturn(throwable -> {
                    syncStatus = false;
                    log.error(TPUtility.getPrintStackTrace(throwable));
                    return null;
                }),
                duties.subscribeOn(Schedulers.io()).onErrorReturn(throwable -> {
                    syncStatus = false;
                    log.error(TPUtility.getPrintStackTrace(throwable));
                    return null;
                }),
                features.subscribeOn(Schedulers.io()).onErrorReturn(throwable -> {
                    syncStatus = false;
                    log.error(TPUtility.getPrintStackTrace(throwable));
                    return null;
                }),
                gpsLocations.subscribeOn(Schedulers.io()).onErrorReturn(throwable -> {
                    syncStatus = false;
                    log.error(TPUtility.getPrintStackTrace(throwable));
                    return null;
                }),
                specialActivitiesLocation.subscribeOn(Schedulers.io()).onErrorReturn(throwable -> {
                    syncStatus = false;
                    log.error(TPUtility.getPrintStackTrace(throwable));
                    return null;
                }),
                locations.subscribeOn(Schedulers.io()).onErrorReturn(throwable -> {
                    syncStatus = false;
                    log.error(TPUtility.getPrintStackTrace(throwable));
                    return null;
                }),
                repeatOffenders.subscribeOn(Schedulers.io()).onErrorReturn(throwable -> {
                    syncStatus = false;
                    log.error(TPUtility.getPrintStackTrace(throwable));
                    return null;
                }),
                makesAndModels.subscribeOn(Schedulers.io()).onErrorReturn(throwable -> {
                    syncStatus = false;
                    log.error(TPUtility.getPrintStackTrace(throwable));
                    return null;
                }),
                (directionResponse, durationResponse, dutyResponse, featureResponse, gpsLocationResponse, specialActivitiesLocationResponse, locationResponse, repeatOffenderResponse, makeAndModelResponse) -> {
                    List<Object> list = new ArrayList<>();
                    if (fullSync) {
                        Direction.removeAll();
                        Duration.removeAll();
                        Duty.removeAll();
                        Feature.removeAll();
                        GPSLocation.removeAll();
                        SpecialActivitiesLocation.removeAll();
                        Location.removeAll();
                        RepeatOffender.removeAll();
                        MakeAndModel.removeAll();
                    }
                    try {
                        if (directionResponse.getResult() != null) {
                            for (Direction direction : directionResponse.getResult()) {
                                if (!fullSync && direction.getSyncDataId() != 0) {
                                    Direction.removeById(direction.getPrimaryKey());
                                    continue;
                                }
                                Direction.insertDirection(direction);
                            }
                            log.info("Updated " + directionResponse.getResult().size() + " Direction Records");
                        } else {
                            syncStatus = false;
                        }
                    } catch (Exception e) {
                        syncStatus = false;
                        log.error(TPUtility.getPrintStackTrace(e));
                    }
                    try {
                        if (durationResponse.getResult() != null) {
                            for (Duration duration : durationResponse.getResult()) {
                                if (!fullSync && duration.getSyncDataId() != 0) {
                                    Duration.removeById(duration.getPrimaryKey());
                                    continue;
                                }
                                Duration.insertDuration(duration);
                            }
                            log.info("Updated " + durationResponse.getResult().size() + " Duration Records");
                        } else {
                            syncStatus = false;
                        }
                    } catch (Exception e) {
                        syncStatus = false;
                        log.error(TPUtility.getPrintStackTrace(e));
                    }
                    try {
                        if (dutyResponse.getResult() != null) {
                            for (Duty duty : dutyResponse.getResult()) {
                                if (!fullSync && duty.getSyncDataId() != 0) {
                                    Duty.removeById(duty.getPrimaryKey());
                                    continue;
                                }
                                Duty.insertDuty(duty);

                            }
                            log.info("Updated " + dutyResponse.getResult().size() + " Duty Records");
                        } else {
                            syncStatus = false;
                        }
                    } catch (Exception e) {
                        syncStatus = false;
                        log.error(TPUtility.getPrintStackTrace(e));
                    }
                    try {
                        if (featureResponse.getResult() != null) {
                            for (Feature feature : featureResponse.getResult()) {
                                if (!fullSync && feature.getSyncDataId() != 0) {
                                    Feature.removeById(feature.getPrimaryKey());
                                    continue;
                                }
                                feature.setFeature(feature.getFeature().toUpperCase());
                                Feature.insertFeature(feature);

                            }
                            log.info("Updated " + featureResponse.getResult().size() + " Feature Records");
                        } else {
                            syncStatus = false;
                        }
                    } catch (Exception e) {
                        syncStatus = false;
                        log.error(TPUtility.getPrintStackTrace(e));
                    }
                    try {
                        if (gpsLocationResponse.getResult() != null) {
                            for (GPSLocation gpsLocation : gpsLocationResponse.getResult()) {
                                if (!fullSync && gpsLocation.getSyncDataId() != 0) {
                                    GPSLocation.removeById(gpsLocation.getPrimaryKey());
                                    continue;
                                }
                                GPSLocation.insertGPSLocation(gpsLocation);
                            }
                            log.info("Updated " + gpsLocationResponse.getResult().size() + " GPSLocation Records");
                        } else {
                            syncStatus = false;
                        }
                    } catch (Exception e) {
                        syncStatus = false;
                        log.error(TPUtility.getPrintStackTrace(e));
                    }
                    try {
                        if (specialActivitiesLocationResponse.getResult() != null) {
                            for (SpecialActivitiesLocation activitiesLocation : specialActivitiesLocationResponse.getResult()) {
                                SpecialActivitiesLocation.insertSpecialActivitiesLocation(activitiesLocation);
                            }
                            log.info("Updated " + specialActivitiesLocationResponse.getResult().size() + " SpecialActivitiesLocation Records");
                        } else {
                            syncStatus = false;
                        }
                    } catch (Exception e) {
                        syncStatus = false;
                        log.error(TPUtility.getPrintStackTrace(e));
                    }
                    try {
                        if (locationResponse.getResult() != null) {
                            for (Location location : locationResponse.getResult()) {
                                if (!fullSync && location.getSyncDataId() != 0) {
                                    Location.removeById(location.getPrimaryKey());
                                    continue;
                                }
                                Location.insertLocation(location);
                            }
                            log.info("Updated " + locationResponse.getResult().size() + " Location Records");
                        } else {
                            syncStatus = false;
                        }
                    } catch (Exception e) {
                        syncStatus = false;
                        log.error(TPUtility.getPrintStackTrace(e));
                    }

                    // this code is changed by mohit 12/01/2023.this code is used for restrict repeat offender in ofline mode
                    try {
                        if (repeatOffenderResponse.getResult() != null) {
                            /*for (RepeatOffender repeatOffender : repeatOffenderResponse.getResult()) {
                                if (!fullSync && repeatOffender.getSyncDataId() != 0) {
                                    RepeatOffender.removeById(repeatOffender.getPrimaryKey());
                                    continue;
                                }
                                //RepeatOffender.insertRepeatOffender(repeatOffender).subscribe();
                            }*/
                            //log.info("Updated " + repeatOffenderResponse.getResult().size() + " RepeatOffender Records");
                        } else {
                            syncStatus = false;
                        }
                    } catch (Exception e) {
                        syncStatus = false;
                        log.error(TPUtility.getPrintStackTrace(e));
                    }
                    //End
                    try {
                        if (makeAndModelResponse.getResult() != null) {
                            for (MakeAndModel makeAndModel : makeAndModelResponse.getResult()) {
                                if (!fullSync && makeAndModel.getSyncDataId() != 0) {
                                    MakeAndModel.removeById(makeAndModel.getPrimaryKey());
                                    continue;
                                }
                                MakeAndModel.insertMakeAndModel(makeAndModel);
                            }
                            log.info("Updated " + makeAndModelResponse.getResult().size() + " MakeAndModel Records");
                        } else {
                            syncStatus = false;
                        }
                    } catch (Exception e) {
                        syncStatus = false;
                        log.error(TPUtility.getPrintStackTrace(e));
                    }

                    list.add(directionResponse);
                    list.add(durationResponse);
                    list.add(dutyResponse);
                    list.add(featureResponse);
                    list.add(gpsLocationResponse);
                    list.add(specialActivitiesLocationResponse);
                    list.add(locationResponse);
                    list.add(repeatOffenderResponse);
                    list.add(makeAndModelResponse);
                    //Log.i(TAG, "apply: Time Taken results1" + (currentTime - System.currentTimeMillis()));
                    return list;
                }).observeOn(AndroidSchedulers.mainThread()).subscribeWith(new Observer<List<Object>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.i(TAG, "onSubscribe: " + d.toString());
            }

            @Override
            public void onNext(@NonNull List<Object> objects) {

            }

            @Override
            public void onError(@NonNull Throwable e) {
                syncStatus = false;
                log.error(TPUtility.getPrintStackTrace(e));
                /*TPException appEx = new TPException();
                appEx.setErrorMessage("Internal system error. Please try again");
                try {
                    throw appEx;
                } catch (TPException ex) {
                    ex.printStackTrace();
                }*/
                observable5 = true;
                sendCallbackForSync(callback);
                Log.e(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onComplete() {
                observable5 = true;
                sendCallbackForSync(callback);
            }
        });
        users.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(@NotNull Call<UserResponse> call, @NotNull Response<UserResponse> response) {
                try {
                    UserResponse user = response.body();
                    if (user != null && user.getResult() != null) {
                        if (fullSync) {
                            User.removeAll();
                        }
                        for (User user1 : user.getResult()) {
                            if (!fullSync && user1.getSyncDataId() != 0) {
                                User.removeById(user1.getPrimaryKey());
                                continue;
                            }
                            User.insertUser(user1);
                        }
                        log.info("Updated " + user.getResult().size() + " Users Records");
                    } else {
                        syncStatus = false;
                    }
                } catch (Exception e) {
                    syncStatus = false;
                    log.error(TPUtility.getPrintStackTrace(e));
                    e.printStackTrace();
                }
                observable6 = true;
                sendCallbackForSync(callback);
            }

            @Override
            public void onFailure(@androidx.annotation.NonNull Call<UserResponse> call, @androidx.annotation.NonNull Throwable t) {
                syncStatus = false;
                log.error(TPUtility.getPrintStackTrace(t));
                observable6 = true;
                sendCallbackForSync(callback);
            }
        });



        patrollers.enqueue(new Callback<PatrollersResponse>() {
            @Override
            public void onResponse(@NotNull Call<PatrollersResponse> call, @NotNull Response<PatrollersResponse> response) {
                try {
                    PatrollersResponse patrollersResponse = response.body();
                    Gson gson = new GsonBuilder()
                            .registerTypeAdapter(long.class, new LongDeserializer())
                            .create();


                    if (patrollersResponse != null && patrollersResponse.getResult() != null) {
                        if (fullSync) {
                            GenetecPatrollers.removeAll();
                        }
                        for (GenetecPatrollers genetecPatrollers : patrollersResponse.getResult()) {
                            if (!fullSync && genetecPatrollers.getSyncDataId() != 0) {
                                String jsonResponseString = String.valueOf(genetecPatrollers.getCreatedOn());
                                GenetecPatrollers model = gson.fromJson(jsonResponseString, GenetecPatrollers.class);

                                GenetecPatrollers.removeById(genetecPatrollers.getPrimaryKey());
                                continue;
                            }
                            GenetecPatrollers.insertPatrollers(genetecPatrollers);
                        }
                        log.info("Updated " + patrollersResponse.getResult().size() + " GenetecPatrollers Records");
                    } else {
                        syncStatus = false;
                    }
                } catch (Exception e) {
                    syncStatus = false;
                    log.error(TPUtility.getPrintStackTrace(e));
                    e.printStackTrace();
                }
                observable7 = true;
                sendCallbackForSync(callback);
            }

            @Override
            public void onFailure(@androidx.annotation.NonNull Call<PatrollersResponse> call, @androidx.annotation.NonNull Throwable t) {
                syncStatus = false;
                log.error(TPUtility.getPrintStackTrace(t));
                observable7 = true;
                sendCallbackForSync(callback);
            }
        });
    }

    void sendCallbackForSync(Handler.Callback callback) {
        if (observable1 && observable2 && observable3 && observable4 && observable5 && observable6 && observable7) {
            android.os.Message message = new android.os.Message();
            if (syncStatus) {
                message.what = 2;
            } else {
                message.what = 1;
            }
            syncStatus = true;
            observable1 = false;
            observable2 = false;
            observable3 = false;
            observable4 = false;
            observable5 = false;
            observable6 = false;
            observable7 = false;
            log.info("Sync Status is observable5 " + syncStatus);
            callback.handleMessage(message);
        }
    }

    public void updateTicketHistory() throws TPException {
        try {
            if (Feature.isSystemFeatureAllowed("disableTicketHistory")) {
                TicketHistory.removeAllTicketHistory();
                return;
            }
            int days = 0;
            // Check Feature Table
            if (Feature.isSystemFeatureAllowed("ticketHistoryDays")) {
                String daysStr = Feature.getFeatureValue("ticketHistoryDays");
                try {
                    days = Integer.parseInt(daysStr);
                } catch (Exception e) {
                    log.error(TPUtility.getPrintStackTrace(e));
                }
            }

            Date endDate = new Date();
            Date startDate = new Date();
            DeviceInfo deviceInfo = TPApplication.getInstance().getDeviceInfo();
            if (deviceInfo != null && deviceInfo.getLastSync() != null) {
                startDate = deviceInfo.getLastSync();
            }

            long diff = endDate.getTime() - startDate.getTime();
            float dayCount = ((float) diff) / (24f * 60f * 60f * 1000f);
            if (days != 0 && dayCount > days) {
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DAY_OF_MONTH, -days);
                startDate = cal.getTime();
            }

            // JSONArray objects = service.getTicketHistory(TPApplication.getInstance().custId, startDate, endDate);

            ApiRequest apiRequest = ServiceGenerator.createRxService(ApiRequest.class);
            Params params = new Params();
            params.setCustId(TPApplication.getInstance().custId);
            params.setStartDate(DateUtil.getSQLStringFromDate2(startDate));
            params.setEndDate(DateUtil.getSQLStringFromDate2(endDate));
            //  RepeatOffender_Rpc requestPOJO = new RepeatOffender_Rpc();
            RequestPOJO requestPOJO = new RequestPOJO();
            requestPOJO.setMethod("getTickeHistory");
            requestPOJO.setParams(params);
            // System.out.println("RequestObj**"+new Gson().toJson(requestPOJO));

            apiRequest.getTickeHistory(requestPOJO).enqueue(new Callback<TicketHistoryResponse>() {
                @Override
                public void onResponse(Call<TicketHistoryResponse> call, Response<TicketHistoryResponse> response) {
                    if (response.isSuccessful()) {
                        ArrayList<TicketHistory> ticketHistory = new ArrayList<TicketHistory>();
                        // TicketHistoryResponse ticketHistoryResponse = new TicketHistoryResponse();
                        ticketHistory = (ArrayList<TicketHistory>) response.body().getResult();
                        // ticketHistory = response.body();
                        if (ticketHistory != null && !ticketHistory.equals("")) {
                            for (int i = 0; i < ticketHistory.size(); i++) {

                                TicketHistory.insertTicketHistory(ticketHistory.get(i));
                            }
                        }
                        log.info("Updated  Ticket History Records");


                    }
                }

                @Override
                public void onFailure(Call<TicketHistoryResponse> call, Throwable t) {
                    //progressDialog.dismiss();
                    log.error(TPUtility.getPrintStackTrace(t));
                }

            });
           /* if (objects != null) {
                TicketHistory.removeAllTicketHistory();
                for (int i = 0; i < objects.length(); i++) {
                    TicketHistory ticketHistory = new TicketHistory((JSONObject) objects.get(i));
                    TicketHistory.insertTicketHistory(ticketHistory);
                }
                log.info("Updated " + objects.length() + " Ticket History Records");
            }*/
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            TPException appEx = new TPException();
            appEx.setErrorMessage("Internal system error. Please try again");
            throw appEx;
        }

    }


    public void syncChalkPictures(final ArrayList<SyncData> syncData, final Context context) throws TPException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ArrayList<String> uploadImages = new ArrayList<String>();
                    JSONArray chalkPictures = new JSONArray();

                    for (SyncData data : syncData) {
                        String table = data.getTableName();
                        String primaryKey = data.getPrimaryKey();
                        String opt = data.getActivity();
                        if (opt.equals("INSERT")) {
                            if (table.equals(TPConstant.TABLE_CHALK_PICTURES)) {
                                ChalkPicture picture = ChalkPicture.getChalkPictureByPrimaryKey(primaryKey);
                                if (picture != null) {
                                    chalkPictures.put(picture.getJSONObject());
                                    uploadImages.add(picture.getImagePath());
                                }
                            }
                        } else if (opt.equals("UPDATE")) {
                            if (table.equals(TPConstant.TABLE_CHALK_PICTURES)) {
                                ChalkPicture picture = ChalkPicture.getChalkPictureByPrimaryKey(primaryKey);
                                if (picture != null) {
                                    chalkPictures.put(picture.getJSONObject());
                                    uploadImages.add(picture.getImagePath());
                                }
                            }
                        }
                    }

                    if (service.syncChalkPictures(chalkPictures)) {
                        for (int i = 0; i < chalkPictures.length(); i++) {
                            String primaryKey = ((JSONObject) chalkPictures.get(i)).getString("picture_id");
                            SyncData.removeSyncData(TPConstant.TABLE_CHALK_PICTURES, primaryKey);
                        }
                    }

                    service.syncUploadImages(uploadImages);

                } catch (Exception e) {
                    Log.w("TicketPRO", TPUtility.getPrintStackTrace(e));
                }
            }
        }).start();
    }

    @Override
    public boolean syncTicketData(final ArrayList<SyncData> syncData, final Context context) throws TPException {
        boolean successStatus;
        String errorMessage = null;

        try {
            JSONArray deviceArray = new JSONArray();
            DeviceInfo deviceInfo = TPApplication.getInstance().getDeviceInfo();
            deviceArray.put(deviceInfo.getJSONObject());
            service.syncDevices(deviceArray);

            JSONArray tickets = new JSONArray();
            ArrayList<String> uploadImages = new ArrayList<String>();
            ArrayList<String> uploadVoiceComments = new ArrayList<String>();
            for (SyncData data : syncData) {
                String table = data.getTableName();
                String primaryKey = data.getPrimaryKey();

                if (table.equals(TPConstant.TABLE_TICKETS)) {
                    Ticket ticket = Ticket.getTicketsByPrimaryId(primaryKey);
                    if (ticket != null) {
                        tickets.put(ticket.getJSONObject());
                    }
                }
            }

            // Prepare Tickets JSON
            for (int i = 0; i < tickets.length(); i++) {
                try {
                    JSONObject object = tickets.getJSONObject(i);
                    long ticketId = object.getLong("ticket_id");
                    long citationNumber = object.getLong("citation_number");

                    JSONArray ticketComments = new JSONArray();
                    ArrayList<TicketComment> comments = TicketComment.getTicketComments(ticketId, citationNumber);
                    for (TicketComment comment : comments) {
                        ticketComments.put(comment.getJSONObject());
                        if (comment.isVoiceComment()) {
                            uploadVoiceComments.add(comment.getComment());
                        }
                    }
                    object.put("ticketComments", ticketComments);
                    JSONArray ticketPictures = new JSONArray();
                    ArrayList<TicketPicture> pictures = TicketPicture.getTicketPictures(ticketId, citationNumber);

                    for (TicketPicture picture : pictures) {
                        if ("Y".equalsIgnoreCase(picture.getLprNotification())) {
                            continue;
                        }
                        ticketPictures.put(picture.getJSONObject());
                        uploadImages.add(picture.getImagePath());
                    }
                    object.put("ticketPictures", ticketPictures);

                    JSONArray ticketViolations = new JSONArray();
                    ArrayList<TicketViolation> violations = TicketViolation.getTicketViolations(ticketId, citationNumber);
                    for (TicketViolation violation : violations) {
                        ticketViolations.put(violation.getJSONObject());
                    }

                    object.put("ticketViolations", ticketViolations);
                } catch (Exception e) {
                    log.error(TPUtility.getPrintStackTrace(e));
                }
            }

            // Update Tickets
            JSONArray syncTicketResult = service.syncTickets(tickets);
            if (syncTicketResult != null) {
                if (syncTicketResult.length() > 0) {

                    // Log Errors
                    ArrayList<String> failedTickets = new ArrayList<String>();
                    for (int i = 0; i < syncTicketResult.length(); i++) {
                        JSONObject failure = syncTicketResult.getJSONObject(i);
                        failedTickets.add(failure.getString("ticketId"));

                        // Remove duplicate citations
                        if (failure.getString("message") != null
                                && failure.getString("message").contains("Citation already exists")) {
                            SyncData.removeSyncData(TPConstant.TABLE_TICKETS, failure.getString("ticketId"));
                        }

                        log.error("Sync Error: citation# " + failure.getString("citationNumber") + ", error - "
                                + failure.getString("message"));
                    }

                    // Remove success records
                    for (int i = 0; i < tickets.length(); i++) {
                        String primaryKey = ((JSONObject) tickets.get(i)).getString("ticket_id");

                        if (!failedTickets.contains(primaryKey)) {
                            SyncData.removeSyncData(TPConstant.TABLE_TICKETS, primaryKey);
                            //Ticket.removeOlderTicketById(Long.parseLong(primaryKey));
                        }
                    }

                    successStatus = false;
                    errorMessage = syncTicketResult.getJSONObject(0).getString("message");
                } else {
                    for (int i = 0; i < tickets.length(); i++) {
                        String primaryKey = ((JSONObject) tickets.get(i)).getString("ticket_id");
                        SyncData.removeSyncData(TPConstant.TABLE_TICKETS, primaryKey);
                    }

                    successStatus = true;
                }
            } else {
                successStatus = false;
            }

            // Upload Pictures
            service.syncUploadImages(uploadImages);

            // Upload Voice Comments
            final int custId = TPApplication.getInstance().custId;
            final ArrayList<String> voiceMemos = uploadVoiceComments;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    boolean hasUploaded = false;
                    for (int i = 0; i < voiceMemos.size(); i++) {
                        hasUploaded = TPUtility.uploadFile(TPUtility.getVoiceMemosFolder() + voiceMemos.get(i),
                                TPConstant.SERVICE_URL + "/uploadfile", custId);
                        if (!hasUploaded) {
                            TPUtility.markPendingVoiceComment(voiceMemos.get(i));
                        }
                    }
                }
            }).start();

        } catch (JSONRPCException e) {
            Log.e("TicketPRO", "Sync Error " + e.getMessage());
            throw new TPException(TPException.NETWORK_IO_ERROR, e.getMessage());

        } catch (NumberFormatException e) {
            Log.e("TicketPRO", "NumberFormatException " + e.getMessage());
            throw new TPException(TPException.GENERIC_ERROR, e.getMessage());

        } catch (Exception e) {
            Log.e("TicketPRO", "Error " + e.getMessage());
            throw new TPException(TPException.GENERIC_ERROR, e.getMessage());
        }

        return successStatus;
    }


    @Override
    public boolean syncRepeatOffender(String stateCode, String plate, int violId, int custId, String created_date) throws TPException {
        boolean successStatus;

        // update repeatOffender count
        try {
            successStatus = service.updateRepeatOffendersCount(stateCode, plate, violId, custId, created_date);
        } catch (JSONRPCException e) {
            Log.e("TicketPRO", "Sync Error " + e.getMessage());
            throw new TPException(TPException.NETWORK_IO_ERROR, e.getMessage());

        } catch (NumberFormatException e) {
            Log.e("TicketPRO", "NumberFormatException " + e.getMessage());
            throw new TPException(TPException.GENERIC_ERROR, e.getMessage());

        } catch (Exception e) {
            Log.e("TicketPRO", "Error " + e.getMessage());
            throw new TPException(TPException.GENERIC_ERROR, e.getMessage());
        }

        return successStatus;
    }

    @Override
    public boolean syncRepeatOffender(RepeatOffenderParams repeatOffenderParams) throws TPException {
        boolean successStatus;
        try {
            successStatus = service.updateRepeatOffendersCount(repeatOffenderParams);
        } catch (JSONRPCException e) {
            Log.e("TicketPRO", "Sync Error " + e.getMessage());
            throw new TPException(TPException.NETWORK_IO_ERROR, e.getMessage());

        } catch (NumberFormatException e) {
            Log.e("TicketPRO", "NumberFormatException " + e.getMessage());
            throw new TPException(TPException.GENERIC_ERROR, e.getMessage());

        } catch (Exception e) {
            Log.e("TicketPRO", "Error " + e.getMessage());
            throw new TPException(TPException.GENERIC_ERROR, e.getMessage());
        }

        return successStatus;
    }

    public void syncData(final ArrayList<SyncData> syncData, final Context context) throws TPException {
        new Thread(() -> {
            try {
                JSONArray deviceArray = new JSONArray();
                DeviceInfo deviceInfo = TPApplication.getInstance().getDeviceInfo();
                deviceArray.put(deviceInfo.getJSONObject());
                service.syncDevices(deviceArray);

                JSONArray reports = new JSONArray();
                JSONArray tickets = new JSONArray();
                JSONArray updateTickets = new JSONArray();
                JSONArray chalks = new JSONArray();
                JSONArray specialActivityReports = new JSONArray();
                ArrayList<String> uploadImages = new ArrayList<String>();
                ArrayList<String> uploadVoiceComments = new ArrayList<String>();

                for (SyncData data : syncData) {
                    String table = data.getTableName();
                    String primaryKey = data.getPrimaryKey();
                    String opt = data.getActivity();
                    if (opt.equals("INSERT")) {
                        if (table.equals(TPConstant.TABLE_DUTY_REPORTS)) {
                            DutyReport report = DutyReport.getDutyReportByPrimaryKey(primaryKey);
                            if (report != null) {
                                reports.put(report.getJSONObject());
                            }
                        } else if (table.equals(TPConstant.TABLE_TICKETS)) {
                            Ticket ticket = Ticket.getTicketsByPrimaryId(primaryKey);
                            if (ticket != null) {
                                tickets.put(ticket.getJSONObject());
                            }
                        } else if (table.equals(TPConstant.TABLE_CHALKS)) {
                            ChalkVehicle chalk = ChalkVehicle.getChalkVehicleByPrimaryKey(primaryKey);
                            if (chalk != null) {
                                chalks.put(chalk.getJSONObject());
                            }
                            try {
                                ALPRChalk alprChalk = ALPRChalk.getChalkVehicleById(Long.parseLong(primaryKey));
                                if (alprChalk != null) {
                                    chalks.put(alprChalk.getJSONObject());
                                }
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                        } else if (table.equals(TPConstant.TABLE_SPECIAL_ACTIVITY_REPORTS)) {
                            SpecialActivityReport report = SpecialActivityReport
                                    .getSpecialActivityReportByPrimaryKey(primaryKey);
                            if (report != null) {
                                specialActivityReports.put(report.getJSONObject());
                            }
                        }
                    } else if (opt.equals("UPDATE")) {
                        if (table.equals(TPConstant.TABLE_TICKETS)) {
                            Ticket ticket = Ticket.getTicketsByPrimaryId(primaryKey);
                            if (ticket != null) {
                                updateTickets.put(ticket.getJSONObject());
                            }
                        }
                    }
                }

                // Prepare Tickets JSON
                for (int i = 0; i < tickets.length(); i++) {
                    try {
                        JSONObject object = tickets.getJSONObject(i);
                        long ticketId = object.getLong("ticket_id");
                        long citationNumber = object.getLong("citation_number");

                        JSONArray ticketComments = new JSONArray();
                        ArrayList<TicketComment> comments = TicketComment.getTicketComments(ticketId, citationNumber);
                        for (TicketComment comment : comments) {
                            ticketComments.put(comment.getJSONObject());
                            if (comment.isVoiceComment()) {
                                uploadVoiceComments.add(comment.getComment());
                            }
                        }

                        object.put("ticketComments", ticketComments);

                        JSONArray ticketPictures = new JSONArray();
                        ArrayList<TicketPicture> pictures = TicketPicture.getTicketPictures(ticketId, citationNumber);
                        for (TicketPicture picture : pictures) {
                            if ("Y".equalsIgnoreCase(picture.getLprNotification())) {
                                continue;
                            }

                            ticketPictures.put(picture.getJSONObject());
                            uploadImages.add(picture.getImagePath());
                        }

                        object.put("ticketPictures", ticketPictures);

                        JSONArray ticketViolations = new JSONArray();
                        ArrayList<TicketViolation> violations = TicketViolation.getTicketViolations(ticketId, citationNumber);
                        for (TicketViolation violation : violations) {
                            ticketViolations.put(violation.getJSONObject());
                        }

                        object.put("ticketViolations", ticketViolations);

                    } catch (Exception e) {
                        log.error(TPUtility.getPrintStackTrace(e));
                    }
                }

                // Prepare Chalks JSON
                for (int i = 0; i < chalks.length(); i++) {
                    try {
                        JSONObject object = chalks.getJSONObject(i);
                        long chalkId = object.getLong("chalk_id");

                        JSONArray chalkPictures = new JSONArray();
                        ArrayList<ChalkPicture> pictures = ChalkPicture.getChalkPictures(chalkId);
                        for (ChalkPicture picture : pictures) {
                            chalkPictures.put(picture.getJSONObject());
                            uploadImages.add(picture.getImagePath());
                        }

                        object.put("chalkPictures", chalkPictures);
                        JSONArray chalkComments = new JSONArray();
                        ArrayList<ChalkComment> comments = ChalkComment.getChalkComments(chalkId);
                        for (ChalkComment comment : comments) {
                            chalkComments.put(comment.getJSONObject());
                        }

                        object.put("chalkComments", chalkComments);

                    } catch (Exception e) {
                        log.error(TPUtility.getPrintStackTrace(e));
                    }
                }

                // Update Tickets
                try {
                    JSONArray syncTicketResult = service.syncTickets(tickets);
                    if (syncTicketResult != null) {
                        for (int i = 0; i < tickets.length(); i++) {
                            String primaryKey = ((JSONObject) tickets.get(i)).getString("ticket_id");
                            SyncData.removeSyncData(TPConstant.TABLE_TICKETS, primaryKey);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Update Tickets
                try {
                    if (service.syncUpdatesTickets(updateTickets)) {
                        for (int i = 0; i < updateTickets.length(); i++) {
                            String primaryKey = ((JSONObject) tickets.get(i)).getString("ticket_id");
                            SyncData.removeSyncData(TPConstant.TABLE_TICKETS, primaryKey);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Update Chalks
                if (service.syncChalks(chalks)) {
                    for (int i = 0; i < chalks.length(); i++) {
                        String primaryKey = ((JSONObject) chalks.get(i)).getString("chalk_id");
                        SyncData.removeSyncData(TPConstant.TABLE_CHALKS, primaryKey);
                        SyncData.removeSyncData(TPConstant.TABLE_ALPR_CHALK, primaryKey);

                    }
                }

                // Upload Pictures
                service.syncUploadImages(uploadImages);

                // Upload Voice Comments
                final int custId = TPApplication.getInstance().custId;
                final ArrayList<String> voiceMemos = uploadVoiceComments;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        boolean hasUploaded = false;
                        for (int i = 0; i < voiceMemos.size(); i++) {
                            hasUploaded = TPUtility.uploadFile(TPUtility.getVoiceMemosFolder() + voiceMemos.get(i),
                                    TPConstant.FILE_UPLOAD + "/uploadfile", custId);
                            if (!hasUploaded) {
                                TPUtility.markPendingVoiceComment(voiceMemos.get(i));
                            }
                        }
                    }
                }).start();

            } catch (Exception e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }
        }).start();
    }


    @Override
    public void setServiceHandler(ServiceHandler service) {
        this.service = service;
    }

    @Override
    public User doLogin(String username, String password) throws TPException {
        try {
            return service.doLogin(username, password);
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            TPException appEx = new TPException();
            appEx.setErrorMessage("Internal system error. Please try again");
            throw appEx;
        }
    }

    @Override
    public String getVersionNo() {
        return "TicketPro v1.0";
    }

    @Override
    public ArrayList<Color> getColors() throws TPException {
        try {
            CustomerInfo custInfo = TPApplication.getInstance().getCustomerInfo();
            int custId = custInfo.getCustId();
            ArrayList<Color> colors = new ArrayList<Color>();

            if (TPApplication.getInstance().dbConfigured) {
                return Color.getColors(custId);
            }

            JSONArray colorsArray = service.getColors(custId, true);
            for (int i = 0; i < colorsArray.length(); i++) {
                colors.add(new Color(colorsArray.getJSONObject(i)));
            }

            return colors;

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            TPException appEx = new TPException();
            appEx.setErrorMessage("Internal system error. Please try again");
            throw appEx;
        }
    }

    @Override
    public ArrayList<Body> getBodies() throws TPException {
        try {
            CustomerInfo custInfo = TPApplication.getInstance().getCustomerInfo();
            int custId = custInfo.getCustId();

            if (TPApplication.getInstance().dbConfigured) {
                return Body.getBodies(custId);
            }

            ArrayList<Body> bodies = new ArrayList<Body>();
            if (BaseActivityImpl.instance.isNetworkConnected()) {
                JSONArray bodiesArray = service.getBodies(custId, true);
                for (int i = 0; i < bodiesArray.length(); i++) {
                    bodies.add(new Body(bodiesArray.getJSONObject(i)));
                }
            }

            return bodies;
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            TPException appEx = new TPException();
            appEx.setErrorMessage("Internal system error. Please try again");
            throw appEx;
        }

    }

    @Override
    public ArrayList<Contact> getContacts() throws TPException {
        try {
            CustomerInfo custInfo = TPApplication.getInstance().getCustomerInfo();
            int custId = custInfo.getCustId();
            if (TPApplication.getInstance().dbConfigured) {
                return Contact.getContacts();
            }

            ArrayList<Contact> contacts = new ArrayList<Contact>();
            JSONArray contactsArray = service.getContacts(custId, true);
            for (int i = 0; i < contactsArray.length(); i++) {
                contacts.add(new Contact(contactsArray.getJSONObject(i)));
            }

            return contacts;

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            TPException appEx = new TPException();
            appEx.setErrorMessage("Internal system error. Please try again");
            throw appEx;
        }
    }

    @Override
    public ArrayList<Comment> getComments() throws TPException {
        try {
            CustomerInfo custInfo = TPApplication.getInstance().getCustomerInfo();
            int custId = custInfo.getCustId();

            if (TPApplication.getInstance().dbConfigured) {
                return Comment.getComments();
            }

            ArrayList<Comment> comments = new ArrayList<Comment>();
            JSONArray commentsArray = service.getComments(custId, true);

            if (commentsArray != null) {
                for (int i = 0; i < commentsArray.length(); i++) {
                    comments.add(new Comment(commentsArray.getJSONObject(i)));
                }
            }

            return comments;

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));

            TPException appEx = new TPException();
            appEx.setErrorMessage("Internal system error. Please try again");
            throw appEx;
        }
    }

    @Override
    public ArrayList<Feature> getFeatures() throws TPException {
        try {
            CustomerInfo custInfo = TPApplication.getInstance().getCustomerInfo();
            int custId = custInfo.getCustId();

            if (TPApplication.getInstance().dbConfigured) {
                return Feature.getFeatures(custId);
            }

            ArrayList<Feature> list = new ArrayList<Feature>();

            JSONArray array = service.getFeatures(custId, true);
            for (int i = 0; i < array.length(); i++) {
                list.add(new Feature(array.getJSONObject(i)));
            }
            return list;
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            TPException appEx = new TPException();
            appEx.setErrorMessage("Internal system error. Please try again");
            throw appEx;
        }

    }

    @Override
    public ArrayList<Direction> getDirections() throws TPException {
        try {
            CustomerInfo custInfo = TPApplication.getInstance().getCustomerInfo();
            int custId = custInfo.getCustId();

            if (TPApplication.getInstance().dbConfigured) {
                return Direction.getDirections();
            }

            ArrayList<Direction> list = new ArrayList<Direction>();
            JSONArray array = service.getDirections(custId, true);
            for (int i = 0; i < array.length(); i++) {
                list.add(new Direction(array.getJSONObject(i)));
            }
            return list;
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            TPException appEx = new TPException();
            appEx.setErrorMessage("Internal system error. Please try again");
            throw appEx;
        }

    }

    @Override
    public ArrayList<Duration> getDurations() throws TPException {
        try {
            if (TPApplication.getInstance().dbConfigured) {
                return Duration.getDurations();
            }

            ArrayList<Duration> list = new ArrayList<Duration>();

            JSONArray array = service.getDurations(TPApplication.getInstance().custId, true);
            for (int i = 0; i < array.length(); i++) {
                list.add(new Duration(array.getJSONObject(i)));
            }
            return list;
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            TPException appEx = new TPException();
            appEx.setErrorMessage("Internal system error. Please try again");
            throw appEx;
        }

    }

    @Override
    public ArrayList<Duty> getDuties() throws TPException {
        try {
            if (TPApplication.getInstance().dbConfigured) {
                return Duty.getDuties();
            }

            ArrayList<Duty> list = new ArrayList<Duty>();
            JSONArray array = service.getDuties(TPApplication.getInstance().custId, true);
            for (int i = 0; i < array.length(); i++) {
                list.add(new Duty(array.getJSONObject(i)));
            }

            return list;

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            TPException appEx = new TPException();
            appEx.setErrorMessage("Internal system error. Please try again");
            throw appEx;
        }

    }

    @Override
    public ArrayList<Location> getLocations() throws TPException {
        try {
            CustomerInfo custInfo = TPApplication.getInstance().getCustomerInfo();
            int custId = custInfo.getCustId();

            if (TPApplication.getInstance().dbConfigured) {
                return Location.getLocations(custId);
            }

            ArrayList<Location> list = new ArrayList<Location>();
            JSONArray array = service.getLocations(custId, true);
            for (int i = 0; i < array.length(); i++) {
                list.add(new Location(array.getJSONObject(i)));
            }
            return list;
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            TPException appEx = new TPException();
            appEx.setErrorMessage("Internal system error. Please try again");
            throw appEx;
        }

    }

    @Override
    public ArrayList<GPSLocation> getGPSLocations() throws TPException {
        try {
            CustomerInfo custInfo = TPApplication.getInstance().getCustomerInfo();
            int custId = custInfo.getCustId();

            if (TPApplication.getInstance().dbConfigured) {
                return GPSLocation.getGPSLocations(custId);
            }

            ArrayList<GPSLocation> list = new ArrayList<GPSLocation>();

            JSONArray array = service.getGPSLocations(custId, true);
            for (int i = 0; i < array.length(); i++) {
                list.add(new GPSLocation(array.getJSONObject(i)));
            }

            return list;
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            TPException appEx = new TPException();
            appEx.setErrorMessage("Internal system error. Please try again");
            throw appEx;
        }

    }

    @Override
    public ArrayList<MakeAndModel> getMakeAndModels() throws TPException {
        try {
            CustomerInfo custInfo = TPApplication.getInstance().getCustomerInfo();
            int custId = custInfo.getCustId();

            if (TPApplication.getInstance().dbConfigured) {
                return MakeAndModel.getMakesAndModels(custId);
            }

            ArrayList<MakeAndModel> list = new ArrayList<MakeAndModel>();

            JSONArray array = service.getMakesAndModels(custId, true);
            for (int i = 0; i < array.length(); i++) {
                list.add(new MakeAndModel(array.getJSONObject(i)));
            }
            return list;
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            TPException appEx = new TPException();
            appEx.setErrorMessage("Internal system error. Please try again");
            throw appEx;
        }

    }

    @Override
    public ArrayList<Message> getMessages() throws TPException {
        try {
            if (TPApplication.getInstance().dbConfigured) {
                User userInfo = TPApplication.getInstance().getUserInfo();
                if (userInfo != null) {
                    return Message.getMessages(userInfo.getDepartment());
                }
            }

            ArrayList<Message> list = new ArrayList<Message>();
            JSONArray array = service.getMessages(TPApplication.getInstance().custId, true);
            for (int i = 0; i < array.length(); i++) {
                list.add(new Message(array.getJSONObject(i)));
            }

            return list;

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            TPException appEx = new TPException();
            appEx.setErrorMessage("Internal system error. Please try again");
            throw appEx;
        }
    }

    @Override
    public ArrayList<Message> getServerMessages() throws TPException {
        try {
            ArrayList<Message> list = new ArrayList<Message>();
            JSONArray array = service.getMessages(TPApplication.getInstance().custId, true);
            for (int i = 0; i < array.length(); i++) {
                list.add(new Message(array.getJSONObject(i)));
            }

            return list;

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));

            TPException appEx = new TPException();
            appEx.setErrorMessage("Internal system error. Please try again");
            throw appEx;
        }
    }

    @Override
    public ArrayList<Meter> getMeters() throws TPException {
        try {
            CustomerInfo custInfo = TPApplication.getInstance().getCustomerInfo();
            int custId = custInfo.getCustId();

            if (TPApplication.getInstance().dbConfigured) {
                return Meter.getMeters(custId);
            }

            ArrayList<Meter> list = new ArrayList<Meter>();

            JSONArray array = service.getMeters(custId, true);
            for (int i = 0; i < array.length(); i++) {
                list.add(new Meter(array.getJSONObject(i)));
            }
            return list;
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            TPException appEx = new TPException();
            appEx.setErrorMessage("Internal system error. Please try again");
            throw appEx;
        }

    }

    @Override
    public ArrayList<Permit> getPermits() throws TPException {
        try {
            CustomerInfo custInfo = TPApplication.getInstance().getCustomerInfo();
            int custId = custInfo.getCustId();


            ArrayList<Permit> list = new ArrayList<Permit>();

            JSONArray array = service.getPermits(custId, true);
            for (int i = 0; i < array.length(); i++) {
                list.add(new Permit(array.getJSONObject(i)));
            }
            return list;
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            TPException appEx = new TPException();
            appEx.setErrorMessage("Internal system error. Please try again");
            throw appEx;
        }

    }

    @Override
    public ArrayList<State> getStates() throws TPException {
        try {
            CustomerInfo custInfo = TPApplication.getInstance().getCustomerInfo();
            int custId = custInfo.getCustId();

            if (TPApplication.getInstance().dbConfigured) {
                return State.getStates(custId);
            }

            ArrayList<State> list = new ArrayList<State>();

            JSONArray array = service.getStates(custId, true);
            for (int i = 0; i < array.length(); i++) {
                list.add(new State(array.getJSONObject(i)));
            }
            return list;
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            TPException appEx = new TPException();
            appEx.setErrorMessage("Internal system error. Please try again");
            throw appEx;
        }

    }

    @Override
    public ArrayList<Ticket> getTickets(int userId, int deviceId, String userType) throws TPException {
        try {
            return Ticket.getTickets(userId, userType);
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            TPException appEx = new TPException();
            appEx.setErrorMessage("Internal system error. Please try again.");
            throw appEx;
        }
    }

    @Override
    public ArrayList<StreetPrefix> getStreetPrefixes() throws TPException {
        try {
            CustomerInfo custInfo = TPApplication.getInstance().getCustomerInfo();
            int custId = custInfo.getCustId();

            if (TPApplication.getInstance().dbConfigured) {
                return StreetPrefix.getStreetPrefixes(custId);
            }

            ArrayList<StreetPrefix> list = new ArrayList<StreetPrefix>();

            JSONArray array = service.getStreetPrefixes(custId, true);
            for (int i = 0; i < array.length(); i++) {
                list.add(new StreetPrefix(array.getJSONObject(i)));
            }

            return list;
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            TPException appEx = new TPException();
            appEx.setErrorMessage("Internal system error. Please try again");
            throw appEx;
        }
    }

    @Override
    public ArrayList<StreetSuffix> getStreetSuffixes() throws TPException {
        try {
            CustomerInfo custInfo = TPApplication.getInstance().getCustomerInfo();
            int custId = custInfo.getCustId();

            if (TPApplication.getInstance().dbConfigured) {
                return StreetSuffix.getStreetSuffixes();
            }

            ArrayList<StreetSuffix> list = new ArrayList<StreetSuffix>();

            JSONArray array = service.getStreetSuffixes(custId, true);
            for (int i = 0; i < array.length(); i++) {
                list.add(new StreetSuffix(array.getJSONObject(i)));
            }
            return list;
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            TPException appEx = new TPException();
            appEx.setErrorMessage("Internal system error. Please try again.");
            throw appEx;
        }

    }

    @Override
    public ArrayList<User> getUsers() throws TPException {
        ArrayList<User> list = new ArrayList<User>();
        try {
            CustomerInfo custInfo = TPApplication.getInstance().getCustomerInfo();
            int custId = custInfo.getCustId();

        /*    if (TPApplication.getInstance().dbConfigured) {
                return User.getUsers(TPConstant.MODULE_NAME);
            }*/
            JSONArray array = service.getUsers(custId);
            if (array != null && array.length() > 0) {
                User.removeAll();

                for (int i = 0; i < array.length(); i++) {
                    User user = new User((JSONObject) array.get(i));
                    User.insertUser(user);
                }
                //log.info("Updated " + objects.length() + " Users Records");
            }
            list = User.getUsers(TPConstant.MODULE_NAME);
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            TPException appEx = new TPException();
            appEx.setErrorMessage("Internal system error. Please try again.");
            throw appEx;
        }

        return list;
    }

    @Override
    public ArrayList<GenetecPatrollers> getGenetecPatrollers() throws TPException {
        ArrayList<GenetecPatrollers> list = new ArrayList<GenetecPatrollers>();
        try {
            CustomerInfo custInfo = TPApplication.getInstance().getCustomerInfo();
            int custId = custInfo.getCustId();

        /*    if (TPApplication.getInstance().dbConfigured) {
                return User.getUsers(TPConstant.MODULE_NAME);
            }*/
            JSONArray array = service.getUsers(custId);
            if (array != null && array.length() > 0) {
                GenetecPatrollers.removeAll();

                for (int i = 0; i < array.length(); i++) {
                    GenetecPatrollers genetecPatrollers = new GenetecPatrollers((JSONObject) array.get(i));
                    GenetecPatrollers.insertPatrollers(genetecPatrollers);
                }
                //log.info("Updated " + objects.length() + " Users Records");
            }
            list = GenetecPatrollers.getPatrollers(TPConstant.MODULE_NAME);
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            TPException appEx = new TPException();
            appEx.setErrorMessage("Internal system error. Please try again.");
            throw appEx;
        }

        return list;
    }

    @Override
    public ArrayList<Zone> getZones() throws TPException {
        try {
            CustomerInfo custInfo = TPApplication.getInstance().getCustomerInfo();
            int custId = custInfo.getCustId();

            if (TPApplication.getInstance().dbConfigured) {
                return Zone.getZones(custId);
            }

            ArrayList<Zone> list = new ArrayList<Zone>();
            JSONArray array = service.getZones(custId, true);
            for (int i = 0; i < array.length(); i++) {
                list.add(new Zone(array.getJSONObject(i)));
            }
            return list;
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            TPException appEx = new TPException();
            appEx.setErrorMessage("Internal system error. Please try again.");
            throw appEx;
        }
    }

    @Override
    public ArrayList<Violation> getViolations() throws TPException {

        try {
            CustomerInfo custInfo = TPApplication.getInstance().getCustomerInfo();
            int custId = custInfo.getCustId();
            if (TPApplication.getInstance().dbConfigured) {
                return Violation.getViolations(custId);
            }

            ArrayList<Violation> list = new ArrayList<Violation>();
            JSONArray array = service.getViolations(custId, true);
            for (int i = 0; i < array.length(); i++) {
                list.add(new Violation(array.getJSONObject(i)));
            }
            return list;

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            TPException appEx = new TPException();
            appEx.setErrorMessage("Internal system error. Please try again.");
            throw appEx;
        }

    }

    @Override
    public ArrayList<TicketComment> getLiveTicketComments(long citationNumber) throws TPException {
        try {
            ArrayList<TicketComment> list = new ArrayList<TicketComment>();
            JSONArray array = service.getTicketComments(citationNumber);
            for (int i = 0; i < array.length(); i++) {
                list.add(new TicketComment(array.getJSONObject(i)));
            }

            return list;
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            TPException appEx = new TPException();
            appEx.setErrorMessage("Internal system error. Please try again.");
            throw appEx;
        }
    }

    @Override
    public ArrayList<TicketPicture> getLiveTicketPictures(long citationNumber) throws TPException {
        try {
            ArrayList<TicketPicture> list = new ArrayList<TicketPicture>();
            JSONArray array = service.getTicketPictures(citationNumber);
            for (int i = 0; i < array.length(); i++) {
                list.add(new TicketPicture(array.getJSONObject(i)));
            }
            return list;
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            TPException appEx = new TPException();
            appEx.setErrorMessage("Internal system error. Please try again.");
            throw appEx;
        }

    }

    @Override
    public ArrayList<TicketViolation> getLiveTicketViolations(long citationNumber) throws TPException {
        try {
            ArrayList<TicketViolation> list = new ArrayList<TicketViolation>();
            JSONArray array = service.getTicketViolations(citationNumber);

            for (int i = 0; i < array.length(); i++) {
                list.add(new TicketViolation(array.getJSONObject(i)));
            }
            return list;
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            TPException appEx = new TPException();
            appEx.setErrorMessage("Internal system error. Please try again.");
            throw appEx;
        }

    }

    @Override
    public void getLiveTicketViolations1(long citationNumber, TicketViolationHandler context) throws TPException {
        try {
            ApiRequest apiRequest = ServiceGenerator.createRxService(ApiRequest.class);
            TicketViolation_Rpc requestPOJO = new TicketViolation_Rpc();
            requestPOJO.setMethod("getTicketViolations");
            TicketViolationRequest params = new TicketViolationRequest();
            params.setCustId(TPApplication.getInstance().custId);
            params.setCitationNumberId((int) citationNumber);
            requestPOJO.setTicketViolationRequest(params);
            System.out.println("RequestObj**" + new Gson().toJson(requestPOJO));
            apiRequest.getTicketViolations(requestPOJO).enqueue(new Callback<TicketViolationResponse>() {
                @Override
                public void onResponse(@NonNull Call<TicketViolationResponse> call, @NonNull Response<TicketViolationResponse> response) {
                    if (response.isSuccessful() && response.code()==200) {

                        TicketViolationResponse ticketViolationResponse = response.body();
                        ArrayList<TicketViolation> ticketViolations = new ArrayList<TicketViolation>();
                        ticketViolations = (ArrayList<TicketViolation>) ticketViolationResponse.getResult();
                        if (ticketViolations.size() != 0)
                            context.ticketViolationHandler(ticketViolations);
                        Log.i(TAG, "onResponse: getTicketViolations result");
                    }
                }

                @Override
                public void onFailure(@NonNull Call<TicketViolationResponse> call, @NonNull Throwable t) {
                    Log.e(TAG, "onFailure: getTicketViolations failed");
                }
            });
            // return service.deleteTicketComments(citationNumber, violationId, comment);
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }


    }

    @Override
    public ArrayList<ChalkVehicle> getChalkVehicles(int userId, int deviceId) throws TPException {
        try {
            if (TPApplication.getInstance().dbConfigured) {
                return ChalkVehicle.getChalkVehicles(userId);
            }

            ArrayList<ChalkVehicle> list = new ArrayList<ChalkVehicle>();

            JSONArray array = service.getChalkVehicles(userId, deviceId);
            for (int i = 0; i < array.length(); i++) {
                list.add(new ChalkVehicle(array.getJSONObject(i)));
            }
            return list;
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            TPException appEx = new TPException();
            appEx.setErrorMessage("Internal system error. Please try again.");
            throw appEx;
        }
    }

    @Override
    public ArrayList<ChalkVehicle> getAllChalkedVehicles(int deviceId) throws TPException {
        try {
            if (TPApplication.getInstance().dbConfigured) {
                return ChalkVehicle.getAllChalkedVehicle();
            }

            ArrayList<ChalkVehicle> list = new ArrayList<ChalkVehicle>();

            JSONArray array = service.getChalkVehicle(deviceId);
            for (int i = 0; i < array.length(); i++) {
                list.add(new ChalkVehicle(array.getJSONObject(i)));
            }

            return list;

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            TPException appEx = new TPException();
            appEx.setErrorMessage("Internal system error. Please try again.");
            throw appEx;
        }
    }

    @Override
    public ArrayList<ChalkPicture> getChalkPictures(long chalkId) throws TPException {

        try {
            if (TPApplication.getInstance().dbConfigured) {
                return ChalkPicture.getChalkPictures(chalkId);
            }

            ArrayList<ChalkPicture> list = new ArrayList<ChalkPicture>();

            JSONArray array = service.getChalkPictures(chalkId);
            for (int i = 0; i < array.length(); i++) {
                list.add(new ChalkPicture(array.getJSONObject(i)));
            }
            return list;
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            TPException appEx = new TPException();
            appEx.setErrorMessage("Internal system error. Please try again.");
            throw appEx;
        }

    }

    @Override
    public ArrayList<ChalkComment> getChalkComments(long chalkId) throws TPException {
        try {
            if (TPApplication.getInstance().dbConfigured) {
                return ChalkComment.getChalkComments(chalkId);
            }

            ArrayList<ChalkComment> list = new ArrayList<ChalkComment>();

            JSONArray array = service.getChalkComments(chalkId);
            for (int i = 0; i < array.length(); i++) {
                list.add(new ChalkComment(array.getJSONObject(i)));
            }
            return list;
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            TPException appEx = new TPException();
            appEx.setErrorMessage("Internal system error. Please try again.");
            throw appEx;
        }

    }

    @Override
    public ArrayList<String> getViolationTitles() throws TPException {
        try {
            ArrayList<String> list = new ArrayList<String>();
            for (Violation object : getViolations()) {
                list.add(object.getTitle());
            }
            return list;
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            TPException appEx = new TPException();
            appEx.setErrorMessage("Internal system error. Please try again.");
            throw appEx;
        }
    }

    @Override
    public ArrayList<String> getBodyTitles() throws TPException {
        try {
            ArrayList<String> list = new ArrayList<String>();
            for (Body object : getBodies()) {
                list.add(object.getTitle());
            }
            return list;
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            TPException appEx = new TPException();
            appEx.setErrorMessage("Internal system error. Please try again.");
            throw appEx;
        }
    }

    @Override
    public ArrayList<String> getColorTitles() throws TPException {
        try {
            ArrayList<String> list = new ArrayList<String>();
            for (Color object : getColors()) {
                list.add(object.getTitle());
            }
            return list;
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            TPException appEx = new TPException();
            appEx.setErrorMessage("Internal system error. Please try again.");
            throw appEx;
        }
    }

    @Override
    public ArrayList<String> getCommentTitles() throws TPException {
        try {
            ArrayList<String> list = new ArrayList<String>();
            for (Comment object : getComments()) {
                list.add(object.getTitle());
            }
            return list;
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            TPException appEx = new TPException();
            appEx.setErrorMessage("Internal system error. Please try again.");
            throw appEx;
        }
    }

    @Override
    public ArrayList<String> getStateTitles() throws TPException {
        try {
            ArrayList<String> list = new ArrayList<String>();
            for (State object : getStates()) {
                list.add(object.getTitle());
            }
            return list;
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            TPException appEx = new TPException();
            appEx.setErrorMessage("Internal system error. Please try again.");
            throw appEx;
        }
    }

    @Override
    public ArrayList<String> getMakeAndModelTitles() throws TPException {
        try {
            ArrayList<String> list = new ArrayList<String>();
            for (MakeAndModel object : getMakeAndModels()) {
                list.add(object.getMakeTitle());
            }
            return list;
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            TPException appEx = new TPException();
            appEx.setErrorMessage("Internal system error. Please try again.");
            throw appEx;
        }
    }

    @Override
    public ArrayList<String> getZonesTitles() throws TPException {
        try {
            ArrayList<String> list = new ArrayList<String>();
            for (Zone object : getZones()) {
                list.add(object.getTitle());
            }
            return list;
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            TPException appEx = new TPException();
            appEx.setErrorMessage("Internal system error. Please try again.");
            throw appEx;
        }
    }

    @Override
    public ArrayList<String> getDurationsTitles() throws TPException {
        try {
            ArrayList<String> list = new ArrayList<String>();
            for (Duration object : getDurations()) {
                list.add(object.getTitle());
            }
            return list;
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            TPException appEx = new TPException();
            appEx.setErrorMessage("Internal system error. Please try again.");
            throw appEx;
        }
    }

    @Override
    public ArrayList<String> getDirectionsTitles() throws TPException {
        try {
            ArrayList<String> list = new ArrayList<String>();
            for (Direction object : getDirections()) {
                list.add(object.getTitle());
            }
            return list;
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            TPException appEx = new TPException();
            appEx.setErrorMessage("Internal system error. Please try again.");
            throw appEx;
        }
    }

    @Override
    public ArrayList<String> getStreetPrefixTitles() throws TPException {
        try {
            ArrayList<String> list = new ArrayList<String>();
            for (StreetPrefix object : getStreetPrefixes()) {
                list.add(object.getTitle());
            }
            return list;
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            TPException appEx = new TPException();
            appEx.setErrorMessage("Internal system error. Please try again.");
            throw appEx;
        }
    }

    @Override
    public ArrayList<String> getStreetSuffixTitles() throws TPException {
        try {
            ArrayList<String> list = new ArrayList<String>();
            for (StreetSuffix object : getStreetSuffixes()) {
                list.add(object.getTitle());
            }
            return list;
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            TPException appEx = new TPException();
            appEx.setErrorMessage("Internal system error. Please try again.");
            throw appEx;
        }
    }

    @Override
    public ArrayList<String> getSpecialActivitiesTitles() throws TPException {
        try {
            ArrayList<String> list = new ArrayList<String>();
            for (SpecialActivity object : getSpecialActivities(TPApplication.getInstance().custId)) {
                list.add(object.getTitle());
            }
            return list;
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            TPException appEx = new TPException();
            appEx.setErrorMessage("Internal system error. Please try again.");
            throw appEx;
        }
    }

    @Override
    public ArrayList<String> getSpecialDispositionsTitles() throws TPException {
        try {
            ArrayList<String> list = new ArrayList<String>();
            for (SpecialActivityDisposition object : getSpecialActivityDispositions(
                    TPApplication.getInstance().custId)) {
                list.add(object.getTitle());
            }
            return list;
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            TPException appEx = new TPException();
            appEx.setErrorMessage("Internal system error. Please try again.");
            throw appEx;
        }
    }

    @Override
    public ArrayList<Hotlist> getHotlist() throws TPException {
        try {
            CustomerInfo custInfo = TPApplication.getInstance().getCustomerInfo();
            int custId = custInfo.getCustId();

            if (TPApplication.getInstance().dbConfigured) {
                return Hotlist.getHostlist();
            }

            ArrayList<Hotlist> list = new ArrayList<Hotlist>();
            JSONArray array = service.getHotlist(custId, true);
            for (int i = 0; i < array.length(); i++) {
                list.add(new Hotlist(array.getJSONObject(i)));
            }

            return list;
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            TPException appEx = new TPException();
            appEx.setErrorMessage("Internal system error. Please try again.");
            throw appEx;
        }
    }

    @Override
    public ArrayList<VoidTicketReason> getVoidTicketReasons(int custId) throws TPException {
        try {
            if (TPApplication.getInstance().dbConfigured) {
                return VoidTicketReason.getVoidReasons(custId);
            }

            ArrayList<VoidTicketReason> list = new ArrayList<VoidTicketReason>();

            JSONArray array = service.getVoidTicketReasons(custId, true);
            for (int i = 0; i < array.length(); i++) {
                list.add(new VoidTicketReason(array.getJSONObject(i)));
            }
            return list;
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            TPException appEx = new TPException();
            appEx.setErrorMessage("Internal system error. Please try again.");
            throw appEx;
        }

    }

    @Override
    public ArrayList<String> getLocationTitles() throws TPException {
        try {
            ArrayList<String> list = new ArrayList<String>();
            for (Location object : getLocations()) {
                list.add(object.getLocation());
            }

            return list;
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            TPException appEx = new TPException();
            appEx.setErrorMessage("Internal system error. Please try again.");
            throw appEx;
        }
    }

    @Override
    public ArrayList<SpecialActivity> getSpecialActivities(int custId) throws Exception {
        try {
            if (TPApplication.getInstance().dbConfigured) {
                return SpecialActivity.getSpecialActivities();
            }

            ArrayList<SpecialActivity> list = new ArrayList<SpecialActivity>();

            JSONArray array = service.getSpecialActivities(custId, true);
            for (int i = 0; i < array.length(); i++) {
                list.add(new SpecialActivity(array.getJSONObject(i)));
            }
            return list;
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            TPException appEx = new TPException();
            appEx.setErrorMessage("Internal system error. Please try again.");
            throw appEx;
        }
    }

    @Override
    public ArrayList<SpecialActivityDisposition> getSpecialActivityDispositions(int custId) throws Exception {
        try {
            if (TPApplication.getInstance().dbConfigured) {
                return SpecialActivityDisposition.getSpecialActivityDispositions();
            }

            ArrayList<SpecialActivityDisposition> list = new ArrayList<SpecialActivityDisposition>();

            JSONArray array = service.getSpecialActivityDispositions(custId, true);
            for (int i = 0; i < array.length(); i++) {
                list.add(new SpecialActivityDisposition(array.getJSONObject(i)));
            }
            return list;
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            TPException appEx = new TPException();
            appEx.setErrorMessage("Internal system error. Please try again.");
            throw appEx;
        }
    }

    @Override
    public ArrayList<PrintMacro> getPrintMacros(int custId) throws Exception {
        try {
            if (TPApplication.getInstance().dbConfigured) {
                return PrintMacro.getPrintMacros();
            }

            ArrayList<PrintMacro> list = new ArrayList<PrintMacro>();

            JSONArray array = service.getPrintMacros(custId, true);
            for (int i = 0; i < array.length(); i++) {
                list.add(new PrintMacro(array.getJSONObject(i)));
            }
            return list;
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            TPException appEx = new TPException();
            appEx.setErrorMessage("Internal system error. Please try again.");
            throw appEx;
        }
    }

    @Override
    public ArrayList<PrintTemplate> getPrintTemplates(int custId) throws Exception {
        try {
            if (TPApplication.getInstance().dbConfigured) {
                return PrintTemplate.getPrintTemplates(custId);
            }

            ArrayList<PrintTemplate> list = new ArrayList<>();

            JSONArray array = service.getPrintTemplates(custId, true);
            for (int i = 0; i < array.length(); i++) {
                list.add(new PrintTemplate(array.getJSONObject(i)));
            }
            return list;
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            TPException appEx = new TPException();
            appEx.setErrorMessage("Internal system error. Please try again.");
            throw appEx;
        }
    }

    @Override
    public ArrayList<CallInList> getCallInList(int custId) throws Exception {
        try {
            if (TPApplication.getInstance().dbConfigured) {
                return CallInList.getCallInList();
            }

            ArrayList<CallInList> list = new ArrayList<CallInList>();
            JSONArray array = service.getCallInList(custId, true);
            for (int i = 0; i < array.length(); i++) {
                list.add(new CallInList(array.getJSONObject(i)));
            }
            return list;
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            TPException appEx = new TPException();
            appEx.setErrorMessage("Internal system error. Please try again.");
            throw appEx;
        }
    }

    @Override
    public Meter searchMeterHistory(String meter) throws TPException {
        try {
            JSONArray array = service.searchMeters(TPApplication.getInstance().custId, meter);

            if (array.length() > 0) {
                return new Meter(array.getJSONObject(0));
            }

            return null;
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            TPException appEx = new TPException();
            appEx.setErrorMessage("Internal system error. Please try again.");
            throw appEx;
        }
    }


    @Override
    public void searchMeterHistory1(String meter, MeterHandler context) throws TPException {
        try {
            ApiRequest apiRequest = ServiceGenerator.createRxService(ApiRequest.class);
            MeterRequest_Rpc requestPOJO = new MeterRequest_Rpc();
            requestPOJO.setMethod("searchMeters");
            MeterParams params = new MeterParams();
            params.setCustId(TPApplication.getInstance().custId);
            params.setMeter(meter);
            requestPOJO.setMeterParams(params);
            System.out.println("RequestObj**" + new Gson().toJson(requestPOJO));
            apiRequest.searchMeters(requestPOJO).enqueue(new Callback<MeterReponse>() {
                @Override
                public void onResponse(@NonNull Call<MeterReponse> call, @NonNull Response<MeterReponse> response) {
                    if (response.isSuccessful()&& response.code()==200) {

                        MeterReponse meterReponse = response.body();
                        ArrayList<Meter> meters = new ArrayList<Meter>();
                        Meter meterData = null;
                        meters = (ArrayList<Meter>) meterReponse.getResult();
                        if (meters.size() != 0)
                            meterData = meters.get(0);
                        context.meterResponse(meterData, meter);
                        Log.i(TAG, "onResponse: searchMeters get");
                    }
                }

                @Override
                public void onFailure(@NonNull Call<MeterReponse> call, @NonNull Throwable t) {
                    Log.e(TAG, "onFailure: searchMeters failed");
                }
            });
            // return service.deleteTicketComments(citationNumber, violationId, comment);
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }

    }

    @Override
    public Permit searchPermitHistory(String permit) throws TPException {
        try {
            JSONArray array = service.searchPermits(TPApplication.getInstance().custId, permit);
            if (array.length() > 0)
                return new Permit(array.getJSONObject(0));

            return null;
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            TPException appEx = new TPException();
            appEx.setErrorMessage("Internal system error. Please try again.");
            throw appEx;
        }
    }


    @Override
    public void searchPermitHistory1(String permit, PermitHandler context) throws TPException {
        try {
            ApiRequest apiRequest = ServiceGenerator.createRxService(ApiRequest.class);
            PermitRequest_Rpc requestPOJO = new PermitRequest_Rpc();
            requestPOJO.setMethod("searchPermits");
            PermitParams params = new PermitParams();
            params.setCustId(TPApplication.getInstance().custId);
            params.setPermit(permit);
            requestPOJO.setPermitParams(params);
            System.out.println("RequestObj**" + new Gson().toJson(requestPOJO));
            apiRequest.searchPermits(requestPOJO).enqueue(new Callback<PermitResponse>() {
                @Override
                public void onResponse(@NonNull Call<PermitResponse> call, @NonNull Response<PermitResponse> response) {
                    if (response.isSuccessful()&&response.code()==200) {

                        PermitResponse permitResponse = response.body();
                        ArrayList<Permit> permits = new ArrayList<Permit>();
                        Permit permitData = null;
                        permits = (ArrayList<Permit>) permitResponse.getResult();
                        if (permits.size() != 0)
                            permitData = permits.get(0);
                        context.permitResponse(permitData);
                        Log.i(TAG, "onResponse: searchMeters get");
                    }else {
                        context.permitResponseFail();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<PermitResponse> call, @NonNull Throwable t) {
                    Log.e(TAG, "onFailure: searchMeters failed");
                }
            });
            // return service.deleteTicketComments(citationNumber, violationId, comment);
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }

    }

    @Override
    public Ticket searchPlateHistory(String plate, String state) throws TPException {
        try {
            JSONArray array = service.searchPlates(TPApplication.getInstance().custId, plate, state);
            if (array.length() > 0) {
                return new Ticket(array.getJSONObject(0));
            }

            return null;

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            TPException appEx = new TPException();
            appEx.setErrorMessage("Internal system error. Please try again.");
            throw appEx;
        }
    }

    @Override
    public ArrayList<Ticket> searchPlateHistories(String plate, String state) throws TPException {
        try {
            ArrayList<Ticket> list = new ArrayList<Ticket>();
            JSONArray array = service.searchPlates(TPApplication.getInstance().custId, plate, state);
            for (int i = 0; i < array.length(); i++) {
                list.add(new Ticket(array.getJSONObject(i)));
            }

            return list;

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            TPException appEx = new TPException();
            appEx.setErrorMessage("Internal system error. Please try again.");
            throw appEx;
        }
    }

    @Override
    public ArrayList<Ticket> searchPlateHistoriesVin(String vin, String state) throws TPException {
        try {
            ArrayList<Ticket> list = new ArrayList<Ticket>();
            JSONArray array = service.searchPlatesVin(TPApplication.getInstance().custId, vin, state);
            for (int i = 0; i < array.length(); i++) {
                list.add(new Ticket(array.getJSONObject(i)));
            }

            return list;

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            TPException appEx = new TPException();
            appEx.setErrorMessage("Internal system error. Please try again.");
            throw appEx;
        }
    }

    @Override
    public Ticket searchVinHistory(String vin, String state) throws TPException {
        try {
            JSONArray array = service.searchVins(TPApplication.getInstance().custId, vin, state);
            if (array.length() > 0)
                return new Ticket(array.getJSONObject(0));

            return null;
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            TPException appEx = new TPException();
            appEx.setErrorMessage("Internal system error. Please try again.");
            throw appEx;
        }
    }



    @Override
    public void searchVinHistory1(String vin, String state, SearchVinHistoryHandler context) throws TPException {
        try {
            ApiRequest apiRequest = ServiceGenerator.createRxService(ApiRequest.class);
            PermitRequest_Rpc requestPOJO = new PermitRequest_Rpc();
            requestPOJO.setMethod("searchVins");
            PermitParams params = new PermitParams();
            params.setCustId(TPApplication.getInstance().custId);
            params.setVin(vin);
            params.setState(state);
            requestPOJO.setPermitParams(params);
            System.out.println("RequestObj**" + new Gson().toJson(requestPOJO));
            apiRequest.searchVins(requestPOJO).enqueue(new Callback<SearchVinResponse>() {
                @Override
                public void onResponse(@NonNull Call<SearchVinResponse> call, @NonNull Response<SearchVinResponse> response) {
                    if (response.isSuccessful()&&response.code()==200) {

                        SearchVinResponse searchVinResponse = response.body();
                        ArrayList<Ticket> tickets = new ArrayList<Ticket>();
                        Ticket ticketData = null;
                        tickets = (ArrayList<Ticket>) searchVinResponse.getResult();
                        if (tickets.size() != 0)
                            ticketData = tickets.get(0);
                        context.searchVinHistory(ticketData);
                        Log.i(TAG, "onResponse: searchVins get");
                    }
                }

                @Override
                public void onFailure(@NonNull Call<SearchVinResponse> call, @NonNull Throwable t) {
                    Log.e(TAG, "onFailure: searchVins failed");
                }
            });
            // return service.deleteTicketComments(citationNumber, violationId, comment);
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }

    @Override
    public Permit searchPermitVinHistory(String vin, String state) throws TPException {
        try {
            JSONArray array = service.searchPermitVins(TPApplication.getInstance().custId, vin, state);
            if (array.length() > 0)
                return new Permit(array.getJSONObject(0));

            return null;
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            TPException appEx = new TPException();
            appEx.setErrorMessage("Internal system error. Please try again.");
            throw appEx;
        }
    }



    @Override
    public void searchPermitVinHistory1(String vin, String state, PermitHandler context) throws TPException {
        try {
            ApiRequest apiRequest = ServiceGenerator.createRxService(ApiRequest.class);
            PermitRequest_Rpc requestPOJO = new PermitRequest_Rpc();
            requestPOJO.setMethod("searchPermitVins");
            PermitParams params = new PermitParams();
            params.setCustId(TPApplication.getInstance().custId);
            params.setVin(vin);
            params.setState(state);
            requestPOJO.setPermitParams(params);
            System.out.println("RequestObj**" + new Gson().toJson(requestPOJO));
            apiRequest.searchPermitVins(requestPOJO).enqueue(new Callback<PermitResponse>() {
                @Override
                public void onResponse(@NonNull Call<PermitResponse> call, @NonNull Response<PermitResponse> response) {
                    if (response.isSuccessful()&&response.code()==200) {

                        PermitResponse permitResponse = response.body();
                        ArrayList<Permit> permits = new ArrayList<Permit>();
                        Permit permitData = null;
                        permits = (ArrayList<Permit>) permitResponse.getResult();
                        if (permits.size() != 0)
                            permitData = permits.get(0);
                        context.permitVinResponse(permitData, vin);
                        Log.i(TAG, "onResponse: searchPermitVins get");
                    }
                }

                @Override
                public void onFailure(@NonNull Call<PermitResponse> call, @NonNull Throwable t) {
                    Log.e(TAG, "onFailure: searchPermitVins failed");
                }
            });
            // return service.deleteTicketComments(citationNumber, violationId, comment);
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }

    }

    @Override
    public ArrayList<Hotlist> searchHotlist(String plate, String state) throws TPException {
        try {
            ArrayList<Hotlist> list = new ArrayList<Hotlist>();
            JSONArray array = service.searchHotlist(TPApplication.getInstance().custId, plate, state);
            for (int i = 0; i < array.length(); i++) {
                list.add(new Hotlist(array.getJSONObject(i)));
            }

            return list;
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            TPException appEx = new TPException();
            appEx.setErrorMessage("Internal system error. Please try again.");
            throw appEx;
        }
    }

    @Override
    public boolean updateTickets(ArrayList<Ticket> tickets) {
        if (tickets == null || tickets.size() == 0)
            return false;

        JSONArray ticketsJSON = new JSONArray();
        for (int i = 0; i < tickets.size(); i++) {
            ticketsJSON.put(tickets.get(i).getJSONObject());
        }

        return service.syncUpdatesTickets(ticketsJSON);
    }

    @Override
    public ArrayList<LocationGroup> getLocationGroups(int custId) throws Exception {

        try {
            if (TPApplication.getInstance().dbConfigured) {
                return LocationGroup.getLocationGroups(custId);
            }

            ArrayList<LocationGroup> list = new ArrayList<LocationGroup>();
            JSONArray array = service.getLocationGroups(custId, true);
            for (int i = 0; i < array.length(); i++) {
                list.add(new LocationGroup(array.getJSONObject(i)));
            }

            return list;

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            TPException appEx = new TPException();
            appEx.setErrorMessage("Internal system error. Please try again.");
            throw appEx;
        }
    }

    @Override
    public ArrayList<LocationGroupLocation> getLocationGroupLocations(int custId) throws Exception {

        try {
            if (TPApplication.getInstance().dbConfigured) {
                return LocationGroupLocation.getLocationGroupLocations(custId);
            }

            ArrayList<LocationGroupLocation> list = new ArrayList<LocationGroupLocation>();
            JSONArray array = service.getLocationGroupLocations(custId, true);
            for (int i = 0; i < array.length(); i++) {
                list.add(new LocationGroupLocation(array.getJSONObject(i)));
            }

            return list;

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            TPException appEx = new TPException();
            appEx.setErrorMessage("Internal system error. Please try again.");
            throw appEx;
        }

    }

    @Override
    public ArrayList<CommentGroup> getCommentGroups(int custId) throws Exception {

        try {
            if (TPApplication.getInstance().dbConfigured) {
                return CommentGroup.getCommentGroups();
            }

            ArrayList<CommentGroup> list = new ArrayList<CommentGroup>();
            JSONArray array = service.getCommentGroups(custId, true);
            for (int i = 0; i < array.length(); i++) {
                list.add(new CommentGroup(array.getJSONObject(i)));
            }

            return list;

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            TPException appEx = new TPException();
            appEx.setErrorMessage("Internal system error. Please try again.");
            throw appEx;
        }

    }

    @Override
    public ArrayList<CommentGroupComment> getCommentGroupComments(int custId) throws Exception {

        try {
            if (TPApplication.getInstance().dbConfigured) {
                return CommentGroupComment.getCommentGroupComments();
            }

            ArrayList<CommentGroupComment> list = new ArrayList<CommentGroupComment>();
            JSONArray array = service.getCommentGroupComments(custId, true);
            for (int i = 0; i < array.length(); i++) {
                list.add(new CommentGroupComment(array.getJSONObject(i)));
            }

            return list;

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            TPException appEx = new TPException();
            appEx.setErrorMessage("Internal system error. Please try again.");
            throw appEx;
        }

    }

    @Override
    public ArrayList<ViolationGroup> getViolationGroups(int custId) throws Exception {

        try {
            if (TPApplication.getInstance().dbConfigured) {
                return ViolationGroup.getViolationGroups(custId);
            }

            ArrayList<ViolationGroup> list = new ArrayList<ViolationGroup>();
            JSONArray array = service.getViolationGroups(custId, true);
            for (int i = 0; i < array.length(); i++) {
                list.add(new ViolationGroup(array.getJSONObject(i)));
            }

            return list;

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            TPException appEx = new TPException();
            appEx.setErrorMessage("Internal system error. Please try again.");
            throw appEx;
        }

    }

    @Override
    public ArrayList<ViolationGroupViolation> getViolationGroupViolations(int custId) throws Exception {

        try {

            if (TPApplication.getInstance().dbConfigured) {
                return ViolationGroupViolation.getViolationGroupViolations(custId);
            }

            ArrayList<ViolationGroupViolation> list = new ArrayList<ViolationGroupViolation>();
            JSONArray array = service.getViolationGroupViolations(custId, true);
            for (int i = 0; i < array.length(); i++) {
                list.add(new ViolationGroupViolation(array.getJSONObject(i)));
            }

            return list;

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            TPException appEx = new TPException();
            appEx.setErrorMessage("Internal system error. Please try again.");
            throw appEx;
        }

    }

    @Override
    public ArrayList<Location> getLocations(String groups) throws TPException {

        ArrayList<Location> locations = new ArrayList<Location>();
        if (groups == null || groups.equals(""))
            return locations;

        String[] groupCodes = groups.split(",");
        for (int i = 0; i < groupCodes.length; i++) {
            try {
                ArrayList<Location> groupLocations = LocationGroup.getLocationsByGroup(groupCodes[i]);
                for (Location location : groupLocations) {
                    if (!locations.contains(location)) {
                        locations.add(location);
                    }
                }
            } catch (Exception e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }
        }

        return locations;
    }

    @Override
    public ArrayList<Violation> getViolations(String groups) throws TPException {

        ArrayList<Violation> violations = new ArrayList<Violation>();
        if (groups == null || groups.equals(""))
            return violations;

        String[] groupCodes = groups.split(",");
        for (int i = 0; i < groupCodes.length; i++) {
            try {
                ArrayList<Violation> groupViolations = ViolationGroup.getViolationsByGroup(groupCodes[i]);
                for (Violation violation : groupViolations) {
                    if (!violations.contains(violation)) {
                        violations.add(violation);
                    }
                }
            } catch (Exception e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }
        }

        return violations;
    }

    @Override
    public ArrayList<Comment> getComments(String groups) throws TPException {

        ArrayList<Comment> comments = new ArrayList<Comment>();
        if (groups == null || groups.equals("")) {
            return comments;
        }

        String[] groupCodes = groups.split(",");
        for (int i = 0; i < groupCodes.length; i++) {
            try {
                ArrayList<Comment> groupComments = CommentGroup.getCommentsByGroup(groupCodes[i]);
                for (Comment comment : groupComments) {
                    if (!comments.contains(comment)) {
                        comments.add(comment);
                    }
                }
            } catch (Exception e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }
        }

        return comments;
    }

    @Override
    public ArrayList<Comment> getCommentsById(int[] Ids) throws TPException {
        ArrayList<Comment> comments = new ArrayList<>();
        if (Ids == null) {
            return comments;
        }
        for (int id : Ids) {
            try {
                Comment comment;
                comment = Comment.getCommentById(id);
                comments.add(comment);
            } catch (Exception e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }
        }
        return comments;
    }

    @Override
    public ChalkVehicle searchChalkHistory(String plate, String state) throws TPException {

        try {
            JSONArray array = service.searchChalks(TPApplication.getInstance().custId, plate, state);
            if (array.length() > 0)
                return new ChalkVehicle(array.getJSONObject(0));

            return null;
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            TPException appEx = new TPException();
            appEx.setErrorMessage("Internal system error. Please try again.");
            throw appEx;
        }
    }

    @Override
    public RepeatOffender searchRepeatOffenders(String plate, String violation) throws TPException {

        try {
            JSONArray array = service.searchRepeatOffenders(TPApplication.getInstance().custId, plate, violation);
            if (array.length() > 0) {
                return new RepeatOffender(array.getJSONObject(0));
            }

            return null;

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            TPException appEx = new TPException();
            appEx.setErrorMessage("Internal system error. Please try again.");
            throw appEx;
        }
    }

    @Override
    public boolean updateGCMRegistrationId(String deviceName, String GCMRegId) throws TPException {
        try {
            RequestPOJO requestPOJO = new RequestPOJO();
            Params params = new Params();
            params.setDeviceIName(deviceName);
            params.setRegistrationId(GCMRegId);
            requestPOJO.setParams(params);
            requestPOJO.setMethod("updateGCMRegistrationId");
            ApiRequest api = ServiceGenerator.createRxService(ApiRequest.class);
            Call<ResponseBody> gcm = api.updateGCMRegistrationID(requestPOJO);
            Response<ResponseBody> execute = gcm.execute();
            return execute.isSuccessful();
            //return service.updateGCMRegistrationId(deviceName, GCMRegId);
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            TPException appEx = new TPException();
            appEx.setErrorMessage("Internal system error. Please try again.");
            throw appEx;
        }
    }

    @Override
    public boolean updateCallInReport(CallInReport report) {
        if (report == null)
            return false;

        JSONArray callInReports = new JSONArray();
        callInReports.put(report.getJSONObject());

        return service.syncCallInReports(callInReports);
    }

    @Override
    public boolean sendErrorLog(ArrayList<ErrorLog> errors) throws TPException {
        try {
            return service.sendErrorLog(errors);
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            TPException appEx = new TPException();
            appEx.setErrorMessage("Internal system error. Please try again.");
            throw appEx;
        }
    }

    @Override
    public ArrayList<ChalkVehicle> getUserChalks(int userId, Date fromDate, Date toDate, int durationId)
            throws TPException {

        ArrayList<ChalkVehicle> chalks = new ArrayList<ChalkVehicle>();
        JSONArray objects;
        try {
            objects = service.getUserChalks(TPApplication.getInstance().custId, userId, fromDate, toDate, durationId);
            for (int i = 0; i < objects.length(); i++) {
                JSONObject chalk = (JSONObject) objects.get(i);
                ChalkVehicle chalkVehicle = new ChalkVehicle(chalk);
                if (chalkVehicle.getLatitude().equalsIgnoreCase("null")) {
                    chalkVehicle.setLatitude("");
                }
                if (chalkVehicle.getLongitude().equalsIgnoreCase("null")) {
                    chalkVehicle.setLongitude("");
                }
                chalks.add(chalkVehicle);

                if (chalk.has("chalkComments")) {
                    JSONArray comments = chalk.getJSONArray("chalkComments");
                    for (int j = 0; j < comments.length(); j++) {
                        chalkVehicle.getComments().add(new ChalkComment((JSONObject) comments.get(j)));
                    }
                }

                if (chalk.has("chalkPictures")) {
                    JSONArray pictures = chalk.getJSONArray("chalkPictures");
                    for (int j = 0; j < pictures.length(); j++) {
                        chalkVehicle.getChalkPictures().add(new ChalkPicture((JSONObject) pictures.get(j)));
                    }
                }
            }
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }

        return chalks;
    }

    @Override
    public boolean updateChalkStatus(long chalkId, String status, String reason) throws TPException {
        try {

            return service.updateChalkStatus(chalkId, status, reason);

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            TPException appEx = new TPException();
            appEx.setErrorMessage("Internal system error. Please try again.");
            throw appEx;
        }
    }

    @Override
    public boolean updateTicketsComments(int violationId, ArrayList<TicketComment> ticketComments) {
        if (ticketComments == null)
            return false;

        try {
            ApiRequest apiRequest = ServiceGenerator.createRxService(ApiRequest.class);
            TicketComment_Rpc requestPOJO = new TicketComment_Rpc();
            requestPOJO.setMethod("updateTicketComments");
            TicketCommentRequest params = new TicketCommentRequest();
            params.setViolationId(violationId);
            params.setTicketCommment(ticketComments);
            requestPOJO.setParams(params);
            //System.out.println("RequestObj**"+new Gson().toJson(requestPOJO));
            apiRequest.updateTicketComments(requestPOJO).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    if (response.isSuccessful()&&response.code()==200) {
                        // progressDialog.dismiss();
                        // Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        ArrayList<SyncData> syncDataList = new ArrayList<SyncData>();
                        SyncData syncCommentData = new SyncData();
                        syncCommentData.setActivity("INSERT");
                        syncCommentData.setPrimaryKey(TicketComment.getNextPrimaryId() + "");
                        syncCommentData.setActivityDate(new Date());
                        syncCommentData.setCustId(TPApplication.getInstance().custId);
                        syncCommentData.setTableName(TPConstant.TABLE_TICKET_COMMENTS);
                        syncCommentData.setStatus("Pending");

                        try {
                            SyncData.insertSyncData(syncCommentData).subscribe();
                            //DatabaseHelper.getInstance().insertOrReplace(syncCommentData.getContentValues(), "sync_data");
                        } catch (SQLiteException e) {
                            log.error(TPUtility.getPrintStackTrace(e));

                        } catch (Exception e) {
                            log.error(TPUtility.getPrintStackTrace(e));
                        }

                        syncDataList.add(syncCommentData);
                        Log.i(TAG, "onResponse: comment updated");
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                    Log.e(TAG, "onFailure: comment updation failed");
                }
            });
            // return service.updateTicketComments(violationId, ticketComments);
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }

        return false;
    }

    @Override
    public boolean deleteTicketComment(long citationNumber, int violationId, String comment) {
        try {
            ApiRequest apiRequest = ServiceGenerator.createRxService(ApiRequest.class);
            RequestPOJO requestPOJO = new RequestPOJO();
            requestPOJO.setMethod("deleteTicketComments");
            Params params = new Params();
            params.setCitationNumber(citationNumber);
            params.setViolationId(violationId);
            params.setCommentText(comment);
            requestPOJO.setParams(params);
            apiRequest.deleteTicketComments(requestPOJO).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    if (response.isSuccessful()&&response.code()==200) {
                        // progressDialog.dismiss();
                        // Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "onResponse: comment deleted");
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                    Log.e(TAG, "onFailure: comment deletion failed");
                }
            });
            // return service.deleteTicketComments(citationNumber, violationId, comment);
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }

        return false;
    }

    /**
     * This code is changed by mohit for delete picture from ticket 27/02/2023.
     *
     * @param citationNumber
     * @param imageName
     * @return
     */
    @Override
    public boolean deleteTicketPicture(long citationNumber, String imageName) {
        try {

            //This code is changed by mohit 27/02/2023
            ApiRequest apiRequest = ServiceGenerator.createRxService(ApiRequest.class);
            RequestPOJO requestPOJO = new RequestPOJO();
            requestPOJO.setMethod("deleteTicketPicture");
            Params params = new Params();
            params.setCitationNumber(citationNumber);
            params.setImageName(imageName);
            params.setCustId(TPApplication.getInstance().custId);
            requestPOJO.setParams(params);
            apiRequest.deleteTicketPicture(requestPOJO).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    if (response.isSuccessful()&&response.code()==200) {
                        // progressDialog.dismiss();
                        // Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "onResponse: picture deleted");
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                    Log.e(TAG, "onFailure: Picture Deletion failed");
                }
            });
            //End
            //return service.deleteTicketPicture(citationNumber, imageName);
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }

        return false;
    }

    @Override
    public boolean updateTicketPicture(long citationNumber, TicketPicture picture) {
        if (picture == null) {
            return false;
        }

        try {
            return service.updateTicketPicture(citationNumber, picture);
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }

        return false;
    }


    @Override
    public boolean updateTicketPicture1(long citationNumber, TicketPicture picture) {
        if (picture == null) {
            return false;
        }

        try {

            ApiRequest apiRequest = ServiceGenerator.createRxService(ApiRequest.class);
            TicketPicture_Rpc requestPOJO = new TicketPicture_Rpc();
            requestPOJO.setMethod("updateTicketPicture");
            TicketPictureRequestParams params = new TicketPictureRequestParams();
            params.setCitationNumber((int) citationNumber);
            params.setCustId(TPApplication.getInstance().getCustId());
            params.setticketPicture(picture);
            requestPOJO.setParams(params);
            int i = Ticket.getPhotoCount(String.valueOf(citationNumber));
            Ticket.ticketUpdatePhotoCount(String.valueOf(citationNumber),i +1);

            //  System.out.println("RequestObj**"+new Gson().toJson(requestPOJO));
            apiRequest.updateTicketPicture(requestPOJO).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    if (response.isSuccessful()&&response.code()==200) {
                        // progressDialog.dismiss();
                        // Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                        Log.e(TAG, "onResponse: TicketPicture updated");
                    }
                }


                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                    Log.e(TAG, "onFailure: TicketPicture updated failed");
                    SyncData syncData = new SyncData();
                    syncData.setActivity("INSERT");
                    syncData.setPrimaryKey(picture.getPictureId() + "");
                    syncData.setActivityDate(new Date());
                    syncData.setCustId(TPApplication.getInstance().custId);
                    syncData.setTableName(TPConstant.TABLE_TICKET_PICTURES);
                    syncData.setStatus("Pending");
                    SyncData.insertSyncData(syncData).subscribe();
                    //DatabaseHelper.getInstance().insertOrReplace(syncData.getContentValues(), "sync_data");
                }
            });
            //End
            //return service.deleteTicketPicture(citationNumber, imageName);
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }


        return false;
    }

    @Override
    public void uploadImages(long citationNumber, int s_no, ArrayList<String> images) throws TPException {

        service.syncUploadImagesEdit(citationNumber, s_no, images);
    }

    @Override
    public boolean uploadSelfi(ArrayList<String> images, Context ctx) throws TPException {
        return service.syncSelfiImages(images, ctx);
    }

    @Override
    public boolean updateDutyReport(DutyReport report) {
        if (report == null)
            return false;

        JSONArray dutyReports = new JSONArray();
        dutyReports.put(report.getJSONObject());

        return service.syncDutyReports(dutyReports);
    }

    @Override
    public void updateDutyReport1(DutyReport report, int custId) {
        try {
            ApiRequest apiRequest = ServiceGenerator.createRxService(ApiRequest.class);
            ActivityRequest_Rpc requestPOJO = new ActivityRequest_Rpc();
            requestPOJO.setMethod("updateDutyReports");
            ActivityRequest params = new ActivityRequest();
            JSONArray dutyReports = new JSONArray();
            dutyReports.put(report.getJSONObject());
            // params.setDutyReport(report);
            requestPOJO.setDutyReportParams(report);
            System.out.println("RequestObj**" + new Gson().toJson(requestPOJO));
            apiRequest.updateDutyReports(requestPOJO).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    if (response.isSuccessful()&&response.code()==200) {
                        // progressDialog.dismiss();
                        // Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "onResponse: Duty Report Updated");
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                    Log.e(TAG, "onFailure: Duty Report update failed");
                    try {
                        SyncData syncData = new SyncData();
                        syncData.setActivity("INSERT");
                        syncData.setPrimaryKey(DutyReport.getLastInsertId() + "");
                        syncData.setActivityDate(new Date());
                        syncData.setCustId(custId);
                        syncData.setTableName(TPConstant.TABLE_DUTY_REPORTS);
                        syncData.setStatus("Pending");
                        SyncData.insertSyncData(syncData).subscribe();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            // return service.deleteTicketComments(citationNumber, violationId, comment);
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }


    @Override
    public boolean sendEmail(String from, String[] to, String subject, String message) throws TPException {


        try {
            return service.sendEmail(from, to, subject, message);
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            TPException appEx = new TPException();
            appEx.setErrorMessage("Internal system error. Please try again.");
            throw appEx;
        }
    }

    @Override
    public CustomerInfo getCustomerInfo(int custId) throws TPException {
        try {
            JSONObject clientInfo = service.getClientInfo(custId);
            CustomerInfo customerInfo = new CustomerInfo(clientInfo);

            return customerInfo;
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }

        return null;
    }

    @Override
    public JSONObject getValidTicket(int custId, long citationNumber) throws TPException {
        try {
            return service.getValidTicket(custId, citationNumber);
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }

        return null;
    }

    @Override
    public ArrayList<Vendor> getVendors(int custId) throws TPException {
        ArrayList<Vendor> list = new ArrayList<Vendor>();
        try {
            JSONArray array = service.getVendors(custId, true);
            for (int i = 0; i < array.length(); i++) {
                list.add(new Vendor(array.getJSONObject(i)));
            }
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            TPException appEx = new TPException();
            appEx.setErrorMessage("Internal system error. Please try again.");
            throw appEx;
        }

        return list;
    }

    @Override
    public ArrayList<VendorService> getVendorServices(int custId) throws TPException {
        ArrayList<VendorService> list = new ArrayList<VendorService>();
        try {
            JSONArray array = service.getVendorServices(custId, true);
            for (int i = 0; i < array.length(); i++) {
                list.add(new VendorService(array.getJSONObject(i)));
            }
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            TPException appEx = new TPException();
            appEx.setErrorMessage("Internal system error. Please try again.");
            throw appEx;
        }

        return list;
    }

    @Override
    public ArrayList<VendorServiceRegistration> getVendorServiceRegistrations(int custId, int userId, int deviceId)
            throws TPException {
        ArrayList<VendorServiceRegistration> list = new ArrayList<VendorServiceRegistration>();
        try {
            JSONArray array = service.getVendorServiceRegistrations(custId, userId, deviceId, true);
            for (int i = 0; i < array.length(); i++) {
                list.add(new VendorServiceRegistration(array.getJSONObject(i)));
            }
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            TPException appEx = new TPException();
            appEx.setErrorMessage("Internal system error. Please try again.");
            throw appEx;
        }

        return list;
    }

    @Override
    public void updateUserServices(boolean fullSync) {
        try {
            JSONArray services = service.getVendorServiceRegistrations(0, TPApplication.getInstance().userId, 0,
                    fullSync);

            for (int i = 0; i < services.length(); i++) {
                JSONObject service = (JSONObject) services.get(i);

                VendorServiceRegistration vendorService = new VendorServiceRegistration(service);
                VendorServiceRegistration.insertVendorServiceRegistration(vendorService);
               /* DatabaseHelper.getInstance().insertOrReplace(vendorService.getContentValues(),
                        "vendor_service_registrations");*/
            }


            log.info("Updated " + services.length() + " Vendor Service Registration Records");

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }

    @Override
    public boolean sendMobileNowLog(ArrayList<MobileNowLog> logs) throws TPException {
        try {
            return service.sendMobileNowLog(logs);
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            TPException appEx = new TPException();
            appEx.setErrorMessage("Internal system error. Please try again.");
            throw appEx;
        }
    }

    @Override
    public ArrayList<VendorItem> getVendorItems(int custId) throws TPException {
        ArrayList<VendorItem> list = new ArrayList<VendorItem>();
        try {
            JSONArray array = service.getVendorItems(custId, true);
            for (int i = 0; i < array.length(); i++) {
                list.add(new VendorItem(array.getJSONObject(i)));
            }
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            TPException appEx = new TPException();
            appEx.setErrorMessage("Internal system error. Please try again.");
            throw appEx;
        }

        return list;
    }

    @Override
    public ArrayList<Permit> searchPermitsByPlate(int custId, String plate, String state) throws TPException {
        ArrayList<Permit> list = new ArrayList<Permit>();
        try {
            JSONArray array = service.searchPermitsByPlate(custId, plate, state);
            for (int i = 0; i < array.length(); i++) {
                list.add(new Permit(array.getJSONObject(i)));
            }
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            TPException appEx = new TPException();
            appEx.setErrorMessage("Internal system error. Please try again.");
            throw appEx;
        }

        return list;
    }

    @Override
    public ArrayList<MaintenanceLog> getMaintenanceLogs() throws TPException {
        try {
            if (TPApplication.getInstance().dbConfigured) {
                return MaintenanceLog.getLogs();
            }
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            TPException appEx = new TPException();
            appEx.setErrorMessage("Internal system error. Please try again.");
            throw appEx;
        }

        return new ArrayList<MaintenanceLog>();
    }

    @Override
    public boolean newHotlist(Hotlist hotList, HotListHandler context) throws TPException {
        try {
            // This code is changed by mohit 28/02/2023
            ApiRequest apiRequest = ServiceGenerator.createRxService(ApiRequest.class);
            HotList_Rpc hotList_rpc = new HotList_Rpc();

            HotListParams hotListParams = new HotListParams();
            HotListRequest params = new HotListRequest();
            hotList_rpc.setMethod("newHotlist");
            hotListParams.setPlate(hotList.getPlate());
            hotListParams.setCustId(hotList.getCustId());
            hotListParams.setStateCode(hotList.getStateCode());
            hotListParams.setPlateType(hotList.getPlateType());
            hotListParams.setBeginDate(String.valueOf(hotList.getBeginDate()));
            if (hotList.getEndDate() != null) {
                hotListParams.setEndDate(String.valueOf(hotList.getEndDate()));
            }
            hotListParams.setComments(hotList.getComments());
            params.setHotList(hotListParams);
            hotList_rpc.setParams(params);

            System.out.println("RequestObj**" + new Gson().toJson(hotList_rpc));
            apiRequest.newHotlist(hotList_rpc).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    if (response.isSuccessful()&&response.code()==200) {
                        HotListHandler hotListHandler = null;
                        hotListHandler = context;
                        hotListHandler.hotListHandler(true);
                        // progressDialog.dismiss();
                        // Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        // Toast.makeText(, "HotList added successfully.", Toast.LENGTH_LONG).show();
                        Log.i(TAG, "onResponse: hotList inserted");

                    }else {

                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                    Log.e(TAG, "onFailure: hotList inserted failed");
                }
            });
            //ENd
            // return service.newHotList(hotList);
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            TPException appEx = new TPException();
            appEx.setErrorMessage("Internal system error. Please try again.");
            throw appEx;
        }
        return false;
    }

    @Override
    public VersionUpdate getVersionUpdates(int custId, String module) throws TPException {
        JSONObject result;
        VersionUpdate versionUpdate;
        try {
            result = service.getVersionUpdates(custId, module);
            if (result == null || result.has("serviceError")) {
                return null;
            }

            versionUpdate = new VersionUpdate(result);

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            TPException appEx = new TPException();
            appEx.setErrorMessage("Internal system error. Please try again.");
            throw appEx;
        }

        return versionUpdate;
    }

    @Override
    public RepeatOffender repeatOffenderFromService(int custId, String plate, int violId, String stateCode)
            throws TPException {
        try {
            RepeatOffender repeatOffender = null;
            JSONArray objects = service.getRepeatOffendersLive(stateCode, plate, violId);
            for (int i = 0; i < objects.length(); i++) {
                // if(stateCode.equalsIgnoreCase(((JSONObject)
                // objects.get(i)).getString("state_code")) &&
                // plate.equalsIgnoreCase(((JSONObject) objects.get(i)).getString("plate")) &&
                // violId==((JSONObject) objects.get(i)).getInt("custid")){
                repeatOffender = new RepeatOffender((JSONObject) objects.get(i), plate, custId, stateCode);
            } // }
            return repeatOffender;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;


    }

    public static void repeatOffenderFromService1(Violation violation, int custId, String plate, int violId, String stateCode, RepeatOffenderHandler repeatOffenderHandler)
            throws TPException {
        try {
            ApiRequest apiRequest = ServiceGenerator.createRxService(ApiRequest.class);
            RepeatOffendersFromService repeatOffendersFromService = new RepeatOffendersFromService();
            repeatOffendersFromService.setStateCode(stateCode);
            repeatOffendersFromService.setCustId(custId);
            repeatOffendersFromService.setPlate(plate);
            repeatOffendersFromService.setViolationId(violId);
            //  RepeatOffender_Rpc requestPOJO = new RepeatOffender_Rpc();
            RepeatOffendersLive_Rpc requestPOJO = new RepeatOffendersLive_Rpc();
            requestPOJO.setMethod("getRepeatOffendersLive");
            requestPOJO.setRepeatOffenderLive(repeatOffendersFromService);
            System.out.println("RequestObj**" + new Gson().toJson(requestPOJO));

            apiRequest.getRepeatOffendersLive(requestPOJO).enqueue(new Callback<RepeatOffenderResponse>() {
                @Override
                public void onResponse(Call<RepeatOffenderResponse> call, Response<RepeatOffenderResponse> response) {
                    try {
                        if (response.isSuccessful() && response.body() != null&&response.code()==200) {
                            // progressDialog.dismiss();
                            RepeatOffender repeatOffender = new RepeatOffender();
                            List<RepeatOffender> result = response.body().getResult();
                            for (int i = 0; i < result.size(); i++) {
                                repeatOffender = result.get(i);
                            }
                            repeatOffenderHandler.successRepeatOffender(violation, repeatOffender);
                        }else {
                            repeatOffenderHandler.failRepeatOffender(response.message());

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.debug(e.getMessage());
                        repeatOffenderHandler.failRepeatOffender(e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<RepeatOffenderResponse> call, Throwable t) {
                    //progressDialog.dismiss();
                    //log.error(TPUtility.getPrintStackTrace(t));
                    repeatOffenderHandler.failRepeatOffender(t.getMessage());
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public RepeatOffender repeatOffenderFromService(RepeatOffendersFromService repeatOffendersFromService)
            throws TPException {
        // TODO Auto-generated method stub
        try {
            RepeatOffender repeatOffender = null;
            JSONArray objects = service.getRepeatOffendersLive(repeatOffendersFromService);
            for (int i = 0; i < objects.length(); i++) {
                // if(stateCode.equalsIgnoreCase(((JSONObject)
                // objects.get(i)).getString("state_code")) &&
                // plate.equalsIgnoreCase(((JSONObject) objects.get(i)).getString("plate")) &&
                // violId==((JSONObject) objects.get(i)).getInt("custid")){
                repeatOffender = new RepeatOffender((JSONObject) objects.get(i));
            } // }
            return repeatOffender;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean lastUpDateRepeatOffender(final ArrayList<RepeatOffender> repeatOffenders) throws TPException {
        boolean successStatus = false;
        new Thread(new Runnable() {
            @Override
            public void run() {
                // update repeatOffender count
                try {
                    JSONArray jsonArray = service.lastUpDateRepeatOffenders(repeatOffenders);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        RepeatOffender repeatOffender = new RepeatOffender((JSONObject) jsonArray.get(i));

                        updateRepeatOffender(repeatOffender.getCustId(), repeatOffender.getStateCode(), repeatOffender.getPlate(), repeatOffender.getViolationId(), "S");
                    }


                } catch (JSONRPCException e) {
                    // Log.e("TicketPRO", "Sync Error " + e.getMessage());

                } catch (NumberFormatException e) {
                    // Log.e("TicketPRO", "NumberFormatException " + e.getMessage());

                } catch (Exception e) {
                    //Log.e("TicketPRO", "Error " + e.getMessage());
                }
            }
        }).start();

        return successStatus;
    }

    /**
     * Code By SANJIV
     *
     * @param custid
     * @param timestamp
     * @return
     * @throws TPException
     */
    @Override
    public ArrayList<RepeatOffender> getlastUpDateRepeatOffender(final int custid, final String timestamp) throws TPException {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONArray lastUpdateRepeatOffender = service.getLastUpdateRepeatOffender(custid, timestamp);
                    for (int i = 0; i < lastUpdateRepeatOffender.length(); i++) {
                        RepeatOffender repeatOffender = new RepeatOffender((JSONObject) lastUpdateRepeatOffender.get(i));

                        boolean b = RepeatOffender.checkIsDataAlreadyInDBorNot(
                                repeatOffender.getCustId(),
                                repeatOffender.getStateCode(),
                                repeatOffender.getPlate(),
                                repeatOffender.getViolationId()
                        );
                        if (b) {

                            updateInsert(repeatOffender.getCustId(),
                                    repeatOffender.getStateCode(),
                                    repeatOffender.getPlate(),
                                    repeatOffender.getViolationId());
                            Log.d("TicketPRO", "===================updated============ ");
                        } else {
                            insertUpdate(repeatOffender.getCustId(),
                                    repeatOffender.getPlate(),
                                    repeatOffender.getViolation(),
                                    repeatOffender.getCount(),
                                    repeatOffender.getViolationId(),
                                    repeatOffender.getStateCode(),
                                    repeatOffender.getCreatTime());

                            Log.d("TicketPRO", "===================inserted============ ");
                        }
                    }

                } catch (Exception e) {
                    // Log.e("TicketPRO", "Error " + e.getMessage());
                }
            }
        }).start();

        return null;
    }

    /**
     * Code by SANJIV
     *
     * @param custId
     * @param moduleId
     * @return
     * @throws Exception
     */
    @Override
    public EulaModel getEULAText(int custId, int moduleId) throws Exception {
        EulaModel eulaModel;
        JSONObject result;
        try {
            result = service.getEULA(custId, moduleId);
            if (result == null || result.has("serviceError")) {
                return null;
            }

            eulaModel = new EulaModel(result);

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            TPException appEx = new TPException();
            appEx.setErrorMessage("Internal system error. Please try again.");
            throw appEx;
        }
        return eulaModel;
    }

    /**
     * CODE BY SANJIV
     *
     * @param custId
     * @param recId
     * @param isAccpted
     * @return
     * @throws Exception
     */
    @Override
    public JSONObject updateEULAAcceptance(int userId, int recId, String isAccpted, int custId) throws Exception {
        try {
            return service.updateEULAAcceptance(userId, recId, isAccpted, custId);
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            TPException appEx = new TPException();
            appEx.setErrorMessage("Internal system error. Please try again.");
            throw appEx;
        }
    }


    @SuppressLint("HardwareIds")
    private String getDeviceId(Context context) {
        String deviceName = "";
        SharedPreferences mPreferences;
        mPreferences = context.getSharedPreferences(context.getPackageName(), MODE_PRIVATE);
        deviceName = mPreferences.getString("DeviceID", null);

        if (deviceName == null) {
            // Check for telephony features
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

            try {
                if (telephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE) {
                    deviceName = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
                } else {
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return "";
                    }
                    deviceName = telephonyManager.getDeviceId();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (TPUtility.isRunningOnEmulator(context.getApplicationContext()) || "000000000000000".equals(deviceName)) {
                    deviceName = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (deviceName == null || deviceName.equals("")) {
                try {
                    WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                    WifiInfo wInfo = wifiManager.getConnectionInfo();
                    deviceName = wInfo.getMacAddress();
                } catch (Exception e) {
                    deviceName = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
                }
            }

            if (deviceName == null || deviceName.equals("")) {
                deviceName = Long.toString(Math.abs(UUID.randomUUID().getLeastSignificantBits()));
            }

            // Append ModuleName
            deviceName = deviceName + "-" + TPConstant.MODULE_NAME;

            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putString("DeviceID", deviceName);
            editor.apply();
        }
        return deviceName;
    }

    public void updateRepeatOffenderByDate(int custId) {
        /* Sync Repeat Offenders */
        try {
            JSONArray objects = service.getRepeatOffenders(custId, true);
            for (int i = 0; i < objects.length(); i++) {
                //JSONObject object = (JSONObject) objects.get(i);
                RepeatOffender repeatOffender = new RepeatOffender((JSONObject) objects.get(i));
                RepeatOffender.insertRepeatOffender(repeatOffender).subscribe();
            }

            log.info("Updated " + objects.length() + " RepeatOffender Records");
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }

    @Override
    public ArrayList<SpecialActivityReport> getActivityReport(int custId, String date) throws TPException {
        try {
            ArrayList<SpecialActivityReport> list = new ArrayList<>();
            JSONArray array = service.getActivityList(custId, date);
            for (int i = 0; i < array.length(); i++) {
                list.add(new SpecialActivityReport(array.getJSONObject(i)));
            }
            return list;
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            TPException appEx = new TPException();
            appEx.setErrorMessage("Internal system error. Please try again.");
            throw appEx;
        }

    }

    /**
     * This code is changed by mohit for Special Activity Report 27/03/2023.
     *
     * @param custId
     * @param date
     * @return
     */

    @Override
    public void getActivityReport1(int custId, String date, SpecialActivityHandler context) throws TPException {
        try {
            ApiRequest apiRequest = ServiceGenerator.createRxService(ApiRequest.class);
            SpecialActivityReport_Rpc requestPOJO = new SpecialActivityReport_Rpc();
            requestPOJO.setMethod("getSpecialActivityReports");
            SPecialActivityReportParams params = new SPecialActivityReportParams();
            params.setCustId(custId);
            params.setCreatedDate(date);
            requestPOJO.setParams(params);
            System.out.println("RequestObj**" + new Gson().toJson(requestPOJO));
            apiRequest.getSpecialActivityReports(requestPOJO).enqueue(new Callback<SpecialActivityReportResponse>() {
                @Override
                public void onResponse(@NonNull Call<SpecialActivityReportResponse> call, @NonNull Response<SpecialActivityReportResponse> response) {
                    if (response.isSuccessful()&&response.code()==200) {

                        SpecialActivityReportResponse specialActivityReportsResponse = response.body();
                        ArrayList<SpecialActivityReport> specialActivityReports = (ArrayList<SpecialActivityReport>) specialActivityReportsResponse.getResult();

                        context.specialActivityHandler(specialActivityReports);
                        Log.i(TAG, "onResponse: getSpecialActivityReports Updated");
                    }
                }

                @Override
                public void onFailure(@NonNull Call<SpecialActivityReportResponse> call, @NonNull Throwable t) {
                    Log.e(TAG, "onFailure: getSpecialActivityReports failed");
                }
            });
            // return service.deleteTicketComments(citationNumber, violationId, comment);
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }

    }


    /**
     * This code is changed by mohit for updateActivity 6/03/2023.
     *
     * @param activityReports
     * @return
     */
    @Override
    public boolean updateActivity(JSONArray activityReports) throws Exception {

        // This method modify by mohit 6/03/2023
        try {
            ApiRequest apiRequest = ServiceGenerator.createRxService(ApiRequest.class);
            ActivityRequest_Rpc requestPOJO = new ActivityRequest_Rpc();
            requestPOJO.setMethod("updateSpecialActivityReports");
            ActivityRequest params = new ActivityRequest();
            params.setActivityRequest(activityReports);
            requestPOJO.setParams(params);
            //  System.out.println("RequestObj**"+new Gson().toJson(requestPOJO));
            apiRequest.updateSpecialActivityReports(requestPOJO).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    if (response.isSuccessful()&&response.code()==200) {
                        // progressDialog.dismiss();
                        // Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "onResponse: update Special Activity Updated");
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                    Log.e(TAG, "onFailure: update Special Activity failed");
                }
            });
            // return service.deleteTicketComments(citationNumber, violationId, comment);
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
        //End

      /*  boolean syncSpecialActivityResult = service.syncSpecialActivityReports(activityReports);
        if (syncSpecialActivityResult) {
            try {
                for (int i = 0; i < specialActivityReports.length(); i++) {
                    String primaryKey = ((JSONObject) specialActivityReports.get(i)).getString("report_id");
                    SyncData.removeSyncData(TPConstant.TABLE_SPECIAL_ACTIVITY_REPORTS, primaryKey);
                }
            } catch (Exception e) {
                log.error("SyncError " + e.getMessage());
            }
        }*/
        return false;
    }


    @Override
    public boolean updateActivityPicture(JSONArray activityPicture, ArrayList<String> image) throws Exception {
        try {
            ApiRequest apiRequest = ServiceGenerator.createRxService(ApiRequest.class);
            ActivityRequest_Rpc requestPOJO = new ActivityRequest_Rpc();
            requestPOJO.setMethod("updateSpecialActivityPictures");
            ActivityRequest params = new ActivityRequest();
            params.setActivityRequestList(activityPicture);
            requestPOJO.setParams(params);
            System.out.println("RequestObj**" + new Gson().toJson(requestPOJO));
            apiRequest.updateSpecialActivityPictures(requestPOJO).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    if (response.isSuccessful()&&response.code()==200) {
                        // progressDialog.dismiss();
                        // Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        service.syncUploadImages(image);
                        Log.i(TAG, "onResponse: updateSpecialActivityPictures");
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                    Log.e(TAG, "onFailure: updateSpecialActivityPictures failed");
                }
            });

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }

        /*boolean syncSpecialActivityResult = service.syncSpecialActivityPicture(activityPicture);
        service.syncUploadImages(image);
        if (syncSpecialActivityResult) {
            try {
                // for (int i = 0; i < specialActivityReports.length(); i++) {
                //    String primaryKey = ((JSONObject) specialActivityReports.get(i)).getString("report_id");
                //   SyncData.removeSyncData(TPConstant.TABLE_SPECIAL_ACTIVITY_REPORTS, primaryKey);
            } catch (Exception e) {
                log.error("SyncError " + e.getMessage());
            }
        }
        */

        return false;
    }

    /**
     * This code is changed by mohit for getPlacard 28/02/2023.
     *
     * @param agencyCode
     * @param user
     * @param plate
     * @param placard
     * @param source
     * @return
     */
    @Override
    public boolean getPlacard(String agencyCode, String user, String plate, String placard, String source) throws Exception {
        try {
            // This code is changed by mohit 28/02/2023
            ApiRequest apiRequest = ServiceGenerator.createRxService(ApiRequest.class);
            RequestPOJO requestPOJO = new RequestPOJO();
            requestPOJO.setMethod("GetLicenseDetail");
            Params params = new Params();
            params.setAgencyCode(agencyCode);
            params.setUser(user);
            params.setPlacard(placard);
            params.setDeviceId(TPApplication.getInstance().deviceId);
            params.setPlate(plate);
            params.setSource(source);
            requestPOJO.setParams(params);
            apiRequest.GetLicenseDetail(requestPOJO).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    if (response.isSuccessful()&&response.code()==200) {
                        // progressDialog.dismiss();
                        // Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "onResponse: placard inserted");
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                    Log.e(TAG, "onFailure: placard inserted failed");
                }
            });

            //ENd
            // return service.getPlacard(agencyCode, user, plate, placard, source);
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            TPException appEx = new TPException();
            appEx.setErrorMessage("Internal system error. Please try again.");
            throw appEx;
        }
        return false;
    }


  /*  public boolean syncUserSettings(UserSetting userSetting)
{
    try {
        // This code is changed by mohit 28/02/2023
        ApiRequest apiRequest = ServiceGenerator.createRxService(ApiRequest.class);
        RequestPOJO requestPOJO = new RequestPOJO();
        requestPOJO.setMethod("GetLicenseDetail");
        UserSettingParams params = new UserSettingParams();
        Params params1 = new Params();
        params.setUserId(userSetting.getUserId());
        params.setDeviceId(userSetting.getDeviceId());
        params.setAutoSyncInterval(userSetting.getAutoSyncInterval());
        params.setDeviceId(TPApplication.getInstance().deviceId);
        params.setDataRetentionPeriod(userSetting.getDataRetentionPeriod());
        params.setGps(userSetting.getGps());
        params.setDataBackup(userSetting.getDataBackup());
        params.setUserPrefs(userSetting.getUserPrefs());
        params1.setUserSetting(params);
        requestPOJO.setParams(params);
        apiRequest.GetLicenseDetail(requestPOJO).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    // progressDialog.dismiss();
                    // Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "onResponse: placard inserted");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: placard inserted failed" );
            }
        });

        //ENd
        // return service.getPlacard(agencyCode, user, plate, placard, source);
    } catch (Exception e) {
        log.error(TPUtility.getPrintStackTrace(e));
        TPException appEx = new TPException();
        appEx.setErrorMessage("Internal system error. Please try again.");
        throw appEx;
    }
    return  false;
}*/
}
