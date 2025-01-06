package com.ticketpro.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
    /**
     * Created by Mohit on 27-03-2023.
     */
    public class SpecialActivityReportResponse {
        @SerializedName("result")
        @Expose
        private List<SpecialActivityReport> result = null;
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("jsonrpc")
        @Expose
        private String jsonrpc;

        public List<SpecialActivityReport> getResult() {
            return result;
        }

        public void setResult(List<SpecialActivityReport> result) {
            this.result = result;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getJsonrpc() {
            return jsonrpc;
        }

        public void setJsonrpc(String jsonrpc) {
            this.jsonrpc = jsonrpc;
        }
    }


