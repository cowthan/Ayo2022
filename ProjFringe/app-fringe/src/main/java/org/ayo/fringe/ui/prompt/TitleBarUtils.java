package org.ayo.fringe.ui.prompt;

import org.ayo.fringe.R;
import org.ayo.view.widget.TitleBar;
import org.ayo.fringe.utils.Utils;

/**
 * Created by Administrator on 2016/4/20.
 */
public class TitleBarUtils {

    public static void setTitleBar(TitleBar titlebar, String title){
        titlebar.title(title).bgColor(Utils.getColor(R.color.main_color));
    }

}
