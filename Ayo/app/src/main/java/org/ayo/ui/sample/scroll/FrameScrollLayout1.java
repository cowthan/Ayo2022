package org.ayo.ui.sample.scroll;

import android.content.Context;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * Created by qiaoliang on 2017/9/30.
 */

public class FrameScrollLayout1 extends FrameLayout {
    public FrameScrollLayout1(@NonNull Context context) {
        super(context);
    }

    public FrameScrollLayout1(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FrameScrollLayout1(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public FrameScrollLayout1(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}
