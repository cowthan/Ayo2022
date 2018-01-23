package org.ayo.ui.sample;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;

import com.ayo.sdk.demo.DemoJobCreator;
import com.evernote.android.job.JobManager;
import com.zebdar.tom.IMCore;
import com.zebdar.tom.ai.AiDispatcher;
import com.zebdar.tom.ai.AiWorker;
import com.zebdar.tom.ai.OnAiCallback;

import org.ayo.AppCore;
import org.ayo.editor.MyEditor;
import org.ayo.fresco.Flesco;
import org.ayo.imagepicker.MyImagePicker;
import org.ayo.log.LogReporter;
import org.ayo.log.Trace;
import org.ayo.notify.AyoUI_notify;
import org.ayo.notify.toaster.Toaster;
import org.ayo.sample.BuildConfig;
import org.ayo.ui.sample.editor.MyEmojiRepo;
import org.ayo.view.AyoViewLib;



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

        //初始化Ayo SDK
        Trace.setLevel(DEBUG ? Trace.LEVEL_VERBOSE : Trace.LEVEL_SILENCE);
        Trace.setLogToFile(false);
        AppCore.getDefault().init(this, DEBUG);
        AppCore.getDefault().setSDRoot("ayo");

        //初始化Ayo SystemBarExtra
        AyoUI_notify.init(app);
        AyoViewLib.init(app);

        //x.Ext.init(this);

        //初始化日志类


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

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


}
