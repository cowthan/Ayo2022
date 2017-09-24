package org.ayo.ui.sample.template.recycler;

import android.os.Handler;
import android.view.View;

import org.ayo.list.adapter.ItemBean;
import org.ayo.list.adapter.AyoItemTemplate;
import org.ayo.list.recycler.XRecyclerView;
import org.ayo.template.recycler.AyoListTemplateFragment;
import org.ayo.template.recycler.condition.AyoCondition;
import org.ayo.ui.sample.template.recycler.template.Top2Template;
import org.ayo.ui.sample.template.recycler.template.TopTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/21.
 */
public class DemoTopListFragment extends AyoListTemplateFragment<ItemBean>{


    @Override
    public void loadData(AyoCondition cond) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                List<ItemBean> list = Data.getTopList();
                onLoadOk(list);
                //Toaster.toastShort("哈哈哈");
            }
        }, 2000);
    }

    @Override
    public AyoCondition initCondition() {
        return new AyoCondition(1);
    }

    @Override
    protected void onCreateViewFinished(View root, XRecyclerView mXRecyclerView) {
        super.onCreateViewFinished(root, mXRecyclerView);
    }

    @Override
    public void loadCache() {

    }

    @Override
    protected List<AyoItemTemplate> getTemplate() {
        List<AyoItemTemplate> t = new ArrayList<>();
        t.add(new TopTemplate(getActivity()));
        t.add(new Top2Template(getActivity()));
       // t.add(new TopBannerTemplate(getActivity()));
        return t;
    }
}
