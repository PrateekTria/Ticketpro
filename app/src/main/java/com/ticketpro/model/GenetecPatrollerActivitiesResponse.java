package com.ticketpro.model;

public class GenetecPatrollerActivitiesResponse {


    /**
     * result : true
     */

    private ResultBean result;
    /**
     * result : {"result":true}
     * id : 82F85DB43CBF6
     * jsonrpc : 2.0
     */

    private String id;
    private String jsonrpc;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
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

    public static class ResultBean {
        private boolean result;

        public boolean isResult() {
            return result;
        }

        public void setResult(boolean result) {
            this.result = result;
        }
    }
}
