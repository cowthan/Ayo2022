package org.ayo.file;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import org.ayo.AppCore;
import org.ayo.log.Trace;

import java.io.File;

/**
 * Created by Administrator on 2017/7/16.
 */

public class FilePath {
    public static String getPathInRoot(String subPath){
        String path = AppCore.ROOT;
        if(subPath != null){
            if(subPath.startsWith("/")){
                path = AppCore.ROOT + subPath.substring(1);
            }else{
                path = AppCore.ROOT + subPath;
            }
        }
        return path;
    }

    public static String getDirInRoot(String subPath){
        String path = AppCore.ROOT;
        if(subPath != null){
            if(subPath.startsWith("/")){
                path = AppCore.ROOT + subPath.substring(1);
            }else{
                path = AppCore.ROOT + subPath;
            }
        }
        FileOperator.createDirIfNotExists(path);
        return path;
    }

    public static String getPicturePath() {
        String path = "";
        Context context = AppCore.app();
        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            path = context.getFilesDir().getAbsolutePath();
        }else{
            File file = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            if (file != null){
                path = file.getAbsolutePath();
            }else{
                file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                if (file != null){
                    path = file.getAbsolutePath();
                }else{
                    path = context.getFilesDir().getAbsolutePath();
                }
            }
        }
        Trace.i(AppCore.TAG, "getPicturePath returns " + path);
        return path;
    }


    private final static String FILE_EXTENSION_SEPARATOR = ".";

    /**
     * Get file name from the path (without extension)
     *
     * @param filePath
     *            The path of the file
     * @return String File name without extension
     * @see <pre>
     *      getFileNameWithoutExtension(null)               =   null
     *      getFileNameWithoutExtension("")                 =   ""
     *      getFileNameWithoutExtension("   ")              =   "   "
     *      getFileNameWithoutExtension("abc")              =   "abc"
     *      getFileNameWithoutExtension("a.mp3")            =   "a"
     *      getFileNameWithoutExtension("a.b.rmvb")         =   "a.b"
     *      getFileNameWithoutExtension("/home/admin")      =   "admin"
     *      getFileNameWithoutExtension("/home/admin/a.txt/b.mp3")  =   "b"
     * </pre>
     */
    public static String getFileNameWithoutExtension(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return filePath;
        }

        int extenPosi = filePath.lastIndexOf(FILE_EXTENSION_SEPARATOR);
        int filePosi = filePath.lastIndexOf(File.separator);
        if (filePosi == -1) {
            return (extenPosi == -1 ? filePath : filePath.substring(0, extenPosi));
        } else {
            if (extenPosi == -1) {
                return filePath.substring(filePosi + 1);
            } else {
                return (filePosi < extenPosi ? filePath.substring(filePosi + 1, extenPosi) : filePath.substring(filePosi + 1));
            }
        }
    }

    /**
     * Get file name from the path (with extension)
     *
     * @param filePath
     *            The path of the file
     * @return String File name with extension
     * @see <pre>
     *      getFileName(null)               =   null
     *      getFileName("")                 =   ""
     *      getFileName("   ")              =   "   "
     *      getFileName("a.mp3")            =   "a.mp3"
     *      getFileName("a.b.rmvb")         =   "a.b.rmvb"
     *      getFileName("abc")              =   "abc"
     *      getFileName("/home/admin")      =   "admin"
     *      getFileName("/home/admin/a.txt/b.mp3")  =   "b.mp3"
     * </pre>
     */
    public static String getFileName(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return filePath;
        }

        int filePosi = filePath.lastIndexOf(File.separator);
        if (filePosi == -1) {
            return filePath;
        }
        return filePath.substring(filePosi + 1);
    }

    /**
     * Get folder name from the path
     *
     * @param filePath
     *            The path of the file
     * @return String The folder path
     * @see <pre>
     *      getFolderName(null)               =   null
     *      getFolderName("")                 =   ""
     *      getFolderName("   ")              =   ""
     *      getFolderName("a.mp3")            =   ""
     *      getFolderName("a.b.rmvb")         =   ""
     *      getFolderName("abc")              =   ""
     *      getFolderName("/home/admin")      =   "/home"
     *      getFolderName("/home/admin/a.txt/b.mp3")  =   "/home/admin/a.txt"
     * </pre>
     */
    public static String getFolderName(String filePath) {

        if (TextUtils.isEmpty(filePath)) {
            return filePath;
        }

        int filePosi = filePath.lastIndexOf(File.separator);
        if (filePosi == -1) {
            return "";
        }
        return filePath.substring(0, filePosi);
    }

    /**
     * Get the extension name from the path
     *
     * @param filePath
     *            The path of the file
     * @return String The extension name of the file
     * @see <pre>
     *      getFileExtension(null)               =   ""
     *      getFileExtension("")                 =   ""
     *      getFileExtension("   ")              =   ""
     *      getFileExtension("a.mp3")            =   "mp3"
     *      getFileExtension("a.b.rmvb")         =   "rmvb"
     *      getFileExtension("abc")              =   ""
     *      getFileExtension("c:\\")              =   ""
     *      getFileExtension("c:\\a")             =   ""
     *      getFileExtension("c:\\a.b")           =   "b"
     *      getFileExtension("c:a.txt\\a")        =   ""
     *      getFileExtension("/home/admin")      =   ""
     *      getFileExtension("/home/admin/a.txt/b")  =   ""
     *      getFileExtension("/home/admin/a.txt/b.mp3")  =   "mp3"
     * </pre>
     */
    public static String getFileExtension(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return filePath;
        }

        int extenPosi = filePath.lastIndexOf(FILE_EXTENSION_SEPARATOR);
        int filePosi = filePath.lastIndexOf(File.separator);
        if (extenPosi == -1) {
            return "";
        } else {
            if (filePosi >= extenPosi) {
                return "";
            }
            return filePath.substring(extenPosi + 1);
        }
    }
}
