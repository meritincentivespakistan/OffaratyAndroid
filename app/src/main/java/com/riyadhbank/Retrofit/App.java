package com.riyadhbank.Retrofit;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;



public class App extends Application {
    private ApiRequestHelper apiRequestHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        doInit();
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

    }

    private void doInit() {
        this.apiRequestHelper = ApiRequestHelper.init(this);
    }

    public synchronized ApiRequestHelper getApiRequestHelper() {
        return apiRequestHelper;
    }

}
