package com.zebdar.tom.chat.background;

import android.app.Activity;

/**
 * Created by Administrator on 2017/7/19.
 */

public abstract class BaseChatBgProvider implements ChatBackgroundProvider {

    private boolean isAttached = false;

    public void onAttached(Activity activity){
        isAttached = true;
    }

    public void onDetached(Activity activity){
        isAttached = false;
    }

    public boolean isAttached(){
        return isAttached;
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }
}
