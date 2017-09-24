package org.ayo.fringe.model;

import org.ayo.list.adapter.ItemBean;

import java.util.List;

/**
 * Created by qiaoliang on 2017/9/7.
 */

public class MmModel implements ItemBean{
    public String id;
    public String title;
    public String img_url;
    public List<String> all;

    @Override
    public String getTag9527() {
        return null;
    }
}
