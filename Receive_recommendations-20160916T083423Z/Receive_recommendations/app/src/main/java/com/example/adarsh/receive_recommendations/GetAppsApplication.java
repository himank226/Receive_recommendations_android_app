package com.example.adarsh.receive_recommendations;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

/**
 * The GetApps application
 */
public class GetAppsApplication extends Application {

    private AppEventsLogger mAppEventsLogger;

    @Override

    public void onCreate() {
        super.onCreate();

        FacebookSdk.sdkInitialize(this);
        mAppEventsLogger = AppEventsLogger.newLogger(this);
    }

    /**
     * Returns the {@link AppEventsLogger} for the application9
     *
     * @return the logger for the application
     */
    public AppEventsLogger getLogger() {
        return mAppEventsLogger;
    }
}
