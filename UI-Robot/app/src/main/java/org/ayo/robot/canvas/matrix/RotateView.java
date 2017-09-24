package org.ayo.robot.canvas.matrix;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import org.ayo.robot.BaseView;

/**
 * Created by Administrator on 2016/12/19.
 */

public class RotateView extends BaseView {
    public RotateView(Context context) {
        super(context);
    }


    @Override
    protected void drawShape(Canvas canvas, int w, int h, Paint mPaint) {

        getObservable().notifyDataChanged(this, "圆心(" + centerX + ", " + centerY + ") 当前rotate角度：" + rotate);
        int rectW = 150;
        int rectH = 150;
        int left = w/2;
        int top = h/2;
        /*
         * 绘制一个红色矩形
         */
        mPaint.setColor(Color.RED);
        canvas.drawRect(left, top, top + rectW, top + rectH, mPaint);
        canvas.drawCircle(centerX, centerY, 10, mPaint);

        /*
         * 保存画布并绘制一个黑色的矩形
         */
        canvas.save();
        mPaint.setColor(Color.BLACK);
        canvas.rotate(rotate, centerX, centerY);
        canvas.drawRect(left, top, top + rectW, top + rectH, mPaint);
        canvas.drawRect(10, 10, rectW, rectH, mPaint);
        canvas.restore();
    }

    @Override
    public String getTitle() {
        return "canvas.rotate(angle)";
    }

    @Override
    public String getMethod() {
        return "转的是画布，然后所有画布上的东西就跟着转了";
    }

    @Override
    public String getComment() {
        return "canvas.rotate(degree)默认以画布左上角为圆心旋转\n" +
                "rotate(degredd, px, py)指定旋转的圆心\n" +
                "注意，是画布本身旋转，正数是顺时针，负数是逆时针\n" +
                "画布转了几度，在其上的内容就转了几度\n" +
                "剪切区域会受matrix影响";
    }

    int rotate = 0;
    int centerX = 20;
    int centerY = 20;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        rotate = 0;
        centerX = w/2;
        centerY = h/2;
    }

    protected void changeRotateAngle(int dx) {
        rotate += dx/3f;
        invalidate();
    }

    public void changeCenter(int dx, int dy){
        centerX += dx;
        centerY += dy;
        invalidate();
    }


}
