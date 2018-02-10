package org.ayo.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import org.ayo.AppCore;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by qiaoliang on 2017/6/16.
 *
 * 往AppService里注册action，Intent和任务
 *
 */

public class ServiceDelegate {
    private ServiceDelegate(){}

    private static final class H{
        private static final ServiceDelegate instance = new ServiceDelegate();
    }

    public static ServiceDelegate getDefault(){
        return H.instance;
    }

    private HashMap<String, ServiceAction> actions = new HashMap<>();
    private HashMap<String, List<OnServiceActionCallback>> actionCallback = new HashMap<>();

    public void register(String action, ServiceAction task){
        //反正不管有没有已注册，就是覆盖
        actions.put(action, task);
    }

    public void addActionCallback(String action, OnServiceActionCallback c){
        if(c == null) return;
        List<OnServiceActionCallback> list = actionCallback.get(action);
        if(list == null){
            list = new LinkedList<>();
            actionCallback.put(action, list);
        }
        list.add(c);
    }

    public void removeActionCallback(String action, OnServiceActionCallback c){
        if(c == null) return;
        List<OnServiceActionCallback> list = actionCallback.get(action);
        if(list == null){
            return;
        }
        if(list.contains(c)){
            list.remove(c);
        }
    }

    public void notifyActionResult(String action, final Object result){
        final List<OnServiceActionCallback> list = actionCallback.get(action);
        if(list == null){
            return;
        }
        AppCore.getDefault().getGlobalHandler().post(new Runnable() {
            @Override
            public void run() {
                for(OnServiceActionCallback c: list){
                    c.onFinish(result);
                }
            }
        });

    }

    public void runAction(Context c, String action, Intent intent){
        if(intent == null) intent = new Intent();
        intent.setComponent(new ComponentName(c, GlobalIntentService.class));
        intent.setAction(action);
        c.startService(intent);
    }

    boolean containsAction(String action){
        return actions.containsKey(action);
    }

    ServiceAction getAction(String action){
        return actions.get(action);
    }
}
