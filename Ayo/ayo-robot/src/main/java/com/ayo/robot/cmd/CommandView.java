package com.ayo.robot.cmd;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by Administrator on 2016/12/13.
 */

public class CommandView extends FrameLayout {
    public CommandView(Context context) {
        super(context);
        init();
    }

    public CommandView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CommandView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CommandView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }


    private void init(){

    }
}
