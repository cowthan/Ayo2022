package org.ayo.ui.sample.view_learn;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2016/11/26.
 */

public class CircleLoadingView extends View {
    public CircleLoadingView(Context context) {
        super(context);
        init();
    }

    public CircleLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircleLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CircleLoadingView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private Paint mCirclePaint;
    private Paint mArcPaint;
    private Paint mTextPaint;


    private void init(){
        mCirclePaint = new Paint();
        mCirclePaint.setColor(getResources().getColor(android.R.color.holo_blue_light));
        mCirclePaint.setStyle(Paint.Style.FILL);

        mArcPaint = new Paint();
        mArcPaint.setColor(Color.YELLOW);
        mArcPaint.setStyle(Paint.Style.STROKE);
        mArcPaint.setStrokeWidth(30);

        mTextPaint = new Paint();
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int length = getMeasuredWidth();
        float mCircleXY = length / 2;
        float mRadius = (length * 0.5f / 2);
        RectF mArcRectF = new RectF(
                (float)(length * 0.1),
                (float)(length * 0.1),
                (float)(length * 0.9),
                (float)(length * 0.9)
                );

        ///内圆
        canvas.drawCircle(mCircleXY, mCircleXY, mRadius, mCirclePaint);
        ///外部弧线
        canvas.drawArc(mArcRectF, 270, mSweepAngle, false, mArcPaint);
        ///绘制文字
        String text = "文字";
        int textSize = 200;
        canvas.drawText(text, 0, text.length(), mCircleXY, mCircleXY + (textSize / 4), mTextPaint);
    }

    private float mSweepAngle;

    public void setSweepValue(float sweep){
        if(sweep != 0){
            mSweepAngle = sweep;
        }else{
            mSweepAngle = 25;
        }
        this.invalidate();
    }
}
