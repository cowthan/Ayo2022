package org.ayo.component.core;

import android.app.Application;


/**
 * Created by Administrator on 2016/9/20.
 */
public class Core {
    public static Application app;

    public static boolean DEBUG = true;

    public static void init(Application app, boolean debug){
        Core.app = app;
        DEBUG = debug;
    }

}
