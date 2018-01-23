package com.app.core.utils;


import com.app.core.prompt.Toaster;

import org.ayo.AppCore;
import org.ayo.log.Trace;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by qiaoliang on 2017/10/29.
 */

public class ImagePicker {

    private final int PHOTO = 1;

    private final int ALBUM = 2;

    private final int CROP_PHOTO = 3;

    public interface ImagePickerCallback{
        void onFinish(String path);
    }

    private boolean needCrop = true;

    private ImagePickerCallback callback;

    private String outputFileName;
    private String outputFilePath;

    private String cropOutputFilePath;

    public static ImagePicker create(boolean needCrop, ImagePickerCallback callback){
        ImagePicker imagePicker = new ImagePicker();
        imagePicker.callback = callback;
        imagePicker.needCrop = needCrop;
        return imagePicker;
    }

    private ImagePicker(){}

    public void openCamera(Activity activity){
//        function = new TakePhoto(PHOTO);

        String fileDir = getPicturePath(activity) + "avatar/";
        outputFileName = Long.toString(System.currentTimeMillis()) + ".jpg";
        outputFilePath = fileDir + outputFileName;

        if (!new File(fileDir).exists()) {
            new File(fileDir).mkdirs();
        }

        File destination = new File(outputFilePath);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(destination));
        activity.startActivityForResult(intent, PHOTO);
    }

    public void openAlbum(Activity activity){
        String fileDir = getPicturePath(AppCore.app()) + "avatar/";
        outputFileName = Long.toString(System.currentTimeMillis()) + ".jpg";
        outputFilePath = fileDir + outputFileName;
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/jpeg");
        activity.startActivityForResult(intent, ALBUM);
    }


    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        if(requestCode == PHOTO){
            if (new File(outputFilePath).exists()) {

                if(needCrop){
                    File outputFile = new File(getPicturePath(AppCore.app()) + "avatar/thumb_" + outputFileName);
                    cropOutputFilePath = outputFile.getAbsolutePath();
                    Intent intent = getCropImageIntent(
                            Uri.fromFile(new File(outputFilePath)),
                            Uri.fromFile(outputFile));
                    if (isIntentAvailable(activity, intent)) {
                        activity.startActivityForResult(intent, CROP_PHOTO);
                    } else {
                        Toaster.toastShort("相册不可用");
                    }
                }else{
                    callback.onFinish(outputFilePath);
                }


            }
        }else if(requestCode == ALBUM){
            if (data != null) {
                ContentResolver resolver = AppCore.app().getContentResolver();
                // 照片的原始资源地址
                Uri originalUri = getUri(AppCore.app(), data);
                if (originalUri != null) {
                    String[] imgs = {
                            MediaStore.Images.Media.DATA
                    };// 将图片URI转换成存储路径
                    Cursor cursor = resolver.query(originalUri, imgs, null, null, null);
                    if (cursor != null) {
                        cursor.moveToFirst();
                        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        cursor.moveToFirst();
                        if (cursor.getString(index) != null) {
                            copyfile(cursor.getString(index), outputFilePath,
                                    false);

                            if(needCrop){
                                File outputFile = new File(getPicturePath(AppCore.app()) + "avatar/thumb_" + outputFileName);
                                cropOutputFilePath = outputFile.getAbsolutePath();
                                Intent albuIntent = getCropImageIntent(
                                        Uri.fromFile(new File(outputFilePath)),
                                        Uri.fromFile(outputFile));
                                activity.startActivityForResult(albuIntent, CROP_PHOTO);
                            }else{
                                callback.onFinish(outputFilePath);
                            }

                        } else {
                            Toaster.toastShort("无权限访问选定的图片");
                        }
                    } else {
                        Toaster.toastShort("无权限访问选定的图片");
                    }
                }
            }
        }else if(requestCode == CROP_PHOTO){
            String path = cropOutputFilePath;
            callback.onFinish(path);
        }
    }

    private Intent getCropImageIntent(Uri fromPhotoUri, Uri toPhotoUri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(fromPhotoUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, toPhotoUri);
        return intent;
    }



    private static String getPicturePath(Context context) {
        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
            return context.getFilesDir().getAbsolutePath();
        File file = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (file != null)
            return file.getAbsolutePath();
        file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        if (file != null)
            return file.getAbsolutePath();
        return context.getFilesDir().getAbsolutePath();
    }

    public static boolean isIntentAvailable(Context context, Intent intent) {
        final PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
                PackageManager.GET_ACTIVITIES);
        return list.size() > 0;
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
     * 拷贝文件
     *
     * @param sourcePath
     * @param targetPath
     * @param delete 是否删除原文件
     * @return
     */
    public static String copyfile(String sourcePath, String targetPath, Boolean delete) {
        File fromFile = new File(sourcePath);
        File toFile = new File(targetPath);
        if (!fromFile.exists()) {
            return null;
        }
        if (!fromFile.isFile()) {
            return null;
        }
        if (!fromFile.canRead()) {
            return null;
        }
        if (!toFile.getParentFile().exists()) {
            toFile.getParentFile().mkdirs();
        }
        try {
            FileInputStream fosfrom = new FileInputStream(fromFile);
            FileOutputStream fosto = new FileOutputStream(toFile);
            byte bt[] = new byte[1024];
            int c;
            while ((c = fosfrom.read(bt)) > 0) {
                fosto.write(bt, 0, c); // 将内容写到新文件当中
            }
            fosfrom.close();
            fosto.close();
        } catch (Exception ex) {
            Trace.e("readfile", ex.getMessage());
        }
        return targetPath;
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
                Trace.e("cn.teamtone", "文件不存在");
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

}
