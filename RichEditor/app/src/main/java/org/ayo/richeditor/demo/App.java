package org.ayo.richeditor.demo;

import android.app.Application;

import org.ayo.AppCore;
import org.ayo.component.core.Core;
import org.ayo.editor.MyEditor;
import org.ayo.fresco.Flesco;
import org.ayo.imagepicker.MyImagePicker;
import org.ayo.log.LogReporter;
import org.ayo.log.Trace;
import org.ayo.notify.AyoUI_notify;
import org.ayo.notify.toaster.Toaster;


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

        //core初始化
        Core.init(this, DEBUG);
        Trace.setLevel(DEBUG ? Trace.LEVEL_VERBOSE : Trace.LEVEL_SILENCE);
        Trace.setLogToFile(false);
        AppCore.getDefault().init(this, DEBUG);
        AppCore.getDefault().setSDRoot("ayo");
        Flesco.initFresco(this);
        LogReporter.init(this, "辅助助手");

        //image picker初始化
        AyoUI_notify.init(app);
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


        MyEditor.getDefault().init(App.app, App.DEBUG);
        MyEditor.getDefault().setEmojiRepo(new MyEmojiRepo());

    }
}
