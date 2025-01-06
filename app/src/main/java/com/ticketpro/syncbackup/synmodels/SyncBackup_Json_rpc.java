package com.ticketpro.syncbackup.synmodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ticketpro.syncbackup.synmodels.Params;

public class SyncBackup_Json_rpc {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("method")
        @Expose
        private String method;
        @SerializedName("params")
        @Expose
        private Params params;
        @SerializedName("jsonrpc")
        @Expose
        private String jsonrpc;

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

        public Params getParams() {
            return params;
        }

        public void setParams(Params params) {
            this.params = params;
        }

        public String getJsonrpc() {
            return jsonrpc;
        }

        public void setJsonrpc(String jsonrpc) {
            this.jsonrpc = jsonrpc;
        }

    }

