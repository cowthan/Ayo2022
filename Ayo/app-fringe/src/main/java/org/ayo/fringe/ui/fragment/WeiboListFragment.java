package org.ayo.fringe.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import org.ayo.UserCenter;
import org.ayo.core.Lang;
import org.ayo.fringe.R;
import org.ayo.http.callback.BaseHttpCallback;
import org.ayo.http.callback.FailInfo;
import org.ayo.http.callback.HttpProblem;
import org.ayo.list.adapter.AyoItemTemplate;
import org.ayo.notify.toaster.Toaster;
import org.ayo.template.recycler.AyoListTemplateFragment;
import org.ayo.template.recycler.condition.AyoCondition;
import org.ayo.template.status.DefaultStatus;
import org.ayo.template.status.StatusProvider;
import org.ayo.template.status.StatusUIManager;
import org.ayo.fringe.api2.WeiboApi;
import org.ayo.fringe.event.LoginOkEvent;
import org.ayo.fringe.model.ResponseTimeline;
import org.ayo.fringe.model.timeline.Timeline;
import org.ayo.fringe.ui.LoginActivity;
import org.ayo.fringe.ui.adapter.TimeLineWeiboAdapter;
import org.ayo.fringe.utils.Utils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;


/**
 * 微博列表页
 */
public class WeiboListFragment extends AyoListTemplateFragment<Timeline>  {

    private static final String STATUS_NOT_LOGIN_TO_WEIBO = "not login to weibo";

    @Override
    protected void initStatusUI(StatusUIManager statusUIManager) {
        super.initStatusUI(statusUIManager);
        statusUIManager.addStatusProvider(STATUS_NOT_LOGIN_TO_WEIBO,
                new NotLoginToWeiboStatusView(getActivity(),
                        DefaultStatus.STATUS_LOADING, mXRecyclerView, new StatusProvider.OnStatusViewCreateCallback() {
            @Override
            public void onCreate(String status, View statusView) {
                View v = statusView.findViewById(R.id.btn_retry);
                v.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        LoginActivity.start(getActivity());
                    }
                });
            }
        }));
    }

    @Override
    protected void onCreate2(View view, @Nullable Bundle bundle) {
        super.onCreate2(view, bundle);
        EventBus.getDefault().register(this);

        UserCenter.getDefault().addLoginStatusListener(new UserCenter.OnLoginStatusChangeListener() {
            @Override
            public void onLogin(Object user) {
                refreshAuto();
            }

            @Override
            public void onLogout(Object user) {

            }
        });
    }

    @Override
    protected void onDestroy2() {
        EventBus.getDefault().unregister(this);
        super.onDestroy2();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceive(LoginOkEvent e){

    }

    @Override
    public void loadCache() {

    }

    @Override
    public void loadData(AyoCondition cond) {

        String weiboToken = Utils.getWeiboToken(); //WeiboAccessTokenKeeper.readAccessToken(App.app).getToken();
        if(Lang.isEmpty(weiboToken)){
            showStatus(STATUS_NOT_LOGIN_TO_WEIBO);
        }else{
            WeiboApi.getPublicTimelines("timeline", cond.page, weiboToken, new BaseHttpCallback<ResponseTimeline>() {
                @Override
                public void onFinish(boolean isSuccess, HttpProblem problem, FailInfo resp, ResponseTimeline r) {
                    if(isSuccess){
                        onLoadOk(r.statuses);
                    }else{
                        if(resp.code == 400){
                            showStatus(STATUS_NOT_LOGIN_TO_WEIBO);
                            stopRefreshOrLoadMore(isLoadMore);
                        }else{
                            showStatus(STATUS_NOT_LOGIN_TO_WEIBO);
//                            onLoadFail(false, DefaultStatus.STATUS_SERVER_ERROR, resp.reason, resp.code);
                            Toaster.toastShort(resp.reason);
                        }
                    }
                }
            });
        }


    }


    @Override
    protected List<AyoItemTemplate> getTemplate() {
        List<AyoItemTemplate> templates = new ArrayList<>();
        templates.add(new TimeLineWeiboAdapter(getActivity()));
        return templates;
    }


    @Override
    public AyoCondition initCondition() {
        return new TimeLineCondition(1);
    }


    private class TimeLineCondition extends AyoCondition{

        public TimeLineCondition(int pageStart) {
            super(pageStart);
        }

    }

    public static class NotLoginToWeiboStatusView extends StatusProvider {

        public NotLoginToWeiboStatusView(Context context, String status, View contentView, OnStatusViewCreateCallback callback) {
            super(context, status, contentView, callback);
        }

        @Override
        public View getStatusView() {
            return View.inflate(mContext, R.layout.status_view_not_login_to_weibo, null);
        }
    }

}
