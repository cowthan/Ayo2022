package com.bolex.timetask.timetask;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by qiaoliang on 2017/11/14.
 */

public class AlarmCenter {

    private static final String ACTION = "alarm_center_global";

    AlarmManager am;
    private Map<String, PendingIntent> pendingIntentMap = new HashMap<>();

    private static final class Holder{
        private static final AlarmCenter instance = new AlarmCenter();
    }

    public static AlarmCenter getDefault(){
        return Holder.instance;
    }

    private AlarmCenter(){
        am = (AlarmManager) App.app.getSystemService(Context.ALARM_SERVICE);
        AlarmReceiver receiver = new AlarmReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION);
        App.app.registerReceiver(receiver, filter);
    }

    public void alarm(String name, long futureTimeMillis){
        if(pendingIntentMap.containsKey(name)){
            throw new RuntimeException("名字重复-" + name);
        }
        int requestCode = 0;
        Intent intent = new Intent();
        intent.setAction(ACTION);
        intent.putExtra("name", name);
        PendingIntent mPendingIntent = PendingIntent.getBroadcast(App.app, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        pendingIntentMap.put(name, mPendingIntent);

        am.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureTimeMillis, mPendingIntent);
    }

    public void alarmRepeat(String name, long futureTimeMillis, long intervalMillis){
        if(pendingIntentMap.containsKey(name)){
            throw new RuntimeException("名字重复-" + name);
        }
        int requestCode = 0;
        Intent intent = new Intent();
        intent.setAction(ACTION);
        intent.putExtra("name", name);
        PendingIntent mPendingIntent = PendingIntent.getBroadcast(App.app, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        pendingIntentMap.put(name, mPendingIntent);

        am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureTimeMillis, intervalMillis, mPendingIntent);
    }

    public void cancelAlarm(String name){
        if(pendingIntentMap.containsKey(name)){
            am.cancel(pendingIntentMap.get(name));
            pendingIntentMap.remove(name);
        }
    }

    public class AlarmReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ACTION)) {
                Log.d("AlarmReceiver", "log log log");
                String name = intent.getStringExtra("name");
                if(TextUtils.isEmpty(name)){
                    Log.d("AlarmReceiver", "收到未标注name的Alarm，无法继续下一步");
                }else{
                    notifyCallbacks(name);
                }
            }
        }
    }


    ///============回调注册，注销和通知
    public interface OnAlarmCallback {
        void onAlarm(String name);
    }

    private SparseArray<OnAlarmCallback> callbacks = new SparseArray<>();

    public void addCallback(Object tag, OnAlarmCallback callback) {
        int key = tag.hashCode();
        if (callbacks.get(key) == null) {
            callbacks.put(key, callback);
        }
    }

    public void removeCallbackByTag(Object tag) {
        int key = tag.hashCode();
        if (callbacks.get(key) != null) {
            callbacks.remove(key);
        }
    }

    private void notifyCallbacks(String name){
        for (int i = 0; i < callbacks.size(); i++) {
            callbacks.get(callbacks.keyAt(i)).onAlarm(name);
        }
    }

}
