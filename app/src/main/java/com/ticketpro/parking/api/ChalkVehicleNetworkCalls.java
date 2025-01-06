package com.ticketpro.parking.api;

import android.util.Log;

import com.google.gson.Gson;
import com.ticketpro.api.ApiRequest;
import com.ticketpro.api.ServiceGenerator;
import com.ticketpro.model.ALPRChalk;
import com.ticketpro.model.ChalkComment;
import com.ticketpro.model.ChalkPicture;
import com.ticketpro.model.ChalkVehicle;
import com.ticketpro.model.DeviceInfo;
import com.ticketpro.model.DutyReport;
import com.ticketpro.model.Params;
import com.ticketpro.model.RequestPOJO;
import com.ticketpro.model.SpecialActivityReport;
import com.ticketpro.model.SyncData;
import com.ticketpro.model.Ticket;
import com.ticketpro.model.TicketComment;
import com.ticketpro.model.TicketPicture;
import com.ticketpro.model.TicketResponse;
import com.ticketpro.model.TicketViolation;
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

import static com.ticketpro.parking.api.WriteTicketNetworkCalls.syncUploadImages;
import static com.ticketpro.parking.api.WriteTicketNetworkCalls.uploadVoiceComments;

/**
 * Created by Rohit on 13-08-2020.
 */
public class ChalkVehicleNetworkCalls {

    private static final String TAG = "ChalkVehicleNetworkCall";
    private static final Logger log = Logger.getLogger("ChalkVehicleNetworkCalls");

