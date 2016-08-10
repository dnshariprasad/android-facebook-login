package com.android_social_apis;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

/**
 * Created by Hari on 8/10/16.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //Facebook init - pwd - 1\
        FacebookSdk.sdkInitialize(getApplicationContext());
    }
}
