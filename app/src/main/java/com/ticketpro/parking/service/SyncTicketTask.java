package com.ticketpro.parking.service;

import android.os.AsyncTask;

import com.ticketpro.model.SyncData;
import com.ticketpro.model.Ticket;
import com.ticketpro.model.TicketComment;
import com.ticketpro.model.TicketPicture;
import com.ticketpro.model.TicketViolation;
import com.ticketpro.parking.activity.TPApplication;
import com.ticketpro.util.TPConstant;
import com.ticketpro.util.TPUtility;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class SyncTicketTask extends AsyncTask<Ticket, Void, Boolean> {
    private Ticket ticket;
    private ServiceHandler service;
    private Logger logger = Logger.getLogger(SyncTicketTask.class);

    public SyncTicketTask(ServiceHandler service) {
        this.service = service;
    }

    @Override
    protected Boolean doInBackground(Ticket... params) {
        if (params.length > 0) {
            ticket = params[0];
        }

        if (ticket == null) {
            return false;
        }

        JSONObject ticketJSON = ticket.getJSONObject();
        JSONArray ticketArray = new JSONArray();
        ArrayList<String> uploadImages = new ArrayList<>();
        ArrayList<String> uploadVoiceComments = new ArrayList<>();

        final int custId = TPApplication.getInstance().custId;
        final ArrayList<String> voiceMemos = uploadVoiceComments;
        boolean hasUploaded;
        JSONArray syncTicketResult;

        try {
            long ticketId = ticketJSON.getLong("ticket_id");
            long citationNumber = ticketJSON.getLong("citation_number");

            JSONArray ticketComments = new JSONArray();
            ArrayList<TicketComment> comments = TicketComment.getTicketComments(ticketId, citationNumber);
            for (TicketComment comment : comments) {
                ticketComments.put(comment.getJSONObject());
                if (comment.isVoiceComment()) {
                    uploadVoiceComments.add(comment.getComment());
                }
            }

            ticketJSON.put("ticketComments", ticketComments);

            JSONArray ticketPictures = new JSONArray();
            ArrayList<TicketPicture> pictures = TicketPicture.getTicketPictures(ticketId, citationNumber);
            for (TicketPicture picture : pictures) {
                if (!uploadImages.contains(picture.getImagePath())) {
                    uploadImages.add(picture.getImagePath());
                }

                ticketPictures.put(picture.getJSONObject());
            }

            ticketJSON.put("ticketPictures", ticketPictures);

            JSONArray ticketViolations = new JSONArray();
            ArrayList<TicketViolation> violations = TicketViolation.getTicketViolations(ticketId, citationNumber);
            for (TicketViolation violation : violations) {
                ticketViolations.put(violation.getJSONObject());
            }

            ticketJSON.put("ticketViolations", ticketViolations);

            ticketArray.put(ticketJSON);

            // Upload Pictures
            service.syncUploadImages(uploadImages);

            // Upload Voice Comments
            for (String memo : voiceMemos) {
                hasUploaded = TPUtility.uploadFile(TPUtility.getVoiceMemosFolder() + memo, TPConstant.SERVICE_URL + "/uploadfile", custId);
                if (!hasUploaded) {
                    TPUtility.markPendingVoiceComment(memo);
                }
            }
        } catch (Exception e) {
            logger.error(TPUtility.getPrintStackTrace(e));
            return false;
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean response) {
        if (ticket != null && response.booleanValue()) {
            //ticket.setStatus("S");

            try {
                //DatabaseHelper.getInstance().insertOrReplace(ticket.getContentValues(), "tickets");
                Ticket.insertTicket(ticket,"");
                SyncData.removeSyncData("tickets", String.valueOf(ticket.getTicketId()));
            } catch (Exception e) {
                logger.error(TPUtility.getPrintStackTrace(e));
            }
        }
    }
}