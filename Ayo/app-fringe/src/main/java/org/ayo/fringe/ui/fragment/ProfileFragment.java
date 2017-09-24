package org.ayo.fringe.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import org.ayo.fringe.R;
import org.ayo.fringe.ui.base.BaseFrgFragment;

/**
 * 个人中心
 */
public class ProfileFragment extends BaseFrgFragment implements View.OnClickListener{

    private Activity ctx;

    @Override
    protected int getLayoutId() {
        return R.layout.wb_frag_profile;
    }

    @Override
    protected void onCreate2(View view, @Nullable Bundle bundle) {
        ctx = this.getActivity();
        TextView txt_pengyouquan = (TextView) findViewById(R.id.txt_pengyouquan); //朋友圈
        TextView txt_friend = (TextView) findViewById(R.id.txt_friend); //通讯录
        TextView txt_saoyisao = (TextView) findViewById(R.id.txt_saoyisao);//扫一扫
        TextView txt_yaoyiyao = (TextView) findViewById(R.id.txt_yaoyiyao);//摇一摇
        TextView txt_nearby = (TextView) findViewById(R.id.txt_nearby);//附件的人
        TextView txt_piaoliuping = (TextView) findViewById(R.id.txt_piaoliuping);//漂流瓶
        TextView txt_vedio = (TextView) findViewById(R.id.txt_vedio);//录视频
        TextView txt_game = (TextView) findViewById(R.id.txt_game);//游戏

        txt_pengyouquan.setOnClickListener(this);
        txt_friend.setOnClickListener(this);
        txt_saoyisao.setOnClickListener(this);
        txt_yaoyiyao.setOnClickListener(this);
        txt_nearby.setOnClickListener(this);
        txt_piaoliuping.setOnClickListener(this);
        txt_vedio.setOnClickListener(this);
        txt_game.setOnClickListener(this);
    }

    @Override
    protected void onDestroy2() {

    }

    @Override
    protected void onPageVisibleChanged(boolean b, boolean b1, @Nullable Bundle bundle) {

    }

    @Override
    public void onClick(View view) {

    }
}
