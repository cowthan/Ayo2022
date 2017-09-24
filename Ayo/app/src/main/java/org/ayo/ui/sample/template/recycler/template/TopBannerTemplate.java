package org.ayo.ui.sample.template.recycler.template;

import android.app.Activity;

import org.ayo.list.adapter.ItemBean;
import org.ayo.list.adapter.AyoViewHolder;
import org.ayo.list.adapter.AyoItemTemplate;
import org.ayo.ui.sample.template.recycler.model.TopBanner;

/**
 * Created by Administrator on 2016/8/21.
 */
public class TopBannerTemplate extends AyoItemTemplate {

    public TopBannerTemplate(Activity activity) {
        super(activity, null);
    }

    @Override
    public boolean isForViewType(ItemBean itemBean, int position) {
        if(itemBean instanceof TopBanner){
            return true;
        }
        return false;
    }

    @Override
    public void onBindViewHolder(ItemBean itemBean, int position, AyoViewHolder holder) {

    }

    @Override
    protected int getLayoutId() {
        return 0;
    }
}
