package org.ayo.appsist.monitor;

import android.view.View;

/**
 * Description: <悬浮视图>
 * Author: hui.zhao
 * Date: 2016/9/26
 * Copyright: Ctrip
 */

public interface IFloatView {
    public void release();

    public void attachToWindow(View view, int gravity, int x, int y, int width, int height);
}
