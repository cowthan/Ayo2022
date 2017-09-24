package org.ayo.push;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by qiaoliang on 2017/4/14.
 */

public class PushManager {

    private PushManager(){}

    private static class Holder{
        private static final PushManager instance = new PushManager();
    }

    public static PushManager getDefault(){
        return Holder.instance;
    }

    private Map<String, PushListener> listeners = new HashMap<>();

    public void regist(String name, PushListener pushListener){
        listeners.put(name, pushListener);
    }

    public void unregist(String name){
        if(listeners.containsKey(name)) listeners.remove(name);
    }

    public void unregist(PushListener p){
        if(listeners.containsValue(p)) {
            for(String key: listeners.keySet()){
                if(p.equals(listeners.get(key))) listeners.remove(key);
            }
        }
    }

    public Collection<PushListener> getPushListeners(){
        return listeners.values();
    }

    public void notifyRegIdReceived(String platform, String regId){
        for(String key: listeners.keySet()){
            listeners.get(key).onRegistIdReceived(platform, regId);
        }
    }

    public void notifyPassthroughReceived(String platform, String msgId, String payload){
        for(String key: listeners.keySet()){
            listeners.get(key).onPassthroughReceived(platform, msgId, payload);
        }
    }

    public void notifyNotificationReceived(String platform, String msgId, String payload){
        for(String key: listeners.keySet()){
            listeners.get(key).onNotificationReceived(platform, msgId, payload);
        }
    }

    public void notifyNotificationClicked(String platform, String msgId, String payload){
        for(String key: listeners.keySet()){
            listeners.get(key).onNotificationClicked(platform, msgId, payload);
        }
    }
}
