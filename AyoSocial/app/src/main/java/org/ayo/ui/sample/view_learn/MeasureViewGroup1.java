package org.ayo.ui.sample.view_learn;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * 默认行为的ViewGroup
 */

public class MeasureViewGroup1 extends ViewGroup {
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MeasureViewGroup1(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public MeasureViewGroup1(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MeasureViewGroup1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MeasureViewGroup1(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
    }


}
