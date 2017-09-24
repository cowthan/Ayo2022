package com.zebdar.tom.chat.menu;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

/**
 * Created by Administrator on 2017/7/23.
 */

public abstract class ChatMenuItem {

    private Activity mActivity;

    protected Activity getActivity(){
        return mActivity;
    }

    public void bind(Activity activity){
        this.mActivity = activity;
    }

    /** 显示图标 */
    public abstract int getIconId();

    /** 点击之后，在输入框下面显示的view，如果没有，则返回null */
    public abstract View getMenuView();

    /** 如果不在输入框下面显示View，就自己响应点击事件 */
    public abstract void onClick();

    /** 不管从哪儿唤起的for result Activity，IntentView都会将结果转发到各个MenuItem，这里自己判断，自己取结果 */
    public abstract void onActivityResult(int requestCode, int resultCode, Intent data);

    /** 显示提示，长按时会提示这个按钮是干什么的 */
    public abstract String getHint();

}
