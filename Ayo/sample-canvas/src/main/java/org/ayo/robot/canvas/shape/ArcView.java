package org.ayo.robot.canvas.shape;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import org.ayo.robot.BaseView;

public class ArcView extends BaseView {
    public ArcView(Context context) {
        super(context);
        init();
    }

    private void init(){
    }


    int centerX, centerY;
    int rw, rh;
    boolean isInited = false;

    private int startAngle = 0;
    private int sweepAngle = 90;
    private boolean useCenter = true;

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
        ///画Arc
        paint.setColor(Color.BLUE);
        int left = centerX - rw/2;
        int top = centerY - rh/2;
        int right = left + rw;
        int bottom = top + rh;
        RectF rectF = new RectF(left, top, right, bottom);
        canvas.drawArc(rectF, startAngle, sweepAngle, useCenter, paint);
        //drawArc(RectF oval, float startAngle, float sweepAngle, boolean useCenter, Paint paint)

        //画外接矩形，并回复paint
        Paint.Style oldStyle = paint.getStyle();
        float oldWidth = paint.getStrokeWidth();
        paint.setStrokeWidth(1);
        paint.setColor(Color.YELLOW);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(rectF, paint);

        paint.setStyle(oldStyle);
        paint.setStrokeWidth(oldWidth);

        ///---------------------
//        rectF = new RectF(200, 200, 100, 150);
//        canvas.drawArc(rectF, 0, 180, true, paint);
    }



    public void switchShape(){
        useCenter = !useCenter;
        invalidate();
        getObservable().notifyDataChanged(this, "起始角 = " + startAngle + ", 跨度 = " + sweepAngle + ", " + (useCenter ? "扇形" : "弧线"));
    }

    @Override
    public String getTitle() {
        return "canvas.drawArc(rectF, startAngle, sweepAngle, useCenter, paint)";
    }

    @Override
    public String getMethod() {
        return "画扇形，弧形（支持FILL和STROKE）";
    }

    @Override
    public String getComment() {
        return "画个扇形或者弧线--都可以填充\n" +
                "弧线的填充就是收尾相连形成close\n" +
                "不论弧线还是扇形，都是取自一个由外接矩形定义的椭圆\n" +
                "startAngle：从圆心水平向右是0，顺时针数，总数360\n" +
                "sweepAngle：弧度\n" +
                "useCenter：true表示要扇形，false表示要弧线\n" +
                "startAngle变化会引起旋转\n" +
                "sweepAngle变化会引起范围变化";
    }

    public void changeStartAngle(int dy){
        startAngle += dy;
        invalidate();
        getObservable().notifyDataChanged(this, "起始角 = " + startAngle + ", 跨度 = " + sweepAngle + ", " + (useCenter ? "扇形" : "弧线"));
    }

    public void changeSweepAngle(int dy){
        sweepAngle += dy;
        invalidate();
        getObservable().notifyDataChanged(this, "起始角 = " + startAngle + ", 跨度 = " + sweepAngle + ", " + (useCenter ? "扇形" : "弧线"));
    }

    public void changeRectSize(int dx, int dy){
        rw += dx;
        rh += dy;
        invalidate();
    }
}