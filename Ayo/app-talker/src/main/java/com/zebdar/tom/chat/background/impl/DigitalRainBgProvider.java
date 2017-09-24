package com.zebdar.tom.chat.background.impl;

import android.app.Activity;
import android.view.View;

import com.zebdar.tom.R;
import com.zebdar.tom.chat.background.BaseChatBgProvider;


/**
 * Created by Administrator on 2017/7/18.
 *
 * 静态图背景
 */

public class DigitalRainBgProvider extends BaseChatBgProvider {

    View mBgView;
    @Override
    public View getBackground(Activity activity) {
        if(mBgView == null){
            mBgView = View.inflate(activity, R.layout.layout_chat_bg_digital_rain, null);
            BlackEmpireView bev = (BlackEmpireView) mBgView.findViewById(R.id.bev);
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
