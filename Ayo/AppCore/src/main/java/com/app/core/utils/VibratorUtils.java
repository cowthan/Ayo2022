package com.app.core.utils;

import android.content.Context;
import android.os.Vibrator;

import org.ayo.AppCore;

/**
 * Created by qiaoliang on 2017/10/27.
 */

public class VibratorUtils {

    public static void vibrate(){
        Vibrator v = (Vibrator) AppCore.app().getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(300);

//        v.vibrate(long[] pattern, int repeat);
//        v.cancel();
    }

}
