package org.ayo.social.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.text.TextUtils;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import org.ayo.log.Trace;

import java.io.ByteArrayOutputStream;

/**
 * Created by qiaoliang on 2017/6/20.
 */

public class SocialUtils {

    public static final String REQUEST_TAG = "social-center";
    public final static int THUMB_SIZE = 75;
    public final static String backupImageUrl = "http://img.dongqiudi.com/app/shareicon100x100.png";

    public interface OnDownloadCallback{
        void onFinish(String path);
    }

    public static void getImageFromUrl(final Context context, final String url, final OnDownloadCallback callback){
        //先从frecso的缓存里尝试获取，如果没有，则下载
//        Bitmap bitmap = null;
//        if (AppUtils.isFrescoImageDownloaded(AppUtils.parse(url))) {
//            File file = AppUtils.getFrescoCachedImageOnDisk(AppUtils.parse(url));
//            if (file != null && file.exists()){
//               if(callback != null) callback.onFinish(file.getAbsolutePath());
//            }
//        }
//
//        String path = AppUtils.getPicturePath(context) + "/" + System.currentTimeMillis() + ".jpg";
//        DownloadRequest req = new DownloadRequest(url, path,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        if(callback != null) callback.onFinish(response);
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Trace.e("share-download", "下载失败：" + url + ", 失败理由：" + error.getMessage());
//                if(callback != null) callback.onFinish(null);
//            }
//        });
//
//        HttpTools.getDefault().addToRequestQueue(req, REQUEST_TAG);

        throw new RuntimeException("此方法没实现，没法下载图片！哈哈哈哈！");

    }

    public static Bitmap getBitmapFromPath(String path){
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        return bitmap;
    }

    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }


    public static byte[] compressBitmap(byte[] var0, int var1) {
        boolean var2 = false;
        if (var0 != null && var0.length >= var1) {
            ByteArrayOutputStream var3 = new ByteArrayOutputStream();
            Bitmap var4 = BitmapFactory.decodeByteArray(var0, 0, var0.length);
            int var5 = 1;
            double var6;

            while (true) {
                while (!var2 && var5 <= 10) {
                    var6 = Math.pow(0.8D, (double)var5);
                    int var8 = (int)(100.0D * var6);
                    var4.compress(Bitmap.CompressFormat.JPEG, var8, var3);
                    if (var3 != null && var3.size() < var1) {
                        var2 = true;
                    } else {
                        var3.reset();
                        ++var5;
                    }
                }

                if (var3 != null) {
                    byte[] var9 = var3.toByteArray();
                    if (!var4.isRecycled()) {
                        var4.recycle();
                    }

                    if (var9 != null && var9.length <= 0) {
                        Trace.d("image-comporess", "### 您的原始图片太大,导致缩略图压缩过后还大于32KB,请将分享到微信的图片进行适当缩小.");
                    }

                    return var9;
                }
                break;
            }
        }

        return var0;
    }

    /**
     * 得到bitmap的大小
     */
    public static int getBitmapSize(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {    //API 19
            return bitmap.getAllocationByteCount();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {//API 12
            return bitmap.getByteCount();
        }
        // 在低版本中用一行的字节x高度
        return bitmap.getRowBytes() * bitmap.getHeight();                //earlier version
    }



    /**
     * 注销sinaWeibo登录
     */
    public static void logoutSina(Activity a) {
        CookieSyncManager.createInstance(a);
        CookieSyncManager.getInstance().startSync();
        CookieManager.getInstance().removeAllCookie();
    }

    public static String handleTitle(String title){
        return (!TextUtils.isEmpty(title) && title.length() > 30
                ? title.substring(0, 29)
                : title);
    }

    public static String handleDesc(String desc, String suffix){
        String s = ((!TextUtils.isEmpty(desc) && desc.length() > 50
                ? desc.substring(0, 49)
                : desc))
                + suffix;
        return s;
    }
}
