package com.zebdar.tom.chat.adapter;

import android.text.util.Linkify;
import android.view.View;
import android.widget.TextView;

import com.zebdar.tom.R;
import com.zebdar.tom.chat.MessageTypes;
import com.zebdar.tom.chat.model.IMMsg;
import com.zebdar.tom.chat.prompt.MessageOperator;
import com.zebdar.tom.emoji.ExpressionUtil;

import org.ayo.core.Lang;
import org.ayo.list.adapter.AyoViewHolder;

/**
 * Created by Administrator on 2017/7/19.
 *
 * 普通文本消息，对于所有文本消息，这个应该放在最后面
 */

public class TextCommonTemplate extends AbstractMessageItemTemplate{

    public TextCommonTemplate() {
        super();
    }

    @Override
    public boolean isForViewType(IMMsg itemBean, int position) {
        return Lang.isEquals(MessageTypes.MSG_TYPE_TEXT, itemBean.getType());
    }

    @Override
    public void onBindViewHolder(final IMMsg msg, int position, AyoViewHolder holder) {
        TextView tv_text = holder.id(R.id.tv_text);

        tv_text.setText(ExpressionUtil.prase(getActivity(), tv_text, msg.getContent()));
        Linkify.addLinks(tv_text, Linkify.ALL);

        tv_text.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                onMessageClick(msg);
            }
        });
        tv_text.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v) {
                onMessageClick(msg);
                return false;
            }
        });

        View body_container = holder.id(R.id.body_container);
        if(msg.getIsComing() == 0){
            body_container.setBackgroundResource(R.drawable.sel_chat_item_bg_me);
        }else{
            body_container.setBackgroundResource(R.drawable.sel_chat_item_bg_others);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_chat_text_rece;
    }

    @Override
    protected void onMessageClick(IMMsg msg) {
        MessageOperator.clip(getActivity(), msg);
    }

    @Override
    protected void onMessageLongClick(IMMsg msg) {
        MessageOperator.clip(getActivity(), msg);
    }
}
