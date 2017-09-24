package com.zebdar.tom.chat.menu;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/23.
 *
 * 管理menu
 */

public class ChatMenuManager {

    private ChatMenuManager(){}

    private static final class Holder{
        private static final ChatMenuManager instance = new ChatMenuManager();
    }

    public static ChatMenuManager getDefault(){
        return Holder.instance;
    }

    private List<ChatMenuItem> menus = new ArrayList<>();

    public void register(ChatMenuItem menu){
        menus.add(menu);
    }

    public List<ChatMenuItem> getMenus(){
        return menus;
    }

}