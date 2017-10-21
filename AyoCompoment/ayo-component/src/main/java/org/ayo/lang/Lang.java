
package org.ayo.lang;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Created by qiaoliang on 2017/4/8. 常用工具类，减少业务代码里的null判断，getContext, 强转等
 */

public class Lang {

    private static Application app;
    private static boolean DEBUG = false;

    public static void init(Application a, boolean isDebug){
        app = a;
        DEBUG = isDebug;
    }

    public static Application app() {
        return app;
    }

    // -----------------------------------------------------
    // 判空系列
    // -----------------------------------------------------
    public static boolean isEmpty(Collection<?> c) {
        return c == null || c.isEmpty();
    }

    public static boolean isNotEmpty(Collection<?> c) {
        return !isEmpty(c);
    }

    public static boolean isEmpty(Map<?, ?> c) {
        return c == null || c.isEmpty();
    }

    public static boolean isNotEmpty(Map<?, ?> c) {
        return !isEmpty(c);
    }

    public static <T> boolean isEmpty(T[] c) {
        return c == null || c.length == 0;
    }

    public static <T> boolean isNotEmpty(T[] c) {
        return !isEmpty(c);
    }

    public static boolean isEmpty(String c) {
        return c == null || c.isEmpty();
    }

    public static boolean isNotEmpty(String c) {
        return !isEmpty(c);
    }

    public static boolean isNull(Object o) {
        return o == null;
    }

    public static boolean isNotNull(Object o) {
        return o != null;
    }

    // -----------------------------------------------------
    // 相等系列
    // -----------------------------------------------------
    public static <T> boolean isEquals(T o1, T o2) {
        if (o1 == null || o2 == null)
            return false;
        return o1.equals(o2);
    }

    public static boolean isEqualsIgnoreCase(String s1, String s2) {
        if (s1 == null || s2 == null) {
            return false;
        } else {
            return s1.equalsIgnoreCase(s2);
        }
    }

    public static <T> boolean isNotEquals(T o1, T o2) {
        if (o1 == null || o2 == null)
            return true;
        return !o1.equals(o2);
    }

    public static boolean isNotEqualsIgnoreCase(String s1, String s2) {
        if (s1 == null || s2 == null) {
            return true;
        } else {
            return !s1.equalsIgnoreCase(s2);
        }
    }

    // -----------------------------------------------------
    // 字符串系列
    // -----------------------------------------------------
    public static String snull(Object maybeNull, String replaceNull) {
        return maybeNull == null ? replaceNull : maybeNull.toString();
    }

    public static String snull(Object maybeNull) {
        return maybeNull == null ? "" : maybeNull.toString();
    }

    public static String toString(Object obj) {
        return obj == null ? "" : obj.toString();
    }

    // -----------------------------------------------------
    // 集合数组长度系列
    // -----------------------------------------------------
    public static int count(Collection<?> c) {
        return c == null ? 0 : c.size();
    }

    public static int count(Map<?, ?> c) {
        return c == null ? 0 : c.size();
    }

    public static <T> int count(T[] c) {
        return c == null ? 0 : c.length;
    }

    public static <T> int count(String s) {
        return s == null ? 0 : s.length();
    }

    // -----------------------------------------------------
    // 资源系列
    // -----------------------------------------------------
    public static String rstring(int id) {
        return app().getResources().getString(id);
    }

    public static String rstring(int id, Object... formatArgs) {
        return app().getResources().getString(id, formatArgs);
    }

    public static int rcolor(int id) {
        return app().getResources().getColor(id);
    }

    public static float rdimen(int id) {
        return app().getResources().getDimension(id);
    }

    // -----------------------------------------------------
    // 类型转换系列
    // -----------------------------------------------------
    public static int toInt(String strInt) {
        try {
            return Integer.parseInt(strInt);
        } catch (Exception e) {
            return 0;
        }
    }

