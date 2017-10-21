package com.orgnet.getdown;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;

import org.ayo.component.MasterFragment;
import org.ayo.component.StateModel;
import org.ayo.component.indicator.PageIndicator;
import org.ayo.component.indicator.TypicalIndicator;
import org.ayo.component.indicator.TypicalPageInfo;
import org.ayo.component.pages.MasterPagerFragment;

/**
 * Created by Administrator on 2017/1/5.
 */

public class DemoPagerFragment extends MasterPagerFragment {

    @Override
    protected int getLayoutId() {
        if(getArguments().getInt("layout", -1) != -1){
            return getArguments().getInt("layout");
        }
        return super.getLayoutId();
    }

    @Override
    protected StateModel createStateModel() {
        return null;
    }

    @Override
    protected void onCreate2(View contentView, @Nullable Bundle savedInstanceState) {
        super.onCreate2(contentView, savedInstanceState);
        getViewPager().addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected MasterFragment[] createFragments() {
        return new MasterFragment[]{
                new LogPage(),
                new LogPage(),
                new LogPage()
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
        //v.setupWithViewPager();
        return v;
    }

    @Override
    protected void onPageVisibleChanged(boolean visible, boolean isFirstTimeVisible, @Nullable Bundle savedInstanceState) {
        super.onPageVisibleChanged(visible, isFirstTimeVisible, savedInstanceState);
    }
}
