package com.worse.more.breaker.ui.account;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.worse.more.breaker.R;
import com.worse.more.breaker.ui.widget.IconTextView;
import com.worse.more.breaker.ui.widget.MenuItem;

import org.ayo.component.MasterFragment;
import org.ayo.view.Display;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/7/28.
 */
public class PersonalCenterFragment extends MasterFragment{

    @Bind(R.id.btn_my_qa)
    IconTextView btn_my_qa;

    @Bind(R.id.btn_my_comment)
    IconTextView btn_my_comment;

    @Bind(R.id.btn_my_fav)
    IconTextView btn_my_fav;

    @Bind(R.id.menu_system_msg)
    MenuItem menu_system_msg;

    @Bind(R.id.menu_feedback)
    MenuItem menu_feedback;

    @Bind(R.id.menu_car_info)
    MenuItem menu_car_info;

    @Bind(R.id.menu_setting)
    MenuItem menu_setting;


    @Override
    protected int getLayoutId() {
        return R.layout.act_frag_personal_center;
    }

    @Override
    protected void onCreate2(View view, @Nullable Bundle bundle) {
        ButterKnife.bind(this, view);

        btn_my_qa.setMode(IconTextView.MODE_ICON_LEFT, Display.dip2px(8));
        btn_my_qa.setText("我的提问");
        btn_my_qa.setIcon(R.mipmap.ic_launcher);
        btn_my_qa.setImageSize(Display.dip2px(30), Display.dip2px(30));

        btn_my_comment.setMode(IconTextView.MODE_ICON_LEFT, Display.dip2px(8));
        btn_my_comment.setText("我的评论");
        btn_my_comment.setIcon(R.mipmap.ic_launcher);
        btn_my_comment.setImageSize(Display.dip2px(30), Display.dip2px(30));

        btn_my_fav.setMode(IconTextView.MODE_ICON_LEFT, Display.dip2px(8));
        btn_my_fav.setText("我的收藏");
        btn_my_fav.setIcon(R.mipmap.ic_launcher);
        btn_my_fav.setImageSize(Display.dip2px(30), Display.dip2px(30));

        menu_system_msg.setMenuInfo("系统消息", R.mipmap.ic_launcher, new View.OnClickListener(){
            @Override
            public void onClick(View v) {

            }
        });

        menu_feedback.setMenuInfo("意见反馈", R.mipmap.ic_launcher, new View.OnClickListener(){
            @Override
            public void onClick(View v) {

            }
        });

        menu_system_msg.setMenuInfo("车辆信息", R.mipmap.ic_launcher, new View.OnClickListener(){
            @Override
            public void onClick(View v) {

            }
        });

        menu_setting.setMenuInfo("设置", R.mipmap.ic_launcher, new View.OnClickListener(){
            @Override
            public void onClick(View v) {

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
