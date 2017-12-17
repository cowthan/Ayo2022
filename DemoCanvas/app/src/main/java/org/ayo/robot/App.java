package org.ayo.robot;

import android.app.Application;

import org.ayo.Ayo;
import org.ayo.sample.menu.notify.ToasterDebug;

/**
 * Created by cowthan on 2016/1/24.
 */
public class App extends Application{

    public static Application app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        ToasterDebug.init(this);
        Ayo.init(this, "robot", true, true);
        Ayo.debug = true;
    }
}
