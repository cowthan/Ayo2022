package org.ayo.component.pages;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import org.ayo.component.MasterFragment;
import org.ayo.component.indicator.OnTabSelectedListener;
import org.ayo.component.indicator.PageIndicator;
import org.ayo.kit.R;

/**
 * 承载了一个ViewPager和一个Indicator，每一个Tab页是一个MasterFragment
 */

public abstract class MasterPagerFragment extends MasterFragment{

    protected abstract MasterFragment[] createFragments();
    protected abstract int getDefaultItemPosition();
    protected abstract PageIndicator createPageIndicator();

    protected PageIndicator pageIndicator;
    protected MasterFragment[] mFragments;
    protected FrameLayout indicator_container;
    ViewPager mViewPager ;

    final protected PageIndicator getPageIndicator(){
        return pageIndicator;
    }

    final protected ViewPager getViewPager(){
        return mViewPager;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.ayo_page_viewpager;
    }

    @Override
    protected void onCreate2(View contentView, @Nullable Bundle savedInstanceState) {
        mViewPager = (ViewPager) contentView.findViewById(R.id.viewpager);
        indicator_container = id(R.id.indicator_container);
        initPages();
    }
    
    private void initPages(){
        
        mFragments = createFragments();
        mViewPager.setAdapter(new SimpplePagerFragmentAdapter(getChildFragmentManager()));

        int defaultIndex = getDefaultItemPosition();

        pageIndicator = createPageIndicator();
        if(pageIndicator != null){
            pageIndicator.setCurrentItem(defaultIndex);
            indicator_container.addView((View) pageIndicator, createIndicatorParams());
            setupIndicator();
        }

        mViewPager.setCurrentItem(defaultIndex);
    }

    final protected FrameLayout.LayoutParams createIndicatorParams(){
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        return lp;
    }

    @Override
    protected void onPageVisibleChanged(boolean visible, boolean isFirstTimeVisible, @Nullable Bundle savedInstanceState) {
        if(visible){
            if(isFirstTimeVisible){
                //initPages();
            }else{
                
            }
        }else{
            
        }
    }

//    protected void setIndicatorLocation(int gravity, boolean overlay){
//        if(gravity != Gravity.TOP && gravity != Gravity.BOTTOM){
//            throw new RuntimeException("只支持Gravity.TOP和Gravity.BOTTO");
//        }
//        RelativeLayout.LayoutParams lpContent = (RelativeLayout.LayoutParams)mViewPager.getLayoutParams();
//        RelativeLayout.LayoutParams lpIndicator = (RelativeLayout.LayoutParams)indicator_container.getLayoutParams();
//
//        lpContent.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
//        lpContent.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
//        if(gravity == Gravity.TOP) {
//            lpIndicator.addRule(RelativeLayout.ALIGN_PARENT_TOP);
//            if(overlay){
//                lpContent.addRule(RelativeLayout.ABOVE, 0);
//                lpContent.addRule(RelativeLayout.BELOW, 0);
//            }else{
//                lpContent.addRule(RelativeLayout.ABOVE, 0);
//                lpContent.addRule(RelativeLayout.BELOW, R.id.indicator_container);
//            }
//
//        }else{
//            lpContent.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
//            if(overlay){
//                lpContent.addRule(RelativeLayout.ABOVE, 0);
//                lpContent.addRule(RelativeLayout.BELOW, 0);
//            }else{
//                lpContent.addRule(RelativeLayout.ABOVE, R.id.indicator_container);
//                lpContent.addRule(RelativeLayout.BELOW, 0);
//            }
//        }
//        mViewPager.setLayoutParams(lpContent);
//        indicator_container.setLayoutParams(lpIndicator);
//    }

    @Override
    protected void onDestroy2() {

    }



    final protected void setupIndicator() {

        pageIndicator.setOnTabSelectListener(new OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position, int prePosition) {
                mViewPager.setCurrentItem(position, false);
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {
                MasterFragment currentFragment = mFragments[position];
                int count = currentFragment.getChildFragmentManager().getBackStackEntryCount();

                Log.i("Tab Fragment", "onTabReselected--" + position);
            }
        });

        pageIndicator.setupWithViewPager(mViewPager);
    }

    private final class SimpplePagerFragmentAdapter extends FragmentPagerAdapter {

        public SimpplePagerFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
           return mFragments[position];
        }

        @Override
        public int getCount() {
            return mFragments.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragments[position].getClass().getSimpleName();
        }
    }
}
