package com.ticketpro.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EulaResult {
    @SerializedName("eula")
    @Expose
    private EulaModel eula;
    @SerializedName("EulaAcceptedByCust")
    @Expose
    private String eulaAcceptedByCust;

    public EulaModel getEula() {
        return eula;
    }

    public void setEula(EulaModel eula) {
        this.eula = eula;
    }

    public String getEulaAcceptedByCust() {
        return eulaAcceptedByCust;
    }

    public void setEulaAcceptedByCust(String eulaAcceptedByCust) {
        this.eulaAcceptedByCust = eulaAcceptedByCust;
    }
}
