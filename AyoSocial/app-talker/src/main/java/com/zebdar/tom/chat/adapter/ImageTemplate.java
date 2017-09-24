package com.zebdar.tom.chat.adapter;

import android.view.View;

import com.facebook.drawee.view.SimpleDraweeView;
import com.zebdar.tom.R;
import com.zebdar.tom.chat.MessageTypes;
import com.zebdar.tom.chat.model.IMMsg;
import com.zebdar.tom.chat.prompt.MessageOperator;

import org.ayo.core.Lang;
import org.ayo.fresco.Flesco;
import org.ayo.list.adapter.AyoViewHolder;

import static com.zebdar.tom.R.id.iv_location;

/**
 * Created by Administrator on 2017/7/19.
 *
 * 普通文本消息，对于所有文本消息，这个应该放在最后面
 */

public class ImageTemplate extends AbstractMessageItemTemplate{

    @Override
    public boolean isForViewType(IMMsg itemBean, int position) {
        return Lang.isEquals(MessageTypes.MSG_TYPE_IMG, itemBean.getType());
    }

    @Override
    public void onBindViewHolder(final IMMsg msg, int position, AyoViewHolder holder) {
        SimpleDraweeView iv_image = holder.id(iv_location);
        Flesco.setImageUri(iv_image, msg.getContent());

        iv_image.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                onMessageClick(msg);
            }
        });
        iv_image.setOnLongClickListener(new View.OnLongClickListener(){
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
        return R.layout.item_chat_image_rece;
    }

    @Override
    protected void onMessageClick(IMMsg msg) {
        MessageOperator.delonly(getActivity(), msg);
    }

    @Override
    protected void onMessageLongClick(IMMsg msg) {
        MessageOperator.delonly(getActivity(), msg);
    }
}