    public static int toInt(String strInt, int defaultValue) {
        try {
            return Integer.parseInt(strInt);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static long toLong(String strInt) {
        try {
            return Long.parseLong(strInt);
        } catch (Exception e) {
            return 0;
        }
    }

    public static long toLong(String strInt, int defaultValue) {
        try {
            return Long.parseLong(strInt);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static double toDouble(String strInt) {
        try {
            return Double.parseDouble(strInt);
        } catch (Exception e) {
            return 0;
        }
    }

    public static double toDouble(String strInt, int defaultValue) {
        try {
            return Double.parseDouble(strInt);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static float toFloat(String str) {
        try {
            return Float.parseFloat(str);
        } catch (Exception e) {
            return 0;
        }
    }

    public static float toFloat(String str, int defaultValue) {
        try {
            return Float.parseFloat(str);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    // -----------------------------------------------------
    // 日期转换系列
    // -----------------------------------------------------
    public static String toDate(String pattern, String seconds) {
        return toDate(pattern, toInt(seconds));
    }

    public static String toDate(String pattern, long seconds) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.getDefault());
        return sdf.format(new Date(seconds * 1000));
    }

    public static String toDate(String pattern, Date date) {
        if (date == null)
            return "";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.getDefault());
        return sdf.format(date);
    }

    /**
     * 尝试把str的时间格式转为format的格式，通过new Date(str)来转，只能尽量转，失败则返回原来字符串
     * 
     * @param str 类似Tue May 31 17:46:55 +0800 2011的字符串
     * @param format
     */
    public static String tryToDate(String str, String format) {
        try {
            Date date = new Date(str);
            DateFormat df = new SimpleDateFormat(format);
            String s = df.format(date);
            return s;
        } catch (Exception e) {
            e.printStackTrace();
            return str;
        }
    }

    // -----------------------------------------------------
    // Bundle取值系列
    // -----------------------------------------------------
    public static String rstring(Intent intent, String key, String defaultValue) {
        return (intent == null || intent.hasExtra(key)) ? intent.getStringExtra(key) : defaultValue;
    }

    public static String rstring(Bundle intent, String key, String defaultValue) {
        return (intent == null || intent.containsKey(key)) ? intent.getString(key) : defaultValue;
    }

    public static String rstring(Intent intent, String key) {
        return (intent == null || intent.hasExtra(key)) ? intent.getStringExtra(key) : "";
    }

    public static String rstring(Bundle intent, String key) {
        return (intent == null || intent.containsKey(key)) ? intent.getString(key) : "";
    }

    public static int rint(Intent intent, String key, int defaultValue) {
        return (intent == null || intent.hasExtra(key)) ? intent.getIntExtra(key, defaultValue)
                : defaultValue;
    }

    public static int rint(Bundle intent, String key, int defaultValue) {
        return (intent == null || intent.containsKey(key)) ? intent.getInt(key) : defaultValue;
    }

    public static int rint(Intent intent, String key) {
        return (intent == null || intent.hasExtra(key)) ? intent.getIntExtra(key, 0) : 0;
    }

    public static int rint(Bundle intent, String key) {
        return (intent == null || intent.containsKey(key)) ? intent.getInt(key) : 0;
    }

    public static boolean rbool(Intent intent, String key, boolean defaultValue) {
        return (intent == null || intent.hasExtra(key)) ? intent.getBooleanExtra(key, defaultValue)
                : defaultValue;
    }

    public static boolean rbool(Bundle intent, String key, boolean defaultValue) {
        return (intent == null || intent.containsKey(key)) ? intent.getBoolean(key) : defaultValue;
    }

    public static boolean rbool(Intent intent, String key) {
        return (intent == null || intent.hasExtra(key)) ? intent.getBooleanExtra(key, false)
                : false;
    }

    public static boolean rbool(Bundle intent, String key) {
        return (intent == null || intent.containsKey(key)) ? intent.getBoolean(key) : false;
    }

    public static <T> T rparsable(Intent intent, String key) {
        return (intent == null || intent.hasExtra(key)) ? (T)intent.getParcelableExtra(key) : null;
    }

    public static <T> T rparsable(Bundle intent, String key) {
        return (intent == null || intent.containsKey(key)) ? (T)intent.getParcelable(key) : null;
    }

    public static <T> T rserializable(Intent intent, String key) {
        return (intent == null || intent.hasExtra(key)) ? (T)intent.getSerializableExtra(key)
                : null;
    }

    public static <T> T rserializable(Bundle intent, String key) {
        return (intent == null || intent.containsKey(key)) ? (T)intent.getSerializable(key) : null;
    }

    // -----------------------------------------------------
    // 集合系列
    // -----------------------------------------------------
    public static <T> ArrayList<T> newArrayList(T... t) {
        ArrayList<T> list = new ArrayList<T>();
        if (t == null || t.length == 0) {
            return list;
        } else {
            for (int i = 0; i < t.length; i++) {
                list.add(t[i]);
            }
        }
        return list;
    }

    public static <K, V> Map<K, V> newHashMap(Object... args) {
        Map<K, V> m = new HashMap<K, V>();

        if (args != null && args.length > 0) {
            for (int i = 0; i < args.length; i += 2) {
                int ki = i;
                int vi = i + 1;
                if (ki < args.length && vi < args.length) {
                    K k = (K)args[ki];
                    V v = (V)args[vi];
                    m.put(k, v);
                }
            }
        }
        return m;
    }

    public static <K> Set<K> newHashSet(K... args) {
        Set<K> set = new HashSet<>();

        if (args != null && args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                set.add(args[i]);
            }
        }
        return set;
    }

    public static <T> T lastElement(List<T> list) {
        if (list == null || list.size() == 0)
            return null;
        return list.get(list.size() - 1);
    }

    public static <T> T firstElement(List<T> list) {
        if (list == null || list.size() == 0)
            return null;
        return list.get(0);
    }

    public static <T> T elementAt(List<T> list, int index, T defaultValue) {
        if (list == null || list.size() == 0)
            return defaultValue;
        if (index < 0 || index >= list.size())
            return defaultValue;
        return list.get(index);
    }

    public static <T> T elementAt(List<T> list, int index) {
        if (list == null || list.size() == 0)
            return null;
        if (index < 0 || index >= list.size())
            return null;
        return list.get(index);
    }

    public interface OnWalk<T> {

        /**
         * @param index current index
         * @param t current element
         * @param total list.size()
         */
        boolean process(int index, T t, int total);

    }

    public interface Func<IN, OUT> {
        OUT process(IN ele);
    }

    public static <OUT, IN> List<OUT> cast(List<IN> list, Func<IN, OUT> func) {
        if (Lang.isEmpty(list))
            return new ArrayList<>();
        List<OUT> res = new ArrayList<>();
        for (IN ele : list) {
            res.add(func.process(ele));
        }
        return res;
    }

    public static <T> void each(Collection<T> c, OnWalk<T> callback) {
        if (callback == null)
            return;
        if (Lang.isNotEmpty(c)) {
            int count = 0;
            for (T o : c) {
                callback.process(count, o, c.size());
                count++;
            }
        }
    }

    public static <K, V> void each(Map<K, V> c, OnWalk<Map.Entry<K, V>> callback) {
        if (callback == null)
            return;
        if (c == null || c.size() == 0)
            return;

        int count = 0;
        for (Map.Entry<K, V> entry : c.entrySet()) {
            callback.process(count, entry, c.size());
            count++;
        }
    }

    public static <T> Collection<T> combine(Collection<T> c1, Collection<T> c2) {
        if (c1 == null && c2 == null)
            return null;
        if (c1 == null)
            return c2;
        if (c2 == null)
            return c1;
        c1.addAll(c2);
        return c1;
    }

    public static <T> boolean contains(T[] array, T ele) {
        if (null == array || array.length == 0)
            return false;
        if (ele == null)
            return false;
        for (T e : array) {
            if (isEquals(e, ele))
                return true;
        }
        return false;
    }

    public static <T> boolean contains(List<T> array, T ele) {
        if (null == array || array.size() == 0)
            return false;
        if (ele == null)
            return false;
        return array.contains(ele);
    }

    public static <T> boolean contains(Set<T> array, T ele) {
        if (null == array || array.size() == 0)
            return false;
        if (ele == null)
            return false;
        return array.contains(ele);
    }

    public static <K, V> boolean containsKey(Map<K, V> map, K key) {
        if (null == map || map.size() == 0)
            return false;
        if (key == null)
            return false;
        return map.containsKey(key);
    }

    public static <K, V> boolean containsValue(Map<K, V> map, V value) {
        if (null == map || map.size() == 0)
            return false;
        if (value == null)
            return false;
        return map.containsValue(value);
    }

    // -----------------------------------------------------
    // View系列
    // -----------------------------------------------------
    public static <T extends View> T findViewById(View v, int id) {
        return v == null ? null : (T)v.findViewById(id);
    }

    public static <T extends View> T findViewById(Activity v, int id) {
        return v == null ? null : (T)v.findViewById(id);
    }

    public static void setText(TextView tv, String text) {
        if (tv != null) {
            if (!TextUtils.isEmpty(text)) {
                tv.setText(text);
            } else {
                tv.setText("");
            }
        }
    }

    public static void setHtml(TextView tv, String html) {
        if (tv != null) {
            if (!TextUtils.isEmpty(html)) {
                if (Build.VERSION.SDK_INT >= 24) {
                    tv.setText(Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT));
                } else {
                    tv.setText(Html.fromHtml(html));
                }
            } else {
                tv.setText("");
            }
        }
    }

    // -----------------------------------------------------
    // 适配系列
    // -----------------------------------------------------
    private static int statusBarHeight = 0;

    private static int screenWidth = 0;

    private static int screenHeight = 0;

    private static void init(Context context) {
        statusBarHeight = getStatusBarHeight(context);
        screenWidth = getScreenWidth(
                (WindowManager)context.getSystemService(Context.WINDOW_SERVICE));
        screenHeight = getScreenHeight(
                (WindowManager)context.getSystemService(Context.WINDOW_SERVICE));
    }

    private static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen",
                "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private static int getScreenHeight(WindowManager manager) {
        DisplayMetrics metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);
        return metrics.heightPixels;
    }

    private static int getScreenWidth(WindowManager manager) {
        DisplayMetrics metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }

    public static int screenWidth() {
        if (screenWidth == 0) {
            init(app());
        }
        return screenWidth;
    }

    public static int screenHeight() {
        if (screenWidth == 0) {
            init(app());
        }
        return screenHeight;
    }

    public static int statusBarHeight() {
        if (screenWidth == 0) {
            init(app());
        }
        return statusBarHeight;
    }

    public static float dip2px(float dpValue) {
        final float scale = app().getResources().getDisplayMetrics().density;
        return (dpValue * scale + 0.5f);
    }

    public static float px2dip(float pxValue) {
        final float scale = app().getResources().getDisplayMetrics().density;
        return (pxValue / scale + 0.5f) - 15;
    }

    public static float sp2px(float value) {
        DisplayMetrics metrics = app().getResources().getDisplayMetrics();
        return value * metrics.scaledDensity;
    }

    public static float px2sp(float value) {
        DisplayMetrics metrics = app().getResources().getDisplayMetrics();
        return value / metrics.scaledDensity;
    }

    public static final int WRAP = ViewGroup.LayoutParams.WRAP_CONTENT;

    public static final int MATCH = ViewGroup.LayoutParams.MATCH_PARENT;

    public static ViewGroup.MarginLayoutParams layoutParam(int w, int h) {
        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(w, h);
        return lp;
    }

    public static ViewGroup.MarginLayoutParams changeLayoutParams(View v, int w, int h) {
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams)v.getLayoutParams();
        if (lp == null) {
            lp = layoutParam(w, h);
        } else {
            lp.width = w;
            lp.height = h;
        }
        return lp;
    }

