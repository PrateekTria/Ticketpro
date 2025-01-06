package com.ticketpro.syncbackup.synmodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Dbinfo {

    @SerializedName("dbname")
    @Expose
    private String dbname;
    @SerializedName("dbData")
    @Expose
    private String dbData;

    public String getDbname() {
        return dbname;
    }

    public void setDbname(String dbname) {
        this.dbname = dbname;
    }

    public String getDbData() {
        return dbData;
    }

    public void setDbData(String dbData) {
        this.dbData = dbData;
    }
}
