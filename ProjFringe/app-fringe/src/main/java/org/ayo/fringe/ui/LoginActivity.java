package org.ayo.fringe.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import org.ayo.UserCenter;
import org.ayo.fringe.R;
import org.ayo.http.callback.BaseHttpCallback;
import org.ayo.http.callback.FailInfo;
import org.ayo.http.callback.HttpProblem;
import org.ayo.http.callback.NetWorkUtils;
import org.ayo.notify.toaster.Toaster;
import org.ayo.social.SocialCenter;
import org.ayo.social.callback.BaseShareCallback;
import org.ayo.social.callback.ShareCallback;
import org.ayo.social.model.SocialAccountInfo;
import org.ayo.view.progress.ProgressView;
import org.ayo.fringe.Config;
import org.ayo.fringe.api2.ApiUser;
import org.ayo.fringe.model.LoginUserInfo;
import org.ayo.fringe.ui.base.BaseFrgFragment;
import org.ayo.fringe.ui.base.Pages;
import org.ayo.fringe.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/4/20.
 */
public class LoginActivity extends BaseFrgFragment {

    public static void start(Activity c){
        Pages.startWithSwipeback(c, LoginActivity.class, null);
    }

    @BindView(R.id.pv_loading)
    ProgressView pv_loading;

    @BindView(R.id.rl_content)
    RelativeLayout rl_content;

    @BindView(R.id.btn_login)
    Button btn_login;

    @BindView(R.id.btn_in)
    Button btn_in;

    @Override
    protected int getLayoutId() {
        return R.layout.wb_ac_login;
    }

    @Override
    protected void onCreate2(View view, @Nullable Bundle bundle) {
        View r = findViewById(R.id.root);
        ButterKnife.bind(this, r);

        rl_content.setVisibility(View.GONE);

        if(NetWorkUtils.isConnected(getActivity())){
            checkWeiboAuthExpired();
        }else{
            Toaster.toastShort("5月20之前还不支持离线使用");
        }

        btn_login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    @Override
    protected void onDestroy2() {

    }

    @Override
    protected void onPageVisibleChanged(boolean b, boolean b1, @Nullable Bundle bundle) {

    }

    /**
     * 检查微博授权是否过时
     */
    private void checkWeiboAuthExpired() {
        if(Config.API.USE_RETROFIT){
//            WBUserApi api = RetrofitManager.getRetrofit().create(WBUserApi.class);
//            api.getUserInfo(Utils.getWeiboToken(), Utils.getCurrentWeiboUserUid(), "")
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Action1<LoginUserInfo>() {
//                        @Override
//                        public void call(LoginUserInfo loginUserInfo) {
//                            Toaster.toastShort("欢迎回来，" + loginUserInfo.screen_name);
//                            postJumpToMain();
//                        }
//                    }, new Action1<Throwable>() {
//                        @Override
//                        public void call(Throwable throwable) {
//                            rl_content.setVisibility(View.VISIBLE);
//                            pv_loading.setVisibility(View.GONE);
//                            Toaster.toastShort("请先通过微博登录授权");
//                        }
//                    });
        }else{
            ApiUser.getLoginUserInfo("获取当前登录用户信息",
                    Utils.getWeiboToken(),
                    Utils.getCurrentWeiboUserUid(),
                    "",
                    new BaseHttpCallback<LoginUserInfo>() {
                        @Override
                        public void onFinish(boolean isSuccess, HttpProblem problem, FailInfo resp, LoginUserInfo loginUserInfo) {
                            if (isSuccess) {
                                Toaster.toastShort("欢迎回来，" + loginUserInfo.screen_name);
                                postJumpToMain();
                            } else {
                                rl_content.setVisibility(View.VISIBLE);
                                pv_loading.setVisibility(View.GONE);
                                Toaster.toastShort("请先通过微博登录授权");
                            }
                        }
                    }
            );
        }
    }

    private void postJumpToMain(){
        MainPagerFragment.start(getActivity());
        finish();
    }


    private ShareCallback callback = new BaseShareCallback(){
        @Override
        public void onLoginSuccess(SocialAccountInfo info) {
            super.onLoginSuccess(info);
            Toaster.toastShort("登录成功--" + info.accessToken);
            Config.weibo.saveLoginInfo(info);
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            UserCenter.getDefault().notifyLoginOk(info);
            finish();
            //requestSocialLogin(info);
        }

        @Override
        public void onCancel() {
            super.onCancel();
            Toaster.toastShort("登录取消");
        }

        @Override
        public void onFail(@Nullable Exception e, String reason) {
            super.onFail(e, reason);
            Toaster.toastShort("登录失败");
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SocialCenter.getDefault().tryToGetWbCallback(requestCode, resultCode, data);
        SocialCenter.getDefault().tryToGetQQCallback(requestCode, resultCode, data);
    }

    public void login(){
        SocialCenter.getDefault().login(SocialCenter.PLATFORM_WB, getActivity(), callback);
    }

    @OnClick(R.id.btn_in)
    public void in(){
        MainPagerFragment.start(getActivity());
        finish();
    }

}
