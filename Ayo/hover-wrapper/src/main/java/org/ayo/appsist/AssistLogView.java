package org.ayo.appsist;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.telephony.TelephonyManager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.ayo.hover.wrapper.R;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

import io.mattcarroll.hover.Navigator;
import io.mattcarroll.hover.NavigatorContent;
import io.mattcarroll.hover.hoverdemo.introduction.HoverMotion;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * Created by Administrator on 2017/2/4 0004.
 */

public class AssistLogView extends FrameLayout implements NavigatorContent {

    private static final String TAG = "LogcatTextViewDemo";

    public AssistLogView(Context context) {
        super(context);
        init();
    }

    public AssistLogView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AssistLogView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public AssistLogView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }
    private HoverMotion mHoverMotion;
    View iv_refresh;
    TextView tv_info;

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.ass_view_log, this, true);

        mHoverMotion = new HoverMotion();
        tv_info = (TextView) findViewById(R.id.tv_info);
        iv_refresh =  findViewById( R.id.iv_refresh );

        String s = LogReaper.getLog(this.getContext());
        tv_info.setText(s);

        iv_refresh.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String s = LogReaper.getLog(getContext());
                tv_info.setText(s);
            }
        });
    }


    @NonNull
    @Override
    public View getView() {
        return this;
    }

    @Override
    public void onShown(@NonNull Navigator navigator) {
        mHoverMotion.start(iv_refresh);
    }

    @Override
    public void onHidden() {
        mHoverMotion.stop();
    }

    public static class LogReaper{
        public static String getLog(Context ctx){
            StringBuilder sb = new StringBuilder();

            ///Build信息
            sb.append("---------------Build------------" + "\n");
            sb.append("设备: "+Build.BRAND + " / " + Build.MODEL + "\n");
            sb.append("设备序列号: "+Build.SERIAL + "\n");
            String androidName = "";
            if(Build.VERSION.SDK_INT >= 25){
                androidName = "7.0及其以上";
            }else if(Build.VERSION.SDK_INT >= 23 && Build.VERSION.SDK_INT <= 24){
                androidName = "Marshmallow";
            }else if(Build.VERSION.SDK_INT >= 21 && Build.VERSION.SDK_INT <= 22){
                androidName = "Lollipop";
            }else if(Build.VERSION.SDK_INT == 19){
                androidName = "KitKat";
            }else if(Build.VERSION.SDK_INT >= 16 && Build.VERSION.SDK_INT <= 18) {
                androidName = "Jelly Bean";
            }else if(Build.VERSION.SDK_INT == 15){
                androidName = "Ice Cream Sandwich";
            }else if(Build.VERSION.SDK_INT < 15){
                androidName = "too low";
            }
            sb.append("系统: " + Build.VERSION.RELEASE + " / " + Build.VERSION.SDK_INT + " / " + androidName + "\n");//系统版本号
            sb.append("出厂时间: " + new SimpleDateFormat("yyyy-MM-dd").format(new Date(Build.TIME)) + "\n");//出厂时间

            sb.append("\n");
            sb.append("Build.BOARD: " + Build.BOARD + "\n");
            sb.append("Build.BOOTLOADER: "+Build.BOOTLOADER + "\n");
            sb.append("设备名   Build.DEVICE: "+Build.DEVICE + "\n");//设备名
            sb.append("显示设备号  Build.DISPLAY: "+Build.DISPLAY + "\n");//显示设备号
            sb.append("设备指纹 Build.FINGERPRINT: "+Build.FINGERPRINT + "\n");//设备指纹
            sb.append("Build.HARDWARE: "+Build.HARDWARE + "\n");
            sb.append("Build.HOST: "+Build.HOST + "\n");
            sb.append("设备硬件id Build.ID: "+Build.ID + "\n");//设备硬件id
            sb.append("厂商 Build.MANUFACTURER: "+Build.MANUFACTURER + "\n");//厂商
            sb.append("产品名，和DEVICE一样 Build.PRODUCT: "+Build.PRODUCT + "\n");//产品名，和DEVICE一样
            sb.append("Build.TAGS: "+Build.TAGS + "\n");
            sb.append("Build.TYPE: "+Build.TYPE + "\n");
            sb.append("Build.UNKNOWN: "+Build.UNKNOWN + "\n");
            sb.append("Build.USER: "+Build.USER + "\n");
            sb.append("Build.RADIO: "+Build.RADIO + "\n");
            sb.append("Build.VERSION.CODENAME: "+Build.VERSION.CODENAME + "\n");
            sb.append("Build.VERSION.INCREMENTAL: "+Build.VERSION.INCREMENTAL + "\n");//不详，重要
            sb.append("\n");

            sb.append("---------------CPU------------" + "\n");
            sb.append("CPU名字: " + getCpuName() + "\n");
            if(Build.VERSION.SDK_INT >= 21){
                sb.append("CPU_ABI: "+Build.SUPPORTED_ABIS + "\n");
                sb.append("CPU_ABI_32: "+Build.SUPPORTED_32_BIT_ABIS + "\n");
                sb.append("CPU_ABI_64: "+Build.SUPPORTED_64_BIT_ABIS + "\n");
            }else{
                sb.append("CPU_ABI: "+Build.CPU_ABI + "\n");
                sb.append("CPU_ABI2: "+Build.CPU_ABI2 + "\n");
            }
            sb.append("CPU最大频率: " + getMaxCpuFreq() + "\n");
            sb.append("CPU最小频率: " + getMinCpuFreq() + "\n");
            sb.append("CPU当前频率: " + getCurCpuFreq() + "\n");
            sb.append("\n");

            sb.append("---------------各种号------------" + "\n");
            TelephonyManager tm = (TelephonyManager) ctx.getSystemService(TELEPHONY_SERVICE);
            String imei = tm.getDeviceId();
            String imsi = tm.getSubscriberId();
            String numer = tm.getLine1Number(); // 手机号码
            String serviceName = tm.getSimOperatorName(); // 运营商
            sb.append("版本: Android " + android.os.Build.VERSION.RELEASE + "\n");
            sb.append("IMEI: " + imei + "\n");
            sb.append("IMSI: " + imsi + "\n");
            sb.append("手机号码: " + numer + "\n");
            sb.append("运营商: " + serviceName + "\n");
            sb.append("\n");

            sb.append("---------------screen------------" + "\n");
            //这种方式在service中无法使用，
            DisplayMetrics dm = new DisplayMetrics();
            WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
            wm.getDefaultDisplay().getMetrics(dm);
            sb.append("屏幕分辨率: " + dm.widthPixels + "*" +  dm.heightPixels + "\n");
            sb.append("density: " + dm.density + "\n");
            sb.append("densityDpi: " + dm.densityDpi + "-" + getDpi(dm.densityDpi) + "\n");
            sb.append("适配：\n");
            sb.append("- mdpi 160dp 1x\n    LauncherIcon(48), Notification(22 in 24)\n      ActionBar and Tab Icon(24 in 32)\n");
            sb.append("- hdpi 240dp, 1.5x \n    LauncherIcon(72), Notification(33 in 36)\n      ActionBar and Tab Icon(36 in 48)\n");
            sb.append("- xhdpi 320dp, 2x \n     LauncherIcon(96), Notification(44 in 48)\n      ActionBar and Tab Icon(48 in 64)\n");
            sb.append("- xxhdpi 480dp, 3x \n    LauncherIcon(144), Notification(66 in 72)\n     ActionBar and Tab Icon(72 in 96)\n");
            sb.append("- xxxhdpi 640dp, 4x\n    LauncherIcon(192), Notification(88 in 96)\n     ActionBar and Tab Icon(96 in 128)\n");
            sb.append("\n");

            sb.append("---------------net------------" + "\n");
            sb.append("联网类型: " + GetNetype(ctx) + "\n");
            sb.append("Mac地址: " + getMacAddress(ctx) + "\n");

            try{
                for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                    NetworkInterface intf = en.nextElement();
                    for (Enumeration<InetAddress> enumIpAddr = intf
                            .getInetAddresses(); enumIpAddr.hasMoreElements();) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress()) {
                            sb.append("IP地址: " + inetAddress.getHostAddress().toString() + (inetAddress.isLoopbackAddress() ? "----loopback" : "") + "\n");
                        }
                    }
                }
            }catch (SocketException e) {
                // TODO: handle exception
                //Utils.log("WifiPreference IpAddress---error-" + e.toString());
                e.printStackTrace();
            }


            //获取wifi服务
            try {
                WifiManager wifiManager = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
                //判断wifi是否开启
                if (!wifiManager.isWifiEnabled()) {
                    wifiManager.setWifiEnabled(true);
                }
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                int ipAddress = wifiInfo.getIpAddress();
                String ip = intToIp(ipAddress);

                sb.append("IP地址-2: " + ip + "\n");
            }catch (Exception e){
                e.printStackTrace();
            }

            sb.append("\n");

            return sb.toString();
        }

        private static String intToIp(int i) {

            return (i & 0xFF ) + "." +
                    ((i >> 8 ) & 0xFF) + "." +
                    ((i >> 16 ) & 0xFF) + "." +
                    ( i >> 24 & 0xFF) ;
        }

        private static String getDpi(int density){
            if(density == 160) return "mdpi";
            else if(density == 240) return "hdpi";
            else if(density == 320) return "xhdpi";
            else if(density == 480) return "xxhdpi";
            else if(density == 640) return "xxxhdpi";
            return "未知-" + density;
        }

        public static String getMaxCpuFreq() {
            String result = "";
            ProcessBuilder cmd;
            try {
                String[] args = {"/system/bin/cat", "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq"};
                cmd = new ProcessBuilder(args);
                Process process = cmd.start();
                InputStream in = process.getInputStream();
                byte[] re = new byte[24];
                while (in.read(re) != -1) {
                    result = result + new String(re);
                }
                in.close();
            } catch (IOException ex) {
                ex.printStackTrace();
                result = "N/A";
            }
            return result.trim() + "Hz";
        }

        // 获取CPU最小频率（单位KHZ）

        public static String getMinCpuFreq() {
            String result = "";
            ProcessBuilder cmd;
            try {
                String[] args = {"/system/bin/cat",
                        "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_min_freq"};
                cmd = new ProcessBuilder(args);
                Process process = cmd.start();
                InputStream in = process.getInputStream();
                byte[] re = new byte[24];
                while (in.read(re) != -1) {
                    result = result + new String(re);
                }
                in.close();
            } catch (IOException ex) {
                ex.printStackTrace();
                result = "N/A";
            }
            return result.trim() + "Hz";
        }

        // 实时获取CPU当前频率（单位KHZ）

        public static String getCurCpuFreq() {
            String result = "N/A";
            try {
                FileReader fr = new FileReader(
                        "/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq");
                BufferedReader br = new BufferedReader(fr);
                String text = br.readLine();
                result = text.trim() + "Hz";
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        public static String getCpuName() {
            try {
                FileReader fr = new FileReader("/proc/cpuinfo");
                BufferedReader br = new BufferedReader(fr);
                String text = br.readLine();
                String[] array = text.split(":\\s+", 2);
                for (int i = 0; i < array.length; i++) {
                }
                return array[1];
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        //返回值 -1：没有网络  1：WIFI网络  2：wap网络  3：net网络
        public static String GetNetype(Context context)
        {
            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if(networkInfo==null)
            {
                return "没联网";
            }
            int nType = networkInfo.getType();
            if(nType==ConnectivityManager.TYPE_MOBILE)
            {
                return networkInfo.getExtraInfo().toLowerCase();
//                if(networkInfo.getExtraInfo().toLowerCase().equals("cmnet"))
//                {
//                    return "net网络";
//                }
//                else
//                {
//                    return "wap网络";
//                }
            }
            else if(nType==ConnectivityManager.TYPE_WIFI)
            {
                return "wifi";
            }
            return "未知";
        }

        //判断MOBILE网络是否可用
        public boolean isMobileConnected(Context context) {
            if (context != null) {
                ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo mMobileNetworkInfo = mConnectivityManager
                        .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                if (mMobileNetworkInfo != null) {
                    return mMobileNetworkInfo.isAvailable();
                }
            }
            return false;
        }

        //判断WIFI网络是否可用
        public boolean isWifiConnected(Context context) {
            if (context != null) {
                ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo mWiFiNetworkInfo = mConnectivityManager
                        .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                if (mWiFiNetworkInfo != null) {
                    return mWiFiNetworkInfo.isAvailable();
                }
            }
            return false;
        }

        //判断是否有网络连接
        public boolean isNetworkConnected(Context context) {
            if (context != null) {
                ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
                if (mNetworkInfo != null) {
                    return mNetworkInfo.isAvailable();
                }
            }
            return false;
        }

        public static String getMacAddress(Context x){
            String result = "";
            WifiManager wifiManager = (WifiManager) x.getSystemService(Context.WIFI_SERVICE);
            if(wifiManager == null) return "";
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            result = wifiInfo.getMacAddress();
            return result;
        }
    }
}
