package org.ayo.ui.sample;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


/**
 * Created by Administrator on 2016/11/29.
 */

public class DemoFragmentAdapter extends FragmentPagerAdapter {

    private static final int PAGE_COUNT = 3;

    private Context mContext;

    public DemoFragmentAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        return DemoFragment.newInstance("fragment-" + position);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "广场";
            case 1:
                return "好友";
            case 2:
                return "我";
            default:
                return "微博";
        }
    }
}
