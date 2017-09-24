package org.ayo.fringe.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;

import org.ayo.animate.TweenAnimation;
import org.ayo.component.Master;
import org.ayo.fringe.R;
import org.ayo.template.pager.FragmentPager;
import org.ayo.fringe.ui.base.BaseFrgFragment;
import org.ayo.fringe.ui.fragment.GuideFrgFragmentVedioFragment;
import org.ayo.fringe.widget.spring.SpringIndicator;

import java.util.ArrayList;
import java.util.List;


/**
 * 应该仿慕课网做一个
 */
public class GuideActivity extends BaseFrgFragment {

    public static void start(Activity c){
        Master.startPage(c, GuideActivity.class, null);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.wb_ac_guide;
    }

    @Override
    protected void onCreate2(View view, @Nullable Bundle bundle) {

        UI.systembar(this, true);

        FragmentPager pager = (FragmentPager) findViewById(R.id.pager);
        SpringIndicator indicator = (SpringIndicator) findViewById(R.id.indicator);
        final Button btn_we_are_in = (Button) findViewById(R.id.btn_we_are_in);
        btn_we_are_in.setVisibility(View.GONE);
        btn_we_are_in.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), MainActivity.class);
//                startActivity(intent);

                LoginActivity.start(getActivity());
                TweenAnimation.apply(getActivity(), TweenAnimation.fade_in, TweenAnimation.fade_out);
                finish();
            }
        });

        List<Fragment> fragments = new ArrayList<>();

        GuideFrgFragmentVedioFragment f1 = new GuideFrgFragmentVedioFragment();
        f1.setAssetVedioPath("guideVedio/guide_1.mp4");

        GuideFrgFragmentVedioFragment f2 = new GuideFrgFragmentVedioFragment();
        f2.setAssetVedioPath("guideVedio/guide_2.mp4");

        GuideFrgFragmentVedioFragment f3 = new GuideFrgFragmentVedioFragment();
        f3.setAssetVedioPath("guideVedio/guide_3.mp4");

        fragments.add(f1);
        fragments.add(f2);
        fragments.add(f3);

        String[] titles = {
                1+"", 2+"", 3+""
        };


        pager.attach(getChildFragmentManager(), fragments, titles, indicator, new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == 2){
                    btn_we_are_in.setVisibility(View.VISIBLE);
                }else{
                    btn_we_are_in.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

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
