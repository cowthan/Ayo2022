package org.ayo.fringe.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;

import org.ayo.component.MasterFragment;
import org.ayo.component.indicator.PageIndicator;
import org.ayo.component.indicator.TypicalIndicator;
import org.ayo.component.indicator.TypicalPageInfo;
import org.ayo.component.pages.MasterTabFragment;
import org.ayo.fringe.Config;
import org.ayo.fringe.R;
import org.ayo.fringe.ui.base.Pages;
import org.ayo.fringe.ui.fragment.ProfileFragment;
import org.ayo.fringe.ui.fragment.TimelineListFragment;
import org.ayo.fringe.ui.fragment.TopListFragment;
import org.ayo.fringe.ui.fragment.WeiboListFragment;
import org.ayo.fringe.ui.prompt.Poper;
import org.ayo.fringe.ui.prompt.TitleBarUtils;
import org.ayo.notify.toaster.Toaster;
import org.ayo.view.widget.TitleBar;


/**
 * 主页的内容部分，不包括左右侧滑菜单
 */
public class MainPagerFragment2 extends MasterTabFragment {

    public static void start(Activity c){
        Pages.start(c, MainPagerFragment2.class, null);
    }


    TitleBar titlebar;


    protected int getLayoutId() {
        return R.layout.page_main_frame;
    }


    @Override
    protected void onCreate2(View contentView, @Nullable Bundle savedInstanceState) {
        super.onCreate2(contentView, savedInstanceState);

        titlebar = id(R.id.titlebar);
        UI.systembar(this);
        UI.titlebar1(getActivity(), titlebar, "主页");

        changeTitlebar(0);

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
        return new MasterFragment[]{
                new WeiboListFragment(),
                new TopListFragment(),
                new ProfileFragment(),
                new TimelineListFragment(),
                new ProfileFragment(),
        };
    }

    @Override
    protected int getDefaultItemPosition() {
        return 0;
    }

    @Override
    protected PageIndicator createPageIndicator() {
        TypicalIndicator v = new TypicalIndicator(getActivity());
        v.addTab(new TypicalPageInfo(R.drawable.typical_ic_weixin_normal, R.drawable.typical_ic_weixin_pressed, "圈子"));
        v.addTab(new TypicalPageInfo(R.drawable.typical_ic_weixin_normal, R.drawable.typical_ic_weixin_pressed, "资讯"));
        v.addTab(new TypicalPageInfo(R.drawable.typical_ic_weixin_normal, R.drawable.typical_ic_weixin_pressed, "看图"));
        v.addTab(new TypicalPageInfo(R.drawable.typical_ic_weixin_normal, R.drawable.typical_ic_weixin_pressed, "视频"));
        v.addTab(new TypicalPageInfo(R.drawable.typical_ic_weixin_normal, R.drawable.typical_ic_weixin_pressed, "小说"));
        //v.setupWithViewPager();
        return v;
    }


    private void changeTitlebar(int index){
        int currentItem = index;
        if(currentItem == 0){
            changeTitleBarTo0Or1(0, "动态", Config.DIR.APP_DIR);
        }else if(currentItem == 1){
            changeTitleBarTo0Or1(1, "资讯", Config.DIR.USER_DIR);
        }else if(currentItem == 2){
            changeTitleBarTo2();
        }else if(currentItem == 3){
            changeTitleBarTo3();
        }else if(currentItem == 4){
            changeTitleBarTo4();
        }
    }

    private void changeTitleBarTo0Or1(final int item, String title, final String dataDir){
        TitleBarUtils.setTitleBar(titlebar, title);
        titlebar.leftButton(0)
                .clearRightButtons()
                .rightButton(1, R.drawable.ic_add)
                .rightButton(2, R.drawable.ic_more)
                .callback(new TitleBar.Callback() {

                    @Override
                    public void onLeftButtonClicked(View v) {

                    }

                    @Override
                    public void onRightButtonClicked(int index, View v) {
                        if(index == 1){
                            Poper.showCreateSheet(getActivity());
                        }else if(index == 2){
                        }
                    }
                });
    }

    private void changeTitleBarTo2(){
        TitleBarUtils.setTitleBar(titlebar, "问答");
        titlebar.leftButton(0)
                .clearRightButtons()
                .rightButton(1, R.drawable.sel_download)
                .callback(new TitleBar.Callback() {

                    @Override
                    public void onLeftButtonClicked(View v) {

                    }

                    @Override
                    public void onRightButtonClicked(int index, View v) {
                        if(index == 1){
                            Toaster.toastShort("资讯搜索");
                        }
                    }
                });
    }

    private void changeTitleBarTo3(){
        TitleBarUtils.setTitleBar(titlebar, "咨询");
        titlebar.leftButton(0)
                .clearRightButtons()
                .rightButton(1, R.drawable.sel_download)
                .callback(new TitleBar.Callback() {

                    @Override
                    public void onLeftButtonClicked(View v) {

                    }

                    @Override
                    public void onRightButtonClicked(int index, View v) {
                        if(index == 1){
                            Toaster.toastShort("好友列表");
                        }
                    }
                });
    }

    private void changeTitleBarTo4(){
        TitleBarUtils.setTitleBar(titlebar, "个人中心");
        titlebar.leftButton(0)
                .clearRightButtons();
    }



}
