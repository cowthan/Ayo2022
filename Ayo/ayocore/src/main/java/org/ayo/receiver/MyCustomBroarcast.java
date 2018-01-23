package org.ayo.receiver;

import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Administrator on 2017/7/9.
 */

public class MyCustomBroarcast {
    public static final String ACTION = "org.ayo.receiver.global";

    public String type;
    public Bundle payload;

    public static boolean isMyCustomBroardcast(Intent intent){
        return ACTION.equals(intent.getAction());
    }

    public static MyCustomBroarcast parse(Intent intent){
        MyCustomBroarcast m = new MyCustomBroarcast();
        if(intent == null) return m;
        m.type = intent.getStringExtra("type");
        m.payload = intent.getBundleExtra("payload");
        return m;
    }

}
