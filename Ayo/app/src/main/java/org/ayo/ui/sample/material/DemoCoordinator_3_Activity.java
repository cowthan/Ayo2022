package org.ayo.ui.sample.material;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.View;

import org.ayo.sample.R;
import org.ayo.ui.sample.base.AyoActivity;

/**
 * CoordinatorLayout + FloatActionButton
 *
 */

public class DemoCoordinator_3_Activity  extends AyoActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.ac_demo_m_coordinator_3;
    }

    @Override
    protected void onCreate2(View view, @Nullable Bundle bundle) {
        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Snackbar.make(view,"FloatActionButton被点击了",Snackbar.LENGTH_LONG)
                        .setAction("cancel", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //这里的单击事件代表点击消除Action后的响应事件

                            }
                        })
                        .show();
            }
        });
    }

    @Override
    protected void onDestroy2() {

    }

    @Override
    protected void onPageVisibleChanged(boolean b, boolean b1, @Nullable Bundle bundle) {

    }
}
