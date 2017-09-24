package org.ayo.ui.sample.view_learn;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2016/11/26.
 */

public class MusicWaveView extends View {
    public MusicWaveView(Context context) {
        super(context);
    }

    public MusicWaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MusicWaveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MusicWaveView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    int mWidth;
    int mRectHeight;
    int mRectWidth;
    int rectCount = 10;
    Paint mPaint2;
    LinearGradient mLinearGradient;
    double mRandom;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for(int i = 0; i < rectCount; i++){
            int offset = 20;
            mRandom = Math.random();
            float currentHeight = (float) (mRectHeight * mRandom);
            canvas.drawRect((float)(mWidth * 0.4 / 2 + mRectWidth * i + offset),  /// ?????
                    currentHeight,
                    (float)(mWidth * 0.4 / 2 + mRectWidth * (i + 1)),
                    mRectHeight,
                    mPaint2
                    );
        }

        postInvalidateDelayed(300);
    }

    /**
     * 添加渐变颜色，使效果更逼真
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = getWidth();
        mRectHeight = getHeight();
        mRectWidth = (int)(mWidth * 0.6 / rectCount);
        mLinearGradient = new LinearGradient(
                0, 0,
                mRectWidth, mRectHeight,
                Color.YELLOW,
                Color.BLUE,
                Shader.TileMode.CLAMP
        );

        mPaint2 = new Paint();
        mPaint2.setColor(Color.YELLOW);
        mPaint2.setStyle(Paint.Style.FILL);
        mPaint2.setShader(mLinearGradient);
    }
}
