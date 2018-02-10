package com.app.core.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.util.Log;

import org.ayo.AppCore;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * Created by qiaoliang on 2017/10/30.
 */

public class BitmapUtils {


    public static final class compress{

        public static void compressBitmap(String filePath, File file){
            // 数值越高，图片像素越低
            int inSampleSize = 2;
            BitmapFactory.Options options = new BitmapFactory.Options();
            //采样率
            options.inSampleSize = inSampleSize;
            Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            // 把压缩后的数据存放到baos中
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50 ,baos);
            try {
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(baos.toByteArray());
                fos.flush();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public static void compressImageToFile(String sourceBitmap,File file) {
            // 0-100 100为不压缩
            int options = 50;
            Bitmap bm = BitmapFactory.decodeFile(sourceBitmap);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            // 把压缩后的数据存放到baos中
            bm.compress(Bitmap.CompressFormat.JPEG, options, baos);
            try {
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(baos.toByteArray());
                fos.flush();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        public static void getimage(String srcPath, File outputFile) {

            BitmapFactory.Options newOpts = new BitmapFactory.Options();
            // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
            newOpts.inJustDecodeBounds = true;
            Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空

            newOpts.inJustDecodeBounds = false;
            int w = newOpts.outWidth;
            int h = newOpts.outHeight;
            float hh = 1920f;
            float ww = 1080f;
            // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
            int be = 1;// be=1表示不缩放
            if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
                be = (int)(newOpts.outWidth / ww);
            } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
                be = (int)(newOpts.outHeight / hh);
            }
            if (be <= 0)
                be = 1;
            newOpts.inSampleSize = be;// 设置缩放比例
            // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
            bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
            // 压缩好比例大小后再进行质量压缩
            Bitmap compressImage = compressImage(bitmap, 1000);

            try {
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outputFile));
                compressImage.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                bos.flush();
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public static Bitmap compressImage(Bitmap image, int kbValue) {

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
            int options = 90;

            while (baos.toByteArray().length / 1024 > kbValue) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
                baos.reset(); // 重置baos即清空baos
                image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
                options -= 10;// 每次都减少10
            }
            ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中

            Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
            return bitmap;
        }

        /**
         * 压缩图片：降低图片的质量，像素不会减少
         * @param bmp
         * @param file
         */
        public static void compressImageToFile(Bitmap bmp,File file) {
            // 0-100 100为不压缩
            int options = 50;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            // 把压缩后的数据存放到baos中
            bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
            try {
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(baos.toByteArray());
                fos.flush();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * 通过缩放图片像素来减少图片占用内存大小
         * @param bmp
         * @param file
         */
        public static void compressBitmapToFile2(Bitmap bmp, File file, int ratio){
            // 尺寸压缩倍数,值越大，图片尺寸越小
//            int ratio = 2;
            // 压缩Bitmap到对应尺寸
            Bitmap result = Bitmap.createBitmap(bmp.getWidth() / ratio, bmp.getHeight() / ratio, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(result);
            Rect rect = new Rect(0, 0, bmp.getWidth() / ratio, bmp.getHeight() / ratio);
            canvas.drawBitmap(bmp, null, rect, null);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            // 把压缩后的数据存放到baos中
            result.compress(Bitmap.CompressFormat.JPEG, 100 ,baos);
            try {
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(baos.toByteArray());
                fos.flush();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static File getLocalVideoThumb(String path, int compressPersentage){
        try {
            Bitmap bm = getVideoThumbnail(path);
            File dir = new File(AppCore.ROOT);
            File f = saveBitmap(bm, dir, createRandomFileName("png"), compressPersentage);
            return f;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static double getVideoDuration(String path){
        MediaPlayer player = new MediaPlayer();
        try {
            player.setDataSource(path);  //recordingFilePath（）为音频文件的路径
            player.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        double duration= player.getDuration();//获取音频的时间
        Log.d("ACETEST", "### duration: " + duration);
        player.release();//记得释放资源
        return duration;
    }

    public static Bitmap getVideoThumbnail(String videoPath) {
        MediaMetadataRetriever media = new MediaMetadataRetriever();
        media.setDataSource(videoPath);
        //这个获取的应该是关键帧，如果指定了时间，也是尝试获取指定时间前后的关键帧，会有重复，也没什么意义了吧
        Bitmap bitmap = media.getFrameAtTime();
        return bitmap;
    }
    public static final String TAG = "dd";
    public static File saveBitmap(Bitmap bm, File dir, String fileName, int compressPersentage){
        Log.e(TAG, "保存图片");
        File f = new File(dir, fileName);
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.JPEG, compressPersentage, out);
            out.flush();
            out.close();
            Log.i(TAG, "已经保存");
            return f;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String createRandomFileName(String suffix){
        if(suffix.contains(".")) {
            suffix = suffix.substring(1);
        }
        return System.currentTimeMillis() + "." + suffix;
    }

    /**
     * 通过缩放图片像素来减少图片占用内存大小
     * @param bmp
     * @param ratio 2表示缩小两倍
     */
    public static Bitmap createScaledBitmap(Bitmap bmp, int ratio){
        // 尺寸压缩倍数,值越大，图片尺寸越小
//            int ratio = 2;
        // 压缩Bitmap到对应尺寸
        Bitmap result = Bitmap.createBitmap(bmp.getWidth() / ratio, bmp.getHeight() / ratio, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        Rect rect = new Rect(0, 0, bmp.getWidth() / ratio, bmp.getHeight() / ratio);
        canvas.drawBitmap(bmp, null, rect, null);
        return result;
    }

    public static Bitmap createScaledBitmap(File file, int expectedWidth, int expecteHeight){
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getAbsolutePath(), newOpts);// 此时返回bm为空

        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        float hhhh = expectedWidth;
        float wwww = expecteHeight;
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > wwww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int)(newOpts.outWidth / wwww);
        } else if (w < h && h > hhhh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int)(newOpts.outHeight / hhhh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), newOpts);
        return  bitmap;
    }

    public static Bitmap createScaledBitmap(int resId, int expectedWidth, int expecteHeight){
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(AppCore.app().getResources(), resId, newOpts);

        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        float hhhh = expectedWidth;
        float wwww = expecteHeight;
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > wwww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int)(newOpts.outWidth / wwww);
        } else if (w < h && h > hhhh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int)(newOpts.outHeight / hhhh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        newOpts.inJustDecodeBounds = false;
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        Bitmap bitmap = BitmapFactory.decodeResource(AppCore.app().getResources(), resId, newOpts);
        return  bitmap;
    }

}