    public static void ensureSizeRatioAccordingToRealWidth(View v, int standardW, int standardH, int realW){
        int rh = (int) (standardH * realW * 1.0 / standardW);
        changeLayoutParams(v, realW, rh);
    }

    public static void ensureSizeRatioAccordingToRealHeight(View v, int standardW, int standardH, int realH){
        int rw = (int) (standardW * realH * 1.0 / standardH);
        changeLayoutParams(v, rw, realH);
    }

    // -----------------------------------------------------
    // String创建系列
    // -----------------------------------------------------
    public static String fromStream(InputStream inputStream) {
        String jsonStr = "";
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        boolean len = false;

        try {
            int len1;
            while ((len1 = inputStream.read(buffer, 0, buffer.length)) != -1) {
                out.write(buffer, 0, len1);
            }

            jsonStr = new String(out.toByteArray());
        } catch (IOException var6) {
            var6.printStackTrace();
        }

        return jsonStr;
    }

    public static <T> String fromList(List<T> list, String delemeter, boolean ignoreNull) {
        if (Lang.isEmpty(list)) {
            return "";
        } else {
            String res = "";

            for (int i = 0; i < list.size(); ++i) {
                if (!ignoreNull || list.get(i) != null) {
                    res = res + Lang.snull(list.get(i)) + (i == list.size() - 1 ? "" : delemeter);
                }
            }

            return res;
        }
    }

