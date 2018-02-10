
package com.app.core.prompt;

import android.widget.Toast;

import org.ayo.AppCore;


/**
 * 只会显示一个toast  不会有延迟的toast
 * 
 * @author pengjun
 */
public class Toaster {

    /**
     * 唯一的toast
     */
    private static Toast mToast = null;

    private Toaster() {
    }

    public static void toastLong(final String tip){
    	showToast(tip, Toast.LENGTH_LONG);
    }
    
    public static void toastShort(final String tip){
    	showToast(tip, Toast.LENGTH_SHORT);
    }
    
    private static void showToast(final int stringid, final int lastTime) {
    	 if (mToast != null) {
             // mToast.cancel();
         } else {
             mToast = Toast.makeText(AppCore.app(), stringid, lastTime);
         }
         mToast.setText(stringid);
         mToast.show();
    }

    private static void showToast(final String tips, final int lastTime) {
    	if (mToast != null) {
            // mToast.cancel();
        } else {
            mToast = Toast.makeText(AppCore.app(), tips, lastTime);
        }
        mToast.setText(tips);
        mToast.show();
    }


}
