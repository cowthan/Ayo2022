package org.ayo.ui.sample.view_learn;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import org.ayo.sample.R;
import org.ayo.ui.sample.BasePage;

/**
 * Created by Administrator on 2016/11/26.
 */

public class CircleLoadingViewDemo extends BasePage {


    @Override
    protected int getLayoutId() {
        return R.layout.ac_demo_circle_progress_view;
    }

    @Override
    protected void onCreate2(View view, @Nullable Bundle bundle) {
        CircleLoadingView clv = (CircleLoadingView) findViewById(R.id.clv);
        clv.setSweepValue(180);
    }

    @Override
    protected void onDestroy2() {

    }

    @Override
    protected void onPageVisibleChanged(boolean b, boolean b1, @Nullable Bundle bundle) {

    }
}
