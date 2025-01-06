package com.ticketpro.parking.api;

import com.ticketpro.api.ApiRequest;
import com.ticketpro.api.ServiceGenerator;
import com.ticketpro.model.Params;
import com.ticketpro.model.RequestPOJO;
import com.ticketpro.model.TicketResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetworkCalls {
    public static boolean checkForExistingRecord(String oldDeviceName, String newDeviceName) {
        ApiRequest api = ServiceGenerator.createRxService(ApiRequest.class);
        Params params = new Params();
        params.setDeviceIName(oldDeviceName);
        params.setDeviceNameNew(newDeviceName);
        RequestPOJO requestPOJO = new RequestPOJO();
        requestPOJO.setMethod("updatedeviceid");
        requestPOJO.setParams(params);
        final boolean[] check = {false};
        api.updatedeviceid(requestPOJO).enqueue(new Callback<TicketResponse>() {
            @Override
            public void onResponse(Call<TicketResponse> call, Response<TicketResponse> response) {
                try {
                    if (response.isSuccessful()&& response.body()!=null){
                        if (response.body().getResult().getResult()!=null) {
                            check[0] = true;
                        }else {
                            check[0] = false;
                        }
                    }else {
                        check[0] = false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<TicketResponse> call, Throwable t) {
                check[0] = false;
            }
        });

       /* try {
            //Response<TicketResponse> execute = api.updatedeviceid(requestPOJO).execute();
            if (execute.body() != null) {
                if (execute.body().getResult().getResult().toString().equals("true")) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        return check[0];
    }

}
