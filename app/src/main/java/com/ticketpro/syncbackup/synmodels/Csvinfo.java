package com.ticketpro.syncbackup.synmodels;

import com.google.gson.annotations.SerializedName;

public class Csvinfo {


    @SerializedName("csvname")
    String csvname;

    @SerializedName("csvData")
    String csvData;


    public void setCsvname(String csvname) {
        this.csvname = csvname;
    }
    public String getCsvname() {
        return csvname;
    }

    public void setCsvData(String csvData) {
        this.csvData = csvData;
    }
    public String getCsvData() {
        return csvData;
    }

}
