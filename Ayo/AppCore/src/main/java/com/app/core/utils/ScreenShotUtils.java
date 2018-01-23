package com.app.core.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by qiaoliang on 2017/11/19.
 */

public class ScreenShotUtils {
    public interface OnScreenShotCallback {
        void onScreenShotOk(String savedPath, Bitmap bitmap);

        void onScreenShotFail();
    }
    public static void createScreenShot(final Activity activity, final View v, final OnScreenShotCallback callback) {
        //这句必须在主线程
        final Bitmap bmp = loadBitmapFromView(v);
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String savedPath = buildImage(activity, bmp, null);
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            if (TextUtils.isEmpty(savedPath)) {
                                callback.onScreenShotFail();
                            } else {
                                callback.onScreenShotOk(savedPath, bmp);
                            }
                        }
                    }
                });
            }
        }).start();
    }

    public static Bitmap loadBitmapFromView(View v) {
        if (v == null || v.getWidth() == 0 || v.getHeight() == 0) {
            return null;
        }
        if(v instanceof WebView){
            return captureWebView((WebView) v);
        }else{
            Bitmap screenshot;
            screenshot = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(screenshot);
            c.translate(-v.getScrollX(), -v.getScrollY());
            v.draw(c);
            return screenshot;
        }
    }

    public static Bitmap captureWebView(WebView webView) {
        Bitmap bmp;
        float scale = webView.getScale();
        int webViewHeight = (int) (webView.getContentHeight() * scale + 0.5);
        bmp = Bitmap.createBitmap(webView.getWidth(), webViewHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        webView.draw(canvas);
        return bmp;
    }

    public static String buildImage(Context context, Bitmap bitmap, Bitmap foot) {
        Bitmap bmp = mergeBitmap(bitmap, foot);
        return saveBitmap(context, bmp);
    }

    public static Bitmap mergeBitmap(Bitmap bmp, Bitmap foot) {
        if (foot == null) {
            return bmp;
        }
        int bmpWidth = bmp.getWidth();
        int footWidth = foot.getWidth();

        int bmpHeight = bmp.getHeight();
        int footerheight = foot.getHeight();

        // 生成两个图片合并大小的Bitmap
        Bitmap newbmp = Bitmap.createBitmap(bmpWidth, bmpHeight + footerheight,
                Bitmap.Config.ARGB_8888);
        Canvas cv = new Canvas(newbmp);
        cv.drawColor(Color.WHITE);
        cv.drawBitmap(bmp, 0, 0, null);// 在 0，0坐标开始画入bmp
        cv.drawBitmap(foot, 0, bmpHeight, null);// 在 0，bmpHeight
        // +
        // kebiaoheight坐标开始填充课表的Bitmap
        // 就绘制白色的界面填充剩下的未铺满全屏的界面
        if (footWidth < bmpWidth) {
            Bitmap ne = Bitmap.createBitmap(bmpWidth - footWidth, footerheight,
                    Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(ne);
            canvas.drawColor(Color.WHITE);
            cv.drawBitmap(ne, footWidth, bmpHeight, null);
        }
        cv.save(Canvas.ALL_SAVE_FLAG);// 保存
        cv.restore();// 存储
        // 回收
        bmp.recycle();
        foot.recycle();
        return newbmp;
    }
    public static String saveBitmap(Context context, Bitmap bmp) {
        if (bmp == null) {
            return null;
        }
        return saveCompressFile(bmp, "share.jpg", context);
    }


    public static String saveCompressFile(Bitmap bitmap, String fileName, Context context) {
        Bitmap bm = getCompressImage(bitmap);
        String path = getDownloadPath(context) + "/share_image/";
        File dirFile = new File(path);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        File myCaptureFile = new File(path + fileName);
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
            bm.compress(Bitmap.CompressFormat.JPEG, 70, bos);
            bos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                bos.close();
                bm.recycle();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return path + fileName;
    }

    private static Bitmap getCompressImage(Bitmap bitmap) {
        int width = bitmap.getWidth();
        float widthF = bitmap.getWidth();
        int newWidth = 720;
        int height = bitmap.getHeight();
        float newHeight = (720 / widthF) * height;
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = (newHeight) / height;
        if (width <= 720) {
            // 分辨率小于720不处理,原图1:1
            matrix.postScale(1, 1);
        } else {
            matrix.postScale(scaleWidth, scaleHeight);
        }
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        bitmap.recycle();
        return resizedBitmap;
    }

    private static String getDownloadPath(Context context) {
        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
            return context.getFilesDir().getAbsolutePath();
        File file = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        if (file != null)
            return file.getAbsolutePath();
        file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        if (file != null)
            return file.getAbsolutePath();
        return context.getFilesDir().getAbsolutePath();
    }

}
