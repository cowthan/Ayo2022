package org.ayo.template.recycler;

import org.ayo.template.status.DefaultStatus;
import org.ayo.template.status.DefaultStatusProvider;
import org.ayo.template.status.StatusProvider;
import org.ayo.template.status.StatusUIManager;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

/**
 */
public abstract class AyoListFragment<T> extends AyoRefreshable{


    public void onLoadOk(List<T> t){
        _refreshList(t, isLoadMore);
    }

    protected List<T> getData(){
        return list;
    }

    public void onLoadFail(boolean forceChangeUIWhenHasData, String status, String errorReason, int errorCode){
        boolean isUIChanged = false;
        if(list == null || list.size() == 0){
            showStatus(status);
            isUIChanged = true;
        }else{
            if(forceChangeUIWhenHasData){
                showStatus(status);
                isUIChanged = true;
            }
        }
        notifyError(isUIChanged, status, errorReason, errorCode);
    }

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

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return generateLinearLayout(false, false);
    }

    @Override
    protected void initStatusUI(StatusUIManager statusUIManager) {
        statusUIManager.addStatusProvider(DefaultStatus.STATUS_LOADING, new DefaultStatusProvider.DefaultLoadingStatusView(getActivity(), DefaultStatus.STATUS_LOADING, mXRecyclerView, new StatusProvider.OnStatusViewCreateCallback() {
            @Override
            public void onCreate(String status, View statusView) {

            }
        }));

        statusUIManager.addStatusProvider(DefaultStatus.STATUS_EMPTY, new DefaultStatusProvider.DefaultEmptyStatusView(getActivity(), DefaultStatus.STATUS_EMPTY, mXRecyclerView, new StatusProvider.OnStatusViewCreateCallback() {
            @Override
            public void onCreate(String status, View statusView) {

            }
        }));

        statusUIManager.addStatusProvider(DefaultStatus.STATUS_SERVER_ERROR, new DefaultStatusProvider.DefaultServerErrorStatusView(getActivity(), DefaultStatus.STATUS_SERVER_ERROR, mXRecyclerView, new StatusProvider.OnStatusViewCreateCallback() {
            @Override
            public void onCreate(String status, View statusView) {

            }
        }));

        statusUIManager.addStatusProvider(DefaultStatus.STATUS_LOGIC_FAIL, new DefaultStatusProvider.DefaultLogicFailStatusView(getActivity(), DefaultStatus.STATUS_LOGIC_FAIL, mXRecyclerView, new StatusProvider.OnStatusViewCreateCallback() {
            @Override
            public void onCreate(String status, View statusView) {

            }
        }));

        statusUIManager.addStatusProvider(DefaultStatus.STATUS_NETOFF, new DefaultStatusProvider.DefaultNetOffStatusView(getActivity(), DefaultStatus.STATUS_NETOFF, mXRecyclerView, new StatusProvider.OnStatusViewCreateCallback() {
            @Override
            public void onCreate(String status, View statusView) {

            }
        }));

        statusUIManager.addStatusProvider(DefaultStatus.STATUS_lOCAL_ERROR, new DefaultStatusProvider.DefaultLocalErrorStatusView(getActivity(), DefaultStatus.STATUS_lOCAL_ERROR, mXRecyclerView, new StatusProvider.OnStatusViewCreateCallback() {
            @Override
            public void onCreate(String status, View statusView) {

            }
        }));
    }


}
