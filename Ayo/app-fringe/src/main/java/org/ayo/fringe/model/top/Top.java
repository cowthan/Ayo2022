package org.ayo.fringe.model.top;


import org.ayo.core.Lang;
import org.ayo.http.utils.JsonUtils;
import org.ayo.list.adapter.ItemBean;

import java.util.List;

/**
 * 最新新闻热词，及其来源
 */
public class  Top  implements ItemBean {
    public String title;//资讯标题
    public String authorHeadImg;
    public String authorName;
    public String authorUrl;
    public String coverImages;
    public String createAt;
    public String shortIntro;
    public String readCount;
    public String topUrl;

    private List<String> covers;

    public boolean isSingleImage(){
        return !isTrippleImage();
    }

    public boolean isTrippleImage(){
        covers = JsonUtils.getBeanList(coverImages, String.class);
        return Lang.count(covers) == 3;
    }

    public String getCoverImages(int i){
        covers = JsonUtils.getBeanList(coverImages, String.class);
        return Lang.count(covers) > i ? covers.get(i) : null;
    }

    @Override
    public String getTag9527() {
        return "";
    }
}
