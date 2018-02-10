package org.ayo.imagepicker;


import android.widget.Toast;

import org.ayo.AppCore;

/**
 * Created by qiaoliang on 2017/8/19.
 */

public class MyToaster {
    public static void toastShort(String toast){
        Toast.makeText(AppCore.app(), toast, Toast.LENGTH_SHORT).show();
    }

    public static void toastLong(String toast){
        Toast.makeText(AppCore.app(), toast, Toast.LENGTH_LONG).show();

    }
}
