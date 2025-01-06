package com.ticketpro.model;

import java.util.List;

public class CurveSenseZoneItemSelectedList {


    /**
     * space_name : W12_B11-029
     * zone : Assay St
     * zone_id : 5864
     * latitude : 29.9206965
     * longitude : -95.2025657
     * entry_image : https://pems.pemsportal.com/importtran/api/v1/bollard/vioImage/TurboData/84a878c9-830e-4717-a102-76c2f74c7bd4
     * id : 84a878c9-830e-4717-a102-76c2f74c7bd4
     * is_Violated : Y
     * vio_type : No Payment
     * vio_sub_type : null
     * entry_time : 2024-10-16T18:00:54
     * plate_no : 6315l75
     * state : NA
     */

    private List<VioEventsBean> vio_events;

    public List<VioEventsBean> getVio_events() {
        return vio_events;
    }

    public void setVio_events(List<VioEventsBean> vio_events) {
        this.vio_events = vio_events;
    }


}
