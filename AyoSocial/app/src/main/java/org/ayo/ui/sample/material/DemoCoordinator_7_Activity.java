package org.ayo.ui.sample.material;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import org.ayo.sample.R;
import org.ayo.ui.sample.base.AyoActivity;
import org.ayo.view.widget.TitleBar;

/**
 * CoordinatorLayout自定义Behavior，自己实现滚动特效
 */

public class DemoCoordinator_7_Activity extends AyoActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.ac_demo_m_coordinator_7;
    }

    @Override
    protected void onCreate2(View view, @Nullable Bundle bundle) {
        TitleBar titleBar = (TitleBar) findViewById(R.id.titlebar);
        titleBar.title("What the fuck !")
                .bgColor(Color.parseColor("#e60012"));
    }

    @Override
    protected void onDestroy2() {

    }

    @Override
    protected void onPageVisibleChanged(boolean b, boolean b1, @Nullable Bundle bundle) {

    }

}
