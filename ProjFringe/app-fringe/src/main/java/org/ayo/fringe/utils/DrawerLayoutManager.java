package org.ayo.fringe.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import org.ayo.fringe.R;
import org.ayo.view.Display;

/**
 * 还没加入ActionBar相关的功能
 * ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
 //                R.drawable.ic_drawer, R.string.drawer_open,
 //                R.string.drawer_close)
 //        {
 //
 //            public void onDrawerClosed(View view)
 //            {
 //
 //                //invalidateOptionsMenu(); // creates call to
 //                // onPrepareOptionsMenu()
 //            }
 //
 //            public void onDrawerOpened(View drawerView)
 //            {
 //
 //                //invalidateOptionsMenu(); // creates call to
 //                // onPrepareOptionsMenu()
 //            }
 //        };

 //getActionBar().setDisplayHomeAsUpEnabled(true);
 // getActionBar().setHomeButtonEnabled(true);
 */
public class DrawerLayoutManager {

    public static final int LEFT = 1;
    public static final int RIGHT = 2;


    private DrawerLayout drawerLayout;
    private int gravity;
    private DrawerLayout.DrawerListener drawerListener;

    private FragmentActivity mActivity;
    private Fragment contentFragment;
    private Fragment drawerFragment;
    private boolean hasSavedInstance = false;
    private DrawerLayoutManager(){

    }

    public static DrawerLayoutManager attach(FragmentActivity activity, Fragment contentFragment, DrawerLayout.DrawerListener drawerListener){
        DrawerLayoutManager dm = new DrawerLayoutManager();
        dm.mActivity = activity;
        //activity.setContentView(R.layout.ayo_ac_drawerlayout);
        dm.drawerLayout = (DrawerLayout) activity.findViewById(R.id.drawer_layout);
        dm.drawerListener = drawerListener;
        dm.drawerLayout.addDrawerListener(drawerListener);
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, contentFragment).commit();
        return dm;
    }

    public Drawer setLeftDrawerFragment(Fragment leftFragment){
        mActivity.getSupportFragmentManager().beginTransaction().replace(R.id.fl_left, leftFragment).commit();
        return new Drawer(drawerLayout, Gravity.START, (FrameLayout) drawerLayout.findViewById(R.id.left_drawer));
    }

    public Drawer setRightDrawerFragment(Fragment rightFragment){
        mActivity.getSupportFragmentManager().beginTransaction().replace(R.id.fl_right, rightFragment).commit();
        return new Drawer(drawerLayout, Gravity.END, (FrameLayout) drawerLayout.findViewById(R.id.right_drawer));
    }


    public static class Drawer{

        private DrawerLayout drawerLayout;
        private int gravity;
        private FrameLayout drawerView;

        private Drawer(DrawerLayout drawerLayout, int gravity, FrameLayout drawerView){
            this.drawerLayout = drawerLayout;
            this.gravity = gravity;
            this.drawerView = drawerView;
        }

        public boolean isOpen(){
            return drawerLayout.isDrawerOpen(gravity);
        }

        public void toggle(){
            if(drawerLayout.isDrawerOpen(this.gravity)){
                drawerLayout.closeDrawer(this.gravity);
            }else{
                drawerLayout.openDrawer(this.gravity);
            }
        }

        public void setWidth(float ration){
            int w = (int) (Display.screenWidth * ration);
            ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) drawerView.getLayoutParams();
            lp.width = w;
            drawerView.setLayoutParams(lp);
        }

    }

    /**
     * 把drawerLayout锁定在当前状态，无法再通过手指动作来控制开关，只能通过代码控制，toggle()
     */
    public void lockInCurrentMode(){
        if(drawerLayout.isDrawerOpen(gravity)){
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
        }else{
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }

    /**
     * 将DrawerLayout锁定在开启状态，如果当前未开启，会开启，无法再通过手指动作来控制开关，只能通过代码控制，toggle()
     */
    public void lockOpen(){
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
    }

    /**
     * 将DrawerLayout锁定在关闭状态，如果当前未关闭，会关闭，无法再通过手指动作来控制开关，只能通过代码控制，toggle()
     */
    public void lockClose(){
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    /**
     * 解除锁定，但不会改变当前开关状态
     */
    public void unlock(){
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }


    public DrawerLayout getDrawerLayout(){
        return drawerLayout;
    }



}
