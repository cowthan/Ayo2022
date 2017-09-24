package org.ayo.fringe.ui.main;

import android.app.Activity;

import org.ayo.component.MasterFragment;
import org.ayo.component.indicator.TypicalPageInfo;
import org.ayo.view.widget.TitleBar;

/**
 * Created by qiaoliang on 2017/9/7.
 *
 * 向主页框架注册页面
 */

public interface PageProvider {

    /**
     * 页面
     * @return
     */
    MasterFragment createFragment(Activity activity);

    /**
     * 当前页面显示时，titlebr的处理
     * @param titleBar
     */
    void handleTitleBar(Activity activity, TitleBar titleBar);

    /**
     * 当前页面对应的indicator的item
     * @return
     */
    TypicalPageInfo createTabItemView(Activity activity);

}
