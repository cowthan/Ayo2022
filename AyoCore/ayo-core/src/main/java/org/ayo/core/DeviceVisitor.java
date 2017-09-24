package org.ayo.core;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import org.ayo.core.Lang;

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
import java.util.HashMap;
import java.util.Map;

/**
 * 设备信息搜集，记录日志时用
 */

public class DeviceVisitor{

    private static void append(StringBuilder sb, Map<String, String> m, String key){
        sb.append(key + ": " + Lang.snull(m.get(key), "") + "\n");
    }

    private static String cache = null;

    public static String getDeviceInfoString(Context ctx){
        if(cache != null) return cache;
        Map<String, String> map = getLog(ctx);
        StringBuilder sb = new StringBuilder();

        sb.append("---------------Build------------" + "\n");
        append(sb, map, "设备");
        append(sb, map, "设备序列号");
        append(sb, map, "系统");
        append(sb, map, "出厂时间");

        sb.append("\n");
        append(sb, map, "Build.BOARD");
        append(sb, map, "Build.BOOTLOADER");
        append(sb, map, "设备名   Build.DEVICE");//设备名
        append(sb, map, "显示设备号  Build.DISPLAY");//显示设备号
        append(sb, map, "设备指纹 Build.FINGERPRINT");//设备指纹
        append(sb, map, "Build.HARDWARE");
        append(sb, map, "Build.HOST");
        append(sb, map, "设备硬件id Build.ID");//设备硬件id
        append(sb, map, "厂商 Build.MANUFACTURER");//厂商
        append(sb, map, "产品名，和DEVICE一样 Build.PRODUCT");//产品名，和DEVICE一样
        append(sb, map, "Build.TAGS");
        append(sb, map, "Build.TYPE");
        append(sb, map, "Build.UNKNOWN");
        append(sb, map, "Build.USER");
        append(sb, map, "Build.RADIO");
        append(sb, map, "Build.VERSION.CODENAME");
        append(sb, map, "Build.VERSION.INCREMENTAL");//不详，重要
        sb.append("\n");

        sb.append("---------------CPU------------" + "\n");
        append(sb, map, "CPU名字");
        if(Build.VERSION.SDK_INT >= 21){
            append(sb, map, "CPU_ABI");
            append(sb, map, "CPU_ABI_32");
            append(sb, map, "CPU_ABI_64");
        }else{
            append(sb, map, "CPU_ABI");
            append(sb, map, "CPU_ABI2");
        }
        append(sb, map, "CPU最大频率");
        append(sb, map, "CPU最小频率");
        append(sb, map, "CPU当前频率");
        sb.append("\n");

        sb.append("---------------各种号------------" + "\n");
        append(sb, map, "版本");
        append(sb, map, "IMEI");
        append(sb, map, "IMSI");
        append(sb, map, "手机号码");
        append(sb, map, "运营商");
        sb.append("\n");

        sb.append("---------------screen------------" + "\n");
        append(sb, map, "屏幕分辨率");
        append(sb, map, "density");
        append(sb, map, "densityDpi");
        append(sb, map, "适配");
        sb.append("\n");

        sb.append("---------------net------------" + "\n");
        append(sb, map, "联网类型");
        append(sb, map, "Mac地址");
        append(sb, map, "IP地址");
        append(sb, map, "IP地址-2");
        sb.append("\n");

        cache = sb.toString();
        return cache;
    }


