package com.zebdar.tom.chat.adapter;

import com.zebdar.tom.chat.model.IMMsg;

import org.ayo.list.adapter.AyoItemTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/19.
 *
 * 管理聊天列表里的item模板
 */

public class ItemTemplateManager {

    private ItemTemplateManager(){
    }

    private static final class Holder{
        private static final ItemTemplateManager instance = new ItemTemplateManager();
    }

    public static ItemTemplateManager getDefault(){
        return Holder.instance;
    }

    private List<AyoItemTemplate<IMMsg>> templates = new ArrayList<>();
    private int currentPosition = -1;

    public synchronized void registerTemplate(AyoItemTemplate<IMMsg> template){
        templates.add(template);
    }

    public List<AyoItemTemplate<IMMsg>> getTemplates(){
        return templates;
    }
}