    public interface StringConverter<T> {
        String convert(T t);
    }

    public static <T> String fromList(List<T> list, String delemeter, boolean ignoreNull,
            StringConverter<T> converter) {
        if (Lang.isEmpty(list)) {
            return "";
        } else {
            String res = "";

            for (int i = 0; i < list.size(); ++i) {
                if (!ignoreNull || list.get(i) != null) {
                    res = res + Lang.snull(converter.convert(list.get(i)))
                            + (i == list.size() - 1 ? "" : delemeter);
                }
            }

            return res;
        }
    }

    public static <T> String fromArray(T[] list, String delemeter, boolean ignoreNull) {
        if (Lang.isEmpty(list)) {
            return "";
        } else {
            String res = "";

            for (int i = 0; i < list.length; ++i) {
                if (!ignoreNull || list[i] != null) {
                    res = res + Lang.snull(list[i]) + (i == list.length - 1 ? "" : delemeter);
                }
            }

            return res;
        }
    }

    public static <T> String fromSet(Set<T> set, String delemeter, boolean ignoreNull) {
        if (Lang.isEmpty(set)) {
            return "";
        } else {
            String res = "";
            int i = 0;
            Iterator var5 = set.iterator();

            while (true) {
                while (var5.hasNext()) {
                    Object str = var5.next();
                    if (ignoreNull && str == null) {
                        ++i;
                    } else {
                        res = res + Lang.snull(str) + (i == set.size() - 1 ? "" : delemeter);
                        ++i;
                    }
                }

                return res;
            }
        }
    }

