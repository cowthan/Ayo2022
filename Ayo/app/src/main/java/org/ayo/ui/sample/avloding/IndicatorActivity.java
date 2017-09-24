package org.ayo.ui.sample.avloding;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import org.ayo.list.progress.AVLoadingIndicatorView;
import org.ayo.sample.R;
import org.ayo.ui.sample.BasePage;

/**
 * Created by Jack Wang on 2016/8/5.
 */

public class IndicatorActivity extends BasePage {

    private AVLoadingIndicatorView avi;

    @Override
    protected int getLayoutId() {
        return R.layout.avloding_activity_indicator;
    }

    @Override
    protected void onCreate2(View view, @Nullable Bundle bundle) {
        int indicator = getArguments().getInt("indicator");
        avi= (AVLoadingIndicatorView) findViewById(R.id.avi);
        avi.setIndicator(indicator, Color.WHITE);

        findViewById(R.id.btn_show).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                avi.setVisibility(View.VISIBLE);
                // or avi.smoothToShow();
            }
        });

        findViewById(R.id.btn_hide).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                avi.setVisibility(View.GONE);
                // or avi.smoothToHide();
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
