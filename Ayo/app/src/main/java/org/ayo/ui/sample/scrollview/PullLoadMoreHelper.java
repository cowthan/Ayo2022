package org.ayo.ui.sample.scrollview;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qiaoliang on 2017/3/24.
 */

public abstract class PullLoadMoreHelper<T extends View> {

    protected T wrappedView;
    protected OnPullListener onPullListener;

    public void attach(T wrappedView){
        this.wrappedView = wrappedView;
        setOnScrollChangeListenersToWrapperView(wrappedView);
    }

    public void setOnPullListener(OnPullListener l){
        this.onPullListener = l;
    }


    public interface OnMyScrollChangeListener {
        /**
         * Called when the scroll position of a view changes.
         *
         * @param v The view whose scroll position has changed.
         * @param scrollX Current horizontal scroll origin.
         * @param scrollY Current vertical scroll origin.
         * @param oldScrollX Previous horizontal scroll origin.
         * @param oldScrollY Previous vertical scroll origin.
         */
        void onScrollChange(View v, int scrollX, int scrollY,
                            int oldScrollX, int oldScrollY);
    }

    protected List<OnMyScrollChangeListener> onScrollChangeListeners = new ArrayList<>();

    public void addOnScrollChangeListener(OnMyScrollChangeListener l){
        onScrollChangeListeners.add(l);
    }


    protected void onMyScrollChange(T v, int scrollX, int scrollY, int oldScrollX, int oldScrollY){
        if(alreadyBottom(v, scrollX, scrollY, oldScrollX, oldScrollY)){
            onPullListener.onPullLoadMore();
        }else{

        }

        for(OnMyScrollChangeListener l: onScrollChangeListeners){
            l.onScrollChange(v, scrollX, scrollY, oldScrollX, oldScrollY);
        }
    }

    protected abstract void setOnScrollChangeListenersToWrapperView(T t);
    protected abstract boolean alreadyBottom(T view, int scrollX, int scrollY, int oldScrollX, int oldScrollY);

}
