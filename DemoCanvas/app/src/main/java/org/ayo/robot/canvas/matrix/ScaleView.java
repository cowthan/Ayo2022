package org.ayo.robot.canvas.matrix;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import org.ayo.lang.Bitmaps;
import org.ayo.robot.BaseView;
import org.ayo.robot.R;

/**
 * Created by Administrator on 2016/12/19.
 */

public class ScaleView extends BaseView {

    Bitmap bitmap;

    public ScaleView(Context context) {
        super(context);
        init();
    }

    private void init(){
        bitmap = Bitmaps.getBitmapByResource(getContext(), R.drawable.test5, 500, 500);
    }


    @Override
    protected void drawShape(Canvas canvas, int w, int h, Paint mPaint) {

        getObservable().notifyDataChanged(this, "圆心(" + centerX + ", " + centerY + ") 当前scale(" + sx + ", " + sy + ")");
        int rectW = 150;
        int rectH = 150;
        int left = w/2;
        int top = h/2;
        if(!isInited){
            centerX = w/2;
            centerY = h/2;
            isInited = true;
        }
        /*
         * 绘制一个红色矩形
         */
        mPaint.setColor(Color.RED);
        canvas.drawRect(left, top, top + rectW, top + rectH, mPaint);
        canvas.drawBitmap(bitmap, left, top, null);
        canvas.drawCircle(centerX, centerY, 10, mPaint);

        /*
         * 保存画布并绘制一个黑色的矩形：下面绘制的东西会跟着画布缩放
         */
        canvas.save();
        mPaint.setColor(Color.BLACK);
        canvas.scale(sx, sy, centerX, centerY);
        canvas.drawRect(left, top, top + rectW, top + rectH, mPaint);
        canvas.drawBitmap(bitmap, left, top, null);
        canvas.drawRect(10, 10, rectW, rectH, mPaint);
        canvas.restore();


    }

    @Override
    public String getTitle() {
        return "canvas.scale(sx, sy, centerX, centerY)";
    }

    @Override
    public String getMethod() {
        return "对画布进行缩放";
    }

    @Override
    public String getComment() {
        return "centerX和Y对缩放的影响还是很大的\n" +
                "缩放可以模拟出倒影效果\n" +
                "以上边界为轴旋转，缩放(1, -1)，则上边界作为镜子了";
    }

    float sx = 1;
    float sy = 1;
    int centerX = 20;
    int centerY = 20;
    private boolean isInited = false;

    protected void changeScale(int dx, int dy) {
        sx += dx/50f;
        sy += dy/50f;
        invalidate();
    }

    public void changeCenter(int dx, int dy){
        centerX += dx;
        centerY += dy;
        invalidate();
    }


}
