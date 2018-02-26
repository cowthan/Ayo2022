package org.ayo.ui.sample;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;

import org.ayo.component.MasterActivity;
import org.ayo.sample.R;


/**
 */

public class SplashActivity extends MasterActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.ac_splash;
    }

    @Override
    protected void onCreate2(View view, @Nullable Bundle bundle) {




        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                Intent i = new Intent(getActivity(), ChatActivity.class);
                Intent i = new Intent(getActivity(), MainnActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.base_fade_in, R.anim.base_fade_out);

//                i = new Intent(getActivity(), HttpMainActivity.class);
//                startActivity(i);
//                overridePendingTransition(R.anim.base_fade_in, R.anim.base_fade_out);

                finish();
            }
        }, 1500);
    }

    @Override
    protected void onDestroy2() {

    }

    @Override
    protected void onPageVisibleChanged(boolean b, boolean b1, @Nullable Bundle bundle) {

    }

}
