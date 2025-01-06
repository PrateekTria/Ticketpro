package com.ticketpro.util;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.text.TextUtils;
import android.util.Log;

import com.ticketpro.api.ApiRequest;
import com.ticketpro.api.ServiceGenerator;
import com.ticketpro.model.DeviceData;
import com.ticketpro.model.Feature;
import com.ticketpro.model.FirebaseModel;
import com.ticketpro.model.FirebaseResponse;
import com.ticketpro.model.Ticket;
import com.ticketpro.model.TokenBody;
import com.ticketpro.model.TokenResponse;
import com.ticketpro.model.User;
import com.ticketpro.model.VendorService;
import com.ticketpro.parking.activity.TPApplication;
import com.ticketpro.parking.dao.ParkingDatabase;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import io.reactivex.annotations.NonNull;
import okhttp3.RequestBody;
import okio.Buffer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FirebaseDBUpdater {

    private final DeviceData deviceData = new DeviceData();
    @SuppressLint("StaticFieldLeak")
    private static Context mContext;
    private static Logger log = null;
    private static SharedPreferences sharedPreferences;
    private static String token;

    public FirebaseDBUpdater(Context context) {
        mContext = context;
        log = Logger.getLogger(getClass());
        sharedPreferences = mContext.getSharedPreferences(mContext.getPackageName(), MODE_PRIVATE);
    }

    //long lastID;
    public synchronized void updateFB(Location location, String activityName, Ticket activeTicket) throws Exception {
        try {
            //Toast.makeText(mContext, "updateFB Called", Toast.LENGTH_SHORT).show();
            //log.info("updateFB Called");

            //Set Firebase Field tracking URL.
            try {
                VendorService vendorService = VendorService.getServiceByName(VendorService.FIELD_TRACKER_TICKETPRO);
                if (TPConstant.IS_DEVELOPMENT_BUILD) {
                    if (vendorService.getTestURL() != null && !vendorService.getTestURL().isEmpty()) {
                        TPConstant.FIREBASE_DB_URL = vendorService.getTestURL();
                    }
                } else {
                    if (vendorService.getProdURL() != null && !vendorService.getProdURL().isEmpty()) {
                        TPConstant.FIREBASE_DB_URL = vendorService.getProdURL();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                //log.error("Field Tracker URL not found in Vendor Service Table.");
            }

            // //Toast.makeText(mContext, "UpdateFB method called. " + TPConstant.activityName, Toast.LENGTH_SHORT).show();
            String dutyId = "", dutyName = "", custId = "", deviceId = "", deviceName = "", username = "";
            final String firstLogin;
            final String lastTicketTimeStamp;
            final String currTimeStamp;
            String pushToken;
            final String moduleId = "1";
            String fullName = "";
            String device = "";
            String badge = "";
            String userId = "";
            String timeStamp;

            boolean isActive = false, deviceInactivity = false;
            TPApplication tpApp = TPApplication.getInstance();
            try {
               // Log.i("LocationService", "Start");
                if (!activityName.equalsIgnoreCase("logOut")) {
                    if (tpApp.getActiveDutyInfo() != null) {
                        if (tpApp.getActiveDutyInfo().getId() != 0) {
                            dutyId = String.valueOf(tpApp.getActiveDutyInfo().getId());
                        }

                        if (tpApp.getActiveDutyInfo().getTitle() != null) {
                            dutyName = tpApp.getActiveDutyInfo().getTitle();
                        }

                        String allowTicket = tpApp.getActiveDutyInfo().getAllowTicket();
                        if (allowTicket.equalsIgnoreCase("Y") || allowTicket.equalsIgnoreCase("W")) {
                            isActive = true;
                        }
                    }
                }

                if (tpApp.getCustId() != 0) {
                    custId = String.valueOf(tpApp.getCustId());
                }

                if (tpApp.getDeviceId() != 0) {
                    deviceId = String.valueOf(tpApp.getDeviceId());
                }

                if (tpApp.getDeviceName() != null) {
                    deviceName = tpApp.getDeviceName();
                }

                if (tpApp.getDeviceInfo().getDevice() != null) {
                    device = tpApp.getDeviceInfo().getDevice();
                }

                firstLogin = tpApp.getFirstLogin();
                if (!activityName.equalsIgnoreCase("logOut")) {
                    if (tpApp.getUserInfo() != null) {
                        if (tpApp.getUserInfo().getUsername() != null) {
                            username = tpApp.getUserInfo().getUsername();
                        }

                        if (tpApp.getUserInfo().getFullName() != null) {
                            fullName = tpApp.getUserInfo().getFullName();
                        }

                        if (tpApp.getUserInfo().getUserId() != 0) {
                            userId = String.valueOf(tpApp.getUserInfo().getUserId());
                        }

                        if (tpApp.getUserInfo().getBadge() != null) {
                            badge = tpApp.getUserInfo().getBadge();
                        }
                    }
                }

                lastTicketTimeStamp = mContext.getSharedPreferences(mContext.getPackageName(), MODE_PRIVATE).getString(TPConstant.PREFS_KEY_LASTTICKETTIME, "");

                pushToken = mContext.getSharedPreferences(mContext.getPackageName(), MODE_PRIVATE).getString(TPConstant.PUSH_TOKEN, "");

                @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm");
                System.out.println("receive Method");
                if (!TextUtils.isEmpty(lastTicketTimeStamp)) {
                    if (DateUtil.getTimeDiff(formatter, lastTicketTimeStamp, formatter.format(new Date())) > Integer.parseInt(Feature.getFeatureValue(Feature.DEVICEINACTIVITYTIME))) {
                        if (isActive) {
                            deviceInactivity = true;
                        }
                    }
                }

                timeStamp = DateUtil.getCurrentDateTimeMillis();
                currTimeStamp = String.valueOf(System.currentTimeMillis());

                if (activityName.equalsIgnoreCase("")) {
                    if (TPConstant.latitude == location.getLatitude() && TPConstant.longitude == location.getLongitude()) {
                        System.out.println("Duplicate Record");
                        return;
                    }
                }

                //Toast.makeText(mContext, "deviceData entry start", Toast.LENGTH_SHORT).show();
                deviceData.setDutyId(dutyId);
                deviceData.setDutyName(dutyName);
                deviceData.setCustId(custId);
                deviceData.setSync_status("P");
                deviceData.setDeviceId(deviceId);
                deviceData.setDeviceName(deviceName);
                deviceData.setLattitude(location.getLatitude());
                deviceData.setAltitude(location.getAltitude());
                deviceData.setLongitude(location.getLongitude());
                deviceData.setAccuracy(location.getAccuracy());
                deviceData.setName(username);
                deviceData.setUserId(userId);
                deviceData.setFullName(fullName);
                deviceData.setDevice(device);
                deviceData.setBadge(badge);
                deviceData.setTimeStamp(timeStamp);
                deviceData.setActive(isActive);
                deviceData.setFirstLogin(firstLogin);
                deviceData.setLastTicketTimeStamp(lastTicketTimeStamp);
                deviceData.setCurrTimeStamp(currTimeStamp);
                deviceData.setPushToken(pushToken);
                deviceData.setModuleId(moduleId);
                deviceData.setDeviceInactivity(deviceInactivity);
                deviceData.setAppVersion(mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionName);
                deviceData.setActivityName(activityName);
                deviceData.setLoggedIn(!username.equalsIgnoreCase("") && !username.isEmpty());

                if (activeTicket != null) {
                    deviceData.setViolation(activeTicket.getViolationCode());
                    deviceData.setCitation(String.valueOf(activeTicket.getCitationNumber()));
                    deviceData.setAddress(activeTicket.getLocation());
                } else {
                    deviceData.setViolation("");
                    deviceData.setCitation("");
                    deviceData.setAddress("");
                }

                final FirebaseModel firebaseModel = new FirebaseModel();
                firebaseModel.setCustId(custId);
                firebaseModel.setDeviceId(deviceId);
                try {

                    //Toast.makeText(mContext, "Database update called.", Toast.LENGTH_SHORT).show();
                    //DatabaseHelper.getInstance().insertOrReplace(deviceData.getContentValues(), "FT_DeviceHistory");
                    DeviceData.insertDeviceData(deviceData, sharedPreferences);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                getLocationFromlatlng(deviceData, firebaseModel);

            } catch (Exception e) {
                System.out.println("Exception");
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();        }
    }

    public static synchronized void getLocationFromlatlng(final DeviceData data, final FirebaseModel firebaseModel) {
        firebaseModel.setDeviceData(data);
        token = sharedPreferences.getString(TPConstant.PREFS_KEY_FB_TOKEN, "");
        if (token.equals("")) {
            getToken(firebaseModel, data.getCurrTimeStamp());
        } else
            updateDataToServer(firebaseModel, data.getCurrTimeStamp());
            /*try {
            ApiRequest apiRequest = ServiceGenerator.createGoogleService(ApiRequest.class);
            apiRequest.getAddressfromLatLng(data.getLattitude() + "," + data.getLongitude(), TPConstant.GOOGLE_MAP_API_KEY_TICKETPROAPP).enqueue(new Callback<GeocodeLocation>() {
                @Override
                public void onResponse(@NonNull Call<GeocodeLocation> call, @NonNull Response<GeocodeLocation> response) {
                    try {
                        //Toast.makeText(mContext, "GetAddress method called.", Toast.LENGTH_SHORT).show();
                        GeocodeLocation geocodeLocation = response.body();
                        List<GeocodeResult> geocodeResults;
                        if (geocodeLocation != null) {
                            geocodeResults = geocodeLocation.getResults();
                            if (geocodeResults != null && geocodeResults.size() > 0) {
                                String[] address = geocodeResults.get(0).getFormattedAddress().split(",");
                                data.setAddress(address[0]);
                                firebaseModel.setDeviceData(data);
                            } else {
                                return;
                            }
                        }
                        token = sharedPreferences.getString(TPConstant.PREFS_KEY_FB_TOKEN, "");
                        if (token.equals("")) {
                            getToken(firebaseModel, data.getCurrTimeStamp());
                        } else
                            updateDataToServer(firebaseModel, data.getCurrTimeStamp());
                    } catch (Exception e) {
                        //Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                        //log.error(TPUtility.getPrintStackTrace(e));
                    }
                }

                @Override
                public void onFailure(Call<GeocodeLocation> call, Throwable t) {
                    //log.error("Error getting token." + t.getMessage());
                    //Toast.makeText(mContext, "GetAddress method called. Failure", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            //log.error("Error getting token." + e.getMessage());
            //Toast.makeText(mContext, "GetAddress method called. Failure. " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }*/
    }

    private static synchronized void updateDataToServer(final FirebaseModel firebaseModel, final String timeStamp) {
        try {
            token = "Bearer " + token;
            ApiRequest apiRequest = ServiceGenerator.createFBService(ApiRequest.class, token);
            apiRequest.postDatatoFBDB(firebaseModel).enqueue(new Callback<FirebaseResponse>() {
                @Override
                public void onResponse(@androidx.annotation.NonNull Call<FirebaseResponse> call, @androidx.annotation.NonNull Response<FirebaseResponse> response) {
                    //Toast.makeText(mContext, "Data updated to server" + response.code(), Toast.LENGTH_SHORT).show();
                    //String body = bodyToString(response.raw().request().body());
                    //log.info(token);
                    //log.info(body);
                    //Toast.makeText(mContext, "Firebase Update :" + response.body().getFirebaseUpdate() + ":" + "Database Update" + response.body().getDatabaseUpdate(), Toast.LENGTH_SHORT).show();
                    //log.info("Firebase Update :" + response.body().getFirebaseUpdate() + ":" + "Database Update" + response.body().getDatabaseUpdate());
                    FirebaseResponse firebaseResponse = response.body();
                    try {
                        if (firebaseResponse != null && firebaseResponse.isTokenExpired() != null) {
                            if (firebaseResponse.isTokenExpired()) {
                                getToken(firebaseModel, timeStamp);
                                return;
                            }
                        }
                    } catch (Exception e) {
                        //Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                        //log.error(TPUtility.getPrintStackTrace(e));
                    }

                    try {
                        if (response.body() != null && response.body().getDatabaseUpdate().equals(true)) {
                            ParkingDatabase.getInstance(TPApplication.getInstance()).ftDeviceHistoryDao().updateSyncStatus(timeStamp);
                        }
                    } catch (Exception e) {
                        //Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                        //log.error(TPUtility.getPrintStackTrace(e));
                    }
                    //Log.i("MyLocationReceiver", response.body() != null ? response.body().toString() : null);
                }

                @Override
                public void onFailure(@NonNull Call<FirebaseResponse> call, @NonNull Throwable t) {
                    //Log.i("MyLocationReceiver", Objects.requireNonNull(t.getMessage()));
                    //log.error("Error saving data to Server" + t.getMessage());
                }
            });
        } catch (Exception e) {
            //log.error("Error saving data to Server" + e.getMessage());
            e.printStackTrace();

        }
    }

    private static String bodyToString(final RequestBody request) {
        try {
            final Buffer buffer = new Buffer();
            if (request != null)
                request.writeTo(buffer);
            else
                return "";
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }

    private static synchronized void getToken(final FirebaseModel firebaseModel, final String currTimeStamp) {
        try {
            //Toast.makeText(mContext, "Token method called.", Toast.LENGTH_SHORT).show();
            ApiRequest apiRequest = ServiceGenerator.createService(ApiRequest.class);
            User user = TPApplication.getInstance().getUserInfo();
            TokenBody tokenBody = new TokenBody();
            tokenBody.setId("82F85DB43CBF6");
            if (user != null) {
                tokenBody.setCustId(user.getCustId());
                tokenBody.setUsername(user.getUsername());
                tokenBody.setPassword(user.getPassword());
            } else {
                tokenBody.setCustId(Integer.parseInt(firebaseModel.getDeviceData().getCustId()));
                tokenBody.setUsername(sharedPreferences.getString(TPConstant.PREFS_KEY_LAST_USERNAME, ""));
                tokenBody.setPassword(sharedPreferences.getString(TPConstant.PREFS_KEY_LAST_USERPWD, ""));
            }
            apiRequest.getToken(tokenBody).enqueue(new Callback<TokenResponse>() {
                @Override
                public void onResponse(@NonNull Call<TokenResponse> call, @NonNull Response<TokenResponse> response) {
                    TokenResponse tokenResponse = response.body();
                    assert tokenResponse != null;
                    //Toast.makeText(mContext, "Token method called." + tokenResponse.getToken() + tokenResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    if (tokenResponse.getToken() != null) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(TPConstant.PREFS_KEY_FB_TOKEN, tokenResponse.getToken());
                        editor.apply();
                        token = tokenResponse.getToken();
                    }
                    updateDataToServer(firebaseModel, currTimeStamp);
                }

                @Override
                public void onFailure(@NonNull Call<TokenResponse> call, @NonNull Throwable t) {
                    //Log.i("FBUPDATER", "onFailure: ");
                    //Toast.makeText(mContext, "Token method failure.", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            //log.error(e.getMessage());
            //Toast.makeText(mContext, "Token method failure." + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
