package com.zebdar.tom.chat.background;

import android.app.Activity;
import android.view.View;

/**
 * Created by Administrator on 2017/7/18.
 *
 * 聊天整体背景提供者
 */

public interface ChatBackgroundProvider {

    /** 获得背景View */
    View getBackground(Activity activity);

    int getStatusBarColor();

    /** 聊天内容可能可以显示出情绪变化，背景可以随之变化（但是：如果设置成非0，小键盘的行为就不对了！啥情况啊） */
    void onMoodChange(Activity activity, String mood);

    /** 聊天前景色是否需要设置成深色系，这和特定背景的色系有关系 */
    boolean needForegroundDarkTheme();

    void onResume();

    void onPause();

}
