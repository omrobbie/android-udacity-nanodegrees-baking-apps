package com.omrobbie.bakingapp;

import android.app.Application;

import com.omrobbie.bakingapp.util.rest.RestClient;

public class App extends Application {

    private static RestClient restClient;

    @Override
    public void onCreate() {
        super.onCreate();
        restClient = new RestClient();
    }

    public static RestClient getRestClient() {
        return restClient;
    }
}
