package com.ticketpro.parking.api;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ticketpro.api.ApiRequest;
import com.ticketpro.api.RetrofitServiceGenerator;
import com.ticketpro.api.ServiceGenerator;
import com.ticketpro.exception.TPException;
import com.ticketpro.model.DeviceInfo;
import com.ticketpro.model.ErrorLog;
import com.ticketpro.model.Feature;
import com.ticketpro.model.Hotlist;
import com.ticketpro.model.HotlistResponse;
import com.ticketpro.model.MobileNowLog;
import com.ticketpro.model.Params;
import com.ticketpro.model.Permit;
import com.ticketpro.model.PermitResponse;
import com.ticketpro.model.RequestPOJO;
import com.ticketpro.model.Ticket;
import com.ticketpro.model.TicketComment;
import com.ticketpro.model.TicketFailure;
import com.ticketpro.model.TicketPicture;
import com.ticketpro.model.TicketResponse;
import com.ticketpro.model.TicketSuccess;
import com.ticketpro.model.TicketsResponse;
import com.ticketpro.parking.activity.TPApplication;
import com.ticketpro.parking.activity.WriteTicketActivity;
import com.ticketpro.util.CSVUtility;
import com.ticketpro.util.DateUtil;
import com.ticketpro.util.TPConstant;
import com.ticketpro.util.TPUtility;

import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Rohit on 10-08-2020.
 */
public class WriteTicketNetworkCalls {
    static boolean breakFlag = false;
    private static final String TAG = "WriteTicketNetworkCalls";
    private static final Logger log = Logger.getLogger("WriteTicketNetworkCalls");
    public TPApplication TPApp;
    static int ticketCounter = 0;
    public WriteTicketNetworkCalls() {
        this.TPApp = TPApplication.getInstance();
    }


