package com.org.core.demo;

import android.app.Application;

import org.ayo.AppCore;
import org.ayo.component.core.Core;
import org.ayo.fresco.Flesco;
import org.ayo.log.LogReporter;
import org.ayo.log.Trace;

/**
 * Created by Administrator on 2017/9/23.
 */

public class App extends Application {
    public static Application app;

    public static boolean DEBUG = BuildConfig.DEBUG;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;

        Core.init(this, DEBUG);
        Trace.setLevel(DEBUG ? Trace.LEVEL_VERBOSE : Trace.LEVEL_SILENCE);
        Trace.setLogToFile(false);
        AppCore.getDefault().init(this, DEBUG);
        AppCore.getDefault().setSDRoot("ayo");
        Flesco.initFresco(this);
        LogReporter.init(this, "辅助助手");
    }
}
