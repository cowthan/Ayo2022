package org.ayo.fringe.ui;

import android.app.Activity;
import android.view.View;

import org.ayo.component.MasterActivity;
import org.ayo.component.MasterFragment;
import org.ayo.core.Lang;
import org.ayo.fringe.R;
import org.ayo.statusbar.StatusBarCompat;
import org.ayo.view.widget.TitleBar;

/**
 * Created by Administrator on 2017/1/14.
 */

public class UI {

    public static void systembar(MasterActivity a){
        systembar(a, false);
    }

    public static void systembar(MasterFragment a){
        systembar(a, false);
    }

    public static void systembar(MasterActivity a, boolean taken){
        StatusBarCompat.setStatusBarColor(a, Lang.rcolor(R.color.colorPrimaryDark));
    }

    public static void systembar(MasterFragment a, boolean taken){
        StatusBarCompat.setStatusBarColor(a.getActivity(), Lang.rcolor(R.color.colorPrimaryDark));
    }

    public static void titlebar1(final Activity a, TitleBar titlebar, String name){
        titlebar.title(name)
                .bgColor(Lang.rcolor(R.color.colorPrimary))
                .titleColor(R.color.white)
                .leftButton(0)
                .clearRightButtons()
                .callback(new TitleBar.Callback() {

                    @Override
                    public void onLeftButtonClicked(View v) {
                        a.finish();
                    }

                    @Override
                    public void onRightButtonClicked(int index, View v) {

                    }
                });
    }

    public static void titlebar2(final Activity a, TitleBar titlebar, String name){
        titlebar.title(name)
                .bgColor(Lang.rcolor(R.color.colorPrimary))
                .titleColor(R.color.white)
                .leftButton(R.drawable.sel_back_red)
                .clearRightButtons()
                .callback(new TitleBar.Callback() {

                    @Override
                    public void onLeftButtonClicked(View v) {
                        a.finish();
                    }

                    @Override
                    public void onRightButtonClicked(int index, View v) {

                    }
                });
    }

}
