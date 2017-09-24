package org.ayo.appsist;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.github.moduth.blockcanary.BlockCanary;
import com.github.moduth.blockcanary.BlockCanaryContext;

import org.ayo.hover.wrapper.BuildConfig;

import java.util.List;

/**
 * Created by Administrator on 2017/2/6 0006.
 */

public class DevAssistant {

    public static Application app;
    private static boolean isInited = false;

    public static void init(Application a){
        app = a;
        if(isInited) return;

        BlockCanary.install(a, new AppContext(a)).start();


        isInited = true;
    }

    private static class AppContext extends BlockCanaryContext {
        private static final String TAG = "AppContext";
        private Context mContext;
        public AppContext(Context c){
            mContext = c;
        }

        @Override
        public String provideQualifier() {
            String qualifier = "";
            try {
                PackageInfo info = mContext.getPackageManager()
                        .getPackageInfo(mContext.getPackageName(), 0);
                qualifier += info.versionCode + "_" + info.versionName + "_YYB";
            } catch (PackageManager.NameNotFoundException e) {
                Log.e(TAG, "provideQualifier exception", e);
            }
            return qualifier;
        }

        @Override
        public String provideUid() {
            return "87224330";
        }

        @Override
        public String provideNetworkType() {
            return "4G";
        }

        @Override
        public int provideMonitorDuration() {
            return 9999;
        }

        @Override
        public int provideBlockThreshold() {
            return 500;
        }

        @Override
        public boolean displayNotification() {
            return BuildConfig.DEBUG;
        }

        @Override
        public List<String> concernPackages() {
            List<String> list = super.provideWhiteList();
            list.add("com.example");
            return list;
        }

        @Override
        public List<String> provideWhiteList() {
            List<String> list = super.provideWhiteList();
            list.add("com.whitelist");
            return list;
        }
    }
}
