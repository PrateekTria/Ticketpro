package com.ticketpro.model;

import android.content.ContentValues;
import android.os.AsyncTask;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.ticketpro.exception.TPException;
import com.ticketpro.parking.activity.TPApplication;
import com.ticketpro.parking.dao.ParkingDatabase;
import com.ticketpro.util.DateUtil;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import io.reactivex.CompletableObserver;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@Entity(tableName = "ticket_history")
public class TicketHistory {
    @PrimaryKey
    @ColumnInfo(name = "ticket_id")
    private long ticketId;
    @ColumnInfo(name = "ticket_date")
    private Date ticketDate;
    @ColumnInfo(name = "plate")
    private String plate;
    @ColumnInfo(name = "vin")
    private String vin;
    @ColumnInfo(name = "expiration")
    private String expiration;
    @ColumnInfo(name = "state_code")
    private String stateCode;
    @ColumnInfo(name = "make_code")
    private String makeCode;
    @ColumnInfo(name = "body_code")
    private String bodyCode;
    @ColumnInfo(name = "color_code")
    private String colorCode;
    @ColumnInfo(name = "permit")
    private String permit;
    @ColumnInfo(name = "meter")
    private String meter;
    @ColumnInfo(name = "is_void")
    private String isVoid;
    @ColumnInfo(name = "is_chalked")
    private String isChalked;
    @ColumnInfo(name = "is_warn")
    private String isWarn;
    @ColumnInfo(name = "is_driveaway")
    private String isDriveAway;
    @ColumnInfo(name = "violation_code")
    private String violationCode;
    @ColumnInfo(name = "violation")
    private String violation;

    public TicketHistory() {

    }

    public TicketHistory(JSONObject object) throws Exception {

        this.setTicketId(!object.isNull("ticket_id") ? object.getLong("ticket_id") : 0);
        this.setTicketDate(DateUtil.getDateFromSQLString(object.getString("ticket_date")));
        this.setPlate(object.getString("plate"));
        this.setVin(object.getString("vin"));
        this.setExpiration(object.getString("expiration"));
        this.setMeter(object.getString("meter"));
        this.setPermit(object.getString("permit"));
        this.setBodyCode(object.getString("body_code"));
        this.setStateCode(object.getString("state_code"));
        this.setColorCode(object.getString("color_code"));
        this.setMakeCode(object.getString("make_code"));
        this.setIsVoid(object.getString("is_void"));
        this.setIsWarn(object.getString("is_warn"));
        this.setIsDriveAway(object.getString("is_driveaway"));
        this.setIsChalked(object.getString("is_chalked"));
        this.setViolation(object.getString("violation"));
        this.setViolationCode(object.getString("violation_code"));

    }


