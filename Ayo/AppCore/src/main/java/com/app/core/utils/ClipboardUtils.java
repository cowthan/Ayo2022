package com.app.core.utils;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;

import org.ayo.AppCore;

/**
 * Created by qiaoliang on 2017/11/1.
 */

public class ClipboardUtils {

    public static void putString(String s){
        ClipboardManager clipManager = (ClipboardManager) AppCore.app().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(null, s);
        clipManager.setPrimaryClip(clip);
        /*
        //创建一个包含 htmlText 的 ClipData
        //一般在浏览器中对网页进行拷贝的时候会调用此方法
        //其中 htmlText 是包含 HTML 标签的字符串
        static public ClipData newHtmlText(CharSequence label, CharSequence text, String htmlText);
        //创建一个包含 Intent 的 ClipData
        static public ClipData newIntent(CharSequence label, Intent intent);
        //创建一个包含 Uri 的 ClipData，MimeType 会根据 Uri 进行修改
        static public ClipData newUri(ContentResolver resolver, CharSequence label, Uri uri);
        //与 newUri 相对应，但是并不会根据 Uri 修改 MimeType
        static public ClipData newRawUri(CharSequence label, Uri uri);
        */
    }

    public static String getString(){
        ClipboardManager clipManager = (ClipboardManager) AppCore.app().getSystemService(Context.CLIPBOARD_SERVICE);
        //判断剪贴板里是否有内容
        if(!clipManager.hasPrimaryClip()) {
            return "";
        }
        ClipData clip = clipManager.getPrimaryClip();
        if(clip == null || clip.getItemCount() == 0) return "";

        //获取 ClipDescription
        ClipDescription description = clip.getDescription();
        //获取 label
        String label = description.getLabel().toString();
        //获取 text

        ClipData.Item item = clip.getItemAt(0);
        CharSequence text = item.getText();
        if(text == null) return "";
        return text.toString();
    }
}
