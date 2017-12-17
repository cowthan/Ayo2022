package org.ayo.log;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;

import org.ayo.core.Lang;
import org.ayo.http.utils.JsonUtils;

import java.util.Map;

/**
 * Created by Administrator on 2017/7/11.
 */

public final class LogReporter {

    public static String APP_NAME = "app-name";
    private static final String ACTION = "org.ayo.receiver.global";
    private static Application app;

    public static void init(Application app, String appName){
        LogReporter.app = app;
        APP_NAME = appName;
    }

    private static void report(String title, String subtitle, String content, Object extra, int type, boolean needNotification){
        MyCustomBroadcast m = new MyCustomBroadcast();
        m.type = "log";
        m.payload = new Bundle();
        m.payload.putString("title", "【" + APP_NAME + "】" + title);
        m.payload.putString("subTitle", subtitle);
        m.payload.putString("content", content);
        m.payload.putString("extra", extra == null ? "" : JsonUtils.toJson(extra));
        m.payload.putInt("type", type);
        m.payload.putBoolean("needNotification", needNotification);
        m.payload.putString("reportTime", System.currentTimeMillis() + "");
        sendBroadcast(m);
    }

    public static void report(String eventName, String eventId, Object extra){
        report("事件(" + eventName + ")", eventId, "", extra, 3, false);
    }

    public static void report(String title, String subtitle, String content, Object extra){
        report(title, subtitle, content, extra, 3, false);
    }

    public static void report(String title, String subtitle, String content, Object extra, boolean needNotification){
        report(title, subtitle, content, extra, 3, needNotification);
    }


    public static void report(Throwable e){
        report("发生异常", e.getLocalizedMessage(), Lang.readThrowable(e), null, 1, true);
    }

    public static void reportHttpStart(String tag, String method, String url, Map<String, String> headers){
        report("发起请求", method + ", " + url, JsonUtils.toJson(headers), null, 2, false);
    }

    public static void reportHttpEnd(String tag, String method, String url, Map<String, String> headers, Throwable e){
        report("结束请求", method + ", " + url, JsonUtils.toJson(headers) + "<br/><hr/>" + Lang.readThrowable(e), null, 2, true);
    }

    public static void reportHttpEnd(String tag, String method, String url, Map<String, String> headers, String response){
        report("结束请求", method + ", " + url, JsonUtils.toJson(headers) + "<br/><hr/>" + response, null, 2, false);
    }

    private static void sendBroadcast(MyCustomBroadcast m){
        Intent intent = new Intent();
        intent.putExtra("type", m.type);
        intent.putExtra("payload", m.payload);
        intent.setAction(ACTION);
        app.sendBroadcast(intent);
    }

    private static class MyCustomBroadcast {
        public String type;
        public Bundle payload;
    }
}
