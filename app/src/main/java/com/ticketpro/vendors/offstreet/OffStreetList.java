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
    private List<OffStreet> sessions;

    public List<OffStreet> getSessions() {
        return sessions;
    }

    public void setSessions(List<OffStreet> sessions) {
        this.sessions = sessions;
    }

}
