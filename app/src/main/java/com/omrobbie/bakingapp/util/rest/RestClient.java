package com.omrobbie.bakingapp.util.rest;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient {

    private APIService apiService;

    public RestClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .client(new OkHttpClient())
                .baseUrl("http://go.udacity.com/android-baking-app-json/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(APIService.class);
    }

    public APIService getService() {
        return apiService;
    }
}
