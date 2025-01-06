package com.ticketpro.parking.service;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import com.google.gson.Gson;
import com.ticketpro.api.ApiRequest;
import com.ticketpro.api.ServiceGenerator;
import com.ticketpro.model.ChalkComment;
import com.ticketpro.model.ChalkPicture;
import com.ticketpro.model.ChalkVehicle;
import com.ticketpro.model.Params;
import com.ticketpro.model.RequestPOJO;
import com.ticketpro.model.SyncData;
import com.ticketpro.model.Ticket;
import com.ticketpro.model.TicketPicture;
import com.ticketpro.model.chalk_response.ChalkResponse;
import com.ticketpro.parking.activity.TPApplication;
import com.ticketpro.util.TPConstant;
import com.ticketpro.util.TPUtility;

import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JobIntentServiceSaveChalk extends JobIntentService {

    private static final String TAG = "JobIntentServiceSaveChalk";
    private static final Logger log = Logger.getLogger("JobIntentServiceSaveChalk");
    public TPApplication TPApp;

    public JobIntentServiceSaveChalk() {
        this.TPApp = TPApplication.getInstance();
    }

    public static void enqueueWork(Activity context, Intent serviceIntent) {
        enqueueWork(context, JobIntentServiceSaveChalk.class, 125, serviceIntent);
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {

        try {
            ArrayList<ChalkVehicle> pendingChalkedVehicle = ChalkVehicle.getPendingChalkedVehicle();
            if (pendingChalkedVehicle != null && pendingChalkedVehicle.size() > 0) {
                for (int i = 0; i < pendingChalkedVehicle.size(); i++) {
                    ChalkVehicle chalkVehicle = pendingChalkedVehicle.get(i);
                    saveChalkOnServer(chalkVehicle);

                }
            } else {}

            //Image is pending
            ArrayList<ChalkVehicle> pendingPIChalkedVehicle = ChalkVehicle.getPendingPIChalkedVehicle();
            if (pendingPIChalkedVehicle.size() > 0) {
                for (int i = 0; i < pendingPIChalkedVehicle.size(); i++) {
                    ChalkVehicle chalkVehicle = pendingPIChalkedVehicle.get(i);
                    /**
                     * We are only fetching the images captured from the device that needs to be
                     * uploaded and not the LPR because LPR images are already uploaded from server to Server
                     */
                    ArrayList<ChalkPicture> chalkPictures = ChalkPicture.getPendingChalkPicturesById(chalkVehicle.getChalkId());
                    if (chalkPictures != null && chalkPictures.size() > 0) {
                        final ArrayList<ChalkPicture> uploadImages = new ArrayList<>();
                        for (int j = 0; j < chalkPictures.size(); j++) {
                            ChalkPicture ticketPicture = chalkPictures.get(j);
                            uploadImages.add(ticketPicture);
                        }
                        syncUploadImages(chalkVehicle.getChalkId(), uploadImages);
                    } else {
                        /**
                         *  All the images are synchronized as part of background thread.
                         */
                        ChalkVehicle.updateChalkStatus(chalkVehicle.getChalkId(),"S");                    }

                }
            }else {}


        } catch (Exception e) {
            e.printStackTrace();
            log.debug(e.getMessage());
        }
    }

    private synchronized void saveChalkOnServer(ChalkVehicle chalkVehicle) throws Exception {
        ArrayList<ChalkPicture> chalkPictures1 = new ArrayList<>();
        ArrayList<ChalkVehicle> chalkVehicles = new ArrayList<>();

        ArrayList<ChalkPicture> chalkPicturesPath = ChalkPicture.getChalkPictures(chalkVehicle.getChalkId());
        ArrayList<ChalkPicture> pictures = new ArrayList<>();
        if (chalkPicturesPath!=null && chalkPicturesPath.size()>0) {
            for (ChalkPicture picture : chalkPicturesPath) {
                //chalkPictures1.add(picture);
                String[] path = picture.getImagePath().split("/");
                picture.setImagePath(path[path.length - 1]);
                pictures.add(picture);
            }
            chalkVehicle.setChalkPictures(pictures);
        }

        ApiRequest api = ServiceGenerator.createRxService(ApiRequest.class);
        RequestPOJO requestPOJO = new RequestPOJO();
        requestPOJO.setMethod("updateChalks");
        Params params = new Params();
        chalkVehicles.add(chalkVehicle);
        params.setChalks(chalkVehicles);
        requestPOJO.setParams(params);
        api.syncChalks(requestPOJO).enqueue(new Callback<ChalkResponse>() {
            @Override
            public void onResponse(@NotNull Call<ChalkResponse> call, @NotNull Response<ChalkResponse> response) {
                try {
                    if (response.isSuccessful() && response.body() != null && response.body().getResult().getResult()) {
                        //long apkChalkId = response.body().getResult().getSuccess().getApkChalkId();

                        if (chalkPictures1.size() > 0) {
                            syncUploadImages(chalkVehicle.getChalkId(), ChalkPicture.getChalkPictures(chalkVehicle.getChalkId()));

                            ChalkVehicle.updateChalkStatus(chalkVehicle.getChalkId(),"PI");
                        }else {
                            ChalkVehicle.updateChalkStatus(chalkVehicle.getChalkId(),"S");
                        }

                    }else {
                        log.debug("Chalk fail");
                        log.debug("Request is: " +new Gson().toJson(response.raw().request()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.debug(e.getMessage());
                }
            }

            @Override
            public void onFailure(@NotNull Call<ChalkResponse> call, @NotNull Throwable t) {
                log.error(TPUtility.getPrintStackTrace(t));
            }
        });
    }

    private static void __updateChalkPictureImageStatus(int sNo, long chalkId, boolean uploadFlag) {
        String imageStatus = "P";
        if (uploadFlag) {
            imageStatus = "S";
        }
        ChalkPicture.__updatePictureStatus(sNo,chalkId,imageStatus);
    }

    public static void syncUploadImages(long citationNumber, final ArrayList<ChalkPicture> images) {
        new Thread(() -> {
            boolean uploadFlag = false;
            for (ChalkPicture ticketPicture : images) {
                try {
                    if (!ticketPicture.getImagePath().contains("VLPR")) {
                        uploadFlag = TPUtility.uploadFile(ticketPicture.getImagePath(),
                                TPConstant.FILE_UPLOAD + "/uploadfile",
                                TPApplication.getInstance().getCustId());

                        __updateChalkPictureImageStatus(ticketPicture.getPictureId(), citationNumber, uploadFlag);

                    }

                } catch (Exception e) {
                    log.error(TPUtility.getPrintStackTrace(e));
                    //TPUtility.markPendingImage(imagePath);
                    __updateChalkPictureImageStatus(ticketPicture.getPictureId(), citationNumber, uploadFlag);
                }
            }
        }).start();
    }


    public static void syncUploadImages(final ArrayList<String> images) {
        if (images == null || images.size() == 0) {
            return;
        }

        new Thread(() -> {
            boolean uploadFlag = true;
            for (String imagePath : images) {
                try {
                    if (!imagePath.contains("VLPR")) {
                        uploadFlag = TPUtility.uploadFile(imagePath,
                                TPConstant.FILE_UPLOAD + "/uploadfile",
                                TPApplication.getInstance().getCustId());
                    }
                    if (!uploadFlag) {
                        TPUtility.markPendingImage(imagePath);
                    }
                } catch (Exception e) {
                    log.error(TPUtility.getPrintStackTrace(e));
                    TPUtility.markPendingImage(imagePath);
                }
            }
        }).start();
    }

}
