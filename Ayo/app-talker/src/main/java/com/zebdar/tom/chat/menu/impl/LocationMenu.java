package com.zebdar.tom.chat.menu.impl;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.zebdar.tom.Const;
import com.zebdar.tom.PreferencesUtils;
import com.zebdar.tom.R;
import com.zebdar.tom.chat.ChatManager;
import com.zebdar.tom.chat.MessageCenter;
import com.zebdar.tom.chat.MessageHelper;
import com.zebdar.tom.chat.menu.ChatMenuItem;
import com.zebdar.tom.chat.model.IMMsg;

/**
 * Created by Administrator on 2017/7/23.
 */

public class LocationMenu extends ChatMenuItem {

    @Override
    public int getIconId() {
        return R.drawable.btn_chat_add_location_selector;
    }

    @Override
    public View getMenuView() {
        return null;
    }

    @Override
    public void onClick() {
        IMMsg msg = MessageHelper.createTextMessage("位置", ChatManager.getDefault().getMe().id, ChatManager.getDefault().getCurrentPartner().id, true);
        MessageCenter.getDefault().send(msg);

        String lat = PreferencesUtils.getSharePreStr(getActivity(), Const.LOCTION);//经纬度
        if (TextUtils.isEmpty(lat)) {
            lat = "116.404,39.915";//北京
        }

        String imgUrl = Const.LOCATION_URL_S + lat + "&markers=|" + lat + "&markerStyles=l,A,0xFF0000";
        msg = MessageHelper.createLocationMessage(imgUrl, ChatManager.getDefault().getMe().id, ChatManager.getDefault().getCurrentPartner().id, false);
        MessageCenter.getDefault().onReceive(msg);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public String getHint() {
        return "";
    }
}
