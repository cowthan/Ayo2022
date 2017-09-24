package org.ayo.robot.paint.text;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;

import org.ayo.robot.BaseView;

/**
 * Created by Administrator on 2016/12/25.
 */

public class DrawTextView extends BaseView {
    public DrawTextView(Context context) {
        super(context);
    }

    public DrawTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public DrawTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private String txt = "不管";
    float startX = 0;
    float startY = 0;
    float textSize = 13;
    boolean shouldDrawDemoFrame = false;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        startX = 100;
        startY = 200;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void drawShape(Canvas canvas, int w, int h, Paint paint) {
        paint.setTextSize(textSize);
        paint.setTextAlign(Paint.Align.LEFT);
        //paint.setTextScaleX(1);
        //paint.setTextSkewX(0);
        //paint.setTextLocale(Locale.getDefault());
        paint.setColor(Color.BLACK);

        canvas.drawText(txt, startX, startY, paint);

        ///画基准线
        if(shouldDrawDemoFrame){
            paint.setColor(0x99ff0000);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(1);
            Paint.FontMetrics mFontMetrics = paint.getFontMetrics();

            Log.d("Aige", "ascent：" + mFontMetrics.ascent);
            Log.d("Aige", "top：" + mFontMetrics.top);
            Log.d("Aige", "leading：" + mFontMetrics.leading);
            Log.d("Aige", "descent：" + mFontMetrics.descent);
            Log.d("Aige", "bottom：" + mFontMetrics.bottom);
            float totalWidth = paint.measureText(txt);

            ///及算宽度：需要算出文字宽度，然后在左右各画两条竖线（不画矩形，否则跟别的线重合就看不出来了）
            float leftBorder = startX;
            float rightBorder = startX + totalWidth;
            float topBorder = startY + mFontMetrics.top;
            float borderHeight = mFontMetrics.bottom - mFontMetrics.top;
            paint.setColor(Color.WHITE);
            canvas.drawLine(leftBorder, topBorder, leftBorder, topBorder + borderHeight, paint);
            canvas.drawLine(rightBorder, topBorder, rightBorder, topBorder + borderHeight, paint);

            ///画出几条线：startY就是基准线
            //ascent
            paint.setColor(Color.BLUE);
            float ascentPostion = startY + mFontMetrics.ascent;
            canvas.drawLine(startX - 50, ascentPostion, startX + totalWidth + 50, ascentPostion, paint);

            //descent
            paint.setColor(Color.RED);
            float descentPostion = startY + mFontMetrics.descent;
            canvas.drawLine(startX - 50, descentPostion, startX + totalWidth + 50, descentPostion, paint);

            //top
            paint.setColor(Color.YELLOW);
            float topPosition = startY + mFontMetrics.top;
            canvas.drawLine(startX - 50, topPosition, startX + totalWidth + 50, topPosition, paint);

            //bottom
            paint.setColor(Color.GREEN);
            float bottomPosition = startY + mFontMetrics.bottom;
            canvas.drawLine(startX - 50, bottomPosition, startX + totalWidth + 50, bottomPosition, paint);

            //leading：行间距相关参数，只有一行，则此值恒为0
//            paint.setColor(Color.GRAY);
//            float leadingPosition = startY + mFontMetrics.leading;
//            canvas.drawLine(startX - 50, leadingPosition, startX + totalWidth + 50, leadingPosition, paint);

            ///base line
            paint.setColor(Color.GRAY);
            float basePosition = startY;
            canvas.drawLine(startX - 50, basePosition, startX + totalWidth + 50, basePosition, paint);

        }
    }

    public void moveTextBy(int dx, int dy){
        startX += dx;
        startY += dy;
        invalidate();
    }

    public void changeTextSizeBy(int dx){
        textSize += dx;
        invalidate();
    }

    public void enableDemoFrame(boolean enable){
        shouldDrawDemoFrame = enable;
        invalidate();
    }

    public boolean isFrameEnabled(){
        return shouldDrawDemoFrame;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public String getMethod() {
        return null;
    }

    @Override
    public String getComment() {
        return null;
    }
}
