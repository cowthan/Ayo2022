package org.ayo.editor;

import android.widget.Toast;

/**
 * Created by qiaoliang on 2017/8/19.
 */

public class MyToaster {
    public static void toastShort(String toast){
        Toast.makeText(MyEditor.app(), toast, Toast.LENGTH_SHORT).show();
    }

    public static void toastLong(String toast){
        Toast.makeText(MyEditor.app(), toast, Toast.LENGTH_LONG).show();
    }
}
