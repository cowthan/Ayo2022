package org.ayo.template.recycler;

import org.ayo.list.adapter.ItemBean;
import org.ayo.list.recycler.XRecyclerView;
import org.ayo.template.recycler.condition.AyoCondition;

/**
 * Created by Administrator on 2016/8/21.
 */
abstract class AyoRefreshable<T extends ItemBean> extends AyoListable<T> {


    protected boolean isLoadMore = false;
    protected AyoCondition condition;

    public abstract void loadData(AyoCondition cond);
    public abstract AyoCondition initCondition();

    @Override
    protected void initXRecyclerView() {
        condition = initCondition();
        mXRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                isLoadMore = false;
                condition.onPullDown();
                loadData(condition);
            }

            @Override
            public void onLoadMore() {
                isLoadMore = true;
                condition.onPullUp();
                loadData(condition);
            }
        });
    }

}