    public static void syncTickets(List<Ticket> ticketsArray) throws InterruptedException {
        if (!ticketsArray.isEmpty()) {

            for (int i = 0; i < ticketsArray.size(); i++) {
                Ticket ticket = ticketsArray.get(i);
                System.out.println("Citation:**************" + ticket.getCitationNumber());
                Thread.sleep(5000);
                if (breakFlag) {
                    break;
                }else {
                    saveTicketOnServer(ticket);
                }
            }
        }

    }
    private static synchronized void saveTicketOnServer(Ticket ticket) {
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

            if (!_singleTicketArray.isEmpty()) {

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
                                log.debug("Request this Citation " + ticket.getCitationNumber());
                                //saveTicketOnServer(ticketsArrayDetails);
                            }

                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                            log.error(e.getMessage());
                            //log.debug(new Gson().toJson(response.raw().request()));
                            log.debug("Request this Citation " + ticket.getCitationNumber());
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
                        log.error("Function name is saveTicketTask()  " + t.getMessage());
                        log.error(TPUtility.getPrintStackTrace(t));
                        log.debug("Request this Citation " + ticket.getCitationNumber());

                        call.cancel();
                        breakFlag = true;
                        //ticketCounter++;
                        //saveTicketOnServer(ticketsArrayDetails);
                    }

                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            log.debug("Request this Citation " + ticket.getCitationNumber());

        }
    }

    private static void updateTicketStatus(TicketSuccess response, String status) {
        try {
            if (response != null && response.getResult()) {
                Ticket.updateTicket(String.valueOf(response.getCitationNumber()), status);
                log.info("Ticket Save on server=====>" + response.getCitationNumber());
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


    public static void syncDevices(ArrayList<DeviceInfo> deviceInfo,String name) {
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
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                Log.i(TAG, "onResponse: ");
                log.trace("updateDevices process done");
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                log.debug(name+" -Function name is syncDevices() " + t.getMessage());
                log.error(TPUtility.getPrintStackTrace(t));
            }
        });
    }

    private static void updateTicketStatus(TicketSuccess response) {
        try {
            if (response != null && response.getResult()) {
                Ticket.updateTicket(String.valueOf(response.getCitationNumber()), "S");
                log.info("Ticket Save on server=====>" + response.getCitationNumber());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void syncUploadImages(final ArrayList<String> images) {
        if (images == null || images.size() == 0) {
            return;
        }

        new Thread(() -> {
            boolean uploadFlag = true;
            for (String imagePath : images) {
                try {
                   // if (!imagePath.contains("VLPR")) {
                        uploadFlag = TPUtility.uploadFile(imagePath,
                                TPConstant.FILE_UPLOAD + "/uploadfile",
                                TPApplication.getInstance().getCustId());
                  //  }
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

    // Upload Voice Comments

    public static void sendErrorLogs(ArrayList<ErrorLog> list) {
        ApiRequest api = ServiceGenerator.createRxService(ApiRequest.class);
        RequestPOJO requestPOJO = new RequestPOJO();
        requestPOJO.setMethod("updateErrorLog");
        for (ErrorLog errorLog : list) {
            errorLog.setDate(DateUtil.getSQLStringFromDate(errorLog.getErrorDate()));
        }
        Params params = new Params();
        params.setErrorLog(list);
        requestPOJO.setParams(params);
        api.sendErrorLogs(requestPOJO).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                try {
                    assert response.body() != null;
                    Log.i(TAG, "onResponse: " + response.body().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                log.debug("Function name is sendErrorLogs() " + t.getMessage());
                log.error(TPUtility.getPrintStackTrace(t));
            }
        });
    }

    public static void sendEmail(final Activity activity, String from, String[] to, String subject, String message) {
        ApiRequest api = ServiceGenerator.createRxService(ApiRequest.class);
        RequestPOJO requestPOJO = new RequestPOJO();
        requestPOJO.setMethod("sendEmail");
        Params params = new Params();
        params.setFrom(from);
        params.setTo(to);
        params.setMessage(message);
        params.setSubject(subject);
        requestPOJO.setParams(params);
        api.sendEmail(requestPOJO).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                Toast.makeText(activity, "Your email sent successfully", Toast.LENGTH_LONG)
                        .show();
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                log.error(TPUtility.getPrintStackTrace(t));
                Toast.makeText(activity, "Failed sending email, please try again",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    public static void updateChalkStatus(long chalkId, String status, String dispositionReason) {
        ApiRequest api = ServiceGenerator.createRxService(ApiRequest.class);
        RequestPOJO requestPOJO = new RequestPOJO();
        requestPOJO.setMethod("updateChalkStatus");
        Params params = new Params();
        params.setChalkId(chalkId);
        params.setStatus(status);
        params.setDispositionReason(dispositionReason);
        requestPOJO.setParams(params);
        api.updateChalkStatus(requestPOJO).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                try {
                    Log.i(TAG, "onResponse: " + response.body());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                log.debug("Function name is updateChalkStatus() " + t.getMessage());

                log.error(TPUtility.getPrintStackTrace(t));
            }
        });
    }

    public static ArrayList<Ticket> searchAllTickets(String plate, WriteTicketActivity activity) {
        ArrayList<Ticket> tickets = null;
        ApiRequest api = ServiceGenerator.createRxService(ApiRequest.class);
        Params params = new Params();
        params.setCustId(TPApplication.getInstance().custId);
        params.setPlate(plate);
        params.setState(activity.stateEditText.getText().toString());
        RequestPOJO requestPOJO = new RequestPOJO();
        requestPOJO.setMethod("searchPlates");
        requestPOJO.setParams(params);

        try {
            Response<TicketsResponse> execute = api.getPlateInfo(requestPOJO).execute();
            if (execute.body() != null && !execute.body().equals("")) {
                tickets = (ArrayList<Ticket>) execute.body().getResult();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (Feature.isFeatureAllowed("park_TrackPlateLookup")) {
                try {
                    MobileNowLog log = new MobileNowLog();
                    log.setCustId(TPApplication.getInstance().custId);
                    log.setDeviceId(TPApplication.getInstance().deviceId);
                    log.setUserId(TPApplication.getInstance().userId);
                    log.setRequestDate(new Date());
                    log.setPlate_number(plate);
                    log.setRequestParams("searchPlates");
                    log.setServiceMode("Request");
                    log.setResponseText(e.getMessage());
                    CSVUtility.writeMobileLogCSV(log);
                    MobileNowLog.insertMobileNowLog(log);
                    ArrayList<MobileNowLog> logs = new ArrayList<>();
                    logs.add(log);
                    WriteTicketNetworkCalls.sendMobileNogLogs(logs);

                } catch (Exception e1) {
                    log.debug("Function name is searchAllTickets() " + e1.getMessage());
                    log.error(TPUtility.getPrintStackTrace(e1));
                }
            }
        }
        return tickets;
    }

    public static ArrayList<Hotlist> searchHotlist(String plate, WriteTicketActivity activity) {
        ArrayList<Hotlist> hotlists = null;
        ApiRequest api = RetrofitServiceGenerator.createRxService(ApiRequest.class);
        Params params = new Params();
        params.setCustId(TPApplication.getInstance().custId);
        params.setPlate(plate);
        params.setState(activity.stateEditText.getText().toString());
        RequestPOJO requestPOJO = new RequestPOJO();
        requestPOJO.setMethod("searchHotlist");
        requestPOJO.setParams(params);
        try {
            Response<HotlistResponse> execute = api.searchHotlist(requestPOJO).execute();
            assert execute.body() != null;
            hotlists = (ArrayList<Hotlist>) execute.body().getResult();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                MobileNowLog log = new MobileNowLog();
                log.setCustId(TPApplication.getInstance().custId);
                log.setDeviceId(TPApplication.getInstance().deviceId);
                log.setUserId(TPApplication.getInstance().userId);
                log.setRequestDate(new Date());
                log.setPlate_number(plate);
                log.setRequestParams("searchHotlist");
                log.setServiceMode("Request");
                log.setResponseText(e.getMessage());
                CSVUtility.writeMobileLogCSV(log);
                MobileNowLog.insertMobileNowLog(log);
                ArrayList<MobileNowLog> logs = new ArrayList<>();
                logs.add(log);
                WriteTicketNetworkCalls.sendMobileNogLogs(logs);

            } catch (Exception e1) {
                log.debug("Function name is searchHotlist() " + e1.getMessage());

                log.error(TPUtility.getPrintStackTrace(e1));
            }
        }


        return hotlists;
    }

    public static ArrayList<Permit> searchAllPermitByPlate(String plate, WriteTicketActivity activity, int hotlist, int tickets) {
        ArrayList<Permit> permits = null;
        ApiRequest api = RetrofitServiceGenerator.createRxService(ApiRequest.class);
        Params params = new Params();
        params.setCustId(TPApplication.getInstance().custId);
        params.setPlate(plate);
        params.setState(activity.stateEditText.getText().toString());
        RequestPOJO requestPOJO = new RequestPOJO();
        requestPOJO.setMethod("searchPermitsByPlate");
        requestPOJO.setParams(params);
        try {
            Response<PermitResponse> execute = api.searchAllPermitByPlate(requestPOJO).execute();
            assert execute.body() != null;
            permits = (ArrayList<Permit>) execute.body().getResult();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                MobileNowLog log = new MobileNowLog();
                log.setCustId(TPApplication.getInstance().custId);
                log.setDeviceId(TPApplication.getInstance().deviceId);
                log.setUserId(TPApplication.getInstance().userId);
                log.setRequestDate(new Date());
                log.setPlate_number(plate);
                log.setRequestParams("searchPermitsByPlate");
                log.setServiceMode("Request");
                log.setResponseText(e.getMessage());
                CSVUtility.writeMobileLogCSV(log);
                MobileNowLog.insertMobileNowLog(log);
                ArrayList<MobileNowLog> logs = new ArrayList<>();
                logs.add(log);
                WriteTicketNetworkCalls.sendMobileNogLogs(logs);
            } catch (Exception e1) {
                log.debug("Function name is searchAllPremitByPlate() " + e1.getMessage());

                log.error(TPUtility.getPrintStackTrace(e1));
            }
        }
        if (Feature.isFeatureAllowed(Feature.PARK_SEARCH_TRACK)) {
            try {
                String permit = "0";
                MobileNowLog log = new MobileNowLog();
                log.setCustId(TPApplication.getInstance().custId);
                log.setDeviceId(TPApplication.getInstance().deviceId);
                log.setUserId(TPApplication.getInstance().userId);
                log.setRequestDate(new Date());
                log.setPlate_number(plate);
                log.setRequestParams("TicketPro");
                log.setServiceMode("Request");
                String plateHistory = String.valueOf(tickets);
                String hotList = String.valueOf(hotlist);
//                assert permits != null;
                if (permits.size()>0) {
                    permit = String.valueOf(permits.size());
                }
                log.setResponseText("PlateHistory: " + plateHistory + ", Hotlists: " + hotList + ", Permits: " + permit);

                CSVUtility.writeMobileLogCSV(log);
                MobileNowLog.insertMobileNowLog(log);

                ArrayList<MobileNowLog> logs = new ArrayList<>();
                logs.add(log);
                WriteTicketNetworkCalls.sendMobileNogLogs(logs);
                /*if (activity.screenBLProcessor instanceof WriteTicketBLProcessor)
                    ((WriteTicketBLProcessor) activity.screenBLProcessor).sendMobileNowLog(logs);*/
            } catch (Exception e) {
                log.debug("Function name is searchAllPrmitByPlate() " + e.getMessage());

                log.error(TPUtility.getPrintStackTrace(e));
            }
        }
        return permits;
    }

    public static void sendMobileNogLogs(ArrayList<MobileNowLog> logs) {
        ApiRequest api = RetrofitServiceGenerator.createRxService(ApiRequest.class);
        Params params = new Params();
        params.setCustId(TPApplication.getInstance().custId);
        params.setMobileNowLogs(logs);
        RequestPOJO requestPOJO = new RequestPOJO();
        requestPOJO.setMethod("updateMobileNowLog");
        requestPOJO.setParams(params);
        api.updateMobileNowLog(requestPOJO).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                Log.i(TAG, "onResponse: Success");
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                log.debug("Function name is sendMobileNowLogs() " + t.getMessage());

                log.error(TPUtility.getPrintStackTrace(t) + "\n MobileNowLog ");
            }
        });
    }

    ///

    public static ArrayList<Ticket> searchAllTicketsByVin(String vin, WriteTicketActivity activity) {
        ArrayList<Ticket> tickets = null;
        ApiRequest api = RetrofitServiceGenerator.createRxService(ApiRequest.class);
        Params params = new Params();
        params.setCustId(TPApplication.getInstance().custId);
        params.setVin(vin);
        params.setState(activity.stateEditText.getText().toString());
        RequestPOJO requestPOJO = new RequestPOJO();
        requestPOJO.setMethod("searchVins");
        requestPOJO.setParams(params);

        try {
            Response<TicketsResponse> execute = api.getPlateInfo(requestPOJO).execute();
            if (execute.body() != null) {
                tickets = (ArrayList<Ticket>) execute.body().getResult();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tickets;
    }

}
