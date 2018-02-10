package org.ayo.imagepicker;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by qiaoliang on 2017/8/20.
 */

public class MyUtils {
    public static int dip2px(float dpValue) {
        final float scale = MyImagePicker.app().getResources().getDisplayMetrics().density;
        return (int)(dpValue * scale + 0.5f);
    }
    /**
     * 获取屏幕宽高
     *
     * @return
     */
    public static int getScreenHeight() {
        DisplayMetrics metric = new DisplayMetrics();
        WindowManager wm = (WindowManager) MyImagePicker.app().getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metric);
        return metric.heightPixels;
    }


    public static int getStatusBarHeight(Context context) {
        final Resources resources = context.getResources();
        final int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        int height;
        if (resourceId > 0)
            height = resources.getDimensionPixelSize(resourceId);
        else
            height = (int) Math.ceil(25 * resources.getDisplayMetrics().density);
        return height;
    }

    public static Uri parse(String url) {
        if (TextUtils.isEmpty(url))
            return Uri.parse("");
        return Uri.parse(url);
    }

    /**
     * @param path
     * @return 0 正常 1 文件路径为空 2文件不存在 3读取文件异常 4GIF图片大小超出范围(20M) 5 超出长度
     */
    public static int canImageUpload(String path) {
        if (TextUtils.isEmpty(path)) {
            return 1;
        }

        File file = new File(path);
        if (file == null || !file.exists())
            return 2;
        if (path.toLowerCase().endsWith("gif")) {
            FileInputStream is = null;
            try {
                is = new FileInputStream(file);
                if (is != null) {
                    if (is.available() > MyImagePicker.UPLOAD_LIMIT_M * 1024 * 1024) {
                        return 4;
                    } else {
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = true;
                        BitmapFactory.decodeFile(file.getAbsolutePath(), options);
                        if (options.outHeight > 1280) {
                            return 5;
                        }
                        return 0;
                    }
                } else {
                    return 3;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return 3;
            } finally {
                if (is != null)
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        } else
            return 0;
    }
}
