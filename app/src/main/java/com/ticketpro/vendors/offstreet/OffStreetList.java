package com.ticketpro.vendors.offstreet;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Expose;


import java.util.List;

/**
 * **********************************************************
 * File: OffStreetList.java
 * Created by: SANJIV
 * Date: 11/4/24
 * Description:
 * **********************************************************
 */

public class OffStreetList {
    @SerializedName("sessions")
    @Expose
    private List<Session> sessions;

    public List<Session> getSessions() {
        return sessions;
    }

    public void setSessions(List<Session> sessions) {
        this.sessions = sessions;
    }

}
