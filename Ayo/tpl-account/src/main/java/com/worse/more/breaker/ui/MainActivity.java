package com.worse.more.breaker.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.worse.more.breaker.R;

public class MainActivity extends BaseActivity {

    public static void start(Context c){
        Intent i = new Intent(c, MainActivity.class);
        if(c instanceof Activity){

        }else{
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        c.startActivity(i);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.ayo_fragment_container;
    }

    @Override
    protected void onCreate2(View contentView, @Nullable Bundle savedInstanceState) {
        if(savedInstanceState == null){
            loadRootFragment(R.id.body, new MainFragment());
        }
    }

    @Override
    protected void onDestroy2() {

    }

    @Override
    protected void onPageVisibleChanged(boolean visible, boolean isFirstTimeVisible, @Nullable Bundle savedInstanceState) {

    }

}
