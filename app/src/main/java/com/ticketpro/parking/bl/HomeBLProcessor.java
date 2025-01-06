package com.ticketpro.parking.bl;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import com.ticketpro.exception.TPException;
import com.ticketpro.model.DeviceInfo;
import com.ticketpro.model.ServiceResult;

public class HomeBLProcessor extends BLProcessorImpl {

    public HomeBLProcessor() {
        setLogger(BLProcessorImpl.class.getName());
    }

    public boolean doSynchronize(boolean fullSync, Activity context, Handler.Callback callback) {
        try {
            return proxy.syncDatabase(fullSync, context, callback);
        } catch (Exception e) {
            return false;
        }
    }

    public ServiceResult isRegisteredDevice(String deviceName) throws TPException {
        return proxy.isRegisteredDevice(deviceName);
    }


    public DeviceInfo registeredDevice(DeviceInfo device) throws TPException {
        return proxy.registeredDevice(device);
    }

    public void configureDevice(final Handler.Callback callback) throws TPException {
        proxy.updateAllTables(true, new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message message) {
                try {
                    proxy.updateTicketHistory();
                    callback.handleMessage(message);
                } catch (TPException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
    }

    public void updateDeviceInfo(Context context) throws TPException {
        proxy.updateDeviceInfo(context);
    }

}
