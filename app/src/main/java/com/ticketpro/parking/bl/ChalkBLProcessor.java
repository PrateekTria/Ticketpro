package com.ticketpro.parking.bl;

import android.content.Context;

import com.ticketpro.exception.TPException;
import com.ticketpro.model.ALPRChalk;
import com.ticketpro.model.ChalkPicture;
import com.ticketpro.model.ChalkVehicle;
import com.ticketpro.model.SyncData;
import com.ticketpro.model.Zone;
import com.ticketpro.parking.activity.TPApplication;
import com.ticketpro.util.TPConstant;

import java.util.ArrayList;
import java.util.Date;

public class ChalkBLProcessor extends BLProcessorImpl {

    public ChalkBLProcessor(TPApplication TPApp) {
        setLogger(ChalkBLProcessor.class.getName());
        setTPApplication(TPApp);
    }

    public ArrayList<String> getDurations() throws TPException {
        return proxy.getDurationsTitles();
    }

    public void syncData(ArrayList<SyncData> list, Context context) throws TPException {
        proxy.syncData(list, context);
    }

    public void syncChalkPicture(ArrayList<SyncData> list, Context context) throws TPException {
        proxy.syncChalkPictures(list, context);
    }

    public ArrayList<ChalkPicture> getChalkPictures(long chalkId) throws TPException {
        return proxy.getChalkPictures(chalkId);
    }

    public ArrayList<Zone> getZones() throws TPException {
        return proxy.getZones();
    }

    public ArrayList<String> getZonesTitles() throws TPException {
        return proxy.getZonesTitles();
    }

    public ArrayList<String> getLocationTitles() throws TPException {
        return proxy.getLocationTitles();
    }

    public ArrayList<ChalkVehicle> getChalkVehicles() throws TPException {
        return proxy.getChalkVehicles(TPApp.getCurrentUserId(), TPApp.getDeviceId());
    }

    public ArrayList<ChalkVehicle> getUserChalks(int userId, Date fromDate, Date toDate, int durationId) throws TPException {
        return proxy.getUserChalks(userId, fromDate, toDate, durationId);
    }

    public ArrayList<ChalkVehicle> getChalkByLocation(Context ctx) throws Exception {
        return ChalkVehicle.getChalkByType(TPApp.getCurrentUserId(), TPConstant.CHALK_TYPE_LOCATION, ctx);
    }

    public ArrayList<ChalkVehicle> getChalkByPhoto(Context ctx) throws Exception {
        return ChalkVehicle.getChalkByType(TPApp.getCurrentUserId(), TPConstant.CHALK_TYPE_PHOTO, ctx);
    }

    public ArrayList<ALPRChalk> getChalkByPhotoALPR(Context ctx) throws Exception {
        return ALPRChalk.getChalkVehicles();
    }

    public ArrayList<ChalkVehicle> getChalkByPlate(Context ctx) throws Exception {
        return ChalkVehicle.getChalkByType(TPApp.getCurrentUserId(), TPConstant.CHALK_TYPE_PLATE, ctx);
    }

    public ArrayList<ChalkVehicle> getAllChalkedVehicles() throws TPException {
        return proxy.getAllChalkedVehicles(TPApp.getDeviceId());
    }

}
