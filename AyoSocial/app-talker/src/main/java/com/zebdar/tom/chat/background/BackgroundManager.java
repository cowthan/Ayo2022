package com.zebdar.tom.chat.background;

import com.zebdar.tom.chat.background.impl.DigitalRainBgProvider;
import com.zebdar.tom.chat.background.impl.StaticImageBgProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/19.
 */

public class BackgroundManager {

    private BackgroundManager(){
        bgs.add(new StaticImageBgProvider());
        bgs.add(new DigitalRainBgProvider());
    }

    private static final class Holder{
        private static final BackgroundManager instance = new BackgroundManager();
    }

    public static BackgroundManager getDefault(){
        return Holder.instance;
    }

    private List<BaseChatBgProvider> bgs = new ArrayList<>();
    private int currentPosition = -1;

    public synchronized BaseChatBgProvider nextBackgroundProvider(){
        currentPosition++;
        if(currentPosition >= bgs.size()){
            currentPosition = 0;
        }
        return bgs.get(currentPosition);
    }

}
