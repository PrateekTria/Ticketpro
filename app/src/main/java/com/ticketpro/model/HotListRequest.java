package com.ticketpro.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HotListRequest {



    @SerializedName("hotlist")
    @Expose
    private HotListParams hotlist;


    public HotListParams getHotList() {
        return hotlist;
    }

    public void setHotList(HotListParams hotlist) {
        this.hotlist = hotlist;
    }
}
