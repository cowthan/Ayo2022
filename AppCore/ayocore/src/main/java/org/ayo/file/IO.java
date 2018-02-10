package org.ayo.file;

import org.ayo.AppCore;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.io.Writer;

/**
 * Created by Administrator on 2017/7/16.
 */

public class IO {

    public static InputStream fromRaw(int rawId){
        InputStream in = AppCore.app().getResources().openRawResource(rawId);
        return in;
    }

    public static InputStream fromAssets(String subPath){
        InputStream in = null;
        try {
            in = AppCore.app().getAssets().open(subPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return in;
    }


    public static String string(InputStream is){
        if(is == null) return "";
        try {
            InputStreamReader reader = new InputStreamReader(is);
            return string(reader);
        } finally {
            close(is);
        }
    }

    public static String string(Reader is){
        if(is == null) return "";
        BufferedReader br = new BufferedReader(is);
        String line = "";
        StringBuilder sb = new StringBuilder();
        try {
            while((line = br.readLine()) != null){
                sb.append(line);
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(br);
            close(is);
        }
        return "";
    }

    public static String string(String path){
        return string(new File(path));
    }

    public static String string(File f){
        try {
            return string(new FileInputStream(f));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static byte[] bytes(InputStream is){

        return null;
    }



    public static byte[] bytes(String path){

        return null;
    }

    public static byte[] bytes(File f){

        return null;
    }

    public static void output(String content, OutputStream os){
        try {
            OutputStreamWriter writer = new OutputStreamWriter(os);
            output(content, writer);
        } finally {
            close(os);
        }

    }

    public static void output(String content, Writer writer){
        BufferedWriter bw = new BufferedWriter(writer);
        try {
            bw.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            close(bw);
            close(writer);
        }
    }

    public static void output(byte[] content, OutputStream os){

    }

    public static void output(String content, String path, boolean append){
        output(content, new File(path), append);
    }

    public static void output(String content, File f, boolean append){

        try {
            FileOutputStream os = new FileOutputStream(f, append);
            output(content, os);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static byte[] readFromFile(String fileName, int offset, int len) {
        if (fileName == null) {
            return null;
        }

        File file = new File(fileName);
        if (!file.exists()) {
            //WLog.i(FileUtils.class, "readFromFile: file not found");
            return null;
        }

        if (len == -1) {
            len = (int) file.length();
        }

        if (offset < 0) {
            //WLog.e(FileUtils.class, "readFromFile invalid offset:" + offset);
            return null;
        }
        if (len <= 0) {
            //WLog.e(FileUtils.class, "readFromFile invalid len:" + len);
            return null;
        }
        if (offset + len > (int) file.length()) {
            //WLog.e(FileUtils.class, "readFromFile invalid file len:" + file.length());
            return null;
        }

        byte[] b = null;
        try {
            RandomAccessFile in = new RandomAccessFile(fileName, "r");
            b = new byte[len];
            in.seek(offset);
            in.readFully(b);
            in.close();

        } catch (Exception e) {
            // WLog.e(FileUtils.class, "readFromFile : errMsg = " + e.getMessage());
            e.printStackTrace();
        }
        return b;
    }



    //=====================================================================
    public static void close(InputStream is){
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void close(Reader is){
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void close(BufferedReader is){
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void close(OutputStream is){
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void close(Writer is){
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void close(BufferedWriter is){
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
