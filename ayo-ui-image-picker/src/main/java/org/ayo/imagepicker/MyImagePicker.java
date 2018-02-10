package org.ayo.imagepicker;

import android.app.Application;

/**
 * Created by qiaoliang on 2017/8/20.
 */

public class MyImagePicker {

    private MyImagePicker() {
    }

    private static final class Holder {
        private static final MyImagePicker instance = new MyImagePicker();
    }

    public static MyImagePicker getDefault() {
        return Holder.instance;
    }

    private static Application application;

    private static boolean DEBUG;

    public void init(Application application, boolean isDebug) {
        MyImagePicker.application = application;
        DEBUG = isDebug;
    }

    public static Application app() {
        return application;
    }

    public static boolean isDebug() {
        return DEBUG;
    }


    public static final int UPLOAD_LIMIT_M = 20;

    /**
     * 打开相机
     */
    public static final int RESULT_CODE_PHOTO = 0x1000;

    /**
     * 选取图片
     */
    public static final int RESULT_CODE_PICK_PIC = 0x1001;
    /**
     * 选取视频
     */
    public static final int RESULT_CODE_PICK_VIDEO = 0x1002;


    //=========================


    //=========================
    public interface LocalToaster{
        void toastShort(String s);
        void toastLong(String s);
    }

    private LocalToaster localToaster;

    public void setToaster(LocalToaster localToaster){
        this.localToaster = localToaster;
    }

    public LocalToaster getLocalToaster(){
        return localToaster;
    }
}
