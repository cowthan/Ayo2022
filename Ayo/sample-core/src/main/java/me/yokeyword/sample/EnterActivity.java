package me.yokeyword.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import org.ayo.component.MasterFragment;
import org.ayo.component.StateModel;
import org.ayo.component.sample.R;

/**
 * Created by YoKeyword on 16/6/5.
 */
public class EnterActivity extends MasterFragment {
    private Toolbar mToolBar;
    private TextView mTvBtnFlow, mTvBtnWechat, mTvBtnZhihu;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_enter;
    }

    @Override
    protected void onCreate2(View contentView, @Nullable Bundle savedInstanceState) {
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        mTvBtnFlow = (TextView) findViewById(R.id.tv_btn_flow);
        mTvBtnWechat = (TextView) findViewById(R.id.tv_btn_wechat);
        mTvBtnZhihu = (TextView) findViewById(R.id.tv_btn_zhihu);

//        setSupportActionBar(mToolBar);

        mTvBtnFlow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), me.yokeyword.sample.demo_flow.MainActivity.class));
            }
        });

        mTvBtnWechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), me.yokeyword.sample.demo_wechat.MainActivity.class));
            }
        });

        mTvBtnZhihu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), me.yokeyword.sample.demo_zhihu.MainActivity.class));
            }
        });
    }


    @Override
    protected void onDestroy2() {

    }

    @Override
    protected void onPageVisibleChanged(boolean visible, boolean isFirstTimeVisible, @Nullable Bundle savedInstanceState) {

    }

    @Override
    protected StateModel createStateModel() {
        return null;
    }
}
