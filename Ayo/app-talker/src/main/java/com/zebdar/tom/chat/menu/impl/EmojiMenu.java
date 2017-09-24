package com.zebdar.tom.chat.menu.impl;

import android.content.Intent;
import android.view.View;

import com.zebdar.tom.chat.menu.ChatMenuItem;

/**
 * Created by Administrator on 2017/7/23.
 *
 * 表情比较特殊：
 * 1 需要影响的是输入框，而非直接发消息
 * 2 表情的View可以一直放在InputView中，不需要频繁add和remove
 *
 * 暂时放着
 */

public class EmojiMenu extends ChatMenuItem {
    @Override
    public int getIconId() {
        return 0;
    }

    @Override
    public View getMenuView() {
        return null;
    }

    @Override
    public void onClick() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public String getHint() {
        return null;
    }
}
