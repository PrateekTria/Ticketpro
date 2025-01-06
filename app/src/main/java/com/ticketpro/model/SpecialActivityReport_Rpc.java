package com.ticketpro.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


    public class SpecialActivityReport_Rpc {
        @SerializedName("jsonrpc")
        @Expose
        private String jsonrpc = "2.0";
        @SerializedName("id")
        @Expose
        private String id = "82F85DB43CBF6";
        @SerializedName("method")
        @Expose
        private String method;
        @SerializedName("params")
        @Expose
        private SPecialActivityReportParams params;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }

        public SPecialActivityReportParams getParams() {
            return params;
        }

        public void setParams(SPecialActivityReportParams params) {
            this.params = params;
        }

        public String getJsonrpc() {
            return jsonrpc;
        }

        public void setJsonrpc(String jsonrpc) {
            this.jsonrpc = jsonrpc;
        }


    }


