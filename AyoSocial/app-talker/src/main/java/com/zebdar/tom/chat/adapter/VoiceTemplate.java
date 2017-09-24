package com.zebdar.tom.chat.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zebdar.tom.Const;
import com.zebdar.tom.R;
import com.zebdar.tom.chat.MessageTypes;
import com.zebdar.tom.chat.model.IMMsg;
import com.zebdar.tom.chat.prompt.MessageOperator;

import org.ayo.AppCore;
import org.ayo.core.Lang;
import org.ayo.list.adapter.AyoViewHolder;

/**
 * Created by Administrator on 2017/7/19.
 *
 * 普通文本消息，对于所有文本消息，这个应该放在最后面
 */

public class VoiceTemplate extends AbstractMessageItemTemplate{


    @Override
    public boolean isForViewType(IMMsg itemBean, int position) {
        return Lang.isEquals(MessageTypes.MSG_TYPE_VOICE, itemBean.getType());
    }

    @Override
    public void onBindViewHolder(final IMMsg msg, int position, AyoViewHolder holder) {
        LinearLayout layout_voice = holder.id(R.id.layout_voice);//语音 语音播放按钮父控件
        ImageView iv_voice =  holder.id(R.id.iv_voice);//动画
        ImageView iv_fy =  holder.id(R.id.iv_fy);//翻译按钮
        final TextView tv_fy =  holder.id(R.id.tv_fy);//翻译内容

        final String[] _content = msg.getContent().split(Const.SPILT);
        tv_fy.setText(_content[1]);
        tv_fy.setVisibility(View.GONE);
        layout_voice.setOnClickListener(new RecordPlayClickListener(AppCore.app(), iv_voice, _content[0]));
        iv_fy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv_fy.getVisibility() == View.GONE) {
                    tv_fy.setVisibility(View.VISIBLE);
                } else {
                    tv_fy.setVisibility(View.GONE);
                }
            }
        });
        layout_voice.setOnLongClickListener(new View.OnLongClickListener(){
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
        return R.layout.item_chat_voice_rece;
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
