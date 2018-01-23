
package com.app.core.scroll;

/**
 * Created by qiaoliang on 2017/8/10.
 */

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 判断RecyclerView上滑下滑
 */
public abstract class RecyclerViewScrollDetector extends RecyclerView.OnScrollListener {
    private int mLastScrollY; // 第一个可视的item的顶部坐标

    private int mPreviousFirstVisibleItem; // 上一次滑动的第一个可视item的索引值

    private RecyclerView mListView;// 列表控件，如ListView

    /**
     * 滑动距离响应的临界值，这个值可根据需要自己指定 只有只有滑动距离大于mScrollThreshold，才会响应滑动动作
     */
    private int mScrollThreshold = 50;

    private int mNewState = 0;

    public RecyclerViewScrollDetector() {
    }

    // 当认为ListView向上滑动时会被调用，由子类去定义的。
    protected abstract void onScrollUp();

    // 当认为ListView下滑动时会被调用，由子类去定义的。
    protected abstract void onScrollDown();

    public void onItemStateChanged(int firstVisibleItem, int lastVisibleItem, int visibleCount){

    }

    public void onTouchTop(){

    }

    public void onTouchBottom(){

    }


    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        mNewState = newState;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);


        //onScrollStateChanged 方法
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        //判断是当前layoutManager是否为LinearLayoutManager
        //只有LinearLayoutManager才有查找第一个和最后一个可见view位置的方法
        if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
            //获取最后一个可见view的位置
            int lastItemPosition = linearManager.findLastVisibleItemPosition();
            //获取第一个可见view的位置
            int firstItemPosition = linearManager.findFirstVisibleItemPosition();
            int visibleCount = lastItemPosition - firstItemPosition + 1;

            RecyclerView.Adapter adapter = recyclerView.getAdapter();
            int totalCount = adapter.getItemCount();
            onScroll(recyclerView, firstItemPosition, visibleCount, totalCount);
            if (mNewState == RecyclerView.SCROLL_STATE_IDLE && lastItemPosition + 1 == adapter.getItemCount()) {
                //最后一个itemView的position为adapter中最后一个数据时,说明该itemView就是底部的view了
                //需要注意position从0开始索引,adapter.getItemCount()是数据量总数
                onTouchBottom();
            }
            //同理检测是否为顶部itemView时,只需要判断其位置是否为0即可
            if (mNewState == RecyclerView.SCROLL_STATE_IDLE && firstItemPosition == 0) {
                onTouchTop();
            }
        }

//        int newScrollY = this.getTopItemScrollY();
//        // 判断滑动距离是否大于 mScrollThreshold
//        boolean isSignificantDelta = Math
//                .abs(this.mLastScrollY - newScrollY) > this.mScrollThreshold;
//        if (isSignificantDelta) {
//            // 对于第一个可视的item，根据其前后两次的顶部坐标判断滑动方向
//            if (this.mLastScrollY > newScrollY) {
//                this.onScrollUp();
//            } else {
//                this.onScrollDown();
//            }
//        }
//        this.mLastScrollY = newScrollY;

    }

    // 核心方法，该方法封装了滑动方向判断的逻辑，但ListView产生滑动之后，该方法会被调用。
    // 1.首先，判断滑动后第一个可视的item和滑动前是否同一个，如果是同一个，进入第2步，否则进入第3步
    // 2.则这次滑动距离小于一个Item的高度，比较第一个可视的item的顶部坐标在滑动前后的差值，就知道了滑动的距离
    // 3.这个好办，直接比较滑动前后firstVisibleItem的值就可以判断滑动方向了。
    public void onScroll(RecyclerView view, int firstVisibleItem, int visibleItemCount,
                         int totalItemCount) {
        if (totalItemCount != 0) {
            // 滑动距离：不超过一个item的高度
            if (this.isSameRow(firstVisibleItem)) {
                int newScrollY = this.getTopItemScrollY();
                // 判断滑动距离是否大于 mScrollThreshold
                boolean isSignificantDelta = Math
                        .abs(this.mLastScrollY - newScrollY) > this.mScrollThreshold;
                if (isSignificantDelta) {
                    // 对于第一个可视的item，根据其前后两次的顶部坐标判断滑动方向
                    if (this.mLastScrollY > newScrollY) {
                        this.onScrollUp();
                    } else {
                        this.onScrollDown();
                    }
                }
                this.mLastScrollY = newScrollY;
            } else {// 根据第一个可视Item的索引值不同，判断滑动方向

                onItemStateChanged(firstVisibleItem, firstVisibleItem + visibleItemCount-1, visibleItemCount);

                if (firstVisibleItem > this.mPreviousFirstVisibleItem) {
                    this.onScrollUp();
                } else {
                    this.onScrollDown();
                }
                this.mLastScrollY = this.getTopItemScrollY();
                this.mPreviousFirstVisibleItem = firstVisibleItem;
            }
        }
    }

    public void setScrollThreshold(int scrollThreshold) {
        this.mScrollThreshold = scrollThreshold;
    }

    public void setRecyclerView(@NonNull RecyclerView listView) {
        this.mListView = listView;
    }

    private boolean isSameRow(int firstVisibleItem) {
        return firstVisibleItem == this.mPreviousFirstVisibleItem;
    }

    private int getTopItemScrollY() {
        if (this.mListView != null && this.mListView.getChildAt(0) != null) {
            View topChild = this.mListView.getChildAt(0);
            return topChild.getTop();
        } else {
            return 0;
        }
    }
}
