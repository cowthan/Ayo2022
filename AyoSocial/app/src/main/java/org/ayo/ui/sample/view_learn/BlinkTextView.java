package org.ayo.ui.sample.view_learn;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 文字闪烁
 */

public class BlinkTextView extends TextView {
    public BlinkTextView(Context context) {
        super(context);
        init();
    }

    public BlinkTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BlinkTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BlinkTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private Paint mPaint1;

    private void init(){
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);//通过父类绘制文本
        if(mGradientMatrix != null){
            mTranslate += mViewWidth / 5;
            if(mTranslate > 2 * mViewWidth){
                mTranslate = -mViewWidth;
            }
            mGradientMatrix.setTranslate(mTranslate, 0);
            mLinearGradient.setLocalMatrix(mGradientMatrix);
            postInvalidateDelayed(100);
        }
    }


    private int mViewWidth;
    private LinearGradient mLinearGradient;
    private Matrix mGradientMatrix;
    private int mTranslate;


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if(mViewWidth == 0){
            mViewWidth = getMeasuredWidth();
            if(mViewWidth > 0){
                mPaint1 = getPaint();
                mLinearGradient = new LinearGradient(
                        0, 0,
                        mViewWidth, 0,
                        new int[]{
                            Color.BLUE, 0xffffffff, Color.BLUE
                        },
                        null,
                        Shader.TileMode.CLAMP
                );
                mPaint1.setShader(mLinearGradient);
                mGradientMatrix = new Matrix();
            }
        }
    }


}
