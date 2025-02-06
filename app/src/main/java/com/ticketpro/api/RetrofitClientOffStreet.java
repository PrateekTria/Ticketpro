package com.ticketpro.api;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientOffStreet {

    private static final String BASE_URL = "https://public.offstreet.io/";  // Still default base URL

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request originalRequest = chain.request();
                            Request requestWithHeader = originalRequest.newBuilder()
                                    .header("x-api-key", "17a877bf-b997-499e-beeb-8b52b8b4c5d5") // Add the token
                                    .build();
                            return chain.proceed(requestWithHeader);
                        }
                    })
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)  // Default base URL
                    .client(client)  // Use the custom OkHttp client with the interceptor
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
