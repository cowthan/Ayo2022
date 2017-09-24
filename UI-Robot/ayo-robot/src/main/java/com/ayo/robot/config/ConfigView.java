package com.ayo.robot.config;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by Administrator on 2016/12/13.
 */

public class ConfigView extends FrameLayout {
    public ConfigView(Context context) {
        super(context);
        init();
    }

    public ConfigView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ConfigView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ConfigView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){

    }
}
