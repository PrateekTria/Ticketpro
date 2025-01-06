package com.ticketpro.util;

import static com.ticketpro.util.TPConstant.DISPLAY_MESSAGE_ACTION;
import static com.ticketpro.util.TPConstant.EXTRA_MESSAGE;
import static com.ticketpro.util.TPConstant.TAG;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ticketpro.api.ApiRequest;
import com.ticketpro.api.ServiceGenerator;
import com.ticketpro.model.Params;
import com.ticketpro.model.RequestPOJO;
import com.ticketpro.parking.activity.TPApplication;
import com.ticketpro.parking.service.ServiceHandlerImpl;

import io.reactivex.annotations.NonNull;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Helper class used to manage device GCM Registration.
 */
public final class GCMUtilities {

    public static boolean register(final Context context, final String regId) {
        Log.i(TAG, "Registering device (regId = " + regId + ")");

        new Thread(() -> {
            TPApplication app = (TPApplication) context.getApplicationContext();
            ServiceHandlerImpl service = new ServiceHandlerImpl();
            RequestPOJO requestPOJO = new RequestPOJO();
            Params params = new Params();
            params.setDeviceIName(app.getDeviceName());
            params.setRegistrationId(regId);
            requestPOJO.setParams(params);
            requestPOJO.setMethod("updateGCMRegistrationId");
            ApiRequest api = ServiceGenerator.createRxService(ApiRequest.class);
            Call<ResponseBody> gcm = api.updateGCMRegistrationID(requestPOJO);
            gcm.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    Log.i(TAG, "onResponse: ");
                }

                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                    Log.e(TAG, "onFailure: ");
                }
            });
            /*try {
                service.updateGCMRegistrationId(app.getDeviceName(), regId);
            } catch (Exception e) {
                e.printStackTrace();
            }*/
        }).start();
        return false;
    }

    public static void unregister(final Context context, final String regId) {
        Log.i(TAG, "Unregistering device (regId = " + regId + ")");
        new Thread(new Runnable() {
            @Override
            public void run() {
                TPApplication app = (TPApplication) context.getApplicationContext();
                ServiceHandlerImpl service = new ServiceHandlerImpl();
                try {
                    service.updateGCMRegistrationId(app.getDeviceName(), "");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    public static void displayMessage(Context context, String message) {
        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        intent.putExtra(EXTRA_MESSAGE, message);
        context.sendBroadcast(intent);
    }


}
