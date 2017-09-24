package com.worse.more.breaker;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;

import org.ayo.AppCore;
import org.ayo.list.AyoUI_list;
import org.ayo.log.CrashHandler;
import org.ayo.notify.AyoUI_notify;
import org.ayo.view.AyoViewLib;

/**
 */
public class App extends Application{

    public static App app;


    public void onCreate() {
        super.onCreate();
        app = this;

        //初始化AyoSDK
        AppCore.init(this, "car", true, true);
        AppCore.debug = true;
        AyoUI_list.init(this);
        AyoUI_notify.init(this);

        //初始化AyoView
        AyoViewLib.init(this);

        //初始化ImageLoader

        //初始化全局异常处理
        CrashHandler crashHandler = CrashHandler.getInstance();
        Thread.setDefaultUncaughtExceptionHandler(crashHandler);

        //TODO：初始化数据库

        //TODO：初始化Http库和下载相关

        //TODO: 初始化数据统计

        //TODO: 初始化推送

        //TODO：初始化地图

        //TODO：初始化IM相关

        //TODO：初始化视频相关


        //TODO: 其他初始化

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
