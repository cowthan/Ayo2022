package org.ayo.ui.sample.scrollview;

import android.support.v4.widget.NestedScrollView;

/**
 * Created by qiaoliang on 2017/3/24.
 */

public class PullLoadMoreNestedScrollViewHelper extends PullLoadMoreHelper<NestedScrollView> {

    @Override
    protected void setOnScrollChangeListenersToWrapperView(NestedScrollView pullLoadMoreNestedScrollView) {
        pullLoadMoreNestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                onMyScrollChange(v, scrollX, scrollY, oldScrollX, oldScrollY);
            }
        });
    }

    @Override
    protected boolean alreadyBottom(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        int scrollViewHeight = v.getHeight();
        int childHeight = v.getChildAt(0).getHeight();
        if(scrollViewHeight + scrollY == childHeight){
            return true;
        }
        return false;
    }
}
