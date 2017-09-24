package org.ayo.fringe.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.view.View;

import org.ayo.fringe.R;
import org.ayo.fringe.ui.base.BaseFrgActivity;
import org.ayo.fringe.ui.main.MmListPageProvider;
import org.ayo.fringe.ui.main.PageManager;
import org.ayo.fringe.utils.DrawerLayoutManager;
import org.ayo.notify.toaster.Toaster;

/**
 * Created by Administrator on 2017/1/18 0018.
 */

public class MainActivity extends BaseFrgActivity {

    public static void start(Context c){
        Intent i = new Intent(c, MainActivity.class);
        if(c instanceof Activity){

        }else{
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        c.startActivity(i);
    }

    public static boolean isForeground = false;

    @Override
    protected int getLayoutId() {
        return R.layout.ayo_ac_drawerlayout;
    }

    @Override
    protected void onCreate2(View contentView, @Nullable Bundle savedInstanceState) {
        UI.systembar(this, false);

        PageManager.getDefault().clear();
        PageManager.getDefault().addPageProvider(new MmListPageProvider());
//        PageManager.getDefault().addPageProvider(new TimelineListPageProvider());
//        PageManager.getDefault().addPageProvider(new TopListPageProvider());

        MainPagerFragment contentFragment = new MainPagerFragment();
        MainMenuFragment leftFragment = new MainMenuFragment();
        SampleFragment rightFragment = new SampleFragment();
        DrawerLayoutManager drawerLayoutManager = DrawerLayoutManager.attach(this, contentFragment, new DrawerLayout.DrawerListener(){

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        final DrawerLayoutManager.Drawer leftDrawer = drawerLayoutManager.setLeftDrawerFragment(leftFragment);
        DrawerLayoutManager.Drawer rightDrawer = drawerLayoutManager.setRightDrawerFragment(rightFragment);

        leftFragment.addOnMenuActionCallback(new MainMenuFragment.OnMenuActionCallback() {
            @Override
            public void onMenuChecked(int menu) {
                leftDrawer.toggle();
                if(menu == MainMenuFragment.MENU_HOME){
                    Toaster.toastShort("主页");
                }else if(menu == MainMenuFragment.MENU_CUSTOMERS){
                    Toaster.toastShort("用户列表");
                }else{
                    Toaster.toastShort("尚未定义");
                }
            }
        });
        ///drawerLayoutManager.getDrawerLayout().setFitsSystemWindows(true);  ///这个属性只有放在xml里时，才有用，奇怪了

    }

    @Override
    protected void onDestroy2() {

    }

    @Override
    protected void onPageVisibleChanged(boolean visible, boolean isFirstTimeVisible, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public boolean isSwipebackEnabled() {
        return false;
    }

    @Override
    protected void onResume() {
        isForeground = true;
        super.onResume();
    }


    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
    }

}
