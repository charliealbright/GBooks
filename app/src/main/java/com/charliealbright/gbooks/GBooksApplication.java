package com.charliealbright.gbooks;

import android.app.Application;

import timber.log.Timber;

/**
 * Created by Charlie on 4/4/17.
 */

public class GBooksApplication extends Application {
    @Override public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}
