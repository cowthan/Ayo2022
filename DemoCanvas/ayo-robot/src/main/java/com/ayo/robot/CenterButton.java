package com.ayo.robot;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by Administrator on 2016/12/13.
 */

public class CenterButton extends Button {
    public CenterButton(Context context) {
        super(context);
    }

    public CenterButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CenterButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CenterButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
