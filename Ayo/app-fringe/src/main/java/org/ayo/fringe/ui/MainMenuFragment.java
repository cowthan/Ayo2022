package org.ayo.fringe.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.ayo.fringe.R;
import org.ayo.fringe.ui.base.BaseFrgFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;



/**
 * Created by Administrator on 2016/7/27.
 */
public class MainMenuFragment extends BaseFrgFragment {

    public static final int MENU_HOME = R.id.menu_home;
    public static final int MENU_CUSTOMERS = R.id.menu_customers;

    public interface OnMenuActionCallback{
        void onMenuChecked(int menu);
    }

    @BindView(R.id.left_draw)
    NavigationView mNavigationView;

    private List<OnMenuActionCallback> callbacks = new ArrayList<>();
    public void addOnMenuActionCallback(OnMenuActionCallback c){
        callbacks.add(c);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_main_menu;
    }

    @Override
    protected void onCreate2(View view, @Nullable Bundle bundle) {
        ButterKnife.bind(this, view);

        View headerView = mNavigationView.inflateHeaderView(R.layout.layout_left_draw_header);
        ImageView iv_user_bg = (ImageView) headerView.findViewById(R.id.iv_user_bg);
        ImageView iv_user_icon = (ImageView) headerView.findViewById(R.id.iv_user_icon);
        TextView tv_user_name = (TextView) headerView.findViewById(R.id.tv_user_name);

        mNavigationView.setCheckedItem(R.id.home);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                if(callbacks != null){
                    int itemId = item.getItemId();
                    /////
                    //根据itemId，映射出menu，这里取了个巧，常量的值直接定义为id值
                    /////
                    for(OnMenuActionCallback c: callbacks){
                        c.onMenuChecked(itemId);
                    }
                }
                item.setChecked(true);
                return true;
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
