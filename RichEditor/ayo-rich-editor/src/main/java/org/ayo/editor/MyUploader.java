package org.ayo.editor;

/**
 * Created by qiaoliang on 2017/8/19.
 */

public class MyUploader {

    public interface OnQiniuTokenCallback{
        void onSuccess(String token);
        void onFail(Object failEntity);
    }

    public static void getQiniuToken(OnQiniuTokenCallback callback){

        callback.onSuccess("");

    }


}
