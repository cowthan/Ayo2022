package com.zebdar.tom.ai.impl;

import android.app.Application;
import android.content.Intent;

import com.zebdar.tom.R;
import com.zebdar.tom.ai.AiDispatcher;
import com.zebdar.tom.ai.AiWorker;
import com.zebdar.tom.ai.OnAiCallback;
import com.zebdar.tom.chat.ChatActivity;
import com.zebdar.tom.chat.MessageCenter;
import com.zebdar.tom.speech.SpeechReader;

import org.ayo.core.Lang;

/**
 * Created by Administrator on 2017/7/23.
 */

public class AiManager  {

    public static void init(final Application app){
        AiDispatcher.addAiWoker(new AiWorker() {

            @Override
            public boolean match(String input) {
                return "测试通知".equalsIgnoreCase(input);
            }

            @Override
            public void handle(String input, OnAiCallback callback) {
                Lang.alert(1, "测试", "11223344555", "aaaaa", R.drawable.ic_customer_loading2, ChatActivity.class);
            }
        });

        AiDispatcher.addAiWoker(new AiWorker() {

            @Override
            public boolean match(String input) {
                return "测试广播".equalsIgnoreCase(input);
            }

            @Override
            public void handle(String input, OnAiCallback callback) {
                MessageCenter.getDefault().sendText("发起个广播：org.ayo.receiver.global");
                Intent intent = new Intent();
                intent.setAction("org.ayo.receiver.global");
                app.sendBroadcast(intent);
            }
        });

        AiDispatcher.addAiWoker(new AiWorker() {

            @Override
            public boolean match(String input) {
                return "四哥".equalsIgnoreCase(input);
            }

            @Override
            public void handle(String input, OnAiCallback callback) {
                SpeechReader.getDefault().speech("干啥");
            }
        });
    }

    public static AiWorker getDefaultAiWorker(){
        ///默认处理，必须放到最后
        return new AiWorker() {

            @Override
            public boolean match(String input) {
                return true;
            }

            @Override
            public void handle(String input, OnAiCallback callback) {
                //SpeechReader.getDefault().speech("不知道你说的什么玩意儿");
            }
        };
    }
}
