package com.ayo.robot.phone;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by Administrator on 2016/12/13.
 */

public class PhoneInfoView extends FrameLayout {
    public PhoneInfoView(Context context) {
        super(context);
        init();
    }

    public PhoneInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PhoneInfoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PhoneInfoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }


    private void init(){

    }
}
