package org.ayo.ui.sample.scroll;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import org.ayo.sample.R;
import org.ayo.ui.sample.BaseActivity;

/**
 * Created by qiaoliang on 2017/9/30.
 */

public class DemoTouchActivity extends BaseActivity {


    FrameLayout container1;
    LinearLayout container2;
    View view1, view2;

    @Override
    protected int getLayoutId() {
        return R.layout.ac_demo_scroll_1;
    }

    @Override
    protected void onCreate2(View view, @Nullable Bundle bundle) {
        container1 = id(R.id.container1);
        container2 = id(R.id.container2);
        view1 = id(R.id.view1);
        view2 = id(R.id.view2);

        container1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return onTouchEvent(v, event);
            }
        });
    }

    private int lastX = 0;
    private int lastY = 0;
    public boolean onTouchEvent(View v, MotionEvent e) {
        int rawX = (int) e.getRawX();
        int rawY = (int) e.getRawY();
        switch (e.getAction()){
            case MotionEvent.ACTION_DOWN:
                lastX = rawX;
                lastY = rawY;
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }

    @Override
    protected void onDestroy2() {

    }

    @Override
    protected void onPageVisibleChanged(boolean b, boolean b1, @Nullable Bundle bundle) {

    }
}
