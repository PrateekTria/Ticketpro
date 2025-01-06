package com.ticketpro.syncbackup.synmodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Debuginfo {
    @SerializedName("debugfilename")
    @Expose
    private String debugfilename;
    @SerializedName("debugFileData")
    @Expose
    private String debugFileData;

    public String getDebugfilename() {
        return debugfilename;
    }

    public void setDebugfilename(String debugfilename) {
        this.debugfilename = debugfilename;
    }

    public String getDebugFileData() {
        return debugFileData;
    }

    public void setDebugFileData(String debugFileData) {
        this.debugFileData = debugFileData;
    }
}
