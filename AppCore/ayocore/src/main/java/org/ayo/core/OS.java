package org.ayo.core;

import android.content.Intent;
import android.net.Uri;

import org.ayo.AppCore;

/**
 * Created by Administrator on 2016/1/27.
 */
public class OS {

    public static void startBrowser(String url){
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppCore.app().startActivity(intent);
    }

}
