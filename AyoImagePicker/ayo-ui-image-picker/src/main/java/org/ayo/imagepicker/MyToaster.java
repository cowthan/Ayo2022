package org.ayo.imagepicker;

import org.ayo.notify.toaster.Toaster;

/**
 * Created by qiaoliang on 2017/8/19.
 */

public class MyToaster {
    public static void toastShort(String toast){
        Toaster.toastShort(toast);
    }

    public static void toastLong(String toast){
        Toaster.toastLong(toast);
    }
}
