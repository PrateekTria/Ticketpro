package com.ticketpro.parking.api;

import android.util.Log;

import com.ticketpro.api.ApiRequest;
import com.ticketpro.api.ServiceGenerator;
import com.ticketpro.exception.TPException;
import com.ticketpro.model.Params;
import com.ticketpro.model.RepeatOffender;
import com.ticketpro.model.RepeatOffenderResponse;
import com.ticketpro.model.RepeatOffender_Rpc;
import com.ticketpro.model.RequestPOJO;
import com.ticketpro.util.TPUtility;

import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ticketpro.model.RepeatOffender.insertUpdate;
import static com.ticketpro.model.RepeatOffender.updateInsert;
import static com.ticketpro.model.RepeatOffender.updateRepeatOffender;

/**
 * Created by Rohit on 14-08-2020.
 */
public class RepeatOffenderNetworkCalls {

    private static final String TAG = "RepeatOffenderAPICalls";
    private static Logger log = Logger.getLogger("RepeatOffenderNetworkCalls");

    public static void lastRepeatOffenderService(ArrayList<RepeatOffender> repeatOffender) {
        ApiRequest apiRequest = ServiceGenerator.createRxService(ApiRequest.class);
        RequestPOJO requestPOJO = new RequestPOJO();
        requestPOJO.setMethod("lastUpDateRepeatOffenders");
        Params params = new Params();
        params.setRepeatOffenders(repeatOffender);
        requestPOJO.setParams(params);
        apiRequest.lastUpDateRepeatOffenders(requestPOJO).enqueue(new Callback<RepeatOffenderResponse>() {
            @Override
            public void onResponse(Call<RepeatOffenderResponse> call, Response<RepeatOffenderResponse> response) {
                try {
                    assert response.body() != null;
                    for (RepeatOffender repeatOffender : response.body().getResult()) {
                        try {
                            updateRepeatOffender(repeatOffender.getCustId(), repeatOffender.getStateCode(), repeatOffender.getPlate(), repeatOffender.getViolationId(), "S");
                        } catch (TPException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<RepeatOffenderResponse> call, Throwable t) {
                log.debug("Function name is lastRepeatOffenderService() "+t.getMessage());
                log.error(TPUtility.getPrintStackTrace(t));
            }
        });
    }

    public static void getlastRepeatOffenderService(int custId, String currentDate) {
        ApiRequest apiRequest = ServiceGenerator.createRxService(ApiRequest.class);
        RequestPOJO requestPOJO = new RequestPOJO();
        requestPOJO.setMethod("getlastUpDateRepeatOffenders");
        Params params = new Params();
        params.setCustid(custId);
        params.setCreated_date(currentDate);
        requestPOJO.setParams(params);
        apiRequest.getlastRepeatOffenderService(requestPOJO).enqueue(new Callback<RepeatOffenderResponse>() {
            @Override
            public void onResponse(Call<RepeatOffenderResponse> call, Response<RepeatOffenderResponse> response) {
                assert response.body() != null;
                try {
                    for (RepeatOffender repeatOffender : response.body().getResult()) {
                        try {
                            boolean b = RepeatOffender.checkIsDataAlreadyInDBorNot(
                                    repeatOffender.getCustId(),
                                    repeatOffender.getStateCode(),
                                    repeatOffender.getPlate(),
                                    repeatOffender.getViolationId()
                            );
                            if (b) {
                                updateInsert(repeatOffender.getCustId(),
                                        repeatOffender.getStateCode(),
                                        repeatOffender.getPlate(),
                                        repeatOffender.getViolationId());
                                Log.d("TicketPRO", "===================updated============ ");
                            } else {

                                insertUpdate(repeatOffender.getCustId(),
                                        repeatOffender.getPlate(),
                                        repeatOffender.getViolation(),
                                        repeatOffender.getCount(),
                                        repeatOffender.getViolationId(),
                                        repeatOffender.getStateCode(),
                                        repeatOffender.getCreatTime());
                                Log.d("TicketPRO", "===================inserted============ ");
                            }
                        } catch (TPException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<RepeatOffenderResponse> call, Throwable t) {
                log.debug("Function name is getLastRepeatOffenderService() "+t.getMessage());
                log.error("Function Name is getlastRepeatOffenderService() "+TPUtility.getPrintStackTrace(t));

            }
        });
    }

   /* public static void updateRepeatOffendersCount(RepeatOffender repeatOffender) {
        ApiRequest apiRequest = ServiceGenerator.createRxService(ApiRequest.class);
        RequestPOJO requestPOJO = new RequestPOJO();
        requestPOJO.setMethod("updateRepeatOffendersCount");
        Params params = new Params();
        params.setRepeatOffendersobject(repeatOffender);
        requestPOJO.setParams(params);
        apiRequest.updateRepeatOffendersCount(requestPOJO).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    assert response.body() != null;
                    if(response.isSuccessful())
                    {

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("","Function name is updateRepeatOffendersCount() "+t.getMessage());
                log.error(TPUtility.getPrintStackTrace(t));
            }
        });
    }*/
}
