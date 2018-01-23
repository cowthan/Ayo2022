package com.app.core.viewpager;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/28.
 *
 * ViewPager模板代码封装
 */

public class ViewPagerWrapper {

    public static ViewPagerWrapper bind(Activity activity, FragmentManager fm, ViewPager viewPager, List<Fragment> fragments, ViewPager.OnPageChangeListener onPageChangeListener){
        ViewPagerWrapper w = new ViewPagerWrapper();
        w.mViewPager = viewPager;
        w.mActivity = activity;
        w.mFragmentManager = fm;
        viewPager.addOnAdapterChangeListener(new ViewPager.OnAdapterChangeListener() {
            @Override
            public void onAdapterChanged(@NonNull ViewPager viewPager, @Nullable PagerAdapter oldAdapter, @Nullable PagerAdapter newAdapter) {

            }
        });
        viewPager.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {

            }
        });
        viewPager.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {

            }

            @Override
            public void onViewDetachedFromWindow(View v) {

            }
        });
        if(onPageChangeListener != null){
            viewPager.addOnPageChangeListener(onPageChangeListener);
        }
        if(fragments != null && fragments.size() > 0){
            FragmentPagerAdapter mAdapter = new MyFragmentAdapter(fm, fragments);
            viewPager.setAdapter(mAdapter);
        }
        viewPager.setOffscreenPageLimit(4);
        viewPager.setCurrentItem(0);
        return w;
    }

    public static ViewPagerWrapper bind(Activity activity,  ViewPager viewPager, List<View> views, ViewPager.OnPageChangeListener onPageChangeListener){
        ViewPagerWrapper w = new ViewPagerWrapper();
        w.mViewPager = viewPager;
        w.mActivity = activity;
        viewPager.addOnAdapterChangeListener(new ViewPager.OnAdapterChangeListener() {
            @Override
            public void onAdapterChanged(@NonNull ViewPager viewPager, @Nullable PagerAdapter oldAdapter, @Nullable PagerAdapter newAdapter) {

            }
        });
        viewPager.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {

            }
        });
        viewPager.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {

            }

            @Override
            public void onViewDetachedFromWindow(View v) {

            }
        });
        if(onPageChangeListener != null){
            viewPager.addOnPageChangeListener(onPageChangeListener);
        }
        if(views != null && views.size() > 0){
            PagerAdapter mAdapter = new MyViewPageAdapter(activity, views);
            viewPager.setAdapter(mAdapter);
        }
        viewPager.setOffscreenPageLimit(4);
        viewPager.setCurrentItem(0);
        return w;
    }

    private ViewPager mViewPager;
    private Activity mActivity;
    private FragmentManager mFragmentManager;

    public int getCurrentItem(){
        return mViewPager.getCurrentItem();
    }

    public void setCurrentItem(int position, boolean animate){
        mViewPager.setCurrentItem(position, animate);
    }

    public void notifyTabsChanged(List<Fragment> fragments, int defaultPosition){
        PagerAdapter mAdapter = new MyFragmentAdapter(mFragmentManager, fragments);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(defaultPosition);
    }

    public void notifyTabsChanged(ArrayList<View> views, int defaultPosition){
        PagerAdapter mAdapter = new MyViewPageAdapter(mActivity, views);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(defaultPosition);
    }

    private static class MyFragmentAdapter extends FragmentPagerAdapter {

        private List<Fragment> mFragments;

        public MyFragmentAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            mFragments=fragments;
        }

        @Override
        public Fragment getItem(int arg0) {
            return mFragments.get(arg0);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

    }

    private static class MyViewPageAdapter extends PagerAdapter{

        List<View> mViews;
        Activity mActivity;

        public MyViewPageAdapter(Activity activity, List<View> views){
            mActivity = activity;
            mViews = views;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
        @Override
        public int getCount() {
            return mViews == null ? 0 : mViews.size();
        }
        public Object instantiateItem(ViewGroup container, int position) {
            View v = mViews.get(position);
            container.addView(v);
            return v;
        }
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
