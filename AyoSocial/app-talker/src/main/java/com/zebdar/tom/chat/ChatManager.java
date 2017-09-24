package com.zebdar.tom.chat;

import com.zebdar.tom.chat.model.IMUser;

/**
 * Created by Administrator on 2017/7/23.
 */

public class ChatManager {
    private ChatManager(){}

    private static final class Holder{
        private static final ChatManager instance = new ChatManager();
    }

    public static ChatManager getDefault(){
        return Holder.instance;
    }

    private IMUser me;
    private IMUser currentPartner;
    private Object currentGroup;

    public void setMe(IMUser me){
        this.me = me;
    }

    public void setCurrentPartner(IMUser user){
        this.currentPartner = user;
    }

    public IMUser getMe(){
        return me;
    }

    public IMUser getCurrentPartner(){
        return currentPartner;
    }

    public static IMUser getFakeFouther(){
        IMUser me = new IMUser();
        me.id = "9529";
        me.name = "四哥";
        me.portrait = "http://img5.imgtn.bdimg.com/it/u=2691636238,1926634979&fm=11&gp=0.jpg";
        return me;
    }

}
