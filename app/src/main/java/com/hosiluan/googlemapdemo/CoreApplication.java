package com.hosiluan.googlemapdemo;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by User on 11/13/2017.
 */

public class CoreApplication extends Application {

    private static CoreApplication sApplication;

    public CoreApplication() {
    }

    public static  CoreApplication getInstance(){
        return sApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//        MultiDex.install(base);
    }

    public void showToast(String message){
        Toast.makeText(sApplication, message, Toast.LENGTH_SHORT).show();
    }

    public void logDebug(String message){
        Log.d("Luan",message);
    }
}
