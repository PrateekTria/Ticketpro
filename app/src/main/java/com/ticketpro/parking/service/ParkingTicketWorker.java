package com.ticketpro.parking.service;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.gson.Gson;
import com.ticketpro.api.ApiRequest;
import com.ticketpro.api.ServiceGenerator;
import com.ticketpro.model.DeviceInfo;
import com.ticketpro.model.Params;
import com.ticketpro.model.RequestPOJO;
import com.ticketpro.model.Ticket;
import com.ticketpro.model.TicketComment;
import com.ticketpro.model.TicketPicture;
import com.ticketpro.model.TicketResponse;
import com.ticketpro.model.TicketSuccess;
import com.ticketpro.parking.activity.TPApplication;
import com.ticketpro.util.TPConstant;
import com.ticketpro.util.TPUtility;

import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ParkingTicketWorker extends Worker {

    private static final String TAG = "ParkingTicketWorker";
    private static final Logger log = Logger.getLogger("ParkingTicketWorker");
    public TPApplication TPApp;
    boolean SocketTimeOutFlag = false;
    public ParkingTicketWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);

    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            ArrayList<DeviceInfo> deviceInfo = new ArrayList<>();
            deviceInfo.add(TPApplication.getInstance().getDeviceInfo());

            if(TPApplication.getInstance().getDeviceInfo() != null){
                syncDevices(deviceInfo, "ParkingTicketWorker");
            }
            /**
             * ticket data is pending
             */
            ArrayList<Ticket> ticketsArray = Ticket.getPendingTickets();

            if (!ticketsArray.isEmpty()) {

                for (int i = 0; i < ticketsArray.size(); i++) {
                    Ticket ticket = ticketsArray.get(i);
                    System.out.println("Citation:**************" + ticket.getCitationNumber());
                    if (SocketTimeOutFlag) {
                        break;
                    }else {
                        saveTicketOnServer(ticket);
                        Thread.sleep(5000);
                    }
                }
            }

            ArrayList<Ticket> ticketsArrayPI = Ticket.getPendingTicketsPI();
            if (ticketsArrayPI.size() > 0 && !SocketTimeOutFlag) {
                for (int i = 0; i < ticketsArrayPI.size(); i++) {
                    Ticket ticket = ticketsArrayPI.get(i);
                    /**
                     * We are only fetching the images captured from the device that needs to be
                     * uploaded and not the LPR because LPR images are already uploaded from server to Server
                     */
                    ArrayList<TicketPicture> ticketPicturesPending = TicketPicture.getTicketPicturesPending(ticket.getCitationNumber());
                    if (ticketPicturesPending != null && ticketPicturesPending.size() > 0) {
                        final ArrayList<TicketPicture> uploadImages = new ArrayList<>();
                        for (int j = 0; j < ticketPicturesPending.size(); j++) {
                            TicketPicture ticketPicture = ticketPicturesPending.get(j);
                            uploadImages.add(ticketPicture);
                        }
                        syncUploadImages(ticket.getCitationNumber(), uploadImages);
                    } else {
                        /**
                         *  All the images are synchronized as part of background thread.
                         */
                        Ticket.updateTicket(String.valueOf(ticket.getCitationNumber()), "S");
                    }

                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        return Result.success();
    }

    public static void syncDevices(ArrayList<DeviceInfo> deviceInfo, String name) {
        if(deviceInfo == null || deviceInfo.isEmpty()){
            return;
        }
        Params params = new Params();
        params.setDevices(deviceInfo);
        RequestPOJO requestPOJO = new RequestPOJO();
        requestPOJO.setParams(params);
        requestPOJO.setMethod("updateDevices");
        ApiRequest api = ServiceGenerator.createRxService(ApiRequest.class);
        api.syncDevices(requestPOJO).enqueue(new Callback<ResponseBody>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                Log.i(TAG, "onResponse: ");

                // log.info("updateDevices process done");
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                log.info(name + "-Function name is syncDevices() " + t.getMessage());
                log.error(TPUtility.getPrintStackTrace(t));
                call.cancel();
            }
        });
    }

    private synchronized void saveTicketOnServer(Ticket ticket) {
        try {
            ApiRequest api = ServiceGenerator.createRxService(ApiRequest.class);
            RequestPOJO requestPOJO;
            Params params;
            /*if (ticketCounter == ticketsArrayDetails.size()) {
                ticketCounter = 0;
                return;
            }*/
            //Ticket ticket = ticketsArrayDetails.get(ticketCounter);

            final ArrayList<Ticket> _singleTicketArray = new ArrayList<>();
            final ArrayList<TicketPicture> uploadImages = new ArrayList<>();
            final ArrayList<String> uploadVoiceComments = new ArrayList<>();

            long ticketId = Ticket.getTicketIdByCitationNumber(ticket.getCitationNumber());
            ArrayList<TicketComment> comments = TicketComment.getTicketComments(ticketId, ticket.getCitationNumber());
            for (TicketComment comment : comments) {
                if (comment.isVoiceComment()) {
                    uploadVoiceComments.add(comment.getComment());
                }
            }
            ArrayList<TicketPicture> pictures = TicketPicture.getTicketPictures(ticketId, ticket.getCitationNumber());
            ArrayList<TicketPicture> ticketPictures = new ArrayList<>();
            for (TicketPicture picture : pictures) {
                if ("Y".equalsIgnoreCase(picture.getLprNotification())) {
                    continue;
                }
                if (!picture.getImagePath().contains("VLPR")) {
                    uploadImages.add(picture);
                }
                String[] path = picture.getImagePath().split("/");
                picture.setImagePath(path[path.length - 1]);
                ticketPictures.add(picture);
            }
            ticket.setTicketPictures(ticketPictures);
            _singleTicketArray.add(ticket);
            requestPOJO = new RequestPOJO();
            requestPOJO.setMethod("updateTickets");
            params = new Params();
            params.setTickets(_singleTicketArray);
            requestPOJO.setParams(params);

            if (_singleTicketArray.size() > 0) {

                api.syncTickets(requestPOJO).enqueue(new Callback<TicketResponse>() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onResponse(@NotNull Call<TicketResponse> call, @NotNull Response<TicketResponse> response) {
                        //ticketCounter++;
                        try {
                            if (response.isSuccessful() && response.body() != null && response.body().getResult().getResult()) {
                                Log.i(TAG, "onResponse: " + new Gson().toJson(response.body()));
                                if (uploadImages.size() > 0) {
                                    syncUploadImages(ticket.getCitationNumber(), TicketPicture.getTicketPicturesByCitationPI(ticket.getCitationNumber()));
                                }
                                uploadVoiceComments(uploadVoiceComments);

                                if (response.body().getResult().getSuccess() != null && uploadImages.size() > 0) {
                                    /**
                                     *the data is saved on server but we do not have image upload status
                                     * as of now so we are updating on ticket table sync status as PI(Pending Image)
                                     */
                                    updateTicketStatus(response.body().getResult().getSuccess(), "PI");
                                } else {
                                    /**
                                     * IF there is no image to send on the server.
                                     */
                                    updateTicketStatus(response.body().getResult().getSuccess(), "S");
                                }
                                //saveTicketOnServer(ticketsArrayDetails);
                            } else {
                                //log.debug(new Gson().toJson(response.raw().request()));
                                //log.debug(response.body().getResult().getResult());
                                // log.debug("Request this Citation " + ticket.getCitationNumber());
                                //saveTicketOnServer(ticketsArrayDetails);
                            }

                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                            //log.error(e.getMessage());
                            //log.debug(new Gson().toJson(response.raw().request()));
                            //log.debug("Request this Citation " + ticket.getCitationNumber());
                            // saveTicketOnServer(ticketsArrayDetails);
                            /*try {
                                ArrayList<Ticket> ticketsArray = Ticket.getPendingTickets();
                                if (ticketsArray.size()>0){

                                    WriteTicketNetworkCalls.syncTickets(ticketsArray);
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                log.error(ex.getMessage());
                            }*/
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<TicketResponse> call, @NotNull Throwable t) {
                        call.cancel();
                        if (t.getMessage().equalsIgnoreCase("timeout")|| t.getMessage().equalsIgnoreCase("Read timed out")){
                            SocketTimeOutFlag = true;
                        }
                    }

                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            // log.error(e.getMessage());
            //log.debug("Request this Citation " + ticket.getCitationNumber());

        }
    }

    private static void updateTicketStatus(TicketSuccess response, String status) {
        try {
            if (response != null && response.getResult()) {
                Ticket.updateTicket(String.valueOf(response.getCitationNumber()), status);
                //log.info("Ticket Save on server from WorkerClass=====>" + response.getCitationNumber());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void syncUploadImages(long citationNumber, final ArrayList<TicketPicture> images) {
        new Thread(() -> {
            boolean uploadFlag = false;
            for (TicketPicture ticketPicture : images) {
                try {
                    //if (!ticketPicture.getImagePath().contains("VLPR")) {
                        uploadFlag = TPUtility.uploadFile(ticketPicture.getImagePath(),
                                TPConstant.FILE_UPLOAD + "/uploadfile",
                                TPApplication.getInstance().getCustId());
                        __updateTicketPictureImageStatus(ticketPicture.getS_no(), citationNumber, uploadFlag);

                  //  }

                } catch (Exception e) {
                    log.error(TPUtility.getPrintStackTrace(e));
                    //TPUtility.markPendingImage(imagePath);
                    __updateTicketPictureImageStatus(ticketPicture.getS_no(), citationNumber, uploadFlag);
                }
            }
        }).start();
    }

    private void syncTicketImage(long citationNumber, final ArrayList<String> images) {
        for (String imagePath : images) {
            try {
               // if (!imagePath.contains("VLPR")) {

                    File file = new File(imagePath);
                    RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

             //   }
                // __updateTicketPictureImageStatus(citationNumber, uploadFlag);

            } catch (Exception e) {
                log.error(TPUtility.getPrintStackTrace(e));
                TPUtility.markPendingImage(imagePath);
            }
        }

    }

    private static void __updateTicketPictureImageStatus(int sNo, long citationNumber, boolean uploadFlag) {
        String imageStatus = "P";
        if (uploadFlag) {
            imageStatus = "S";
        }
        TicketPicture.updateTicketPictureSyncStatus(String.valueOf(citationNumber), imageStatus, sNo);
    }

    // Upload Voice Comments
    public static void uploadVoiceComments(ArrayList<String> uploadVoiceComments) {
        if (uploadVoiceComments == null || uploadVoiceComments.size() == 0) {
            return;
        }
        final int custId = TPApplication.getInstance().custId;
        final ArrayList<String> voiceMemos = uploadVoiceComments;
        new Thread(() -> {
            boolean hasUploaded;
            for (String memo : voiceMemos) {
                hasUploaded = TPUtility.uploadFile(TPUtility.getVoiceMemosFolder() + memo,
                        TPConstant.FILE_UPLOAD + "/uploadfile", custId);
                if (!hasUploaded) {
                    TPUtility.markPendingVoiceComment(memo);
                }
            }
        }).start();
    }

}
