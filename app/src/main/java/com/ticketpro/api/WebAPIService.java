package com.ticketpro.api;

import com.ticketpro.model.EdmundGOVAPI;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by tspl-7 on 27/6/18.
 */

public interface WebAPIService {

    @GET("1GKFK66867J196460*BA?format=json")
    Call<EdmundGOVAPI> getValues();
}
