package com.ticketpro.syncbackup.synmodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UploadDebugResponse {

        @SerializedName("result")
        @Expose
        private UploadDebugResponseResult result;
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("jsonrpc")
        @Expose
        private String jsonrpc;

        public UploadDebugResponseResult getResult() {
            return result;
        }

        public void setResult(UploadDebugResponseResult result) {
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
