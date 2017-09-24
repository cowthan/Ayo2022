package org.ayo.ui.sample.material;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.ayo.sample.R;
import org.ayo.ui.sample.DemoFragmentAdapter;
import org.ayo.ui.sample.base.AyoActivity;

/**
 * CoordinatorLayout和TabLayout
 */

public class DemoTabLayoutActivity   extends AyoActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.ac_demo_m_tablayout;
    }

    @Override
    protected void onCreate2(View view, @Nullable Bundle bundle) {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        ViewPager viewpager = (ViewPager) findViewById(R.id.viewpager);

        viewpager.setAdapter(new DemoFragmentAdapter(getActivity(), getSupportFragmentManager()));

        //方法1：这句就可以把TabLayout和ViewPager连起来，Adapter的getPageTitle返回的就是tab的文字，只支持文字，当然也可以返回SpannableString
        //tabs.setupWithViewPager(viewpager);

        //方法2：
    }

    @Override
    protected void onDestroy2() {

    }

    @Override
    protected void onPageVisibleChanged(boolean b, boolean b1, @Nullable Bundle bundle) {

    }

}
