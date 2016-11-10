package org.db0.targetcontroller;

import android.app.Application;

import io.realm.Realm;

/**
 * @author Timo/QVIK
 * @since 06.11.2016
 */

public class TargetControllerApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(getApplicationContext());
    }
}
