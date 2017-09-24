package org.ayo.fringe.ui.main;

import android.app.Activity;
import android.view.View;

import org.ayo.component.MasterFragment;
import org.ayo.component.indicator.TypicalPageInfo;
import org.ayo.fringe.R;
import org.ayo.fringe.ui.fragment.MMListFragment;
import org.ayo.fringe.ui.prompt.Poper;
import org.ayo.fringe.ui.prompt.TitleBarUtils;
import org.ayo.view.widget.TitleBar;

/**
 * Created by qiaoliang on 2017/9/7.
 */

public class MmListPageProvider implements PageProvider {

    private String title = "私照";

    @Override
    public MasterFragment createFragment(Activity activity) {
        return new MMListFragment();
    }

    @Override
    public void handleTitleBar(final Activity activity, TitleBar titleBar) {
        TitleBarUtils.setTitleBar(titleBar, title);
        titleBar.leftButton(0)
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
                            Poper.showCreateSheet(activity);
                        }else if(index == 2){
                        }
                    }
                });
    }

    @Override
    public TypicalPageInfo createTabItemView(Activity activity) {
        TypicalPageInfo itemView = new TypicalPageInfo(R.drawable.typical_ic_weixin_normal, R.drawable.typical_ic_weixin_pressed, title);
        return itemView;
    }
}
