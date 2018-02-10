package org.ayo.imagepicker;

import android.content.Context;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;

public class FixedWidthTextView extends android.support.v7.widget.AppCompatTextView {
    private int mFixTextViewWidth;
    private int mPaddingWidth;

    public FixedWidthTextView(Context context) {
        super(context, null);
        init();
    }

    public FixedWidthTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaddingWidth = getPaddingLeft() + getPaddingRight();
        mFixTextViewWidth = (int) getTextViewLength((String) getText()) + mPaddingWidth;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        if (widthMode == MeasureSpec.AT_MOST) {
            if (mFixTextViewWidth > mPaddingWidth) {
                setMeasuredDimension(mFixTextViewWidth, getMeasuredHeight());
            }

        }

    }

    private float getTextViewLength(String text) {
        if (TextUtils.isEmpty(text))
            return 0;
        TextPaint paint = getPaint();
        return paint.measureText(text);
    }
}