    public static void saveChalk(ArrayList<SyncData> syncList) throws Exception {
        ApiRequest api = ServiceGenerator.createRxService(ApiRequest.class);
        RequestPOJO requestPOJO;
        if (syncList != null) {
            Params params = new Params();
            ArrayList<DeviceInfo> deviceInfo = new ArrayList<>();
            deviceInfo.add(TPApplication.getInstance().getDeviceInfo());
            params.setDevices(deviceInfo);
            requestPOJO = new RequestPOJO();
            requestPOJO.setParams(params);
            requestPOJO.setMethod("updateDevices");
            api.syncDevices(requestPOJO).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                    Log.i(TAG, "onResponse: ");
                }

                @Override
                public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                   log.error(TPUtility.getPrintStackTrace(t));
                }
            });

            ArrayList<String> uploadImages = new ArrayList<>();
            ArrayList<String> uploadVoiceComments = new ArrayList<>();
            ArrayList<Ticket> tickets = new ArrayList<>();
            ArrayList<Ticket> updateTickets = new ArrayList<>();
            ArrayList<DutyReport> reports = new ArrayList<>();
            ArrayList<ChalkVehicle> chalks = new ArrayList<>();
            ArrayList<SpecialActivityReport> specialActivityReports = new ArrayList<>();

            for (SyncData syncData : syncList) {
                String table = syncData.getTableName();
                String primaryKey = syncData.getPrimaryKey();
                String opt = syncData.getActivity();
                if (opt.equals("INSERT")) {
                    switch (table) {
                        case TPConstant.TABLE_DUTY_REPORTS: {
                            DutyReport report = DutyReport.getDutyReportByPrimaryKey(primaryKey);
                            if (report != null) {
                                reports.add(report);
                            }
                            break;
                        }
                        case TPConstant.TABLE_TICKETS:
                            Ticket ticket = Ticket.getTicketsByPrimaryId(primaryKey);
                            if (ticket != null) {
                                tickets.add(ticket);
                            }
                            break;
                        case TPConstant.TABLE_CHALKS:
                            ChalkVehicle chalk = ChalkVehicle.getChalkVehicleByPrimaryKey(primaryKey);
                            if (chalk != null) {
                                chalks.add(chalk);
                            }
                            try {
                                ALPRChalk alprChalk = ALPRChalk.getChalkVehicleById(Long.parseLong(primaryKey));
                                if (alprChalk != null) {
                                    ChalkVehicle chalkVehicle = ALPRChalk.convertToChalk(alprChalk);
                                    chalks.add(chalkVehicle);
                                }
                            } catch (NumberFormatException e) {
                                 e.printStackTrace();
                            }
                            break;
                        case TPConstant.TABLE_SPECIAL_ACTIVITY_REPORTS: {
                            SpecialActivityReport report = SpecialActivityReport
                                    .getSpecialActivityReportByPrimaryKey(primaryKey);
                            if (report != null) {
                                specialActivityReports.add(report);
                            }
                            break;
                        }
                    }
                } else if (opt.equals("UPDATE")) {
                    if (table.equals(TPConstant.TABLE_TICKETS)) {
                        Ticket ticket = Ticket.getTicketsByPrimaryId(primaryKey);
                        if (ticket != null) {
                            updateTickets.add(ticket);
                        }
                    }
                }
            }

            for (Ticket ticket : tickets) {
                try {
                    long ticketId = ticket.getTicketId();
                    long citationNumber = ticket.getCitationNumber();
                    ArrayList<TicketComment> comments = TicketComment.getTicketComments(ticketId, citationNumber);
                    for (TicketComment comment : comments) {
                        if (comment.isVoiceComment()) {
                            uploadVoiceComments.add(comment.getComment());
                        }
                    }
                    ticket.setTicketComments(comments);
                    ArrayList<TicketPicture> pictures = TicketPicture.getTicketPictures(ticketId, citationNumber);
                    for (TicketPicture picture : pictures) {
                        if ("Y".equalsIgnoreCase(picture.getLprNotification())) {
                            continue;
                        }
                        uploadImages.add(picture.getImagePath());
                    }
                    ticket.setTicketPictures(pictures);
                    ArrayList<TicketViolation> violations = TicketViolation.getTicketViolations(ticketId, citationNumber);
                    ticket.setTicketViolations(violations);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

          /*  for (ChalkVehicle chalkVehicle : chalks) {
                try {
                    long chalkId = chalkVehicle.getChalkId();
                    ArrayList<ChalkPicture> pictures = ChalkPicture.getChalkPictures(chalkId);
                    for (ChalkPicture picture : pictures) {
                        uploadImages.add(picture.getImagePath());
                    }
                    chalkVehicle.setChalkPictures(pictures);
                    ArrayList<ChalkComment> comments = ChalkComment.getChalkComments(chalkId);
                    chalkVehicle.setComments(comments);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }*/

            //Code by Rohit
            for (ChalkVehicle chalkVehicle : chalks) {
                try {
                    long chalkId = chalkVehicle.getChalkId();
                    ArrayList<ChalkPicture> pictures = ChalkPicture.getChalkPictures(chalkId);
                    for (ChalkPicture picture : pictures) {
                        uploadImages.add(picture.getImagePath());
                        String[] path = picture.getImagePath().split("/");
                        picture.setImagePath(path[path.length - 1]);
                    }
                    chalkVehicle.setChalkPictures(pictures);
                    ArrayList<ChalkComment> comments = ChalkComment.getChalkComments(chalkId);
                    chalkVehicle.setComments(comments);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            syncTickets(tickets);
            syncUpdateTickets(updateTickets);
            //syncChalks(chalks);
            syncUploadImages(uploadImages);
            uploadVoiceComments(uploadVoiceComments);
        }
    }

    public static void syncTickets(ArrayList<Ticket> tickets) {
        ApiRequest api = ServiceGenerator.createRxService(ApiRequest.class);
        RequestPOJO requestPOJO = new RequestPOJO();
        requestPOJO.setMethod("updateTickets");
        Params params = new Params();
        params.setTickets(tickets);
        requestPOJO.setParams(params);

        api.syncTickets(requestPOJO).enqueue(new Callback<TicketResponse>() {
            @Override
            public void onResponse(@NotNull Call<TicketResponse> call, @NotNull Response<TicketResponse> response) {
                try {
                    Log.i(TAG, "onResponse: " + new Gson().toJson(response.body()));
                    for (int i = 0; i < tickets.size(); i++) {
                        String primaryKey = String.valueOf(tickets.get(i).getTicketId());
                        SyncData.removeSyncData(TPConstant.TABLE_TICKETS, primaryKey);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NotNull Call<TicketResponse> call, @NotNull Throwable t) {
                log.error(TPUtility.getPrintStackTrace(t));
            }
        });
    }

    public static void syncUpdateTickets(ArrayList<Ticket> updateTickets) {
        ApiRequest api = ServiceGenerator.createRxService(ApiRequest.class);
        RequestPOJO requestPOJO = new RequestPOJO();
        requestPOJO.setMethod("updateTicketChanges");
        Params params = new Params();
        params.setTickets(updateTickets);
        requestPOJO.setParams(params);

        api.syncTickets(requestPOJO).enqueue(new Callback<TicketResponse>() {
            @Override
            public void onResponse(@NotNull Call<TicketResponse> call, @NotNull Response<TicketResponse> response) {
                try {
                    Log.i(TAG, "onResponse: " + new Gson().toJson(response.body()));
                    for (int i = 0; i < updateTickets.size(); i++) {
                        String primaryKey = String.valueOf(updateTickets.get(i).getTicketId());
                        SyncData.removeSyncData(TPConstant.TABLE_TICKETS, primaryKey);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NotNull Call<TicketResponse> call, @NotNull Throwable t) {
                log.error(TPUtility.getPrintStackTrace(t));
            }
        });
    }

   /* public static void syncChalks(ArrayList<ChalkVehicle> chalks) {
        ApiRequest api = ServiceGenerator.createRxService(ApiRequest.class);
        RequestPOJO requestPOJO = new RequestPOJO();
        requestPOJO.setMethod("updateChalks");
        Params params = new Params();
        params.setChalks(chalks);
        requestPOJO.setParams(params);
        api.syncChalks(requestPOJO).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                Log.i(TAG, "onResponse: " + response.body());
                for (int i = 0; i < chalks.size(); i++) {
                    String primaryKey = String.valueOf(chalks.get(i).getChalkId());
                    SyncData.removeSyncData(TPConstant.TABLE_CHALKS, primaryKey);
                    SyncData.removeSyncData(TPConstant.TABLE_ALPR_CHALK, primaryKey);

                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                log.error(TPUtility.getPrintStackTrace(t));
            }
        });

    }*/
}