    public static void removeAllTicketHistory() throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).ticketHistoryDao().removeAllTicketHistory();
    }


    public static void removeTicketHistoryById(long ticketId) throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).ticketHistoryDao().removeTicketHistoryById(ticketId);
    }

    public static Date getMaxTicketDate() throws Exception {
        Date ticketDate;
        ticketDate = ParkingDatabase.getInstance(TPApplication.getInstance()).ticketHistoryDao().getMaxTicketDate();
        return ticketDate;
    }

    public static Date getMinTicketDate() throws Exception {
        Date ticketDate;
        ticketDate = ParkingDatabase.getInstance(TPApplication.getInstance()).ticketHistoryDao().getMinTicketDate();
        return ticketDate;
    }


    public static ArrayList<TicketHistory> getTickets() throws Exception {
        ArrayList<TicketHistory> list;
        list = (ArrayList<TicketHistory>) ParkingDatabase.getInstance(TPApplication.getInstance()).ticketHistoryDao().getTickets();
        return list;
    }

    public static ArrayList<Ticket> searchAllPreviousTicketsByPlate(String state, String plate) throws Exception {
        ArrayList<TicketHistory> list;
        list = (ArrayList<TicketHistory>) ParkingDatabase.getInstance(TPApplication.getInstance()).ticketHistoryDao().searchAllPreviousTicketsByPlate(state, plate);
        ArrayList<Ticket> tickets = new ArrayList<>();
        for (TicketHistory object : list) {
            Ticket ticket = new Ticket();
            ticket.setTicketId(object.getTicketId());
            ticket.setTicketDate(object.getTicketDate());
            ticket.setPlate(object.getPlate());
            ticket.setVin(object.getVin());
            ticket.setExpiration(object.getExpiration());
            ticket.setStateCode(object.getStateCode());
            ticket.setMeter(object.getMeter());
            ticket.setPermit(object.getPermit());
            ticket.setMakeCode(object.getMakeCode());
            ticket.setColorCode(object.getColorCode());
            ticket.setBodyCode(object.getBodyCode());
            ticket.setIsWarn(object.getIsWarn());
            ticket.setIsChalked(object.getIsChalked());
            ticket.setIsVoid(object.getIsVoid());
            ticket.setIsDriveAway(object.getIsDriveAway());
            ticket.setViolation(object.getViolation());
            ticket.setViolationCode(object.getViolationCode());
            tickets.add(ticket);
        }
        return tickets;
    }

    public static Ticket searchPreviousTicketByPlate(String state, String plate) throws TPException {
        TicketHistory object;
        object = ParkingDatabase.getInstance(TPApplication.getInstance()).ticketHistoryDao().searchPreviousTicketByPlate(state, plate);
        Ticket ticket = new Ticket();
        ticket.setTicketId(object.getTicketId());
        ticket.setTicketDate(object.getTicketDate());
        ticket.setPlate(object.getPlate());
        ticket.setVin(object.getVin());
        ticket.setExpiration(object.getExpiration());
        ticket.setStateCode(object.getStateCode());
        ticket.setMeter(object.getMeter());
        ticket.setPermit(object.getPermit());
        ticket.setMakeCode(object.getMakeCode());
        ticket.setColorCode(object.getColorCode());
        ticket.setBodyCode(object.getBodyCode());
        ticket.setIsWarn(object.getIsWarn());
        ticket.setIsChalked(object.getIsChalked());
        ticket.setIsVoid(object.getIsVoid());
        ticket.setIsDriveAway(object.getIsDriveAway());
        ticket.setViolation(object.getViolation());
        ticket.setViolationCode(object.getViolationCode());
        return ticket;
    }

    public static Ticket searchPreviousTicketByVIN(String state, String vin) throws TPException {
        TicketHistory object;
        object = ParkingDatabase.getInstance(TPApplication.getInstance()).ticketHistoryDao().searchPreviousTicketByVIN(state, vin);
        Ticket ticket = new Ticket();
        if (object != null) {
            ticket.setTicketId(object.getTicketId());
            ticket.setTicketDate(object.getTicketDate());
            ticket.setPlate(object.getPlate());
            ticket.setVin(object.getVin());
            ticket.setExpiration(object.getExpiration());
            ticket.setStateCode(object.getStateCode());
            ticket.setMeter(object.getMeter());
            ticket.setPermit(object.getPermit());
            ticket.setMakeCode(object.getMakeCode());
            ticket.setColorCode(object.getColorCode());
            ticket.setBodyCode(object.getBodyCode());
            ticket.setIsWarn(object.getIsWarn());
            ticket.setIsChalked(object.getIsChalked());
            ticket.setIsVoid(object.getIsVoid());
            ticket.setIsDriveAway(object.getIsDriveAway());
            ticket.setViolation(object.getViolation());
            ticket.setViolationCode(object.getViolationCode());
        }
        return ticket;
    }

    public static Ticket searchPreviousTicketByMeter(String meter) throws TPException {
        TicketHistory object;
        object = ParkingDatabase.getInstance(TPApplication.getInstance()).ticketHistoryDao().searchPreviousTicketByMeter(meter);
        Ticket ticket = new Ticket();
        ticket.setTicketId(object.getTicketId());
        ticket.setTicketDate(object.getTicketDate());
        ticket.setPlate(object.getPlate());
        ticket.setVin(object.getVin());
        ticket.setExpiration(object.getExpiration());
        ticket.setStateCode(object.getStateCode());
        ticket.setMeter(object.getMeter());
        ticket.setPermit(object.getPermit());
        ticket.setMakeCode(object.getMakeCode());
        ticket.setColorCode(object.getColorCode());
        ticket.setBodyCode(object.getBodyCode());
        ticket.setIsWarn(object.getIsWarn());
        ticket.setIsChalked(object.getIsChalked());
        ticket.setIsVoid(object.getIsVoid());
        ticket.setIsDriveAway(object.getIsDriveAway());
        ticket.setViolation(object.getViolation());
        ticket.setViolationCode(object.getViolationCode());
        return ticket;
    }

    public static void removeAll() throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).ticketHistoryDao().removeAll();
    }

    public static void removeById(int id) throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).ticketHistoryDao().removeById(id);
    }

    public ContentValues getContentValues() throws Exception {
        ContentValues values = new ContentValues();
        values.put("ticket_id", this.ticketId);
        values.put("ticket_date", DateUtil.getSQLStringFromDate2(this.ticketDate));
        values.put("plate", this.plate);
        values.put("vin", this.vin);
        values.put("expiration", this.expiration);
        values.put("body_code", this.bodyCode);
        values.put("state_code", this.stateCode);
        values.put("color_code", this.colorCode);
        values.put("make_code", this.makeCode);
        values.put("meter", this.meter);
        values.put("permit", this.permit);
        values.put("is_void", this.isVoid);
        values.put("is_warn", this.isWarn);
        values.put("is_chalked", this.isChalked);
        values.put("is_driveaway", this.isDriveAway);
        values.put("violation", this.violation);
        values.put("violation_code", this.violationCode);
        return values;
    }

    public JSONObject getJSONObject() {
        JSONObject values = new JSONObject();
        try {
            values.put("ticket_id", this.ticketId);
            values.put("ticket_date", DateUtil.getSQLStringFromDate2(this.ticketDate));
            values.put("plate", this.plate);
            values.put("vin", this.vin);
            values.put("expiration", this.expiration);
            values.put("body_code", this.bodyCode);
            values.put("state_code", this.stateCode);
            values.put("color_code", this.colorCode);
            values.put("make_code", this.makeCode);
            values.put("meter", this.meter);
            values.put("permit", this.permit);
            values.put("is_void", this.isVoid);
            values.put("is_warn", this.isWarn);
            values.put("is_chalked", this.isChalked);
            values.put("is_driveaway", this.isDriveAway);
            values.put("violation", this.violation);
            values.put("violation_code", this.violationCode);
        } catch (Exception e) {
        }

        return values;
    }

    public long getTicketId() {
        return ticketId;
    }

    public void setTicketId(long ticketId) {
        this.ticketId = ticketId;
    }

    public Date getTicketDate() {
        return ticketDate;
    }

    public void setTicketDate(Date ticketDate) {
        this.ticketDate = ticketDate;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    public String getMakeCode() {
        return makeCode;
    }

    public void setMakeCode(String makeCode) {
        this.makeCode = makeCode;
    }

    public String getBodyCode() {
        return bodyCode;
    }

    public void setBodyCode(String bodyCode) {
        this.bodyCode = bodyCode;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public String getPermit() {
        return permit;
    }

    public void setPermit(String permit) {
        this.permit = permit;
    }

    public String getMeter() {
        return meter;
    }

    public void setMeter(String meter) {
        this.meter = meter;
    }

    public String getIsVoid() {
        return isVoid;
    }

    public void setIsVoid(String isVoid) {
        this.isVoid = isVoid;
    }

    public String getIsChalked() {
        return isChalked;
    }

    public void setIsChalked(String isChalked) {
        this.isChalked = isChalked;
    }

    public String getIsWarn() {
        return isWarn;
    }

    public void setIsWarn(String isWarn) {
        this.isWarn = isWarn;
    }

    public String getIsDriveAway() {
        return isDriveAway;
    }

    public void setIsDriveAway(String isDriveAway) {
        this.isDriveAway = isDriveAway;
    }

    public boolean isChalked() {
        if (this.isChalked != null && this.isChalked.equals("Y"))
            return true;

        return false;
    }

    public boolean isVoided() {
        if (this.isVoid != null && this.isVoid.equals("Y"))
            return true;

        return false;
    }

    public boolean isDriveAway() {
        if (this.isDriveAway != null && this.isDriveAway.equals("Y"))
            return true;

        return false;
    }

    public boolean isWarned() {
        if (this.isWarn != null && this.isWarn.equals("Y"))
            return true;

        return false;
    }

    public String getViolationCode() {
        return violationCode;
    }

    public void setViolationCode(String violationCode) {
        this.violationCode = violationCode;
    }

    public String getViolation() {
        return violation;
    }

    public void setViolation(String violation) {
        this.violation = violation;
    }

    public static void insertTicketHistory(TicketHistory ticketHistory){
        ParkingDatabase.getInstance(TPApplication.getInstance()).ticketHistoryDao()
                .insertTicketHistory(ticketHistory).subscribeOn(Schedulers.io())
                .subscribeWith(new CompletableObserver() {
            @Override
            public void onSubscribe(@NotNull Disposable d) {

            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onError(@NotNull Throwable e) {

            }
        });
    }
}

