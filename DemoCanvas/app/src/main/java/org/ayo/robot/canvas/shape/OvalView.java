package org.ayo.robot.canvas.shape;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import org.ayo.robot.BaseView;

public class OvalView extends BaseView {
    public OvalView(Context context) {
        super(context);
        init();
    }

    private void init(){

    }

    int centerX, centerY;
    int rw, rh;
    boolean isInited = false;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if(!isInited){
            centerX = w/2;
            centerY = h/2;
            rw = w - 100;
            rh = h - 100;
            isInited = true;
        }
    }


    @Override
    protected void drawShape(Canvas canvas, int w, int h, Paint paint) {
        //画椭圆
        paint.setColor(Color.BLUE);
        int left = centerX - rw/2;
        int top = centerY - rh/2;
        int right = left + rw;
        int bottom = top + rh;
        RectF rectF = new RectF(left, top, right, bottom);
        canvas.drawOval(rectF, paint);

        //画外接矩形，便于观察
        Paint.Style oldStyle = paint.getStyle();
        float oldWidth = paint.getStrokeWidth();
        paint.setStrokeWidth(2);
        paint.setColor(Color.YELLOW);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(rectF, paint);

        //恢复paint
        paint.setStyle(oldStyle);
        paint.setStrokeWidth(oldWidth);
    }

    @Override
    public String getTitle() {
        return "canvas.drawOval(RectF, paint)";
    }

    @Override
    public String getMethod() {
        return "画个椭圆";
    }

    @Override
    public String getComment() {
        return "画个椭圆，基于其外接矩形来画";
    }


    public void moveCenter(int dx, int dy){
        centerX += dx;
        centerY += dy;
        invalidate();
    }

    public void changeSize(int dw, int dh){
        rw += dw;
        rh += dh;
        invalidate();
        getObservable().notifyDataChanged(this, "横半径 = " + rw + ", 竖半径 = " + rh);
    }

}