    public static String[] split(String s, String delemeter) {
        return s == null ? null : s.split(delemeter);
    }

    public static List<String> split(String s, int elementLength) {
        ArrayList list = new ArrayList();
        if (s == null) {
            return list;
        } else {
            if (s.length() <= elementLength) {
                list.add(s);
            } else {
                int start = 0;

                while (start < s.length()) {
                    int end = start + elementLength;
                    if (end > s.length()) {
                        end = s.length();
                    }

                    String element = s.substring(start, end);
                    start = end;
                    list.add(element);
                }
            }

            return list;
        }
    }

    // -----------------------------------------------------
    // path及uri系列
    // -----------------------------------------------------
    public static String pathToUri(String localPath) {
        if (Lang.isEmpty(localPath))
            return "";
        if (!localPath.startsWith("/"))
            return localPath;
        return "file://" + localPath;
    }

    // -----------------------------------------------------
    // 异步系列
    // -----------------------------------------------------
    private static class SafeHandler<T> extends Handler{
        private WeakReference<T> mContainer;

        private SafeHandler(T container){
            super(Looper.getMainLooper());
            mContainer = new WeakReference<T>(container);
        }

        public static <T> SafeHandler<T> bind(T t){
            return new SafeHandler<>(t);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }

        public void postTask(final Runnable r, long delayMillis){
            this.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(mContainer != null && mContainer.get() != null){
                        r.run();
                    }
                }
            }, delayMillis);
        }
    }

    public static void post(Activity a, Runnable task, long delayMillis) {
         SafeHandler.bind(a).postTask(task, delayMillis);
    }

    public static void post(Fragment a, Runnable task, long delayMillis) {
        SafeHandler.bind(a).postTask(task, delayMillis);
    }

    /** fragment退出调用onDestroy不是很及时 取消请求滞后 所以得加个判断 */
    public static boolean isFragmentStillRunning(Fragment f){
        return f != null && f.getActivity() != null && !f.isDetached();
    }

    // -----------------------------------------------------
    // 日志系列
    // -----------------------------------------------------
    public static void log(String tag, String msg) {
        if (DEBUG) {
            Log.e(tag, msg);
        }
    }

    public static void log(String msg) {
        log("debug-log", msg);
    }

    // -----------------------------------------------------
    // form表单validate系列
    // -----------------------------------------------------



    // -----------------------------------------------------
    // 版本compact系列
    // -----------------------------------------------------
    @TargetApi(11)
    public static void enableStrictMode() {
        if (Lang.hasGingerbread()) {
            StrictMode.ThreadPolicy.Builder threadPolicyBuilder = new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog();
            StrictMode.VmPolicy.Builder vmPolicyBuilder = new StrictMode.VmPolicy.Builder().detectAll().penaltyLog();

            if (Lang.hasHoneycomb()) {
                threadPolicyBuilder.penaltyFlashScreen();
            }
            StrictMode.setThreadPolicy(threadPolicyBuilder.build());
            StrictMode.setVmPolicy(vmPolicyBuilder.build());
        }
    }

    public static boolean hasFroyo() {
        // Can use static final constants like FROYO, declared in later versions
        // of the OS since they are inlined at compile time. This is guaranteed
        // behavior.
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }

    public static boolean hasGingerbread() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }

    public static boolean hasGingerbreadMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD_MR1;
    }

    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    public static boolean hasHoneycombMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
    }

    public static boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    public static boolean hasJellyBeanMR2() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2;
    }

    public static boolean hasKitKat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    // -----------------------------------------------------
    // 辅助工具
    // -----------------------------------------------------
    public static void printIntent(Intent intent){
        if(intent == null){
            Lang.log("intent", "null");
        }else{
            ///打印data，category等
            Lang.log("intent", "getAction() = " + intent.getAction());
            Lang.log("intent", "getDataString() = " + intent.getDataString());
            Lang.log("intent", "getData() = " + intent.getData());
            Lang.log("intent", "getPackage() = " + intent.getPackage());
            Lang.log("intent", "getScheme() = " + intent.getScheme());
            Lang.log("intent", "getType() = " + intent.getType());
            Lang.log("intent", "getScheme() = " + intent.getScheme());

            ///打印category
            Lang.log("intent", "=====category=====");
            Set<String> categories = intent.getCategories();
            if(categories != null){
                for(String s: categories){
                    Lang.log("intent", s);
                }
            }

            Lang.log("intent", "==========");
            ///打印参数
            Lang.log("intent", "=====参数们=====");
            if(intent.getExtras() != null){
                Set<String> keySet = intent.getExtras().keySet();
                if(keySet != null){
                    for(String key: keySet){
                        Lang.log("intent", key + " ==> " + intent.getExtras().get(key));
                    }
                }
            }
            Lang.log("intent", "==========");
        }
    }
}
