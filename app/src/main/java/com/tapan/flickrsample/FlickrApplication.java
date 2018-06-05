package com.tapan.flickrsample;

import android.app.Application;

import com.tapan.flickrsample.apis.APIClient;
import com.tapan.flickrsample.apis.APIInterface;

public class FlickrApplication extends Application {

    public static APIInterface mApiService;

    @Override
    public void onCreate() {
        super.onCreate();

        mApiService = APIClient.getClient().create(APIInterface.class);
    }
}