    private static Map<String, String> getLog(Context ctx){
        Map<String, String> sb = new HashMap<>();

        ///Build信息
        ///////sb.put("---------------Build------------" + "\n");
        sb.put("设备", Build.BRAND + " / " + Build.MODEL + "\n");
        sb.put("设备序列号", Build.SERIAL + "\n");
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
        sb.put("系统", Build.VERSION.RELEASE + " / " + Build.VERSION.SDK_INT + " / " + androidName + "\n");//系统版本号
        sb.put("出厂时间", new SimpleDateFormat("yyyy-MM-dd").format(new Date(Build.TIME)) + "\n");//出厂时间

        ///////sb.put("\n");
        sb.put("Build.BOARD", Build.BOARD + "\n");
        sb.put("Build.BOOTLOADER", Build.BOOTLOADER + "\n");
        sb.put("设备名   Build.DEVICE", Build.DEVICE + "\n");//设备名
        sb.put("显示设备号  Build.DISPLAY", Build.DISPLAY + "\n");//显示设备号
        sb.put("设备指纹 Build.FINGERPRINT", Build.FINGERPRINT + "\n");//设备指纹
        sb.put("Build.HARDWARE", Build.HARDWARE + "\n");
        sb.put("Build.HOST", Build.HOST + "\n");
        sb.put("设备硬件id Build.ID", Build.ID + "\n");//设备硬件id
        sb.put("厂商 Build.MANUFACTURER", Build.MANUFACTURER + "\n");//厂商
        sb.put("产品名，和DEVICE一样 Build.PRODUCT", Build.PRODUCT + "\n");//产品名，和DEVICE一样
        sb.put("Build.TAGS", Build.TAGS + "\n");
        sb.put("Build.TYPE", Build.TYPE + "\n");
        sb.put("Build.UNKNOWN", Build.UNKNOWN + "\n");
        sb.put("Build.USER", Build.USER + "\n");
        sb.put("Build.RADIO", Build.RADIO + "\n");
        sb.put("Build.VERSION.CODENAME", Build.VERSION.CODENAME + "\n");
        sb.put("Build.VERSION.INCREMENTAL", Build.VERSION.INCREMENTAL + "\n");//不详，重要
        //////sb.put("\n");

        //////sb.put("---------------CPU------------" + "\n");
        sb.put("CPU名字", getCpuName() + "\n");
        if(Build.VERSION.SDK_INT >= 21){
            sb.put("CPU_ABI", Build.SUPPORTED_ABIS + "\n");
            sb.put("CPU_ABI_32", Build.SUPPORTED_32_BIT_ABIS + "\n");
            sb.put("CPU_ABI_64", Build.SUPPORTED_64_BIT_ABIS + "\n");
        }else{
            sb.put("CPU_ABI", Build.CPU_ABI + "\n");
            sb.put("CPU_ABI2", Build.CPU_ABI2 + "\n");
        }
        sb.put("CPU最大频率", getMaxCpuFreq() + "\n");
        sb.put("CPU最小频率", getMinCpuFreq() + "\n");
        sb.put("CPU当前频率", getCurCpuFreq() + "\n");
        ///////sb.put("\n");

        ///////sb.put("---------------各种号------------" + "\n");
        TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
        String imei = tm.getDeviceId();
        String imsi = tm.getSubscriberId();
        String numer = tm.getLine1Number(); // 手机号码
        String serviceName = tm.getSimOperatorName(); // 运营商
        sb.put("版本", Build.VERSION.RELEASE + "\n");
        sb.put("IMEI", imei + "\n");
        sb.put("IMSI", imsi + "\n");
        sb.put("手机号码", numer + "\n");
        sb.put("运营商", serviceName + "\n");
        ///////sb.put("\n");

        ///////sb.put("---------------screen------------" + "\n");
        //这种方式在service中无法使用，
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        sb.put("屏幕分辨率", dm.widthPixels + "*" +  dm.heightPixels + "\n");
        sb.put("density", dm.density + "\n");
        sb.put("densityDpi", dm.densityDpi + "-" + getDpi(dm.densityDpi) + "\n");
        String s = "";
        s += "- mdpi 160dp 1x\n    LauncherIcon(48), Notification(22 in 24)\n      ActionBar and Tab Icon(24 in 32)\n";
        s += "- hdpi 240dp, 1.5x \n    LauncherIcon(72), Notification(33 in 36)\n      ActionBar and Tab Icon(36 in 48)\n";
        s += "- xhdpi 320dp, 2x \n     LauncherIcon(96), Notification(44 in 48)\n      ActionBar and Tab Icon(48 in 64)\n";
        s += "- xxhdpi 480dp, 3x \n    LauncherIcon(144), Notification(66 in 72)\n     ActionBar and Tab Icon(72 in 96)\n";
        s += "- xxxhdpi 640dp, 4x\n    LauncherIcon(192), Notification(88 in 96)\n     ActionBar and Tab Icon(96 in 128)\n";
        sb.put("适配", s);
        ///////sb.put("\n");

        ///////sb.put("---------------net------------" + "\n");
        sb.put("联网类型", GetNetype(ctx) + "\n");
        sb.put("Mac地址", getMacAddress(ctx) + "\n");

        try{
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        sb.put("IP地址", inetAddress.getHostAddress().toString() + (inetAddress.isLoopbackAddress() ? "----loopback" : "") + "\n");
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

            sb.put("IP地址-2", ip + "\n");
        }catch (Exception e){
            e.printStackTrace();
        }

        ///////sb.put("\n");

        return sb;
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
