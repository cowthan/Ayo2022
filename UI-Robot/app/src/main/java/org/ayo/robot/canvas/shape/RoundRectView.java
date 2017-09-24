package org.ayo.robot.canvas.shape;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import org.ayo.robot.BaseView;

public class RoundRectView extends BaseView {
    public RoundRectView(Context context) {
        super(context);
        init();
    }

    private void init(){

    }

    int xRadius = 30;
    int yRadius = 30;
    int centerX, centerY;
    int rw, rh;
    boolean isInited = false;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if(!isInited){
            centerX = w/2;
            centerY = h/2;
            rw = w - 200;
            rh = h - 200;
            isInited = true;
        }
    }

    @Override
    protected void drawShape(Canvas canvas, int w, int h, Paint paint) {
        drawRoundRect(canvas, centerX, centerY, rw, rh, xRadius, yRadius, paint);
    }

    public static void drawRoundRect(Canvas canvas, int centerX, int centerY, int w, int h, int xRadius, int yRadius, Paint p){
        int left = centerX - w/2;
        int top = centerY - h/2;
        int right = left + w;
        int bottom = top + h;
        canvas.drawRoundRect(new RectF(left, top, right, bottom),xRadius, yRadius, p);
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
    }

    public void changeRadius(int dx, int dy) {
        if(xRadius + dx < 0) return;
        if(yRadius + dy < 0) return;
        xRadius += dx;
        yRadius += dy;
        invalidate();
        getObservable().notifyDataChanged(this, "xRadius = " + xRadius + ", yRadius = " + yRadius);
    }


    @Override
    public String getTitle() {
        return "canvas.drawRoundRect(rectF, xRadius, yRadius, paint)";
    }

    @Override
    public String getMethod() {
        return "画圆角矩形";
    }

    @Override
    public String getComment() {
        return "画个圆角矩形\n" +
                "使用RectF和xRadius，yRadius\n" +
                "xRadius和yRadius不能小于0\n" +
                "能变成类似立体圆柱";
    }
}