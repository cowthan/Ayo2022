package org.ayo.component.pages;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import org.ayo.component.MasterFragment;
import org.ayo.component.R;
import org.ayo.component.indicator.OnTabSelectedListener;
import org.ayo.component.indicator.PageIndicator;
import org.ayo.fragmentation.SupportFragment;
import org.ayo.fragmentation.anim.FragmentAnimator;

/**
 * 承载了几个Tab页和一个Indicator，每一个Tab页是一个MasterFragment
 */

public abstract class MasterTabFragment extends MasterFragment{

    protected abstract MasterFragment[] createFragments();
    protected abstract int getDefaultItemPosition();
    protected abstract PageIndicator createPageIndicator();

    protected PageIndicator pageIndicator;
    protected MasterFragment currentFragment;
    protected MasterFragment[] mFragments;
    protected FrameLayout indicator_container;
    protected FrameLayout fl_container;

    protected PageIndicator getPageIndicator(){
        return pageIndicator;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.ayo_page_tab;
    }

    @Override
    protected void onCreate2(View contentView, @Nullable Bundle savedInstanceState) {
        indicator_container = id(R.id.indicator_container);
        fl_container = id(R.id.fl_container);
        initPages(savedInstanceState);
    }

//    protected void setIndicatorLocation(int gravity, boolean overlay){
//        if(gravity != Gravity.TOP && gravity != Gravity.BOTTOM){
//            throw new RuntimeException("只支持Gravity.TOP和Gravity.BOTTO");
//        }
//        RelativeLayout.LayoutParams lpContent = (RelativeLayout.LayoutParams)fl_container.getLayoutParams();
//        RelativeLayout.LayoutParams lpIndicator = (RelativeLayout.LayoutParams)indicator_container.getLayoutParams();
//
//        lpContent.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
//        lpContent.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
//        if(gravity == Gravity.TOP) {
//            lpContent.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
//            lpContent.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);;
//            lpIndicator.addRule(RelativeLayout.ALIGN_PARENT_TOP);
//            lpIndicator.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
//            if(overlay){
//                lpContent.addRule(RelativeLayout.ABOVE, 0);
//                lpContent.addRule(RelativeLayout.BELOW, 0);
//            }else{
//                lpContent.addRule(RelativeLayout.ABOVE, 0);
//                lpContent.addRule(RelativeLayout.BELOW, R.id.indicator_container);
//            }
//
//        }else{
//            lpIndicator.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//            if(overlay){
//                lpContent.addRule(RelativeLayout.ABOVE, 0);
//                lpContent.addRule(RelativeLayout.BELOW, 0);
//            }else{
//                lpContent.addRule(RelativeLayout.ABOVE, R.id.indicator_container);
//                lpContent.addRule(RelativeLayout.BELOW, 0);
//            }
//
//        }
//        fl_container.setLayoutParams(lpContent);
//        indicator_container.setLayoutParams(lpIndicator);
//    }



    @Override
    protected void onPageVisibleChanged(boolean visible, boolean isFirstTimeVisible, @Nullable Bundle savedInstanceState) {
        if(isVisible()){
            if(isFirstTimeVisible){
                //initPages(savedInstanceState);
            }else{

            }
        }else{

        }
    }

    private int currentPosition = 0;
    private void initPages(Bundle savedInstanceState){
        currentPosition = getDefaultItemPosition();
        if (savedInstanceState == null) {
            mFragments = createFragments();
            loadMultipleRootFragment(R.id.fl_container, currentPosition, mFragments);
            currentFragment = mFragments[currentPosition];
        } else {
            // 这里库已经做了Fragment恢复,所有不需要额外的处理了, 不会出现重叠问题

            // 这里我们需要拿到mFragments的引用,也可以通过getSupportFragmentManager.getFragments()自行进行判断查找(效率更高些),用下面的方法查找更方便些
            mFragments = new MasterFragment[getChildFragmentManager().getFragments().size()];
            for(int i = 0; i < getChildFragmentManager().getFragments().size(); i++){
                mFragments[i] = (MasterFragment) getChildFragmentManager().getFragments().get(i);
                if(mFragments[i].isVisible()){
                    currentFragment = mFragments[i];
                    currentPosition = i;
                }
            }
        }

        pageIndicator = createPageIndicator();
        pageIndicator.setCurrentItem(getDefaultItemPosition());
        indicator_container.addView((View) pageIndicator, createIndicatorParams());
        setupIndicator();
    }

    protected FrameLayout.LayoutParams createIndicatorParams(){
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        return lp;
    }

    @Override
    protected void onDestroy2() {

    }

    @Override
    protected FragmentAnimator onCreateFragmentAnimator() {
        return super.onCreateFragmentAnimator();
    }

    public void setCurrentItem(int position){
        showHideFragment(mFragments[position]);
        currentFragment = mFragments[position];
        if(onPageChangeListener != null) onPageChangeListener.onPageSelected(position);
    }

    public int getCurrentItem(){
        return currentPosition;
    }

    protected void setupIndicator() {

        pageIndicator.setOnTabSelectListener(new OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position, int prePosition) {
                showHideFragment(mFragments[position], mFragments[prePosition]);
                currentFragment = mFragments[position];
                if(onPageChangeListener != null) onPageChangeListener.onPageSelected(position);
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {
                SupportFragment currentFragment = mFragments[position];
                int count = currentFragment.getChildFragmentManager().getBackStackEntryCount();

                Log.i("Tab Fragment", "onTabReselected--" + position);
            }
        });
    }

    private ViewPager.OnPageChangeListener onPageChangeListener;

    protected void setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener){
        this.onPageChangeListener = onPageChangeListener;
    }
}
