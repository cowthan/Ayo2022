package com.zebdar.tom.chat.background.impl;

import android.app.Activity;
import android.view.View;

import com.facebook.drawee.view.SimpleDraweeView;
import com.zebdar.tom.R;
import com.zebdar.tom.chat.background.BaseChatBgProvider;

import org.ayo.fresco.Flesco;

/**
 * Created by Administrator on 2017/7/18.
 *
 * 静态图背景
 */

public class StaticImageBgProvider extends BaseChatBgProvider {

    View mBgView;

    @Override
    public View getBackground(Activity activity) {
        if(mBgView == null){
            mBgView = View.inflate(activity, R.layout.layout_chat_bg_static_img, null);
            SimpleDraweeView iv = (SimpleDraweeView) mBgView.findViewById(R.id.iv);
            Flesco.setImageUri(iv, "http://s6.sinaimg.cn/mw690/0062Eartgy6SYJXgcoBb5&690");
        }
        return mBgView;
    }

    @Override
    public void onMoodChange(Activity activity, String mood) {
    }

    @Override
    public boolean needForegroundDarkTheme() {
        return true;
    }

    @Override
    public int getStatusBarColor() {
        return 0;
    }
}
