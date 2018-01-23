package org.ayo.robot.canvas.shape;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import org.ayo.robot.BaseView;

public class CircleView extends BaseView {
    public CircleView(Context context) {
        super(context);
        init();
    }

    private void init(){

    }

    int centerX, centerY;
    int radius;
    boolean isInited = false;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if(!isInited){
            centerX = w/2;
            centerY = h/2;
            radius = (w - 100)/2;
            isInited = true;
        }
    }

    @Override
    protected void drawShape(Canvas canvas, int w, int h, Paint paint) {
        canvas.drawCircle(centerX, centerY, radius, paint);
    }

    public void moveCenter(int dx, int dy){
        centerX += dx;
        centerY += dy;
        invalidate();
    }

    public void changeRadius(int dx, int dy){
        radius += dx;
        invalidate();
    }

    @Override
    public String getTitle() {
        return "canvas.drawCircle(w/2, h/2, radius, paint)";
    }

    @Override
    public String getMethod() {
        return "画个圆";
    }

    @Override
    public String getComment() {
        return "画个圆形\n前两个参数是圆心\n" +
                "第三个参数是半径\n" +
                "style可以指定填充还是画框\nstroke width大一点，就是个圆环";
    }

}