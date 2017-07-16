package com.nbdeg.unityplanner.utils;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by nbdeg on 6/6/2017.
 */

public class SetPersistance extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
