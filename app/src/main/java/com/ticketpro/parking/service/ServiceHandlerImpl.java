package com.ticketpro.parking.service;

import static com.ticketpro.util.TPConstant.TAG;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.ticketpro.api.ApiRequest;
import com.ticketpro.api.ServiceGenerator;
import com.ticketpro.model.ErrorLog;
import com.ticketpro.model.Feature;
import com.ticketpro.model.Hotlist;
import com.ticketpro.model.MobileNowLog;
import com.ticketpro.model.RepeatOffender;
import com.ticketpro.model.RepeatOffenderParams;
import com.ticketpro.model.RepeatOffender_Rpc;
import com.ticketpro.model.RepeatOffendersFromService;
import com.ticketpro.model.Ticket;
import com.ticketpro.model.TicketComment;
import com.ticketpro.model.TicketPicture;
import com.ticketpro.model.User;
import com.ticketpro.parking.activity.TPApplication;
import com.ticketpro.util.DateUtil;
import com.ticketpro.util.TPConstant;
import com.ticketpro.util.TPUtility;

import org.alexd.jsonrpc.JSONRPCClient;
import org.alexd.jsonrpc.JSONRPCException;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceHandlerImpl implements ServiceHandler {
    private JSONRPCClient serviceClient;
    private Logger log = Logger.getLogger("ServiceHandlerImpl");

    public ServiceHandlerImpl() {

    }

    public JSONRPCClient getServiceClient() throws Exception {

        if (serviceClient == null) {
            serviceClient = new JSONRPCSecureClient(TPConstant.SERVICE_URL);
            String featureValue = Feature.getFeatureValue(Feature.TIMEOUT);
            int i = 5000;
            if (featureValue != null && !TextUtils.isEmpty(featureValue)) {
                i = Integer.parseInt(featureValue) * 1000;
            }
            //serviceClient.setDebug(TPConstant.IS_DEBUG_MODE);
            serviceClient.setConnectionTimeout(i);
            serviceClient.setSoTimeout(i);

        }
        return serviceClient;
    }

    public void setServiceClient(JSONRPCClient serviceClient) throws Exception {
        this.serviceClient = serviceClient;
    }

    @Override
    public boolean syncDatabase() {
        return false;
    }

    @Override
    public boolean syncDevices(JSONArray devices) {
        if(devices == null ){
            return false;
        }

        JSONObject params = new JSONObject();
        try {
            Log.i(TAG, "updateDeviceInfo: " + devices);
            params.putOpt("devices", devices);
            JSONObject result = getServiceClient().callJSONObject("updateDevices", params);
            Log.i(TAG, "updateDeviceInfo: " + devices + result.toString());
            if (!result.isNull("serviceError")) {
                sendError(result.getString("serviceError"));
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }

        return false;
    }

    @Override
    public boolean syncUserSettings(JSONArray settings) {
        JSONObject params = new JSONObject();
        try {
            params.putOpt("userSettings", settings);
            JSONObject result = getServiceClient().callJSONObject("updateUserSettings", params);
            if (!result.isNull("serviceError")) {
                sendError(result.getString("serviceError"));
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }

        return false;
    }

    @Override
    public JSONArray syncTickets(JSONArray tickets) throws JSONRPCException, Exception {
        JSONObject params = new JSONObject();
        params.putOpt("tickets", tickets);
        JSONObject result = getServiceClient().callJSONObject("updateTickets", params);
        if (!result.isNull("serviceError")) {
            return null;
        }
        return result.getJSONArray("failures");
    }

    @Override
    public boolean syncUpdatesTickets(JSONArray tickets) {
        JSONObject params = new JSONObject();
        try {
            params.putOpt("tickets", tickets);
            JSONObject result = getServiceClient().callJSONObject("updateTicketChanges", params);
            if (!result.isNull("serviceError")) {
                sendError(result.getString("serviceError"));
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
        return false;
    }

    @Override
    public boolean syncTicketComments(int violationId, JSONArray comments) {
        JSONObject params = new JSONObject();
        try {
            params.putOpt("violationId", violationId);
            params.putOpt("comments", comments);
            JSONObject result = getServiceClient().callJSONObject("updateTicketComments", params);
            if (!result.isNull("serviceError")) {
                sendError(result.getString("serviceError"));
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }

        return false;
    }

    @Override
    public boolean syncTicketViolations(JSONArray violations) {
        JSONObject params = new JSONObject();
        try {
            params.putOpt("violations", violations);
            JSONObject result = getServiceClient().callJSONObject("updateTicketViolations", params);
            if (!result.isNull("serviceError")) {
                sendError(result.getString("serviceError"));
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
        return false;
    }

    @Override
    public boolean syncTicketPictures(JSONArray pictures) {
        JSONObject params = new JSONObject();
        try {
            params.putOpt("pictures", pictures);
            JSONObject result = getServiceClient().callJSONObject("updateTicketPictures", params);
            if (!result.isNull("serviceError")) {
                sendError(result.getString("serviceError"));
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
        return false;
    }

    @Override
    public boolean syncChalks(JSONArray chalks) {
        JSONObject params = new JSONObject();
        if(chalks == null || chalks.length() == 0){
            return false;
        }else{
            try {
                params.putOpt("chalks", chalks);
                JSONObject result = getServiceClient().callJSONObject("updateChalks", params);
                if (!result.isNull("serviceError")) {
                    sendError(result.getString("serviceError"));
                    return false;
                } else {
                    return true;
                }
            } catch (Exception e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }
        }


        return false;
    }

    @Override
    public boolean syncChalkPictures(JSONArray chalkPictures) {
        JSONObject params = new JSONObject();
        try {
            params.putOpt("chalkPictures", chalkPictures);
            JSONObject result = getServiceClient().callJSONObject("updateChalkPictures", params);
            if (!result.isNull("serviceError")) {
                sendError(result.getString("serviceError"));
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }

        return false;
    }

    @Override
    public boolean syncChalkComments(JSONArray chalkComments) {
        JSONObject params = new JSONObject();
        try {
            params.putOpt("chalkComments", chalkComments);
            JSONObject result = getServiceClient().callJSONObject("updateChalkComments", params);
            if (!result.isNull("serviceError")) {
                sendError(result.getString("serviceError"));
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
        return false;
    }

    @Override
    public boolean syncDutyReports(JSONArray dutyReports) {
        JSONObject params = new JSONObject();
        if(dutyReports == null || dutyReports.length() == 0){
            return false;
        }else{
            try {
                params.putOpt("dutyReports", dutyReports);
                JSONObject result = getServiceClient().callJSONObject("updateDutyReports", params);
                if (!result.isNull("serviceError")) {
                    sendError(result.getString("serviceError"));
                    return false;
                } else {
                    return true;
                }
            } catch (Exception e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }
        }


        return false;
    }

    @Override
    public boolean syncCallInReports(JSONArray callInReports) {
        JSONObject params = new JSONObject();

        if(callInReports == null || callInReports.length() == 0){
            return false;
        }else{

            try {
                params.putOpt("callReports", callInReports);

                JSONObject result = getServiceClient().callJSONObject("updateCallInReports", params);
                if (!result.isNull("serviceError")) {
                    sendError(result.getString("serviceError"));
                    return false;
                } else {
                    return true;
                }

            } catch (Exception e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }
        }

        return false;
    }

    @Override
    public boolean syncHotListReports(JSONArray hotListReports) {
        JSONObject params = new JSONObject();
        try {
            params.putOpt("hotlist", hotListReports);

            JSONObject result = getServiceClient().callJSONObject("updateHotListReports", params);
            if (!result.isNull("serviceError")) {
                sendError(result.getString("serviceError"));
                return false;
            } else {
                return true;
            }

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }

        return false;
    }

    @Override
    public JSONObject getDeviceInfo(String deviceName) throws Exception {
        log.info("Requesting DEVICE ID ->" + deviceName);
        log.info(TPConstant.MODULE_NAME);

        JSONObject params = new JSONObject();
        params.putOpt("deviceName", deviceName);
        params.putOpt("moduleName", TPConstant.MODULE_NAME);
        params.putOpt("updateDeviceId", true);

        JSONObject object = getServiceClient().callJSONObject("getDeviceInfo", params);
        if (!object.isNull("notRegistered") || !object.isNull("notActive")) {
            log.info("Device Registration Error: " + object.toString());
        }

        log.info(object);
        return object;
    }

    @Override
    public JSONObject getClientInfo(int custId) throws Exception {
        JSONObject params = new JSONObject();
        params.putOpt("custId", custId);

        JSONObject object = getServiceClient().callJSONObject("getClientInfo", params);
        if (!object.isNull("serviceError")) {
            return null;
        }

        return object;
    }

    @Override
    public JSONArray getUsers(int custId) throws Exception {
        JSONObject params = new JSONObject();
        params.putOpt("custId", custId);
        return getServiceClient().callJSONArray("getUsers", params);
    }

    @Override
    public JSONArray getGenetecPatrollers(int custId) throws Exception {
        JSONObject params = new JSONObject();
        params.putOpt("custId", custId);
        return getServiceClient().callJSONArray("getPatrollersData", params);
    }

    @Override
    public JSONArray grtSpecialActivitieslocation(int custId, boolean fullSync) throws Exception {
        JSONObject params = new JSONObject();
        params.putOpt("custId", custId);
        params.putOpt("fullSync", fullSync);
        return getServiceClient().callJSONArray("getSpecialActivitiesLocation", params);

    }

    @Override
    public JSONArray getCustomers(int custId) throws Exception {
        JSONObject params = new JSONObject();
        params.putOpt("custId", custId);
        return getServiceClient().callJSONArray("getCustomers", params);
    }

    @Override
    public JSONArray getMakesAndModels(int custId, boolean fullSync) throws Exception {
        JSONObject params = new JSONObject();
        JSONArray objects = null;
        params.putOpt("custId", custId);
        params.putOpt("fullSync", fullSync);
        objects = getServiceClient().callJSONArray("getModelsAndMakes", params);
        return objects;
    }

    @Override
    public JSONArray getDeviceGroup(int custId, boolean fullSync) throws Exception {
        JSONObject params = new JSONObject();
        JSONArray objects = null;
        params.putOpt("custId", custId);
        params.putOpt("fullSync", fullSync);
        objects = getServiceClient().callJSONArray("getDeviceGroup", params);
        return objects;
    }

    @Override
    public JSONArray getBodies(int custId, boolean fullSync) throws Exception {
        JSONObject params = new JSONObject();
        JSONArray objects = null;
        params.putOpt("custId", custId);
        params.putOpt("fullSync", fullSync);
        objects = getServiceClient().callJSONArray("getBodies", params);

        return objects;
    }

    @Override
    public JSONArray getContacts(int custId, boolean fullSync) throws Exception {
        JSONObject params = new JSONObject();
        JSONArray objects = null;
        params.putOpt("custId", custId);
        params.putOpt("fullSync", fullSync);
        objects = getServiceClient().callJSONArray("getContacts", params);

        return objects;
    }

    @Override
    public JSONArray getColors(int custId, boolean fullSync) throws Exception {
        JSONObject params = new JSONObject();
        JSONArray objects = null;
        params.putOpt("custId", custId);
        params.putOpt("fullSync", fullSync);
        objects = getServiceClient().callJSONArray("getColors", params);

        return objects;
    }

    @Override
    public JSONArray getFeatures(int custId, boolean fullSync) throws Exception {
        JSONObject params = new JSONObject();
        JSONArray objects;
        params.putOpt("custId", custId);
        params.putOpt("fullSync", fullSync);
        objects = getServiceClient().callJSONArray("getFeatures", params);

        return objects;
    }

    @Override
    public JSONArray getLocations(int custId, boolean fullSync) throws Exception {
        JSONObject params = new JSONObject();
        JSONArray objects = null;
        params.putOpt("custId", custId);
        params.putOpt("fullSync", fullSync);
        objects = getServiceClient().callJSONArray("getLocations", params);

        return objects;
    }

    @Override
    public JSONArray getDuties(int custId, boolean fullSync) throws Exception {
        JSONObject params = new JSONObject();
        JSONArray objects = null;
        params.putOpt("custId", custId);
        params.putOpt("fullSync", fullSync);
        objects = getServiceClient().callJSONArray("getDuties", params);
        return objects;
    }

    @Override
    public JSONArray getMessages(int custId, boolean fullSync) throws Exception {
        JSONObject params = new JSONObject();
        JSONArray objects = null;
        params.putOpt("custId", custId);
        params.putOpt("fullSync", fullSync);
        objects = getServiceClient().callJSONArray("getMessages", params);

        return objects;
    }

    @Override
    public JSONArray getHotlist(int custId, boolean fullSync) throws Exception {
        JSONObject params = new JSONObject();
        JSONArray objects = null;
        params.putOpt("custId", custId);
        params.putOpt("fullSync", fullSync);
        objects = getServiceClient().callJSONArray("getHotlist", params);

        return objects;
    }

    @Override
    public JSONArray getMeters(int custId, boolean fullSync) throws Exception {
        JSONObject params = new JSONObject();
        JSONArray objects = null;
        params.putOpt("custId", custId);
        params.putOpt("fullSync", fullSync);
        objects = getServiceClient().callJSONArray("getMeters", params);

        return objects;
    }


    @Override
    public JSONArray getPermits(int custId, boolean fullSync) throws Exception {
        JSONObject params = new JSONObject();
        JSONArray objects = null;
        params.putOpt("custId", custId);
        params.putOpt("fullSync", fullSync);
        objects = getServiceClient().callJSONArray("getPermits", params);

        return objects;
    }

    @Override
    public JSONArray getStates(int custId, boolean fullSync) throws Exception {
        JSONObject params = new JSONObject();
        JSONArray objects = null;
        params.putOpt("custId", custId);
        params.putOpt("fullSync", fullSync);
        objects = getServiceClient().callJSONArray("getStates", params);

        return objects;
    }

    @Override
    public JSONArray getViolations(int custId, boolean fullSync) throws Exception {
        JSONObject params = new JSONObject();
        JSONArray objects = null;
        params.putOpt("custId", custId);
        params.putOpt("fullSync", fullSync);
        objects = getServiceClient().callJSONArray("getViolations", params);

        return objects;
    }

    @Override
    public JSONArray getComments(int custId, boolean fullSync) throws Exception {
        JSONObject params = new JSONObject();
        JSONArray objects = null;
        params.putOpt("custId", custId);
        params.putOpt("fullSync", fullSync);
        objects = getServiceClient().callJSONArray("getComments", params);

        return objects;
    }

    @Override
    public JSONArray getDirections(int custId, boolean fullSync) throws Exception {
        JSONObject params = new JSONObject();
        JSONArray objects = null;
        params.putOpt("custId", custId);
        params.putOpt("fullSync", fullSync);
        objects = getServiceClient().callJSONArray("getDirections", params);

        return objects;
    }

    @Override
    public JSONArray getStreetPrefixes(int custId, boolean fullSync) throws Exception {
        JSONObject params = new JSONObject();
        JSONArray objects = null;
        params.putOpt("custId", custId);
        params.putOpt("fullSync", fullSync);
        objects = getServiceClient().callJSONArray("getStreetPrefixes", params);
        return objects;
    }

    @Override
    public JSONArray getStreetSuffixes(int custId, boolean fullSync) throws Exception {
        JSONObject params = new JSONObject();
        JSONArray objects = null;
        params.putOpt("custId", custId);
        params.putOpt("fullSync", fullSync);
        objects = getServiceClient().callJSONArray("getStreetSuffixes", params);
        Log.i("Suffix_Objects", objects.toString());
        return objects;
    }

    @Override
    public JSONArray getDurations(int custId, boolean fullSync) throws Exception {
        JSONObject params = new JSONObject();
        JSONArray objects = null;
        params.putOpt("custId", custId);
        params.putOpt("fullSync", fullSync);
        objects = getServiceClient().callJSONArray("getDurations", params);

        return objects;
    }

    @Override
    public JSONArray getUserSettings(int custId) throws Exception {

        JSONObject params = new JSONObject();
        JSONArray objects = null;
        params.putOpt("custId", custId);
        objects = getServiceClient().callJSONArray("getUserSettings", params);

        // TODO - Pending user settings issue

        return objects;
    }

    @Override
    public JSONArray getZones(int custId, boolean fullSync) throws Exception {
        JSONObject params = new JSONObject();
        JSONArray objects = null;
        params.putOpt("custId", custId);
        params.putOpt("fullSync", fullSync);
        objects = getServiceClient().callJSONArray("getZones", params);

        return objects;
    }

    @Override
    public JSONArray getTicketComments(long citationNumber) throws Exception {
        JSONObject params = new JSONObject();
        JSONArray objects = null;
        params.putOpt("citationNumber", citationNumber);
        objects = getServiceClient().callJSONArray("getTicketComments", params);

        return objects;
    }

    @Override
    public JSONArray getTicketPictures(long citationNumber) throws Exception {
        JSONObject params = new JSONObject();
        JSONArray objects = null;
        params.putOpt("citationNumber", citationNumber);
        objects = getServiceClient().callJSONArray("getTicketPictures", params);

        return objects;
    }

    @Override
    public JSONArray getTicketViolations(long citationNumber) throws Exception {
        JSONObject params = new JSONObject();
        JSONArray objects = null;
        params.putOpt("citationNumber", citationNumber);
        params.putOpt("custId", TPApplication.getInstance().custId);
        objects = getServiceClient().callJSONArray("getTicketViolations", params);

        return objects;
    }

    @Override
    public JSONArray getChalkVehicles(int userId, int deviceId) throws Exception {
        JSONObject params = new JSONObject();
        JSONArray objects = null;
        params.putOpt("userId", userId);
        params.putOpt("deviceId", deviceId);
        objects = getServiceClient().callJSONArray("getChalkVehicles", params);

        return objects;
    }

    @Override
    public JSONArray getChalkVehicle(int deviceId) throws Exception {
        JSONObject params = new JSONObject();
        JSONArray objects = null;
        params.putOpt("deviceId", deviceId);
        objects = getServiceClient().callJSONArray("getChalkVehicles", params);

        return objects;
    }

    @Override
    public JSONArray getChalkPictures(long chalkId) throws Exception {
        JSONObject params = new JSONObject();
        JSONArray objects = null;
        params.putOpt("chalkId", chalkId);
        objects = getServiceClient().callJSONArray("getChalkPictures", params);

        return objects;
    }

    @Override
    public JSONArray getChalkComments(long chalkId) throws Exception {
        JSONObject params = new JSONObject();
        JSONArray objects = null;
        params.putOpt("chalkId", chalkId);
        objects = getServiceClient().callJSONArray("getChalkComments", params);

        return objects;
    }

    @Override
    public JSONArray getTickets(int userId, int deviceId) throws Exception {
        JSONObject params = new JSONObject();
        JSONArray objects = null;
        params.putOpt("userId", userId);
        params.putOpt("deviceId", deviceId);
        objects = getServiceClient().callJSONArray("getTickets", params);

        return objects;
    }

    @Override
    public JSONArray getGPSLocations(int custId, boolean fullSync) throws Exception {
        JSONObject params = new JSONObject();
        JSONArray objects = null;
        params.putOpt("custId", custId);
        params.putOpt("fullSync", fullSync);
        objects = getServiceClient().callJSONArray("getGPSLocations", params);

        return objects;
    }

    @Override
    public User doLogin(String username, String password) throws Exception {
        JSONObject params = new JSONObject();
        User result = null;
        params.putOpt("username", username);
        params.putOpt("password", password);
        JSONObject object = getServiceClient().callJSONObject("doLogin", params);
        if (object.isNull("serviceError")) {
            result = new User(object);
        }

        return result;
    }

    @Override
    public JSONArray getVoidTicketReasons(int custId, boolean fullSync) throws Exception {
        JSONObject params = new JSONObject();
        JSONArray objects = null;
        params.putOpt("custId", custId);
        params.putOpt("fullSync", fullSync);
        objects = getServiceClient().callJSONArray("getVoidTicketReasons", params);

        return objects;
    }

    @Override
    public JSONArray getSpecialActivities(int custId, boolean fullSync) throws Exception {
        JSONObject params = new JSONObject();
        JSONArray objects = null;
        params.putOpt("custId", custId);
        params.putOpt("fullSync", fullSync);
        objects = getServiceClient().callJSONArray("getSpecialActivities", params);

        return objects;
    }

    @Override
    public JSONArray getSpecialActivityDispositions(int custId, boolean fullSync) throws Exception {
        JSONObject params = new JSONObject();
        JSONArray objects = null;
        params.putOpt("custId", custId);
        params.putOpt("fullSync", fullSync);
        objects = getServiceClient().callJSONArray("getSpecialActivityDispositions", params);

        return objects;
    }

    @Override
    public JSONArray getPrintMacros(int custId, boolean fullSync) throws Exception {
        JSONObject params = new JSONObject();
        JSONArray objects = null;
        try {
            params.putOpt("custId", custId);
            params.putOpt("fullSync", fullSync);
            objects = getServiceClient().callJSONArray("getPrintMacros", params);
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }

        return objects;
    }

    @Override
    public JSONArray getPrintTemplates(int custId, boolean fullSync) throws Exception {
        JSONObject params = new JSONObject();
        JSONArray objects = null;
        try {
            params.putOpt("custId", custId);
            params.putOpt("fullSync", fullSync);
            objects = getServiceClient().callJSONArray("getPrintTemplates", params);
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }

        return objects;
    }

    @Override
    public JSONArray getCallInList(int custId, boolean fullSync) throws Exception {
        JSONObject params = new JSONObject();
        JSONArray objects = null;
        try {
            params.putOpt("custId", custId);
            params.putOpt("fullSync", fullSync);
            objects = getServiceClient().callJSONArray("getCallInList", params);
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }

        return objects;
    }

    @Override
    public boolean syncSpecialActivityReports(JSONArray specialActivityReports) {
        JSONObject params = new JSONObject();

        if(specialActivityReports == null || specialActivityReports.length() == 0){
            return false;
        }else{
            try {
                params.putOpt("activityReports", specialActivityReports);
                JSONObject result = getServiceClient().callJSONObject("updateSpecialActivityReports", params);
                if (!result.isNull("serviceError")) {
                    sendError(result.getString("serviceError"));
                    return false;
                } else {
                    return true;
                }
            } catch (Exception e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }

        }


        return false;
    }

    @Override
    public boolean syncSpecialActivityPicture(JSONArray specialActivityReports) {
        JSONObject params = new JSONObject();
        try {
            params.putOpt("activity", specialActivityReports);
            JSONObject result = getServiceClient().callJSONObject("updateSpecialActivityPictures", params);
            if (!result.isNull("serviceError")) {
                sendError(result.getString("serviceError"));
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }

        return false;
    }

    @Override
    public boolean syncUploadImagesEdit(long citationNumber, int s_no, final ArrayList<String> images) {
        if (images == null || images.size() == 0) {
            return false;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean uploadFlag = true;
                for (String imagePath : images) {
                    try {
						/*if(imagePath.contains("selfi")){
							uploadFlag = TPUtility.uploadFile(imagePath, 
									TPConstant.SERVICE_URL + "/uploadfile", 
									TPApplication.getInstance().getCustId());
						}else{*/

                        //}

                        if (!imagePath.contains("VLPR")) {
                            uploadFlag = TPUtility.uploadFile(imagePath,
                                    TPConstant.FILE_UPLOAD + "/uploadfile",
                                    TPApplication.getInstance().getCustId());
                        }
                        if (!uploadFlag) {
                            TPUtility.markPendingImage(imagePath);
                            if (!Ticket.isTicketPending(citationNumber)){
                                Ticket.updateTicket(String.valueOf(citationNumber), "PI");
                            }

                        }else {
                            TicketPicture.updateTicketPictureSyncStatus(String.valueOf(citationNumber), "S",s_no);
                        }
                    } catch (Exception e) {
                        log.error(TPUtility.getPrintStackTrace(e));
                        TPUtility.markPendingImage(imagePath);
                    }
                }
            }
        }).start();

        return true;
    }


    @Override
    public boolean syncUploadImages(final ArrayList<String> images) {
        if (images == null || images.size() == 0) {
            return false;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean uploadFlag = true;
                for (String imagePath : images) {
                    try {
						/*if(imagePath.contains("selfi")){
							uploadFlag = TPUtility.uploadFile(imagePath,
									TPConstant.SERVICE_URL + "/uploadfile",
									TPApplication.getInstance().getCustId());
						}else{*/

                        //}

                        if (!imagePath.contains("VLPR")) {
                            uploadFlag = TPUtility.uploadFile(imagePath,
                                    TPConstant.FILE_UPLOAD + "/uploadfile",
                                    TPApplication.getInstance().getCustId());
                        }
                        if (!uploadFlag) {
                            TPUtility.markPendingImage(imagePath);

                        }else {

                        }
                    } catch (Exception e) {
                        log.error(TPUtility.getPrintStackTrace(e));
                        TPUtility.markPendingImage(imagePath);
                    }
                }
            }
        }).start();

        return true;
    }

    @Override
    public boolean syncSelfiImages(final ArrayList<String> images, final Context ctx) {
        if (images == null || images.size() == 0) {
            return false;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean uploadFlag = true;
                for (String imagePath : images) {
                    try {
                        uploadFlag = TPUtility.uploadFile(imagePath,
                                TPConstant.FILE_UPLOAD + "/uploadfile",
                                TPApplication.getInstance().getCustId());

                        if (!uploadFlag) {
                            TPUtility.markPendingImage(imagePath);
                        }
                    } catch (Exception e) {
                        log.error(TPUtility.getPrintStackTrace(e));
                        TPUtility.markPendingImage(imagePath);
                    }
                }
            }
        }).start();

        return true;
    }

    @Override
    public JSONArray searchMeters(int custId, String meter) throws Exception {
        JSONObject params = new JSONObject();
        JSONArray objects = null;
        try {
            params.putOpt("custId", custId);
            params.putOpt("meter", meter);
            objects = getServiceClient().callJSONArray("searchMeters", params);
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
        return objects;
    }

    @Override
    public JSONArray searchPermits(int custId, String permit) throws Exception {
        JSONObject params = new JSONObject();
        JSONArray objects = null;
        try {
            params.putOpt("custId", custId);
            params.putOpt("permit", permit);
            objects = getServiceClient().callJSONArray("searchPermits", params);
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }

        return objects;
    }

    @Override
    public JSONArray searchPlates(int custId, String plate, String state) throws Exception {
        JSONObject params = new JSONObject();
        JSONArray objects = null;
        try {
            params.putOpt("custId", custId);
            params.putOpt("plate", plate);
            params.putOpt("state", state);
            objects = getServiceClient().callJSONArray("searchPlates", params);

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }

        return objects;
    }

    @Override
    public JSONArray searchVins(int custId, String vin, String state) throws Exception {
        JSONObject params = new JSONObject();
        JSONArray objects = null;
        try {
            params.putOpt("custId", custId);
            params.putOpt("vin", vin);
            params.putOpt("state", state);
            objects = getServiceClient().callJSONArray("searchVins", params);
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }

        return objects;
    }

    @Override
    public JSONArray searchPlatesVin(int custId, String vin, String state) throws Exception {
        JSONObject params = new JSONObject();
        JSONArray objects = null;
        try {
            params.putOpt("custId", custId);
            params.putOpt("vin", vin);
            params.putOpt("state", state);
            objects = getServiceClient().callJSONArray("searchVins", params);
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }

        return objects;
    }

    @Override
    public JSONArray searchPermitVins(int custId, String vin, String state) throws Exception {
        JSONObject params = new JSONObject();
        JSONArray objects = null;
        try {
            params.putOpt("custId", custId);
            params.putOpt("vin", vin);
            params.putOpt("state", state);
            objects = getServiceClient().callJSONArray("searchPermitVins", params);
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }

        return objects;
    }

    @Override
    public JSONArray searchHotlist(int custId, String plate, String state) throws Exception {
        JSONObject params = new JSONObject();
        JSONArray objects = null;
        try {
            params.putOpt("custId", custId);
            params.putOpt("plate", plate);
            params.putOpt("state", state);
            objects = getServiceClient().callJSONArray("searchHotlist", params);
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }

        return objects;
    }

    @Override
    public JSONArray getTicketHistory(int custId, Date startDate, Date endDate) throws Exception {
        JSONObject params = new JSONObject();
        JSONArray objects = null;
        try {
            params.putOpt("custId", custId);
            params.putOpt("startDate", DateUtil.getSQLStringFromDate2(startDate));
            params.putOpt("endDate", DateUtil.getSQLStringFromDate2(endDate));
            objects = getServiceClient().callJSONArray("getTickeHistory", params);
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }

        return objects;
    }

    @Override
    public JSONArray getRepeatOffenders(int custId, boolean fullSync) throws Exception {
        JSONObject params = new JSONObject();
        JSONArray objects = null;
        params.putOpt("custId", custId);
        params.putOpt("fullSync", fullSync);
        objects = getServiceClient().callJSONArray("getRepeatOffenders", params);

        return objects;
    }

    @Override
    public JSONArray getRepeatOffendersLive(String stateCode, String plate, int violationId) throws Exception {

       /* ApiRequest apiRequest = ServiceGenerator.createRxService(ApiRequest.class);
        RepeatOffendersFromService repeatOffendersFromService = new RepeatOffendersFromService();
        final JSONArray[] object = new JSONArray[1];
        repeatOffendersFromService.setStateCode(stateCode);
        repeatOffendersFromService.setPlate(plate);
        repeatOffendersFromService.setViolationId(violationId);
        //  RepeatOffender_Rpc requestPOJO = new RepeatOffender_Rpc();
        RepeatOffendersLive_Rpc requestPOJO = new RepeatOffendersLive_Rpc();
        requestPOJO.setMethod("getRepeatOffendersLive");

        requestPOJO.setRepeatOffenderLive(repeatOffendersFromService);


        System.out.println("RequestObj**"+new Gson().toJson(requestPOJO));
        apiRequest.getRepeatOffendersLive(requestPOJO).enqueue(new Callback<RepeatOffenders>() {
            @Override
            public void onResponse(Call<RepeatOffenders> call, Response<RepeatOffenders> response) {
                if(response.isSuccessful()){
                    // progressDialog.dismiss();
                    JSONObject jsonObject = new JSONObject();
                    RepeatOffenders repeatOffenders = new RepeatOffenders();
                    List<RepeatOffendersResult> repeatOffendersResult = new ArrayList<RepeatOffendersResult>();
                    repeatOffenders = response.body();
                    repeatOffendersResult = repeatOffenders.getResult();
                    System.out.println("RepeatOffenderLiveFine:"+repeatOffenders.getResult());
                    //  jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    //object[0] = jsonObject.getJSONArray("result");


                }
            }

            @Override
            public void onFailure(Call<RepeatOffenders> call, Throwable t) {
                //progressDialog.dismiss();
                log.error(TPUtility.getPrintStackTrace(t));
            }
        });
        return object[0];*/

        JSONObject params = new JSONObject();
        JSONArray objects = null;
        params.putOpt("state_code", stateCode);
        params.put("plate", plate);
        params.put("violation_id", violationId);
        objects = getServiceClient().callJSONArray("getRepeatOffendersLive", params);

        return objects;
    }

    @Override
    public JSONArray getRepeatOffendersLive(RepeatOffendersFromService repeatOffendersFromService) throws Exception {
       /* ApiRequest apiRequest = ServiceGenerator.createRxService(ApiRequest.class);
        final JSONArray[] object = new JSONArray[1];
        //  RepeatOffender_Rpc requestPOJO = new RepeatOffender_Rpc();
        RepeatOffendersLive_Rpc requestPOJO = new RepeatOffendersLive_Rpc();
        requestPOJO.setMethod("getRepeatOffendersLive");

        requestPOJO.setRepeatOffenderLive(repeatOffendersFromService);


        System.out.println("RequestObj**"+new Gson().toJson(requestPOJO));
        apiRequest.getRepeatOffendersLive(requestPOJO).enqueue(new Callback<RepeatOffenders>() {
            @Override
            public void onResponse(Call<RepeatOffenders> call, Response<RepeatOffenders> response) {
                if(response.isSuccessful()){
                    // progressDialog.dismiss();
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject = new JSONObject(new Gson().toJson(response.body()));
                        object[0] = jsonObject.getJSONArray("result");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<RepeatOffenders> call, Throwable t) {
                 //progressDialog.dismiss();
                log.error(TPUtility.getPrintStackTrace(t));
            }
        });*/
        return null;
    }

    @Override
    public boolean updateRepeatOffendersCount(String stateCode, String plate, int violationId, int custId, String created_date) throws Exception {
        JSONObject params = new JSONObject();
        boolean result = false;

        try {
            params.putOpt("state_code", stateCode);
            params.put("plate", plate);
            params.put("violation_id", violationId);
            params.put("custid", custId);
            params.put("created_date", created_date);

            result = getServiceClient().callBoolean("updateRepeatOffendersCount", params);
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
        return result;
    }

    @Override
    public boolean updateRepeatOffendersCount(RepeatOffenderParams repeatOffenderParams) throws Exception {
        final boolean[] result = {false};

        ApiRequest apiRequest = ServiceGenerator.createRxService(ApiRequest.class);
        //  RepeatOffender_Rpc requestPOJO = new RepeatOffender_Rpc();
        RepeatOffender_Rpc requestPOJO = new RepeatOffender_Rpc();
        requestPOJO.setMethod("updateRepeatOffendersCount");
        // params.setRepeatOffendersobject(repeatOffender);
        requestPOJO.setRepeatOffenderParams(repeatOffenderParams);
        System.out.println("RequestObj**"+new Gson().toJson(requestPOJO));
        apiRequest.updateRepeatOffendersCount(requestPOJO).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    assert response.body() != null;
                    if(response.isSuccessful())
                    { System.out.println("RepeatOffender updateCount"+response.body());
                       result[0] =  true;
                       /* Intent serviceIntent = new Intent(WriteTicketActivity.this, JobIntentServiceSaveTicket.class);
                        JobIntentServiceSaveTicket.enqueueWork(WriteTicketActivity.this, serviceIntent);*/
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("","Function name is updateRepeatOffendersCount() "+t.getMessage());
                log.error(TPUtility.getPrintStackTrace(t));
                result[0] = false;
            }
        });
        return result[0];
    }


    @Override
    public JSONArray lastUpDateRepeatOffenders(ArrayList<RepeatOffender> repeatOffenders) throws Exception {
        JSONArray objects = null;
        try {
            JSONArray array = new JSONArray();
            for (RepeatOffender offender : repeatOffenders) {
                array.put(offender.getRepeatOffenderJson());
            }
            JSONObject params = new JSONObject();
            params.putOpt("LROArr", array);
            objects = getServiceClient().callJSONArray("lastUpDateRepeatOffenders", params);
        } catch (Exception e) {
            //log.error(TPUtility.getPrintStackTrace(e));
        }

        return objects;
    }

    @Override
    public JSONObject getGPSLocation(double lat, double lon) throws Exception {

        JSONObject params = new JSONObject();
        JSONObject objects = null;
        try {
            params.putOpt("lat", lat);
            params.putOpt("lon", lon);
            objects = getServiceClient().callJSONObject("getGPSLocation", params);
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
        return objects;
    }

    @Override
    public JSONObject registerDevice(JSONObject device) throws Exception {

        JSONObject params = new JSONObject();
        JSONObject objects = null;
        try {
            params.putOpt("deviceInfo", device);
            objects = getServiceClient().callJSONObject("registerDevice", params);
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }

        return objects;
    }

    @Override
    public JSONArray getLocationGroups(int custId, boolean fullSync) throws Exception {

        JSONObject params = new JSONObject();
        JSONArray objects = null;
        params.putOpt("custId", custId);
        params.putOpt("fullSync", fullSync);
        objects = getServiceClient().callJSONArray("getLocationGroups", params);

        return objects;

    }

    @Override
    public JSONArray getLocationGroupLocations(int custId, boolean fullSync) throws Exception {

        JSONObject params = new JSONObject();
        JSONArray objects = null;
        params.putOpt("custId", custId);
        params.putOpt("fullSync", fullSync);
        objects = getServiceClient().callJSONArray("getLocationGroupLocations", params);

        return objects;

    }

    @Override
    public JSONArray getCommentGroups(int custId, boolean fullSync) throws Exception {

        JSONObject params = new JSONObject();
        JSONArray objects = null;
        params.putOpt("custId", custId);
        params.putOpt("fullSync", fullSync);
        objects = getServiceClient().callJSONArray("getCommentGroups", params);

        return objects;

    }

    @Override
    public JSONArray getCommentGroupComments(int custId, boolean fullSync) throws Exception {

        JSONObject params = new JSONObject();
        JSONArray objects = null;
        params.putOpt("custId", custId);
        params.putOpt("fullSync", fullSync);
        objects = getServiceClient().callJSONArray("getCommentGroupComments", params);

        return objects;

    }

    @Override
    public JSONArray getViolationGroups(int custId, boolean fullSync) throws Exception {

        JSONObject params = new JSONObject();
        JSONArray objects = null;
        params.putOpt("custId", custId);
        params.putOpt("fullSync", fullSync);
        objects = getServiceClient().callJSONArray("getViolationGroups", params);

        return objects;

    }

    @Override
    public JSONArray getViolationGroupViolations(int custId, boolean fullSync) throws Exception {

        JSONObject params = new JSONObject();
        JSONArray objects = null;
        params.putOpt("custId", custId);
        params.putOpt("fullSync", fullSync);
        objects = getServiceClient().callJSONArray("getViolationGroupViolations", params);

        return objects;

    }

    @Override
    public JSONArray searchChalks(int custId, String plate, String state) throws Exception {

        JSONObject params = new JSONObject();
        JSONArray objects = null;
        try {
            params.putOpt("custId", custId);
            params.putOpt("plate", plate);
            params.putOpt("state", state);
            objects = getServiceClient().callJSONArray("searchChalks", params);
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
        return objects;
    }

    @Override
    public JSONArray searchRepeatOffenders(int custId, String plate, String violation) throws Exception {
        JSONObject params = new JSONObject();
        JSONArray objects = null;
        try {
            params.putOpt("custId", custId);
            params.putOpt("plate", plate);
            params.putOpt("violation", violation);
            objects = getServiceClient().callJSONArray("searchRepeatOffenders", params);
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
        return objects;
    }

    @Override
    public JSONArray searchPermitsByPlate(int custId, String plate, String state) throws Exception {
        JSONObject params = new JSONObject();
        JSONArray objects = null;
        try {
            params.putOpt("custId", custId);
            params.putOpt("plate", plate);
            params.putOpt("state", state);
            objects = getServiceClient().callJSONArray("searchPermitsByPlate", params);
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
        return objects;
    }

    @Override
    public boolean updateGCMRegistrationId(String deviceName, String registrationId) throws Exception {
        JSONObject params = new JSONObject();
        try {
            params.putOpt("deviceName", deviceName);
            params.putOpt("registrationId", registrationId);
            JSONObject result = getServiceClient().callJSONObject("updateGCMRegistrationId", params);
            if (!result.isNull("serviceError")) {
                sendError(result.getString("serviceError"));
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean sendErrorLog(ArrayList<ErrorLog> errors) throws Exception {
        try {
            JSONArray errorLogs = new JSONArray();
            for (ErrorLog error : errors) {
                errorLogs.put(error.getJSONObject());
            }

            JSONObject params = new JSONObject();
            params.putOpt("errorLog", errorLogs);
            JSONObject result = getServiceClient().callJSONObject("updateErrorLog", params);
            if (!result.isNull("serviceError")) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }

        return false;
    }

    @Override
    public boolean sendMobileNowLog(ArrayList<MobileNowLog> logs) throws Exception {
        try {
            JSONArray mobileNowLogs = new JSONArray();
            for (MobileNowLog log : logs) {
                mobileNowLogs.put(log.getJSONObject());
            }

            JSONObject params = new JSONObject();
            params.putOpt("mobileNowLogs", mobileNowLogs);
            JSONObject result = getServiceClient().callJSONObject("updateMobileNowLog", params);

            if (!result.isNull("serviceError")) {
                sendError(result.getString("serviceError"));
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }

        return false;
    }

    @Override
    public JSONArray getUserChalks(int custId, int userId, Date fromDate, Date toDate, int durationId)
            throws Exception {
        JSONObject params = new JSONObject();
        JSONArray objects = null;
        try {
            params.putOpt("custId", custId);
            params.putOpt("userId", userId);
            params.putOpt("startDate", DateUtil.getSQLStringFromDate2(fromDate));
            params.putOpt("endDate", DateUtil.getSQLStringFromDate2(toDate));
            params.putOpt("durationId", durationId);
            objects = getServiceClient().callJSONArray("getUserChalks", params);
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }

        return objects;
    }

    @Override
    public boolean updateChalkStatus(long chalkId, String status, String dispositionReason) throws Exception {

        JSONObject params = new JSONObject();
        try {
            params.putOpt("chalkId", chalkId);
            params.putOpt("status", status);
            params.putOpt("dispositionReason", dispositionReason);

            JSONObject result = getServiceClient().callJSONObject("updateChalkStatus", params);
            if (!result.isNull("serviceError")) {
                sendError(result.getString("serviceError"));
                return false;
            } else {
                return true;
            }

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }

        return false;
    }

    @Override
    public boolean updateTicketComments(int violationId, ArrayList<TicketComment> ticketComments) throws Exception {
        try {
            JSONArray ticketCommentsJSON = new JSONArray();
            for (TicketComment comment : ticketComments) {
                ticketCommentsJSON.put(comment.getJSONObject());
            }

            JSONObject params = new JSONObject();
            params.putOpt("violationId", violationId);
            params.putOpt("comments", ticketCommentsJSON);
            JSONObject result = getServiceClient().callJSONObject("updateTicketComments", params);
            if (!result.isNull("serviceError")) {
                sendError(result.getString("serviceError"));
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }

        return false;
    }

    @Override
    public boolean deleteTicketComments(long citationNumber, int violationId, String comment) throws Exception {

        try {
            JSONObject params = new JSONObject();
            params.putOpt("citationNumber", citationNumber);
            params.putOpt("violationId", violationId);
            params.putOpt("commentText", comment);

            JSONObject result = getServiceClient().callJSONObject("deleteTicketComments", params);
            if (!result.isNull("serviceError")) {
                sendError(result.getString("serviceError"));
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }

        return false;
    }

    @Override
    public boolean deleteTicketPicture(long citationNumber, String imageName) throws Exception {
        try {
            JSONObject params = new JSONObject();
            params.putOpt("citationNumber", citationNumber);
            params.putOpt("imageName", imageName);
            params.putOpt("custId", TPApplication.getInstance().custId);

            JSONObject result = getServiceClient().callJSONObject("deleteTicketPicture", params);
            if (!result.isNull("serviceError")) {
                sendError(result.getString("serviceError"));
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }

        return false;
    }

    @Override
    public boolean updateTicketPicture(long citationNumber, TicketPicture picture) throws Exception {
        try {
            JSONObject params = new JSONObject();
            params.putOpt("citationNumber", citationNumber);
            params.putOpt("ticketPicture", picture.getJSONObject());
            params.putOpt("custId", TPApplication.getInstance().custId);

            JSONObject result = getServiceClient().callJSONObject("updateTicketPicture", params);
            if (!result.isNull("serviceError")) {
                sendError(result.getString("serviceError"));
                return false;
            } else {
                return true;
            }

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }

        return false;
    }

    private void sendError(String message) {
        try {
            ErrorLog error = new ErrorLog();
            error.setCustId(TPApplication.getInstance().getCustId());
            error.setUserId(TPApplication.getInstance().getCurrentUserId());
            error.setDeviceId(TPApplication.getInstance().getDeviceInfo().getDeviceId());
            error.setDevice(TPApplication.getInstance().getDeviceInfo().getDevice());
            error.setModule(TPConstant.MODULE_NAME);
            error.setApp_version(TPApplication.getInstance().getDeviceInfo().getAppVersion());
            error.setErrorDesc(message);
            error.setErrorType("Service Error");
            error.setErrorDate(new Date());

            ArrayList<ErrorLog> errors = new ArrayList<ErrorLog>();
            errors.add(error);

            sendErrorLog(errors);
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }

    @Override
    public boolean sendEmail(String from, String[] to, String subject, String message) throws Exception {
        boolean result = false;
        try {
            JSONArray emailIds = new JSONArray();
            for (String email : to) {
                emailIds.put(email);
            }

            JSONObject params = new JSONObject();
            params.putOpt("from", from);
            params.putOpt("to", emailIds);
            params.putOpt("subject", subject);
            params.putOpt("message", message);

            result = getServiceClient().callBoolean("sendEmail", params);
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }

        return result;
    }

    @Override
    public JSONObject getValidTicket(int custId, long citationNumber) throws Exception {
        try {
            JSONObject params = new JSONObject();
            params.putOpt("citationNumber", citationNumber);
            params.putOpt("custId", custId);

            return getServiceClient().callJSONObject("getValidTicket", params);

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }

        return null;
    }

    @Override
    public JSONArray getVendors(int custId, boolean fullSync) throws Exception {
        JSONObject params = new JSONObject();
        JSONArray objects = null;
        params.putOpt("custId", custId);
        params.putOpt("fullSync", fullSync);
        objects = getServiceClient().callJSONArray("getVendors", params);

        return objects;
    }

    @Override
    public JSONArray getVendorServices(int custId, boolean fullSync) throws Exception {
        JSONObject params = new JSONObject();
        JSONArray objects = null;
        params.putOpt("custId", custId);
        params.putOpt("fullSync", fullSync);
        objects = getServiceClient().callJSONArray("getVendorServices", params);

        return objects;
    }

    @Override
    public JSONArray getVendorServiceRegistrations(int custId, int userId, int deviceId, boolean fullSync)
            throws Exception {

        JSONObject params = new JSONObject();
        JSONArray objects = null;
        params.putOpt("custId", custId);
        params.putOpt("userId", userId);
        params.putOpt("deviceId", deviceId);
        params.putOpt("fullSync", fullSync);
        objects = getServiceClient().callJSONArray("getVendorServiceRegistrations", params);

        return objects;
    }

    @Override
    public JSONArray getVendorItems(int custId, boolean fullSync) throws Exception {
        JSONObject params = new JSONObject();
        JSONArray objects = null;
        params.putOpt("custId", custId);
        params.putOpt("fullSync", fullSync);
        objects = getServiceClient().callJSONArray("getVendorItems", params);

        return objects;
    }

    @Override
    public JSONArray getModules(int custId, boolean fullSync) throws Exception {
        JSONObject params = new JSONObject();
        JSONArray objects = null;
        params.putOpt("custId", custId);
        params.putOpt("fullSync", fullSync);
        objects = getServiceClient().callJSONArray("getModules", params);

        return objects;
    }

    @Override
    public JSONArray getCustomerModules(int custId, boolean fullSync) throws Exception {
        JSONObject params = new JSONObject();
        JSONArray objects = null;
        params.putOpt("custId", custId);
        params.putOpt("fullSync", fullSync);
        objects = getServiceClient().callJSONArray("getCustomerModules", params);

        return objects;
    }

    @Override
    public ArrayList<String> verifyAndSyncTickets(int custId, ArrayList<String> citations) throws Exception {
        ArrayList<String> missingCitations = new ArrayList<String>();
        try {
            JSONArray citationsArray = new JSONArray();
            for (String citation : citations) {
                citationsArray.put(citation);
            }

            JSONObject params = new JSONObject();
            params.putOpt("custId", custId);
            params.putOpt("citations", citationsArray);

            JSONArray result = getServiceClient().callJSONArray("verifyAndSyncTickets", params);
            if (result != null) {
                for (int index = 0; index < result.length(); index++) {
                    missingCitations.add(result.getString(index));
                }
            }
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }

        return missingCitations;
    }


    @Override
    public boolean newHotList(Hotlist hotList) throws Exception {
        boolean result = false;
        JSONObject params = new JSONObject();
        JSONObject requestJSON = new JSONObject();
        try {
            params.putOpt("plate", hotList.getPlate());
            params.putOpt("custid", hotList.getCustId());
            params.putOpt("state_code", hotList.getStateCode());
            params.putOpt("plate_type", hotList.getPlateType());

            params.putOpt("begin_date", hotList.getBeginDate());
            params.putOpt("end_date", hotList.getEndDate());

            params.putOpt("comments", hotList.getComments());
            requestJSON.put("hotlist", params);
            result = getServiceClient().callBoolean("newHotlist", requestJSON);

        } catch (Exception e) {
            e.printStackTrace();
            log.error(TPUtility.getPrintStackTrace(e));
        }
        return result;
    }

    @Override
    public JSONObject getVersionUpdates(int custId, String module) throws JSONException {
        JSONObject params = new JSONObject();
        JSONObject result = null;
        params.putOpt("custId", custId);
        params.putOpt("module", module);

        try {
            result = getServiceClient().callJSONObject("getVersionUpdates", params);
        } catch (JSONRPCException e) {
            log.error(TPUtility.getPrintStackTrace(e));
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }

        return result;
    }

    @Override
    public JSONArray getLastUpdateRepeatOffender(int custid, String created_date) throws JSONException {
        JSONArray objects = null;
        try {
            JSONObject params = new JSONObject();
            params.putOpt("custid", custid);
            params.putOpt("created_date", created_date);
            objects = getServiceClient().callJSONArray("getlastUpDateRepeatOffenders", params);

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
        return objects;
    }

    @Override
    public JSONObject getEULA(int custId, int moduleId) throws Exception {
        JSONObject params = new JSONObject();
        JSONObject result = null;
        params.putOpt("userid", custId);
        params.putOpt("module_id", moduleId);

        try {
            result = getServiceClient().callJSONObject("getEULA", params);
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }

        return result;
    }




    @Override
    public JSONObject updateEULAAcceptance(int userId, int recId, String isAcceptance, int custId) throws Exception {
        JSONObject result = null;
        JSONObject params = new JSONObject();
        try {
            params.putOpt("userid", userId);
            params.putOpt("eula_id", recId);
            params.putOpt("is_accepted", isAcceptance);
            params.putOpt("custId", custId);

            result = getServiceClient().callJSONObject("eulaAcceptance", params);

        } catch (Exception e) {
            e.printStackTrace();
            log.error(TPUtility.getPrintStackTrace(e));
        }
        return result;
    }

    @Override
    public JSONArray getActivityList(int custId, String date) throws Exception {
        JSONArray objects = null;
        try {
            JSONObject params = new JSONObject();
            params.putOpt("custid", custId);
            params.putOpt("createDate", date);
            objects = getServiceClient().callJSONArray("getSpecialActivityReports", params);

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
        return objects;
    }

    @Override
    public boolean getPlacard(String agencyCode, String user, String plate, String placard, String source) throws Exception {
        boolean result = false;
        JSONObject params = new JSONObject();
        try {
            params.putOpt("AgencyCode", agencyCode);
            params.putOpt("User", user);
            params.putOpt("plate", plate);
            params.putOpt("placard", placard);
            params.putOpt("source", source);
            params.putOpt("deviceId", TPApplication.getInstance().deviceId);

            result = getServiceClient().callBoolean("GetLicenseDetail", params);

        } catch (Exception e) {
            e.printStackTrace();
            log.error(TPUtility.getPrintStackTrace(e));
        }
        return result;
    }
}

