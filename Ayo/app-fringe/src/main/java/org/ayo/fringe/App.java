package org.ayo.fringe;

import android.app.Application;
import android.app.Notification;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.xiaomi.mipush.sdk.MiPushClient;

import org.ayo.AppCore;
import org.ayo.animate.AyoAnim;
import org.ayo.core.Lang;
import org.ayo.fresco.Flesco;
import org.ayo.http.utils.JsonUtils;
import org.ayo.imagepicker.MyImagePicker;
import org.ayo.list.AyoUI_list;
import org.ayo.log.LogReporter;
import org.ayo.log.Trace;
import org.ayo.notify.AyoUI_notify;
import org.ayo.social.SocialCenter;
import org.ayo.view.AyoViewLib;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashSet;
import java.util.Set;

import cn.jpush.android.api.CustomPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by Administrator on 2016/4/11.
 */
public class App extends Application {

    public static App app;
    public static boolean isInitialed = false;
    public static final boolean DEBUG = BuildConfig.DEBUG;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;

        Trace.setLevel(DEBUG ? Trace.LEVEL_VERBOSE : Trace.LEVEL_SILENCE);
        Trace.setLogToFile(false);
        AppCore.getDefault().init(app, DEBUG);
        AppCore.getDefault().setSDRoot("fringe");

        //初始化AyoView
        AyoAnim.init(this);
        AyoViewLib.init(this);
        AyoUI_list.init(this);
        AyoUI_notify.init(this);

        // 社交中心初始化（登录和分享）
        SocialCenter.getDefault().setAppIdQQ(BuildConfig.QQ_ID);
        SocialCenter.getDefault().setAppIdWx(BuildConfig.WEIXIN_ID);
        SocialCenter.getDefault().setPlatformInfoWx(BuildConfig.WEIXIN_SECRET, BuildConfig.WEIXIN_STATE, BuildConfig.WEIXIN_SCOPE);
        SocialCenter.getDefault().setAppIdWb(BuildConfig.SINA_ID);
        SocialCenter.getDefault().setPlatformInfoWb(BuildConfig.REDIRECTURL, BuildConfig.SINA_SCOPE);

        Trace.e("sign", Lang.app.getSign()); //("com.iwomedia.taitai"));

        ///极光推送
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        Set<String> tags = new HashSet<>();
        tags.add("tag1");
        tags.add("tag_all");
        JPushInterface.setAliasAndTags(this, "special", tags, new TagAliasCallback() {
            @Override
            public void gotResult(int i, String s, Set<String> set) {
                Log.e("jpush", "jpush-registration-id = " + JPushInterface.getRegistrationID(getApplicationContext()));
                Log.e("jpush", "jpush-alias = " + s);
                Log.e("jpush", "jpush-tags = " + JsonUtils.toJson(set));
            }
        });
        //JPushInterface.setPushNotificationBuilder(1, new DefaultPushNotificationBuilder().buildNotification());

        //小米推送
        MiPushClient.registerPush(this, "2882303761517302152", "5181730216152");

        //图片加载库
        Flesco.initFresco(app);
        MyImagePicker.getDefault().init(this, false);

        LogReporter.init(this, "辅助助手");

        EventBus.getDefault().register(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceive(Object e){

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static void initJpush(final Context context) {
        JPushInterface.setDebugMode(true);
        JPushInterface.init(context);
        CustomPushNotificationBuilder builder = new CustomPushNotificationBuilder(context,
                R.layout.notification_light, R.id.icon, R.id.title, R.id.text);
        // 指定最顶层状态栏小图标
        builder.layoutIconDrawable = R.mipmap.ic_launcher;
        builder.statusBarDrawable = R.mipmap.ic_launcher;
        builder.notificationFlags = Notification.FLAG_AUTO_CANCEL | Notification.FLAG_SHOW_LIGHTS; // 设置为自动消失和呼吸灯闪烁
        builder.notificationDefaults =  Notification.DEFAULT_VIBRATE
                | Notification.DEFAULT_LIGHTS; // 设置为铃声、震动、呼吸灯闪烁都要


        JPushInterface.setPushNotificationBuilder(1, builder);

//        if (!TextUtils.isEmpty(JPushInterface.getRegistrationID(context))) {
//            AppService.startRegisterDevice(context, JPushInterface.getRegistrationID(context), 0);
//        }
    }
}
