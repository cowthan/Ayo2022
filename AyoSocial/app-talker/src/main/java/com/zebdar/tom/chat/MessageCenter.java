package com.zebdar.tom.chat;

import android.os.Handler;
import android.os.Looper;

import com.zebdar.tom.ai.AiDispatcher;
import com.zebdar.tom.ai.OnAiCallback;
import com.zebdar.tom.chat.model.OnMessageChangedListener;
import com.zebdar.tom.chat.exception.SendErrorException;
import com.zebdar.tom.chat.model.IMMsg;
import com.zebdar.tom.chat.model.memdb.MessageCache;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qiaoliang on 2017/6/24.
 *
 * 消息中心，负责消息的发送，存储，获取，处理监听
 *
 * MessageCenter.send(Message m);
 *
 * MessageCenter.addMessageListener(new Listener);
 * - on add
 * - on update
 * - on delete
 *
 * MessageCenter.registMessageType(type, XXMessage)
 *
 */

public class MessageCenter {

    private MessageCenter(){
        mainHandler = new Handler(Looper.getMainLooper());
    }

    private static final class H{
        private static final MessageCenter instance = new MessageCenter();
    }

    public static MessageCenter getDefault(){
        return H.instance;
    }

    private Handler mainHandler;

    //---------------------------------------------
    // 管理消息收发
    //---------------------------------------------
    private List<OnMessageChangedListener> remoteListeners = new ArrayList<>();

    public void addOnMessageRemoteListener(OnMessageChangedListener l){
        remoteListeners.add(l);
    }

    public void removeOnMessageRemoteListener(OnMessageChangedListener l){
        if(remoteListeners.contains(l)){
            remoteListeners.remove(l);
        }
    }

    public void send(final IMMsg m){
        send(m, true);
    }

    private void send(final IMMsg m, final boolean needAiHandle){
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                // 先通知发送开始事件
                m.setIsReaded("1");
                MessageCache db = new MessageCache();
                db.insert(m);
                notifySendStart(m);

                //如果是IM，或者发图片，或者其他什么，可能有loading过程，也可能没有
                //但这里，应该是一个http查询的过程，查询过程要走send回调，查询结果要走receive回调
                if(m.getType().equals(MessageTypes.MSG_TYPE_TEXT)){
                    if(needAiHandle){
                        AiDispatcher.dispatch(m.getContent(), new OnAiCallback() {
                            @Override
                            public void onResponse(String input, String output) {
                                IMMsg msg = MessageHelper.createTextMessage(output, "", "", true);
                                send(msg, false);
                            }
                        });
                    }

                }

                // 总是得插入数据库，也总是得更新状态
                notifySendFinish(m, true, null);
            }
        });
    }

    public void sendText(String text){
        IMMsg msg = MessageHelper.createTextMessage(text, "", "", true);
        send(msg, true);
    }

    private void notifySendStart(IMMsg m){

        for(OnMessageChangedListener rl: remoteListeners){
            rl.onAdd(m);
        }
    }

    private void notifySending(IMMsg m, int progress){
        for(OnMessageChangedListener rl: remoteListeners){
            rl.onLoading(m, progress == 100, progress);
        }
    }

    private void notifySendFinish(IMMsg m, boolean isSuccess, SendErrorException e){
        for(OnMessageChangedListener rl: remoteListeners){
            //rl.onSendFinish(m, isSuccess, e);
        }
    }

    /**
     * 这个在这里需要由外部调用，因为这不是IM系统，收到的回复其实是http查询的结果
     * 也可能推送过来
     * 也可能是用户操作导致的通知
     *
     * */
    public void onReceive(final IMMsg m){
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                m.setIsReaded("0");
                MessageCache db = new MessageCache();
                db.insert(m);
                m.setIsComing(1);

                notifyReceive(m);
            }
        });

    }

    private void notifyReceive(IMMsg m){
        for(OnMessageChangedListener rl: remoteListeners){
            rl.onAdd(m);
        }
    }

    public void delete(IMMsg msg){
        MessageCache db = new MessageCache();
        db.deleteMsgById(msg.getMsgId());
        notifyDelete(msg);
    }

    private void notifyDelete(IMMsg m){
        for(OnMessageChangedListener rl: remoteListeners){
            rl.onDelete(m);
        }
    }


}
