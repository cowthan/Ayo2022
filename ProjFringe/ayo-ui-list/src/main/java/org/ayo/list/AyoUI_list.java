package org.ayo.list;

import android.app.Application;

/**
 * Created by Administrator on 2016/8/21.
 */
public class AyoUI_list {
    public static Application app;

    public static void init(Application app){
        AyoUI_list.app = app;
        LocalDisplay.init(app);
    }

}
