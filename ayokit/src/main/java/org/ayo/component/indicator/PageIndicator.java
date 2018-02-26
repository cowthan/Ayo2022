package org.ayo.component.indicator;

import android.support.v4.view.ViewPager;

/**
 * Created by Administrator on 2017/1/5 0005.
 */

public interface PageIndicator<T extends PageIndicatorInfo> {

    public void addTab(T tabInfo);
    public void setOnTabSelectListener(OnTabSelectedListener onTabSelectedListener);
    public void setCurrentItem(int position);
    public int getCurrentItemPosition();
    public void setupWithViewPager(ViewPager pager);

}
