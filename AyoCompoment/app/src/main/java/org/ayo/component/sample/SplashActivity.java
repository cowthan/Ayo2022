package org.ayo.component.sample;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;

import org.ayo.component.MasterActivity;


/**
 * Created by Administrator on 2016/12/20.
 */

public class SplashActivity extends MasterActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.ac_splash;
    }

    @Override
    protected void onCreate2(View contentView, @Nullable Bundle savedInstanceState) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(getActivity(), MainActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.base_fade_in, R.anim.base_fade_out);
                finish();
            }
        }, 1500);
    }

    @Override
    protected void onDestroy2() {

    }

    @Override
    protected void onPageVisibleChanged(boolean visible, boolean isFirstTimeVisible, @Nullable Bundle savedInstanceState) {

    }

}
