
package com.ticketpro.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GeocodeLocation {

    /*@SerializedName("plus_code")
    @Expose
    private PlusCode plusCode;*/
    @SerializedName("results")
    @Expose
    private List<GeocodeResult> results = null;
    @SerializedName("status")
    @Expose
    private String status;

    /*public PlusCode getPlusCode() {
        return plusCode;
    }

    public void setPlusCode(PlusCode plusCode) {
        this.plusCode = plusCode;
    }*/

    public List<GeocodeResult> getResults() {
        return results;
    }

    public void setResults(List<GeocodeResult> results) {
        this.results = results;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
