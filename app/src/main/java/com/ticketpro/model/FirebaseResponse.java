package com.ticketpro.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class FirebaseResponse {
    @SerializedName("firebaseUpdate")
    @Expose
    private Boolean firebaseUpdate;
    @SerializedName("databaseUpdate")
    @Expose
    private Boolean databaseUpdate;
    @SerializedName("tokenExpired")
    @Expose
    private Boolean isTokenExpired;



    public Boolean isTokenExpired() {
        return isTokenExpired;
    }

    public void setTokenExpired(Boolean isTokenExpired) {
        this.isTokenExpired = isTokenExpired;
    }

    public Boolean getDatabaseUpdate() {
        return databaseUpdate;
    }

    public void setDatabaseUpdate(Boolean databaseUpdate) {
        this.databaseUpdate = databaseUpdate;
    }

    public Boolean getFirebaseUpdate() {
        return firebaseUpdate;
    }

    public void setFirebaseUpdate(Boolean firebaseUpdate) {
        this.firebaseUpdate = firebaseUpdate;
    }
}


