package com.bolex.timetask.timetask;

import android.app.Application;

/**
 * Created by qiaoliang on 2017/11/14.
 */

public class App extends Application {

    public static Application app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
    }
}
