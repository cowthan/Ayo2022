package org.ayo.ui.sample;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.v4.content.ContextCompat;

import com.ayo.sdk.demo.DemoJobCreator;
import com.evernote.android.job.JobManager;
import com.zebdar.tom.IMCore;
import com.zebdar.tom.ai.AiDispatcher;
import com.zebdar.tom.ai.AiWorker;
import com.zebdar.tom.ai.OnAiCallback;

import org.ayo.AppCore;
import org.ayo.component.core.Core;
import org.ayo.editor.MyEditor;
import org.ayo.fresco.Flesco;
import org.ayo.imagepicker.MyImagePicker;
import org.ayo.log.LogReporter;
import org.ayo.log.Trace;
import org.ayo.notify.AyoUI_notify;
import org.ayo.notify.toaster.Toaster;
import org.ayo.sample.BuildConfig;
import org.ayo.sample.R;
import org.ayo.ui.sample.editor.MyEmojiRepo;
import org.ayo.view.AyoViewLib;

import io.mattcarroll.hover.hoverdemo.Bus;
import io.mattcarroll.hover.hoverdemo.appstate.AppStateTracker;
import io.mattcarroll.hover.hoverdemo.theming.HoverTheme;
import io.mattcarroll.hover.hoverdemo.theming.HoverThemeManager;


/**
 * Created by cowthan on 2016/1/24.
 */
//@ReportsCrashes(
//        formUri = "http://www.backendofyourchoice.com/reportpath"
//)
public class App extends Application{

    public static Application app;

    public static boolean DEBUG = BuildConfig.DEBUG;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        Core.init(this, DEBUG);

        //初始化Ayo SDK
        Trace.setLevel(DEBUG ? Trace.LEVEL_VERBOSE : Trace.LEVEL_SILENCE);
        Trace.setLogToFile(false);
        AppCore.getDefault().init(this, DEBUG);
        AppCore.getDefault().setSDRoot("ayo");
        AyoViewLib.init(this);
//        log.init(true, "ayo-sample_divider");

        //初始化Ayo SystemBarExtra
        AyoUI_notify.init(app);
        AyoViewLib.init(app);

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });

        //x.Ext.init(this);

        //初始化日志类

        //初始化网络图片加载工具类
//        VanGogh.initImageBig(R.mipmap.loading_big);
//        VanGogh.initImageMiddle(R.mipmap.loading_big);
//        VanGogh.initImageSmall(R.mipmap.loading_big);
//        VanGogh.init(this, new ImageLoaderUseUIL(this, AppCore.ROOT));

        //初始化全局异常处理
        setupTheme();
        setupAppStateTracking();

        JobManager.create(this).addJobCreator(new DemoJobCreator());

        IMCore.onCreate(this);

        AiDispatcher.addAiWoker(new AiWorker() {
            @Override
            public boolean match(String input) {
                return "三哥".equals(input);
            }

            @Override
            public void handle(String input, OnAiCallback callback) {
                Intent intent = new Intent(app, MainnActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                app.startActivity(intent);
            }
        });

        Flesco.initFresco(this);
        LogReporter.init(this, "辅助助手");


        MyEditor.getDefault().init(App.app, App.DEBUG);
        MyEditor.getDefault().setEmojiRepo(new MyEmojiRepo());

        MyImagePicker.getDefault().init(App.app, App.DEBUG);
        MyImagePicker.getDefault().setToaster(new MyImagePicker.LocalToaster() {
            @Override
            public void toastShort(String s) {
                Toaster.toastShort(s);
            }

            @Override
            public void toastLong(String s) {
                Toaster.toastLong(s);
            }
        });
    }

    private void setupTheme() {
        HoverTheme defaultTheme = new HoverTheme(
                ContextCompat.getColor(this, R.color.hover_accent),
                ContextCompat.getColor(this, R.color.hover_base));
        HoverThemeManager.init(Bus.getInstance(), defaultTheme);
    }

    private void setupAppStateTracking() {
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

        AppStateTracker.init(this, Bus.getInstance());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (activityManager.getAppTasks().size() > 0) {
                AppStateTracker.getInstance().trackTask(activityManager.getAppTasks().get(0).getTaskInfo());
            }
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


}
