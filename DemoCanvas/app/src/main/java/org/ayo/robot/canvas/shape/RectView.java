package org.ayo.robot.canvas.shape;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import org.ayo.robot.BaseView;

public class RectView extends BaseView {
    public RectView(Context context) {
        super(context);
        init();
    }

    private void init(){

    }

    int centerX, centerY;
    int rw, rh;
    boolean isInited = false;

    @Override
    protected void drawShape(Canvas canvas, int w, int h, Paint paint) {
//        Rect rect = new Rect(100, 100, 200, 200);
//        RectF rectF = new RectF(100, 100, 200, 200);

        if(!isInited){
            centerX = w/2;
            centerY = h/2;
            rw = w - 200;
            rh = h - 200;
            isInited = true;
        }
       drawRect(canvas, centerX, centerY, rw, rh, paint);

        /*
        Rect的四个顶点是int
        RectF的四个顶点float
        RectF和rx（x-radius）,ry（y-radius）构成了圆角Rect
        rx    The x-radius of the oval used to round the corners
        ry    The y-radius of the oval used to round the corners

        Rect和RectF包含的方法：
        inset
        union
        是否包含点或矩形
         */
    }

    public static void drawRect(Canvas canvas, int centerX, int centerY, int w, int h, Paint p){
        int left = centerX - w/2;
        int top = centerY - h/2;
        int right = left + w;
        int bottom = top + h;
        canvas.drawRect(new RectF(left, top, right, bottom), p);
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

    @Override
    public String getTitle() {
        return "canvas.drawRect(rectF, paint)";
    }

    @Override
    public String getMethod() {
        return "画矩形";
    }

    @Override
    public String getComment() {
        return "画个矩形\n" +
                "Rect处理int\n" +
                "RectF处理float\n" +
                "二者都有inset，union，contains点或矩形的方法";
    }

}