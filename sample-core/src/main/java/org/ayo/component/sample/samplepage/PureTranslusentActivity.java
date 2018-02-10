package org.ayo.component.sample.samplepage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import org.ayo.component.MasterActivity;
import org.ayo.component.sample.R;

/**
 * Created by Administrator on 2017/5/21.
 */

public class PureTranslusentActivity extends MasterActivity {
    public static void start(Activity a, boolean newTask){
        Intent intent = new Intent(a, PureTranslusentActivity.class);
        if(newTask) intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        a.startActivity(intent);
    }
    @Override
    protected int getLayoutId() {
        return R.layout.ac_fragment_container;
    }


    @Override
    protected void onCreate2(View contentView, @Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void onDestroy2() {

    }

    @Override
    protected void onPageVisibleChanged(boolean visible, boolean isFirstTimeVisible, @Nullable Bundle savedInstanceState) {

    }
}
