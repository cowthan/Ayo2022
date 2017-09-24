package com.worse.more.breaker.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.worse.more.breaker.R;
import com.worse.more.breaker.ui.account.LoginActivity;

import org.ayo.core.Lang;
import org.ayo.view.widget.TitleBar;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/7/27.
 */
public class MainPageFragment extends BasePage{


    @Bind(R.id.titlebar)
    TitleBar titlebar;

    @Override
    protected int getLayoutId() {
        return R.layout.br_frag_main;
    }

    @Override
    protected void onCreate2(View view, @Nullable Bundle bundle) {
        ButterKnife.bind(this, view);

        titlebar.title("主页")
                .bgColor(Lang.rcolor(R.color.theme_color))
                .leftButton(0)
                .rightButton(1, R.drawable.f015)
                .callback(new TitleBar.Callback() {
                    @Override
                    public void onLeftButtonClicked(View v) {

                    }

                    @Override
                    public void onRightButtonClicked(int index, View v) {
                        if(index == 1){
                            LoginActivity.start(getActivity());
                        }
                    }
                });
    }

    @Override
    protected void onDestroy2() {

    }

    @Override
    protected void onPageVisibleChanged(boolean b, boolean b1, @Nullable Bundle bundle) {

    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        //
        super.setUserVisibleHint(isVisibleToUser);
    }
}
