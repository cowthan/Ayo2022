package org.ayo.notify;

import android.app.Application;

/**
 * Created by Administrator on 2016/8/20.
 */
public class AyoUI_notify {

    public static Application app;

    public static void init(Application app){
        AyoUI_notify.app = app;
        LocalDisplay.init(app);
    }

}
