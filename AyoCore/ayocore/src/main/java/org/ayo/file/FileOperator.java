package org.ayo.file;

import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;

import org.ayo.AppCore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Administrator on 2017/7/16.
 */

public class FileOperator {

    public static final String PATH_SEP = "/";

    /**
     * notify the andoid system to refresh the files, because a new file(path) is added
     */
    public static void notifyFileChanged(String path){
        try {
            Uri uri = Uri.fromFile(new File(path));
            //Uri.parse("file://" + Environment.getExternalStorageDirectory()+"/image/"+ fileName)
            AppCore.app().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /** full path */
    public static void createFileIfNotExists(String filepath) {

        File f = new File(filepath);
        if(f.exists() && f.isFile()){
            return;
        }

        int index = filepath.lastIndexOf(PATH_SEP);
        if(index != -1){
            String dirPath = filepath.substring(0, index);
            createDirIfNotExists(dirPath);
        }
        try {
            f.createNewFile();
        } catch (IOException e) {
        }
    }

    public static void createDirIfNotExists(String dirPath){
        File dir = new File(dirPath);
        if(dir.exists() && dir.isDirectory()){
            return;
        }else{
            dir.mkdirs();
        }
    }

    /**
     * from: full path
     * toDir: dir path
     * @return
     */
    public static File moveFile(String fileFrom, String dirTo) {

        File f = new File(fileFrom);
        File dirToT = new File(dirTo);
        if (!dirToT.exists()) {
            dirToT.mkdirs();
        }
        if (f.renameTo(new File(dirTo, f.getName()))) {
            return new File(dirTo, f.getName());
        } else {
            return null;
        }
    }

    /**
     *
     * @param fileFrom  full path of file
     * @param toDir dir, if not exist, will be created
     * @param newFilename 新文件名
     * @return
     */
    public static File copyFile(String fileFrom, String toDir, String newFilename) {

        File f = new File(fileFrom);
        if(f.exists() && f.isFile()){
            File dirTo = new File(toDir);
            if (!dirTo.exists()) {
                dirTo.mkdirs();
            }else{
                if(!dirTo.isDirectory()){
                    throw new IllegalArgumentException("dirTo is exists, but is not a dir");
                }
            }

            File out = new File(dirTo, newFilename);
            try {
                streamToFile(new FileInputStream(f), out);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return out;

        }else{
            throw new IllegalArgumentException("source file must be exists, and must be a file--" + fileFrom);
        }

    }


    public static boolean copyAssetFile(final String fileName, final String dirTo, boolean overrideOldFile) {
        File fileTo = new File(dirTo, fileName);
        if(!overrideOldFile){

            if (fileTo.exists() && fileTo.length() > 0) {
                return true;
            }
        }
        boolean res = false; //{false,false};
        AssetManager am = AppCore.app().getAssets();
        try {
            InputStream is = am.open(fileName);
            FileOutputStream fos = new FileOutputStream(fileTo);
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
            fos.close();
            is.close();
            res = true;
        } catch (Exception e) {
            e.printStackTrace();
            res = false;
        }
        return res;
    }

    private static void streamToFile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

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
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (s <= 1024) {
            s = 1;
        } else {
            s = s / 1024;
        }
        return s;
    }


    /**
     * 获取SD卡空余空间
     *
     * @return 返回剩余空间 单位：byte （-1：表示失败）
     */
    public static long getSDFreeSize() {
        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            return -1;
        }
        // 取得SD卡文件路径
        File path = Environment.getExternalStorageDirectory();
        StatFs sf = new StatFs(path.getPath());
        // 获取单个数据块的大小(Byte)
        long blockSize = sf.getBlockSize();
        // 空闲的数据块的数量
        long freeBlocks = sf.getAvailableBlocks();
        // 返回SD卡空闲大小
        return (freeBlocks * blockSize); // 单位byte
    }

    public static boolean isFolderExist(String directoryPath) {
        if (TextUtils.isEmpty(directoryPath)) {
            return false;
        }

        File dire = new File(directoryPath);
        return (dire.exists() && dire.isDirectory());
    }

    public static boolean deleteFile(String path) {
        if (TextUtils.isEmpty(path)) {
            return true;
        }

        File file = new File(path);
        if (file.exists()) {
            if (file.isFile()) {
                return file.delete();
            } else if (file.isDirectory()) {
                for (File f : file.listFiles()) {
                    if (f.isFile()) {
                        f.delete();
                    } else if (f.isDirectory()) {
                        deleteFile(f.getAbsolutePath());
                    }
                }
                return file.delete();
            }
            return false;
        }
        return true;
    }
}
