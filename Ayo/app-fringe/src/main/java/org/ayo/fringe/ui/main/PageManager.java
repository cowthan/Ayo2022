package org.ayo.fringe.ui.main;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by qiaoliang on 2017/9/7.
 *
 * 主页管理器，要显示在主页的页面，都在这里注册
 */

public class PageManager {

    private PageManager(){}

    private static final class Holder{
        private static final PageManager instance = new PageManager();
    }

    public static PageManager getDefault(){
        return Holder.instance;
    }

    private List<PageProvider> providers = new LinkedList<>();

    public void addPageProvider(PageProvider pageProvider){
        providers.add(pageProvider);
    }

    public void clear(){
        providers.clear();
    }

    public int getPageCount(){
        return providers.size();
    }

    public List<PageProvider> getPageProviders(){
        return providers;
    }

}
