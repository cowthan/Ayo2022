package org.ayo.ui.sample.template.recycler.template;

import android.app.Activity;

import org.ayo.list.adapter.ItemBean;
import org.ayo.list.adapter.AyoViewHolder;
import org.ayo.list.adapter.AyoItemTemplate;
import org.ayo.sample.R;
import org.ayo.ui.sample.template.recycler.model.Top;

/**
 * Created by Administrator on 2016/8/21.
 */
public class TopTemplate extends AyoItemTemplate {

    public TopTemplate(Activity activity) {
        super(activity, null);
    }

    @Override
    public boolean isForViewType(ItemBean itemBean, int position) {
        if(itemBean instanceof Top){
            Top t = (Top) itemBean;
            if(t.type == 1) return true;
        }
        return false;
    }

    @Override
    public void onBindViewHolder(ItemBean itemBean, int position, AyoViewHolder holder) {
        Top t = (Top) itemBean;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_top;
    }
}
