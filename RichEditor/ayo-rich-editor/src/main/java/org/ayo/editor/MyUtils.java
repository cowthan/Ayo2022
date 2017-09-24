package org.ayo.editor;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by qiaoliang on 2017/8/19.
 */

public class MyUtils {

    public static int dip2px(float dpValue) {
        final float scale = MyEditor.app().getResources().getDisplayMetrics().density;
        return (int)(dpValue * scale + 0.5f);
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
    /**
     * 判断手机ROM是否是MIUI
     *
     * @return
     */
    public static boolean isMIUIRom() {
        String name = Build.BRAND;
        return !TextUtils.isEmpty(name) && name.toLowerCase().equals("xiaomi");
    }

    /**
     * 判断手机ROM是否是MIUI
     *
     * @return
     */
    public static boolean isOppoRom() {
        String name = Build.BRAND;
        return !TextUtils.isEmpty(name) && name.toLowerCase().equals("oppo");
    }

    /**
     * 判断是否是锤子系统
     * @return
     */
    public static boolean isSmartisan() {
        String name = Build.BRAND;
        return !TextUtils.isEmpty(name) && name.equalsIgnoreCase("SMARTISAN");
    }

    /**
     * 判断手机ROM是否是华为
     *
     * @return
     */

    public static boolean isHUAWEIRom(){
        String name = Build.BRAND;
        return !TextUtils.isEmpty(name) && name.toLowerCase().equals("huawei");
    }

    /**
     * 判断手机ROM是否是三星
     *
     * @return
     */
    public static boolean isSamsungRom() {
        String name = Build.BRAND;
        return !TextUtils.isEmpty(name) && name.toLowerCase().equals("samsung");
    }

    /**
     * 判断手机ROM是否是Meizu
     *
     * @return
     */
    public static boolean isMeizuRom() {
        String name = Build.BRAND;
        return !TextUtils.isEmpty(name) && name.toLowerCase().equals("meizu");
    }

    /**
     * 读取图片的旋转的角度
     *
     * @param path 图片绝对路径
     * @return 图片的旋转角度
     */
    public static int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 将图片按照某个角度进行旋转
     *
     * @param bm 需要旋转的图片
     * @param degree 旋转角度
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;

        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }

    /**
     * 判断上传的图片是否超过限制(由服务器控制)
     *
     * @return
     */
    public static boolean getFileSize(List<String> filePaths) {
        if (filePaths == null || filePaths.size() == 0) {
            return true;
        }
        long blockSize = 0;
        for (int i = 0; i < filePaths.size(); i++) {
            File file = new File(filePaths.get(i));
            try {
                if (!file.isDirectory()) {
                    blockSize += getFileSizes(file);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        boolean out = blockSize / 1024f > 20;
        if (out) {
            return false;
        } else
            return true;
    }


    public static long getFileSizes(File f) {// 取得文件大小
        long s = 0;
        try {
            if (f.exists()) {
                FileInputStream fis = null;
                fis = new FileInputStream(f);
                f.createNewFile();
               s = fis.available();
            } else {
                Log.e("cn.teamtone", "文件不存在");
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (s <= 1024) {
            s = 1;
        } else {
            s = s / 1024;
        }
        return s;
    }

    public static int getThumbIdByFileName(Context context, String file) {
        Cursor c = null;
        try {
            c = MediaStore.Images.Media.query(context.getContentResolver(),
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{
                            MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA
                    }, "(" + MediaStore.Images.Media.MIME_TYPE + " in (?, ?, ?)) AND " + MediaStore.Images.Media.DATA + " = ? ", new String[]{
                            "image/jpeg", "image/png", "image/gif", file
                    }, " _id DESC");
            if (c != null && c.moveToFirst()) {
                return c.getInt(0);
            }
        } finally {
            if (c != null)
                c.close();
        }
        return -1;
    }

    /**
     * 通过图片全路径截取图片名称
     *
     * @param filePath
     * @return
     */
    public static String dealFilePathToFileName(String filePath) {
        if (filePath == null || !filePath.contains("/")) {
            return "";
        }
        String[] strs = filePath.split("/");
        return strs[strs.length - 1];
    }

    /**
     * 解决小米手机上6.0获取图片路径返回cursor为null的情况
     *
     * @param intent
     * @return
     */
    public static Uri getUri(Context context, Intent intent) {
        Uri uri = intent.getData();
        String type = intent.getType();
        //修复华为手机(畅玩4X)url为null问题
        if (uri != null && uri.getScheme().equals("file")) {
            String path = uri.getEncodedPath();
            if (path != null) {
                path = Uri.decode(path);
                ContentResolver cr = context.getContentResolver();
                StringBuffer buff = new StringBuffer();
                buff.append("(").append(MediaStore.Images.ImageColumns.DATA).append("=")
                        .append("'" + path + "'").append(")");
                Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{
                        MediaStore.Images.ImageColumns._ID
                }, buff.toString(), null, null);
                try {
                    int index = 0;
                    for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                        index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                        // set _id value
                        index = cur.getInt(index);
                    }
                    if (index == 0) {
                        // todo do nothing
                    } else {
                        Uri uri_temp = Uri.parse("content://media/external/images/media/" + index);
                        if (uri_temp != null) {
                            uri = uri_temp;
                        }
                    }
                } finally {
                    if (cur != null)
                        cur.close();
                }
            }
        }
        return uri;
    }

    /**
     * @param path
     * @return 0 正常 1 文件路径为空 2文件不存在 3读取文件异常 4GIF图片大小超出范围 5 超出长度
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
                    if (is.available() > MyEditor.UPLOAD_LIMIT_M * 1024 * 1024) {
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

    /**
     * 获取视频第一帧作为图片
     *
     * @param path
     * @return
     */
    public static Bitmap getBitmapByVideo(String path) {
        try {
            MediaMetadataRetriever media = new MediaMetadataRetriever();
            media.setDataSource(path);
            Bitmap bitmap = media.getFrameAtTime();
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
