package org.ayo.component.sample;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import org.ayo.AppCore;
import org.ayo.fresco.Flesco;
import org.ayo.log.CrashHandler;
import org.ayo.log.LogReporter;
import org.ayo.log.Trace;
import org.ayo.sample.menu.notify.ToasterDebug;

/**
 * Created by cowthan on 2016/1/24.
 */
public class App extends Application{

    public static Application app;
    public static boolean DEBUG = BuildConfig.DEBUG;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        ToasterDebug.init(this);
        LogReporter.init(this, "AyoCore示例");
        Trace.setLevel(DEBUG ? Trace.LEVEL_VERBOSE : Trace.LEVEL_SILENCE);
        Trace.setLogToFile(false);
        AppCore.getDefault().init(this, DEBUG);
        AppCore.getDefault().setSDRoot("ayo-core-sample");
        CrashHandler crashHandler = CrashHandler.getInstance();
        Thread.setDefaultUncaughtExceptionHandler(crashHandler);
        Flesco.initFresco(this);

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                Log.e("activity---栈", activity.getClass().getSimpleName() + " onCreate(savedInstanceState = " + (savedInstanceState == null ? "null" : "有值") + ")");
            }

            @Override
            public void onActivityStarted(Activity activity) {
                Log.e("activity---栈", activity.getClass().getSimpleName() + " onStart");
            }

            @Override
            public void onActivityResumed(Activity activity) {
                Log.e("activity---栈", activity.getClass().getSimpleName() + " onResume");
            }

            @Override
            public void onActivityPaused(Activity activity) {
                Log.e("activity---栈", activity.getClass().getSimpleName() + " onPause");
            }

            @Override
            public void onActivityStopped(Activity activity) {
                Log.e("activity---栈", activity.getClass().getSimpleName() + " onStop");
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                Log.e("activity---栈", activity.getClass().getSimpleName() + " onSaveInstanceState");
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                Log.e("activity---栈", activity.getClass().getSimpleName() + " onDestroy");
            }

        });

    }


}
