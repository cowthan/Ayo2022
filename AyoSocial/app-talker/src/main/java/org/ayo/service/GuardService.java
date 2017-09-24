package org.ayo.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;

import com.zebdar.tom.R;
import com.zebdar.tom.chat.ChatActivity;
import com.zebdar.tom.chat.MessageCenter;


/**
 * Created by Administrator on 2017/7/11.
 */

public class GuardService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        Intent intent = new Intent(this, ChatActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        mBuilder.setContentTitle("安卓开发辅助app")//设置通知栏标题
                .setContentText("正在运行，你不用担心...") //设置通知栏显示内容
                .setContentIntent(pendingIntent) //设置通知栏点击意图
//	            .setNumber(number) //设置通知集合的数量
                .setTicker("开发辅助") //通知首次出现在通知栏，带上升动画效果的
                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                .setPriority(Notification.PRIORITY_DEFAULT) //设置该通知优先级
                .setAutoCancel(false)//设置这个标志当用户单击面板就可以让通知将自动取消
                .setOngoing(true)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                .setDefaults(Notification.DEFAULT_VIBRATE)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
                //Notification.DEFAULT_ALL  Notification.DEFAULT_SOUND 添加声音 // requires VIBRATE permission
                .setSmallIcon(R.drawable.app_icon);//设置通知小ICON
        startForeground(1, mBuilder.build());
        MessageCenter.getDefault().sendText("全局后台Service已启动！");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }
}
