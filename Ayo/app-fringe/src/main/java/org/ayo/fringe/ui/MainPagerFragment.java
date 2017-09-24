package org.ayo.fringe.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;

import org.ayo.component.MasterFragment;
import org.ayo.component.indicator.PageIndicator;
import org.ayo.component.indicator.TypicalIndicator;
import org.ayo.component.pages.MasterTabFragment;
import org.ayo.fringe.R;
import org.ayo.fringe.ui.base.Pages;
import org.ayo.fringe.ui.main.PageManager;
import org.ayo.view.widget.TitleBar;


/**
 * 主页的内容部分，不包括左右侧滑菜单
 */
public class MainPagerFragment extends MasterTabFragment {

    public static void start(Activity c){
        Pages.start(c, MainPagerFragment.class, null);
    }

    TitleBar titlebar;

    int mDefaultPageIndex = 0;

    protected int getLayoutId() {
        return R.layout.page_main_frame;
    }

    @Override
    protected void onCreate2(View contentView, @Nullable Bundle savedInstanceState) {
        super.onCreate2(contentView, savedInstanceState);

        titlebar = id(R.id.titlebar);
        UI.systembar(this);
        UI.titlebar1(getActivity(), titlebar, "主页");

        setCurrentItem(mDefaultPageIndex);
        changeTitlebar(mDefaultPageIndex);

        setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changeTitlebar(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected MasterFragment[] createFragments() {

        int count = PageManager.getDefault().getPageCount();
        MasterFragment[] pages = new MasterFragment[count];
        for(int i = 0; i < count; i++){
            pages[i] = PageManager.getDefault().getPageProviders().get(i).createFragment(getActivity());
        }
        return pages;
    }

    @Override
    protected int getDefaultItemPosition() {
        return mDefaultPageIndex;
    }

    @Override
    protected PageIndicator createPageIndicator() {
        TypicalIndicator v = new TypicalIndicator(getActivity());
        int count = PageManager.getDefault().getPageCount();
        for(int i = 0; i < count; i++){
            v.addTab(PageManager.getDefault().getPageProviders().get(i).createTabItemView(getActivity()));
        }
        return v;
    }

    private void changeTitlebar(int index){
        PageManager.getDefault().getPageProviders().get(index).handleTitleBar(getActivity(), titlebar);
    }

}
