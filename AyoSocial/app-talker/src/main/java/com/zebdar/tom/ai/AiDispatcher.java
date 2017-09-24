package com.zebdar.tom.ai;

import android.util.Log;

import com.zebdar.tom.ai.impl.AiManager;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by qiaoliang on 2017/6/25.
 *
 * 人工智能转发，接收聊天页来的文本，或者推送，或者http请求，转发到具体功能模块
 */

public class AiDispatcher {

    private static List<AiWorker> workers = new LinkedList<>();

    public static void addAiWoker(AiWorker w){
        workers.add(w);
    }

    public static void dispatch(String input, OnAiCallback callback){

        for(AiWorker worker: workers){

            if(worker.match(input)){
                Log.e("ai--work", input + "---matchs---" + worker.getClass().getSimpleName());
                worker.handle(input, callback);
                return; ///找到第一个就处理并退出
            }

        }

        Log.e("ai--work", input + "---doesn't match any Worker，后期会尝试从服务器处理---");
        AiManager.getDefaultAiWorker().handle(input, callback);
    }

    private static boolean keyMatch(String userInput, String keyword){
        return userInput.equalsIgnoreCase(keyword);
    }

}
