package com.zebdar.tom.chat.adapter;

import com.zebdar.tom.chat.model.IMMsg;

import org.ayo.list.adapter.AyoItemTemplate;

/**
 * Created by Administrator on 2017/7/19.
 */

public abstract class AbstractMessageItemTemplate extends AyoItemTemplate<IMMsg> {

    public AbstractMessageItemTemplate() {
        super(null, null);
    }

    protected abstract void onMessageClick(IMMsg msg);
    protected abstract void onMessageLongClick(IMMsg msg);

}
