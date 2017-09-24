package com.worse.more.breaker.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.worse.more.breaker.R;
import com.worse.more.breaker.ui.account.LoginActivity;

import org.ayo.core.Async;

/**
 * Created by Administrator on 2016/7/27.
 */
public class SplashActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.br_ac_splash;
    }

    @Override
    protected void onCreate2(View contentView, @Nullable Bundle savedInstanceState) {
        Async.post(new Runnable() {
            @Override
            public void run() {
                LoginActivity.start(getActivity());
                finish();
            }
        }, 2000);
    }

    @Override
    protected void onDestroy2() {

    }

    @Override
    protected void onPageVisibleChanged(boolean visible, boolean isFirstTimeVisible, @Nullable Bundle savedInstanceState) {

    }

}
