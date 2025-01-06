package com.ticketpro.parking.bl;

import com.ticketpro.exception.TPException;
import com.ticketpro.model.MaintenanceLog;
import com.ticketpro.model.Message;
import com.ticketpro.model.MobileNowLog;
import com.ticketpro.model.Ticket;
import com.ticketpro.parking.activity.TPApplication;

import java.util.ArrayList;


public class CommonBLProcessor extends BLProcessorImpl {

    public CommonBLProcessor(TPApplication TPApp) {
        setLogger(CommonBLProcessor.class.getName());
        setTPApplication(TPApp);
    }

    public ArrayList<Ticket> getTickets() throws TPException {
        ArrayList<Ticket> tickets = null;
        try {
            tickets = proxy.getTickets(TPApp.getCurrentUserId(), TPApp.getDeviceId(), TPApp.getUserInfo().getUserType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tickets;
    }

    public ArrayList<MaintenanceLog> getMaintenanceLogs() throws TPException {
        return proxy.getMaintenanceLogs();
    }

    public ArrayList<Message> getMessages() throws TPException {
        return proxy.getMessages();
    }

    public ArrayList<Message> getServerMessages() throws TPException {
        return proxy.getServerMessages();
    }

    public boolean sendEmail(String from, String[] to, String subject, String message) throws TPException {
        return proxy.sendEmail(from, to, subject, message);
    }

    public boolean sendMobileNowLog(ArrayList<MobileNowLog> logs) throws TPException {
        return proxy.sendMobileNowLog(logs);
    }

    public ArrayList<Ticket> getTicketsbyPlate() throws TPException {
        return proxy.getTickets(TPApp.getCurrentUserId(), TPApp.getDeviceId(), TPApp.getUserInfo().getUserType());
    }
}
