package com.worse.more.breaker;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;

import org.ayo.AppCore;
import org.ayo.component.core.Core;
import org.ayo.log.CrashHandler;
import org.ayo.log.Trace;
import org.ayo.notify.AyoUI_notify;
import org.ayo.view.AyoViewLib;

/**
 */
public class App extends Application{

    public static App app;

    public static boolean DEBUG = BuildConfig.DEBUG;

    public void onCreate() {
        super.onCreate();
        app = this;
        Core.init(this, DEBUG);

        //初始化Ayo SDK
        Trace.setLevel(DEBUG ? Trace.LEVEL_VERBOSE : Trace.LEVEL_SILENCE);
        Trace.setLogToFile(false);
        AppCore.getDefault().init(this, DEBUG);
        AppCore.getDefault().setSDRoot("ayo");

        //初始化Ayo SystemBarExtra
        AyoUI_notify.init(app);
        AyoViewLib.init(app);

        //初始化ImageLoader

        //初始化全局异常处理
        CrashHandler crashHandler = CrashHandler.getInstance();
        Thread.setDefaultUncaughtExceptionHandler(crashHandler);

        Intent intent = new Intent();
        intent.setAction("org.ayo.receiver.global");
        app.sendBroadcast(intent);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public void startActivity(Context c, Class<?> klass){

    }

}
