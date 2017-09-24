package org.ayo.video;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Administrator on 2017/8/22.
 */

public class VideoCenter {

    private VideoCenter(){}

    private static final class Holder{
        private static final VideoCenter instance = new VideoCenter();
    }

    public static VideoCenter getDefault(){
        return Holder.instance;
    }

    public void post(Object e){

    }


    public void upLoadVideoUr(Context context, String title, String url) {
        // 在准备好的状态下去发送
//        String vdeoStreamUrl = Const.Urls.SERVER_PATH + "/video/callback";
//        StringRequest request = new StringRequest(Request.Method.POST, vdeoStreamUrl,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Trace.d(TAG, "response:" + response);
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//                Trace.e(TAG, "error:" + error);
//            }
//        });
//
//        request.setHeaders(AppUtils.getOAuthMap(context));
//        HashMap<String, String> params = new HashMap<String, String>();
//        params.put("url", title);
//        params.put("stream", url);
//        request.setParams(params);
//        request.addHeader("lang", getString(R.string.lang));
//        request.setShouldCache(false);
//        HttpTools.getDefault().addToRequestQueue(request);
    }



    public void startCheckUrlInvalidThread(final String type, final String src, final String stream) {
//        if (TextUtils.isEmpty(stream))
//            return;
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    HttpURLConnection con = (HttpURLConnection)new URL(stream).openConnection();
//                    con.setInstanceFollowRedirects(false);
//                    con.connect();
//                    con.setConnectTimeout(10 * 1000);
//                    if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
//                        EventBus.getDefault().post(new VideoUrlInvalidCheckEvent(true, type, stream,
//                                src, con.getURL().toString()));
//                        Trace.d(TAG, con.getResponseCode() + "    " + con.getURL().toString());
//                        return;
//                    } else if (con.getResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP) {
//                        String location = con.getHeaderField("Location");
//                        Trace.d(TAG, con.getResponseCode() + "    " + location);
//                        EventBus.getDefault().post(
//                                new VideoUrlInvalidCheckEvent(true, type, stream, src, location));
//
//                        con = (HttpURLConnection)new URL(location).openConnection();
//                        con.setInstanceFollowRedirects(false);
//                        con.connect();
//                        con.setConnectTimeout(10 * 1000);
//                        if (con.getResponseCode() != HttpURLConnection.HTTP_OK
//                                && con.getResponseCode() != HttpURLConnection.HTTP_MOVED_TEMP
//                                && con.getResponseCode() != HttpURLConnection.HTTP_MOVED_PERM) {
//                            EventBus.getDefault()
//                                    .post(new VideoUrlInvalidCheckEvent(false, type, stream, src));
//                        }
//                        Trace.d(TAG, "checkUrlInvalidThread:" + con.getResponseCode() + "    "
//                                + location);
//                        return;
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                EventBus.getDefault().post(new VideoUrlInvalidCheckEvent(false, type, stream, src));
//            }
//        }).start();
    }

    public static void showToast(Context c, String msg){
        Toast.makeText(c, msg, Toast.LENGTH_SHORT).show();
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen",
                "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static long compareCurrent(String time) throws ParseException {
        SimpleDateFormat utcformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        utcformat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date mGmsDate = utcformat.parse(time);
        return mGmsDate.getTime() - System.currentTimeMillis();
    }

    /**
     * 判断网络类型
     *
     * @return 0:wifi 1:流量 2:无网络
     */
    public static int checkNetworkIsWifi(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobNetInfo = connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifiNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if ((wifiNetInfo == null || !wifiNetInfo.isConnected())) {
            if (mobNetInfo != null && mobNetInfo.isConnected()) {
                return 1;
            } else {
                return 2;
            }

        } else {
            return 0;
        }
    }
}
