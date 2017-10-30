package com.example.scandemo5;

import android.app.Application;
import android.content.Context;

import com.example.scandemo5.Exception.CrashHandler;

/**
 * Created by Sam on 2017/8/22.
 */

public class MyApp extends Application {

    private static Context context;
    private static Application application;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        application = this;
        //初始化崩溃处理
//        CrashHandler catchHandler = CrashHandler.getInstance();
//        catchHandler.init(getApplicationContext());
    }

    public static Context getContext() {
        return context;
    }

    public static Application getApplication() {
        return application;
    }
}
