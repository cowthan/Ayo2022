package org.ayo.template.recycler;

import org.ayo.list.recycler.XRecyclerView;
import org.ayo.template.status.DefaultStatus;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;

/**
 */
public abstract class AyoListTemplateFragment<T> extends AyoListFragment<T> {


    boolean initAfterViewCreated = true;

    public void enableInitAfterViewCreated(boolean enable){
        initAfterViewCreated = enable;
    }

    @Override
    protected void onCreateViewFinished(View root, XRecyclerView mXRecyclerView) {
        if(initAfterViewCreated){
//            loadCache();
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    refreshAuto();
//                }
//            }, RANGE_FROM_CACHE_TO_REFRESH);
        }else{

        }

    }

    @Override
    protected void onPageVisibleChanged(boolean isVisibleToUser, boolean isFirstCome, @Nullable Bundle bundle) {
        super.onPageVisibleChanged(isVisibleToUser, isFirstCome, bundle);
        if(isVisibleToUser){
            if(isFirstCome){
                loadCache();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshAuto();
                    }
                }, RANGE_FROM_CACHE_TO_REFRESH);
            }else{
                //Toast.makeText(getActivity(), "又可见了", Toast.LENGTH_SHORT).show();
            }
        }else{

        }
    }


    public void refreshAuto(){
        clearStatus();
        mXRecyclerView.setRefreshing(true);
    }

    public void refreshWithLoadingStatus(){
        showStatus(DefaultStatus.STATUS_LOADING);
        condition.onPullDown();
        loadData(condition);
    }

    public abstract void loadCache();
//
//
//
//    @Override
//    public void loadData(AyoCondition cond) {
//
//    }
//
//    @Override
//    protected List<AyoItemTemplate> getTemplate() {
//        return null;
//    }
}
