package com.worse.more.breaker.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.worse.more.breaker.R;
import com.worse.more.breaker.ui.account.PersonalCenterFragment;

import org.ayo.component.MasterFragment;
import org.ayo.component.indicator.PageIndicator;
import org.ayo.component.indicator.TypicalIndicator;
import org.ayo.component.indicator.TypicalPageInfo;
import org.ayo.component.pages.MasterPagerFragment;

/**
 * Created by Administrator on 2017/1/13 0013.
 */

public class MainFragment extends MasterPagerFragment {

    protected int getLayoutId() {
        return org.ayo.component.R.layout.ayo_page_viewpager;
    }

    @Override
    protected MasterFragment[] createFragments() {
        return new MasterFragment[]{
                new MainPageFragment(),
                new SampleFragment(),
                new SampleFragment(),
                new PersonalCenterFragment()
        };
    }

    @Override
    protected int getDefaultItemPosition() {
        return 0;
    }

    @Override
    protected PageIndicator createPageIndicator() {
        TypicalIndicator v = new TypicalIndicator(getActivity());
        v.addTab(new TypicalPageInfo(R.drawable.typical_ic_weixin_normal, R.drawable.typical_ic_weixin_pressed, "首页"));
        v.addTab(new TypicalPageInfo(R.drawable.typical_ic_weixin_normal, R.drawable.typical_ic_weixin_pressed, "首页2"));
        v.addTab(new TypicalPageInfo(R.drawable.typical_ic_weixin_normal, R.drawable.typical_ic_weixin_pressed, "首页3"));
        v.addTab(new TypicalPageInfo(R.drawable.typical_ic_weixin_normal, R.drawable.typical_ic_weixin_pressed, "首页4"));
        //v.setupWithViewPager();

        v.setBackgroundColor(0x77e60012);
        return v;
    }

    @Override
    protected void onPageVisibleChanged(boolean visible, boolean isFirstTimeVisible, @Nullable Bundle savedInstanceState) {
        super.onPageVisibleChanged(visible, isFirstTimeVisible, savedInstanceState);
    }
}
